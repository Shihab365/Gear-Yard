package com.lupinesoft.gearyard;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class favCustomAdapter extends RecyclerView.Adapter<favCustomAdapter.MyViewHolder>{

    Context context;
    List<FavList> favLists;
    final String urls = "http://100.72.26.84/GYadminPanel/appDB/delete_fav/delete_fav_data.php";

    public favCustomAdapter(Context context, List<FavList> favLists) {
        this.context = context;
        this.favLists = favLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recy_fav_item, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        final FavList favList = favLists.get(i);
        myViewHolder.tvModel.setText(favList.getModel());
        myViewHolder.tvBrand.setText(favList.getBrand());
        String photoUrl = favList.getPhotoURL();
        Picasso.with(context).load(photoUrl).into(myViewHolder.favImage);

        myViewHolder.disLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String delUID = favList.getUID();
                String delPID = favList.getPID();
                new deletePersonalFav().execute(delUID, delPID);

                favLists.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, favLists.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return favLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView favImage, disLikeButton;
        TextView tvModel, tvBrand;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            favImage = itemView.findViewById(R.id.fav_image_ID);
            tvModel = itemView.findViewById(R.id.fav_tv_model_ID);
            tvBrand = itemView.findViewById(R.id.fav_tv_brand_ID);
            disLikeButton = itemView.findViewById(R.id.fav_Dis_likeButton_ID);
        }
    }

    public class deletePersonalFav extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String delUID = strings[0];
            String delPID = strings[1];

            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("UID", delUID)
                    .add("PID", delPID)
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
}
