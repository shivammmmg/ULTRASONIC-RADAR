import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Represents a line graph for visualizing data points over time.
 */
public class LineGraph {

    /**
     * Enumeration of available chart types: CURVE or STEP.
     */
    public enum ChartType {
        CURVE, STEP
    }

    private XYSeriesCollection dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;
    private JFrame frame;
    private Map<String, XYSeries> seriesMap;
    private Map<String, LinkedList<DataPoint>> dataWindow;

    private static final long WINDOW_DURATION_MS = 2 * 60 * 1000; // 2 minutes
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * Initializes the LineGraph instance.
     *
     * @param title      The title of the graph.
     * @param xAxisLabel The label for the X-axis.
     * @param yAxisLabel The label for the Y-axis.
     * @param chartType  The type of chart (CURVE or STEP).
     */
    public LineGraph(String title, String xAxisLabel, String yAxisLabel, ChartType chartType) {
        dataset = new XYSeriesCollection();
        chart = createChart(title, xAxisLabel, yAxisLabel, chartType);
        chartPanel = new ChartPanel(chart);
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        seriesMap = new HashMap<>();
        dataWindow = new HashMap<>();
    }

    private JFreeChart createChart(String title, String xAxisLabel, String yAxisLabel, ChartType chartType) {
        JFreeChart chart;
        if (chartType == ChartType.CURVE) {
            chart = ChartFactory.createXYLineChart(
                    title,
                    xAxisLabel,
                    yAxisLabel,
                    dataset
            );
        } else {
            chart = ChartFactory.createXYStepChart(
                    title,
                    xAxisLabel,
                    yAxisLabel,
                    dataset
            );
        }

        // Format the x-axis to display timestamp in hh:mm:ss format
        DateAxis dateAxis = new DateAxis(xAxisLabel);
        dateAxis.setDateFormatOverride(timeFormat);
        chart.getXYPlot().setDomainAxis(dateAxis);

        return chart;
    }

    /**
     * Adds a data point to the graph.
     *
     * @param seriesName The name of the data series.
     * @param timestamp  The timestamp of the data point.
     * @param y          The Y-axis value of the data point.
     */
    public void addDataPoint(String seriesName, long timestamp, double y) {
        XYSeries series = seriesMap.get(seriesName);
        if (series == null) {
            series = new XYSeries(seriesName);
            seriesMap.put(seriesName, series);
            dataset.addSeries(series);
            dataWindow.put(seriesName, new LinkedList<>());
        }

        removeOldDataPoints(seriesName, timestamp);

        series.add(timestamp, y);
        dataWindow.get(seriesName).add(new DataPoint(timestamp, y));
    }

    private void removeOldDataPoints(String seriesName, long timestamp) {
        LinkedList<DataPoint> dataPoints = dataWindow.get(seriesName);
        while (!dataPoints.isEmpty() && timestamp - dataPoints.getFirst().timestamp > WINDOW_DURATION_MS) {
            dataPoints.removeFirst();
        }
    }

    /**
     * Clears all data points from the graph.
     */
    public void clearData() {
        for (XYSeries series : seriesMap.values()) {
            series.clear();
            dataWindow.get(series.getKey()).clear();
        }
    }

    /**
     * Refreshes the graph to reflect any changes in the data.
     */
    public void refresh() {
        chartPanel.repaint();
    }

    /**
     * Closes the graph window.
     */
    public void close() {
        frame.dispose();
    }

    /**
     * Represents a data point with timestamp and Y-axis value.
     */
    private static class DataPoint {
        private long timestamp;
        private double y;

        public DataPoint(long timestamp, double y) {
            this.timestamp = timestamp;
            this.y = y;
        }
    }
}
