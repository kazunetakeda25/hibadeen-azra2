package com.azra2.ui.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.azra2.utils.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class AuthActivity extends AppCompatActivity {

    protected static String         TAG;
    private ProgressDialog          mProgressDlg;

    private CallbackManager         mFacebookCallbackManager;
    private GoogleSignInClient      mGoogleSignInClient;

    private static final int        RC_SIGN_IN = 201;

    protected static String         deviceToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName();

        if (Utils.isNullOrEmpty(deviceToken)) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            deviceToken = task.getResult().getToken();
                            Log.d(TAG, deviceToken);
//                            Toast.makeText(getApplicationContext(), deviceToken, Toast.LENGTH_SHORT).show();
//                            eH8JUdMETbGM0lQ1247QcV:APA91bHgLLlmNQXPOIPYTwsatl46r-cOqAaMGkRtUR8s9dVmczq2NcFwO_uQHvJo1ufSeUlTEZZ3PWizdOPy1Zqv9sNeNFuBSo0CSzFkz_DX6y7ObCkPKNUjYpwmLRYJle1qhgeejjUm
                        }
                    });

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    /*String msg = getString(R.string.msg_token_fmt, token);
                    Log.d(TAG, msg);
                    Toast.makeText(AuthActivity.this, msg, Toast.LENGTH_SHORT).show();*/
                }
            });
        }

        if (mFacebookCallbackManager == null) {
//            FacebookSdk.sdkInitialize(getApplicationContext());
            mFacebookCallbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.i(TAG, "facebook login success");
//                    requestFacebook();
                }

                @Override
                public void onCancel() {
                    Log.i(TAG, "facebook login canceled");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.i(TAG, "facebook login failed");
                }
            });
        }

        if (mGoogleSignInClient == null) {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /********** Facebook Login ***********/
    protected void onFacebookLogin() {

        if (AccessToken.getCurrentAccessToken() != null) {
            requestFacebook();
        }else {
            LoginManager.getInstance().
                    logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_friends", "user_about_me"));
        }

    }

    private void requestFacebook() {

        showProgress("Please wait...");

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback(){
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                dismissProgress();

                if (response.getError() == null) {
                    try {

//                        newUser = new ParseUser();
//                        newUser.setUsername(object.getString("email"));
//                        newUser.setEmail(object.getString("email"));
//
//                        newUser.setPassword(getResources().getString(R.string.app_name));
//
//                        if (object.has("birthday")) newUser.put("birthday", object.getString("birthday"));
//
//                        if (object.has("first_name")) newUser.put("first_name", object.getString("first_name"));
//                        if (object.has("last_name")) newUser.put("last_name", object.getString("last_name"));
//                        if (object.has("gender")) newUser.put("gender", object.getString("gender"));
//
//                        if (object.has("locale")) newUser.put("locale", object.getString("locale"));
//                        if (object.has("timezone")) newUser.put("timezone", object.getString("timezone"));
//
//                        if (object.has("name")) newUser.put("name", object.getString("name"));
//                        newUser.put("socialname", "facebook");

                        String picturePath = "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large";
                        Log.e(TAG, "path = " + picturePath);

//                        checkUserFromParse(object.getString("email"), picturePath);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    showToast(response.getError().getErrorUserMessage());
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,birthday,first_name,last_name,gender,locale,timezone");

        request.setParameters(parameters);
        request.executeAsync();
    }

    /************ Google Login **************/

    private void onGoogleLogin() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            //no need google login : google account is already exist
//            updateUI(account);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showProgress(String title) {
        mProgressDlg = Utils.openNewDialog(this, title, false, false);
    }

    protected void dismissProgress() {
        try {
            if (mProgressDlg != null && mProgressDlg.isShowing())
                mProgressDlg.dismiss();
        } catch (Exception e) {}
    }


}