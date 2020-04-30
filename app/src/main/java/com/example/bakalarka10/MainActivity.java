package com.example.bakalarka10;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Cursor cursor;
    int myPermissionRequestCalendar = 0;
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Event> events;
    FloatingActionButton fab;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.add);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) ;
        else {
            requestCalendarPermission();
        }

        getEvents();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewEvent.class);
                startActivity(intent);
            }
        });
    }


    // tato metoda vrati udaje z Events Tabulky a vytvori array objektov triedy Event a kazdy objekt zobrazi ako samostatnu kartu

    private void getEvents() {
            final String[] EVENT_PROJECTION = new String[]{
                    CalendarContract.Events.TITLE,                         // 0
                    CalendarContract.Events.DESCRIPTION,                   // 1
                    CalendarContract.Events.EVENT_LOCATION,                // 2
                    CalendarContract.Events.ACCOUNT_NAME                   // 3
            };

            ContentResolver cr = getContentResolver();
            Uri uri = CalendarContract.Events.CONTENT_URI;


            cursor = cr.query(uri, EVENT_PROJECTION, null, null, CalendarContract.Events.DTSTART
            );


            if (cursor.getCount() > 0) {

                events = new ArrayList<>();

                while (cursor.moveToNext()) {
                    if (cursor != null) {


                            String titleValue = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
                            String descriptionValue = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                            String locationValue = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                            String nameValue = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.ACCOUNT_NAME));

                            Event event = new Event(titleValue, descriptionValue, null, null);

                            events.add(event);

                            recyclerView.setLayoutManager(new LinearLayoutManager(this));

                            adapter = new Adapter(this, events);
                            recyclerView.setAdapter(adapter);

                    }
                }
            }

        }

    //tato metoda sluzi na poziadanie o povolenia aplikacie

    private void requestCalendarPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CALENDAR)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for app to load data from your calendar")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_CALENDAR},
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
                            new String[]{Manifest.permission.READ_CALENDAR},
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
}



