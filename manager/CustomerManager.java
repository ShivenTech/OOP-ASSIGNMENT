package manager;

import user.customer.Customer;
import rental.Rental;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerManager {

    public static void addNewCustomer(Scanner scanner, ArrayList<Customer> customerList) {
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

    public static void updateCustomerDetails(Scanner scanner, ArrayList<Customer> customerList) {
        System.out.println("\n--- UPDATE CUSTOMER ---");
        System.out.print("Enter Customer ID (e.g., C001): ");
        String id = scanner.nextLine();
        Customer c = null;
        for (Customer cust : customerList) { if (cust.getId().equalsIgnoreCase(id)) { c = cust; break; } }
        if (c == null) { System.out.println("Error: Customer not found."); return; }

        boolean updating = true;
        while (updating) {
            System.out.println("\nUpdating: " + c.getName() + " (" + c.getId() + ")");
            System.out.println("1. Update Name\n2. Update License\n3. Update Phone\n4. Update Email\n0. Return");
            System.out.print("Select field: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine();
                switch(choice) {
                    case 1: System.out.print("New Name: "); c.setName(scanner.nextLine()); break;
                    case 2: System.out.print("New License: "); c.setDrivingLicense(scanner.nextLine()); break;
                    case 3: System.out.print("New Phone: "); c.setContactNumber(scanner.nextLine()); break;
                    case 4: System.out.print("New Email: "); c.setEmail(scanner.nextLine()); break;
                    case 0: updating = false; break;
                    default: System.out.println("Invalid.");
                }
                if(choice > 0 && choice <= 4) System.out.println("Update successful!");
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void deleteCustomer(Scanner scanner, ArrayList<Customer> customerList, ArrayList<Rental> rentalList) {
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

    public static void overrideCustomerTier(Scanner scanner, ArrayList<Customer> customerList) {
        System.out.println("\n--- OVERRIDE CUSTOMER TIER ---");
        System.out.print("Enter Customer ID to override (e.g., C001): ");
        String id = scanner.nextLine();
        Customer c = null;
        for (Customer cust : customerList) { if (cust.getId().equalsIgnoreCase(id)) { c = cust; break; } }
        if (c == null) { System.out.println("Error: Customer not found."); return; }

        System.out.println("Current Spent: RM " + c.getTotalSpent() + " | Active Tier: " + c.getMembershipTier());
        System.out.println("1. Force Normal\n2. Force Silver\n3. Force Gold\n4. Force Platinum\n5. Reset to Auto-Calculate\n0. Cancel");
        System.out.print("Choice: ");

        try {
            int choice = scanner.nextInt(); scanner.nextLine();
            switch (choice) {
                case 1: c.setManualTierOverride("Normal"); System.out.println("Forced to Normal."); break;
                case 2: c.setManualTierOverride("Silver"); System.out.println("Forced to Silver."); break;
                case 3: c.setManualTierOverride("Gold"); System.out.println("Forced to Gold."); break;
                case 4: c.setManualTierOverride("Platinum"); System.out.println("Forced to Platinum."); break;
                case 5: c.setManualTierOverride("None"); System.out.println("Override removed."); break;
                case 0: return;
                default: System.out.println("Invalid choice.");
            }
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    public static void viewAllCustomers(ArrayList<Customer> customerList) {
        System.out.println("\n--- ALL REGISTERED CUSTOMERS ---");
        if (customerList.isEmpty()) { System.out.println("No customers found."); return; }
        System.out.printf("%-10s %-20s %-15s %-15s %-10s %-15s\n", "Cust ID", "Name", "License No", "Phone", "Tier", "Total Spent(RM)");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Customer c : customerList) {
            System.out.printf("%-10s %-20s %-15s %-15s %-10s %-15.2f\n", c.getId(), c.getName(), c.getDrivingLicense(), c.getContactNumber(), c.getMembershipTier(), c.getTotalSpent());
        }
    }
}