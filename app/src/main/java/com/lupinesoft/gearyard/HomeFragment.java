package com.lupinesoft.gearyard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    TextView textCateViewAll, tvTrendBtn;
    Fragment fragment;
    CircleImageView myPhoto;
    TextView tvName;
    ImageView notifyIcon, btnTv, btnLaptop;
    EditText etSearch;
    String strName = ProfileFragment.Name;
    String picURL = ProfileFragment.dndLink;

    RecyclerView recyclerView, trendRecy, newRecy, popRecy;
    RecyclerView.Adapter adapter, adapterT, adapterN, adapterP;
    String json_string, json_stringT, json_stringN, json_stringP;
    JSONObject jsonObject, jsonObjectT, jsonObjectN, jsonObjectP;
    JSONArray jsonArray, jsonArrayT, jsonArrayN, jsonArrayP;
    List<JfuItem> listItems;
    List<TrendItem> listItemsT;
    List<NewItem> listItemsN;
    List<PopItem> listItemsP;
    String strSearchType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home, container, false);
        textCateViewAll = v.findViewById(R.id.home_cate_viewall_id);
        myPhoto = v.findViewById(R.id.home_photo_id);
        tvName = v.findViewById(R.id.home_name_id);
        notifyIcon = v.findViewById(R.id.home_notify_icon_id);
        etSearch = v.findViewById(R.id.home_search_id);
        btnTv = v.findViewById(R.id.home_btn_tv_id);
        btnLaptop = v.findViewById(R.id.home_btn_laptop_id);
        tvTrendBtn = v.findViewById(R.id.home_tv_btn_trend_id);

        Picasso.with(getContext()).load(picURL).into(myPhoto);
        tvName.setText(strName);

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    strSearchType = etSearch.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("search",strSearchType);
                    fragment = new SrcResultFragment();
                    fragment.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragmentId, fragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });

        notifyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new NotificationFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        textCateViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new CategoryViewAllFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        tvTrendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new TrendingFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        btnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new tvFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        btnLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new laptopFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        recyclerView = v.findViewById(R.id.jfu_recyclerview_ID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        trendRecy = v.findViewById(R.id.tdng_recyclerview_ID);
        trendRecy.setHasFixedSize(true);
        trendRecy.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        newRecy = v.findViewById(R.id.new_recyclerview_ID);
        newRecy.setHasFixedSize(true);
        newRecy.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        popRecy = v.findViewById(R.id.pop_recyclerview_ID);
        popRecy.setHasFixedSize(true);
        popRecy.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        listItems = new ArrayList<>();
        listItemsT = new ArrayList<>();
        listItemsN = new ArrayList<>();
        listItemsP = new ArrayList<>();

        //----------------------JFU---------------------
        String urls = "http://100.72.26.84/GYadminPanel/appDB/jfu/get_jfu_data.php";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(urls).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String str = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            json_string=str;
                            if(json_string==null){
                                Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                try {
                                    jsonObject = new JSONObject(json_string);
                                    jsonArray = jsonObject.getJSONArray("data");
                                    for(int i=0; i<jsonArray.length(); i++){
                                        JSONObject jo = jsonArray.getJSONObject(i);
                                        JfuItem item = new JfuItem(
                                                jo.getString("PID"),
                                                jo.getString("Brand"),
                                                jo.getString("Model"),
                                                jo.getString("Price"),
                                                jo.getString("Photo")
                                        );
                                        listItems.add(item);
                                    }
                                    adapter = new JFU_CustomAdapter(getContext(), listItems);
                                    recyclerView.setAdapter(adapter);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });

        //----------------------TREND---------------------
        String urlsTrend = "http://100.72.26.84/GYadminPanel/appDB/trend/get_trend_data.php";
        OkHttpClient clientTrend = new OkHttpClient();
        Request requestTrend = new Request.Builder().url(urlsTrend).build();
        clientTrend.newCall(requestTrend).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String str = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            json_stringT=str;
                            if(json_stringT==null){
                                Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                try {
                                    jsonObjectT = new JSONObject(json_stringT);
                                    jsonArrayT = jsonObjectT.getJSONArray("data");
                                    for(int i=0; i<jsonArrayT.length(); i++){
                                        JSONObject jo = jsonArrayT.getJSONObject(i);
                                        TrendItem item = new TrendItem(
                                                jo.getString("PID"),
                                                jo.getString("Brand"),
                                                jo.getString("Model"),
                                                jo.getString("Price"),
                                                jo.getString("Photo")
                                        );
                                        listItemsT.add(item);
                                    }
                                    adapterT = new Trend_CustomAdapter(getContext(), listItemsT);
                                    trendRecy.setAdapter(adapterT);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });

        //----------------------NEW---------------------
        String urlsN = "http://100.72.26.84/GYadminPanel/appDB/new/get_new_data.php";
        OkHttpClient clientN = new OkHttpClient();
        Request requestN = new Request.Builder().url(urlsN).build();
        clientN.newCall(requestN).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String str = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            json_stringN=str;
                            if(json_stringN==null){
                                Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                try {
                                    jsonObjectN = new JSONObject(json_stringN);
                                    jsonArrayN = jsonObjectN.getJSONArray("data");
                                    for(int i=0; i<jsonArrayN.length(); i++){
                                        JSONObject jo = jsonArrayN.getJSONObject(i);
                                        NewItem item = new NewItem(
                                                jo.getString("PID"),
                                                jo.getString("Brand"),
                                                jo.getString("Model"),
                                                jo.getString("Price"),
                                                jo.getString("Photo")
                                        );
                                        listItemsN.add(item);
                                    }
                                    adapterN = new NEW_CustomAdapter(getContext(), listItemsN);
                                    newRecy.setAdapter(adapterN);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });

        //----------------------POPULAR---------------------
        String urlsP = "http://100.72.26.84/GYadminPanel/appDB/popular/get_popular_data.php";
        OkHttpClient clientP = new OkHttpClient();
        Request requestP = new Request.Builder().url(urlsP).build();
        clientP.newCall(requestP).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String str = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            json_stringP=str;
                            if(json_stringP==null){
                                Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                try {
                                    jsonObjectP = new JSONObject(json_stringP);
                                    jsonArrayP = jsonObjectP.getJSONArray("data");
                                    for(int i=0; i<jsonArrayP.length(); i++){
                                        JSONObject jo = jsonArrayP.getJSONObject(i);
                                        PopItem item = new PopItem(
                                                jo.getString("PID"),
                                                jo.getString("Brand"),
                                                jo.getString("Model"),
                                                jo.getString("Price"),
                                                jo.getString("Photo")
                                        );
                                        listItemsP.add(item);
                                    }
                                    adapterP = new POP_CustomAdapter(getContext(), listItemsP);
                                    popRecy.setAdapter(adapterP);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });

        return v;
    }
}
