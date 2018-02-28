package com.group14.events_near_me;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Ben on 26/02/2018.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, ChildEventListener {
    FirebaseController firebase;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        firebase = ((EventsApplication)this.getApplication()).getDatabase();

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
        firebase.getRoot().child("events").addChildEventListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d("MyDebug", "clicked on map");
        String s = firebase.getDatabase().getReference().child("events").push().getKey();
        firebase.getRoot().child("events").child(s).setValue(new Event((float)latLng.latitude, (float)latLng.longitude, System.nanoTime(), firebase.getCurrentUserId()));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.remove();
        firebase.getDatabase().getReference("/events/" + marker.getTitle()).removeValue();
        return true;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
        Log.d("MyDebug", "onChildAdded:" + dataSnapshot.getKey());

        Event event = dataSnapshot.getValue(Event.class);
        Log.d("MyDebug", event.lat + ", " + event.lng);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(event.lat, event.lng));
        markerOptions.title(dataSnapshot.getKey());
        map.addMarker(markerOptions);
    }

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
}
