package com.akp.aonestar.New_ShopIt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.GameSelection;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.Utility;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.auth.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SCANQRANDSUBMITDETAILS extends AppCompatActivity {
    ImageView menuImg;
    String UserId;
    ImageView image_qr;
    EditText amount_et,transcation_number_et;
    AppCompatButton submit_btn;
    TextView userid_et,betno_et;
    String getProCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanqrandsubmitdetails);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        getProCode=getIntent().getStringExtra("pro_code");

        menuImg=findViewById(R.id.menuImg);
        image_qr=findViewById(R.id.image_qr);
        amount_et=findViewById(R.id.amount_et);
        submit_btn=findViewById(R.id.Add_button);
        userid_et=findViewById(R.id.userid_et);
        transcation_number_et=findViewById(R.id.transcation_number_et);
        betno_et=findViewById(R.id.betno_et);

        Glide.with(SCANQRANDSUBMITDETAILS.this)
                .load("https://aoneshop.co.in/assets/Qr_AoneShop.png").error(R.drawable.a_logo)
                .into(image_qr);

        userid_et.setText(UserId);
        betno_et.setText(getProCode);


        amount_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (amount_et.getText().toString().equalsIgnoreCase("")) {
                    amount_et.setError("Please enter amount \n Greater than 10");
                    Toast.makeText(getApplicationContext(),"Please enter amount Greater than 10",Toast.LENGTH_LONG).show();
                } else {
                    if ((Long.parseLong(amount_et.getText().toString().trim()) < 10) || (amount_et.length() == 0) /*||(TextUtils.isEmpty(ets2.getText().toString()))*/) {
                        amount_et.setError("Please enter amount \n Greater than 10");
                    } else {
                        submit_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (amount_et.getText().toString().equalsIgnoreCase("")){
                                    amount_et.setError("Fields can't be blank!");
                                    amount_et.requestFocus();
//                    Toast.makeText(getApplicationContext()," wait for response",Toast.LENGTH_LONG).show();
                                }
                               else if (transcation_number_et.getText().toString().equalsIgnoreCase("")){
                                    transcation_number_et.setError("Fields can't be blank!");
                                    transcation_number_et.requestFocus();
//                    Toast.makeText(getApplicationContext()," wait for response",Toast.LENGTH_LONG).show();
                                }
                                else if (betno_et.getText().toString().equalsIgnoreCase("")){
                                    betno_et.setError("Fields can't be blank!");
                                    betno_et.requestFocus();
//                    Toast.makeText(getApplicationContext()," wait for response",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    String smsNumber = "9155690499"; //without '+'
                                    String message = "UserId:- "+UserId+"\nBet Amount:- "+amount_et.getText().toString()+"\nTransaction No:- "+transcation_number_et.getText().toString()+"\nBetNo:- "+betno_et.getText().toString();
                                    String appPackage = "";
                                    if (isAppInstalled(SCANQRANDSUBMITDETAILS.this, "com.whatsapp.w4b")) {
                                        appPackage = "com.whatsapp.w4b";
                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                        sendIntent.setPackage(appPackage);
// you can remove this part ("&text=" + "your message")
                                        String url = "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + message;
                                        sendIntent.setData(Uri.parse(url));
                                        startActivity(sendIntent);
                                        //do ...
                                    } else if (isAppInstalled(SCANQRANDSUBMITDETAILS.this, "com.whatsapp")) {
                                        appPackage = "com.whatsapp";
                                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                        sendIntent.setPackage(appPackage);
// you can remove this part ("&text=" + "your message")
                                        String url = "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + message;
                                        sendIntent.setData(Uri.parse(url));
                                        startActivity(sendIntent);
                                        //do ...
                                    } else {
                                        Toast.makeText(SCANQRANDSUBMITDETAILS.this, "whatsApp is not installed", Toast.LENGTH_LONG).show();
                                    } }}});
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });



        submit_btn.setOnClickListener(v -> {
            if (amount_et.getText().toString().equalsIgnoreCase("")) {
                amount_et.setError("Please enter amount \n Greater than 10");
                Toast.makeText(getApplicationContext(),"Please enter amount Greater than 10",Toast.LENGTH_LONG).show();
            } else {
                if ((Long.parseLong(amount_et.getText().toString().trim()) < 10) || (amount_et.length() == 0) /*||(TextUtils.isEmpty(ets2.getText().toString()))*/) {
                    amount_et.setError("Please enter amount \n Greater than 10");
                } else {
                    submit_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (amount_et.getText().toString().equalsIgnoreCase("")) {
                                amount_et.setError("Fields can't be blank!");
                                amount_et.requestFocus();
//                    Toast.makeText(getApplicationContext()," wait for response",Toast.LENGTH_LONG).show();
                            } else if (transcation_number_et.getText().toString().equalsIgnoreCase("")) {
                                transcation_number_et.setError("Fields can't be blank!");
                                transcation_number_et.requestFocus();
//                    Toast.makeText(getApplicationContext()," wait for response",Toast.LENGTH_LONG).show();
                            } else if (betno_et.getText().toString().equalsIgnoreCase("")) {
                                betno_et.setError("Fields can't be blank!");
                                betno_et.requestFocus();
//                    Toast.makeText(getApplicationContext()," wait for response",Toast.LENGTH_LONG).show();
                            } else {
                                String smsNumber = "9155690499"; //without '+'
                                String message = "UserId:- " + UserId + "\nBet Amount:- " + amount_et.getText().toString() + "\nTransaction No:- " + transcation_number_et.getText().toString() + "\nBetNo:- " + betno_et.getText().toString();
                                String appPackage = "";
                                if (isAppInstalled(SCANQRANDSUBMITDETAILS.this, "com.whatsapp.w4b")) {
                                    appPackage = "com.whatsapp.w4b";
                                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                    sendIntent.setPackage(appPackage);
// you can remove this part ("&text=" + "your message")
                                    String url = "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + message;
                                    sendIntent.setData(Uri.parse(url));
                                    startActivity(sendIntent);
                                    //do ...
                                } else if (isAppInstalled(SCANQRANDSUBMITDETAILS.this, "com.whatsapp")) {
                                    appPackage = "com.whatsapp";
                                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                    sendIntent.setPackage(appPackage);
// you can remove this part ("&text=" + "your message")
                                    String url = "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + message;
                                    sendIntent.setData(Uri.parse(url));
                                    startActivity(sendIntent);
                                    //do ...
                                } else {
                                    Toast.makeText(SCANQRANDSUBMITDETAILS.this, "whatsApp is not installed", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }}});

        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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