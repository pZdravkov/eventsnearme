package com.group14.events_near_me;

/**
 * Created by Ben on 27/02/2018.
 */

public class User {
    public String firstName;
    public String surname;
    public boolean gender;
    public Long dateOfBirth;
    public String googleAuthToken;

    public User() {

    }

    public User(String firstName, String surname, boolean gender, Long dateOfBirth, String googleAuthToken) {
        this.firstName = firstName;
        this.surname = surname;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.googleAuthToken = googleAuthToken;
    }
}