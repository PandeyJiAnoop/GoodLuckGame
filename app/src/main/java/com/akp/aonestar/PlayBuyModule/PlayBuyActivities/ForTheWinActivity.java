package com.akp.aonestar.PlayBuyModule.PlayBuyActivities;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.akp.aonestar.Basic.ForgotPassword;
import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Basic.Oder_Summery_Details;
import com.akp.aonestar.GoodLuckModule.AdapterForBanner;
import com.akp.aonestar.GoodLuckModule.AnimationHelper;
import com.akp.aonestar.GoodLuckModule.BannerData;
import com.akp.aonestar.GoodLuckModule.ChooseDiscountProduct;
import com.akp.aonestar.PlayBuyModule.NetworkManager.DialogManager;
import com.akp.aonestar.PlayBuyModule.NetworkManager.InternetConnection;
import com.akp.aonestar.PlayBuyModule.NetworkManager.MyVolleySingleton;
import com.akp.aonestar.PlayBuyModule.NetworkManager.WebServices;
import com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors.AdaptorImageList;
import com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors.AdaptorImageSlider;
import com.akp.aonestar.PlayBuyModule.PlayBuyDashboard;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Banner.BannerModel;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Category.DataResponseCategory;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Category.ResponseItem;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ForTheWinNumberList.DataResponseNumbers;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ModelImg;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt.DataResponseWinAmount;
import com.akp.aonestar.PlayBuyModule.PlayBuy_Oder_Summery_Details;
import com.akp.aonestar.PlayBuyModule.SundaySpecial;
import com.akp.aonestar.PriceDropModule.PriceDropDashboard;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class ForTheWinActivity extends AppCompatActivity implements View.OnClickListener {
    AdaptorImageList adaptorImageList;
    RecyclerView recyclerDummyNumList, recyclerPickNum, recyclerCategoryList, recyclerBuyNumber, recyclerValueAmt;
    AdaptorNumberList adaptorNumberList;
    AdaptorPickNumList adaptorPickNumList;
    LinearLayout linearForPlay;
    String cateName, categoryId;
    ForTheWinActivity.AdaptorMainCategoryList adaptorMainCategoryList;
    AdaptorImageSlider adaptorImageSlider;
    AdaptorForTheWinAmountList adaptorForTheWinAmountList;
    AdaptorForTheWinValueAmt adaptorForTheWinValueAmt;
    private int selectedPosition = -1;
    private int selectedPosition1 = -1;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;
    final int time = 4000; // it's the delay time for sliding between items in recyclerview
    TextView tvSelectedNum;

    Dialog alertDialog, alertDialog1;
    List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt.ResponseItem> itemList = new ArrayList<>();
    String selectedBitAmt = "", selectedBitAmtValue = "", dummyNum = "", WinAmount="";
    AdaptorPopupSelectAmt adaptorPopupSelectAmt;

    ProgressDialog progressDialog;
    boolean dummyAmt = false;
    FloatingActionButton close;
    private String UserId;
    private AlertDialog alertDialog_loos,alertDialog_win;
    private AlertDialog alertDialog2;


    RecyclerView tranding_rec,wallet_histroy;
    public static AppCompatButton previousSeletedButton = null;
    private ImageView txt_nodata;
    private Dialog dialog;
    int i=0;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
//    private DashboardAdapter customerListAdapter;
CirclePageIndicator indicator;
    ViewPager pager;
    List<BannerData> bannerData = new ArrayList<>();
    private static int currentPage = 0;
    ImageView norecord_tv,norecord_tv1,norecord_tv2,norecord_tv3;
    ArrayList<HashMap<String, String>> arrayList5 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList6 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList7 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList8 = new ArrayList<>();
    RecyclerView recyclerProductList, recyclerDisProductList1, recyclerDisProductList2,
            recyclerDisProductList3;
    LottieAnimationView animationView;


    ArrayList<HashMap<String, String>> arrayListwin = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayListloss = new ArrayList<>();
    RecyclerView recyclerTossMatchAmt, recyclerTossMatchValue;
    AdaptorMatchGetNum adaptorMatchGetNum;
   AdaptorMatchGetNumValues adaptorMatchGetNumValues;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_the_win);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        mp = MediaPlayer.create(ForTheWinActivity.this, R.raw.akp_click);
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();
        GifImageView gl_img = findViewById(R.id.gl_img);
        GifImageView pd_img = findViewById(R.id.pd_img);
        GifImageView spot_img = findViewById(R.id.spot_img);
        animationView = findViewById(R.id.animationView);
        gl_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), GoodLuckGameProductView.class);
                startActivity(intent);
            }
        });
        pd_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), PriceDropDashboard.class);
                startActivity(intent);
            }
        });
        spot_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming Soon!",Toast.LENGTH_LONG).show();
            }
        });
        GifImageView sunday_sp = findViewById(R.id.sunday_sp);
        sunday_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SundaySpecial.class);
                startActivity(intent);
            }
        });
        LinearLayoutCompat FormetchandGet=findViewById(R.id.gamedetails_ll);
        FormetchandGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductDetails();
//                FormetchandGet_popup();
            }
        });


        initViews();
        GetWinnerList("Win");
        GetLooserList("Loss");
    }

    private void initViews() {
        progressDialog = new ProgressDialog(this);
        tvSelectedNum = findViewById(R.id.tvSelectedNum);
        close = findViewById(R.id.close);
        recyclerCategoryList = findViewById(R.id.recyclerCategoryList);
        recyclerDummyNumList = findViewById(R.id.recyclerDummyNumList);
        recyclerPickNum = findViewById(R.id.recyclerPickNum);
        recyclerBuyNumber = findViewById(R.id.recyclerBuyNumber);
        recyclerValueAmt = findViewById(R.id.recyclerValueAmt);
        linearForPlay = findViewById(R.id.linearForPlay);

        close.setOnClickListener(this);
        linearForPlay.setOnClickListener(this);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager1);
        recyclerDummyNumList.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerDummyNumList.setHasFixedSize(true);
        recyclerPickNum.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerPickNum.setHasFixedSize(true);

        recyclerCategoryList.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategoryList.setHasFixedSize(true);
        /*recyclerBuyNumber.setLayoutManager(new LinearLayoutManager(this));
        recyclerBuyNumber.setHasFixedSize(true);
        recyclerValueAmt.setLayoutManager(new LinearLayoutManager(this));
        recyclerValueAmt.setHasFixedSize(true);*/

        categoryId = getIntent().getStringExtra("catId");
        cateName = getIntent().getStringExtra("catName");
        recyclerTossMatchAmt = findViewById(R.id.recyclerTossMatchAmtwinner);
        recyclerTossMatchValue = findViewById(R.id.recyclerTossMatchValuelooser);
        recyclerTossMatchAmt.setLayoutManager(new LinearLayoutManager(this));
        recyclerTossMatchAmt.setHasFixedSize(true);
        recyclerTossMatchValue.setLayoutManager(new LinearLayoutManager(this));
        recyclerTossMatchValue.setHasFixedSize(true);



        if (InternetConnection.checkConnection(getApplicationContext())) {
            getMainCatList();
            getCatBannerList(categoryId);
            getNumberListData();
            getWinPrizeList();
        } else {
            DialogManager.openCheckInternetDialog(ForTheWinActivity.this);
        }




    }

    private void getWinPrizeList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.PLAYBUY_ForTheWinAmountList, null, response -> {
            Gson gson = new Gson();
            try {
                DataResponseWinAmount dataResponseNumbers = gson.fromJson(response.toString(), DataResponseWinAmount.class);
                if (dataResponseNumbers.isStatus()) {
                    itemList.clear();
                    itemList = dataResponseNumbers.getResponse();
                    adaptorForTheWinAmountList = new AdaptorForTheWinAmountList(getApplicationContext(), dataResponseNumbers.getResponse());
                    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                    recyclerBuyNumber.setLayoutManager(linearLayoutManager);
                    recyclerBuyNumber.setAdapter(adaptorForTheWinAmountList);

                    adaptorForTheWinValueAmt = new AdaptorForTheWinValueAmt(getApplicationContext(), dataResponseNumbers.getResponse());
                    final LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
                    recyclerValueAmt.setLayoutManager(linearLayoutManager1);
                    recyclerValueAmt.setAdapter(adaptorForTheWinValueAmt);


                    final int speedScroll = 1500;
                    final Handler handler = new Handler();
                    final Runnable runnable = new Runnable() {
                        int count = 0;
                        boolean flag = true;
                        @Override
                        public void run() {
                            if(count < adaptorForTheWinAmountList.getItemCount()){
                                if(count==adaptorForTheWinAmountList.getItemCount()-1){
                                    flag = false;
                                }else if(count == 0){
                                    flag = true;
                                }
                                if(flag) count++;
                                else count--;

                                recyclerBuyNumber.smoothScrollToPosition(count);
                                handler.postDelayed(this,speedScroll);
                            }
                        }
                    };

                    handler.postDelayed(runnable,speedScroll);



                    final int speedScroll1 = 1500;
                    final Handler handler1 = new Handler();
                    final Runnable runnable1 = new Runnable() {
                        int count1 = 0;
                        boolean flag1 = true;
                        @Override
                        public void run() {
                            if(count1 < adaptorForTheWinValueAmt.getItemCount()){
                                if(count1==adaptorForTheWinValueAmt.getItemCount()-1){
                                    flag1 = false;
                                }else if(count1 == 0){
                                    flag1 = true;
                                }
                                if(flag1) count1++;
                                else count1--;

                                recyclerValueAmt.smoothScrollToPosition(count1);
                                handler1.postDelayed(this,speedScroll1);
                            }
                        }
                    };

                    handler1.postDelayed(runnable1,speedScroll1);



                    // Auto Scroll Left To Right
                    /*final int scrollSpeed = 100;   // Scroll Speed in Milliseconds
                    final int scrollSpeed1 = 100;   // Scroll Speed in Milliseconds
                    final Handler handler = new Handler();
                    final Handler handler1 = new Handler();
                    final Runnable runnable = new Runnable() {
                        int x = 20;        // Pixels To Move/Scroll
                        boolean flag = true;
                        // Find Scroll Position By Accessing RecyclerView's LinearLayout's Visible Item Position
                        int scrollPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                        int arraySize = dataResponseNumbers.getResponse().size();  // Gets RecyclerView's Adapter's Array Size
                        @Override
                        public void run() {
                            if (scrollPosition < arraySize) {
                                if (scrollPosition == arraySize - 1) {
                                    flag = false;
                                } else if (scrollPosition <= 1) {
                                    flag = true;
                                }
                                if (!flag) {
                                    try {
                                        // Delay in Seconds So User Can Completely Read Till Last String
                                        TimeUnit.SECONDS.sleep(1);
                                        recyclerBuyNumber.scrollToPosition(0);  // Jumps Back Scroll To Start Point
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                // Know The Last Visible Item
                                scrollPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                                recyclerBuyNumber.smoothScrollBy(x, 0);
                                handler.postDelayed(this, scrollSpeed);
                            }
                        }
                    };
                    handler.postDelayed(runnable, scrollSpeed);
                    final Runnable runnable1 = new Runnable() {
                        int x = 20;
                        boolean flag1 = true;
                        int scrollPosition1 = linearLayoutManager1.findLastCompletelyVisibleItemPosition();
                        int arraySize1 = dataResponseNumbers.getResponse().size();  // Gets RecyclerView's Adapter's Array Size
                        @Override
                        public void run() {
                            if (scrollPosition1 < arraySize1) {
                                if (scrollPosition1 == arraySize1 - 1) {
                                    flag1 = false;
                                } else if (scrollPosition1 <= 1) {
                                    flag1 = true;
                                }
                                if (!flag1) {
                                    try {
                                        // Delay in Seconds So User Can Completely Read Till Last String
                                        TimeUnit.SECONDS.sleep(1);
                                        recyclerValueAmt.scrollToPosition(0);  // Jumps Back Scroll To Start Point
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                // Know The Last Visible Item
                                scrollPosition1 = linearLayoutManager1.findLastCompletelyVisibleItemPosition();
                                recyclerValueAmt.smoothScrollBy(x, 0);
                                handler.postDelayed(this, scrollSpeed1);
                            }
                        }
                    };
                    handler1.postDelayed(runnable1, scrollSpeed1);*/

                } else {
                    Toast.makeText(ForTheWinActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void getNumberListData() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.PLAYBUY_NumberList, null, response -> {
            Gson gson = new Gson();
            try {
                DataResponseNumbers dataResponseNumbers = gson.fromJson(response.toString(), DataResponseNumbers.class);
                if (dataResponseNumbers.isStatus()) {
                    adaptorNumberList = new AdaptorNumberList(getApplicationContext(), dataResponseNumbers.getResponse());
                    recyclerDummyNumList.setAdapter(adaptorNumberList);

                    adaptorPickNumList = new AdaptorPickNumList(getApplicationContext(), dataResponseNumbers.getResponse());
                    recyclerPickNum.setAdapter(adaptorPickNumList);



                } else {
                    Toast.makeText(ForTheWinActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    private void getMainCatList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.CategoryList, null, response -> {
            Gson gson = new Gson();
            try {
                DataResponseCategory dataResponseCategory = gson.fromJson(response.toString(), DataResponseCategory.class);
                if (dataResponseCategory.isStatus()) {
                    adaptorMainCategoryList = new ForTheWinActivity.AdaptorMainCategoryList(getApplicationContext(), dataResponseCategory.getResponse());
                    recyclerCategoryList.setAdapter(adaptorMainCategoryList);
                } else {
                    Toast.makeText(ForTheWinActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearForPlay:
                if (tvSelectedNum.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please pick any number", Toast.LENGTH_SHORT).show();
                } else {
                    openPlayDialog();
                }
                break;
            case R.id.close:
                GameDetails_popup(categoryId);
                break;
        }
    }

    private void openPlayDialog() {
        alertDialog = new Dialog(ForTheWinActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.popup_for_the_win);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans)));
        alertDialog.setCancelable(false);
        alertDialog.show();
        //alertDialog.getWindow().setLayout((6 * width)/7, ActionBar.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerSelectAmt = alertDialog.findViewById(R.id.recyclerSelectAmt);
        recyclerSelectAmt.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerSelectAmt.setHasFixedSize(true);
        TextView tvYes = alertDialog.findViewById(R.id.tvYes);
        TextView tvCancel = alertDialog.findViewById(R.id.tvCancel);

        adaptorPopupSelectAmt = new AdaptorPopupSelectAmt(getApplicationContext(), itemList);
        recyclerSelectAmt.setAdapter(adaptorPopupSelectAmt);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedBitAmt.isEmpty()) {
                    Toast.makeText(ForTheWinActivity.this, "Please select amount!", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForTheWinActivity.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
                    builder.setTitle("Alert Confirmation for Amount:- ₹ " + selectedBitAmt)
                            .setMessage("Are you sure you want to Play?")
                            .setCancelable(false)
                            .setIcon(R.drawable.logo)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    getPlayAmtData(selectedBitAmt);
                                    //dialog.cancel();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) { dialog.cancel();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        }
                    });
                    builder.create().show();
                }
            }
        });

    }

    private void GameDetails_popup(String getId) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(ForTheWinActivity.this).inflate(R.layout.popup_gameoverview, viewGroup, false);

        TextView view_details_tv = dialogView.findViewById(R.id.view_details_tv);


        FloatingActionButton close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
//        arrayList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(ForTheWinActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.PLAYBUY_GameDetailsbyCat, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray Jarray = object.getJSONArray("Response");
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject jsonObject1 = Jarray.getJSONObject(i);
                        view_details_tv.setText(Html.fromHtml(jsonObject1.getString("GameDes")));
                    }
                } catch (JSONException e) {
                    Toast.makeText(ForTheWinActivity.this, "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        }, error -> {
            progressDialog.dismiss();
            Log.d("myTag", "message:" + error);
            Toast.makeText(ForTheWinActivity.this, "No Record Found!", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryId", getId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ForTheWinActivity.this);
        //setting the view of the builder to our custom view that we already inflated
        builder1.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog1 = builder1.create();
        alertDialog1.show();
        WindowManager.LayoutParams wmlp = alertDialog1.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.99);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.55);
        alertDialog1.getWindow().setLayout(width, height);

    }

    private void getPlayAmtData(String selectedBitAmt) {
        progressDialog.show();
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CategoryId", categoryId);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Number", tvSelectedNum.getText().toString().trim());
            jsonObject.put("Amount", selectedBitAmt);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Log.e("TAG", "getPlayAmtData: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ForthewinPlayNow, jsonObject, response -> {
            try {
                Log.e("TAG", "getScratchProductDetails: " + response);
                if (response.getBoolean("Status")) {
                    progressDialog.dismiss();
                    dummyAmt = true;
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    dummyNum = String.valueOf(jsonObject1.getString("Number"));
                    getNumberListData();

                    animationView.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animationView.setVisibility(View.GONE);
                            getForTheWinProduct();
                        }
                    }, 3000);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + response.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                progressDialog.dismiss();
                throw new RuntimeException(e);
            }

        }, error -> {
            progressDialog.dismiss();
            Log.e("TAG", "onErrorResponse: " + error.toString());
        });
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void getForTheWinProduct() {
        progressDialog.show();
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", UserId);
            jsonObject.put("PickNumber", tvSelectedNum.getText().toString().trim());
            jsonObject.put("AdminNumber", dummyNum);
            jsonObject.put("BidAmount", selectedBitAmt);
            jsonObject.put("WinAmount", selectedBitAmtValue);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Log.e("TAG", "getPlayAmtData: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_Forthewin_WinLose, jsonObject, response -> {
            try {
                Log.e("TAG", "getForTheWinPurchase: " + response);
                if (response.getBoolean("Status")) {
                    progressDialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
//                    win poupup show here
                    final MediaPlayer mp = MediaPlayer.create(ForTheWinActivity.this, R.raw.blast_one);
                    mp.start();
                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(ForTheWinActivity.this).inflate(R.layout.dynamic_winpopup, viewGroup, false);
                    Button rlBack = (AppCompatButton) dialogView.findViewById(R.id.btncancel);
                    TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);
                    Button buy_btn =(AppCompatButton)dialogView.findViewById(R.id.buy_btn);
                    buy_btn.setVisibility(View.GONE);
                    buy_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mp.stop();
                            Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_Summery_Details.class);
                            try {
                                intent.putExtra("p_code",jsonObject1.getString("ProductId"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intent.putExtra("p_userid",UserId);
                            intent.putExtra("p_disamount","");
                            intent.putExtra("p_price","");
                            intent.putExtra("p_name","");
                            intent.putExtra("p_img","");
                            intent.putExtra("class_name","forthewin");
                            startActivity(intent);

//                            mp.stop();
//
//                            finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());
//                            overridePendingTransition(0, 0);
                            alertDialog_win.dismiss();
                        }
                    });

                    txt_msg.setText(jsonObject1.getString("Msg"));
                    rlBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mp.stop();
                            finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                            alertDialog_win.dismiss();
                        }
                    });
                    //Now we need an AlertDialog.Builder object
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForTheWinActivity.this);
                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView);
                    //finally creating the alert dialog and displaying it
                    alertDialog_win = builder.create();
                    alertDialog_win.show();

                } else {
                    progressDialog.dismiss();
//                    loos poupup show here
                    Toast.makeText(this, "" + response.getString("Message"), Toast.LENGTH_SHORT).show();

                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(ForTheWinActivity.this).inflate(R.layout.dynamic_loosepopup, viewGroup, false);
                    Button rlBack = (AppCompatButton) dialogView.findViewById(R.id.btncancel);
                    TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);
                    txt_msg.setText(response.getString("Message"));
                    rlBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            alertDialog_loos.dismiss();
                        }
                    });
                    //Now we need an AlertDialog.Builder object
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForTheWinActivity.this);
                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView);
                    //finally creating the alert dialog and displaying it
                    alertDialog_loos = builder.create();
                    alertDialog_loos.show();

                }
            } catch (JSONException e) {
                progressDialog.dismiss();
                throw new RuntimeException(e);
            }

        }, error -> {
            progressDialog.dismiss();
            Log.e("TAG", "onErrorResponse: " + error.toString());
        });
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public class AdaptorMainCategoryList extends RecyclerView.Adapter<ForTheWinActivity.AdaptorMainCategoryList.MyCategoryHolder> {

        Context context;
        List<ResponseItem> catList;

        public AdaptorMainCategoryList(Context context, List<ResponseItem> catList) {
            this.context = context;
            this.catList = catList;
        }

        @NonNull
        @Override
        public ForTheWinActivity.AdaptorMainCategoryList.MyCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ForTheWinActivity.AdaptorMainCategoryList.MyCategoryHolder(LayoutInflater.from(context).inflate(R.layout.main_category_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ForTheWinActivity.AdaptorMainCategoryList.MyCategoryHolder holder, @SuppressLint("RecyclerView") int position) {
            Glide.with(context)
                    .load(catList.get(position).getCatImg())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.catImage);
            //Glide.with(MainActivity.this).load(catList.get(position).getCatImg()).error(R.drawable.logo).into(holder.catImage);

            holder.tvCatName.setText(catList.get(position).getCategoryName());
            if (catList.get(position).getCategoryName().equalsIgnoreCase(cateName)) {
                holder.linearLayout.setBackgroundResource(R.drawable.back_yellow);
                holder.tvCatName.setTextColor(ContextCompat.getColor(context, R.color.black));
            }

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (catList.get(position).getCategoryName()) {
                        case "SCRATCH & GET": mp.start();
                            startActivity(new Intent(getApplicationContext(), PlayBuyDashboard.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName())
                            );
                            finish();
                            break;

                        case "TOSS BOSS": mp.start();
                            startActivity(new Intent(getApplicationContext(), TossBossActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName()));
                            finish();
                            break;

                        case "LUCK & WIN": mp.start();
                            startActivity(new Intent(getApplicationContext(), MatchAndGetActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName())
                            );
                            finish();
                            break;

                        case "TRIPLE MATCH": mp.start();
                            startActivity(new Intent(getApplicationContext(), ThreeInOneActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName())
                            );
                            finish();
                            break;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return catList.size();
        }

        public class MyCategoryHolder extends RecyclerView.ViewHolder {

            GifImageView catImage;
            TextView tvCatName;
            LinearLayout linearLayout;

            View viewLay;

            public MyCategoryHolder(@NonNull View itemView) {
                super(itemView);
                catImage = itemView.findViewById(R.id.catImage);
                tvCatName = itemView.findViewById(R.id.tvCatName);
                linearLayout = itemView.findViewById(R.id.linearLayout);
                viewLay = itemView.findViewById(R.id.viewLay);
            }
        }
    }

    public class AdaptorNumberList extends RecyclerView.Adapter<AdaptorNumberList.MyNumberHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ForTheWinNumberList.ResponseItem> numberList;

        public AdaptorNumberList(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ForTheWinNumberList.ResponseItem> numberList) {
            this.context = context;
            this.numberList = numberList;
        }

        @NonNull
        @Override
        public MyNumberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyNumberHolder(LayoutInflater.from(context).inflate(R.layout.pic_number_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyNumberHolder holder, int position) {
            holder.tvNum.setText(String.valueOf(numberList.get(position).getNumber()));
            if (dummyAmt) {
                if (String.valueOf(numberList.get(position).getNumber()).equals(dummyNum)) {
                    holder.lineBluSel.setBackgroundResource(R.drawable.back_selected_red);
                }
            }
        }

        @Override
        public int getItemCount() {
            return numberList.size();
        }

        public class MyNumberHolder extends RecyclerView.ViewHolder {
            TextView tvNum;

            LinearLayout lineBluSel;

            public MyNumberHolder(@NonNull View itemView) {
                super(itemView);
                tvNum = itemView.findViewById(R.id.tvNum);
                lineBluSel = itemView.findViewById(R.id.lineBluSel);
            }
        }
    }


    public class AdaptorPickNumList extends RecyclerView.Adapter<AdaptorPickNumList.MyPickNumHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ForTheWinNumberList.ResponseItem> list;

        public AdaptorPickNumList(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ForTheWinNumberList.ResponseItem> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyPickNumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyPickNumHolder(LayoutInflater.from(context).inflate(R.layout.selected_layout_number_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyPickNumHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.tvNum.setText(String.valueOf(list.get(position).getNumber()));

            holder.linearSelected.setOnClickListener(view -> {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
                tvSelectedNum.setText(String.valueOf(list.get(position).getNumber()));
            });

            if (selectedPosition == position) {
                holder.linearSelected.setBackgroundResource(R.drawable.back_selected_red);
            } else {
                holder.linearSelected.setBackgroundResource(R.drawable.back_blue_white);
            }

        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
            //return list.size();
        }

        public class MyPickNumHolder extends RecyclerView.ViewHolder {
            LinearLayout linearSelected;
            TextView tvNum;

            public MyPickNumHolder(@NonNull View itemView) {
                super(itemView);
                linearSelected = itemView.findViewById(R.id.linearSelected);
                tvNum = itemView.findViewById(R.id.tvNum);
            }
        }
    }

    public class AdaptorForTheWinAmountList extends RecyclerView.Adapter<AdaptorForTheWinAmountList.MyForWinAmtHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt.ResponseItem> list;

        public AdaptorForTheWinAmountList(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt.ResponseItem> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyForWinAmtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyForWinAmtHolder(LayoutInflater.from(context).inflate(R.layout.win_amount_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyForWinAmtHolder holder, int position) {
            holder.tvNum.setText(String.valueOf(list.get(position).getBitAmount()));
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        public class MyForWinAmtHolder extends RecyclerView.ViewHolder {

            TextView tvNum;

            public MyForWinAmtHolder(@NonNull View itemView) {
                super(itemView);
                tvNum = itemView.findViewById(R.id.tvNum);
            }
        }
    }

    public class AdaptorForTheWinValueAmt extends RecyclerView.Adapter<AdaptorForTheWinValueAmt.MyForWinAmtHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt.ResponseItem> list;

        public AdaptorForTheWinValueAmt(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt.ResponseItem> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyForWinAmtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyForWinAmtHolder(LayoutInflater.from(context).inflate(R.layout.win_amount_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyForWinAmtHolder holder, int position) {
            holder.tvNum.setText(String.valueOf(list.get(position).getAmount()));
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        public class MyForWinAmtHolder extends RecyclerView.ViewHolder {

            TextView tvNum;

            public MyForWinAmtHolder(@NonNull View itemView) {
                super(itemView);
                tvNum = itemView.findViewById(R.id.tvNum);
            }
        }
    }

    public class AdaptorPopupSelectAmt extends RecyclerView.Adapter<AdaptorPopupSelectAmt.MyForWinAmt1Holder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt.ResponseItem> list1;

        public AdaptorPopupSelectAmt(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt.ResponseItem> list) {
            this.context = context;
            this.list1 = list;
        }

        @NonNull
        @Override
        public MyForWinAmt1Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyForWinAmt1Holder(LayoutInflater.from(context).inflate(R.layout.layout_choose_amt, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyForWinAmt1Holder holder, int position) {
            holder.tvNum1.setText(String.valueOf(list1.get(position).getBitAmount()));

            holder.linear1Selected.setOnClickListener(view -> {
                selectedPosition1 = holder.getAdapterPosition();
                notifyDataSetChanged();
                selectedBitAmt = String.valueOf(list1.get(position).getBitAmount());
                selectedBitAmtValue = String.valueOf(list1.get(position).getAmount());
            });

            if (selectedPosition1 == position) {
                holder.linear1Selected.setBackgroundResource(R.drawable.back_selected_red);
                holder.tvNum1.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                holder.linear1Selected.setBackgroundResource(R.drawable.back_blue_white);
                holder.tvNum1.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
        }

        @Override
        public int getItemCount() {
            return list1 == null ? 0 : list1.size();
        }

        public class MyForWinAmt1Holder extends RecyclerView.ViewHolder {

            TextView tvNum1;
            LinearLayout linear1Selected;

            public MyForWinAmt1Holder(@NonNull View itemView) {
                super(itemView);
                linear1Selected = itemView.findViewById(R.id.linear1Selected);
                tvNum1 = itemView.findViewById(R.id.tvNum1);
            }
        }
    }







/*
    private void FormetchandGet_popup() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(ForTheWinActivity.this).inflate(R.layout.popup_gameoverview_viewmore, viewGroup, false);
        FloatingActionButton close = dialogView.findViewById(R.id.close);
        txt_nodata= dialogView.findViewById(R.id.txt_nodata);
        tranding_rec = dialogView.findViewById(R.id.tranding_rec);
        wallet_histroy = dialogView.findViewById(R.id.wallet_histroy);

        arrayList.clear();
        arrayList1.clear();
        GetCategoryAPI();
        getHistory("");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();
            }
        });







        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ForTheWinActivity.this);
        //setting the view of the builder to our custom view that we already inflated
        builder1.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog2 = builder1.create();
        alertDialog2.show();
        WindowManager.LayoutParams wmlp = alertDialog2.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.99);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.50);
        alertDialog2.getWindow().setLayout(width, height);
    }


    public void GetCategoryAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "PLAYBUY_Subcategory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("CategoryId", jsonObject1.getString("CategoryId"));
                        hm.put("CategoryName", jsonObject1.getString("CategoryName"));
                        arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(ForTheWinActivity.this, 3);
                    CategoryAdapter customerListAdapter = new CategoryAdapter(ForTheWinActivity.this, arrayList);
                    tranding_rec.setLayoutManager(gridLayoutManager);
                    tranding_rec.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
                Toast.makeText(ForTheWinActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryId", "3");
                params.put("Search", "");
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList3;
        public CategoryAdapter(Context context, List<HashMap<String,String>> arrayList3) {
            this.arrayList3=arrayList3;
            this.context=context; }
        @NonNull
        @Override
        public CategoryAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_category, viewGroup, false);
            CategoryAdapter.VH viewHolder = new CategoryAdapter.VH(view);
            return viewHolder; }
        @Override
        public void onBindViewHolder(@NonNull CategoryAdapter.VH vh, int i) {
            vh.cat_name_tv.setText("Category("+arrayList3.get(i).get("CategoryName")+")");


            vh.cat_name_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( ( previousSeletedButton == null ) || ( previousSeletedButton == vh.cat_name_tv ) ) {
                        vh.cat_name_tv.setBackgroundColor( ContextCompat.getColor( context, R.color.redone) );
                    }
                    else{
                        previousSeletedButton.setBackgroundColor( ContextCompat.getColor( context, R.color.yellow) );
                        vh.cat_name_tv.setBackgroundColor( ContextCompat.getColor( context, R.color.redone) );
                    }
                    previousSeletedButton = vh.cat_name_tv;
                    arrayList1.clear();
//                         customerListAdapter.notifyDataSetChanged();
                    getHistory(arrayList3.get(i).get("CategoryId"));
                }
            });
        }
        @Override
        public int getItemCount() {
            return arrayList3.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            AppCompatButton cat_name_tv;
            public VH(@NonNull View itemView) {
                super(itemView);
                cat_name_tv=itemView.findViewById(R.id.cat_name_tv);
            }}}

    public void getHistory(String catid) {
        final ProgressDialog progressDialog = new ProgressDialog(ForTheWinActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "PLAYBUY_categoryGameProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("what",response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        txt_nodata.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("ProductMRP", jsonObject.getString("ProductMRP"));
                            hashlist.put("ProductSaleRate", jsonObject.getString("ProductSaleRate"));
                            arrayList1.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ForTheWinActivity.this, 3);
                        customerListAdapter = new DashboardAdapter(ForTheWinActivity.this, arrayList1);
                        wallet_histroy.setLayoutManager(gridLayoutManager);
                        wallet_histroy.setAdapter(customerListAdapter);
                    } else {
                        txt_nodata.setVisibility(View.VISIBLE);
                        Toast.makeText(ForTheWinActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ForTheWinActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryId", catid);
                params.put("Search", "");
                return params;
                // return super.getParams();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);
    }

    public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.VH> {
        Context context;
        List<HashMap<String, String>> arrayList;
        private int like = 0;

        public DashboardAdapter(Context context, List<HashMap<String, String>> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public DashboardAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_details_common, viewGroup, false);
            DashboardAdapter.VH viewHolder = new DashboardAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull DashboardAdapter.VH vh, final int i) {
            AnimationHelper.animatate(context, vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(ForTheWinActivity.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup3(arrayList.get(i).get("ProductCode"));
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            TextView name,code;
            ImageView img;


            public VH(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                name = itemView.findViewById(R.id.name);
                code = itemView.findViewById(R.id.code);
            }
        }

    }

    //ProductDetailspopupdetails
    private void openOfferpopup3(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(ForTheWinActivity.this);
        dialog.setContentView(R.layout.dynamic_productdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        AppCompatButton Cancel = dialog.findViewById(R.id.btn_cancel);
        TextView name = dialog.findViewById(R.id.name);
        TextView amount = dialog.findViewById(R.id.amount_tv);
        TextView discount_tv = dialog.findViewById(R.id.discount_tv);
        TextView orderamount_tv = dialog.findViewById(R.id.orderamount_tv);
        ImageView imageView = dialog.findViewById(R.id.imageView);
        TextView p_code = dialog.findViewById(R.id.p_code);

        TextView productdetails_tv = dialog.findViewById(R.id.productdetails_tv);
        LinearLayout view_more_ll = dialog.findViewById(R.id.view_more_ll);
        view_more_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    productdetails_tv.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 1) {
                    productdetails_tv.setVisibility(View.GONE);

                    i = 0;
                }
            }
        });


        Cancel.setText("Exit>");
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(ForTheWinActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUYProductDeatilsbyId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ProductDeatilsbyId",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String planCode = jsonObject2.getString("ProductCode");
                        String plan_name = jsonObject2.getString("ProductName");
                        String productMrp = jsonObject2.getString("ProductMRP");
                        String saleRate = jsonObject2.getString("ProductSaleRate");
                        String productImg = jsonObject2.getString("ProductImg");
                        String discount = jsonObject2.getString("DisPer");
                        String description = jsonObject2.getString("Description");
                        if (description.equalsIgnoreCase("")){
                            productdetails_tv.setText("Details Not Found!!");
                        }
                        else{
                            productdetails_tv.setText(description);
                        }
                        name.setText(plan_name);
                        p_code.setText(planCode);
                        amount.setText("\u20B9 "+productMrp);
                        discount_tv.setText(discount+" %");
                        orderamount_tv.setText("\u20B9 "+saleRate);
                        Glide.with(ForTheWinActivity.this)
                                .load(productImg).error(R.drawable.a_logo)
                                .into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(), FullImagePage.class);
                                try {
                                    intent.putExtra("path",jsonObject2.getString("ProductImg"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }
                        });
                    }

                    if (jsonObject.getString("Message").equalsIgnoreCase("You have already visited.")){
                        Toast.makeText(ForTheWinActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ForTheWinActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ProductId",productcode);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails*/

















    private void showProductDetails() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(ForTheWinActivity.this).inflate(R.layout.three_in_one_product_details, viewGroup, false);



        TextView cat_one = dialogView.findViewById(R.id.cat_one);
        TextView  cat_two = dialogView.findViewById(R.id.cat_two);
        TextView  cat_three = dialogView.findViewById(R.id.cat_three);
        TextView  cat_four = dialogView.findViewById(R.id.cat_four);
        LinearLayout last_cat_ll = dialogView.findViewById(R.id.last_cat_ll);

        cat_one.setText("Rs. 10");
        cat_two.setText("Rs. 20");
        cat_three.setText("Rs. 50");

        last_cat_ll.setVisibility(View.GONE);

        norecord_tv = dialogView.findViewById(R.id.norecord_tv);
        norecord_tv1 = dialogView.findViewById(R.id.norecord_tv1);
        norecord_tv2 = dialogView.findViewById(R.id.norecord_tv2);
        norecord_tv3 = dialogView.findViewById(R.id.norecord_tv3);


        recyclerProductList = dialogView.findViewById(R.id.recyclerProductList);
        recyclerDisProductList1 = dialogView.findViewById(R.id.recyclerDisProductList1);
        recyclerDisProductList2 = dialogView.findViewById(R.id.recyclerDisProductList2);
        recyclerDisProductList3 = dialogView.findViewById(R.id.recyclerDisProductList3);
        LinearLayout close = dialogView.findViewById(R.id.linearClose);


        getHistory("9");
        getHistory1("8");
        getHistory2("10");
        getHistory3("4");


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ForTheWinActivity.this);
        //setting the view of the builder to our custom view that we already inflated
        builder1.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog1 = builder1.create();
        alertDialog1.show();
        WindowManager.LayoutParams wmlp = alertDialog1.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.99);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.55);
        alertDialog1.getWindow().setLayout(width, height);
    }

    public void getHistory(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(ForTheWinActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUY_ThreeInOneProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);

                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("ProductMRP", jsonObject.getString("ProductMRP"));
                            hashlist.put("ProductSaleRate", jsonObject.getString("ProductSaleRate"));
                            arrayList5.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ForTheWinActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        ForTheWinActivity.TaskEarningAdapter customerListAdapter = new ForTheWinActivity.TaskEarningAdapter(ForTheWinActivity.this, arrayList5);
                        recyclerProductList.setLayoutManager(gridLayoutManager);
                        recyclerProductList.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(ForTheWinActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(ForTheWinActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("CategoryId",str_cat);
            params.put("Search","");
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter extends RecyclerView.Adapter<ForTheWinActivity.TaskEarningAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public ForTheWinActivity.TaskEarningAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            ForTheWinActivity.TaskEarningAdapter.VH viewHolder = new ForTheWinActivity.TaskEarningAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ForTheWinActivity.TaskEarningAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(ForTheWinActivity.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup(arrayList.get(i).get("ProductCode"));
                }
            });

        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView name,code;
            ImageView img;
            public VH(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                name = itemView.findViewById(R.id.name);
                code = itemView.findViewById(R.id.code);
            }
        }
    }

    //ProductDetailspopupdetails
    private void openOfferpopup(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(ForTheWinActivity.this);
        dialog.setContentView(R.layout.dynamic_productdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        AppCompatButton Cancel = dialog.findViewById(R.id.btn_cancel);
        TextView name = dialog.findViewById(R.id.name);
        TextView amount = dialog.findViewById(R.id.amount_tv);
        TextView discount_tv = dialog.findViewById(R.id.discount_tv);
        TextView orderamount_tv = dialog.findViewById(R.id.orderamount_tv);
        ImageView imageView = dialog.findViewById(R.id.imageView);
        TextView p_code = dialog.findViewById(R.id.p_code);

        TextView ProductSizeUnittv = dialog.findViewById(R.id.ProductSizeUnittv);
        TextView ProductSizeValuetv = dialog.findViewById(R.id.ProductSizeValuetv);
        TextView ProductColorTv = dialog.findViewById(R.id.ProductColorTv);
        TextView ProductLengthTv = dialog.findViewById(R.id.ProductLengthTv);
        ImageView outofstok_img = dialog.findViewById(R.id.outofstok_img);

        TextView ServiceChargeTv = dialog.findViewById(R.id.ServiceChargeTv);
        TextView DeliveryFeeTv = dialog.findViewById(R.id.DeliveryFeeTv);


        TextView productdetails_tv = dialog.findViewById(R.id.productdetails_tv);
        LinearLayout view_more_ll = dialog.findViewById(R.id.view_more_ll);
        view_more_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    productdetails_tv.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 1) {
                    productdetails_tv.setVisibility(View.GONE);

                    i = 0;
                }
            }
        });

        AppCompatButton buy_btn = dialog.findViewById(R.id.buy_btn);
        buy_btn.setVisibility(View.GONE);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(ForTheWinActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUYProductDeatilsbyId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("ghf",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String planCode = jsonObject2.getString("ProductCode");
                        String plan_name = jsonObject2.getString("ProductName");
                        String productMrp = jsonObject2.getString("ProductMRP");
                        String saleRate = jsonObject2.getString("ProductSaleRate");
                        String productImg = jsonObject2.getString("ProductImg");
                        String discount = jsonObject2.getString("DisPer");

                        String productSizeUnit = jsonObject2.getString("ProductSizeUnit");
                        String productSizeValue = jsonObject2.getString("ProductSizeValue");

                        String productColor = jsonObject2.getString("ProductColor");
                        String productLength = jsonObject2.getString("ProductLength");
                        String isStock = jsonObject2.getString("IsStock");

                        ProductColorTv.setText(productColor);
                        ProductLengthTv.setText(productLength);

                        if (isStock.equalsIgnoreCase("0")){
                            outofstok_img.setVisibility(View.VISIBLE);
                            imageView.setAlpha((float) 0.5);
                        }
                        else {
                            outofstok_img.setVisibility(View.GONE);

                        }


                        ProductSizeUnittv.setText(productSizeUnit);
                        ProductSizeValuetv.setText(productSizeValue);


                        String description = jsonObject2.getString("Description");
                        if (description.equalsIgnoreCase("")){
                            productdetails_tv.setText("Details Not Found!!");
                        }
                        else{
                            productdetails_tv.setText(description);
                        }
                        name.setText(plan_name);
                        p_code.setText(planCode);
                        amount.setText("\u20B9 "+productMrp);
                        discount_tv.setText(discount+" %");
                        orderamount_tv.setText("\u20B9 "+saleRate);
                        Glide.with(ForTheWinActivity.this)
                                .load(productImg).error(R.drawable.a_logo)
                                .into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(), FullImagePage.class);
                                try {
                                    intent.putExtra("path",jsonObject2.getString("ProductImg"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }
                        });
                    }

                    if (jsonObject.getString("Message").equalsIgnoreCase("You have already visited.")){
                        Toast.makeText(ForTheWinActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ForTheWinActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ProductId",productcode);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails





    public void getHistory1(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(ForTheWinActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUY_ThreeInOneProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);

                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv1.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("ProductMRP", jsonObject.getString("ProductMRP"));
                            hashlist.put("ProductSaleRate", jsonObject.getString("ProductSaleRate"));
                            arrayList6.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ForTheWinActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        ForTheWinActivity.TaskEarningAdapter1 customerListAdapter = new ForTheWinActivity.TaskEarningAdapter1(ForTheWinActivity.this, arrayList6);
                        recyclerDisProductList1.setLayoutManager(gridLayoutManager);
                        recyclerDisProductList1.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv1.setVisibility(View.VISIBLE);
//                        Toast.makeText(ForTheWinActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(ForTheWinActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("CategoryId",str_cat);
            params.put("Search","");
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter1 extends RecyclerView.Adapter<ForTheWinActivity.TaskEarningAdapter1.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter1(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public ForTheWinActivity.TaskEarningAdapter1.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            ForTheWinActivity.TaskEarningAdapter1.VH viewHolder = new ForTheWinActivity.TaskEarningAdapter1.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ForTheWinActivity.TaskEarningAdapter1.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(ForTheWinActivity.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup1(arrayList.get(i).get("ProductCode"));
                }
            });

        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView name,code;
            ImageView img;
            public VH(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                name = itemView.findViewById(R.id.name);
                code = itemView.findViewById(R.id.code);
            }
        }
    }

    //ProductDetailspopupdetails
    private void openOfferpopup1(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(ForTheWinActivity.this);
        dialog.setContentView(R.layout.dynamic_productdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        AppCompatButton Cancel = dialog.findViewById(R.id.btn_cancel);
        TextView name = dialog.findViewById(R.id.name);
        TextView amount = dialog.findViewById(R.id.amount_tv);
        TextView discount_tv = dialog.findViewById(R.id.discount_tv);
        TextView orderamount_tv = dialog.findViewById(R.id.orderamount_tv);
        ImageView imageView = dialog.findViewById(R.id.imageView);
        TextView p_code = dialog.findViewById(R.id.p_code);
        TextView ProductSizeUnittv = dialog.findViewById(R.id.ProductSizeUnittv);
        TextView ProductSizeValuetv = dialog.findViewById(R.id.ProductSizeValuetv);
        TextView ProductColorTv = dialog.findViewById(R.id.ProductColorTv);
        TextView ProductLengthTv = dialog.findViewById(R.id.ProductLengthTv);
        ImageView outofstok_img = dialog.findViewById(R.id.outofstok_img);

        TextView ServiceChargeTv = dialog.findViewById(R.id.ServiceChargeTv);
        TextView DeliveryFeeTv = dialog.findViewById(R.id.DeliveryFeeTv);


        TextView productdetails_tv = dialog.findViewById(R.id.productdetails_tv);
        LinearLayout view_more_ll = dialog.findViewById(R.id.view_more_ll);
        view_more_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    productdetails_tv.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 1) {
                    productdetails_tv.setVisibility(View.GONE);

                    i = 0;
                }
            }
        });
        AppCompatButton buy_btn = dialog.findViewById(R.id.buy_btn);
        buy_btn.setVisibility(View.GONE);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(ForTheWinActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUYProductDeatilsbyId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ProductDeatilsbyId",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String planCode = jsonObject2.getString("ProductCode");
                        String plan_name = jsonObject2.getString("ProductName");
                        String productMrp = jsonObject2.getString("ProductMRP");
                        String saleRate = jsonObject2.getString("ProductSaleRate");
                        String productImg = jsonObject2.getString("ProductImg");
                        String discount = jsonObject2.getString("DisPer");

                        String productSizeUnit = jsonObject2.getString("ProductSizeUnit");
                        String productSizeValue = jsonObject2.getString("ProductSizeValue");

                        String productColor = jsonObject2.getString("ProductColor");
                        String productLength = jsonObject2.getString("ProductLength");
                        String isStock = jsonObject2.getString("IsStock");

                        ProductColorTv.setText(productColor);
                        ProductLengthTv.setText(productLength);

                        if (isStock.equalsIgnoreCase("0")){
                            outofstok_img.setVisibility(View.VISIBLE);
                            imageView.setAlpha((float) 0.5);
                        }
                        else {
                            outofstok_img.setVisibility(View.GONE);

                        }


                        ProductSizeUnittv.setText(productSizeUnit);
                        ProductSizeValuetv.setText(productSizeValue);

                        String description = jsonObject2.getString("Description");
                        if (description.equalsIgnoreCase("")){
                            productdetails_tv.setText("Details Not Found!!");
                        }
                        else{
                            productdetails_tv.setText(description);
                        }
                        name.setText(plan_name);
                        p_code.setText(planCode);
                        amount.setText("\u20B9 "+productMrp);
                        discount_tv.setText(discount+" %");
                        orderamount_tv.setText("\u20B9 "+saleRate);
                        Glide.with(ForTheWinActivity.this)
                                .load(productImg).error(R.drawable.a_logo)
                                .into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(), FullImagePage.class);
                                try {
                                    intent.putExtra("path",jsonObject2.getString("ProductImg"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }
                        });
                    }

                    if (jsonObject.getString("Message").equalsIgnoreCase("You have already visited.")){
                        Toast.makeText(ForTheWinActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ForTheWinActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ProductId",productcode);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails











    public void getHistory2(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(ForTheWinActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUY_ThreeInOneProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);

                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv2.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("ProductMRP", jsonObject.getString("ProductMRP"));
                            hashlist.put("ProductSaleRate", jsonObject.getString("ProductSaleRate"));
                            arrayList7.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ForTheWinActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        ForTheWinActivity.TaskEarningAdapter2 customerListAdapter = new ForTheWinActivity.TaskEarningAdapter2(ForTheWinActivity.this, arrayList7);
                        recyclerDisProductList2.setLayoutManager(gridLayoutManager);
                        recyclerDisProductList2.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv2.setVisibility(View.VISIBLE);
//                        Toast.makeText(ForTheWinActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(ForTheWinActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("CategoryId",str_cat);
            params.put("Search","");
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter2 extends RecyclerView.Adapter<ForTheWinActivity.TaskEarningAdapter2.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter2(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public ForTheWinActivity.TaskEarningAdapter2.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            ForTheWinActivity.TaskEarningAdapter2.VH viewHolder = new ForTheWinActivity.TaskEarningAdapter2.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ForTheWinActivity.TaskEarningAdapter2.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(ForTheWinActivity.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup2(arrayList.get(i).get("ProductCode"));
                }
            });

        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView name,code;
            ImageView img;
            public VH(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                name = itemView.findViewById(R.id.name);
                code = itemView.findViewById(R.id.code);
            }
        }
    }

    //ProductDetailspopupdetails
    private void openOfferpopup2(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(ForTheWinActivity.this);
        dialog.setContentView(R.layout.dynamic_productdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        AppCompatButton Cancel = dialog.findViewById(R.id.btn_cancel);
        TextView name = dialog.findViewById(R.id.name);
        TextView amount = dialog.findViewById(R.id.amount_tv);
        TextView discount_tv = dialog.findViewById(R.id.discount_tv);
        TextView orderamount_tv = dialog.findViewById(R.id.orderamount_tv);
        ImageView imageView = dialog.findViewById(R.id.imageView);
        TextView p_code = dialog.findViewById(R.id.p_code);
        TextView ProductSizeUnittv = dialog.findViewById(R.id.ProductSizeUnittv);
        TextView ProductSizeValuetv = dialog.findViewById(R.id.ProductSizeValuetv);

        TextView ProductColorTv = dialog.findViewById(R.id.ProductColorTv);
        TextView ProductLengthTv = dialog.findViewById(R.id.ProductLengthTv);
        ImageView outofstok_img = dialog.findViewById(R.id.outofstok_img);

        TextView productdetails_tv = dialog.findViewById(R.id.productdetails_tv);
        LinearLayout view_more_ll = dialog.findViewById(R.id.view_more_ll);
        view_more_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    productdetails_tv.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 1) {
                    productdetails_tv.setVisibility(View.GONE);

                    i = 0;
                }
            }
        });

        AppCompatButton buy_btn = dialog.findViewById(R.id.buy_btn);
        buy_btn.setVisibility(View.GONE);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(ForTheWinActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUYProductDeatilsbyId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ProductDeatilsbyId",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String planCode = jsonObject2.getString("ProductCode");
                        String plan_name = jsonObject2.getString("ProductName");
                        String productMrp = jsonObject2.getString("ProductMRP");
                        String saleRate = jsonObject2.getString("ProductSaleRate");
                        String productImg = jsonObject2.getString("ProductImg");
                        String discount = jsonObject2.getString("DisPer");

                        String productSizeUnit = jsonObject2.getString("ProductSizeUnit");
                        String productSizeValue = jsonObject2.getString("ProductSizeValue");

                        String productColor = jsonObject2.getString("ProductColor");
                        String productLength = jsonObject2.getString("ProductLength");
                        String isStock = jsonObject2.getString("IsStock");

                        ProductColorTv.setText(productColor);
                        ProductLengthTv.setText(productLength);

                        if (isStock.equalsIgnoreCase("0")){
                            outofstok_img.setVisibility(View.VISIBLE);
                            imageView.setAlpha((float) 0.5);
                        }
                        else {
                            outofstok_img.setVisibility(View.GONE);

                        }


                        ProductSizeUnittv.setText(productSizeUnit);
                        ProductSizeValuetv.setText(productSizeValue);


                        String description = jsonObject2.getString("Description");
                        if (description.equalsIgnoreCase("")){
                            productdetails_tv.setText("Details Not Found!!");
                        }
                        else{
                            productdetails_tv.setText(description);
                        }
                        name.setText(plan_name);
                        p_code.setText(planCode);
                        amount.setText("\u20B9 "+productMrp);
                        discount_tv.setText(discount+" %");
                        orderamount_tv.setText("\u20B9 "+saleRate);
                        Glide.with(ForTheWinActivity.this)
                                .load(productImg).error(R.drawable.a_logo)
                                .into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(), FullImagePage.class);
                                try {
                                    intent.putExtra("path",jsonObject2.getString("ProductImg"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }
                        });
                    }

                    if (jsonObject.getString("Message").equalsIgnoreCase("You have already visited.")){
                        Toast.makeText(ForTheWinActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ForTheWinActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ProductId",productcode);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails










    public void getHistory3(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(ForTheWinActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUY_ThreeInOneProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);

                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv3.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("ProductMRP", jsonObject.getString("ProductMRP"));
                            hashlist.put("ProductSaleRate", jsonObject.getString("ProductSaleRate"));
                            arrayList8.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ForTheWinActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        ForTheWinActivity.TaskEarningAdapter3 customerListAdapter = new ForTheWinActivity.TaskEarningAdapter3(ForTheWinActivity.this, arrayList8);
                        recyclerDisProductList3.setLayoutManager(gridLayoutManager);
                        recyclerDisProductList3.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv3.setVisibility(View.VISIBLE);
//                        Toast.makeText(ForTheWinActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(ForTheWinActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("CategoryId",str_cat);
            params.put("Search","");
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter3 extends RecyclerView.Adapter<ForTheWinActivity.TaskEarningAdapter3.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter3(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public ForTheWinActivity.TaskEarningAdapter3.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            ForTheWinActivity.TaskEarningAdapter3.VH viewHolder = new ForTheWinActivity.TaskEarningAdapter3.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ForTheWinActivity.TaskEarningAdapter3.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(ForTheWinActivity.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup3(arrayList.get(i).get("ProductCode"));
                }
            });

        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView name,code;
            ImageView img;
            public VH(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                name = itemView.findViewById(R.id.name);
                code = itemView.findViewById(R.id.code);
            }
        }
    }

    //ProductDetailspopupdetails
    private void openOfferpopup3(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(ForTheWinActivity.this);
        dialog.setContentView(R.layout.dynamic_productdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        AppCompatButton Cancel = dialog.findViewById(R.id.btn_cancel);
        TextView name = dialog.findViewById(R.id.name);
        TextView amount = dialog.findViewById(R.id.amount_tv);
        TextView discount_tv = dialog.findViewById(R.id.discount_tv);
        TextView orderamount_tv = dialog.findViewById(R.id.orderamount_tv);
        ImageView imageView = dialog.findViewById(R.id.imageView);
        TextView p_code = dialog.findViewById(R.id.p_code);
        TextView ProductSizeUnittv = dialog.findViewById(R.id.ProductSizeUnittv);
        TextView ProductSizeValuetv = dialog.findViewById(R.id.ProductSizeValuetv);

        TextView ProductColorTv = dialog.findViewById(R.id.ProductColorTv);
        TextView ProductLengthTv = dialog.findViewById(R.id.ProductLengthTv);
        ImageView outofstok_img = dialog.findViewById(R.id.outofstok_img);

        TextView productdetails_tv = dialog.findViewById(R.id.productdetails_tv);
        LinearLayout view_more_ll = dialog.findViewById(R.id.view_more_ll);
        view_more_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i == 0) {
                    productdetails_tv.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 1) {
                    productdetails_tv.setVisibility(View.GONE);

                    i = 0;
                }
            }
        });
        AppCompatButton buy_btn = dialog.findViewById(R.id.buy_btn);
        buy_btn.setVisibility(View.GONE);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(ForTheWinActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUYProductDeatilsbyId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ProductDeatilsbyId",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String planCode = jsonObject2.getString("ProductCode");
                        String plan_name = jsonObject2.getString("ProductName");
                        String productMrp = jsonObject2.getString("ProductMRP");
                        String saleRate = jsonObject2.getString("ProductSaleRate");
                        String productImg = jsonObject2.getString("ProductImg");
                        String discount = jsonObject2.getString("DisPer");
                        String productSizeUnit = jsonObject2.getString("ProductSizeUnit");
                        String productSizeValue = jsonObject2.getString("ProductSizeValue");

                        String productColor = jsonObject2.getString("ProductColor");
                        String productLength = jsonObject2.getString("ProductLength");
                        String isStock = jsonObject2.getString("IsStock");

                        ProductColorTv.setText(productColor);
                        ProductLengthTv.setText(productLength);

                        if (isStock.equalsIgnoreCase("0")){
                            outofstok_img.setVisibility(View.VISIBLE);
                            imageView.setAlpha((float) 0.5);
                        }
                        else {
                            outofstok_img.setVisibility(View.GONE);

                        }


                        ProductSizeUnittv.setText(productSizeUnit);
                        ProductSizeValuetv.setText(productSizeValue);

                        String description = jsonObject2.getString("Description");
                        if (description.equalsIgnoreCase("")){
                            productdetails_tv.setText("Details Not Found!!");
                        }
                        else{
                            productdetails_tv.setText(description);
                        }
                        name.setText(plan_name);
                        p_code.setText(planCode);
                        amount.setText("\u20B9 "+productMrp);
                        discount_tv.setText(discount+" %");
                        orderamount_tv.setText("\u20B9 "+saleRate);
                        Glide.with(ForTheWinActivity.this)
                                .load(productImg).error(R.drawable.a_logo)
                                .into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(), FullImagePage.class);
                                try {
                                    intent.putExtra("path",jsonObject2.getString("ProductImg"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }
                        });
                    }

                    if (jsonObject.getString("Message").equalsIgnoreCase("You have already visited.")){
                        Toast.makeText(ForTheWinActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ForTheWinActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ProductId",productcode);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails

    private void getCatBannerList(String id) {
        final ProgressDialog progressDialog = new ProgressDialog(ForTheWinActivity.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.POST,  WebServices.BannerList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("banneruimg",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    String JsonInString = jsonObject.getString("Response");
                    bannerData = BannerData.createJsonInList(JsonInString);
                    pager.setAdapter(new AdapterForBanner(ForTheWinActivity.this, bannerData));
                    indicator.setViewPager(pager);
                    indicator.setFillColor(Color.RED);
                    final float density = getResources().getDisplayMetrics().density;
                    indicator.setRadius(5 * density);
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == bannerData.size()) {
                                currentPage = 0;
                            }
                            pager.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 5000, 3000);
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;
                        }
                        @Override
                        public void onPageScrolled(int pos, float arg1, int arg2) { }
                        @Override
                        public void onPageScrollStateChanged(int pos) { }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryId", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),PlayBuyDashboard.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }




    //    Banner winer and looser list API
    public void GetWinnerList(String type) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.PLAYBUY_WinLossList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("UserId", jsonObject.getString("UserId"));
                            hashlist.put("Img", jsonObject.getString("Img"));;
                            arrayListwin.add(hashlist);
                        }
                        adaptorMatchGetNum = new AdaptorMatchGetNum(getApplicationContext(), arrayListwin);
                        recyclerTossMatchAmt.setAdapter(adaptorMatchGetNum);


                        final int speedScroll = 2500;
                        final Handler handler = new Handler();
                        final Runnable runnable = new Runnable() {
                            int wcount = 0;
                            boolean wflag = true;
                            @Override
                            public void run() {
                                if(wcount < adaptorMatchGetNum.getItemCount()){
                                    if(wcount==adaptorMatchGetNum.getItemCount()-1){
                                        wflag = false;
                                    }else if(wcount == 0){
                                        wflag = true;
                                    }
                                    if(wflag) wcount++;
                                    else wcount--;

                                    recyclerTossMatchAmt.smoothScrollToPosition(wcount);
                                    handler.postDelayed(this,speedScroll);
                                }
                            }
                        };

                        handler.postDelayed(runnable,speedScroll);



                        final int speedScroll1 = 2500;
                        final Handler handler1 = new Handler();
                        final Runnable runnable1 = new Runnable() {
                            int wcount1 = 0;
                            boolean wflag1 = true;
                            @Override
                            public void run() {
                                if(wcount1 < adaptorMatchGetNumValues.getItemCount()){
                                    if(wcount1==adaptorMatchGetNumValues.getItemCount()-1){
                                        wflag1 = false;
                                    }else if(wcount1 == 0){
                                        wflag1 = true;
                                    }
                                    if(wflag1) wcount1++;
                                    else wcount1--;

                                    recyclerTossMatchValue.smoothScrollToPosition(wcount1);
                                    handler1.postDelayed(this,speedScroll1);
                                }
                            }
                        };

                        handler1.postDelayed(runnable1,speedScroll1);



                    } else {
//                        Toast.makeText(ThreeInOneActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(ForTheWinActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("Type",type);;
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);

    }
    public void GetLooserList(String type) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.PLAYBUY_WinLossList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("UserId", jsonObject.getString("UserId"));
                            hashlist.put("Img", jsonObject.getString("Img"));;
                            arrayListloss.add(hashlist);
                        }
                        adaptorMatchGetNumValues = new AdaptorMatchGetNumValues(getApplicationContext(), arrayListloss);
                        recyclerTossMatchValue.setAdapter(adaptorMatchGetNumValues);


                        final int speedScroll = 2500;
                        final Handler handler = new Handler();
                        final Runnable runnable = new Runnable() {
                            int lcount = 0;
                            boolean lflag = true;
                            @Override
                            public void run() {
                                if(lcount < adaptorMatchGetNum.getItemCount()){
                                    if(lcount==adaptorMatchGetNum.getItemCount()-1){
                                        lflag = false;
                                    }else if(lcount == 0){
                                        lflag = true;
                                    }
                                    if(lflag) lcount++;
                                    else lcount--;

                                    recyclerTossMatchAmt.smoothScrollToPosition(lcount);
                                    handler.postDelayed(this,speedScroll);
                                }
                            }
                        };

                        handler.postDelayed(runnable,speedScroll);



                        final int speedScroll1 = 2500;
                        final Handler handler1 = new Handler();
                        final Runnable runnable1 = new Runnable() {
                            int lcount1 = 0;
                            boolean lflag1 = true;
                            @Override
                            public void run() {
                                if(lcount1 < adaptorMatchGetNumValues.getItemCount()){
                                    if(lcount1==adaptorMatchGetNumValues.getItemCount()-1){
                                        lflag1 = false;
                                    }else if(lcount1 == 0){
                                        lflag1 = true;
                                    }
                                    if(lflag1) lcount1++;
                                    else lcount1--;

                                    recyclerTossMatchValue.smoothScrollToPosition(lcount1);
                                    handler1.postDelayed(this,speedScroll1);
                                }
                            }
                        };

                        handler1.postDelayed(runnable1,speedScroll1);



                    } else {
//                        Toast.makeText(ThreeInOneActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(ForTheWinActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("Type",type);;
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ForTheWinActivity.this);
        requestQueue.add(stringRequest);

    }
    public class AdaptorMatchGetNum extends RecyclerView.Adapter<AdaptorMatchGetNum.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public AdaptorMatchGetNum(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public AdaptorMatchGetNum.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.winner_top_layout, viewGroup, false);
            AdaptorMatchGetNum.VH viewHolder = new AdaptorMatchGetNum.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptorMatchGetNum.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.win_name_tv.setText("WIN\n"+arrayList.get(i).get("UserId"));
            Glide.with(ForTheWinActivity.this)
                    .load(arrayList.get(i).get("Img")).error(R.drawable.a_logo)
                    .into(vh.plane_img);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView win_name_tv;
            ImageView plane_img;
            public VH(@NonNull View itemView) {
                super(itemView);
                plane_img = itemView.findViewById(R.id.plane_img);
                win_name_tv = itemView.findViewById(R.id.win_name_tv);
            }
        }
    }
    public class AdaptorMatchGetNumValues extends RecyclerView.Adapter<AdaptorMatchGetNumValues.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public AdaptorMatchGetNumValues(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public AdaptorMatchGetNumValues.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.looser_top_layout, viewGroup, false);
            AdaptorMatchGetNumValues.VH viewHolder = new AdaptorMatchGetNumValues.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptorMatchGetNumValues.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.win_name_tv.setText("Loos\n"+arrayList.get(i).get("UserId"));
            Glide.with(ForTheWinActivity.this)
                    .load(arrayList.get(i).get("Img")).error(R.drawable.a_logo)
                    .into(vh.plane_img);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView win_name_tv;
            ImageView plane_img;
            public VH(@NonNull View itemView) {
                super(itemView);
                plane_img = itemView.findViewById(R.id.plane_img);
                win_name_tv = itemView.findViewById(R.id.win_name_tv);
            }
        }
    }


}
