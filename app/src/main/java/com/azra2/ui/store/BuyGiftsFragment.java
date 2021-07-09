package com.azra2.ui.store;

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
 * Use the {@link BuyGiftsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyGiftsFragment extends Fragment {

    View rootView;
    NavController navController;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuyGiftsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyGiftsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyGiftsFragment newInstance(String param1, String param2) {
        BuyGiftsFragment fragment = new BuyGiftsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_buy_gifts, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        navController = Navigation.findNavController(rootView);

        view.findViewById(R.id.cv_into_money_gifts).setOnClickListener(view1 -> {
            NavDirections action = BuyGiftsFragmentDirections.actionBuyGiftsToBuyMoneyGifts();
            navController.navigate(action);
        });
        view.findViewById(R.id.cv_into_coin_gifts).setOnClickListener(view1 -> {
            NavDirections action = BuyGiftsFragmentDirections.actionBuyGiftsToBuyCoinGifts();
            navController.navigate(action);
        });
    }


}