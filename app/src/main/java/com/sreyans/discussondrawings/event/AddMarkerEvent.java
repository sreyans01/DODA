package com.sreyans.discussondrawings.event;

import com.sreyans.discussondrawings.model.Marker;

public class AddMarkerEvent {
    private Marker marker;

    public AddMarkerEvent(Marker marker) {
        this.marker = marker;
    }

    public Marker getMarker() {
        return marker;
    }

}
