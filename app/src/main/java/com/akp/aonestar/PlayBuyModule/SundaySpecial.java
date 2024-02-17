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
import android.net.Uri;
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
import android.window.SplashScreen;

import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.GameSelection;
import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.GoodLuckModule.AdapterForBanner;
import com.akp.aonestar.GoodLuckModule.AnimationHelper;
import com.akp.aonestar.GoodLuckModule.BannerData;
import com.akp.aonestar.GoodLuckModule.DoWantAnythingElse;
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
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.SplashActivity;
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

public class SundaySpecial extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerScratchProductList, recycScratchList;
    AdaptorImageSlider adaptorImageSlider;
    LinearLayout linearScratchLay, linearPlay, linearScratch;
    AdaptorCateWiseProductList adaptorCateWiseProductList;
    AdaptorCateWiseProductList1 adaptorCateWiseProductList1;
    SundaySpecial.AdaptorScratchProductList adaptorScratchProductList;
    Dialog alertDialog, alertDialog1, alertDialogamt;
    String productImage, productName, productPrice, productCode, productDisAmt;
    ProgressDialog progressDialog;
    List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> listProduct = new ArrayList<>();
    List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> listProduct1 = new ArrayList<>();
    private String UserId;
    private final int limit = 9;
    private Dialog dialog;

    int i = 0;
    TextView f_one_tv, f_two_tv, f_add_tv, h_one_tv, h_two_tv, h_three_tv, h_four_tv, h_five_tv, h_add_tv;


    LinearLayout dummy_ll;

    String SelectedNumber;
    int k = 0;

    private final ArrayList<HashMap<String, String>> sundaydummyList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private SundayDummyAdapter sundaydummypdfAdapTer;
    ImageView norecord_tv;
    private RecyclerView dummy_no_rec;
    LinearLayout scrateh_ll, fifty_ll, hundred_ll, number_show_hide_ll;
    RelativeLayout show_hide_rl;
    String GetNumber;
    CirclePageIndicator indicator;
    ViewPager pager;
    List<BannerData> bannerData = new ArrayList<>();
    private static int currentPage = 0;
    private AlertDialog alertDialog_win, alertDialog_loos;
    private int disabledPosition = -1; // Initialize with an invalid position


    ArrayList<HashMap<String, String>> arrayListwin = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayListloss = new ArrayList<>();
    RecyclerView recyclerTossMatchAmt, recyclerTossMatchValue;
    AdaptorMatchGetNum adaptorMatchGetNum;
    AdaptorMatchGetNumValues adaptorMatchGetNumValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunday_special);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();

        initViews();
        if (UserId.equalsIgnoreCase("")) {
        } else {
            BlockUnblockAPI();
        }

        dummy_no_rec = findViewById(R.id.dummy_no_rec);


        SundayDummyNoAPI();

        GetWinnerList("Win");
        GetLooserList("Loss");
    }

    private void initViews() {
        progressDialog = new ProgressDialog(SundaySpecial.this);
        number_show_hide_ll = findViewById(R.id.number_show_hide_ll);
        linearScratchLay = findViewById(R.id.linearScratchLay);
        linearScratch = findViewById(R.id.linearScratch);
        linearPlay = findViewById(R.id.linearPlay);
        show_hide_rl = findViewById(R.id.show_hide_rl);
        recycScratchList = findViewById(R.id.recycScratchList);
        dummy_ll = findViewById(R.id.dummy_ll);
        scrateh_ll = findViewById(R.id.scrateh_ll);
        fifty_ll = findViewById(R.id.fifty_ll);
        hundred_ll = findViewById(R.id.hundred_ll);
        recyclerScratchProductList = findViewById(R.id.recyclerScratchProductList);

        linearScratch.setOnClickListener(this);
        linearPlay.setOnClickListener(this);

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager1);
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


        f_one_tv = findViewById(R.id.f_one_tv);
        f_two_tv = findViewById(R.id.f_two_tv);
        f_add_tv = findViewById(R.id.f_add_tv);
        h_one_tv = findViewById(R.id.h_one_tv);
        h_two_tv = findViewById(R.id.h_two_tv);
        h_three_tv = findViewById(R.id.h_three_tv);
        h_four_tv = findViewById(R.id.h_four_tv);
        h_five_tv = findViewById(R.id.h_five_tv);
        h_add_tv = findViewById(R.id.h_add_tv);


        if (InternetConnection.checkConnection(getApplicationContext())) {
            getCatBannerList("6");
            getCatProdList();
            getCatProdList1("1");
            ScretchgetCatProdList();
        } else {
            DialogManager.openCheckInternetDialog(SundaySpecial.this);
        }


        show_hide_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (k == 0) {
                    number_show_hide_ll.setVisibility(View.GONE);
                    k++;
                } else if (k == 1) {
                    number_show_hide_ll.setVisibility(View.VISIBLE);
                    k = 0;
                }
            }
        });
    }

    private void getCatProdList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Search", "");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_SundaySpecialProduct, jsonObject, response -> {
            Gson gson = new Gson();
            try {
                DataResponseProduct productCategoryModel = gson.fromJson(response.toString(), DataResponseProduct.class);
                if (productCategoryModel.isStatus()) {
                    listProduct.clear();
                    listProduct = productCategoryModel.getResponse();
                    adaptorCateWiseProductList = new AdaptorCateWiseProductList(getApplicationContext(), productCategoryModel.getResponse());
                    recyclerScratchProductList.setAdapter(adaptorCateWiseProductList);
                } else {
                    Toast.makeText(SundaySpecial.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    private void ScretchgetCatProdList() {
        JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WebServices.PLAYBUY_SundaySpecialNumberList, jsonObject, response -> {
            Gson gson = new Gson();
            try {
                DataResponseProduct productCategoryModel = gson.fromJson(response.toString(), DataResponseProduct.class);
                if (productCategoryModel.isStatus()) {
                    adaptorScratchProductList = new SundaySpecial.AdaptorScratchProductList(getApplicationContext(), productCategoryModel.getResponse());
                    recycScratchList.setAdapter(adaptorScratchProductList);


                } else {
                    Toast.makeText(SundaySpecial.this, "Record not found", Toast.LENGTH_SHORT).show();
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
                    listProduct1 = productCategoryModel.getResponse();


                } else {
                    Toast.makeText(SundaySpecial.this, "Record not found", Toast.LENGTH_SHORT).show();
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
                CheckSundayGameActiveAPI();

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
        View dialogView = LayoutInflater.from(SundaySpecial.this).inflate(R.layout.show_product_popup_details, viewGroup, false);

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
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SundaySpecial.this);
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
        View dialogView = LayoutInflater.from(SundaySpecial.this).inflate(R.layout.popup_gameoverview, viewGroup, false);

        TextView view_details_tv = dialogView.findViewById(R.id.view_details_tv);

        FloatingActionButton close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
//        arrayList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(SundaySpecial.this);
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
                    Toast.makeText(SundaySpecial.this, "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        }, error -> {
            progressDialog.dismiss();
            Log.d("myTag", "message:" + error);
            Toast.makeText(SundaySpecial.this, "No Record Found!", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SundaySpecial.this);
        requestQueue.add(stringRequest);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SundaySpecial.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SundaySpecial.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_SundaySpecialPlayNow, jsonObject, response -> {
            try {
                Log.e("TAG", "getScratchProductDetails: " + response);
                if (response.getBoolean("Status")) {
                    progressDialog.dismiss();
                    recycScratchList.setVisibility(View.VISIBLE);
                    linearPlay.setVisibility(View.GONE);
                    dummy_ll.setVisibility(View.GONE);
                    scrateh_ll.setVisibility(View.VISIBLE);
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
                    linearPlay.setVisibility(View.VISIBLE);
                    scrateh_ll.setVisibility(View.GONE);
                    dummy_ll.setVisibility(View.VISIBLE);
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

    public class AdaptorCateWiseProductList extends RecyclerView.Adapter<AdaptorCateWiseProductList.MyCatProduct> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList;

        public AdaptorCateWiseProductList(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public AdaptorCateWiseProductList.MyCatProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptorCateWiseProductList.MyCatProduct(LayoutInflater.from(context).inflate(R.layout.layout_category_wise_product, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptorCateWiseProductList.MyCatProduct holder, int position) {
            Glide.with(context)
                    .load(itemList.get(position).getProductImg())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.img);
            holder.name.setText(itemList.get(position).getProductName());
            holder.code.setText(itemList.get(position).getPoints());

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

            if (itemList.size() > limit) {
                return limit;
            } else {
                return itemList.size();
            }

//            return itemList.size();
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

    public class AdaptorScratchProductList extends RecyclerView.Adapter<SundaySpecial.AdaptorScratchProductList.MyScratchHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList1;

        public AdaptorScratchProductList(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList) {
            this.context = context;
            this.itemList1 = itemList;
        }

        @NonNull
        @Override
        public SundaySpecial.AdaptorScratchProductList.MyScratchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SundaySpecial.AdaptorScratchProductList.MyScratchHolder(LayoutInflater.from(context).inflate(R.layout.scratch_layout_sunday, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SundaySpecial.AdaptorScratchProductList.MyScratchHolder holder, @SuppressLint("RecyclerView") int position) {
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
//                    Toast.makeText(getApplicationContext(),itemList1.get(position).getNumber(),Toast.LENGTH_LONG).show();
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
//             if(itemList.size() > limit){
//                return limit;
//            }
//            else
//            {
//                return itemList.size();
//            }
            return itemList1.size();
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


    public class AdaptorCateWiseProductList1 extends RecyclerView.Adapter<AdaptorCateWiseProductList1.MyCatProduct> {
        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList;

        public AdaptorCateWiseProductList1(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public AdaptorCateWiseProductList1.MyCatProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptorCateWiseProductList1.MyCatProduct(LayoutInflater.from(context).inflate(R.layout.layout_category_wise_product, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptorCateWiseProductList1.MyCatProduct holder, int position) {
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
    private void openScratchDialogBox(String Number) {
        final MediaPlayer mp = MediaPlayer.create(SundaySpecial.this, R.raw.blast_one);
        mp.start();
        alertDialog = new Dialog(SundaySpecial.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_scratch_layout_sunday);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans)));
        alertDialog.setCancelable(false);
        alertDialog.show();
        //alertDialog.getWindow().setLayout((6 * width)/7, ActionBar.LayoutParams.WRAP_CONTENT);


        GifImageView gif_a = alertDialog.findViewById(R.id.gif_a);
        TextView numb = alertDialog.findViewById(R.id.numb);
        LinearLayout linearProceed = alertDialog.findViewById(R.id.linearProceed);
        ScratchCard scratchCard1 = alertDialog.findViewById(R.id.scratchCard1);

        numb.setText(Number);
        linearProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
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
                    if (SelectedNumber.equalsIgnoreCase("50")) {
                        if (f_one_tv.getText().toString().equalsIgnoreCase("?")) {
                            f_one_tv.setText(Number);
                            alertDialog.dismiss();
                        } else if (f_two_tv.getText().toString().equalsIgnoreCase("?")) {
                            f_two_tv.setText(Number);
                            int total = Integer.parseInt(f_one_tv.getText().toString()) + Integer.parseInt(f_two_tv.getText().toString());
                            f_add_tv.setText(String.valueOf(total));
                            MatchedNumberAPI(SelectedNumber, f_one_tv.getText().toString(), f_two_tv.getText().toString(), "0", "0", "0");
                            alertDialog.dismiss();

                        }
                    } else {
                        if (h_one_tv.getText().toString().equalsIgnoreCase("?")) {
                            h_one_tv.setText(Number);
                            alertDialog.dismiss();
                        } else if (h_two_tv.getText().toString().equalsIgnoreCase("?")) {
                            h_two_tv.setText(Number);
                            alertDialog.dismiss();
                        } else if (h_three_tv.getText().toString().equalsIgnoreCase("?")) {
                            h_three_tv.setText(Number);
                            alertDialog.dismiss();
                        } else if (h_four_tv.getText().toString().equalsIgnoreCase("?")) {
                            h_four_tv.setText(Number);
                            alertDialog.dismiss();
                        } else if (h_five_tv.getText().toString().equalsIgnoreCase("?")) {
                            h_five_tv.setText(Number);
                            int total1 = Integer.parseInt(h_one_tv.getText().toString()) + Integer.parseInt(h_two_tv.getText().toString()) + Integer.parseInt(h_three_tv.getText().toString()) + Integer.parseInt(h_four_tv.getText().toString()) + Integer.parseInt(h_five_tv.getText().toString());
                            h_add_tv.setText(String.valueOf(total1));
                            MatchedNumberAPI(SelectedNumber, h_one_tv.getText().toString(), h_two_tv.getText().toString(), h_three_tv.getText().toString(), h_four_tv.getText().toString(), h_five_tv.getText().toString());
                            alertDialog.dismiss();

                        }
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

    private void MatchedNumberAPI(String bet_amt, String num1, String num2, String num3, String num4, String num5) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", UserId);
            jsonObject.put("BetAmount", bet_amt);
            jsonObject.put("BoxAmount1", num1);
            jsonObject.put("BoxAmount2", num2);
            jsonObject.put("BoxAmount3", num3);
            jsonObject.put("BoxAmount4", num4);
            jsonObject.put("BoxAmount5", num5);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_SundaySpecialMacthingBox, jsonObject, response -> {
            try {
                Log.d("sdfgdsgdssdgsdgg", "PLAYBUY_SundaySpecialMacthingBox: " + response);
                if (response.getBoolean("Status") == true) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                    Toast.makeText(this, "" + jsonObject1.getString("ProductName")+"("+jsonObject1.getString("ProductCode")+")", Toast.LENGTH_SHORT).show();
                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(SundaySpecial.this).inflate(R.layout.dynamic_winpopup, viewGroup, false);
                    Button rlBack = (AppCompatButton) dialogView.findViewById(R.id.btncancel);
                    TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);
                    Button buy_btn = (AppCompatButton) dialogView.findViewById(R.id.buy_btn);
                    txt_msg.setText(jsonObject1.getString("ProductName") + "(" + jsonObject1.getString("ProductCode") + ")");


                    buy_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), PlayBuy_Oder_Summery_Details.class);
                            try {
                                intent.putExtra("p_code", jsonObject1.getString("ProductCode"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intent.putExtra("p_userid", UserId);
                            intent.putExtra("p_disamount", "");
                            intent.putExtra("p_price", "");
                            intent.putExtra("p_name", "");
                            intent.putExtra("p_img", "");
                            intent.putExtra("class_name", "SundaySpecial");
                            startActivity(intent);
                            alertDialog_win.dismiss();
                        }
                    });


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
                    AlertDialog.Builder builder = new AlertDialog.Builder(SundaySpecial.this);
                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView);
                    //finally creating the alert dialog and displaying it
                    alertDialog_win = builder.create();
                    alertDialog_win.show();

                } else {
                    Toast.makeText(this, response.getString("Message"), Toast.LENGTH_SHORT).show();
                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(SundaySpecial.this).inflate(R.layout.dynamic_loosepopup, viewGroup, false);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(SundaySpecial.this);
                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView);
                    //finally creating the alert dialog and displaying it
                    alertDialog_loos = builder.create();
                    alertDialog_loos.show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
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
                    linearPlay.setVisibility(View.VISIBLE);
                    scrateh_ll.setVisibility(View.GONE);
                    dummy_ll.setVisibility(View.VISIBLE);
                    recyclerScratchProductList.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SundaySpecial.this, SundaySpecial.class);
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
        dialog = new Dialog(SundaySpecial.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(SundaySpecial.this);
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
                        Glide.with(SundaySpecial.this)
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
                        Toast.makeText(SundaySpecial.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SundaySpecial.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SundaySpecial.this);
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
                        Toast.makeText(SundaySpecial.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SundaySpecial.this);
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
                        Toast.makeText(SundaySpecial.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(SundaySpecial.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SundaySpecial.this);
        requestQueue.add(stringRequest);

    }




/*
    private void DirectPointAddPlaceOrder(String p_dis,String p_price,String p_service,String p_del) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProductCode", GetproductCode);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Discount", p_dis);
            jsonObject.put("Amount",  p_price);

            jsonObject.put("OrderAmount", txt_payableAmount.getText().toString());
            jsonObject.put("IsWallet", "Online");
            jsonObject.put("UtrNo", utr_et.getText().toString());
            jsonObject.put("Name", Getname_et);
            jsonObject.put("MobileNo", Getmob_et);
            jsonObject.put("EmialId", Getemail_et);
            jsonObject.put("Address", Getaddress_et);
            jsonObject.put("Pincode", Getpincode_et);

            jsonObject.put("Street", Getstreet_et);
            jsonObject.put("Landmark", Getloc_et);
            jsonObject.put("ServiceCharge", p_service);
            jsonObject.put("DeliveryFee", p_del);
            jsonObject.put("Size", size_et.getSelectedItem().toString());
            jsonObject.put("Color", color_et.getSelectedItem().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ScratchGetPlay_Purchase, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SundaySpecial.this, GoodLuckGameProductView.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, response.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
*/


    private void openPlayDialog() {
        alertDialogamt = new Dialog(SundaySpecial.this);
        alertDialogamt.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogamt.setContentView(R.layout.popup_amountnew_sunday);
        alertDialogamt.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialogamt.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans)));
        alertDialogamt.setCancelable(false);
        alertDialogamt.show();
        //alertDialog.getWindow().setLayout((6 * width)/7, ActionBar.LayoutParams.WRAP_CONTENT);


        LinearLayout fifty = alertDialogamt.findViewById(R.id.fifty);
        LinearLayout hundred = alertDialogamt.findViewById(R.id.hundred);

        fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNumber = "50";
                fifty.setBackgroundResource(R.drawable.back_selected_red);
                hundred.setBackgroundResource(R.drawable.back_blue_white);
            }
        });

        hundred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedNumber = "100";
                hundred.setBackgroundResource(R.drawable.back_selected_red);
                fifty.setBackgroundResource(R.drawable.back_blue_white);
            }
        });


        TextView tvYes = alertDialogamt.findViewById(R.id.tvYes);
        TextView tvCancel = alertDialogamt.findViewById(R.id.tvCancel);

        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScratchProductDetails("1", SelectedNumber);
                if (SelectedNumber.equalsIgnoreCase("50")) {
                    fifty_ll.setVisibility(View.VISIBLE);
                    hundred_ll.setVisibility(View.GONE);
                } else {
                    hundred_ll.setVisibility(View.VISIBLE);
                    fifty_ll.setVisibility(View.GONE);
                }
                alertDialogamt.cancel();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogamt.dismiss();
            }
        });
//

    }


    public void SundayDummyNoAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebServices.PLAYBUY_SundaySpecialDummyNumberList, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Message");
                    if (status.equalsIgnoreCase("Success")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
//                            title_tv.setText("External Link Report("+Response.length()+")");
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("Number", jsonObject.getString("Number"));
                            sundaydummyList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 5);
                        sundaydummypdfAdapTer = new SundayDummyAdapter(getApplicationContext(), sundaydummyList);
                        dummy_no_rec.setLayoutManager(layoutManager);
                        dummy_no_rec.setAdapter(sundaydummypdfAdapTer);
                    } else {
                        Toast.makeText(SundaySpecial.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SundaySpecial.this, "Something went wrong!" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(SundaySpecial.this);
        requestQueue.add(stringRequest);

    }

    public class SundayDummyAdapter extends RecyclerView.Adapter<DummySundayList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public SundayDummyAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }

        public DummySundayList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DummySundayList(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final DummySundayList holder, final int position) {
            holder.tvMNum.setText(data.get(position).get("Number"));


        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class DummySundayList extends RecyclerView.ViewHolder {
        TextView tvMNum;

        public DummySundayList(View itemView) {
            super(itemView);
            tvMNum = itemView.findViewById(R.id.tvMNum);
        }
    }

    private void getCatBannerList(String id) {
        final ProgressDialog progressDialog = new ProgressDialog(SundaySpecial.this);
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
                    pager.setAdapter(new AdapterForBanner(SundaySpecial.this, bannerData));
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
        RequestQueue requestQueue = Volley.newRequestQueue(SundaySpecial.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), PlayBuyDashboard.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
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
                            ;
                            arrayListwin.add(hashlist);
                        }
                        adaptorMatchGetNum = new AdaptorMatchGetNum(getApplicationContext(), arrayListwin);
                        recyclerTossMatchAmt.setAdapter(adaptorMatchGetNum);


                        final int speedScroll = 1500;
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


                        final int speedScroll1 = 1500;
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
                Toast.makeText(SundaySpecial.this, "Something went wrong!" + error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(SundaySpecial.this);
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


                        final int speedScroll = 1500;
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


                        final int speedScroll1 = 1500;
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
                Toast.makeText(SundaySpecial.this, "Something went wrong!" + error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(SundaySpecial.this);
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
            Glide.with(SundaySpecial.this)
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
            Glide.with(SundaySpecial.this)
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


    public void CheckSundayGameActiveAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl + "PLAYBUY_SundaySpecialGameCheck", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("dfdff", "" + response);
//                Toast.makeText(LoginScreenAkp.this, response, Toast.LENGTH_SHORT).show();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        openPlayDialog();
                    } else {
                        Toast.makeText(getApplicationContext(), object.getString("Message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SundaySpecial.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SundaySpecial.this);
        requestQueue.add(stringRequest);
    }

}