
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Converter extends JFrame {

    private final JTextField amount = new JTextField(10);

    private final JComboBox<String> target =
        new JComboBox<>(new String[]{"USD", "EUR"});

    private final JLabel result = new JLabel(" ");

    private static double rate(String currency) {
        return currency.equals("USD") ? 0.012 : 0.011;
    }

    public Converter() {
        super("Currency Converter");

        setLayout(new FlowLayout());

        JButton convertButton = new JButton("Convert");

        add(new JLabel("Amount (INR):"));
        add(amount);
        add(new JLabel("Convert to:"));
        add(target);
        add(convertButton);
        add(result);

        // TODO 1: One lambda listener for button and Enter key
        java.awt.event.ActionListener convertAction =
            e -> convert();

        convertButton.addActionListener(convertAction);
        amount.addActionListener(convertAction);

        // Handle window closing using WindowAdapter
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);
    }

    private void convert() {
        // TODO 2: Validate amount and display conversion
        try {
            double inr = Double.parseDouble(amount.getText().trim());

            if (!Double.isFinite(inr) || inr <= 0) {
                throw new NumberFormatException();
            }

            String currency = (String) target.getSelectedItem();
            double converted = inr * rate(currency);

            String message = String.format(
                "%.2f INR = %.2f %s",
                inr, converted, currency
            );

            result.setText(message);
            JOptionPane.showMessageDialog(
                this, message, "Conversion Result",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException ex) {
            result.setText("Invalid amount");
            JOptionPane.showMessageDialog(
                this, "Invalid amount. Enter a positive number.",
                "Input Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new Converter().setVisible(true)
        );
    }
}
