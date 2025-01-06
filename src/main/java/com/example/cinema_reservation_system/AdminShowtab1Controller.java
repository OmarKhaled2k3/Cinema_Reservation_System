package com.example.cinema_reservation_system;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminShowtab1Controller implements Initializable {

    @FXML
    private ComboBox<Movie> box_movies;

    @FXML
    private Button btn_back;

    @FXML
    private Button btn_continue;

    private static Movie selectedMovie;

    public static void setSelectedMovie(Movie movie) {
        selectedMovie = movie;
    }

    public static Movie getSelectedMovie() {
        return selectedMovie;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        box_movies.setItems(FXCollections.observableArrayList(fetchMovies()));

        btn_continue.setDisable(true);

        box_movies.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btn_continue.setDisable(newValue == null);
        });
    }

    private ArrayList<Movie> fetchMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            Database db = Database.getInstance();
            String query = "SELECT * FROM movies";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("duration"),
                        rs.getString("description"),
                        rs.getString("imageurl")
                ));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }


    @FXML
    private void goBack() throws IOException {
        SceneController.launchScene("Admin_View.fxml");
    }

    @FXML
    private void gotomanage2() throws IOException {
        setSelectedMovie(box_movies.getSelectionModel().getSelectedItem());
        SceneController.launchScene("Admin_Showtimetab2.fxml");
        AdminShowtab2Controller.setMovieDetails(selectedMovie.getId(),selectedMovie.getTitle());
    }

}
