
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

// Method 1: Creating threads by extending Thread
class ScannerThread extends Thread {
    private int stationId;
    private int scriptsScanned;
    private List<Integer> finishOrder;

    ScannerThread(int id, int scripts, List<Integer> order) {
        stationId = id;
        scriptsScanned = scripts;
        finishOrder = order;
        setName("Station-" + id);
    }

    public void run() {
        try {
            // Simulate scanning work
            Thread.sleep(ThreadLocalRandom.current().nextInt(50, 201));

            System.out.println(
                getName() + " scanned " + scriptsScanned + " scripts."
            );

            finishOrder.add(stationId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Method 2: Creating threads by implementing Runnable
class ScannerTask implements Runnable {
    private int stationId;
    private int scriptsScanned;
    private List<Integer> finishOrder;

    ScannerTask(int id, int scripts, List<Integer> order) {
        stationId = id;
        scriptsScanned = scripts;
        finishOrder = order;
    }

    public void run() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(50, 201));

            System.out.println(
                "Runnable Station-" + stationId +
                " scanned " + scriptsScanned + " scripts."
            );

            finishOrder.add(stationId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Main {

    static void testThreadClass(int trial)
            throws InterruptedException {

        List<Integer> order =
            Collections.synchronizedList(new ArrayList<>());

        Thread[] stations = new Thread[4];
        int total = 0;

        for (int i = 0; i < 4; i++) {
            stations[i] =
                new ScannerThread(i + 1, (i + 1) * 10, order);
            stations[i].start();
        }

        System.out.println("Trial " + trial + " - Thread subclass");

        for (Thread station : stations) {
            System.out.println(
                station.getName() + " alive: " + station.isAlive()
            );
            station.join();
        }

        for (int i = 0; i < 4; i++) {
            total += (i + 1) * 10;
        }

        System.out.println("Finish order: " + order);
        System.out.println("Total scripts scanned: " + total);
        System.out.println();
    }

    static void testRunnable(int trial)
            throws InterruptedException {

        List<Integer> order =
            Collections.synchronizedList(new ArrayList<>());

        Thread[] stations = new Thread[4];
        int total = 0;

        for (int i = 0; i < 4; i++) {
            ScannerTask task =
                new ScannerTask(i + 1, (i + 1) * 10, order);

            stations[i] = new Thread(task, "Station-" + (i + 1));
            stations[i].start();
        }

        System.out.println("Trial " + trial + " - Runnable");

        for (Thread station : stations) {
            System.out.println(
                station.getName() + " alive: " + station.isAlive()
            );
            station.join();
        }

        for (int i = 0; i < 4; i++) {
            total += (i + 1) * 10;
        }

        System.out.println("Finish order: " + order);
        System.out.println("Total scripts scanned: " + total);
        System.out.println();
    }

    static void priorityExperiment()
            throws InterruptedException {

        System.out.println("===== PRIORITY EXPERIMENT =====");

        Thread low = new Thread(() -> {
            System.out.println("Low priority station running.");
        });

        Thread high = new Thread(() -> {
            System.out.println("High priority station running.");
        });

        low.setPriority(Thread.MIN_PRIORITY);
        high.setPriority(Thread.MAX_PRIORITY);

        low.start();
        high.start();

        low.join();
        high.join();

        System.out.println("Priority experiment completed.");
        System.out.println();
    }

    static void daemonExperiment()
            throws InterruptedException {

        System.out.println("===== DAEMON THREAD EXPERIMENT =====");

        Thread daemon = new Thread(() -> {
            try {
                System.out.println("Daemon station started.");
                Thread.sleep(2000);
                System.out.println("Daemon station finished.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        daemon.setDaemon(true);
        daemon.start();

        Thread.sleep(100);

        System.out.println("Main thread is ending.");
        System.out.println("Is daemon alive? " + daemon.isAlive());
    }

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println("===== THREAD SUBCLASS: 10 TRIALS =====");

        for (int i = 1; i <= 10; i++) {
            testThreadClass(i);
        }

        System.out.println("===== RUNNABLE: 10 TRIALS =====");

        for (int i = 1; i <= 10; i++) {
            testRunnable(i);
        }

        priorityExperiment();
        daemonExperiment();

        System.out.println("Main method completed.");
    }
}
