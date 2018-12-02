package com.example.user.smures;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReservationActivity extends AppCompatActivity {

    private final String TAG ="ReservationActivity";

    private EditText id_edt;
    private EditText facility_edt;
    private EditText name_edt;
    private EditText department_edt;
    private TextView date_tv;
    private TextView startTime_tv, resEndTime_tv;
    private TextView endTime_tv;
    private EditText groupName_edt;
    private EditText peopleCount_edt;
    private EditText cooperationContents_edt;

    private Button selectData_btn;
    private Button selectStartTime_btn;
    private Button selectEndTime_btn;
    private Button submit_btn;

    private SimpleDateFormat dateFormat, dateFormatYear, dateFormatMon, dateFormatDay;
    private String year, month, day;
    private int selectYear, selectMonth, selectDay;
    private int selectStHour, selectStMin, selectEdHour, selectEdMin;
    private int stHour, stMin, endHour, endMin;
    private Calendar pickedDate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        pickedDate = Calendar.getInstance();
        setLayout();
    }

    private void setLayout() {
        id_edt = (EditText)findViewById(R.id.resIdEdt);
        facility_edt = (EditText)findViewById(R.id.resFacilityEdt);
        name_edt = (EditText)findViewById(R.id.resNameEdt);
        department_edt = (EditText)findViewById(R.id.resDepartmentEdt);
        date_tv = (TextView) findViewById(R.id.resSelectDateTV);
        startTime_tv = (TextView) findViewById(R.id.resStartTimeTV);
        resEndTime_tv = (TextView) findViewById(R.id.resEndTimeTV);
        endTime_tv = (TextView) findViewById(R.id.resEndTimeTV);
        groupName_edt = (EditText) findViewById(R.id.resGroupNameEdt);
        peopleCount_edt = (EditText) findViewById(R.id.resPeopleCountEdt);
        cooperationContents_edt = (EditText) findViewById(R.id.resCooperationEdt);

        selectData_btn = (Button) findViewById(R.id.resSelectDateBtn);
        selectStartTime_btn = (Button) findViewById(R.id.resStartTimeBtn);
        selectEndTime_btn = (Button) findViewById(R.id.resEndTimeBtn);
        submit_btn = (Button)findViewById(R.id.resSubmitBtn);

        id_edt.setText(UserInfo.getUid());


        facility_edt.setText(getIntent().getStringExtra("department"));
        name_edt.setText(UserInfo.getName());
        department_edt.setText(UserInfo.getDepartment());

        date_tv.setText(currentDate());

        selectStartTime_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              stHour = pickedDate.get(Calendar.HOUR_OF_DAY);
              stMin = pickedDate.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),
                        AlertDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selectStHour = hourOfDay;
                                selectStMin = minute;
                                startTime_tv.setText(selectStHour+":"+selectStMin);
                            }
                        },stHour,stMin,true);
                timePickerDialog.show();

            }

        });

        selectEndTime_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endHour = pickedDate.get(Calendar.HOUR_OF_DAY);
                endMin = pickedDate.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),
                        AlertDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                selectEdHour = hourOfDay;
                                selectEdMin = minute;
                                resEndTime_tv.setText(selectEdHour+":"+selectEdMin);
                            }
                        },endHour,endMin,true);
                timePickerDialog.show();
            }
        });

        selectData_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickedDate.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day) + 1);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                selectYear = year;
                                selectMonth = month + 1;
                                selectDay = dayOfMonth;
                                date_tv.setText(selectYear+"-"+selectMonth+"-"+selectDay);
                            }
                        },
                        pickedDate.get(Calendar.YEAR),
                        pickedDate.get(Calendar.MONTH),
                        pickedDate.get(Calendar.DATE)

                );

                datePickerDialog.show();
                Log.e(TAG,"selectYear = "+year + "selectMonth = " +selectMonth + "selectDay = "+selectDay);
            }
        });
    }

    private String currentDate() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);

        dateFormatYear = new SimpleDateFormat("yyyy");
        dateFormatMon = new SimpleDateFormat("MM");
        dateFormatDay = new SimpleDateFormat("dd");


        year = dateFormatYear.format(mDate);
        month = dateFormatMon.format(mDate);
        day = dateFormatDay.format(mDate);

        return dateFormat.format(mDate);
    }

}
