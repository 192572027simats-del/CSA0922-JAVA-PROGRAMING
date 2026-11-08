
public class Main {

    static final int TICKETS = 10000;
    static final int FIELDS = 50;

    static volatile long resultHolder;

    // Original approach: String concatenation
    static long generateWithString() {
        long checksum = 0;

        for (int i = 0; i < TICKETS; i++) {
            String ticket = "";

            for (int j = 1; j <= FIELDS; j++) {
                ticket += "Field" + j + ":Value" + i + " ";
            }

            checksum += ticket.length();
        }

        resultHolder = checksum;
        return checksum;
    }

    // Redesigned approach: StringBuilder
    static long generateWithBuilder() {
        long checksum = 0;

        for (int i = 0; i < TICKETS; i++) {
            StringBuilder ticket = new StringBuilder();

            for (int j = 1; j <= FIELDS; j++) {
                ticket.append("Field")
                      .append(j)
                      .append(":Value")
                      .append(i)
                      .append(" ");
            }

            checksum += ticket.length();
        }

        resultHolder = checksum;
        return checksum;
    }

    static long measureString() {
        long start = System.nanoTime();
        generateWithString();
        return System.nanoTime() - start;
    }

    static long measureBuilder() {
        long start = System.nanoTime();
        generateWithBuilder();
        return System.nanoTime() - start;
    }

    public static void main(String[] args) {

        System.out.println("Q7 - REDESIGN VALIDATION");
        System.out.println("------------------------------");
        System.out.println("Tickets: " + TICKETS);
        System.out.println("Fields per ticket: " + FIELDS);

        // Warm-up
        generateWithString();
        generateWithBuilder();

        long stringTotal = 0;
        long builderTotal = 0;

        int repetitions = 5;

        for (int i = 0; i < repetitions; i++) {
            stringTotal += measureString();
            builderTotal += measureBuilder();
        }

        long stringAverage = stringTotal / repetitions;
        long builderAverage = builderTotal / repetitions;

        System.out.println("\nAVERAGE EXECUTION TIME");
        System.out.println("String: " + stringAverage + " ns");
        System.out.println("StringBuilder: " + builderAverage + " ns");

        double improvement =
            ((double)(stringAverage - builderAverage)
            / stringAverage) * 100;

        System.out.printf(
            "Performance improvement: %.2f%%\n", improvement
        );

        // Verify that both approaches produce the same result
        long originalResult = generateWithString();
        long redesignedResult = generateWithBuilder();

        System.out.println("\nCORRECTNESS CHECK");
        System.out.println("Original checksum: " + originalResult);
        System.out.println("Redesigned checksum: " + redesignedResult);
        System.out.println(
            "Same output length: " +
            (originalResult == redesignedResult)
        );

        System.out.println("\nTESTING COMPLETED");
    }
}
