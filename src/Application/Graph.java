package Application;

import Fit.FitActivity;
import Fit.FitRecord;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public final class Graph extends JPanel {
    private static final double SMOOTHING_WINDOW_SECONDS = 3.0;
    private static final int AXIS_TICK_COUNT = 5;
    private static final int LEFT_MARGIN = 70;
    private static final int RIGHT_MARGIN = 25;
    private static final int TOP_MARGIN = 30;
    private static final int BOTTOM_MARGIN = 55;

    private final List<PowerPoint> points;

    public Graph(FitActivity activity) {
        points = powerPoints(activity);
        setPreferredSize(new Dimension(900, 550));
        setBackground(Color.WHITE);
    }

    public static void showPowerGraph(FitActivity activity) {
        if (java.awt.GraphicsEnvironment.isHeadless()) {
            System.out.println("Cannot display the power graph because Java is running in headless mode.");
            return;
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Power over time");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(new Graph(activity));
            frame.pack();
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (points.isEmpty()) {
                drawCenteredMessage(g, "No power data with timestamps available.");
                return;
            }

            int plotWidth = getWidth() - LEFT_MARGIN - RIGHT_MARGIN;
            int plotHeight = getHeight() - TOP_MARGIN - BOTTOM_MARGIN;
            double maxTimeMinutes = Math.max(points.get(points.size() - 1).seconds() / 60.0, 1);
            double maxPower = Math.max(points.stream().mapToDouble(PowerPoint::power).max().orElse(1), 1);
            double chartMaxPower = Math.ceil(maxPower / 100.0) * 100.0 + 100.0;

            g.setColor(Color.DARK_GRAY);
            g.drawLine(LEFT_MARGIN, TOP_MARGIN, LEFT_MARGIN, TOP_MARGIN + plotHeight);
            g.drawLine(LEFT_MARGIN, TOP_MARGIN + plotHeight,
                    LEFT_MARGIN + plotWidth, TOP_MARGIN + plotHeight);
            drawAxisTicks(g, maxTimeMinutes, chartMaxPower, plotWidth, plotHeight);

            g.setColor(new Color(35, 100, 190));
            g.setStroke(new BasicStroke(2f));
            for (int i = 1; i < points.size(); i++) {
                PowerPoint previous = points.get(i - 1);
                PowerPoint current = points.get(i);
                g.draw(new Line2D.Double(
                        x(previous.seconds() / 60.0, maxTimeMinutes, plotWidth),
                        y(previous.power(), chartMaxPower, plotHeight),
                        x(current.seconds() / 60.0, maxTimeMinutes, plotWidth),
                        y(current.power(), chartMaxPower, plotHeight)));
            }

            g.setColor(Color.DARK_GRAY);
            FontMetrics metrics = g.getFontMetrics();
            g.drawString("Time (minutes)", LEFT_MARGIN + (plotWidth - metrics.stringWidth("Time (minutes)")) / 2,
                    getHeight() - 15);
            g.rotate(-Math.PI / 2);
            g.drawString("Power (W)", -(TOP_MARGIN + plotHeight / 2 + metrics.stringWidth("Power (W)") / 2), 20);
        } finally {
            g.dispose();
        }
    }

    private void drawAxisTicks(Graphics2D g, double maxTimeMinutes, double maxPower,
                               int plotWidth, int plotHeight) {
        FontMetrics metrics = g.getFontMetrics();
        for (int i = 0; i <= AXIS_TICK_COUNT; i++) {
            double fraction = (double) i / AXIS_TICK_COUNT;
            int x = (int) Math.round(LEFT_MARGIN + fraction * plotWidth);

            g.setColor(new Color(225, 225, 225));
            g.drawLine(x, TOP_MARGIN, x, TOP_MARGIN + plotHeight);

            g.setColor(Color.DARK_GRAY);
            String timeLabel = Integer.toString((int) Math.round(fraction * maxTimeMinutes));
            g.drawString(timeLabel, x - metrics.stringWidth(timeLabel) / 2,
                    TOP_MARGIN + plotHeight + metrics.getAscent() + 8);
        }

        for (int power = 0; power <= maxPower; power += 100) {
            double fraction = power / maxPower;
            int y = (int) Math.round(TOP_MARGIN + plotHeight - fraction * plotHeight);

            g.setColor(new Color(225, 225, 225));
            g.drawLine(LEFT_MARGIN, y, LEFT_MARGIN + plotWidth, y);

            g.setColor(Color.DARK_GRAY);
            String powerLabel = Integer.toString(power);
            g.drawString(powerLabel, LEFT_MARGIN - metrics.stringWidth(powerLabel) - 8,
                    y + metrics.getAscent() / 2);
        }

    }

    private String formatAxisValue(double value) {
        return value == Math.rint(value)
                ? String.format("%.0f", value)
                : String.format("%.1f", value);
    }

    private double x(double minutes, double maxTimeMinutes, int plotWidth) {
        return LEFT_MARGIN + minutes / maxTimeMinutes * plotWidth;
    }

    private double y(double power, double maxPower, int plotHeight) {
        return TOP_MARGIN + plotHeight - power / maxPower * plotHeight;
    }

    private void drawCenteredMessage(Graphics2D g, String message) {
        FontMetrics metrics = g.getFontMetrics();
        g.setColor(Color.DARK_GRAY);
        g.drawString(message, (getWidth() - metrics.stringWidth(message)) / 2, getHeight() / 2);
    }

    private static List<PowerPoint> powerPoints(FitActivity activity) {
        List<PowerPoint> result = new ArrayList<>();
        if (activity == null || activity.startTime() == null) {
            return result;
        }

        for (FitRecord record : activity.records()) {
            if (record.timestamp() != null && record.power() != null) {
                double seconds = Duration.between(activity.startTime(), record.timestamp()).toMillis() / 1000.0;
                result.add(new PowerPoint(Math.max(seconds, 0), record.power()));
            }
        }
        return smooth(result);
    }

    private static List<PowerPoint> smooth(List<PowerPoint> rawPoints) {
        List<PowerPoint> smoothed = new ArrayList<>(rawPoints.size());
        double halfWindow = SMOOTHING_WINDOW_SECONDS / 2;
        for (PowerPoint point : rawPoints) {
            double lowerBound = point.seconds() - halfWindow;
            double upperBound = point.seconds() + halfWindow;
            double powerSum = 0;
            int sampleCount = 0;
            for (PowerPoint candidate : rawPoints) {
                if (candidate.seconds() >= lowerBound && candidate.seconds() <= upperBound) {
                    powerSum += candidate.power();
                    sampleCount++;
                }
            }
            smoothed.add(new PowerPoint(point.seconds(), powerSum / sampleCount));
        }
        return List.copyOf(smoothed);
    }

    private record PowerPoint(double seconds, double power) {
    }
}
