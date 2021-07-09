package com.azra2.ui.room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;
import com.azra2.model.YMImageInfo;
import com.azra2.utils.Utils;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RectangleImageAdapter extends RecyclerView.Adapter<RectangleImageAdapter.RectImgViewHolder>{

    private Context context;
    private List<YMImageInfo> datalist;
    public int dispCount = 6;

    public RectangleImageAdapter(Context c, List<YMImageInfo> list){
        this.context = c;
        this.datalist = list;
    }

    @Override
    public RectImgViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_rectangle_image, viewGroup, false);
        return new RectImgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RectImgViewHolder viewHolder, int position) {
        if(position > dispCount - 1) return;

        YMImageInfo imageInfo = datalist.get(position);

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        if(!Utils.isNullOrEmpty(imageInfo.getFile())){
            builder.build().load(imageInfo.getFile())
                    .placeholder((R.mipmap.ic_launcher_round))
                    .error(R.mipmap.ic_launcher_round)
                    .into(viewHolder.ivAgentImage);
        }
    }

    @Override
    public int getItemCount() {
        if(datalist.size() <= dispCount) return datalist.size();
        else return dispCount;
    }


    public class RectImgViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivAgentImage;
        public RectImgViewHolder(View itemView){
            super(itemView);
            ivAgentImage = itemView.findViewById(R.id.ivAgentImage);
        }
    }

}
