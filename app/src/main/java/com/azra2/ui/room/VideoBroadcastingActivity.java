package com.azra2.ui.room;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.ChatManager;
import com.azra2.model.MessageAdapter;
import com.azra2.model.MessageBean;
import com.azra2.model.YMResponse;
import com.azra2.model.YMUser;
import com.azra2.utils.ImageUtil;
import com.azra2.utils.MessageUtil;
import com.azra2.utils.TinyDB;
import com.azra2.utils.Utils;
import com.bumptech.glide.Glide;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmMessageType;
import io.agora.rtm.RtmStatusCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoBroadcastingActivity extends BaseCallActivity {

    private static final String TAG = VideoBroadcastingActivity.class.getSimpleName();
    private static final String HEART_ANIMATION = "heart_animation_123";

    private static final int PERMISSION_REQ_ID = 22;

    // Ask for Android device permissions at runtime.
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private boolean isHidden = false;

    private YMUser currentUser;

    private String mUserName = "";
    private boolean isCallEnd = false;

//    private int mPeerId = 0;

    private RtcEngine mRtcEngine;
    // Create a SurfaceView object for local video
    private FrameLayout mLocalContainer;
    private SurfaceView mLocalView;

    // Create a SurfaceView object for remote video
    private RelativeLayout mRemoteContainer;
    private SurfaceView mRemoteView;

    private ProgressDialog progressDialog;
    private EditText mMsgEditText;
    private ImageView mBigImage;
    private RecyclerView mRecyclerView;
    private TextView tvName, tvShowHide;
    private ImageView ivChatProfile, ivChevronUp, ivChevronDown;
    private List<MessageBean> mMessageBeanList = new ArrayList<>();
    private MessageAdapter mMessageAdapter;

    private String mChannelName = "";
    private int mChannelMemberCount = 1;

//    private RtmClientListener mClientListener;
    private RtmChannel mRtmChannel;

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        // Listen for the onJoinChannelSuccess callback.
        // This callback occurs when the local user successfully joins the channel.
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("AGORA: ","Succeeded to join Rtc channel, uid: " + (uid & 0xFFFFFFFFL));
                }
            });
        }

        @Override
        // Listen for the onFirstRemoteVideoDecoded callback.
        // This callback occurs when the first video frame of the host is received and decoded after the host successfully joins the channel.
        // You can call the setupRemoteVideo method in this callback to set up the remote video view.
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                    setupRemoteVideo(uid);
                    joinLive();
                }
            });
        }

        @Override
        // Listen for the onUserOffline callback.
        // This callback occurs when the host leaves the channel or drops offline.
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("AGORA: ","User offline, uid: " + (uid & 0xFFFFFFFFL));
                    Toast.makeText(VideoBroadcastingActivity.this, " Livestreaming finished.", Toast.LENGTH_SHORT).show();
                    isCallEnd = true;
                    leaveLive();
                    endCall();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_broadcasting);
        this.selectedAgent = AGApplication.the().getSelectedAgent();

        initFields();
        initUI();

        progressDialog.show();
        // If all the permissions are granted, initialize the RtcEngine object and join a channel.
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initRtm();
            doRtmLogin();
            initRtcEngineAndJoinChannel();
            createAndJoinRtmChannel();
        }
    }

    @Override
    public void onSuccess(Void aVoid) {
        Log.d(TAG, "Succeeded to join Rtm channel.");
        progressDialog.dismiss();
    }

    @Override
    public void onFailure(ErrorInfo errorInfo) {
        Log.e(TAG, "Failed to join Rtm channel: \n" + errorInfo.toString());
    }

    private void initFields(){
//        TinyDB tinyDB = new TinyDB(getApplicationContext());
//        this.currentUser = tinyDB.getObject(YMConst.KEY_CURRENT_USER, YMUser.class);
        this.currentUser = AGApplication.the().getMe();
        this.agUid = currentUser.getId() * 2; // id * 2
        this.mUserName = currentUser.getNickName();

        this.agAid = AGApplication.the().getSelectedAgent().getId() * 2 + 1; //defaultValue?// id * 2 + 1
        this.mChannelName = "_" + agAid;  //for broadcasting
    }

    private void initUI() {
//        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);

        mMsgEditText = findViewById(R.id.message_edittiext);
//        mBigImage = findViewById(R.id.big_image);
        tvShowHide = findViewById(R.id.tvShowHide);
        ivChevronDown = findViewById(R.id.ivChevronDown);
        ivChevronUp = findViewById(R.id.ivChevronUp);
        ivChatProfile = findViewById(R.id.ivChatProfile);
        tvName = findViewById(R.id.tvName);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this));
        String profileImage = AGApplication.the().getSelectedAgent().getProfileImage();
        if(!Utils.isNullOrEmpty(profileImage)){
            builder.build().load(profileImage)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(ivChatProfile);
        }
        tvName.setText(AGApplication.the().getSelectedAgent().getNickName());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mMessageAdapter = new MessageAdapter(this, mMessageBeanList, message -> {
            if (message.getMessage().getMessageType() == RtmMessageType.IMAGE) {
                if (!TextUtils.isEmpty(message.getCacheFile())) {
                    Glide.with(this).load(message.getCacheFile()).into(mBigImage);
                    mBigImage.setVisibility(View.VISIBLE);
                } else {
                    ImageUtil.cacheImage(this, mRtmClient, (RtmImageMessage) message.getMessage(), new ResultCallback<String>() {
                        @Override
                        public void onSuccess(String file) {
                            message.setCacheFile(file);
                            runOnUiThread(() -> {
                                Glide.with(VideoBroadcastingActivity.this).load(file).into(mBigImage);
                                mBigImage.setVisibility(View.VISIBLE);
                            });
                        }

                        @Override
                        public void onFailure(ErrorInfo errorInfo) {

                        }
                    });
                }
            }
        });
        mRecyclerView = findViewById(R.id.message_list);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMessageAdapter);
//        mCallBtn = findViewById(R.id.btn_call);
//        mMuteBtn = findViewById(R.id.btn_mute);
//        mSwitchCameraBtn = findViewById(R.id.btn_switch_camera);

//        mLogView = findViewById(R.id.log_recycler_view);

    }

    /*************************   Agora RTC for video broadcasting   *****************************/

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    private void initRtcEngineAndJoinChannel(){
        initializeRtcEngine();
        setRtcChannelProfile();
        setRtcClientRole();
        setupRtcVideoConfig();
//        setupLocalVideo();
        joinRtcChannel();
    }

    // Initialize the RtcEngine object.
    private void initializeRtcEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setRtcChannelProfile() {
        mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
    }

    private void setRtcClientRole() {
        int clientRole = Constants.CLIENT_ROLE_AUDIENCE;
        mRtcEngine.setClientRole(clientRole);
    }

    private void setupRtcVideoConfig() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        mRtcEngine.enableVideo();

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupRtcLocalVideo() {

        mLocalView = RtcEngine.CreateRendererView(getBaseContext());
        mLocalView.setZOrderMediaOverlay(true);
        mLocalContainer.addView(mLocalView);
        // Set the local video view.
        VideoCanvas localVideoCanvas = new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        mRtcEngine.setupLocalVideo(localVideoCanvas);
    }

    private void joinRtcChannel() {

        // For SDKs earlier than v3.0.0, call this method to enable interoperability between the Native SDK and the Web SDK if the Web SDK is in the channel. As of v3.0.0, the Native SDK enables the interoperability with the Web SDK by default.
//        rtcEngine.enableWebSdkInteroperability(true);

        String token = getString(R.string.agora_access_token);
        if (TextUtils.isEmpty(token)) {
            token = null; // default, no token
        }
        mRtcEngine.joinChannel(token, mChannelName, "", agUid);

        BeautyOptions BEAUTY_OPTIONS = new BeautyOptions();
        mRtcEngine.setBeautyEffectOptions(true, BEAUTY_OPTIONS);
    }

    private void setupRemoteVideo(int uid) {
        mRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        mRemoteContainer.addView(mRemoteView);
        // Set the remote video view.
        mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isCallEnd) { //when press back button
            leaveLive();
            endCall();
        }
        RtcEngine.destroy();
    }

    private void leaveRtcChannel() {
        // Leave the current channel.
        mRtcEngine.leaveChannel();
    }


    private void removeRemoteVideo() {
        if (mRemoteView != null) {
            mRemoteContainer.removeView(mRemoteView);
        }
        // Destroys remote view
        mRemoteView = null;
    }

    private void joinLive(){
        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMResponse> call = service.joinLive( AGApplication.the().getMe().getId(),selectedAgent.getId(),
                AGApplication.the().getMe().getAppToken());

        ProgressDialog progressDialog = new ProgressDialog(VideoBroadcastingActivity.this);
        progressDialog.setMessage("");
        call.enqueue(new Callback<YMResponse>() {
            @Override
            public void onResponse(Call<YMResponse> call, Response<YMResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {
                    YMResponse response1 = response.body();
                    if (response1.getResult().equalsIgnoreCase("false")) {
                        switch (response.body().getMessage()){
                            case YMConst.MSG_INVALID_TOKEN:
                                Toast.makeText(VideoBroadcastingActivity.this, getResources().getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(VideoBroadcastingActivity.this, getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }else {

                    }
                }
            }

            @Override
            public void onFailure(Call<YMResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(VideoBroadcastingActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void leaveLive(){
        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMResponse> call = service.leaveLive( AGApplication.the().getMe().getId(), selectedAgent.getId(),
                AGApplication.the().getMe().getAppToken());

        ProgressDialog progressDialog = new ProgressDialog(VideoBroadcastingActivity.this);
        progressDialog.setMessage("");
        call.enqueue(new Callback<YMResponse>() {
            @Override
            public void onResponse(Call<YMResponse> call, Response<YMResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {
                    YMResponse response1 = response.body();
                    if (response1.getResult().equalsIgnoreCase("false")) {
                        switch (response.body().getMessage()){
                            case YMConst.MSG_INVALID_TOKEN:
                                Toast.makeText(VideoBroadcastingActivity.this, getResources().getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(VideoBroadcastingActivity.this, getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }else {

                    }
                }
            }

            @Override
            public void onFailure(Call<YMResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(VideoBroadcastingActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void endCall() {
        releaseRtmChannel();
        removeLocalVideo();
        removeRemoteVideo();
        leaveRtcChannel();
        finish();
    }

    private void releaseRtmChannel(){
        mRtmChannel.release();
    }

    private void removeLocalVideo() {
        if (mLocalView != null) {
            mLocalContainer.removeView(mLocalView);
        }
        mLocalView = null;
    }

    public void onCloseClicked(View view){
        isCallEnd = true;
        leaveLive();
        endCall();
    }

    public void onHideClick(View view){
        isHidden = !isHidden;
        if(isHidden){
            tvShowHide.setText(getResources().getText(R.string.show_comments));
            ivChevronUp.setVisibility(View.VISIBLE);
            ivChevronDown.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
        }
        else {
            tvShowHide.setText(getResources().getText(R.string.hide_comments));
            ivChevronUp.setVisibility(View.GONE);
            ivChevronDown.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * API CALL: create and join channel
     */
    private void createAndJoinRtmChannel() {
        // step 1: create a channel instance
        mRtmChannel = mRtmClient.createChannel(mChannelName, new VideoBroadcastingActivity.MyChannelListener());
        if (mRtmChannel == null) {
            finish();
            return;
        }

        // step 2: join the channel
        mRtmChannel.join(this);
    }

    /**
     * API CALL: get channel member list
     */
    private void getChannelMemberList() {
        mRtmChannel.getMembers(new ResultCallback<List<RtmChannelMember>>() {
            @Override
            public void onSuccess(final List<RtmChannelMember> responseInfo) {
                runOnUiThread(() -> {
                    mChannelMemberCount = responseInfo.size();
//                    refreshChannelTitle();
                });
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.e(TAG, "failed to get channel members, err: " + errorInfo.getErrorCode());
            }
        });
    }

    /**
     * API CALL: send message to a channel
     */
    private void sendChannelMessage(RtmMessage message) {
        mRtmChannel.sendMessage(message, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                // refer to RtmStatusCode.ChannelMessageState for the message state
                final int errorCode = errorInfo.getErrorCode();
                runOnUiThread(() -> {
                    Toast.makeText(VideoBroadcastingActivity.this, "error occured while sendgin message:" + errorInfo.toString(), Toast.LENGTH_LONG).show();
                    }
                );
            }
        });
    }

    public void onFlyClick(@NonNull View v) {
        int coins = AGApplication.the().getMe().getCoinBalance();
        int balanceDegree = 0;

        if(coins >= 500) balanceDegree = YMConst.BD_CROWN;
        else if(coins<500 && coins>=250) balanceDegree = YMConst.BD_DIAMOND;
        else if(coins<250 && coins>=100) balanceDegree = YMConst.BD_COIN;
        else if(coins<100)balanceDegree = YMConst.BD_NOTHING;

        String msg = balanceDegree + ":" + this.mUserName + ":";
        RtmMessage message = mRtmClient.createMessage();

        switch (v.getId()) {
            case R.id.cvFly:
                if (!mMsgEditText.getText().toString().equals("")) {
                    msg += mMsgEditText.getText().toString() ;
                    mMsgEditText.setText("");
                }
                else return;
                break;
            case R.id.cvHi:
                msg += "Hi!";
                break;
            case R.id.cvHello:
                msg += "Hello!";
                break;
            case R.id.cvSmileEmoji:
                msg += "\uD83D\uDE00";
                break;
            case R.id.cvLoveEye:
                msg += "\uD83D\uDE0D";
                break;
            case R.id.cvMoneyEye:
                msg += "\uD83E\uDD11";
                break;
            case R.id.cvStarEye:
                msg += "\uD83E\uDD29";
                break;
            case R.id.cvLoveSmile:
                msg += "\uD83E\uDD70";
                break;
            case R.id.cvHeart:
                showHeartAnimation();
                msg = HEART_ANIMATION;
                message.setText(msg);
                sendChannelMessage(message);
                return;
        }

        message.setText(msg);
        sendChannelMessage(message);

        String userAccount = String.valueOf(agUid);
        MessageBean messageBean = new MessageBean(userAccount, message, false);
        mMessageBeanList.add(messageBean);
        mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
        mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
    }

    /**
     * API CALL: leave and release channel
     */
    private void leaveAndReleaseChannel() {
        if (mRtmChannel != null) {
            mRtmChannel.leave(null);
            mRtmChannel.release();
            mRtmChannel = null;
        }
    }

    /**
     * API CALLBACK: rtm event listener
     * for peer to peer message
     */
    class MyRtmClientListener implements RtmClientListener {

        @Override
        public void onConnectionStateChanged(final int state, int reason) {
            runOnUiThread(() -> {
                switch (state) {
                    case RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING:
//                        showToast(getString(R.string.reconnecting));
                        break;
                    case RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED:
//                        showToast(getString(R.string.account_offline));
//                        setResult(MessageUtil.ACTIVITY_RESULT_CONN_ABORTED);
                        finish();
                        break;
                }
            });
        }

        @Override
        public void onMessageReceived(final RtmMessage message, final String peerId) {
            runOnUiThread(() -> {
                String peerAccount = "user_"+ peerId; //////////////////////////////////////////////
                if (peerId.equals(peerAccount)) {
                    MessageBean messageBean = new MessageBean(peerId, message, false);
                    messageBean.setBackground(getMessageColor(peerId));
                    mMessageBeanList.add(messageBean);
                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                    mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
                } else {
                    MessageUtil.addMessageBean(peerId, message);
                }
            });
        }

        @Override
        public void onImageMessageReceivedFromPeer(final RtmImageMessage rtmImageMessage, final String peerId) {
            runOnUiThread(() -> {
                String peerAccount = "user_"+ peerId; //////////////////////////////////////////////
                if (peerId.equals(peerAccount)) {
                    MessageBean messageBean = new MessageBean(peerId, rtmImageMessage, false);
                    messageBean.setBackground(getMessageColor(peerId));
                    mMessageBeanList.add(messageBean);
                    mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                    mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
                } else {
                    MessageUtil.addMessageBean(peerId, rtmImageMessage);
                }
            });
        }

        @Override
        public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

        }

        @Override
        public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

        }

        @Override
        public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

        }

        @Override
        public void onTokenExpired() {

        }

        @Override
        public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

        }
    }

    /**
     * API CALLBACK: rtm channel event listener
     * for channel message
     */
    class MyChannelListener implements RtmChannelListener {
        @Override
        public void onMemberCountUpdated(int i) {

        }

        @Override
        public void onAttributesUpdated(List<RtmChannelAttribute> list) {

        }

        @Override
        public void onMessageReceived(final RtmMessage message, final RtmChannelMember fromMember) {
            runOnUiThread(() -> {
                if(message.getText().equalsIgnoreCase(HEART_ANIMATION)){
                    showHeartAnimation();
                    return;
                }
                String account = fromMember.getUserId();
                Log.i(TAG, "onMessageReceived account = " + account + " msg = " + message);
                MessageBean messageBean = new MessageBean(account, message, false);
                messageBean.setBackground(getMessageColor(account));
                mMessageBeanList.add(messageBean);
                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
            });
        }

        @Override
        public void onImageMessageReceived(final RtmImageMessage rtmImageMessage, final RtmChannelMember rtmChannelMember) {
            runOnUiThread(() -> {
                String account = rtmChannelMember.getUserId();
                Log.i(TAG, "onMessageReceived account = " + account + " msg = " + rtmImageMessage);
                MessageBean messageBean = new MessageBean(account, rtmImageMessage, false);
                messageBean.setBackground(getMessageColor(account));
                mMessageBeanList.add(messageBean);
                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
            });
        }

        @Override
        public void onFileMessageReceived(RtmFileMessage rtmFileMessage, RtmChannelMember rtmChannelMember) {

        }

        @Override
        public void onMemberJoined(RtmChannelMember member) {
            runOnUiThread(() -> {
//                mChannelMemberCount++;
//                refreshChannelTitle();
            });
        }

        @Override
        public void onMemberLeft(RtmChannelMember member) {
            runOnUiThread(() -> {
//                mChannelMemberCount--;
//                refreshChannelTitle();
            });
        }
    }

    private int getMessageColor(String account) {
        for (int i = 0; i < mMessageBeanList.size(); i++) {
            if (account.equals(mMessageBeanList.get(i).getAccount())) {
                return mMessageBeanList.get(i).getBackground();
            }
        }
        return MessageUtil.COLOR_ARRAY[MessageUtil.RANDOM.nextInt(MessageUtil.COLOR_ARRAY.length)];
    }


    /********************************** Heart Animation ******************************************/
    public void flyEmoji(final int resId) {
        ZeroGravityAnimation animation = new ZeroGravityAnimation();
        animation.setCount(1);
        animation.setScalingFactor(1.0f);
        animation.setOriginationDirection(Direction.BOTTOM);
        animation.setDestinationDirection(Direction.TOP);
        animation.setImage(resId);
        animation.setAnimationListener(new Animation.AnimationListener() {
                                           @Override
                                           public void onAnimationStart(Animation animation) {

                                           }
                                           @Override
                                           public void onAnimationEnd(Animation animation) {

                                           }
                                           @Override
                                           public void onAnimationRepeat(Animation animation) {

                                           }
                                       }
        );

        ViewGroup container = findViewById(R.id.animation_holder);
        animation.play(this,container);

    }

    public void emoji_one() {
        // You can change the number of emojis that will be flying on screen
        for (int i = 0; i < 4; i++) {
            flyEmoji(R.drawable.heart6);
        }
    }
    // You can change the number of emojis that will be flying on screen

    public void emoji_two(){
        for(int i=0;i<4;i++) {
            flyEmoji(R.drawable.heart2);
        }
    }
    // You can change the number of emojis that will be flying on screen

    public void emoji_three(){
        for(int i=0;i<4;i++) {
            flyEmoji(R.drawable.heart3);
        }
    }

    // This method will be used if You want to fly your Emois Over any view
    public void flyObject(final int resId, final int duration, final Direction from, final Direction to, final float scale) {

        ZeroGravityAnimation animation = new ZeroGravityAnimation();
        animation.setCount(1);
        animation.setScalingFactor(scale);
        animation.setOriginationDirection(from);
        animation.setDestinationDirection(to);
        animation.setImage(resId);
        animation.setDuration(duration);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                flyObject(resId, duration, from, to, scale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        ViewGroup container = (ViewGroup) findViewById(R.id.animation_holder);
        animation.play(this,container);

    }

    public void showHeartAnimation(){
        emoji_one();
        emoji_two();
        emoji_three();
    }

}


class ZeroGravityAnimation {

    private static final int RANDOM_DURATION = -1;


    private Direction mOriginationDirection = Direction.RANDOM;
    private Direction mDestinationDirection = Direction.RANDOM;
    private int mDuration = RANDOM_DURATION;
    private int mCount = 1;
    private int mImageResId;
    private float mScalingFactor = 1f;
    private Animation.AnimationListener mAnimationListener;


    /**
     * Sets the orignal direction. The animation will originate from the given direction.
     */
    public ZeroGravityAnimation setOriginationDirection(Direction direction) {
        this.mOriginationDirection = direction;
        return this;
    }

    /**
     * Sets the animation destination direction. The translate animation will proceed towards the given direction.
     *
     * @param direction
     * @return
     */
    public ZeroGravityAnimation setDestinationDirection(Direction direction) {
        this.mDestinationDirection = direction;
        return this;
    }

    /**
     * Will take a random time duriation for the animation
     *
     * @return
     */
    public ZeroGravityAnimation setRandomDuration() {
        return setDuration(RANDOM_DURATION);
    }

    /**
     * Sets the time duration in millseconds for animation to proceed.
     *
     * @param duration
     * @return
     */
    public ZeroGravityAnimation setDuration(int duration) {
        this.mDuration = duration;
        return this;
    }

    /**
     * Sets the image reference id for drawing the image
     *
     * @param resId
     * @return
     */
    public ZeroGravityAnimation setImage(int resId) {
        this.mImageResId = resId;
        return this;
    }

    /**
     * Sets the image scaling value.
     *
     * @param scale
     * @return
     */
    public ZeroGravityAnimation setScalingFactor(float scale) {
        this.mScalingFactor = scale;
        return this;
    }

    public ZeroGravityAnimation setAnimationListener(Animation.AnimationListener listener) {
        this.mAnimationListener = listener;
        return this;
    }

    public ZeroGravityAnimation setCount(int count) {
        this.mCount = count;
        return this;
    }


    /**
     * Starts the Zero gravity animation by creating an OTT and attach it to th given ViewGroup
     *
     * @param activity
     * @param ottParent
     */
    public void play(Activity activity, ViewGroup ottParent) {

        DirectionGenerator generator = new DirectionGenerator();

        if (mCount > 0) {

            for (int i = 0; i < mCount; i++) {

                final int iDupe = i;

                Direction origin = mOriginationDirection == Direction.RANDOM ? generator.getRandomDirection() : mOriginationDirection;
                Direction destination = mDestinationDirection == Direction.RANDOM ? generator.getRandomDirection(origin) : mDestinationDirection;

                int startingPoints[] = generator.getPointsInDirection(activity, origin);
                int endPoints[] = generator.getPointsInDirection(activity, destination);

                Bitmap scaledBitmap = BitmapFactory.decodeResource(activity.getResources(), mImageResId);

//                Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), mImageResId);
//                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * mScalingFactor), (int) (bitmap.getHeight() * mScalingFactor), false);

                switch (origin) {
                    case LEFT:
                        startingPoints[0] -= scaledBitmap.getWidth();
                        break;

                    case RIGHT:
                        startingPoints[0] += scaledBitmap.getWidth();
                        break;

                    case TOP:
                        startingPoints[1] -= scaledBitmap.getHeight();
                        break;
                    case BOTTOM:
                        startingPoints[1] += scaledBitmap.getHeight();
                        break;
                }

                switch (destination) {
                    case LEFT:
                        endPoints[0] -= scaledBitmap.getWidth();
                        break;

                    case RIGHT:
                        endPoints[0] += scaledBitmap.getWidth();
                        break;

                    case TOP:
                        endPoints[1] -= scaledBitmap.getHeight();
                        break;
                    case BOTTOM:
                        endPoints[1] += scaledBitmap.getHeight();
                        break;
                }

                final OverTheTopLayer layer = new OverTheTopLayer();

                FrameLayout ottLayout = layer.with(activity)
//                        .scale(mScalingFactor)
                        .attachTo(ottParent)
                        .setBitmap(scaledBitmap, startingPoints)
                        .create();


                switch (origin) {
                    case LEFT:

                }

                int deltaX = endPoints[0] - startingPoints[0];
                int deltaY = endPoints[1] - startingPoints[1];

                int duration = mDuration;
                if (duration == RANDOM_DURATION) {
                    duration = RandomUtil.generateRandomBetween(2000, 8000);
                }

                TranslateAnimation animation = new TranslateAnimation(0, deltaX, 0, deltaY);
                animation.setDuration(duration);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        if (iDupe == 0) {
                            if (mAnimationListener != null) {
                                mAnimationListener.onAnimationStart(animation);
                            }
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layer.destroy();

                        if (iDupe == (mCount - 1)) {
                            if (mAnimationListener != null) {
                                mAnimationListener.onAnimationEnd(animation);
                            }
                        }
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                layer.applyAnimation(animation);
            }
        } else {

            Log.e(ZeroGravityAnimation.class.getSimpleName(), "Count was not provided, animation was not started");
        }
    }

    /**
     * Takes the content view as view parent for laying the animation objects and starts the animation.
     *
     * @param activity - activity on which the zero gravity animation should take place.
     */
    public void play(Activity activity) {

        play(activity, null);

    }
}

class RandomUtil {

    /**
     * Generates the random between two given integers.
     */
    public static int generateRandomBetween(int start, int end) {

        Random random = new Random();
        int rand = random.nextInt(Integer.MAX_VALUE - 1) % end;

        if (rand < start) {
            rand = start;
        }
        return rand;
    }
}

enum Direction {

    TOP, LEFT, RIGHT, BOTTOM,RANDOM
}

class DirectionGenerator {


    /**
     * Gets the random pixel points in the given direction of the screen
     * @param activity - activity from where you are referring the random value.
     * @param direction - on among LEFT,RIGHT,TOP,BOTTOM,RANDOM
     * @return a pixel point {x,y} in the given direction.
     */
    public int[] getPointsInDirection(Activity activity, Direction direction) {

        switch (direction) {

            case LEFT:
                return getRandomLeft(activity);
            case RIGHT:
                return getRandomRight(activity);
            case BOTTOM:
                return getRandomBottom(activity);
            case TOP:
                return getRandomTop(activity);

            default:
                Direction[] allDirections = new Direction[]{Direction.LEFT,Direction.TOP,Direction.BOTTOM,Direction.RIGHT};
                int index = new Random().nextInt(allDirections.length);
                return getPointsInDirection(activity, allDirections[index]);

        }

    }

    /**
     * Gets the random pixel points in the left direction of the screen. The value will be of {0,y} where y will be a random value.
     * @param activity - activity from where you are referring the random value.
     * @return a pixel point {x,y}.
     */
    public int[] getRandomLeft(Activity activity) {

        int x = 0;

        int height = activity.getResources().getDisplayMetrics().heightPixels;

        Random random = new Random();
        int y = random.nextInt(height);

        return new int[]{x, y};
    }

    /**
     * Gets the random pixel points in the top direction of the screen. The value will be of {x,0} where x will be a random value.
     * @param activity - activity from where you are referring the random value.
     * @return a pixel point {x,y}.
     */
    public int[] getRandomTop(Activity activity) {

        int y = 0;

        int width = activity.getResources().getDisplayMetrics().widthPixels;

        //Random random = new Random();
        int x = 0;//random.nextInt(width);

        return new int[]{x, y};
    }

    /**
     * Gets the random pixel points in the right direction of the screen. The value will be of {screen_width,y} where y will be a random value.
     * @param activity - activity from where you are referring the random value.
     * @return a pixel point {x,y}.
     */
    public int[] getRandomRight(Activity activity) {


        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;

        int x = width ;

        Random random = new Random();
        int y = random.nextInt(height);

        return new int[]{x, y};
    }

    /**
     * Gets the random pixel points in the bottom direction of the screen. The value will be of {x,screen_height} where x will be a random value.
     * @param activity - activity from where you are referring the random value.
     * @return a pixel point {x,y}.
     */
    public int[] getRandomBottom(Activity activity) {


        //int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;


        int y = height ;
        //Random random = new Random();
        int x = 0;//random.nextInt(width);

        return new int[]{x, y};
    }

    /**
     * Gets a random direction.
     * @return one among LEFT,RIGHT,BOTTOM,TOP
     */
    public Direction getRandomDirection() {
        Direction[] allDirections = new Direction[]{Direction.LEFT,Direction.TOP,Direction.BOTTOM,Direction.RIGHT};
        int index = new Random().nextInt(allDirections.length);
        return (allDirections[index]);
    }

    /**
     * Gets a random direction skipping the given direction.
     * @param toSkip a direction which should not be returned by this method.
     * @return one among LEFT,RIGHT,BOTTOM if TOP is provided as direction to skip,
     * one among TOP,RIGHT,BOTTOM if LEFT is provided as direction to skip
     * and so on.
     */
    public Direction getRandomDirection(Direction toSkip) {
        Direction[] allExceptionalDirections;
        switch (toSkip) {

            case LEFT:
                allExceptionalDirections = new Direction[]{Direction.TOP,Direction.BOTTOM,Direction.RIGHT};
                break;
            case RIGHT:
                allExceptionalDirections = new Direction[]{Direction.TOP,Direction.BOTTOM,Direction.LEFT};
                break;
            case BOTTOM:
                allExceptionalDirections = new Direction[]{Direction.TOP,Direction.LEFT,Direction.RIGHT};
                break;
            case TOP:
                allExceptionalDirections = new Direction[]{Direction.LEFT,Direction.BOTTOM,Direction.RIGHT};
                break;

            default:
                allExceptionalDirections = new Direction[]{Direction.LEFT,Direction.TOP,Direction.BOTTOM,Direction.RIGHT};


        }

        int index = new Random().nextInt(allExceptionalDirections.length);
        return (allExceptionalDirections[index]);
    }
}

class OverTheTopLayer {

    public static class OverTheTopLayerException extends RuntimeException {
        public OverTheTopLayerException(String msg) {
            super(msg);
        }
    }

    private WeakReference<Activity> mWeakActivity;
    private WeakReference<ViewGroup> mWeakRootView;
    private FrameLayout mCreatedOttLayer;
    private float mScalingFactor = 1.0f;
    private int[] mDrawLocation = {0, 0};
    private Bitmap mBitmap;


    public OverTheTopLayer() {
    }

    /**
     * To create a layer on the top of activity

     */
    public OverTheTopLayer with(Activity weakReferenceActivity) {
        mWeakActivity = new WeakReference<Activity>(weakReferenceActivity);
        return this;
    }

    /**
     * Draws the image as per the drawable resource id on the given location pixels.

     */
    public OverTheTopLayer generateBitmap(Resources resources, int drawableResId, float mScalingFactor, int[] location) {

        if (location == null) {
            location = new int[]{0, 0};
        } else if (location.length != 2) {
            throw new OverTheTopLayerException("Requires location as an array of length 2 - [x,y]");
        }

        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawableResId);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * mScalingFactor), (int) (bitmap.getHeight() * mScalingFactor), false);

        this.mBitmap = scaledBitmap;

        this.mDrawLocation = location;
        return this;
    }

    public OverTheTopLayer setBitmap(Bitmap bitmap, int[] location) {

        if (location == null) {
            location = new int[]{0, 0};
        } else if (location.length != 2) {
            throw new OverTheTopLayerException("Requires location as an array of length 2 - [x,y]");
        }

        this.mBitmap = bitmap;
        this.mDrawLocation = location;
        return this;
    }


    /**
     * Holds the scaling factor for the image.
     *
     * @param scale
     * @return
     */
    public OverTheTopLayer scale(float scale) {

        if (scale <= 0) {
            throw new OverTheTopLayerException("Scaling should be > 0");

        }
        this.mScalingFactor = scale;


        return this;
    }

    /**
     * Attach the OTT layer as the child of the given root view.
     * @return
     */
    public OverTheTopLayer attachTo(ViewGroup rootView) {
        this.mWeakRootView = new WeakReference<ViewGroup>(rootView);
        return this;
    }

    /**
     * Creates an OTT.
     * @return
     */
    public FrameLayout create() {


        if(mCreatedOttLayer != null) {
            destroy();
        }

        if (mWeakActivity == null) {
            throw new OverTheTopLayerException("Could not create the layer as not activity reference was provided.");
        }

        Activity activity = mWeakActivity.get();

        if (activity != null) {
            ViewGroup attachingView = null;


            if (mWeakRootView != null && mWeakRootView.get() != null) {
                attachingView = mWeakRootView.get();
            } else {
                attachingView = (ViewGroup) activity.findViewById(android.R.id.content);
            }


            ImageView imageView = new ImageView(activity);

            imageView.setImageBitmap(mBitmap);


            int minWidth = mBitmap.getWidth();
            int minHeight = mBitmap.getHeight();

            imageView.measure(View.MeasureSpec.makeMeasureSpec(minWidth, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(minHeight, View.MeasureSpec.AT_MOST));

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();

            if (params == null) {
                params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.TOP);
                imageView.setLayoutParams(params);
            }

            int xPosition = mDrawLocation[0];
            int yPosition = mDrawLocation[1];

            params.width = minWidth;
            params.height = minHeight;

            params.leftMargin = xPosition;
            params.topMargin = yPosition;

            imageView.setLayoutParams(params);

            FrameLayout ottLayer = new FrameLayout(activity);
            FrameLayout.LayoutParams topLayerParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.TOP);

            ottLayer.setLayoutParams(topLayerParam);

            ottLayer.addView(imageView);

            attachingView.addView(ottLayer);

            mCreatedOttLayer = ottLayer;


        } else {
            Log.e(OverTheTopLayer.class.getSimpleName(), "Could not create the layer. Reference to the activity was lost");
        }

        return mCreatedOttLayer;

    }

    /**
     * Kills the OTT
     */
    public void destroy() {


        if (mWeakActivity == null) {
            throw new OverTheTopLayerException("Could not create the layer as not activity reference was provided.");
        }

        Activity activity = mWeakActivity.get();

        if (activity != null) {
            ViewGroup attachingView = null;


            if (mWeakRootView != null && mWeakRootView.get() != null) {
                attachingView = mWeakRootView.get();
            } else {
                attachingView = (ViewGroup) activity.findViewById(android.R.id.content);
            }

            if (mCreatedOttLayer != null) {

                attachingView.removeView(mCreatedOttLayer);
                mCreatedOttLayer = null;
            }


        } else {

            Log.e(OverTheTopLayer.class.getSimpleName(), "Could not destroy the layer as the layer was never created.");

        }

    }

    /**
     * Applies the animation to the image view present in OTT.
     * @param animation
     */
    public void applyAnimation(Animation animation) {

        if(mCreatedOttLayer != null) {
            ImageView drawnImageView = (ImageView) mCreatedOttLayer.getChildAt(0);
            drawnImageView.startAnimation(animation);
        }
    }
}