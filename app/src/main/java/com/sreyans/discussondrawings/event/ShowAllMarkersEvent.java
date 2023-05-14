package com.sreyans.discussondrawings.event;

import com.sreyans.discussondrawings.model.Drawing;
import com.sreyans.discussondrawings.model.Marker;

import java.util.ArrayList;

public class ShowAllMarkersEvent {
    private Drawing drawing;

    public ShowAllMarkersEvent(Drawing drawing) {
        this.drawing = drawing;
    }

    public Drawing getDrawing() {
        return drawing;
    }

}
