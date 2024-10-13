package Javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

enum searchType{
    ByName,ByCountry,ByPosition,BySalaryRange
}

public class InputBox {

    private final searchType type ;
    private final Stage window;

    private String info;
    private double[] range;

    private String label;

    public Stage getWindow() {
        return window;
    }

    public InputBox(searchType type) throws Exception {
        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("InputBox");
        window.setResizable(false);
        window.initStyle(StageStyle.UNDECORATED);

        this.type = type;

        display();

    }

    public void display() throws Exception{
        switch (type){
            case ByName:
                label = "Player's Name:";
                display_InputBox1();
                break;
            case ByCountry:
                label = "Player's Country:";
                display_InputBox1();
                break;
            case ByPosition:
                label = "Player's Position:";
                display_InputBox1();
                break;
            case BySalaryRange:
                display_InputBox2();
                break;
        }

    }


    private void display_InputBox1() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("InputBox1.fxml"));
        Parent root = loader.load();

        // Loading the controller
        InputBoxController controller = loader.getController();
        controller.getlabel().setText(label);
        controller.setInputBox(this);

        window.setScene(new Scene(root, 300, 200));
        window.showAndWait();
    }

    private void display_InputBox2() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("InputBox2.fxml"));
        Parent root = loader.load();

        // Loading the controller
        InputBoxController controller = loader.getController();
        controller.setInputBox(this);

        window.setScene(new Scene(root, 300, 200));
        window.showAndWait();
    }

    public void setRange(double range1,double range2) {
        range = new double[2];
        range[0] = range1;
        range[1] = range2;
    }

    public double[] getRange() {
        return range;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public searchType getType(){
        return type;
    }


}
