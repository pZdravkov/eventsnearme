package com.group14.events_near_me.event_view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.group14.events_near_me.EventsApplication;
import com.group14.events_near_me.R;
import com.group14.events_near_me.SignUp;

import java.util.Calendar;

/**
 * Created by Ben on 05/03/2018.
 */

public class EventViewSignUpFragment extends Fragment {

    private boolean isSignedUp = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_view_sign_up, null);

        if(isSignedUp){
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
        String name = ((EventsApplication)getActivity().getApplication()).getFirebaseController().getCurrentUserName();

        // generate timestamp of current time. getInstance will give a calendar of current time
        Calendar calendar = Calendar.getInstance();
        long timestamp = calendar.getTimeInMillis();

        SignUp signup = new SignUp(eventID, userID, timestamp, name);
        ref.child(key).setValue(signup);

        isSignedUp = true;


    }
}
