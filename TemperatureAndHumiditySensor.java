import com.fazecast.jSerialComm.SerialPort;

/**
 * Entry point for the Temperature and Humidity Sensor application.
 */
public class TemperatureAndHumiditySensor {

    /**
     * Main method to run the Temperature and Humidity Sensor application.
     * It initializes and starts the sensor controller for humidity readings.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        String portDescription = "/dev/cu.SLAB_USBtoUART"; // Specify the port description
        long durationMillis = 50000; // Duration for which the sensor should run
        String sensorType = "Humidity"; // Sensor type (can be "Humidity" or "Pressure")

        // Create a SensorController instance for humidity readings
        SensorController sensorController = new SensorController(portDescription, durationMillis, sensorType);
        sensorController.run(); // Start the sensor controller
    }
}
