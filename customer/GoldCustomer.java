package customer;

// Gold Customer: 5% Discount
class GoldCustomer extends Customer {
    public GoldCustomer(String id, String name, String license, String phone, String email) {
        super(id, name, license, phone, email);
    }

    @Override
    public double getDiscountRate() {
        return 0.05; 
    }

    @Override
    public String getMembershipTier() {
        return "Gold";
    }
}
