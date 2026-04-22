package manager;

import rental.Rental;
import car.Car;
import user.customer.Customer;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class RentalManager {

    public static void viewAllRentals(Scanner scanner, ArrayList<Rental> rentalList) {
        boolean active = true;
        while (active) {
            Collections.sort(rentalList, (r1, r2) -> r1.getRentedCar().getBrand().compareToIgnoreCase(r2.getRentedCar().getBrand()));
            System.out.println("\n--- VIEW RENTAL RECORDS ---");
            if (rentalList.isEmpty()) { System.out.println("No rentals found."); return; }
            System.out.println("1. All Rental History\n2. Active Rentals\n3. Past Rentals\n4. Search by Customer ID\n5. Search by Car Plate\n0. Return");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine();
                switch (choice) {
                    case 1: printFilteredRentals(rentalList, "ALL", ""); break;
                    case 2: printFilteredRentals(rentalList, "STATUS", "true"); break;
                    case 3: printFilteredRentals(rentalList, "STATUS", "false"); break;
                    case 4: System.out.print("Enter Customer ID: "); printFilteredRentals(rentalList, "CUSTOMER", scanner.nextLine()); break;
                    case 5: System.out.print("Enter Car Plate: "); printFilteredRentals(rentalList, "PLATE", scanner.nextLine()); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    private static void printFilteredRentals(ArrayList<Rental> rentalList, String filterType, String filterValue) {
        System.out.printf("\n%-10s %-15s %-15s %-10s %-15s %-10s\n", "Rental ID", "Customer ID", "Car Plate", "Days", "Total(RM)", "Status");
        System.out.println("--------------------------------------------------------------------------------");
        boolean found = false;
        for (Rental r : rentalList) {
            boolean isMatch = filterType.equals("ALL") ||
                             (filterType.equals("STATUS") && r.isActive() == Boolean.parseBoolean(filterValue)) ||
                             (filterType.equals("CUSTOMER") && r.getRenter().getId().equalsIgnoreCase(filterValue)) ||
                             (filterType.equals("PLATE") && r.getRentedCar().getPlateNum().equalsIgnoreCase(filterValue));
            if (isMatch) {
                System.out.printf("%-10s %-15s %-15s %-10d %-15.2f %-10s\n", r.getRentalId(), r.getRenter().getId(), r.getRentedCar().getPlateNum(), r.getRentalDays(), r.getTotalCost(), r.isActive() ? "Active" : "Returned");
                found = true;
            }
        }
        if (!found) System.out.println("No matching records found.");
    }

    public static void rentCar(Scanner scanner, ArrayList<Car> carList, ArrayList<Rental> rentalList, Customer activeCustomer) {
        System.out.println("\n--- RENT A CAR ---");
        try {
            System.out.print("Enter Car Plate Number you wish to rent: "); String plate = scanner.nextLine();
            Car selectedCar = null;
            for (Car car : carList) if (car.getPlateNum().equalsIgnoreCase(plate)) { selectedCar = car; break; }
            
            if (selectedCar == null) { System.out.println("Error: Car not found."); return; }
            if (!selectedCar.isAvailable()) { System.out.println("Sorry, car is rented out."); return; }
            
            System.out.print("Enter number of days to rent (Max 30 days): "); int days = scanner.nextInt(); scanner.nextLine(); 
            if (days <= 0 || days > 30) { System.out.println("DENIED: You can only rent a car for 1 to 30 days."); return; }

            String newRentalId = "R" + (rentalList.size() + 1001); 
            Rental newRental = new Rental(newRentalId, selectedCar, activeCustomer, days);
            
            rentalList.add(newRental);
            selectedCar.setAvailable(false); 

            System.out.println("\nSUCCESS: Car successfully rented!");
            System.out.println("(Note: Your loyalty spending will be updated once the car is returned.)");
            newRental.printReceipt();
        } catch (Exception e) { System.out.println("Error: Invalid input."); scanner.nextLine(); }
    }

    public static void returnCar(Scanner scanner, ArrayList<Rental> rentalList, Customer activeCustomer) {
        System.out.println("\n--- RETURN A CAR ---");
        System.out.print("Enter your active Rental ID (e.g., R1001): "); String rentId = scanner.nextLine();
        Rental rentalToReturn = null;
        for (Rental r : rentalList) {
            if (r.getRentalId().equalsIgnoreCase(rentId) && r.getRenter().getId().equals(activeCustomer.getId())) { rentalToReturn = r; break; }
        }
        if (rentalToReturn == null) { System.out.println("Error: Rental ID not found or not yours."); return; }
        if (!rentalToReturn.isActive()) { System.out.println("Notice: Already returned."); return; }

        rentalToReturn.returnCar(); 
        rentalToReturn.getRentedCar().setAvailable(true); 
        
        activeCustomer.addSpending(rentalToReturn.getTotalCost());
        
        System.out.println("SUCCESS: Car " + rentalToReturn.getRentedCar().getPlateNum() + " returned.");
        System.out.println(">> RM " + String.format("%.2f", rentalToReturn.getTotalCost()) + " has been added to your lifetime spend!");
        
        String currentTier = activeCustomer.getMembershipTier();
        if (!currentTier.equals("Normal") && activeCustomer.getManualTierOverride().equals("None")) {
            System.out.println(">> TIER STATUS: You are currently a " + currentTier + " member!");
        }
    }
}