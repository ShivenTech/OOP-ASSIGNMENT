package user.customer;

import user.User;

public abstract class Customer extends User {
    private String drivingLicense;

    public Customer(String id, String name, String drivingLicense, String contactNumber, String email) {
        super(id, name, contactNumber, email); 
        this.drivingLicense = drivingLicense;
    }

    @Override public String getRole() { return "Customer"; }

    public abstract double getDiscountRate();
    public abstract String getMembershipTier();

    public String getDrivingLicense() { return drivingLicense; }
    public void setDrivingLicense(String drivingLicense) { this.drivingLicense = drivingLicense; }
}