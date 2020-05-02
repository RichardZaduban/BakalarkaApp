package com.example.bakalarka10;

public class Event {
     String title;
     String description;
     String location;
     String id;
     long start, end;

    public Event(String title, String description, String location, String id, long start, long end) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.id = id;
        this.start = start;
        this.end = end;
    }
}
