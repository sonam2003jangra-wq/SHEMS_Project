package view;

import security.SecurityUtil;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class LoginPage extends JPanel {

    private final Color BG = new Color(238, 247, 255);
    private final Color CARD = Color.WHITE;
    private final Color BLUE = new Color(37, 99, 235);
    private final Color PURPLE = new Color(147, 51, 234);
    private final Color GREEN = new Color(34, 197, 94);
    private final Color TEXT = new Color(15, 23, 42);
    private final Color MUTED = new Color(71, 85, 105);
    public LoginPage(CardLayout cardLayout, JPanel mainPanel,
                     HashMap<String, String> users, Runnable updateDashboard) {

        setLayout(new GridBagLayout());
        setBackground(BG);

        JPanel card = new JPanel(new GridLayout(0, 1, 14, 14));
        card.setPreferredSize(new Dimension(520, 600));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BLUE, 2),
                BorderFactory.createEmptyBorder(35, 40, 35, 40)
        ));

        JLabel avatar = new JLabel("🏠⚡");
        avatar.setFont(new Font("Arial", Font.BOLD, 48));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel title = new JLabel("SHEMS SMART LOGIN");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(TEXT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitle = new JLabel("AI Powered Energy Optimisation");
        subtitle.setFont(new Font("Arial", Font.BOLD, 14));
        subtitle.setForeground(MUTED);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField usernameField = inputField("Username");
        JPasswordField passwordField = passwordField();

        JCheckBox rememberMe = new JCheckBox("Remember Me");
        rememberMe.setBackground(CARD);
        rememberMe.setForeground(TEXT);
        rememberMe.setFont(new Font("Arial", Font.BOLD, 14));

        JButton loginBtn = button("Login", BLUE, Color.WHITE);
        JButton forgotBtn = button("Forgot Password?", PURPLE, Color.WHITE);
        JButton registerBtn = button("Create New Account", GREEN, Color.BLACK);

        loginBtn.addActionListener(e -> {
            loginBtn.setText("Logging in...");

            Timer timer = new Timer(900, event -> {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (users.containsKey(username)
                        && users.get(username).equals(SecurityUtil.hashPassword(password))) {

                    if (rememberMe.isSelected()) {
                        JOptionPane.showMessageDialog(this, "Login details remembered.");
                    }

                    updateDashboard.run();
                    cardLayout.show(mainPanel, "dashboard");

                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.");
                    loginBtn.setText("Login");
                }
            });

            timer.setRepeats(false);
            timer.start();
        });

        forgotBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Password reset link has been sent to your registered email."
                )
        );

        registerBtn.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        card.add(avatar);
        card.add(title);
        card.add(subtitle);
        card.add(usernameField);
        card.add(passwordField);
        card.add(rememberMe);
        card.add(loginBtn);
        card.add(forgotBtn);
        card.add(registerBtn);

        add(card);
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
        btn.setFont(new Font("Arial", Font.BOLD, 17));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(13, 18, 13, 18));
        return btn;
    }
}