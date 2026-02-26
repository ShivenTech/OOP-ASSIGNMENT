import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Customer {
    // Attributes based on the system description
    private String customerId;
    private String name;
    private String drivingLicense;
    private String contactNumber;
    private String email;

    // Constructor
    public Customer(String customerId, String name, String drivingLicense, String contactNumber, String email) {
        this.customerId = customerId;
        this.name = name;
        this.drivingLicense = drivingLicense;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    // Method to save customer details to a text file
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
            // Saving data in a comma-separated format for easy reading later
            writer.write(customerId + "," + name + "," + drivingLicense + "," + contactNumber + "," + email);
            writer.newLine();
            System.out.println("Customer registration successful. Data saved to file.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving customer data: " + e.getMessage());
        }
    }

    // Getters and Setters (Useful for the 'Rental' or 'Data Handler' classes later)
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDrivingLicense() { return drivingLicense; }
    public void setDrivingLicense(String drivingLicense) { this.drivingLicense = drivingLicense; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

//helloheloehlojhijkjkj
    @Override
    public String toString() {
        return "Customer ID: " + customerId + " | Name: " + name + " | License: " + drivingLicense;
    }

}



System.out.println("Hello world");
