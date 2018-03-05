package com.group14.events_near_me;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;

/**
 * Created by Ben on 26/02/2018.
 *
 * the first activity a new user will see. It prompts them to authenticate with google,
 * once they do that ask for extra information that google doesn't supply
 */

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private GoogleSignInClient googleClient;
    private GoogleSignInAccount account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        googleClient = ((EventsApplication)getApplication()).getClient();

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.confirmDetails).setOnClickListener(this);

        boolean isSignedIn = getIntent().getBooleanExtra("isAuthenticated", false);
        if (isSignedIn) {
            account = ((EventsApplication)getApplication()).getAccount();
            setupDetailsEntry();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                // call the google client to display authentication screen
                Intent signIn = googleClient.getSignInIntent();
                // use request code 123 so we know when we get a response
                startActivityForResult(signIn, 123);
                break;
            case R.id.confirmDetails:
                // when they click to confirm details entry
                boolean gender = ((Switch)findViewById(R.id.genderSelect)).isChecked();

                DatePicker datePicker = findViewById(R.id.dobSelect);
                Calendar dateOfBirth = Calendar.getInstance();
                dateOfBirth.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                // create a new user based on the data they entered
                User user = new User(account.getGivenName(), account.getFamilyName(), gender, dateOfBirth.getTimeInMillis(), account.getIdToken());
                String userID = ((EventsApplication)this.getApplication()).getFirebaseController().addUser(user);
                // write userID to shared preferences
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE).edit();
                editor.putString(getString(R.string.preference_id_key), userID);
                editor.apply();

                Intent intent = new Intent(this, MapActivity.class);
                finish();
                startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the request code is the one that symbolises a response to the google authentication request
        if (requestCode == 123) {
            // get the task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // try and get the google account
                account = task.getResult(ApiException.class);
                ((EventsApplication)this.getApplication()).getFirebaseController().authenticate(account);

                // if no error is thrown the user has authenticated so set up details entry
                setupDetailsEntry();
            } catch (ApiException e) {
                // the user maybe pressed cancel on the authenticate screen so don't change the UI so they can do it again
                e.printStackTrace();
            }
        }
    }

    /**
     * The method enables extra personal details to be shared by the user. After
     * the Google authentication is complete, the invisible elements are made visible
     * which allows the user to select his age and gender. A confirm button to send
     * the data to FireBase is made visible as well. The Google sign-in button is
     * hidden and disabled
     */
    private void setupDetailsEntry() {

        // Create a number picker for the age with values between 1 and 100.
        findViewById(R.id.dobSelect).setVisibility(View.VISIBLE);
        findViewById(R.id.genderSelect).setVisibility(View.VISIBLE);
        findViewById(R.id.confirmDetails).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
    }
}
