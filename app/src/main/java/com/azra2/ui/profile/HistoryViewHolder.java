package com.azra2.ui.profile;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivImage;
    public TextView tvTitle, tvContent, tvAttach;

    public HistoryViewHolder(View itemView) {
        super(itemView);
        ivImage = itemView.findViewById(R.id.ivImage);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvContent = itemView.findViewById(R.id.tvContent);
        tvAttach = itemView.findViewById(R.id.tvAttach);
    }
}
