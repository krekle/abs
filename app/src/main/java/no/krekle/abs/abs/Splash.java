package no.krekle.abs.abs;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class Splash extends Activity {

    private ProgressBar progress;
    private TextView feedback;
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private final int REQUEST_ENABLE_BT = 1337;
    private final String obd_address = "00:06:66:77:72:BF";
    // Bluetooth Mac
    //00:06:66:77:72:BF
    //0006667772BF

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide actionbar
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_splash);

        // Ref gui elements
        this.progress = (ProgressBar) findViewById(R.id.progressBar);
        this.feedback = (TextView) findViewById(R.id.txtFeedback);

        // Animate Progressbar
        this.progress.setIndeterminate(true);

        // Bluetooth
        bluetoothConnectionProcess(0);

    }

    // Wrapper for whole bluetooth connection
    private void bluetoothConnectionProcess(int progress) {

        if (progress == 0) {
            // Feedback text
            setFeedbackText("Activating bluetooth");

            // Start bluetooth
            this.adapter = enableBluetooth();

            progress++;
        }

        else if (progress == 1) {
            // Check device compatability
            if (this.adapter == null) {
                setFeedbackText("");
                Toast.makeText(getApplicationContext(), "Bluetooth not supported by this device!", Toast.LENGTH_LONG).show();
                return;
            }
            progress++;
        }

        else if(progress >= 2) {
            //Feedback text
            setFeedbackText("Searching for device");

            // Search for device
            device = findBondedDevices();

            if (device == null) {
                device = findBluetoothDevices();
            }
            progress++;
        }

        else if(progress >= 3) {
            //Feedback text
            setFeedbackText("Connecting to device");

            progress++;
        }


        else if(progress >= 4) {
            //Feedback text
            setFeedbackText("Connect to device");

        }

        // Change activity after wait
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                changeActivity();
            }
        }, 2000);


    }

    // Changes screen to Stationary/Home
    private void changeActivity() {
        this.startActivity(new Intent(getApplicationContext(), Stationary.class));
        this.overridePendingTransition(0, 0);
    }

    private BluetoothAdapter enableBluetooth() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null) {
            if (!adapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            // Bluetooth is enabled
            return adapter;
        }
        // Bluetooth adapter not avaliable on phone
        return null;
    }

    // Callback
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Enable bluetooth
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {

                // Connect to device
                bluetoothConnectionProcess(1);
            } else {

                // Bluetooth enabling failed!
                Toast.makeText(getApplicationContext(), "Bluetooth not enabled on this device!", Toast.LENGTH_LONG);
            }
        }

    }

    private void connectToDevice(BluetoothDevice device) {

        if (device != null) {
            // Connect to the device
            device.createBond();
            device.setPairingConfirmation(true);
        } else {
            // Paired
            device = findBluetoothDevices();

        }


    }

    private BluetoothDevice findBondedDevices() {
        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {

            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                if (device.getAddress().equals(obd_address)) {
                    Log.v("FIND BONDED DEVICE", device.toString());
                    return device;
                }
            }
            Log.v("FIND BONDED DEVICE", "No bonded devices found");
            return null;
        } else {
            Log.v("FIND BONDED DEVICE", "No bonded devices found");
            return null;
        }
    }


    private BluetoothDevice findBluetoothDevices() {
        // Search for device
        adapter.startDiscovery();

        // Create a BroadcastReceiver for ACTION_FOUND
        Log.v("FIND DEVICE", "Finding device");
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    if (device.getAddress().equals(obd_address)) {
                        // connect to device
                    }
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        adapter.cancelDiscovery();
        return null;
    }

    private void setFeedbackText(String feedback) {
        this.feedback.setText(feedback);
    }

}