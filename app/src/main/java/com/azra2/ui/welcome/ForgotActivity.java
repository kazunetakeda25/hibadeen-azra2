package com.azra2.ui.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMUser;
import com.azra2.model.YMUserResponse;
import com.azra2.utils.TinyDB;
import com.azra2.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity {

    AppCompatEditText etEmail, etCode0, etCode1, etCode2, etCode3;
    CardView cvSend;
    TextView tvSend;
    LinearLayout llConfirm;
    ProgressDialog progressDialog;
    String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.message_wait));

        cvSend = findViewById(R.id.cvSend);
        tvSend = findViewById(R.id.tvSend);

        etEmail = findViewById(R.id.etEmail);
        etEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0 && Utils.isValidEmail(s)) {
                    cvSend.setCardBackgroundColor(getResources().getColor(R.color.btnEnableColor));
                    tvSend.setText(getResources().getString(R.string.send));
                } else {
                    cvSend.setCardBackgroundColor(getResources().getColor(R.color.btnDisableColor));
                    tvSend.setText(getResources().getString(R.string.send_password));
                }
            }
        });

        findViewById(R.id.tvLogin).setOnClickListener(view -> {
            ForgotActivity.this.finish();
        });

        cvSend.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            if (Utils.isValidEmail(email)) {
                llConfirm.setVisibility(View.VISIBLE);
            }
        });

        etCode0 = findViewById(R.id.etCode0);
        etCode1 = findViewById(R.id.etCode1);
        etCode2 = findViewById(R.id.etCode2);
        etCode3 = findViewById(R.id.etCode3);
        llConfirm = findViewById(R.id.llConfirm);
        llConfirm.setVisibility(View.GONE);

        findViewById(R.id.cvConfirm).setOnClickListener(view -> {
            String newPass = etCode0.getText().toString().trim()
                    + etCode1.getText().toString().trim()
                    + etCode2.getText().toString().trim()
                    + etCode3.getText().toString().trim();

            if (newPass.length() == 4) {
                //need to send code
                forgot(newPass);
//                showSuccessDialog();
            }
        });

        findViewById(R.id.tvResend).setOnClickListener(view -> {
//            ForgotActivity.this.finish();
        });
    }

    private void forgot(String newPassword){

        progressDialog.show();

        String email = etEmail.getText().toString();

        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserResponse> call = service.userForgotPassword(email, newPassword, AGApplication.the().getMe().getAppToken());

        call.enqueue(new Callback<YMUserResponse>() {
            @Override
            public void onResponse(Call<YMUserResponse> call, Response<YMUserResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {
                    YMUserResponse response1 = response.body();
                    if (response1.getResult().equalsIgnoreCase("false")) {
                        switch (response.body().getMessage()){
                            case YMConst.MSG_INVALID_TOKEN:
                                Toast.makeText(ForgotActivity.this, getResources().getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(ForgotActivity.this, getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }else {
                        YMUser user = response1.getData();
//                        TinyDB tinyDB = new TinyDB(ForgotActivity.this);
//                        tinyDB.putObject(YMConst.KEY_CURRENT_USER, user);
                        AGApplication.the().setMe(user);
                        showSuccessDialog();
//                        goMainScreen();
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

    private void goMainScreen() {
        startActivity(new Intent(ForgotActivity.this, LoginActivity.class));
        ForgotActivity.this.finish();
    }

    private void showSuccessDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getResources().getString(R.string.select_avatar));

        final View customLayout = getLayoutInflater().inflate(R.layout.view_password_dialog, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        customLayout.findViewById(R.id.ivClose).setOnClickListener(view -> {
            dialog.dismiss();
            ForgotActivity.this.finish();
        });

        customLayout.findViewById(R.id.tvLogin).setOnClickListener(view -> {
            dialog.dismiss();
            ForgotActivity.this.finish();
        });
    }
}