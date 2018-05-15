package biometricstationclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jopbo_000
 */
public class FXMLBiometricStationRegisterController implements Initializable{

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }  
    
        @FXML
    private void handleRegisterDoneButton(ActionEvent event) throws Exception{
       
       
       Parent root = FXMLLoader.load(getClass().getResource("FXMLBioMetricStationClient.fxml"));
       Scene scene = new Scene(root);
       Stage stage = new Stage();
       stage.setScene(scene);
       FXMLBiometricStationLoginController.closeWindow();
       stage.show();
       
    }
    
}
