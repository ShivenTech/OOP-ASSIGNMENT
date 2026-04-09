import java.io.*;
import java.util.ArrayList;
import java.util.List;


public abstract class DataHandler {
    protected String fileName;

    public DataHandler(String fileName) {
        this.fileName = fileName;
    }

   
    public abstract void save(List<Car> data) throws IOException;
    public abstract List<Car> load() throws IOException;
}


class CarJsonHandler extends DataHandler {

    public CarJsonHandler(String fileName) {
        super(fileName);
    }

    
    public void save(List<Car> cars) throws IOException {
        // Chapter 2: Using PrintWriter for file output
        PrintWriter writer = new PrintWriter(new FileWriter(fileName));
        
      
        for (int i = 0; i < cars.size(); i++) {
            Car c = cars.get(i);
            writer.println("  {");
            // Chapter 5: String concatenation to build JSON format
            writer.println("    \"plate\": \"" + c.getPlate() + "\",");
            writer.println("    \"model\": \"" + c.getModel() + "\",");
            writer.println("    \"rate\": " + c.getDailyRate() + ",");
            writer.println("    \"isAvailable\": " + c.isAvailable());
            writer.write("  }");
            
            if (i < cars.size() - 1) writer.print(","); // Chapter 3: Array/List logic
            writer.println();
        }
        writer.println("]");
        writer.close();
    }

    
    public List<Car> load() throws IOException {
        List<Car> cars = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        
        
        String plate = "", model = "";
        double rate = 0;
        boolean available = false;

       
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            if (line.contains("\"plate\":")) {
                plate = parseValue(line);
            } else if (line.contains("\"model\":")) {
                model = parseValue(line);
            } else if (line.contains("\"rate\":")) {
                rate = Double.parseDouble(parseValue(line));
            } else if (line.contains("\"isAvailable\":")) {
                available = Boolean.parseBoolean(parseValue(line));
            } else if (line.startsWith("}") || line.startsWith("},")) {
                // Chapter 4: Using Constructor to create a new Object
                cars.add(new Car(plate, model, rate, available));
            }
        }
        reader.close();
        return cars;
    }

  
    private String parseValue(String line) {
        String value = line.split(":")[1].trim();
        return value.replace("\"", "").replace(",", "");
    }
}
