package com.lupinesoft.gearyard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DashboardActivity extends AppCompatActivity {
    BubbleNavigationLinearView bubbleNavigationLinearView;
    TextView tvName;
    CircleImageView ivProPic;
    public static String strUID;
    private SharedPrefConfig sharedPrefConfig;
    final String urls = "http://100.72.26.84/GYadminPanel/appDB/dash_info/get_dash_data.php";
    public static String Name, Photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        tvName = findViewById(R.id.home_name_id);
        ivProPic = findViewById(R.id.home_photo_id);

        sharedPrefConfig = new SharedPrefConfig(getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", Context.MODE_PRIVATE);

        strUID = sharedPreferences.getString("uid", "Data not found");

        String yourUID = strUID;
        new userDash().execute(yourUID);

        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                Fragment fragment;
                if(position==0){
                    fragment = new HomeFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragmentId, fragment)
                            .commit();
                }
                if(position==1){
                    fragment = new CartFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragmentId, fragment)
                            .commit();
                }
                if(position == 2){
                    fragment = new FavouriteFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragmentId, fragment)
                            .commit();
                }
                if(position==3){
                    Bundle bundle = new Bundle();
                    bundle.putString("uid",strUID);
                    fragment = new ProfileFragment();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragmentId, fragment)
                            .commit();
                }
            }
        });
    }

    public class userDash extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String yourUID = strings[0];
            OkHttpClient okHttpClient= new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("UID", yourUID)
                    .build();
            Request request = new Request.Builder()
                    .url(urls)
                    .post(formBody)
                    .build();
            Response response=null;
            try{
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()){
                    String result= response.body().string();
                    JSONObject obj = new JSONObject(result);
                    JSONObject client = obj.getJSONObject("data");

                    Name = client.getString("Name");
                    Photo = client.getString("Photo");

                    final String dndLink = "http://100.72.26.84/GYadminPanel/appDB/pro_update/"+Photo;

                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvName.setText(Name);
                            Picasso.with(DashboardActivity.this).load(dndLink).into(ivProPic);
                        }
                    });
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
