package com.azra2.ui.mainfrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;
import com.azra2.model.YMNotification;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<YMNotification> dataList;
    private Context context;

    public NotificationAdapter(Context context, ArrayList<YMNotification> dataList){
        this.context = context;
        this.dataList = dataList;
    }


    @Override
    public int getItemViewType(int position){
//        return dataList.get(position).getType();
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =null;
        RecyclerView.ViewHolder viewHolder = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notif_00_layout,parent,false);
        viewHolder = new NotificationAdapter.NotificationViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        switch (holder.getItemViewType()){
            case 0:
                NotificationViewHolder vh0 = (NotificationViewHolder)holder;
                vh0.tvContent.setText("Notification 1 Admin approved \nyour request to change rooms.\nYou have officially joined the\nPrivate Room.");
                vh0.tvTime.setText("2m");
                break;
            case 1:
                NotificationViewHolder vh1 = (NotificationViewHolder)holder;
                vh1.tvContent.setText("Notification 1 username1 \nnotified you for a Private Room Call.");
                vh1.tvTime.setText("2m");
                break;

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivImage;
        public TextView tvContent;
        public TextView tvTime;

        public NotificationViewHolder(View itemView){
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
        }

    }


}
