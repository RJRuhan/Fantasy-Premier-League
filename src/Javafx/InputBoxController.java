package Javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class InputBoxController {

    @FXML
    private TextField Min_Salary_textField;
    @FXML
    private TextField Max_Salary_textField;
    @FXML
    private TextField inputTextField;
    @FXML
    private Label label;

    private InputBox inputBox;

    public Label getlabel (){
        return label;
    }

    public void EnterButtonPressed(ActionEvent actionEvent) {
        if( inputBox.getType() == searchType.BySalaryRange ){
            double range1,range2;
            try{
                range1 = Double.parseDouble(Min_Salary_textField.getText());
                range2 = Double.parseDouble(Max_Salary_textField.getText());
            }catch(NumberFormatException e){
                alertBox("Input can only be a number.");
                return;
            }

            if( range1 > range2 ) {
                alertBox("Min. Salary can't be greater than max. Salary.");
                return;
            }

            if( range1 < 0 | range2 < 0 ){
                alertBox("Input can't be negative");
                return;
            }

            inputBox.setRange(range1,range2);
        }
        else{
            inputBox.setInfo(inputTextField.getText());
        }

        inputBox.getWindow().close();
    }

    public void setInputBox(InputBox inputBox) {
        this.inputBox = inputBox;
    }

    public void alertBox(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("INVALID INPUT");
        alert.setHeaderText("Please Enter valid Range");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void CancelButtonClicked(ActionEvent actionEvent) {
        inputBox.getWindow().close();
    }
}
