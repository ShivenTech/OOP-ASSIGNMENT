package customer;

// Platinum Customer: 10% Discount
public class PlatinumCustomer extends Customer {
    public PlatinumCustomer(String id, String name, String license, String phone, String email) {
        super(id, name, license, phone, email);
    }

    @Override
    public double getDiscountRate() {
        return 0.10; 
    }

    @Override
    public String getMembershipTier() {
        return "Platinum";
    }
}
