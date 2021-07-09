package com.azra2.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.azra2.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        CardView cardView = findViewById(R.id.cvLogin);
        TextView textView = findViewById(R.id.tvSignup);

        cardView.setOnClickListener(view -> login());
        textView.setOnClickListener(view->signup());
    }

    private void login() {
//        String token = YMFirebaseMessagingService.getToken(getApplicationContext());
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }

    private void signup() {
        startActivity(new Intent(WelcomeActivity.this, SignupActivity.class));
        finish();
    }
}