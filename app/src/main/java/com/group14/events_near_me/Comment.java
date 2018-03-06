package com.group14.events_near_me;

/**
 * Created by Ben on 05/03/2018.
 *
 * data storage class for a comment
 */

public class Comment {
    public String userID;
    public String eventID;
    public String body;
    public long timestamp;

    public Comment() {

    }

    public Comment(String userID, String eventID, String body, long timestamp) {
        this.userID = userID;
        this.eventID = eventID;
        this.body = body;
        this.timestamp = timestamp;
    }
}