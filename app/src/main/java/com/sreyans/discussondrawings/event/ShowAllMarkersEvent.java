package com.sreyans.discussondrawings.event;

import com.sreyans.discussondrawings.model.Drawing;

public class ShowAllMarkersEvent {
    private Drawing drawing;

    public ShowAllMarkersEvent(Drawing drawing) {
        this.drawing = drawing;
    }

    public Drawing getDrawing() {
        return drawing;
    }

}
