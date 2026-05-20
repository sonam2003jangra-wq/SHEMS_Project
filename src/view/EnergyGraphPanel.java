package view;

import javax.swing.*;
import java.awt.*;

public class EnergyGraphPanel extends JPanel {

    private double[] usageData = {0.0, 0.0, 0.0};
    private String[] labels = {"Light", "AC", "Fridge"};

    public EnergyGraphPanel() {
        setBackground(new Color(210, 255, 220));
        setBorder(BorderFactory.createTitledBorder("Energy Usage Graph"));
    }

    public void updateUsage(double light, double ac, double fridge) {
        usageData[0] = light;
        usageData[1] = ac;
        usageData[2] = fridge;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(new Font("Arial", Font.BOLD, 13));

        int x = 35;
        int yBase = getHeight() - 45;
        int barWidth = 45;
        int maxBarHeight = 150;
        double maxUsage = 15.0;

        for (int i = 0; i < usageData.length; i++) {
            int barHeight = (int) ((usageData[i] / maxUsage) * maxBarHeight);

            g2.setColor(new Color(0, 255, 120));
            g2.fillRoundRect(x, yBase - barHeight, barWidth, barHeight, 12, 12);

            g2.setColor(Color.BLACK);
            g2.drawString(labels[i], x, yBase + 20);
            g2.drawString(usageData[i] + " kWh", x - 5, yBase - barHeight - 8);

            x += 70;
        }
    }
}