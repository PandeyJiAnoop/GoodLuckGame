package com.akp.aonestar.PlayBuyModule.PlayBuyActivities;

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
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.GameSelection;
import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Basic.MyOrderList;
import com.akp.aonestar.Basic.OderDetailsTracking;
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
import com.akp.aonestar.PlayBuyModule.PlayBuyDashboard;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Banner.BannerModel;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Category.DataResponseCategory;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Category.ResponseItem;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ModelImg;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.DataResponseProduct;
import com.akp.aonestar.PlayBuyModule.PlayBuy_Oder_Summery_Details;
import com.akp.aonestar.PlayBuyModule.ScratchCardManager.ScratchCard;
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

public class MatchAndGetActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerCategoryList, recyclerScratchProductList, recycScratchList;

    AdaptorMainCategoryList adaptorMainCategoryList;
    AdaptorImageSlider adaptorImageSlider;

    LinearLayout linearScratchLay, linearPlay, linearScratch;
    FloatingActionButton close;
    AdaptorCateWiseProductList adaptorCateWiseProductList;
    AdaptorCateWiseProductList1 adaptorCateWiseProductList1;
    AdaptorScratchProductList adaptorScratchProductList;

    Dialog alertDialog, alertDialog1,alertDialogamt;
    String productImage, productName, productPrice, productCode, productDisAmt;

    ProgressDialog progressDialog;
    List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> listProduct= new ArrayList<>();
    List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> listProduct1= new ArrayList<>();
    private String UserId;
    private final int limit = 9;
    private Dialog dialog;

    int i=0;

    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private FriendsListAdapter pdfAdapTer;
    RecyclerView recyclerSelectAmt;
    private int selectedPosition1 = -1;
    String SelectedNumber;
    String categoryId,cateName;
    CirclePageIndicator indicator;
    ViewPager pager;
    List<BannerData> bannerData = new ArrayList<>();
    private static int currentPage = 0;


    ArrayList<HashMap<String, String>> arrayListwin = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayListloss = new ArrayList<>();
    RecyclerView recyclerTossMatchAmt, recyclerTossMatchValue;
    AdaptorMatchGetNum adaptorMatchGetNum;
    AdaptorMatchGetNumValues adaptorMatchGetNumValues;
    MediaPlayer mp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_and_get);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        categoryId = getIntent().getStringExtra("catId");
        cateName = getIntent().getStringExtra("catName");
        mp = MediaPlayer.create(MatchAndGetActivity.this, R.raw.akp_click);
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();
        GifImageView gl_img = findViewById(R.id.gl_img);
        GifImageView pd_img = findViewById(R.id.pd_img);
        GifImageView spot_img = findViewById(R.id.spot_img);
        GifImageView sunday_sp = findViewById(R.id.sunday_sp);
        sunday_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SundaySpecial.class);
                startActivity(intent);
            }
        });
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
        initViews();
        GetWinnerList("Win");
        GetLooserList("Loss");
        if (UserId.equalsIgnoreCase("")){
        }
        else {
            BlockUnblockAPI();
        }
    }

    private void initViews() {
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager1);
        progressDialog = new ProgressDialog(MatchAndGetActivity.this);

        linearScratchLay = findViewById(R.id.linearScratchLay);
        linearScratch = findViewById(R.id.linearScratch);
        linearPlay = findViewById(R.id.linearPlay);
        close = findViewById(R.id.close);
        recyclerCategoryList = findViewById(R.id.recyclerCategoryList);
        recycScratchList = findViewById(R.id.recycScratchList);
        recyclerScratchProductList = findViewById(R.id.recyclerScratchProductList);

        linearScratch.setOnClickListener(this);
        linearPlay.setOnClickListener(this);
        close.setOnClickListener(this);

         recyclerCategoryList.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategoryList.setHasFixedSize(true);
        recyclerScratchProductList.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerScratchProductList.setHasFixedSize(true);
        recycScratchList.setLayoutManager(new GridLayoutManager(this, 3));
        recycScratchList.setHasFixedSize(true);

        recyclerTossMatchAmt = findViewById(R.id.recyclerTossMatchAmtwinner);
        recyclerTossMatchValue = findViewById(R.id.recyclerTossMatchValuelooser);
        recyclerTossMatchAmt.setLayoutManager(new LinearLayoutManager(this));
        recyclerTossMatchAmt.setHasFixedSize(true);
        recyclerTossMatchValue.setLayoutManager(new LinearLayoutManager(this));
        recyclerTossMatchValue.setHasFixedSize(true);


        if (InternetConnection.checkConnection(getApplicationContext())) {
            getMainCatList();
            getCatBannerList(categoryId);
            getCatProdList(categoryId);
            getCatProdList1(categoryId);
        } else {
            DialogManager.openCheckInternetDialog(MatchAndGetActivity.this);
        }


    }

    private void getCatProdList(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CategoryId", id);
            jsonObject.put("Search", "");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ScratchPlayProduct, jsonObject, response -> {
            Gson gson = new Gson();
            Log.e("TAG", "scratchCardRecycler: "+response);
            try {
                DataResponseProduct productCategoryModel = gson.fromJson(response.toString(), DataResponseProduct.class);
                if (productCategoryModel.isStatus()) {
                    listProduct.clear();
                    listProduct = productCategoryModel.getResponse();
                    adaptorCateWiseProductList = new AdaptorCateWiseProductList(getApplicationContext(), productCategoryModel.getResponse());
                    recyclerScratchProductList.setAdapter(adaptorCateWiseProductList);
                    adaptorScratchProductList = new AdaptorScratchProductList(getApplicationContext(), productCategoryModel.getResponse());
                    recycScratchList.setAdapter(adaptorScratchProductList);


                } else {
                    Toast.makeText(MatchAndGetActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }



    private void getCatProdList1(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CategoryId", id);
            jsonObject.put("Search", "");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ProductbyCategory, jsonObject, response -> {
            Gson gson = new Gson();
            try {
                DataResponseProduct productCategoryModel = gson.fromJson(response.toString(), DataResponseProduct.class);
                if (productCategoryModel.isStatus()) {
                    listProduct1.clear();
                    listProduct1= productCategoryModel.getResponse();


                } else {
                    Toast.makeText(MatchAndGetActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
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
                    adaptorMainCategoryList = new AdaptorMainCategoryList(getApplicationContext(), dataResponseCategory.getResponse());
                    recyclerCategoryList.setAdapter(adaptorMainCategoryList);
                } else {
                    Toast.makeText(MatchAndGetActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearScratch:
                /*recycScratchList.setVisibility(View.GONE);
                recyclerScratchProductList.setVisibility(View.VISIBLE);*/
                showProductDetails();
                break;

            case R.id.linearPlay:
//                openPlayDialog();
                openAlertDialog();
                break;

            case R.id.close:
                GameDetails_popup("1");
                break;

        }
    }

    private void showProductDetails() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(MatchAndGetActivity.this).inflate(R.layout.show_product_popup_details, viewGroup, false);

        RecyclerView recyclerProductDetails = dialogView.findViewById(R.id.recyclerProductDetails);
        recyclerProductDetails.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerProductDetails.setHasFixedSize(true);
        LinearLayout close = dialogView.findViewById(R.id.linearClose);

        adaptorCateWiseProductList1 = new AdaptorCateWiseProductList1(getApplicationContext(), listProduct1);
        recyclerProductDetails.setAdapter(adaptorCateWiseProductList1);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MatchAndGetActivity.this);
        //setting the view of the builder to our custom view that we already inflated
        builder1.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog1 = builder1.create();
        alertDialog1.show();
        WindowManager.LayoutParams wmlp = alertDialog1.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.99);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.55);
        alertDialog1.getWindow().setLayout(width, height);
    }

    private void GameDetails_popup(String getId) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(MatchAndGetActivity.this).inflate(R.layout.popup_gameoverview, viewGroup, false);

        TextView view_details_tv = dialogView.findViewById(R.id.view_details_tv);

        FloatingActionButton close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
//        arrayList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
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
                    Toast.makeText(MatchAndGetActivity.this, "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        }, error -> {
            progressDialog.dismiss();
            Log.d("myTag", "message:" + error);
            Toast.makeText(MatchAndGetActivity.this, "No Record Found!", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MatchAndGetActivity.this);
        //setting the view of the builder to our custom view that we already inflated
        builder1.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog1 = builder1.create();
        alertDialog1.show();
        WindowManager.LayoutParams wmlp = alertDialog1.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.99);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.55);
        alertDialog1.getWindow().setLayout(width, height);

    }

    private void openAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MatchAndGetActivity.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
        builder.setTitle("Alert Confirmation for Amount:- ₹ 20")
                .setMessage("Are you sure you want to Play?")
                .setCancelable(false)
                .setIcon(R.drawable.logo)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getScratchProductDetails("4","20.0");
                        dialog.cancel();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                        /*Intent intent=new Intent(getApplicationContext(),PriceDropDashboard.class);
                        startActivity(intent);*/
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void getScratchProductDetails(String catId,String betamount) {
        progressDialog.show();
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CategoryId", catId);
            jsonObject.put("UserId", UserId);
            jsonObject.put("BetAmount", betamount);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_LuckAndWinPlayNow, jsonObject, response -> {
            try {
                Log.e("TAG", "getScratchProductDetails: " + response);
                if (response.getBoolean("Status")) {
                    progressDialog.dismiss();
                    recycScratchList.setVisibility(View.VISIBLE);
                    linearPlay.setVisibility(View.GONE);
                    recyclerScratchProductList.setVisibility(View.GONE);
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    productImage = jsonObject1.getString("ProductImg");
                    productName = jsonObject1.getString("ProductName");
                    productCode = String.valueOf(jsonObject1.getInt("ProductCode"));
                    productPrice = String.valueOf(jsonObject1.getDouble("ProductSaleRate"));
                    productDisAmt = String.valueOf(jsonObject1.getDouble("DisAmt"));

                } else {
                    progressDialog.dismiss();
                    recycScratchList.setVisibility(View.GONE);
                    recyclerScratchProductList.setVisibility(View.VISIBLE);
                    Toast.makeText(this, ""+response.getString("Message"), Toast.LENGTH_SHORT).show();


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
            //Glide.with(MatchAndGetActivity.this).load(catList.get(position).getCatImg()).error(R.drawable.logo).into(holder.catImage);

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

                        case "TOSS BOSS": mp.start();
                            startActivity(new Intent(getApplicationContext(), TossBossActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName()));
                            finish();
                            break;

                        case "FOR THE WIN": mp.start();
                            startActivity(new Intent(getApplicationContext(), ForTheWinActivity.class)
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
    public class AdaptorCateWiseProductList extends RecyclerView.Adapter<AdaptorCateWiseProductList.MyCatProduct> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList;

        public AdaptorCateWiseProductList(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public MyCatProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyCatProduct(LayoutInflater.from(context).inflate(R.layout.layout_category_wise_product, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyCatProduct holder, int position) {
            Glide.with(context)
                    .load(itemList.get(position).getProductImg())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.img);
            holder.name.setText(itemList.get(position).getProductName());
            holder.code.setText(String.valueOf("₹ " + itemList.get(position).getProductSaleRate()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup1(String.valueOf(itemList.get(position).getProductCode()));
                }
            });

//            dfgfd
        }

        @Override
        public int getItemCount() {

//            if(itemList.size() > limit){
//                return limit;
//            }
//            else
//            {
//                return itemList.size();
//            }

            return itemList.size();
        }

        public class MyCatProduct extends RecyclerView.ViewHolder {
            ImageView img;
            TextView name, code;

            public MyCatProduct(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                name = itemView.findViewById(R.id.name);
                code = itemView.findViewById(R.id.code);
            }
        }
    }
    public class AdaptorScratchProductList extends RecyclerView.Adapter<AdaptorScratchProductList.MyScratchHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList;

        public AdaptorScratchProductList(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public MyScratchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyScratchHolder(LayoutInflater.from(context).inflate(R.layout.scratch_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyScratchHolder holder, @SuppressLint("RecyclerView") int position) {
//            Glide.with(context)
//                    .load(itemList.get(position).getProductImg())
//                    .placeholder(R.drawable.logo)
//                    .error(R.drawable.logo)
//                    .into(holder.pro_img);
            holder.pro_name.setText(itemList.get(position).getProductName());
            holder.pro_code.setText(String.valueOf("₹ " + itemList.get(position).getProductSaleRate()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Toast.makeText(MatchAndGetActivity.this, ""+itemList.get(position).getProductName()+
                            "\n"+itemList.get(position).getProductSaleRate(), Toast.LENGTH_SHORT).show();*/
                    openScratchDialogBox(productCode,productImage, productName, "₹ " + productPrice);
                }
            });
        }

        @Override
        public int getItemCount() {
             if(itemList.size() > limit){
                return limit;
            }
            else
            {
                return itemList.size();
            }
//            return itemList.size();
        }

        public class MyScratchHolder extends RecyclerView.ViewHolder {

            ImageView pro_img;
            TextView pro_name, pro_code;

            public MyScratchHolder(@NonNull View itemView) {
                super(itemView);
                pro_img = itemView.findViewById(R.id.pro_img);
                pro_name = itemView.findViewById(R.id.pro_name);
                pro_code = itemView.findViewById(R.id.pro_code);
            }
        }
    }




    public class AdaptorCateWiseProductList1 extends RecyclerView.Adapter<AdaptorCateWiseProductList1.MyCatProduct> {
        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList;
        public AdaptorCateWiseProductList1(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public MyCatProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyCatProduct(LayoutInflater.from(context).inflate(R.layout.layout_category_wise_product, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyCatProduct holder, int position) {
            Glide.with(context)
                    .load(itemList.get(position).getProductImg())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.img);
            holder.name.setText(itemList.get(position).getProductName());
            holder.code.setText(String.valueOf("₹ " + itemList.get(position).getProductSaleRate()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup1(String.valueOf(itemList.get(position).getProductCode()));
                }
            });

        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public class MyCatProduct extends RecyclerView.ViewHolder {
            ImageView img;
            TextView name, code;

            public MyCatProduct(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                name = itemView.findViewById(R.id.name);
                code = itemView.findViewById(R.id.code);
            }
        }
    }







    @SuppressLint("SetTextI18n")
    private void openScratchDialogBox(String pcode,String productImg, String productName, String price) {
        final MediaPlayer mp = MediaPlayer.create(MatchAndGetActivity.this, R.raw.blast_one);
        mp.start();
        alertDialog = new Dialog(MatchAndGetActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_scratch_layout);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans)));
        alertDialog.setCancelable(false);
        alertDialog.show();
        //alertDialog.getWindow().setLayout((6 * width)/7, ActionBar.LayoutParams.WRAP_CONTENT);
        TextView tv_name = alertDialog.findViewById(R.id.tv_name);

        GifImageView gif_a = alertDialog.findViewById(R.id.gif_a);
        ImageView imgProduct = alertDialog.findViewById(R.id.imgProduct);
        TextView proName = alertDialog.findViewById(R.id.productName);
        LinearLayout linearExit = alertDialog.findViewById(R.id.linearExit);
        LinearLayout linearProceed = alertDialog.findViewById(R.id.linearProceed);
        ScratchCard scratchCard1 = alertDialog.findViewById(R.id.scratchCard1);
        Glide.with(MatchAndGetActivity.this)
                .load(productImg)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imgProduct);
        proName.setText(productName + "\n" + price);
        tv_name.setText("Buy Now");
        if (productName.equalsIgnoreCase("Better Luck Next Time")){
            linearProceed.setVisibility(View.GONE);
            gif_a.setVisibility(View.GONE);
            proName.setText(productName);

        }
        else{
            scratchCard1.setOnScratchListener(new ScratchCard.OnScratchListener() {
                @Override
                public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                    if (visiblePercent > 0.7) {
//                        scratch(true);
                        mp.stop();
//                        DirectPointAddPlaceOrder(pcode,"0","0");
                        Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_Summery_Details.class);
                        intent.putExtra("p_code",productCode);
                        intent.putExtra("p_userid",UserId);
                        intent.putExtra("p_disamount",productDisAmt);
                        intent.putExtra("p_price",productPrice);
                        intent.putExtra("p_name",productName);
                        intent.putExtra("p_img",productImg);
                        intent.putExtra("class_name","scretchget");
                        startActivity(intent);
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



        linearExit.setOnClickListener(v -> {
            alertDialog.dismiss();
            mp.stop();
            Intent intent=new Intent(MatchAndGetActivity.this,MatchAndGetActivity.class);
            startActivity(intent);
        });

        linearProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Scratch your Card",Toast.LENGTH_LONG).show();
                scratchCard1.setOnScratchListener(new ScratchCard.OnScratchListener() {
                    @Override
                    public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                        if (visiblePercent > 0.7) {
//                        scratch(true);
                            mp.stop();
//                            DirectPointAddPlaceOrder(pcode,"0","0");
//                        update 17-05-2023
                            Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_Summery_Details.class);
                        intent.putExtra("p_code",productCode);
                        intent.putExtra("p_userid",UserId);
                        intent.putExtra("p_disamount",productDisAmt);
                        intent.putExtra("p_price",productPrice);
                        intent.putExtra("p_name",productName);
                        intent.putExtra("p_img",productImg);
                        intent.putExtra("class_name","scretchget");
                        startActivity(intent);
//                        update 17-05-2023
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Scratch your Card",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });








    }



    private void getProductDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProductCode", productCode);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Discount", productDisAmt);
            jsonObject.put("Amount", productPrice);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ScratchGetPlay_Purchase, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    alertDialog.dismiss();
                    recycScratchList.setVisibility(View.GONE);
                    recyclerScratchProductList.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MatchAndGetActivity.this,MatchAndGetActivity.class);
                    startActivity(intent);
                } else {
                    alertDialog.dismiss();
                    Toast.makeText(this, response.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                alertDialog.dismiss();
                throw new RuntimeException(e);
            }

        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }






    //ProductDetailspopupdetails
    private void openOfferpopup1(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(MatchAndGetActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
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
                        Glide.with(MatchAndGetActivity.this)
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
                        Toast.makeText(MatchAndGetActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails


    public void BlockUnblockAPI() {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"UserblockUnblockStatus", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("wallet","sadsad"+response);
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog1.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                    } else {
                        Toast.makeText(MatchAndGetActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MatchAndGetActivity.this);
                        builder.setTitle("Login Alert Confirmation!").setMessage(object.getString("Message")).setCancelable(false).setIcon(R.drawable.logo).setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                                SharedPreferences.Editor editor = myPrefs.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                                startActivity(intent); } });
                        builder.create().show();
                        Toast.makeText(MatchAndGetActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                Log.v("addadada", String.valueOf(params));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);

    }




    private void DirectPointAddPlaceOrder(String p_code,String p_oamt,String p_price) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", UserId);
            jsonObject.put("ProductCode", p_code);
            jsonObject.put("OrderAmount", p_oamt);
            jsonObject.put("Amount",  p_price);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ScratchandPlay_Points, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status") == true) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MatchAndGetActivity.this, GameSelection.class);
                    startActivity(intent);
                    alertDialog.dismiss();
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
        alertDialogamt = new Dialog(MatchAndGetActivity.this);
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
                getScratchProductDetails("4",SelectedNumber);
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
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
                holder.tvNum1.setTextColor(ContextCompat.getColor(MatchAndGetActivity.this, R.color.white));
            } else {
                holder.linear1Selected.setBackgroundResource(R.drawable.back_blue_white);
                holder.tvNum1.setTextColor(ContextCompat.getColor(MatchAndGetActivity.this, R.color.black));
            }




//            holder.linear1Selected.setOnClickListener(view -> {
//                selectedPosition1 = holder.getAdapterPosition();
//                notifyDataSetChanged();
//            });
//            if (selectedPosition1 == position) {
//                holder.linear1Selected.setBackgroundResource(R.drawable.back_selected_red);
//                holder.tvNum1.setTextColor(ContextCompat.getColor(MatchAndGetActivity.this, R.color.white));
//            } else {
//                holder.linear1Selected.setBackgroundResource(R.drawable.back_blue_white);
//                holder.tvNum1.setTextColor(ContextCompat.getColor(MatchAndGetActivity.this, R.color.black));
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
    private void getCatBannerList(String id) {
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
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
                    pager.setAdapter(new AdapterForBanner(MatchAndGetActivity.this, bannerData));
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
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
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
            Glide.with(MatchAndGetActivity.this)
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
            Glide.with(MatchAndGetActivity.this)
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



/*
package com.akp.aonestar.PlayBuyModule.PlayBuyActivities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import com.airbnb.lottie.LottieAnimationView;
import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Basic.Oder_Summery_Details;
import com.akp.aonestar.GoodLuckModule.AnimationHelper;
import com.akp.aonestar.PlayBuyModule.NetworkManager.DialogManager;
import com.akp.aonestar.PlayBuyModule.NetworkManager.InternetConnection;
import com.akp.aonestar.PlayBuyModule.NetworkManager.MyVolleySingleton;
import com.akp.aonestar.PlayBuyModule.NetworkManager.WebServices;
import com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors.AdaptorImageList;
import com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors.AdaptorImageSlider;
import com.akp.aonestar.PlayBuyModule.MatchAndGetActivity;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Banner.BannerModel;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Category.DataResponseCategory;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Category.ResponseItem;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchAndGetNum.DataResponseMatchOpenNum;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchGameList.DataResponseGetMatchNum;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchHidenum.DataResponseGetMatchHideNum;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ModelImg;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ModelNumber;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt.DataResponseWinAmount;
import com.akp.aonestar.PlayBuyModule.PlayBuy_Oder_Summery_Details;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


public class MatchAndGetActivity extends AppCompatActivity implements View.OnClickListener {


    AdaptorImageList adaptorImageList;
    RecyclerView recyclerImageList, recyclerDummyNumList, recyclerPintchImage, recyclerBanner,
            recyclerCategoryList, recyclerMatchNumList, recyclerMatchNumValueList;
    List<ModelImg> list = new ArrayList<>();
    //AdaptorNumberList adaptorNumberList;
    List<ModelNumber> numbers = new ArrayList<>();
    AdaptorPinchImageList adaptorPinchImageList;
    LinearLayoutCompat linearPlay;

    LinearLayout linearScratch, linearThreeOne, linearForTheWin, linearTossBoss;
    private int selectedPosition = -1;

    String cateName, categoryId;
    MatchAndGetActivity.AdaptorMainCategoryList adaptorMainCategoryList;
    AdaptorImageSlider adaptorImageSlider;

    NestedScrollView nestedLayout;
    AdaptorMatchGetOpenNumList adaptorMatchGetOpenNumList;
    TextView tvNum1, tvNum2, tvNum3, tvNum4;
    int pickCounter = 0, matchCount = 0, matchNum = 0;
    AdaptorMatchGetNum adaptorMatchGetNum;
    AdaptorMatchGetNumValues matchGetNumValues;

    List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt.ResponseItem> itemList = new ArrayList<>();

    Dialog alertDialog, alertDialog1,alertDialog2;
    String selectedBitAmt = "", selectedBitAmtValue = "";

    AdaptorPopupSelectAmt adaptorPopupSelectAmt;
    ProgressDialog progressDialog;
    boolean playGreen = false;
    FloatingActionButton close;
    private String UserId;
    private AlertDialog alertDialog_loos;
    private AlertDialog alertDialog_win;

    RecyclerView tranding_rec,wallet_histroy;
    public static AppCompatButton previousSeletedButton = null;
    private ImageView txt_nodata;
    private Dialog dialog;
    int i=0;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
//    private DashboardAdapter customerListAdapter;

    ImageView norecord_tv,norecord_tv1,norecord_tv2,norecord_tv3,norecord_tv4,norecord_tv5,norecord_tv6;
    ArrayList<HashMap<String, String>> arrayList5 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList6 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList7 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList8 = new ArrayList<>();

    ArrayList<HashMap<String, String>> arrayList9 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList10 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList11 = new ArrayList<>();

    RecyclerView recyclerProductList, recyclerDisProductList1, recyclerDisProductList2,
            recyclerDisProductList3,recyclerDisProductList4, recyclerDisProductList5,
            recyclerDisProductList6;
    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_and_get);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");

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

        LinearLayoutCompat FormetchandGet=findViewById(R.id.gamedetails_ll);
        FormetchandGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductDetails();

//                FormetchandGet_popup();
            }
        });

        initviews();
    }

    private void initviews() {

        */
/*linearScratch = findViewById(R.id.linearScratch);
        linearThreeOne = findViewById(R.id.linearThreeOne);
        linearForTheWin = findViewById(R.id.linearForTheWin);
        linearTossBoss = findViewById(R.id.linearTossBoss);*//*


        progressDialog = new ProgressDialog(this);

        nestedLayout = findViewById(R.id.nestedLayout);
        recyclerImageList = findViewById(R.id.recyclerImageList);
        recyclerBanner = findViewById(R.id.recyclerBanner);
        recyclerCategoryList = findViewById(R.id.recyclerCategoryList);
        recyclerDummyNumList = findViewById(R.id.recyclerDummyNumList);
        recyclerMatchNumList = findViewById(R.id.recyclerMatchNumList);
        recyclerMatchNumValueList = findViewById(R.id.recyclerMatchNumValueList);
        tvNum1 = findViewById(R.id.tvNum1);
        tvNum2 = findViewById(R.id.tvNum2);
        tvNum3 = findViewById(R.id.tvNum3);
        tvNum4 = findViewById(R.id.tvNum4);
        recyclerPintchImage = findViewById(R.id.recyclerPintchImage);
        close = findViewById(R.id.close);
        linearPlay = findViewById(R.id.linearPlay);

        linearPlay.setOnClickListener(this);
        close.setOnClickListener(this);

        recyclerImageList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerImageList.setHasFixedSize(true);
        recyclerDummyNumList.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerDummyNumList.setHasFixedSize(true);
        recyclerPintchImage.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerPintchImage.setHasFixedSize(true);
        recyclerBanner.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerBanner.setHasFixedSize(true);
        recyclerCategoryList.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategoryList.setHasFixedSize(true);
        recyclerMatchNumList.setLayoutManager(new LinearLayoutManager(this));
        recyclerMatchNumList.setHasFixedSize(true);
        recyclerMatchNumValueList.setLayoutManager(new LinearLayoutManager(this));
        recyclerMatchNumValueList.setHasFixedSize(true);

        categoryId = getIntent().getStringExtra("catId");
        cateName = getIntent().getStringExtra("catName");

        */
/*linearScratch.setOnClickListener(this);
        linearThreeOne.setOnClickListener(this);
        linearForTheWin.setOnClickListener(this);
        linearTossBoss.setOnClickListener(this);*//*


        list.add(new ModelImg(R.drawable.img1));
        list.add(new ModelImg(R.drawable.img2));
        list.add(new ModelImg(R.drawable.img3));
        list.add(new ModelImg(R.drawable.img4));

        adaptorImageList = new AdaptorImageList(getApplicationContext(), list);
        recyclerImageList.setAdapter(adaptorImageList);

        if (InternetConnection.checkConnection(getApplicationContext())) {
            getMainCatList();
            getCatBannerList(categoryId);
            getMatchOpenNumList();
            getMatchNumber();
        } else {
            DialogManager.openCheckInternetDialog(MatchAndGetActivity.this);
        }

        nestedLayout.fullScroll(View.FOCUS_UP);
        nestedLayout.smoothScrollTo(0, 0);
        nestedLayout.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    //Log.i("scroll", "Scroll DOWN....");
                }
                if (scrollY < oldScrollY) {
                    //you can write here
                    //Log.i("scroll", "Scroll UP.....");
                    recyclerBanner.setVisibility(View.GONE);
                    recyclerImageList.setVisibility(View.VISIBLE);
                }

                if (scrollY == 0) {
                    //Log.i("scroll", "TOP SCROLL.....");
                }

                if (scrollY == (v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight())) {
                    //Log.i("scroll", "BOTTOM SCROLL...");
                    */
/*linearParenId.setVisibility(View.GONE);
                    linearBottomLay.setVisibility(View.GONE);*//*

                }
            }
        });

    }

    private void getMatchNumber() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.PLAYBUY_MatchGetmatchList, null, response -> {
            Gson gson = new Gson();
            try {
                DataResponseGetMatchNum getMatchNum = gson.fromJson(response.toString(), DataResponseGetMatchNum.class);
                if (getMatchNum.isStatus()) {
                    adaptorMatchGetNum = new AdaptorMatchGetNum(getApplicationContext(), getMatchNum.getResponse());
                    recyclerMatchNumList.setAdapter(adaptorMatchGetNum);

                    matchGetNumValues = new AdaptorMatchGetNumValues(getApplicationContext(), getMatchNum.getResponse());
                    recyclerMatchNumValueList.setAdapter(matchGetNumValues);



                    final int speedScroll = 1500;
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

                                recyclerMatchNumList.smoothScrollToPosition(count);
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
                            if(count1 < matchGetNumValues.getItemCount()){
                                if(count1==matchGetNumValues.getItemCount()-1){
                                    flag1 = false;
                                }else if(count1 == 0){
                                    flag1 = true;
                                }
                                if(flag1) count1++;
                                else count1--;

                                recyclerMatchNumValueList.smoothScrollToPosition(count1);
                                handler1.postDelayed(this,speedScroll1);
                            }
                        }
                    };

                    handler1.postDelayed(runnable1,speedScroll1);










                    getWinPrizeList();
                } else {
                    Toast.makeText(MatchAndGetActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void getWinPrizeList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.PLAYBUY_ForTheWinAmountList, null, response -> {
            Gson gson = new Gson();
            try {
                DataResponseWinAmount dataResponseNumbers = gson.fromJson(response.toString(), DataResponseWinAmount.class);
                if (dataResponseNumbers.isStatus()) {
                    itemList.clear();
                    itemList = dataResponseNumbers.getResponse();
                } else {
                    Toast.makeText(MatchAndGetActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void getMatchOpenNumList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.PLAYBUY_MacthGetNumberList_Open, null, response -> {
            Gson gson = new Gson();
            try {
                DataResponseMatchOpenNum matchOpenNum = gson.fromJson(response.toString(), DataResponseMatchOpenNum.class);
                if (matchOpenNum.isStatus()) {
                    adaptorMatchGetOpenNumList = new AdaptorMatchGetOpenNumList(getApplicationContext(), matchOpenNum.getResponse());
                    recyclerDummyNumList.setAdapter(adaptorMatchGetOpenNumList);
                } else {
                    Toast.makeText(MatchAndGetActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void getCatBannerList(String id) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CategoryId", id);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.BannerList, jsonObject, response -> {
            Gson gson = new Gson();
            try {
                BannerModel bannerModel = gson.fromJson(response.toString(), BannerModel.class);
                if (bannerModel.isStatus()) {
                    adaptorImageSlider = new AdaptorImageSlider(getApplicationContext(), bannerModel.getResponse());
                    recyclerBanner.setAdapter(adaptorImageSlider);
                } else {
                    Toast.makeText(MatchAndGetActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
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
                    adaptorMainCategoryList = new MatchAndGetActivity.AdaptorMainCategoryList(getApplicationContext(), dataResponseCategory.getResponse());
                    recyclerCategoryList.setAdapter(adaptorMainCategoryList);
                } else {
                    Toast.makeText(MatchAndGetActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
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
            case R.id.linearPlay:
                if (playGreen) {
                    if (matchCount == 4) {
                        Log.e("TAG", "matchNum: " + matchNum);
                        //Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
                        purchaseGame();
                    } else {
                        if (matchCount > 4) {
                            Toast.makeText(this, "Over", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Please matched all digit", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (tvNum1.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please pick any number", Toast.LENGTH_SHORT).show();
                    }
                    else if (tvNum2.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please pick number", Toast.LENGTH_SHORT).show();
                    }
                    else  if (tvNum3.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please pick number", Toast.LENGTH_SHORT).show();
                    }
                    else  if (tvNum4.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please pick  number", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        openPlayDialog();
                    }
                }
                break;

            case R.id.close:
                GameDetails_popup(categoryId);
                break;

        }
    }

    private void GameDetails_popup(String getId) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(MatchAndGetActivity.this).inflate(R.layout.popup_gameoverview, viewGroup, false);

        TextView view_details_tv = dialogView.findViewById(R.id.view_details_tv);


        FloatingActionButton close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
//        arrayList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
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
                    Toast.makeText(MatchAndGetActivity.this, "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        }, error -> {
            progressDialog.dismiss();
            Log.d("myTag", "message:" + error);
            Toast.makeText(MatchAndGetActivity.this, "No Record Found!", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MatchAndGetActivity.this);
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
    private void purchaseGame() {
        progressDialog.show();
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", UserId);
            jsonObject.put("MatchCount", matchNum);
            jsonObject.put("Amount", selectedBitAmt);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Log.e("TAG", "matchRequest: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_MacthGet_WinLose, jsonObject, response -> {
            try {
                Log.e("TAG", "getMatchANdGet: " + response);
                if (response.getBoolean("Status")) {
                    progressDialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    final MediaPlayer mp = MediaPlayer.create(MatchAndGetActivity.this, R.raw.blast_one);
                    mp.start();
                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(MatchAndGetActivity.this).inflate(R.layout.dynamic_winpopup, viewGroup, false);
                    Button rlBack = (AppCompatButton) dialogView.findViewById(R.id.btncancel);
                    TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);
                    Button buy_btn =(AppCompatButton)dialogView.findViewById(R.id.buy_btn);
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
                            intent.putExtra("class_name","matchget");
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
//                    buy_btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mp.stop();
//                            finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());
//                            overridePendingTransition(0, 0);
//                            alertDialog_win.dismiss();
//                        }
//                    });
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(MatchAndGetActivity.this);
                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView);
                    //finally creating the alert dialog and displaying it
                    alertDialog_win = builder.create();
                    alertDialog_win.show();



                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + response.getString("Message"), Toast.LENGTH_SHORT).show();

                    Toast.makeText(MatchAndGetActivity.this, "Process Completed\nOops! Number Didn't matched😭😭", Toast.LENGTH_SHORT).show();
                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(MatchAndGetActivity.this).inflate(R.layout.dynamic_loosepopup, viewGroup, false);
                    Button rlBack = (AppCompatButton) dialogView.findViewById(R.id.btncancel);
                    TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);
                    txt_msg.setText("Oops! Better Luck Next Time");
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(MatchAndGetActivity.this);
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

    private void openPlayDialog() {
        alertDialog = new Dialog(MatchAndGetActivity.this);
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

        adaptorPopupSelectAmt = new MatchAndGetActivity.AdaptorPopupSelectAmt(getApplicationContext(), itemList);
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
                    Toast.makeText(MatchAndGetActivity.this, "Please select amount!", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MatchAndGetActivity.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
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
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    builder.create().show();
                }
            }
        });

    }

    private void getPlayAmtData(String selectedBitAmt) {
        progressDialog.show();
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CategoryId", categoryId);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Amount", selectedBitAmt);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Log.e("TAG", "getPlayAmtData: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_MacthGetPlayNow, jsonObject, response -> {
            try {
                Log.e("TAG", "getMatchANdGet: " + response);
                if (response.getBoolean("Status")) {
                    progressDialog.dismiss();
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    getHiddenNumList();
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

    private void getHiddenNumList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.PLAYBUY_MacthGetNumberList_Close, null, response -> {
            Gson gson = new Gson();
            try {
                DataResponseGetMatchHideNum getMatchHideNum = gson.fromJson(response.toString(), DataResponseGetMatchHideNum.class);
                if (getMatchHideNum.isStatus()) {
                    playGreen = true;
                    linearPlay.setBackgroundResource(R.color.green_color);
                    recyclerDummyNumList.setVisibility(View.GONE);
                    recyclerPintchImage.setVisibility(View.VISIBLE);
                    adaptorPinchImageList = new AdaptorPinchImageList(getApplicationContext(), getMatchHideNum.getResponse());
                    recyclerPintchImage.setAdapter(adaptorPinchImageList);
                } else {
                    Toast.makeText(MatchAndGetActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public class AdaptorMainCategoryList extends RecyclerView.Adapter<MatchAndGetActivity.AdaptorMainCategoryList.MyCategoryHolder> {

        Context context;
        List<ResponseItem> catList;

        public AdaptorMainCategoryList(Context context, List<ResponseItem> catList) {
            this.context = context;
            this.catList = catList;
        }

        @NonNull
        @Override
        public MatchAndGetActivity.AdaptorMainCategoryList.MyCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MatchAndGetActivity.AdaptorMainCategoryList.MyCategoryHolder(LayoutInflater.from(context).inflate(R.layout.main_category_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MatchAndGetActivity.AdaptorMainCategoryList.MyCategoryHolder holder, @SuppressLint("RecyclerView") int position) {
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
                        case "SCRATCH & GET":
                            startActivity(new Intent(getApplicationContext(), MatchAndGetActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName())
                            );
                            finish();
                            break;

                        case "TOSS BOSS":
                            startActivity(new Intent(getApplicationContext(), TossBossActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName()));
                            finish();
                            break;

                        case "FOR THE WIN":
                            startActivity(new Intent(getApplicationContext(), ForTheWinActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName())
                            );
                            finish();
                            break;

                        case "TRIPLE MATCH":
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

    public class AdaptorMatchGetOpenNumList extends RecyclerView.Adapter<AdaptorMatchGetOpenNumList.MyOpenNumHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchAndGetNum.ResponseItem> datas;

        public AdaptorMatchGetOpenNumList(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchAndGetNum.ResponseItem> datas) {
            this.context = context;
            this.datas = datas;
        }

        @NonNull
        @Override
        public MyOpenNumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyOpenNumHolder(LayoutInflater.from(context).inflate(R.layout.match_get_num_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyOpenNumHolder holder, int position) {
            holder.tvMNum.setText(String.valueOf(datas.get(position).getNumber()));

            holder.lineMBluSel.setOnClickListener(view -> {
                pickCounter++;
                if (pickCounter <= 4) {
                    holder.lineMBluSel.setBackgroundResource(R.drawable.back_selected_red);
                    if (pickCounter == 1) {
                        tvNum1.setText(String.valueOf(datas.get(position).getNumber()));
                    } else if (pickCounter == 2) {
                        tvNum2.setText(String.valueOf(datas.get(position).getNumber()));
                    } else if (pickCounter == 3) {
                        tvNum3.setText(String.valueOf(datas.get(position).getNumber()));
                    } else if (pickCounter == 4) {
                        tvNum4.setText(String.valueOf(datas.get(position).getNumber()));
                    }
                } else {
                    Toast.makeText(MatchAndGetActivity.this, "Only chosen four number", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public class MyOpenNumHolder extends RecyclerView.ViewHolder {
            TextView tvMNum;
            LinearLayout lineMBluSel;

            public MyOpenNumHolder(@NonNull View itemView) {
                super(itemView);
                lineMBluSel = itemView.findViewById(R.id.lineMBluSel);
                tvMNum = itemView.findViewById(R.id.tvMNum);
            }
        }
    }

    public class AdaptorMatchGetNum extends RecyclerView.Adapter<AdaptorMatchGetNum.MyMatchHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchGameList.ResponseItem> matchList;

        public AdaptorMatchGetNum(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchGameList.ResponseItem> matchList) {
            this.context = context;
            this.matchList = matchList;
        }

        @NonNull
        @Override
        public MyMatchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyMatchHolder(LayoutInflater.from(context).inflate(R.layout.match_num_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyMatchHolder holder, int position) {
            holder.tvMNum.setText(String.valueOf(matchList.get(position).getBitAmount()));
        }

        @Override
        public int getItemCount() {
            return matchList.size();
        }

        public class MyMatchHolder extends RecyclerView.ViewHolder {
            TextView tvMNum;

            public MyMatchHolder(@NonNull View itemView) {
                super(itemView);
                tvMNum = itemView.findViewById(R.id.tvMNum);
            }
        }
    }

    public class AdaptorMatchGetNumValues extends RecyclerView.Adapter<AdaptorMatchGetNumValues.MyMatchValueHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchGameList.ResponseItem> matchList;

        public AdaptorMatchGetNumValues(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchGameList.ResponseItem> matchList) {
            this.context = context;
            this.matchList = matchList;
        }

        @NonNull
        @Override
        public MyMatchValueHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyMatchValueHolder(LayoutInflater.from(context).inflate(R.layout.match_num_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyMatchValueHolder holder, int position) {
            holder.tvMNum.setText(String.valueOf(matchList.get(position).getAmount()));
        }

        @Override
        public int getItemCount() {
            return matchList.size();
        }

        public class MyMatchValueHolder extends RecyclerView.ViewHolder {
            TextView tvMNum;

            public MyMatchValueHolder(@NonNull View itemView) {
                super(itemView);
                tvMNum = itemView.findViewById(R.id.tvMNum);
            }
        }
    }

    public class AdaptorPopupSelectAmt extends RecyclerView.Adapter<MatchAndGetActivity.AdaptorPopupSelectAmt.MyForWinAmt1Holder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt.ResponseItem> list1;

        public AdaptorPopupSelectAmt(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.WinAmt.ResponseItem> list) {
            this.context = context;
            this.list1 = list;
        }

        @NonNull
        @Override
        public MatchAndGetActivity.AdaptorPopupSelectAmt.MyForWinAmt1Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MatchAndGetActivity.AdaptorPopupSelectAmt.MyForWinAmt1Holder(LayoutInflater.from(context).inflate(R.layout.layout_choose_amt, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MatchAndGetActivity.AdaptorPopupSelectAmt.MyForWinAmt1Holder holder, int position) {
            holder.tvNum1.setText(String.valueOf(list1.get(position).getBitAmount()));

            holder.linear1Selected.setOnClickListener(view -> {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
                selectedBitAmt = String.valueOf(list1.get(position).getBitAmount());
                selectedBitAmtValue = String.valueOf(list1.get(position).getAmount());
            });

            if (selectedPosition == position) {
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

    public class AdaptorPinchImageList extends RecyclerView.Adapter<AdaptorPinchImageList.MyPinchHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchHidenum.ResponseItem> hideList;

        public AdaptorPinchImageList(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.MatchHidenum.ResponseItem> hideList) {
            this.context = context;
            this.hideList = hideList;
        }

        @NonNull
        @Override
        public MyPinchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyPinchHolder(LayoutInflater.from(context).inflate(R.layout.layout_pinch_image, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyPinchHolder holder, int position) {
            holder.tvScrNum.setText(String.valueOf(hideList.get(position).getNumber()));
            final MediaPlayer mp = MediaPlayer.create(MatchAndGetActivity.this, R.raw.blast);
            holder.rel_pinch.setOnClickListener(v -> {
                mp.start();
                matchCount++;
                if (matchCount <= 4) {
                    holder.imagePinImg.setVisibility(View.GONE);
                    holder.tvScrNum.setVisibility(View.VISIBLE);
                    if (matchCount == 1) {
                        if (String.valueOf(hideList.get(position).getNumber()).equals(tvNum1.getText().toString().toString())
                                || String.valueOf(hideList.get(position).getNumber()).equals(tvNum2.getText().toString().toString()) ||
                                String.valueOf(hideList.get(position).getNumber()).equals(tvNum3.getText().toString().toString()) ||
                                String.valueOf(hideList.get(position).getNumber()).equals(tvNum4.getText().toString().toString())) {
                            Toast.makeText(MatchAndGetActivity.this, "Matched", Toast.LENGTH_SHORT).show();
                            matchNum++;
                        }
                    } else if (matchCount == 2) {
                        if (String.valueOf(hideList.get(position).getNumber()).equals(tvNum1.getText().toString().toString())
                                || String.valueOf(hideList.get(position).getNumber()).equals(tvNum2.getText().toString().toString()) ||
                                String.valueOf(hideList.get(position).getNumber()).equals(tvNum3.getText().toString().toString()) ||
                                String.valueOf(hideList.get(position).getNumber()).equals(tvNum4.getText().toString().toString())) {
                            Toast.makeText(MatchAndGetActivity.this, "Matched", Toast.LENGTH_SHORT).show();
                            matchNum++;
                        }
                    } else if (matchCount == 3) {
                        if (String.valueOf(hideList.get(position).getNumber()).equals(tvNum1.getText().toString().toString())
                                || String.valueOf(hideList.get(position).getNumber()).equals(tvNum2.getText().toString().toString()) ||
                                String.valueOf(hideList.get(position).getNumber()).equals(tvNum3.getText().toString().toString()) ||
                                String.valueOf(hideList.get(position).getNumber()).equals(tvNum4.getText().toString().toString())) {
                            Toast.makeText(MatchAndGetActivity.this, "Matched", Toast.LENGTH_SHORT).show();
                            matchNum++;
                        }
                    } else if (matchCount == 4) {
                        if (String.valueOf(hideList.get(position).getNumber()).equals(tvNum1.getText().toString().toString())
                                || String.valueOf(hideList.get(position).getNumber()).equals(tvNum2.getText().toString().toString()) ||
                                String.valueOf(hideList.get(position).getNumber()).equals(tvNum3.getText().toString().toString()) ||
                                String.valueOf(hideList.get(position).getNumber()).equals(tvNum4.getText().toString().toString())) {
                            Toast.makeText(MatchAndGetActivity.this, "Matched", Toast.LENGTH_SHORT).show();
                            animationView.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    animationView.setVisibility(View.GONE);
                                    purchaseGame();
                                }
                            }, 3000);
                            matchNum++;
                        }
                        else {
                            animationView.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    animationView.setVisibility(View.GONE);
                                    purchaseGame();
                                }
                            }, 3000);
                        }
                    }
                } else {
                    matchCount = 4;
                }
            });
        }

        @Override
        public int getItemCount() {
            return hideList == null ? 0 : hideList.size();
        }

        public class MyPinchHolder extends RecyclerView.ViewHolder {

            ImageView imagePinImg;
            TextView tvScrNum;
            RelativeLayout rel_pinch;

            public MyPinchHolder(@NonNull View itemView) {
                super(itemView);
                rel_pinch = itemView.findViewById(R.id.rel_pinch);
                imagePinImg = itemView.findViewById(R.id.imagePinImg);
                tvScrNum = itemView.findViewById(R.id.tvScrNum);
            }
        }
    }
    
    















    private void showProductDetails() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(MatchAndGetActivity.this).inflate(R.layout.match_and_get_product_details, viewGroup, false);


        norecord_tv = dialogView.findViewById(R.id.norecord_tv);
        norecord_tv1 = dialogView.findViewById(R.id.norecord_tv1);
        norecord_tv2 = dialogView.findViewById(R.id.norecord_tv2);
        norecord_tv3 = dialogView.findViewById(R.id.norecord_tv3);



        norecord_tv4 = dialogView.findViewById(R.id.norecord_tv4);
        norecord_tv5 = dialogView.findViewById(R.id.norecord_tv5);
        norecord_tv6 = dialogView.findViewById(R.id.norecord_tv6);


        recyclerProductList = dialogView.findViewById(R.id.recyclerProductList);
        recyclerDisProductList1 = dialogView.findViewById(R.id.recyclerDisProductList1);
        recyclerDisProductList2 = dialogView.findViewById(R.id.recyclerDisProductList2);
        recyclerDisProductList3 = dialogView.findViewById(R.id.recyclerDisProductList3);

        recyclerDisProductList4 = dialogView.findViewById(R.id.recyclerDisProductList4);
        recyclerDisProductList5 = dialogView.findViewById(R.id.recyclerDisProductList5);
        recyclerDisProductList6 = dialogView.findViewById(R.id.recyclerDisProductList6);



        LinearLayout close = dialogView.findViewById(R.id.linearClose);


        getHistory("12");
        getHistory1("13");
        getHistory2("11");

        getHistory3("14");


        getHistory4("18");
        getHistory5("19");
        getHistory6("20");


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MatchAndGetActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUY_categoryGameProduct", new Response.Listener<String>() {
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
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MatchAndGetActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        MatchAndGetActivity.TaskEarningAdapter customerListAdapter = new MatchAndGetActivity.TaskEarningAdapter(MatchAndGetActivity.this, arrayList5);
                        recyclerProductList.setLayoutManager(gridLayoutManager);
                        recyclerProductList.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter extends RecyclerView.Adapter<MatchAndGetActivity.TaskEarningAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public MatchAndGetActivity.TaskEarningAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            MatchAndGetActivity.TaskEarningAdapter.VH viewHolder = new MatchAndGetActivity.TaskEarningAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MatchAndGetActivity.TaskEarningAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(MatchAndGetActivity.this)
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
        dialog = new Dialog(MatchAndGetActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
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
                        Glide.with(MatchAndGetActivity.this)
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
                        Toast.makeText(MatchAndGetActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails





    public void getHistory1(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUY_categoryGameProduct", new Response.Listener<String>() {
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
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MatchAndGetActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        MatchAndGetActivity.TaskEarningAdapter1 customerListAdapter = new MatchAndGetActivity.TaskEarningAdapter1(MatchAndGetActivity.this, arrayList6);
                        recyclerDisProductList1.setLayoutManager(gridLayoutManager);
                        recyclerDisProductList1.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv1.setVisibility(View.VISIBLE);
                        
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter1 extends RecyclerView.Adapter<MatchAndGetActivity.TaskEarningAdapter1.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter1(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public MatchAndGetActivity.TaskEarningAdapter1.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            MatchAndGetActivity.TaskEarningAdapter1.VH viewHolder = new MatchAndGetActivity.TaskEarningAdapter1.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MatchAndGetActivity.TaskEarningAdapter1.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(MatchAndGetActivity.this)
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
        dialog = new Dialog(MatchAndGetActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
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
                        Glide.with(MatchAndGetActivity.this)
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
                        Toast.makeText(MatchAndGetActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails











    public void getHistory2(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUY_categoryGameProduct", new Response.Listener<String>() {
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
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MatchAndGetActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        MatchAndGetActivity.TaskEarningAdapter2 customerListAdapter = new MatchAndGetActivity.TaskEarningAdapter2(MatchAndGetActivity.this, arrayList7);
                        recyclerDisProductList2.setLayoutManager(gridLayoutManager);
                        recyclerDisProductList2.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv2.setVisibility(View.VISIBLE);
                        
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter2 extends RecyclerView.Adapter<MatchAndGetActivity.TaskEarningAdapter2.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter2(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public MatchAndGetActivity.TaskEarningAdapter2.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            MatchAndGetActivity.TaskEarningAdapter2.VH viewHolder = new MatchAndGetActivity.TaskEarningAdapter2.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MatchAndGetActivity.TaskEarningAdapter2.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(MatchAndGetActivity.this)
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
        dialog = new Dialog(MatchAndGetActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
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
                        Glide.with(MatchAndGetActivity.this)
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
                        Toast.makeText(MatchAndGetActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails










    public void getHistory3(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUY_categoryGameProduct", new Response.Listener<String>() {
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
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MatchAndGetActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        MatchAndGetActivity.TaskEarningAdapter3 customerListAdapter = new MatchAndGetActivity.TaskEarningAdapter3(MatchAndGetActivity.this, arrayList8);
                        recyclerDisProductList3.setLayoutManager(gridLayoutManager);
                        recyclerDisProductList3.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv3.setVisibility(View.VISIBLE);
                        
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter3 extends RecyclerView.Adapter<MatchAndGetActivity.TaskEarningAdapter3.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter3(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public MatchAndGetActivity.TaskEarningAdapter3.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            MatchAndGetActivity.TaskEarningAdapter3.VH viewHolder = new MatchAndGetActivity.TaskEarningAdapter3.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MatchAndGetActivity.TaskEarningAdapter3.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(MatchAndGetActivity.this)
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
        dialog = new Dialog(MatchAndGetActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
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
                        Glide.with(MatchAndGetActivity.this)
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
                        Toast.makeText(MatchAndGetActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails


















    public void getHistory4(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUY_categoryGameProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);

                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv4.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("ProductMRP", jsonObject.getString("ProductMRP"));
                            hashlist.put("ProductSaleRate", jsonObject.getString("ProductSaleRate"));
                            arrayList9.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MatchAndGetActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        MatchAndGetActivity.TaskEarningAdapter4 customerListAdapter = new MatchAndGetActivity.TaskEarningAdapter4(MatchAndGetActivity.this, arrayList9);
                        recyclerDisProductList4.setLayoutManager(gridLayoutManager);
                        recyclerDisProductList4.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv4.setVisibility(View.VISIBLE);
                        
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter4 extends RecyclerView.Adapter<MatchAndGetActivity.TaskEarningAdapter4.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter4(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public MatchAndGetActivity.TaskEarningAdapter4.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            MatchAndGetActivity.TaskEarningAdapter4.VH viewHolder = new MatchAndGetActivity.TaskEarningAdapter4.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MatchAndGetActivity.TaskEarningAdapter4.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(MatchAndGetActivity.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup4(arrayList.get(i).get("ProductCode"));
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
    private void openOfferpopup4(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(MatchAndGetActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
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
                        Glide.with(MatchAndGetActivity.this)
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
                        Toast.makeText(MatchAndGetActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails








    public void getHistory5(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUY_categoryGameProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);

                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv5.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("ProductMRP", jsonObject.getString("ProductMRP"));
                            hashlist.put("ProductSaleRate", jsonObject.getString("ProductSaleRate"));
                            arrayList10.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MatchAndGetActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        MatchAndGetActivity.TaskEarningAdapter5 customerListAdapter = new MatchAndGetActivity.TaskEarningAdapter5(MatchAndGetActivity.this, arrayList10);
                        recyclerDisProductList5.setLayoutManager(gridLayoutManager);
                        recyclerDisProductList5.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv5.setVisibility(View.VISIBLE);
                        
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter5 extends RecyclerView.Adapter<MatchAndGetActivity.TaskEarningAdapter5.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter5(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public MatchAndGetActivity.TaskEarningAdapter5.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            MatchAndGetActivity.TaskEarningAdapter5.VH viewHolder = new MatchAndGetActivity.TaskEarningAdapter5.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MatchAndGetActivity.TaskEarningAdapter5.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(MatchAndGetActivity.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup5(arrayList.get(i).get("ProductCode"));
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
    private void openOfferpopup5(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(MatchAndGetActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
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
                        Glide.with(MatchAndGetActivity.this)
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
                        Toast.makeText(MatchAndGetActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails







    public void getHistory6(String str_cat) {
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUY_categoryGameProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);

                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv6.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("ProductMRP", jsonObject.getString("ProductMRP"));
                            hashlist.put("ProductSaleRate", jsonObject.getString("ProductSaleRate"));
                            arrayList11.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MatchAndGetActivity .this, 1,GridLayoutManager.HORIZONTAL, false);
                        MatchAndGetActivity.TaskEarningAdapter6 customerListAdapter = new MatchAndGetActivity.TaskEarningAdapter6(MatchAndGetActivity.this, arrayList11);
                        recyclerDisProductList6.setLayoutManager(gridLayoutManager);
                        recyclerDisProductList6.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv6.setVisibility(View.VISIBLE);
                        
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter6 extends RecyclerView.Adapter<MatchAndGetActivity.TaskEarningAdapter6.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter6(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public MatchAndGetActivity.TaskEarningAdapter6.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_threeinone, viewGroup, false);
            MatchAndGetActivity.TaskEarningAdapter6.VH viewHolder = new MatchAndGetActivity.TaskEarningAdapter6.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MatchAndGetActivity.TaskEarningAdapter6.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(MatchAndGetActivity.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup6(arrayList.get(i).get("ProductCode"));
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
    private void openOfferpopup6(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(MatchAndGetActivity.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(MatchAndGetActivity.this);
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
                        Glide.with(MatchAndGetActivity.this)
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
                        Toast.makeText(MatchAndGetActivity.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MatchAndGetActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MatchAndGetActivity.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails














}
    
    
    
    
    
    
    
    
    
*/
