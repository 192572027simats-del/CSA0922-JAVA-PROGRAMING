
public class Main {
    public static void main(String[] args) {

        Vehicle v1 = new Car("TN01AB1234");
        Vehicle v2 = new ElectricCar("TN02CD5678");

        System.out.println("CAR DETAILS");
        v1.displayDetails();
        System.out.println("Parking Fee: Rs " + v1.calculateFee());

        System.out.println();

        System.out.println("ELECTRIC CAR DETAILS");
        v2.displayDetails();
        System.out.println("Parking Fee: Rs " + v2.calculateFee());

        // Interface methods
        ElectricCar ev = (ElectricCar) v2;
        ev.charge(10);
        ev.showChargingInfo();

        ev.insure();

        // Calling static interface method
        System.out.println("Maximum charging rate: "
                + Chargeable.getMaximumRate() + " kW");
    }
}

// Abstract class
abstract class Vehicle {
    String regNo;

    Vehicle(String regNo) {
        this.regNo = regNo;
    }

    // Abstract method
    abstract double calculateFee();

    // Normal method
    void displayDetails() {
        System.out.println("Registration Number: " + regNo);
    }
}

// Interface 1
interface Chargeable {

    void charge(double units);

    // Default method
    default void showChargingInfo() {
        System.out.println("Vehicle supports battery charging.");
    }

    // Static method
    static int getMaximumRate() {
        return 50;
    }
}

// Interface 2
interface Insurable {
    void insure();
}

// Child class
class Car extends Vehicle {

    Car(String regNo) {
        super(regNo);
    }

    @Override
    double calculateFee() {
        return 90.0;
    }
}

// Child class implementing multiple interfaces
class ElectricCar extends Vehicle implements Chargeable, Insurable {

    ElectricCar(String regNo) {
        super(regNo);
    }

    @Override
    double calculateFee() {
        return 90.0;
    }

    @Override
    public void charge(double units) {
        double cost = units * 12;
        System.out.println("Charging Cost: Rs " + cost);
    }

    @Override
    public void insure() {
        System.out.println("Electric car insurance activated.");
    }
}
