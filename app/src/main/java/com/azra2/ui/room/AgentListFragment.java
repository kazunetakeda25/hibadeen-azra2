package com.azra2.ui.room;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.azra2.ui.user.AgentAdapter;
import com.azra2.ui.user.AgentViewHolder;
import com.azra2.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentListFragment extends Fragment implements AgentViewHolder.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView rvAgentList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public View root;

    private AgentAdapter agentAdapter;
    private ArrayList<YMAgent> agentList = new ArrayList<>();

    private ProgressDialog progressDialog;

    int roomType = -1;

    static int limit = 15;
    static int offset = 0;
    static int times = 0;

    public AgentListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_agent_list, container, false);
        initUI(root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        AgentListFragmentArgs args = AgentListFragmentArgs.fromBundle(getArguments());
        this.roomType  = args.getRoomType();

        rvAgentList = view.findViewById(R.id.rvAgentList);
        agentAdapter = new AgentAdapter(getActivity(), AgentListFragment.this, agentList);
        rvAgentList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvAgentList.setLayoutManager(layoutManager);
        rvAgentList.setAdapter(agentAdapter);

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
    public void onResume() {
        super.onResume();
        fetchAgentFromServer(false);
    }

    @Override
    public void onRefresh() {
        fetchAgentFromServer(false);
    }

    @Override
    public void onItemClicked(View view, int position) {

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
            call = service.getAgents(limit, 0, roomType, AGApplication.the().getMe().getAppToken());
        }
        else {
            offset = agentAdapter.getItemCount();
            call = service.getAgents(limit, offset, roomType, AGApplication.the().getMe().getAppToken());
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
                        agentAdapter.notifyDataSetChanged();
                    }
                    else {
                        switch (response.body().getMessage()){
                            case YMConst.MSG_INVALID_TOKEN:
                                Toast.makeText(getContext(), getResources().getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    };
                }
            }

            @Override
            public void onFailure(Call<YMAgentList> call, Throwable t) {
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initUI(View view) {

    }

    public int getRoomType(){
        return roomType;
    }

//    public int getCallType(){
//        return callType;
//    }


}