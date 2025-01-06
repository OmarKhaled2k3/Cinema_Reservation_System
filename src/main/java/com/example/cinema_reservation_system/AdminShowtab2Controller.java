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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AdminShowtab2Controller implements Initializable {

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_remove;

    @FXML
    private Button btn_save;

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
        }
    }

    private void loadShowtimes(int movieId) {
        try {
            Database db = Database.getInstance();
            String query = "SELECT time FROM showtime WHERE MovieID = ?";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list_view.getItems().add(rs.getString("time"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void saveShowtimeToDB(String datetime) {
        try {
            Database db = Database.getInstance();
            String query = "INSERT INTO showtime (time, MovieID) VALUES (?, ?)";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setString(1, datetime);
            stmt.setInt(2, movieId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void addShowtime(ActionEvent event) {
        LocalDate date = datepicker.getValue();
        String time = time_field.getText();

        if (date != null && !time.isEmpty()) {
            try {
                LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
                String datetime = date + " " + time;
                saveShowtimeToDB(datetime);
                showtimes.add(datetime);
                time_field.clear();
                datepicker.setValue(null);
            } catch (Exception e) {
                showAlert("Invalid time format. Use HH:mm.");
            }
        } else {
            showAlert("Please enter date and time.");
        }
    }
    @FXML
    void removeShowtime(ActionEvent event) {
        String selectedItem = list_view.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            deleteShowtimeFromDB(selectedItem);
            showtimes.remove(selectedItem);
        } else {
            showAlert("Select a showtime to remove.");
        }
    }
    private void deleteShowtimeFromDB(String datetime) {
        try {
            Database db = Database.getInstance();
            String query = "DELETE FROM showtime WHERE time = ? AND MovieID = ?";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setString(1, datetime);
            stmt.setInt(2, movieId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack1() throws IOException {
        SceneController.launchScene("Admin_Showtimetab1.fxml");
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
