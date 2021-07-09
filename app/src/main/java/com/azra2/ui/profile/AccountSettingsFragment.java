package com.azra2.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.model.YMUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountSettingsFragment extends Fragment {

    YMUser me;
    TextView tvEmail, tvPassword, tvPhoneNumber;


    public AccountSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_settings, container, false);
    }

    @Override
    public void onViewCreated(View view , Bundle savedInstanceId){
        me = AGApplication.the().getMe();
        NavController navController = Navigation.findNavController(view);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPassword = view.findViewById(R.id.tvPassword);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumber);

        tvEmail.setText(me.getEmail());
//        tvPassword.setText("");
        tvPhoneNumber.setText(me.getPhone());

        view.findViewById(R.id.clEmail).setOnClickListener(view1 -> {
            navController.navigate(AccountSettingsFragmentDirections.actionAccountSettingsFragmentToEmailSettingFragment());
        });

        view.findViewById(R.id.clPassword).setOnClickListener(view1 -> {
            navController.navigate(AccountSettingsFragmentDirections.actionAccountSettingsFragmentToPasswordSettingFragment());
        });

        view.findViewById(R.id.clContactNumber).setOnClickListener(view1 -> {
            navController.navigate(AccountSettingsFragmentDirections.actionAccountSettingsFragmentToCntctNmbrSettingFragment());
        });
    }

}