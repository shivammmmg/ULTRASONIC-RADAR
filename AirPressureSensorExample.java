/**
 * Entry point for the Air Pressure Sensor application.
 */
public class AirPressureSensorExample {

    /**
     * Main method to run the Air Pressure Sensor application.
     * It initializes and starts the sensor controller for pressure readings.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        String portDescription = "/dev/cu.SLAB_USBtoUART"; // Specify the port description
        long durationMillis = 23000; // Duration for which the sensor should run
        String sensorType = "Pressure"; // Sensor type (can be "Humidity" or "Pressure")

        // Create a SensorController instance for pressure readings
        SensorController sensorController = new SensorController(portDescription, durationMillis, sensorType);
        sensorController.run(); // Start the sensor controller
    }
}
