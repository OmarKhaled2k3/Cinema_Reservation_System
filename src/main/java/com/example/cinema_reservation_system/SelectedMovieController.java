package com.example.cinema_reservation_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SelectedMovieController implements Initializable {
    Database db = Database.getInstance();
    @FXML
    private Text Duration;

    @FXML
    private Button backButton;

    @FXML
    private Button bookButton;

    @FXML
    private HBox Showtimebox;

    @FXML
    private Text description;

    @FXML
    private ImageView selectedMoviePoster;

    @FXML
    private Text title;

    public void initialize(URL location, ResourceBundle resources) {

        Movie movie = retrieveMovie();
        title.setText( movie.getTitle());
        selectedMoviePoster.setImage(new Image(movie.getImageurl()));
        description.setText(movie.getDescription());
        Duration.setText(String.valueOf(movie.getDuration())+" min");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        ArrayList<ShowTime> showTimes= retrieveShowtimes(movie);
        for(int i =0;i<showTimes.size();i++){
            String formattedDateTime = showTimes.get(i).getStartTime().format(formatter);
            ShowTime showTime = showTimes.get(i);
            Button btn = new Button(formattedDateTime);
            Showtimebox.getChildren().add(btn);
            btn.setOnMouseClicked(e -> {
                // System.out.printf("Mouse clicked cell [%d, %d]%n", rowIndex, colIndex);
                // System.out.println("Film Title: " + id);
                try {
                    Reservation reservation = Reservation.getInstance();

                    reservation.setShowtime(showTime);
                    SceneController.launchScene("Seats_Admin.fxml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }



    }

    private Movie retrieveMovie(){
        Movie movie =null;
        Reservation reservation = Reservation.getInstance();
        int movieId = reservation.getMovieId();
        try {
            Database db = Database.getInstance();
            ResultSet resultSet = db.executeQuery("select * from movies where id="+movieId);
            if (resultSet != null) {
                while (resultSet.next()) {
                    String genre=resultSet.getString("genre").trim();
                    String title=resultSet.getString("title").trim();
                    int duration= Integer.parseInt(resultSet.getString("duration").trim().split(" ")[0]);
                    String description=resultSet.getString("description").trim();
                    String imageurl=resultSet.getString("imageurl").trim();
                    movie = new Movie(movieId,title,genre,duration,description,imageurl);
                    //System.out.println("CellID : " + movieId + " Type : " + type);
                }
                resultSet.close();
            }
        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
        return movie;
    }
    private ArrayList<ShowTime> retrieveShowtimes(Movie movie){
        ArrayList<ShowTime> showTimes =new ArrayList<>();
        Reservation reservation = Reservation.getInstance();
        reservation.getCustomer().setModify(false);
        int movieId = reservation.getMovieId();
        try {
            Database db = Database.getInstance();
            System.out.print(movieId);
            ResultSet resultSet = db.executeQuery("select * from showtime where movieid="+movieId);
            if (resultSet != null) {
                while (resultSet.next()) {
                    int showtimeID = resultSet.getInt("id");
                    String time=resultSet.getString("time").trim();
                    String seats=resultSet.getString("seats").trim();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
                    System.out.println("here"+"showtimeID");
                    ShowTime showTime =  new ShowTime(showtimeID,movie,dateTime);
                    showTime.setSeatList(Seat.SeatsConversion(seats));
                    showTimes.add(showTime);
                }
                resultSet.close();
            }
        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
        return showTimes;
    }
    public void backToMoviesScene(ActionEvent event) throws IOException {

        SceneController.launchScene("SelectMovie.fxml");
    }

}
