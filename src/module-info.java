module myjfx {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.desktop;
    opens Javafx to javafx.graphics, javafx.fxml;

    opens Database to javafx.base;
}