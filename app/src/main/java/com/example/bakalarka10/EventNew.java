package com.example.bakalarka10;


//Model novej udalosti
public class EventNew {

    static String title;
    static String description;
    static String location;
    static String id;
    static long start, end;



    public EventNew(String title, String description, String location, long start, long end, String id) {
        this.title = title;
        this.description = description;
        EventNew.location = location;
        this.start = start;
        this.end = end;
        this.id = id;

    }

}
