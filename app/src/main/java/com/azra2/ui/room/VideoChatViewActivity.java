package com.azra2.ui.room;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.model.ChatManager;
import com.azra2.model.YMUser;
import com.azra2.ui.user.AgentAdapter;
import com.azra2.utils.MessageUtil;
import com.azra2.utils.TinyDB;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.LocalInvitation;
import io.agora.rtm.RemoteInvitation;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmCallEventListener;
import io.agora.rtm.RtmCallManager;
import io.agora.rtm.RtmClient;

public class VideoChatViewActivity extends BaseCallActivity {
    private static final String TAG = VideoChatViewActivity.class.getSimpleName();

    private static final int PERMISSION_REQ_ID = 22;

    // Permission WRITE_EXTERNAL_STORAGE is not mandatory
    // for Agora RTC SDK, just in case if you wanna save
    // logs to external sdcard.
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private int roomType = -1;
    private int callType = -1;
    private String agentName = "";

    private String mUserName = "";

    private String mChannelName = "";

    private RtcEngine mRtcEngine;
    private boolean mMuted;

    private boolean isMicOff = false;
    private boolean isCameraOff = false;
    private boolean isVolumeOff = false;
    private boolean isSwitched = false;
    private boolean isCallEnd = false;

    private Timer timer;
    private int time = 0;

    private ImageView mMicBtn, mCameraBtn, mVolumeBtn, mSwitchBtn, mCallBtn;
    private TextView tvTime;

    private FrameLayout mLocalContainer;
    private RelativeLayout mRemoteContainer;
    private SurfaceView mLocalView;
    private SurfaceView mRemoteView;

    ProgressDialog progressDialog;

    // Customized logger view
//    private LoggerRecyclerView mLogView;

    /**
     * Event handler registered into RTC engine for RTC callbacks.
     * Note that UI operations needs to be in UI thread because RTC
     * engine deals with the events in a separate thread.
     */
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        /**
         * Occurs when the local user joins a specified channel.
         * The channel name assignment is based on channelName specified in the joinChannel method.
         * If the uid is not specified when joinChannel is called, the server automatically assigns a uid.
         *
         * @param channel Channel name.
         * @param uid User ID.
         * @param elapsed Time elapsed (ms) from the user calling joinChannel until this callback is triggered.
         */
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora", "Join channel success, uid:"+ (uid & 0xFFFFFFFFL));
//                    mLogView.logI("Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                }
            });
        }

        /**
         * Occurs when the first remote video frame is received and decoded.
         * This callback is triggered in either of the following scenarios:
         *
         *     The remote user joins the channel and sends the video stream.
         *     The remote user stops sending the video stream and re-sends it after 15 seconds. Possible reasons include:
         *         The remote user leaves channel.
         *         The remote user drops offline.
         *         The remote user calls the muteLocalVideoStream method.
         *         The remote user calls the disableVideo method.
         *
         * @param uid User ID of the remote user sending the video streams.
         * @param width Width (pixels) of the video stream.
         * @param height Height (pixels) of the video stream.
         * @param elapsed Time elapsed (ms) from the local user calling the joinChannel method until this callback is triggered.
         */
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    mLogView.logI("First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                    setupRemoteVideo(uid);
                }
            });
        }

        /**
         * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
         *
         * There are two reasons for users to become offline:
         *
         *     Leave the channel: When the user/host leaves the channel, the user/host sends a
         *     goodbye message. When this message is received, the SDK determines that the
         *     user/host leaves the channel.
         *
         *     Drop offline: When no data packet of the user or host is received for a certain
         *     period of time (20 seconds for the communication profile, and more for the live
         *     broadcast profile), the SDK assumes that the user/host drops offline. A poor
         *     network connection may lead to false detections, so we recommend using the
         *     Agora RTM SDK for reliable offline detection.
         *
         * @param uid ID of the user or host who leaves the channel or goes offline.
         * @param reason Reason why the user goes offline:
         *
         *     USER_OFFLINE_QUIT(0): The user left the current channel.
         *     USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
         *     USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
         */
        @Override
        public void onUserOffline(final int uid, int reason) {
            super.onUserOffline(uid, reason);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","User offline, uid: " + (uid & 0xFFFFFFFFL));
//                    mLogView.logI("User offline, uid: " + (uid & 0xFFFFFFFFL));
                    isCallEnd = true;
                    endCall();
                }
            });
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        @Override
        public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
//            log.debug("onFirstLocalVideoFrame " + width + " " + height + " " + elapsed);
            Log.i("agora", "Local video");
        }

        @Override
        public void onRemoteVideoStateChanged(final int uid, int state, int reason, int elapsed) {
            super.onRemoteVideoStateChanged(uid, state, reason, elapsed);
            if(state == Constants.REMOTE_VIDEO_STATE_STARTING) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("agora", "Remote video starting , uid:" + (uid & 0xFFFFFFFFL));
                        setupRemoteVideo(uid);
                    }
                });
            }
        }

    };

    public void startTimer() {
        timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        time +=1;
                        if(time % 60 == 0){
                            int restCoins = AGApplication.the().getMe().getCoinBalance() - VideoChatViewActivity.this.selectedAgent.getPerformancePrice();
                            AGApplication.the().getMe().setCoinBalance(restCoins);
                            if(restCoins > VideoChatViewActivity.this.selectedAgent.getPerformancePrice()){

                            }
                            else{ //ends call
                                isCallEnd = true;
                                endCallDueCoin();
                            }
                        }
                        String strTime = "";
                        int hh, mm, ss;
                        hh = time / 3600;
                        if(hh < 10) strTime += "0"+hh+":";
                        else strTime += hh + ":";

                        mm = (time - 3600 * hh)/60;
                        if(mm < 10) strTime += "0"+mm+":";
                        else strTime += mm + ":";

                        ss = time - 3600 * hh - 60 * mm;
                        if(ss < 10) strTime += "0"+ss+"";
                        else strTime += ss + "";
//                        String str = String.format(Locale.getDefault(), "%d:%d:%d", hh,mm,ss);
                        tvTime.setText(strTime);
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat_view);
        this.selectedAgent = AGApplication.the().getSelectedAgent();
        initRtm();
        doRtmLogin();

        startTimer();

        initFields();
        initUI();
//        progressDialog.show();
        // Ask for permissions at runtime.
        // This is just an example set of permissions. Other permissions
        // may be needed, and please refer to our online documents.
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initEngineAndJoinChannel();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
//        progressDialog.show();
    }

    private void initFields(){
//        TinyDB tinyDB = new TinyDB(getApplicationContext());
//        this.currentUser = tinyDB.getObject(YMConst.KEY_CURRENT_USER, YMUser.class);
        this.me = AGApplication.the().getMe();
        this.agUid = me.getId() * 2; // id * 2
        this.mUserName = me.getNickName();

        Intent intent = getIntent();
        this.roomType = intent.getIntExtra(YMConst.ROOM_TYPE, -1);
        this.callType = intent.getIntExtra(YMConst.CALL_TYPE, -1);
        this.agAid = Integer.parseInt(intent.getStringExtra(YMConst.AG_AGENT_ID)); //defaultValue?// id * 2 + 1
        this.agentName = intent.getStringExtra(YMConst.AGENT_NAME);

        this.mChannelName = agUid + "_" + agAid;   //for 1 to 1
    }

    private void initUI() {
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);

        mMicBtn = findViewById(R.id.ivMic);
        mCameraBtn = findViewById(R.id.ivCamera);
        mVolumeBtn = findViewById(R.id.ivVolume);
        mSwitchBtn = findViewById(R.id.ivSwitch);
        mCallBtn = findViewById(R.id.ivEndCall);
        tvTime = findViewById(R.id.tvTime);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Calling your agent...");
        progressDialog.setCancelable(true);

//        mLogView = findViewById(R.id.log_recycler_view);

        // Sample logs are optional.
//        showSampleLogs();
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                        "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                finish();
                return;
            }

            // Here we continue only if all permissions are granted.
            // The permissions can also be granted in the system settings manually.
            initEngineAndJoinChannel();
        }
    }

    private void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initEngineAndJoinChannel() {
        // This is our usual steps for joining
        // a channel and starting a call.
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();
    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getApplicationContext(), getString(R.string.agora_app_id), mRtcEventHandler);
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoConfig() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
        if(this.callType == YMConst.CT_VOICE) mRtcEngine.disableVideo(); //voicecall
        else if (this.callType == YMConst.CT_VIDEO) mRtcEngine.enableVideo(); //videocall
        else if(this.callType == YMConst.CT_UNDEFINED) {
            Toast.makeText(getBaseContext(), "There is some problem with your permission!", Toast.LENGTH_SHORT).show();
            this.finish(); //not defined
        }

        // Please go to this page for detailed explanation
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        // This is used to set a local preview.
        // The steps setting local and remote view are very similar.
        // But note that if the local user do not have a uid or do
        // not care what the uid is, he can set his uid as ZERO.
        // Our server will assign one and return the uid via the event
        // handler callback function (onJoinChannelSuccess) after
        // joining the channel successfully.
        mLocalView = RtcEngine.CreateRendererView(getBaseContext());
        mLocalView.setZOrderOnTop(false);
        mLocalView.setZOrderMediaOverlay(true);
        mLocalContainer.addView(mLocalView);
        // Initializes the local video view.
        // RENDER_MODE_HIDDEN: Uniformly scale the video until it fills the visible boundaries. One dimension of the video may have clipped contents.
        mRtcEngine.setupLocalVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
//        mRtcEngine.startPreview();
    }

    private void joinChannel() {
        // 1. Users can only see each other after they join the
        // same channel successfully using the same app id.
        // 2. One token is only valid for the channel name that
        // you use to generate this token.
        String token = getString(R.string.agora_access_token);
        if (TextUtils.isEmpty(token) ) {
            token = null; // default, no token
        }
        mRtcEngine.joinChannel(token, mChannelName, "", agUid);

        BeautyOptions BEAUTY_OPTIONS = new BeautyOptions();
        mRtcEngine.setBeautyEffectOptions(true, BEAUTY_OPTIONS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isCallEnd) {
            endCall();
        }
        /*
          Destroys the RtcEngine instance and releases all resources used by the Agora SDK.

          This method is useful for apps that occasionally make voice or video calls,
          to free up resources for other operations when not making calls.
         */
        RtcEngine.destroy();
    }

    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    public void onMicOffClicked(View view){
        isMicOff = !isMicOff;
        mRtcEngine.muteLocalAudioStream(isMicOff);
        int resSrc = isMicOff ? R.drawable.mic_off_24:R.drawable.mic_24;
        mMicBtn.setImageResource(resSrc);
        int resBack = isMicOff ? R.drawable.tab_layout_trans_white_back : R.drawable.tab_layout_trans_back;
        mMicBtn.setBackgroundResource(resBack);
    }

    public void onCameraDisabledClicked(View view){
        isCameraOff = !isCameraOff;
        //Disable/Enable the local camera
        mRtcEngine.enableLocalVideo(!isCameraOff);
        int resSrc = isCameraOff ? R.drawable.videocam_off_24: R.drawable.videocam_24;
        mCameraBtn.setImageResource(resSrc);
        int resBack = isCameraOff ? R.drawable.tab_layout_trans_white_back : R.drawable.tab_layout_trans_back;
        mCameraBtn.setBackground(getResources().getDrawable(resBack));
    }

    public void onVolumeOffClicked(View view) {
        isVolumeOff = !isVolumeOff;
        mRtcEngine.muteAllRemoteAudioStreams(isVolumeOff);
        int resSrc = isVolumeOff ? R.drawable.volume_off_24:R.drawable.volume_up_24;
        mVolumeBtn.setImageResource(resSrc);
        int resBack = isVolumeOff ? R.drawable.tab_layout_trans_white_back : R.drawable.tab_layout_trans_back;
        mVolumeBtn.setBackground(getResources().getDrawable(resBack));
    }

    public void onSwitchCameraClicked(View view) {
        isSwitched = !isSwitched;
        // Switches between front and rear cameras.
        mRtcEngine.switchCamera();
        int resBack = isSwitched ? R.drawable.tab_layout_trans_white_back : R.drawable.tab_layout_trans_back;
        mSwitchBtn.setBackground(getResources().getDrawable(resBack));
    }

    public void onCallClicked(View view) {
        isCallEnd = true;
        endCall();
//        if (mCallEnd) {
//            startCall();
//            mCallEnd = false;
//            mCallBtn.setImageResource(R.drawable.btn_endcall);
//        } else {
//            endCall();
//            mCallEnd = true;
//            mCallBtn.setImageResource(R.drawable.btn_startcall);
//        }
//
//        showButtons(!mCallEnd);
    }

    private void startCall() {
        setupLocalVideo();
        joinChannel();
    }

    private void endCall() {
        timer.cancel();
        removeLocalVideo();
        removeRemoteVideo();
        leaveChannel();
        showCallEndedDlg();
    }

    private void endCallDueCoin() {
        timer.cancel();
        removeLocalVideo();
        removeRemoteVideo();
        leaveChannel();
        showCallEndedDueCoinDlg();
    }

    private void setupRemoteVideo(int uid) {
        // Only one remote video view is available for this
        // tutorial. Here we check if there exists a surface
        // view tagged as this uid.
        int count = mRemoteContainer.getChildCount();
        View view = null;
        for (int i = 0; i < count; i++) {
            View v = mRemoteContainer.getChildAt(i);
            if (v.getTag() instanceof Integer && ((int) v.getTag()) == uid) {
                view = v;
            }
        }

        if (view != null) {
            return;
        }

        /*
          Creates the video renderer view.
          CreateRendererView returns the SurfaceView type. The operation and layout of the view
          are managed by the app, and the Agora SDK renders the view provided by the app.
          The video display view must be created using this method instead of directly
          calling SurfaceView.
         */
        mRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        mRemoteContainer.addView(mRemoteView);
        // Initializes the video view of a remote user.
        mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        mRemoteView.setTag(uid);
    }

    private void removeLocalVideo() {
        if (mLocalView != null) {
            mLocalContainer.removeView(mLocalView);
        }
        mLocalView = null;
    }

    private void removeRemoteVideo() {
        if (mRemoteView != null) {
            mRemoteContainer.removeView(mRemoteView);
        }
        // Destroys remote view
        mRemoteView = null;
    }

    private void showButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
//        mMuteBtn.setVisibility(visibility);
//        mSwitchCameraBtn.setVisibility(visibility);
    }

    private void showCallEndedDlg(){
        final Dialog dlgCallEnded = new Dialog(VideoChatViewActivity.this);
        dlgCallEnded.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlgCallEnded.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgCallEnded.setCancelable(false);
        dlgCallEnded.setContentView(R.layout.dialog_call_ended);
        Window window = dlgCallEnded.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        CardView btnCallEnd = dlgCallEnded.findViewById(R.id.CallEndBtn);
        btnCallEnd.setOnClickListener(v -> {
            dlgCallEnded.dismiss();
            this.finish();
        });

        dlgCallEnded.show();
    }

    private void showCallEndedDueCoinDlg(){
        final Dialog dlgCallEnded = new Dialog(VideoChatViewActivity.this);
        dlgCallEnded.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlgCallEnded.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgCallEnded.setCancelable(true);
        dlgCallEnded.setContentView(R.layout.dialog_call_ended_due_coin);
        Window window = dlgCallEnded.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        CardView btnBuyCoin = dlgCallEnded.findViewById(R.id.cvBuyCoinBtn);
        ImageView btnClose = dlgCallEnded.findViewById(R.id.ivCloseBtn);

        btnBuyCoin.setOnClickListener(v -> {
            dlgCallEnded.dismiss();
            this.finish();
        });

        btnClose.setOnClickListener(v -> {
            dlgCallEnded.dismiss();
            this.finish();
        });

        dlgCallEnded.show();


    }




}