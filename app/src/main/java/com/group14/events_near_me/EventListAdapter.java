package com.group14.events_near_me;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ben on 04/03/2018.
 */

public class EventListAdapter extends ArrayAdapter<Event> {
    private Context context;
    private ArrayList<Event> events;
    private int layoutResourceId;

    public EventListAdapter(Context context, int layoutResourceId, ArrayList<Event> events) {
        super(context, layoutResourceId, events);
        this.context = context;
        this.events = events;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View row;
        if (convertView != null) {
            row = convertView;
        } else {
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        Event e = events.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.UK);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(e.startTime);
        String s = sdf.format(calendar.getTime());
        ((TextView)row.findViewById(R.id.eventListStartTime)).setText(s);

        calendar.setTimeInMillis(e.endTime);
        s = sdf.format(calendar.getTime());
        ((TextView)row.findViewById(R.id.eventListEndTime)).setText(s);

        ((TextView)row.findViewById(R.id.eventListLocation)).setText(e.lat + ", " + e.lng);

        return row;
    }
}