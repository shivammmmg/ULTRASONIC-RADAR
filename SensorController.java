import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Manages the control flow of the sensor application.
 */
class SensorController {
    private final String portDescription;
    private final long durationMillis;
    private SerialPort comPort;
    private DataProcessor dataProcessor;
    private GraphPanel graphPanel;

    private String sensorType;

    /**
     * Initializes the sensor controller with port description and duration.
     *
     * @param portDescription The description of the serial port.
     * @param durationMillis  The duration in milliseconds for the program to run.
     * @param sensorType      The type of the sensor (e.g., "Humidity" or "Pressure").
     */
    public SensorController(String portDescription, long durationMillis, String sensorType) {
        this.portDescription = portDescription;
        this.durationMillis = durationMillis;
        this.sensorType = sensorType;

        try {
            // Sleep to give some time for the serial port to initialize
            Thread.sleep(2000);
            this.comPort = SerialPort.getCommPort(portDescription);
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.graphPanel = new GraphPanel(sensorType);
        this.dataProcessor = new DataProcessor(graphPanel);
    }

    /**
     * Starts the sensor application.
     */
    public void run() {
        try {
            establishConnection();
            Thread.sleep(1000);
            attachDataListener();

            // Sleep to allow data collection
            Thread.sleep(1000);

            //System.out.println("Calling Schedule Program stop");
            scheduleProgramStop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            //System.out.println("Inside finally of run method of Sensor controller");
             //Uncomment the following lines if you want to stop the program when the timer ends
             //if (comPort != null && comPort.isOpen()) {
               //  stop();
             //}
            //}
        }
    }

    /**
     * Establishes a connection to the serial port.
     */
    private void establishConnection() {
        System.out.println("Opening serial port...");
        if (comPort.openPort()) {
            System.out.println("Serial port opened successfully.");
        } else {
            System.err.println("Failed to open the serial port.");
        }
    }

    /**
     * Attaches a data listener to the serial port.
     */
    private void attachDataListener() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //System.out.println("Attaching data listener...");
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);
                String dataChunk = new String(newData);
                dataProcessor.processData(dataChunk, sensorType);
            }
        });
    }

    /**
     * Schedules the program to stop after a specified duration.
     */
    private void scheduleProgramStop() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Inside run of SensorController, will stop the program now");
                stop();
            }
        }, durationMillis);
    }

    /**
     * Stops the program and closes the serial port and graph panel.
     */
    private void stop() {
        System.out.println("Stopping the program...");
        if (comPort != null && comPort.isOpen()) {
            comPort.closePort();
        }
        graphPanel.dispose();
        // Uncomment the following line if you want to exit the program
         System.exit(0);
    }
}
