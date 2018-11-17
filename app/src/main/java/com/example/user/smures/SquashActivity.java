package com.example.user.smures;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SquashActivity extends Fragment {
    private View v;
    private Button res;
    private long now;
    private Date date;
    private SimpleDateFormat simpleDateFormatYear, simpleDateFormatMon, simpleDateFormatDay;
    private String year, month, day;
    private int selectYear, selectMonth, selectDay;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_squash, container, false);

        res = (Button) v.findViewById(R.id.squash_res);

        now = System.currentTimeMillis();
        date = new Date(now);
        simpleDateFormatYear = new SimpleDateFormat("yyyy");
        simpleDateFormatMon = new SimpleDateFormat("M");
        simpleDateFormatDay = new SimpleDateFormat("d");

        year = simpleDateFormatYear.format(date);
        month = simpleDateFormatMon.format(date);
        day = simpleDateFormatDay.format(date);

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar pickedDate = Calendar.getInstance();
                Calendar minDate = Calendar.getInstance();
                Calendar maxDate = Calendar.getInstance();

                pickedDate.set(Integer.parseInt(day),Integer.parseInt(month)-1,Integer.parseInt(day)+1);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);

                                selectYear = year;
                                selectMonth = month+1;
                                selectDay = dayOfMonth;

                                if(permissionCheck== PackageManager.PERMISSION_DENIED){   // 권한 없음
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},0);
                                } else{   //권한 있음
                                    Intent intent = new Intent(getActivity(), CamActivity.class);
                                    intent.putExtra("year",selectYear);
                                    intent.putExtra("month",selectMonth);
                                    intent.putExtra("day",selectDay);
                                    startActivity(intent);

                                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent,1);*/
                                }
                                Toast.makeText(getActivity(),"select date : "+ selectYear + "-"+selectMonth+"-"+selectDay,Toast.LENGTH_LONG).show();
                            }
                        },
                        pickedDate.get(Calendar.YEAR),
                        pickedDate.get(Calendar.MONTH),
                        pickedDate.get(Calendar.DATE)
                );

                minDate.set(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day)+1);
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());

                maxDate.set(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day)+8);
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

                datePickerDialog.show();
            }
        });
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(getActivity(), CamActivity.class);
            intent.putExtra("year",selectYear);
            intent.putExtra("month",selectMonth);
            intent.putExtra("day",selectDay);
            startActivity(intent);
        } else{
            Toast.makeText(getActivity(),"예약하려면 권한을 승낙하여야 합니다.",Toast.LENGTH_SHORT).show();
        }
    }
}
