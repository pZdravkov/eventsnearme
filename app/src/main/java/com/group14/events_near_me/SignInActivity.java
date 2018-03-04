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
                Intent signIn = googleClient.getSignInIntent();
                startActivityForResult(signIn, 123);
                break;
            case R.id.confirmDetails:
                boolean gender = ((Switch)findViewById(R.id.genderSelect)).isChecked();

                DatePicker datePicker = findViewById(R.id.dobSelect);
                Calendar dateOfBirth = Calendar.getInstance();
                dateOfBirth.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                User user = new User(account.getGivenName(), account.getFamilyName(), gender, dateOfBirth.getTimeInMillis(), account.getIdToken());
                String userID = ((EventsApplication)this.getApplication()).getDatabase().addUser(user);
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

        if (requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                account = task.getResult(ApiException.class);
                ((EventsApplication)this.getApplication()).getDatabase().authenticate(account);

                setupDetailsEntry();
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "An error occurred, please try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     +     * The method enables extra personal details to be shared by the user. After
     +     * the Google authentication is complete, the invisible elements are made visible
     +     * which allows the user to select his age and gender. A confirm button to send
     +     * the data to Firebase is made visible as well. The Google sign-in button is
     +     * hidden and disabled.
     +     */
    private void setupDetailsEntry() {
        findViewById(R.id.ageText).setVisibility(View.VISIBLE);

        // Create a number picker for the age with values between 1 and 100.
        findViewById(R.id.dobSelect).setVisibility(View.VISIBLE);
        findViewById(R.id.genderText).setVisibility(View.VISIBLE);
        findViewById(R.id.genderSelect).setVisibility(View.VISIBLE);
        findViewById(R.id.confirmDetails).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
    }
}
