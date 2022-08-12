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

public class cartCustomAdapter extends RecyclerView.Adapter<cartCustomAdapter.MyViewHolder> {

    Context context;
    List<CartList> cartLists;
    final String urls = "http://100.72.26.84/GYadminPanel/appDB/delete_cart/delete_cart_data.php";

    public cartCustomAdapter(Context context, List<CartList> cartLists) {
        this.context = context;
        this.cartLists = cartLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recy_cart_view, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        final CartList cartList = cartLists.get(i);

        myViewHolder.tvBrand.setText(cartList.getBrand());
        myViewHolder.tvModel.setText(cartList.getModel());
        myViewHolder.tvQuantity.setText(cartList.getQuantity());
        myViewHolder.tvTotal.setText(cartList.getTotal());

        String photoUrl = cartList.getPhotoUrl();
        Picasso.with(context).load(photoUrl).into(myViewHolder.cartImage);

        myViewHolder.cartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String delUID = cartList.getUID();
                String delPID = cartList.getPID();
                new deletePersonalCart().execute(delUID, delPID);

                String tempS = CartFragment.tvTotalCost.getText().toString();
                int tempINT = Integer.parseInt(tempS);
                int tempSP = Integer.parseInt(cartList.getTotal());
                int totalC = tempINT - tempSP;

                CartFragment.tvTotalCost.setText(String.valueOf(totalC));

                cartLists.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, cartLists.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView cartImage, cartDelete;
        TextView tvBrand, tvModel, tvQuantity, tvTotal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImage = itemView.findViewById(R.id.cart_imageview_ID);
            tvBrand = itemView.findViewById(R.id.cart_tv_brand_ID);
            tvModel = itemView.findViewById(R.id.cart_tv_model_ID);
            tvQuantity = itemView.findViewById(R.id.cart_tv_quantity_ID);
            tvTotal = itemView.findViewById(R.id.cart_tv_total_ID);
            cartDelete = itemView.findViewById(R.id.cart_button_deleteID);
        }
    }

    public class deletePersonalCart extends AsyncTask<String, Void, String>{

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
