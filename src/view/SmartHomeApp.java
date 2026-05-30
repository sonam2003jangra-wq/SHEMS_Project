package view;

import controller.EnergyController;
import factory.ApplianceFactory;
import model.*;
import observer.HomeownerObserver;
import observer.TechnicianObserver;
import security.SecurityUtil;
import strategy.FlatRatePricing;
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

    private JLabel usageLabel, costLabel, pricingLabel, carbonLabel, scoreLabel, recommendationLabel;
    private JTextArea notificationArea;
    private StringBuilder notificationHistory = new StringBuilder();

    private EnergyGraphPanel graphPanel;

    private final Color BG = new Color(219, 234, 254);
    private final Color CARD = new Color(241, 245, 249);
    private final Color BLUE = new Color(37, 99, 235);
    private final Color SKY = new Color(14, 165, 233);
    private final Color GREEN = new Color(34, 197, 94);
    private final Color PURPLE = new Color(147, 51, 234);
    private final Color ORANGE = new Color(249, 115, 22);
    private final Color RED = new Color(239, 68, 68);
    private final Color TEXT = new Color(15, 23, 42);

    public SmartHomeApp() {

        users.put("kaif", SecurityUtil.hashPassword("12345"));

        system = EnergyManagementSystem.getInstance();
        controller = new EnergyController(system);

        Homeowner homeowner = new Homeowner(1, "Kaif", "kaif@email.com", "+447123456789");
        system.addObserver(new HomeownerObserver(homeowner));
        system.addObserver(new TechnicianObserver("technician@email.com"));

        controller.addAppliance(ApplianceFactory.createAppliance("light", 101, "Smart Lighting Hub", 2.5));
        controller.addAppliance(ApplianceFactory.createAppliance("ac", 102, "Climate Control Centre", 12.0));
        controller.addAppliance(ApplianceFactory.createAppliance("fridge", 103, "Food Storage Monitor", 8.0));

        setTitle("Smart Home Energy Management System");
        setSize(1500, 980);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new LoginPage(cardLayout, mainPanel, users, this::updateDashboard), "login");
        mainPanel.add(registerPage(), "register");
        mainPanel.add(dashboardPage(), "dashboard");
        mainPanel.add(devicesPage(), "devices");
        mainPanel.add(graphPage(), "graph");
        mainPanel.add(analyticsPage(), "analytics");
        mainPanel.add(renewablePage(), "renewable");
        mainPanel.add(notificationPage(), "notifications");
        mainPanel.add(reportsPage(), "reports");
        mainPanel.add(adminPage(), "admin");
        mainPanel.add(logoutPage(), "logout");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
        setVisible(true);
    }

    private JPanel registerPage() {
        JPanel panel = backgroundPanel();
        JPanel card = createCard(520, 560);

        card.add(titleLabel("Create SHEMS Account"));
        card.add(subtitleLabel("Smart Energy • Secure Access • Clean Living"));

        JTextField nameField = inputField("Full Name");
        JTextField emailField = inputField("Email Address");
        JTextField usernameField = inputField("Username");
        JPasswordField passwordField = passwordField();

        JButton registerBtn = button("Register Account", GREEN, Color.WHITE);
        JButton backBtn = button("Back to Login", BLUE, Color.WHITE);

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
        panel.setBackground(BG);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(BG);
        northPanel.add(headerPanel(), BorderLayout.NORTH);
        northPanel.add(navigationBar(), BorderLayout.SOUTH);

        JPanel cards = new JPanel(new GridLayout(3, 4, 20, 20));
        cards.setBackground(BG);
        cards.setBorder(BorderFactory.createEmptyBorder(15, 25, 20, 25));

        cards.add(deviceCard("Smart Lighting Hub", 101));
        cards.add(deviceCard("Climate Control Centre", 102));
        cards.add(deviceCard("Food Storage Monitor", 103));

        graphPanel = new EnergyGraphPanel();
        cards.add(graphPanel);

        cards.add(analyticsCard());
        cards.add(optimisationCard());
        cards.add(adminCard());
        cards.add(renewableCard());

        cards.add(aiAssistantCard());
        cards.add(batteryCard());
        cards.add(carbonTrackerCard());
        cards.add(savingTargetCard());

        notificationArea = notificationBox(7, 30);

        panel.add(northPanel, BorderLayout.NORTH);
        panel.add(cards, BorderLayout.CENTER);
        panel.add(new JScrollPane(notificationArea), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel headerPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG);
        topPanel.setBorder(BorderFactory.createEmptyBorder(18, 25, 8, 25));

        JLabel header = new JLabel("SMART HOME ENERGY MANAGEMENT SYSTEM");
        header.setForeground(TEXT);
        header.setFont(new Font("Arial", Font.BOLD, 30));

        JLabel subHeader = new JLabel("AI Energy Assistant • Carbon Tracker • Renewable Optimisation • Smart Saving");
        subHeader.setForeground(new Color(71, 85, 105));
        subHeader.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setBackground(BG);
        titleBox.add(header);
        titleBox.add(subHeader);

        JButton logoutBtn = button("Logout", RED, Color.WHITE);
        logoutBtn.addActionListener(e -> cardLayout.show(mainPanel, "logout"));

        topPanel.add(titleBox, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel navigationBar() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        nav.setBackground(new Color(30, 64, 175));
        nav.setBorder(BorderFactory.createEmptyBorder(6, 22, 6, 22));

        nav.add(navButton("Dashboard", "dashboard"));
        nav.add(navButton("Devices", "devices"));
        nav.add(navButton("Energy Graph", "graph"));
        nav.add(navButton("Analytics", "analytics"));
        nav.add(navButton("Renewable", "renewable"));
        nav.add(navButton("Notifications", "notifications"));
        nav.add(navButton("Reports", "reports"));
        nav.add(navButton("Admin", "admin"));

        return nav;
    }

    private JButton navButton(String text, String page) {
        JButton btn = new JButton(text);
        btn.setBackground(BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(SKY);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(BLUE);
            }
        });

        btn.addActionListener(e -> {
            if (page.equals("notifications")) {
                mainPanel.add(notificationPage(), "notifications");
            }
            if (page.equals("reports")) {
                mainPanel.add(reportsPage(), "reports");
            }
            if (page.equals("graph")) {
                mainPanel.add(graphPage(), "graph");
            }
            cardLayout.show(mainPanel, page);
        });

        return btn;
    }

    private JPanel devicesPage() {
        JPanel panel = pageBase("Device Control Centre");

        JPanel grid = new JPanel(new GridLayout(1, 3, 22, 22));
        grid.setBackground(BG);
        grid.add(deviceCard("Smart Lighting Hub", 101));
        grid.add(deviceCard("Climate Control Centre", 102));
        grid.add(deviceCard("Food Storage Monitor", 103));

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel graphPage() {
        JPanel panel = pageBase("Live Energy Graph Centre");

        JPanel content = new JPanel(new BorderLayout(22, 22));
        content.setBackground(BG);

        EnergyGraphPanel largeGraph = new EnergyGraphPanel();
        largeGraph.setPreferredSize(new Dimension(1100, 650));

        largeGraph.updateUsage(
                system.getAppliances().get(0).getCurrentUsage(),
                system.getAppliances().get(1).getCurrentUsage(),
                system.getAppliances().get(2).getCurrentUsage()
        );

        JPanel graphCard = professionalCard(new BorderLayout());
        graphCard.add(largeGraph, BorderLayout.CENTER);

        JPanel sidePanel = professionalCard(new GridLayout(0, 1, 12, 12));
        sidePanel.setPreferredSize(new Dimension(330, 430));

        JButton refreshBtn = button("Refresh Graph", BLUE, Color.WHITE);
        JButton analyseBtn = button("Analyse Highest Usage", ORANGE, Color.WHITE);
        JButton exportBtn = button("Export Graph Report", PURPLE, Color.WHITE);

        refreshBtn.addActionListener(e -> {
            largeGraph.updateUsage(
                    system.getAppliances().get(0).getCurrentUsage(),
                    system.getAppliances().get(1).getCurrentUsage(),
                    system.getAppliances().get(2).getCurrentUsage()
            );
            addNotification("Graph refreshed successfully.");
        });

        analyseBtn.addActionListener(e -> {
            addNotification("Highest usage appliance analysed: Climate Control Centre.");
            JOptionPane.showMessageDialog(this, "Highest usage appliance: Climate Control Centre");
        });

        exportBtn.addActionListener(e -> {
            addNotification("Graph report exported successfully.");
            JOptionPane.showMessageDialog(this, "Graph report generated for current appliance usage.");
        });

        sidePanel.add(cardTitle("Graph Controls"));
        sidePanel.add(infoLabel("Visualise live appliance consumption."));
        sidePanel.add(refreshBtn);
        sidePanel.add(analyseBtn);
        sidePanel.add(exportBtn);

        content.add(graphCard, BorderLayout.CENTER);
        content.add(sidePanel, BorderLayout.EAST);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel analyticsPage() {
        JPanel panel = pageBase("Energy Analytics Dashboard");

        JPanel grid = new JPanel(new GridLayout(1, 3, 22, 22));
        grid.setBackground(BG);
        grid.add(analyticsCard());
        grid.add(aiAssistantCard());
        grid.add(carbonTrackerCard());

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel renewablePage() {
        JPanel panel = pageBase("Renewable Energy Centre");

        JPanel grid = new JPanel(new GridLayout(1, 3, 22, 22));
        grid.setBackground(BG);
        grid.add(renewableCard());
        grid.add(batteryCard());
        grid.add(savingTargetCard());

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel notificationPage() {
        JPanel panel = pageBase("Smart Notification Centre");

        JTextArea fullNotificationArea = notificationBox(20, 50);

        if (notificationHistory.length() > 0) {
            fullNotificationArea.setText(notificationHistory.toString());
        } else {
            fullNotificationArea.setText(
                    "No notifications available yet.\n\n" +
                            "Notifications will appear here after device control, pricing changes, " +
                            "fault scans, renewable-energy actions, graph refreshes, and report generation."
            );
        }

        panel.add(new JScrollPane(fullNotificationArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel reportsPage() {
        JPanel panel = pageBase("Monthly Energy Report");

        JTextArea report = notificationBox(20, 50);
        report.setText(
                "SHEMS Monthly Energy Report\n\n" +
                        "Total Usage: " + system.calculateTotalUsage() + " kWh\n" +
                        "Estimated Cost: £" + system.calculateTotalCost() + "\n" +
                        "Current Pricing Plan: " + system.getPricingPlanName() + "\n" +
                        "Carbon Saving Estimate: " + String.format("%.1f", Math.max(0, 20 - system.calculateTotalUsage())) + " kg CO₂\n" +
                        "System Status: Operational\n\n" +
                        "Recommendation: Use Eco Mode during peak hours and schedule appliances for off-peak periods."
        );

        panel.add(new JScrollPane(report), BorderLayout.CENTER);
        return panel;
    }

    private JPanel adminPage() {
        JPanel panel = pageBase("Admin Control Panel");

        JPanel grid = new JPanel(new GridLayout(1, 2, 22, 22));
        grid.setBackground(BG);
        grid.add(adminCard());
        grid.add(optimisationCard());

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel pageBase(String title) {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(22, 28, 28, 28));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG);
        top.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setBackground(BG);

        JLabel pageTitle = new JLabel(title);
        pageTitle.setFont(new Font("Arial", Font.BOLD, 32));
        pageTitle.setForeground(TEXT);

        JLabel subtitle = new JLabel("Professional smart-energy module for monitoring, optimisation and system control");
        subtitle.setFont(new Font("Arial", Font.BOLD, 14));
        subtitle.setForeground(new Color(71, 85, 105));

        titleBox.add(pageTitle);
        titleBox.add(subtitle);

        JButton backBtn = button("← Back to Dashboard", BLUE, Color.WHITE);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));

        top.add(titleBox, BorderLayout.WEST);
        top.add(backBtn, BorderLayout.EAST);

        panel.add(top, BorderLayout.NORTH);
        return panel;
    }

    private JPanel professionalCard(LayoutManager layout) {
        JPanel card = new JPanel(layout);
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(148, 163, 184), 2),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));
        return card;
    }

    private JPanel deviceCard(String name, int id) {
        JPanel card = smallCard();

        JLabel status = infoLabel("Status: OFF");

        JButton onBtn = button("Power ON", GREEN, Color.WHITE);
        JButton offBtn = button("Power OFF", RED, Color.WHITE);
        JButton scheduleBtn = button("Smart Schedule", PURPLE, Color.WHITE);

        onBtn.addActionListener(e -> {
            controller.turnOnDevice(id);
            status.setText("Status: ON");
            updateDashboard();
            addNotification(name + " activated successfully.");
        });

        offBtn.addActionListener(e -> {
            controller.turnOffDevice(id);
            status.setText("Status: OFF");
            updateDashboard();
            addNotification(name + " deactivated successfully.");
        });

        scheduleBtn.addActionListener(e -> {
            String time = JOptionPane.showInputDialog(this, "Enter schedule time for " + name + ":");
            if (time != null && !time.trim().isEmpty()) {
                addNotification(name + " scheduled at " + time + ".");
            }
        });

        card.add(cardTitle(name));
        card.add(status);
        card.add(onBtn);
        card.add(offBtn);
        card.add(scheduleBtn);

        return card;
    }

    private JPanel analyticsCard() {
        JPanel card = smallCard();

        usageLabel = infoLabel("Current Usage: 0.0 kWh");
        costLabel = infoLabel("Estimated Cost: £0.0");
        carbonLabel = infoLabel("Carbon Saved: 0.0 kg CO₂");
        scoreLabel = infoLabel("Efficiency Score: 100/100");

        JButton analyseBtn = button("Analyse Energy", BLUE, Color.WHITE);

        analyseBtn.addActionListener(e -> {
            updateDashboard();
            addNotification("Energy analytics updated successfully.");
        });

        card.add(cardTitle("Live Energy Analytics"));
        card.add(usageLabel);
        card.add(costLabel);
        card.add(carbonLabel);
        card.add(scoreLabel);
        card.add(analyseBtn);

        return card;
    }

    private JPanel optimisationCard() {
        JPanel card = smallCard();

        pricingLabel = infoLabel("Plan: Flat Rate Pricing");
        recommendationLabel = infoLabel("Tip: Energy usage is efficient");

        JButton peakBtn = button("Peak Pricing", ORANGE, Color.WHITE);
        JButton ecoBtn = button("Eco Mode", GREEN, Color.WHITE);
        JButton resetBtn = button("Reset Dashboard", SKY, Color.WHITE);

        peakBtn.addActionListener(e -> {
            controller.changePricingPlan(new PeakHourPricing());
            updateDashboard();
            addNotification("Peak pricing activated.");
        });

        ecoBtn.addActionListener(e -> {
            controller.changePricingPlan(new GreenEnergyPricing());
            updateDashboard();
            addNotification("Eco Mode activated. Green discount applied.");
        });

        resetBtn.addActionListener(e -> {
            for (Appliance appliance : system.getAppliances()) {
                appliance.turnOff();
            }
            controller.changePricingPlan(new FlatRatePricing());
            updateDashboard();
            addNotification("Dashboard reset successfully.");
        });

        card.add(cardTitle("Cost Optimisation Panel"));
        card.add(pricingLabel);
        card.add(recommendationLabel);
        card.add(peakBtn);
        card.add(ecoBtn);
        card.add(resetBtn);

        return card;
    }

    private JPanel adminCard() {
        JPanel card = smallCard();

        JButton faultBtn = button("Run Fault Scan", RED, Color.WHITE);
        JButton reportBtn = button("Monthly Report", PURPLE, Color.WHITE);
        JButton nightBtn = button("Night Saving Mode", BLUE, Color.WHITE);

        faultBtn.addActionListener(e -> {
            system.getAppliances().get(1).setMalfunction(true);
            controller.checkDeviceFaults();
            addNotification("Fault scan completed. Technician notified by email/SMS.");
        });

        reportBtn.addActionListener(e -> {
            addNotification("Monthly energy report generated successfully.");
            mainPanel.add(reportsPage(), "reports");
            cardLayout.show(mainPanel, "reports");
        });

        nightBtn.addActionListener(e -> {
            controller.turnOffDevice(101);
            controller.changePricingPlan(new GreenEnergyPricing());
            updateDashboard();
            addNotification("Night Saving Mode activated.");
        });

        card.add(cardTitle("Admin Control Console"));
        card.add(infoLabel("Reports • Faults • Smart Modes"));
        card.add(faultBtn);
        card.add(reportBtn);
        card.add(nightBtn);

        return card;
    }

    private JPanel renewableCard() {
        JPanel card = smallCard();

        JButton solarBtn = button("Solar Boost", ORANGE, Color.WHITE);
        JButton windBtn = button("Wind Boost", SKY, Color.WHITE);
        JButton addBtn = button("Add Source", GREEN, Color.WHITE);
        JButton generateBtn = button("Generate Green Energy", BLUE, Color.WHITE);

        solarBtn.addActionListener(e -> addNotification("Solar energy enabled."));
        windBtn.addActionListener(e -> addNotification("Wind energy enabled."));

        addBtn.addActionListener(e -> {
            String source = JOptionPane.showInputDialog(this, "Enter renewable source:");
            if (source != null && !source.trim().isEmpty()) {
                addNotification("Renewable source added: " + source + ".");
            }
        });

        generateBtn.addActionListener(e -> {
            String amount = JOptionPane.showInputDialog(this, "Enter generated energy in kWh:");
            if (amount != null && !amount.trim().isEmpty()) {
                try {
                    double generated = Double.parseDouble(amount);
                    controller.changePricingPlan(new GreenEnergyPricing());
                    updateDashboard();
                    addNotification("Green energy generated: " + generated + " kWh.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter valid kWh value.");
                }
            }
        });

        card.add(cardTitle("Renewable Energy Centre"));
        card.add(infoLabel("Solar • Wind • Hydro Simulation"));
        card.add(solarBtn);
        card.add(windBtn);
        card.add(addBtn);
        card.add(generateBtn);

        return card;
    }

    private JPanel aiAssistantCard() {
        JPanel card = smallCard();

        JButton aiBtn = button("Generate AI Tip", BLUE, Color.WHITE);

        aiBtn.addActionListener(e -> {
            double total = system.calculateTotalUsage();

            if (total > 15) {
                addNotification("AI Tip: Climate Control Centre is consuming high energy. Use Eco Mode.");
            } else {
                addNotification("AI Tip: Energy usage is currently efficient.");
            }
        });

        card.add(cardTitle("AI Energy Assistant"));
        card.add(infoLabel("Personalised saving recommendations"));
        card.add(aiBtn);

        return card;
    }

    private JPanel batteryCard() {
        JPanel card = smallCard();

        JProgressBar battery = new JProgressBar(0, 100);
        battery.setValue(76);
        battery.setStringPainted(true);
        battery.setString("Battery Storage: 76%");

        JButton optimiseBtn = button("Optimise Battery", GREEN, Color.WHITE);
        optimiseBtn.addActionListener(e -> addNotification("Battery storage level optimised for evening usage."));

        card.add(cardTitle("Battery Storage Monitor"));
        card.add(infoLabel("Renewable backup energy level"));
        card.add(battery);
        card.add(optimiseBtn);

        return card;
    }

    private JPanel carbonTrackerCard() {
        JPanel card = smallCard();

        JButton carbonBtn = button("Check Carbon Level", ORANGE, Color.WHITE);

        carbonBtn.addActionListener(e -> {
            double total = system.calculateTotalUsage();

            if (total > 20) {
                addNotification("High energy usage detected. Please reduce consumption.");
            } else {
                addNotification("Carbon Status: Consumption is within safe range.");
            }
        });

        card.add(cardTitle("Carbon Emission Tracker"));
        card.add(infoLabel("Monitor CO₂ impact"));
        card.add(carbonBtn);

        return card;
    }

    private JPanel savingTargetCard() {
        JPanel card = smallCard();

        JProgressBar target = new JProgressBar(0, 100);
        target.setValue(65);
        target.setStringPainted(true);
        target.setString("Daily Saving Target: 65%");

        JButton improveBtn = button("Improve Target", PURPLE, Color.WHITE);
        improveBtn.addActionListener(e -> addNotification("Saving Target Tip: Turn off unused appliances."));

        card.add(cardTitle("Daily Saving Target"));
        card.add(infoLabel("Track daily efficiency goal"));
        card.add(target);
        card.add(improveBtn);

        return card;
    }

    private JPanel logoutPage() {
        JPanel panel = backgroundPanel();
        JPanel card = createCard(440, 300);

        JLabel message = infoLabel("Are you sure you want to logout?");
        message.setHorizontalAlignment(SwingConstants.CENTER);

        JButton yesBtn = button("Yes, Logout", RED, Color.WHITE);
        JButton cancelBtn = button("Cancel", BLUE, Color.WHITE);

        yesBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        cancelBtn.addActionListener(e -> cardLayout.show(mainPanel, "dashboard"));

        card.add(titleLabel("Logout Page"));
        card.add(message);
        card.add(yesBtn);
        card.add(cancelBtn);

        panel.add(card);
        return panel;
    }

    private JTextArea notificationBox(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.BOLD, 17));
        area.setBackground(Color.WHITE);
        area.setForeground(new Color(22, 101, 52));
        area.setMargin(new Insets(14, 14, 14, 14));
        area.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BLUE, 2),
                "Smart Notification Centre",
                0,
                0,
                new Font("Arial", Font.BOLD, 16),
                BLUE
        ));
        return area;
    }

    private void addNotification(String message) {
        notificationHistory.append(message).append("\n");

        if (notificationArea != null) {
            notificationArea.append(message + "\n");
            notificationArea.setCaretPosition(notificationArea.getDocument().getLength());
        }
    }

    private void updateDashboard() {
        if (usageLabel == null || costLabel == null || pricingLabel == null) return;

        double lightUsage = system.getAppliances().get(0).getCurrentUsage();
        double acUsage = system.getAppliances().get(1).getCurrentUsage();
        double fridgeUsage = system.getAppliances().get(2).getCurrentUsage();
        double total = system.calculateTotalUsage();

        usageLabel.setText("Current Usage: " + total + " kWh");
        costLabel.setText("Estimated Cost: £" + system.calculateTotalCost());
        pricingLabel.setText("Plan: " + system.getPricingPlanName());

        carbonLabel.setText("Carbon Saved: " + String.format("%.1f", Math.max(0, 20 - total)) + " kg CO₂");
        scoreLabel.setText("Efficiency Score: " + Math.max(40, (int) (100 - total * 3)) + "/100");

        if (total > 15) {
            recommendationLabel.setText("Tip: Reduce AC usage to lower cost");
        } else {
            recommendationLabel.setText("Tip: Energy usage is efficient");
        }

        if (graphPanel != null) {
            graphPanel.updateUsage(lightUsage, acUsage, fridgeUsage);
        }
    }

    private JPanel backgroundPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG);
        return panel;
    }

    private JPanel createCard(int width, int height) {
        JPanel card = new JPanel(new GridLayout(0, 1, 12, 12));
        card.setPreferredSize(new Dimension(width, height));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(148, 163, 184), 2),
                BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));
        return card;
    }

    private JPanel smallCard() {
        JPanel card = new JPanel(new GridLayout(0, 1, 9, 9));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(148, 163, 184), 2),
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));
        return card;
    }

    private JLabel titleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(TEXT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JLabel subtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(71, 85, 105));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JLabel cardTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 17));
        label.setForeground(BLUE);
        return label;
    }

    private JLabel infoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(TEXT);
        return label;
    }

    private JTextField inputField(String title) {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.BOLD, 15));
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createTitledBorder(title));
        return field;
    }

    private JPasswordField passwordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Arial", Font.BOLD, 15));
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createTitledBorder("Password"));
        return field;
    }

    private JButton button(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(190, 38));
        btn.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }
}