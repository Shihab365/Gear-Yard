package com.lupinesoft.gearyard;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization;
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel;
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType;
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType;
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz;
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener;

import java.io.IOException;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MobileBankingActivity extends AppCompatActivity {
    String total, mycart, mycad, transID, trackID;
    String sUID = DashboardActivity.strUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_banking);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        total = getIntent().getStringExtra("total");
        mycart = getIntent().getStringExtra("mycart");
        mycad = getIntent().getStringExtra("mycad");

        int fnlAmount = Integer.parseInt(total);

        char[] chars = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)) + "-");
        for (int i = 0; i < 5; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);
        transID = sb.toString();

        char[] charsT = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789".toCharArray();
        Random rndT = new Random();
        StringBuilder sbT = new StringBuilder((100000 + rndT.nextInt(900000)) + "");
        for (int i = 0; i < 5; i++)
            sbT.append(charsT[rndT.nextInt(charsT.length)]);
        trackID = sbT.toString();

        final SSLCommerzInitialization sslCommerzInitialization = new SSLCommerzInitialization
                ("lupin5ef9e6736b011","lupin5ef9e6736b011@ssl", fnlAmount, SSLCCurrencyType.BDT, transID,
                        "productType", SSLCSdkType.TESTBOX);

        IntegrateSSLCommerz
                .getInstance(MobileBankingActivity.this)
                .addSSLCommerzInitialization(sslCommerzInitialization)
                .buildApiCall(new SSLCTransactionResponseListener() {
                    @Override
                    public void transactionSuccess(SSLCTransactionInfoModel sslcTransactionInfoModel) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                submitUserHdy();
                            }
                        }).start();

                        Intent intent = new Intent(MobileBankingActivity.this, EndActivity.class);
                        ActivityOptions options = ActivityOptions.makeCustomAnimation(MobileBankingActivity.this,
                                R.anim.enter_right_to_left,R.anim.exit_right_to_left);
                        startActivity(intent, options.toBundle());
                    }

                    @Override
                    public void transactionFail(String s) {

                    }

                    @Override
                    public void merchantValidationError(String s) {

                    }
                });
    }
    public void submitUserHdy() {
        String urlsHDY = "http://100.72.26.84/GYadminPanel/appDB/user_hdy/user_hdy_insert.php";
        String UID = sUID;
        String insTotal = total;
        String insMycart = mycart;
        String insMycad = mycad;
        String insMytid = transID;
        String insMytracking = trackID;

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("UID", UID)
                .add("Total", insTotal)
                .add("Cart", insMycart)
                .add("Cad", insMycad)
                .add("TID", insMytid)
                .add("Tracking", insMytracking)
                .build();

        Request request = new Request.Builder().url(urlsHDY)
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                Log.d("TAG", ""+response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
