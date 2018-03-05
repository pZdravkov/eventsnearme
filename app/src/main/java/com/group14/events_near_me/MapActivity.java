package com.group14.events_near_me;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Calendar;

/**
 * Created by Ben on 26/02/2018.
 *
 * displays events on a map
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, ChildEventListener, GestureDetector.OnGestureListener {
    private FirebaseController firebase;
    private GoogleMap map;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        firebase = ((EventsApplication)this.getApplication()).getFirebaseController();

        // start the background task of getting a google map
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        gestureDetector = new GestureDetector(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebase.getRoot().child("events").removeEventListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
        // when the map is ready get events to add to it
        firebase.getRoot().child("events").addChildEventListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // when the user clicks on the map in an empty spot create an event there
        // TODO change this, it's just for testing
        Log.d("MyDebug", "clicked on map");
        String key = firebase.getDatabase().getReference().child("events").push().getKey();
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.roll(Calendar.HOUR_OF_DAY, true);
        Event event = new Event((float)latLng.latitude, (float)latLng.longitude,
                calendar.getTimeInMillis(), calendar2.getTimeInMillis(), firebase.getCurrentUserId());
        firebase.getRoot().child("events").child(key).setValue(event);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // when the user clicks a marker for an event delete the event
        // TODO change this, it's just for testing
        marker.remove();
        firebase.getDatabase().getReference("/events/" + marker.getTitle()).removeValue();
        return true;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        Log.d("MyDebug", "onChildAdded:" + dataSnapshot.getKey());

        Event event = dataSnapshot.getValue(Event.class);
        Log.d("MyDebug", event.lat + ", " + event.lng);

        // create a marker for the event and add it to the map
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(event.lat, event.lng));
        markerOptions.title(dataSnapshot.getKey());
        map.addMarker(markerOptions);
    }

    // TODO support when events are changed and removed, probably with an arraylist of the markers

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
        Log.d("MyDebug", "onChildChanged:" + dataSnapshot.getKey());

        Event event = dataSnapshot.getValue(Event.class);
        Log.d("MyDebug", event.lat + ", " + event.lng);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.d("MyDebug", "onChildRemoved:" + dataSnapshot.getKey());

        Event event = dataSnapshot.getValue(Event.class);
        Log.d("MyDebug", event.lat + ", " + event.lng);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
        Log.d("MyDebug", "onChildMoved:" + dataSnapshot.getKey());

        Event event = dataSnapshot.getValue(Event.class);
        Log.d("MyDebug", event.lat + ", " + event.lng);
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
        // when the user swipes right go to the list view
        // TODO add transition animation
        if (motionEvent.getRawX() > motionEvent1.getRawX()) {
            Intent intent = new Intent(this, ListActivity.class);
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