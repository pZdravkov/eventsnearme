package com.example.group14.events_near_me;

import android.app.Application;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

/**
 * Created by Ben on 26/02/2018.
 */

public class EventsApplication extends Application {
    private GoogleSignInClient googleClient;
    private FirebaseController database;
    @Override
    public void onCreate() {
        super.onCreate();

        database = new FirebaseController();
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestProfile()
                .build();
        googleClient = GoogleSignIn.getClient(this, options);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            database.authenticate(account);
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }
    }

    public GoogleSignInClient getClient() {
        return googleClient;
    }

    public FirebaseController getDatabase() {
        return database;
    }
}
