package com.eecs.radar;

import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import processing.core.PApplet;
import java.io.IOException;

public class RadarSystem {
    private static final String PORT = "COM4"; // Replace with the correct port for your Arduino board
    private static final int SERVO_PIN = 9;    // Replace with the actual digital I/O pin number for the servo motor
    private static final int ECHO_PIN = 2;     // Replace with the actual digital I/O pin number for the echo sensor
    private static final int DISTANCE_TIMEOUT_MS = 500; // Timeout for ultrasonic distance measurement

    private static FirmataDevice device;
    private static UltrasonicSensor ultrasonicSensor;
    private static RadarGraph radarGraph;

    public static void main(String[] args) throws IOException {
        setup();

        while (true) {
            performScan();
            try {
                Thread.sleep(1000); // Adjust the delay time as needed
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void setup() {
        try {
            IODevice groveBoard = new FirmataDevice(PORT);
            groveBoard.start();
            groveBoard.ensureInitializationIsDone();

            ultrasonicSensor = new UltrasonicSensor(groveBoard, 2, 3);

            // Initialize the RadarGraph instance
            radarGraph = new RadarGraph();
            radarGraph.init(); // Setup the Processing sketch
            radarGraph.start(); // Start the sketch

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void performScan() throws IOException {
        float[] distances = new float[360];

        for (int angle = 0; angle < 360; angle += 1) {
            try {
                device.getPin(SERVO_PIN).setValue(angle);
                Thread.sleep(100); // Adjust the delay time as needed
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

            double distance = ultrasonicSensor.getDistance();
            System.out.println("Angle: " + angle + ", Distance: " + distance + " cm");

            // Update the radar graph distances
            distances[angle] = (float) distance;
        }

        // Update the radar graph with the new distances
        radarGraph.updateDistances(distances);
    }
}
