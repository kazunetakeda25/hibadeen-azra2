package com.azra2.ui.mainfrag;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.azra2.model.YMUser;
import com.azra2.utils.TinyDB;
import com.azra2.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rvNotifications;
    private NotificationAdapter adapter;
    private ArrayList<YMNotification> datalist = new ArrayList<>();

    private ProgressDialog progressDialog;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        initUI(root);
        return root;
    }

    private void initUI(View view) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState){

        datalist.add(new YMNotification());
        datalist.add(new YMNotification());

        rvNotifications = view.findViewById(R.id.rvNotifications);
        rvNotifications.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvNotifications.setLayoutManager(layoutManager);

        adapter = new NotificationAdapter(getContext(), datalist);
        rvNotifications.setAdapter(adapter);

        mSwipeRefreshLayout = view.findViewById(R.id.slNotifications);
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
        fetchNotificationFromServer();
    }

    @Override
    public void onRefresh() {
        fetchNotificationFromServer();
    }

    private void fetchNotificationFromServer(){
        if (!Utils.getInstance(getActivity()).isOnline()) {
            Toast.makeText(getActivity(), "Please check network state.", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();

        int uid = 0;
//        TinyDB tinyDB = new TinyDB(getContext());
//        YMUser currentUser = tinyDB.getObject(YMConst.KEY_CURRENT_USER, YMUser.class);
        uid = AGApplication.the().getMe().getId();

        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMNotificationList> call = service.getUserNotifications(uid);

        call.enqueue(new Callback<YMNotificationList>() {
            @Override
            public void onResponse(Call<YMNotificationList> call, Response<YMNotificationList> response) {
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                if (response.body() != null) {

                    String result = response.body().getResult();

                    if(result.equalsIgnoreCase("true")){
                        datalist.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
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
            public void onFailure(Call<YMNotificationList> call, Throwable t) {
                progressDialog.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
//                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }



}
