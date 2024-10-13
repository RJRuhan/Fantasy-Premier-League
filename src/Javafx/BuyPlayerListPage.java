package Javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class BuyPlayerListPage {

    private final Stage window;

    HomePage homePage;

    public BuyPlayerListPage(HomePage homePage) throws Exception {


        this.homePage = homePage;

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("BuyPlayerList");
        window.setResizable(false);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("BuyPlayerList.fxml"));
        Parent root = loader.load();

        // Loading the controller
        BuyPlayerListController controller = loader.getController();
        controller.setBuyPlayerListPage(this);
        controller.init();

        window.setScene(new Scene(root, 720, 550));
        window.showAndWait();

    }

    public Stage getWindow() {
        return window;
    }
}
