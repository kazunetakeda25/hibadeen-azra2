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
import com.azra2.model.YMUserCall;
import com.azra2.model.YMUserLive;
import com.azra2.model.YMUserMinute;
import com.azra2.model.YMUserMinuteResponse;
import com.azra2.model.YMUserPayment;
import com.azra2.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MinuteHistoryFragment extends Fragment {

    private RecyclerView rvHistory;
    private ArrayList<YMUserCall> calldata = new ArrayList<>();
    private ArrayList<YMUserLive> livedata = new ArrayList<>();
    private MinuteHistoryAdapter historyAdapter;
    private ProgressDialog progressDialog;

    public MinuteHistoryFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_minute_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceId){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");

        this.rvHistory = view.findViewById(R.id.rvMinuteHistory);
        this.rvHistory.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvHistory.setLayoutManager(layoutManager);

        getMinuteHistory();
        this.historyAdapter = new MinuteHistoryAdapter(getContext(), this.calldata, this.livedata);
        this.rvHistory.setAdapter(historyAdapter);
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
                if (response.body() != null) {

                    String result = response.body().getResult();

                    if(result.equalsIgnoreCase("true")){
//                        minuteList.addAll(response.body().getData().getCalls());
//                        minuteList.addAll(response.body().getData().getLives());
                        calldata.addAll(response.body().getData().getCalls());
                        livedata.addAll(response.body().getData().getLives());
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
//                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}