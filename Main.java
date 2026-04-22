import car.*;
import user.customer.Customer;
import user.employee.*;
import rental.Rental;
import dataHandler.DataHandler;
import manager.*; // NEW: Imports all your clean manager classes!

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    
    // The Central Data State
    static ArrayList<Car> carList = new ArrayList<>();
    static ArrayList<Customer> customerList = new ArrayList<>();
    static ArrayList<Rental> rentalList = new ArrayList<>();
    static ArrayList<Employee> employeeList = new ArrayList<>(); 
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Booting up Car Rental System...");
        DataHandler.loadAllData(carList, customerList, rentalList, employeeList);
        
        // Safety Dummy Data Generators
        if (carList.isEmpty()) carList.add(new Sedan(101, "SED1111", "Toyota", "Vios", 2023, 120.0, 'A', true, 5000));
        if (customerList.isEmpty()) customerList.add(new Customer("C001", "Ali Bin Abu", "L987654", "0123456789", "ali@email.com", "pass123", 0.0, "None"));
        if (employeeList.isEmpty()) {
            employeeList.add(new Admin("E001", "Boss Admin", "011-1111", "boss@company.com", "admin123"));
            employeeList.add(new Staff("E002", "Desk Staff", "012-2222", "staff@company.com", "staff123"));
        }
        
        boolean running = true;
        while (running) {
            System.out.println("\n=== SYSTEM LOGIN ===");
            System.out.println("1. Employee Login (Admin/Staff)\n2. Existing Customer Login\n3. New Customer Registration\n0. Shut Down System & Save Data");
            System.out.print("Please select your portal (0-3): ");
            
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1:
                        System.out.print("Enter Employee ID (e.g., E001): "); String empId = scanner.nextLine();
                        System.out.print("Enter Password: "); String pass = scanner.nextLine();
                        Employee loggedInEmp = null;
                        for (Employee e : employeeList) if (e.getId().equalsIgnoreCase(empId) && e.getPassword().equals(pass)) { loggedInEmp = e; break; }
                        
                        if (loggedInEmp != null) {
                            System.out.println("\nLogin Successful. Role: " + loggedInEmp.getRole());
                            if (loggedInEmp.getRole().equals("Admin")) adminDashboard(loggedInEmp);
                            else staffDashboard(loggedInEmp);
                        } else System.out.println("Error: Invalid ID or Password."); 
                        break;
                    case 2:
                        System.out.print("Enter your Customer ID (e.g., C001): "); String custId = scanner.nextLine();
                        System.out.print("Enter Password: "); String custPass = scanner.nextLine();
                        Customer loggedInUser = null;
                        for (Customer c : customerList) if (c.getId().equalsIgnoreCase(custId) && c.getPassword().equals(custPass)) { loggedInUser = c; break; }
                        
                        if (loggedInUser != null) {
                            System.out.println("\nWelcome back, " + loggedInUser.getName() + "!");
                            customerMenu(loggedInUser);
                        } else System.out.println("Error: Invalid ID or Password."); 
                        break;
                    case 3:
                        CustomerManager.addNewCustomer(scanner, customerList); 
                        System.out.println(">> Please use Option 2 to log in with your new credentials."); break;
                    case 0:
                        System.out.println("\nInitiating shutdown sequence...");
                        DataHandler.saveAllData(carList, customerList, rentalList, employeeList);
                        System.out.println("Exiting the system. Goodbye!");
                        running = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error: Invalid input."); scanner.nextLine(); }
        }
    }

    // ==========================================
    // MENU CONTROLLERS
    // ==========================================
    public static void adminDashboard(Employee admin) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- ADMIN DASHBOARD (" + admin.getName() + ") ---");
            System.out.println("1. Manage Cars\n2. Manage Customers\n3. Manage Staff\n0. Log Out");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: manageCarsMenu(); break;
                    case 2: manageCustomerMenu(); break;
                    case 3: manageStaffMenu(admin); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void staffDashboard(Employee staff) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- STAFF DASHBOARD (" + staff.getName() + ") ---");
            System.out.println("1. View All Rentals\n2. View All Available Cars\n3. Manage Customers\n0. Log Out");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: RentalManager.viewAllRentals(scanner, rentalList); break;
                    case 2: CarManager.viewAvailableCars(scanner, carList); break;
                    case 3: staffManageCustomerMenu(); break; 
                    case 0: active = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void manageCarsMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n--- MANAGE CARS ---");
            System.out.println("1. View All Rentals\n2. View All Available Cars\n3. Add New Car\n4. Update Car Details\n0. Return");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: RentalManager.viewAllRentals(scanner, rentalList); break;
                    case 2: CarManager.viewAvailableCars(scanner, carList); break;
                    case 3: CarManager.addNewCar(scanner, carList); break;
                    case 4: CarManager.updateCarDetails(scanner, carList); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void manageCustomerMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n--- ADMIN: MANAGE CUSTOMERS ---");
            System.out.println("1. Register New Customer\n2. Update Customer Details\n3. Delete Customer");
            System.out.println("4. View All Customers\n5. Override Customer Loyalty Tier\n0. Return");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: CustomerManager.addNewCustomer(scanner, customerList); break;
                    case 2: CustomerManager.updateCustomerDetails(scanner, customerList); break;
                    case 3: CustomerManager.deleteCustomer(scanner, customerList, rentalList); break;
                    case 4: CustomerManager.viewAllCustomers(customerList); break;
                    case 5: CustomerManager.overrideCustomerTier(scanner, customerList); break; 
                    case 0: active = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void staffManageCustomerMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n--- STAFF: MANAGE CUSTOMERS ---");
            System.out.println("1. Update Customer Details (Non-Confidential)\n2. Delete Customer\n3. View All Customers\n0. Return");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: CustomerManager.updateCustomerDetails(scanner, customerList); break; 
                    case 2: CustomerManager.deleteCustomer(scanner, customerList, rentalList); break;
                    case 3: CustomerManager.viewAllCustomers(customerList); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void manageStaffMenu(Employee loggedInAdmin) {
        boolean active = true;
        while (active) {
            System.out.println("\n--- MANAGE STAFF ---");
            System.out.println("1. Register New Employee\n2. Update Employee Details\n3. Delete Staff\n4. View All Employees\n0. Return");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: EmployeeManager.addNewEmployeeMenu(scanner, employeeList); break;
                    case 2: EmployeeManager.updateEmployeeDetails(scanner, employeeList); break;
                    case 3: EmployeeManager.deleteEmployee(scanner, employeeList, loggedInAdmin); break;
                    case 4: EmployeeManager.viewAllEmployees(employeeList); break;
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
            System.out.println("Total Lifetime Spend: RM " + activeCustomer.getTotalSpent()); 
            System.out.println("1. View Available Cars\n2. Rent a Car\n3. Return a Car\n4. View My Rental History\n5. Update Password\n0. Log Out");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine(); 
                switch (choice) {
                    case 1: CarManager.viewAvailableCars(scanner, carList); break;
                    case 2: RentalManager.rentCar(scanner, carList, rentalList, activeCustomer); break;
                    case 3: RentalManager.returnCar(scanner, rentalList, activeCustomer); break;
                    case 4: 
                        System.out.println("\n--- YOUR RENTAL HISTORY ---");
                        RentalManager.viewAllRentals(new Scanner(activeCustomer.getId() + "\n0\n"), rentalList); 
                        break;
                    case 5: 
                        System.out.print("Enter New Password: "); activeCustomer.setPassword(scanner.nextLine());
                        System.out.println("SUCCESS: Password updated!"); break;
                    case 0: active = false; break;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }
}