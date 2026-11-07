
public class Main {
    public static void main(String[] args) {

        // Creating a vehicle object
        Vehicle vehicle = new Vehicle();

        // Setting values using setters
        vehicle.setRegNo("TN01AB1234");
        vehicle.setVehicleType("Car");

        // Displaying values using getters
        System.out.println("VEHICLE DETAILS");
        System.out.println("Registration: " + vehicle.getRegNo());
        System.out.println("Type: " + vehicle.getVehicleType());

        // Testing validation
        vehicle.setRegNo("");

        System.out.println("After invalid update: "
                + vehicle.getRegNo());

        System.out.println();

        // Wrapper class and autoboxing
        Integer ticketNumber = 1001;
        int number = ticketNumber;

        System.out.println("TICKET DETAILS");
        System.out.println("Ticket Number: " + ticketNumber);
        System.out.println("Unboxed Number: " + number);

        // Integer comparison
        Integer a = 127;
        Integer b = 127;

        Integer x = 128;
        Integer y = 128;

        System.out.println("127 using == : " + (a == b));
        System.out.println("128 using == : " + (x == y));
        System.out.println("128 using equals(): " + x.equals(y));
    }
}

class Vehicle {

    // Private fields
    private String regNo;
    private String vehicleType;

    // Getter for registration number
    public String getRegNo() {
        return regNo;
    }

    // Setter with validation
    public void setRegNo(String regNo) {
        if (regNo != null && !regNo.trim().isEmpty()) {
            this.regNo = regNo;
        } else {
            System.out.println("Invalid registration number!");
        }
    }

    // Getter for vehicle type
    public String getVehicleType() {
        return vehicleType;
    }

    // Setter with validation
    public void setVehicleType(String vehicleType) {
        if (vehicleType != null &&
                (vehicleType.equals("Car") ||
                 vehicleType.equals("TwoWheeler") ||
                 vehicleType.equals("ElectricCar"))) {

            this.vehicleType = vehicleType;

        } else {
            System.out.println("Invalid vehicle type!");
        }
    }
}
