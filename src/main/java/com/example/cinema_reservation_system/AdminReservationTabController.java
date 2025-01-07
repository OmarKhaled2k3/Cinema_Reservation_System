package com.example.cinema_reservation_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class AdminReservationTabController implements Initializable  {
    Dictionary<String, ArrayList<ShowTime>> selectedShowtimebyMovie= new Hashtable<>();
    @FXML
    private Button btn_back;

    @FXML
    private Button btn_createnew;

    @FXML
    private Button btn_edit;

    @FXML
    private Label movie_label;

    @FXML
    private ComboBox<String> movie_dropdown;

    @FXML
    private ListView<String> showtime_list;

    private ObservableList<String> movies = FXCollections.observableArrayList();
    private ObservableList<String> showtimes = FXCollections.observableArrayList();
    private Movie selectedmovie;
    private Database db = Database.getInstance(); // Get Database instance

    @Override
    public void initialize(java.net.URL location, ResourceBundle resources) {
        movies.addAll(getMovieTitles());
        movie_dropdown.setItems(movies);

        movie_dropdown.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedmovie = getMovieByTitle(newValue);
                loadShowtimes(selectedmovie.getId(),selectedmovie.getTitle());
                showtime_list.setItems(showtimes);
            }
        });
    }

    private ArrayList<String> getMovieTitles() {
        ArrayList<String> titles = new ArrayList<>();
        try {
            String query = "SELECT title FROM movies";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                titles.add(rs.getString("title"));
            }
            rs.close();
        } catch (SQLException e) {
            // Handle the exception gracefully (e.g., display an error message, log the error)
            e.printStackTrace();
        }
        return titles;
    }

    private Movie getMovieByTitle(String title) {
        try {
            String query = "SELECT * FROM movies WHERE title = ?";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("duration"),
                        rs.getString("description"),
                        rs.getString("imageurl")
                );
            }
        } catch (SQLException e) {
            // Handle the exception gracefully (e.g., display an error message, log the error)
            e.printStackTrace();
        }
        return null;
    }

    private void loadShowtimes(int movieId,String movieTitle) {
        showtimes.clear();
        try {
            String query = "SELECT time, id,seats FROM showtime WHERE MovieID = ?";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            String time="";
            int ShowTimeId=1;
            Movie movie = new Movie(movieId,movieTitle);
            ArrayList<ShowTime>showTimes = new ArrayList<>();
            while (rs.next()) {
                time=rs.getString("time");
                String showTimeSeats=rs.getString("seats").trim();
                ShowTimeId = rs.getInt("id");
                showtimes.add(time);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
                ShowTime showTime = new ShowTime(ShowTimeId,movie,dateTime);
                ArrayList<Seat> showTimeSeatsList = Seat.SeatsConversion(showTimeSeats);/// Seats# 1,4,6 Normal Total Price=10 \n Seats# 50,55 VIP Seats TP = 200
                showTime.setSeatList(showTimeSeatsList);
                showTimes.add(showTime);
            }
            selectedShowtimebyMovie.put(movieTitle,showTimes);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
       }

    @FXML
    private void goBack() throws Exception{
        SceneController.launchScene("Admin_View.fxml");
    }

    @FXML
    private void gotomanage() throws Exception{
        int index = showtime_list.getSelectionModel().getSelectedIndex();
        Reservation reservation = Reservation.getInstance();
        if(index!=-1){
            ShowTime selectedShowtime= (selectedShowtimebyMovie.get(selectedmovie.getTitle())).get(index);
            reservation.setShowtime(selectedShowtime);

        }
        SceneController.launchScene("Admin_ManageReservationTab.fxml");
    }
}