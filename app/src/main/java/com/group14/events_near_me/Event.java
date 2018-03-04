package com.group14.events_near_me;

/**
 * Created by Ben on 28/02/2018.
 */

public class Event {
    public float lat;
    public float lng;
    public Long startTime;
    public Long endTime;
    public String ownerID;

    public Event() {

    }

    public Event(float lat, float lng, Long startTime, Long endTime, String ownerID) {
        this.lat = lat;
        this.lng = lng;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ownerID = ownerID;
    }
}