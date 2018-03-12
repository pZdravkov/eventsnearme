package com.group14.events_near_me.event_view;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.group14.events_near_me.EventsApplication;
import com.group14.events_near_me.R;
import com.group14.events_near_me.User;

import java.util.ArrayList;

/**
 * Created by Ben on 05/03/2018.
 */

public class EventViewAttendingFragment extends ListFragment implements ChildEventListener {

    private ArrayList<User> users = new ArrayList<>();
    String eventID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retrieve event id
        eventID = ((EventViewActivity)getActivity()).getEventID();

        // set list adapter for attending list
        setListAdapter(new AttendingListAdapter(getContext(), R.layout.event_attending_list_line, users));

        ((EventsApplication)getActivity().getApplication()).getFirebaseController()
                .getRoot().child("signups").orderByChild("eventID")
                .equalTo(eventID).addChildEventListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_view_attending, null);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        // add new comment to list of comments then update listView
        User user = dataSnapshot.getValue(User.class);
        users.add(user);
        getListView().invalidateViews();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        // find that comment in the list and remove it
        for (int x = 0; x < users.size(); x++) {
            if (users.get(x).equals(user)) {
                users.remove(x);
            }
        }
        getListView().invalidateViews();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
