package dataHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import customer.Customer; 
import car.Car;          
public abstract class DataHandler {
    protected String fileName;

    public DataHandler(String fileName) {
        this.fileName = fileName;
    }

    // Abstract methods to be implemented by the Json handler
    public abstract void saveCustomers(List<Customer> data, String path) throws IOException;
    public abstract List<Customer> loadCustomers(String path) throws IOException;
}

class CarJsonHandler extends DataHandler {

    public CarJsonHandler(String fileName) {
        super(fileName);
    }

    // Chapter 2: Using throws for Exception Handling
    public void saveCustomers(List<Customer> customers, String path) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(path));
        writer.println("[");
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            writer.println("  {");
            // Chapter 5: String processing to build JSON
            // Syncing with Customer.java getters
            writer.println("    \"id\": \"" + c.getCustomerId() + "\",");
            writer.println("    \"name\": \"" + c.getName() + "\",");
            writer.println("    \"license\": \"" + c.getDrivingLicense() + "\",");
            writer.println("    \"contact\": \"" + c.getContactNumber() + "\",");
            writer.println("    \"email\": \"" + c.getEmail() + "\"");
            writer.print("  }");
            if (i < customers.size() - 1) writer.print(",");
            writer.println();
        }
        writer.println("]");
        writer.close();
    }

    public List<Customer> loadCustomers(String path) throws IOException {
        List<Customer> list = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return list;

        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        String id = "", name = "", license = "", contact = "", email = "";

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            // Chapter 5: Parsing JSON strings
            if (line.contains("\"id\":")) id = parseValue(line);
            else if (line.contains("\"name\":")) name = parseValue(line);
            else if (line.contains("\"license\":")) license = parseValue(line);
            else if (line.contains("\"contact\":")) contact = parseValue(line);
            else if (line.contains("\"email\":")) email = parseValue(line);
            else if (line.startsWith("}") || line.startsWith("},")) {
                // Chapter 4: Object construction
                // Syncing with Customer constructor
                list.add(new Customer(id, name, license, contact, email));
            }
        }
        reader.close();
        return list;
    }

    // Method to save CAR data, syncing with CAR.java getters
    public void saveCars(List<Car> cars) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(fileName));
        writer.println("[");
        for (int i = 0; i < cars.size(); i++) {
            Car c = cars.get(i);
            writer.println("  {");
            writer.println("    \"plate\": \"" + c.getPlateNum() + "\",");
            writer.println("    \"model\": \"" + c.getModel() + "\",");
            writer.println("    \"rate\": " + c.getDailyrate() + ",");
            writer.println("    \"isAvailable\": " + c.isAvailable());
            writer.print("  }");
            if (i < cars.size() - 1) writer.print(",");
            writer.println();
        }
        writer.println("]");
        writer.close();
    }

    private String parseValue(String line) {
        return line.split(":")[1].replace("\"", "").replace(",", "").trim();
    }
}