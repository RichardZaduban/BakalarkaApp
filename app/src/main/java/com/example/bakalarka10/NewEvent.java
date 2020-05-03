package com.example.bakalarka10;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class NewEvent extends AppCompatActivity {

    public static final String TAG = "NewEvent";
    String id = null;

    private EditText s_date,e_date,title_event,location_event,description_event;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int sYear,sMonth,sDay,sHour,sMinute;
    private Calendar c;
    private Context ctx = this;
   // ArrayList<String> accounts;
    MaterialButton button;
    public String title,description,location;
    EventNew eventNew;


    EventNew setStartInsertValues() {

        eventNew = new EventNew(EventNew.title, EventNew.description, EventNew.location, EventNew.start, EventNew.end, id);

        return eventNew;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mYear= Calendar.getInstance().get(Calendar.YEAR);
        mMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
        mDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;
        mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ;
        mMinute = Calendar.getInstance().get(Calendar.MINUTE);

        sYear= Calendar.getInstance().get(Calendar.YEAR);
        sMonth=Calendar.getInstance().get(Calendar.MONTH)+1;
        sDay=Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;
        sHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ;
        sMinute = Calendar.getInstance().get(Calendar.MINUTE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_new);
        s_date = findViewById(R.id.starting_date);
        e_date = findViewById(R.id.ending_date);
        title_event = findViewById(R.id.title_event);
        location_event = findViewById(R.id.location);
        button = findViewById(R.id.add_button);
        description_event = findViewById(R.id.description);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: title: " + title_event.getText().toString());

                 EventNew.title = title_event.getText().toString();
                 EventNew.location = location_event.getText().toString();
                EventNew.description = description_event.getText().toString();
                final Calendar startingTime = Calendar.getInstance();
                startingTime.set(sYear,sMonth,sDay,sHour,sMinute);
                EventNew.start = startingTime.getTimeInMillis();
                Log.d(TAG, "onClick: "+startingTime.getTimeInMillis());
                final Calendar endingTime = Calendar.getInstance();
                endingTime.set(mYear,mMonth,mDay,mHour,mMinute);
                EventNew.end = endingTime.getTimeInMillis();
                Log.d(TAG, "onClick: "+endingTime.getTimeInMillis());

                QueryHandler.insertEvent(ctx);
                Intent intent = new Intent(NewEvent.this, MainActivity.class);
                startActivity(intent);

            }

        });

        s_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDatepickerStart(s_date);
            }
        });

        e_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatepickerEnd(e_date);
            }
        });

        // getAccounts();
        //ArrayList<String> account = getAccounts();
        //AutoCompleteTextView select_account = findViewById(R.id.account);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getAccounts());
        //select_account.setAdapter(adapter);

    }

    private void showDatepickerStart(final EditText editText){
        c = Calendar.getInstance();
        int mYearParam = sYear;
        int mMonthParam = sMonth-1;
        int mDayParam = sDay;

        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx,
                new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        sMonth = monthOfYear;
                        sYear=year;
                        sDay=dayOfMonth;


                    }
                }, mYearParam, mMonthParam, mDayParam);

        TimePickerDialog timePickerDialog = new TimePickerDialog(ctx,
                new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int pHour,
                                          int pMinute) {
                        //c = Calendar.getInstance();
                        sHour = pHour;
                        sMinute = pMinute;

                        editText.setText(sYear + "-" + sMonth + "-" + sDay + "-" + sHour + ":" + sMinute);
                    }
                }, sHour, sMinute, true);

        timePickerDialog.show();

        datePickerDialog.show();
    }

    private void showDatepickerEnd(final EditText editText){
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
                        mMonth = monthOfYear;
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

    // tato metoda sa da pouzit pri vbrani kalendara do ktoreho vkladat event
    /*ArrayList<String> getAccounts() {
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
    }*/


}



