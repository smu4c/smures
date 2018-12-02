package com.example.user.smures;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class TennisActivity extends Fragment {
    private View v;
    private int month, day, year;
    private String year_s, month_s, day_s, sumDate;
    private String myJSON;
    private ListView tennisListView;
    private ArrayList<HashMap<String, String>> tennisDataList;

    private JSONArray myData = null;

    private Button res;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_tennis, container, false);

        res = (Button) v.findViewById(R.id.tennis_res);

        tennisListView = (ListView) v.findViewById(R.id.tennisListView);
        tennisDataList = new ArrayList<>();

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResIntroActivity.class);
                intent.putExtra("department","테니스장");
                startActivity(intent);
            }
        });

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(v, R.id.calendarTennis)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                tennisDataList.clear();
                month = date.get(Calendar.MONTH)+1;
                day = date.get(Calendar.DAY_OF_MONTH);
                year = date.get(Calendar.YEAR);

                year_s = String.valueOf(year);
                month_s = String.valueOf(month);

                if(day<10) {
                    day_s = "0"+String.valueOf(day);
                }
                else {
                    day_s = String.valueOf(day);
                }
                sumDate = year_s+"-"+month_s+"-"+day_s+" "+"00:00:00";

                GetDataJSON getDataJSON = new GetDataJSON();
                getDataJSON.execute();
            }
        });
        return v;
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            myData = jsonObj.getJSONArray("response");

            for (int i = 0; i < myData.length(); i++) {
                JSONObject c = myData.getJSONObject(i);

                String stdCode = c.getString("단체명");
                String startTime = c.getString("시작시간");
                String endTime = c.getString("종료시간");

                HashMap<String, String> tennisDataMap = new HashMap<String, String>();
                tennisDataMap.put("단체명", stdCode);
                tennisDataMap.put("시작시간", startTime);
                tennisDataMap.put("종료시간", endTime);
                tennisDataList.add(tennisDataMap);
            }

            final ListAdapter adapter = new SimpleAdapter(
                    getActivity(),
                    tennisDataList, R.layout.list_mydata,
                    new String[]{"단체명", "시작시간", "종료시간"},
                    new int[]{R.id.facility, R.id.startTime, R.id.endTime}
            );

            tennisListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class GetDataJSON extends AsyncTask<String, Void, String> {
        String target;
        @Override

        protected void onPreExecute() {
            try {
                target = UserInfo.getUrl()+"GetAllResData.php?kind=테니스장"+"&date="+sumDate;;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        protected String doInBackground(String... voids) {
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(target);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                InputStream inputStream = con.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                con.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            myJSON = result;
            showList();
        }
    }
}
