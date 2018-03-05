package com.group14.events_near_me;

/**
 * Created by Ben on 28/02/2018.
 *
 * data storage class for an event
 */

public class Event {
    public float lat;
    public float lng;
    public long startTime;
    public long endTime;
    public String ownerID;

    public Event() {

    }

    public Event(float lat, float lng, long startTime, long endTime, String ownerID) {
        this.lat = lat;
        this.lng = lng;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ownerID = ownerID;
    }
}