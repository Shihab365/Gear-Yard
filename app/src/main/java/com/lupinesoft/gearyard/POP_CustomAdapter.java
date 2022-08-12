package com.lupinesoft.gearyard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class POP_CustomAdapter extends RecyclerView.Adapter<POP_CustomAdapter.MyViewHolder>{
    Context context;
    List<PopItem> listItems;

    public POP_CustomAdapter(Context context, List<PopItem> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recy_front_model, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        final PopItem listItem = listItems.get(i);
        String path1 = "http://100.72.26.84/GYadminPanel/all_photo/";
        String path2 = listItem.getPhoto();
        final String photoUrl = path1 + path2;
        Picasso.with(context).load(photoUrl).into(myViewHolder.proImage);
        myViewHolder.textBrand.setText(listItem.getBrand());
        myViewHolder.textModel.setText(listItem.getModel());
        myViewHolder.textPrice.setText(listItem.getPrice());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView proImage, buttonImage;
        TextView textBrand;
        TextView textModel;
        TextView textPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            proImage = itemView.findViewById(R.id.recy_Image_ID);
            textModel = itemView.findViewById(R.id.recy_model_ID);
            textBrand = itemView.findViewById(R.id.recy_brand_ID);
            textPrice = itemView.findViewById(R.id.recy_price_ID);
            buttonImage = itemView.findViewById(R.id.recy_addButton_ID);
        }
    }
}


