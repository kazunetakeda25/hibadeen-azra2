package com.azra2.ui.store;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;
import com.azra2.model.YMAgent;
import com.azra2.ui.MainActivity;
import com.azra2.ui.room.AgentListFragment;
import com.azra2.utils.Utils;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SimpleAgentAdapter extends RecyclerView.Adapter<SimpleAgentViewHolder> {

    private ArrayList<YMAgent> dataList;
    private MainActivity mainActivity;
    private SimpleAgentViewHolder.OnItemClickListener listener;
    private ShareGiftFragment parentFragment;
    private ProgressDialog progressDialog;

    public SimpleAgentAdapter(Activity baseActivity, ShareGiftFragment parentFragment, ArrayList<YMAgent> list){
        this.dataList = list;
        this.mainActivity = (MainActivity)baseActivity;
        this.listener = (SimpleAgentViewHolder.OnItemClickListener)parentFragment;
        this.parentFragment = parentFragment;
        this.progressDialog = new ProgressDialog(mainActivity);
    }

    @Override
    public SimpleAgentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view =null;
        SimpleAgentViewHolder viewHolder = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small_agent,parent,false);
        viewHolder = new SimpleAgentViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SimpleAgentViewHolder holder, final int position) {

        YMAgent agent = dataList.get(position);

        Picasso.Builder builder = new Picasso.Builder(mainActivity);
        builder.downloader(new OkHttp3Downloader(mainActivity));
        if (!Utils.isNullOrEmpty(agent.getProfileImage())) {
            builder.build().load( agent.getProfileImage())
                    .placeholder((R.mipmap.ic_launcher_round))
                    .error(R.mipmap.ic_launcher_round)
                    .into(holder.ivProfile);
        }

        holder.tvName.setText(agent.getNickName());

        //set item click listener
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(v, position));

        if(agent.selected){
            holder.ivProfileCover.setVisibility(View.VISIBLE);
            holder.ivCheck.setVisibility(View.VISIBLE);
        }
        else{
            holder.ivProfileCover.setVisibility(View.INVISIBLE);
            holder.ivCheck.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}
