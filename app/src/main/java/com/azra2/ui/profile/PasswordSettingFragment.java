package com.azra2.ui.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.PreferencesHelper;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMUser;
import com.azra2.model.YMUserResponse;
import com.azra2.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;


public class PasswordSettingFragment extends Fragment {
    YMUser me;
    View rootView;
    EditText etCurrentPassword, etNewPassword, etConfirmPassword;

    public PasswordSettingFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_password_setting, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        me = AGApplication.the().getMe();

        etCurrentPassword = rootView.findViewById(R.id.etCurrentPassword);
        etNewPassword = rootView.findViewById(R.id.etNewPassword);
        etConfirmPassword = rootView.findViewById(R.id.etConfirmPassword);

        rootView.findViewById(R.id.cvDoneBtn).setOnClickListener(v -> {
            changePassword();
        });

    }

    private void changePassword(){

        String oldPass = PreferencesHelper.loadValue(getApplicationContext(), YMConst.LOG_PASS);
        String currentPass, newPass, confirmPass;
        currentPass = etCurrentPassword.getText().toString();
        if(!oldPass.equalsIgnoreCase(currentPass)){
            Toast.makeText(getContext(), "Please input current password again.", Toast.LENGTH_SHORT).show();
            etCurrentPassword.requestFocus();
            return;
        }
        newPass = etNewPassword.getText().toString();
        if(Utils.isNullOrEmpty(newPass)){
            Toast.makeText(getContext(), "Please input new password.", Toast.LENGTH_SHORT).show();
            etNewPassword.requestFocus();
            return;
        }
        confirmPass = etConfirmPassword.getText().toString();
        if(!confirmPass.equalsIgnoreCase(newPass)){
            Toast.makeText(getContext(), "Please confirm new password again.", Toast.LENGTH_SHORT).show();
            etConfirmPassword.requestFocus();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getResources().getString(R.string.message_wait));
        progressDialog.show();

        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserResponse> call = service.userChangePassword(me.getId(),currentPass,newPass, me.getAppToken());
        call.enqueue(new Callback<YMUserResponse>() {
            @Override
            public void onResponse(Call<YMUserResponse> call, Response<YMUserResponse> response) {
                progressDialog.dismiss();
                YMUserResponse response1 = response.body();
                if(response1 != null){
                    if(response1.getResult().equalsIgnoreCase("false")){
                        switch (response.body().getMessage()){
                            case YMConst.MSG_INVALID_TOKEN:
                                Toast.makeText(getContext(), getResources().getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return;
                    }
                    else{
                        YMUser user = response1.getData();
                        if(user != null){
                            AGApplication.the().setMe(user);
                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_PASS, newPass);

                            //go back to profile screen
                            NavController navController = Navigation.findNavController(rootView);
                            NavDirections directions = PasswordSettingFragmentDirections.actionPasswordSettingFragmentToNavProfile();
                            navController.navigate(directions);

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<YMUserResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), getResources().getString(R.string.message_failed_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }
}