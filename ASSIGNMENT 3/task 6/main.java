
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// Wallet class for deadlock demonstration
class Wallet {
    int balance;
    int id;

    Wallet(int id, int balance) {
        this.id = id;
        this.balance = balance;
    }
}

class WalletTransfer {

    // Safe transfer using consistent lock ordering
    static void transfer(Wallet a, Wallet b, int amount) {

        Wallet first = a.id < b.id ? a : b;
        Wallet second = a.id < b.id ? b : a;

        synchronized (first) {
            synchronized (second) {

                if (a.balance >= amount) {
                    a.balance -= amount;
                    b.balance += amount;

                    System.out.println(
                        "Transferred Rs. " + amount +
                        " from Wallet " + a.id +
                        " to Wallet " + b.id
                    );
                }
            }
        }
    }
}

// Producer-consumer buffer
class SharedBuffer {

    private int[] buffer = new int[5];
    private int count = 0;
    private int in = 0;
    private int out = 0;

    // Producer adds data
    synchronized void produce(int value)
            throws InterruptedException {

        while (count == buffer.length) {
            wait();
        }

        buffer[in] = value;
        in = (in + 1) % buffer.length;
        count++;

        System.out.println("Produced: " + value);

        notifyAll();
    }

    // Consumer removes data
    synchronized int consume()
            throws InterruptedException {

        while (count == 0) {
            wait();
        }

        int value = buffer[out];
        out = (out + 1) % buffer.length;
        count--;

        System.out.println("Consumed: " + value);

        notifyAll();

        return value;
    }
}

// Alternative producer-consumer implementation
class BlockingQueueExample {

    static void demonstrate() throws InterruptedException {

        BlockingQueue<Integer> queue =
            new ArrayBlockingQueue<>(5);

        System.out.println("\nArrayBlockingQueue demonstration:");

        for (int i = 1; i <= 5; i++) {
            queue.put(i);
            System.out.println("Added to queue: " + i);
        }

        for (int i = 1; i <= 5; i++) {
            System.out.println(
                "Removed from queue: " + queue.take()
            );
        }
    }
}

public class Main {

    public static void main(String[] args)
            throws InterruptedException {

        // PART A: Deadlock prevention
        System.out.println("===== PART A: WALLET TRANSFER =====");

        Wallet w1 = new Wallet(1, 1000);
        Wallet w2 = new Wallet(2, 1000);

        Thread t1 = new Thread(() ->
            WalletTransfer.transfer(w1, w2, 100)
        );

        Thread t2 = new Thread(() ->
            WalletTransfer.transfer(w2, w1, 200)
        );

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Wallet 1 balance: " + w1.balance);
        System.out.println("Wallet 2 balance: " + w2.balance);

        // PART B: Producer-consumer
        System.out.println("\n===== PART B: PRODUCER-CONSUMER =====");

        SharedBuffer buffer = new SharedBuffer();

        int[] sum = {0};

        Thread producer = new Thread(() -> {

            try {
                for (int i = 1; i <= 20; i++) {
                    buffer.produce(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {

            try {
                for (int i = 1; i <= 20; i++) {
                    sum[0] += buffer.consume();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

        System.out.println("Total consumed sum: " + sum[0]);

        // Alternative implementation
        try {
            BlockingQueueExample.demonstrate();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
