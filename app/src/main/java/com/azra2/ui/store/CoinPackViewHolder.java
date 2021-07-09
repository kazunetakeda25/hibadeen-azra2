package com.azra2.ui.store;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;

public class CoinPackViewHolder extends RecyclerView.ViewHolder {
    public final View mView;

    public TextView tvCount , tvCountStr, tvPrice;
    public ImageView ivTick;
    public CardView cvPrice;

    public CoinPackViewHolder(View itemView){
        super(itemView);

        mView = itemView;
        ivTick = mView.findViewById(R.id.ivTick);
        tvCount = mView.findViewById(R.id.tvCount);
        tvCountStr = mView.findViewById(R.id.tvCountStr);
        tvPrice = mView.findViewById(R.id.tvPrice);
        cvPrice = mView.findViewById(R.id.cvPrice);

    }

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

}
