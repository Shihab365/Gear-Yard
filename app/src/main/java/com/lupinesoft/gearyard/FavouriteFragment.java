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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FavouriteFragment extends Fragment {
    RecyclerView favRecyclerView;
    RecyclerView.Adapter adapter;
    JSONObject jsonObject;
    JSONArray jsonArray;
    List<FavList> favLists;
    final String urls = "http://100.72.26.84/GYadminPanel/appDB/get_fav/get_fav_data.php";
    String sUID = DashboardActivity.strUID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favourite, container, false);
        favRecyclerView = v.findViewById(R.id.fav_recyclerview_ID);
        favRecyclerView.setHasFixedSize(true);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        favLists = new ArrayList<>();

        String strUName = sUID;
        new getPersonalFav().execute(strUName);

        return v;
    }

    public class getPersonalFav extends AsyncTask<String, Void, String> {

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
                        FavList item = new FavList(
                                jo.getString("UID"),
                                jo.getString("PID"),
                                jo.getString("Brand"),
                                jo.getString("Model"),
                                jo.getString("PhotoURL")
                        );
                        favLists.add(item);
                    }
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new favCustomAdapter(getContext(), favLists);
                            favRecyclerView.setAdapter(adapter);
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
