package com.lupinesoft.gearyard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class laptopFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    List<RecyList> listItems;
    ImageView buttonBackArrow;
    Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_laptop, container, false);

        buttonBackArrow = v.findViewById(R.id.laptop_button_back_id);

        buttonBackArrow.setOnClickListener(new View.OnClickListener() {
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

        recyclerView = v.findViewById(R.id.laptop_recyclerview_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        listItems = new ArrayList<>();

        String urls = "http://100.72.26.84/GYadminPanel/appDB/laptop/get_laptop_data.php";
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
                                        RecyList item = new RecyList(
                                                jo.getString("PID"),
                                                jo.getString("Brand"),
                                                jo.getString("Model"),
                                                jo.getString("Price"),
                                                jo.getString("Photo")
                                        );
                                        listItems.add(item);
                                    }
                                    adapter = new laptopCustomAdapter(getContext(), listItems);
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

        return v;
    }
}
