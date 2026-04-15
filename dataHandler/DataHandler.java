package dataHandler;

import car.Car;
import customer.Customer;
import rental.Rental;

import java.io.*;
import java.util.ArrayList;

public class DataHandler {

    // ==========================================
    // 1. THE "SAVE GAME" METHODS (JSON FORMAT)
    // ==========================================
    public static void saveAllData(ArrayList<Car> cars, ArrayList<Customer> customers, ArrayList<Rental> rentals) {
        saveCars(cars);
        saveCustomers(customers);
        saveRentals(rentals);
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
        } catch (IOException e) {
            System.out.println("Error saving cars: " + e.getMessage());
        }
    }

    private static void saveCustomers(ArrayList<Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("customers.json"))) {
            writer.println("[");
            for (int i = 0; i < customers.size(); i++) {
                Customer c = customers.get(i);
                writer.println("  {");
                writer.println("    \"customerId\": \"" + c.getCustomerId() + "\",");
                writer.println("    \"name\": \"" + c.getName() + "\",");
                writer.println("    \"drivingLicense\": \"" + c.getDrivingLicense() + "\",");
                writer.println("    \"contactNumber\": \"" + c.getContactNumber() + "\",");
                writer.println("    \"email\": \"" + c.getEmail() + "\"");
                writer.print("  }");
                if (i < customers.size() - 1) writer.println(",");
                else writer.println();
            }
            writer.println("]");
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    private static void saveRentals(ArrayList<Rental> rentals) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("rentals.json"))) {
            writer.println("[");
            for (int i = 0; i < rentals.size(); i++) {
                Rental r = rentals.get(i);
                writer.println("  {");
                writer.println("    \"rentalId\": \"" + r.getRentalId() + "\",");
                writer.println("    \"plateNum\": \"" + r.getRentedCar().getPlateNum() + "\",");
                writer.println("    \"customerId\": \"" + r.getRenter().getCustomerId() + "\",");
                writer.println("    \"rentalDays\": " + r.getRentalDays() + ",");
                writer.println("    \"isActive\": " + r.isActive());
                writer.print("  }");
                if (i < rentals.size() - 1) writer.println(",");
                else writer.println();
            }
            writer.println("]");
        } catch (IOException e) {
            System.out.println("Error saving rentals: " + e.getMessage());
        }
    }

    // ==========================================
    // 2. THE "LOAD GAME" METHODS (JSON FORMAT)
    // ==========================================
    public static void loadAllData(ArrayList<Car> cars, ArrayList<Customer> customers, ArrayList<Rental> rentals) {
        loadCars(cars);
        loadCustomers(customers);
        loadRentals(rentals, cars, customers);
    }

    // Tiny helper to strip out the JSON quotes and commas when reading
    private static String parseJsonString(String line) {
        try { return line.split(":")[1].replace("\"", "").replace(",", "").trim(); } 
        catch (Exception e) { return ""; }
    }

    private static String parseJsonNumber(String line) {
        try { return line.split(":")[1].replace(",", "").trim(); } 
        catch (Exception e) { return "0"; }
    }

    private static void loadCars(ArrayList<Car> cars) {
        File file = new File("cars.json");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int id = 0, year = 0, mileage = 0;
            double rate = 0.0, deposit = 0.0;
            String plate = "", brand = "", model = "", category = "";
            char trans = 'A';
            boolean available = true;

            while ((line = reader.readLine()) != null) {
                if (line.contains("\"vehicleId\":")) id = Integer.parseInt(parseJsonNumber(line));
                else if (line.contains("\"plateNum\":")) plate = parseJsonString(line);
                else if (line.contains("\"brand\":")) brand = parseJsonString(line);
                else if (line.contains("\"model\":")) model = parseJsonString(line);
                else if (line.contains("\"carYears\":")) year = Integer.parseInt(parseJsonNumber(line));
                else if (line.contains("\"dailyrate\":")) rate = Double.parseDouble(parseJsonNumber(line));
                else if (line.contains("\"transmission\":")) trans = parseJsonString(line).charAt(0);
                else if (line.contains("\"available\":")) available = Boolean.parseBoolean(parseJsonNumber(line));
                else if (line.contains("\"mileage\":")) mileage = Integer.parseInt(parseJsonNumber(line));
                else if (line.contains("\"category\":")) category = parseJsonString(line);
                else if (line.contains("\"deposit\":")) deposit = Double.parseDouble(parseJsonNumber(line));
                else if (line.contains("}")) {
                    if (!plate.isEmpty()) {
                        cars.add(new Car(id, plate, brand, model, year, rate, trans, available, mileage, category, deposit));
                        plate = ""; 
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("No existing car JSON found. Starting fresh.");
        }
    }

    private static void loadCustomers(ArrayList<Customer> customers) {
        File file = new File("customers.json");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String id = "", name = "", license = "", phone = "", email = "";

            while ((line = reader.readLine()) != null) {
                if (line.contains("\"customerId\":")) id = parseJsonString(line);
                else if (line.contains("\"name\":")) name = parseJsonString(line);
                else if (line.contains("\"drivingLicense\":")) license = parseJsonString(line);
                else if (line.contains("\"contactNumber\":")) phone = parseJsonString(line);
                else if (line.contains("\"email\":")) email = parseJsonString(line);
                else if (line.contains("}")) {
                    if (!id.isEmpty()) {
                        customers.add(new Customer(id, name, license, phone, email));
                        id = ""; 
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No existing customer JSON found. Starting fresh.");
        }
    }

    private static void loadRentals(ArrayList<Rental> rentals, ArrayList<Car> cars, ArrayList<Customer> customers) {
        File file = new File("rentals.json");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String rentId = "", plate = "", custId = "";
            int days = 0;
            boolean isActive = true;

            while ((line = reader.readLine()) != null) {
                if (line.contains("\"rentalId\":")) rentId = parseJsonString(line);
                else if (line.contains("\"plateNum\":")) plate = parseJsonString(line);
                else if (line.contains("\"customerId\":")) custId = parseJsonString(line);
                else if (line.contains("\"rentalDays\":")) days = Integer.parseInt(parseJsonNumber(line));
                else if (line.contains("\"isActive\":")) isActive = Boolean.parseBoolean(parseJsonNumber(line));
                else if (line.contains("}")) {
                    if (!rentId.isEmpty()) {
                        Car linkedCar = null;
                        for (Car c : cars) if (c.getPlateNum().equals(plate)) { linkedCar = c; break; }
                        
                        Customer linkedCust = null;
                        for (Customer c : customers) if (c.getCustomerId().equals(custId)) { linkedCust = c; break; }

                        if (linkedCar != null && linkedCust != null) {
                            Rental r = new Rental(rentId, linkedCar, linkedCust, days);
                            r.setActive(isActive);
                            rentals.add(r);
                        }
                        rentId = ""; 
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("No existing rental JSON found. Starting fresh.");
        }
    }
}