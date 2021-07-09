package com.azra2.ui.mainfrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.azra2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment {

    View rootView;
    NavController navController;

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_store, container, false);
        initUI(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        navController = Navigation.findNavController(rootView);

        view.findViewById(R.id.cv_into_buy_coins).setOnClickListener(view1 -> {
            NavDirections action = StoreFragmentDirections.actionStoreToBuyCoins();
            navController.navigate(action);
        });
        view.findViewById(R.id.cv_into_buy_gifts).setOnClickListener(view1 -> {
            NavDirections action = StoreFragmentDirections.actionStoreToBuyGifts();
            navController.navigate(action);
        });

    }

    private void initUI(View view) {

    }
}
