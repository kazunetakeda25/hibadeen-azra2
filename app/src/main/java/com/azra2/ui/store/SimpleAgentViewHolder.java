package com.azra2.ui.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;
import com.azra2.model.YMAgent;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SimpleAgentViewHolder extends RecyclerView.ViewHolder{
    public final View mView;

    public CircleImageView ivProfile, ivProfileCover;
    public TextView tvName;
    public ImageView ivCheck;

    public SimpleAgentViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        ivProfile = mView.findViewById(R.id.ivProfile);
        tvName = mView.findViewById(R.id.tvName);
        ivProfileCover = mView.findViewById(R.id.ivProfileCover);
        ivCheck = mView.findViewById(R.id.ivCheck);
    }

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }
}
