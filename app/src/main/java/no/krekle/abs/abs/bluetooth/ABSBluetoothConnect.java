package no.krekle.abs.abs.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

import no.krekle.abs.abs.client.ABSCallback;
import no.krekle.abs.abs.client.AbsApi;
import no.krekle.abs.abs.driver.DriveInstance;
import no.krekle.abs.abs.driver.DriveLog;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by krekle on 02/03/16.
 */
public class ABSBluetoothConnect extends Thread implements ABSCallback {

    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private ABSBluetoothCallback caller;

    public ABSBluetoothConnect(BluetoothDevice device, ABSBluetoothCallback caller) {

        device.fetchUuidsWithSdp();

        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        this.caller = caller;
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            if(device.getBondState()==device.BOND_BONDED){
                System.out.println("BONDED");
                tmp = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
            } else {
                System.out.println("NOT BONDED");
                tmp = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
            }
            //tmp = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmSocket = tmp;
        System.out.println(mmSocket.getRemoteDevice().getName());
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.cancelDiscovery();

        connect();
    }

    private void connect() {
        System.out.println("@Run");


        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception

            mmSocket.connect();

            // Notify the progress to the activity
            caller.bluetoothConnected();

            // Do work to manage the connection in a separate thread
            ABSBluetoothManager manager = new ABSBluetoothManager(mmSocket, this.caller);
            manager.start();

        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            System.out.println("CONNECTION FAILED");
            connectException.printStackTrace();


            try {
                mmSocket.close();
                caller.bluetoothFailed();
            } catch (IOException closeException) {
                System.out.println("CONNECTION FAILED");
                closeException.printStackTrace();
            }
            return;
        }
    }

    /**
     * Will cancel an in-progress connection, and close the socket
     */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }

    // ///////////////////////
    //  HTTP COMMUNICATION  //
    //////////////////////////

    private void sendMock() {
        Random random = new Random();

        DriveLog log = new DriveLog(3);
        for (int i = 0; i < 5; i++) {
            Double speed = random.nextDouble() * 80.0;
            log.addLog(new DriveInstance(speed.intValue()));
        }
        AbsApi api = new AbsApi();
        api.insert(log, this);

        Log.v("LOG SENDT", "ok");
    }

    @Override
    public void success(Response response) {

    }

    @Override
    public void failure(RetrofitError response) {

    }

    @Override
    public void inProgress() {

    }
}
