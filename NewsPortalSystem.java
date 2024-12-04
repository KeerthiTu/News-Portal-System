import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class NewsPortalSystem {
    // Data storage for users and articles
    private HashMap<String, String> users = new HashMap<>();
    private HashMap<String, ArrayList<String>> articles = new HashMap<>();
    private String currentUser = null;

    // GUI components
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public NewsPortalSystem() {
        // Initialize categories
        articles.put("Politics", new ArrayList<>());
        articles.put("Sports", new ArrayList<>());
        articles.put("Technology", new ArrayList<>());

        // Predefined admin account
        users.put("admin", "admin");

        // Initialize GUI
        frame = new JFrame("News Portal System");
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add panels
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");
        mainPanel.add(createAdminPanel(), "Admin");
        mainPanel.add(createUserPanel(), "User");

        frame.add(mainPanel);
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Authentication Module: Login Panel
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("Login");
        JButton btnRegister = new JButton("Register");

        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (users.containsKey(username) && users.get(username).equals(password)) {
                currentUser = username;
                if (username.equals("admin")) {
                    cardLayout.show(mainPanel, "Admin");
                } else {
                    cardLayout.show(mainPanel, "User");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRegister.addActionListener(e -> cardLayout.show(mainPanel, "Register"));

        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnRegister);

        return panel;
    }

    // Authentication Module: Registration Panel
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnRegister = new JButton("Register");
        JButton btnBack = new JButton("Back");

        btnRegister.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (users.containsKey(username)) {
                JOptionPane.showMessageDialog(frame, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                users.put(username, password);
                JOptionPane.showMessageDialog(frame, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(mainPanel, "Login");
            }
        });

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnRegister);
        panel.add(btnBack);

        return panel;
    }

    // Admin Panel Module
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> categoryBox = new JComboBox<>(articles.keySet().toArray(new String[0]));
        JTextField txtArticle = new JTextField();
        JTextArea txtDisplay = new JTextArea();
        txtDisplay.setEditable(false);

        JButton btnAdd = new JButton("Add Article");
        JButton btnRemove = new JButton("Remove Article");
        JButton btnLogout = new JButton("Logout");

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryBox);
        inputPanel.add(new JLabel("Article:"));
        inputPanel.add(txtArticle);
        inputPanel.add(btnAdd);
        inputPanel.add(btnRemove);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtDisplay), BorderLayout.CENTER);
        panel.add(btnLogout, BorderLayout.SOUTH);

        // Event handlers
        btnAdd.addActionListener(e -> {
            String category = (String) categoryBox.getSelectedItem();
            String article = txtArticle.getText();
            if (!article.isEmpty()) {
                articles.get(category).add(article);
                JOptionPane.showMessageDialog(frame, "Article added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtArticle.setText("");
                updateDisplay(category, txtDisplay);
            } else {
                JOptionPane.showMessageDialog(frame, "Article cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRemove.addActionListener(e -> {
            String category = (String) categoryBox.getSelectedItem();
            String article = txtArticle.getText();
            if (articles.get(category).remove(article)) {
                JOptionPane.showMessageDialog(frame, "Article removed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtArticle.setText("");
                updateDisplay(category, txtDisplay);
            } else {
                JOptionPane.showMessageDialog(frame, "Article not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLogout.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        categoryBox.addActionListener(e -> {
            String category = (String) categoryBox.getSelectedItem();
            updateDisplay(category, txtDisplay);
        });

        return panel;
    }

    // User Dashboard Module
    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> categoryBox = new JComboBox<>(articles.keySet().toArray(new String[0]));
        JTextArea txtDisplay = new JTextArea();
        txtDisplay.setEditable(false);

        JButton btnLogout = new JButton("Logout");

        panel.add(categoryBox, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtDisplay), BorderLayout.CENTER);
        panel.add(btnLogout, BorderLayout.SOUTH);

        // Event handlers
        categoryBox.addActionListener(e -> {
            String category = (String) categoryBox.getSelectedItem();
            updateDisplay(category, txtDisplay);
        });

        btnLogout.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        return panel;
    }

    // Utility: Update displayed articles
    private void updateDisplay(String category, JTextArea txtDisplay) {
        ArrayList<String> categoryArticles = articles.get(category);
        txtDisplay.setText(String.join("\n", categoryArticles));
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(NewsPortalSystem::new);
    }
}
