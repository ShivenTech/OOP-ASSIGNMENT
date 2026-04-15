package car;

class MPV extends Car {
    public MPV(int id, String plate, String brand, String model, int year, double rate, char trans, boolean avail, int miles) {
        super(id, plate, brand, model, year, rate, trans, avail, miles);
    }
    @Override public String getCategory() { return "MPV"; }
    @Override public double getDeposit() { return 400.0; }
}