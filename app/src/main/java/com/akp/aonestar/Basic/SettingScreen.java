package com.akp.aonestar.Basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Ecommerce.E_OrderList;
import com.akp.aonestar.Home.AddToCart;
import com.akp.aonestar.IncomeReport.DirectIncomeReport;
import com.akp.aonestar.IncomeReport.LevelIncomeReport;
import com.akp.aonestar.IncomeReport.PlanPurchaseReport;
import com.akp.aonestar.MyTeam.MyTotalTeamList;
import com.akp.aonestar.MyTeam.ReferralPersonList;
import com.akp.aonestar.New_ShopIt.BP_SwingTradingProductReport;
import com.akp.aonestar.New_ShopIt.NewShopItOrderList;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.SplashActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingScreen extends AppCompatActivity {
    LinearLayout privacy_policy_ll,logout_ll,help_ll,term_ll,directincome_ll,edit_profile_ll,order_ll,levelincome_ll,
            myorderr_ll,myorderr_ll_shopit,refer_ll,Withdraw_history_ll,tradingProductreport_shopit,ecom_orderlist_shopit;
    String UserId,UserName;
    RelativeLayout header;
    TextView mobile_tv,name_tv,first_name_tv;
    ImageView back_img;

    LinearLayout team_referral_ll,cart_ll,walletTransfer_ll;
    TextView myreferal_tv,my_team_tv,referperson_tv;

    int k=0;

    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_setting_screen);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        UserName= sharedPreferences.getString("U_name", "");
        mobile_tv=findViewById(R.id.mobile_tv);
        name_tv=findViewById(R.id.name_tv);
        first_name_tv=findViewById(R.id.first_name_tv);
        header=findViewById(R.id.header);
        order_ll=findViewById(R.id.order_ll);
        edit_profile_ll=findViewById(R.id.edit_profile_ll);
        refer_ll=findViewById(R.id.refer_ll);
        directincome_ll=findViewById(R.id.directincome_ll);
        levelincome_ll=findViewById(R.id.levelincome_ll);
        term_ll=findViewById(R.id.term_ll);
        privacy_policy_ll=findViewById(R.id.privacy_policy_ll);
        logout_ll=findViewById(R.id.logout_ll);
        myorderr_ll=findViewById(R.id.myorderr_ll);
        myorderr_ll_shopit=findViewById(R.id.myorderr_ll_shopit);
        cart_ll=findViewById(R.id.cart_ll);
        referperson_tv=findViewById(R.id.referperson_tv);
        team_referral_ll=findViewById(R.id.team_referral_ll);
        myreferal_tv=findViewById(R.id.myreferal_tv);
        my_team_tv=findViewById(R.id.my_team_tv);
        walletTransfer_ll=findViewById(R.id.walletTransfer_ll);
        profile_image=findViewById(R.id.profile_image);
        Withdraw_history_ll=findViewById(R.id.Withdraw_history_ll);
        tradingProductreport_shopit=findViewById(R.id.tradingProductreport_shopit);

        ecom_orderlist_shopit=findViewById(R.id.ecom_orderlist_shopit);

        myreferal_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ReferralPersonList.class);
                startActivity(intent);
            }
        });
        my_team_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MyTotalTeamList.class);
                startActivity(intent);
            }
        });

        cart_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AddToCart.class);
                startActivity(intent);
            }
        });

        Withdraw_history_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), WithdrawlRequestReportScreen.class);
                startActivity(intent);
            }
        });

        walletTransfer_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), WalletTransferHistory.class);
                startActivity(intent);
            }
        });
        GetProfile();

        referperson_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (k == 0) {
                    team_referral_ll.setVisibility(View.GONE);
                    k++;
                } else if (k == 1) {
                    team_referral_ll.setVisibility(View.VISIBLE);
                    k = 0;
                }
            }
        });

        privacy_policy_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://aoneshop.co.in/privacypolicy.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        refer_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ReferEarnScreen.class);
                startActivity(intent);

            }
        });

        myorderr_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MyOrderList.class);
                startActivity(intent);
            }
        });
        myorderr_ll_shopit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), NewShopItOrderList.class);
                startActivity(intent);
            }
        });

        tradingProductreport_shopit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), BP_SwingTradingProductReport.class);
                startActivity(intent);
            }
        });
        edit_profile_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),EditProfile.class);
                startActivity(intent);
            }
        });

        directincome_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DirectIncomeReport.class);
                startActivity(intent);
            }
        });

        levelincome_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LevelIncomeReport.class);
                startActivity(intent);
            }
        });
        order_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), PlanPurchaseReport.class);
                startActivity(intent);
            }
        });


        back_img=findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logout_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SettingScreen.this);
                builder.setMessage(Html.fromHtml("<font color='#000'>Are you sure want to Logout?</font>"));
                builder.setCancelable(false);
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UserOnlineOfflineAPI(UserId,"2");
                        SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefs.edit();
                        editor.clear();
                        editor.commit();
                        SharedPreferences myPrefs1 = getSharedPreferences("refer_preference", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = myPrefs1.edit();
                        editor1.clear();

                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                        startActivity(intent);
                    }
                });
                android.app.AlertDialog alert = builder.create();
                alert.show();
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nbutton.setBackgroundColor(Color.BLACK);
                nbutton.setTextColor(Color.WHITE);
                Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setBackgroundColor(Color.RED);
                pbutton.setTextColor(Color.WHITE);
            }
        });

        ecom_orderlist_shopit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), E_OrderList.class);
                startActivity(intent);
            }});
    }


    public void GetProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "ProfileView", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            String UserID       = jsonobject.getString("UserID");
                            String MemberName    = jsonobject.getString("MemberName");
                            String MobileNo      = jsonobject.getString("MobileNo");
                            String EmaiLID       = jsonobject.getString("EmaiLID");
                            String RefrerralId       = jsonobject.getString("RefrerralId");
                            String RegDate    = jsonobject.getString("RegDate");

                            String state       = jsonobject.getString("State");
                            String city       = jsonobject.getString("City");
                            String pinCode    = jsonobject.getString("PinCode");
                            name_tv.setText(MemberName+"("+UserID+")");
                            mobile_tv.setText(MobileNo);
                            first_name_tv.setText(MemberName);
                            if (jsonobject.getString("ProfilePic").equalsIgnoreCase("")){
                            }
                            else {
                                Glide.with(SettingScreen.this).load(jsonobject.getString("ProfilePic")).into(profile_image);
                            }
                            Toast.makeText(getApplicationContext(),object.getString("Msg"),Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(SettingScreen.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SettingScreen.this);
        requestQueue.add(stringRequest);
    }







    public void UserOnlineOfflineAPI(String uid,String uaction) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"UserOnlineOffline", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("dfdff",""+response);
//                Toast.makeText(LoginScreenAkp.this, response, Toast.LENGTH_SHORT).show();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray resjsn = object.getJSONArray("Response");
                        for (int i = 0; i < resjsn.length(); i++) {
                            JSONObject jsonobject = resjsn.getJSONObject(i);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingScreen.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",uid);
                params.put("Action",uaction);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SettingScreen.this);
        requestQueue.add(stringRequest);
    }

}
