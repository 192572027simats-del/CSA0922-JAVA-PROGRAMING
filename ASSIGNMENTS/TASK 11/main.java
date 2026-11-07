
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Vehicle class
class Vehicle {
    String regNo;
    String type;
    String userType;
    boolean validMonthlyPass;

    Vehicle(String regNo, String type, String userType,
            boolean validMonthlyPass) {
        this.regNo = regNo;
        this.type = type;
        this.userType = userType;
        this.validMonthlyPass = validMonthlyPass;
    }
}

// Parking ticket class
class ParkingTicket {
    static int ticketCounter = 1000;

    String ticketNo;
    Vehicle vehicle;
    int floor;
    int slot;
    LocalDateTime entryTime;

    ParkingTicket(Vehicle vehicle, int floor, int slot) {
        ticketCounter++;
        this.ticketNo = "T" + ticketCounter;
        this.vehicle = vehicle;
        this.floor = floor;
        this.slot = slot;
        this.entryTime = LocalDateTime.now();
    }

    void displayTicket() {
        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        System.out.println("Ticket: " + ticketNo);
        System.out.println("Vehicle: " + vehicle.regNo);
        System.out.println("Entry Time: " + entryTime.format(formatter));
        System.out.println("Floor: " + floor);
        System.out.println("Slot: " + slot);
    }
}

// Main parking facility class
class ParkingFacility {

    private Vehicle[][] slots;
    private HashMap<String, ParkingTicket> activeTickets;
    private double dailyRevenue;

    ParkingFacility(int[] slotsPerFloor) {
        slots = new Vehicle[slotsPerFloor.length][];

        for (int i = 0; i < slotsPerFloor.length; i++) {
            slots[i] = new Vehicle[slotsPerFloor[i]];
        }

        activeTickets = new HashMap<>();
        dailyRevenue = 0;
    }

    // Check whether a slot position is valid
    boolean isValidSlot(int floor, int slot) {
        return floor >= 0 &&
               floor < slots.length &&
               slot >= 0 &&
               slot < slots[floor].length;
    }

    // Vehicle entry
    ParkingTicket enterVehicle(Vehicle vehicle) {

        if (activeTickets.containsKey(vehicle.regNo)) {
            System.out.println("Duplicate registration rejected: "
                    + vehicle.regNo);
            return null;
        }

        // Find first available slot
        for (int f = 0; f < slots.length; f++) {
            for (int s = 0; s < slots[f].length; s++) {

                if (slots[f][s] == null) {

                    slots[f][s] = vehicle;

                    ParkingTicket ticket =
                        new ParkingTicket(vehicle, f, s);

                    activeTickets.put(vehicle.regNo, ticket);

                    System.out.println("Vehicle parked successfully.");
                    ticket.displayTicket();

                    return ticket;
                }
            }
        }

        System.out.println("Parking Full.");
        return null;
    }

    // Calculate parking fee
    double calculateFee(Vehicle vehicle, int minutes) {

        // Valid monthly pass holders park free
        if ((vehicle.userType.equals("Student") ||
             vehicle.userType.equals("Staff")) &&
             vehicle.validMonthlyPass) {
            return 0;
        }

        // Zero-minute parking has zero fee
        int hours = (minutes + 59) / 60;

        double rate;

        if (vehicle.type.equals("Bike")) {
            rate = 10;
        } else if (vehicle.type.equals("Car")) {
            rate = 30;
        } else {
            rate = 30;
        }

        return hours * rate;
    }

    // Vehicle exit
    void exitVehicle(String regNo, int minutes) {

        if (minutes < 0) {
            System.out.println("Invalid parking duration.");
            return;
        }

        ParkingTicket ticket = activeTickets.get(regNo);

        if (ticket == null) {
            System.out.println("Vehicle or ticket not found.");
            return;
        }

        double fee = calculateFee(ticket.vehicle, minutes);

        // Release the occupied slot
        slots[ticket.floor][ticket.slot] = null;

        // Remove active ticket
        activeTickets.remove(regNo);

        dailyRevenue += fee;

        System.out.println("Vehicle exited: " + regNo);
        System.out.println("Parking Duration: " + minutes + " minutes");
        System.out.println("Parking Fee: Rs " + fee);
        System.out.println("--------------------");
    }

    double getDailyRevenue() {
        return dailyRevenue;
    }
}

// Program execution starts here
public class Main {

    public static void main(String[] args) {

        ParkingFacility parking =
            new ParkingFacility(new int[]{1, 1});

        System.out.println("CAMPUS SMART PARKING SYSTEM");
        System.out.println("--------------------");

        // Test 1: Bike entry
        Vehicle bike = new Vehicle(
            "TN01AB1234", "Bike", "Visitor", false);
        parking.enterVehicle(bike);

        System.out.println("--------------------");

        // Test 2: Car entry
        Vehicle car = new Vehicle(
            "TN02CD5678", "Car", "Visitor", false);
        parking.enterVehicle(car);

        System.out.println("--------------------");

        // Test 3: Full parking
        Vehicle anotherBike = new Vehicle(
            "TN03EF9012", "Bike", "Visitor", false);
        parking.enterVehicle(anotherBike);

        System.out.println("--------------------");

        // Test 4: Duplicate registration
        parking.enterVehicle(bike);

        System.out.println("--------------------");

        // Test 5: Bike exit after 90 minutes
        parking.exitVehicle("TN01AB1234", 90);

        // Test 6: Invalid slot
        System.out.println("Invalid slot test: "
                + parking.isValidSlot(9, 0));

        System.out.println("--------------------");

        // Test 7: Zero-minute parking
        Vehicle zeroVehicle = new Vehicle(
            "TN04GH3456", "Bike", "Visitor", false);
        parking.enterVehicle(zeroVehicle);
        parking.exitVehicle("TN04GH3456", 0);

        System.out.println("--------------------");

        // Test 8: Student with valid monthly pass
        Vehicle student = new Vehicle(
            "TN05JK7890", "Car", "Student", true);
        parking.enterVehicle(student);
        parking.exitVehicle("TN05JK7890", 60);

        System.out.println("--------------------");

        // Test 9: Car exit after 120 minutes
        parking.exitVehicle("TN02CD5678", 120);

        System.out.println("TOTAL DAILY COLLECTION: Rs "
                + parking.getDailyRevenue());
    }
}
