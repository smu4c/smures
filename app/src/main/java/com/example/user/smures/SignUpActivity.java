package com.example.user.smures;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;



public class SignUpActivity extends AppCompatActivity {

    private static final int GALLERY = 1;
    private static final int CAMERA = 2;
    private Bitmap FixBitmap;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] byteArray;
    private String ConvertImage;



    private Spinner big_spinner;
    private Spinner mini_spinner;
    private EditText id_etxt;
    private EditText pw_etxt;
    private EditText name_etxt;
    private EditText phone1_etxt;
    private EditText phone2_etxt;
    private EditText phone3_etxt;
    private Button checkId_btn;
    private Button checkCard_btn;
    private Button signUp_btn;
    private TextView imageCheck_tv;

    private final String CHECK_ID= "checkId";
    private final String CHECK_IMAGE = "checkImage";
    private final String SIGN_UP = "singUp";
    private String whoIs;
    private String department = "";

    private boolean boolResult;
    private final String TAG ="SignUpActivity";


    ArrayAdapter<String> spinnerArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        byteArrayOutputStream = new ByteArrayOutputStream();

        setEditText();
    }

    private void setEditText() {

        big_spinner = (Spinner)findViewById(R.id.signUpBigDepart);
        mini_spinner = (Spinner)findViewById(R.id.signUpDepart);
        id_etxt = (EditText)findViewById(R.id.signUpId);
        pw_etxt = (EditText)findViewById(R.id.signUpPw);
        name_etxt = (EditText)findViewById(R.id.signUpName);
        phone1_etxt = (EditText)findViewById(R.id.signUpTel_1);
        phone2_etxt = (EditText)findViewById(R.id.signUpTel_2);
        phone3_etxt = (EditText)findViewById(R.id.signUpTel_3);
        checkId_btn = (Button)findViewById(R.id.isIdCheck);
        checkCard_btn = (Button)findViewById(R.id.checkCard_photo);
        signUp_btn = (Button)findViewById(R.id.signUpBtn);
        imageCheck_tv = (TextView)findViewById(R.id.stu_image);






        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int sizeOfPhoneNum = phone1_etxt.getText().length()+phone2_etxt.getText().length()+phone3_etxt.getText().length();
                String phone = phone1_etxt.getText().toString()+'-'+phone2_etxt.getText().toString()+'-'+phone3_etxt.getText().toString();
                if (id_etxt.isEnabled()) {
                    Toast.makeText(SignUpActivity.this, "아이디 확인을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(imageCheck_tv.getText().toString().equals("인증을 해주세요")) {
                    Toast.makeText(SignUpActivity.this, "이미지 인증을 해주세요", Toast.LENGTH_SHORT).show();
                } else if(pw_etxt.getText().toString().equals("") || name_etxt.getText().toString().equals("") || sizeOfPhoneNum < 10 || department.equals("")) {
                    Toast.makeText(SignUpActivity.this, "빈칸을 전부 채워주세요", Toast.LENGTH_SHORT).show();
                } else {
                    CheckId task = new CheckId();
                    task.execute(SIGN_UP,UserInfo.getUrl()+"insertData.php",id_etxt.getText().toString(),pw_etxt.getText().toString(), name_etxt.getText().toString(), phone, department);
                }
            }
        });


        checkId_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = id_etxt.getText().toString();
                if(!id.equals("")) {
                    CheckId task = new CheckId();
                    task.execute(CHECK_ID,UserInfo.getUrl()+"IDcheck.php",id);
                } else {
                    Toast.makeText(SignUpActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        checkCard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id_etxt.isEnabled()) {
                    Toast.makeText(SignUpActivity.this, "아이디 확인을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    showPictureDialog();
                }

            }
        });



        big_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item[] = new String[]{};
                switch (position) {
                    case 0: item = getResources().getStringArray(R.array.Global_Humanities); break;
                    case 1: item = getResources().getStringArray(R.array.Design); break;
                    case 2: item = getResources().getStringArray(R.array.Art); break;
                    case 3: item = getResources().getStringArray(R.array.Convergence_Technology); break;
                    case 4: item = getResources().getStringArray(R.array.Technology_department); break;
                }

                spinnerArrayAdapter = new ArrayAdapter<String>(parent.getContext(), android.R.layout.simple_spinner_item,item );
                mini_spinner.setAdapter(spinnerArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            return;
            }
        });
        mini_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void showPictureDialog(){
        if (ContextCompat.checkSelfPermission(SignUpActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        5);
            }
        }
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("갤러리나 카메라를 선택해주세요.");
        String[] pictureDialogItems = {
                "갤러리",
                "카메라" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {//갤러리 선택시 FixBitmap에 선택한 사진 저장
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {///카메라 선택시 바로 찍은 사진 FixBitmap에 저장
            FixBitmap = (Bitmap) data.getExtras().get("data");
        }

        UploadImageToServer(id_etxt.getText().toString());
    }



    public void UploadImageToServer(final String id){ //이미지 업로드
        FixBitmap.compress(Bitmap.CompressFormat.PNG, 40, byteArrayOutputStream);
        byteArray = byteArrayOutputStream.toByteArray();
        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        try {
            String urlEncode = URLEncoder.encode(ConvertImage,"UTF-8");
            CheckId task = new CheckId();
            task.execute(CHECK_IMAGE,UserInfo.getUrl()+"UploadImage.php",id,urlEncode);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG,"Error URL ENCODE = "+e);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {  ////카메라 허가 거부시
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }
            else {
                Toast.makeText(SignUpActivity.this, "카메라를 사용할 수 없습니다!. 카메라 권한을 허용하십시오.", Toast.LENGTH_LONG).show();

            }
        }
    }




    class CheckId extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignUpActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);

            if(whoIs.equals(CHECK_ID)) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolResult = jsonObject.getBoolean("success");
                    if(boolResult) id_etxt.setEnabled(false);
                    Toast.makeText(SignUpActivity.this, "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();

                } catch(Exception e) {
                    Log.e(TAG,"Error Reason : "+ e);
                }
            } else if(whoIs.equals(CHECK_IMAGE)) {
                    imageCheck_tv.setText("학생증 인증 성공");
                    imageCheck_tv.setTextColor(Color.parseColor("#FF00C800"));
            } else if(whoIs.equals(SIGN_UP)) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_slide_out, R.anim.activity_slide_in);
                finish();
            }

        }


        @Override
        protected String doInBackground(String... params) {



            String id = (String)params[2];
            String serverURL = (String)params[1];
            whoIs = (String)params[0];

            String postParameters ="";
            if(whoIs.equals(CHECK_ID)) {
                postParameters = "ID=" + id;
            } else if(whoIs.equals(CHECK_IMAGE)) {

                String bitmap = (String)params[3];
                postParameters = "image_data=" + bitmap + "&image_tag=" + id;
                Log.e(TAG,"data = "+postParameters);
            } else if (whoIs.equals(SIGN_UP)) {

                String pw = (String)params[3];
                String name = (String)params[4];
                String phone = (String)params[5];
                String department = (String)params[6];

                postParameters = "ID=" + id + "&비밀번호=" + pw + "&이름=" + name+ "&전화번호=" + phone+ "&학과=" + department;
            }

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
