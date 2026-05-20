package view;

import controller.EnergyController;
import factory.ApplianceFactory;
import model.*;
import observer.HomeownerObserver;
import observer.TechnicianObserver;
import security.SecurityUtil;
import strategy.GreenEnergyPricing;
import strategy.PeakHourPricing;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class SmartHomeApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private HashMap<String, String> users = new HashMap<>();

    private EnergyManagementSystem system;
    private EnergyController controller;

    private JLabel usageLabel;
    private JLabel costLabel;
    private JLabel pricingLabel;
    private JTextArea notificationArea;

    private EnergyGraphPanel graphPanel;

    public SmartHomeApp() {

        users.put("kaif", SecurityUtil.hashPassword("12345"));

        system = EnergyManagementSystem.getInstance();
        controller = new EnergyController(system);

        Homeowner homeowner = new Homeowner(1, "Kaif", "kaif@email.com", "+447123456789");

        system.addObserver(new HomeownerObserver(homeowner));
        system.addObserver(new TechnicianObserver("technician@email.com"));

        controller.addAppliance(ApplianceFactory.createAppliance("light", 101, "Living Room Light", 2.5));
        controller.addAppliance(ApplianceFactory.createAppliance("ac", 102, "Bedroom AC", 12.0));
        controller.addAppliance(ApplianceFactory.createAppliance("fridge", 103, "Kitchen Fridge", 8.0));

        setTitle("Smart Home Energy Management System");
        setSize(1250, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPage(), "login");
        mainPanel.add(registerPage(), "register");
        mainPanel.add(dashboardPage(), "dashboard");
        mainPanel.add(logoutPage(), "logout");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
        setVisible(true);
    }

    private JPanel loginPage() {
        JPanel panel = backgroundPanel();
        JPanel card = createCard(450, 380);

        card.add(titleLabel("SHEMS Login"));

        JTextField usernameField = inputField("Username");
        JPasswordField passwordField = passwordField();

        JButton loginBtn = primaryButton("Login");
        JButton registerBtn = secondaryButton("Create New Account");

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (users.containsKey(username)
                    && users.get(username).equals(SecurityUtil.hashPassword(password))) {
                updateDashboard();
                cardLayout.show(mainPanel, "dashboard");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        });

        registerBtn.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        card.add(usernameField);
        card.add(passwordField);
        card.add(loginBtn);
        card.add(registerBtn);

        panel.add(card);
        return panel;
    }

    private JPanel registerPage() {
        JPanel panel = backgroundPanel();
        JPanel card = createCard(480, 480);

        card.add(titleLabel("New User Registration"));

        JTextField nameField = inputField("Full Name");
        JTextField emailField = inputField("Email Address");
        JTextField usernameField = inputField("Username");
        JPasswordField passwordField = passwordField();

        JButton registerBtn = primaryButton("Register Account");
        JButton backBtn = secondaryButton("Back to Login");

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password required.");
            } else if (users.containsKey(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists.");
            } else {
                users.put(username, SecurityUtil.hashPassword(password));
                JOptionPane.showMessageDialog(this, "Registration successful.");
                cardLayout.show(mainPanel, "login");
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        card.add(nameField);
        card.add(emailField);
        card.add(usernameField);
        card.add(passwordField);
        card.add(registerBtn);
        card.add(backBtn);

        panel.add(card);
        return panel;
    }

    private JPanel dashboardPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 70, 40));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 70, 40));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));

        JLabel header = new JLabel("Smart Home Energy Dashboard");
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 30));

        JButton logoutBtn = dangerButton("Logout");
        logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "logout"));

        topPanel.add(header, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);

        JPanel cards = new JPanel(new GridLayout(2, 4, 25, 25));
        cards.setBackground(new Color(0, 70, 40));
        cards.setBorder(BorderFactory.createEmptyBorder(15, 25, 25, 25));

        cards.add(applianceCard("Living Room Light", 101));
        cards.add(applianceCard("Bedroom AC", 102));
        cards.add(applianceCard("Kitchen Fridge", 103));

        graphPanel = new EnergyGraphPanel();
        cards.add(graphPanel);

        cards.add(summaryCard());
        cards.add(pricingCard());
        cards.add(adminCard());
        cards.add(renewableCard());

        notificationArea = new JTextArea(9, 30);
        notificationArea.setEditable(false);
        notificationArea.setFont(new Font("Consolas", Font.BOLD, 15));
        notificationArea.setBackground(new Color(240, 255, 245));
        notificationArea.setForeground(new Color(0, 70, 35));
        notificationArea.setMargin(new Insets(14, 14, 14, 14));
        notificationArea.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 160, 80), 3),
                "System Notifications",
                0,
                0,
                new Font("Arial", Font.BOLD, 17),
                new Color(0, 80, 40)
        ));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(cards, BorderLayout.CENTER);
        panel.add(new JScrollPane(notificationArea), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel applianceCard(String name, int id) {
        JPanel card = smallCard();

        JLabel title = cardTitle(name);
        JLabel status = new JLabel("Status: OFF");
        status.setFont(new Font("Arial", Font.BOLD, 14));

        JButton onBtn = primaryButton("Turn ON");
        JButton offBtn = dangerButton("Turn OFF");
        JButton scheduleBtn = secondaryButton("Schedule Device");

        onBtn.addActionListener(e -> {
            controller.turnOnDevice(id);
            status.setText("Status: ON");
            updateDashboard();
            notificationArea.append(name + " turned ON by user.\n");
        });

        offBtn.addActionListener(e -> {
            controller.turnOffDevice(id);
            status.setText("Status: OFF");
            updateDashboard();
            notificationArea.append(name + " turned OFF by user.\n");
        });

        scheduleBtn.addActionListener(e -> {
            String time = JOptionPane.showInputDialog(this, "Enter schedule time for " + name + ":");
            if (time != null && !time.trim().isEmpty()) {
                notificationArea.append(name + " scheduled at " + time + ".\n");
            }
        });

        card.add(title);
        card.add(status);
        card.add(onBtn);
        card.add(offBtn);
        card.add(scheduleBtn);

        return card;
    }

    private JPanel summaryCard() {
        JPanel card = smallCard();

        usageLabel = new JLabel("Usage: 0.0 kWh");
        costLabel = new JLabel("Cost: £0.0");

        JProgressBar bar = new JProgressBar(0, 50);
        bar.setValue(0);
        bar.setStringPainted(true);
        bar.setString("Live Usage");

        JButton checkBtn = primaryButton("Check Energy Usage");

        checkBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Enter usage threshold in kWh:");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    double threshold = Double.parseDouble(input);
                    controller.checkHighUsage(threshold);
                    updateDashboard();
                    notificationArea.append("Usage checked against " + threshold + " kWh.\n");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter valid number.");
                }
            }
        });

        card.add(cardTitle("Energy Summary"));
        card.add(usageLabel);
        card.add(costLabel);
        card.add(bar);
        card.add(checkBtn);

        return card;
    }

    private JPanel pricingCard() {
        JPanel card = smallCard();

        pricingLabel = new JLabel("Current: Flat Rate Pricing");

        JButton peakBtn = primaryButton("Apply Peak Hour Pricing");
        JButton greenBtn = secondaryButton("Apply Green Energy Discount");

        peakBtn.addActionListener(e -> {
            controller.changePricingPlan(new PeakHourPricing());
            updateDashboard();
            notificationArea.append("Peak hour pricing applied.\n");
        });

        greenBtn.addActionListener(e -> {
            controller.changePricingPlan(new GreenEnergyPricing());
            updateDashboard();
            notificationArea.append("Green energy discount applied.\n");
        });

        card.add(cardTitle("Pricing Plan"));
        card.add(pricingLabel);
        card.add(peakBtn);
        card.add(greenBtn);

        return card;
    }

    private JPanel adminCard() {
        JPanel card = smallCard();

        JButton faultBtn = dangerButton("Simulate Device Fault");
        JButton reportBtn = secondaryButton("Generate Energy Report");

        faultBtn.addActionListener(e -> {
            system.getAppliances().get(1).setMalfunction(true);
            controller.checkDeviceFaults();
            notificationArea.append("Device fault detected. Technician notified.\n");
        });

        reportBtn.addActionListener(e -> {
            String reportName = JOptionPane.showInputDialog(this, "Enter report name:");
            if (reportName != null && !reportName.trim().isEmpty()) {
                notificationArea.append("Report generated: " + reportName + ".\n");
                notificationArea.append("Total Usage: " + system.calculateTotalUsage()
                        + " kWh | Cost: £" + system.calculateTotalCost() + "\n");
            }
        });

        card.add(cardTitle("Admin Panel"));
        card.add(new JLabel("Manage users, pricing and devices"));
        card.add(faultBtn);
        card.add(reportBtn);

        return card;
    }

    private JPanel renewableCard() {
        JPanel card = smallCard();

        JButton solarBtn = primaryButton("Enable Solar Energy");
        JButton windBtn = secondaryButton("Enable Wind Energy");
        JButton addBtn = primaryButton("Add Renewable Source");
        JButton generateBtn = secondaryButton("Generate Renewable Energy");

        solarBtn.addActionListener(e ->
                notificationArea.append("Solar energy integration enabled.\n")
        );

        windBtn.addActionListener(e ->
                notificationArea.append("Wind energy integration enabled.\n")
        );

        addBtn.addActionListener(e -> {
            String source = JOptionPane.showInputDialog(this, "Enter renewable source:");
            if (source != null && !source.trim().isEmpty()) {
                notificationArea.append("Renewable source added: " + source + ".\n");
            }
        });

        generateBtn.addActionListener(e -> {
            String amount = JOptionPane.showInputDialog(this, "Enter generated energy amount in kWh:");
            if (amount != null && !amount.trim().isEmpty()) {
                try {
                    double generated = Double.parseDouble(amount);
                    controller.changePricingPlan(new GreenEnergyPricing());
                    updateDashboard();
                    notificationArea.append("Renewable energy generated: " + generated + " kWh.\n");
                    notificationArea.append("Green discount applied automatically.\n");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter valid kWh value.");
                }
            }
        });

        card.add(cardTitle("Renewable Energy"));
        card.add(new JLabel("Solar / Wind / Hydro support"));
        card.add(solarBtn);
        card.add(windBtn);
        card.add(addBtn);
        card.add(generateBtn);

        return card;
    }

    private JPanel logoutPage() {
        JPanel panel = backgroundPanel();
        JPanel card = createCard(430, 300);

        JLabel message = new JLabel("Are you sure you want to logout?");
        message.setHorizontalAlignment(SwingConstants.CENTER);

        JButton yesBtn = dangerButton("Yes, Logout");
        JButton cancelBtn = secondaryButton("Cancel");

        yesBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        cancelBtn.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));

        card.add(titleLabel("Logout Page"));
        card.add(message);
        card.add(yesBtn);
        card.add(cancelBtn);

        panel.add(card);
        return panel;
    }

    private void updateDashboard() {
        double lightUsage = system.getAppliances().get(0).getCurrentUsage();
        double acUsage = system.getAppliances().get(1).getCurrentUsage();
        double fridgeUsage = system.getAppliances().get(2).getCurrentUsage();

        usageLabel.setText("Usage: " + system.calculateTotalUsage() + " kWh");
        costLabel.setText("Cost: £" + system.calculateTotalCost());
        pricingLabel.setText("Current: " + system.getPricingPlanName());

        if (graphPanel != null) {
            graphPanel.updateUsage(lightUsage, acUsage, fridgeUsage);
        }
    }

    private JPanel backgroundPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 70, 40));
        return panel;
    }

    private JPanel createCard(int width, int height) {
        JPanel card = new JPanel(new GridLayout(0, 1, 12, 12));
        card.setPreferredSize(new Dimension(width, height));
        card.setBackground(new Color(220, 255, 225));
        card.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        return card;
    }

    private JPanel smallCard() {
        JPanel card = new JPanel(new GridLayout(0, 1, 14, 14));
        card.setBackground(new Color(210, 255, 220));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 180, 80), 3),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return card;
    }

    private JLabel titleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(new Color(0, 75, 35));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JLabel cardTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(new Color(0, 70, 35));
        return label;
    }

    private JTextField inputField(String title) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createTitledBorder(title));
        return field;
    }

    private JPasswordField passwordField() {
        JPasswordField field = new JPasswordField();
        field.setBorder(BorderFactory.createTitledBorder("Password"));
        return field;
    }

    private JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setBackground(new Color(0, 255, 120));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.BOLD, 17));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 120, 40), 3),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));
        return btn;
    }

    private JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setBackground(new Color(180, 255, 200));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 140, 70), 3),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));
        return btn;
    }

    private JButton dangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setBackground(new Color(255, 70, 70));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.BOLD, 17));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 0, 0), 3),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));
        return btn;
    }
}