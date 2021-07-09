package com.azra2.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.model.YMUserPayment;

import java.util.ArrayList;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<YMUserPayment> datalist;

    public PaymentHistoryAdapter(Context context, ArrayList<YMUserPayment> datalist){
        this.context = context;
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
        YMUserPayment payment = (YMUserPayment)this.datalist.get(position);
        if(payment.getType().equalsIgnoreCase(YMConst.PAYMENT_COIN)){
            viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.coin_minutes));
            viewHolder.tvTitle.setText(payment.getCoin() + " Coins");
            viewHolder.tvContent.setText(payment.getCreatedAt());
            int money = (int)((payment.getMoney() + 0.005) * 100);
            double dmoney = money /100.0;
            viewHolder.tvAttach.setText("$ " + dmoney);
        }
        else if(payment.getType().equalsIgnoreCase(YMConst.PAYMENT_COIN_GIFT)){
            viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.gift));
            viewHolder.tvTitle.setText("Gift Purchase");
            viewHolder.tvContent.setText(payment.getCreatedAt());
            viewHolder.tvAttach.setText(payment.getCoin() + " coins");
        }
        else if(payment.getType().equalsIgnoreCase(YMConst.PAYMENT_COIN_TIP)){
            viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.coin_heart));
            viewHolder.tvTitle.setText("Tip Purchase");
            viewHolder.tvContent.setText(payment.getCreatedAt());
            viewHolder.tvAttach.setText(payment.getCoin() + " coins");
        }
    }

    @Override
    public int getItemCount() {
        return this.datalist.size();
    }


}
