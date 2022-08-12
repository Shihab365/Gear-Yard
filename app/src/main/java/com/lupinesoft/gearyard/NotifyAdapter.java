package com.lupinesoft.gearyard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {

    Context context;
    List<NotifyList> notifyLists;

    public NotifyAdapter(Context context, List<NotifyList> notifyLists) {
        this.context = context;
        this.notifyLists = notifyLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_notify_layout, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        final NotifyList notifyList = notifyLists.get(i);
        myViewHolder.tvTitle.setText(notifyList.getTitle());
        myViewHolder.tvDescription.setText(notifyList.getDescription());
        myViewHolder.tvDate.setText(notifyList.getDate());
    }

    @Override
    public int getItemCount() {
        return notifyLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, tvDescription, tvDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.notify_tvTitle_id);
            tvDescription = itemView.findViewById(R.id.notify_tvDetails_id);
            tvDate = itemView.findViewById(R.id.notify_tvDate_id);
        }
    }

}
