import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BankBalance extends JFrame implements ActionListener {

    private double balance;

    private JTextField amountField;
    private JButton depositBtn, withdrawBtn, showBtn;

    // store history ( for viewing the balance)
    private ArrayList<String> history = new ArrayList<>();

    public BankBalance(double startingBalance) {
        balance = startingBalance;

        // Add starting balance to history
        history.add("Starting Balance: $" + startingBalance);

        // Window
        setTitle("My Cute Bank App");
        setSize(350, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 230, 255));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Input
        amountField = new JTextField();
        amountField.setPreferredSize(new Dimension(200, 40));
        amountField.setMaximumSize(new Dimension(200, 40));
        amountField.setBorder(BorderFactory.createTitledBorder("Enter amount"));
        panel.add(amountField);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Buttons
        depositBtn = createCuteButton("Deposit", new Color(200, 255, 200)); // mint
        withdrawBtn = createCuteButton("Withdraw", new Color(255, 210, 210)); // pink
        showBtn = createCuteButton("Show Balance", new Color(210, 230, 255)); // blue

        // listeners
        depositBtn.addActionListener(this);
        withdrawBtn.addActionListener(this);
        showBtn.addActionListener(this);

        // buttons
        panel.add(depositBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(withdrawBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(showBtn);

        add(panel);
        setVisible(true);
    }

    // style buttons
    private JButton createCuteButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setPreferredSize(new Dimension(150, 35));
        btn.setMaximumSize(new Dimension(150, 35));
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // If the user clicked "Show Balance", skip input checks
        if (e.getSource() == showBtn) {
            showBalanceWindow();
            return;
        }

        // For deposit/withdraw, we DO need to check the input
        String text = amountField.getText();
        double amount;

        try {
            amount = Double.parseDouble(text);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
            return;
        }

        // Deposit
        if (e.getSource() == depositBtn) {
            balance += amount;
            history.add("Deposited: $" + amount);
            JOptionPane.showMessageDialog(this, "Deposited $" + amount);
        }

        // Withdraw
        if (e.getSource() == withdrawBtn) {
            if (amount > balance) {
                JOptionPane.showMessageDialog(this, "Not enough money.");
            } else {
                balance -= amount;
                history.add("Withdrew: $" + amount);
                JOptionPane.showMessageDialog(this, "Withdrew $" + amount);
            }
        }

        amountField.setText("");
    }

    // Separate window for balance summary + history
    private void showBalanceWindow() {
        JFrame summaryFrame = new JFrame("Account Summary");
        summaryFrame.setSize(300, 300);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(230, 245, 255)); // soft baby blue
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel summaryLabel = new JLabel("Current Balance: $" + balance);
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(summaryLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // History label
        JLabel historyLabel = new JLabel("Transaction History:");
        historyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        historyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(historyLabel);

        // Scrollable history list
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String entry : history) {
            listModel.addElement(entry);
        }

        JList<String> historyList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setPreferredSize(new Dimension(250, 120));

        panel.add(scrollPane);

        summaryFrame.add(panel);
        summaryFrame.setVisible(true);
    }

    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog("Enter your starting balance:");
        double startingBalance = Double.parseDouble(input);

        BankBalance app = new BankBalance(startingBalance);

        // Show final balance when closing
        app.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Your final balance is: $" + app.balance);
            }
        });
    }
}
