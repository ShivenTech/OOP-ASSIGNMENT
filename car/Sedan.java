package car;

public class Sedan extends Car {
    public Sedan(int id, String plate, String brand, String model, int year, double rate, char trans, boolean avail, int miles) {
        super(id, plate, brand, model, year, rate, trans, avail, miles);
    }
    @Override public String getCategory() { return "Sedan"; }
    @Override public double getDeposit() { return 200.0; }
}