package com.akp.aonestar.Basic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.R;
import com.smarteist.autoimageslider.SliderView;

import java.util.Timer;

public class Gamedashboard extends AppCompatActivity {

//    SliderLayout sliderLayout;
    ImageView btnone_img,btn_two_img;
    private static final int SPLASH_TIME_OUT = 1500;
    int progresscount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gamedashboard);
        TextView person_name=findViewById(R.id.person_name);
        person_name.setSelected(true);


//        sliderLayout=findViewById(R.id.imageSlider);
//        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL);
//        sliderLayout.setScrollTimeInSec(3); //set scroll delay in seconds :

        btnone_img=findViewById(R.id.btnone_img);
        btn_two_img=findViewById(R.id.btn_two_img);

        btn_two_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnone_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),GoodLuckGameProductView.class);
                startActivity(intent);

            }
        });

//        setSliderViews();


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
                Intent intent=new Intent(getApplicationContext(),GoodLuckGameProductView.class);
                startActivity(intent);
            }
        }, SPLASH_TIME_OUT);

    }
   /* private void setSliderViews() {
        for (int i = 0; i <= 2; i++) {
            SliderView sliderView = new SliderView(Gamedashboard.this);
            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.ban);
                    sliderView.setDescription("Welcome To Shop It Contest");
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.bb);
//                    sliderView.setDescription("सच होगा सपना");
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.bc);
//                    sliderView.setDescription("सोचो  एक  नयी  दुनिया ");
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);

            final int finalI = i;
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(Gamedashboard.this, "Welcome To Shop It Contest" + (finalI + 1), Toast.LENGTH_SHORT).show();
                }
            });

            //at last add this view in your layout :
            sliderLayout.addSliderView(sliderView);
        }
    }*/
}