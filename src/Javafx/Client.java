package Javafx;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import util.NetworkUtil;

import java.io.IOException;

public class Client extends Application {

    private static NetworkUtil networkUtil;


    public static void main(String args[]) {
        launch(args);
    }

    @Override

    public void start(Stage stage){
        String serverAddress = "127.0.0.1";
        int serverPort = 33333;
        try {
            networkUtil = new NetworkUtil(serverAddress, serverPort);
            new HomePage(networkUtil,stage);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server Stopped!");
            alert.setHeaderText("No Connection or the Server has Stopped Working");
            alert.showAndWait();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}


