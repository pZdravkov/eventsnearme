package com.group14.events_near_me;

/**
 * Created by Ben on 28/02/2018.
 */

public class Event {
    public float lat;
    public float lng;
    public Long startTime;
    public String ownerID;

    public Event() {

    }

    public Event(float lat, float lng, Long startTime, String ownerID) {
        this.lat = lat;
        this.lng = lng;
        this.startTime = startTime;
        this.ownerID = ownerID;
    }
}
