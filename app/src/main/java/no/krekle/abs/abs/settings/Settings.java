package no.krekle.abs.abs.settings;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * Created by krekle on 13/04/16.
 */

// Settings singleton
public class Settings {

    // Static instance
    private static Settings instance;

    // Private constructor
    private Settings() {
        // Empty
    }

    // Accessor
    public static Settings getInstance() {
        if (Settings.instance == null) {
            // Create instance if not existing
            Settings.instance = new Settings();
        }
        return Settings.instance;
    }

    /////////////////////////
    //      Settings       //
    /////////////////////////

    // DriveLogs

    private boolean speedOnly = true;

    public boolean isSpeedOnly() {
        return speedOnly;
    }

    public void setSpeedOnly(boolean speedOnly) {
        this.speedOnly = speedOnly;
    }

    private int driveId = 1;

    public int getDriveId() {
        return driveId;
    }

    public void setDriveId(int driveId) {
        this.driveId = driveId;
    }

    // Bluetooth
    private BluetoothDevice device;
    private BluetoothSocket socket;


    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public void setSocket(BluetoothSocket socket) {
        this.socket = socket;
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }

    public BluetoothSocket getSocket() {
        return this.socket;
    }

    // Api Update Rate
    private int apiUpdateInterval = 1; // Interval for sending data to backend in seconds

    public int getApiUpdateInterval() {
        return apiUpdateInterval;
    }

    public void setApiUpdateInterval(int apiUpdateInterval) {
        this.apiUpdateInterval = apiUpdateInterval;
    }

    // GUI Update Rate
    private int guiUpdateInterval = 1; // Interval for updating the gui in seconds

    public int getGuiUpdateInterval() {
        return guiUpdateInterval;
    }

    public void setGuiUpdateInterval(int guiUpdateInterval) {
        this.guiUpdateInterval = guiUpdateInterval;
    }
}
