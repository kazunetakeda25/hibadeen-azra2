package com.azra2.ui.tutorial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.azra2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TutorialPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorialPageFragment extends Fragment {

    private static final String ARG_POSITION = "param_position";
    private int mPosition;

    public TutorialPageFragment() {
        // Required empty public constructor
    }

    public static TutorialPageFragment newInstance(int position) {
        TutorialPageFragment fragment = new TutorialPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorial_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        TextView tvTitle, tvDescription;
        AppCompatImageView ivTutorial;

        tvTitle = view.findViewById(R.id.tvTitle);
        tvDescription = view.findViewById(R.id.tvDescription);
        ivTutorial = view.findViewById(R.id.ivTutorial);

        switch (mPosition) {
            case 0:
                tvTitle.setText(getResources().getString(R.string.buy_coin_gift));
                ivTutorial.setImageResource(R.drawable.onboarding_1_small_v1);
                tvDescription.setText(getResources().getString(R.string.message_tutorial_0));
                break;
            case 1:
                tvTitle.setText(getResources().getString(R.string.menu_public_room));
                ivTutorial.setImageResource(R.drawable.onboarding_2_small_v1);
                tvDescription.setText(getResources().getString(R.string.message_tutorial_1));
                break;
            case 2:
                tvTitle.setText(getResources().getString(R.string.menu_private_room));
                ivTutorial.setImageResource(R.drawable.onboarding_3_small_v1);
                tvDescription.setText(getResources().getString(R.string.message_tutorial_2));
                break;
            case 3:
                tvTitle.setText(getResources().getString(R.string.adult_private_room));
                ivTutorial.setImageResource(R.drawable.onboarding_4_small_v1);
                tvDescription.setText(R.string.message_tutorial_3);
                break;
        }
    }
}