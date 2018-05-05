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
import javafx.scene.chart.NumberAxis;
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
    @FXML
    private Label dateTimeTemperatureLabel;
    @FXML
    private Label dateTimeHeartbeatLabel;
    @FXML
    private Label dateTimeAcceleroLabel;
    @FXML
    private NumberAxis xAxisTemperature;
    @FXML
    private NumberAxis xAxisHeartbeat;
    @FXML
    private NumberAxis xAxisAccelero;

    private Gson gson = new Gson();

    private XYChart.Series temperatureValues[];
    private XYChart.Series heartbeatValues[];
    private XYChart.Series acceleroValues[];

    private final int NUMBER_OF_TEMPERATURE_SERIES = 1;
    private final int NUMBER_OF_HEARTBEAT_SERIES = 1;
    private final int NUMBER_OF_ACCELERO_SERIES = 3;
    private final int SIZE_OF_X_AXIS = 14;
    private final int NUMBER_OF_X_VALUE = 15;
    private final int AXIS_TICK=1;
    private final int AXIS_START=0;

    private double xValueTemperature = 0;
    private double xValueHeartbeat = 0;
    private double xValueAccelero = 0;

    private final int WINDOW = 50;

    private double temperature = 0.0;
    private double heartbeat = 0.0;
    private double xAccelero = 0.0;
    private double yAccelero = 0.0;
    private double zAccelero = 0.0;
    
    private String dateTimeTemperature;
    private String dateTimeHeartbeat;
    private String dateTimeAccelero;

    private double multipleCalculatorTemperature = 1.0;
    private double multipleCalculatorHeartbeat = 1.0;
    private double multipleCalculatorAccelero = 1.0;

    MqttBiometricStationService biometricStationServiceTemperature;
    MqttBiometricStationService biometricStationServiceHeartbeat;
    MqttBiometricStationService biometricStationServiceAccelero;
    //BioMetricStationStringParser parser = new BioMetricStationStringParser();


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
        temperatureChart.getXAxis().setAutoRanging(false);
        xAxisTemperature.setLowerBound(AXIS_START);
        xAxisTemperature.setUpperBound(SIZE_OF_X_AXIS);
        xAxisTemperature.setTickUnit(AXIS_TICK);

        heartBeatChart.getYAxis().setLabel("Heartbeat [BPM]");
        heartBeatChart.getXAxis().setLabel("Measurement");
        heartBeatChart.getXAxis().setAutoRanging(false);
        xAxisHeartbeat.setLowerBound(AXIS_START);
        xAxisHeartbeat.setUpperBound(SIZE_OF_X_AXIS);
        xAxisHeartbeat.setTickUnit(AXIS_TICK);

        acceleroChart.getYAxis().setLabel("Movement");
        acceleroChart.getXAxis().setLabel("Measurement");
        acceleroChart.getXAxis().setAutoRanging(false);
        xAxisAccelero.setLowerBound(AXIS_START);
        xAxisAccelero.setUpperBound(SIZE_OF_X_AXIS);
        xAxisAccelero.setTickUnit(AXIS_TICK);

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
                dateTimeTemperature = dataFromJsonTemperature.getDateTime();
                setNewTemperature();
            } else if (channel.equals("heartbeat")) {
                SensorDataHeartbeat dataFromJsonHeartbeat = gson.fromJson(message, SensorDataHeartbeat.class);
                heartbeat = dataFromJsonHeartbeat.getHeartbeat();
                dateTimeHeartbeat = dataFromJsonHeartbeat.getDateTime();
                setNewHeartbeat();
            } else if (channel.equals("accelero")) {
                SensorDataAccelero dataFromJsonAccelero = gson.fromJson(message, SensorDataAccelero.class);
                xAccelero = dataFromJsonAccelero.getXAccelero();
                yAccelero = dataFromJsonAccelero.getYAccelero();
                zAccelero = dataFromJsonAccelero.getZAccelero();
                dateTimeAccelero = dataFromJsonAccelero.getDateTime();
                setNewAccelero();
            }

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
        temperatureLabel.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
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

//    private void setNewX() {
//        xValue++;
//        if (xValue == WINDOW) {
//            temperatureValues[0].getData().clear();
//            heartbeatValues[0].getData().clear();
//            acceleroValues[0].getData().clear();
//            acceleroValues[1].getData().clear();
//            acceleroValues[2].getData().clear();
//            xValue = 0;
//
//        }
//
//    }
    private void setNewTemperature() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                temperatureValues[0].getData().add(new XYChart.Data(xValueTemperature, temperature));
                String temperatureString = Double.toString(temperature);
                temperatureLabel.setText(temperatureString + " °C");
                dateTimeTemperatureLabel.setText(dateTimeTemperature);

                if (xValueTemperature / NUMBER_OF_X_VALUE == multipleCalculatorTemperature) {
                    xAxisTemperature.setLowerBound(xValueTemperature);
                    xAxisTemperature.setUpperBound(xValueTemperature + SIZE_OF_X_AXIS);
                    multipleCalculatorTemperature++;
                }
                xValueTemperature++;
            }
        });

    }

    private void setNewHeartbeat() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                heartbeatValues[0].getData().add(new XYChart.Data(xValueHeartbeat, heartbeat));
                String heartbeatString = Double.toString(heartbeat);
                heartbeatLabel.setText(heartbeatString + " BPM");
                dateTimeHeartbeatLabel.setText(dateTimeHeartbeat);
                if (xValueHeartbeat / NUMBER_OF_X_VALUE == multipleCalculatorHeartbeat) {
                    xAxisHeartbeat.setLowerBound(xValueHeartbeat);
                    xAxisHeartbeat.setUpperBound(xValueHeartbeat + SIZE_OF_X_AXIS);
                    multipleCalculatorHeartbeat++;
                }
                xValueHeartbeat++;
            }
        });

    }

    private void setNewAccelero() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                acceleroValues[0].getData().add(new XYChart.Data(xValueAccelero, xAccelero));
                acceleroValues[1].getData().add(new XYChart.Data(xValueAccelero, yAccelero));
                acceleroValues[2].getData().add(new XYChart.Data(xValueAccelero, zAccelero));
                String xAcceleroString = Double.toString(xAccelero);
                String yAcceleroString = Double.toString(yAccelero);
                String zAcceleroString = Double.toString(zAccelero);
                xAcceleroLabel.setText(xAcceleroString);
                yAcceleroLabel.setText(yAcceleroString);
                zAcceleroLabel.setText(zAcceleroString);
                dateTimeAcceleroLabel.setText(dateTimeAccelero);
                if (xValueAccelero / NUMBER_OF_X_VALUE == multipleCalculatorAccelero) {
                    xAxisAccelero.setLowerBound(xValueAccelero);
                    xAxisAccelero.setUpperBound(xValueAccelero + SIZE_OF_X_AXIS);
                    multipleCalculatorAccelero++;
                }
                xValueAccelero++;
            }
        });
    }

}
