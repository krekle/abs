package no.krekle.abs.abs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import no.krekle.abs.abs.client.ABSCallback;
import no.krekle.abs.abs.client.AbsApi;
import no.krekle.abs.abs.driver.DriveInstance;
import no.krekle.abs.abs.driver.DriveLog;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Stationary extends Activity implements ABSCallback{

    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stationary);

        test = (TextView) findViewById(R.id.txtTest);

        // Test
        DriveLog log = new DriveLog(3);
        log.addLog(new DriveInstance(60));
        log.addLog(new DriveInstance(30));
        log.addLog(new DriveInstance(40));
        log.addLog(new DriveInstance(60));
        log.addLog(new DriveInstance(70));

        AbsApi api = new AbsApi();
        api.insert(log, this);


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
    public void success(Response response) {
        Log.v("SUCCESS", response.getBody().toString());
        Log.v("SUCCESS", response.getHeaders().toString());
        Log.v("SUCCESS", response.getStatus() + "");

    }

    @Override
    public void failure(RetrofitError response) {
        Log.v("ERROR", response.toString());
    }

    @Override
    public void inProgress() {
        Log.v("IN PROGRESS", "VERY MUCH SO");
    }
}
