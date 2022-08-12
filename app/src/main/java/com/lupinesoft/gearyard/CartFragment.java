package com.lupinesoft.gearyard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CartFragment extends Fragment {
    ImageView buttonArrow;
    Button cartProceed;
    static TextView tvTotalCost;
    Fragment fragment;
    RecyclerView cartRecyclerView;
    RecyclerView.Adapter adapter;
    JSONObject jsonObject;
    JSONArray jsonArray;
    List<CartList> cartLists;
    final String urls = "http://100.72.26.84/GYadminPanel/appDB/get_cart/get_cart_data.php";
    int totalPay = 0;
    String sUID = DashboardActivity.strUID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        buttonArrow = v.findViewById(R.id.cart_button_back_id);
        tvTotalCost = v.findViewById(R.id.tv_cart_totalcost_ID);
        cartProceed = v.findViewById(R.id.cart_proceedButton_ID);

        buttonArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new CategoryViewAllFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right,
                                R.anim.enter_right_to_left, R.anim.exit_right_to_left)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        cartProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("toc", tvTotalCost.getText().toString());
                fragment = new ShippingFragment();
                fragment.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations( R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_right_to_left)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        cartRecyclerView = v.findViewById(R.id.cart_recyclerview_id);
        cartRecyclerView.setHasFixedSize(true);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartLists = new ArrayList<>();

        String strUName = sUID;
        new getPersonalCart().execute(strUName);
        return v;
    }

    public class getPersonalCart extends AsyncTask<String, Void, String> {

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
                        CartList item = new CartList(
                                jo.getString("UID"),
                                jo.getString("PID"),
                                jo.getString("Brand"),
                                jo.getString("Model"),
                                jo.getString("Quantity"),
                                jo.getString("Total"),
                                jo.getString("PhotoUrl")
                        );
                        cartLists.add(item);
                        int x = Integer.parseInt(item.getTotal());
                        totalPay += x;
                    }
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new cartCustomAdapter(getContext(), cartLists);
                            cartRecyclerView.setAdapter(adapter);
                            tvTotalCost.setText(String.valueOf(totalPay));
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
