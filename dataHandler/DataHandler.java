package dataHandler;

import car.*;
import user.customer.*; 
import user.employee.*; // NEW: Import employee classes
import rental.Rental;
import java.io.*;
import java.util.ArrayList;

public class DataHandler {

    // UPDATE: Now accepts the employee list!
    public static void saveAllData(ArrayList<Car> cars, ArrayList<Customer> customers, ArrayList<Rental> rentals, ArrayList<Employee> employees) {
        saveCars(cars);
        saveCustomers(customers);
        saveRentals(rentals);
        saveEmployees(employees); // NEW
        System.out.println(">> System data successfully saved to JSON files.");
    }

    private static void saveCars(ArrayList<Car> cars) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("cars.json"))) {
            writer.println("[");
            for (int i = 0; i < cars.size(); i++) {
                Car c = cars.get(i);
                writer.println("  {");
                writer.println("    \"vehicleId\": " + c.getVehicleId() + ",");
                writer.println("    \"plateNum\": \"" + c.getPlateNum() + "\",");
                writer.println("    \"brand\": \"" + c.getBrand() + "\",");
                writer.println("    \"model\": \"" + c.getModel() + "\",");
                writer.println("    \"carYears\": " + c.getCarYears() + ",");
                writer.println("    \"dailyrate\": " + c.getDailyrate() + ",");
                writer.println("    \"transmission\": \"" + c.getTransmission() + "\",");
                writer.println("    \"available\": " + c.isAvailable() + ",");
                writer.println("    \"mileage\": " + c.getMileage() + ",");
                writer.println("    \"category\": \"" + c.getCategory() + "\",");
                writer.println("    \"deposit\": " + c.getDeposit());
                writer.print("  }");
                if (i < cars.size() - 1) writer.println(",");
                else writer.println();
            }
            writer.println("]");
        } catch (IOException e) { System.out.println("Error saving cars: " + e.getMessage()); }
    }

    private static void saveCustomers(ArrayList<Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("customers.json"))) {
            writer.println("[");
            for (int i = 0; i < customers.size(); i++) {
                Customer c = customers.get(i);
                writer.println("  {");
                writer.println("    \"customerId\": \"" + c.getId() + "\","); 
                writer.println("    \"name\": \"" + c.getName() + "\",");
                writer.println("    \"drivingLicense\": \"" + c.getDrivingLicense() + "\",");
                writer.println("    \"contactNumber\": \"" + c.getContactNumber() + "\",");
                writer.println("    \"email\": \"" + c.getEmail() + "\",");
                writer.println("    \"password\": \"" + c.getPassword() + "\",");
                writer.println("    \"totalSpent\": " + c.getTotalSpent() + ","); // NEW
                writer.println("    \"manualTierOverride\": \"" + c.getManualTierOverride() + "\""); // NEW
                writer.print("  }");
                if (i < customers.size() - 1) writer.println(",");
                else writer.println();
            }
            writer.println("]");
        } catch (IOException e) { System.out.println("Error saving customers: " + e.getMessage()); }
    }

    // NEW: Save Employees to JSON
    private static void saveEmployees(ArrayList<Employee> employees) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("employees.json"))) {
            writer.println("[");
            for (int i = 0; i < employees.size(); i++) {
                Employee e = employees.get(i);
                writer.println("  {");
                writer.println("    \"empId\": \"" + e.getId() + "\","); 
                writer.println("    \"name\": \"" + e.getName() + "\",");
                writer.println("    \"contactNumber\": \"" + e.getContactNumber() + "\",");
                writer.println("    \"email\": \"" + e.getEmail() + "\",");
                writer.println("    \"password\": \"" + e.getPassword() + "\",");
                writer.println("    \"role\": \"" + e.getRole() + "\"");
                writer.print("  }");
                if (i < employees.size() - 1) writer.println(",");
                else writer.println();
            }
            writer.println("]");
        } catch (IOException e) { System.out.println("Error saving employees: " + e.getMessage()); }
    }

    private static void saveRentals(ArrayList<Rental> rentals) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("rentals.json"))) {
            writer.println("[");
            for (int i = 0; i < rentals.size(); i++) {
                Rental r = rentals.get(i);
                writer.println("  {");
                writer.println("    \"rentalId\": \"" + r.getRentalId() + "\",");
                writer.println("    \"plateNum\": \"" + r.getRentedCar().getPlateNum() + "\",");
                writer.println("    \"customerId\": \"" + r.getRenter().getId() + "\","); 
                writer.println("    \"rentalDays\": " + r.getRentalDays() + ",");
                writer.println("    \"isActive\": " + r.isActive());
                writer.print("  }");
                if (i < rentals.size() - 1) writer.println(",");
                else writer.println();
            }
            writer.println("]");
        } catch (IOException e) { System.out.println("Error saving rentals: " + e.getMessage()); }
    }

    // UPDATE: Now accepts the employee list!
    public static void loadAllData(ArrayList<Car> cars, ArrayList<Customer> customers, ArrayList<Rental> rentals, ArrayList<Employee> employees) {
        loadCars(cars);
        loadCustomers(customers);
        loadEmployees(employees); // NEW
        loadRentals(rentals, cars, customers);
    }

    private static String parseStr(String line) {
        try { return line.split(":")[1].replace("\"", "").replace(",", "").trim(); } 
        catch (Exception e) { return ""; }
    }
    private static String parseNum(String line) {
        try { return line.split(":")[1].replace(",", "").trim(); } 
        catch (Exception e) { return "0"; }
    }

    private static void loadCars(ArrayList<Car> cars) {
        File file = new File("cars.json");
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int id = 0, year = 0, mileage = 0; double rate = 0.0;
            String plate = "", brand = "", model = "", category = "";
            char trans = 'A'; boolean available = true;

            while ((line = reader.readLine()) != null) {
                if (line.contains("\"vehicleId\":")) id = Integer.parseInt(parseNum(line));
                else if (line.contains("\"plateNum\":")) plate = parseStr(line);
                else if (line.contains("\"brand\":")) brand = parseStr(line);
                else if (line.contains("\"model\":")) model = parseStr(line);
                else if (line.contains("\"carYears\":")) year = Integer.parseInt(parseNum(line));
                else if (line.contains("\"dailyrate\":")) rate = Double.parseDouble(parseNum(line));
                else if (line.contains("\"transmission\":")) trans = parseStr(line).charAt(0);
                else if (line.contains("\"available\":")) available = Boolean.parseBoolean(parseNum(line));
                else if (line.contains("\"mileage\":")) mileage = Integer.parseInt(parseNum(line));
                else if (line.contains("\"category\":")) category = parseStr(line);
                else if (line.contains("}")) {
                    if (!plate.isEmpty()) {
                        if (category.equals("SUV")) cars.add(new SUV(id, plate, brand, model, year, rate, trans, available, mileage));
                        else if (category.equals("MPV")) cars.add(new MPV(id, plate, brand, model, year, rate, trans, available, mileage));
                        else if (category.equals("Coupe")) cars.add(new Coupe(id, plate, brand, model, year, rate, trans, available, mileage));
                        else if (category.equals("EV")) cars.add(new EV(id, plate, brand, model, year, rate, trans, available, mileage));
                        else if (category.equals("Hybrid")) cars.add(new Hybrid(id, plate, brand, model, year, rate, trans, available, mileage));
                        else if (category.equals("Sports Car")) cars.add(new SportsCar(id, plate, brand, model, year, rate, trans, available, mileage));
                        else cars.add(new Sedan(id, plate, brand, model, year, rate, trans, available, mileage));
                        plate = ""; 
                    }
                }
            }
        } catch (Exception e) { System.out.println("No existing car JSON found."); }
    }

    private static void loadCustomers(ArrayList<Customer> customers) {
        File file = new File("customers.json");
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String id = "", name = "", license = "", phone = "", email = "";
            String password = "pass123"; 
            double totalSpent = 0.0;
            String override = "None", legacyTier = "";
            
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"customerId\":")) id = parseStr(line);
                else if (line.contains("\"name\":")) name = parseStr(line);
                else if (line.contains("\"drivingLicense\":")) license = parseStr(line);
                else if (line.contains("\"contactNumber\":")) phone = parseStr(line);
                else if (line.contains("\"email\":")) email = parseStr(line);
                else if (line.contains("\"password\":")) password = parseStr(line);
                else if (line.contains("\"totalSpent\":")) totalSpent = Double.parseDouble(parseNum(line));
                else if (line.contains("\"manualTierOverride\":")) override = parseStr(line);
                else if (line.contains("\"tier\":")) legacyTier = parseStr(line); // Just in case it's the old JSON format
                else if (line.contains("}")) {
                    if (!id.isEmpty()) {
                        // Protect old users: If they had a rank in the old system, lock it in as an override!
                        if (override.equals("None") && !legacyTier.isEmpty() && !legacyTier.equals("Normal")) {
                            override = legacyTier;
                        }
                        customers.add(new Customer(id, name, license, phone, email, password, totalSpent, override));
                        id = ""; legacyTier = ""; override = "None"; totalSpent = 0.0; password = "pass123";
                    }
                }
            }
        } catch (Exception e) { System.out.println("No existing customer JSON found."); }
    }

    // NEW: Load Employees from JSON
    private static void loadEmployees(ArrayList<Employee> employees) {
        File file = new File("employees.json");
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String id = "", name = "", phone = "", email = "", password = "", role = "";
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"empId\":")) id = parseStr(line);
                else if (line.contains("\"name\":")) name = parseStr(line);
                else if (line.contains("\"contactNumber\":")) phone = parseStr(line);
                else if (line.contains("\"email\":")) email = parseStr(line);
                else if (line.contains("\"password\":")) password = parseStr(line);
                else if (line.contains("\"role\":")) role = parseStr(line);
                else if (line.contains("}")) {
                    if (!id.isEmpty()) {
                        // Polymorphism: Rebuild them as the correct subclass!
                        if (role.equals("Admin")) employees.add(new Admin(id, name, phone, email, password));
                        else employees.add(new Staff(id, name, phone, email, password));
                        id = ""; role = "";
                    }
                }
            }
        } catch (Exception e) { System.out.println("No existing employee JSON found."); }
    }

    private static void loadRentals(ArrayList<Rental> rentals, ArrayList<Car> cars, ArrayList<Customer> customers) {
        File file = new File("rentals.json");
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line; String rentId = "", plate = "", custId = ""; int days = 0; boolean isActive = true;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"rentalId\":")) rentId = parseStr(line);
                else if (line.contains("\"plateNum\":")) plate = parseStr(line);
                else if (line.contains("\"customerId\":")) custId = parseStr(line);
                else if (line.contains("\"rentalDays\":")) days = Integer.parseInt(parseNum(line));
                else if (line.contains("\"isActive\":")) isActive = Boolean.parseBoolean(parseNum(line));
                else if (line.contains("}")) {
                    if (!rentId.isEmpty()) {
                        Car linkedCar = null;
                        for (Car c : cars) if (c.getPlateNum().equals(plate)) { linkedCar = c; break; }
                        Customer linkedCust = null;
                        for (Customer c : customers) if (c.getId().equals(custId)) { linkedCust = c; break; } 
                        if (linkedCar != null && linkedCust != null) {
                            Rental r = new Rental(rentId, linkedCar, linkedCust, days);
                            r.setActive(isActive); rentals.add(r);
                        }
                        rentId = ""; 
                    }
                }
            }
        } catch (Exception e) { System.out.println("No existing rental JSON found."); }
    }
}