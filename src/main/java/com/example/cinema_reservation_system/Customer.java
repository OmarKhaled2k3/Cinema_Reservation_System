package com.example.cinema_reservation_system;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Customer extends User implements Reservable {
    public Customer(String name, String email, String username, String password) {
        super(name, email,username,password);

    }
    public Customer(String name,int id) {
        super(name, id);

    }
    @Override
    public void login() {
        System.out.println(name + " logged in.");
    }

    @Override
    public void logout() {
        System.out.println(name + " logged out.");
    }

    @Override
    public void createReservation() {
        Reservation reservation = Reservation.getInstance();
        int id = reservation.getId();
        int showtimeID = reservation.getShowtime().getId();
        int customerID = reservation.getCustomer().getId();
        String reservationQuery="";
        ResultSet resultSet;
        try {
            Database db = Database.getInstance();
            ArrayList<String> query = new ArrayList<>();
            if(reservation.getFoodOrder()!=null) {
                int foodOrderID =0;
                String foodOrderIDQuery = "Select MAX(ID) from foodorder ";
                resultSet = db.executeQuery(foodOrderIDQuery);
                if(resultSet!=null){
                    if(resultSet.next()){
                        foodOrderID = resultSet.getInt("MAX(ID)");
                    }
                resultSet.close();
                }
                foodOrderID++;
                String foodQuery = String.format("INSERT INTO foodorder (id,food,quantity) VALUES(%d,%s,%s)", foodOrderID,reservation.getFoodOrder().FoodItemsToString(), reservation.getFoodOrder().FoodQtyToString());
                reservationQuery = String.format("INSERT INTO Reservation (ShowtimeID,customerID,FoodOrderID,seats) VALUES(%d,%d,%d,%s)", showtimeID, customerID, foodOrderID , reservation.getSeatSelectedString());
                query.add(foodQuery);

            }
            else {
                reservationQuery = String.format("INSERT INTO Reservation (ShowtimeID,customerID,seats) VALUES(%d,%d,%s)",  showtimeID,customerID,reservation.getSeatSelectedString());
            }
            query.add(reservationQuery);
            reservation.getShowtime().getSeatList().addAll(reservation.getSeatSelected());
            ArrayList<Seat> showtimeReservedSeats=reservation.getShowtime().getSeatList();
            String showtimeSeats = Seat.SeatsConversiontoString(showtimeReservedSeats);
            String showtimeQuery = String.format("UPDATE showtime SET seats=%s where id=%d", showtimeSeats,showtimeID);
            query.add(showtimeQuery);
            for(String singleQuery:query){
                try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(singleQuery)) {
                    stmt.executeUpdate();
                }
            }

        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
        reservation.reset(++id);
    }
    //ShowTime showTime = new ShowTime(Movie,starttime);
    // Seats
    //ShowTime showTime = new ShowTime(new Movie("dsfsdf","sdfs",50), LocalDateTime.now());
    //Reservation reservation = new Reservation(new Customer("sdf","sdf","sdf","sdf") ,showTime,new ArrayList<Seat>());
    @Override
    public void cancelReservation() {
        Reservation reservation = Reservation.getInstance();
        int id = reservation.getId();

        try {
            Database db = Database.getInstance();
            ResultSet resultSet = db.executeQuery("delete * from reservation where id="+String.valueOf(id));
            resultSet.close();

        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
        reservation.reset(id);
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

    public void viewReservation() {
        // Example logic for viewing a reservation
        System.out.println("Viewing reservation history for " + name);
    }

}
