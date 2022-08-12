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

public class NotificationFragment extends Fragment {
    ImageView backArrowBtn;
    Fragment fragment;
    RecyclerView notifyRecyclerView;
    RecyclerView.Adapter adapter;
    JSONObject jsonObject;
    JSONArray jsonArray;
    List<NotifyList> notifyLists;
    final String urls = "http://100.72.26.84/GYadminPanel/notifyDB/get_notify/get_notify_data.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_notification, container, false);

        backArrowBtn = v.findViewById(R.id.notify_back_arrowBtn_id);
        notifyRecyclerView = v.findViewById(R.id.notify_recyclerview_id);

        backArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new HomeFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        notifyRecyclerView.setHasFixedSize(true);
        notifyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notifyLists = new ArrayList<>();

        String strNotify = "Notify";
        new getAllNotification().execute(strNotify);
        return v;
    }

    public class getAllNotification extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String strNotify = strings[0];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("Type", strNotify)
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
                        NotifyList item = new NotifyList(
                                jo.getString("Type"),
                                jo.getString("Title"),
                                jo.getString("Description"),
                                jo.getString("Date")
                        );
                        notifyLists.add(item);
                    }
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new NotifyAdapter(getContext(), notifyLists);
                            notifyRecyclerView.setAdapter(adapter);
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
