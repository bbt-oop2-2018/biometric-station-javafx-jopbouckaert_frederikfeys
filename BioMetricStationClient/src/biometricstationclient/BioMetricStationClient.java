package biometricstationclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author jopbo_000
 */
public class BioMetricStationClient extends Application {
    private static Stage closingStage = new Stage();
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLBiometricStationLogin.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        setClosingStage(stage);
        stage.show();
        
    }
    
    public static void closeWindow(){
        closingStage.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }


    private void setClosingStage(Stage closingStage) {
        BioMetricStationClient.closingStage = closingStage;
    }
    
}
