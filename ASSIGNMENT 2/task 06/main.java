
import java.util.*;

class Patient {
    String name;
    int priority;

    Patient(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return name + "(Priority " + priority + ")";
    }
}

public class Main {

    static void testNormalQueue() {
        Queue<Patient> queue = new LinkedList<>();

        queue.add(new Patient("Arun", 3));
        queue.add(new Patient("Bala", 1));
        queue.add(new Patient("Charan", 2));
        queue.add(new Patient("Deepak", 1));

        System.out.println("NORMAL QUEUE (FIFO):");

        while (!queue.isEmpty()) {
            System.out.println("Treated: " + queue.poll());
        }
    }

    static void testPriorityQueue() {
        PriorityQueue<Patient> queue =
            new PriorityQueue<>(
                Comparator.comparingInt(p -> p.priority)
            );

        queue.add(new Patient("Arun", 3));
        queue.add(new Patient("Bala", 1));
        queue.add(new Patient("Charan", 2));
        queue.add(new Patient("Deepak", 1));

        System.out.println("\nPRIORITY QUEUE:");

        while (!queue.isEmpty()) {
            System.out.println("Treated: " + queue.poll());
        }
    }

    static long measureQueue() {
        Queue<Integer> queue = new LinkedList<>();

        long start = System.nanoTime();

        for (int i = 0; i < 100000; i++) {
            queue.offer(i);
        }

        while (!queue.isEmpty()) {
            queue.poll();
        }

        return System.nanoTime() - start;
    }

    static long measurePriorityQueue() {
        PriorityQueue<Integer> queue = new PriorityQueue<>();

        long start = System.nanoTime();

        for (int i = 0; i < 100000; i++) {
            queue.offer(i);
        }

        while (!queue.isEmpty()) {
            queue.poll();
        }

        return System.nanoTime() - start;
    }

    public static void main(String[] args) {

        System.out.println("Q6 - HOSPITAL PATIENT QUEUE");
        System.out.println("------------------------------");

        testNormalQueue();
        testPriorityQueue();

        System.out.println("\nPERFORMANCE COMPARISON");

        long normalTime = measureQueue();
        long priorityTime = measurePriorityQueue();

        System.out.println(
            "Normal Queue Time: " + normalTime + " ns"
        );

        System.out.println(
            "PriorityQueue Time: " + priorityTime + " ns"
        );

        System.out.println("\nTESTING COMPLETED");
    }
}
