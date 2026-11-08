
import java.util.*;

class Song {
    int id;
    String name;

    Song(int id) {
        this.id = id;
        this.name = "Song " + id;
    }
}

public class Main {

    static volatile long resultHolder = 0;

    static final int OPERATIONS = 1000;
    static final int REPEATS = 3;

    // Create a playlist containing the requested number of songs
    static List<Song> createPlaylist(List<Song> list, int size) {
        for (int i = 0; i < size; i++) {
            list.add(new Song(i));
        }
        return list;
    }

    // Create additional songs before timing starts
    static Song[] createExtraSongs() {
        Song[] extra = new Song[OPERATIONS];

        for (int i = 0; i < OPERATIONS; i++) {
            extra[i] = new Song(100000 + i);
        }

        return extra;
    }

    // Test ArrayList or LinkedList
    static long testList(
        String type, String operation, int size) {

        long totalTime = 0;

        for (int repeat = 0; repeat < REPEATS; repeat++) {

            List<Song> list;

            if (type.equals("ArrayList")) {
                list = new ArrayList<>();
            } else {
                list = new LinkedList<>();
            }

            createPlaylist(list, size);
            Song[] extra = createExtraSongs();

            long checksum = 0;
            long start = System.nanoTime();

            switch (operation) {

                case "Add End":
                    for (int i = 0; i < OPERATIONS; i++) {
                        list.add(extra[i]);
                    }
                    break;

                case "Add Front":
                    for (int i = 0; i < OPERATIONS; i++) {
                        list.add(0, extra[i]);
                    }
                    break;

                case "Add Middle":
                    for (int i = 0; i < OPERATIONS; i++) {
                        list.add(list.size() / 2, extra[i]);
                    }
                    break;

                case "Remove Front":
                    for (int i = 0; i < OPERATIONS; i++) {
                        list.remove(0);
                    }
                    break;

                case "Remove Middle":
                    for (int i = 0; i < OPERATIONS; i++) {
                        list.remove(list.size() / 2);
                    }
                    break;

                case "Access Index":
                    for (int i = 0; i < OPERATIONS; i++) {
                        int index = (i * 7919) % size;
                        checksum += list.get(index).id;
                    }
                    break;

                case "Iteration":
                    for (int r = 0; r < 5; r++) {
                        for (Song song : list) {
                            checksum += song.id;
                        }
                    }
                    break;
            }

            long end = System.nanoTime();

            totalTime += end - start;
            resultHolder += checksum + list.size();
        }

        return totalTime / REPEATS;
    }

    // Test ArrayDeque for Up Next queue operations
    static long testDeque(String operation, int size) {

        long totalTime = 0;

        for (int repeat = 0; repeat < REPEATS; repeat++) {

            ArrayDeque<Song> deque = new ArrayDeque<>();

            for (int i = 0; i < size; i++) {
                deque.addLast(new Song(i));
            }

            Song[] extra = createExtraSongs();
            long start = System.nanoTime();

            switch (operation) {

                case "Add Front":
                    for (int i = 0; i < OPERATIONS; i++) {
                        deque.addFirst(extra[i]);
                    }
                    break;

                case "Add End":
                    for (int i = 0; i < OPERATIONS; i++) {
                        deque.addLast(extra[i]);
                    }
                    break;

                case "Remove Front":
                    for (int i = 0; i < OPERATIONS; i++) {
                        deque.removeFirst();
                    }
                    break;
            }

            long end = System.nanoTime();
            totalTime += end - start;

            resultHolder += deque.size();
        }

        return totalTime / REPEATS;
    }

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 100000};

        String[] operations = {
            "Add End",
            "Add Front",
            "Add Middle",
            "Remove Front",
            "Remove Middle",
            "Access Index",
            "Iteration"
        };

        System.out.println("Q2 - PLAYLIST PERFORMANCE COMPARISON");
        System.out.println("Times are average nanoseconds over "
                           + REPEATS + " runs.");
        System.out.println("Each edit/access operation performs "
                           + OPERATIONS + " operations.");
        System.out.println("-----------------------------------------------");

        for (int size : sizes) {

            System.out.println("\nPLAYLIST SIZE: " + size);
            System.out.printf(
                "%-16s %-17s %-17s%n",
                "Operation", "ArrayList(ns)", "LinkedList(ns)"
            );

            for (String operation : operations) {

                long arrayTime =
                    testList("ArrayList", operation, size);

                long linkedTime =
                    testList("LinkedList", operation, size);

                System.out.printf(
                    "%-16s %-17d %-17d%n",
                    operation, arrayTime, linkedTime
                );
            }

            System.out.println("\nARRAYDEQUE - UP NEXT QUEUE");

            System.out.printf(
                "%-16s %-17s%n",
                "Operation", "ArrayDeque(ns)"
            );

            String[] dequeOperations = {
                "Add Front",
                "Add End",
                "Remove Front"
            };

            for (String operation : dequeOperations) {

                long time = testDeque(operation, size);

                System.out.printf(
                    "%-16s %-17d%n",
                    operation, time
                );
            }
        }

        System.out.println("\n-----------------------------------------------");
        System.out.println("Result checksum: " + resultHolder);
        System.out.println("EXPERIMENT COMPLETED");
    }
}
