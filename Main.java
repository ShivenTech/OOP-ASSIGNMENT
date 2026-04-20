import car.*;
import user.customer.*;
import user.employee.*;
import rental.Rental;
import dataHandler.DataHandler;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

// Note: No 'package' declaration at the top since it's in the root folder!
public class Main {
    
    static ArrayList<Car> carList = new ArrayList<>();
    static ArrayList<Customer> customerList = new ArrayList<>();
    static ArrayList<Rental> rentalList = new ArrayList<>();
    static ArrayList<Employee> employeeList = new ArrayList<>(); 
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Booting up Car Rental System...");
        DataHandler.loadAllData(carList, customerList, rentalList);
        
        if (carList.isEmpty() && customerList.isEmpty()) {
            initializeDummyData();
        }
        
        initializeEmployees(); 
        
        boolean running = true;
        while (running) {
            System.out.println("\n=== SYSTEM LOGIN ===");
            System.out.println("1. Employee Login (Admin/Staff)");
            System.out.println("2. Customer Login");
            System.out.println("0. Shut Down System & Save Data");
            System.out.print("Please select your portal (0-2): ");
            
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1:
                        System.out.print("Enter Employee ID (e.g., E001): ");
                        String empId = scanner.nextLine();
                        System.out.print("Enter Password: ");
                        String pass = scanner.nextLine();
                        
                        Employee loggedInEmp = null;
                        for (Employee e : employeeList) {
                            if (e.getId().equalsIgnoreCase(empId) && e.getPassword().equals(pass)) {
                                loggedInEmp = e; break;
                            }
                        }
                        
                        if (loggedInEmp != null) {
                            System.out.println("\nLogin Successful. Role: " + loggedInEmp.getRole());
                            if (loggedInEmp.getRole().equals("Admin")) adminMenu(loggedInEmp);
                            else staffMenu(loggedInEmp);
                        } else { System.out.println("Error: Invalid ID or Password."); }
                        break;
                        
                    case 2:
                        System.out.print("Enter your Customer ID (e.g., C001): ");
                        String custId = scanner.nextLine();
                        Customer loggedInUser = null;
                        
                        for (Customer c : customerList) {
                            if (c.getId().equalsIgnoreCase(custId)) { loggedInUser = c; break; }
                        }
                        
                        if (loggedInUser != null) {
                            System.out.println("\nWelcome back, " + loggedInUser.getName() + "!");
                            customerMenu(loggedInUser);
                        } else { System.out.println("Error: Customer ID not found."); }
                        break;
                    case 0:
                        System.out.println("\nInitiating shutdown sequence...");
                        DataHandler.saveAllData(carList, customerList, rentalList);
                        System.out.println("Exiting the system. Goodbye!");
                        running = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error: Invalid input."); scanner.nextLine(); }
        }
    }

    public static void adminMenu(Employee admin) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- ADMIN DASHBOARD (" + admin.getName() + ") ---");
            System.out.println("1. View All Active Rentals");
            System.out.println("2. View All Available Cars");
            System.out.println("3. Add a New Car");
            System.out.println("4. Register a New Customer");
            System.out.println("5. View All Registered Customers");
            System.out.println("0. Log Out to Main Menu");
            System.out.print("Select an option (0-5): ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: viewAllRentals(); break;
                    case 2: viewAvailableCars(); break;
                    case 3: addNewCar(); break;
                    case 4: addNewCustomer(); break;
                    case 5: viewAllCustomers(); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void staffMenu(Employee staff) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- STAFF DASHBOARD (" + staff.getName() + ") ---");
            System.out.println("1. View All Active Rentals");
            System.out.println("2. View All Available Cars");
            System.out.println("3. View All Registered Customers");
            System.out.println("0. Log Out to Main Menu");
            System.out.println("(Note: Staff cannot add new inventory.)");
            System.out.print("Select an option (0-3): ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: viewAllRentals(); break;
                    case 2: viewAvailableCars(); break;
                    case 3: viewAllCustomers(); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void customerMenu(Customer activeCustomer) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- CUSTOMER DASHBOARD (" + activeCustomer.getMembershipTier() + ") ---");
            System.out.println("1. View Available Cars");
            System.out.println("2. Rent a Car");
            System.out.println("3. Return a Car");
            System.out.println("4. View My Rental History");
            System.out.println("0. Log Out");
            System.out.print("Select an option (0-4): ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: viewAvailableCars(); break;
                    case 2: rentCar(activeCustomer); break;
                    case 3: returnCar(activeCustomer); break;
                    case 4: printFilteredRentals("CUSTOMER", activeCustomer.getId()); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void rentCar(Customer activeCustomer) {
        System.out.println("\n--- RENT A CAR ---");
        try {
            System.out.print("Enter Car Plate Number you wish to rent: ");
            String plate = scanner.nextLine();
            Car selectedCar = null;
            for (Car car : carList) if (car.getPlateNum().equalsIgnoreCase(plate)) { selectedCar = car; break; }
            
            if (selectedCar == null) { System.out.println("Error: Car not found."); return; }
            if (!selectedCar.isAvailable()) { System.out.println("Sorry, car is rented out."); return; }
            
            System.out.print("Enter number of days to rent: ");
            int days = scanner.nextInt(); scanner.nextLine(); 
            if (days <= 0) { System.out.println("Error: Must be at least 1 day."); return; }

            String newRentalId = "R" + (rentalList.size() + 1001); 
            Rental newRental = new Rental(newRentalId, selectedCar, activeCustomer, days);
            rentalList.add(newRental);
            selectedCar.setAvailable(false); 

            System.out.println("\nSUCCESS: Car successfully rented!");
            newRental.printReceipt();
        } catch (Exception e) { System.out.println("Error: Invalid input."); scanner.nextLine(); }
    }

    public static void returnCar(Customer activeCustomer) {
        System.out.println("\n--- RETURN A CAR ---");
        System.out.print("Enter your active Rental ID (e.g., R1001): ");
        String rentId = scanner.nextLine();
        Rental rentalToReturn = null;
        for (Rental r : rentalList) {
            if (r.getRentalId().equalsIgnoreCase(rentId) && r.getRenter().getId().equals(activeCustomer.getId())) {
                rentalToReturn = r; break;
            }
        }
        if (rentalToReturn == null) { System.out.println("Error: Rental ID not found or not yours."); return; }
        if (!rentalToReturn.isActive()) { System.out.println("Notice: Already returned."); return; }

        rentalToReturn.returnCar(); 
        rentalToReturn.getRentedCar().setAvailable(true); 
        System.out.println("SUCCESS: Car " + rentalToReturn.getRentedCar().getPlateNum() + " returned.");
    }

    public static void viewAllRentals() {
        sortRecordsByBrand(); 
        System.out.println("\n--- VIEW RENTAL RECORDS ---");
        if (rentalList.isEmpty()) { System.out.println("No rentals found."); return; }
        System.out.println("1. All Rentals\n2. Active Rentals\n3. Search by Customer ID");
        System.out.print("Select: ");
        try {
            int choice = scanner.nextInt(); scanner.nextLine();
            switch (choice) {
                case 1: printFilteredRentals("ALL", ""); break;
                case 2: printFilteredRentals("STATUS", "true"); break;
                case 3: 
                    System.out.print("Enter Customer ID: ");
                    printFilteredRentals("CUSTOMER", scanner.nextLine()); break;
                default: System.out.println("Invalid.");
            }
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    private static void printFilteredRentals(String filterType, String filterValue) {
        System.out.printf("\n%-10s %-15s %-15s %-10s %-15s %-10s\n", "Rental ID", "Customer ID", "Car Plate", "Days", "Total(RM)", "Status");
        System.out.println("--------------------------------------------------------------------------------");
        boolean found = false;
        for (Rental r : rentalList) {
            boolean isMatch = filterType.equals("ALL") ||
                             (filterType.equals("STATUS") && r.isActive() == Boolean.parseBoolean(filterValue)) ||
                             (filterType.equals("CUSTOMER") && r.getRenter().getId().equalsIgnoreCase(filterValue));
            if (isMatch) {
                System.out.printf("%-10s %-15s %-15s %-10d %-15.2f %-10s\n", r.getRentalId(), r.getRenter().getId(), r.getRentedCar().getPlateNum(), r.getRentalDays(), r.getTotalCost(), r.isActive() ? "Active" : "Returned");
                found = true;
            }
        }
        if (!found) System.out.println("No matching records found.");
    }

    public static void viewAvailableCars() {
        sortRecordsByBrand(); 
        System.out.println("\n--- VIEW AVAILABLE CARS ---");
        System.out.println("1. All Cars\n2. Filter by Brand\n3. Filter by Type\n0. Go Back");
        System.out.print("Select: ");
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
                for (int i = 0; i < filters.size(); i++) System.out.println((i + 1) + ". " + filters.get(i));
                System.out.print("Choice: ");
                int sub = scanner.nextInt(); scanner.nextLine();
                if (sub > 0 && sub <= filters.size()) printFilteredCars((choice == 2) ? "BRAND" : "TYPE", filters.get(sub - 1));
            }
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    private static void printFilteredCars(String filterType, String filterValue) {
        System.out.printf("\n%-10s %-12s %-15s %-15s %-10s\n", "Plate", "Brand", "Model", "Rate/Day (RM)", "Category");
        System.out.println("----------------------------------------------------------------------");
        for (Car car : carList) {
            if (!car.isAvailable()) continue; 
            if (filterType.equals("ALL") || (filterType.equals("BRAND") && car.getBrand().equalsIgnoreCase(filterValue)) || (filterType.equals("TYPE") && car.getCategory().equalsIgnoreCase(filterValue))) {
                System.out.printf("%-10s %-12s %-15s %-15.2f %-10s\n", car.getPlateNum(), car.getBrand(), car.getModel(), car.getDailyrate(), car.getCategory());
            }
        }
    }

    public static void viewAllCustomers() {
        System.out.println("\n--- ALL REGISTERED CUSTOMERS ---");
        if (customerList.isEmpty()) { System.out.println("No customers found."); return; }
        System.out.printf("%-10s %-20s %-15s %-15s %-10s\n", "Cust ID", "Name", "License No", "Phone", "Tier");
        System.out.println("---------------------------------------------------------------------------");
        for (Customer c : customerList) {
            System.out.printf("%-10s %-20s %-15s %-15s %-10s\n", c.getId(), c.getName(), c.getDrivingLicense(), c.getContactNumber(), c.getMembershipTier());
        }
    }

    public static void addNewCar() {
        System.out.println("\n--- ADD A NEW CAR ---");
        try {
            System.out.print("Plate Number: "); String plate = scanner.nextLine();
            System.out.print("Brand: "); String brand = scanner.nextLine();
            System.out.print("Model: "); String model = scanner.nextLine();
            System.out.print("Year: "); int year = scanner.nextInt(); scanner.nextLine();
            System.out.print("Daily Rate: "); double rate = scanner.nextDouble(); scanner.nextLine();
            System.out.print("Transmission (A/M): "); char trans = scanner.nextLine().toUpperCase().charAt(0);
            System.out.print("Mileage: "); int mileage = scanner.nextInt(); scanner.nextLine();
            System.out.println("Type: 1.Sedan 2.SUV 3.MPV 4.Coupe 5.EV 6.Hybrid 7.Sports");
            System.out.print("Choice: "); int type = scanner.nextInt(); scanner.nextLine();

            int id = carList.size() + 1001; 
            Car newCar = switch(type) {
                case 2 -> new SUV(id, plate, brand, model, year, rate, trans, true, mileage);
                case 3 -> new MPV(id, plate, brand, model, year, rate, trans, true, mileage);
                case 4 -> new Coupe(id, plate, brand, model, year, rate, trans, true, mileage);
                case 5 -> new EV(id, plate, brand, model, year, rate, trans, true, mileage);
                case 6 -> new Hybrid(id, plate, brand, model, year, rate, trans, true, mileage);
                case 7 -> new SportsCar(id, plate, brand, model, year, rate, trans, true, mileage);
                default -> new Sedan(id, plate, brand, model, year, rate, trans, true, mileage);
            };
            carList.add(newCar);
            System.out.println("SUCCESS: Car added!");
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    public static void addNewCustomer() {
        System.out.println("\n--- REGISTER NEW CUSTOMER ---");
        try {
            System.out.print("Full Name: "); String name = scanner.nextLine();
            System.out.print("Driving License: "); String license = scanner.nextLine();
            System.out.print("Contact Number: "); String phone = scanner.nextLine();
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.println("Tier: 1.Normal 2.Gold 3.Platinum");
            System.out.print("Choice: "); int tier = scanner.nextInt(); scanner.nextLine();

            String id = String.format("C%03d", customerList.size() + 1);
            Customer newCust = switch(tier) {
                case 2 -> new GoldCustomer(id, name, license, phone, email);
                case 3 -> new PlatinumCustomer(id, name, license, phone, email);
                default -> new NormalCustomer(id, name, license, phone, email);
            };
            customerList.add(newCust);
            System.out.println("SUCCESS: Customer registered! ID: " + id);
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    private static void sortRecordsByBrand() {
        Collections.sort(carList, (c1, c2) -> c1.getBrand().compareToIgnoreCase(c2.getBrand()));
        Collections.sort(rentalList, (r1, r2) -> r1.getRentedCar().getBrand().compareToIgnoreCase(r2.getRentedCar().getBrand()));
    }

    public static void initializeEmployees() {
        employeeList.add(new Admin("E001", "Boss Admin", "011-1111", "boss@company.com", "admin123"));
        employeeList.add(new Staff("E002", "Desk Staff", "012-2222", "staff@company.com", "staff123"));
    }

    public static void initializeDummyData() {
        carList.add(new Sedan(101, "SED1111", "Toyota", "Vios", 2023, 120.0, 'A', true, 5000));
        customerList.add(new NormalCustomer("C001", "Ali Bin Abu", "L987654", "0123456789", "ali@email.com"));
        System.out.println(">> System initialized with dummy data.");
    }
}