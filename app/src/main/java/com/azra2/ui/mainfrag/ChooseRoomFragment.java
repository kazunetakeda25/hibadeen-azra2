package com.azra2.ui.mainfrag;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.azra2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseRoomFragment extends Fragment {

    View root;
    NavController navController;

    int roomType = -1;
    int callType = -1;


    public ChooseRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_choose_room, container, false);
        return root;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        navController = Navigation.findNavController(root);

        view.findViewById(R.id.cv_into_public_room).setOnClickListener(view1 -> {
            NavDirections action = ChooseRoomFragmentDirections.actionNavChooseRoomToAgentListFragment(1);
            navController.navigate(action);
//            showChoiceCallTypeDlg(1);
        });
        view.findViewById(R.id.cv_into_private_room).setOnClickListener(view1 -> {
            NavDirections action = ChooseRoomFragmentDirections.actionNavChooseRoomToAgentListFragment(2);
            navController.navigate(action);
//            showChoiceCallTypeDlg(2);
        });
        view.findViewById(R.id.cv_into_adult_private_room).setOnClickListener(view1 -> {
            NavDirections action = ChooseRoomFragmentDirections.actionNavChooseRoomToAgentListFragment(3);
            navController.navigate(action);
//            showChoiceCallTypeDlg(3);
        });
    }

    private void showChoiceCallTypeDlg(int rType){
        final Dialog dlgChoiceCallType = new Dialog(getActivity());
        dlgChoiceCallType.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlgChoiceCallType.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgChoiceCallType.setCancelable(false);
        dlgChoiceCallType.setContentView(R.layout.dialog_call_type_selection);
        Window window = dlgChoiceCallType.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView videoBtn = dlgChoiceCallType.findViewById(R.id.tvVideocallBtn);
        TextView voiceBtn = dlgChoiceCallType.findViewById(R.id.tvVoicecallBtn);

        voiceBtn.setOnClickListener(v1 -> {
            this.roomType = rType;
            this.callType = 0; //voicecall
            NavDirections action = ChooseRoomFragmentDirections.actionNavChooseRoomToAgentListFragment(roomType);
            navController.navigate(action);
            dlgChoiceCallType.dismiss();
        });
        videoBtn.setOnClickListener(v1 -> {
            this.roomType = rType;
            callType = 1; //videocall
            NavDirections action = ChooseRoomFragmentDirections.actionNavChooseRoomToAgentListFragment(roomType);
            navController.navigate(action);
            dlgChoiceCallType.dismiss();
        });
        dlgChoiceCallType.show();
    }


}