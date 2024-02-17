package com.akp.aonestar.Coin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.akp.aonestar.R;

public class CoinPaymnet extends AppCompatActivity {
ImageView sliding_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_paymnet);
        sliding_menu=findViewById(R.id.sliding_menu);
        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }});
    }
}