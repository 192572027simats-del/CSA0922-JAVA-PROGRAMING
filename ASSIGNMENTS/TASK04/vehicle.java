
public class Vehicle {

    String regNo;
    String vehicleType;

    // No-argument constructor
    Vehicle() {
        this("Unknown", "Unknown");
    }

    // Parameterized constructor
    Vehicle(String regNo) {
        this(regNo, "Car");
    }

    // Overloaded parameterized constructor
    Vehicle(String regNo, String vehicleType) {
        this.regNo = regNo;
        this.vehicleType = vehicleType;
    }

    // Copy constructor
    Vehicle(Vehicle oldVehicle) {
        this(oldVehicle.regNo, oldVehicle.vehicleType);
    }

    // Display vehicle details
    void displayDetails() {
        System.out.println("Registration Number: " + regNo);
        System.out.println("Vehicle Type: " + vehicleType);
        System.out.println("--------------------");
    }
}
