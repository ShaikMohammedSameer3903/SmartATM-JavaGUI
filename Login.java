

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Login extends JFrame {
    private JTextField accountField;
    private JPasswordField passwordField;
    private JLabel captchaLabel;
    private JTextField captchaField;
    private String currentCaptchaCode;
    private static final String[] validAccountNumbers = {"123456", "789012", "345678", "901234", "567890", "123123", "456456", "789789", "000111", "222333"};
    private static final String[] validPasswords = {"1234", "5678", "9012", "3456", "7890", "1122", "3344", "5566", "7788", "9900"};

    public Login() {
        super("Login");
        setResizable(false);
        setLocationRelativeTo(null);
        buildLogin();
        pack();
        setSize(400, 200);
        setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    private void buildLogin() {
        // Create components
        JPanel panel = new JPanel();
        JLabel accountLabel = new JLabel("Account Number:");
        JLabel passwordLabel = new JLabel("Password:");
        captchaLabel = new JLabel("Captcha:");
        captchaField = new JTextField(10);
        JButton refreshButton = new JButton("Refresh");

        accountField = new JTextField(15);
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        // Set layout
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add some spacing

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(accountLabel, gbc);
        gbc.gridx = 1;
        panel.add(accountField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        gbc.gridx = 0; // Move captcha label to the same column as username and password labels
        gbc.gridy = 2;
        panel.add(captchaLabel, gbc);
        gbc.gridx = 1; // Move captcha field to the same column as username and password fields
        gbc.gridwidth = 2; // Span captcha field across two columns
        panel.add(captchaField, gbc);
        gbc.gridx = 0; // Move refresh button to the next row
        gbc.gridy = 3;
        gbc.gridwidth = 1; // Refresh button only occupies one column
        panel.add(refreshButton, gbc);
        gbc.gridx = 1; // Move login button to the same row as refresh button
        gbc.gridwidth = 2; // Login button spans two columns
        panel.add(loginButton, gbc);

        // Set background color
        panel.setBackground(new Color(0, 128, 128));

        // Add panel to the frame
        add(panel, BorderLayout.CENTER);

        // Add action listener for login button
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // You can implement your authentication logic here
                String accountNumber = accountField.getText();
                char[] password = passwordField.getPassword();
                String enteredCaptcha = captchaField.getText();

                // Validate the captcha
                if (checkCaptcha(enteredCaptcha)) {
                    // Validate the account number and password
                    if (isValidAccount(accountNumber, password)) {
                        dispose(); // Close the login window
                        new ATM(); // Open the ATM window
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid account number or password. Try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        // Clear the password field
                        passwordField.setText("");
                        // Generate a new captcha
                        generateCaptcha();
                        captchaField.setText(""); // Clear the captcha field
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Captcha. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    captchaField.setText(""); // Clear the captcha field
                    generateCaptcha();
                }
            }
        });

        // Add action listener for refresh button
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                generateCaptcha();
            }
        });
        generateCaptcha();
    }

    private void generateCaptcha() {
        currentCaptchaCode = generateRandomCaptcha();
        captchaLabel.setText("Captcha: " + currentCaptchaCode);
    }

    private boolean checkCaptcha(String enteredCaptcha) {
        return enteredCaptcha.equalsIgnoreCase(currentCaptchaCode);
    }

    private String generateRandomCaptcha() {
        // Generate a simple random captcha code (you can customize this as needed)
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder captchaCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * characters.length());
            captchaCode.append(characters.charAt(index));
        }
        return captchaCode.toString();
    }

    private boolean isValidAccount(String accountNumber, char[] password) {
        // In a real application, you would validate the account number and password
        // against a database or another secure mechanism.
        // This is just a placeholder for demonstration purposes.
        for (int i = 0; i < validAccountNumbers.length; i++) {
            if (accountNumber.equals(validAccountNumbers[i]) && new String(password).equals(validPasswords[i])) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}

