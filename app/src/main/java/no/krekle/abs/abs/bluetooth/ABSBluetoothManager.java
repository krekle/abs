package no.krekle.abs.abs.bluetooth;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import no.krekle.abs.abs.driver.DriveInstance;
import no.krekle.abs.abs.driver.LogParser;
import no.krekle.abs.abs.settings.Settings;

/**
 * Created by krekle on 29/03/16.
 */
public class ABSBluetoothManager extends Thread {

    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private ABSBluetoothCallback caller;
    private DriveInstance driveInstance;

    public ABSBluetoothManager(BluetoothSocket socket, ABSBluetoothCallback caller) {
        // Callback route
        this.caller = caller;

        // Bluetooth socket
        mmSocket = socket;

        // Temp Streams
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Streams created");

        // Set final streams
        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        // Send ref to manager - for sending and canceling
        caller.bluetoothManager(this);
    }

    public void run() {
        System.out.println("Ready");

        // Read buffer for the stream
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read() TODO: remove
        ArrayList<String> strings = new ArrayList<>();

        int count = 0; // TODO: remove

        // Read one line
        String line = "";
        while (true) {

            try {
                while (mmInStream.available() > 0) {
                    //Character c = (char) ( mmInStream.read(buffer) + '0');
                    int c_int = mmInStream.read();
                    char c = (char) c_int;

                    if (c_int == 0) continue;
                    //System.out.println("C:" + c);

                    if (c == '#') {
                        //strings.add(line.toString());
                        parseOdbLine(line);
                        line = "";
                        line += c;
                    } else {
                        line += c;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseOdbLine(String odbLine) {

        // If this is first log entry, create new instance
        if (driveInstance == null) {
            driveInstance = new DriveInstance();
        }

        // Add new data to the driveInstance
        driveInstance = LogParser.parseObdLine(odbLine, driveInstance);

        // Check if this is speed only or everything
        if (Settings.getInstance().isSpeedOnly()) {
            if(driveInstance.isCompleteSpeedOnly()) {
                // Route data through the callback
                caller.bluetoothCallback(driveInstance);

                // Create a new instance and keep listening
                driveInstance = new DriveInstance();
            }

        } else {

            if (driveInstance.isComplete()) {
                // Route data through the callback
                caller.bluetoothCallback(driveInstance);

                // Create a new instance and keep listening
                driveInstance = new DriveInstance();
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }
}
