/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricstationclient;

import be.vives.oop.mqtt.chatservice.MqttBiometricStationService;
import be.vives.oop.mqtt.chatservice.IMqttMessageHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author jopbo_000
 */
public class FXMLBioMetricStationClientController implements Initializable, IMqttMessageHandler {
    
    @FXML
    private Label label;
    MqttBiometricStationService biometricStationService;
    BioMetricStationStringParser parser = new BioMetricStationStringParser();
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
        //biometricStationService.sendMqttData("[" + "ABCDE" + "|"  + "|" +  "|" + "EDCBA" + "]");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        biometricStationService = new MqttBiometricStationService("jop", "test");
        biometricStationService.setMessageHandler(this);
        disconnectClientOnClose();
    }  
    
    
    
    @Override
    public void messageArrived(String channel, String message) {
       System.out.println(message);
       SensorData data = parser.parse(message);
       System.out.println(data);

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
