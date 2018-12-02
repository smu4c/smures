package com.example.user.smures;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class ResIntroActivity extends AppCompatActivity {

    String depart;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_intro);
        depart = getIntent().getStringExtra("department");
        this.context = this;

        final CheckBox cb = (CheckBox)findViewById(R.id.checkBox);

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb.isChecked() == true) {
                    Intent intent = new Intent(context, ReservationActivity.class);
                    intent.putExtra("department",depart);
                    startActivity(intent);
                    finish();
                }

            } // end onClick
        }); // end setOnClickListener

    } // end onCreate()
}