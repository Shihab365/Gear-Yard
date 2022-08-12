package com.lupinesoft.gearyard;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentFragment extends Fragment {
    Bundle bundle;
    String total, mycart, mycad, delC_UID;
    ImageView backArrow;
    CardView cardMobile, cardCredit;
    Fragment fragment;
    String sUID = DashboardActivity.strUID;
    final String urlsCA = "http://100.72.26.84/GYadminPanel/appDB/cAddress_delete/delete_address_data.php";
    final String urlsUC = "http://100.72.26.84/GYadminPanel/appDB/usercart_delete/delete_address_data.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment, container, false);
        backArrow = v.findViewById(R.id.paymeny_backButton_id);
        cardMobile = v.findViewById(R.id.payment_mobile_id);
        cardCredit = v.findViewById(R.id.payment_card_id);

        bundle = this.getArguments();
        if(bundle!=null){
            total = bundle.getString("total");
            mycart = bundle.getString("mycart");
            mycad = bundle.getString("mycad");
        }

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ShippingFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right,
                                R.anim.enter_right_to_left, R.anim.exit_right_to_left)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        cardMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delC_UID = sUID;
                new deleteCurrentAddress().execute(delC_UID);
                new deleteUserCart().execute(delC_UID);

                Intent intent = new Intent(getContext(), MobileBankingActivity.class);
                intent.putExtra("total", total);
                intent.putExtra("mycart", mycart);
                intent.putExtra("mycad", mycad);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(), R.anim.enter_right_to_left,R.anim.exit_right_to_left);
                startActivity(intent, options.toBundle());
            }
        });

        cardCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                delC_UID = sUID;
                new deleteCurrentAddress().execute(delC_UID);
                new deleteUserCart().execute(delC_UID);

                Intent intent = new Intent(getContext(), CardPaymentActivity.class);
                intent.putExtra("total", total);
                intent.putExtra("mycart", mycart);
                intent.putExtra("mycad", mycad);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getContext(), R.anim.enter_right_to_left,R.anim.exit_right_to_left);
                startActivity(intent, options.toBundle());
            }
        });
        return v;
    }

    public class deleteCurrentAddress extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String delUID = strings[0];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("UID", delUID)
                    .build();

            Request request = new Request.Builder()
                    .url(urlsCA)
                    .post(formBody)
                    .build();
            Response response = null;

            try {
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()){

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class deleteUserCart extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String delUID = strings[0];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("UID", delUID)
                    .build();

            Request request = new Request.Builder()
                    .url(urlsUC)
                    .post(formBody)
                    .build();
            Response response = null;

            try {
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()){

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
