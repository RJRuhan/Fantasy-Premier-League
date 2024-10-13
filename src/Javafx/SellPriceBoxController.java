package Javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SellPriceBoxController {

    double Price;
    private Stage window;
    boolean CancelButtonClicked = false;

    @FXML private TextField PriceTextField;

    public void SubmitButtonClicked(ActionEvent actionEvent) {
        try{
            Price = Double.parseDouble(PriceTextField.getText());

            if( Price < 0 )
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("INVALID INPUT");
                alert.setHeaderText("Please Enter valid Price");
                alert.setContentText("Price can't be negative");
                alert.showAndWait();
                return;
            }

            window.close();

        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("INVALID INPUT");
            alert.setHeaderText("Please Enter valid Price");
            alert.setContentText("Input can only be a decimal number");
            alert.showAndWait();
        }

    }

    public void setWindow(Stage window) {
        this.window = window;
    }

    public void CancelSellButtonClicked(ActionEvent actionEvent) {
        CancelButtonClicked = true;
        window.close();
    }

    public void init(){
        PriceTextField.textProperty().addListener( (observable,oldValue,newValue)->{
            try{
                Double.parseDouble(newValue);
                PriceTextField.setStyle("-fx-text-fill: black");
            }
            catch (Exception e){
                    PriceTextField.setStyle("-fx-text-fill: #ff0000");
            }
        });
    }
}
