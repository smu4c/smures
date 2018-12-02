package com.example.user.smures;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.UserHandle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity {
    private String stdCode, stdName, stdFac, startTime, endTime, cellNum, depart, reason, groupName, cooper, person, approve;
    private String yymmdd;
    private ListView main2ListView;
    private ArrayList<HashMap<String, String>> main2DataList, main2DataListIntent;
    private JSONArray myData = null;
    private String myJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Log.d("TAG","성공");
        main2ListView = (ListView) findViewById(R.id.resListView);
        main2DataList = new ArrayList<>();
        main2DataListIntent = new ArrayList<>();

        main2ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", ""+main2DataList.get(position));

                Intent intent = new Intent(Main2Activity.this, ApproveActivity.class);
                intent.putExtra("학번",main2DataListIntent.get(position).get("학번"));
                intent.putExtra("이름",main2DataListIntent.get(position).get("이름"));
                intent.putExtra("시설",main2DataListIntent.get(position).get("시설"));
                intent.putExtra("전화번호",main2DataListIntent.get(position).get("전화번호"));
                intent.putExtra("학과",main2DataListIntent.get(position).get("학과"));
                intent.putExtra("신청사유",main2DataListIntent.get(position).get("신청사유"));
                intent.putExtra("단체명",main2DataListIntent.get(position).get("단체명"));
                intent.putExtra("협조내용",main2DataListIntent.get(position).get("협조내용"));
                intent.putExtra("사용인원",main2DataListIntent.get(position).get("사용인원"));
                intent.putExtra("승인현황",main2DataListIntent.get(position).get("승인현황"));
                intent.putExtra("날짜",main2DataListIntent.get(position).get("날짜"));
                intent.putExtra("시작시간",main2DataListIntent.get(position).get("시작시간"));
                intent.putExtra("종료시간",main2DataListIntent.get(position).get("종료시간"));
                startActivity(intent);
                overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
                finish();
            }
        });

        GetDataJSON getDataJSON = new GetDataJSON();
        getDataJSON.execute();
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            myData = jsonObj.getJSONArray("response");

            for (int i = 0; i < myData.length(); i++) {
                JSONObject c = myData.getJSONObject(i);
                Log.d("TAG","성공"+c);
                stdCode = c.getString("학번");
                stdName = c.getString("이름");
                stdFac = c.getString("시설");
                cellNum = c.getString("전화번호");
                depart = c.getString("학과");
                reason = c.getString("신청사유");
                groupName = c.getString("단체명");
                cooper = c.getString("협조내용");
                person = c.getString("사용인원");
                approve = c.getString("승인현황");
                startTime = c.getString("시작시간");
                endTime = c.getString("종료시간");

                yymmdd = startTime.substring(0, 10);

                HashMap<String, String> main2DataMapIntent = new HashMap<String, String>();
                main2DataMapIntent.put("학번", stdCode);
                main2DataMapIntent.put("이름", stdName);
                main2DataMapIntent.put("시설", stdFac);
                main2DataMapIntent.put("날짜", yymmdd);
                main2DataMapIntent.put("전화번호", cellNum);
                main2DataMapIntent.put("학과", depart);
                main2DataMapIntent.put("신청사유", reason);
                main2DataMapIntent.put("단체명", groupName);
                main2DataMapIntent.put("협조내용", cooper);
                main2DataMapIntent.put("사용인원", person);
                main2DataMapIntent.put("승인현황", approve);
                main2DataMapIntent.put("시작시간", startTime);
                main2DataMapIntent.put("종료시간", endTime);
                main2DataListIntent.add(main2DataMapIntent);

                HashMap<String, String> main2DataMap = new HashMap<String, String>();
                main2DataMap.put("학번", stdCode);
                main2DataMap.put("이름", stdName);
                main2DataMap.put("시설", stdFac);
                main2DataMap.put("날짜", yymmdd);
                main2DataList.add(main2DataMap);
            }

            final ListAdapter adapter = new SimpleAdapter(
                    getApplicationContext(),
                    main2DataList, R.layout.list_main2data,
                    new String[]{"학번", "이름", "시설", "날짜"},
                    new int[]{R.id.stdCode, R.id.stdName, R.id.stdFac, R.id.yymmdd}
            );

            main2ListView.setAdapter(adapter);
        } catch (JSONException e) {

            Log.d("TAG","실패");
            e.printStackTrace();
        }
    }

    class GetDataJSON extends AsyncTask<String, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = UserInfo.getUrl()+"_loadData.php?type="+UserInfo.getType()+"&ID="+UserInfo.getUid();
            }
            catch (Exception e) {
                Log.d("TAG","실패 GetDataJson");
                e.printStackTrace();
            }
        }

        protected String doInBackground(String... voids) {
            BufferedReader bufferedReader = null;

            try {
                Log.d("TAG","성공 doInBackground");
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
                Log.d("TAG","실패 doInBackground");
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
