
class ParkingTicket {

    // Shared static counter
    static int ticketCounter;

    String ticketNo;
    String vehicleNo;
    boolean active;

    // Static block
    static {
        ticketCounter = 1000;
        System.out.println("Ticket system initialized.");
    }

    // Constructor
    ParkingTicket(String vehicleNo) {
        ticketCounter++;

        this.ticketNo = "T" + ticketCounter;
        this.vehicleNo = vehicleNo;
        this.active = true;
    }

    // Static method
    static int getTotalTickets() {
        return ticketCounter - 1000;
    }

    void displayTicket() {
        System.out.println("Ticket Number: " + ticketNo);
        System.out.println("Vehicle Number: " + vehicleNo);
        System.out.println("Active: " + active);
        System.out.println("--------------------");
    }
}

public class Parking {

    // Method that creates and returns an object
    static ParkingTicket createTicket(String vehicleNo) {
        ParkingTicket ticket = new ParkingTicket(vehicleNo);
        return ticket;
    }

    // Method that receives an object
    static void displayDetails(ParkingTicket ticket) {
        ticket.displayTicket();
    }

    // Method that receives and updates an object
    static void closeTicket(ParkingTicket ticket) {
        ticket.active = false;
        System.out.println(ticket.ticketNo + " has been closed.");
    }

    public static void main(String[] args) {

        // Creating ticket objects
        ParkingTicket t1 = createTicket("TN01AB1234");
        ParkingTicket t2 = createTicket("TN02CD5678");

        // Passing objects to another method
        displayDetails(t1);
        displayDetails(t2);

        // Closing a ticket
        closeTicket(t1);

        System.out.println("Total tickets issued: "
                + ParkingTicket.getTotalTickets());

        // Removing references
        t1 = null;
        t2 = null;

        // Objects are now eligible for garbage collection
        System.out.println("Ticket references removed.");
        System.gc();
    }
}
