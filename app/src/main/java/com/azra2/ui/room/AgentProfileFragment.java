package com.azra2.ui.room;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMAgent;
import com.azra2.model.YMImageInfo;
import com.azra2.model.YMResponse;
import com.azra2.ui.MainActivity;
import com.azra2.utils.Utils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentProfileFragment extends Fragment {
    YMAgent selectedAgent;
    View root;
    NavController navController;
    MainActivity mainActivity;

    ImageView ivProfile, ivCloseBtn,ivStatus, ivStatus2, ivAction, ivPlayBtn;
    TextView tvStatus, tvName, tvCountryName, tvAction, tvTipBtn, tvTipMeBtn, tvReviewBtn, tvAboutMe;
    CountryCodePicker countryCodePicker;
    RatingBar rbStar;
    LinearLayout llAction;
    RecyclerView rvGallery;
    RectangleImageAdapter imageAdapter;
    List<YMImageInfo> datalist = new ArrayList<>();
    ChipGroup chipGroup;
    ArrayList<String> tagList = new ArrayList<>();
    MediaPlayer mediaPlayer;


    public AgentProfileFragment() {
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
        root = inflater.inflate(R.layout.fragment_agent_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        initField();
        initUi();
        initTags();
    }


    private void initField(){
        this.selectedAgent = AGApplication.the().getSelectedAgent();
        this.navController = Navigation.findNavController(root);
        this.mainActivity = (MainActivity) getActivity();
        datalist.addAll(selectedAgent.getGallery());
        String str = selectedAgent.getInterests();
        String[] interests = str.split(",");
        for(int i = 0; i< interests.length; i++){
            tagList.add(interests[i]);
        }

    }

    private void initUi(){
        ivProfile = root.findViewById(R.id.ivProfile);
        ivCloseBtn = root.findViewById(R.id.ivCloseBtn);
        ivStatus = root.findViewById(R.id.ivStatus);
        ivStatus2 = root.findViewById(R.id.ivStatus2);
        ivAction = root.findViewById(R.id.ivAction);
        ivPlayBtn = root.findViewById(R.id.ivPlayBtn);
        tvStatus = root.findViewById(R.id.tvStatus);
        tvName = root.findViewById(R.id.tvName);
        tvCountryName = root.findViewById(R.id.tvCountryName);
        tvAction = root.findViewById(R.id.tvAction);
        tvTipBtn = root.findViewById(R.id.tvTipBtn);
        tvTipMeBtn = root.findViewById(R.id.tvTipMeBtn);
        tvReviewBtn = root.findViewById(R.id.tvReviewBtn);
        tvAboutMe = root.findViewById(R.id.tvAboutMe);
        countryCodePicker = root.findViewById(R.id.ccp);
        rbStar = root.findViewById(R.id.rbStar);
        llAction = root.findViewById(R.id.llAction);
        rvGallery = root.findViewById(R.id.rvGallery);
        chipGroup = root.findViewById(R.id.tag_group);

        Picasso.Builder builder = new Picasso.Builder(getActivity());
        builder.downloader(new OkHttp3Downloader(getActivity()));
        if(!Utils.isNullOrEmpty(selectedAgent.getProfileImage())){
            builder.build().load(selectedAgent.getProfileImage())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(ivProfile);
        }

        ivCloseBtn.setOnClickListener(v -> {
            NavDirections directions = AgentProfileFragmentDirections.actionAgentProfileFragmentToAgentListFragment(selectedAgent.getRoomType());
            navController.navigate(directions);
        });

        countryCodePicker.setCountryForPhoneCode(Integer.parseInt(selectedAgent.getCountry()));
        rbStar.setRating(selectedAgent.getReviewStar().floatValue());
        tvCountryName.setText(countryCodePicker.getSelectedCountryName());

        //set UI according to status
        if(selectedAgent.getRoomType() == YMConst.RT_PUBLIC){ //public room
            ivStatus.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.online_dot));
            tvStatus.setText(R.string.live);
            tvStatus.setTextColor(getActivity().getResources().getColor(R.color.onlineColor));
            ivStatus2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.online_dot));
            tvName.setText(selectedAgent.getNickName());
            llAction.setBackground(getActivity().getResources().getDrawable(R.drawable.purple_oval_gradient));
            ivAction.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.camera_on));
            tvAction.setText(R.string.join_me);
        }
        else{ //private or adult
            switch (selectedAgent.getStatus()){
                case YMConst.STATUS_OFFLINE: //offline
                    ivStatus.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.offline_dot));
                    tvStatus.setText(R.string.offline);
                    tvStatus.setTextColor(getActivity().getResources().getColor(R.color.offlineColor));
                    ivStatus2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.offline_dot));
                    tvName.setText(selectedAgent.getNickName());
                    llAction.setBackground(getActivity().getResources().getDrawable(R.drawable.black_oval_gradient));
                    ivAction.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.notifications));
                    tvAction.setText(R.string.notify_me);
                    break;

                case YMConst.STATUS_ONLINE: //online
                    ivStatus.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.online_dot));
                    tvStatus.setText(R.string.online);
                    tvStatus.setTextColor(getActivity().getResources().getColor(R.color.onlineColor));
                    ivStatus2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.online_dot));
                    tvName.setText(selectedAgent.getNickName());
                    llAction.setBackground(getActivity().getResources().getDrawable(R.drawable.purple_oval_gradient));
                    ivAction.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.phone_24));
                    tvAction.setText(R.string.call_me);
                    break;

                case YMConst.STATUS_BUSY: //busy
                    ivStatus.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.busy_dot));
                    tvStatus.setText(R.string.busy);
                    tvStatus.setTextColor(getActivity().getResources().getColor(R.color.busyColor));
                    ivStatus2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.busy_dot));
                    tvName.setText(selectedAgent.getNickName());
                    llAction.setBackground(getActivity().getResources().getDrawable(R.drawable.black_oval_gradient));
                    ivAction.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.notifications));
                    tvAction.setText(R.string.notify_me);
                    break;
                default:
            }
        }

        //
        llAction.setOnClickListener(v -> {
            AGApplication.the().setSelectedAgent(selectedAgent);
            int agentId = selectedAgent.getId();
            String agentName = selectedAgent.getNickName();

            int userId = AGApplication.the().getMe().getId() ;

            if(tvAction.getText().toString().equalsIgnoreCase(mainActivity.getResources().getText(R.string.join_me).toString())){
                Intent intentPublic = new Intent(mainActivity, VideoBroadcastingActivity.class);
                mainActivity.startActivity(intentPublic);
            }

            if( tvAction.getText().toString().equalsIgnoreCase(mainActivity.getResources().getText(R.string.call_me).toString()) ){
                String agUid = String.valueOf( 2* userId );
                String agAid = String.valueOf( 2* agentId + 1 );
                String channelName = agUid + "_" + agAid;
                String userName = AGApplication.the().getMe().getNickName();
                mainActivity.gotoCallingInterface(agAid, userName, channelName, YMConst.ROLE_CALLER);
            }

            if(tvAction.getText().toString().equalsIgnoreCase(mainActivity.getResources().getText(R.string.notify_me).toString())){
                YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
                Call<YMResponse> call = service.notifyAgent(userId, agentId, selectedAgent.getRoomType(),
                        AGApplication.the().getMe().getAppToken());
                call.enqueue(new Callback<YMResponse>() {
                    @Override
                    public void onResponse(Call<YMResponse> call, Response<YMResponse> response) {
                        if (response.body() != null) {
                            YMResponse response1 = response.body();
                            if (response1.getResult().equalsIgnoreCase("false")) {
                                switch (response.body().getMessage()){
                                    case YMConst.MSG_INVALID_TOKEN:
                                        Toast.makeText(getContext(), getResources().getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });

        //set gallery
        imageAdapter = new RectangleImageAdapter(getContext(), datalist);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        rvGallery.setLayoutManager(layoutManager);
        rvGallery.setAdapter(imageAdapter);

        //
        tvAboutMe.setText(selectedAgent.getAbout());

        tvReviewBtn.setOnClickListener(v -> {
            NavDirections action = AgentProfileFragmentDirections.actionAgentProfileFragmentToReviewFragment();
            navController.navigate(action);
        });

        tvTipBtn.setOnClickListener(v -> {

        });
        tvTipMeBtn.setOnClickListener(v -> {

        });

        ivPlayBtn.setOnClickListener(v -> {
            try {
                playAudio(getContext(), selectedAgent.getAudio());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void initTags(){
        for (int index = 0; index < tagList.size(); index++) {
            String tagName = tagList.get(index);
            Chip chip = new Chip(getContext());
            setChip(chip,tagName, index);

            chipGroup.addView(chip);
        }
    }

    private void setChip(Chip chip, String tagName, int index){
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setText(tagName);
        chip.setTextColor(getResources().getColor(R.color.white));
        int colorIndex = index % 6;
        chip.setChipBackgroundColorResource(YMConst.CHIP_COLOR_ARRAY[colorIndex]);
//        chip.setCloseIconResource(R.drawable.close_24);
//        chip.setCloseIconTintResource(R.color.white);
        chip.setCloseIconVisible(false);
        //Added click listener on close icon to remove tag from ChipGroup
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagList.remove(tagName);
                chipGroup.removeView(chip);
            }
        });
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

    public void playAudio(final Context context, final String url) throws Exception {
        if (mediaPlayer == null) {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(context,Uri.parse(url));
                mediaPlayer.prepare();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        if(mediaPlayer != null){
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    killMediaPlayer();
                }
            });
            mediaPlayer.start();
        }

    }

    private void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}