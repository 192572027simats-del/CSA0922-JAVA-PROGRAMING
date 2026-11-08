
import java.util.*;

class Medicine {
    String code;
    String name;
    int stock;

    Medicine(String code, int stock) {
        this.code = code;
        this.name = "Medicine " + code;
        this.stock = stock;
    }
}

public class Main {

    static final int LOOKUPS = 100000;
    static volatile long resultHolder = 0;

    // Generate medicine codes
    static String medicineCode(int number) {
        return String.format("MED%06d", number);
    }

    // Measure insertion time
    static long measurePut(Map<String, Integer> map,
                           int size) {

        long start = System.nanoTime();

        // Insert in descending order
        for (int i = size - 1; i >= 0; i--) {
            map.put(medicineCode(i), i + 10);
        }

        return System.nanoTime() - start;
    }

    // Measure 100,000 lookups
    static long measureGet(Map<String, Integer> map,
                           int size) {

        long sum = 0;

        long start = System.nanoTime();

        for (int i = 0; i < LOOKUPS; i++) {
            String key = medicineCode(i % size);
            Integer stock = map.get(key);

            if (stock != null) {
                sum += stock;
            }
        }

        long end = System.nanoTime();

        resultHolder += sum;

        return end - start;
    }

    // Measure inventory report generation
    static long measureIteration(Map<String, Integer> map) {

        StringBuilder report = new StringBuilder();

        long start = System.nanoTime();

        for (Map.Entry<String, Integer> entry
                : map.entrySet()) {

            report.append(entry.getKey())
                  .append(" : ")
                  .append(entry.getValue())
                  .append("\n");
        }

        long end = System.nanoTime();

        resultHolder += report.length();

        return end - start;
    }

    // Display first five medicine codes
    static void displaySample(Map<String, Integer> map) {

        int count = 0;

        for (String code : map.keySet()) {
            System.out.print(code + " ");
            count++;

            if (count == 5) {
                break;
            }
        }

        System.out.println();
    }

    static void testMap(String name,
                        Map<String, Integer> map,
                        int size) {

        long putTime = measurePut(map, size);
        long getTime = measureGet(map, size);
        long iterationTime = measureIteration(map);

        System.out.printf(
            "%-15s %-15d %-15d %-15d%n",
            name, putTime, getTime, iterationTime
        );

        System.out.println("Report order sample:");
        displaySample(map);

        System.out.println();
    }

    public static void main(String[] args) {

        int[] sizes = {1000, 10000, 100000};

        System.out.println("Q4 - MEDICINE INVENTORY MAP COMPARISON");
        System.out.println("Lookup operations per map: " + LOOKUPS);

        for (int size : sizes) {

            System.out.println("\n======================================");
            System.out.println("INVENTORY SIZE: " + size);
            System.out.println("======================================");

            System.out.printf(
                "%-15s %-15s %-15s %-15s%n",
                "Map Type", "Put(ns)", "Get(ns)", "Iteration(ns)"
            );

            HashMap<String, Integer> hashMap =
                new HashMap<>();

            TreeMap<String, Integer> treeMap =
                new TreeMap<>();

            LinkedHashMap<String, Integer> linkedHashMap =
                new LinkedHashMap<>();

            testMap("HashMap", hashMap, size);
            testMap("TreeMap", treeMap, size);
            testMap("LinkedHashMap", linkedHashMap, size);

            System.out.println("ORDER VERIFICATION");

            System.out.println("HashMap sample:");
            displaySample(hashMap);

            System.out.println("TreeMap sample:");
            displaySample(treeMap);

            System.out.println("LinkedHashMap sample:");
            displaySample(linkedHashMap);
        }

        System.out.println("======================================");
        System.out.println("EXPERIMENT COMPLETED");
        System.out.println("Result checksum: " + resultHolder);
    }
}
