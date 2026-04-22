import car.*;
import user.customer.*;
import user.employee.*;
import rental.Rental;
import dataHandler.DataHandler;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Main {
    
    static ArrayList<Car> carList = new ArrayList<>();
    static ArrayList<Customer> customerList = new ArrayList<>();
    static ArrayList<Rental> rentalList = new ArrayList<>();
    static ArrayList<Employee> employeeList = new ArrayList<>(); 
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Booting up Car Rental System...");
        DataHandler.loadAllData(carList, customerList, rentalList, employeeList);
        
        if (carList.isEmpty()) {
            carList.add(new Sedan(101, "SED1111", "Toyota", "Vios", 2023, 120.0, 'A', true, 5000));
            System.out.println(">> System initialized with dummy Car.");
        }
        
        if (customerList.isEmpty()) {
            customerList.add(new Customer("C001", "Ali Bin Abu", "L987654", "0123456789", "ali@email.com", "pass123", 0.0, "None"));
            System.out.println(">> System initialized with dummy Customer.");
        }
        
        if (employeeList.isEmpty()) {
            initializeEmployees(); 
            System.out.println(">> System initialized with dummy Employees.");
        }
        
        boolean running = true;
        while (running) {
            System.out.println("\n=== SYSTEM LOGIN ===");
            System.out.println("1. Employee Login (Admin/Staff)");
            System.out.println("2. Existing Customer Login");
            System.out.println("3. New Customer Registration");
            System.out.println("0. Shut Down System & Save Data");
            System.out.print("Please select your portal (0-3): ");
            
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1:
                        System.out.print("Enter Employee ID (e.g., E001): "); String empId = scanner.nextLine();
                        System.out.print("Enter Password: "); String pass = scanner.nextLine();
                        Employee loggedInEmp = null;
                        for (Employee e : employeeList) {
                            if (e.getId().equalsIgnoreCase(empId) && e.getPassword().equals(pass)) { loggedInEmp = e; break; }
                        }
                        if (loggedInEmp != null) {
                            System.out.println("\nLogin Successful. Role: " + loggedInEmp.getRole());
                            if (loggedInEmp.getRole().equals("Admin")) adminDashboard(loggedInEmp);
                            else staffDashboard(loggedInEmp);
                        } else { System.out.println("Error: Invalid ID or Password."); }
                        break;
                    case 2:
                        System.out.print("Enter your Customer ID (e.g., C001): "); String custId = scanner.nextLine();
                        System.out.print("Enter Password: "); String custPass = scanner.nextLine();
                        Customer loggedInUser = null;
                        for (Customer c : customerList) {
                            if (c.getId().equalsIgnoreCase(custId) && c.getPassword().equals(custPass)) { loggedInUser = c; break; }
                        }
                        if (loggedInUser != null) {
                            System.out.println("\nWelcome back, " + loggedInUser.getName() + "!");
                            customerMenu(loggedInUser);
                        } else { System.out.println("Error: Invalid ID or Password."); }
                        break;
                    case 3:
                        System.out.println("\n--- WELCOME TO PUBLIC REGISTRATION ---");
                        addNewCustomer(); 
                        System.out.println(">> Please use Option 2 to log in with your new ID and Password.");
                        break;
                    case 0:
                        System.out.println("\nInitiating shutdown sequence...");
                        DataHandler.saveAllData(carList, customerList, rentalList, employeeList);
                        System.out.println("Exiting the system. Goodbye!");
                        running = false; break;
                    default: System.out.println("Invalid option. Please Enter Option (0 - 3)");
                }
            } catch (Exception e) { System.out.println("Error: Invalid input. Please try again."); scanner.nextLine(); }
        }
    }

    // ==========================================
    // MAIN DASHBOARDS
    // ==========================================
    public static void adminDashboard(Employee admin) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- ADMIN DASHBOARD (" + admin.getName() + ") ---");
            System.out.println("1. Manage Cars");
            System.out.println("2. Manage Customers");
            System.out.println("3. Manage Staff");
            System.out.println("0. Log Out");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: manageCarsMenu(); break;
                    case 2: manageCustomerMenu(); break;
                    case 3: manageStaffMenu(admin); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option. Please Enter Option (0 - 3)");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void staffDashboard(Employee staff) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- STAFF DASHBOARD (" + staff.getName() + ") ---");
            System.out.println("1. View All Rentals");
            System.out.println("2. View All Available Cars");
            System.out.println("3. Manage Customers");
            System.out.println("0. Log Out");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: viewAllRentals(); break;
                    case 2: viewAvailableCars(); break;
                    case 3: staffManageCustomerMenu(); break; 
                    case 0: active = false; break;
                    default: System.out.println("Invalid option. Please Enter Option (0 - 3)");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    // ==========================================
    // 1. MANAGE CARS MODULE
    // ==========================================
    public static void manageCarsMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n--- MANAGE CARS ---");
            System.out.println("1. View All Rentals");
            System.out.println("2. View All Available Cars");
            System.out.println("3. Add New Car");
            System.out.println("4. Update Car Details");
            System.out.println("0. Return");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: viewAllRentals(); break;
                    case 2: viewAvailableCars(); break;
                    case 3: addNewCar(); break;
                    case 4: updateCarDetails(); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option. Please Enter Option (0 - 4)");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void updateCarDetails() {
        System.out.println("\n--- UPDATE CAR DETAILS ---");
        System.out.print("Enter Car ID of the car to update (e.g., 101): ");
        try {
            int searchId = scanner.nextInt(); scanner.nextLine(); 
            Car carToUpdate = null;
            for (Car c : carList) { if (c.getVehicleId() == searchId) { carToUpdate = c; break; } }
            if (carToUpdate == null) { System.out.println("Error: Car ID not found."); return; }

            boolean updating = true;
            while (updating) {
                System.out.println("\nUpdating: " + carToUpdate.getBrand() + " " + carToUpdate.getModel() + " (ID: " + carToUpdate.getVehicleId() + ")");
                System.out.println("1. Update Plate Number");
                System.out.println("2. Update Brand");
                System.out.println("3. Update Model");
                System.out.println("4. Update Car Years");
                System.out.println("5. Update Daily Rate");
                System.out.println("6. Update Transmission");
                System.out.println("7. Update Status (Available/Unavailable)");
                System.out.println("8. Update Mileage");
                System.out.println("0. Return");
                System.out.print("Select field to update: ");
                
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch(choice) {
                    case 1: System.out.print("New Plate Number: "); carToUpdate.setPlateNum(scanner.nextLine()); break;
                    case 2: System.out.print("New Brand: "); carToUpdate.setBrand(scanner.nextLine()); break;
                    case 3: System.out.print("New Model: "); carToUpdate.setModel(scanner.nextLine()); break;
                    case 4: System.out.print("New Car Year: "); carToUpdate.setCarYears(scanner.nextInt()); scanner.nextLine(); break;
                    case 5: System.out.print("New Daily Rate (RM): "); carToUpdate.setDailyrate(scanner.nextDouble()); scanner.nextLine(); break;
                    case 6: System.out.print("New Transmission (A/M): "); carToUpdate.setTransmission(scanner.nextLine().toUpperCase().charAt(0)); break;
                    case 7: 
                        carToUpdate.setAvailable(!carToUpdate.isAvailable()); 
                        System.out.println("Status toggled to: " + (carToUpdate.isAvailable() ? "Available" : "Unavailable")); 
                        break;
                    case 8: System.out.print("New Mileage (km): "); carToUpdate.setMileage(scanner.nextInt()); scanner.nextLine(); break;
                    case 0: updating = false; break;
                    default: System.out.println("Invalid option. Please Enter Option (0 - 8)");
                }
                if(choice > 0 && choice <= 8) System.out.println("Update successful!");
            }
        } catch (Exception e) { System.out.println("Error: Invalid input format."); scanner.nextLine(); }
    }

    // ==========================================
    // 2. MANAGE CUSTOMER MODULE (ADMIN)
    // ==========================================
    public static void manageCustomerMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n--- ADMIN: MANAGE CUSTOMERS ---");
            System.out.println("1. Register New Customer");
            System.out.println("2. Update Customer Details");
            System.out.println("3. Delete Customer");
            System.out.println("4. View All Customers");
            System.out.println("5. Override Customer Loyalty Tier (Bypass)"); 
            System.out.println("0. Return");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: addNewCustomer(); break;
                    case 2: updateCustomerDetails(); break;
                    case 3: deleteCustomer(); break;
                    case 4: viewAllCustomers(); break;
                    case 5: overrideCustomerTier(); break; 
                    case 0: active = false; break;
                    default: System.out.println("Invalid option. Please Enter Option (0 - 5)");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    // ==========================================
    // 2B. MANAGE CUSTOMER MODULE (RESTRICTED STAFF)
    // ==========================================
    public static void staffManageCustomerMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n--- STAFF: MANAGE CUSTOMERS ---");
            System.out.println("1. Update Customer Details (Non-Confidential)");
            System.out.println("2. View All Customers");
            System.out.println("0. Return");
            System.out.println("(Note: Staff cannot register accounts or alter tiers/passwords.)");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: updateCustomerDetails(); break; 
                    case 2: viewAllCustomers(); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option. Please Enter Option (0 - 2)");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    // ==========================================
    // CUSTOMER CRUD & OVERRIDE
    // ==========================================
    public static void updateCustomerDetails() {
        System.out.println("\n--- UPDATE CUSTOMER ---");
        System.out.print("Enter Customer ID (e.g., C001): ");
        String id = scanner.nextLine();
        Customer c = null;
        for (Customer cust : customerList) { if (cust.getId().equalsIgnoreCase(id)) { c = cust; break; } }
        if (c == null) { System.out.println("Error: Customer not found."); return; }

        boolean updating = true;
        while (updating) {
            System.out.println("\nUpdating: " + c.getName() + " (" + c.getId() + ")");
            System.out.println("1. Update Name");
            System.out.println("2. Update Driving License");
            System.out.println("3. Update Contact Number");
            System.out.println("4. Update Email");
            System.out.println("0. Return");
            System.out.print("Select field: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine();
                switch(choice) {
                    case 1: System.out.print("New Name: "); c.setName(scanner.nextLine()); break;
                    case 2: System.out.print("New License: "); c.setDrivingLicense(scanner.nextLine()); break;
                    case 3: System.out.print("New Phone: "); c.setContactNumber(scanner.nextLine()); break;
                    case 4: System.out.print("New Email: "); c.setEmail(scanner.nextLine()); break;
                    case 0: updating = false; break;
                    default: System.out.println("Invalid. Please Enter Option (0 - 4)");
                }
                if(choice > 0 && choice <= 4) System.out.println("Update successful!");
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void deleteCustomer() {
        System.out.println("\n--- DELETE CUSTOMER ---");
        System.out.print("Enter Customer ID to delete: ");
        String id = scanner.nextLine();
        Customer toDelete = null;
        for (Customer c : customerList) { if (c.getId().equalsIgnoreCase(id)) { toDelete = c; break; } }
        
        if (toDelete == null) { System.out.println("Error: Customer not found."); return; }
        
        for (Rental r : rentalList) {
            if (r.getRenter().getId().equals(toDelete.getId()) && r.isActive()) {
                System.out.println("DENIED: Cannot delete customer. They currently have an active unreturned car.");
                return;
            }
        }
        customerList.remove(toDelete);
        System.out.println("SUCCESS: Customer " + toDelete.getName() + " deleted.");
    }

    public static void overrideCustomerTier() {
        System.out.println("\n--- OVERRIDE CUSTOMER TIER ---");
        System.out.print("Enter Customer ID to override (e.g., C001): ");
        String id = scanner.nextLine();
        Customer c = null;
        for (Customer cust : customerList) { if (cust.getId().equalsIgnoreCase(id)) { c = cust; break; } }
        
        if (c == null) { System.out.println("Error: Customer not found."); return; }

        System.out.println("Current Total Spent: RM " + c.getTotalSpent());
        System.out.println("Current Active Tier: " + c.getMembershipTier());
        System.out.println("\nSelect manual override setting:");
        System.out.println("1. Force Normal");
        System.out.println("2. Force Silver");
        System.out.println("3. Force Gold");
        System.out.println("4. Force Platinum");
        System.out.println("5. Reset to Auto-Calculate (Remove Override)");
        System.out.println("0. Cancel");
        System.out.print("Choice: ");

        try {
            int choice = scanner.nextInt(); scanner.nextLine();
            switch (choice) {
                case 1: c.setManualTierOverride("Normal"); System.out.println("Customer permanently forced to Normal."); break;
                case 2: c.setManualTierOverride("Silver"); System.out.println("Customer permanently forced to Silver."); break;
                case 3: c.setManualTierOverride("Gold"); System.out.println("Customer permanently forced to Gold."); break;
                case 4: c.setManualTierOverride("Platinum"); System.out.println("Customer permanently forced to Platinum."); break;
                case 5: c.setManualTierOverride("None"); System.out.println("Override removed. System will auto-calculate tier based on spending."); break;
                case 0: return;
                default: System.out.println("Invalid choice. Please Enter Option (0 - 5)");
            }
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    // ==========================================
    // 3. MANAGE STAFF MODULE
    // ==========================================
    public static void manageStaffMenu(Employee loggedInAdmin) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- MANAGE STAFF ---");
            System.out.println("1. Register New Employee");
            System.out.println("2. Update Employee Details");
            System.out.println("3. Delete Staff");
            System.out.println("4. View All Employees");
            System.out.println("0. Return");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: addNewEmployeeMenu(); break;
                    case 2: updateEmployeeDetails(); break;
                    case 3: deleteEmployee(loggedInAdmin); break;
                    case 4: viewAllEmployees(); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option. Please Enter Option (0 - 4)");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void addNewEmployeeMenu() {
        System.out.println("\n--- REGISTER NEW EMPLOYEE ---");
        System.out.println("1. Staff (Limited Access)");
        System.out.println("2. Admin (Full Access)");
        System.out.println("0. Return");
        System.out.print("Choice: ");
        try {
            int choice = scanner.nextInt(); scanner.nextLine();
            if (choice == 1) addNewEmployee("Staff");
            else if (choice == 2) addNewEmployee("Admin");
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    public static void updateEmployeeDetails() {
        System.out.println("\n--- UPDATE EMPLOYEE ---");
        System.out.print("Enter Employee ID (e.g., E001): ");
        String id = scanner.nextLine();
        Employee emp = null;
        for (Employee e : employeeList) { if (e.getId().equalsIgnoreCase(id)) { emp = e; break; } }
        if (emp == null) { System.out.println("Error: Employee not found."); return; }

        boolean updating = true;
        while (updating) {
            System.out.println("\nUpdating: " + emp.getName() + " (" + emp.getRole() + ")");
            System.out.println("1. Change Name");
            System.out.println("2. Change Phone");
            System.out.println("3. Change Email");
            System.out.println("4. Change Password");
            System.out.println("0. Return");
            System.out.print("Select field: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine();
                switch(choice) {
                    case 1: System.out.print("New Name: "); emp.setName(scanner.nextLine()); break;
                    case 2: System.out.print("New Phone: "); emp.setContactNumber(scanner.nextLine()); break;
                    case 3: System.out.print("New Email: "); emp.setEmail(scanner.nextLine()); break;
                    case 4: System.out.print("New Password: "); emp.setPassword(scanner.nextLine()); break;
                    case 0: updating = false; break;
                    default: System.out.println("Invalid. Please Enter Option (0 - 4)");
                }
                if(choice > 0 && choice <= 4) System.out.println("Update successful!");
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void deleteEmployee(Employee loggedInAdmin) {
        System.out.println("\n--- DELETE EMPLOYEE ---");
        System.out.print("Enter Employee ID to delete: ");
        String id = scanner.nextLine();
        
        if (id.equalsIgnoreCase(loggedInAdmin.getId())) {
            System.out.println("DENIED: You cannot delete your own account while logged in!"); return;
        }

        Employee toDelete = null;
        for (Employee e : employeeList) { if (e.getId().equalsIgnoreCase(id)) { toDelete = e; break; } }
        
        if (toDelete != null) {
            employeeList.remove(toDelete);
            System.out.println("SUCCESS: " + toDelete.getRole() + " " + toDelete.getName() + " deleted.");
        } else { System.out.println("Error: Employee not found."); }
    }

    // ==========================================
    // CREATION METHODS
    // ==========================================
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
            Car newCar;
            
            // CORRECTED: Classic switch statement with assignments and breaks!
            switch(type) {
                case 2: newCar = new SUV(id, plate, brand, model, year, rate, trans, true, mileage); break;
                case 3: newCar = new MPV(id, plate, brand, model, year, rate, trans, true, mileage); break;
                case 4: newCar = new Coupe(id, plate, brand, model, year, rate, trans, true, mileage); break;
                case 5: newCar = new EV(id, plate, brand, model, year, rate, trans, true, mileage); break;
                case 6: newCar = new Hybrid(id, plate, brand, model, year, rate, trans, true, mileage); break;
                case 7: newCar = new SportsCar(id, plate, brand, model, year, rate, trans, true, mileage); break;
                default: newCar = new Sedan(id, plate, brand, model, year, rate, trans, true, mileage); break;
            }
            
            carList.add(newCar);
            System.out.println("SUCCESS: Car added!");
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    public static void addNewCustomer() {
        System.out.println("\n--- REGISTRATION ---");
        try {
            System.out.print("Full Name: "); String name = scanner.nextLine();
            System.out.print("Driving License: "); String license = scanner.nextLine();
            System.out.print("Contact Number: "); String phone = scanner.nextLine();
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.print("Set Password: "); String password = scanner.nextLine();

            String id = String.format("C%03d", customerList.size() + 1);
            
            Customer newCust = new Customer(id, name, license, phone, email, password, 0.0, "None");
            customerList.add(newCust);
            
            System.out.println("SUCCESS: Customer registered! ID: " + id);
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    public static void addNewEmployee(String role) {
        try {
            System.out.print("Full Name: "); String name = scanner.nextLine();
            System.out.print("Contact Number: "); String phone = scanner.nextLine();
            System.out.print("Email: "); String email = scanner.nextLine();
            System.out.print("Set Password: "); String password = scanner.nextLine();

            String id = String.format("E%03d", employeeList.size() + 1);
            Employee newEmp = role.equals("Admin") ? new Admin(id, name, phone, email, password) : new Staff(id, name, phone, email, password);
            employeeList.add(newEmp);
            System.out.println("SUCCESS: " + role + " account created! Login ID: " + id);
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    // ==========================================
    // VIEWING AND RENTAL MODULES
    // ==========================================
    public static void viewAllRentals() {
        boolean active = true;
        while (active) {
            sortRecordsByBrand(); 
            System.out.println("\n--- VIEW RENTAL RECORDS ---");
            if (rentalList.isEmpty()) { 
                System.out.println("No rentals found."); 
                return; 
            }
            System.out.println("1. All Rental History");
            System.out.println("2. Active Rentals");
            System.out.println("3. Past Rentals");
            System.out.println("4. Search by Customer ID");
            System.out.println("5. Search by Car Plate");
            System.out.println("0. Return");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine();
                switch (choice) {
                    case 1: printFilteredRentals("ALL", ""); break;
                    case 2: printFilteredRentals("STATUS", "true"); break;
                    case 3: printFilteredRentals("STATUS", "false"); break;
                    case 4: 
                        System.out.print("Enter Customer ID (e.g., C001): "); 
                        printFilteredRentals("CUSTOMER", scanner.nextLine()); break;
                    case 5: 
                        System.out.print("Enter Car Plate (e.g., ABC1233): "); 
                        printFilteredRentals("PLATE", scanner.nextLine()); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option. Please Enter Option (0 - 5)");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    private static void printFilteredRentals(String filterType, String filterValue) {
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

    public static void viewAvailableCars() {
        boolean active = true;
        while (active) {
            sortRecordsByBrand(); 
            System.out.println("\n--- VIEW AVAILABLE CARS ---");
            System.out.println("1. All Cars\n2. Filter by Brand\n3. Filter by Type\n0. Return");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine();
                if (choice == 1) {
                    printFilteredCars("ALL", "");
                } else if (choice == 2 || choice == 3) {
                    ArrayList<String> filters = new ArrayList<>();
                    for (Car car : carList) {
                        if (car.isAvailable()) {
                            String val = (choice == 2) ? car.getBrand() : car.getCategory();
                            if (!filters.contains(val)) filters.add(val);
                        }
                    }
                    if (filters.isEmpty()) { System.out.println("No cars available."); continue; }
                    
                    System.out.println("\n--- Select Filter ---");
                    System.out.println("0. Cancel");
                    for (int i = 0; i < filters.size(); i++) System.out.println((i + 1) + ". " + filters.get(i));
                    System.out.print("Choice: ");
                    int sub = scanner.nextInt(); scanner.nextLine();
                    
                    if (sub > 0 && sub <= filters.size()) {
                        printFilteredCars((choice == 2) ? "BRAND" : "TYPE", filters.get(sub - 1));
                    }
                } else if (choice == 0) {
                    active = false;
                } else {
                    System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
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
        System.out.printf("%-10s %-20s %-15s %-15s %-10s %-15s\n", "Cust ID", "Name", "License No", "Phone", "Tier", "Total Spent(RM)");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Customer c : customerList) {
            System.out.printf("%-10s %-20s %-15s %-15s %-10s %-15.2f\n", c.getId(), c.getName(), c.getDrivingLicense(), c.getContactNumber(), c.getMembershipTier(), c.getTotalSpent());
        }
    }

    public static void viewAllEmployees() {
        System.out.println("\n--- ALL REGISTERED EMPLOYEES ---");
        System.out.printf("%-10s %-20s %-15s %-25s %-10s\n", "Emp ID", "Name", "Phone", "Email", "Role");
        System.out.println("------------------------------------------------------------------------------------");
        for (Employee e : employeeList) System.out.printf("%-10s %-20s %-15s %-25s %-10s\n", e.getId(), e.getName(), e.getContactNumber(), e.getEmail(), e.getRole());
    }

    // ==========================================
    // CUSTOMER MENU FUNCTIONS
    // ==========================================
    public static void customerMenu(Customer activeCustomer) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- CUSTOMER DASHBOARD (" + activeCustomer.getMembershipTier() + ") ---");
            System.out.println("Total Lifetime Spend: RM " + activeCustomer.getTotalSpent()); 
            System.out.println("1. View Available Cars");
            System.out.println("2. Rent a Car");
            System.out.println("3. Return a Car");
            System.out.println("4. View My Rental History");
            System.out.println("5. Update Password");
            System.out.println("0. Log Out");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: viewAvailableCars(); break;
                    case 2: rentCar(activeCustomer); break;
                    case 3: returnCar(activeCustomer); break;
                    case 4: printFilteredRentals("CUSTOMER", activeCustomer.getId()); break;
                    case 5: 
                        System.out.print("Enter New Password: "); 
                        activeCustomer.setPassword(scanner.nextLine());
                        System.out.println("SUCCESS: Password updated!");
                        break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option. Please Enter Option (0 - 5)");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    // THE 30-DAY LIMIT VALIDATION IS HERE
    public static void rentCar(Customer activeCustomer) {
        System.out.println("\n--- RENT A CAR ---");
        try {
            System.out.print("Enter Car Plate Number you wish to rent: "); String plate = scanner.nextLine();
            Car selectedCar = null;
            for (Car car : carList) if (car.getPlateNum().equalsIgnoreCase(plate)) { selectedCar = car; break; }
            
            if (selectedCar == null) { System.out.println("Error: Car not found."); return; }
            if (!selectedCar.isAvailable()) { System.out.println("Sorry, car is rented out."); return; }
            
            System.out.print("Enter number of days to rent (Max 30 days): "); int days = scanner.nextInt(); scanner.nextLine(); 
            
            // Rejects inputs over 30 days instantly
            if (days <= 0 || days > 30) { 
                System.out.println("DENIED: You can only rent a car for 1 to 30 days."); 
                return; 
            }

            String newRentalId = "R" + (rentalList.size() + 1001); 
            Rental newRental = new Rental(newRentalId, selectedCar, activeCustomer, days);
            
            activeCustomer.addSpending(newRental.getTotalCost());
            
            rentalList.add(newRental);
            selectedCar.setAvailable(false); 

            System.out.println("\nSUCCESS: Car successfully rented!");
            System.out.println("(Your total spending has been updated to count towards your next Tier upgrade!)");
            newRental.printReceipt();
            
        } catch (Exception e) { System.out.println("Error: Invalid input. Number of day(s) cannot exceed 30 days"); scanner.nextLine(); }
    }

    public static void returnCar(Customer activeCustomer) {
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
        System.out.println("SUCCESS: Car " + rentalToReturn.getRentedCar().getPlateNum() + " returned.");
    }

    // ==========================================
    // UTILITIES
    // ==========================================
    private static void sortRecordsByBrand() {
        Collections.sort(carList, (c1, c2) -> c1.getBrand().compareToIgnoreCase(c2.getBrand()));
        Collections.sort(rentalList, (r1, r2) -> r1.getRentedCar().getBrand().compareToIgnoreCase(r2.getRentedCar().getBrand()));
    }

    public static void initializeEmployees() {
        employeeList.add(new Admin("E001", "Boss Admin", "011-1111", "boss@company.com", "admin123"));
        employeeList.add(new Staff("E002", "Desk Staff", "012-2222", "staff@company.com", "staff123"));
    }
}