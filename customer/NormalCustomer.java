package customer;

// Normal Customer: 0% Discount
public class NormalCustomer extends Customer {
    public NormalCustomer(String id, String name, String license, String phone, String email) {
        super(id, name, license, phone, email); // Calls the superclass constructor
    }

    @Override
    public double getDiscountRate() {
        return 0.0; 
    }

    @Override
    public String getMembershipTier() {
        return "Normal";
    }
}
