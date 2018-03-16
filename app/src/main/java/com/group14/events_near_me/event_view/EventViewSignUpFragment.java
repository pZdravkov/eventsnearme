package com.group14.events_near_me.event_view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
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

public class EventViewSignUpFragment extends Fragment {

    private boolean attending = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_view_sign_up, null);

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

        // get properties to put into table
        String userID = ((EventsApplication)getActivity().getApplication()).getFirebaseController().getCurrentUserId();
        String eventID = ((EventViewActivity)getActivity()).getEventID();

        long timestamp = Calendar.getInstance().getTimeInMillis();

        SignUp signup = new SignUp(eventID, userID, timestamp);
        ref.child(key).setValue(signup);

        attending = true;

    }
    public void setAttending(){
        this.attending = true;
        try {
            getActivity().findViewById(R.id.isAttending).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.signUp).setEnabled(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
