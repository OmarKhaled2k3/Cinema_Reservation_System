package com.example.cinema_reservation_system;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends User implements Reservable{

    public Admin(String name, String email, String username, String password) {
        super(name, email,username,password);

    }
    public Admin(String name) {
        super(name);

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

    public void createReservation() {
        Reservation reservation = Reservation.getInstance();
        int id = reservation.getId();
        int showtimeID = reservation.getShowtime().getId();
        int customerID = reservation.getCustomer().getId();
        String reservationQuery;

        try {
            Database db = Database.getInstance();
            ArrayList<String> query = new ArrayList<>();
            if(reservation.getFoodOrder()!=null) {
                int foodOrderID =foodOrderID_Query();

                String foodQuery = String.format("INSERT INTO foodorder (id,food,quantity) VALUES(%d,%s,%s)", foodOrderID,reservation.getFoodOrder().FoodItemsToString(), reservation.getFoodOrder().FoodQtyToString());
                try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(foodQuery)) {
                    stmt.executeUpdate();
                }
                reservationQuery = "INSERT INTO Reservation (ShowtimeID,customerID,FoodOrderID,seats) VALUES(?,?,?,?)";
                try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(reservationQuery);) {
                    stmt.setInt(1, showtimeID);
                    stmt.setInt(2, customerID);
                    stmt.setString(3, String.valueOf(foodOrderID));
                    stmt.setString(4, reservation.getSeatSelectedString());
                    stmt.executeUpdate();
                }

            }
            else {
                reservationQuery = "INSERT INTO Reservation (ShowtimeID,customerID,seats) VALUES(?,?,?)";
                try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(reservationQuery)) {
                    stmt.setInt(1, showtimeID);
                    stmt.setInt(2, customerID);
                    stmt.setString(3, reservation.getSeatSelectedString());
                    stmt.executeUpdate();
                }
            }

            reservation.getShowtime().getSeatList().addAll(reservation.getSeatSelected());
            ArrayList<Seat> showtimeReservedSeats=reservation.getShowtime().getSeatList();
            String showtimeSeats = Seat.SeatsConversiontoString(showtimeReservedSeats);
            String showtimeQuery = String.format("UPDATE showtime SET seats=%s where id=%d", showtimeSeats,showtimeID);
            showtimeQuery = "UPDATE showtime SET seats=? where id=?";
            try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(showtimeQuery)) {
                stmt.setString(1, showtimeSeats);
                stmt.setInt(2, showtimeID);
                stmt.executeUpdate();
            }
        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
    }
    @Override
    public void cancelReservation() {
        Reservation reservation = Reservation.getInstance();
        int id = reservation.getId();
        int foodOrderID=foodOrderID_Query(id);

        String foodDeleteQuery="delete from foodorder where id="+String.valueOf(foodOrderID);
        try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(foodDeleteQuery)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String reservationDeleteQuery="delete from reservation where id="+String.valueOf(id);
        try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(reservationDeleteQuery)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        deleteOldfromShowtime();
    }
    public void modifyReservation() {
        cancelReservation();
        createReservation();
    }
    public int foodOrderID_Query() throws SQLException {
        int foodOrderID=0;
        Database db = Database.getInstance();
        String foodOrderIDQuery = "Select MAX(ID) from foodorder ";
        ResultSet resultSet;
        resultSet = db.executeQuery(foodOrderIDQuery);
        if(resultSet!=null){
            if(resultSet.next()){
                foodOrderID = resultSet.getInt("MAX(ID)");
            }
            resultSet.close();
        }
        foodOrderID++;
        return foodOrderID;
    }
    public int foodOrderID_Query(int reservationID) {
        int foodOrderID=0;
        try{
            Database db=Database.getInstance();
            String foodOrderIDQuery = "Select foodorderid from reservation where id="+String.valueOf(reservationID);
            ResultSet resultSet;
            resultSet = db.executeQuery(foodOrderIDQuery);
            if(resultSet!=null){
                if(resultSet.next()){
                    foodOrderID = resultSet.getInt("foodorderid");
                }
                resultSet.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return foodOrderID;
    }
    private void deleteOldfromShowtime() {
        Reservation reservation = Reservation.getInstance();

        reservation.getShowtime().UpdateSeats();
        //reservation.getShowtime().setSeatList();
        int showtimeID = reservation.getShowtime().getId();
        ArrayList<Seat> showtimeReservedSeats=reservation.getShowtime().getSeatList();
        String showtimeSeats = Seat.SeatsConversiontoString(showtimeReservedSeats);
        String showtimeQuery = "UPDATE showtime SET seats=? where id=?";
        try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(showtimeQuery)) {
            stmt.setString(1, showtimeSeats);
            stmt.setInt(2, showtimeID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
