package com.azra2.ui.mainfrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.YMConst;
import com.azra2.model.YMUser;
import com.azra2.ui.profile.BalancesFragment;
import com.azra2.ui.profile.HistoryFragment;
import com.azra2.utils.Utils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final int PAGE_COUNT = 3;
    YMUser me;

    ImageView ivProfile;
    TextView tvName, tvNickName, tvEmail, tvPhoneNumber, tvBio;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    View root;
    NavController navController;
    private String[] tabstr = new String[]{"Balances", "Payments History", "Minutes History"};

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        me = AGApplication.the().getMe();
        navController = Navigation.findNavController(root); //*here root == view*//

        ivProfile = root.findViewById(R.id.ivProfile);
        tvName = root.findViewById(R.id.tvName);
        tvNickName = root.findViewById(R.id.tvNickname);
        tvEmail = root.findViewById(R.id.tvEmail);
        tvPhoneNumber = root.findViewById(R.id.tvPhoneNumber);
        tvBio = root.findViewById(R.id.tvBio);

        tabLayout = root.findViewById(R.id.tlProfile);
        viewPager = root.findViewById(R.id.vpProfile);

        tvName.setText(me.getFirstName() + " " + me.getLastName());
        tvNickName.setText(me.getNickName());
        tvEmail.setText(me.getEmail());
        tvPhoneNumber.setText(me.getPhone());
        tvBio.setText(me.getBio());

        String url = me.getProfileImage();
        if(!Utils.isNullOrEmpty(url)){
            Picasso.Builder builder = new Picasso.Builder(getContext());
            builder.downloader(new OkHttp3Downloader(getContext()));
            builder.build().load(url)
                    .placeholder((R.mipmap.ic_launcher_round))
                    .error(R.mipmap.ic_launcher_round)
                    .into(this.ivProfile);
        }

        FragmentStateAdapter pagerAdapter = new ProfilePagerAdapter(ProfileFragment.this);
        viewPager.setAdapter(pagerAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, true,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabstr[position]);
                        viewPager.setCurrentItem(tab.getPosition(), true);
                    }
                });
        tabLayoutMediator.attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        viewPager.getAdapter();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int i = viewPager.getChildCount();
                View child = viewPager.getChildAt(0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        view.findViewById(R.id.ivSettings).setOnClickListener(view1 -> {
            NavDirections action = ProfileFragmentDirections.actionNavProfileToProfileSettingsFragment();
            navController.navigate(action);
        });

        view.findViewById(R.id.clProfileEditing).setOnClickListener(view1->{
            NavDirections action = ProfileFragmentDirections.actionNavProfileToEditProfileFragment();
            navController.navigate(action);
        });

    }


}

