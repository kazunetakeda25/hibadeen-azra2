package com.azra2.ui.store;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;

public class MoneyGiftViewHolder extends RecyclerView.ViewHolder {
    public final View mView;

    ImageView ivTick;
    TextView tvWorth, tvWorthStr;
    CardView cvWorth;

    public MoneyGiftViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        ivTick = mView.findViewById(R.id.ivTick);
        tvWorth = mView.findViewById(R.id.tvWorth);
        tvWorthStr = mView.findViewById(R.id.tvWorthStr);
        cvWorth = mView.findViewById(R.id.cvWorth);
    }

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

}
