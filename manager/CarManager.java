package manager;

import car.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class CarManager {

    public static void addNewCar(Scanner scanner, ArrayList<Car> carList) {
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
            
            // Classic switch statement (Java 8 Safe!)
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

    public static void updateCarDetails(Scanner scanner, ArrayList<Car> carList) {
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
                System.out.println("1. Update Plate Number\n2. Update Brand\n3. Update Model\n4. Update Car Years");
                System.out.println("5. Update Daily Rate\n6. Update Transmission\n7. Update Status\n8. Update Mileage\n0. Return");
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
                        System.out.println("Status toggled to: " + (carToUpdate.isAvailable() ? "Available" : "Unavailable")); break;
                    case 8: System.out.print("New Mileage (km): "); carToUpdate.setMileage(scanner.nextInt()); scanner.nextLine(); break;
                    case 0: updating = false; break;
                    default: System.out.println("Invalid option.");
                }
                if(choice > 0 && choice <= 8) System.out.println("Update successful!");
            }
        } catch (Exception e) { System.out.println("Error: Invalid input format."); scanner.nextLine(); }
    }

    public static void viewAvailableCars(Scanner scanner, ArrayList<Car> carList) {
        boolean active = true;
        while (active) {
            Collections.sort(carList, (c1, c2) -> c1.getBrand().compareToIgnoreCase(c2.getBrand()));
            System.out.println("\n--- VIEW AVAILABLE CARS ---");
            System.out.println("1. All Cars\n2. Filter by Brand\n3. Filter by Type\n0. Return");
            System.out.print("Select an option: ");
            try {
                int choice = scanner.nextInt(); scanner.nextLine();
                if (choice == 1) { printFilteredCars(carList, "ALL", ""); } 
                else if (choice == 2 || choice == 3) {
                    ArrayList<String> filters = new ArrayList<>();
                    for (Car car : carList) {
                        if (car.isAvailable()) {
                            String val = (choice == 2) ? car.getBrand() : car.getCategory();
                            if (!filters.contains(val)) filters.add(val);
                        }
                    }
                    if (filters.isEmpty()) { System.out.println("No cars available."); continue; }
                    
                    System.out.println("\n--- Select Filter ---");
                    for (int i = 0; i < filters.size(); i++) System.out.println((i + 1) + ". " + filters.get(i));
                    System.out.println("0. Cancel");
                    System.out.print("Choice: ");
                    int sub = scanner.nextInt(); scanner.nextLine();
                    
                    if (sub > 0 && sub <= filters.size()) {
                        printFilteredCars(carList, (choice == 2) ? "BRAND" : "TYPE", filters.get(sub - 1));
                    }
                } else if (choice == 0) { active = false; } 
                else { System.out.println("Invalid option."); }
            } catch (Exception e) { System.out.println("Error."); scanner.nextLine(); }
        }
    }

    private static void printFilteredCars(ArrayList<Car> carList, String filterType, String filterValue) {
        System.out.printf("\n%-10s %-12s %-15s %-15s %-10s\n", "Plate", "Brand", "Model", "Rate/Day (RM)", "Category");
        System.out.println("----------------------------------------------------------------------");
        for (Car car : carList) {
            if (!car.isAvailable()) continue; 
            if (filterType.equals("ALL") || (filterType.equals("BRAND") && car.getBrand().equalsIgnoreCase(filterValue)) || (filterType.equals("TYPE") && car.getCategory().equalsIgnoreCase(filterValue))) {
                System.out.printf("%-10s %-12s %-15s %-15.2f %-10s\n", car.getPlateNum(), car.getBrand(), car.getModel(), car.getDailyrate(), car.getCategory());
            }
        }
    }
}