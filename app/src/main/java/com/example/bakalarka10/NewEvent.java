package com.example.bakalarka10;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
    long start = System.currentTimeMillis();
    long end = System.currentTimeMillis()+1000000;
    String id = null;

    private EditText s_date,e_date,title_event,location_event,description_event;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private Calendar c;
    private Context ctx = this;
    ArrayList<String> accounts;
    MaterialButton button;
    public String title,description,location;
    EventNew eventNew;


    EventNew setStartInsertValues() {

        Log.d(TAG, "setStartInsertValues: " + title);
        eventNew = new EventNew(EventNew.title, EventNew.description, EventNew.location, start, end, id);

        return eventNew;
    }

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
        location_event = findViewById(R.id.location);
        button = findViewById(R.id.add_button);
        description_event = findViewById(R.id.description);


        final long start, end;
        final Calendar beginTime = Calendar.getInstance();
        beginTime.set(mYear,mMonth,mDay,mDay,mMinute);
        //start = beginTime.getTimeInMillis();
        //end = beginTime.getTimeInMillis()+1000000;

        final long s = System.currentTimeMillis();
        final long e = System.currentTimeMillis()+1;


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: title: " + title_event.getText().toString());

                 EventNew.title = title_event.getText().toString();
                 EventNew.location = location_event.getText().toString();
                EventNew.description = description_event.getText().toString();

                QueryHandler.insertEvent(ctx);

            }

        });

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

        // getAccounts();
        //ArrayList<String> account = getAccounts();
        //AutoCompleteTextView select_account = findViewById(R.id.account);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getAccounts());
        //select_account.setAdapter(adapter);

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



