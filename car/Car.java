package car;

public abstract class Car {
    private int vehicleId;
    private String plateNum;
    private String brand;
    private String model;
    private int carYears;
    private double dailyrate;
    private char transmission;
    private boolean available;
    private int mileage;

    public Car(int vehicleId, String plateNum, String brand, String model,
               int carYears, double dailyrate, char transmission, boolean available, int mileage) {
        this.vehicleId = vehicleId; this.plateNum = plateNum; this.brand = brand;
        this.model = model; this.carYears = carYears; this.dailyrate = dailyrate;
        this.transmission = transmission; this.available = available; this.mileage = mileage;
    }

    public abstract String getCategory();
    public abstract double getDeposit();

    public int getVehicleId() { return vehicleId; }
    public String getPlateNum() { return plateNum; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getCarYears() { return carYears; }
    public double getDailyrate() { return dailyrate; }
    public char getTransmission() { return transmission; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public int getMileage() { return mileage; }
    public void setPlateNum(String plateNum) { this.plateNum = plateNum; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setCarYears(int carYears) { this.carYears = carYears; }
    public void setDailyrate(double dailyrate) { this.dailyrate = dailyrate; }
    public void setTransmission(char transmission) { this.transmission = transmission; }
    public void setMileage(int mileage) { this.mileage = mileage; }
}