package car;

class SUV extends Car {
    public SUV(int id, String plate, String brand, String model, int year, double rate, char trans, boolean avail, int miles) {
        super(id, plate, brand, model, year, rate, trans, avail, miles);
    }
    @Override public String getCategory() { return "SUV"; }
    @Override public double getDeposit() { return 500.0; }
}