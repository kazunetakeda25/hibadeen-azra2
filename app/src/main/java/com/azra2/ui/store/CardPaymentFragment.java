package com.azra2.ui.store;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.azra2.R;
import com.azra2.utils.Utils;


public class CardPaymentFragment extends Fragment {

    EditText etCardNumber, etOwnerName, etExpDate, etCvc;
    CardView cvTransfer;
    LinearLayout llAddCard;

    public CardPaymentFragment() {
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
        return inflater.inflate(R.layout.fragment_card_payment, container, false);
    }

    @Override
    public void onViewCreated(View parent, Bundle bundle){
        etCardNumber = parent.findViewById(R.id.etCardNumber);
        etOwnerName = parent.findViewById(R.id.etOwnerName);
        etExpDate = parent.findViewById(R.id.etExpDate);
        etCvc = parent.findViewById(R.id.etCvc);
        cvTransfer = parent.findViewById(R.id.cvTransfer);
        llAddCard = parent.findViewById(R.id.llAddCard);

        etCvc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() !=0 && etCardNumber.getText().length() != 0 && etOwnerName.getText().length()!=0 && etExpDate.getText().length()!=0){
                    cvTransfer.setCardBackgroundColor(getResources().getColor(R.color.btnEnableColor));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //transfer
        cvTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transferMoney();
            }
        });

        //add card
        llAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void transferMoney(){
        String cardNumber = etCardNumber.getText().toString();
        String ownerName = etOwnerName.getText().toString();
        String expDate = etExpDate.getText().toString();
        String cvc = etCvc.getText().toString();
        if(Utils.isNullOrEmpty(cardNumber)){
            etCardNumber.requestFocus();
            return;
        }
        if(Utils.isNullOrEmpty(ownerName)){
            etOwnerName.requestFocus();
            return;
        }
        if(Utils.isNullOrEmpty(expDate)){
            etExpDate.requestFocus();
            return;
        }
        if(Utils.isNullOrEmpty(cvc)){
            etCvc.requestFocus();
            return;
        }

        //transfer money


    }

}