package com.lupinesoft.gearyard;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    TextView textSignUp;
    Button buttonSignIn;
    EditText etEmail, etPassword;
    final String urls = "http://100.72.26.84/GYadminPanel/appDB/login/login_info.php";
    String UID;
    private SharedPrefConfig prefConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        textSignUp = findViewById(R.id.login_textview_signup_id);
        buttonSignIn = findViewById(R.id.login_button_signin_id);
        etEmail = findViewById(R.id.login_email_id);
        etPassword = findViewById(R.id.login_password_id);

        prefConfig = new SharedPrefConfig(getApplicationContext());
        if(prefConfig.readLoginStatus()){
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = etEmail.getText().toString();
                String userPassword = etPassword.getText().toString();
                new userLogin().execute(userEmail, userPassword);
            }
        });
    }

    public class userLogin extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String userEmail = strings[0];
            String userPassword = strings[1];

            OkHttpClient okHttpClient= new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("Email", userEmail)
                    .add("Password", userPassword)
                    .build();
            Request request = new Request.Builder()
                    .url(urls)
                    .post(formBody)
                    .build();
            Response response=null;
            try{
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()){
                    String result= response.body().string();
                    JSONObject obj = new JSONObject(result);
                    JSONObject employee = obj.getJSONObject("data");

                    UID = employee.getString("UID");

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid", UID);
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(LoginActivity.this,
                            R.anim.enter_right_to_left,R.anim.exit_right_to_left);
                    editor.commit();
                    prefConfig.writeLoginStatus(true);
                    startActivity(intent, options.toBundle());
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
