
import java.util.HashMap;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Duration;

// Parent class
abstract class Vehicle {
    String regNo;
    String userType;
    boolean oneDayPass;
    LocalDateTime passExpiry;

    Vehicle(String regNo, String userType, boolean oneDayPass,
            LocalDateTime entryTime) {
        this.regNo = regNo;
        this.userType = userType;
        this.oneDayPass = oneDayPass;

        if (oneDayPass && userType.equals("Visitor")) {
            passExpiry = entryTime.plusDays(1);
        }
    }

    abstract double calculateFee(LocalDateTime entry,
                                 LocalDateTime exit);
}

// Car class
class Car extends Vehicle {

    Car(String regNo, String userType, boolean pass,
        LocalDateTime entry) {
        super(regNo, userType, pass, entry);
    }

    @Override
    double calculateFee(LocalDateTime entry, LocalDateTime exit) {
        long minutes = Duration.between(entry, exit).toMinutes();
        long hours = (minutes + 59) / 60;
        return hours * 30.0;
    }
}

// New category: E-bike
class EBike extends Vehicle {

    EBike(String regNo, String userType, boolean pass,
          LocalDateTime entry) {
        super(regNo, userType, pass, entry);
    }

    @Override
    double calculateFee(LocalDateTime entry, LocalDateTime exit) {
        return 25.0; // Flat fee
    }
}

// New category: Delivery van
class DeliveryVan extends Vehicle {

    DeliveryVan(String regNo, String userType, boolean pass,
                LocalDateTime entry) {
        super(regNo, userType, pass, entry);
    }

    @Override
    double calculateFee(LocalDateTime entry, LocalDateTime exit) {

        long totalMinutes =
            Duration.between(entry, exit).toMinutes();

        long daytimeMinutes = 0;

        // Find parking time that overlaps 9 AM to 5 PM
        LocalDate date = entry.toLocalDate();

        while (!date.isAfter(exit.toLocalDate())) {

            LocalDateTime dayStart = date.atTime(9, 0);
            LocalDateTime dayEnd = date.atTime(17, 0);

            LocalDateTime start =
                entry.isAfter(dayStart) ? entry : dayStart;

            LocalDateTime end =
                exit.isBefore(dayEnd) ? exit : dayEnd;

            if (end.isAfter(start)) {
                daytimeMinutes +=
                    Duration.between(start, end).toMinutes();
            }

            date = date.plusDays(1);
        }

        long outsideMinutes = totalMinutes - daytimeMinutes;

        // Double car rate during daytime = Rs 60/hour
        // Normal car rate outside daytime = Rs 30/hour
        return (daytimeMinutes * 60.0 / 60)
             + (outsideMinutes * 30.0 / 60);
    }
}

// Parking ticket
class ParkingTicket {
    static int counter = 2000;

    String ticketNo;
    Vehicle vehicle;
    int floor;
    int slot;
    LocalDateTime entryTime;

    ParkingTicket(Vehicle vehicle, int floor, int slot,
                  LocalDateTime entryTime) {
        counter++;
        ticketNo = "T" + counter;
        this.vehicle = vehicle;
        this.floor = floor;
        this.slot = slot;
        this.entryTime = entryTime;
    }
}

// Parking facility
class ParkingFacility {

    private ParkingTicket[][] slots;
    private HashMap<String, ParkingTicket> activeTickets;

    ParkingFacility(int[] slotsPerFloor) {

        slots = new ParkingTicket[slotsPerFloor.length][];

        for (int i = 0; i < slotsPerFloor.length; i++) {
            slots[i] = new ParkingTicket[slotsPerFloor[i]];
        }

        activeTickets = new HashMap<>();
    }

    // Allocate a slot and issue a ticket
    void enterVehicle(Vehicle vehicle, LocalDateTime entryTime) {

        if (activeTickets.containsKey(vehicle.regNo)) {
            System.out.println("Duplicate registration rejected.");
            return;
        }

        for (int f = 0; f < slots.length; f++) {
            for (int s = 0; s < slots[f].length; s++) {

                if (slots[f][s] == null) {

                    ParkingTicket ticket =
                        new ParkingTicket(vehicle, f, s, entryTime);

                    slots[f][s] = ticket;
                    activeTickets.put(vehicle.regNo, ticket);

                    System.out.println("Vehicle parked: "
                            + vehicle.regNo);
                    System.out.println("Ticket: "
                            + ticket.ticketNo);
                    System.out.println("Floor: " + f
                            + ", Slot: " + s);

                    return;
                }
            }
        }

        System.out.println("Parking Full.");
    }

    // Exit vehicle and calculate fee
    void exitVehicle(String regNo, LocalDateTime exitTime) {

        ParkingTicket ticket = activeTickets.get(regNo);

        if (ticket == null) {
            System.out.println("Ticket not found.");
            return;
        }

        if (exitTime.isBefore(ticket.entryTime)) {
            System.out.println("Invalid exit time.");
            return;
        }

        Vehicle vehicle = ticket.vehicle;
        double fee;

        // Valid visitor one-day pass
        if (vehicle.userType.equals("Visitor")
                && vehicle.oneDayPass
                && !exitTime.isAfter(vehicle.passExpiry)) {
            fee = 0;
        } else {
            fee = vehicle.calculateFee(ticket.entryTime, exitTime);
        }

        slots[ticket.floor][ticket.slot] = null;
        activeTickets.remove(regNo);

        System.out.println("Vehicle exited: " + regNo);
        System.out.println("Parking Fee: Rs " + fee);
        System.out.println("--------------------");
    }
}

// Main class
public class Main {

    public static void main(String[] args) {

        // Five floors with different slot counts
        ParkingFacility parking =
            new ParkingFacility(new int[]{1, 2, 1, 2, 1});

        System.out.println("CAMPUS SMART PARKING - TASK 12");
        System.out.println("--------------------");

        // Test 1: E-bike
        LocalDateTime eBikeEntry =
            LocalDateTime.of(2026, 11, 7, 11, 0);

        EBike ebike = new EBike(
            "TN01EB1234", "Visitor", false, eBikeEntry);

        parking.enterVehicle(ebike, eBikeEntry);

        // Test 2: Delivery van from 8 AM to 10 AM
        LocalDateTime vanEntry =
            LocalDateTime.of(2026, 11, 7, 8, 0);

        DeliveryVan van = new DeliveryVan(
            "TN02DV5678", "Visitor", false, vanEntry);

        parking.enterVehicle(van, vanEntry);

        // Test 3: Visitor with one-day pass
        LocalDateTime passEntry =
            LocalDateTime.of(2026, 11, 7, 12, 0);

        Car visitor = new Car(
            "TN03VC9012", "Visitor", true, passEntry);

        parking.enterVehicle(visitor, passEntry);

        // Test 4: Normal car
        LocalDateTime carEntry =
            LocalDateTime.of(2026, 11, 7, 15, 0);

        Car car = new Car(
            "TN04CA3456", "Visitor", false, carEntry);

        parking.enterVehicle(car, carEntry);

        System.out.println("--------------------");
        System.out.println("VEHICLE EXIT DETAILS");
        System.out.println("--------------------");

        // E-bike stays for 2 hours
        parking.exitVehicle("TN01EB1234",
            LocalDateTime.of(2026, 11, 7, 13, 0));

        // Van stays from 8 AM to 10 AM
        parking.exitVehicle("TN02DV5678",
            LocalDateTime.of(2026, 11, 7, 10, 0));

        // Visitor exits within one-day pass validity
        parking.exitVehicle("TN03VC9012",
            LocalDateTime.of(2026, 11, 7, 13, 0));

        // Normal car stays for 2 hours
        parking.exitVehicle("TN04CA3456",
            LocalDateTime.of(2026, 11, 7, 17, 0));
    }
}
