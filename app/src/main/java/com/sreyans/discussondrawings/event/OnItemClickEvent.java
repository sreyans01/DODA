package com.sreyans.discussondrawings.event;

import com.sreyans.discussondrawings.model.Marker;

public class OnItemClickEvent {
    private Object data;

    public OnItemClickEvent(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

}
