package rental;

import car.Car;
import user.customer.Customer; // Updated Import

public class Rental {
    private String rentalId;
    private Car rentedCar;       
    private Customer renter;     
    private int rentalDays;
    private double totalCost;
    private boolean isActive; 

    public Rental(String rentalId, Car rentedCar, Customer renter, int rentalDays) {
        this.rentalId = rentalId;
        this.rentedCar = rentedCar;
        this.renter = renter;
        this.rentalDays = rentalDays;
        this.totalCost = calculateTotalCost();
        this.isActive = true; 
    }

    private double calculateTotalCost() {
        if (rentedCar != null && renter != null) {
            double baseCost = rentedCar.getDailyrate() * rentalDays;
            double discountAmount = baseCost * renter.getDiscountRate(); 
            return baseCost - discountAmount; 
        }
        return 0.0;
    }

    public void returnCar() { this.isActive = false; }

    public void printReceipt() {
        System.out.println("\n=====================================");
        System.out.println("          RENTAL RECEIPT             ");
        System.out.println("=====================================");
        System.out.println("Rental ID    : " + rentalId);
        System.out.println("Customer Name: " + renter.getName());
        System.out.println("Tier/Discount: " + renter.getMembershipTier() + " (" + (renter.getDiscountRate() * 100) + "% Off)");
        System.out.println("Car Details  : " + rentedCar.getBrand() + " " + rentedCar.getModel() + " (" + rentedCar.getPlateNum() + ")");
        System.out.println("Rental Days  : " + rentalDays + " days");
        System.out.println("Total Cost   : RM " + String.format("%.2f", totalCost));
        System.out.println("Status       : " + (isActive ? "Active" : "Completed"));
        System.out.println("=====================================\n");
    }

    public String getRentalId() { return rentalId; }
    public Car getRentedCar() { return rentedCar; }
    public Customer getRenter() { return renter; }
    public int getRentalDays() { return rentalDays; }
    public double getTotalCost() { return totalCost; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
}