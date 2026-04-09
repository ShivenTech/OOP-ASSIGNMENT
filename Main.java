import car.Car;
import customer.Customer;
import rental.Rental;
import dataHandler.DataHandler;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Collections;
import java.util.Comparator;

public class Main {
    
    // 1. Temporary Data Storage (RAM)
    static ArrayList<Car> carList = new ArrayList<>();
    static ArrayList<Customer> customerList = new ArrayList<>();
    static ArrayList<Rental> rentalList = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    // ==========================================
    // THE MAIN METHOD (Program Entry Point)
    // ==========================================
    public static void main(String[] args) {
        
        System.out.println("Booting up Car Rental System...");
        
        // 2. LOAD DATA: Try to load existing data from the text files first
        DataHandler.loadAllData(carList, customerList, rentalList);
        
        // 3. FALLBACK: If the files were totally empty, load the dummy data so the system isn't blank
        if (carList.isEmpty() && customerList.isEmpty()) {
            initializeDummyData();
        }
        
        boolean running = true;
        while (running) {
            System.out.println("\n=== CAR RENTAL MANAGEMENT SYSTEM ===");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. View All Active Rentals");
            System.out.println("4. View All Available Cars");
            System.out.println("5. Add a New Car");
            System.out.println("6. Register a New Customer");
            System.out.println("7. Exit System & Save Data");
            System.out.print("Please select an option (1-7): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the scanner buffer

                switch (choice) {
                    case 1: 
                        rentCar(); 
                        break;
                    case 2: 
                        returnCar(); 
                        break;
                    case 3: 
                        viewAllRentals(); 
                        break;
                    case 4: 
                        viewAvailableCars(); 
                        break;
                    case 5:
                        addNewCar();
                        break;
                    case 6:
                        addNewCustomer();
                        break;
                    case 7: 
                        System.out.println("\nInitiating shutdown sequence...");
                        // 4. SAVE DATA: Save everything back to the text files right before closing
                        DataHandler.saveAllData(carList, customerList, rentalList);
                        System.out.println("Exiting the system. Goodbye!");
                        running = false; 
                        break;
                    default: 
                        System.out.println("Invalid option. Please choose between 1 and 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a number, not text.");
                scanner.nextLine(); // Clears the bad input to prevent an infinite loop
            }
        }
    }

    // ==========================================
    // RENTAL MODULE: CREATE (Rent a Car)
    // ==========================================
    public static void rentCar() {
        System.out.println("\n--- RENT A CAR ---");
        try {
            System.out.print("Enter Customer ID (e.g., C001): ");
            String custId = scanner.nextLine();
            Customer selectedCustomer = null;

            for (Customer c : customerList) {
                if (c.getCustomerId().equalsIgnoreCase(custId)) {
                    selectedCustomer = c;
                    break;
                }
            }

            if (selectedCustomer == null) {
                System.out.println("Error: Customer not found. Please register the customer first.");
                return; 
            }

            System.out.print("Enter Car Plate Number (e.g., ABC1233): ");
            String plate = scanner.nextLine();
            Car selectedCar = null;

            for (Car car : carList) {
                if (car.getPlateNum().equalsIgnoreCase(plate)) {
                    selectedCar = car;
                    break;
                }
            }

            if (selectedCar == null) {
                System.out.println("Error: Car not found in the system.");
                return;
            }

            if (!selectedCar.isAvailable()) {
                System.out.println("Sorry, the car " + selectedCar.getPlateNum() + " is currently rented out.");
                return;
            }

            System.out.print("Enter number of days to rent: ");
            int days = scanner.nextInt(); 
            scanner.nextLine(); // Clear buffer

            if (days <= 0) {
                System.out.println("Error: Rental days must be at least 1.");
                return;
            }

            // Generate a unique Rental ID
            String newRentalId = "R" + (rentalList.size() + 1001); 
            Rental newRental = new Rental(newRentalId, selectedCar, selectedCustomer, days);

            rentalList.add(newRental);
            selectedCar.setAvailable(false); // Update the car's status to unavailable

            System.out.println("\nSUCCESS: Car successfully rented!");
            newRental.printReceipt();

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input. Please enter valid numbers where required.");
            scanner.nextLine(); 
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // ==========================================
    // RENTAL MODULE: UPDATE/DELETE (Return a Car)
    // ==========================================
    public static void returnCar() {
        System.out.println("\n--- RETURN A CAR ---");
        System.out.print("Enter Rental ID (e.g., R1001): ");
        String rentId = scanner.nextLine();

        Rental rentalToReturn = null;
        for (Rental r : rentalList) {
            if (r.getRentalId().equalsIgnoreCase(rentId)) {
                rentalToReturn = r;
                break;
            }
        }

        if (rentalToReturn == null) {
            System.out.println("Error: Rental ID not found.");
            return;
        }

        if (!rentalToReturn.isActive()) {
            System.out.println("Notice: This rental has already been completed and returned.");
            return;
        }

        // Update object states to complete the return
        rentalToReturn.returnCar(); 
        rentalToReturn.getRentedCar().setAvailable(true); 

        System.out.println("SUCCESS: Car " + rentalToReturn.getRentedCar().getPlateNum() + " has been returned.");
        System.out.println("The vehicle is now available for rent again.");
    }

    // ==========================================
    // RENTAL MODULE: READ (View & Filter Rentals)
    // ==========================================
    public static void viewAllRentals() {
        sortRecordsByBrand(); 
        
        System.out.println("\n--- VIEW RENTAL RECORDS ---");
        if (rentalList.isEmpty()) {
            System.out.println("No rentals found in the system.");
            return;
        }

        System.out.println("1. View All Rentals");
        System.out.println("2. View Active Rentals Only (Cars currently out)");
        System.out.println("3. View Completed Rentals Only (Cars returned)");
        System.out.println("4. Search by Customer ID");
        System.out.println("5. Search by Car Plate");
        System.out.println("0. Go Back to Main Menu");
        System.out.print("Please select an option (0-5): ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear the scanner buffer

            switch (choice) {
                case 0:
                    System.out.println("Returning to Main Menu...");
                    break;
                case 1:
                    printFilteredRentals("ALL", "");
                    break;
                case 2:
                    printFilteredRentals("STATUS", "true"); 
                    break;
                case 3:
                    printFilteredRentals("STATUS", "false"); 
                    break;
                case 4:
                    System.out.print("Enter Customer ID (e.g., C001): ");
                    String custId = scanner.nextLine();
                    printFilteredRentals("CUSTOMER", custId);
                    break;
                case 5:
                    System.out.print("Enter Car Plate (e.g., ABC1233): ");
                    String plate = scanner.nextLine();
                    printFilteredRentals("PLATE", plate);
                    break;
                default:
                    System.out.println("Invalid option. Returning to menu.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Please enter a valid number.");
            scanner.nextLine(); 
        }
    }

    // ==========================================
    // HELPER METHOD: PRINTS RENTALS BASED ON FILTER
    // ==========================================
    private static void printFilteredRentals(String filterType, String filterValue) {
        System.out.println("\n--- RENTAL RECORDS ---");
        System.out.printf("%-10s %-15s %-15s %-10s %-15s %-10s\n", 
                          "Rental ID", "Customer ID", "Car Plate", "Days", "Total(RM)", "Status");
        System.out.println("--------------------------------------------------------------------------------");
        
        boolean found = false;
        
        for (Rental r : rentalList) {
            boolean isMatch = false;

            if (filterType.equals("ALL")) {
                isMatch = true;
            } else if (filterType.equals("STATUS")) {
                boolean targetStatus = Boolean.parseBoolean(filterValue);
                if (r.isActive() == targetStatus) {
                    isMatch = true;
                }
            } else if (filterType.equals("CUSTOMER") && r.getRenter().getCustomerId().equalsIgnoreCase(filterValue)) {
                isMatch = true;
            } else if (filterType.equals("PLATE") && r.getRentedCar().getPlateNum().equalsIgnoreCase(filterValue)) {
                isMatch = true;
            }

            if (isMatch) {
                String status = r.isActive() ? "Active" : "Returned";
                System.out.printf("%-10s %-15s %-15s %-10d %-15.2f %-10s\n", 
                                  r.getRentalId(), r.getRenter().getCustomerId(), 
                                  r.getRentedCar().getPlateNum(), r.getRentalDays(), 
                                  r.getTotalCost(), status);
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No records found matching your search criteria.");
        }
    }

    // ==========================================
    // QUICK VIEW & FILTER FOR AVAILABLE CARS
    // ==========================================
    public static void viewAvailableCars() {
        sortRecordsByBrand(); 
        
        System.out.println("\n--- VIEW AVAILABLE CARS ---");
        System.out.println("1. View All Available Cars");
        System.out.println("2. Filter by Brand");
        System.out.println("3. Filter by Car Type");
        System.out.println("4. Go Back to Main Menu");
        System.out.print("Please select an option (1-4): ");

        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear the scanner buffer

            switch (choice) {
                case 1:
                    printFilteredCars("ALL", "");
                    break;
                    
                case 2:
                    ArrayList<String> brands = new ArrayList<>();
                    for (Car car : carList) {
                        if (car.isAvailable() && !brands.contains(car.getBrand())) {
                            brands.add(car.getBrand());
                        }
                    }
                    
                    if (brands.isEmpty()) {
                        System.out.println("No cars are currently available.");
                        break;
                    }

                    System.out.println("\n--- SELECT A BRAND ---");
                    System.out.println("0. Go Back"); 
                    for (int i = 0; i < brands.size(); i++) {
                        System.out.println((i + 1) + ". " + brands.get(i));
                    }
                    
                    System.out.print("Enter your choice (0-" + brands.size() + "): ");
                    int brandChoice = scanner.nextInt();
                    scanner.nextLine();
                    
                    if (brandChoice == 0) {
                        System.out.println("Returning to menu...");
                        break; 
                    } else if (brandChoice >= 1 && brandChoice <= brands.size()) {
                        String selectedBrand = brands.get(brandChoice - 1); 
                        printFilteredCars("BRAND", selectedBrand);
                    } else {
                        System.out.println("Invalid selection. Returning to menu.");
                    }
                    break;
                    
                case 3:
                    ArrayList<String> categories = new ArrayList<>();
                    for (Car car : carList) {
                        if (car.isAvailable() && !categories.contains(car.getCategory())) {
                            categories.add(car.getCategory());
                        }
                    }
                    
                    if (categories.isEmpty()) {
                        System.out.println("No cars are currently available.");
                        break;
                    }

                    System.out.println("\n--- SELECT A CAR TYPE ---");
                    System.out.println("0. Go Back"); 
                    for (int i = 0; i < categories.size(); i++) {
                        System.out.println((i + 1) + ". " + categories.get(i));
                    }
                    
                    System.out.print("Enter your choice (0-" + categories.size() + "): ");
                    int catChoice = scanner.nextInt();
                    scanner.nextLine();
                    
                    if (catChoice == 0) {
                        System.out.println("Returning to menu...");
                        break; 
                    } else if (catChoice >= 1 && catChoice <= categories.size()) {
                        String selectedCat = categories.get(catChoice - 1);
                        printFilteredCars("TYPE", selectedCat);
                    } else {
                        System.out.println("Invalid selection. Returning to menu.");
                    }
                    break;
                    
                case 4:
                    System.out.println("Returning to Main Menu...");
                    return; 
                    
                default:
                    System.out.println("Invalid option. Returning to Main Menu.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Please enter a valid number.");
            scanner.nextLine(); 
        }
    }

    // ==========================================
    // HELPER METHOD: PRINTS CARS BASED ON FILTER
    // ==========================================
    private static void printFilteredCars(String filterType, String filterValue) {
        System.out.println("\n--- SEARCH RESULTS ---");
        System.out.printf("%-10s %-12s %-15s %-15s %-10s\n", "Plate", "Brand", "Model", "Rate/Day (RM)", "Category");
        System.out.println("----------------------------------------------------------------------");
        
        boolean found = false;
        
        for (Car car : carList) {
            if (!car.isAvailable()) {
                continue; 
            }

            boolean isMatch = false;

            if (filterType.equals("ALL")) {
                isMatch = true;
            } else if (filterType.equals("BRAND") && car.getBrand().equalsIgnoreCase(filterValue)) {
                isMatch = true;
            } else if (filterType.equals("TYPE") && car.getCategory().equalsIgnoreCase(filterValue)) {
                isMatch = true;
            }

            if (isMatch) {
                System.out.printf("%-10s %-12s %-15s %-15.2f %-10s\n", 
                        car.getPlateNum(), car.getBrand(), car.getModel(), car.getDailyrate(), car.getCategory());
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No available cars found matching your criteria.");
        }
    }

    // ==========================================
    // CAR MODULE: ADD NEW CAR
    // ==========================================
    public static void addNewCar() {
        System.out.println("\n--- ADD A NEW CAR ---");
        try {
            System.out.print("Enter Plate Number (e.g., VHA1234): ");
            String plate = scanner.nextLine();
            
            // Check if car already exists
            for (Car c : carList) {
                if (c.getPlateNum().equalsIgnoreCase(plate)) {
                    System.out.println("Error: A car with this plate number already exists.");
                    return;
                }
            }

            System.out.print("Enter Brand (e.g., Toyota): ");
            String brand = scanner.nextLine();
            System.out.print("Enter Model (e.g., Vios): ");
            String model = scanner.nextLine();
            System.out.print("Enter Manufacturing Year: ");
            int year = scanner.nextInt();
            System.out.print("Enter Daily Rental Rate (RM): ");
            double rate = scanner.nextDouble();
            scanner.nextLine(); // Clear buffer
            System.out.print("Enter Transmission (A for Auto, M for Manual): ");
            char trans = scanner.nextLine().toUpperCase().charAt(0);
            System.out.print("Enter Current Mileage (km): ");
            int mileage = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            System.out.print("Enter Category (Economy / SUV / Luxury): ");
            String category = scanner.nextLine();
            System.out.print("Enter Required Deposit (RM): ");
            double deposit = scanner.nextDouble();
            scanner.nextLine(); // Clear buffer

            // Auto-generate Vehicle ID
            int newVehicleId = carList.size() + 1001; 
            
            Car newCar = new Car(newVehicleId, plate, brand, model, year, rate, trans, true, mileage, category, deposit);
            carList.add(newCar);
            System.out.println("\nSUCCESS: " + brand + " " + model + " has been added to the system!");
            
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input format. Please try again.");
            scanner.nextLine(); // Clear bad input
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // ==========================================
    // CUSTOMER MODULE: REGISTER NEW CUSTOMER
    // ==========================================
    public static void addNewCustomer() {
        System.out.println("\n--- REGISTER NEW CUSTOMER ---");
        try {
            System.out.print("Enter Full Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Driving License Number: ");
            String license = scanner.nextLine();
            
            // Basic validation to prevent duplicate licenses
            for (Customer c : customerList) {
                if (c.getDrivingLicense().equalsIgnoreCase(license)) {
                    System.out.println("Error: This driving license is already registered.");
                    return;
                }
            }

            System.out.print("Enter Contact Number: ");
            String phone = scanner.nextLine();
            System.out.print("Enter Email Address: ");
            String email = scanner.nextLine();

            // Auto-generate Customer ID (e.g., C003)
            String newCustId = String.format("C%03d", customerList.size() + 1);
            
            Customer newCustomer = new Customer(newCustId, name, license, phone, email);
            customerList.add(newCustomer);
            System.out.println("\nSUCCESS: Customer registered successfully!");
            System.out.println("Your assigned Customer ID is: " + newCustId);
            
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // ==========================================
    // HELPER METHOD: SORT BY BRAND (A-Z)
    // ==========================================
    private static void sortRecordsByBrand() {
        Collections.sort(carList, new Comparator<Car>() {
            @Override
            public int compare(Car c1, Car c2) {
                return c1.getBrand().compareToIgnoreCase(c2.getBrand());
            }
        });

        Collections.sort(rentalList, new Comparator<Rental>() {
            @Override
            public int compare(Rental r1, Rental r2) {
                return r1.getRentedCar().getBrand().compareToIgnoreCase(r2.getRentedCar().getBrand());
            }
        });
    }

    // ==========================================
    // INITIALIZE DUMMY DATA 
    // ==========================================
    public static void initializeDummyData() {
        carList.add(new Car(101, "ABC1233", "Toyota",     "Vios",          2023, 45.0,  'A', true, 5000, "Economy", 200.0));
        carList.add(new Car(102, "DEF4561", "Honda",      "City",          2023, 48.0,  'A', true, 6000, "Economy", 200.0));
        carList.add(new Car(103, "GHI7894", "Nissan",     "Almera",        2022, 42.0,  'M', true, 8000, "Economy", 200.0));
        carList.add(new Car(104, "JKL0125", "Mitsubishi", "Mirage",        2023, 40.0,  'M', true, 4000, "Economy", 200.0));
        carList.add(new Car(105, "MQL2242", "Toyota",     "Camry",         2024, 60.0,  'A', true, 4500, "Economy", 200.0));
        carList.add(new Car(106, "MDS1116", "Honda",      "Accord",        2021, 46.0,  'A', true, 6000, "Economy", 200.0));
        carList.add(new Car(107, "BJJ3301", "Volkswagen", "Passat",        2021, 53.0,  'A', true, 5000, "Economy", 200.0));
        carList.add(new Car(108, "KQV2226", "Perodua",    "Bezza",         2024, 43.0,  'A', true, 3800, "Economy", 200.0));
        carList.add(new Car(109, "SED5631", "Toyota",     "Altis",         2024, 58.0,  'A', true, 5600, "Economy", 200.0));
        carList.add(new Car(110, "WMN4498", "Nissan",     "Sentra",        2024, 58.0,  'A', true, 4000, "Economy", 200.0));

        // SUV Cars
        carList.add(new Car(201, "MNO3453", "Toyota",     "Fortuner",      2024, 120.0, 'A', true, 2000, "SUV", 500.0));
        carList.add(new Car(202, "PQR6782", "Honda",      "CR-V",          2023, 120.0, 'A', true, 3500, "SUV", 500.0));
        carList.add(new Car(203, "STU9014", "Mitsubishi", "Montero",       2024, 125.0, 'A', true, 1500, "SUV", 500.0));
        carList.add(new Car(204, "VWX2346", "Ford",       "Everest",       2023, 125.0, 'A', true, 4200, "SUV", 500.0));
        carList.add(new Car(205, "BYS9902", "Honda",      "HR-V",          2024, 130.0, 'A', true, 3300, "SUV", 500.0));
        carList.add(new Car(206, "WMS7768", "Honda",      "BR-V",          2023, 125.0, 'A', true, 3400, "SUV", 500.0));
        carList.add(new Car(207, "MNO3454", "Toyota",     "Corolla Cross", 2024, 150.0, 'A', true, 2500, "SUV", 500.0));
        carList.add(new Car(208, "JYB7865", "Toyota",     "Rush",          2024, 145.0, 'A', true, 3000, "SUV", 500.0));
        carList.add(new Car(209, "BHY9803", "Mitsubishi", "Outlander",     2024, 155.0, 'A', true, 2000, "SUV", 500.0));
        carList.add(new Car(210, "VWX2347", "Ford",       "Edge",          2024, 145.0, 'A', true, 3200, "SUV", 500.0));

        // Luxury Cars
        carList.add(new Car(301, "YZA5637", "Mercedes", "C-Class",  2024, 200.0, 'A', true, 1000, "Luxury", 1000.0));
        carList.add(new Car(302, "BCD8904", "BMW",      "3 Series", 2024, 210.0, 'A', true,  800, "Luxury", 1000.0));
        carList.add(new Car(303, "EFG1235", "Audi",     "A4",       2023, 210.0, 'A', true, 2500, "Luxury", 1000.0));
        carList.add(new Car(304, "HIJ4566", "Lexus",    "ES",       2024, 220.0, 'A', true,  500, "Luxury", 1000.0));
        carList.add(new Car(305, "SYH7781", "Audi",     "A8",       2023, 230.0, 'A', true, 1500, "Luxury", 1000.0));
        carList.add(new Car(306, "BJJ9930", "BMW",      "M5",       2024, 280.0, 'A', true, 1600, "Luxury", 1000.0));
        carList.add(new Car(307, "BYY1111", "Mercedes", "S-Class",  2024, 300.0, 'A', true, 1000, "Luxury", 1000.0));
        carList.add(new Car(308, "KJL8888", "Nissan",   "GT-R35",   2022, 280.0, 'A', true, 2500, "Luxury", 1000.0));
        carList.add(new Car(309, "KJL9999", "Nissan",   "GT-R33",   2022, 290.0, 'A', true, 2700, "Luxury", 1000.0));
        carList.add(new Car(310, "UJR6677", "Audi",     "RS7",      2023, 290.0, 'A', true, 1500, "Luxury", 1000.0));
        
        customerList.add(new Customer("C001", "Ali Bin Abu", "L987654", "0123456789", "ali@email.com"));
        customerList.add(new Customer("C002", "Siti Nurhaliza", "L112233", "0198765432", "siti@email.com"));
        
        System.out.println(">> System initialized with default dummy data.");
    }
}