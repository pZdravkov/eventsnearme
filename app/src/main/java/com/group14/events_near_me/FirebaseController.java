package com.group14.events_near_me;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Ben on 26/02/2018.
 *
 * This class controls the connection with firebase, and serves references to the application
 */

public class FirebaseController {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String currentUserId;
  
    public FirebaseController() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://com2027-group14-1519651413217.firebaseio.com/");
        database.setPersistenceEnabled(true);
        ref = database.getReference();
    }

    public void authenticate(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential);
    }

    public String addUser(User user) {
        currentUserId = ref.child("users").push().getKey();
        ref.child("users").child(currentUserId).setValue(user);
        return currentUserId;
    }

    public void setTextViewToName(final TextView view, String userID) {
        ref.child("users/" + userID + "/firstName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // split the current name in two around the space between forename and surname
                String[] splitName = view.getText().toString().split(" ");
                splitName[0] = dataSnapshot.getValue(String.class);
                view.setText(splitName[0] + " " + splitName[1]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });

        ref.child("users/" + userID + "/surname").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // split the current name in two around the space between forename and surname
                String[] splitName = view.getText().toString().split(" ");
                splitName[1] = dataSnapshot.getValue(String.class);
                view.setText(splitName[0] + " " + splitName[1]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }


    public void setCurrentUserId(String id) {
        currentUserId = id;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public DatabaseReference getRoot() {
        return ref;
    }
}
