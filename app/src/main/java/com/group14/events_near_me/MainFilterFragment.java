package com.group14.events_near_me;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ben on 12/03/2018.
 */
public class MainFilterFragment extends Fragment implements View.OnClickListener {
    private HashMap<String, Event> events;
    private ArrayList<String> eventNames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events = ((MainActivity)getActivity()).getEvents();
        eventNames = ((MainActivity)getActivity()).getEventNames();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_filter, null);
        view.findViewById(R.id.MainFilterDistance).setOnClickListener(this);
        view.findViewById(R.id.MainFilterStart).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.MainFilterDistance:
                break;
            case R.id.MainFilterStart:
                break;
        }
    }
}
