package com.azra2.ui.store;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMCoinPack;
import com.azra2.model.YMUser;
import com.azra2.model.YMUserResponse;
import com.azra2.ui.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinPackAdapter extends RecyclerView.Adapter<CoinPackViewHolder> {

    private ArrayList<YMCoinPack> dataList;
    private MainActivity activity;
    private CoinPackViewHolder.OnItemClickListener listener;
    private BuyCoinsFragment mParentFragment;
    private View parentView;
    public CoinPackViewHolder selectedHolder;

    public CoinPackAdapter(Activity activity, BuyCoinsFragment parent, ArrayList<YMCoinPack> dataList){
        this.activity = (MainActivity) activity;
        this.dataList = dataList;
        this.listener = parent;
        parentView = parent.rootView;
        mParentFragment = parent;
    }

    @NonNull
    @Override
    public CoinPackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_coins, parent, false);
        return new CoinPackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinPackViewHolder holder, int position) {

        YMCoinPack coinPack = dataList.get(position);

        //set item click listener
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(v, position));

        //set UI
        if(coinPack.selected){
            holder.ivTick.setVisibility(View.VISIBLE);
            holder.tvPrice.setText(activity.getResources().getString(R.string.buy));
            holder.tvPrice.setTextColor(activity.getResources().getColor(R.color.btnFacebookColor));
            selectedHolder = holder;
        }
        else {
            holder.ivTick.setVisibility(View.INVISIBLE);
            holder.tvPrice.setText("$" + Double.toString(coinPack.getPrice()));
            holder.tvPrice.setTextColor(activity.getResources().getColor(R.color.amber_900));
        }
        holder.tvCount.setText("x " + coinPack.getCount());
        holder.tvCountStr.setText(coinPack.getCount() + " "+ activity.getResources().getString(R.string.coins));

        //
        holder.cvPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tvPrice.getText().toString().equalsIgnoreCase(activity.getResources().getString(R.string.buy))){
//                    mParentFragment.launchPurchageFlow(coinPack); //real
                    purchaseCoins(coinPack.getCount(), coinPack.getPrice()); //for backend test

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    void purchaseCoins(int coinCount, double price){
        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(activity.getResources().getString(R.string.message_wait));
        progressDialog.show();
        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserResponse> call = service.buyCoin(AGApplication.the().getMe().getId(), coinCount, price,
                AGApplication.the().getMe().getAppToken());

        call.enqueue(new Callback<YMUserResponse>() {
            @Override
            public void onResponse(Call<YMUserResponse> call, Response<YMUserResponse> response) {
                progressDialog.dismiss();
                YMUserResponse response1 = response.body();
                if(response1 != null){
                    if(response1.getResult().equalsIgnoreCase("false")){
                        Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        YMUser user = response1.getData();
                        if(user != null){
                            AGApplication.the().setMe(user);

                            AGApplication.the().purchasedCoinCount = coinCount;
                            AGApplication.the().purchagePrice = price;

                            DateFormat df = new SimpleDateFormat("d MMM yyyy HH:mm");
                            String date = df.format(Calendar.getInstance().getTime());
                            AGApplication.the().purchaseTime = date;
                            showPurchasedDlg(coinCount);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<YMUserResponse> call, Throwable t) {
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPurchasedDlg(int coinCount){
        final Dialog dlgCoinPurchased = new Dialog(activity);
        dlgCoinPurchased.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlgCoinPurchased.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgCoinPurchased.setCancelable(false);
        dlgCoinPurchased.setContentView(R.layout.dialog_coins_purchased);
        Window window = dlgCoinPurchased.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView closeBtn = dlgCoinPurchased.findViewById(R.id.ivClose);
        TextView tvTitle = dlgCoinPurchased.findViewById(R.id.tvCoinPurchagedTitle);
        TextView tvContent = dlgCoinPurchased.findViewById(R.id.tvCoinPurchagedContent);
        TextView tvCheck = dlgCoinPurchased.findViewById(R.id.tvCheckCoinBalance);

        tvTitle.setText(String.format(activity.getResources().getString(R.string.coin_purchased),coinCount));
        tvContent.setText(String.format(activity.getResources().getString(R.string.you_purchased_), coinCount));

        closeBtn.setOnClickListener(v -> {
            dlgCoinPurchased.dismiss();
        });

        tvCheck.setOnClickListener(v -> {
            dlgCoinPurchased.dismiss();
            NavController navController = Navigation.findNavController(parentView);
            NavDirections directions = BuyCoinsFragmentDirections.actionBuyCoinsFragmentToNavProfile();
            navController.navigate(directions);
        });

        dlgCoinPurchased.show();
    }



}
