package com.example.user.smures;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
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
    private TextView startTime_tv;
    private TextView endTime_tv;
    private EditText groupName_edt;
    private EditText peopleCount_edt;
    private EditText whyReservation_edt;
    private EditText cooperationContents_edt;


    private Button selectData_btn;
    private Button selectStartTime_btn;
    private Button selectEndTime_btn;
    private Button submit_btn;

    private SimpleDateFormat dateFormat, dateFormatYear, dateFormatMon, dateFormatDay, dateFormatHour, dataFormatMin;
    private int nYear, nMonth, nDay, nHour, nMin;
    private int selectYear, selectMonth, selectDay;
    private int selectStHour, selectStMin, selectEdHour, selectEdMin;
    private int nowYear, nowMonth, nowDay ,nowHour, nowMinute;
    private Calendar pickedDate;
    private String resDateFormat = "____-__-__";


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
        endTime_tv = (TextView) findViewById(R.id.resEndTimeTV);
        groupName_edt = (EditText) findViewById(R.id.resGroupNameEdt);
        peopleCount_edt = (EditText) findViewById(R.id.resPeopleCountEdt);
        whyReservation_edt = (EditText)findViewById(R.id.resReasonEdt);
        cooperationContents_edt = (EditText) findViewById(R.id.resCooperationEdt);

        selectData_btn = (Button) findViewById(R.id.resSelectDateBtn);
        selectStartTime_btn = (Button) findViewById(R.id.resStartTimeBtn);
        selectEndTime_btn = (Button) findViewById(R.id.resEndTimeBtn);
        submit_btn = (Button)findViewById(R.id.resSubmitBtn);

        id_edt.setText(UserInfo.getUid());


        facility_edt.setText(getIntent().getStringExtra("department"));
        name_edt.setText(UserInfo.getName());
        department_edt.setText(UserInfo.getDepartment());
        date_tv.setText(resDateFormat);
        resDateFormat = currentDate();


        submit_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String resDate = date_tv.getText().toString();
                String resStartTime = startTime_tv.getText().toString();
                String resEndTime = endTime_tv.getText().toString();
                String resCooperationContents = cooperationContents_edt.getText().toString();
                String resPeopleCount = peopleCount_edt.getText().toString();
                String resWhy = whyReservation_edt.getText().toString();
                String resGroupName = groupName_edt.getText().toString();

                 if(resDate.equals("____-__-__") || resStartTime.equals("__ : __") || resEndTime.equals("__ : __") || resCooperationContents.equals("") || resPeopleCount.equals("") || resWhy.equals("") || resGroupName.equals("")) {
                    Toast.makeText(ReservationActivity.this, "값을 전부 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {

                    resStartTime = resDate+" "+resStartTime+":00";
                    resEndTime = resDate+" "+resEndTime+":00";

                    /**
                    SimpleDateFormat sendDataFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");



                    Date dSendStartTime = null;
                    Date dSendEndTime = null;
                    try {
                        dSendStartTime = sendDataFormat.parse(resStartTime);
                        dSendEndTime = sendDataFormat.parse(resEndTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.e(TAG,"캐치문빠짐빠짐빠짐");
                    }

                    resStartTime = sendDataFormat.format(dSendStartTime);
                    resEndTime = sendDataFormat.format(dSendEndTime);

                    **/

                     Log.e(TAG,"Send Data : resData = "+resDate + ", resStartTime = "+resStartTime+", resEndTime = "+resEndTime+ ", resCooperationContents = "+resCooperationContents+", resPeopleCount = "+resPeopleCount+", resWhy = "+resWhy+", resGroupName = " +resGroupName);

                     InsertRes task = new InsertRes();
                    task.execute(UserInfo.getUrl()+"reservation.php",UserInfo.getUid(),facility_edt.getText().toString(),resDate,resStartTime,resEndTime,resPeopleCount,resCooperationContents,resGroupName,resWhy);
                }

            }
        });


        selectStartTime_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowHour = pickedDate.get(Calendar.HOUR_OF_DAY);
                nowMinute = pickedDate.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),
                        AlertDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                if(resDateFormat.equals("____-__-__")) {
                                    startTime_tv.setTextColor(Color.parseColor("#FF0000"));
                                    Toast.makeText(ReservationActivity.this, "날짜를 먼저 입력해주세요.", Toast.LENGTH_SHORT).show();
                                } else {
                                    selectStHour = hourOfDay;
                                    selectStMin = minute;
                                    startTime_tv.setText(selectStHour+":"+selectStMin);
                                    startTime_tv.setTextColor(Color.parseColor("#FF00C800"));
                                    endTime_tv.setText("__ : __");
                                }

                            }
                        },nowHour,nowMinute,true);
                timePickerDialog.show();

            }

        });

        selectEndTime_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowHour = pickedDate.get(Calendar.HOUR_OF_DAY);
                nowMinute = pickedDate.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),
                        AlertDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                if(resDateFormat.equals("____-__-__")) {
                                    endTime_tv.setTextColor(Color.parseColor("#FF0000"));
                                    endTime_tv.setText("__ : __");
                                    Toast.makeText(ReservationActivity.this, "날짜를 먼저 입력해주세요.", Toast.LENGTH_SHORT).show();
                                } else {
                                    if(startTime_tv.getText().toString().equals("__ : __")) {
                                        endTime_tv.setTextColor(Color.parseColor("#FF0000"));
                                        endTime_tv.setText("__ : __");
                                        Toast.makeText(ReservationActivity.this, "시작시간을 먼저 입력해주세요.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        selectEdHour = hourOfDay;
                                        selectEdMin = minute;
                                        if(selectStHour >= selectEdHour || selectStHour+1 >selectEdHour || ((selectStHour+1==selectEdHour) &&(selectEdMin-selectStMin < 0))) {
                                            endTime_tv.setTextColor(Color.parseColor("#FF0000"));
                                            endTime_tv.setText("__ : __");
                                            Toast.makeText(ReservationActivity.this, "최소 대여 시간은 1시간 입니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            endTime_tv.setText(selectEdHour+":"+selectEdMin);
                                            endTime_tv.setTextColor(Color.parseColor("#FF00C800"));
                                        }


                                    }
                                }

                            }
                        },nowHour,nowMinute,true);
                timePickerDialog.show();
            }
        });

        selectData_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickedDate.set(nYear, nMonth - 1, nDay + 1);
                nowYear =  pickedDate.get(Calendar.YEAR);
                nowMonth = pickedDate.get(Calendar.MONTH);
                nowDay = pickedDate.get(Calendar.DATE);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                selectYear = year;
                                selectMonth = month + 1;
                                selectDay = dayOfMonth;
                                if(selectYear < nYear ) {
                                    Toast.makeText(ReservationActivity.this, "지난 연도 입니다 다시 확인해주세요", Toast.LENGTH_SHORT).show();
                                    resDateFormat = "____-__-__";
                                    date_tv.setText(resDateFormat);
                                    date_tv.setTextColor(Color.parseColor("#FF0000"));
                                } else {
                                    if(selectYear== nYear && selectMonth < nMonth){
                                        Toast.makeText(ReservationActivity.this, "지난 월입니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                                        resDateFormat = "____-__-__";
                                        date_tv.setText(resDateFormat);
                                        date_tv.setTextColor(Color.parseColor("#FF0000"));
                                    } else {
                                        if(selectYear== nYear && selectMonth == nMonth && selectDay < nDay) {
                                            Toast.makeText(ReservationActivity.this, "지난 날입니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                            resDateFormat = "____-__-__";
                                            date_tv.setText(resDateFormat);
                                            date_tv.setTextColor(Color.parseColor("#FF0000"));
                                        } else {
                                            if (selectYear== nYear && selectMonth== nMonth && selectDay == nDay) {
                                                Toast.makeText(ReservationActivity.this, "당일 예약은 불가능합니다. 하루전에 시도해주세요.", Toast.LENGTH_SHORT).show();
                                            } else {

                                                if (selectMonth < 10 && selectDay >= 10) {
                                                    resDateFormat = String.format(selectYear + "-" + 0 + selectMonth + "-" + selectDay);
                                                } else if (selectMonth < 10 && selectDay < 10) {
                                                    resDateFormat = String.format(selectYear + "-" + 0 + selectMonth + "-" + 0 + selectDay);
                                                } else if (selectMonth >= 10 && selectDay < 10) {
                                                    resDateFormat = String.format(selectYear + "-" + selectMonth + "-" + 0 + selectDay);
                                                } else {
                                                    resDateFormat = String.format(selectYear + "-" + selectMonth + "-" + selectDay);
                                                }

                                                date_tv.setText(resDateFormat);
                                                date_tv.setTextColor(Color.parseColor("#FF00C800"));
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        nowYear,
                        nowMonth,
                        nowDay
                );
                datePickerDialog.show();

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
        dateFormatHour = new SimpleDateFormat("hh");
        dataFormatMin = new SimpleDateFormat("mm");


        nYear = Integer.parseInt(dateFormatYear.format(mDate));
        nMonth = Integer.parseInt(dateFormatMon.format(mDate));
        nDay = Integer.parseInt(dateFormatDay.format(mDate));
        nHour = Integer.parseInt(dateFormatHour.format(mDate));
        nMin = Integer.parseInt(dataFormatMin.format(mDate));


        return dateFormat.format(mDate);
    }



    class InsertRes extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ReservationActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String answer = jsonObject.getString("result");

                if(answer.equals("success")) {
                    finish();
                } else {
                    Toast.makeText(ReservationActivity.this, "예약하신 시간에 예약이 존재합니다. 시간을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){

            }

        }


        @Override
        protected String doInBackground(String... params) {


            /**
             *   task.execute(
             *
             *          UserInfo.getUrl()+"reservation.php",
             *          UserInfo.getUid(),
             *          facility_edt.getText().toString(),
             *          resDate
             *          resStartTime,
             *          resEndTime,
             *          resPeopleCount,
             *          resCooperationContents,
             *          resGroupName,
             *          resWhy
             *          );
             *
             * **/
            String id = (String)params[1];
            String serverURL = (String)params[0];

            String facility = (String)params[2];
            String date = (String)params[3];
            String startTime = (String)params[4];
            String endTime = (String)params[5];
            String peopleCount = (String)params[6];
            String cooperContents = (String)params[7];
            String groupName = (String)params[8];
            String why = (String)params[9];


            String postParameters = "학번=" + id + "&시설=" + facility + "&시작시간=" + startTime+
                                    "&종료시간=" + endTime+ "&사용인원=" + peopleCount +
                                    "&협조내용=" + cooperContents + "&단체명=" + groupName+ "&신청사유=" + why;

            Log.e(TAG,"JSON = "+postParameters);

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {

                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }

}
