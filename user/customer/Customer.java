package user.customer;

import user.User;

public class Customer extends User { // Note: No longer 'abstract'!
    private String drivingLicense;
    private double totalSpent;
    private String manualTierOverride; // Holds "None", "Silver", "Gold", or "Platinum"

    public Customer(String id, String name, String drivingLicense, String contactNumber, String email, String password, double totalSpent, String manualTierOverride) {
        super(id, name, contactNumber, email, password);
        this.drivingLicense = drivingLicense;
        this.totalSpent = totalSpent;
        this.manualTierOverride = manualTierOverride;
    }

    @Override public String getRole() { return "Customer"; }

    // Adds money to their profile every time they rent!
    public void addSpending(double amount) {
        this.totalSpent += amount;
    }

    // Dynamic Tier Calculation
    public String getMembershipTier() {
        // 1. Check if the Admin has forced a specific tier
        if (!manualTierOverride.equals("None")) {
            return manualTierOverride;
        }
        // 2. Otherwise, auto-calculate based on spending history
        if (totalSpent >= 100000) return "Platinum";
        if (totalSpent >= 50000) return "Gold";
        if (totalSpent >= 25000) return "Silver";
        return "Normal";
    }

    // Dynamic Discount Calculation
    public double getDiscountRate() {
        return switch(getMembershipTier()) {
            case "Platinum" -> 0.15; // 15%
            case "Gold" -> 0.10;     // 10%
            case "Silver" -> 0.05;   // 5%
            default -> 0.0;          // 0%
        };
    }

    // Getters and Setters
    public String getDrivingLicense() { return drivingLicense; }
    public void setDrivingLicense(String drivingLicense) { this.drivingLicense = drivingLicense; }
    public double getTotalSpent() { return totalSpent; }
    public void setTotalSpent(double totalSpent) { this.totalSpent = totalSpent; }
    public String getManualTierOverride() { return manualTierOverride; }
    public void setManualTierOverride(String manualTierOverride) { this.manualTierOverride = manualTierOverride; }
}