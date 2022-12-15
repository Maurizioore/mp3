module com.example.mp3 {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.mp3 to javafx.fxml;
    exports com.example.mp3;
}