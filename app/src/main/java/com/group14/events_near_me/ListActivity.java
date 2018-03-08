package com.group14.events_near_me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.group14.events_near_me.event_view.EventViewActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ben on 04/03/2018.
 *
 * The activity which displays events in list form
 */

public class ListActivity extends AppCompatActivity implements ChildEventListener, GestureDetector.OnGestureListener {
    private HashMap<String, Event> events = new HashMap<>();
    private ArrayList<String> eventNames = new ArrayList<>();
    private ListView list;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        // set the adapter for the list
        list = findViewById(R.id.eventListView);
        list.setAdapter(new EventListAdapter(this, R.layout.events_list_line, eventNames, events));

        // register a listener for events
        ((EventsApplication)getApplication()).getFirebaseController().getRoot().child("events").addChildEventListener(this);

        // when an event is clicked get the ID of that event and pass it to the EventViewActivity
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String eventID = eventNames.get(i);
                Intent intent = new Intent(ListActivity.this, EventViewActivity.class);
                intent.putExtra("EventID", eventID);
                startActivity(intent);
            }
        });

        // set this as a gesture Detector for the side swipe
        gestureDetector = new GestureDetector(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // remove listener
        ((EventsApplication)getApplication()).getFirebaseController().getRoot().child("events").removeEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        Log.d("MyDebug", "EventsList: onChildAdded:" + dataSnapshot.getKey());

        // add the new event to both the hashmap and its ID to the arraylist
        Event event = dataSnapshot.getValue(Event.class);
        events.put(dataSnapshot.getKey(), event);
        eventNames.add(dataSnapshot.getKey());
        // force the list to redraw itself with the new event
        list.invalidateViews();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
        Log.d("MyDebug", "EventsList: onChildChanged:" + dataSnapshot.getKey());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.d("MyDebug", "onChildRemoved:" + dataSnapshot.getKey());

        Event event = dataSnapshot.getValue(Event.class);
        // find the event in the events hashmap and remove it
        for (int x = 0; x < events.size(); x++) {
            if (events.get(x).equals(event)) {
                events.remove(x);
            }
        }
        // find the event's ID in the eventNames and remove it
        for (int x = 0; x < eventNames.size(); x++) {
            if (eventNames.get(x).equals(dataSnapshot.getKey())) {
                eventNames.remove(x);
            }
        }
        // redraw the list without the event
        list.invalidateViews();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
        Log.d("MyDebug", "EventsList: onChildMoved:" + dataSnapshot.getKey());
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w("MyDebug", "postComments:onCancelled", databaseError.toException());
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        // detect a swipe to the left and when it happens move to the map view
        // TODO add transition animation
        if (motionEvent.getRawX() < motionEvent1.getRawX()) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
            finish();

            return true;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }
}