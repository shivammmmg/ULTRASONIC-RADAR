import java.util.ArrayList;
import java.util.List;

/**
 * Processes raw data received from the sensor and updates the graph panel.
 */
class DataProcessor {
    private String accumulatedData = "";
    private double temperature1 = 0.0;
    private double humidity1 = 0.0;
    private double pressure1 = 0.0;
    private String sensorType;

    private List<Long> timeStamps = new ArrayList<>();
    private List<Double> temperatureReadings = new ArrayList<>();
    private List<Double> humidityReadings = new ArrayList<>();
    private GraphPanel graphPanel; // Store the GraphPanel instance

    /**
     * Initializes the DataProcessor with a GraphPanel instance.
     *
     * @param graphPanel The GraphPanel instance to update.
     */
    public DataProcessor(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    /**
     * Processes raw data received from the sensor.
     *
     * @param data The raw data to be processed.
     * @param sensorType The type of the sensor (e.g., "Humidity" or "Pressure").
     */
    public void processData(String data, String sensorType) {
        accumulatedData += data;
        this.sensorType = sensorType;
        int delimiterIndex;

        while ((delimiterIndex = accumulatedData.indexOf("\n")) != -1) {
            String completeMessage = accumulatedData.substring(0, delimiterIndex);
            accumulatedData = accumulatedData.substring(delimiterIndex + 1);
            processCompleteMessage(completeMessage);
        }
    }

    /**
     * Processes a complete message received from the sensor.
     *
     * @param completeMessage The complete message to be processed.
     */
    private void processCompleteMessage(String completeMessage) {
        //System.out.println("** Inside processCompleteMessage Complete message: " + completeMessage);

        String[] parts = completeMessage.split("\\t");
        //for(int i = 0; i < parts.length; i++)
            //System.out.println("parts["+i+"] value is : "+parts[i]);

        if (parts.length >= 2) {
            String[] temperatureParts = parts[0].split(":");
            String[] sensorParts = parts[1].split(":");

            if (temperatureParts.length >= 2 && sensorParts.length >= 2) {
                temperature1 = extractValue(temperatureParts[1]);
                System.out.println("Extracted Temperature: " + temperature1);
                if(sensorType.equalsIgnoreCase("Humidity")) {
                    humidity1 = extractValue(sensorParts[1]);
                    System.out.println("Extracted Humidity: " + humidity1);
                }
                else {
                    pressure1 = extractPressureValue(sensorParts[1]); // Extract sensor value and divide by 1000
                    System.out.println("Extracted Pressure: " + pressure1);

                }
                System.out.println("---------------------------------------------------");
              //System.out.println("Calling Graphpanel class temperature1: " + temperature1 );

// Assuming sensorType is already set in the DataProcessor class
                if (sensorType.equalsIgnoreCase("humidity")) {
                    graphPanel.addData(temperature1, humidity1);
                } else {
                    graphPanel.addData(temperature1, pressure1);
                }

                temperature1 = 0.0;
                humidity1 = 0.0;
                pressure1 = 0.0;
            }
        }
    }

    /**
     * Extracts the pressure value from a value with unit and converts to kPa.
     *
     * @param valueWithUnit The value with unit to be extracted.
     * @return The extracted pressure value in kPa.
     */
    private double extractPressureValue(String valueWithUnit) {
        double pressureValue = extractValue(valueWithUnit);
        return pressureValue / 1000.0; // Divide by 1000 to convert to kPa
    }

    /**
     * Extracts a numeric value from a value with unit.
     *
     * @param valueWithUnit The value with unit to be extracted.
     * @return The extracted numeric value.
     */
    private double extractValue(String valueWithUnit) {
        //System.out.println("valueWithUnit is "+valueWithUnit);
        String cleanedValue = valueWithUnit.replaceAll("[^\\d.]", "").replaceAll("\\s+", "");
        return Double.parseDouble(cleanedValue);
    }
}