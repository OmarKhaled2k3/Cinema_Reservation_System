module com.example.cinema_reservation_system {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cinema_reservation_system to javafx.fxml;
    exports com.example.cinema_reservation_system;
}