package com.azra2.ui.room;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.azra2.AGApplication;
import com.azra2.common.YMConst;
import com.azra2.model.ChatManager;
import com.azra2.model.YMAgent;
import com.azra2.model.YMUser;
import com.azra2.utils.MessageUtil;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.LocalInvitation;
import io.agora.rtm.RemoteInvitation;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmCallEventListener;
import io.agora.rtm.RtmCallManager;
import io.agora.rtm.RtmClient;

public abstract class BaseCallActivity extends AppCompatActivity implements RtmCallEventListener, ResultCallback<Void> {
    private static final String TAG = BaseCallActivity.class.getSimpleName();
    protected ProgressDialog progressDialog;

    //For call invitation
    protected RtmCallManager mRtmCallManager;
    protected RtmClient mRtmClient;
    protected ChatManager mChatManager;
    protected LocalInvitation mInvitation;
    //
    protected int agUid = -1; //agora user id
    protected int agAid = -1; //agora agent id
    protected YMUser me;
    protected YMAgent selectedAgent;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting...");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLocalInvitationReceivedByPeer(LocalInvitation localInvitation){

    }

    @Override
    public void onLocalInvitationAccepted(LocalInvitation localInvitation, String s) {
    }

    @Override
    public void onLocalInvitationRefused(LocalInvitation localInvitation, String s) {
    }

    @Override
    public void onLocalInvitationCanceled(LocalInvitation localInvitation) {

    }

    @Override
    public void onLocalInvitationFailure(LocalInvitation localInvitation, int i) {

    }

    @Override
    public void onRemoteInvitationReceived(RemoteInvitation remoteInvitation) {

    }

    @Override
    public void onRemoteInvitationAccepted(RemoteInvitation remoteInvitation) {

    }

    @Override
    public void onRemoteInvitationRefused(RemoteInvitation remoteInvitation) {

    }

    @Override
    public void onRemoteInvitationCanceled(RemoteInvitation remoteInvitation) {

    }

    @Override
    public void onRemoteInvitationFailure(RemoteInvitation remoteInvitation, int i) {

    }

    @Override
    public void onSuccess(Void aVoid) {

    }

    @Override
    public void onFailure(ErrorInfo errorInfo) {
    }

    protected void initRtm() {
        mChatManager = AGApplication.the().getChatManager();
        mRtmClient = mChatManager.getRtmClient();
        mRtmCallManager = mRtmClient.getRtmCallManager();
        mRtmCallManager.setEventListener(this);
    }


    /**
     * API CALL: login RTM server
     */
    protected void doRtmLogin() {
        mRtmClient.login(null, String.valueOf(agUid), this);
    }

    /**
     * API CALL: logout from RTM server
     */
    protected void doRtmLogout() {
        mRtmClient.logout(null);
    }

    protected void inviteCall(final String agAid, final String userName, final String channelName) {
        // Creates LocalInvitation
        mInvitation = mRtmCallManager.createLocalInvitation(agAid);
        mInvitation.setContent(userName);
        mInvitation.setChannelId(channelName);
        // Sends call invitation
        mRtmCallManager.sendLocalInvitation(mInvitation, this);
    }

    protected void answerCall(final RemoteInvitation invitation) {
        if (mRtmCallManager != null && invitation != null) {
            mRtmCallManager.acceptRemoteInvitation(invitation, this);
        }
    }

    protected void cancelLocalInvitation() {
        if (mRtmCallManager != null && mInvitation != null) {
            mRtmCallManager.cancelLocalInvitation(mInvitation, this);
        }
    }

    protected void refuseRemoteInvitation(@NonNull RemoteInvitation invitation) {
        if (mRtmCallManager != null) {
            mRtmCallManager.refuseRemoteInvitation(invitation, this);
        }
    }

    public void gotoCallingInterface(String agAid, String userName, String channelName, int role) {
        gotoCallingActivity(agAid, userName, channelName, role);
    }

    protected void gotoCallingActivity(String agAid, String userName, String channelName, int role) {
        Intent intent = new Intent(this, CallActivity.class);
        intent.putExtra(YMConst.AG_AGENT_ID, agAid);
        intent.putExtra(YMConst.USER_NAME, userName);
        intent.putExtra(YMConst.CHANNEL_NAME, channelName);
        intent.putExtra(YMConst.CALLING_ROLE, role);
        startActivity(intent);
    }

    protected void gotoVideoChatActivity(String agAid, String userName, String channelName, int role){
        YMAgent agent = AGApplication.the().getSelectedAgent();
        Intent intentPrivate = new Intent(this, VideoChatViewActivity.class);
        intentPrivate.putExtra(YMConst.ROOM_TYPE, agent.getRoomType());
        intentPrivate.putExtra(YMConst.CALL_TYPE, agent.getCallType());
        intentPrivate.putExtra(YMConst.AG_AGENT_ID, agAid);
        intentPrivate.putExtra(YMConst.AGENT_NAME, agent.getNickName());
        startActivity(intentPrivate);
    }


}
