module com.example.mp3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.mp3 to javafx.fxml;
    exports com.example.mp3;
}