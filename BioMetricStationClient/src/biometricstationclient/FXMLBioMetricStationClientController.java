/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricstationclient;

import biometricstationservice.MqttBiometricStationService;
import biometricstationservice.IMqttMessageHandler;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author jopbo_000
 */
public class FXMLBioMetricStationClientController implements Initializable, IMqttMessageHandler {

    @FXML
    private Button refreshData;
    @FXML
    private LineChart temperatureChart;
    @FXML
    private LineChart heartBeatChart;
    @FXML
    private LineChart acceleroChart;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label heartbeatLabel;
    @FXML
    private Label xAcceleroLabel;
    @FXML
    private Label yAcceleroLabel;
    @FXML
    private Label zAcceleroLabel;

    private Gson gson = new Gson();

    private XYChart.Series temperatureValues[];
    private XYChart.Series heartbeatValues[];
    private XYChart.Series acceleroValues[];

    private final int NUMBER_OF_TEMPERATURE_SERIES = 1;
    private final int NUMBER_OF_HEARTBEAT_SERIES = 1;
    private final int NUMBER_OF_ACCELERO_SERIES = 3;

    private int xValue = 0;
    private final int WINDOW = 50;

    private double temperature = 0.0;
    private double heartbeat = 0.0;
    private double xAccelero = 0.0;
    private double yAccelero = 0.0;
    private double zAccelero = 0.0;

    MqttBiometricStationService biometricStationServiceTemperature;
    MqttBiometricStationService biometricStationServiceHeartbeat;
    MqttBiometricStationService biometricStationServiceAccelero;
    BioMetricStationStringParser parser = new BioMetricStationStringParser();

    ObservableList value;

    @FXML
    private void handleButtonRefreshData(ActionEvent event) {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        temperatureValues = new XYChart.Series[NUMBER_OF_TEMPERATURE_SERIES];
        heartbeatValues = new XYChart.Series[NUMBER_OF_HEARTBEAT_SERIES];
        acceleroValues = new XYChart.Series[NUMBER_OF_ACCELERO_SERIES];

        temperatureValues[0] = createXYChartTemperature("Temperature");
        heartbeatValues[0] = createXYChartHeartbeat("Heartbeat");
        acceleroValues[0] = createXYAccelero("X accelero");
        acceleroValues[1] = createXYAccelero("Y accelero");
        acceleroValues[2] = createXYAccelero("Z accelero");

        temperatureChart.getYAxis().setLabel("Temperature [celcius]");
        temperatureChart.getXAxis().setLabel("Measurement");

        heartBeatChart.getYAxis().setLabel("Heartbeat [BPM]");
        heartBeatChart.getXAxis().setLabel("Measurement");

        acceleroChart.getYAxis().setLabel("Movement");
        acceleroChart.getXAxis().setLabel("Measurement");

        biometricStationServiceTemperature = new MqttBiometricStationService("jop", "temperature");
        biometricStationServiceTemperature.setMessageHandler(this);

        biometricStationServiceHeartbeat = new MqttBiometricStationService("jop", "heartbeat");
        biometricStationServiceHeartbeat.setMessageHandler(this);

        biometricStationServiceAccelero = new MqttBiometricStationService("jop", "accelero");
        biometricStationServiceAccelero.setMessageHandler(this);

        disconnectClientOnClose();

    }

    @Override
    public void messageArrived(String channel, String message) {
        try {
            if (channel.equals("temperature")) {
                SensorDataTemperature dataFromJsonTemperature = gson.fromJson(message, SensorDataTemperature.class);
                temperature = dataFromJsonTemperature.getTemperature();
                setNewTemperature();
            } else if (channel.equals("heartbeat")) {
                SensorDataHeartbeat dataFromJsonHeartbeat = gson.fromJson(message, SensorDataHeartbeat.class);
                heartbeat = dataFromJsonHeartbeat.getHeartbeat();
                setNewHeartbeat();
            } else if (channel.equals("accelero")) {
                SensorDataAccelero dataFromJsonAccelero = gson.fromJson(message, SensorDataAccelero.class);
                xAccelero = dataFromJsonAccelero.getXAccelero();
                yAccelero = dataFromJsonAccelero.getYAccelero();
                zAccelero = dataFromJsonAccelero.getZAccelero();
                setNewAccelero();
            }

            setNewData();
        } catch (JsonSyntaxException mje) {

        }

    }

    private XYChart.Series createXYChartTemperature(String name) {

        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        temperatureChart.getData().add(series);
        return series;

    }

    private XYChart.Series createXYChartHeartbeat(String name) {

        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        heartBeatChart.getData().add(series);
        return series;

    }

    private XYChart.Series createXYAccelero(String name) {

        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        acceleroChart.getData().add(series);
        return series;

    }

    private void disconnectClientOnClose() {
        // Source: https://stackoverflow.com/a/30910015
        refreshData.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                // scene is set for the first time. Now its the time to listen stage changes.
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        // stage is set. now is the right time to do whatever we need to the stage in the controller.
                        ((Stage) newWindow).setOnCloseRequest((event) -> {
                            biometricStationServiceTemperature.disconnect();
                            biometricStationServiceHeartbeat.disconnect();
                            biometricStationServiceAccelero.disconnect();
                        });
                    }
                });
            }
        });
    }

    private void setNewData() {
        xValue++;
        if (xValue == WINDOW) {
            temperatureValues[0].getData().clear();
            heartbeatValues[0].getData().clear();
            acceleroValues[0].getData().clear();
            acceleroValues[1].getData().clear();
            acceleroValues[2].getData().clear();
            xValue = 0;

        }

    }

    private void setNewTemperature() {

        temperatureValues[0].getData().add(new XYChart.Data(xValue, temperature));
        String temperatureString = Double.toString(temperature);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                temperatureLabel.setText(temperatureString + " °C");
            }
        });

    }

    private void setNewHeartbeat() {

        heartbeatValues[0].getData().add(new XYChart.Data(xValue, heartbeat));
        String heartbeatString = Double.toString(heartbeat);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                heartbeatLabel.setText(heartbeatString + " BPM");
            }
        });

    }

    private void setNewAccelero() {

        acceleroValues[0].getData().add(new XYChart.Data(xValue, xAccelero));
        acceleroValues[1].getData().add(new XYChart.Data(xValue, yAccelero));
        acceleroValues[2].getData().add(new XYChart.Data(xValue, zAccelero));
        String xAcceleroString = Double.toString(xAccelero);
        String yAcceleroString = Double.toString(yAccelero);
        String zAcceleroString = Double.toString(zAccelero);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                xAcceleroLabel.setText(xAcceleroString);
                yAcceleroLabel.setText(yAcceleroString);
                zAcceleroLabel.setText(zAcceleroString);
            }
        });
    }

}
