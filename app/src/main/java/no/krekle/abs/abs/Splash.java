package no.krekle.abs.abs;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import no.krekle.abs.abs.bluetooth.Connections;
import no.krekle.abs.abs.client.AbsApi;
import no.krekle.abs.abs.driver.DriveLog;
import no.krekle.abs.abs.settings.Settings;


public class Splash extends Activity {

    private ProgressBar progress;
    private TextView feedback;
    private BluetoothAdapter adapter;
    private BluetoothDevice device;
    private final int REQUEST_ENABLE_BT = 1337;
    private final String obd_address = "00:06:66:77:72:BF";
    DriveLog log = new DriveLog(38);
    AbsApi api = new AbsApi();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Ref gui elements
        this.progress = (ProgressBar) findViewById(R.id.progressBar);
        this.feedback = (TextView) findViewById(R.id.txtFeedback);

        // Animate Progressbar
        this.progress.setIndeterminate(true);

        // Check if bluetooth is enabled
        if (Connections.blueToothEnabled()) {
            getAdapter();
        } else {
            enableBluetooth();
        }

        bluetoothConnectionProcess();
    }

    // Turn bluetooth on for the device
    private void enableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    // Get the bluetooth adapter for the device
    private void getAdapter() {
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    // Wrapper for whole bluetooth connection
    private void bluetoothConnectionProcess() {

        // Discover device
        BluetoothDevice device = null;

        // Look for device until found
        while (device == null) {
            setFeedbackText("Searching for device ...");
            device = Connections.discoverDevices(adapter, obd_address);
        }

        // Notify user
        setFeedbackText("Device found, name: " + device.getName());

        // Store Device in settings
        Settings.getInstance().setDevice(device);

        // Change activity after wait
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                setFeedbackText("Connecting ...");
                changeActivity();
            }
        }, 2000);

    }

    // Changes screen to Stationary/Home
    private void changeActivity() {
        this.progress.setIndeterminate(false);
        this.startActivity(new Intent(getApplicationContext(), Stationary.class));
        this.overridePendingTransition(0, 0);
        this.finish();
    }

    // Callback
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Enable bluetooth
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                getAdapter();

                bluetoothConnectionProcess();
            } else {

                // Bluetooth enabling failed!
                Toast.makeText(getApplicationContext(), "Bluetooth not enabled on this device!", Toast.LENGTH_LONG);
            }
        }

    }

    private void setFeedbackText(String feedback) {
        this.feedback.setText(feedback);
    }


}