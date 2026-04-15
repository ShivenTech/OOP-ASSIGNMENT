import car.*;
import customer.*;
import rental.Rental;
import dataHandler.DataHandler;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Collections;
import java.util.Comparator;

public class Main {
    
    // Temporary Data Storage (RAM)
    static ArrayList<Car> carList = new ArrayList<>();
    static ArrayList<Customer> customerList = new ArrayList<>();
    static ArrayList<Rental> rentalList = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    // ==========================================
    // THE GATEKEEPER (Login Menu)
    // ==========================================
    public static void main(String[] args) {
        System.out.println("Booting up Car Rental System...");
        DataHandler.loadAllData(carList, customerList, rentalList);
        
        if (carList.isEmpty() && customerList.isEmpty()) {
            initializeDummyData();
        }
        
        boolean running = true;
        while (running) {
            System.out.println("\n=== SYSTEM LOGIN ===");
            System.out.println("1. Employee / Admin Login");
            System.out.println("2. Customer Login");
            System.out.println("0. Shut Down System & Save Data");
            System.out.print("Please select your portal (0-2): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (choice) {
                    case 1:
                        System.out.print("Enter Admin Password: ");
                        String pass = scanner.nextLine();
                        if (pass.equals("admin123")) {
                            adminMenu();
                        } else {
                            System.out.println("Error: Incorrect Password.");
                        }
                        break;
                    case 2:
                        System.out.print("Enter your Customer ID (e.g., C001): ");
                        String custId = scanner.nextLine();
                        Customer loggedInUser = null;
                        
                        for (Customer c : customerList) {
                            if (c.getCustomerId().equalsIgnoreCase(custId)) {
                                loggedInUser = c;
                                break;
                            }
                        }
                        
                        if (loggedInUser != null) {
                            System.out.println("\nWelcome back, " + loggedInUser.getName() + "!");
                            customerMenu(loggedInUser);
                        } else {
                            System.out.println("Error: Customer ID not found. Please ask an Admin to register you.");
                        }
                        break;
                    case 0:
                        System.out.println("\nInitiating shutdown sequence...");
                        DataHandler.saveAllData(carList, customerList, rentalList);
                        System.out.println("Exiting the system. Goodbye!");
                        running = false; 
                        break;
                    default: 
                        System.out.println("Invalid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number.");
                scanner.nextLine(); 
            }
        }
    }

    // ==========================================
    // PORTAL 1: ADMIN MENU
    // ==========================================
    public static void adminMenu() {
        boolean adminActive = true;
        while (adminActive) {
            System.out.println("\n--- ADMIN DASHBOARD ---");
            System.out.println("1. View All Active Rentals");
            System.out.println("2. View All Available Cars");
            System.out.println("3. Add a New Car");
            System.out.println("4. Register a New Customer");
            System.out.println("5. View All Registered Customers");
            System.out.println("0. Log Out to Main Menu");
            System.out.print("Select an option (0-5): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1: viewAllRentals(); break;
                    case 2: viewAvailableCars(); break;
                    case 3: addNewCar(); break;
                    case 4: addNewCustomer(); break;
                    case 5: viewAllCustomers(); break;
                    case 0: 
                        System.out.println("Logging out Admin...");
                        adminActive = false; 
                        break;
                    default: System.out.println("Invalid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number.");
                scanner.nextLine(); 
            }
        }
    }

    // ==========================================
    // PORTAL 2: CUSTOMER MENU
    // ==========================================
    public static void customerMenu(Customer activeCustomer) {
        boolean custActive = true;
        while (custActive) {
            System.out.println("\n--- CUSTOMER DASHBOARD (" + activeCustomer.getMembershipTier() + " Member) ---");
            System.out.println("1. View Available Cars");
            System.out.println("2. Rent a Car");
            System.out.println("3. Return a Car");
            System.out.println("4. View My Rental History");
            System.out.println("0. Log Out");
            System.out.print("Select an option (0-4): ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1: viewAvailableCars(); break;
                    case 2: rentCar(activeCustomer); break; // Pass the active customer!
                    case 3: returnCar(activeCustomer); break;
                    case 4: printFilteredRentals("CUSTOMER", activeCustomer.getCustomerId()); break;
                    case 0: 
                        System.out.println("Logging out...");
                        custActive = false; 
                        break;
                    default: System.out.println("Invalid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number.");
                scanner.nextLine(); 
            }
        }
    }

    // ==========================================
    // RENTAL MODULE: CREATE (Customer Specific)
    // ==========================================
    public static void rentCar(Customer activeCustomer) {
        System.out.println("\n--- RENT A CAR ---");
        try {
            System.out.print("Enter Car Plate Number you wish to rent (e.g., ABC1233): ");
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
            scanner.nextLine(); // CLEAR BUFFER

            if (days <= 0) {
                System.out.println("Error: Rental days must be at least 1.");
                return;
            }

            String newRentalId = "R" + (rentalList.size() + 1001); 
            Rental newRental = new Rental(newRentalId, selectedCar, activeCustomer, days);

            rentalList.add(newRental);
            selectedCar.setAvailable(false); 

            System.out.println("\nSUCCESS: Car successfully rented!");
            newRental.printReceipt();

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input. Please enter valid numbers.");
            scanner.nextLine(); 
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    // ==========================================
    // RENTAL MODULE: UPDATE/DELETE (Customer Specific)
    // ==========================================
    public static void returnCar(Customer activeCustomer) {
        System.out.println("\n--- RETURN A CAR ---");
        System.out.print("Enter your active Rental ID (e.g., R1001): ");
        String rentId = scanner.nextLine();

        Rental rentalToReturn = null;
        for (Rental r : rentalList) {
            if (r.getRentalId().equalsIgnoreCase(rentId) && r.getRenter().getCustomerId().equals(activeCustomer.getCustomerId())) {
                rentalToReturn = r;
                break;
            }
        }

        if (rentalToReturn == null) {
            System.out.println("Error: Rental ID not found or does not belong to you.");
            return;
        }

        if (!rentalToReturn.isActive()) {
            System.out.println("Notice: This rental has already been completed and returned.");
            return;
        }

        rentalToReturn.returnCar(); 
        rentalToReturn.getRentedCar().setAvailable(true); 

        System.out.println("SUCCESS: Car " + rentalToReturn.getRentedCar().getPlateNum() + " has been returned.");
    }

    // ==========================================
    // ALL OTHER MODULES (Unchanged Logic, just clean)
    // ==========================================

    public static void viewAllRentals() {
        sortRecordsByBrand(); 
        System.out.println("\n--- VIEW RENTAL RECORDS ---");
        if (rentalList.isEmpty()) { System.out.println("No rentals found."); return; }

        System.out.println("1. View All Rentals");
        System.out.println("2. View Active Rentals Only");
        System.out.println("3. Search by Customer ID");
        System.out.print("Select an option: ");
        try {
            int choice = scanner.nextInt(); scanner.nextLine();
            switch (choice) {
                case 1: printFilteredRentals("ALL", ""); break;
                case 2: printFilteredRentals("STATUS", "true"); break;
                case 3: 
                    System.out.print("Enter Customer ID: ");
                    printFilteredRentals("CUSTOMER", scanner.nextLine()); break;
                default: System.out.println("Invalid option.");
            }
        } catch (InputMismatchException e) { System.out.println("Error: Invalid number."); scanner.nextLine(); }
    }

    private static void printFilteredRentals(String filterType, String filterValue) {
        System.out.println("\n--- RENTAL RECORDS ---");
        System.out.printf("%-10s %-15s %-15s %-10s %-15s %-10s\n", "Rental ID", "Customer ID", "Car Plate", "Days", "Total(RM)", "Status");
        System.out.println("--------------------------------------------------------------------------------");
        boolean found = false;
        for (Rental r : rentalList) {
            boolean isMatch = filterType.equals("ALL") ||
                             (filterType.equals("STATUS") && r.isActive() == Boolean.parseBoolean(filterValue)) ||
                             (filterType.equals("CUSTOMER") && r.getRenter().getCustomerId().equalsIgnoreCase(filterValue));
            if (isMatch) {
                System.out.printf("%-10s %-15s %-15s %-10d %-15.2f %-10s\n", r.getRentalId(), r.getRenter().getCustomerId(), r.getRentedCar().getPlateNum(), r.getRentalDays(), r.getTotalCost(), r.isActive() ? "Active" : "Returned");
                found = true;
            }
        }
        if (!found) System.out.println("No matching records found.");
    }

    public static void viewAvailableCars() {
        sortRecordsByBrand(); 
        System.out.println("\n--- VIEW AVAILABLE CARS ---");
        System.out.println("1. View All Available Cars\n2. Filter by Brand\n3. Filter by Car Type\n0. Go Back");
        System.out.print("Select an option: ");
        try {
            int choice = scanner.nextInt(); scanner.nextLine();
            if (choice == 1) printFilteredCars("ALL", "");
            else if (choice == 2 || choice == 3) {
                ArrayList<String> filters = new ArrayList<>();
                for (Car car : carList) {
                    if (car.isAvailable()) {
                        String val = (choice == 2) ? car.getBrand() : car.getCategory();
                        if (!filters.contains(val)) filters.add(val);
                    }
                }
                if (filters.isEmpty()) { System.out.println("No cars available."); return; }
                
                System.out.println("\n--- SELECT OPTION ---");
                System.out.println("0. Go Back");
                for (int i = 0; i < filters.size(); i++) System.out.println((i + 1) + ". " + filters.get(i));
                System.out.print("Enter choice: ");
                int subChoice = scanner.nextInt(); scanner.nextLine();
                if (subChoice > 0 && subChoice <= filters.size()) printFilteredCars((choice == 2) ? "BRAND" : "TYPE", filters.get(subChoice - 1));
            }
        } catch (InputMismatchException e) { System.out.println("Error: Invalid number."); scanner.nextLine(); }
    }

    private static void printFilteredCars(String filterType, String filterValue) {
        System.out.println("\n--- SEARCH RESULTS ---");
        System.out.printf("%-10s %-12s %-15s %-15s %-10s\n", "Plate", "Brand", "Model", "Rate/Day (RM)", "Category");
        System.out.println("----------------------------------------------------------------------");
        boolean found = false;
        for (Car car : carList) {
            if (!car.isAvailable()) continue; 
            if (filterType.equals("ALL") || (filterType.equals("BRAND") && car.getBrand().equalsIgnoreCase(filterValue)) || (filterType.equals("TYPE") && car.getCategory().equalsIgnoreCase(filterValue))) {
                System.out.printf("%-10s %-12s %-15s %-15.2f %-10s\n", car.getPlateNum(), car.getBrand(), car.getModel(), car.getDailyrate(), car.getCategory());
                found = true;
            }
        }
        if (!found) System.out.println("No available cars found.");
    }

    public static void addNewCar() {
        System.out.println("\n--- ADD A NEW CAR ---");
        try {
            System.out.print("Enter Plate Number (e.g., VHA1234): "); String plate = scanner.nextLine();
            for (Car c : carList) if (c.getPlateNum().equalsIgnoreCase(plate)) { System.out.println("Error: Car exists."); return; }
            System.out.print("Enter Brand (e.g., Toyota): "); String brand = scanner.nextLine();
            System.out.print("Enter Model (e.g., Vios): "); String model = scanner.nextLine();
            System.out.print("Enter Manufacturing Year: "); int year = scanner.nextInt(); scanner.nextLine();
            System.out.print("Enter Daily Rental Rate (RM): "); double rate = scanner.nextDouble(); scanner.nextLine();
            System.out.print("Enter Transmission (A/M): "); char trans = scanner.nextLine().toUpperCase().charAt(0);
            System.out.print("Enter Mileage (km): "); int mileage = scanner.nextInt(); scanner.nextLine();
            
            System.out.println("Select Car Type: \n1. Sedan \n2. SUV \n3. MPV \n4. Coupe \n5. EV \n6. Hybrid \n7. Sports Car");
            System.out.print("Choice: ");
            int type = scanner.nextInt(); scanner.nextLine();

            int id = carList.size() + 1001; 
            Car newCar = null;
            switch(type) {
                case 1: newCar = new Sedan(id, plate, brand, model, year, rate, trans, true, mileage); break;
                case 2: newCar = new SUV(id, plate, brand, model, year, rate, trans, true, mileage); break;
                case 3: newCar = new MPV(id, plate, brand, model, year, rate, trans, true, mileage); break;
                case 4: newCar = new Coupe(id, plate, brand, model, year, rate, trans, true, mileage); break;
                case 5: newCar = new EV(id, plate, brand, model, year, rate, trans, true, mileage); break;
                case 6: newCar = new Hybrid(id, plate, brand, model, year, rate, trans, true, mileage); break;
                case 7: newCar = new SportsCar(id, plate, brand, model, year, rate, trans, true, mileage); break;
                default: System.out.println("Invalid type. Defaulting to Sedan."); newCar = new Sedan(id, plate, brand, model, year, rate, trans, true, mileage);
            }
            carList.add(newCar);
            System.out.println("\nSUCCESS: " + brand + " " + model + " added!");
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    public static void addNewCustomer() {
        System.out.println("\n--- REGISTER NEW CUSTOMER ---");
        try {
            System.out.print("Enter Full Name: "); String name = scanner.nextLine();
            System.out.print("Enter Driving License Number: "); String license = scanner.nextLine();
            for (Customer c : customerList) if (c.getDrivingLicense().equalsIgnoreCase(license)) { System.out.println("Error: License exists."); return; }
            System.out.print("Enter Contact Number: "); String phone = scanner.nextLine();
            System.out.print("Enter Email Address: "); String email = scanner.nextLine();
            
            System.out.println("Select Membership Tier: \n1. Normal \n2. Gold (10% Off) \n3. Platinum (20% Off)");
            System.out.print("Choice: ");
            int tier = scanner.nextInt(); scanner.nextLine();

            String id = String.format("C%03d", customerList.size() + 1);
            Customer newCust = null;
            switch(tier) {
                case 2: newCust = new GoldCustomer(id, name, license, phone, email); break;
                case 3: newCust = new PlatinumCustomer(id, name, license, phone, email); break;
                default: newCust = new NormalCustomer(id, name, license, phone, email); break;
            }
            customerList.add(newCust);
            System.out.println("\nSUCCESS: Customer registered! ID: " + id);
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    public static void viewAllCustomers() {
        System.out.println("\n--- ALL REGISTERED CUSTOMERS ---");
        if (customerList.isEmpty()) { System.out.println("No customers found."); return; }
        System.out.printf("%-10s %-20s %-15s %-15s %-10s\n", "Cust ID", "Name", "License No", "Phone", "Tier");
        System.out.println("---------------------------------------------------------------------------");
        for (Customer c : customerList) {
            System.out.printf("%-10s %-20s %-15s %-15s %-10s\n", c.getCustomerId(), c.getName(), c.getDrivingLicense(), c.getContactNumber(), c.getMembershipTier());
        }
    }

    private static void sortRecordsByBrand() {
        Collections.sort(carList, new Comparator<Car>() {
            @Override public int compare(Car c1, Car c2) { return c1.getBrand().compareToIgnoreCase(c2.getBrand()); }
        });
        Collections.sort(rentalList, new Comparator<Rental>() {
            @Override public int compare(Rental r1, Rental r2) { return r1.getRentedCar().getBrand().compareToIgnoreCase(r2.getRentedCar().getBrand()); }
        });
    }

    public static void initializeDummyData() {
        
        // 1. SEDAN (10 Cars)
        carList.add(new Sedan(101, "SED1111", "Toyota", "Vios", 2023, 120.0, 'A', true, 5000));
        carList.add(new Sedan(102, "SED2222", "Honda", "City", 2023, 130.0, 'A', true, 6000));
        carList.add(new Sedan(103, "SED3333", "Nissan", "Almera", 2022, 110.0, 'M', true, 8000));
        carList.add(new Sedan(104, "SED4444", "Toyota", "Camry", 2024, 250.0, 'A', true, 4500));
        carList.add(new Sedan(105, "SED5555", "Honda", "Accord", 2021, 240.0, 'A', true, 12000));
        carList.add(new Sedan(106, "SED6666", "Volkswagen", "Passat", 2021, 260.0, 'A', true, 15000));
        carList.add(new Sedan(107, "SED7777", "Perodua", "Bezza", 2024, 90.0, 'A', true, 3800));
        carList.add(new Sedan(108, "SED8888", "Toyota", "Altis", 2024, 180.0, 'A', true, 5600));
        carList.add(new Sedan(109, "SED9999", "Nissan", "Sentra", 2024, 170.0, 'A', true, 4000));
        carList.add(new Sedan(110, "SED1010", "Honda", "Civic", 2023, 200.0, 'A', true, 5500));

        // 2. SUV (10 Cars)
        carList.add(new SUV(201, "SUV1111", "Toyota", "Fortuner", 2024, 300.0, 'A', true, 2000));
        carList.add(new SUV(202, "SUV2222", "Honda", "CR-V", 2023, 280.0, 'A', true, 3500));
        carList.add(new SUV(203, "SUV3333", "Mitsubishi", "Pajero", 2024, 290.0, 'A', true, 1500));
        carList.add(new SUV(204, "SUV4444", "Ford", "Everest", 2023, 310.0, 'A', true, 4200));
        carList.add(new SUV(205, "SUV5555", "Honda", "HR-V", 2024, 220.0, 'A', true, 3300));
        carList.add(new SUV(206, "SUV6666", "Proton", "X70", 2023, 200.0, 'A', true, 3400));
        carList.add(new SUV(207, "SUV7777", "Toyota", "Corolla Cross", 2024, 230.0, 'A', true, 2500));
        carList.add(new SUV(208, "SUV8888", "Mazda", "CX-5", 2024, 250.0, 'A', true, 3000));
        carList.add(new SUV(209, "SUV9999", "Subaru", "Forester", 2024, 260.0, 'A', true, 2000));
        carList.add(new SUV(210, "SUV1010", "Nissan", "X-Trail", 2024, 240.0, 'A', true, 3200));

        // 3. MPV (10 Cars)
        carList.add(new MPV(301, "MPV1111", "Toyota", "Alphard", 2024, 600.0, 'A', true, 1000));
        carList.add(new MPV(302, "MPV2222", "Toyota", "Vellfire", 2024, 600.0, 'A', true, 1200));
        carList.add(new MPV(303, "MPV3333", "Nissan", "Serena", 2023, 350.0, 'A', true, 5000));
        carList.add(new MPV(304, "MPV4444", "Hyundai", "Staria", 2024, 500.0, 'A', true, 2000));
        carList.add(new MPV(305, "MPV5555", "Mitsubishi", "Xpander", 2023, 200.0, 'A', true, 8000));
        carList.add(new MPV(306, "MPV6666", "Perodua", "Alza", 2024, 150.0, 'A', true, 3000));
        carList.add(new MPV(307, "MPV7777", "Proton", "Exora", 2022, 140.0, 'A', true, 15000));
        carList.add(new MPV(308, "MPV8888", "Kia", "Carnival", 2024, 450.0, 'A', true, 4000));
        carList.add(new MPV(309, "MPV9999", "Honda", "Odyssey", 2022, 400.0, 'A', true, 12000));
        carList.add(new MPV(310, "MPV1010", "Toyota", "Innova", 2024, 300.0, 'A', true, 3000));

        // 4. COUPE (10 Cars)
        carList.add(new Coupe(401, "CUP1111", "Ford", "Mustang", 2023, 700.0, 'A', true, 5000));
        carList.add(new Coupe(402, "CUP2222", "Chevrolet", "Camaro", 2022, 650.0, 'A', true, 8000));
        carList.add(new Coupe(403, "CUP3333", "BMW", "4 Series", 2024, 800.0, 'A', true, 2000));
        carList.add(new Coupe(404, "CUP4444", "Mercedes", "C-Class Coupe", 2023, 850.0, 'A', true, 4000));
        carList.add(new Coupe(405, "CUP5555", "Audi", "TT", 2022, 600.0, 'A', true, 10000));
        carList.add(new Coupe(406, "CUP6666", "Jaguar", "F-Type", 2023, 900.0, 'A', true, 3000));
        carList.add(new Coupe(407, "CUP7777", "Lexus", "RC", 2022, 750.0, 'A', true, 6000));
        carList.add(new Coupe(408, "CUP8888", "Subaru", "BRZ", 2024, 500.0, 'M', true, 1500));
        carList.add(new Coupe(409, "CUP9999", "Toyota", "GR86", 2024, 500.0, 'M', true, 1200));
        carList.add(new Coupe(410, "CUP1010", "Nissan", "Z", 2024, 650.0, 'A', true, 2000));

        // 5. EV (10 Cars)
        carList.add(new EV(501, "EVC1111", "Tesla", "Model 3", 2024, 400.0, 'A', true, 2000));
        carList.add(new EV(502, "EVC2222", "Tesla", "Model Y", 2024, 450.0, 'A', true, 1500));
        carList.add(new EV(503, "EVC3333", "Porsche", "Taycan", 2023, 1200.0, 'A', true, 4000));
        carList.add(new EV(504, "EVC4444", "Audi", "e-tron", 2023, 800.0, 'A', true, 5000));
        carList.add(new EV(505, "EVC5555", "Hyundai", "Ioniq 5", 2024, 350.0, 'A', true, 3000));
        carList.add(new EV(506, "EVC6666", "Kia", "EV6", 2024, 360.0, 'A', true, 2500));
        carList.add(new EV(507, "EVC7777", "Nissan", "Leaf", 2022, 250.0, 'A', true, 12000));
        carList.add(new EV(508, "EVC8888", "BMW", "i4", 2024, 600.0, 'A', true, 2000));
        carList.add(new EV(509, "EVC9999", "Mercedes", "EQS", 2024, 1500.0, 'A', true, 1000));
        carList.add(new EV(510, "EVC1010", "BYD", "Atto 3", 2024, 200.0, 'A', true, 1500));

        // 6. HYBRID (10 Cars)
        carList.add(new Hybrid(601, "HYB1111", "Toyota", "Prius", 2023, 200.0, 'A', true, 5000));
        carList.add(new Hybrid(602, "HYB2222", "Honda", "City RS", 2024, 180.0, 'A', true, 3000));
        carList.add(new Hybrid(603, "HYB3333", "Toyota", "Camry Hybrid", 2023, 300.0, 'A', true, 8000));
        carList.add(new Hybrid(604, "HYB4444", "Toyota", "RAV4 Hybrid", 2024, 350.0, 'A', true, 2000));
        carList.add(new Hybrid(605, "HYB5555", "Kia", "Niro", 2023, 250.0, 'A', true, 4000));
        carList.add(new Hybrid(606, "HYB6666", "Nissan", "Kicks e-POWER", 2024, 220.0, 'A', true, 1500));
        carList.add(new Hybrid(607, "HYB7777", "Volvo", "XC90 Recharge", 2024, 800.0, 'A', true, 2000));
        carList.add(new Hybrid(608, "HYB8888", "Porsche", "Cayenne E-Hybrid", 2023, 1500.0, 'A', true, 5000));
        carList.add(new Hybrid(609, "HYB9999", "BMW", "330e", 2024, 500.0, 'A', true, 3000));
        carList.add(new Hybrid(610, "HYB1010", "Lexus", "ES Hybrid", 2024, 600.0, 'A', true, 2500));

        // 7. SPORTS CAR (10 Cars)
        carList.add(new SportsCar(701, "SPC1111", "Porsche", "911 Carrera", 2024, 2000.0, 'A', true, 1000));
        carList.add(new SportsCar(702, "SPC2222", "Nissan", "GT-R", 2023, 1800.0, 'A', true, 2000));
        carList.add(new SportsCar(703, "SPC3333", "Audi", "R8", 2022, 2200.0, 'A', true, 3000));
        carList.add(new SportsCar(704, "SPC4444", "Lamborghini", "Huracan", 2023, 4000.0, 'A', true, 1500));
        carList.add(new SportsCar(705, "SPC5555", "Ferrari", "488 GTB", 2021, 4500.0, 'A', true, 4000));
        carList.add(new SportsCar(706, "SPC6666", "Honda", "NSX", 2022, 2500.0, 'A', true, 2000));
        carList.add(new SportsCar(707, "SPC7777", "Chevrolet", "Corvette", 2024, 1500.0, 'A', true, 1000));
        carList.add(new SportsCar(708, "SPC8888", "Mercedes", "AMG GT", 2023, 2800.0, 'A', true, 2500));
        carList.add(new SportsCar(709, "SPC9999", "Aston Martin", "Vantage", 2022, 3000.0, 'A', true, 5000));
        carList.add(new SportsCar(710, "SPC1010", "Maserati", "MC20", 2024, 3500.0, 'A', true, 1000));
        
        // Dummy Customers
        customerList.add(new NormalCustomer("C001", "Ali Bin Abu", "L987654", "0123456789", "ali@email.com"));
        customerList.add(new GoldCustomer("C002", "Siti Nurhaliza", "L112233", "0198765432", "siti@email.com"));
        customerList.add(new PlatinumCustomer("C003", "Ahmad Albab", "L556677", "0112233445", "ahmad@email.com"));
        
        System.out.println(">> System initialized with 70 vehicles and default customers.");
    }
}