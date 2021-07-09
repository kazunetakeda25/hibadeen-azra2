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
import com.azra2.model.YMMoneyGift;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyMoneyGiftsFragment extends Fragment implements MoneyGiftViewHolder.OnItemClickListener{

    private RecyclerView rvMoneyGiftList;
    MoneyGiftAdapter moneyGiftAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<YMMoneyGift> moneyGifts = new ArrayList<>();

    public BuyMoneyGiftsFragment() {
        initMoneyGifts();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_money_gifts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvMoneyGiftList = view.findViewById(R.id.rvMoneyGiftList);
        moneyGiftAdapter = new MoneyGiftAdapter(getActivity(), view, moneyGifts, this);
        rvMoneyGiftList.setHasFixedSize(true);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvMoneyGiftList.setLayoutManager(layoutManager);
        rvMoneyGiftList.setAdapter(moneyGiftAdapter);

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
        for(int i=0; i<moneyGiftAdapter.getItemCount();i++){
            if(moneyGifts.get(i).selected == true){
                moneyGifts.get(i).selected = false;
                moneyGiftAdapter.notifyItemChanged(i);
            }
        }
        moneyGifts.get(position).selected = true;
        moneyGiftAdapter.notifyItemChanged(position);
    }

    private void initMoneyGifts(){
        moneyGifts.add(new YMMoneyGift(1, 0.99));
        moneyGifts.add(new YMMoneyGift(1, 1.99));
        moneyGifts.add(new YMMoneyGift(1, 2.99));
        moneyGifts.add(new YMMoneyGift(1, 4.99));
        moneyGifts.add(new YMMoneyGift(1, 9.99));
        moneyGifts.add(new YMMoneyGift(1, 18.99));
        moneyGifts.add(new YMMoneyGift(1, 27.99));
        moneyGifts.add(new YMMoneyGift(1, 35.99));
        moneyGifts.add(new YMMoneyGift(1, 59.99));
    }

//    @Override
//    public void onRefresh(){
//
//    }

}