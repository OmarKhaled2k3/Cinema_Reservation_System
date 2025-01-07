package com.example.cinema_reservation_system;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class AdminManage_ReservationController implements Initializable {
    static ArrayList<Customer> customers = new ArrayList<>();
    static ArrayList<ArrayList<Seat>> seatsReservedbyIndex = new ArrayList<ArrayList<Seat>>();
    static ArrayList<ArrayList<FoodItem>> FoodItemsList = new ArrayList<ArrayList<FoodItem>>();
    private static ArrayList<Integer> reservationIDs = new ArrayList<Integer>();
    public Label showtimeMovie;
    @FXML
    private Button btn_back;

    @FXML
    private Button btn_modify;

    @FXML
    private Button btn_remove;
    @FXML
    private Button btn_save;





    public static class ReservationDataObject {
        private int resID;
        private int customerID;
        private String customerName;
        private String seats;
        private String fooditems;
        private String foodqty;
        private Double seatsPrice;
        private Double foodPrice;
        private Double totalPrice;
        public ReservationDataObject(int resID, int CustomerID, String customerName, String seats, String fooditems, String foodqty, Double seatsPrice, Double foodPrice, Double totalPrice) {
            this.resID = resID;
            this.customerID = CustomerID;
            this.customerName = customerName;
            this.seats = seats;
            this.fooditems = fooditems;
            this.foodqty = foodqty;
            this.seatsPrice = seatsPrice;
            this.foodPrice = foodPrice;
            this.totalPrice = totalPrice;
        }

        public String getCustomerName() {return customerName;}
        public int getResID() {
            return resID;
        }

        public int getCustomerID() {
            return customerID;
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
    private TableColumn<ReservationDataObject, Integer> cusid_column;

    @FXML
    private TableColumn<ReservationDataObject, Double> seatprice_subcolumn;

    @FXML
    private TableColumn<ReservationDataObject, String> seats_column;

    @FXML
    private TableColumn<ReservationDataObject, Double> totalprice_column;


    ArrayList<ReservationDataObject> reservationDataObjectList = new ArrayList<>();

    @FXML
    public void initialize(java.net.URL location, ResourceBundle resources) {
        reservation_table.getItems().clear();
        fooditem_subcolumn.setCellValueFactory(new PropertyValueFactory<>("fooditems"));
        foodprice_subcolumn.setCellValueFactory(new PropertyValueFactory<>("foodPrice"));
        foodqty_subcolumn.setCellValueFactory(new PropertyValueFactory<>("foodqty"));
        seatprice_subcolumn.setCellValueFactory(new PropertyValueFactory<>("seatsPrice"));
        seats_column.setCellValueFactory(new PropertyValueFactory<>("seats"));
        totalprice_column.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        resid_column.setCellValueFactory(new PropertyValueFactory<>("resID"));
        cusid_column.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        Reservation reservation = Reservation.getInstance();
        if(reservation.getShowtime() != null){
            retrieveReservations(reservation.getShowtime().getId());
        }
        String showtimeMovieString="";
        showtimeMovieString+=reservation.getShowtime().getStartTime().toString();
        showtimeMovieString+="  "+ reservation.getShowtime().getMovie().getTitle();
        showtimeMovie.setText(showtimeMovieString);
        reservation_table.getItems().setAll(reservationDataObjectList);
    }

   @FXML
    private void retrieveReservations( int x){
        Database db = Database.getInstance();
       String query = String.format(
               "SELECT " +
                       "    r.ID AS ReservationID, " +
                       "    a.ID AS CustomerID, " +  // Assuming you want the customer ID as well
                       "    a.name AS CustomerName, " +  // Selecting the customer name from accounts
                       "    r.seats AS SeatsReserved, " +  // Seats reserved by the customer
                       "    fo.food AS FoodOrderItem, " +
                       "    fo.quantity AS FoodQuantity " +
                       "FROM " +
                       "    reservation r " +
                       "JOIN " +
                       "    accounts a ON r.CustomerID = a.ID " +
                       "LEFT JOIN " +
                       "    foodorder fo ON r.FoodOrderID = fo.ID " +
                       "WHERE " +
                       "    r.ShowtimeID = %d", x
       );
        ResultSet resultSet = db.executeQuery(query);
        try{
            customers.clear();
            seatsReservedbyIndex.clear();
            FoodItemsList.clear();
            reservationIDs.clear();
            // Burger x5 - 20$ \n Pizza x3 - 10$
            if (resultSet != null) {
                while (resultSet.next()) {
                    int reservationID = resultSet.getInt("ReservationID");
                    int CustomerID = resultSet.getInt("CustomerID");
                    String CustomerName = resultSet.getString("CustomerName");
                    String foodOrderItem = resultSet.getString("FoodOrderItem"); // 4600000 Changed to String to match the food item type
                    String foodOrderQty = resultSet.getString("FoodQuantity"); // 5300000 Changed to String to match the food item type
                    if (foodOrderItem == null) {
                        foodOrderItem = "";  // Set to empty string if null
                    }
                    if (foodOrderQty == null) {
                        foodOrderQty = "0";  // Set to 0 if null
                    }// 5300000 Changed to String to match the food item type
                    String seatsReservedString=resultSet.getString("SeatsReserved").trim();
                    ArrayList<Seat> seatsReserved = Seat.SeatsConversion(seatsReservedString);
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
                    else FoodItemsList.add(new ArrayList<>());
                    double totalPriceSeats=Seat.TotalSeatsPrice();
                    double totalPrice=totalPriceSeats+totalPriceFood;
                    Customer customer = new Customer(CustomerName,CustomerID);
                    ReservationDataObject reservationDataObject = new ReservationDataObject(reservationID,CustomerID,CustomerName,seatsReservedString,FoodItemsDisplayFormat,FoodItemsQtyDisplayFormat,totalPriceSeats,totalPriceFood,totalPrice);
                    reservationDataObjectList.add(reservationDataObject);
                    seatsReservedbyIndex.add(seatsReserved);
                    reservationIDs.add(reservationID);
                    customers.add(customer);

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
            reservation.addSeatSelected(seatsReservedbyIndex.get(index));
            reservation.addFoodOrder(FoodItemsList.get(index));
            reservation.setSeatsOld(seatsReservedbyIndex.get(index));
            reservation.setCustomer(customers.get(index));
            customers.get(index).setModify(true);
            SceneController.launchScene("Seats_Admin.fxml");
        }

    }
    @FXML
    private void goBack() throws Exception{
        SceneController.launchScene("Admin_ReservationTab.fxml");
    }
    public void remove(ActionEvent actionEvent) {
        int index = reservation_table.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            Reservation reservation = Reservation.getInstance();
            reservation.setId(reservationIDs.get(index));
            reservation.addSeatSelected(seatsReservedbyIndex.get(index));
            reservation.addFoodOrder(FoodItemsList.get(index));
            reservation.setSeatsOld(seatsReservedbyIndex.get(index));
            reservation.getShowtime().UpdateSeatsInverted();
            reservation.setCustomer(customers.get(index));
            reservation.getAdmin().cancelReservation();
            RemoveatIndex(index);
            reservation_table.refresh();
            showAlert("","Reservaton deleted successfully");
        }
    }
    private void RemoveatIndex(int index){
        customers.remove(index);
        seatsReservedbyIndex.remove(index);
        FoodItemsList.remove(index);
        reservationIDs.remove(index);
        reservation_table.getItems().remove(index);
        reservationDataObjectList.remove(index);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
