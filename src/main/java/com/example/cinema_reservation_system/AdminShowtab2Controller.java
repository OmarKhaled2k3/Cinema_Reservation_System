package com.example.cinema_reservation_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminShowtab2Controller implements Initializable {

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_remove;

    @FXML
    private DatePicker datepicker;

    @FXML
    private ListView<String> list_view;

    @FXML
    private Label movie_label;

    @FXML
    private TextField time_field;

    private static int movieId;
    private static String movieTitle;
    private ObservableList<String> showtimes = FXCollections.observableArrayList();
    private static final Logger logger = Logger.getLogger(AdminShowtab2Controller.class.getName());

    public static void setMovieDetails(int id, String title) {
        movieId = id;
        movieTitle = title;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Movie selectedMovie = AdminShowtab1Controller.getSelectedMovie();

        if (selectedMovie != null) {
            movie_label.setText(selectedMovie.getTitle());
            loadShowtimes(selectedMovie.getId());
        } else {
            showAlert("No movie selected. Returning to previous screen.", Alert.AlertType.ERROR);
            try {
                goBack();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to navigate back", e);
            }
        }

        list_view.setItems(showtimes);
    }

    private void loadShowtimes(int movieId) {
        try {
            Database db = Database.getInstance();
            if (db == null) {
                showAlert("Database connection error.", Alert.AlertType.ERROR);
                return;
            }

            String query = "SELECT time FROM showtime WHERE MovieID = ?";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                showtimes.add(rs.getString("time"));
            }
            rs.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error loading showtimes", e);
        }
    }

    @FXML
    void addShowtime(ActionEvent event) {
        LocalDate date = datepicker.getValue();
        String time = time_field.getText();

        if (date != null && !time.isEmpty()) {
            try {
                if (date.isBefore(LocalDate.now())) {
                    showAlert("Cannot add showtime for a past date.", Alert.AlertType.ERROR);
                    return;
                }

                LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
                String datetime = date + " " + parsedTime;


                if (showtimes.contains(datetime)) {
                    showAlert("Showtime already exists.", Alert.AlertType.ERROR);
                    return;
                }

                saveToDatabase(datetime);
                showtimes.add(datetime);
                time_field.clear();
                datepicker.setValue(null);
                showAlert("Showtime added successfully.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Invalid time format. Use HH:mm:ss.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Please enter both date and time.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void removeShowtime(ActionEvent event) {
        String selectedItem = list_view.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            deleteShowtimeFromDB(selectedItem);
            showtimes.remove(selectedItem);
            showAlert("Showtime removed successfully.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Select a showtime to remove.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void Savebtn(ActionEvent event) {
        LocalDate date = datepicker.getValue();
        String time = time_field.getText();


        LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
        String datetime = date + " " + parsedTime;
        saveToDatabase(datetime);
       showAlert("saved successfully.", Alert.AlertType.INFORMATION);
    }
    private void saveToDatabase(String datetime) {
        try {
            Database db = Database.getInstance();
            if (db == null) {
                showAlert("Database connection error.", Alert.AlertType.ERROR);
                return;
            }

            String query = "INSERT INTO showtime (time, MovieID) VALUES (?, ?)";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setString(1, datetime);
            stmt.setInt(2, movieId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saving showtime", e);
        }
    }

    private void deleteShowtimeFromDB(String datetime) {
        try {
            Database db = Database.getInstance();
            if (db == null) {
                showAlert("Database connection error.", Alert.AlertType.ERROR);
                return;
            }

            String query = "DELETE FROM showtime WHERE time = ? AND MovieID = ?";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setString(1, datetime);
            stmt.setInt(2, movieId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting showtime", e);
        }
    }

    @FXML
    private void goBack() throws IOException {
        SceneController.launchScene("Admin_Showtimetab1.fxml");
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }
}
