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
import com.azra2.model.YMMoneyGift;

import java.util.ArrayList;

public class MoneyGiftAdapter extends RecyclerView.Adapter<MoneyGiftViewHolder> {
    private ArrayList<YMMoneyGift> dataList;
    private Context context;
    private MoneyGiftViewHolder.OnItemClickListener listener;
    private View parentView;
    public MoneyGiftViewHolder selectedHolder;

    public MoneyGiftAdapter(Context context, View parent, ArrayList<YMMoneyGift> dataList, MoneyGiftViewHolder.OnItemClickListener listener){
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
        this.parentView = parent;
    }

    @NonNull
    @Override
    public MoneyGiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_gifts, parent, false);
        return new MoneyGiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoneyGiftViewHolder holder, int position) {

        YMMoneyGift moneyGift = dataList.get(position);

        //set item click listener
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(v, position));

        //set UI
        if(moneyGift.selected){
            holder.ivTick.setVisibility(View.VISIBLE);
            holder.tvWorth.setText(context.getResources().getString(R.string.buy));
            holder.tvWorth.setTextColor(context.getResources().getColor(R.color.btnFacebookColor));
            selectedHolder = holder;
        }
        else {
            holder.ivTick.setVisibility(View.INVISIBLE);
            holder.tvWorth.setText("$" + Double.toString(moneyGift.getWorth()));
            holder.tvWorth.setTextColor(context.getResources().getColor(R.color.red_A400));
        }

        holder.tvWorthStr.setText("Worth $" + moneyGift.getWorth());

        //
        holder.cvWorth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tvWorth.getText().toString().equalsIgnoreCase(context.getResources().getString(R.string.buy))){
                    NavDirections action = BuyMoneyGiftsFragmentDirections.actionBuyMoneyGiftsFragmentToShareGiftFragment(context.getResources().getString(R.string.money_gift),
                            "0", String.valueOf(moneyGift.getWorth()));
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
