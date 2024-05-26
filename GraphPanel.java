import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a line graph for visualizing data points over time.
 */
public class GraphPanel extends JFrame {
    private static final int MAX_DATA_POINTS = 100;
    private static final long DATA_READ_INTERVAL = 1500;

    private JFreeChart chart;

    private List<Long> timeStamps = new ArrayList<>();
    private List<Double> temperatureReadings = new ArrayList<>();
    private List<Double> sensorReadings = new ArrayList<>();

    private TimeSeriesCollection dataset;
    private TimeSeries valueSeries;

    private String sensorType;

    private ChartPanel chartPanel;

    public GraphPanel(String sensorType) {
        dataset = new TimeSeriesCollection();
        this.sensorType = sensorType;
        String title = "";
        if(sensorType.contains("hum") || sensorType.contains("Hum") )
            title = " Temperature and Humidity";
        else title = "Temperature and Pressure";

        chart = ChartFactory.createTimeSeriesChart(
                title,
                "Time in Seconds",
                "Value",
                dataset,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();
        NumberAxis yAxis;
        if (sensorType.equalsIgnoreCase("Humidity"))
            yAxis = new NumberAxis("Humidity in %RH");
        else
            yAxis = new NumberAxis("Pressure in kPa");
        yAxis.setRange(0, 120); // Set the y-axis range for both temperature and pressure
        Font boldFont = new Font(yAxis.getLabelFont().getName(), Font.BOLD, yAxis.getLabelFont().getSize());
        yAxis.setLabelFont(boldFont); // Set the font for the Y-axis label

        plot.setRangeAxis(yAxis);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
        plot.setRenderer(renderer);

        this.chartPanel = new ChartPanel(chart);
        this.chartPanel.setPreferredSize(new Dimension(1000, 750));

        setContentPane(this.chartPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        addTemperatureSeries(); // Initialize the temperature series
        if (sensorType.equalsIgnoreCase("Humidity"))
            addHumiditySeries();
        else
            addPressureSeries(); // Initialize the pressure series
    }

    private void addTemperatureSeries() {
        TimeSeries temperatureSeries = new TimeSeries("Temperature");
        for (int i = 0; i < timeStamps.size(); i++) {
            long timeStamp = timeStamps.get(i);
            double temperature = temperatureReadings.get(i);
            temperatureSeries.addOrUpdate(new Second(new Date(timeStamp)), temperature);
        }
        dataset.addSeries(temperatureSeries);
    }

    private void addPressureSeries() {
        TimeSeries sensorSeries = new TimeSeries("Pressure");
        for (int i = 0; i < timeStamps.size(); i++) {
            long timeStamp = timeStamps.get(i);
            double pressure = sensorReadings.get(i);
            sensorSeries.addOrUpdate(new Second(new Date(timeStamp)), pressure);
        }
        dataset.addSeries(sensorSeries);
    }

    private void addHumiditySeries() {
        TimeSeries sensorSeries = new TimeSeries("Humidity");
        for (int i = 0; i < timeStamps.size(); i++) {
            long timeStamp = timeStamps.get(i);
            double humidity = sensorReadings.get(i);
            sensorSeries.addOrUpdate(new Second(new Date(timeStamp)), humidity);
        }
        dataset.addSeries(sensorSeries);
    }

    /**
     * Adds temperature and humidity data to the graph.
     *
     * @param temperature The temperature reading.
     * @param sensorData  The humidity or Pressure reading.
     */
    public void addData(double temperature, double sensorData) {
        long currentTimeMillis = System.currentTimeMillis();

        // Only add data if the specified interval has passed since the last data point
        if (timeStamps.isEmpty() || currentTimeMillis - timeStamps.get(timeStamps.size() - 1) >= DATA_READ_INTERVAL) {
            timeStamps.add(currentTimeMillis);
            temperatureReadings.add(temperature);
            sensorReadings.add(sensorData);

            if (timeStamps.size() > MAX_DATA_POINTS) {
                timeStamps.remove(0);
                temperatureReadings.remove(0);
                sensorReadings.remove(0);
            }

            updateDataset();
        }
    }

    private void updateDataset() {
        dataset.removeAllSeries();
        addTemperatureSeries();
        if (sensorType.equalsIgnoreCase("Humidity"))
            addHumiditySeries();
        else
            addPressureSeries();

        chart.fireChartChanged();
        chartPanel.repaint();
    }

    private void addDataSeries() {
        System.out.println("*** Inside addDataSeries****");
        TimeSeries temperatureSeries = new TimeSeries("Temperature");
        TimeSeries sensorSeries;

        if (sensorType.equalsIgnoreCase("Humidity"))
            sensorSeries = new TimeSeries("Humidity");
        else
            sensorSeries = new TimeSeries("Pressure");
        for (int i = 0; i < timeStamps.size(); i++) {
            long timeStamp = timeStamps.get(i);
            double temperature = temperatureReadings.get(i);
            double sensorValue = sensorReadings.get(i);
            System.out.println("Inside addDataSeries   temperature:   " + temperature + "    " + sensorType + "    " + sensorValue);
            temperatureSeries.addOrUpdate(new Second(new Date(timeStamp)), temperature);
            sensorSeries.addOrUpdate(new Second(new Date(timeStamp)), sensorValue);
        }
    }
}
