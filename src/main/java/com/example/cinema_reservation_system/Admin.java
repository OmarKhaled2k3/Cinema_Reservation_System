package com.example.cinema_reservation_system;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Admin extends User implements Reservable{

    public Admin(String name, String email, String username, String password) {
        super(name, email,username,password);

    }
    @Override
    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String inputUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String inputPassword = scanner.nextLine();

        if (authenticate(inputUsername, inputPassword)) {
            System.out.println(name + " (Admin) logged in.");
        } else {
            System.out.println("Invalid credentials. Login failed.");
        }
    }
    // Authenticate method
    private boolean authenticate(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.getPassword().equals(inputPassword);
    }
    @Override
    public void logout() {
        System.out.println(name + " (Admin) logged out.");
    }

    @Override
    public void createReservation() {
        Reservation reservation = Reservation.getInstance();
        int reservationId = reservation.getId();
        int showtimeID = reservation.getShowtime().getId();


        try {
            Database db = Database.getInstance();
            String query;
            query = String.format("INSERT INTO Reservation (ID,ShowtimeID,customerID) VALUES(%d,%d,%d)", reservationId, showtimeID,getId());
            ResultSet resultSet = db.executeQuery(query);
            resultSet.close();

        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
    }

    public void createReservation(int customerID) {
        Reservation reservation = Reservation.getInstance();
        int reservationId = reservation.getId();
        int showtimeID = reservation.getShowtime().getId();

        try {
            Database db = Database.getInstance();
            String query;
            query = String.format("INSERT INTO Reservation (ID,ShowtimeID,customerID,seats) VALUES(%d,%d,%d,%s)", reservationId, showtimeID,customerID,reservation.getSeatSelectedString());
            ResultSet resultSet = db.executeQuery(query);
            resultSet.close();

        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
    }

    public void modifyReservation() {
        Reservation reservation = Reservation.getInstance();
        int reservationId = reservation.getId();
        int showtimeID = reservation.getShowtime().getId();

        try {
            Database db = Database.getInstance();
            String query;
                query = String.format("UPDATE Reservation SET ShowtimeID = %d, seats=%s where id=%d",  showtimeID,reservation.getSeatSelectedString(),reservationId);
            ResultSet resultSet = db.executeQuery(query);
            resultSet.close();

        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
    }

    @Override
    public void cancelReservation() {
        // Admin can cancel reservations
        Reservation reservation = Reservation.getInstance();
        int id = reservation.getId();

        try {
            Database db = Database.getInstance();
            ResultSet resultSet = db.executeQuery("delete * from reservation where id="+String.valueOf(id));
            resultSet.close();

        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
    }
    public void listAllReservations() {
        // Admin can cancel reservations
        System.out.println("Admin " + name + " viewing all reservation history.");
    }
    public void AddShowTime(ShowTime showTime){
        // Admin can add show time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the LocalDateTime instance
        String formattedDateTime = showTime.getStartTime().format(formatter);
        int movieId = showTime.getMovie().getId();
        try {
            Database db = Database.getInstance();
            String query;
            query = String.format("INSERT INTO showtime (time,MovieID) VALUES(%s,%d)", formattedDateTime, movieId);
            ResultSet resultSet = db.executeQuery(query);
            resultSet.close();

        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
    }
    public void DeleteShowTime(ShowTime showTime){
        try {
            Database db = Database.getInstance();
            String query;
            query = String.format("delete * from showtime where id="+String.valueOf(showTime.getId()));
            ResultSet resultSet = db.executeQuery(query);
            resultSet.close();

        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
    }
    public void AddMovie(Movie movie){
        String movieTitle = movie.getTitle();
        String genre = movie.getGenre();
        String duration = String.valueOf(movie.getDuration()) + " min";
        String description = movie.getDescription();
        String url = movie.getImageurl();
        try {
            Database db = Database.getInstance();
            String query;
            query = String.format("INSERT INTO movies (title,genre,duration,description,imageurl) VALUES(%s,%s,%s,%s,%s)",movieTitle,genre,duration,description,url);
            ResultSet resultSet = db.executeQuery(query);
            resultSet.close();

        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
    }
    public void DeleteMovie(Movie movie){
        try {
            Database db = Database.getInstance();
            String query;
            query = String.format("delete * from movies where id="+String.valueOf(movie.getId()));
            ResultSet resultSet = db.executeQuery(query);
            resultSet.close();

        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
    }
    public void AddFoodItem(FoodItem foodItem){
        try {
            Database db = Database.getInstance();
            String query;
            query = String.format("INSERT INTO food (name,type,price) VALUES(%s,%s,%f)", foodItem.getName(),foodItem.getType(), foodItem.getPrice());
            ResultSet resultSet = db.executeQuery(query);
            resultSet.close();

        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
    }
    public void DeleteFoodItem(FoodItem foodItem){
        try {
            Database db = Database.getInstance();
            String query;
            query = String.format("delete * from food where name="+String.valueOf(foodItem.getName()));
            ResultSet resultSet = db.executeQuery(query);
            resultSet.close();
        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
    }

}
