package com.azra2.ui.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.PreferencesHelper;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMUser;
import com.azra2.model.YMUserResponse;
import com.azra2.ui.MainActivity;
import com.azra2.utils.TinyDB;
import com.azra2.utils.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AuthActivity {

    AppCompatEditText etEmail, etPassword;
    CardView cvLogin;

    private CallbackManager         mFacebookCallbackManager;
    GoogleSignInClient              mGoogleSignInClient;

    private static final int        RC_SIGN_IN = 201;

    String TAG = getClass().getSimpleName();
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setStatusBar();

        etEmail = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        cvLogin = findViewById(R.id.cvLogin);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.message_wait));


        cvLogin.setOnClickListener(view -> login());

        findViewById(R.id.tvSignup).setOnClickListener(view -> signup());

        findViewById(R.id.tvForgot).setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
            startActivity(intent);
        });

//        findViewById(R.id.cvFacebook);
//        findViewById(R.id.llGoogle);

        etEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0 && etPassword.getText().length() > 0) {
                    cvLogin.setCardBackgroundColor(getResources().getColor(R.color.btnEnableColor));
                } else {
                    cvLogin.setCardBackgroundColor(getResources().getColor(R.color.btnDisableColor));
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0 && etEmail.getText().length() > 0) {
                    cvLogin.setCardBackgroundColor(getResources().getColor(R.color.btnEnableColor));
                } else {
                    cvLogin.setCardBackgroundColor(getResources().getColor(R.color.btnDisableColor));
                }
            }
        });

        if (mFacebookCallbackManager == null) {
//            FacebookSdk.sdkInitialize(getApplicationContext());

            mFacebookCallbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.i(TAG, "facebook login success");
                    requestFacebook();
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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
        }else {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void login() {

        String emailOrPhone = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if(Utils.isNullOrEmpty(emailOrPhone)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_email_phone), Toast.LENGTH_SHORT).show();
            etEmail.requestFocus();
            return;
        }

        if(Utils.isNullOrEmpty(password)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_password), Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
            return;
        }

        if (!Utils.getInstance(this).isOnline()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.message_network), Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserResponse> call = service.userLogin(emailOrPhone, password, deviceToken);

        call.enqueue(new Callback<YMUserResponse>() {
            @Override
            public void onResponse(Call<YMUserResponse> call, Response<YMUserResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {
                    YMUserResponse response1 = response.body();
                    if (response1.getResult().equalsIgnoreCase("false")) {
                        Toast.makeText(getApplicationContext(), response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        YMUser user = response1.getData();
                        if(user != null) {
                            //save user info to TinyDB
                            TinyDB tinyDB = new TinyDB(LoginActivity.this);
                            tinyDB.putObject(YMConst.KEY_CURRENT_USER, user);

                            // save user info to Application(to memory)
                            AGApplication.the().setMe(user);

                            //save log info to sharedPreference
                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_STATE, YMConst.LOGGED_IN);
//                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_EMAIL, emailOrPhone);
//                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_PASS, password);
//                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_TOKEN, deviceToken);

                            //go to ...
                            goMainScreen();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<YMUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void signup() {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        LoginActivity.this.finish();
    }

    private void goMainScreen() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        LoginActivity.this.finish();
    }

    protected void facebook_login() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {
            requestFacebook();
        }else {
            LoginManager.getInstance().
                    logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday", "user_gender"));
        }
    }

    /********** Facebook Login ***********/

    private void requestFacebook() {

//        showProgress("Please wait...");

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {

//                dismissProgress();

            if (response.getError() == null) {
                try {

                    Log.i("FacebookUser", object.getString("email"));

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
////                        if (object.has("locale")) newUser.put("locale", object.getString("locale"));
////                        if (object.has("timezone")) newUser.put("timezone", object.getString("timezone"));
//
//                        if (object.has("name")) newUser.put("name", object.getString("name"));
//                        newUser.put("socialname", "facebook");
//
//                        String picturePath = "https://graph.facebook.com/" + object.getString("id") + "/picture?type=large";
//                        Log.e(TAG, "path = " + picturePath);
//
//                        checkUserFromParse(object.getString("email"), picturePath);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
//                    showToast(response.getError().getErrorUserMessage());
            }
        });

        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,email,birthday,first_name,last_name,gender,locale,timezone");
        parameters.putString("fields", "id,name,email,birthday,first_name,last_name,gender");

        request.setParameters(parameters);
        request.executeAsync();
    }

    private void google_login() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

//            account.getEmail();
//            Log.e(TAG, account.getDisplayName());
//            Log.d(TAG, account.getEmail());

//            newUser = new ParseUser();
//            newUser.setUsername(account.getEmail());
//            newUser.setEmail(account.getEmail());
//
//            newUser.setPassword(getResources().getString(R.string.app_name));
//
//            newUser.put("name", account.getDisplayName());
//            newUser.put("socialname", "google");
//
//            String picturePath = "";
//            if (account.getPhotoUrl() != null) {
//                picturePath = account.getPhotoUrl().toString();
//            }
//
//            checkUserFromParse(account.getEmail(), picturePath);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void setStatusBar(){
        ///set status bar color
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.grey_500));
    }
}
