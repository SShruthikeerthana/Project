import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseIncomeTracker extends JFrame {
    private JTextField amountField;
    private JTextField descriptionField;
    private JTextField categoryField;
    private JTextField accountField;
    private JTextArea recordArea;
    private double balance;
    private double totalIncome;
    private double totalExpenses;
    private JLabel balanceLabel;
    private List<Transaction> transactions = new ArrayList<>();
    private JTextField budgetField;
    private double monthlyBudget = 0.0;

    public ExpenseIncomeTracker() {
        setTitle("Expense and Income Tracker (in Rupees)");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");

        JMenuItem loginItem = new JMenuItem("Login");
        loginItem.addActionListener(e -> showLoginDialog());
        
                JMenuItem exportItem = new JMenuItem("Export Records");
                exportItem.addActionListener(e -> exportRecordsToFile());
        
                JMenuItem viewHistoryItem = new JMenuItem("View Transaction History");
                viewHistoryItem.addActionListener(e -> showTransactionHistory());
        
                optionsMenu.add(loginItem);
                optionsMenu.add(exportItem);
                optionsMenu.add(viewHistoryItem);
                menuBar.add(optionsMenu);
                setJMenuBar(menuBar);
        
                // Fonts
                Font font = new Font("Arial", Font.PLAIN, 16);
                Font balanceFont = new Font("Arial", Font.BOLD, 20);
        
                // Input Panel
                JPanel inputPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 5, 5);
        
                // Amount Label
                gbc.gridx = 0;
                gbc.gridy = 0;
                JLabel amountLabel = new JLabel("Amount (₹):");
                amountLabel.setFont(new Font("Arial", Font.BOLD, 18));
                inputPanel.add(amountLabel, gbc);
        
                // Amount Field
                gbc.gridx = 1;
                amountField = new JTextField(10);
                amountField.setFont(font);
                inputPanel.add(amountField, gbc);
        
                // Description Label
                gbc.gridx = 0;
                gbc.gridy = 1;
                JLabel descriptionLabel = new JLabel("Description:");
                descriptionLabel.setFont(new Font("Arial", Font.BOLD, 18));
                inputPanel.add(descriptionLabel, gbc);
        
                // Description Field
                gbc.gridx = 1;
                descriptionField = new JTextField(10);
                descriptionField.setFont(font);
                inputPanel.add(descriptionField, gbc);
        
                // Category Label
                gbc.gridx = 0;
                gbc.gridy = 2;
                JLabel categoryLabel = new JLabel("Category:");
                categoryLabel.setFont(new Font("Arial", Font.BOLD, 18));
                inputPanel.add(categoryLabel, gbc);
        
                // Category Field
                gbc.gridx = 1;
                categoryField = new JTextField(10);
                categoryField.setFont(font);
                inputPanel.add(categoryField, gbc);
        
                // Account Label
                gbc.gridx = 0;
                gbc.gridy = 3;
                JLabel accountLabel = new JLabel("Account:");
                accountLabel.setFont(new Font("Arial", Font.BOLD, 18));
                inputPanel.add(accountLabel, gbc);
        
                // Account Field
                gbc.gridx = 1;
                accountField = new JTextField(10);
                accountField.setFont(font);
                inputPanel.add(accountField, gbc);
        
                // Budget Label
                gbc.gridx = 0;
                gbc.gridy = 4;
                JLabel budgetLabel = new JLabel("Monthly Budget (₹):");
                budgetLabel.setFont(new Font("Arial", Font.BOLD, 18));
                inputPanel.add(budgetLabel, gbc);
        
                // Budget Field
                gbc.gridx = 1;
                budgetField = new JTextField(10);
                budgetField.setFont(font);
                inputPanel.add(budgetField, gbc);
        
                // Add Income Button
                gbc.gridx = 0;
                gbc.gridy = 5;
                gbc.gridwidth = 2;
                JButton addIncomeButton = new JButton("Add Income");
                addIncomeButton.setFont(font);
                addIncomeButton.addActionListener(e -> addIncome());
                inputPanel.add(addIncomeButton, gbc);
        
                // Add Expense Button
                gbc .gridy = 6;
                JButton addExpenseButton = new JButton("Add Expense");
                addExpenseButton.setFont(font);
                addExpenseButton.addActionListener(e -> addExpense());
                inputPanel.add(addExpenseButton, gbc);
        
                // Summary Button
                gbc.gridy = 7;
                JButton summaryButton = new JButton("View Summary");
                summaryButton.setFont(font);
                summaryButton.addActionListener(e -> showSummaryReport());
                inputPanel.add(summaryButton, gbc);
        
                // Reset Button
                gbc.gridy = 8;
                JButton resetButton = new JButton("Reset");
                resetButton.setFont(font);
                resetButton.addActionListener(e -> resetTracker());
                inputPanel.add(resetButton, gbc);
        
                add(inputPanel, BorderLayout.NORTH);
        
                // Record Area
                recordArea = new JTextArea();
                recordArea.setEditable(false);
                recordArea.setFont(font);
                add(new JScrollPane(recordArea), BorderLayout.CENTER);
        
                // Balance Panel
                JPanel balancePanel = new JPanel(new FlowLayout());
                balancePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                balancePanel.setBackground(Color.LIGHT_GRAY);
        
                balanceLabel = new JLabel("Current Balance: ₹0.00");
                balanceLabel.setFont(balanceFont);
                balancePanel.add(balanceLabel);
                add(balancePanel, BorderLayout.SOUTH);
        
                loadTransactions();
                setVisible(true);
            }
        
            private Object showLoginDialog() {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'showLoginDialog'");
            }
        
            private void addIncome() {
        String amountText = amountField.getText();
        String description = descriptionField.getText();
        String category = categoryField.getText();
        String account = accountField.getText();

        if (amountText.isEmpty() || description.isEmpty() || category.isEmpty() || account.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                throw new NumberFormatException();
            }
            transactions.add(new Transaction("Income", amount, description, category, account, new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
            totalIncome += amount;
            updateBalance();
            recordArea.append("Income: ₹" + String.format("%.2f", amount) + " - " + description + " (" + category + ", " + account + ") on " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "\n");
            JOptionPane.showMessageDialog(this, "Income added successfully.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered. Please enter a positive number.");
        }
        clearFields();
    }

    private void addExpense() {
        String amountText = amountField.getText();
        String description = descriptionField.getText();
        String category = categoryField.getText();
        String account = accountField.getText();

        if (amountText.isEmpty() || description.isEmpty() || category.isEmpty() || account.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                throw new NumberFormatException();
            }
            transactions.add(new Transaction("Expense", amount, description, category, account, new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
            totalExpenses += amount;
            updateBalance();
            recordArea.append("Expense: ₹" + String.format("%.2f", amount) + " - " + description + " (" + category + ", " + account + ") on " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "\n");
            JOptionPane.showMessageDialog(this, "Expense added successfully.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered. Please enter a positive number.");
        }
        clearFields();
    }

    private void updateBalance() {
        balance = totalIncome - totalExpenses;
        balanceLabel.setText("Current Balance: ₹" + String.format("%.2f", balance));
    }

    private void showSummaryReport() {
        StringBuilder summary = new StringBuilder("Summary Report:\n");
        summary.append("Total Income: ₹").append(String.format("%.2f", totalIncome)).append("\n");
        summary.append("Total Expenses: ₹").append(String.format("%.2f", totalExpenses)).append("\n");
        summary.append("Current Balance: ₹").append(String.format("%.2f", balance)).append("\n");
        JOptionPane.showMessageDialog(this, summary.toString(), "Summary Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showTransactionHistory() {
        StringBuilder history = new StringBuilder("Transaction History:\n");
        for (Transaction transaction : transactions) {
            history.append (transaction.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(this, history.toString(), "Transaction History", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportRecordsToFile() {
        try (FileWriter writer = new FileWriter("transaction_records.csv")) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.toString() + "\n");
            }
            JOptionPane.showMessageDialog(this, "Records exported successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error exporting records.");
        }
    }

    private void resetTracker() {
        balance = 0;
        totalIncome = 0;
        totalExpenses = 0;
        transactions.clear();
        recordArea.setText("");
        updateBalance();
        budgetField.setText("");
    }

    private void clearFields() {
        amountField.setText("");
        descriptionField.setText("");
        categoryField.setText("");
        accountField.setText("");
        budgetField.setText("");
    }

    private void loadTransactions() {
        try (BufferedReader reader = new BufferedReader(new FileReader("transaction_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String type = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    String description = parts[2];
                    String category = parts[3];
                    String account = parts[4];
                    String date = parts[5];
                    transactions.add(new Transaction(type, amount, description, category, account, date));
                    recordArea.append(type + ": ₹" + String.format("%.2f", amount) + " - " + description + " (" + category + ", " + account + ") on " + date + "\n");
                }
            }
        } catch (IOException e) {
            // Handle file not found or other IO exceptions
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseIncomeTracker::new);
    }

    private static class Transaction {
        private String type;
        private double amount;
        private String description;
        private String category;
        private String account;
        private String date;

        public Transaction(String type, double amount, String description, String category, String account, String date) {
            this.type = type;
            this.amount = amount;
            this.description = description;
            this.category = category;
            this.account = account;
            this.date = date;
        }

        @Override
        public String toString() {
            return type + ": ₹" + String.format("%.2f", amount) + " - " + description + " (" + category + ", " + account + ") on " + date;
        }
    }
}