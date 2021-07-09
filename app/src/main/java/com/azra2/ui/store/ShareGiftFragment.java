package com.azra2.ui.store;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMAgent;
import com.azra2.model.YMAgentList;
import com.azra2.model.YMResponse;
import com.azra2.model.YMUser;
import com.azra2.model.YMUserResponse;
import com.azra2.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShareGiftFragment extends Fragment implements SimpleAgentViewHolder.OnItemClickListener , SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView rvAgentList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public View root;

    private TextView tvSort;
    private TextView tvWorth;
    private CardView cvContinue;

    private SimpleAgentAdapter adapter;
    private ArrayList<YMAgent> agentList = new ArrayList<>();

    private ProgressDialog progressDialog;
    private YMAgent selectedAgent;


    public ShareGiftFragment() {
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
        root = inflater.inflate(R.layout.fragment_share_gift, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle){
        ShareGiftFragmentArgs args = ShareGiftFragmentArgs.fromBundle(getArguments());

        this.tvSort = view.findViewById(R.id.tvSort);
        this.tvWorth = view.findViewById(R.id.tvWorth);
        this.cvContinue = view.findViewById(R.id.cvContinue);

        this.tvSort.setText(args.getSort());
        if(args.getSort().equalsIgnoreCase(getContext().getResources().getString(R.string.coin_gift))){
            this.tvWorth.setText(args.getCoinCount() + " coins");

            cvContinue.setOnClickListener(v -> {
                shareCoinGift(Integer.parseInt(args.getCoinCount()));
            });
        }
        else if(args.getSort().equalsIgnoreCase(getContext().getResources().getString(R.string.money_gift)))   {
            this.tvWorth.setText("$ " + args.getMoneyAmount());
        }



        rvAgentList = view.findViewById(R.id.rvAgentList);
        adapter = new SimpleAgentAdapter(getActivity(), ShareGiftFragment.this, agentList );
        rvAgentList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvAgentList.setLayoutManager(layoutManager);
        rvAgentList.setAdapter(adapter);

        mSwipeRefreshLayout = view.findViewById(R.id.slAgents);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....");
    }

    @Override
    public void onItemClicked(View view, int position) {
        for(int i=0; i<adapter.getItemCount();i++){
            if(agentList.get(i).selected == true){
                agentList.get(i).selected = false;
                adapter.notifyItemChanged(i);
            }
        }
        agentList.get(position).selected = true;
        this.selectedAgent = agentList.get(position);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchAgentFromServer(false);
    }

    @Override
    public void onRefresh() {
        fetchAgentFromServer(false);
    }

    private void fetchAgentFromServer(boolean isUserRequired) {
        if (!Utils.getInstance(getActivity()).isOnline()) {
            Toast.makeText(getActivity(), "Please check network state.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMAgentList> call;
        if (isUserRequired) {
            call = service.getAgentsIgnoreRtype(15, 0, AGApplication.the().getMe().getAppToken());
        }
        else {
            call = service.getAgentsIgnoreRtype(15, adapter.getItemCount(), AGApplication.the().getMe().getAppToken());
        }

        call.enqueue(new Callback<YMAgentList>() {
            @Override
            public void onResponse(Call<YMAgentList> call, Response<YMAgentList> response) {
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.body() != null) {
                    if (isUserRequired) {
                        agentList.clear();
                    }
                    String result = response.body().getResult();

                    if(result.equalsIgnoreCase("true")){
                        agentList.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }
                    else {
//                        Toast.makeText(getContext(), "There is no results to show more", Toast.LENGTH_LONG).show();
                    };
                }
            }

            @Override
            public void onFailure(Call<YMAgentList> call, Throwable t) {
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Connecting to backend failed...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void shareCoinGift(int coinCount){
        if(this.selectedAgent == null){
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getResources().getString(R.string.message_wait));
        progressDialog.show();
        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserResponse> call = service.shareCoinGift(AGApplication.the().getMe().getId(), selectedAgent.getId(),
                coinCount, AGApplication.the().getMe().getAppToken());

        call.enqueue(new Callback<YMUserResponse>() {
            @Override
            public void onResponse(Call<YMUserResponse> call, Response<YMUserResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {
                    YMUserResponse response1 = response.body();
                    if (response1.getResult().equalsIgnoreCase("false")) {
                        switch (response.body().getMessage()){
                            case YMConst.MSG_INVALID_TOKEN:
                                Toast.makeText(getContext(), getResources().getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }else {
                        YMUser user = response1.getData();
                        AGApplication.the().setMe(user);
                        showGiftSentDlg(coinCount);
                    }
                }
            }

            @Override
            public void onFailure(Call<YMUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showGiftSentDlg(int coinCount){
        final Dialog dlgShareGift = new Dialog(getContext());
        dlgShareGift.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlgShareGift.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgShareGift.setCancelable(false);
        dlgShareGift.setContentView(R.layout.dialog_gift_sent);
        Window window = dlgShareGift.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView closeBtn = dlgShareGift.findViewById(R.id.ivClose);
        TextView tvYouSent = dlgShareGift.findViewById(R.id.tvYouSent);
        TextView tvGiftTo = dlgShareGift.findViewById(R.id.tvGiftTo);
        TextView tvSendAnother = dlgShareGift.findViewById(R.id.tvSendAnotherGift);

        tvYouSent.setText(tvYouSent.getText().toString() + " " + coinCount + " coins");
        tvGiftTo.setText(tvGiftTo.getText() + " "+ selectedAgent.getNickName());

        closeBtn.setOnClickListener(v -> {
            dlgShareGift.dismiss();
        });

        tvSendAnother.setOnClickListener(v -> {
            dlgShareGift.dismiss();
            NavController navController = Navigation.findNavController(root);
            NavDirections directions = ShareGiftFragmentDirections.actionShareGiftFragmentToBuyGiftsFragment();
            navController.navigate(directions);
        });

        dlgShareGift.show();
    }


}