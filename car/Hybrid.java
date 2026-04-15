package car;

class Hybrid extends Car {
    public Hybrid(int id, String plate, String brand, String model, int year, double rate, char trans, boolean avail, int miles) {
        super(id, plate, brand, model, year, rate, trans, avail, miles);
    }
    @Override public String getCategory() { return "Hybrid"; }
    @Override public double getDeposit() { return 300.0; }
}