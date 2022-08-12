package com.lupinesoft.gearyard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewAddressFragment extends Fragment {
    ImageView backButton;
    Fragment fragment;
    EditText etFullName, etMobile, etState, etCity, etRoad;
    Button submitButton;
    String sUID = DashboardActivity.strUID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_address, container, false);
        backButton = v.findViewById(R.id.newAddress_backButton_id);
        etFullName = v.findViewById(R.id.address_Fname_ID);
        etMobile = v.findViewById(R.id.address_Mobile_ID);
        etState = v.findViewById(R.id.address_State_ID);
        etCity = v.findViewById(R.id.address_City_ID);
        etRoad = v.findViewById(R.id.address_Road_ID);
        submitButton = v.findViewById(R.id.address_Submit_ID);

        backButton.setOnClickListener(new View.OnClickListener() {
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new InsertNewAddress().execute("http://100.72.26.84/GYadminPanel/appDB/address/address_insert.php");
                    }
                }).start();
            }
        });
        return v;
    }

    private class InsertNewAddress extends AsyncTask<String, Void, String> {
        String strUID = sUID;
        String strFName = etFullName.getText().toString();
        String strMobile = etMobile.getText().toString();
        String strState = etState.getText().toString();
        String strCity = etCity.getText().toString();
        String strRoad = etRoad.getText().toString();
        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("UID", strUID)
                    .add("FullName", strFName)
                    .add("Mobile", strMobile)
                    .add("State", strState)
                    .add("City", strCity)
                    .add("Road", strRoad)
                    .build();
            Request request = new Request.Builder().url(strings[0])
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    fragment = new ShippingFragment();
                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right,
                                    R.anim.enter_right_to_left, R.anim.exit_right_to_left)
                            .replace(R.id.fragmentId, fragment)
                            .commit();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
