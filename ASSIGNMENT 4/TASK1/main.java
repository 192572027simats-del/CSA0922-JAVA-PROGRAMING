
class FeeService {

    public String processPayment(String regNo, String amountText) {

        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Registration number cannot be empty."
            );
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Please enter a valid amount."
            );
        }

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Amount must be greater than zero."
            );
        }

        return "Payment successful!\n"
             + "Registration Number: " + regNo
             + "\nAmount Paid: Rs. " + amount;
    }
}

public class Main {

    public static void main(String[] args) {

        FeeService service = new FeeService();

        // Test 1: Valid payment
        test(service, "STU101", "2500");

        // Test 2: Empty registration number
        test(service, "", "2500");

        // Test 3: Invalid amount
        test(service, "STU102", "abc");

        // Test 4: Negative amount
        test(service, "STU103", "-500");
    }

    static void test(
        FeeService service, String regNo, String amount) {

        System.out.println("\n--------------------");

        try {
            System.out.println(
                service.processPayment(regNo, amount)
            );
        } catch (IllegalArgumentException e) {
            System.out.println("Payment Error: " + e.getMessage());
        }
    }
}
