package com.lupinesoft.gearyard;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import com.steelkiwi.library.IncrementProductView;
import com.steelkiwi.library.listener.OnStateListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SrcCustomAdapter extends RecyclerView.Adapter<SrcCustomAdapter.MyViewHolder> implements OnStateListener {

    Context context;
    List<RecyList> listItems;
    Dialog dialog;
    TextView textBrand, textModel, textPrice, textTotal, textCount;
    Button btnAdd;
    String strBrand, strModel, strPrice;
    ImageView btnDismiss;
    static String strPid;
    String uid = DashboardActivity.strUID;

    public SrcCustomAdapter(Context context, List<RecyList> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_custom_layout, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        final RecyList listItem = listItems.get(i);
        String path1 = "http://100.72.26.84/GYadminPanel/all_photo/";
        String path2 = listItem.getPhoto();
        final String photoUrl = path1 + path2;
        Picasso.with(context).load(photoUrl).into(myViewHolder.proImage);
        myViewHolder.textBrand.setText(listItem.getBrand());
        myViewHolder.textModel.setText(listItem.getModel());
        myViewHolder.textPrice.setText(listItem.getPrice());

        myViewHolder.buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LikeButton buttonFav;
                strPid = listItem.getPid();
                dialog = new Dialog(v.getRootView().getContext());
                strBrand = myViewHolder.textBrand.getText().toString();
                strModel = myViewHolder.textModel.getText().toString();
                strPrice = myViewHolder.textPrice.getText().toString();
                dialog.setContentView(R.layout.view_dialog);

                //Code for setParentMiddleIcon start
                InputStream iStream = null;
                try {
                    iStream = (InputStream) new URL(photoUrl).getContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Drawable drawable = Drawable.createFromStream(iStream,"src");

                IncrementProductView view = dialog.findViewById(R.id.productView);
                view.setOnStateListener(SrcCustomAdapter.this);
                view.setParentMiddleIcon(drawable);
                //Code for setParentMiddleIcon end

                textBrand = dialog.findViewById(R.id.dialogBrandId);
                textModel = dialog.findViewById(R.id.dialogModelId);
                textPrice = dialog.findViewById(R.id.textPriceId);
                textTotal = dialog.findViewById(R.id.textTotalId);
                textCount = dialog.findViewById(R.id.textCountId);
                btnDismiss = dialog.findViewById(R.id.btnDismissId);
                btnAdd = dialog.findViewById(R.id.btnCartId);
                buttonFav = dialog.findViewById(R.id.dialogFavButton_id);

                buttonFav.setOnLikeListener(new OnLikeListener() {
                    @Override
                    public void liked(LikeButton likeButton) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new submitFavInfo().execute("http://100.72.26.84/GYadminPanel/appDB/fav/fav_insert.php", photoUrl);
                            }
                        }).start();
                    }
                    @Override
                    public void unLiked(LikeButton likeButton) {

                    }
                });

                btnDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new submitCartInfo().execute("http://100.72.26.84/GYadminPanel/appDB/cart_insert.php", photoUrl);
                            }
                        }).start();
                    }
                });
                textBrand.setText(strBrand);
                textModel.setText(strModel);
                textPrice.setText(strPrice);
                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }

    //Increment view start
    @Override
    public void onCountChange(int count) {
        String str = textPrice.getText().toString();
        int prices = Integer.parseInt(str);

        textCount.setText(""+count);
        textTotal.setText(""+(prices*count));
    }

    @Override
    public void onConfirm(int count) {

    }

    @Override
    public void onClose() {

    }
    //Increment view end

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView proImage, buttonImage;
        TextView textBrand;
        TextView textModel;
        TextView textPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            proImage = itemView.findViewById(R.id.src_iv_photo_id);
            textModel = itemView.findViewById(R.id.src_tv_model_id);
            textBrand = itemView.findViewById(R.id.src_tv_brand_id);
            textPrice = itemView.findViewById(R.id.src_tv_price_id);
            buttonImage = itemView.findViewById(R.id.src_btn_add_id);
        }
    }

    private class submitFavInfo extends AsyncTask<String, Void, String> {
        String strUID = uid;
        String strFavPID = strPid;
        String strBrand = textBrand.getText().toString();
        String strModel = textModel.getText().toString();

        @Override
        protected String doInBackground(String... strings) {
            String strUrl = strings[1];
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("UID", strUID)
                    .add("PID", strFavPID)
                    .add("Brand", strBrand)
                    .add("Model", strModel)
                    .add("PhotoUrl", strUrl)
                    .build();

            Request request = new Request.Builder().url(strings[0])
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class submitCartInfo extends AsyncTask<String, Void, String> {
        String strUID = uid;
        String strBrand = textBrand.getText().toString();
        String strModel = textModel.getText().toString();
        String strPrice = textPrice.getText().toString();
        String strQuantity = textCount.getText().toString();
        String strTotal = textTotal.getText().toString();
        String strPid = SrcCustomAdapter.strPid;
        @Override
        protected String doInBackground(String... strings) {
            String strUrl = strings[1];
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("UID", strUID)
                    .add("PID", strPid)
                    .add("Brand", strBrand)
                    .add("Model", strModel)
                    .add("Price", strPrice)
                    .add("Quantity", strQuantity)
                    .add("Total", strTotal)
                    .add("PhotoUrl", strUrl)
                    .build();

            Request request = new Request.Builder().url(strings[0])
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    dialog.dismiss();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

