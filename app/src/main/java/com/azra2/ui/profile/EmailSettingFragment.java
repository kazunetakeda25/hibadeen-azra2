package com.azra2.ui.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EmailSettingFragment extends Fragment {
    View rootView;
    YMUser me;
    EditText etEmail;

    public EmailSettingFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_email_setting, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        me = AGApplication.the().getMe();

        etEmail = view.findViewById(R.id.etEmail);
        etEmail.setText(AGApplication.the().getMe().getEmail());

        view.findViewById(R.id.cvDoneBtn).setOnClickListener(v -> {
            changeEmail();
        });

        view.findViewById(R.id.ivBackBtn).setOnClickListener(v -> {

        });
    }

    private void changeEmail(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getResources().getString(R.string.message_wait));
        progressDialog.show();

        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserResponse> call = service.userChangeEmail(me.getId(),etEmail.getText().toString(), me.getAppToken());
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
                    }
                    else{
                        YMUser user = response1.getData();
                        if(user != null){
                            AGApplication.the().setMe(user);
                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_EMAIL, etEmail.getText().toString());

                            //go back to profile screen
                            NavController navController = Navigation.findNavController(rootView);
                            NavDirections directions = EmailSettingFragmentDirections.actionEmailSettingFragmentToNavProfile();
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