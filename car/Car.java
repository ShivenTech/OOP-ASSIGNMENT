package car;

// 1. THE ABSTRACT SUPERCLASS
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
               int carYears, double dailyrate, char transmission,
               boolean available, int mileage) {
        this.vehicleId    = vehicleId;
        this.plateNum     = plateNum;
        this.brand        = brand;
        this.model        = model;
        this.carYears     = carYears;
        this.dailyrate    = dailyrate;
        this.transmission = transmission;
        this.available    = available;
        this.mileage      = mileage;
    }

    // 2. POLYMORPHISM: Required methods for all vehicle types
    public abstract String getCategory();
    public abstract double getDeposit();

    // Standard Getters and Setters
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
}