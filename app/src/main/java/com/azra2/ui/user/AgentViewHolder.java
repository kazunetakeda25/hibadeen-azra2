package com.azra2.ui.user;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;
import com.hbb20.CountryCodePicker;

public class AgentViewHolder extends RecyclerView.ViewHolder {

    public final View mView;

    public ImageView ivProfile , ivStatus,ivStatus2, ivAction, ivCallType;
    public TextView tvStatus, tvAction, tvName, tvPrice;
    public LinearLayout llAction, llProfile;
    public CountryCodePicker countryCodePicker;


    public AgentViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        ivProfile = mView.findViewById(R.id.ivProfile);
        ivStatus = mView.findViewById(R.id.ivStatus);
        ivStatus2 = mView.findViewById(R.id.ivStatus2);
        ivAction = mView.findViewById(R.id.ivAction);
        ivCallType = mView.findViewById(R.id.ivCallType);
        countryCodePicker = mView.findViewById(R.id.ccp);

        tvStatus = mView.findViewById(R.id.tvStatus);
        tvAction = mView.findViewById(R.id.tvAction);
        tvName = mView.findViewById(R.id.tvName);
        tvPrice = mView.findViewById(R.id.tvPrice);

        llAction = mView.findViewById(R.id.llAction);
        llProfile = mView.findViewById(R.id.llProfile);
    }

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }


}