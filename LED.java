import org.firmata4j.IODevice;
import org.firmata4j.Pin;

import java.io.IOException;

/**
 * Represents an LED controlled by an Arduino device.
 */

public class LED {
    private IODevice device;
    private Pin pin;

    /**
     * Initializes the LED instance with the specified Arduino device and pin number.
     *
     * @param device    The Arduino IODevice.
     * @param pinNumber The pin number to which the LED is connected.
     */
    public LED(IODevice device, int pinNumber){
        try{
            pin = device.getPin(pinNumber);
            pin.setMode(Pin.Mode.OUTPUT);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Sets the brightness of the LED.
     *
     * @param brightness The brightness level (0 to 255).
     */
    public void setBrigntness(long brightness){
        try {
            brightness = Math.max(0, Math.min(255, brightness));
            pin.setValue(brightness);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Turns off the LED by setting the brightness to 0.
     */
    public void turnOff(){
        setBrigntness(0);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
