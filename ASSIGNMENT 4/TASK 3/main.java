
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    JTextField searchField;
    JComboBox<String> categoryBox;
    DefaultListModel<String> listModel;
    JList<String> resultList;
    JLabel statusLabel;

    String[][] books = {
        {"Java Programming", "Computer Science"},
        {"Python Basics", "Computer Science"},
        {"Database Systems", "Computer Science"},
        {"English Literature", "Literature"},
        {"History of India", "History"}
    };

    public Main() {

        setTitle("Campus Library Search");
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel header = new JPanel(new BorderLayout());

        JLabel title = new JLabel(
            "Campus Library Search", JLabel.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 22));

        header.add(title, BorderLayout.NORTH);

        // Search row
        JPanel searchPanel = new JPanel(new FlowLayout());

        searchField = new JTextField(18);

        String[] categories = {
            "All", "Computer Science", "Literature", "History"
        };

        categoryBox = new JComboBox<>(categories);

        JButton searchButton = new JButton("Search");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(categoryBox);
        searchPanel.add(searchButton);

        header.add(searchPanel, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        // Results area
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);

        JScrollPane scrollPane =
            new JScrollPane(resultList);

        add(scrollPane, BorderLayout.CENTER);

        // Footer buttons
        JPanel footer = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton prevButton = new JButton("Prev");
        JButton nextButton = new JButton("Next");
        JButton reserveButton = new JButton("Reserve");

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(reserveButton);

        footer.add(buttonPanel, BorderLayout.NORTH);

        statusLabel = new JLabel(
            "Enter a book title and click Search."
        );

        footer.add(statusLabel, BorderLayout.SOUTH);

        add(footer, BorderLayout.SOUTH);

        // Search action
        searchButton.addActionListener(e -> searchBooks());

        // Enter key also performs search
        searchField.addActionListener(e -> searchBooks());

        prevButton.addActionListener(e ->
            statusLabel.setText("Previous page selected.")
        );

        nextButton.addActionListener(e ->
            statusLabel.setText("Next page selected.")
        );

        reserveButton.addActionListener(e -> {
            String selected = resultList.getSelectedValue();

            if (selected == null) {
                statusLabel.setText("Please select a book first.");
            } else {
                statusLabel.setText(
                    "Reservation request: " + selected
                );
            }
        });

        setSize(650, 450);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void searchBooks() {

        String keyword =
            searchField.getText().trim().toLowerCase();

        String category =
            (String) categoryBox.getSelectedItem();

        listModel.clear();

        for (String[] book : books) {

            boolean titleMatches =
                book[0].toLowerCase().contains(keyword);

            boolean categoryMatches =
                category.equals("All")
                || book[1].equals(category);

            if (titleMatches && categoryMatches) {
                listModel.addElement(
                    book[0] + " - " + book[1]
                );
            }
        }

        if (listModel.isEmpty()) {
            statusLabel.setText("No books found.");
        } else {
            statusLabel.setText(
                listModel.size() + " book(s) found."
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
