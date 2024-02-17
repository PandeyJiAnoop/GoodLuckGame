package com.akp.aonestar.New_ShopIt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.BottomOnClick;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.GoodLuckModule.AdapterForBanner;
import com.akp.aonestar.GoodLuckModule.AnimationHelper;
import com.akp.aonestar.GoodLuckModule.BannerData;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class PP_BasicSwingProduct extends AppCompatActivity {

    String UserId;
    CirclePageIndicator indicator;
    ViewPager pager;
    List<BannerData> bannerData = new ArrayList<>();
    private static int currentPage = 0;
    RecyclerView all_bets_rv;
    ArrayList<HashMap<String, String>> all_bets_arrayList1 = new ArrayList<>();
    private RequestQueue requestQueue1,requestQueuesell,requestQueuebuy;// Declare
    Runnable mTicker5;
    WebView webview;
//    ImageView details;
    private AlertDialog alertDialog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pp_basic_swing_product);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");

//        details = (ImageView) findViewById(R.id.details);

        ImageView smartwallet_img=findViewById(R.id.smartwallet_img);
        smartwallet_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), WalletHistory.class);
                startActivity(intent);
            }
        });

//        details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GameDetails_popup1("PRIME SWING TRADING");
//            }
//        });



        all_bets_rv=findViewById(R.id.all_bets_rv);
//        *Baneer Code*
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager1);
        getBanner();
        //        header layout call here
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();
        BottomOnClick bottom = (BottomOnClick) findViewById(R.id.bottom);
        bottom.initHeader();

        LinearLayout social_ll=findViewById(R.id.social_ll);
        social_ll.setVisibility(View.GONE);
        ProductListAPI();
//        auto refersh layout for gameId
        final Handler mHandler5 = new Handler();
        mTicker5 = new Runnable() {
            @Override
            public void run() {
                mHandler5.postDelayed(mTicker5, 30000);
                all_bets_arrayList1.clear();
                ProductListAPI();
            }};
        mHandler5.postDelayed(mTicker5, 30000);

        webview=findViewById(R.id.webview);
        webview.loadUrl("https://www.tradingview.com/chart/");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
    }

    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(PP_BasicSwingProduct.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "BannerList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String JsonInString = jsonObject.getString("Response");
                    bannerData = BannerData.createJsonInList(JsonInString);
                    pager.setAdapter(new AdapterForBanner(PP_BasicSwingProduct.this, bannerData));
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
                        }};
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }}, 5000, 3000);
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
                        } });
                } catch (JSONException e) {
                    e.printStackTrace();
                }}}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(PP_BasicSwingProduct.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }}) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ID", "3");
                return params;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PP_BasicSwingProduct.this);
        requestQueue.add(stringRequest);

    }

    //    mybets list on dashboard
    public void ProductListAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GetProductTradingProductList_NEW", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { Log.d("GetProductTrading","sadaf"+response);
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("id", jsonObject.getString("id"));
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("BuyRate", jsonObject.getString("BuyRate"));
                            hashlist.put("SellRate", jsonObject.getString("SellRate"));
                            hashlist.put("RespoCode", jsonObject.getString("RespoCode"));
                            hashlist.put("IsBuy", jsonObject.getString("IsBuy"));
                            all_bets_arrayList1.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PP_BasicSwingProduct.this, 1);
                        PP_BasicSwingProduct.AllbetsListAdapter customerListAdapter = new PP_BasicSwingProduct.AllbetsListAdapter(PP_BasicSwingProduct.this, all_bets_arrayList1);
                        all_bets_rv.setLayoutManager(gridLayoutManager);
                        all_bets_rv.setAdapter(customerListAdapter);
                    } else {
                    }
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
                params.put("UserId", UserId);
                params.put("GameCategory", "Prime");
                Log.d("MyBetReportOnGame","sadaf"+params);
                return params;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (requestQueue1 == null) {
            requestQueue1 = Volley.newRequestQueue(PP_BasicSwingProduct.this);
            requestQueue1.add(stringRequest);
        } else {
            requestQueue1.add(stringRequest);
        }}
    public class AllbetsListAdapter extends RecyclerView.Adapter<PP_BasicSwingProduct.AllbetsListAdapter.VH> {
        Context context;
        List<HashMap<String,String>> allarrayList;
        public AllbetsListAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.allarrayList=arrayList;
            this.context=context;
        }
        @NonNull
        @Override
        public PP_BasicSwingProduct.AllbetsListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_swingproduct_list, viewGroup, false);
            PP_BasicSwingProduct.AllbetsListAdapter.VH viewHolder = new PP_BasicSwingProduct.AllbetsListAdapter.VH(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull PP_BasicSwingProduct.AllbetsListAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.tv1.setText(allarrayList.get(i).get("ProductName"));
            vh.tv2.setText(allarrayList.get(i).get("BuyRate"));
            vh.tv3.setText(allarrayList.get(i).get("SellRate"));
            Animation an = AnimationUtils.loadAnimation(PP_BasicSwingProduct.this, R.anim.blink);
            vh.tv2.startAnimation(an);
            vh.tv3.startAnimation(an);

//            if (!UserId.equalsIgnoreCase("") && !(itemList.get(position).getIsPlan().equalsIgnoreCase("0"))){
            if (!UserId.equalsIgnoreCase("")){
                if (allarrayList.get(i).get("IsBuy").equalsIgnoreCase("NO")){
                    vh.tv3.setClickable(false);
                    vh.tv3.setFocusable(false);
                    vh.tv3.setBackgroundResource(R.color.redone);
                    vh.tv2.setClickable(true);
                    vh.tv2.setFocusable(true);
                }
                else if (allarrayList.get(i).get("IsBuy").equalsIgnoreCase("Yes")){
                    vh.tv2.setClickable(false);
                    vh.tv2.setFocusable(false);
                    vh.tv2.setBackgroundResource(R.color.red);
                    vh.tv3.setClickable(true);
                    vh.tv3.setFocusable(true);

                }
                vh.tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!UserId.equalsIgnoreCase("")){
                            vh.tv2.setBackgroundResource(R.color.green);
                            vh.tv2.setTextColor(Color.WHITE);
                            TradingProductBuyAPI(allarrayList.get(i).get("BuyRate"),allarrayList.get(i).get("ProductCode"));
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Login First!",Toast.LENGTH_SHORT).show();
                        }
                    }});
                vh.tv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!UserId.equalsIgnoreCase("")){
                            vh.tv3.setBackgroundResource(R.color.redone);
                            vh.tv3.setTextColor(Color.WHITE);
                            TradingProductSellAPI(allarrayList.get(i).get("SellRate"),allarrayList.get(i).get("ProductCode"),allarrayList.get(i).get("RespoCode"));

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Login First!",Toast.LENGTH_SHORT).show();
                        } }});
            }
            else {

                vh.tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UserId.equalsIgnoreCase("")){
                            Toast.makeText(getApplicationContext(),"Login and recharge smart wallet and earn more.",Toast.LENGTH_SHORT).show();
                        }

                    }});
                vh.tv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UserId.equalsIgnoreCase("")){
                            Toast.makeText(getApplicationContext(),"Login and recharge smart wallet and earn more.",Toast.LENGTH_SHORT).show();
                        }
                    }});


//                Toast.makeText(getApplicationContext(),"Login First!",Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),"Please Buy A Plan After That You Can Avail many Benefits!",Toast.LENGTH_SHORT).show();
            }



        }

        @Override
        public int getItemCount() {
            return allarrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView tv1,tv2,tv3;
            public VH(@NonNull View itemView) {
                super(itemView);
                tv1 = itemView.findViewById(R.id.tv1);
                tv2 = itemView.findViewById(R.id.tv2);
                tv3 = itemView.findViewById(R.id.tv3);
            }}}



    public void TradingProductBuyAPI(String buyrate,String procode) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"TradingProductBuy", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { Log.d("TradingProductBuy","sadaf"+response);
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            Toast.makeText(getApplicationContext(),jsonObject.getString("Msg"),Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        }
                    } else {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_SHORT).show();

                    }
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
                params.put("UserId", UserId);
                params.put("BuyRate", buyrate);
                params.put("ProductCode", procode);
                params.put("GameCategory", "Prime");
                Log.d("TradingProductbuy","sadaf"+params);
                return params;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (requestQueuebuy == null) {
            requestQueuebuy = Volley.newRequestQueue(PP_BasicSwingProduct.this);
            requestQueuebuy.add(stringRequest);
        } else {
            requestQueuebuy.add(stringRequest);
        }}


    public void TradingProductSellAPI(String sellrate,String procode,String rescode) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"TradingProductSell", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) { Log.d("TradingProductSell","sadaf"+response);
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            Toast.makeText(getApplicationContext(),jsonObject.getString("Msg"),Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        }
                    } else {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_SHORT).show();

                    }
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
                params.put("UserId", UserId);
                params.put("SellRate", sellrate);
                params.put("ProductCode", procode);
                params.put("RespoCode", rescode);
                params.put("GameCategory", "Prime");
                Log.d("TradingProductSell","sadaf"+params);
                return params;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if (requestQueuesell == null) {
            requestQueuesell = Volley.newRequestQueue(PP_BasicSwingProduct.this);
            requestQueuesell.add(stringRequest);
        } else {
            requestQueuesell.add(stringRequest);
        }}


    private void GameDetails_popup1(String getId1) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(PP_BasicSwingProduct.this).inflate(R.layout.popup_gameoverview, viewGroup, false);

        TextView view_details_tv = dialogView.findViewById(R.id.view_details_tv);


        FloatingActionButton close = dialogView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();
            }
        });
//        arrayList.clear();
        final ProgressDialog progressDialog = new ProgressDialog(PP_BasicSwingProduct.this);
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
                    Toast.makeText(PP_BasicSwingProduct.this, "No Record Found!", Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PP_BasicSwingProduct.this);
        requestQueue.add(stringRequest);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PP_BasicSwingProduct.this);
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