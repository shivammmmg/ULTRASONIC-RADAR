import org.firmata4j.IODevice;
import org.firmata4j.firmata.FirmataDevice;
import java.io.IOException;

/**
 * Main class to run the sensor application.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String portName = "/dev/cu.SLAB_USBtoUART";  // Arduino's port
        int soundSensorPin = 16;
        int lightSensorPin = 14; // Light Sensor PIN, need to connect the wires
        int ledPin = 4;

        // Initialize the Firmata device for communication with Arduino
        IODevice device = new FirmataDevice(portName);
        try {
            device.start();
            System.out.println("Device Started");
        } catch (Exception e) {
            throw new RuntimeException("Failed to start the device.", e);
        }
        try {
            // Ensure that the Firmata communication is initialized
            System.out.println("Inside try");
            try {
                device.ensureInitializationIsDone();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Device Initialized");

            // Sleep for a short time to allow Firmata communication to stabilize
            Thread.sleep(500);

            // Initialize and start the sound sensor
            LineGraph soundGraph = new LineGraph("Sound Intensity Graph", "Time", "Sound Intensity", LineGraph.ChartType.CURVE);
            SoundSensor soundSensor = new SoundSensor(device, soundSensorPin, soundGraph);

            Thread.sleep(20000); // Wait for 20 seconds
            System.out.println("Closing the sound sensor and returning from the class");
            soundSensor.close(); // Close the sound sensor
            Thread.sleep(5000);

            // Create the LineGraph for Light Intensity
            LineGraph lightGraph = new LineGraph("Light Intensity Graph", "Time", "Light Intensity", LineGraph.ChartType.CURVE);

            // Create the LED instance for controlling the LED
            LED led = new LED(device, ledPin);

            // Start reading light intensity and display it in the graph
            LightSensor lightSensor = new LightSensor(device, lightSensorPin, lightGraph, led);
            System.out.println("Starting LightSensor reading");
            lightSensor.startReading();

            Thread.sleep(20000); // Wait for 35 seconds
            System.out.println("Closing the light sensor and the graph");
            lightSensor.stopReading(); // Stop reading light intensity
            lightGraph.close(); // Close the light intensity graph
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("Inside finally block, exiting the code");
                device.stop();
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
