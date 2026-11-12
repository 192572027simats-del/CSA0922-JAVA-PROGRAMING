
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

class CinemaBooking {

    private boolean[] booked = new boolean[10];

    private AtomicInteger successfulBookings =
        new AtomicInteger(0);

    // Without synchronization: race condition
    void bookWithoutSync(int seat) {

        if (!booked[seat]) {

            // Give other threads a chance to check the same seat
            Thread.yield();

            booked[seat] = true;
            successfulBookings.incrementAndGet();
        }
    }

    // With synchronization: safe booking
    synchronized void bookWithSync(int seat) {

        if (!booked[seat]) {
            booked[seat] = true;
            successfulBookings.incrementAndGet();
        }
    }

    int getUniqueSeatsSold() {
        int count = 0;

        for (boolean seat : booked) {
            if (seat) {
                count++;
            }
        }

        return count;
    }

    int getSuccessfulBookings() {
        return successfulBookings.get();
    }
}

public class Main {

    static void runExperiment(boolean useSync)
            throws InterruptedException {

        CinemaBooking cinema = new CinemaBooking();

        int numberOfCustomers = 100;

        Thread[] customers = new Thread[numberOfCustomers];

        CountDownLatch startSignal = new CountDownLatch(1);

        long startTime = System.nanoTime();

        for (int i = 0; i < numberOfCustomers; i++) {

            final int seatNumber = i % 10;

            customers[i] = new Thread(() -> {

                try {
                    startSignal.await();

                    if (useSync) {
                        cinema.bookWithSync(seatNumber);
                    } else {
                        cinema.bookWithoutSync(seatNumber);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            customers[i].start();
        }

        // Allow all customers to compete at approximately the same time
        startSignal.countDown();

        for (Thread customer : customers) {
            customer.join();
        }

        long endTime = System.nanoTime();

        long executionTime = endTime - startTime;

        System.out.println("Customers: " + numberOfCustomers);
        System.out.println("Available seats: 10");

        System.out.println(
            "Successful booking attempts: " +
            cinema.getSuccessfulBookings()
        );

        System.out.println(
            "Unique seats actually sold: " +
            cinema.getUniqueSeatsSold()
        );

        System.out.println(
            "Duplicate bookings: " +
            (cinema.getSuccessfulBookings()
             - cinema.getUniqueSeatsSold())
        );

        System.out.println(
            "Execution time: " + executionTime + " ns"
        );
    }

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println(
            "===== WITHOUT SYNCHRONIZATION ====="
        );

        runExperiment(false);

        System.out.println();

        System.out.println(
            "===== WITH SYNCHRONIZATION ====="
        );

        runExperiment(true);
    }
}
