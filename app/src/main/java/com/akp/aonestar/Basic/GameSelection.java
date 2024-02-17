package com.akp.aonestar.Basic;

import static com.akp.aonestar.GoodLuckModule.FreeTrailProductDetails.RANDOM;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Coin.CoinPaymnet;
import com.akp.aonestar.Ecommerce.E_HomePage;
import com.akp.aonestar.MenuWebLink.Menu_AboutUs;
import com.akp.aonestar.MenuWebLink.Menu_Gallery;
import com.akp.aonestar.PlayBuyModule.PlayBuyDashboard;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class GameSelection extends AppCompatActivity {

    GifImageView ivthree;
    ImageView  ivOne,ivtwow,ivfour;
    TextView person_name;

    String UserId,UserName;
    TextView name_tv;
    LinearLayout profile_ll;
    private TextView tvtimeperiod;
    String SelectGame;
    private PopupWindow popupWindow2,popupWindow_ref,pro_popupWindow2;
    private ImageView offer_img;
    RelativeLayout main_ll;

    MediaPlayer mp;
    ImageView sliding_menu;
    DrawerLayout mDrawer;
    private TextView about_us_tv;
    TextView missiontv,certificatetv,carrertv,gallerytv,customerreviewtv,referralgifttv,privacytv,
            menu_shopit,menu_spot,menu_playbuy,termusetv,shipingreturntv;

    GifImageView whatsap,youtube,fb,insta,chat,phone;
    ImageView gameselection_ban_img,pro_img,pro_sharwahatsap_img,coin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_selection);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();

        main_ll=findViewById(R.id.main_ll);

        ivthree = findViewById(R.id.ivthree);
        ivtwow = findViewById(R.id.ivtwo);
        ivOne = findViewById(R.id.ivOne);
        ivfour = findViewById(R.id.ivfour);
        person_name=findViewById(R.id.person_name);
        person_name.setSelected(true);



        whatsap = findViewById(R.id.whatsap);
        youtube = findViewById(R.id.youtube);
        fb = findViewById(R.id.fb);
        insta = findViewById(R.id.insta);
        chat=findViewById(R.id.chat);
        phone=findViewById(R.id.phone);
        coin=findViewById(R.id.coin);
        gameselection_ban_img=findViewById(R.id.gameselection_ban_img);


        about_us_tv=findViewById(R.id.abouttv);
        missiontv=findViewById(R.id.missiontv);
        certificatetv=findViewById(R.id.certificatetv);
        carrertv=findViewById(R.id.carrertv);
        gallerytv=findViewById(R.id.gallerytv);
        customerreviewtv=findViewById(R.id.customerreviewtv);
        referralgifttv=findViewById(R.id.referralgifttv);
        privacytv=findViewById(R.id.privacytv);
        termusetv=findViewById(R.id.termusetv);
        shipingreturntv=findViewById(R.id.shipingreturntv);


      menu_shopit=findViewById(R.id.menu_shopit);
        menu_spot=findViewById(R.id.menu_spot);
        menu_playbuy=findViewById(R.id.menu_playbuy);


        sliding_menu = (ImageView) findViewById(R.id.sliding_menu);
//        slidingmenu
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//                mDrawer.openDrawer(Gravity.START);
                mDrawer.openDrawer(Gravity.START);
                about_us_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GameSelection.this, Menu_AboutUs.class);
                        intent.putExtra("title","https://aoneshop.co.in/App/About");
                        intent.putExtra("url","About Us");
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
                missiontv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GameSelection.this, Menu_AboutUs.class);
                        intent.putExtra("title","https://aoneshop.co.in/App/Mission");
                        intent.putExtra("url","Mission and Vision");

                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
                certificatetv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GameSelection.this, Menu_AboutUs.class);
                        intent.putExtra("title","https://aoneshop.co.in/App/About");
                        intent.putExtra("url","Certificate");
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
                carrertv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GameSelection.this, Menu_AboutUs.class);
                        intent.putExtra("title","https://aoneshop.co.in/App/Careers");
                        intent.putExtra("url","Member Registration");
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }
                });
                gallerytv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GameSelection.this, Menu_AboutUs.class);
                        intent.putExtra("title","https://aoneshop.co.in/app/gallery");
                        intent.putExtra("url","Gallery");
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }});
                customerreviewtv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        mDrawer.closeDrawer(Gravity.START);
                    }});
                referralgifttv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GameSelection.this, Menu_Gallery.class);
//                        intent.putExtra("title","https://aoneshop.co.in/App/About");
//                        intent.putExtra("url","Referral Gift");
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }});
                privacytv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GameSelection.this, Menu_AboutUs.class);
                        intent.putExtra("title","https://aoneshop.co.in/App/PrivacyPolicy");
                        intent.putExtra("url","Privacy Policy");
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }});
                termusetv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GameSelection.this, Menu_AboutUs.class);
                        intent.putExtra("title","https://aoneshop.co.in/App/TermsUse");
                        intent.putExtra("url","Terms and Use");
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }});
                shipingreturntv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GameSelection.this, Menu_AboutUs.class);
                        intent.putExtra("title","https://aoneshop.co.in/App/ShippingReturn");
                        intent.putExtra("url","Shipping & Return");
                        startActivity(intent);
                        mDrawer.closeDrawer(Gravity.START);
                    }});
                menu_shopit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDrawer.closeDrawer(Gravity.START);
                    }});
                menu_spot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDrawer.closeDrawer(Gravity.START);
                    }});
                menu_playbuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDrawer.closeDrawer(Gravity.START);
                    }});
            } });

        gameselection_ban_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfferBanner();
            }
        });


        if (!UserId.equalsIgnoreCase("")){
            GetClearCartBasicPrimeChoiceList();
        }

//        BlockUnblockAPI();
        mp = MediaPlayer.create(GameSelection.this, R.raw.akp_click);
//        mp1 = MediaPlayer.create(GameSelection.this, R.raw.welcomescreen);
//        mp1.start();
        tvtimeperiod=findViewById(R.id.tvtimeperiod);
        tvtimeperiod.setSelected(true);
//        SharedPreferences sharedPrefs1 = getSharedPreferences("MyPref", 0);
//        long time1 = sharedPrefs1.getLong("displayedTime", 0);
//        if(time1 == 0 || time1 < System.currentTimeMillis() - 259200000) // 259200000 (Millisecond) = 24 Hours
//        {
            OfferBannerSingleProduct();
//            // Show welcome screen
//            SharedPreferences.Editor prefsEditor1 = sharedPrefs1.edit();
//            prefsEditor1.putLong("displayedTime", System.currentTimeMillis()).commit();
//            prefsEditor1.apply();
//        }

        ivOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                SelectGame="1";
                GameblockUnblockStatusAPI("1");
            }
        }); ivtwow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                SelectGame="2";
                GameblockUnblockStatusAPI("2");
//                startActivity(new Intent(getApplicationContext(), PlayBuyWelcome.class));
            }
        }); ivthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               
                mp.start();
                SelectGame="3";
                GameblockUnblockStatusAPI("3");
//                Toast.makeText(getApplicationContext(),"Coming Soon!",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(), PriceDropWelcome.class));
            }
        });

        ivfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), E_HomePage.class));

                mp.start();
                SelectGame="4";
                GameblockUnblockStatusAPI("4");
            }
        });





        whatsap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsNumber = "9155690499"; //without '+'
                String message = "Hello AoneStar  I need a Help?";
                String appPackage = "";
                if (isAppInstalled(GameSelection.this, "com.whatsapp.w4b")) {
                    appPackage = "com.whatsapp.w4b";
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setPackage(appPackage);
// you can remove this part ("&text=" + "your message")
                    String url = "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + message;
                    sendIntent.setData(Uri.parse(url));
                    startActivity(sendIntent);

                    //do ...
                } else if (isAppInstalled(GameSelection.this, "com.whatsapp")) {
                    appPackage = "com.whatsapp";
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setPackage(appPackage);
// you can remove this part ("&text=" + "your message")
                    String url = "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + message;
                    sendIntent.setData(Uri.parse(url));
                    startActivity(sendIntent);
                    //do ...
                } else {
                    Toast.makeText(GameSelection.this, "whatsApp is not installed", Toast.LENGTH_LONG).show();
                }


            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://youtube.com/shorts/YNPUJ6iQIrQ?si=M_b1I3mhSYm5jFda";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getOpenFacebookIntent());

            }
        });
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://instagram.com/aoneshop_bobby";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        coin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(GameSelection.this, CoinPaymnet.class);
               startActivity(intent);
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace phoneNumber with the desired phone number to call
                String phoneNumber = "9155690399"; // Replace this with the phone number you want to call
                // Create an intent with ACTION_DIAL to make a phone call
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                // Check if there is an app available to handle the intent
                if (callIntent.resolveActivity(getPackageManager()) != null) {
                    // Start the activity to initiate the phone call
                    startActivity(callIntent);
                } else {
                    Toast.makeText(GameSelection.this, "No app available to handle this action", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        UserOnlineOfflineAPI(UserId,"2");
        finishAffinity();
}


    public void GameblockUnblockStatusAPI(String id) {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GameblockUnblockStatus", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("wallet","sadsad"+response);
                progressDialog1.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        if (SelectGame.equalsIgnoreCase("1")){
//                            startActivity(new Intent(getApplicationContext(), GameWelcome.class));
                            startActivity(new Intent(getApplicationContext(), GoodLuckGameProductView.class));
                        }
                       else if (SelectGame.equalsIgnoreCase("2")){
                            startActivity(new Intent(getApplicationContext(), PlayBuyDashboard.class));
                        }
                        else if (SelectGame.equalsIgnoreCase("3")){
                            Toast.makeText(getApplicationContext(),"Coming Soon!",Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(getApplicationContext(), PriceDropWelcome.class));
                        }
                       else if (SelectGame.equalsIgnoreCase("4")){
                            startActivity(new Intent(getApplicationContext(), E_HomePage.class));
                        }
                       else
                        {
                            startActivity(new Intent(getApplicationContext(), GoodLuckGameProductView.class));
                        }
                    } else {
                        Toast.makeText(GameSelection.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(GameSelection.this);
                        builder.setTitle("Currently Not Available!")
                                .setMessage(object.getString("Message"))
                                .setCancelable(false)
                                .setIcon(R.drawable.logo)
                                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                      dialog.dismiss(); }});
                        builder.create().show();
                        Toast.makeText(GameSelection.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    } } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(GameSelection.this, "Something Went Wrong:-\n" + error, Toast.LENGTH_SHORT).show();
            }}) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("GameId",id);
                Log.v("addadada", String.valueOf(params));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(GameSelection.this);
        requestQueue.add(stringRequest);

    }







    public void OfferBanner(){
        LayoutInflater layoutInflater1 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView3 = layoutInflater1.inflate(R.layout.dashboard_offer_popup,null);
        ImageView Goback1 = (ImageView) customView3.findViewById(R.id.ib_close);
        offer_img=customView3.findViewById(R.id.offer_img);
        // Now we will call setSelected() method
        // and pass boolean value as true
        //instantiate popup window
        GetSingleOfferBanner();
        popupWindow2 = new PopupWindow(customView3, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //display the popup window
        new Handler().postDelayed(new Runnable(){
            public void run() {
                popupWindow2.showAtLocation(main_ll, Gravity.CENTER, 10, 10);
            }
        }, 100L);
        popupWindow2.setFocusable(true);
        // Settings disappear when you click somewhere else
        popupWindow2.setOutsideTouchable(true);
        popupWindow2.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow2.update();
//        popupWindow2.setBackgroundDrawable(new ColorDrawable(R.color.transparent));
        Goback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow2.dismiss();
            }
        });
        offer_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gameselection_Referral_Popup();
                popupWindow2.dismiss();
            }
        });

    }
    public void GetSingleOfferBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(GameSelection.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+"GamePopuplist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { Log.e("GamePopuplist",""+response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        if (jsonObject1.getString("PopUpImg").equalsIgnoreCase("")){
                        }
                        else {
                            Glide.with(getApplicationContext()).load(jsonObject1.getString("PopUpImg")).error(R.drawable.logo).into(offer_img);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(GameSelection.this, "Something Went Wrong!\n"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(GameSelection.this);
        requestQueue.add(stringRequest);

    }



    public void UserOnlineOfflineAPI(String uid,String uaction) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"UserOnlineOffline", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("dfdff",""+response);
//                Toast.makeText(GameSelection.this, response, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(GameSelection.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(GameSelection.this);
        requestQueue.add(stringRequest);
    }

    public Intent getOpenFacebookIntent() {
        try {
            getPackageManager().getPackageInfo("com.akp.aonestar", 0);
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

    private void Gameselection_Referral_Popup() {
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customViewref = layoutInflater.inflate(R.layout.referral_entry_popup,null);
        ImageView Goback_ref = (ImageView) customViewref.findViewById(R.id.Goback_ref);
        EditText mobenter = (EditText) customViewref.findViewById(R.id.mobenter);
        ImageView sharwahatsap_img=  customViewref.findViewById(R.id.sharwahatsap_img);
        TextView useridenter = customViewref.findViewById(R.id.useridenter);
        useridenter.setText("Your ID Number : - "+ UserId);
        sharwahatsap_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobenter.getText().toString() == null){
                    mobenter.setError("Fields can't be blank!");
                    mobenter.requestFocus();
                }
                else {
                    String smsNumber = "9155690499"; //without '+'
                    String message = mobenter.getText().toString();
                    String appPackage = "";
                    if (isAppInstalled(GameSelection.this, "com.whatsapp.w4b")) {
                        appPackage = "com.whatsapp.w4b";
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.setPackage(appPackage);
// you can remove this part ("&text=" + "your message")
                        String url = "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + message;
                        sendIntent.setData(Uri.parse(url));
                        startActivity(sendIntent);

                        //do ...
                    } else if (isAppInstalled(GameSelection.this, "com.whatsapp")) {
                        appPackage = "com.whatsapp";
                        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                        sendIntent.setPackage(appPackage);
// you can remove this part ("&text=" + "your message")
                        String url = "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + message;
                        sendIntent.setData(Uri.parse(url));
                        startActivity(sendIntent);
                        //do ...
                    } else {
                        Toast.makeText(GameSelection.this, "whatsApp is not installed", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        //instantiate popup window
        popupWindow_ref = new PopupWindow(customViewref, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //display the popup window
        popupWindow_ref.showAtLocation(main_ll, Gravity.CENTER, 0, 0);
        popupWindow_ref.setFocusable(true);
        // Settings disappear when you click somewhere else
        popupWindow_ref.setOutsideTouchable(true);
        popupWindow_ref.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow_ref.update();
        Goback_ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_ref.dismiss();
            }
        });}
    
    public void OfferBannerSingleProduct(){
        LayoutInflater layoutInflater1 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView3 = layoutInflater1.inflate(R.layout.dashboard_singlepro_popup,null);
        ImageView pro_ = (ImageView) customView3.findViewById(R.id.pro_close);
        pro_img=customView3.findViewById(R.id.pro_img);
        pro_sharwahatsap_img=customView3.findViewById(R.id.pro_sharwahatsap_img);
        verifyStoragePermission(GameSelection.this);
        pro_sharwahatsap_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserId.equalsIgnoreCase("")){
                    takeScreenShot(pro_img);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Login First!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Now we will call setSelected() method
        // and pass boolean value as true
        GetSingleProduct();
        //instantiate popup window
        pro_popupWindow2 = new PopupWindow(customView3, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //display the popup window
        new Handler().postDelayed(new Runnable(){
            public void run() {
                pro_popupWindow2.showAtLocation(main_ll, Gravity.CENTER, 10, 10);
            }
        }, 100L);
        pro_popupWindow2.setFocusable(true);
        // Settings disappear when you click somewhere else
        pro_popupWindow2.setOutsideTouchable(true);
        pro_popupWindow2.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        pro_popupWindow2.update();
//        popupWindow2.setBackgroundDrawable(new ColorDrawable(R.color.transparent));
        pro_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro_popupWindow2.dismiss();
            }
        });
        pro_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pro_popupWindow2.dismiss();
            }
        });
    }



    public void GetSingleProduct() {
        final ProgressDialog progressDialog = new ProgressDialog(GameSelection.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+"GamePopupSelllist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GamePopupSelllist",response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        if (jsonObject1.getString("PopUpImg").equalsIgnoreCase("")){
                        }
                        else {
                            Glide.with(getApplicationContext()).load(jsonObject1.getString("PopUpImg")).error(R.drawable.logo).into(pro_img);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(GameSelection.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(GameSelection.this);
        requestQueue.add(stringRequest);

    }



    private void takeScreenShot(View view) {
        String smsNumber = "9155690499"; //without '+'
        Date date = new Date();
        CharSequence format = DateFormat.format("MM-dd-yyyy_hh:mm:ss", date);
        try {
            File mainDir = new File(
                    this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FilShare");
            if (!mainDir.exists()) {
                boolean mkdir = mainDir.mkdir();
            }
            String path = mainDir + "/" + "TrendOceans" + "-" + format + ".jpeg";
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            shareScreenShot(smsNumber,imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Share ScreenShot


    private void shareScreenShot(String phoneNumber, File imageFile) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(this, "com.akp.aonestar.provider", imageFile);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "Download Aonestar From PlayStore \n https://play.google.com/store/apps/details?id=com.akp.aonestar"+"\n\n Customer UserId:- "+UserId);
        intent.setType("image/*");
        intent.setPackage("com.whatsapp.w4b"); // WhatsApp Business package name
        // Set the phone number to which you want to share
        intent.putExtra("jid", phoneNumber + "@s.whatsapp.net"); // Country code + phone number
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "WhatsApp Business not installed.", Toast.LENGTH_SHORT).show();
        }
    }

//Permissions Check
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, };
    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity, PERMISSION_STORAGE,REQUEST_EXTERNAL_STORAGE);}}



    public void GetClearCartBasicPrimeChoiceList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"Shoppit_DeleteCart", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("dfdff",""+response);
//                Toast.makeText(GameSelection.this, response, Toast.LENGTH_SHORT).show();
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GameSelection.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }}) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",UserId);
                return params;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(GameSelection.this);
        requestQueue.add(stringRequest);
    }




}
