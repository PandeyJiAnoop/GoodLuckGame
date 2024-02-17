package com.akp.aonestar.PriceDropModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akp.aonestar.Basic.GameSelection;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.R;

import java.util.Timer;

public class PriceDropWelcome extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 1500;
    int progresscount = 0;
    TextView tvtimeperiod;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_price_drop_welcome);
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();

        progressBar=findViewById(R.id.progressBar);
        progressBar.setProgress(progresscount);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progresscount == 100) {
                    /**/
                } else {
                    handler.postDelayed(this, 10);
                    progresscount++;
                    progressBar.setProgress(progresscount);
                }
            }
        }, 100);

        Timer RunSplash = new Timer();
        // Task to do when the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PriceDropWelcome.this, PriceDropDashboard.class));
            }
        }, SPLASH_TIME_OUT);


    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), GameSelection.class);
        startActivity(intent);
    }
}

