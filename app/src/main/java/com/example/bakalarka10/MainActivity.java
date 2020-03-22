package com.example.bakalarka10;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    Cursor cursor;
    int myPermissionRequestCalendar = 0;
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);



        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED);
        else {
            requestCalendarPermission();
        }

        final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Events.TITLE,                          // 0
                CalendarContract.Events.DESCRIPTION,                   // 1
                CalendarContract.Events.EVENT_LOCATION,                    // 2
                CalendarContract.Events.ACCOUNT_NAME                   // 3
        };

        final int PROJECTION_TITLE_INDEX = 0;
        final int PROJECTION_DESCRIPTION_INDEX = 1;
        final int PROJECTION_EVENT_LOCATION_INDEX = 2;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 3;


        cursor = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;
        //String selection = "((" + CalendarContract.Events.ACCOUNT_NAME + " = ?) AND ("
               // + CalendarContract.Events.ACCOUNT_TYPE + " = ?) AND ("
                //+ CalendarContract.Events.OWNER_ACCOUNT + " = ?))";


        cursor = cr.query(uri, EVENT_PROJECTION, null, null, null);
        while (cursor.moveToNext()){
            if (cursor!=null){

                String titleValue = cursor.getString(PROJECTION_TITLE_INDEX);
                String descriptionValue = cursor.getString(PROJECTION_DESCRIPTION_INDEX);
                String locationValue = cursor.getString(PROJECTION_EVENT_LOCATION_INDEX);
                String nameValue = cursor.getString(PROJECTION_ACCOUNT_NAME_INDEX);

                Event event = new Event(titleValue, descriptionValue,null,null);

                 events = new ArrayList<>();
                 events.add(event);
                // events.add(descriptionValue);

                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                adapter = new Adapter(this,events);
                recyclerView.setAdapter(adapter);

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



