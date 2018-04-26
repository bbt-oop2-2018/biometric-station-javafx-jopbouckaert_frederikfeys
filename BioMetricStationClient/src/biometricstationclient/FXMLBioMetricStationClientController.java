/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricstationclient;

import biometricstationservice.MqttBiometricStationService;
import biometricstationservice.IMqttMessageHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
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
    @FXML private Label label;
    @FXML private LineChart temperatureChart;
    @FXML private LineChart heartBeatChart;
    private XYChart.Series temperatureValues[];
    private XYChart.Series heartbeatValues[];
    private final int NUMBER_OF_TEMPERATURE_SERIES = 1;
    private final int NUMBER_OF_HEARTBEAT_SERIES = 1;
    private int xValue = 0;
    private final int WINDOW = 100;
    private double temperature =0.0;
    private double heartbeat=0.0;
    MqttBiometricStationService biometricStationService;
    BioMetricStationStringParser parser = new BioMetricStationStringParser();
    
    @FXML
    private void handleButtonRefreshData(ActionEvent event) {

        temperatureValues[0].getData().add(new XYChart.Data(xValue,temperature));
        heartbeatValues[0].getData().add(new XYChart.Data(xValue,heartbeat)); 
        xValue++;
        if (xValue == WINDOW){
            temperatureValues[0].getData().clear();
            heartbeatValues[0].getData().clear();
            xValue = 0;
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        biometricStationService = new MqttBiometricStationService("jop", "test");
        biometricStationService.setMessageHandler(this);
        disconnectClientOnClose();
        
        temperatureValues = new XYChart.Series[NUMBER_OF_TEMPERATURE_SERIES];
        heartbeatValues = new XYChart.Series[NUMBER_OF_HEARTBEAT_SERIES];
        
        temperatureValues[0] = createXYChartTemperature("Random temperature");
        heartbeatValues[0] = createXYChartHeartbeat("Random heartbeat");
        
        
        temperatureChart.getYAxis().setLabel("Temperature [celcius]");
        temperatureChart.getXAxis().setLabel("Measurement");
        
        heartBeatChart.getYAxis().setLabel("Heartbeat [BPM]");
        heartBeatChart.getXAxis().setLabel("Measurement");
    } 
    
    
    
    
    @Override
    public void messageArrived(String channel, String message) { 
       SensorData data = parser.parse(message);
       System.out.println(data);
       temperature = data.getTemperature();
       heartbeat = data.getHeartBeat();
 
       
       
    }
    
    
    
        private XYChart.Series createXYChartTemperature(String name){
        
        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        temperatureChart.getData().add(series);
        return series;
        
    }
    
    
    private XYChart.Series createXYChartHeartbeat(String name){
        
        XYChart.Series series = new XYChart.Series();
        series.setName(name);
        heartBeatChart.getData().add(series);
        return series;
        
    }
    
    
    
    
    
    
    private void disconnectClientOnClose() {
        // Source: https://stackoverflow.com/a/30910015
        label.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                // scene is set for the first time. Now its the time to listen stage changes.
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        // stage is set. now is the right time to do whatever we need to the stage in the controller.
                        ((Stage) newWindow).setOnCloseRequest((event) -> {
                            biometricStationService.disconnect();
                        });
                    }
                });
            }
        });
    }
    
}
