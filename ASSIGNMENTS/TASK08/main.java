
public class Main {
    public static void main(String[] args) {

        // Upcasting: parent references store child objects
        Vehicle v1 = new TwoWheeler("TN01A1");
        Vehicle v2 = new Car("TN02B2");
        Vehicle v3 = new ElectricCar("TN03C3");

        // Runtime polymorphism
        System.out.println(v1 + " -> Rs " + v1.calculateFee(180));
        System.out.println(v2 + " -> Rs " + v2.calculateFee(180));

        // Overloaded method for charging
        System.out.println(v3 + " -> Rs " + v3.calculateFee(180, 10));

        // Testing equals()
        Vehicle v4 = new Car("TN02B2");
        System.out.println(v2.equals(v4));
    }
}

class Vehicle {
    String regNo;
    double hourlyRate;

    Vehicle(String regNo, double hourlyRate) {
        this.regNo = regNo;
        this.hourlyRate = hourlyRate;
    }

    // Calculate billable hours using integer arithmetic
    int billableHours(int minutes) {
        return (minutes + 59) / 60;
    }

    // Method to calculate normal parking fee
    double calculateFee(int minutes) {
        return billableHours(minutes) * hourlyRate;
    }

    // Overloaded method
    double calculateFee(int minutes, double chargingUnits) {
        return calculateFee(minutes);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + regNo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Vehicle)) {
            return false;
        }

        Vehicle other = (Vehicle) obj;
        return regNo.equals(other.regNo);
    }

    @Override
    public int hashCode() {
        return regNo.hashCode();
    }
}

class TwoWheeler extends Vehicle {
    TwoWheeler(String regNo) {
        super(regNo, 10);
    }

    @Override
    double calculateFee(int minutes) {
        return super.calculateFee(minutes);
    }
}

class Car extends Vehicle {
    Car(String regNo) {
        super(regNo, 30);
    }

    @Override
    double calculateFee(int minutes) {
        return super.calculateFee(minutes);
    }
}

class ElectricCar extends Car {
    ElectricCar(String regNo) {
        super(regNo);
    }

    // Overloaded method with charging fee
    @Override
    double calculateFee(int minutes, double chargingUnits) {
        return super.calculateFee(minutes) + (chargingUnits * 12);
    }
}
