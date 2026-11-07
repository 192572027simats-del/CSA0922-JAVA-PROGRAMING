
class ParkingTicket {

    String ticketNo;
    String vehicleNo;

    // No-argument constructor
    ParkingTicket() {
        this("Not Assigned", "Not Assigned");
    }

    // Parameterized constructor
    ParkingTicket(String ticketNo, String vehicleNo) {
        this.ticketNo = ticketNo;
        this.vehicleNo = vehicleNo;
    }

    // Copy constructor
    ParkingTicket(ParkingTicket oldTicket) {
        this(oldTicket.ticketNo, oldTicket.vehicleNo);
    }

    void displayTicket() {
        System.out.println("Ticket Number: " + ticketNo);
        System.out.println("Vehicle Number: " + vehicleNo);
        System.out.println("--------------------");
    }
}

public class Parking {

    public static void main(String[] args) {

        System.out.println("VEHICLE OBJECTS");

        // Creating objects using different constructors
        Vehicle v1 = new Vehicle();
        Vehicle v2 = new Vehicle("TN01AB1234");
        Vehicle v3 = new Vehicle("TN02CD5678", "TwoWheeler");

        // Copy constructor
        Vehicle v4 = new Vehicle(v3);

        v1.displayDetails();
        v2.displayDetails();
        v3.displayDetails();
        v4.displayDetails();

        System.out.println("PARKING TICKETS");

        // Creating parking ticket objects
        ParkingTicket t1 =
            new ParkingTicket("T101", "TN01AB1234");

        ParkingTicket t2 =
            new ParkingTicket();

        // Copy constructor
        ParkingTicket t3 = new ParkingTicket(t1);

        t1.displayTicket();
        t2.displayTicket();
        t3.displayTicket();
    }
}
