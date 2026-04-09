package rental;

import car.Car;
import customer.Customer;

public class Rental {
    
    // Private Attributes
    private String rentalId;
    private Car rentedCar;       
    private Customer renter;     
    private int rentalDays;
    private double totalCost;
    private boolean isActive; 

    // Constructor
    public Rental(String rentalId, Car rentedCar, Customer renter, int rentalDays) {
        this.rentalId = rentalId;
        this.rentedCar = rentedCar;
        this.renter = renter;
        this.rentalDays = rentalDays;
        this.totalCost = calculateTotalCost();
        this.isActive = true; 
    }

    // Business Logic
    private double calculateTotalCost() {
        if (rentedCar != null) {
            return rentedCar.getRentalRatePerDay() * rentalDays;
        }
        return 0.0;
    }

    public void returnCar() {
        this.isActive = false;
    }

    public void printReceipt() {
        System.out.println("\n=====================================");
        System.out.println("          RENTAL RECEIPT             ");
        System.out.println("=====================================");
        System.out.println("Rental ID    : " + rentalId);
        System.out.println("Customer Name: " + renter.getName());
        System.out.println("Customer IC  : " + renter.getCustomerId());
        System.out.println("Car Details  : " + rentedCar.getBrandModel() + " (" + rentedCar.getPlateNumber() + ")");
        System.out.println("Rental Days  : " + rentalDays + " days");
        System.out.println("Total Cost   : RM " + String.format("%.2f", totalCost));
        System.out.println("Status       : " + (isActive ? "Active" : "Completed"));
        System.out.println("=====================================\n");
    }

    // ==================
    // TXT FILE FORMATTER 
    // ==================
    // This creates a comma-separated string that is easy to save to a .txt file
    // Example output: R1001,VHA1234,010523011234,3,360.0,true
    public String toTextFileFormat() {
        return rentalId + "," + 
               rentedCar.getPlateNumber() + "," + 
               renter.getCustomerId() + "," + 
               rentalDays + "," + 
               totalCost + "," + 
               isActive;
    }

    // Getters and Setters
    public String getRentalId() { return rentalId; }
    public void setRentalId(String rentalId) { this.rentalId = rentalId; }
    public Car getRentedCar() { return rentedCar; }
    public void setRentedCar(Car rentedCar) { 
        this.rentedCar = rentedCar; 
        this.totalCost = calculateTotalCost(); 
    }
    public Customer getRenter() { return renter; }
    public void setRenter(Customer renter) { this.renter = renter; }
    public int getRentalDays() { return rentalDays; }
    public void setRentalDays(int rentalDays) {
        if (rentalDays > 0) {
            this.rentalDays = rentalDays;
            this.totalCost = calculateTotalCost(); 
        } else {
            System.out.println("Error: Rental days must be greater than 0.");
        }
    }
    public double getTotalCost() { return totalCost; }
    public boolean isActive() { return isActive; }
    
    // Setter for loading from file
    public void setActive(boolean active) { this.isActive = active; }
}
