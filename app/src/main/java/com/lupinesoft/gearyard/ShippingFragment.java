package com.lupinesoft.gearyard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShippingFragment extends Fragment {
    Fragment fragment;
    ImageView backArrow;
    Button addNewButton, contButton;
    RecyclerView addressRecyView;
    RecyclerView.Adapter adapter;
    JSONObject jsonObject;
    JSONArray jsonArray;
    List<AddressList> listItems;
    String urls = "http://100.72.26.84/GYadminPanel/appDB/get_address/get_address_data.php";
    String urls_GCA = "http://100.72.26.84/GYadminPanel/appDB/cAddress_get/get_address_data.php";
    String getTOC, mycart, mycad;
    Bundle bundle;
    String sUID = DashboardActivity.strUID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shipping, container, false);
        backArrow = v.findViewById(R.id.shipping_backButton_id);
        addNewButton = v.findViewById(R.id.shipping_addNewButton_ID);
        addressRecyView = v.findViewById(R.id.shipping_recyclerview_ID);
        contButton = v.findViewById(R.id.shipping_continue_ID);

        bundle = this.getArguments();
        if(bundle!=null){
            getTOC = bundle.getString("toc");
        }

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new NewAddressFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations( R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_right_to_left)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new CartFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right,
                                R.anim.enter_right_to_left, R.anim.exit_right_to_left)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getMyCart();
                        getCurrentAddress();
                        bundle = new Bundle();
                        bundle.putString("total", getTOC);
                        bundle.putString("mycart", mycart);
                        bundle.putString("mycad", mycad);
                        fragment = new PaymentFragment();
                        fragment.setArguments(bundle);
                        getFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations( R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                        R.anim.enter_left_to_right, R.anim.exit_right_to_left)
                                .replace(R.id.fragmentId, fragment)
                                .commit();
                    }
                }).start();
            }

        });

        addressRecyView.setHasFixedSize(true);
        addressRecyView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        listItems = new ArrayList<>();

        String strUName = sUID;
        new getPersonalAddress().execute(strUName);

        return v;
    }

    public void getMyCart(){
        String urls_GCD = "http://100.72.26.84/GYadminPanel/appDB/rcv_order/get_order_data.php";
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
                mycart = response.body().string();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getCurrentAddress() {
        String strUName = sUID;
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("UID", strUName)
                .build();
        Request request = new Request.Builder()
                .url(urls_GCA)
                .post(formBody)
                .build();
        Response response = null;
        try{
            response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                String strResponse = response.body().string();
                jsonObject = new JSONObject(strResponse);
                jsonArray = jsonObject.getJSONArray("data");
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jo = jsonArray.getJSONObject(i);
                    AddressList item = new AddressList(
                            jo.getString("UID"),
                            jo.getString("FullName"),
                            jo.getString("Mobile"),
                            jo.getString("State"),
                            jo.getString("City"),
                            jo.getString("Road")
                    );
                    mycad = "UID: "+item.getUID()+", FullName: "+item.getFullName()+", Mobile: "+item.getMobile()+"," +
                            " State: "+item.getState()+", City: "+item.getCity()+", Road: "+item.getRoad();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class getPersonalAddress extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String strUName = strings[0];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("UID", strUName)
                    .build();

            Request request = new Request.Builder()
                    .url(urls)
                    .post(formBody)
                    .build();
            Response response = null;

            try{
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()){
                    String strResponse = response.body().string();
                    jsonObject = new JSONObject(strResponse);
                    jsonArray = jsonObject.getJSONArray("data");
                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jo = jsonArray.getJSONObject(i);
                        AddressList item = new AddressList(
                                jo.getString("UID"),
                                jo.getString("FullName"),
                                jo.getString("Mobile"),
                                jo.getString("State"),
                                jo.getString("City"),
                                jo.getString("Road")
                        );
                        listItems.add(item);
                    }
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new addressCustomAdapter(getContext(), listItems);
                            addressRecyView.setAdapter(adapter);
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
