package com.azra2.ui.user;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMAgent;
import com.azra2.model.YMResponse;
import com.azra2.model.YMUser;
import com.azra2.ui.MainActivity;
import com.azra2.ui.mainfrag.ChooseRoomFragmentDirections;
import com.azra2.ui.room.AgentListFragment;
import com.azra2.ui.room.AgentListFragmentDirections;
import com.azra2.ui.room.VideoBroadcastingActivity;
import com.azra2.ui.room.VideoChatViewActivity;
import com.azra2.utils.TinyDB;
import com.azra2.utils.Utils;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentAdapter extends RecyclerView.Adapter<AgentViewHolder> {

    private ArrayList<YMAgent> dataList;
    private MainActivity mainActivity;
    private AgentViewHolder.OnItemClickListener listener;
    private AgentListFragment parentFragment;
    private ProgressDialog progressDialog;

    public AgentAdapter(Activity baseActivity, AgentListFragment parentFragment, ArrayList<YMAgent> dataList){
        this.mainActivity = (MainActivity) baseActivity;
        this.parentFragment =  parentFragment;
        this.dataList = dataList;
        this.listener = (AgentViewHolder.OnItemClickListener)parentFragment;
        this.progressDialog = new ProgressDialog(mainActivity);

    }

    @NonNull
    @Override
    public AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_agent, parent, false);
        return new AgentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewHolder holder, int position) {

        YMAgent agent = dataList.get(position);

        Picasso.Builder builder = new Picasso.Builder(mainActivity);
        builder.downloader(new OkHttp3Downloader(mainActivity));
        if (!Utils.isNullOrEmpty(agent.getProfileImage())) {
            builder.build().load( agent.getProfileImage())
                    .placeholder((R.mipmap.ic_launcher_round))
                    .error(R.mipmap.ic_launcher_round)
                    .into(holder.ivProfile);
        }
        /*if (!Utils.isNullOrEmpty(agent.getProfileImage())) {
            builder.build().load(YMRetrofit.IMAGE_URL + agent.getProfileImage())
                    .placeholder((R.mipmap.ic_launcher_round))
                    .error(R.mipmap.ic_launcher_round)
                    .into(holder.ivProfile);
        }*/

        //set UI according to status
        if(agent.getRoomType() == YMConst.RT_PUBLIC){ //public room
            holder.ivStatus.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.online_dot));
            holder.tvStatus.setText(R.string.live);
            holder.tvStatus.setTextColor(mainActivity.getResources().getColor(R.color.onlineColor));
            holder.ivStatus2.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.online_dot));
            holder.tvName.setText(agent.getNickName());
            holder.llAction.setBackground(mainActivity.getResources().getDrawable(R.drawable.purple_oval_gradient));
            holder.ivAction.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.camera_on));
            holder.tvAction.setText(R.string.join_me);
        }
        else{ //private or adult
            switch (agent.getStatus()){
                case YMConst.STATUS_OFFLINE: //offline
                    holder.ivStatus.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.offline_dot));
                    holder.tvStatus.setText(R.string.offline);
                    holder.tvStatus.setTextColor(mainActivity.getResources().getColor(R.color.offlineColor));
                    holder.ivStatus2.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.offline_dot));
                    holder.tvName.setText(agent.getNickName());
                    holder.llAction.setBackground(mainActivity.getResources().getDrawable(R.drawable.black_oval_gradient));
//                holder.llAction.setBackground(ContextCompat.getDrawable(context,R.drawable.purple_oval_gradient));
                    holder.ivAction.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.notifications));
                    holder.tvAction.setText(R.string.notify_me);
                    break;

                case YMConst.STATUS_ONLINE: //online
                    holder.ivStatus.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.online_dot));
                    holder.tvStatus.setText(R.string.online);
                    holder.tvStatus.setTextColor(mainActivity.getResources().getColor(R.color.onlineColor));
                    holder.ivStatus2.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.online_dot));
                    holder.tvName.setText(agent.getNickName());
                    holder.llAction.setBackground(mainActivity.getResources().getDrawable(R.drawable.purple_oval_gradient));
                    holder.ivAction.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.phone_24));
                    holder.tvAction.setText(R.string.call_me);
                    break;

                case YMConst.STATUS_BUSY: //busy
                    holder.ivStatus.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.busy_dot));
                    holder.tvStatus.setText(R.string.busy);
                    holder.tvStatus.setTextColor(mainActivity.getResources().getColor(R.color.busyColor));
                    holder.ivStatus2.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.busy_dot));
                    holder.tvName.setText(agent.getNickName());
                    holder.llAction.setBackground(mainActivity.getResources().getDrawable(R.drawable.black_oval_gradient));
                    holder.ivAction.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.notifications));
                    holder.tvAction.setText(R.string.notify_me);
                    break;
                default:
            }
        }

        //
        switch (agent.getCallType()){
            case YMConst.CT_VOICE: //voicecall
                holder.ivCallType.setImageDrawable(mainActivity.getDrawable(R.drawable.phone_24_green));
                break;
            case YMConst.CT_VIDEO: //videocall
                holder.ivCallType.setImageDrawable(mainActivity.getDrawable(R.drawable.camera_on_green));
                break;
        }

        holder.tvPrice.setText(agent.getPerformancePrice() + "");

        holder.countryCodePicker.setCountryForPhoneCode(Integer.parseInt(agent.getCountry()));

        //
        holder.itemView.setOnClickListener(v -> listener.onItemClicked(v, position));

        //
        holder.llAction.setOnClickListener(v -> {
            if(AGApplication.the().getMe().getCoinBalance() < agent.getPerformancePrice()) {
                Toast.makeText(mainActivity, R.string.low_balance, Toast.LENGTH_LONG).show();
                return;
            }
            AGApplication.the().setSelectedAgent(agent);
            int agentId = agent.getId();
            String agentName = agent.getNickName();

            int userId = AGApplication.the().getMe().getId() ;

            if(holder.tvAction.getText().toString().equalsIgnoreCase(mainActivity.getResources().getText(R.string.join_me).toString())){
                Intent intentPublic = new Intent(mainActivity, VideoBroadcastingActivity.class);
                mainActivity.startActivity(intentPublic);
            }

            if( holder.tvAction.getText().toString().equalsIgnoreCase(mainActivity.getResources().getText(R.string.call_me).toString()) ){
                String agUid = String.valueOf( 2* userId );
                String agAid = String.valueOf( 2* agentId + 1 );
                String channelName = agUid + "_" + agAid;
                String userName = AGApplication.the().getMe().getNickName();
                mainActivity.gotoCallingInterface(agAid, userName, channelName, YMConst.ROLE_CALLER);
            }

            if(holder.tvAction.getText().toString().equalsIgnoreCase(mainActivity.getResources().getText(R.string.notify_me).toString())){
                progressDialog.show();
                YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
                Call<YMResponse> call = service.notifyAgent(userId, agentId, agent.getRoomType(), AGApplication.the().getMe().getAppToken());
                call.enqueue(new Callback<YMResponse>() {
                    @Override
                    public void onResponse(Call<YMResponse> call, Response<YMResponse> response) {
                        progressDialog.dismiss();
                        if (response.body() != null) {
                            YMResponse response1 = response.body();
                            if (response1.getResult().equalsIgnoreCase("false")) {
                                switch (response.body().getMessage()){
                                    case YMConst.MSG_INVALID_TOKEN:
                                        Toast.makeText(mainActivity, mainActivity.getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(mainActivity, mainActivity.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                            else {
                                showNotificationSentDlg(agentName);
//                                Toast.makeText(context, "Notification sent to " , Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<YMResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(parentFragment.getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });

        //
        holder.llProfile.setOnClickListener(v -> {
            AGApplication.the().setSelectedAgent(agent);
            NavController navController = Navigation.findNavController(parentFragment.root);
            NavDirections action = AgentListFragmentDirections.actionAgentListFragmentToAgentProfileFragment();
            navController.navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void showNotificationSentDlg(String agentName){
        final Dialog dlgNotfSent = new Dialog(mainActivity);
        dlgNotfSent.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlgNotfSent.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgNotfSent.setCancelable(false);
        dlgNotfSent.setContentView(R.layout.dialog_notification_sent);
        Window window = dlgNotfSent.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView closeBtn = dlgNotfSent.findViewById(R.id.ivClose);
        TextView tvAgentName = dlgNotfSent.findViewById(R.id.tvAgentName);
        tvAgentName.setText(agentName);

        closeBtn.setOnClickListener(v -> {
            dlgNotfSent.dismiss();
        });

        dlgNotfSent.show();
    }




}

