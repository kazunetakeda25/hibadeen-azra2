package com.azra2.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.model.YMUserCall;
import com.azra2.model.YMUserLive;
import com.azra2.model.YMUserMinute;
import com.azra2.model.YMUserPayment;

import java.util.ArrayList;

public class MinuteHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<YMUserCall> calldata;
    private ArrayList<YMUserLive> livedata;

    public MinuteHistoryAdapter(Context context, ArrayList<YMUserCall> calldata, ArrayList<YMUserLive> livedata){
        this.context = context;
        this.calldata = calldata;
        this.livedata = livedata;
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
        if(position < calldata.size()){
            YMUserCall call = this.calldata.get(position);
            viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.minutes));
            switch (Integer.parseInt(call.getRoomType())){
                case YMConst.RT_PRIVATE:
                    viewHolder.tvTitle.setText(context.getResources().getString(R.string.menu_private_room)
                            + ", " + call.getAgentNickName());
                    break;
                case YMConst.RT_ADULT:
                    viewHolder.tvTitle.setText(context.getResources().getString(R.string.menu_adult_private)
                            + ", " + call.getAgentNickName());
                    break;
                default:
            }
            viewHolder.tvContent.setText(call.getCreatedAt());
            viewHolder.tvAttach.setText(call.getDuration() + " mins");
        }
        else{
            YMUserLive live = this.livedata.get(position - this.calldata.size());
            viewHolder.ivImage.setImageDrawable(context.getResources().getDrawable(R.drawable.minutes));
            viewHolder.tvTitle.setText(context.getResources().getString(R.string.menu_public_room)
                    + ", " + live.getAgentNickName());
            viewHolder.tvContent.setText(live.getCreatedAt());
//            viewHolder.tvAttach.setText(live.getDuration() + " mins");
        }

    }

    @Override
    public int getItemCount() {
        return this.calldata.size() + this.livedata.size();
    }
}
