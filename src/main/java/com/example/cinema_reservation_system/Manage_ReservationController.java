package com.example.cinema_reservation_system;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Manage_ReservationController {
    private static ArrayList<ShowTime> showTimesReserved = new ArrayList<>();
    private static ArrayList<ArrayList<Seat>> seatsReservedbyIndex = new ArrayList<ArrayList<Seat>>();
    private static ArrayList<ArrayList<FoodItem>> FoodItemsList = new ArrayList<ArrayList<FoodItem>>();
    private static ArrayList<Integer> reservationIDs = new ArrayList<Integer>();
    @FXML
    private Button btn_back;

    @FXML
    private Button btn_modify;

    @FXML
    private Button btn_remove;
    public static class ReservationDataObject {
        private int resID;
        private String movieName;
        private String showtime;
        private String seats;
        private String fooditems;
        private String foodqty;
        private Double seatsPrice;
        private Double foodPrice;
        private Double totalPrice;

        public ReservationDataObject(int resID, String movieName, String showtime, String seats, String fooditems, String foodqty, Double seatsPrice, Double foodPrice, Double totalPrice) {
            this.resID = resID;
            this.movieName = movieName;
            this.showtime = showtime;
            this.seats = seats;
            this.fooditems = fooditems;
            this.foodqty = foodqty;
            this.seatsPrice = seatsPrice;
            this.foodPrice = foodPrice;
            this.totalPrice = totalPrice;
        }

        public int getResID() {
            return resID;
        }

        public String getMovieName() {
            return movieName;
        }

        public String getShowtime() {
            return showtime;
        }

        public String getSeats() {
            return seats;
        }

        public String getFooditems() {
            return fooditems;
        }

        public String getFoodqty() {
            return foodqty;
        }

        public Double getSeatsPrice() {
            return seatsPrice;
        }

        public Double getFoodPrice() {
            return foodPrice;
        }

        public Double getTotalPrice() {
            return totalPrice;
        }
    }

    @FXML
    private TableColumn<ReservationDataObject, String> fooditem_subcolumn;

    @FXML
    private TableColumn<ReservationDataObject, Double> foodprice_subcolumn;

    @FXML
    private TableColumn<ReservationDataObject, Integer> foodqty_subcolumn;

    @FXML
    private TableColumn<ReservationDataObject, String> moviename_column;

    @FXML
    private TableColumn<ReservationDataObject, String> receipt_column;

    @FXML
    private TableView<ReservationDataObject> reservation_table;

    @FXML
    private TableColumn<ReservationDataObject, Integer> resid_column;

    @FXML
    private TableColumn<ReservationDataObject, Double> seatprice_subcolumn;

    @FXML
    private TableColumn<ReservationDataObject, String> seats_column;

    @FXML
    private TableColumn<ReservationDataObject, String> showtime_column;

    @FXML
    private TableColumn<ReservationDataObject, Double> totalprice_column;


    // Instance of SceneController to switch scenes
    private SceneController sceneController = new SceneController();
    ArrayList<ReservationDataObject> reservationDataObjectList = new ArrayList<>();

    @FXML
    private void initialize(){
        reservation_table.getItems().clear();
        fooditem_subcolumn.setCellValueFactory(new PropertyValueFactory<>("fooditems"));
        foodprice_subcolumn.setCellValueFactory(new PropertyValueFactory<>("foodPrice"));
        foodqty_subcolumn.setCellValueFactory(new PropertyValueFactory<>("foodqty"));
        seatprice_subcolumn.setCellValueFactory(new PropertyValueFactory<>("seatsPrice"));
        seats_column.setCellValueFactory(new PropertyValueFactory<>("seats"));
        totalprice_column.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        showtime_column.setCellValueFactory(new PropertyValueFactory<>("showtime"));
        moviename_column.setCellValueFactory(new PropertyValueFactory<>("movieName"));
        resid_column.setCellValueFactory(new PropertyValueFactory<>("resID"));
        Reservation reservation = Reservation.getInstance();
        if(reservation.getCustomer() != null){
            retrieveReservations(reservation.getCustomer().getId());
        }
    }

    private void retrieveReservations(int customerID){
        Database db = Database.getInstance();
        String query = String.format("SELECT" +
                "    r.ID AS ReservationID," +
                "    m.title AS MovieTitle," +
                "    m.ID AS MovieID," +
                "    s.ID AS ShowtimeID, "+
                "    s.time AS Showtime,"+
                "    r.seats AS Seats," +
                "    s.seats AS AvailableSeats," +
                "    fo.food AS FoodOrderItem," +
                "    fo.quantity AS FoodQuantity" +
                " FROM " +
                "    reservation r" +
                " JOIN " +
                "    showtime s ON r.ShowtimeID = s.ID" +
                " JOIN " +
                "    movies m ON s.MovieID = m.ID" +
                " LEFT JOIN " +
                "    foodorder fo ON r.FoodOrderID = fo.ID" +
                " WHERE " +
                "    r.customerID = %d" +
                " GROUP BY " +
                "    r.ID, m.title, m.ID, s.ID, s.time, r.seats, fo.food, fo.quantity",customerID);
        ResultSet resultSet = db.executeQuery(query);
        try{
            showTimesReserved.clear();
            seatsReservedbyIndex.clear();
            FoodItemsList.clear();
            reservationIDs.clear();
            // Burger x5 - 20$ \n Pizza x3 - 10$
        if (resultSet != null) {
            while (resultSet.next()) {
                int reservationID = resultSet.getInt("ReservationID");
                int movieID = resultSet.getInt("MovieID");
                int showtimeID = resultSet.getInt("ShowtimeID");
                String foodOrderItem = resultSet.getString("FoodOrderItem"); // 4600000 Changed to String to match the food item type
                String foodOrderQty = resultSet.getString("FoodQuantity"); // 5300000 Changed to String to match the food item type
                String movieTitle = resultSet.getString("MovieTitle");
                String showtime = resultSet.getString("Showtime");
                String seatsReservedString=resultSet.getString("Seats").trim();
                String showTimeSeats=resultSet.getString("AvailableSeats").trim();
                ArrayList<Seat> seatsReserved = Seat.SeatsConversion(seatsReservedString);/// Seats# 1,4,6 Normal Total Price=10 \n Seats# 50,55 VIP Seats TP = 200
                ArrayList<Seat> showTimeSeatsList = Seat.SeatsConversion(showTimeSeats);/// Seats# 1,4,6 Normal Total Price=10 \n Seats# 50,55 VIP Seats TP = 200

                seatsReservedString = Seat.SeatsReservedDisplayFormat(seatsReserved);
                ArrayList<FoodItem> foodItems=FoodOrder.StringtoFoodItems(foodOrderItem,foodOrderQty);
                String FoodItemsDisplayFormat = "",FoodItemsQtyDisplayFormat = "";
                double totalPriceFood=0d;
                if(foodItems != null){
                    FoodItemsDisplayFormat = FoodItem.convertFoodNameListToString(foodItems);
                    FoodItemsQtyDisplayFormat = FoodItem.convertFoodQtyListToString(foodItems);
                    totalPriceFood=FoodItem.getTotalCost(foodItems);
                    FoodItemsList.add(foodItems);
                }
                double totalPriceSeats=Seat.TotalSeatsPrice();
                double totalPrice=totalPriceSeats+totalPriceFood;
                ReservationDataObject reservationDataObject = new ReservationDataObject(reservationID,movieTitle,showtime,seatsReservedString,FoodItemsDisplayFormat,FoodItemsQtyDisplayFormat,totalPriceSeats,totalPriceFood,totalPrice);
                reservationDataObjectList.add(reservationDataObject);

                Movie movie = new Movie(movieID,movieTitle);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(showtime, formatter);
                ShowTime showTime = new ShowTime(showtimeID,movie,dateTime);
                showTime.setSeatList(showTimeSeatsList);
                showTimesReserved.add(showTime);
                seatsReservedbyIndex.add(seatsReserved);
                reservationIDs.add(reservationID);
            }
            resultSet.close();
        }
        if (reservationDataObjectList!=null&& !reservationDataObjectList.isEmpty())reservation_table.getItems().addAll(reservationDataObjectList);
    } catch (Exception exception) {
        System.out.println("Error: " + exception);
    }

    }
    @FXML
    private void handleModifyButton(ActionEvent event) throws IOException {
        int index=reservation_table.getSelectionModel().getSelectedIndex();
        if(index!=-1){
        Reservation reservation = Reservation.getInstance();
        reservation.setId(reservationIDs.get(index));
        reservation.setShowtime(showTimesReserved.get(index));
        reservation.addSeatSelected(seatsReservedbyIndex.get(index));
        reservation.addFoodOrder(FoodItemsList.get(index));
        reservation.setSeatsOld(seatsReservedbyIndex.get(index));
        SceneController.launchScene("Seats_Admin.fxml");
        }

    }
    @FXML
    private void goBack() throws Exception{
        SceneController.launchScene("Admin_ReservationTab.fxml");
    }
    @FXML
    private void goBackc() throws Exception{
        SceneController.launchScene("Customer_View.fxml");
    }
}
