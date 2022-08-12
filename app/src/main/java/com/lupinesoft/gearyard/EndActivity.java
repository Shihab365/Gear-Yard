package com.lupinesoft.gearyard;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EndActivity extends AppCompatActivity {
    TextView tvTID;
    String tid;
    String sUID = DashboardActivity.strUID;
    Button btnCont;
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnCont = findViewById(R.id.end_btn_cont_id);

        btnCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvTID = findViewById(R.id.dlv_tv_tracking_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getMyTID();
            }
        }).start();
    }

    public void getMyTID(){
        String urls_GCD = "http://100.72.26.84/GYadminPanel/appDB/get_tid/get_tid_data.php";
        String strUName = sUID;

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("UID", strUName)
                .build();

        Request request = new Request.Builder()
                .url(urls_GCD)
                .post(formBody)
                .build();

        Response response = null;
        try{
            response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                tid = response.body().string();
                tvTID.setText(tid);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
