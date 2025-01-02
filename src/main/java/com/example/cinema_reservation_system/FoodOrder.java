import java.util.ArrayList;
import java.util.List;

class FoodOrder implements Printable {
    private ArrayList<FoodItem> foodItems;
    private double totalCost;
    public FoodOrder() {
        this.foodItems = new ArrayList<>();
    }

    public void addItem(FoodItem item) {
        foodItems.add(item);
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public double getTotalCost() {
        totalCost=0;
        for(FoodItem foodItem : foodItems){
            totalCost+=foodItem.getPrice();
        }
        return totalCost;
    }

    @Override
    public void printDetails() {
        System.out.println("Food Order Details:");
        for (FoodItem item : foodItems) {
            item.printDetails();
        }
        System.out.println("Total Cost: $" + getTotalCost());
    }
}