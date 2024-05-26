package com.eecs.radar;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;

import java.io.IOException;

public class UltrasonicRadar {
    private static final String PORT = "COM4";
    private static final int TRIGGER_PIN = 2;
    private static final int ECHO_PIN = 3;

    private ServoMotor servoMotor;
    private UltrasonicSensor ultrasonicSensor;

    public UltrasonicRadar() {
        IODevice ioDevice;
        try {
            ioDevice = new FirmataDevice(PORT);
            ioDevice.start();
            ioDevice.ensureInitializationIsDone();

            servoMotor = new ServoMotor(ioDevice, 9);
            ultrasonicSensor = new UltrasonicSensor(ioDevice, TRIGGER_PIN, ECHO_PIN);
        } catch (IOException e) {
            System.out.println("Error initializing the IODevice: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Error initializing the IODevice: " + e.getMessage());
        }
    }

    public void scan() throws IOException {
        if (servoMotor == null || ultrasonicSensor == null) {
            System.out.println("Failed to initialize servo motor and ultrasonic sensor. Aborting scan.");
            return;
        }

        servoMotor.setAngle(0); // Rotate the servo motor to the initial angle

        while (true) {
            for (int angle = 0; angle <= 180; angle += 10) {
                servoMotor.setAngle(angle); // Rotate the servo motor to the specified angle
                try {
                    Thread.sleep(1000); // Delay for servo motor movement
                } catch (InterruptedException e) {
                    System.out.println("Thread sleep interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt(); // Restore interrupted status
                }
                try {
                    double distance = ultrasonicSensor.getDistance(); // Get the distance measured by the ultrasonic sensor
                    System.out.println("Angle: " + angle + ", Distance: " + distance + " cm");
                } catch (IOException e) {
                    System.out.println("Error reading distance from the ultrasonic sensor: " + e.getMessage());
                }

                // Delay between each scan
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("Thread sleep interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt(); // Restore interrupted status
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        UltrasonicRadar radar = new UltrasonicRadar();
        radar.scan();
    }
}
