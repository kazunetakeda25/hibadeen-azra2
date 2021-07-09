package com.azra2.ui.room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.utils.Utils;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtm.LocalInvitation;

public class CallActivity extends BaseCallActivity implements View.OnClickListener {
    private static final String TAG = CallActivity.class.getSimpleName();

    private int mRole;
    private String mPeer;
    private String mChannel;
    private AppCompatImageView mAcceptBtn;
    private AppCompatImageView mHangupBtn;
    private MediaPlayer mPlayer;
    private PortraitAnimator mAnimator;
    private CircleImageView ivPeerImage;

    private boolean mInvitationSending;
    private boolean mInvitationReceiving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        mRole = YMConst.ROLE_CALLER;
        this.selectedAgent = AGApplication.the().getSelectedAgent();

        initRtm();
        doRtmLogin();
        initUi();

        Intent intent = getIntent();
        String agAid =intent.getStringExtra(YMConst.AG_AGENT_ID);
        String userName = intent.getStringExtra(YMConst.USER_NAME);
        String channelName = intent.getStringExtra(YMConst.CHANNEL_NAME);
        int role = intent.getIntExtra(YMConst.CALLING_ROLE, -1);
        inviteCall(agAid,userName,channelName);

        startRinging();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onLocalInvitationAccepted(LocalInvitation localInvitation, String s) {
        gotoVideoChatActivity(localInvitation.getCalleeId(), localInvitation.getContent(), localInvitation.getCalleeId(), YMConst.ROLE_CALLER);
        stopRinging();
        this.finish(); // here this indicates CallActivity
    }

    @Override
    public void onLocalInvitationRefused(LocalInvitation localInvitation, String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CallActivity.this, getResources().getString(R.string.she_declined), Toast.LENGTH_SHORT).show();
            }
        });
        stopRinging();
        this.finish();
    }

    @Override
    public void onLocalInvitationCanceled(LocalInvitation localInvitation) {
        stopRinging();
        this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accept_call_btn:
//                answerCall(global().getRemoteInvitation());
                break;
            case R.id.hang_up_btn:
                if (isCaller()) {
                    cancelLocalInvitation();
                } else if (isCallee()) {
//                    refuseRemoteInvitation(global().getRemoteInvitation());
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAnimator.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAnimator.stop();
    }

    private void initUi(){
        ivPeerImage = findViewById(R.id.peer_image);
        Picasso.Builder builder = new Picasso.Builder(CallActivity.this);
        builder.downloader(new OkHttp3Downloader(CallActivity.this));
        if(!Utils.isNullOrEmpty(selectedAgent.getProfileImage())){
            builder.build().load(selectedAgent.getProfileImage())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(ivPeerImage);
        }

        mHangupBtn = findViewById(R.id.hang_up_btn);
        mHangupBtn.setVisibility(View.VISIBLE);
        mHangupBtn.setOnClickListener(this);

        TextView roleText = findViewById(R.id.call_role);
        mAcceptBtn = findViewById(R.id.accept_call_btn);

        if (isCallee()) {
            roleText.setText(R.string.receiving_call);
            mAcceptBtn.setVisibility(View.VISIBLE);
            mAcceptBtn.setOnClickListener(this);
        } else if (isCaller()) {
            roleText.setText(R.string.calling_out);
            mAcceptBtn.setVisibility(View.GONE);
        }

        mAnimator = new PortraitAnimator(
                findViewById(R.id.anim_layer_1),
                findViewById(R.id.anim_layer_2),
                findViewById(R.id.anim_layer_3));

    }

    private boolean isCaller() {
        return mRole == YMConst.ROLE_CALLER;
    }

    private boolean isCallee() {
        return mRole == YMConst.ROLE_CALLEE;
    }

    private void startRinging() {
        if (isCallee()) {
            mPlayer = playCalleeRing();
        } else if (isCaller()) {
            mPlayer = playCallerRing();
        }
    }

    private MediaPlayer playCallerRing() {
        return startRinging(R.raw.basic_ring);
    }

    private MediaPlayer playCalleeRing() {
        return startRinging(R.raw.basic_tones);
    }

    private MediaPlayer startRinging(int resource) {
        MediaPlayer player = MediaPlayer.create(this, resource);
        player.setLooping(true);
        player.start();
        return player;
    }

    private void stopRinging() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private class PortraitAnimator {
        static final int ANIM_DURATION = 3000;

        private Animation mAnim1;
        private Animation mAnim2;
        private Animation mAnim3;
        private View mLayer1;
        private View mLayer2;
        private View mLayer3;
        private boolean mIsRunning;

        PortraitAnimator(View layer1, View layer2, View layer3) {
            mLayer1 = layer1;
            mLayer2 = layer2;
            mLayer3 = layer3;
            mAnim1 = buildAnimation(0);
            mAnim2 = buildAnimation(1000);
            mAnim3 = buildAnimation(2000);
        }

        private AnimationSet buildAnimation(int startOffset) {
            AnimationSet set = new AnimationSet(true);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(ANIM_DURATION);
            alphaAnimation.setStartOffset(startOffset);
            alphaAnimation.setRepeatCount(Animation.INFINITE);
            alphaAnimation.setRepeatMode(Animation.RESTART);
            alphaAnimation.setFillAfter(true);

            ScaleAnimation scaleAnimation = new ScaleAnimation(
                    1.0f, 1.3f, 1.0f, 1.3f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(ANIM_DURATION);
            scaleAnimation.setStartOffset(startOffset);
            scaleAnimation.setRepeatCount(Animation.INFINITE);
            scaleAnimation.setRepeatMode(Animation.RESTART);
            scaleAnimation.setFillAfter(true);

            set.addAnimation(alphaAnimation);
            set.addAnimation(scaleAnimation);
            return set;
        }

        void start() {
            if (!mIsRunning) {
                mIsRunning = true;
                mLayer1.setVisibility(View.VISIBLE);
                mLayer2.setVisibility(View.VISIBLE);
                mLayer3.setVisibility(View.VISIBLE);
                mLayer1.startAnimation(mAnim1);
                mLayer2.startAnimation(mAnim2);
                mLayer3.startAnimation(mAnim3);
            }
        }

        void stop() {
            mLayer1.clearAnimation();
            mLayer2.clearAnimation();
            mLayer3.clearAnimation();
            mLayer1.setVisibility(View.GONE);
            mLayer2.setVisibility(View.GONE);
            mLayer3.setVisibility(View.GONE);
        }
    }

}