package com.lupinesoft.gearyard;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {
    TextView textSignIn;
    EditText etName, etEmail, etAddress, etPassword;
    Button btnSiup;
    String strUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        textSignIn = findViewById(R.id.signup_textview_signin_id);
        etName = findViewById(R.id.sup_name_id);
        etEmail = findViewById(R.id.sup_email_id);
        etAddress = findViewById(R.id.sup_address_id);
        etPassword = findViewById(R.id.sup_password_id);
        btnSiup = findViewById(R.id.sup_button_id);

        char[] chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)) + "");
        for (int i = 0; i < 5; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);
        strUID = sb.toString();

        btnSiup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new InsertSignUp().execute("http://100.72.26.84/GYadminPanel/appDB/user_profile/sign_info_insert.php");
                    }
                }).start();
            }
        });

        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }

    private class InsertSignUp extends AsyncTask<String, Void, String> {

        String UID = strUID;
        String strName = etName.getText().toString();
        String strEmail = etEmail.getText().toString();
        String strAddress = etAddress.getText().toString();
        String strPassword = etPassword.getText().toString();
        String strPhoto = "sample/ic_profile.png";

        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient();

            RequestBody body = new FormBody.Builder()
                    .add("UID", UID)
                    .add("Name", strName)
                    .add("Email", strEmail)
                    .add("Address", strAddress)
                    .add("Password", strPassword)
                    .add("Photo", strPhoto)
                    .build();

            Request request = new Request.Builder().url(strings[0])
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
