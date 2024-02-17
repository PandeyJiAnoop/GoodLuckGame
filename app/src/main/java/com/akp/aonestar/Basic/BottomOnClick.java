package com.akp.aonestar.Basic;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.aonestar.PlayBuyModule.ScratchCardManager.ScratchCard;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.WalletReport.WalletHistory;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class BottomOnClick extends RelativeLayout {

    public static final String TAG = HeaderOnClick.class.getSimpleName();

    String UserId, UserName;
  ImageView whatsap,youtube,fb,insta;
  GifImageView details;

    public BottomOnClick(Context context) {
        super(context);
    }

    public BottomOnClick(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomOnClick(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initHeader() {
        inflateHeader();
    }

    private void inflateHeader() {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bottom, this);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        UserName = sharedPreferences.getString("U_name", "");

        whatsap = (ImageView) findViewById(R.id.whatsap);
        youtube = (ImageView) findViewById(R.id.youtube);
        details = (GifImageView) findViewById(R.id.details);
        fb = (ImageView) findViewById(R.id.fb);
        insta = (ImageView) findViewById(R.id.insta);



        ImageView buynow_btn=findViewById(R.id.buynow_btn);
        buynow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GoodLuckGameProductView.class);
                getContext().startActivity(intent);
            }
        });


        whatsap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsNumber = "9155690499"; //without '+'
                String message = "Hello AoneStar  I need a Help?";
                String appPackage = "";
                if (isAppInstalled(getContext(), "com.whatsapp.w4b")) {
                    appPackage = "com.whatsapp.w4b";
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setPackage(appPackage);
// you can remove this part ("&text=" + "your message")
                    String url = "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + message;
                    sendIntent.setData(Uri.parse(url));
                    getContext().startActivity(sendIntent);

                    //do ...
                } else if (isAppInstalled(getContext(), "com.whatsapp")) {
                    appPackage = "com.whatsapp";
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setPackage(appPackage);
// you can remove this part ("&text=" + "your message")
                    String url = "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + message;
                    sendIntent.setData(Uri.parse(url));
                    getContext().startActivity(sendIntent);
                    //do ...
                } else {
                    Toast.makeText(getContext(), "whatsApp is not installed", Toast.LENGTH_LONG).show();
                }


            }
        });


        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://youtube.com/shorts/YNPUJ6iQIrQ?si=M_b1I3mhSYm5jFda";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                getContext().startActivity(i);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(getOpenFacebookIntent());

            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://instagram.com/aoneshop_bobby";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                getContext().startActivity(i);
            }
        });


    }
    public Intent getOpenFacebookIntent() {
        try {
            getContext().getPackageManager().getPackageInfo("com.akp.aonestar", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=61554143731256&mibextid=ZbWKwL"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=61554143731256&mibextid=ZbWKwL"));
        }
    }
    private boolean isAppInstalled(Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
