package com.akp.aonestar.PlayBuyModule;

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
import com.akp.aonestar.PlayBuyModule.PlayBuyActivities.ForTheWinActivity;
import com.akp.aonestar.PlayBuyModule.PlayBuyActivities.MatchAndGetActivity;
import com.akp.aonestar.PlayBuyModule.PlayBuyActivities.ThreeInOneActivity;
import com.akp.aonestar.PlayBuyModule.PlayBuyActivities.TossBossActivity;
import com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors.AdaptorImageList;
import com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors.AdaptorImageSlider;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Banner.BannerModel;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Category.DataResponseCategory;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Category.ResponseItem;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ModelImg;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.DataResponseProduct;
import com.akp.aonestar.PlayBuyModule.ScratchCardManager.ScratchCard;
import com.akp.aonestar.PriceDropModule.PriceDropDashboard;
import com.akp.aonestar.PriceDropModule.PriceDrop_Oder_Summery_Details;
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

public class PlayBuyDashboard extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerCategoryList, recyclerScratchProductList, recycScratchList;
//    List<ModelImg> list = new ArrayList<>();

    AdaptorMainCategoryList adaptorMainCategoryList;
    AdaptorImageSlider adaptorImageSlider;

    LinearLayout linearScratchLay, linearPlay, linearScratch;
    FloatingActionButton close;
    AdaptorCateWiseProductList adaptorCateWiseProductList;
    AdaptorCateWiseProductList1 adaptorCateWiseProductList1;
    AdaptorScratchProductList adaptorScratchProductList;

    Dialog alertDialog, alertDialog1, alertDialogamt;
    String productImage, productName, productPrice, productCode, productDisAmt;

    ProgressDialog progressDialog;
    List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> listProduct = new ArrayList<>();
    List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> listProduct1 = new ArrayList<>();
    private String UserId;
    private final int limit = 9;
    private Dialog dialog;
    List<BannerData> bannerData = new ArrayList<>();
    int i = 0;
    private static int currentPage = 0;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private FriendsListAdapter pdfAdapTer;
    RecyclerView recyclerSelectAmt;
    private int selectedPosition1 = -1;
    String SelectedNumber;
    CirclePageIndicator indicator;
    ViewPager pager;

    ArrayList<HashMap<String, String>> arrayListwin = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayListloss = new ArrayList<>();
    RecyclerView recyclerTossMatchAmt, recyclerTossMatchValue;
    AdaptorMatchGetNum adaptorMatchGetNum;
    AdaptorMatchGetNumValues adaptorMatchGetNumValues;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_buy_dashboard);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();
        GifImageView gl_img = findViewById(R.id.gl_img);
        GifImageView pd_img = findViewById(R.id.pd_img);
        GifImageView spot_img = findViewById(R.id.spot_img);
        GifImageView sunday_sp = findViewById(R.id.sunday_sp);
        mp = MediaPlayer.create(PlayBuyDashboard.this, R.raw.akp_click);

        sunday_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SundaySpecial.class);
                startActivity(intent);
            }
        });
        gl_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GoodLuckGameProductView.class);
                startActivity(intent);
            }
        });
        pd_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PriceDropDashboard.class);
                startActivity(intent);
            }
        });
        spot_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Coming Soon!", Toast.LENGTH_LONG).show();
            }
        });
        initViews();
        if (UserId.equalsIgnoreCase("")) {
        } else {
            BlockUnblockAPI();
        }

        GetWinnerList("Win");
        GetLooserList("Loss");
    }

    private void initViews() {
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager1);
        progressDialog = new ProgressDialog(PlayBuyDashboard.this);

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


//        list.add(new ModelImg(R.drawable.img1));
//        list.add(new ModelImg(R.drawable.img2));
//        list.add(new ModelImg(R.drawable.img3));
//        list.add(new ModelImg(R.drawable.img4));
//
//        adaptorImageList = new AdaptorImageList(getApplicationContext(), list);
//        recyclerImageList.setAdapter(adaptorImageList);

        if (InternetConnection.checkConnection(getApplicationContext())) {
            getMainCatList();
            getCatBannerList("1");
            getCatProdList("1");
            getCatProdList1("1");
        } else {
            DialogManager.openCheckInternetDialog(PlayBuyDashboard.this);
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
            Log.d("istlayoutresponse", "" + response);
            Gson gson = new Gson();
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
                    Toast.makeText(PlayBuyDashboard.this, "Record not found", Toast.LENGTH_SHORT).show();
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
            Log.d("resp", String.valueOf(response));
            Gson gson = new Gson();
            try {
                DataResponseProduct productCategoryModel = gson.fromJson(response.toString(), DataResponseProduct.class);
                if (productCategoryModel.isStatus()) {
                    listProduct1.clear();
                    listProduct1 = productCategoryModel.getResponse();
                } else {
                    Toast.makeText(PlayBuyDashboard.this, "Record not found", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PlayBuyDashboard.this, "Record not found", Toast.LENGTH_SHORT).show();
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
                openPlayDialog();
//                openAlertDialog();
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
        View dialogView = LayoutInflater.from(PlayBuyDashboard.this).inflate(R.layout.show_product_popup_details, viewGroup, false);

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
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PlayBuyDashboard.this);
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
        View dialogView = LayoutInflater.from(PlayBuyDashboard.this).inflate(R.layout.popup_gameoverview, viewGroup, false);

        TextView view_details_tv = dialogView.findViewById(R.id.view_details_tv);

        FloatingActionButton close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
//        arrayList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(PlayBuyDashboard.this);
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
                    Toast.makeText(PlayBuyDashboard.this, "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        }, error -> {
            progressDialog.dismiss();
            Log.d("myTag", "message:" + error);
            Toast.makeText(PlayBuyDashboard.this, "No Record Found!", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PlayBuyDashboard.this);
        requestQueue.add(stringRequest);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PlayBuyDashboard.this);
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

    private void openAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayBuyDashboard.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
        builder.setTitle("Alert Confirmation for Amount:- ₹ 10")
                .setMessage("Are you sure you want to Play?")
                .setCancelable(false)
                .setIcon(R.drawable.logo)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getScratchProductDetails("1", "10.0");
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

    private void getScratchProductDetails(String catId, String betamount) {
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ScratchGetPlayNow, jsonObject, response -> {
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
            //Glide.with(PlayBuyDashboard.this).load(catList.get(position).getCatImg()).error(R.drawable.logo).into(holder.catImage);

            holder.tvCatName.setText(catList.get(position).getCategoryName());
            if (catList.get(position).getCategoryName().equalsIgnoreCase("SCRATCH & GET")) {
                holder.linearLayout.setBackgroundResource(R.drawable.back_yellow);
                holder.tvCatName.setTextColor(ContextCompat.getColor(context, R.color.black));
                holder.viewLay.setVisibility(View.GONE);
            }

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (catList.get(position).getCategoryName()) {
                        case "TOSS BOSS":
                            mp.start();
                            startActivity(new Intent(getApplicationContext(), TossBossActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName())
                            );
                            break;

                        case "LUCK & WIN":
                            mp.start();
                            startActivity(new Intent(getApplicationContext(), MatchAndGetActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName())
                            );
                            break;

                        case "FOR THE WIN":
                            mp.start();
                            startActivity(new Intent(getApplicationContext(), ForTheWinActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName())
                            );
                            break;

                        case "TRIPLE MATCH":
                            mp.start();
                            startActivity(new Intent(getApplicationContext(), ThreeInOneActivity.class)
                                    .putExtra("catId", String.valueOf(catList.get(position).getPKID()))
                                    .putExtra("catName", catList.get(position).getCategoryName())
                            );
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

           /* holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup1(String.valueOf(itemList.get(position).getProductCode()));
                }
            });*/

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
                    /*Toast.makeText(PlayBuyDashboard.this, ""+itemList.get(position).getProductName()+
                            "\n"+itemList.get(position).getProductSaleRate(), Toast.LENGTH_SHORT).show();*/
                    openScratchDialogBox(productCode, productImage, productName, "₹ " + productPrice);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (itemList.size() > limit) {
                return limit;
            } else {
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
    private void openScratchDialogBox(String pcode, String productImg, String productName, String price) {
        final MediaPlayer mp = MediaPlayer.create(PlayBuyDashboard.this, R.raw.blast_one);
        mp.start();
        alertDialog = new Dialog(PlayBuyDashboard.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_scratch_layout);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans)));
        alertDialog.setCancelable(false);
        alertDialog.show();
        //alertDialog.getWindow().setLayout((6 * width)/7, ActionBar.LayoutParams.WRAP_CONTENT);


        GifImageView gif_a = alertDialog.findViewById(R.id.gif_a);
        ImageView imgProduct = alertDialog.findViewById(R.id.imgProduct);
        TextView proName = alertDialog.findViewById(R.id.productName);
        LinearLayout linearExit = alertDialog.findViewById(R.id.linearExit);
        LinearLayout linearProceed = alertDialog.findViewById(R.id.linearProceed);
        ScratchCard scratchCard1 = alertDialog.findViewById(R.id.scratchCard1);
        Glide.with(PlayBuyDashboard.this)
                .load(productImg)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imgProduct);
        proName.setText(productName + "\n" + price);

        if (productName.equalsIgnoreCase("Better Luck Next Time")) {
            linearProceed.setVisibility(View.GONE);
            gif_a.setVisibility(View.GONE);
            proName.setText(productName);

        } else {
            scratchCard1.setOnScratchListener(new ScratchCard.OnScratchListener() {
                @Override
                public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                    if (visiblePercent > 0.7) {
//                        scratch(true);
                        mp.stop();
                        DirectPointAddPlaceOrder(pcode, "0", "0");

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
            Intent intent = new Intent(PlayBuyDashboard.this, PlayBuyDashboard.class);
            startActivity(intent);
        });

        linearProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Scratch your Card", Toast.LENGTH_LONG).show();
                scratchCard1.setOnScratchListener(new ScratchCard.OnScratchListener() {
                    @Override
                    public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                        if (visiblePercent > 0.7) {
//                        scratch(true);
                            mp.stop();
                            DirectPointAddPlaceOrder(pcode, "0", "0");
                       /* update 17-05-2023 Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_Summery_Details.class);
                        intent.putExtra("p_code",productCode);
                        intent.putExtra("p_userid",UserId);
                        intent.putExtra("p_disamount",productDisAmt);
                        intent.putExtra("p_price",productPrice);
                        intent.putExtra("p_name",productName);
                        intent.putExtra("p_img",productImg);
                        intent.putExtra("class_name","scretchget");
                        startActivity(intent);update 17-05-2023 */
                        } else {
                            Toast.makeText(getApplicationContext(), "Scratch your Card", Toast.LENGTH_LONG).show();
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
                    Intent intent = new Intent(PlayBuyDashboard.this, PlayBuyDashboard.class);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), GameSelection.class);
        startActivity(intent);
    }


    //ProductDetailspopupdetails
    private void openOfferpopup1(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(PlayBuyDashboard.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(PlayBuyDashboard.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "PLAYBUYProductDeatilsbyId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ProductDeatilsbyId", response);
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

                        if (isStock.equalsIgnoreCase("0")) {
                            outofstok_img.setVisibility(View.VISIBLE);
                            imageView.setAlpha((float) 0.5);
                        } else {
                            outofstok_img.setVisibility(View.GONE);

                        }


                        ProductSizeUnittv.setText(productSizeUnit);
                        ProductSizeValuetv.setText(productSizeValue);


                        String description = jsonObject2.getString("Description");
                        if (description.equalsIgnoreCase("")) {
                            productdetails_tv.setText("Details Not Found!!");
                        } else {
                            productdetails_tv.setText(description);
                        }
                        name.setText(plan_name);
                        p_code.setText(planCode);
                        amount.setText("\u20B9 " + productMrp);
                        discount_tv.setText(discount + " %");
                        orderamount_tv.setText("\u20B9 " + saleRate);
                        Glide.with(PlayBuyDashboard.this)
                                .load(productImg).error(R.drawable.a_logo)
                                .into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), FullImagePage.class);
                                try {
                                    intent.putExtra("path", jsonObject2.getString("ProductImg"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }
                        });
                    }

                    if (jsonObject.getString("Message").equalsIgnoreCase("You have already visited.")) {
                        Toast.makeText(PlayBuyDashboard.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(PlayBuyDashboard.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ProductId", productcode);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PlayBuyDashboard.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails


    public void BlockUnblockAPI() {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "UserblockUnblockStatus", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("wallet", "sadsad" + response);
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog1.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                    } else {
                        Toast.makeText(PlayBuyDashboard.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlayBuyDashboard.this);
                        builder.setTitle("Login Alert Confirmation!").setMessage(object.getString("Message")).setCancelable(false).setIcon(R.drawable.logo).setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences myPrefs = getSharedPreferences("login_preference", MODE_PRIVATE);
                                SharedPreferences.Editor editor = myPrefs.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                                startActivity(intent);
                            }
                        });
                        builder.create().show();
                        Toast.makeText(PlayBuyDashboard.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(PlayBuyDashboard.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PlayBuyDashboard.this);
        requestQueue.add(stringRequest);

    }


    private void DirectPointAddPlaceOrder(String p_code, String p_oamt, String p_price) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", UserId);
            jsonObject.put("ProductCode", p_code);
            jsonObject.put("OrderAmount", p_oamt);
            jsonObject.put("Amount", p_price);
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
                    Intent intent = new Intent(PlayBuyDashboard.this, PlayBuyDashboard.class);
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
        alertDialogamt = new Dialog(PlayBuyDashboard.this);
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
                getScratchProductDetails("1", SelectedNumber);
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl + "PLAYBUY_BetAmount", new Response.Listener<String>() {
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
                Toast.makeText(PlayBuyDashboard.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PlayBuyDashboard.this);
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
                holder.tvNum1.setTextColor(ContextCompat.getColor(PlayBuyDashboard.this, R.color.white));
            } else {
                holder.linear1Selected.setBackgroundResource(R.drawable.back_blue_white);
                holder.tvNum1.setTextColor(ContextCompat.getColor(PlayBuyDashboard.this, R.color.black));
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
            tvNum1 = itemView.findViewById(R.id.tvNum1);
            linear1Selected = itemView.findViewById(R.id.linear1Selected);

        }
    }


    private void getCatBannerList(String id) {
        final ProgressDialog progressDialog = new ProgressDialog(PlayBuyDashboard.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.BannerList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("banneruimg", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    String JsonInString = jsonObject.getString("Response");
                    bannerData = BannerData.createJsonInList(JsonInString);
                    pager.setAdapter(new AdapterForBanner(PlayBuyDashboard.this, bannerData));
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
                        public void onPageScrolled(int pos, float arg1, int arg2) {
                        }

                        @Override
                        public void onPageScrollStateChanged(int pos) {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
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
        RequestQueue requestQueue = Volley.newRequestQueue(PlayBuyDashboard.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    //    Banner winer and looser list API
    public void GetWinnerList(String type) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.PLAYBUY_WinLossList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res", response);
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
                            hashlist.put("Img", jsonObject.getString("Img"));
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
                                if (wcount < adaptorMatchGetNum.getItemCount()) {
                                    if (wcount == adaptorMatchGetNum.getItemCount() - 1) {
                                        wflag = false;
                                    } else if (wcount == 0) {
                                        wflag = true;
                                    }
                                    if (wflag) wcount++;
                                    else wcount--;

                                    recyclerTossMatchAmt.smoothScrollToPosition(wcount);
                                    handler.postDelayed(this, speedScroll);
                                }
                            }
                        };

                        handler.postDelayed(runnable, speedScroll);


                        final int speedScroll1 = 2500;
                        final Handler handler1 = new Handler();
                        final Runnable runnable1 = new Runnable() {
                            int wcount1 = 0;
                            boolean wflag1 = true;

                            @Override
                            public void run() {
                                if (wcount1 < adaptorMatchGetNumValues.getItemCount()) {
                                    if (wcount1 == adaptorMatchGetNumValues.getItemCount() - 1) {
                                        wflag1 = false;
                                    } else if (wcount1 == 0) {
                                        wflag1 = true;
                                    }
                                    if (wflag1) wcount1++;
                                    else wcount1--;

                                    recyclerTossMatchValue.smoothScrollToPosition(wcount1);
                                    handler1.postDelayed(this, speedScroll1);
                                }
                            }
                        };

                        handler1.postDelayed(runnable1, speedScroll1);


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
                Log.d("myTag", "message:" + error);
                Toast.makeText(PlayBuyDashboard.this, "Something went wrong!" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Type", type);
                ;
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(PlayBuyDashboard.this);
        requestQueue.add(stringRequest);

    }

    public void GetLooserList(String type) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebServices.PLAYBUY_WinLossList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res", response);
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
                            hashlist.put("Img", jsonObject.getString("Img"));
                            ;
                            arrayListloss.add(hashlist);
                        }
                        adaptorMatchGetNumValues = new AdaptorMatchGetNumValues(getApplicationContext(), arrayListloss);
                        recyclerTossMatchValue.setAdapter(adaptorMatchGetNumValues);
                        //recyclerTossMatchValue.suppressLayout(true);


                        final int speedScroll = 2500;
                        final Handler handler = new Handler();
                        final Runnable runnable = new Runnable() {
                            int count = 0;
                            boolean flag = true;

                            @Override
                            public void run() {
                                if (count < adaptorMatchGetNum.getItemCount()) {
                                    if (count == adaptorMatchGetNum.getItemCount() - 1) {
                                        flag = false;
                                    } else if (count == 0) {
                                        flag = true;
                                    }
                                    if (flag) count++;
                                    else count--;

                                    recyclerTossMatchAmt.smoothScrollToPosition(count);
                                    handler.postDelayed(this, speedScroll);
                                }
                            }
                        };

                        handler.postDelayed(runnable, speedScroll);


                        final int speedScroll1 = 2500;
                        final Handler handler1 = new Handler();
                        final Runnable runnable1 = new Runnable() {
                            int count1 = 0;
                            boolean flag1 = true;

                            @Override
                            public void run() {
                                if (count1 < adaptorMatchGetNumValues.getItemCount()) {
                                    if (count1 == adaptorMatchGetNumValues.getItemCount() - 1) {
                                        flag1 = false;
                                    } else if (count1 == 0) {
                                        flag1 = true;
                                    }
                                    if (flag1) count1++;
                                    else count1--;

                                    recyclerTossMatchValue.smoothScrollToPosition(count1);
                                    handler1.postDelayed(this, speedScroll1);
                                }
                            }
                        };

                        handler1.postDelayed(runnable1, speedScroll1);


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
                Log.d("myTag", "message:" + error);
                Toast.makeText(PlayBuyDashboard.this, "Something went wrong!" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Type", type);
                ;
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(PlayBuyDashboard.this);
        requestQueue.add(stringRequest);

    }

    public class AdaptorMatchGetNum extends RecyclerView.Adapter<AdaptorMatchGetNum.VH> {
        Context context;
        List<HashMap<String, String>> arrayList;

        public AdaptorMatchGetNum(Context context, List<HashMap<String, String>> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
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
            AnimationHelper.animatate(context, vh.itemView, R.anim.alfa_animation);
            vh.win_name_tv.setText("WIN\n" + arrayList.get(i).get("UserId"));
            Glide.with(PlayBuyDashboard.this)
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
        List<HashMap<String, String>> arrayList;

        public AdaptorMatchGetNumValues(Context context, List<HashMap<String, String>> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
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
            AnimationHelper.animatate(context, vh.itemView, R.anim.alfa_animation);
            vh.win_name_tv.setText("Loos\n" + arrayList.get(i).get("UserId"));
            Glide.with(PlayBuyDashboard.this)
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