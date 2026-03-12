module com.example.escriturarapida {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.escriturarapida to javafx.fxml;
    exports com.example.escriturarapida;
    exports com.example.escriturarapida.controller;
    opens com.example.escriturarapida.controller to javafx.fxml;
    exports com.example.escriturarapida.utilities;
    opens com.example.escriturarapida.utilities to javafx.fxml;
}