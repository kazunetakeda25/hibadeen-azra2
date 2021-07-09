package com.azra2.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.model.YMUserMinute;
import com.azra2.model.YMUserPayment;
import com.azra2.ui.mainfrag.NotificationAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Object> datalist;
    private Context context;
    private String mHistoryType;

    public HistoryAdapter(Context context, String historyType, ArrayList<Object> datalist){
        this.context = context;
        this.mHistoryType = historyType;
        this.datalist = datalist;
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item,parent,false);
        viewHolder = new HistoryViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        HistoryViewHolder viewHolder = (HistoryViewHolder)holder;
        if(this.mHistoryType.equalsIgnoreCase(YMConst.PAYMENT_HISTORY)){
            YMUserPayment payment = (YMUserPayment)this.datalist.get(position);
            if(payment.getType().equalsIgnoreCase(YMConst.PAYMENT_COIN)){
                viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.coin_minutes));
                viewHolder.tvTitle.setText(payment.getCoin() + "Coins");
                viewHolder.tvContent.setText(payment.getCreatedAt());
                //viewHolder.tvAttach.setText(payment.getMoney());
            }
        }
        else if(this.mHistoryType.equalsIgnoreCase(YMConst.MINUTE_HISTORY)){
            YMUserMinute minute = (YMUserMinute)this.datalist.get(position);
            viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.minutes));
            switch (Integer.parseInt(minute.getRoomType())){
                case YMConst.RT_PUBLIC:
                    viewHolder.tvTitle.setText(context.getResources().getString(R.string.menu_public_room)
                            + ", " + minute.getAgentNickName());
                    break;
                case YMConst.RT_PRIVATE:
                    viewHolder.tvTitle.setText(context.getResources().getString(R.string.menu_private_room)
                            + ", " + minute.getAgentNickName());
                    break;
                case YMConst.RT_ADULT:
                    viewHolder.tvTitle.setText(context.getResources().getString(R.string.menu_adult_private)
                            + ", " + minute.getAgentNickName());
                    break;
                default:
            }
            viewHolder.tvContent.setText(minute.getCreatedAt());
            viewHolder.tvAttach.setText(minute.getDuration() + " mins");
        }
    }

    @Override
    public int getItemCount() {
        return this.datalist.size();
    }




}
