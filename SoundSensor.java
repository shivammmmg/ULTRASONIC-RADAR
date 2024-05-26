import org.firmata4j.IODevice;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;
import org.firmata4j.PinEventListener;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a sound sensor that monitors sound intensity and triggers events based on changes.
 */
public class SoundSensor {

    private IODevice device;
    private Pin soundSensorPin = null;

    private int windowSize = 10; // Number of readings for moving average
    private int readingsCount = 0;
    private int readingsSum = 0;
    private int soundReference = 0;
    private Queue<Integer> soundReadings = new LinkedList<>();
    private LineGraph lineGraph;

    private static final int UPPER_THRESHOLD = 20;
    private static final int LOWER_THRESHOLD = 10;
    private int consecutiveChanges = 0;

    private boolean isEnabled = true;

    /**
     * Initializes the SoundSensor with the specified IODevice, pin number, and LineGraph.
     *
     * @param device     The IODevice for communication with the sensor.
     * @param pinNumber  The pin number of the sound sensor.
     * @param lineGraph  The LineGraph to display sound intensity data.
     */
    public SoundSensor(IODevice device, int pinNumber, LineGraph lineGraph) {
        try {
            this.device = device;
            soundSensorPin = device.getPin(pinNumber);
            soundSensorPin.setMode(Pin.Mode.ANALOG);
            this.lineGraph = lineGraph;

            setupEventListener();
            if(isEnabled)
                System.out.println("Sound Sensor initialized");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSoundReference(int soundLevel) {
        if (readingsCount >= windowSize) {
            soundReference = readingsSum / windowSize;
            readingsCount = 0;
            readingsSum = 0;
            if(isEnabled)
                System.out.println("Sound Reference is " + soundReference);
        } else {
            readingsCount++;
            readingsSum += soundLevel;
        }
    }

    private void setupEventListener() {
        try {
            soundSensorPin.setMode(Pin.Mode.ANALOG); // Request analog value from the sound sensor
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        }
        soundSensorPin.addEventListener(new PinEventListener() {
            @Override
            public void onModeChange(IOEvent ioEvent) {
            }
            @Override
            public void onValueChange(IOEvent event) {
                int soundLevel = (int) event.getValue();
                soundReadings.add(soundLevel);

                if (soundReadings.size() > windowSize) {
                    soundReadings.poll(); // Remove oldest reading from the queue
                    updateSoundReference(soundLevel);

                    if (Math.abs(soundLevel - soundReference) > UPPER_THRESHOLD) {
                        consecutiveChanges++;
                        if (consecutiveChanges >= windowSize) {
                            if(isEnabled)
                                System.out.println("Sound Change Detected! Sound Level: " + soundLevel);
                            soundReference = soundLevel;
                            consecutiveChanges = 0;
                        }
                    } else if (Math.abs(soundLevel - soundReference) < LOWER_THRESHOLD) {
                        consecutiveChanges = 0;
                    }
                }

                long currentTime = System.currentTimeMillis();
                lineGraph.addDataPoint("Sound Intensity", currentTime, soundLevel);
            }
        });
    }

    /**
     * Closes the SoundSensor by removing event listeners and performing cleanup operations.
     */
    public void close() {
        try {
            // Remove the event listener to stop receiving updates from the sound sensor
            soundSensorPin.removeAllEventListeners();

            // Optional: You may also want to set the pin mode to INPUT to free up the pin
            soundSensorPin.setMode(Pin.Mode.INPUT);

            // Other cleanup operations, if any

            System.out.println("Sound Sensor closed successfully");
            lineGraph.close(); // Close the LineGraph
            System.out.println("LineGraph closed successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
