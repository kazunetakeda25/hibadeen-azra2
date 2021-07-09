package com.azra2.ui.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.azra2.R;
import com.azra2.model.YMCoinGift;

import java.util.ArrayList;

public class CoinGiftAdapter extends RecyclerView.Adapter<CoinGiftViewHolder>{
    private ArrayList<YMCoinGift> dataList;
    private Context context;
    private CoinGiftViewHolder.OnItemClickListener listener;
    private View parentView;
    public CoinGiftViewHolder selectedHolder;

    public CoinGiftAdapter(Context context, View parent, ArrayList<YMCoinGift> dataList, CoinGiftViewHolder.OnItemClickListener listener){
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
        this.parentView = parent;
    }

    @NonNull
    @Override
    public CoinGiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_gifts, parent, false);
        return new CoinGiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinGiftViewHolder holder, int position) {

        YMCoinGift coinGift = dataList.get(position);

        //set item click listener
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(v, position));

        //set UI
        if(coinGift.selected){
            holder.ivTick.setVisibility(View.VISIBLE);
            holder.tvWorth.setText(context.getResources().getString(R.string.buy));
            holder.tvWorth.setTextColor(context.getResources().getColor(R.color.btnFacebookColor));
            selectedHolder = holder;
        }
        else {
            holder.ivTick.setVisibility(View.INVISIBLE);
            holder.tvWorth.setText(Integer.toString(coinGift.getCountOfCoins()));
            holder.tvWorth.setTextColor(context.getResources().getColor(R.color.red_A400));
        }

        holder.tvWorthStr.setText(coinGift.getCountOfCoins() + " coins");

        //
        holder.cvWorth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tvWorth.getText().toString().equalsIgnoreCase(context.getResources().getString(R.string.buy))){
                    NavDirections action = BuyCoinGiftsFragmentDirections.actionBuyCoinGiftsFragmentToShareGiftFragment(context.getResources().getString(R.string.coin_gift),
                            String.valueOf(coinGift.getCountOfCoins()),"0.0");
                    NavController controller = Navigation.findNavController(parentView);
                    controller.navigate(action);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
