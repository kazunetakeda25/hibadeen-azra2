package com.azra2.ui.room;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.azra2.AGApplication;
import com.azra2.R;
import com.azra2.common.PreferencesHelper;
import com.azra2.common.YMConst;
import com.azra2.common.YMRetrofit;
import com.azra2.common.YMRetrofitService;
import com.azra2.model.YMAgent;
import com.azra2.model.YMResponse;
import com.azra2.model.YMUser;
import com.azra2.model.YMUserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReviewFragment extends Fragment {
    View root;
    YMAgent selectedAgent;
    private int spicyLevel =0;
    private float starRating = 0.0f;

    ImageView ivBackBtn;
    TextView tvAgentName;
    RatingBar rbStar;
    EditText etReview;
    CardView cvSendReview;
    ImageView pepperGreen, pepperOrange, pepperRed;


    public ReviewFragment() {
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
        root = inflater.inflate(R.layout.fragment_review, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        initField();
        initUi();
    }

    private void initField(){
        selectedAgent = AGApplication.the().getSelectedAgent();
    }

    private void initUi(){
        ivBackBtn = root.findViewById(R.id.ivBackBtn);
        tvAgentName = root.findViewById(R.id.tvAgentName);
        rbStar = root.findViewById(R.id.rbReviewStar);
        etReview = root.findViewById(R.id.etReview);
        cvSendReview = root.findViewById(R.id.cvSendReview);
        pepperGreen = root.findViewById(R.id.pepperGreen);
        pepperOrange = root.findViewById(R.id.pepperOrange);
        pepperRed = root.findViewById(R.id.pepperRed);

        tvAgentName.setText("with " + selectedAgent.getNickName());
        rbStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                starRating = rating;
            }
        });

        cvSendReview.setOnClickListener(v -> {
            leaveReview();
        });

        pepperRed.setOnClickListener(v -> {
            this.spicyLevel = YMConst.SPICY_THREE;
            setPepper(this.spicyLevel);
        });
        pepperOrange.setOnClickListener(v -> {
            this.spicyLevel = YMConst.SPICY_TWO;
            setPepper(this.spicyLevel);
        });
        pepperGreen.setOnClickListener(v -> {
            this.spicyLevel = YMConst.SPICY_ONE;
            setPepper(this.spicyLevel);
        });
    }

    private void setPepper(int spicyLevel){
        switch (spicyLevel){
            case YMConst.SPICY_ONE:
                pepperGreen.setImageDrawable(getResources().getDrawable(R.drawable.pepper_green));
                pepperOrange.setImageDrawable(getResources().getDrawable(R.drawable.pepper_orange_disabled));
                pepperRed.setImageDrawable(getResources().getDrawable(R.drawable.pepper_red_disabled));
                break;
            case YMConst.SPICY_TWO:
                pepperGreen.setImageDrawable(getResources().getDrawable(R.drawable.pepper_green_disabled));
                pepperOrange.setImageDrawable(getResources().getDrawable(R.drawable.pepper_orange));
                pepperRed.setImageDrawable(getResources().getDrawable(R.drawable.pepper_red_disabled));
                break;
            case YMConst.SPICY_THREE:
                pepperGreen.setImageDrawable(getResources().getDrawable(R.drawable.pepper_green_disabled));
                pepperOrange.setImageDrawable(getResources().getDrawable(R.drawable.pepper_orange_disabled));
                pepperRed.setImageDrawable(getResources().getDrawable(R.drawable.pepper_red));
                break;

        }
    }

    private void leaveReview(){
        YMRetrofitService service = YMRetrofit.getRetrofitInstance().create(YMRetrofitService.class);
        Call<YMResponse> call = service.leaveReview(selectedAgent.getId(), AGApplication.the().getMe().getId(),
                starRating, spicyLevel, etReview.getText().toString(), AGApplication.the().getMe().getAppToken());

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("");
        call.enqueue(new Callback<YMResponse>() {
            @Override
            public void onResponse(Call<YMResponse> call, Response<YMResponse> response) {
                progressDialog.dismiss();

                if (response.body() != null) {
                    YMResponse response1 = response.body();
                    if (response1.getResult().equalsIgnoreCase("false")) {
                        switch (response.body().getMessage()){
                            case YMConst.MSG_INVALID_TOKEN:
                                Toast.makeText(getContext(), getResources().getString(R.string.token_invalid), Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getContext(), getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }else {
                        showReviewSentDlg();
                    }
                }
            }

            @Override
            public void onFailure(Call<YMResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showReviewSentDlg(){
        final Dialog dlgReviewSent = new Dialog(getContext());
        dlgReviewSent.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlgReviewSent.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlgReviewSent.setCancelable(false);
        dlgReviewSent.setContentView(R.layout.dialog_review_sent);
        Window window = dlgReviewSent.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView closeBtn = dlgReviewSent.findViewById(R.id.ivClose);

        closeBtn.setOnClickListener(v -> {
            dlgReviewSent.dismiss();
            NavController navController = Navigation.findNavController(root);
            NavDirections directions = ReviewFragmentDirections.actionReviewFragmentToAgentProfileFragment();
            navController.navigate(directions);
        });

        dlgReviewSent.show();
    }


}















