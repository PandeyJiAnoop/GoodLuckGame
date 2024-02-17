package com.akp.aonestar.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.akp.aonestar.R;
import com.akp.aonestar.SplashActivity;

public class DashBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), SplashActivity.class);
        startActivity(intent);
    }
}