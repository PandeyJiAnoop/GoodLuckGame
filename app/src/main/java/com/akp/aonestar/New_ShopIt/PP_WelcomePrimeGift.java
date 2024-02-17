package com.akp.aonestar.New_ShopIt;

import static com.akp.aonestar.RetrofitAPI.API_Config.getApiClient_ByPost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
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

import com.akp.aonestar.Basic.BottomOnClick;
import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.GameSelection;
import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Ecommerce.E_CartActivity;
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
import com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors.AdaptorImageSlider;
import com.akp.aonestar.PlayBuyModule.PlayBuyDashboard;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Category.DataResponseCategory;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.DataResponseProduct;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem;
import com.akp.aonestar.PlayBuyModule.ScratchCardManager.ScratchCard;
import com.akp.aonestar.PlayBuyModule.SundaySpecial;
import com.akp.aonestar.PriceDropModule.PriceDropDashboard;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.ApiService;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.RetrofitAPI.ConnectToRetrofit;
import com.akp.aonestar.RetrofitAPI.GlobalAppApis;
import com.akp.aonestar.RetrofitAPI.RetrofitCallBackListenar;
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
import retrofit2.Call;

public class PP_WelcomePrimeGift extends AppCompatActivity {

    RecyclerView  recycScratchList,recyclerScratchProductList;
    AdaptorScratchProductList adaptorScratchProductList;
AdaptorCateWiseProductList adaptorCateWiseProductList;
    Dialog alertDialog;
    String productImage, productName, productPrice, productCode, productDisAmt;

    ProgressDialog progressDialog;
    List<Prime_ResponseItem> listProduct = new ArrayList<>();
    private String UserId;
    private final int limit = 9;
    private Dialog dialog;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();

    String GetCatId,GetSubCatId;

    ImageView details;
    private AlertDialog alertDialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pp_welcome_prime_gift);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        GetCatId=getIntent().getStringExtra("cat_id");
        GetSubCatId=getIntent().getStringExtra("sub_cat_id");
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();
        BottomOnClick bottom = (BottomOnClick) findViewById(R.id.bottom1);
        bottom.initHeader();
        LinearLayout social_ll=findViewById(R.id.social_ll);
        social_ll.setVisibility(View.GONE);

        TextView txtMarquee = (TextView) findViewById(R.id.marqueeText);
        txtMarquee.setSelected(true);
        initViews();
    }

    private void initViews() {
        progressDialog = new ProgressDialog(PP_WelcomePrimeGift.this);
        recycScratchList = findViewById(R.id.recycScratchList);
        recycScratchList.setLayoutManager(new GridLayoutManager(this, 3));
        recycScratchList.setHasFixedSize(true);
        recyclerScratchProductList = findViewById(R.id.recyclerScratchProductList);
        recyclerScratchProductList.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerScratchProductList.setHasFixedSize(true);
        if (InternetConnection.checkConnection(getApplicationContext())) {
            getCatProdList(GetCatId,GetSubCatId);
        } else {
            DialogManager.openCheckInternetDialog(PP_WelcomePrimeGift.this);
        }


        details = (ImageView) findViewById(R.id.details);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameDetails_popup1("GoodLuck");
            }
        });


    }

    private void getCatProdList(String cat_id,String sub_cat) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CategoryId", cat_id);
            jsonObject.put("SubCategoryId", sub_cat);
            jsonObject.put("UserId", UserId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.Shoppit_WelcomePrime_New, jsonObject, response -> {
            Log.d("istlayoutresponse", "" + response);
            Gson gson = new Gson();
            try {
                Prime_DataResponseProduct productCategoryModel = gson.fromJson(response.toString(), Prime_DataResponseProduct.class);
                if (productCategoryModel.isStatus()) {
                    listProduct.clear();
                    listProduct = productCategoryModel.getResponse();
                    adaptorCateWiseProductList = new AdaptorCateWiseProductList(getApplicationContext(), productCategoryModel.getResponse());
                    recyclerScratchProductList.setAdapter(adaptorCateWiseProductList);
                     adaptorScratchProductList = new AdaptorScratchProductList(getApplicationContext(), productCategoryModel.getResponse());
                    recycScratchList.setAdapter(adaptorScratchProductList);
                } else {
                    Toast.makeText(PP_WelcomePrimeGift.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    public class AdaptorCateWiseProductList extends RecyclerView.Adapter<AdaptorCateWiseProductList.MyCatProduct> {
        Context context;
        List<Prime_ResponseItem> itemList;
        public AdaptorCateWiseProductList(Context context, List<Prime_ResponseItem> itemList) {
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
                    .load(itemList.get(position).getPrimeProductImg())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.img);
            holder.name.setText(itemList.get(position).getPrimeProductName());
            holder.code.setText(String.valueOf("₹ " + itemList.get(position).getPrimeProductSaleRate()));

            Log.d("resplan",itemList.get(position).getIsPlan());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!UserId.equalsIgnoreCase("") && !(itemList.get(position).getIsPlan().equalsIgnoreCase("0"))){
                        recycScratchList.setVisibility(View.VISIBLE);
                        recyclerScratchProductList.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Please Buy A Plan After That You Can Avail many Benefits!",Toast.LENGTH_SHORT).show();
                    }}});

//            dfgfd
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



    public class AdaptorScratchProductList extends RecyclerView.Adapter<AdaptorScratchProductList.MyScratchHolder> {

        Context context;
        List<Prime_ResponseItem> itemList;

        public AdaptorScratchProductList(Context context, List<Prime_ResponseItem> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public AdaptorScratchProductList.MyScratchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptorScratchProductList.MyScratchHolder(LayoutInflater.from(context).inflate(R.layout.scratch_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptorScratchProductList.MyScratchHolder holder, @SuppressLint("RecyclerView") int position) {
            Glide.with(context)
                    .load(itemList.get(position).getPrimeProductImg())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.pro_img);
            holder.pro_name.setText(itemList.get(position).getPrimeProductName());
            holder.pro_code.setText(String.valueOf("₹ " + itemList.get(position).getPrimeProductSaleRate()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    API_Shoppit_WelcomePrime_New_Scratch(GetCatId,GetSubCatId);
                   /* Toast.makeText(PP_WelcomePrimeGift.this, ""+itemList.get(position).getProductName()+
                            "\n"+itemList.get(position).getProductSaleRate(), Toast.LENGTH_SHORT).show();*/
                    Log.d("popupvalue",productCode+"\n"+itemList.get(position).getPrimeProductImg()+"\n"+productName+"\n"+productCode+"\n"+productPrice);
//                    openScratchDialogBox(itemList.get(position).getPrimeProductCode(), itemList.get(position).getPrimeProductImg(), itemList.get(position).getPrimeProductName(), "₹ " +
//                            itemList.get(position).getPrimeProductSaleRate());
                } });
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
    private void API_Shoppit_WelcomePrime_New_Scratch(String CategoryId, String SubCategoryId) {
        String otp1 = new GlobalAppApis().WelcomePrime_New_Scratch(CategoryId,SubCategoryId,UserId);
        Log.v("otp1cart", otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.Shoppit_WelcomePrime_New_Scratch(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.v("Shoppit_New_Scratch", result);
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            openScratchDialogBox(jsonObject2.getString("ProductCode"),jsonObject2.getString("ProductImg"),
                                    jsonObject2.getString("ProductName"),jsonObject2.getString("ProductMRP"));
                            Toast.makeText(PP_WelcomePrimeGift.this, jsonObject2.getString("msg"), Toast.LENGTH_SHORT).show();

                        }
                    }else {
                        Toast.makeText(PP_WelcomePrimeGift.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, PP_WelcomePrimeGift.this, call1, "", true);


    }

    @SuppressLint("SetTextI18n")
    private void openScratchDialogBox(String pcode, String productImg, String productName, String price) {
        alertDialog = new Dialog(PP_WelcomePrimeGift.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_welcome_prime_scratch_layout);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans)));
        alertDialog.setCancelable(false);
        alertDialog.show();
        //alertDialog.getWindow().setLayout((6 * width)/7, ActionBar.LayoutParams.WRAP_CONTENT);

        TextView title_tv= alertDialog.findViewById(R.id.title_tv);
        title_tv.setText("WELCOME PRIME GIFT\nLOAD OF LUCK");
        TextView marqueeText= alertDialog.findViewById(R.id.marqueeText);
        marqueeText.setText("In This Way you will get scratched six times,after that it will not come,you have to take one of the six");
        GifImageView gif_a = alertDialog.findViewById(R.id.gif_a);
        ImageView imgProduct = alertDialog.findViewById(R.id.imgProduct);
        TextView proName = alertDialog.findViewById(R.id.productName);
        LinearLayout linearExit = alertDialog.findViewById(R.id.linearExit);
        LinearLayout linearProceed = alertDialog.findViewById(R.id.linearProceed);
        ScratchCard scratchCard1 = alertDialog.findViewById(R.id.scratchCard1);
        Glide.with(PP_WelcomePrimeGift.this)
                .load(productImg)
                .placeholder(R.drawable.logo).error(R.drawable.logo).into(imgProduct);
        proName.setText(productName + "\n" + price);
        Log.d("pcode", pcode);
        linearExit.setOnClickListener(v -> {
            DirectPointAddPlaceOrder(pcode, "0");
            alertDialog.dismiss();
        });


        scratchCard1.setOnScratchListener(new ScratchCard.OnScratchListener() {
            @Override
            public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                if (visiblePercent > 0.7) {
                    linearProceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DirectPointAddPlaceOrder(pcode, "1");
                        }
                    }); }}});

       /* linearProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scratchCard1.setOnScratchListener(new ScratchCard.OnScratchListener() {
                    @Override
                    public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                        if (visiblePercent > 0.8) {
                            DirectPointAddPlaceOrder(pcode, "1");
                        } else {
                            Toast.makeText(getApplicationContext(), "Scratch your Card", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });*/
    }



    private void DirectPointAddPlaceOrder(String p_code, String IsWin) {
        JSONObject jsonObject = new JSONObject();
        Log.d("responseparam_prime", String.valueOf(jsonObject));
        try {
            jsonObject.put("UserId", UserId);
            jsonObject.put("ProductCode", p_code);
            jsonObject.put("IsWin", IsWin);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.Shoppit_WelcomePrimeAcceptorNot_New, jsonObject, response -> {
            try {
                Log.d("AcceptorNot_New", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status") == true) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                     finish();
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(this, response.getString("Message"), Toast.LENGTH_SHORT).show();
                    finish();
                    alertDialog.dismiss();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    @Override
    public void onBackPressed() {
       finish();
    }



    private void GameDetails_popup1(String getId1) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(PP_WelcomePrimeGift.this).inflate(R.layout.popup_gameoverview, viewGroup, false);

        TextView view_details_tv = dialogView.findViewById(R.id.view_details_tv);


        FloatingActionButton close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();
            }
        });
//        arrayList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(PP_WelcomePrimeGift.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "MainGameDetailsbyCat", new Response.Listener<String>() {
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
                    Toast.makeText(PP_WelcomePrimeGift.this, "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:" + error);
//                Toast.makeText(ShopIt_PrimePackageList.this, "No Record Found!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryId", getId1);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PP_WelcomePrimeGift.this);
        requestQueue.add(stringRequest);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PP_WelcomePrimeGift.this);
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

}