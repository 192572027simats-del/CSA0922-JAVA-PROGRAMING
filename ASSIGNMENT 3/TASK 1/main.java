
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;

// Custom exception for insufficient balance
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

public class Main {

    static double balance = 5000.0;

    public static void withdraw(double amount)
            throws InsufficientFundsException {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Withdrawal amount must be positive."
            );
        }

        if (amount > balance) {
            throw new InsufficientFundsException(
                "Insufficient balance. Available balance: Rs. " + balance
            );
        }

        balance = balance - amount;

        System.out.println("Withdrawal successful!");
        System.out.println("Cash dispensed: Rs. " + amount);
        System.out.println("Remaining balance: Rs. " + balance);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== ATM SIMULATION =====");
        System.out.println("Initial balance: Rs. " + balance);

        try {
            System.out.print("Enter account number: ");
            long accountNumber = Long.parseLong(sc.nextLine());

            System.out.println("Account number accepted: " + accountNumber);

            System.out.print("Enter withdrawal amount: ");
            double amount = Double.parseDouble(sc.nextLine());

            System.out.print("Is the card-reader file missing? (yes/no): ");
            String choice = sc.nextLine();

            if (choice.equalsIgnoreCase("yes")) {
                // Deliberately opening a file that does not exist
                FileInputStream file =
                    new FileInputStream("missing_card_reader.txt");
                file.close();
            }

            withdraw(amount);

        } catch (NumberFormatException e) {

            System.out.println("Input Error: Please enter numbers only.");

        } catch (IllegalArgumentException e) {

            System.out.println("Invalid Amount: " + e.getMessage());

        } catch (InsufficientFundsException e) {

            System.out.println("Transaction Failed: " + e.getMessage());

        } catch (IOException | SecurityException e) {

            System.out.println("Card Reader Error: " + e.getMessage());

        } finally {

            System.out.println("ATM transaction process completed.");
            System.out.println("Finally block executed.");

            sc.close();
        }
    }
}
