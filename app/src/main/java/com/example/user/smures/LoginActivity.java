package com.example.user.smures;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText uid, passwd;
    private Button login;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uid = (EditText) findViewById(R.id.uid);
        passwd = (EditText) findViewById(R.id.passWd);
        login = (Button) findViewById(R.id.logInBtn);

        uid.setFocusableInTouchMode(true);
        uid.requestFocus();

        login.setOnClickListener(new View.OnClickListener() {   //Login Button ClickListener
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
                finish();
                /*if(uid.getText().toString().equals("") || passwd.getText().toString().equals("")) {   //email 과 password 입력 안 했을 경우
                    Toast.makeText(LoginActivity.this, "빈 곳 없이 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!Pattern.matches("^[0-9]+$", uid.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "ID는 학번입니다.", Toast.LENGTH_SHORT).show();
                }
                else if(uid.getText().toString().length()==9) {
                    loginUser(uid.getText().toString(), passwd.getText().toString());   //login 함수 실행
                }
                else {
                    Toast.makeText(LoginActivity.this, "ID OR PASSWORD ERROR", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    private void loginUser(final String uid, final String passwd) {
        Response.Listener<String> responseLister = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");    //json을 통해 넘어 온 값이 success랑 일치하면 true
                    if(success){
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialog = builder.setMessage("로그인에 성공했습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();

                        UserInfo.uid = uid;

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
                        finish();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        dialog = builder.setMessage("ID OR PASSWORD ERROR")
                                .setNegativeButton("다시 시도", null)
                                .create();
                        dialog.show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        LoginRequest loginRequest = new LoginRequest(uid, passwd, responseLister);   //아이디와 패스워드를 서버에 있는 php로 보내기 위한 클래스에 인자를 넘겨준다.
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }

    @Override
    protected void onStop(){
        super.onStop();

        if(dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }
}
