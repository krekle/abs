package no.krekle.abs.abs;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.math.BigDecimal;
import java.util.Random;

import no.krekle.abs.abs.bluetooth.ABSBluetoothCallback;
import no.krekle.abs.abs.bluetooth.ABSBluetoothConnect;
import no.krekle.abs.abs.bluetooth.ABSBluetoothManager;
import no.krekle.abs.abs.client.ABSCallback;
import no.krekle.abs.abs.client.AbsApi;
import no.krekle.abs.abs.driver.DriveInstance;
import no.krekle.abs.abs.driver.DriveLog;
import no.krekle.abs.abs.history.HistoryActivity;
import no.krekle.abs.abs.settings.Settings;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Stationary extends Activity implements ABSCallback, ABSBluetoothCallback {

    // UI elements
    private CircularProgressBar gauge;
    private ImageButton btnStart;
    private ImageButton btnHistory;
    private ImageButton btnSettings;
    private TextView txtSpeed;
    private TextView txtRPM;
    private TextView txtAvg;

    // Current DriveLog
    private DriveLog driveLog;

    // Update times
    private Long guiUpdate;
    private Long apiUpdate;

    // Api Reference
    private AbsApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationary);

        // Ref to UI elements
        gauge = (CircularProgressBar) findViewById(R.id.stationaryGauge);
        btnStart = (ImageButton) findViewById(R.id.btnStart);
        btnHistory = (ImageButton) findViewById(R.id.btnHistory);
        btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        txtSpeed = (TextView) findViewById(R.id.txtSpeed);
        txtRPM = (TextView) findViewById(R.id.txtRPM);
        txtAvg = (TextView) findViewById(R.id.txtAvg);

        // Click listeners
        applyClickListeners();

        // Retrieve bluetooth device
        BluetoothDevice device = Settings.getInstance().getDevice();

        // Connect and listen
        ABSBluetoothConnect connect = new ABSBluetoothConnect(device, this);
        connect.setName("Connect Thread");
        connect.start();

        // Create api
        api = new AbsApi();

        // Set default times
        guiUpdate = System.nanoTime();
        apiUpdate = System.nanoTime();

        gauge.setProgressBarWidth(1 * 100);
        gauge.setBackgroundProgressBarWidth(1 * 100);

    }

    private void applyClickListeners() {
        final Random rand = new Random();
        // Start
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Start Clicked", Toast.LENGTH_SHORT).show();
                //gauge.setProgressWithAnimation(rand.nextFloat() * 100, 100);
            }
        });
        // History
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "History Clicked", Toast.LENGTH_SHORT).show();
                //gauge.setProgressBarWidth(rand.nextFloat() * 100);

                startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                overridePendingTransition(0, 0);

            }
        });
        // Settings
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Settings Clicked", Toast.LENGTH_SHORT).show();
                //gauge.setBackgroundProgressBarWidth(rand.nextFloat() * 100);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stationary, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //////////////////////////
    //      CALLBACKS       //
    //////////////////////////


    // API Callback
    @Override
    public void success(Response response) {
        //Log.v("SUCCESS", response.getBody().toString());
    }

    @Override
    public void failure(RetrofitError response) {
        //Log.v("ERROR", response.toString());
    }

    @Override
    public void inProgress() {
        //Log.v("IN PROGRESS", "VERY MUCH SO");}
    }

    // CONNECT Bluetooth - Callback
    @Override
    public void bluetoothFailed() {
        Log.v("BLUETOOTH CONNECTION", "FAILED");
    }

    @Override
    public void bluetoothConnected() {
        Toast.makeText(getApplicationContext(), "Device Connection Failed", Toast.LENGTH_SHORT);
        Log.v("BLUETOOTH CONNECTION", "SUCCESS");
    }

    // MANAGE Bluetooth

    // Callback method, here all the parsed bluetooth data is recieved
    @Override
    public void bluetoothCallback(final DriveInstance driveInstance) {
        // Get settings singleton
        Settings settings = Settings.getInstance();

        // Create new DriveLog, if empty
        //if (driveLog == null)
        driveLog = new DriveLog(settings.getDriveId());
        //}

        // Add driveInstance to Log
        driveLog.addLog(driveInstance);

        // Send to api if interval is correct
        if (this.apiUpdate == null || ((System.nanoTime() - this.apiUpdate) / 1000000000.0) >= settings.getApiUpdateInterval()) {
            api.insert(driveLog, this);
        }

        // Update GUI
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(!Settings.getInstance().isSpeedOnly()) {

                // Speed in
                gauge.setProgressWithAnimation((float) driveInstance.getKmSpeed(), 10);
                // Throttle in %
                gauge.setProgressBarWidth(100 + (float)(driveInstance.getThrottle()*0.5));
                // Brake in %
                gauge.setBackgroundProgressBarWidth(100 + (float)(driveInstance.getBrake()*0.5));


                // Update TextViews
                txtSpeed.setText(round((float)driveInstance.getKmSpeed(), 2) + " Km/h");
                txtRPM.setText(driveInstance.getRpm()+ " 1000/min");
                txtAvg.setText(driveInstance.getBrake() + "");

                } else {
                    // Speed in
                    gauge.setProgressWithAnimation((float) driveInstance.getKmSpeed(), 10);

                    // Update TextViews
                    txtSpeed.setText(round((float)driveInstance.getKmSpeed(), 2) + " Km/h");
                    txtRPM.setText("");
                    txtAvg.setText("");
                }
            }
        });
    }

    @Override
    public void bluetoothManager(ABSBluetoothManager manager) {
        Log.v("BLUETOOTH", "MANAGER" + manager.toString());
    }

    @Override
    public void bluetoothListening() {
        Log.v("BLUETOOTH", "LISTENING");
    }

    ////////////////
    //   HELPERS  //
    ////////////////

    private float round(float d, int decimalPlace) {
        return BigDecimal.valueOf(d).setScale(decimalPlace,BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
