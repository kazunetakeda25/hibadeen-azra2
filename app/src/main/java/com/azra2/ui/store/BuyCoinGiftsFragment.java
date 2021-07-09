package com.azra2.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.azra2.R;
import com.azra2.model.YMCoinGift;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
  */
public class BuyCoinGiftsFragment extends Fragment implements CoinGiftViewHolder.OnItemClickListener{
    private RecyclerView rvCoinGiftList;
    CoinGiftAdapter coinGiftAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<YMCoinGift> coinGifts = new ArrayList<>();

    public BuyCoinGiftsFragment() {
        // Required empty public constructor
        initCoinGifts();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_coin_gifts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvCoinGiftList = view.findViewById(R.id.rvCoinGiftList);
        coinGiftAdapter = new CoinGiftAdapter(getActivity(), view, coinGifts, this);
        rvCoinGiftList.setHasFixedSize(true);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvCoinGiftList.setLayoutManager(layoutManager);
        rvCoinGiftList.setAdapter(coinGiftAdapter);

//        mSwipeRefreshLayout = view.findViewById(R.id.slAgent);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);

//        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onItemClicked(View view, int position) {
        for(int i=0; i<coinGiftAdapter.getItemCount();i++){
            if(coinGifts.get(i).selected == true){
                coinGifts.get(i).selected = false;
                coinGiftAdapter.notifyItemChanged(i);
            }
        }
        coinGifts.get(position).selected = true;
        coinGiftAdapter.notifyItemChanged(position);
    }

    private void initCoinGifts(){
        coinGifts.add(new YMCoinGift(1,1));
        coinGifts.add(new YMCoinGift(1,2));
        coinGifts.add(new YMCoinGift(1,3));
        coinGifts.add(new YMCoinGift(1,5));
        coinGifts.add(new YMCoinGift(1,10));
        coinGifts.add(new YMCoinGift(1,20));
        coinGifts.add(new YMCoinGift(1,30));
        coinGifts.add(new YMCoinGift(1,50));
        coinGifts.add(new YMCoinGift(1,100));
    }

}