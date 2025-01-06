package com.example.cinema_reservation_system;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Customer extends User implements Reservable {
    private boolean Modify = false;

    public boolean isModify() {
        return Modify;
    }

    public void setModify(boolean modify) {
        Modify = modify;
    }

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

            //query.add(showtimeQuery);
            /*
            for(String singleQuery:query){
                try (PreparedStatement stmt = Database.getInstance().getConnection().prepareStatement(singleQuery)) {
                    stmt.executeUpdate();
                }
            }

             */

        } catch (Exception exception) {
            System.out.println("Error: " + exception);
        }
        reservation.reset(++id);
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
    public void viewReservation() {
        // Example logic for viewing a reservation
        System.out.println("Viewing reservation history for " + name);
    }

}
