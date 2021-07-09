package com.azra2.ui.room;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.azra2.R;

public class CallEndedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_ended);
    }

    public void onEndClicked(View view){
        finish();
    }
}