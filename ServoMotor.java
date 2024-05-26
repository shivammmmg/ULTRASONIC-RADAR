package com.eecs.radar;

import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;

import java.io.IOException;

/**
 * The ServoMotor class represents a servo motor controlled by Firmata.
 */
public class ServoMotor {
    private final Pin pin;

    public ServoMotor(IODevice ioDevice, int pinNumber) throws IOException {
        pin = ioDevice.getPin(pinNumber);
        pin.setMode(Pin.Mode.SERVO);
    }

    /**
     * Sets the angle of the servo motor.
     *
     * @param angle The angle to set (0-180 degrees).
     */
    public void setAngle(int angle) throws IOException {
        pin.setValue(angle);
    }
}
