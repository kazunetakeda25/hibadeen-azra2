package com.azra2.ui.mainfrag;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.azra2.common.YMConst;
import com.azra2.ui.profile.BalancesFragment;
import com.azra2.ui.profile.HistoryFragment;
import com.azra2.ui.profile.MinuteHistoryFragment;
import com.azra2.ui.profile.PaymentHistoryFragment;

public class ProfilePagerAdapter extends FragmentStateAdapter {

    public ProfilePagerAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            /*case 0:
                return new BalancesFragment();*/
            case 1:
                return new PaymentHistoryFragment();
            case 2:
                return new MinuteHistoryFragment();
            default:
                return new BalancesFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
