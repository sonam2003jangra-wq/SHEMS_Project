package view;

import javax.swing.*;
import java.awt.*;

public class EnergyGraphPanel extends JPanel {

    private double[] usageData = {0.0, 0.0, 0.0};
    private String[] labels = {"Light", "AC", "Fridge"};

    public EnergyGraphPanel() {
        setBackground(new Color(240, 249, 255));
        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(37, 99, 235), 2),
                "Live Energy Usage Graph"
        ));
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
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = 35;
        int yBase = getHeight() - 45;
        int barWidth = 46;
        int maxBarHeight = 150;
        double maxUsage = 15.0;

        Color[] colors = {
                new Color(37, 99, 235),
                new Color(14, 165, 233),
                new Color(34, 197, 94)
        };

        for (int i = 0; i < usageData.length; i++) {
            int barHeight = (int) ((usageData[i] / maxUsage) * maxBarHeight);

            GradientPaint gradient = new GradientPaint(
                    x, yBase - barHeight, colors[i],
                    x, yBase, colors[i].brighter()
            );

            g2.setPaint(gradient);
            g2.fillRoundRect(x, yBase - barHeight, barWidth, barHeight, 15, 15);

            g2.setColor(new Color(15, 23, 42));
            g2.drawString(usageData[i] + " kWh", x - 5, yBase - barHeight - 10);
            g2.drawString(labels[i], x + 2, yBase + 22);

            x += 72;
        }
    }
}