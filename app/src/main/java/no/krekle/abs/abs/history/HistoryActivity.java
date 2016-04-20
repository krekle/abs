package no.krekle.abs.abs.history;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import no.krekle.abs.abs.R;

public class HistoryActivity extends Activity {

    private ListView historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyList = (ListView) findViewById(R.id.history_list);

        // Update the list
        updateList();
    }


    public void updateList() {
        // Create adapter with current history list
        HistoryAdapter adapter = new HistoryAdapter(this, History.getInstance().getHistory());

        // Link adapter to listview
        historyList.setAdapter(adapter);
    }

}
