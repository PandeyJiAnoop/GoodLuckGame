package com.akp.aonestar.PlayBuyModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akp.aonestar.Basic.GameWelcome;
import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.R;

import java.util.Timer;

import pl.droidsonroids.gif.GifImageView;

public class PlayBuyWelcome extends AppCompatActivity {
    GifImageView ivthree;
    private static final int SPLASH_TIME_OUT = 1500;
    int progresscount = 0;
    TextView tvtimeperiod;
    String UserId,UserName;
    TextView name_tv;
    LinearLayout plan_ll;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_buy_welcome);
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();
        plan_ll=findViewById(R.id.plan_ll);
        plan_ll.setVisibility(View.VISIBLE);
        view=findViewById(R.id.view);
        view.setVisibility(View.VISIBLE);


      /*  SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        UserName= sharedPreferences.getString("U_name", "");
        profile_ll=findViewById(R.id.profile_ll);
        name_tv=findViewById(R.id.name_tv);
        if (UserId.equalsIgnoreCase("")){
            name_tv.setText("Login");
        }
        else {
            name_tv.setText(UserName);
        }
        profile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserId.equalsIgnoreCase("")){
                    name_tv.setText("Login");
                    Intent intent=new Intent(getApplicationContext(),LoginScreenAkp.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(getApplicationContext(),SettingScreen.class);
                    startActivity(intent);
                }
            }
        });
*/
        tvtimeperiod=findViewById(R.id.tvtimeperiod);
        tvtimeperiod.setSelected(true);
        ivthree=findViewById(R.id.ivthree);
        ivthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), PlayBuyDashboard.class);
                startActivity(intent);
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progresscount == 100) {
                    /**/
                } else {
                    handler.postDelayed(this, 30);
                    progresscount++;
                }
            }
        }, 200);

        Timer RunSplash = new Timer();
        // Task to do when the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PlayBuyWelcome.this, PlayBuyDashboard.class));
            }
        }, SPLASH_TIME_OUT);
    }

}