package com.lupinesoft.gearyard;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileFragment extends Fragment {
    Button buttonEditAction;
    Fragment fragment;
    final String urls = "http://100.72.26.84/GYadminPanel/appDB/get_profile/get_profile_data.php";
    String UID, Email, Address, Photo;
    TextView tvName, tvUID, tvEmail, tvAddress;
    CircleImageView proCirc;
    public static String dndLink, Name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_profile, container, false);
        buttonEditAction = v.findViewById(R.id.profile_button_editAction_id);
        tvName = v.findViewById(R.id.profile_tv_name_id);
        tvUID = v.findViewById(R.id.profile_tv_uid_id);
        tvEmail = v.findViewById(R.id.profile_tv_email_id);
        tvAddress = v.findViewById(R.id.profile_tv_address_id);
        proCirc = v.findViewById(R.id.profile_iv_user_id);

        final String strUID = getArguments().getString("uid");

        buttonEditAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("uid", UID);
                bundle.putString("name", tvName.getText().toString());
                bundle.putString("email", tvEmail.getText().toString());
                bundle.putString("address", tvAddress.getText().toString());
                bundle.putString("photo", dndLink);
                fragment = new UpdateProfileFragment();
                fragment.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        String yourUID = strUID;
        new userDetails().execute(yourUID);

        return v;
    }

    public class userDetails extends AsyncTask<String, Void, String> {
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

                    UID = client.getString("UID");
                    Name = client.getString("Name");
                    Email = client.getString("Email");
                    Address = client.getString("Address");
                    Photo = client.getString("Photo");

                    dndLink = "http://100.72.26.84/GYadminPanel/appDB/pro_update/"+Photo;

                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.with(getContext()).load(dndLink).into(proCirc);
                            tvName.setText(Name);
                            tvUID.setText(UID);
                            tvEmail.setText(Email);
                            tvAddress.setText(Address);
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
