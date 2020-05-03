package com.example.bakalarka10;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import java.util.Objects;
import java.util.TimeZone;

public class QueryHandler extends AsyncQueryHandler {

    private static final String TAG = "QueryHandler";

    // Projection arrays
    private static final String[] CALENDAR_PROJECTION = new String[]
            {
                    CalendarContract.Calendars._ID
            };

    // The indices for the projection array above.
    private static final int CALENDAR_ID_INDEX = 0;

    private static final int CALENDAR = 0;
    private static final int EVENT = 1;
    private static final int REMINDER = 2;
    private ContentValues values = new ContentValues();
    private static QueryHandler queryHandler;

    // QueryHandler
    public QueryHandler(ContentResolver resolver) {
        super(resolver);
    }


    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        // Use the cursor to move through the returned records
        NewEvent event = new NewEvent();
        Log.d(TAG, "onQueryComplete: "+ event.getTitle());

        cursor.moveToFirst();

        // Get the field values
        long calendarID = cursor.getLong(CALENDAR_ID_INDEX);

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Calendar query complete " + calendarID);

        values.put(CalendarContract.Events.CALENDAR_ID, calendarID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName());
        values.put(CalendarContract.Events.TITLE, EventNew.title);
        values.put(CalendarContract.Events.DTSTART, EventNew.start);
        values.put(CalendarContract.Events.DTEND, EventNew.end);
        values.put(CalendarContract.Events.DESCRIPTION, EventNew.description);

// TOTO SU VECI CO SA INSERTNU SKUTOCNE ako event

        Log.d(TAG, String.valueOf(CALENDAR_ID_INDEX));
        Log.d(TAG, TimeZone.getDefault().getDisplayName());
        Log.d(TAG, String.valueOf(System.currentTimeMillis()));

        startInsert(EVENT, null, CalendarContract.Events.CONTENT_URI, values);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        if (uri != null)
        {
            if (BuildConfig.DEBUG)
                Log.d(TAG, "Insert complete " + uri.getLastPathSegment());

            switch (token)
            {
                case EVENT:
                    long eventID = Long.parseLong(Objects.requireNonNull(uri.getLastPathSegment()));
                    ContentValues values = new ContentValues();
                    values.put(CalendarContract.Reminders.MINUTES, 10);
                    values.put(CalendarContract.Reminders.EVENT_ID, eventID);
                    values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);

                    startInsert(REMINDER, null, CalendarContract.Reminders.CONTENT_URI, values);
                    break;
            }
        }
    }

    // insertEvent
    public static void insertEvent(Context context) {
        ContentResolver resolver = context.getContentResolver();

        if (queryHandler == null)
            queryHandler = new QueryHandler(resolver);

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, 1);

        if (BuildConfig.DEBUG)
            Log.d(TAG, "Calendar query start");
            Log.d(TAG, String.valueOf(CALENDAR_PROJECTION));
            Log.d(TAG, String.valueOf(CalendarContract.Calendars.CONTENT_URI));

        queryHandler.startQuery(CALENDAR, values, CalendarContract.Calendars.CONTENT_URI,
                CALENDAR_PROJECTION, null, null, null);

    }


}