public class Seat implements Printable{
    private int seatNumber;
    private boolean isReserved;
    private String type ;
    private double basePrice = 100;
    private double VIP = 20;
    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.isReserved = false;
    }

    public boolean isReserved() { return isReserved; }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public double getSeatPrice(){
        if(type == "Normal") return basePrice;
        else return basePrice+VIP;
    }
    public int getSeatNumber() { return seatNumber; }

    public String getType() {
        return type;
    }

    public void printDetails() {
        System.out.print(
                "seatNumber=" + seatNumber +
                ", isReserved=" + isReserved +
                ", type='" + type +
                ", Price=" + getSeatPrice() );
    }
}
