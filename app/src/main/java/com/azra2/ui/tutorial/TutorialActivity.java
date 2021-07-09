package com.azra2.ui.tutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.PreferencesHelper;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMUser;
import com.azra2.model.YMUserResponse;
import com.azra2.ui.MainActivity;
import com.azra2.ui.welcome.WelcomeActivity;
import com.azra2.utils.TinyDB;
import com.azra2.utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorialActivity extends FragmentActivity {
    private final static String TAG = TutorialActivity.class.getSimpleName();

    private ViewPager2 viewPager;

    private static final int PAGE_COUNT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        CardView cvStart = findViewById(R.id.cvStart);
        cvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String logstate = PreferencesHelper.loadValue(getApplicationContext(), YMConst.LOG_STATE);
//                String email = PreferencesHelper.loadValue(getApplicationContext(), YMConst.LOG_EMAIL);
//                String password = PreferencesHelper.loadValue(getApplicationContext(), YMConst.LOG_PASS);
//                String deviceToken = PreferencesHelper.loadValue(getApplicationContext(), YMConst.LOG_TOKEN);

                switch (logstate){
                    case YMConst.LOGGED_IN:
                        TinyDB tinyDB = new TinyDB(TutorialActivity.this);
                        YMUser user = (YMUser)tinyDB.getObject(YMConst.KEY_CURRENT_USER, YMUser.class);
                        if(user != null){
                            tokenLogin(user.getId(), user.getAppToken());
                        }
                        break;
                    default:
                        gotoAuthentication();
                        break;
                }
            }
        });

        viewPager = findViewById(R.id.viewPager);

        FragmentStateAdapter pagerAdapter = new TutorialPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tlIndicator);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
            }
        });
        tabLayoutMediator.attach();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private void gotoAuthentication() {
        startActivity(new Intent(TutorialActivity.this, WelcomeActivity.class));
        finish();
    }

    private void gotoMain(){
        //go to MainActivity
        startActivity(new Intent(TutorialActivity.this, MainActivity.class));
        TutorialActivity.this.finish();
    }

    private void tokenLogin(int userId, String apptoken){
        ProgressDialog progressDialog = new ProgressDialog(TutorialActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.message_wait));
        progressDialog.show();
        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMUserResponse> call = service.userTokenLogin(userId, apptoken);

        call.enqueue(new Callback<YMUserResponse>() {
            @Override
            public void onResponse(Call<YMUserResponse> call, Response<YMUserResponse> response) {
                progressDialog.dismiss();
                YMUserResponse response1 = response.body();
                if(response1 != null){
                    if(response1.getResult().equalsIgnoreCase("false")){
                        gotoAuthentication();
                    }
                    else{
                        YMUser user = response1.getData();
                        if(user != null){
                            AGApplication.the().setMe(user);

                            //save my info to tinyDB
                            TinyDB tinyDB = new TinyDB(TutorialActivity.this);
                            tinyDB.putObject(YMConst.KEY_CURRENT_USER, user);

                            //
                            PreferencesHelper.saveValue(getBaseContext(), YMConst.LOG_STATE, YMConst.LOGGED_IN);
//                            PreferencesHelper.saveValue(getBaseContext(), YMConst.LOG_EMAIL, emailOrPhone);
//                            PreferencesHelper.saveValue(getBaseContext(), YMConst.LOG_PASS, password);
//                            PreferencesHelper.saveValue(getBaseContext(), YMConst.LOG_TOKEN, deviceToken);
                            gotoMain();
                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<YMUserResponse> call, Throwable t) {

            }
        });

    }


    private static class TutorialPagerAdapter extends FragmentStateAdapter {

        public TutorialPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int position) {
            return TutorialPageFragment.newInstance(position);
        }

        @Override
        public int getItemCount() {
            return PAGE_COUNT;
        }
    }
}