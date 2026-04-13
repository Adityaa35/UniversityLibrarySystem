package ui;

import services.AuthService;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    
    public LoginFrame() {
        setTitle("University Library System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        services.ReminderThread reminderThread = new services.ReminderThread();
        reminderThread.start();
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("User ID:"), gbc);
        userIdField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(userIdField, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Role:"), gbc);
        roleCombo = new JComboBox<>(new String[]{"STUDENT", "ADMIN"});
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);
        
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> performLogin());
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);
        
        add(panel);
        setVisible(true);
    }
    
    private void performLogin() {
        String userId = userIdField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();
        
        try {
            if (AuthService.authenticate(userId, password, role)) {
                if (role.equals("STUDENT")) {
                    new StudentDashboard(userId);
                } else {
                    new AdminDashboard(userId);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}