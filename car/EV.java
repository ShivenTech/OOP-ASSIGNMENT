package car;

class EV extends Car {
    public EV(int id, String plate, String brand, String model, int year, double rate, char trans, boolean avail, int miles) {
        super(id, plate, brand, model, year, rate, trans, avail, miles);
    }
    @Override public String getCategory() { return "EV"; }
    @Override public double getDeposit() { return 600.0; } 
}