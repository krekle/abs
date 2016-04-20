package no.krekle.abs.abs.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;

/**
 * Created by krekle on 30/03/16.
 */

public class Connections {
    private static boolean state = false;

    public static boolean blueToothEnabled() {
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!bluetooth.isEnabled()) {
            System.out.println("Bluetooth is Disabled...");
            state = false;
        } else if (bluetooth.isEnabled()) {
            String address = bluetooth.getAddress();
            String name = bluetooth.getName();
            System.out.println(name + " : " + address);
            state = true;
        }
        return state;
    }

    public static BluetoothDevice discoverDevices(BluetoothAdapter adapter, String address) {
        System.out.println("Starting discovery");
        adapter.startDiscovery();

        System.out.println("Getting devices");
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice device: devices) {

            // Find desired device
            if (device.getAddress().equals(address)) {
                return device;
            }
        }

        System.out.println("No device found!");
        return null;
    }

}