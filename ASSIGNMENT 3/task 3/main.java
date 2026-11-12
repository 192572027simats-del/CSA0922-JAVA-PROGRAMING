
import java.io.*;
import java.nio.file.*;
import java.util.*;

// Tracks how many input files are currently open
class TrackedReader extends BufferedReader {
    static int openFiles = 0;
    private boolean closed = false;

    TrackedReader(String fileName) throws IOException {
        super(new FileReader(fileName));
        openFiles++;
    }

    public void close() throws IOException {
        if (!closed) {
            super.close();
            closed = true;
            openFiles--;
        }
    }
}

public class Main {

    static String[] files = {
        "store1.txt", "store2.txt", "store3.txt"
    };

    // Create sample input files
    static void createFiles(boolean corrupt) throws IOException {
        Files.write(Paths.get("store1.txt"),
            Arrays.asList("100", "200"));

        if (corrupt) {
            Files.write(Paths.get("store2.txt"),
                Arrays.asList("150", "INVALID", "250"));
        } else {
            Files.write(Paths.get("store2.txt"),
                Arrays.asList("150", "250"));
        }

        Files.write(Paths.get("store3.txt"),
            Arrays.asList("300", "400"));
    }

    // Version 1: Manual closing using finally
    static void manualVersion(String outputFile) {

        TrackedReader reader = null;
        BufferedWriter writer = null;
        int total = 0;

        try {
            writer = new BufferedWriter(
                new FileWriter(outputFile)
            );

            for (String file : files) {
                System.out.println("Reading: " + file);

                try {
                    reader = new TrackedReader(file);

                    String line;
                    while ((line = reader.readLine()) != null) {
                        total += Integer.parseInt(line);
                    }

                } catch (NumberFormatException e) {
                    System.out.println(
                        "Invalid sales data in: " + file
                    );
                    throw e;
                } finally {
                    if (reader != null) {
                        reader.close();
                        reader = null;
                    }
                }
            }

            writer.write("Total Sales: Rs. " + total);
            writer.newLine();
            System.out.println("Report created: " + outputFile);

        } catch (IOException | NumberFormatException e) {
            System.out.println("Manual version error: " + e.getMessage());

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }

                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println("Error while closing: " + e.getMessage());
            }

            System.out.println(
                "Manual version open input files: " +
                TrackedReader.openFiles
            );
        }
    }

    // Version 2: Automatic closing using try-with-resources
    static void tryWithResourcesVersion(String outputFile) {

        int total = 0;

        try (BufferedWriter writer =
                 new BufferedWriter(new FileWriter(outputFile))) {

            for (String file : files) {
                System.out.println("Reading: " + file);

                try (TrackedReader reader =
                         new TrackedReader(file)) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        total += Integer.parseInt(line);
                    }

                } catch (NumberFormatException e) {
                    System.out.println(
                        "Invalid sales data in: " + file
                    );
                    throw e;
                }
            }

            writer.write("Total Sales: Rs. " + total);
            writer.newLine();
            System.out.println("Report created: " + outputFile);

        } catch (IOException | NumberFormatException e) {
            System.out.println(
                "Try-with-resources error: " + e.getMessage()
            );
        }

        System.out.println(
            "Try-with-resources open input files: " +
            TrackedReader.openFiles
        );
    }

    public static void main(String[] args) throws IOException {

        // Test 1: Valid input
        System.out.println("===== TEST 1: VALID INPUT =====");
        createFiles(false);

        manualVersion("manual_report.txt");
        tryWithResourcesVersion("twr_report.txt");

        String manualReport =
            new String(Files.readAllBytes(
                Paths.get("manual_report.txt")
            ));

        String twrReport =
            new String(Files.readAllBytes(
                Paths.get("twr_report.txt")
            ));

        System.out.println("Reports are identical: " +
            manualReport.equals(twrReport));

        System.out.println("\n===== TEST 2: CORRUPTED INPUT =====");
        createFiles(true);

        manualVersion("manual_failed_report.txt");
        tryWithResourcesVersion("twr_failed_report.txt");
    }
}
