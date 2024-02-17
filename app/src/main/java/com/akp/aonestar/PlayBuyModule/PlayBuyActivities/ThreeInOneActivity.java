package com.akp.aonestar.PlayBuyModule.PlayBuyActivities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.GameSelection;
import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Basic.Oder_PersonalDetails;
import com.akp.aonestar.Basic.Oder_Summery_Details;
import com.akp.aonestar.GoodLuckModule.AdapterForBanner;
import com.akp.aonestar.GoodLuckModule.AnimationHelper;
import com.akp.aonestar.GoodLuckModule.BannerData;
import com.akp.aonestar.PlayBuyModule.NetworkManager.DialogManager;
import com.akp.aonestar.PlayBuyModule.NetworkManager.InternetConnection;
import com.akp.aonestar.PlayBuyModule.NetworkManager.MyVolleySingleton;
import com.akp.aonestar.PlayBuyModule.NetworkManager.WebServices;
import com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors.AdaptorImageList;
import com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors.AdaptorImageSlider;
import com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors.AdaptorProductList;
import com.akp.aonestar.PlayBuyModule.PlayBuyDashboard;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Banner.BannerModel;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Category.DataResponseCategory;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Category.ResponseItem;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ModelImg;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ModelProduct;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.DataResponseProduct;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ThreeInOne.DataResponseThreeIn;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.TossBossMatchAmt.DataResTossMatchAmt;
import com.akp.aonestar.PlayBuyModule.PlayBuy_Oder_Summery_Details;
import com.akp.aonestar.PlayBuyModule.ScratchCardManager.ScratchCard;
import com.akp.aonestar.PlayBuyModule.SundaySpecial;
import com.akp.aonestar.PriceDropModule.PriceDropDashboard;
import com.akp.aonestar.PriceDropModule.PriceDropTrailProduct;
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

public class ThreeInOneActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerProductList, recyclerDisProductList1, recyclerDisProductList2,
            recyclerDisProductList3, recyclerCategoryList, recyclerCircleData,recycScratchList;
    List<ModelProduct> dealModels = new ArrayList<>();
    AdaptorProductList adaptorBigDealModel;

    LinearLayout linPitch1, linPitch2, linPitchY1, linPitchRed, linearThreePlay, linearProDetails;

    String cateName, categoryId;
    AdaptorMainCategoryList adaptorMainCategoryList;
    AdaptorImageSlider adaptorImageSlider;
    TextView tvKitchenGood, tv100, tv50, tvJackPot, tvDisArt3, tvDisArt1, tv1000, tvDisArt2, tv500, tv700;
    ProgressDialog progressDialog;
    String productImage, productName, productPrice, productCode, productDisAmt,productCategoryName, selectAmt = "10";
    Dialog alertDialog, alertDialog1;


    TextView value_tv;
    LottieAnimationView animationView;
    FloatingActionButton close;
    AdaptorCircleDataList adaptorCircleDataList;
    private String UserId;

    LinearLayout second_layout_ll,first_layout_ll;


    TextView res_one_tv,res_two_tv,res_three_tv;



    private final int limit = 6;
    ImageView norecord_tv,norecord_tv1,norecord_tv2,norecord_tv3;
    private Dialog dialog,alertDialogamt;
    ArrayList<HashMap<String, String>> arrayList5 = new ArrayList<>();
    int i=0;
    ArrayList<HashMap<String, String>> arrayList6 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList7 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList8 = new ArrayList<>();
 AdaptorScratchProductList adaptorScratchProductList;
    List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> listProduct= new ArrayList<>();
    boolean dummyAmt = false;
    CirclePageIndicator indicator;
    ViewPager pager;
    List<BannerData> bannerData = new ArrayList<>();
    private static int currentPage = 0;
    private AlertDialog alertDialog_loos,alertDialog_win;
    RecyclerView recyclerSelectAmt;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private FriendsListAdapter pdfAdapTer;
    private int selectedPosition1 = -1;
    String SelectedNumber;
    private int disabledPosition = -1; // Initialize with an invalid position
    ArrayList<HashMap<String, String>> arrayListwin = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayListloss = new ArrayList<>();
    RecyclerView recyclerTossMatchAmt, recyclerTossMatchValue;
    AdaptorMatchGetNum adaptorMatchGetNum;
    AdaptorMatchGetNumValues adaptorMatchGetNumValues;
    MediaPlayer mp;  private boolean isClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_in_one);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        mp = MediaPlayer.create(ThreeInOneActivity.this, R.raw.akp_click);
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();
        GifImageView gl_img = findViewById(R.id.gl_img);
        GifImageView pd_img = findViewById(R.id.pd_img);
        GifImageView spot_img = findViewById(R.id.spot_img);
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
        });  GifImageView sunday_sp = findViewById(R.id.sunday_sp);
        sunday_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SundaySpecial.class);
                startActivity(intent);
            }
        });
        initViews();
        GetWinnerList("Win");
        GetLooserList("Loss");

    }

    private void initViews() { recycScratchList = findViewById(R.id.recycScratchList);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager1);
        progressDialog = new ProgressDialog(this);

        animationView = findViewById(R.id.animationView);
        tvKitchenGood = findViewById(R.id.tvKitchenGood);
        close = findViewById(R.id.close);
        tv100 = findViewById(R.id.tv100);
        tv50 = findViewById(R.id.tv50);
        tvJackPot = findViewById(R.id.tvJackPot);
        tvDisArt3 = findViewById(R.id.tvDisArt3);
        tvDisArt1 = findViewById(R.id.tvDisArt1);
        tv1000 = findViewById(R.id.tv1000);
        tv500 = findViewById(R.id.tv500);
        tv700 = findViewById(R.id.tv700);
        tvDisArt2 = findViewById(R.id.tvDisArt2);
        linPitch1 = findViewById(R.id.linPitch1);
        linPitch2 = findViewById(R.id.linPitch2);
        linPitchY1 = findViewById(R.id.linPitchY1);
        linPitchRed = findViewById(R.id.linPitchRed);
        linearThreePlay = findViewById(R.id.linearThreePlay);
        linearProDetails = findViewById(R.id.linearProDetails);
        recyclerCategoryList = findViewById(R.id.recyclerCategoryList);
        recyclerCircleData = findViewById(R.id.recyclerCircleData);
        recyclerTossMatchAmt = findViewById(R.id.recyclerTossMatchAmtwinner);
        recyclerTossMatchValue = findViewById(R.id.recyclerTossMatchValuelooser);
        recyclerTossMatchAmt.setLayoutManager(new LinearLayoutManager(this));
        recyclerTossMatchAmt.setHasFixedSize(true);
        recyclerTossMatchValue.setLayoutManager(new LinearLayoutManager(this));
        recyclerTossMatchValue.setHasFixedSize(true);
        res_one_tv= findViewById(R.id.res_one_tv);
        res_two_tv= findViewById(R.id.res_two_tv);
        res_three_tv= findViewById(R.id.res_three_tv);

        first_layout_ll= findViewById(R.id.first_layout_ll);
        second_layout_ll= findViewById(R.id.second_layout_ll);
        recycScratchList.setLayoutManager(new GridLayoutManager(this, 2));
        recycScratchList.setHasFixedSize(true);

        value_tv= findViewById(R.id.value_tv);
        TextView  one_tv= findViewById(R.id.one_tv);
        TextView  two_tv= findViewById(R.id.two_tv);
        TextView  three_tv= findViewById(R.id.three_tv);
        TextView  four_tv= findViewById(R.id.four_tv);
        TextView  five_tv= findViewById(R.id.five_tv);
        TextView six_tv= findViewById(R.id.six_tv);
        TextView seven_tv= findViewById(R.id.seven_tv);
        TextView eight_tv= findViewById(R.id.eight_tv);



        one_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_tv.setText("1");
                one_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_red);
                two_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                three_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                four_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                five_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                six_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                seven_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                eight_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
            }
        });

        two_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_tv.setText("2");
                one_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                two_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_red);
                three_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                four_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                five_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                six_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                seven_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                eight_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
            }
        });

        three_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_tv.setText("3");
                one_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                two_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                three_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_red);
                four_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                five_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                six_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                seven_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                eight_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
            }
        });
        four_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_tv.setText("4");
                one_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                two_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                three_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                four_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_red);
                five_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                six_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                seven_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                eight_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
            }
        });

        five_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_tv.setText("5");
                one_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                two_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                three_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                four_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                five_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_red);
                six_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                seven_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                eight_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
            }
        });
        six_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_tv.setText("6");
                one_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                two_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                three_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                four_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                five_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                six_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_red);
                seven_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                eight_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
            }
        });
        seven_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_tv.setText("7");
                one_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                two_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                three_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                four_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                five_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                six_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                seven_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_red);
                eight_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
            }
        });
        eight_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_tv.setText("8");
                one_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                two_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                three_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                four_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                five_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                six_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                seven_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_yellow);
                eight_tv.setBackgroundResource(R.drawable.back_white_sqare_rect_red);
            }
        });

        linPitch1.setOnClickListener(this);
        linPitch2.setOnClickListener(this);
        linPitchY1.setOnClickListener(this);
        linPitchRed.setOnClickListener(this);
        linearThreePlay.setOnClickListener(this);
        linearProDetails.setOnClickListener(this);
        close.setOnClickListener(this);

        recyclerCircleData.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerCircleData.setHasFixedSize(true);
        recyclerCategoryList.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategoryList.setHasFixedSize(true);


        categoryId = getIntent().getStringExtra("catId");
        cateName = getIntent().getStringExtra("catName");



        if (InternetConnection.checkConnection(getApplicationContext())) {
            getMainCatList();
            getCatBannerList(categoryId);
            amountWiseProduct("10");
        } else {
            DialogManager.openCheckInternetDialog(ThreeInOneActivity.this);
        }

        ScretchgetCatProdList();
    }


    private void getMainCatList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.CategoryList, null, response -> {
            Gson gson = new Gson();
            try {
                DataResponseCategory dataResponseCategory = gson.fromJson(response.toString(), DataResponseCategory.class);
                if (dataResponseCategory.isStatus()) {
                    adaptorMainCategoryList = new AdaptorMainCategoryList(getApplicationContext(), dataResponseCategory.getResponse());
                    recyclerCategoryList.setAdapter(adaptorMainCategoryList);
                } else {
                    Toast.makeText(ThreeInOneActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
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
//            case R.id.linPitch1:
//                selectAmt = "0";
//                amountWiseProduct("0");
//                linPitch1.setBackgroundResource(R.drawable.pitch_red);
//                linPitch2.setBackgroundResource(R.drawable.pitch_yellow);
//                linPitchRed.setBackgroundResource(R.drawable.pitch_yellow);
//                linPitchY1.setBackgroundResource(R.drawable.pitch_yellow);
//                break;

            case R.id.linPitch2:
                selectAmt = "10";
                amountWiseProduct("10");
                linPitch1.setBackgroundResource(R.drawable.pitch_yellow);
                linPitch2.setBackgroundResource(R.drawable.pitch_red);
                linPitchRed.setBackgroundResource(R.drawable.pitch_yellow);
                linPitchY1.setBackgroundResource(R.drawable.pitch_yellow);
                break;

            case R.id.linPitchY1:
                selectAmt = "50";
                amountWiseProduct("50");
                linPitch1.setBackgroundResource(R.drawable.pitch_yellow);
                linPitch2.setBackgroundResource(R.drawable.pitch_yellow);
                linPitchRed.setBackgroundResource(R.drawable.pitch_yellow);
                linPitchY1.setBackgroundResource(R.drawable.pitch_red);
                break;

            case R.id.linPitchRed:
                selectAmt = "20";
                amountWiseProduct("20");

                linPitch1.setBackgroundResource(R.drawable.pitch_yellow);
                linPitch2.setBackgroundResource(R.drawable.pitch_yellow);
                linPitchRed.setBackgroundResource(R.drawable.pitch_red);
                linPitchY1.setBackgroundResource(R.drawable.pitch_yellow);
                break;

            case R.id.linearThreePlay:
                if (value_tv.getText().toString().equalsIgnoreCase("?")){
                    Toast.makeText(getApplicationContext(),"PICK ANY NUMBER",Toast.LENGTH_LONG).show();
                }
                else {
                    openPlayDialog();
//                    openAlertDialog(selectAmt);
                }
                break;
            case R.id.close:
                GameDetails_popup(categoryId);
                break;

            case R.id.linearProDetails:
                showProductDetails();
                break;
        }
    }

    private void showProductDetails() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(ThreeInOneActivity.this).inflate(R.layout.three_in_one_product_details, viewGroup, false);

        norecord_tv = dialogView.findViewById(R.id.norecord_tv);
        norecord_tv1 = dialogView.findViewById(R.id.norecord_tv1);
        norecord_tv2 = dialogView.findViewById(R.id.norecord_tv2);
        norecord_tv3 = dialogView.findViewById(R.id.norecord_tv3);


        recyclerProductList = dialogView.findViewById(R.id.recyclerProductList);
        recyclerDisProductList1 = dialogView.findViewById(R.id.recyclerDisProductList1);
        recyclerDisProductList2 = dialogView.findViewById(R.id.recyclerDisProductList2);
        recyclerDisProductList3 = dialogView.findViewById(R.id.recyclerDisProductList3);
        LinearLayout close = dialogView.findViewById(R.id.linearClose);


        getHistory("1");
        getHistory1("4");
        getHistory2("3");
        getHistory3("2");


//        recyclerProductList.setHasFixedSize(true);
//        recyclerDisProductList1.setHasFixedSize(true);
//        recyclerDisProductList2.setHasFixedSize(true);
//        recyclerDisProductList3.setHasFixedSize(true);
//        recyclerProductList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
//        recyclerDisProductList1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
//        recyclerDisProductList2.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
//        recyclerDisProductList3.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
//
//        dealModels.add(new ModelProduct(R.drawable.big_img, "MICROWAVE", "UNDER ₹ 600"));
//        dealModels.add(new ModelProduct(R.drawable.big_img, "MICROWAVE", "UNDER ₹ 600"));
//        dealModels.add(new ModelProduct(R.drawable.big_img, "MICROWAVE", "UNDER ₹ 600"));
//
//        adaptorBigDealModel = new AdaptorProductList(getApplicationContext(), dealModels);
//        recyclerProductList.setAdapter(adaptorBigDealModel);
//        recyclerDisProductList1.setAdapter(adaptorBigDealModel);
//        recyclerDisProductList2.setAdapter(adaptorBigDealModel);
//        recyclerDisProductList3.setAdapter(adaptorBigDealModel);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ThreeInOneActivity.this);
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

    private void GameDetails_popup(String getId) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(ThreeInOneActivity.this).inflate(R.layout.popup_gameoverview, viewGroup, false);

        TextView view_details_tv = dialogView.findViewById(R.id.view_details_tv);


        FloatingActionButton close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
//        arrayList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(ThreeInOneActivity.this);
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
                    Toast.makeText(ThreeInOneActivity.this, "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        }, error -> {
            progressDialog.dismiss();
            Log.d("myTag", "message:" + error);
            Toast.makeText(ThreeInOneActivity.this, "No Record Found!", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
        requestQueue.add(stringRequest);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ThreeInOneActivity.this);
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

    private void openAlertDialog(String money) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ThreeInOneActivity.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
        builder.setTitle("Alert Confirmation for Amount:- ₹ " + money)
                .setMessage("Are you sure you want to Play?")
                .setCancelable(false)
                .setIcon(R.drawable.logo)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        first_layout_ll.setVisibility(View.GONE);
                        second_layout_ll.setVisibility(View.VISIBLE);
                        linearThreePlay.setVisibility(View.GONE);
                        PLAYBUY_TripleMatchNumberPlayNowAPI("10",value_tv.getText().toString());


//                        getProductDetails(categoryId, money);
                        dialog.cancel();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        first_layout_ll.setVisibility(View.VISIBLE);
                        second_layout_ll.setVisibility(View.GONE);
                        /*Intent intent=new Intent(getApplicationContext(),PriceDropDashboard.class);
                        startActivity(intent);*/
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    private void amountWiseProduct(String money) {
        /*progressDialog.show();
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);*/

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Amount", money);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ThreeInoneGame, jsonObject, response -> {
            Gson gson = new Gson();
            //progressDialog.dismiss();
            try {
                DataResponseThreeIn responseThreeIn = gson.fromJson(response.toString(), DataResponseThreeIn.class);
                if (responseThreeIn.isStatus()) {
                    adaptorCircleDataList = new AdaptorCircleDataList(getApplicationContext(), responseThreeIn.getResponse());
                    recyclerCircleData.setAdapter(adaptorCircleDataList);
                    /*tvJackPot.setText(responseThreeIn.getResponse().get(0).getCategoryName());
                    tvDisArt1.setText(responseThreeIn.getResponse().get(1).getCategoryName());
                    tv50.setText(responseThreeIn.getResponse().get(2).getCategoryName());
                    tvDisArt2.setText(responseThreeIn.getResponse().get(3).getCategoryName());
                    tv100.setText(responseThreeIn.getResponse().get(4).getCategoryName());
                    tv500.setText(responseThreeIn.getResponse().get(5).getCategoryName());
                    tvDisArt3.setText(responseThreeIn.getResponse().get(6).getCategoryName());
                    tvKitchenGood.setText(responseThreeIn.getResponse().get(7).getCategoryName());
                    tv1000.setText(responseThreeIn.getResponse().get(8).getCategoryName());
                    tv700.setText(responseThreeIn.getResponse().get(9).getCategoryName());*/
                } else {
                    Toast.makeText(ThreeInOneActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                //progressDialog.dismiss();
                throw new RuntimeException(e);
            }
        }, error -> {
            //progressDialog.dismiss();
            Log.e("TAG", "onErrorResponse: " + error.toString());
        });
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void getProductDetails(String catId, String money) {
        progressDialog.show();
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CategoryId", catId);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Amount", money);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Log.e("TAG", "Request: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ThreeInonePlayNow, jsonObject, response -> {
            try {
                Log.e("TAG", "getScratchProductDetailsaa: " + response);
                if (response.getBoolean("Status")) {
                    progressDialog.dismiss();
                    animationView.setVisibility(View.VISIBLE);
                    recyclerCircleData.setVisibility(View.GONE);
                    dummyAmt = true;
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    productImage = jsonObject1.getString("ProductImg");
                    productName = jsonObject1.getString("ProductName");
                    productCode = String.valueOf(jsonObject1.getInt("ProductCode"));
                    productPrice = String.valueOf(jsonObject1.getDouble("ProductSaleRate"));
                    productDisAmt = String.valueOf(jsonObject1.getDouble("DisAmt"));
                    productCategoryName = String.valueOf(jsonObject1.getString("CategoryName"));

                    amountWiseProduct("10");


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animationView.setVisibility(View.GONE);
//                            openScratchDialogBox(productImage, productName, productCode,"₹ " + productPrice,productCategoryName);
                        }
                    }, 3200);

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

    @SuppressLint("SetTextI18n")
    private void openScratchDialogBox(String Number) {
        final MediaPlayer mp = MediaPlayer.create(ThreeInOneActivity.this, R.raw.blast_one);
        mp.start();
        alertDialog = new Dialog(ThreeInOneActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_scratch_layout_sunday);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans)));
        alertDialog.setCancelable(false);
        alertDialog.show();
        //alertDialog.getWindow().setLayout((6 * width)/7, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView tv_btn = alertDialog.findViewById(R.id.tv_btn);
        GifImageView gif_a = alertDialog.findViewById(R.id.gif_a);
        TextView numb = alertDialog.findViewById(R.id.numb);
        LinearLayout linearProceed = alertDialog.findViewById(R.id.linearProceed);
        ScratchCard scratchCard1 = alertDialog.findViewById(R.id.scratchCard1);

        linearProceed.setVisibility(View.GONE);
        numb.setText(Number);
        tv_btn.setText("Add Number");

//        if (productName.equalsIgnoreCase("Better Luck Next Time")){
//
//            linearProceed.setVisibility(View.GONE);
//            gif_a.setVisibility(View.GONE);
//            proName.setText(productName);
//
//        }
//
//
//
//        linearExit.setOnClickListener(v -> {
//            alertDialog.dismiss();
//            mp.stop();
//            Intent intent=new Intent(SundaySpecial.this,SundaySpecial.class);
//            startActivity(intent);
//        });

        scratchCard1.setOnScratchListener(new ScratchCard.OnScratchListener() {
            @Override
            public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                if (visiblePercent > 0.8) {
//                        scratch(true);
                    mp.stop();
                    if (value_tv.getText().toString().equalsIgnoreCase(Number)){

                    if (res_one_tv.getText().toString().equalsIgnoreCase("*")){
                        res_one_tv.setText(Number);
                        alertDialog.dismiss();
                    }
                    else if (res_two_tv.getText().toString().equalsIgnoreCase("*")){
                        res_two_tv.setText(Number);
                        alertDialog.dismiss();
                    }
                    else if (res_three_tv.getText().toString().equalsIgnoreCase("*")){
                        res_three_tv.setText(Number);
                        MatchedNumberAPI(value_tv.getText().toString(),SelectedNumber,res_one_tv.getText().toString(),res_two_tv.getText().toString(),res_three_tv.getText().toString());
                        alertDialog.dismiss();

                    }
                    }
                    else {
                        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        //then we will inflate the custom alert dialog xml that we created
                        View dialogView = LayoutInflater.from(ThreeInOneActivity.this).inflate(R.layout.dynamic_loosepopup, viewGroup, false);
                        Button rlBack = (AppCompatButton) dialogView.findViewById(R.id.btncancel);
                        TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);
                        txt_msg.setText("Oops Sorry!! Number Not Matched\nBetter Luck Next Time😭😭😭😭");
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ThreeInOneActivity.this);
                        //setting the view of the builder to our custom view that we already inflated
                        builder.setView(dialogView);
                        //finally creating the alert dialog and displaying it
                        alertDialog_loos = builder.create();
                        alertDialog_loos.show();
                    }






                       /* update 17-05-2023 Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_Summery_Details.class);
                        intent.putExtra("p_code",productCode);
                        intent.putExtra("p_userid",UserId);
                        intent.putExtra("p_disamount",productDisAmt);
                        intent.putExtra("p_price",productPrice);
                        intent.putExtra("p_name",productName);
                        intent.putExtra("p_img",productImg);
                        intent.putExtra("class_name","scretchget");
                        startActivity(intent);update 17-05-2023 */
                }
            }
        });



    }

    private void callBuyProductDetails() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProductCode", productCode);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Discount", productDisAmt);
            jsonObject.put("Amount", productPrice);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ThreeInonePlay_Purchase, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    alertDialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                } else {
                    alertDialog.dismiss();
                    Toast.makeText(this, "Error:- "+response.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                alertDialog.dismiss();
                throw new RuntimeException(e);
            }

        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public class AdaptorMainCategoryList extends RecyclerView.Adapter<AdaptorMainCategoryList.MyCategoryHolder> {

        Context context;
        List<ResponseItem> catList;

        public AdaptorMainCategoryList(Context context, List<ResponseItem> catList) {
            this.context = context;
            this.catList = catList;
        }

        @NonNull
        @Override
        public MyCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyCategoryHolder(LayoutInflater.from(context).inflate(R.layout.main_category_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyCategoryHolder holder, @SuppressLint("RecyclerView") int position) {
            Glide.with(context)
                    .load(catList.get(position).getCatImg())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.catImage);
            //Glide.with(ThreeInOneActivity.this).load(catList.get(position).getCatImg()).error(R.drawable.logo).into(holder.catImage);

            holder.tvCatName.setText(catList.get(position).getCategoryName());
            if (catList.get(position).getCategoryName().equalsIgnoreCase(cateName)) {
                holder.linearLayout.setBackgroundResource(R.drawable.back_yellow);
                holder.tvCatName.setTextColor(ContextCompat.getColor(context, R.color.black));
            }

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (catList.get(position).getCategoryName()) {
                        case "SCRATCH & GET":
                            mp.start();
                            startActivity(new Intent(getApplicationContext(), PlayBuyDashboard.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName())
                            );
                            finish();
                            break;

                        case "TOSS BOSS":
                            mp.start();
                            startActivity(new Intent(getApplicationContext(), TossBossActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName()));
                            finish();
                            break;

                        case "LUCK & WIN":
                            mp.start();
                            startActivity(new Intent(getApplicationContext(), MatchAndGetActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName())
                            );
                            finish();
                            break;

                        case "FOR THE WIN":
                            mp.start();
                            startActivity(new Intent(getApplicationContext(), ForTheWinActivity.class)
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

    public class AdaptorCircleDataList extends RecyclerView.Adapter<AdaptorCircleDataList.MyCircleHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ThreeInOne.ResponseItem> list;

        public AdaptorCircleDataList(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ThreeInOne.ResponseItem> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MyCircleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyCircleHolder(LayoutInflater.from(context).inflate(R.layout.circle_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyCircleHolder holder, int position) {
            holder.tvProPrice.setText(list.get(position).getCategoryName());
            if (dummyAmt) {
                if (String.valueOf(list.get(position).getCategoryName()).equals(productCategoryName)) {
                    holder.tvProPrice.setBackgroundResource(R.drawable.back_selected_red);
                }
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyCircleHolder extends RecyclerView.ViewHolder {
            TextView tvProPrice;

            public MyCircleHolder(@NonNull View itemView) {
                super(itemView);
                tvProPrice = itemView.findViewById(R.id.tvProPrice);
            }
        }
    }







    public void getHistory(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(ThreeInOneActivity.this);
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
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ThreeInOneActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        TaskEarningAdapter customerListAdapter = new TaskEarningAdapter(ThreeInOneActivity.this, arrayList5);
                        recyclerProductList.setLayoutManager(gridLayoutManager);
                        recyclerProductList.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
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
                Toast.makeText(ThreeInOneActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter extends RecyclerView.Adapter<TaskEarningAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public TaskEarningAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            TaskEarningAdapter.VH viewHolder = new TaskEarningAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskEarningAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(ThreeInOneActivity.this)
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
        dialog = new Dialog(ThreeInOneActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(ThreeInOneActivity.this);
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
                        Glide.with(ThreeInOneActivity.this)
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
                        Toast.makeText(ThreeInOneActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ThreeInOneActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails





    public void getHistory1(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(ThreeInOneActivity.this);
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
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ThreeInOneActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        TaskEarningAdapter1 customerListAdapter = new TaskEarningAdapter1(ThreeInOneActivity.this, arrayList6);
                        recyclerDisProductList1.setLayoutManager(gridLayoutManager);
                        recyclerDisProductList1.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv1.setVisibility(View.VISIBLE);
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
                Toast.makeText(ThreeInOneActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter1 extends RecyclerView.Adapter<TaskEarningAdapter1.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter1(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public TaskEarningAdapter1.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            TaskEarningAdapter1.VH viewHolder = new TaskEarningAdapter1.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskEarningAdapter1.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(ThreeInOneActivity.this)
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
        dialog = new Dialog(ThreeInOneActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(ThreeInOneActivity.this);
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
                        Glide.with(ThreeInOneActivity.this)
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
                        Toast.makeText(ThreeInOneActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ThreeInOneActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails











    public void getHistory2(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(ThreeInOneActivity.this);
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
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ThreeInOneActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        TaskEarningAdapter2 customerListAdapter = new TaskEarningAdapter2(ThreeInOneActivity.this, arrayList7);
                        recyclerDisProductList2.setLayoutManager(gridLayoutManager);
                        recyclerDisProductList2.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv2.setVisibility(View.VISIBLE);
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
                Toast.makeText(ThreeInOneActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter2 extends RecyclerView.Adapter<TaskEarningAdapter2.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter2(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public TaskEarningAdapter2.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            TaskEarningAdapter2.VH viewHolder = new TaskEarningAdapter2.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskEarningAdapter2.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(ThreeInOneActivity.this)
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
        dialog = new Dialog(ThreeInOneActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(ThreeInOneActivity.this);
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
                        Glide.with(ThreeInOneActivity.this)
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
                        Toast.makeText(ThreeInOneActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ThreeInOneActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails










    public void getHistory3(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(ThreeInOneActivity.this);
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
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ThreeInOneActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        TaskEarningAdapter3 customerListAdapter = new TaskEarningAdapter3(ThreeInOneActivity.this, arrayList8);
                        recyclerDisProductList3.setLayoutManager(gridLayoutManager);
                        recyclerDisProductList3.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv3.setVisibility(View.VISIBLE);
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
                Toast.makeText(ThreeInOneActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter3 extends RecyclerView.Adapter<TaskEarningAdapter3.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter3(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public TaskEarningAdapter3.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            TaskEarningAdapter3.VH viewHolder = new TaskEarningAdapter3.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskEarningAdapter3.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(ThreeInOneActivity.this)
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
        dialog = new Dialog(ThreeInOneActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(ThreeInOneActivity.this);
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
                        Glide.with(ThreeInOneActivity.this)
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
                        Toast.makeText(ThreeInOneActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ThreeInOneActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails




    private void getCatBannerList(String id) {
        final ProgressDialog progressDialog = new ProgressDialog(ThreeInOneActivity.this);
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
                    pager.setAdapter(new AdapterForBanner(ThreeInOneActivity.this, bannerData));
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
        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
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




    private void ScretchgetCatProdList() {
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.PLAYBUY_TripleMatchNumberList, jsonObject, response -> {
            Log.d("fjgfjhfg",""+response);
            Gson gson = new Gson();
            try {
                DataResponseProduct productCategoryModel = gson.fromJson(response.toString(), DataResponseProduct.class);
                if (productCategoryModel.isStatus()) {
                    adaptorScratchProductList = new AdaptorScratchProductList(getApplicationContext(), productCategoryModel.getResponse());
                    recycScratchList.setAdapter(adaptorScratchProductList);



                } else {
                    Toast.makeText(ThreeInOneActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public class AdaptorScratchProductList extends RecyclerView.Adapter<AdaptorScratchProductList.MyScratchHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList1;

        public AdaptorScratchProductList(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList) {
            this.context = context;
            this.itemList1 = itemList;
        }

        @NonNull
        @Override
        public AdaptorScratchProductList.MyScratchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptorScratchProductList.MyScratchHolder(LayoutInflater.from(context).inflate(R.layout.scratch_layout_thiplematch, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptorScratchProductList.MyScratchHolder holder, @SuppressLint("RecyclerView") int position) {
            com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem item = itemList1.get(position);
//            Log.d("sfffa",itemList1.get(position).getNumber());
//            Glide.with(context)
//                    .load(itemList.get(position).getProductImg())
//                    .placeholder(R.drawable.logo)
//                    .error(R.drawable.logo)
//                    .into(holder.pro_img);
            holder.scratch_tvMNum.setText(itemList1.get(position).getNumber());

            holder.itemView.setEnabled(!item.isClicked()); // Enable or disable the entire item based on its clicked state
            // Set the background color for disabled items
            int disabledColor = ContextCompat.getColor(context, R.color.dark_gray);
            int itemBackgroundColor = item.isClicked() ? Color.RED : disabledColor;
            holder.view_ll.setBackgroundColor(itemBackgroundColor);

//            // Check if the current position matches the disabledPosition
//            if (position == disabledPosition) {
//                // Disable the item or change its appearance
//                holder.itemView.setEnabled(false);
//                holder.view_ll.setBackgroundResource(R.drawable.alredyscretch);
//                // You can also change the background color, text color, etc.
//            } else {
//                holder.itemView.setEnabled(true);
//                // Reset the appearance of the item to its default state
//            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        updateItemClickedState(position);
                    }

//                    // Update disabledPosition and notify adapter
//                    disabledPosition = holder.getAdapterPosition();
//                    notifyDataSetChanged();
                    openScratchDialogBox(itemList1.get(position).getNumber());
                }
            });


        }
        public void updateItemClickedState(int position) {
            com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem item = itemList1.get(position);
            item.setClicked(!item.isClicked()); // Update the clicked state of the item
            notifyItemChanged(position); // Refresh the UI for the specific item
        }


        @Override
        public int getItemCount() {
             if(itemList1.size() > limit){
                return limit;
            }
            else
            {
                return itemList1.size();
            }
//            return itemList1.size();
        }

        public class MyScratchHolder extends RecyclerView.ViewHolder {
            TextView scratch_tvMNum;
            LinearLayout view_ll;


            public MyScratchHolder(@NonNull View itemView) {
                super(itemView);
                scratch_tvMNum = itemView.findViewById(R.id.scratch_tvMNum);
                view_ll = itemView.findViewById(R.id.view_ll);
            }
        }
    }



    private void PLAYBUY_TripleMatchNumberPlayNowAPI(String p_betamt,String p_nub) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", UserId);
            jsonObject.put("BetAmount", p_betamt);
            jsonObject.put("Number",  p_nub);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_TripleMatchNumberPlayNow, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status") == true) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    second_layout_ll.setVisibility(View.VISIBLE);
                    first_layout_ll.setVisibility(View.GONE);
                    linearThreePlay.setVisibility(View.GONE);

                } else {
                    Toast.makeText(this, response.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }




    private void MatchedNumberAPI(String select_no,String bet_amt,String num1,String num2,String num3) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", UserId);
            jsonObject.put("SelectedNumber", select_no);
            jsonObject.put("BetAmount",  bet_amt);
            jsonObject.put("Number1",  num1);
            jsonObject.put("Number2",  num2);
            jsonObject.put("Number3",  num3);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_Tripple_MatchPurchase, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status") == true) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(ThreeInOneActivity.this).inflate(R.layout.dynamic_winpopup, viewGroup, false);
                    Button rlBack = (AppCompatButton) dialogView.findViewById(R.id.btncancel);
                    TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);

                    Button buy_btn =(AppCompatButton)dialogView.findViewById(R.id.buy_btn);
                    buy_btn.setVisibility(View.GONE);
                    txt_msg.setText(jsonObject1.getString("Msg"));
                    rlBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            alertDialog_win.dismiss();
                        }
                    });
                    //Now we need an AlertDialog.Builder object
                    AlertDialog.Builder builder = new AlertDialog.Builder(ThreeInOneActivity.this);
                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView);
                    //finally creating the alert dialog and displaying it
                    alertDialog_win = builder.create();
                    alertDialog_win.show();



                } else {
                    Toast.makeText(this, response.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }



    private void openPlayDialog() {
        alertDialogamt = new Dialog(ThreeInOneActivity.this);
        alertDialogamt.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogamt.setContentView(R.layout.popup_amountnew);
        alertDialogamt.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialogamt.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans)));
        alertDialogamt.setCancelable(false);
        alertDialogamt.show();
        //alertDialog.getWindow().setLayout((6 * width)/7, ActionBar.LayoutParams.WRAP_CONTENT);

        recyclerSelectAmt = alertDialogamt.findViewById(R.id.recyclerSelectAmt);

        TextView tvYes = alertDialogamt.findViewById(R.id.tvYes);
        TextView tvCancel = alertDialogamt.findViewById(R.id.tvCancel);

        GetGamePriceAPI();
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),""+SelectedNumber,Toast.LENGTH_LONG).show();
                PLAYBUY_TripleMatchNumberPlayNowAPI(SelectedNumber,value_tv.getText().toString());

                alertDialogamt.cancel();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrFriendsList.clear();
                alertDialogamt.dismiss();
            }
        });
//

    }




    public void GetGamePriceAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+"PLAYBUY_BetAmount", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("ddres", response);
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("BetAmount", jsonObject.getString("BetAmount"));
                            arrFriendsList.add(hashlist);
                        }
                        recyclerSelectAmt.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
                        recyclerSelectAmt.setHasFixedSize(true);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        recyclerSelectAmt.setAdapter(pdfAdapTer);
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ThreeInOneActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
        requestQueue.add(stringRequest);

    }
    public class FriendsListAdapter extends RecyclerView.Adapter<FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_layoutamountchoose, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final FriendsList holder, int position) {
            holder.tvNum1.setText(arrFriendsList.get(position).get("BetAmount"));


            holder.linear1Selected.setOnClickListener(view -> {
                selectedPosition1 = holder.getAdapterPosition();
                notifyDataSetChanged();
                SelectedNumber = String.valueOf(arrFriendsList.get(position).get("BetAmount"));
            });

            if (selectedPosition1 == position) {
                holder.linear1Selected.setBackgroundResource(R.drawable.back_selected_red);
                holder.tvNum1.setTextColor(ContextCompat.getColor(ThreeInOneActivity.this, R.color.white));
            } else {
                holder.linear1Selected.setBackgroundResource(R.drawable.back_blue_white);
                holder.tvNum1.setTextColor(ContextCompat.getColor(ThreeInOneActivity.this, R.color.black));
            }




//            holder.linear1Selected.setOnClickListener(view -> {
//                selectedPosition1 = holder.getAdapterPosition();
//                notifyDataSetChanged();
//            });
//            if (selectedPosition1 == position) {
//                holder.linear1Selected.setBackgroundResource(R.drawable.back_selected_red);
//                holder.tvNum1.setTextColor(ContextCompat.getColor(PlayBuyDashboard.this, R.color.white));
//            } else {
//                holder.linear1Selected.setBackgroundResource(R.drawable.back_blue_white);
//                holder.tvNum1.setTextColor(ContextCompat.getColor(PlayBuyDashboard.this, R.color.black));
//            }

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView tvNum1;
        LinearLayout linear1Selected;

        public FriendsList(View itemView) {
            super(itemView);
            tvNum1=itemView.findViewById(R.id.tvNum1);
            linear1Selected=itemView.findViewById(R.id.linear1Selected);

        }
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
                Toast.makeText(ThreeInOneActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
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
                            int count = 0;
                            boolean flag = true;
                            @Override
                            public void run() {
                                if(count < adaptorMatchGetNum.getItemCount()){
                                    if(count==adaptorMatchGetNum.getItemCount()-1){
                                        flag = false;
                                    }else if(count == 0){
                                        flag = true;
                                    }
                                    if(flag) count++;
                                    else count--;

                                    recyclerTossMatchAmt.smoothScrollToPosition(count);
                                    handler.postDelayed(this,speedScroll);
                                }
                            }
                        };

                        handler.postDelayed(runnable,speedScroll);



                        final int speedScroll1 = 2500;
                        final Handler handler1 = new Handler();
                        final Runnable runnable1 = new Runnable() {
                            int count1 = 0;
                            boolean flag1 = true;
                            @Override
                            public void run() {
                                if(count1 < adaptorMatchGetNumValues.getItemCount()){
                                    if(count1==adaptorMatchGetNumValues.getItemCount()-1){
                                        flag1 = false;
                                    }else if(count1 == 0){
                                        flag1 = true;
                                    }
                                    if(flag1) count1++;
                                    else count1--;

                                    recyclerTossMatchValue.smoothScrollToPosition(count1);
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
                Toast.makeText(ThreeInOneActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(ThreeInOneActivity.this);
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
            Glide.with(ThreeInOneActivity.this)
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
            Glide.with(ThreeInOneActivity.this)
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

