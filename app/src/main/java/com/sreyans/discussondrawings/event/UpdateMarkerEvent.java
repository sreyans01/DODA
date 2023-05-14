package com.sreyans.discussondrawings.event;

import com.sreyans.discussondrawings.model.Drawing;
import com.sreyans.discussondrawings.model.Marker;

public class UpdateMarkerEvent {
    private Marker marker;
    private Drawing drawing;

    public UpdateMarkerEvent(Marker marker, Drawing drawing) {
        this.marker = marker;
        this.drawing = drawing;
    }

    public Marker getMarker() {
        return marker;
    }

    public Drawing getDrawing() {
        return drawing;
    }

}
