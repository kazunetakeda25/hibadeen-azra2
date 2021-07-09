package com.azra2.ui.profile;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMUserPayment;
import com.azra2.model.YMUserPaymentResponse;
import com.azra2.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentHistoryFragment extends Fragment {

    private RecyclerView rvHistory;
    private ArrayList<YMUserPayment> datalist = new ArrayList<>();
    private PaymentHistoryAdapter historyAdapter;
    private ProgressDialog progressDialog;


    public PaymentHistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceId){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");

        this.rvHistory = view.findViewById(R.id.rvPaymentHistory);
        this.rvHistory.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvHistory.setLayoutManager(layoutManager);

        getPaymentHistory();
        this.historyAdapter = new PaymentHistoryAdapter(getContext(), this.datalist);
        this.rvHistory.setAdapter(historyAdapter);

    }

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
//                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


    }

}