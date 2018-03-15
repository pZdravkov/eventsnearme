package com.group14.events_near_me.event_view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.group14.events_near_me.EventsApplication;
import com.group14.events_near_me.R;
import com.group14.events_near_me.SignUp;

import java.util.Calendar;

/**
 * Created by Ben on 05/03/2018.
 */

public class EventViewSignUpFragment extends Fragment implements ValueEventListener {


    String userID = null;
    String eventID = null;

    boolean attending = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_view_sign_up, null);

        userID = ((EventsApplication)getActivity().getApplication()).getFirebaseController().getCurrentUserId();
        eventID = ((EventViewActivity)getActivity()).getEventID();


        // check firebase if user has signed up to this event
        ((EventsApplication)getActivity().getApplication()).getFirebaseController().getRoot().child("signups")
                .addListenerForSingleValueEvent(this);

        if(attending) {
            view.findViewById(R.id.isAttending).setVisibility(View.VISIBLE);
            view.findViewById(R.id.signUp).setEnabled(false);
        }

        view.findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpToEvent();
            }
        });

        return view;
    }
    public void signUpToEvent(){

        // get reference to signups table
        DatabaseReference ref = ((EventsApplication)getActivity().getApplication()).getFirebaseController().getRoot().child("signups");

        String key = ref.push().getKey();

        String name = ((EventsApplication)getActivity().getApplication()).getFirebaseController().getCurrentUserName();
        long timestamp = Calendar.getInstance().getTimeInMillis();

        SignUp signup = new SignUp(eventID, userID, timestamp, name);
        ref.child(key).setValue(signup);

    }
    private void setAttending(){
        Log.v("setAttending()", "user is attending this event");
        getActivity().findViewById(R.id.isAttending).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.signUp).setEnabled(false);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for(DataSnapshot signup: dataSnapshot.getChildren()) {
            if (signup.child("userID").getValue().toString().equals(userID) &&
                    signup.child("eventID").getValue().toString().equals(eventID)) {
                // user is signed up to event
                setAttending();
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
