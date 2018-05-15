/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biometricstationclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jopbo_000
 */
public class FXMLBiometricStationLoginController implements Initializable {

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    
    @FXML private Button logInButton;
    @FXML private Button registerButton;
    private static Stage closingStage = new Stage();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }


    @FXML
    private void handleLogInButton(ActionEvent event) throws Exception{
        
       Parent root = FXMLLoader.load(getClass().getResource("FXMLBioMetricStationClient.fxml"));
       Scene scene = new Scene(root);
       Stage stage = new Stage();
       stage.setScene(scene);
       BioMetricStationClient.closeWindow();
       stage.show();
    }
    
    @FXML
    private void handleRegisterButton(ActionEvent event) throws Exception {
        
       Parent root = FXMLLoader.load(getClass().getResource("FXMLBiometricStationRegister.fxml"));
       Scene scene = new Scene(root);
       Stage stage = new Stage();
       stage.setScene(scene);
       BioMetricStationClient.closeWindow();
       setClosingStage(stage);
       stage.show();
    }
    
    public static void closeWindow(){
        closingStage.close();
    }
    
    private void setClosingStage(Stage closingStage) {
        FXMLBiometricStationLoginController.closingStage = closingStage;
    }
    
}
