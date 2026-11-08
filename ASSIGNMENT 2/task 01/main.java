
public class Main {

    static volatile long resultHolder;

    // Workload settings
    static int[] ticketCounts = {1, 1000, 10000};
    static int[] fieldCounts = {10, 20, 50};

    // Create ticket using String
    static String createUsingString(int fields) {
        String ticket = "";

        for (int i = 1; i <= fields; i++) {
            ticket = ticket + "Field" + i + ":Value" + i + "\n";
        }

        return ticket;
    }

    // Create ticket using StringBuilder
    static String createUsingBuilder(int fields) {
        StringBuilder ticket = new StringBuilder();

        for (int i = 1; i <= fields; i++) {
            ticket.append("Field")
                  .append(i)
                  .append(":Value")
                  .append(i)
                  .append("\n");
        }

        return ticket.toString();
    }

    // Create ticket using StringBuffer
    static String createUsingBuffer(int fields) {
        StringBuffer ticket = new StringBuffer();

        for (int i = 1; i <= fields; i++) {
            ticket.append("Field")
                  .append(i)
                  .append(":Value")
                  .append(i)
                  .append("\n");
        }

        return ticket.toString();
    }

    // Measure String approach
    static long measureString(int tickets, int fields) {
        long start = System.nanoTime();

        for (int i = 0; i < tickets; i++) {
            resultHolder += createUsingString(fields).length();
        }

        return System.nanoTime() - start;
    }

    // Measure StringBuilder approach
    static long measureBuilder(int tickets, int fields) {
        long start = System.nanoTime();

        for (int i = 0; i < tickets; i++) {
            resultHolder += createUsingBuilder(fields).length();
        }

        return System.nanoTime() - start;
    }

    // Measure StringBuffer approach
    static long measureBuffer(int tickets, int fields) {
        long start = System.nanoTime();

        for (int i = 0; i < tickets; i++) {
            resultHolder += createUsingBuffer(fields).length();
        }

        return System.nanoTime() - start;
    }

    // Average of repeated measurements
    static double averageTime(int method, int tickets, int fields) {

        long total = 0;
        int repetitions = 5;

        // Warm-up runs
        for (int i = 0; i < 2; i++) {
            if (method == 1)
                measureString(tickets, fields);
            else if (method == 2)
                measureBuilder(tickets, fields);
            else
                measureBuffer(tickets, fields);
        }

        for (int i = 0; i < repetitions; i++) {

            long time;

            if (method == 1)
                time = measureString(tickets, fields);
            else if (method == 2)
                time = measureBuilder(tickets, fields);
            else
                time = measureBuffer(tickets, fields);

            total += time;
        }

        return total / (double) repetitions;
    }

    public static void main(String[] args) {

        System.out.println("STRING PERFORMANCE COMPARISON");
        System.out.println("--------------------------------------------");

        System.out.printf(
            "%-12s %-15s %-18s %-18s%n",
            "Workload", "String(ns)",
            "StringBuilder(ns)", "StringBuffer(ns)"
        );

        for (int i = 0; i < ticketCounts.length; i++) {

            int tickets = ticketCounts[i];
            int fields = fieldCounts[i];

            double stringTime =
                averageTime(1, tickets, fields);

            double builderTime =
                averageTime(2, tickets, fields);

            double bufferTime =
                averageTime(3, tickets, fields);

            System.out.printf(
                "%-12s %-15.0f %-18.0f %-18.0f%n",
                tickets + " tickets",
                stringTime,
                builderTime,
                bufferTime
            );
        }

        System.out.println("--------------------------------------------");
        System.out.println("Total result value: " + resultHolder);
    }
}
