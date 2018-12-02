package com.example.user.smures;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int month, day, year;
    private String year_s, month_s, day_s, sumDate, dateFormat;
    private String myJSON;
    private ListView myDataListView;
    private Date date;
    private long now;
    private ArrayList<HashMap<String, String>> myDataList;
    private SimpleDateFormat simpleDateFormat;
    private JSONArray myData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        now = System.currentTimeMillis();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        date = new Date(now);
        dateFormat = simpleDateFormat.format(date);
        sumDate = dateFormat+"00:00:00";

        GetDataJSON getDataJSON = new GetDataJSON();
        getDataJSON.execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myDataListView = (ListView) findViewById(R.id.myDataListView);
        myDataList = new ArrayList<>();

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(MainActivity.this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                myDataList.clear();
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out).replace(R.id.content_main, new HomeActivity()).commit();
            myDataList.clear();
        } else if (id == R.id.nav_field) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out).replace(R.id.content_main, new FieldActivity()).commit();
            myDataList.clear();
        } else if (id == R.id.nav_squash) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out).replace(R.id.content_main, new SquashActivity()).commit();
            myDataList.clear();
        } else if (id == R.id.nav_tennis) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out).replace(R.id.content_main, new TennisActivity()).commit();
            myDataList.clear();
        } else if (id == R.id.nav_no) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out).replace(R.id.content_main, new NoActivity()).commit();
            myDataList.clear();
        } else if (id == R.id.nav_setting) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out).replace(R.id.content_main, new SettingActivity()).commit();
            myDataList.clear();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            myData = jsonObj.getJSONArray("response");

            for (int i = 0; i < myData.length(); i++) {
                JSONObject c = myData.getJSONObject(i);

                String facility = c.getString("시설");
                String startTime = c.getString("시작시간");
                String endTime = c.getString("종료시간");

                HashMap<String, String> myDataMap = new HashMap<String, String>();
                myDataMap.put("시설", facility);
                myDataMap.put("시작시간", startTime);
                myDataMap.put("종료시간", endTime);
                myDataList.add(myDataMap);
            }

            final ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this,
                    myDataList, R.layout.list_mydata,
                    new String[]{"시설", "시작시간", "종료시간"},
                    new int[]{R.id.facility, R.id.startTime, R.id.endTime}
            );

            myDataListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class GetDataJSON extends AsyncTask<String, Void, String> {
        String target;
        @Override

        protected void onPreExecute() {
            try {
                target = UserInfo.getUrl()+"GetMyResData.php?id="+UserInfo.getUid()+"&date="+sumDate;
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
