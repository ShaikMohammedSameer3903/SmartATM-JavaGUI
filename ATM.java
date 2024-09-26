import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ATM extends JFrame {
    double balanceINR = 10;
    double balanceGBP = 0;
    String[] inputSequence = new String[4];
    String[] transactionHist = new String[100];
    int inputSequenceIndex = 0;
    int transactionIndex = 0;
    boolean readyToEnter = false;
    int exchangeRate = 100;
    private static final String TRANSACTION_FILE_PATH = "C:/Users/shaik/OneDrive/Desktop/javau/transaction_history.txt";

    public ATM() {
        super("ATM - $pec!alCh@ract3rs 123");
        for (int i = 0; i <= 3; i++) {
            inputSequence[i] = "";
        }
        setResizable(false);
        setLocationRelativeTo(null);
        buildApp();
        pack();
        setSize(600, 350);
        setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    protected void buildApp() {
        JLabel displayArea = new JLabel(
                "<html>Instruction Area: <br> Please select a function from the buttons below <br> Current Balance: \u20B9 "
                        + balanceINR + "</html>");
        displayArea.setOpaque(true);
        displayArea.setBackground(new Color(0, 128, 128));
        displayArea.setPreferredSize(new Dimension(100, 100));
        JPanel bottomArea = new JPanel();
        bottomArea.setLayout(new BorderLayout(0, 0));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 5, 5));

        JLabel inputDisplay = new JLabel("Input Area:");
        inputDisplay.setBorder(BorderFactory.createLineBorder(Color.black));
        bottomArea.add(inputDisplay, BorderLayout.NORTH);

        JButton number1 = new JButton("1");
        buttonPanel.add(number1);
        JButton number2 = new JButton("2");
        buttonPanel.add(number2);
        JButton number3 = new JButton("3");
        buttonPanel.add(number3);
        JButton customWithdraw = new JButton("Withdraw");
        buttonPanel.add(customWithdraw);
        JButton number4 = new JButton("4");
        buttonPanel.add(number4);
        JButton number5 = new JButton("5");
        buttonPanel.add(number5);
        JButton number6 = new JButton("6");
        buttonPanel.add(number6);
        JButton deposit = new JButton("Deposit");
        buttonPanel.add(deposit);
        JButton number7 = new JButton("7");
        buttonPanel.add(number7);
        JButton number8 = new JButton("8");
        buttonPanel.add(number8);
        JButton number9 = new JButton("9");
        buttonPanel.add(number9);
        JButton changePIN = new JButton("Change PIN");
        buttonPanel.add(changePIN);
        JButton number0 = new JButton("0");
        buttonPanel.add(number0);
        JButton clear = new JButton("Clear");
        buttonPanel.add(clear);
        JButton enter = new JButton("Enter");
        buttonPanel.add(enter);
        JButton quit = new JButton("Quit");
        buttonPanel.add(quit);

        bottomArea.add(buttonPanel, BorderLayout.CENTER);
        add(displayArea, BorderLayout.NORTH);
        add(bottomArea, BorderLayout.CENTER);

        Component[] components = buttonPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setBackground(new Color(255, 255, 153));
            }
        }

        customWithdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String userInput = JOptionPane.showInputDialog("Enter withdrawal amount:");
                try {
                    double withdrawalAmountINR = Double.parseDouble(userInput);
                    if (withdrawalAmountINR <= 0) {
                        displayArea.setText("Invalid withdrawal amount. Please enter a positive value.");
                    } else if (withdrawalAmountINR > balanceINR) {
                        displayArea.setText("Insufficient funds!");
                    } else {
                        balanceINR -= withdrawalAmountINR;
                        displayArea.setText("<html>₹" + withdrawalAmountINR + " Withdrawn! <br><br>" + finishedTransaction() + "</html>");
                        System.out.println("User Has Withdrawn ₹" + withdrawalAmountINR);
                        updateTransactionHist("User Has Withdrawn ₹" + withdrawalAmountINR);
                        saveTransactionHistoryToFile(TRANSACTION_FILE_PATH);
                    }
                } catch (NumberFormatException e) {
                    displayArea.setText("Invalid input. Please enter a valid number.");
                }
            }
        });

        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null, "Your Receipt: \n" + printReceipt());
                JOptionPane.showMessageDialog(null, "Logging Out! Returning to Login Screen!");
                dispose();
                new Login();
            }
        });

        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                inputDisplay.setText("Input Display: ");
                displayArea.setText("<html>Input Area Cleared! <br><br>" + finishedTransaction() + "</html>");
                clearInput();
                readyToEnter = false;
            }
        });
        clear.setBackground(new Color(255, 0, 0));

        number1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                inputDisplay.setText("Input Display: " + updateInput("1"));
            }
        });

        deposit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String userInput = JOptionPane.showInputDialog("Enter deposit amount:");
                try {
                    int depositAmountINR = Integer.parseInt(userInput);
                    if (depositAmountINR <= 0) {
                        displayArea.setText("Invalid deposit amount. Please enter a positive value.");
                    } else {
                        balanceINR += depositAmountINR;
                        displayArea.setText("<html>You have deposited ₹" + depositAmountINR + "! <br><br>" + finishedTransaction() + "</html>");
                        System.out.println("User Has Deposited ₹" + depositAmountINR);
                        updateTransactionHist("User Has Deposited ₹" + depositAmountINR);
                        saveTransactionHistoryToFile(TRANSACTION_FILE_PATH);
                    }
                } catch (NumberFormatException e) {
                    displayArea.setText("Invalid input. Please enter a valid number.");
                }
            }
        });

        enter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (readyToEnter) {
                    try {
                        double withdrawalAmountINR = Double.parseDouble(getInputSequence());
                        if (withdrawalAmountINR <= 0) {
                            displayArea.setText("Invalid withdrawal amount. Please enter a positive value.");
                        } else if (withdrawalAmountINR > balanceINR) {
                            displayArea.setText("Insufficient funds!");
                        } else {
                            balanceINR -= withdrawalAmountINR;
                            displayArea.setText("<html>₹" + withdrawalAmountINR + " Withdrawn! <br><br>" + finishedTransaction() + "</html>");
                            System.out.println("User Has Withdrawn ₹" + withdrawalAmountINR);
                            updateTransactionHist("User Has Withdrawn ₹" + withdrawalAmountINR);
                            saveTransactionHistoryToFile(TRANSACTION_FILE_PATH);
                        }
                    } catch (NumberFormatException e) {
                        displayArea.setText("Invalid input. Please enter a valid number.");
                    }

                    clearInput();
                    inputDisplay.setText("Input Display: ");
                    readyToEnter = false;
                } else {
                    displayArea.setText("<html> You have not yet chosen an action! <br><br>" + finishedTransaction() + "</html>");
                    clearInput();
                    inputDisplay.setText("Input Display: ");
                }
            }
        });

        changePIN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String newPIN = JOptionPane.showInputDialog("Enter new PIN:");
                if (newPIN != null && newPIN.length() == 4 && newPIN.matches("\\d+")) {
                    updateTransactionHist("User Has Changed PIN to " + newPIN);
                    saveTransactionHistoryToFile(TRANSACTION_FILE_PATH);
                    JOptionPane.showMessageDialog(null, "PIN changed successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid PIN. Please enter a 4-digit number.");
                }
            }
        });
        enter.setBackground(new Color(0, 255, 0));
    }

    void clearInput() {
        for (int i = 0; i <= 3; i++) {
            inputSequence[i] = "";
        }
        inputSequenceIndex = 0;
    }

    String updateInput(String n) {
        if (inputSequenceIndex <= 3) {
            inputSequence[inputSequenceIndex] = n;
            inputSequenceIndex++;

            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < inputSequence.length; i++) {
                strBuilder.append(inputSequence[i]);
            }
            String newString = strBuilder.toString();
            return newString;
        } else {
            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i <= 3; i++) {
                strBuilder.append(inputSequence[i]);
            }
            String newString = strBuilder.toString();
            return newString;
        }
    }

    String getInputSequence() {
        StringBuilder strBuilder = new StringBuilder();
        if (inputSequence[0].equals("")) {
            return "0000";
        } else {
            for (int i = 0; i < inputSequence.length; i++) {
                strBuilder.append(inputSequence[i]);
            }
            String newString = strBuilder.toString();
            return newString;
        }
    }

    String finishedTransaction() {
        return "Instruction Area: <br> Please select a function from the buttons below <br> Current Balance: \u20B9"
                + balanceINR;
    }

    void updateTransactionHist(String action) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateTime = dateFormat.format(date);
        String transactionEntry = dateTime + " - " + action;
        transactionHist[transactionIndex] = transactionEntry;
        transactionIndex++;
    }

    String printReceipt() {
        if (transactionIndex == 0) {
            return "No Transactions Made!";
        } else {
            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < transactionIndex; i++) {
                strBuilder.append(transactionHist[i] + "\n");
            }
            String newString = strBuilder.toString();
            return newString;
        }
    }

    private void saveTransactionHistoryToFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (String transaction : transactionHist) {
                if (transaction != null) {
                    writer.println(transaction);
                }
            }
            System.out.println("Transaction history saved to file: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving transaction history to file: " + filePath);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ATM atm = new ATM();
        });
    }
}
