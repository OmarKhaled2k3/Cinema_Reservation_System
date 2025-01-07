package com.example.cinema_reservation_system;

import java.util.ArrayList;

public class Seat implements Printable{
    private int seatNumber;
    private boolean isReserved;
    private String type ;
    private double basePrice = 100;
    private double VIP = 20;
    private static ArrayList<Seat>standardSeats=new ArrayList<>();
    private static ArrayList<Seat>vipSeats=new ArrayList<>();

    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.isReserved = false;
    }
    public Seat(int seatNumber,boolean isReserved) {
        this.seatNumber = seatNumber;
        this.isReserved = isReserved;
    }
    public static Seat getSeatbyID(ArrayList<Seat> seats,int id){
        for(Seat seat : seats){
            if(seat.seatNumber == id)
                return seat;
        }
        return null;
    }
    public static ArrayList<Seat> SeatsConversion(String seatsString){
        ArrayList<Seat> seatsReserved= new ArrayList<>();
        for(int i=0;i<80;i++){
            if(seatsString.charAt(i) == '1'){
                Seat seat = new Seat(i+1,true);
                seatsReserved.add(seat);
            }
        }
        return seatsReserved;
    }
    public static String SeatsConversiontoString(ArrayList<Seat> seatsReserved){
        StringBuilder seatsString = new StringBuilder("00000000000000000000000000000000000000000000000000000000000000000000000000000000");
        for(Seat i:seatsReserved){
            seatsString.setCharAt((i.seatNumber)-1,'1');
        }
        return seatsString.toString();
    }
    private static void SortSeatsSelectedbyType(ArrayList<Seat>seats) {
        standardSeats.clear();
        vipSeats.clear();
        for(Seat seat : seats){
            if(!seat.isVIP(seat))
                standardSeats.add(seat);
            else{
                vipSeats.add(seat);
            }
        }
    }
    public static double TotalSeatsPrice(){
        double standardSeatsPrice=0d;
        double vipSeatsPrice=0d;
        if(!standardSeats.isEmpty())
            standardSeatsPrice= standardSeats.size()*standardSeats.getFirst().getSeatPrice();
        if(!vipSeats.isEmpty())
            vipSeatsPrice= vipSeats.size()*vipSeats.getFirst().getSeatPrice();
        return standardSeatsPrice+vipSeatsPrice;
    }
    public static String SeatsReservedDisplayFormat(ArrayList<Seat> seatsReserved){
        SortSeatsSelectedbyType(seatsReserved);
        int standardSeatsQty=standardSeats.size();
        int vipSeatsQty=vipSeats.size();
        String vipDetails=" ",standardDetails=" ";
        if(!standardSeats.isEmpty()){
            standardDetails="Std #: [";
            for(Seat seat :standardSeats){
                standardDetails+=seat.seatNumber+", ";
            }
            standardDetails+="] x"+String.valueOf(standardSeatsQty);
            standardDetails+=" TP:"+String.valueOf((double)standardSeatsQty*(standardSeats.getFirst().getSeatPrice())) +" $" +'\n';
        }
        if(!vipSeats.isEmpty()){
            vipDetails="VIP#: [";
            for(Seat seat :vipSeats){
                vipDetails+=seat.seatNumber+", ";
            }
            vipDetails+="] x"+String.valueOf(vipSeatsQty);
            vipDetails+=" TP:"+String.valueOf(vipSeatsQty*vipSeats.getFirst().getSeatPrice()) + " $";
        }

        return standardDetails+vipDetails;

    }
    public boolean isReserved() { return isReserved; }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }
    public double getSeatPrice(int seatNumber){
        String type;
        if(seatNumber >=43 && seatNumber <=65) type = "VIP";
        else type = "Standard";

        if(type == "Standard") return basePrice;
        else return basePrice+VIP;
    }
    public double getSeatPrice(){
        if(!isVIP()) return basePrice;
        else return basePrice+VIP;
    }
    public int getSeatNumber() { return seatNumber; }

    public String getType() {
        if(seatNumber >=43 && seatNumber <=65) type = "VIP";
        else type = "Standard";
        return type;
    }
    public boolean isVIP(){
        return type.equals("VIP");
    }
    public boolean isVIP(Seat seat){
        String type = seat.getType();
        return type.equals("VIP");
    }
    public static String getType(int seatNumber) {
        if(seatNumber >=43 && seatNumber <=65) return  "VIP";
        return "Standard";
    }

    public void printDetails() {
        System.out.print(
                "seatNumber=" + seatNumber +
                ", isReserved=" + isReserved +
                ", type='" + type +
                ", Price=" + getSeatPrice(seatNumber) );
    }
}
