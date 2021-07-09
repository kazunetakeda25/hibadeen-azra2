package com.azra2.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.PreferencesHelper;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMUser;
import com.azra2.model.YMUserResponse;
import com.azra2.ui.MainActivity;
import com.azra2.ui.welcome.LoginActivity;
import com.azra2.utils.TinyDB;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileSettingsFragment extends Fragment {
    View rootView;
    NavController navController;
    MainActivity mainActivity;

    public ProfileSettingsFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceId)
    {
        mainActivity = (MainActivity)getActivity();
        navController = Navigation.findNavController(rootView);

        view.findViewById(R.id.clToEditProfile).setOnClickListener(view1 -> {
            navController.navigate(ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToEditProfileFragment());
        });

        view.findViewById(R.id.clToAccountSettings).setOnClickListener(view1 -> {
            navController.navigate(ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToAccountSettingsFragment());
        });

        view.findViewById(R.id.clToServiceTerms).setOnClickListener(view1 -> {
            navController.navigate(ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToTermsOfServiceFragment());
        });

        view.findViewById(R.id.clToContactUs).setOnClickListener(view1 -> {
            navController.navigate(ProfileSettingsFragmentDirections.actionProfileSettingsFragmentToContactUsFragment());
        });

        view.findViewById(R.id.clLogOut).setOnClickListener(view1 -> {
            logout();
        });

    }

    private void logout(){
        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserResponse> call = service.userLogout(AGApplication.the().getMe().getId());

        call.enqueue(new Callback<YMUserResponse>() {
            @Override
            public void onResponse(Call<YMUserResponse> call, Response<YMUserResponse> response) {
//                progressDialog.dismiss();

                if (response.body() != null) {
                    YMUserResponse response1 = response.body();
                    if (response1.getResult().equalsIgnoreCase("false")) {
                        Toast.makeText(getApplicationContext(), response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        YMUser user = response1.getData();
                        if(user != null) {
//                            TinyDB tinyDB = new TinyDB(getContext());
//                            tinyDB.putObject(YMConst.KEY_CURRENT_USER, user);
                            AGApplication.the().setMe(user); //save user info

                            //save log info
                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_STATE, YMConst.LOGGED_OUT);
//                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_EMAIL, "");
//                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_PASS, "");
//                            PreferencesHelper.saveValue(getApplicationContext(), YMConst.LOG_TOKEN, "");

                            //clear TinyDB
                            TinyDB tinyDB = new TinyDB(getContext());
                            tinyDB.remove(YMConst.KEY_CURRENT_USER);

                            mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));

                            mainActivity.finish();

                            AGApplication.the().getChatManager().getRtmClient().logout(null);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<YMUserResponse> call, Throwable t) {
//                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Lotout failed due to...", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, t.getMessage());
            }
        });
    }

}