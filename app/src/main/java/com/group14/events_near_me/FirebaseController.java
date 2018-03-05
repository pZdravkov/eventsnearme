package com.group14.events_near_me;

import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
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
