package com.example.cinema_reservation_system;

import java.sql.*;
import java.util.Optional;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMovieController implements Initializable {

    @FXML
    private TableView<Movie> table_view;

    @FXML
    private TableColumn<Movie, String> name_column;

    @FXML
    private TableColumn<Movie, String> genre_column;

    @FXML
    private TableColumn<Movie, Integer> duration_column;

    @FXML
    private TableColumn<Movie, String> url_column;

    @FXML
    private TableColumn<Movie, String> desc_column;

    @FXML
    private Button back_button, add_button, remove_button, save_button;

    @FXML
    private TextField namebox, genrebox, durationbox, imageurlbox;

    @FXML
    private TextArea textarea;

    private ObservableList<Movie> movieList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up table columns
        name_column.setCellValueFactory(new PropertyValueFactory<>("title"));
        genre_column.setCellValueFactory(new PropertyValueFactory<>("genre"));
        duration_column.setCellValueFactory(new PropertyValueFactory<>("duration"));
        url_column.setCellValueFactory(new PropertyValueFactory<>("imageurl"));
        desc_column.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Populate the table view with data
        populateTableView();

        // Initialize buttons
        save_button.setDisable(true); // Initially disable save button

        // Clear input fields
        clearFields();

        // Select first row if available
        if (!movieList.isEmpty()) {
            table_view.getSelectionModel().selectFirst();
            showMovieDetails(movieList.get(0));
        }
    }

    private void populateTableView() {
        movieList = FXCollections.observableArrayList();
        Database db = Database.getInstance();
        String query = "SELECT * FROM movies";

        try (ResultSet resultSet = db.getInstance().executeQuery(query)) {
            while (resultSet.next()) {
                movieList.add(new Movie(
                        resultSet.getInt("ID"),
                        resultSet.getString("title"),
                        resultSet.getString("genre"),
                        resultSet.getInt("duration"),
                        resultSet.getString("imageurl"),
                        resultSet.getString("description")
                ));
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load movies from database.");
        }

        table_view.setItems(movieList);

        table_view.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showMovieDetails(newValue);
            }
        });
    }

    private void showMovieDetails(Movie movie) {
        namebox.setText(movie.getTitle());
        genrebox.setText(movie.getGenre());
        durationbox.setText(String.valueOf(movie.getDuration()));
        imageurlbox.setText(movie.getImageurl());
        textarea.setText(movie.getDescription());
        save_button.setDisable(false);
    }

    @FXML
    private void goBack() throws IOException {
        SceneController.launchScene("Admin_View.fxml");
    }

    @FXML
    private void addMovie() {
        String title = namebox.getText();
        String genre = genrebox.getText();
        String durationStr = durationbox.getText();
        String url = imageurlbox.getText();
        String description = textarea.getText();

        if (title.isEmpty() || genre.isEmpty() || durationStr.isEmpty() || url.isEmpty() || description.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        try {
            int duration = Integer.parseInt(durationStr);

            String query = "INSERT INTO movies (title, genre, duration, imageurl, description) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(query)) {
                stmt.setString(1, title);
                stmt.setString(2, genre);
                stmt.setInt(3, duration);
                stmt.setString(4, url);
                stmt.setString(5, description);
                stmt.executeUpdate();
            }

            movieList.add(new Movie(0, title, genre, duration, url, description));
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Error", "Duration must be a number.");
        } catch (SQLException e) {
            showAlert("Error", "Failed to add movie.");
        }
    }

    @FXML
    private void removeMovie() {
        Movie selectedMovie = table_view.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this movie?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    String query = "DELETE FROM movies WHERE title = ?";
                    try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(query)) {
                        stmt.setString(1, selectedMovie.getTitle());
                        stmt.executeUpdate();
                    }
                    movieList.remove(selectedMovie);
                    clearFields();
                } catch (SQLException e) {
                    showAlert("Error", "Failed to delete movie.");
                }
            }
        }
    }

    @FXML
    private void saveMovie() {
        Movie selectedMovie = table_view.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            String title = namebox.getText();
            String genre = genrebox.getText();
            String durationStr = durationbox.getText();
            String url = imageurlbox.getText();
            String description = textarea.getText();

            try {
                int duration = Integer.parseInt(durationStr);

                String query = "UPDATE movies SET genre = ?, duration = ?, url = ?, description = ? WHERE title = ?";
                try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(query)) {
                    stmt.setString(1, genre);
                    stmt.setInt(2, duration);
                    stmt.setString(3, url);
                    stmt.setString(4, description);
                    stmt.setString(5, title);
                    stmt.executeUpdate();
                }

                selectedMovie.getGenreProperty().set(genre);
                selectedMovie.getDurationProperty().set(duration);
                selectedMovie.getImageurlProperty().set(url);
                selectedMovie.getDescriptionProperty().set(description);
                table_view.refresh();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Error", "Duration must be a number.");
            } catch (SQLException e) {
                showAlert("Error", "Failed to update movie.");
            }
        }
    }

    private void clearFields() {
        namebox.clear();
        genrebox.clear();
        durationbox.clear();
        imageurlbox.clear();
        textarea.clear();
        save_button.setDisable(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
