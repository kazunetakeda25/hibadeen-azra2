package com.azra2.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.azra2.R;
import com.azra2.common.YMConst;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class SelectPaymentFragment extends Fragment {

    private boolean isChecked = false;

    private ImageView ivGiftSort;
    private TextView tvContent, tvPrice;
    private RadioGroup rgPayment;
    private RadioButton rbVisa, rbMastercard, rbPaypal, rbGoogle;
    private CardView btnContinue;

    private int mPM = -1; //for payment method


    public SelectPaymentFragment() {
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
        return inflater.inflate(R.layout.fragment_select_payment, container, false);
    }

    @Override
    public void onViewCreated(View parentView, Bundle bundle){
        //initialize UI
        ivGiftSort = parentView.findViewById(R.id.ivGiftSort);
        tvContent = parentView.findViewById(R.id.tvContent);
        tvPrice = parentView.findViewById(R.id.tvPrice);
        rgPayment = parentView.findViewById(R.id.rgPayment);
        rbVisa = parentView.findViewById(R.id.rbVisa);
        rbMastercard = parentView.findViewById(R.id.rbMasterCard);
        rbPaypal = parentView.findViewById(R.id.rbPaypal);
        rbGoogle = parentView.findViewById(R.id.rbGoogle);
        btnContinue = parentView.findViewById(R.id.btnContinue);

        rgPayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(!isChecked) {
                    isChecked = true;
                    btnContinue.setCardBackgroundColor(getResources().getColor(R.color.btnEnableColor));
                }
                switch (checkedId){
                    case R.id.rbVisa:
                        mPM = YMConst.PM_VISA;
                        break;
                    case R.id.rbMasterCard:
                        mPM = YMConst.PM_MASTERCARD;
                        break;
                    case R.id.rbPaypal:
                        mPM = YMConst.PM_PAYPAL;
                        break;
                    case R.id.rbGoogle:
                        mPM = YMConst.PM_GOOGLE;
                        break;
                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(parentView);
                switch (mPM){
                    case YMConst.PM_VISA:
                        //NavDirections action0 = SelectPaymentFragmentDirections.actionSelectPaymentFragmentToCardPaymentFragment();
                        //navController.navigate(action0);
                        break;
                    case YMConst.PM_MASTERCARD:
                        break;
                    case YMConst.PM_PAYPAL:
                        //NavDirections action2 = SelectPaymentFragmentDirections.actionSelectPaymentFragmentToPaypalPaymentFragment();
                        //navController.navigate(action2);
                        break;
                    case YMConst.PM_GOOGLE:
                        break;
                }
            }
        });

    }


}