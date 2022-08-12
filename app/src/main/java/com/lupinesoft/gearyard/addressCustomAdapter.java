package com.lupinesoft.gearyard;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class addressCustomAdapter extends RecyclerView.Adapter<addressCustomAdapter.MyViewHolder> {

    Context context;
    List<AddressList> listItems;
    final String urls = "http://100.72.26.84/GYadminPanel/appDB/delete_address/delete_address_data.php";
    final String urlsCAI = "http://100.72.26.84/GYadminPanel/appDB/cAddress_insert/cAddress_insert.php";

    public addressCustomAdapter(Context context, List<AddressList> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recy_address, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        final AddressList addressList = listItems.get(i);
        myViewHolder.tvFName.setText(addressList.getFullName());
        myViewHolder.tvMobile.setText(addressList.getMobile());
        myViewHolder.tvState.setText(addressList.getState()+" ,");
        myViewHolder.tvCity.setText(addressList.getCity()+" ,");
        myViewHolder.tvRoad.setText(addressList.getRoad()+".");

        myViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String insUID = addressList.getUID();
                final String insFullName = addressList.getFullName();
                final String insMobile = addressList.getMobile();
                final String insState = addressList.getState();
                final String insCity = addressList.getCity();
                final String insRoad = addressList.getRoad();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new submitCAddress().execute(insUID, insFullName, insMobile, insState, insCity, insRoad);
                    }
                }).start();
            }
        });

        myViewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String delUID = addressList.getUID();
                String delFullName = addressList.getFullName();
                String delMobile = addressList.getMobile();
                String delState = addressList.getState();
                String delCity = addressList.getCity();
                String delRoad = addressList.getRoad();

                new deletePersonalAddress().execute(delUID, delFullName, delMobile, delState, delCity, delRoad);

                listItems.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, listItems.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView tvFName, tvMobile, tvState, tvCity, tvRoad, tvDelete;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvFName = itemView.findViewById(R.id.ads_fullname_id);
            tvMobile = itemView.findViewById(R.id.ads_mobile_id);
            tvState = itemView.findViewById(R.id.ads_state_id);
            tvCity = itemView.findViewById(R.id.ads_city_id);
            tvRoad = itemView.findViewById(R.id.ads_road_id);
            tvDelete = itemView.findViewById(R.id.ads_delete_id);
            checkBox = itemView.findViewById(R.id.ckbox_ca_id);
        }
    }

    public class deletePersonalAddress extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String delUID = strings[0];
            String delFullName = strings[1];
            String delMobile = strings[2];
            String delState = strings[3];
            String delCity = strings[4];
            String delRoad = strings[5];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("UID", delUID)
                    .add("FullName", delFullName)
                    .add("Mobile", delMobile)
                    .add("State", delState)
                    .add("City", delCity)
                    .add("Road", delRoad)
                    .build();

            Request request = new Request.Builder()
                    .url(urls)
                    .post(formBody)
                    .build();
            Response response = null;

            try {
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()){

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class submitCAddress extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
             String insUID = strings[0];
             String insFullName = strings[1];
             String insMobile = strings[2];
             String insState = strings[3];
             String insCity = strings[4];
             String insRoad = strings[5];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("UID", insUID)
                    .add("FullName", insFullName)
                    .add("Mobile", insMobile)
                    .add("State", insState)
                    .add("City", insCity)
                    .add("Road", insRoad)
                    .build();

            Request request = new Request.Builder()
                    .url(urlsCAI)
                    .post(formBody)
                    .build();
            Response response = null;

            try {
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()){

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
