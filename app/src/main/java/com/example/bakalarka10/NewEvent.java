package com.example.bakalarka10;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewEvent extends AppCompatActivity {

    public static final String TAG = "NewEvent";
    private static QueryHandler asyncQueryHandler;

    private EditText s_date,e_date,title_event,location,description;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private Calendar c;
    private Context ctx = this;
    ArrayList<String> accounts;
    MaterialButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mYear= Calendar.getInstance().get(Calendar.YEAR);
        mMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
        mDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;
        mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ;
        mMinute = Calendar.getInstance().get(Calendar.MINUTE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_new);
        s_date = findViewById(R.id.starting_date);
        e_date = findViewById(R.id.ending_date);
        title_event = findViewById(R.id.title_event);
        location = findViewById(R.id.location);
        button = findViewById(R.id.add_button);

        final long start, end;
        final Calendar beginTime = Calendar.getInstance();
        beginTime.set(mYear,mMonth,mDay,mDay,mMinute);
       // start = beginTime.getTimeInMillis();
       // end = beginTime.getTimeInMillis();

        String title = "CAUKO";
        long s = System.currentTimeMillis();
        long e = System.currentTimeMillis()+1;
        QueryHandler.insertEvent(ctx, s,
                e, title);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: title: " + title_event.getText().toString());




            }
        });

        getAccounts();

        //ArrayList<String> account = getAccounts();

        //AutoCompleteTextView select_account = findViewById(R.id.account);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getAccounts());
        //select_account.setAdapter(adapter);


        s_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDatepicker(s_date);
            }
        });

        e_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatepicker(e_date);
            }
        });
    }

    private void showDatepicker(final EditText editText){
        c = Calendar.getInstance();
        int mYearParam = mYear;
        int mMonthParam = mMonth-1;
        int mDayParam = mDay;

        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mMonth = monthOfYear + 1;
                        mYear=year;
                        mDay=dayOfMonth;


                    }
                }, mYearParam, mMonthParam, mDayParam);
        TimePickerDialog timePickerDialog = new TimePickerDialog(ctx,
                new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int pHour,
                                          int pMinute) {
                        //c = Calendar.getInstance();
                        mHour = pHour;
                        mMinute = pMinute;

                        editText.setText(mYear + "-" + mMonth + "-" + mDay + "-" + mHour + ":" + mMinute);
                    }
                }, mHour, mMinute, true);

        timePickerDialog.show();

        datePickerDialog.show();
    }

    private ArrayList<String> getAccounts() {
        final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE};
        Cursor cursor;
        ContentResolver contentResolver = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        cursor = contentResolver.query(uri,EVENT_PROJECTION,null,null,null );
        if (cursor.getCount() > 0) {

            ArrayList<String> accounts = new ArrayList<String>();

            while (cursor.moveToNext()) {
                if (cursor != null) {

                    accounts.add(cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME)));
                    System.out.println(accounts);

                    AutoCompleteTextView select_account = findViewById(R.id.account);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accounts);
                    select_account.setAdapter(adapter);
                }
            }
        }
        return accounts;
    }

    private void addEvent() {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(mYear,mMonth,mDay,mDay,mMinute);
        Calendar endTime = Calendar.getInstance();
        endTime.set(mYear,mMonth,mDay,mDay,mMinute+1);

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, "AHOJ");
        values.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

    }

}



