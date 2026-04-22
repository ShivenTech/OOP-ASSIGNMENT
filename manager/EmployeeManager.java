package manager;

import user.employee.*;
import java.util.ArrayList;
import java.util.Scanner;

public class EmployeeManager {

    public static void addNewEmployeeMenu(Scanner scanner, ArrayList<Employee> employeeList) {
        System.out.println("\n--- REGISTER NEW EMPLOYEE ---");
        System.out.println("1. Staff (Limited Access)\n2. Admin (Full Access)\n0. Return");
        System.out.print("Choice: ");
        try {
            int choice = scanner.nextInt(); scanner.nextLine();
            if (choice == 1) addNewEmployee(scanner, employeeList, "Staff");
            else if (choice == 2) addNewEmployee(scanner, employeeList, "Admin");
        } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
    }

    private static void addNewEmployee(Scanner scanner, ArrayList<Employee> employeeList, String role) {
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

    public static void updateEmployeeDetails(Scanner scanner, ArrayList<Employee> employeeList) {
        System.out.println("\n--- UPDATE EMPLOYEE ---");
        System.out.print("Enter Employee ID (e.g., E001): ");
        String id = scanner.nextLine();
        Employee emp = null;
        for (Employee e : employeeList) { if (e.getId().equalsIgnoreCase(id)) { emp = e; break; } }
        if (emp == null) { System.out.println("Error: Employee not found."); return; }

        boolean updating = true;
        while (updating) {
            System.out.println("\nUpdating: " + emp.getName() + " (" + emp.getRole() + ")");
            System.out.println("1. Change Name\n2. Change Phone\n3. Change Email\n4. Change Password\n0. Return");
            System.out.print("Select field: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine();
                switch(choice) {
                    case 1: System.out.print("New Name: "); emp.setName(scanner.nextLine()); break;
                    case 2: System.out.print("New Phone: "); emp.setContactNumber(scanner.nextLine()); break;
                    case 3: System.out.print("New Email: "); emp.setEmail(scanner.nextLine()); break;
                    case 4: System.out.print("New Password: "); emp.setPassword(scanner.nextLine()); break;
                    case 0: updating = false; break;
                    default: System.out.println("Invalid.");
                }
                if(choice > 0 && choice <= 4) System.out.println("Update successful!");
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    public static void deleteEmployee(Scanner scanner, ArrayList<Employee> employeeList, Employee loggedInAdmin) {
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

    public static void viewAllEmployees(ArrayList<Employee> employeeList) {
        System.out.println("\n--- ALL REGISTERED EMPLOYEES ---");
        System.out.printf("%-10s %-20s %-15s %-25s %-10s\n", "Emp ID", "Name", "Phone", "Email", "Role");
        System.out.println("------------------------------------------------------------------------------------");
        for (Employee e : employeeList) System.out.printf("%-10s %-20s %-15s %-25s %-10s\n", e.getId(), e.getName(), e.getContactNumber(), e.getEmail(), e.getRole());
    }
}