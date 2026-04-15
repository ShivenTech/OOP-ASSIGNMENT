package customer;

// 1. ABSTRACT SUPERCLASS
public abstract class Customer {
    private String customerId;
    private String name;
    private String drivingLicense;
    private String contactNumber;
    private String email;

    public Customer(String customerId, String name, String drivingLicense, String contactNumber, String email) {
        this.customerId = customerId;
        this.name = name;
        this.drivingLicense = drivingLicense;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    // 2. POLYMORPHISM: This method MUST be implemented by all subclasses
    public abstract double getDiscountRate();
    
    // Abstract method to easily get their membership level as text
    public abstract String getMembershipTier();

    // Standard Getters and Setters
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

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | Name: %s", getMembershipTier(), customerId, name);
    }
}