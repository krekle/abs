package no.krekle.abs.abs.bluetooth;

import no.krekle.abs.abs.driver.DriveInstance;

/**
 * Created by krekle on 29/03/16.
 */
public interface ABSBluetoothCallback {

    // Connect
    public void bluetoothFailed();

    public void bluetoothConnected();

    // Manage
    public void bluetoothManager(ABSBluetoothManager manager);

    public void bluetoothListening();

    public void bluetoothCallback(DriveInstance driveInstance);
}
