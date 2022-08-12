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
import android.view.WindowManager;
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

public class SrcResultFragment extends Fragment {

    Fragment fragment;
    ImageView backArrowBtn;
    TextView tvYourSearch;

    RecyclerView srcRecyclerView;
    RecyclerView.Adapter adapter;
    JSONObject jsonObject;
    JSONArray jsonArray;
    List<RecyList> searchLists;
    final String urls = "http://100.72.26.84/GYadminPanel/appDB/search/get_search_data.php";

    String strGetType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_src_result, container, false);

        backArrowBtn = v.findViewById(R.id.srcres_back_arrowBtn_id);
        tvYourSearch = v.findViewById(R.id.srcres_tv_yoursearch);

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

        srcRecyclerView = v.findViewById(R.id.srcres_recyclerview_id);
        srcRecyclerView.setHasFixedSize(true);
        srcRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchLists = new ArrayList<>();

        strGetType = getArguments().getString("search");
        new getSearchItem().execute(strGetType);

        tvYourSearch.setText(strGetType);
        return v;
    }

    public class getSearchItem extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String srcResType = strings[0];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("Type", srcResType)
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
                    Log.d("TAG","GET: "+strResponse);

                    jsonObject = new JSONObject(strResponse);
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
                        searchLists.add(item);
                    }
                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new SrcCustomAdapter(getContext(), searchLists);
                            srcRecyclerView.setAdapter(adapter);
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
