package com.group14.events_near_me;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ben on 04/03/2018.
 */

public class EventListAdapter extends ArrayAdapter<String> {
    private Context context;
    private HashMap<String, Event> events;
    private ArrayList<String> eventNames;
    private int layoutResourceId;

    public EventListAdapter(Context context, int layoutResourceId, ArrayList<String> eventNames, HashMap<String, Event> events) {
        super(context, layoutResourceId, eventNames);
        this.context = context;
        this.events = events;
        this.eventNames = eventNames;
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

        Event e = events.get(eventNames.get(position));

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