package com.sreyans.discussondrawings.event;

public class OnItemClickEvent {
    private Object data;

    public OnItemClickEvent(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

}
