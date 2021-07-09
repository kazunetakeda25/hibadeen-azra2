package com.azra2.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;
import com.azra2.common.YMConst;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import java.util.List;

import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmMessageType;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MsgViewHolder> {

    Context context;
    private List<MessageBean> messageBeanList;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public MessageAdapter(Context context, List<MessageBean> messageBeanList, @NonNull OnItemClickListener listener) {
        this.context = context;
        this.inflater = ((Activity) context).getLayoutInflater();
        this.messageBeanList = messageBeanList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.msg_item_layout, parent, false);
        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder holder, int position) {
        setupView(holder, position);
    }

    @Override
    public int getItemCount() {
        return messageBeanList.size();
    }

    private void setupView(MsgViewHolder holder, int position) {
        MessageBean bean = messageBeanList.get(position);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(bean);
        });

        RtmMessage rtmMessage = bean.getMessage();
        switch (rtmMessage.getMessageType()) {
            case RtmMessageType.TEXT:
                String msg = rtmMessage.getText();
                String[] strs = msg.split(":");
                int degree = Integer.parseInt(strs[0]);
                holder.tvName.setText(strs[1]);
                holder.tvContent.setText(strs[2]);

                switch(degree){
                    case YMConst.BD_AGENT:
                        holder.ivDegree.setImageDrawable(context.getResources().getDrawable(R.drawable.star));
                        holder.tvName.setTextColor(context.getResources().getColor(R.color.yellow_400));
                        break;
                    case YMConst.BD_CROWN:
                        holder.ivDegree.setImageDrawable(context.getResources().getDrawable(R.drawable.crown));
                        holder.tvName.setTextColor(context.getResources().getColor(R.color.yellow_A400));

                        break;
                    case YMConst.BD_DIAMOND:
                        holder.ivDegree.setImageDrawable(context.getResources().getDrawable(R.drawable.diamond));
                        holder.tvName.setTextColor(context.getResources().getColor(R.color.cyan_A400));

                        break;
                    case YMConst.BD_COIN:
                        holder.ivDegree.setImageDrawable(context.getResources().getDrawable(R.drawable.coin_heart_small));
                        holder.tvName.setTextColor(context.getResources().getColor(R.color.purple_A400));
                        break;
                    case YMConst.BD_NOTHING:
                        holder.ivDegree.setImageDrawable(null);
                        holder.tvName.setTextColor(context.getResources().getColor(R.color.green_A700));
                        break;
                }
                break;
            case RtmMessageType.IMAGE:
                RtmImageMessage rtmImageMessage = (RtmImageMessage) rtmMessage;
                RequestBuilder<Drawable> builder = Glide.with(holder.itemView)
                        .load(rtmImageMessage.getThumbnail())
                        .override(rtmImageMessage.getThumbnailWidth(), rtmImageMessage.getThumbnailHeight());

                break;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(MessageBean message);
    }

    static class MsgViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDegree;
        TextView tvName, tvContent;

        MsgViewHolder(View itemView) {
            super(itemView);
            this.ivDegree = itemView.findViewById(R.id.ivDegree);
            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvContent = itemView.findViewById(R.id.tvContent);
        }
    }

}
