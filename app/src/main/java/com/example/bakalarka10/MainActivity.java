package com.example.bakalarka10;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "NewEvent";

    Cursor cursor;
    int myPermissionRequestCalendar = 0;
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Event> events;
    FloatingActionButton fab;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout layout;
    Event event;
    Snackbar snackbar;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.add);
        layout = findViewById(R.id.main_relative);


        // ItemTouchHelper slúži na zaznamenávanie interakcie s prvkami v RecyclerView

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if ((direction == ItemTouchHelper.LEFT) || (direction == ItemTouchHelper.RIGHT) ) {
               String eventToDelete = adapter.deleteEvent(position);

                    final String DEBUG_TAG = "Start delete";

                    //Vymazanie udalosti z databázy

                    long eventID = Long.parseLong(eventToDelete);
                   // ContentResolver cr = getContentResolver();
                   // ContentValues values = new ContentValues();
                    Uri deleteUri = null;
                    deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                    int rows = getContentResolver().delete(deleteUri, null, null);
                    Log.i(DEBUG_TAG, "Rows deleted: " + rows);

                }

                // Vtvorenie snackbaru po vymazaní udalosti

                snackbar = Snackbar.make(layout, "Event was was deleted", Snackbar.LENGTH_INDEFINITE);
                snackbar .setDuration(3000);
                snackbar.show();
            }

        };
            new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(recyclerView);

        // ItemTouchHelper slúži na zaznamenávanie interakcie s prvkami v RecyclerView



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEvents();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) ;
        else {
            requestCalendarPermission();
        }

        getEvents();

        // Využitie floating buttonu na prejdenie na aktivitu pridávania
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewEvent.class);
                startActivity(intent);
            }
        });
    }




    // Táto metóda vráti udalosti kalendára


    private void getEvents() {
            final String[] EVENT_PROJECTION = new String[]{
                    CalendarContract.Events.TITLE,                         // 0
                    CalendarContract.Events.DESCRIPTION,                   // 1
                    CalendarContract.Events.EVENT_LOCATION,                // 2
                    CalendarContract.Events.ACCOUNT_NAME,                   // 3
                    CalendarContract.Events._ID,
                    CalendarContract.Events.DTSTART,
                    CalendarContract.Events.DTEND

            };

            ContentResolver cr = getContentResolver();
            Uri uri = CalendarContract.Events.CONTENT_URI;

            long selectionTime = System.currentTimeMillis()-86400000;
        String selection = "(( " + CalendarContract.Events.DTSTART + " >= " + selectionTime + "))";

        // Prechádzanie udalostí v databáze

            cursor = cr.query(uri, EVENT_PROJECTION, selection, null, CalendarContract.Events.DTSTART
            );

            // Prechádzanie udalostí v databáze
        assert cursor != null;
        if (cursor.getCount() > 0) {

                events = new ArrayList<Event>();

                assert cursor != null;
            while (cursor.moveToNext()) {
                    if (cursor != null) {


                            String titleValue = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
                            String descriptionValue = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                            String locationValue = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                            long startValue = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART));
                            long endValue = cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTEND));
                            String idValue = cursor.getString(cursor.getColumnIndex(CalendarContract.Events._ID));
                            String nameValue = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.ACCOUNT_NAME));

                            //Pridanie hodnôt z kalendára do recyclerView

                            Event event = new Event(titleValue, descriptionValue, locationValue, idValue, startValue, endValue);

                            events.add(event);
                        Log.d(TAG, "getEvents: "+ titleValue);
                        Log.d(TAG, "getEvents: "+ event);
                        Log.d(TAG, "getEvents: "+ nameValue);

                            recyclerView.setLayoutManager(new LinearLayoutManager(this));

                            adapter = new Adapter(this,events);
                        Log.d(TAG, "getEvents: " + adapter);
                            recyclerView.setAdapter(adapter);
                    }
                }
            }


        }

    //Metóda slúžiaca na získanie potrebných povolení

    private void requestCalendarPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CALENDAR) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for app to load data from your calendar")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR},
                                    myPermissionRequestCalendar);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();

        }
        else
                {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR},
                            myPermissionRequestCalendar);
                }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == myPermissionRequestCalendar){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
    }
}

// Metódy na vyhľadávanie v udalostich podľa názvu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.events_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }


}



