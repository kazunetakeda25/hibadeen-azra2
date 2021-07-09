package com.azra2;

import android.app.Application;

import com.azra2.model.ChatManager;
import com.azra2.model.YMAgent;
import com.azra2.model.YMUser;

import java.sql.Time;

import io.agora.rtm.RtmCallManager;

public class AGApplication extends Application {
    private static AGApplication sInstance;
    private ChatManager mChatManager;
    private RtmCallManager rtmCallManager;
    private YMUser me;
    private YMAgent selectedAgent;

    //coin purchase
    public int purchasedCoinCount = 0;
    public String purchaseTime = "1 January 2020, 00:00";
    public double purchagePrice = 0.0;

    public static AGApplication the() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mChatManager = new ChatManager(this);
        mChatManager.init();

//        rtmCallManager = mChatManager.getRtmCallManager();
//        rtmCallManager.setEventListener(new RtmCallEventListener() {
//            @Override
//            public void onLocalInvitationReceivedByPeer(LocalInvitation localInvitation) {
//
//            }
//
//            @Override
//            public void onLocalInvitationAccepted(LocalInvitation localInvitation, String s) {
//
//            }
//
//            @Override
//            public void onLocalInvitationRefused(LocalInvitation localInvitation, String s) {
//
//            }
//
//            @Override
//            public void onLocalInvitationCanceled(LocalInvitation localInvitation) {
//
//            }
//
//            @Override
//            public void onLocalInvitationFailure(LocalInvitation localInvitation, int i) {
//
//            }
//
//            @Override
//            public void onRemoteInvitationReceived(RemoteInvitation remoteInvitation) {
//            }
//
//            @Override
//            public void onRemoteInvitationAccepted(RemoteInvitation remoteInvitation) {
//
//            }
//
//            @Override
//            public void onRemoteInvitationRefused(RemoteInvitation remoteInvitation) {
//
//            }
//
//            @Override
//            public void onRemoteInvitationCanceled(RemoteInvitation remoteInvitation) {
//
//            }
//
//            @Override
//            public void onRemoteInvitationFailure(RemoteInvitation remoteInvitation, int i) {
//
//            }
//        });
    }

    public void setMe(YMUser user){
        this.me = user;
    }

    public YMUser getMe(){
        return this.me;
    }

    public void setSelectedAgent(YMAgent agent){
        this.selectedAgent = agent;
    }

    public YMAgent getSelectedAgent(){
        return this.selectedAgent;
    }

    public ChatManager getChatManager() {
        return mChatManager;
    }
}

