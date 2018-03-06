package com.group14.events_near_me;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * Created by Ben on 26/02/2018.
 *
 * The application class for this app handles what activity should be displayed when the app is launched
 * and provides a means for activities to share the FirebaseController
 */

public class EventsApplication extends Application {
    private GoogleSignInClient googleClient;
    private FirebaseController firebase;
    private GoogleSignInAccount account;
    @Override
    public void onCreate() {
        super.onCreate();

        // create a FirebaseController
        firebase = new FirebaseController();
        // generate what details we want from the google sign in session
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestProfile()
                .build();
        googleClient = GoogleSignIn.getClient(this, options);

        account = GoogleSignIn.getLastSignedInAccount(this);
        // check if the account returned non null, meaning the user is already authenticated
        if (account != null) {
            // authenticate with firebase using the google sign in
            firebase.authenticate(account);

            // check shared preferences for user id
            SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE);
            if (preferences.contains(getString(R.string.preference_id_key))) {
                // if preference exists user already has an account go to main map
                firebase.setCurrentUserId(preferences.getString(getString(R.string.preference_id_key), ""));

                Intent intent = new Intent(this, ListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // user has authenticated but does not have an account
                Intent intent = new Intent(this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("isAuthenticated", true);
                startActivity(intent);
            }
        } else {
            // if they aren't go to the sign in activity
            Intent intent = new Intent(this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public GoogleSignInClient getClient() {
        return googleClient;
    }

    public FirebaseController getFirebaseController() {
        return firebase;
    }

    public GoogleSignInAccount getAccount() {
        return account;
    }
}
