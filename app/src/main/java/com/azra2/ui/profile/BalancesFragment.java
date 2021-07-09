package com.azra2.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.azra2.AGApplication;
import com.azra2.R;

public class BalancesFragment extends Fragment {
    private TextView tvCoinBalance, tvLatestCoins, tvLatestPurchageTime, tvLatestPrice;

    public BalancesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_balances, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        this.tvCoinBalance = view.findViewById(R.id.coinBalance);
        this.tvLatestCoins = view.findViewById(R.id.tvLatestCoins);
        this.tvLatestPurchageTime = view.findViewById(R.id.tvLatestPurchageTime);
        this.tvLatestPrice = view.findViewById(R.id.tvLatestPrice);

        this.tvCoinBalance.setText("x " + AGApplication.the().getMe().getCoinBalance());
        this.tvLatestCoins.setText(String.valueOf(AGApplication.the().purchasedCoinCount) + " coins");
        this.tvLatestPrice.setText("$ "+ String.valueOf(AGApplication.the().purchagePrice));
        this.tvLatestPurchageTime.setText(AGApplication.the().purchaseTime);
    }
}