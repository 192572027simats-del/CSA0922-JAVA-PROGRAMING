
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ConverterNamedInner extends JFrame {

    private final JTextField amount = new JTextField(10);

    private final JComboBox<String> target =
        new JComboBox<>(new String[]{"USD", "EUR"});

    private final JLabel result = new JLabel(" ");

    private final JButton convertButton = new JButton("Convert");

    public ConverterNamedInner() {
        super("Currency Converter - Named Inner Class");

        setLayout(new FlowLayout());

        add(new JLabel("Amount (INR):"));
        add(amount);
        add(new JLabel("Convert to:"));
        add(target);
        add(convertButton);
        add(result);

        // Register the named inner class
        ConvertHandler handler = new ConvertHandler();

        convertButton.addActionListener(handler);
        amount.addActionListener(handler);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(440, 150);
        setLocationRelativeTo(null);
    }

    // Named inner class
    private class ConvertHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            convert();
        }
    }

    private void convert() {
        try {
            double inr = Double.parseDouble(amount.getText().trim());

            if (!Double.isFinite(inr) || inr <= 0) {
                throw new NumberFormatException();
            }

            String currency = (String) target.getSelectedItem();

            double rate = currency.equals("USD") ? 0.012 : 0.011;
            double converted = inr * rate;

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
                this,
                "Invalid amount. Enter a positive number.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new ConverterNamedInner().setVisible(true)
        );
    }
}
