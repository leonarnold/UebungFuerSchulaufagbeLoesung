package com.sabel.ufsa;

import java.util.Date;

public class Message {
    private String text;
    private long timestamp;

    public Message(String text) {
        this.text = text;
        this.timestamp = new Date().getTime();
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
