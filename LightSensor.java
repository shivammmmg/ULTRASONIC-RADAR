import org.firmata4j.IODevice;
import org.firmata4j.IOEvent;
import org.firmata4j.Pin;
import org.firmata4j.PinEventListener;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents a Light Sensor that reads light intensity values and provides event notifications.
 */
public class LightSensor {

    private IODevice device;
    private Pin lightSensorPin;
    private Queue<Integer> lightReadings = new LinkedList<>();
    private LineGraph lineGraph;
    private LED led;

    private static final int WINDOW_SIZE = 10;

    /**
     * Initializes the LightSensor with the specified device, pin number, line graph, and LED.
     *
     * @param device      The IODevice to communicate with the sensor.
     * @param pinNumber   The pin number to which the sensor is connected.
     * @param lineGraph   The LineGraph to display the light intensity readings.
     * @param led         The LED instance for controlling LED based on light intensity.
     */

    public LightSensor(IODevice device, int pinNumber, LineGraph lineGraph, LED led) {
        try {
            this.device = device;
            lightSensorPin = device.getPin(pinNumber);
            lightSensorPin.setMode(Pin.Mode.ANALOG);
            this.lineGraph = lineGraph;
            this.led = led;
            System.out.println("All set for Light Sensor");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupEventListener() {
        try {
            lightSensorPin.setMode(Pin.Mode.ANALOG); // Request analog value from the light sensor
            System.out.println("Setup Event listener for Light Sensor");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        }
        System.out.println("Adding Event listener");
        lightSensorPin.addEventListener((new PinEventListener() {

            @Override
            public void onModeChange(IOEvent ioEvent) {
            }

            @Override
            public void onValueChange(IOEvent event) {
                System.out.println("Inside onValueChange");
                int lightLevel = (int) event.getValue();
                lightReadings.add(lightLevel);

                if (lightReadings.size() > WINDOW_SIZE) {
                    lightReadings.poll(); // Remove oldest reading from the queue
                }

                long currentTime = System.currentTimeMillis();
                System.out.println("Adding data point to Graph"+ lightLevel);
                lineGraph.addDataPoint("Light Intensity", currentTime, lightLevel);

                // Update LED brightness based on light intensity
                if (lightLevel > 400) {
                    led.setBrigntness(255);
                } else if (lightLevel > 200 && lightLevel <= 400) {
                    led.setBrigntness(128);
                } else {
                    led.turnOff();
                }
            }
        }));
    }

    /**
     * Starts reading light intensity by setting up event listeners.
     */
    public void startReading() {
        System.out.println("Inside StartReading");
        setupEventListener();
    }

    /**
     * Stops reading light intensity by removing event listeners and turning off the LED.
     */
    public void stopReading() {
        lightSensorPin.removeAllEventListeners();
        led.turnOff();
    }
}
