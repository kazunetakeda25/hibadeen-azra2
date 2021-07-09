package com.azra2.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.azra2.AGApplication;
import com.azra2.Config.Config;
import com.azra2.R;
import com.azra2.common.PreferencesHelper;
import com.azra2.common.YMConst;
import com.azra2.ui.MainActivity;
import com.azra2.ui.welcome.LoginActivity;
import com.azra2.utils.TinyDB;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Map;

public class YMFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = getClass().getSimpleName();

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "From: " + remoteMessage.getFrom());
//        activityName = Utils.getCurrenActivityName(this);

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            RemoteMessage.Notification notf = remoteMessage.getNotification();
            String title = notf.getTitle();
            String body = notf.getBody();

            if (remoteMessage.getData().size() > 0) {
                Map<String, String> data = remoteMessage.getData();
                String value = data.get(YMConst.NTF_TYPE);

                if (value.equalsIgnoreCase(YMConst.NTF_LOGOUT)) {
                    //save log info
                    PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_STATE, YMConst.LOGGED_OUT);
//                    PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_EMAIL, "");
//                    PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_PASS, "");
//                    PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_TOKEN, "");

                    //clear TinyDB
                    TinyDB tinyDB = new TinyDB(getApplicationContext());
                    tinyDB.remove(YMConst.KEY_CURRENT_USER);

                    try { //when app is foreground
                        Intent intent = null;
                        intent = new Intent(this, LoginActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                                PendingIntent.FLAG_CANCEL_CURRENT);

                        showNotification(title, body, pendingIntent);

                        AGApplication.the().getChatManager().getRtmClient().logout(null);

                    } catch (Exception ex) {
                        Log.i("uniq", ex.getMessage());
                    }

                }
                else if(value.equalsIgnoreCase(YMConst.NTF_AGENTLOGIN))
                {
                    try { //when app is foreground
                        Intent intent = null;
                        intent = new Intent(this, MainActivity.class);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                                PendingIntent.FLAG_CANCEL_CURRENT);

                        showNotification(title, body, pendingIntent);

                        AGApplication.the().getChatManager().getRtmClient().logout(null);

                    } catch (Exception ex) {
                        Log.i("uniq", ex.getMessage());
                    }
                }

            }

        /*// Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.i(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                //boolean from_bg = Boolean.parseBoolean(remoteMessage.getData().get("from_bg").toString());
                Map<String, String> data = remoteMessage.getData();
                boolean show_fg = Boolean.parseBoolean(data.get("show_fg"));

                activityName = Utils.getCurrentActivityName(this);

                if (activityName.contains(LoginActivity.class.getSimpleName())) {
                    show_fg = true;
                }

                if (show_fg) {
                    String event = data.get("Event-Name");
                    String title = data.get("title");
                    String body = data.get("body");
                    if (event != null) {
                        if (event.equals("EVENT_NAME")) {
                            try {
                                JSONObject object = new JSONObject(data);
                                if (object != null) {
                                    ((BaseActivity) BaseActivity.context).insertChatUser(object, true);
                                }
                                sendNotificationWithChatID(title, body, remoteMessage.getData().get("chat_id"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        /*if(remoteMessage.getData().size() > 0){

            Map<String, String> data = remoteMessage.getData();
            String userId = data.get("user_id");
            String agentId = data.get("agent_id");
            String userName = data.get("user_name");
            String channelName = data.get("channel_name");
            String roomType = data.get("room_type");

            Intent dialogIntent = new Intent(this, NotificationActivity.class);
//        dialogIntent.putExtra("msg", remoteMessage);
            dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra("channelName", channelName);
            dialogIntent.putExtra("userId", userId);
            startActivity(dialogIntent);
        }*/

        }
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }

    private void showNotification(String title, String messageBody, PendingIntent pendingIntent){
//        Uri customSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.rehq_brand);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
//                .setSound(customSound)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void getImage(final RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        Config.title = data.get("title");
        Config.content = data.get("content");
        Config.imageUrl = data.get("imageUrl");
        Config.gameUrl = data.get("gameUrl");
        //Create thread to fetch image from notification
        if(remoteMessage.getData()!=null){

            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Get image from data Notification
                    Picasso.get()
                            .load(Config.imageUrl)
                            .into(target);
                }
            }) ;
        }
    }

}
