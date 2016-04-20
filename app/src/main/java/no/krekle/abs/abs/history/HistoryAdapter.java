package no.krekle.abs.abs.history;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import no.krekle.abs.abs.R;

/**
 * Created by krekle on 17/04/16.
 */
public class HistoryAdapter extends BaseAdapter {
    private Activity parentActivity;
    private ArrayList<HashMap> data;
    private LayoutInflater inflater;


    public HistoryAdapter(Activity a, ArrayList<HashMap> data) {
        this.parentActivity = a;
        this.data = data;
        this.inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.history_row, null);

        ImageView thumb_image=(ImageView)vi.findViewById(R.id.history_row_image); // thumb image
        TextView message = (TextView)vi.findViewById(R.id.history_row_text); // message

        HashMap row_data = data.get(position);

        // Set the image, default for now
        message.setText((String) row_data.get("MSG"));

        return vi;
    }
}
