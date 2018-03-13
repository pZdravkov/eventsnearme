package com.group14.events_near_me.event_view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.group14.events_near_me.R;
import com.group14.events_near_me.SignUp;

import java.util.ArrayList;

/**
 * Created by marcstevens on 06/03/2018.
 */

public class AttendingListAdapter extends ArrayAdapter<SignUp> {

    private Context context;
    private int resourceId;
    private ArrayList<SignUp> signUps;

    public AttendingListAdapter(Context context, int layoutResourceId, ArrayList<SignUp> signUps) {
        super(context, layoutResourceId, signUps);
        this.context = context;
        this.resourceId = layoutResourceId;
        this.signUps = signUps;
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

        SignUp signUp = signUps.get(position);
        ((TextView)row.findViewById(R.id.attendingName)).setText(signUp.userID);

        return row;
    }
}
