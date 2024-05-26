package com.eecs.radar;

import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;

import java.io.IOException;

/**
 * The UltrasonicSensor class represents an ultrasonic sensor HC-SR04 controlled by Firmata.
 */
public class UltrasonicSensor {
    private final Pin triggerPin;
    private final Pin echoPin;

    public UltrasonicSensor(IODevice ioDevice, int triggerPinNumber, int echoPinNumber) throws IOException {
        triggerPin = ioDevice.getPin(triggerPinNumber);
        triggerPin.setMode(Pin.Mode.OUTPUT);
        echoPin = ioDevice.getPin(echoPinNumber);
        echoPin.setMode(Pin.Mode.INPUT);
    }

    /**
     * Measures the distance to an object using the ultrasonic sensor.
     *
     * @return The distance in centimeters.
     */
    public double getDistance() throws IOException {
        // Send a trigger signal
        triggerPin.setValue(1);
        try {
            Thread.sleep(100); // Wait for 10 microseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        triggerPin.setValue(0);

        // Measure the duration of the echo signal
        long duration = pulseIn(echoPin, 1, 1000000); // Timeout after 1 second

        // Calculate the distance based on the duration
        double distance = duration * 0.0343 / 2; // Speed of sound is approximately 343 meters/second

        return distance;
    }

    private long pulseIn(Pin pin, int state, long timeout) throws IOException {
        long startTime = System.nanoTime();
        long elapsedTime = 0;

        // Wait for the pulse to start (echo pin to go HIGH)
        while (pin.getValue() != state) {
            // Check for timeout (10 µs trigger)
            if (System.nanoTime() - startTime > 10000) {
                return -1; // Timeout occurred
            }
        }

        startTime = System.nanoTime(); // Capture the start time

        // Wait for the pulse to end (echo pin to go LOW)
        while (pin.getValue() == state) {
            // Check for timeout (38 ms)
            if (System.nanoTime() - startTime > timeout * 1000000) {
                return -1; // Timeout occurred
            }
        }

        // Calculate the elapsed time in microseconds
        elapsedTime = (System.nanoTime() - startTime) / 1000;

        // Calculate the distance based on the elapsed time (in microseconds)
        // Speed of sound is approximately 343 meters/second or 34300 centimeters/second
        // Divide by 2 to account for the round-trip travel of the ultrasonic wave
        double distance = elapsedTime * 0.0343 / 2;

        return (long) distance;
    }
}
