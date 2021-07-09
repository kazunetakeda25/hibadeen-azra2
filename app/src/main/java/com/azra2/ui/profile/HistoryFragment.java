package com.azra2.ui.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMNotification;
import com.azra2.model.YMNotificationList;
import com.azra2.model.YMUserMinute;
import com.azra2.model.YMUserMinuteResponse;
import com.azra2.model.YMUserPayment;
import com.azra2.model.YMUserPaymentResponse;
import com.azra2.ui.mainfrag.NotificationAdapter;
import com.azra2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {

//    private static final String ARG_HISTORY_TYPE = "history_type";
//    private String mHistoryType;

    private static final String ARG_POSITION = "param_position";
    private int mPosition;

    private TextView tvCoinBalance, tvLatestCoins, tvLatestPurchageTime, tvLatestPrice;
    private LinearLayout llBalance;


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rvHistory;
    private ArrayList<Object> datalist = new ArrayList<>();
    private ArrayList<YMUserPayment> paymentList = new ArrayList<>();
    private ArrayList<YMUserMinute> minuteList = new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private ProgressDialog progressDialog;

    public HistoryFragment(){
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param position Position
     * @return A new instance of fragment HistoryFragment.
     */
    public static HistoryFragment newInstance(int position) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mHistoryType = getArguments().getString(ARG_HISTORY_TYPE);
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceId){

        this.llBalance = view.findViewById(R.id.llBalance);
        this.tvCoinBalance = view.findViewById(R.id.coinBalance);
        this.tvLatestCoins = view.findViewById(R.id.tvLatestCoins);
        this.tvLatestPurchageTime = view.findViewById(R.id.tvLatestPurchageTime);
        this.tvLatestPrice = view.findViewById(R.id.tvLatestPrice);

        this.tvCoinBalance.setText("x " + AGApplication.the().getMe().getCoinBalance());
        this.tvLatestCoins.setText(String.valueOf(AGApplication.the().purchasedCoinCount) + " coins");
        this.tvLatestPrice.setText("$ "+ String.valueOf(AGApplication.the().purchagePrice));
        this.tvLatestPurchageTime.setText(AGApplication.the().purchaseTime);

        mSwipeRefreshLayout = view.findViewById(R.id.srlHistory);
        //mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");

        rvHistory = view.findViewById(R.id.rvHistory);
        rvHistory.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvHistory.setLayoutManager(layoutManager);

//        getPaymentHistory();
        this.historyAdapter = new HistoryAdapter(getContext(), YMConst.PAYMENT_HISTORY, this.datalist);
        rvHistory.setAdapter(historyAdapter);

//        this.historyAdapter = new HistoryAdapter(getContext(), this.mHistoryType, this.datalist);
//        rvHistory.setAdapter(historyAdapter);

        /*switch (mPosition){
            case 0:
                this.llBalance.setVisibility(View.VISIBLE);
                this.rvHistory.setVisibility(View.GONE);
                break;
            case 1: //payment history
                this.llBalance.setVisibility(View.GONE);
                this.rvHistory.setVisibility(View.VISIBLE);
                getPaymentHistory();
                this.historyAdapter = new HistoryAdapter(getContext(), YMConst.PAYMENT_HISTORY, this.datalist);
                rvHistory.setAdapter(historyAdapter);
                break;
            case 2: //minute history
                this.llBalance.setVisibility(View.GONE);
                this.rvHistory.setVisibility(View.VISIBLE);
                getMinuteHistory();
                this.historyAdapter = new HistoryAdapter(getContext(), YMConst.MINUTE_HISTORY, this.datalist);
                rvHistory.setAdapter(historyAdapter);

                break;
            default:
        }*/

    }

    @Override
    public void onResume(){
        super.onResume();
        /*if(this.mHistoryType.equalsIgnoreCase(YMConst.PAYMENT_HISTORY)){
            getPaymentHistory();
        }
        else if( this.mHistoryType.equalsIgnoreCase(YMConst.MINUTE_HISTORY)){
            getMinuteHistory();
        }*/
    }


    /**
     * */
    private void getPaymentHistory(){
        if (!Utils.getInstance(getActivity()).isOnline()) {
            Toast.makeText(getActivity(), "Please check network state.", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();

        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserPaymentResponse> call = service.getPaymentHistory(AGApplication.the().getMe().getId(), AGApplication.the().getMe().getAppToken());

        call.enqueue(new Callback<YMUserPaymentResponse>() {
            @Override
            public void onResponse(Call<YMUserPaymentResponse> call, Response<YMUserPaymentResponse> response) {
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.body() != null) {

                    String result = response.body().getResult();

                    if(result.equalsIgnoreCase("true")){
                        //paymentList.addAll(response.body().getData());
                        datalist.addAll(response.body().getData());
                        historyAdapter.notifyDataSetChanged();
                    }
                    else {
                        switch (response.body().getMessage()){
                            case YMConst.MSG_INVALID_TOKEN:
//                                Toast.makeText(getContext(), getResources().getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                                break;
                            default:
//                                Toast.makeText(getContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<YMUserPaymentResponse> call, Throwable t) {
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
//                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * */
    private void getMinuteHistory(){
        if (!Utils.getInstance(getActivity()).isOnline()) {
            Toast.makeText(getActivity(), "Please check network state.", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();

        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserMinuteResponse> call = service.getMinuteHistory(AGApplication.the().getMe().getId(), AGApplication.the().getMe().getAppToken());

        call.enqueue(new Callback<YMUserMinuteResponse>() {
            @Override
            public void onResponse(Call<YMUserMinuteResponse> call, Response<YMUserMinuteResponse> response) {
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.body() != null) {

                    String result = response.body().getResult();

                    if(result.equalsIgnoreCase("true")){
//                        minuteList.addAll(response.body().getData().getCalls());
//                        minuteList.addAll(response.body().getData().getLives());
                        datalist.addAll(response.body().getData().getCalls());
                        datalist.addAll(response.body().getData().getLives());
                        historyAdapter.notifyDataSetChanged();
                    }
                    else {
                        switch (response.body().getMessage()){
                            case YMConst.MSG_INVALID_TOKEN:
//                                Toast.makeText(getContext(), getResources().getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                                break;
                            default:
//                                Toast.makeText(getContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<YMUserMinuteResponse> call, Throwable t) {
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
//                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


    }


}