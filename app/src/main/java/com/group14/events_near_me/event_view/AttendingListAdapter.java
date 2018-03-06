package com.group14.events_near_me.event_view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.group14.events_near_me.R;
import com.group14.events_near_me.User;

import java.util.ArrayList;

/**
 * Created by marcstevens on 06/03/2018.
 */

public class AttendingListAdapter extends ArrayAdapter<User> {

    private Context context;
    private int resourceId;
    private ArrayList<User> users;

    public AttendingListAdapter(Context context, int layoutResourceId, ArrayList<User> users) {
        super(context, layoutResourceId, users);
        this.context = context;
        this.resourceId = layoutResourceId;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View row;

        if (convertView != null){
            row = convertView;
        }else {
            row = inflater.inflate(resourceId, parent, false);
        }

        User user = users.get(position);
        ((TextView)row.findViewById(R.id.attendingName)).setText(String.format("%s %s", user.firstName, user.surname));

        return row;
    }
}
