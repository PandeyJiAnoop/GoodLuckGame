package com.akp.aonestar.PriceDropModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.FloatingButtonGameDetails;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.GoodLuckModule.AdapterForBanner;
import com.akp.aonestar.GoodLuckModule.BannerData;
import com.akp.aonestar.PriceDropModule.Adaptors.SliderAdapter;
import com.akp.aonestar.PriceDropModule.Models.SliderItems;
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

public class PriceDropBuyBid extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    ImageView bid_img,bid_img1;
    String getCatId;

    CirclePageIndicator indicator;
    ViewPager pager;
    List<BannerData> bannerData = new ArrayList<>();
    private static int currentPage = 0;

    ImageView img,img_one,img_three;
    TextView name,code,amount,name_one,code_one,name_three,code_three;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();

    RelativeLayout bid_now_rl,bid_winner_rl;
    private AlertDialog alertDialog;
    String UserId;
    LinearLayout ll_topbanner;
    int k=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_drop_buy_bid);
        SharedPreferences sharedPreferences =getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        getCatId=getIntent().getStringExtra("cat_id");
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();


//        *Baneer Code*
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager1);

        img= findViewById(R.id.img);
        name= findViewById(R.id.name);
        code= findViewById(R.id.code);
        amount=findViewById(R.id.amount);

        ll_topbanner=findViewById(R.id.ll_topbanner);

        name_one= findViewById(R.id.name_one);
        code_one= findViewById(R.id.code_one);
        img_one= findViewById(R.id.img_one);
        name_three= findViewById(R.id.name_three);
        code_three= findViewById(R.id.code_three);
        img_three= findViewById(R.id.img_three);



        initViews();
        getBanner();
        getTopBannerAPI();

        //        Floating layout call here
        PriceDropFloatingButtonGameDetails f_details = (PriceDropFloatingButtonGameDetails) findViewById(R.id.f_details);
        f_details.initFloating(getCatId);
    }

    private void initViews() {
        viewPager2 = findViewById(R.id.viewPagerImageSlider);


//        List<SliderItems> sliderItems = new ArrayList<>();
//        sliderItems.add(new SliderItems(R.drawable.slider_img));
//        sliderItems.add(new SliderItems(R.drawable.slider_img));
//        sliderItems.add(new SliderItems(R.drawable.slider_img));
//        sliderItems.add(new SliderItems(R.drawable.slider_img));
//        sliderItems.add(new SliderItems(R.drawable.slider_img));




        ImageView buynow_btn=findViewById(R.id.buynow_btn);
        buynow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        bid_img=findViewById(R.id.bid_img);
        bid_img1=findViewById(R.id.bid_img1);
        Next_Week_BidBannerAPI();

        bid_now_rl= findViewById(R.id.bid_now_rl);
        bid_winner_rl= findViewById(R.id.bid_winner_rl);

        bid_now_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppBidpopup();
            }
        });

        bid_winner_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),BidWinnerList.class);
                startActivity(intent);

            }
        });

    }



    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }

    public void Next_Week_BidBannerAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+"PRIZEDROP_BidBannerList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");

                    Glide.with(PriceDropBuyBid.this).load(object.getString("Img1")).error(R.drawable.a_logo).into(bid_img);
                    Glide.with(PriceDropBuyBid.this).load(object.getString("Img2")).error(R.drawable.a_logo).into(bid_img1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myTag", "message:"+error);
                Toast.makeText(PriceDropBuyBid.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropBuyBid.this);
        requestQueue.add(stringRequest);

    }


    public void getBanner() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_NewLaunchBannerList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String JsonInString = jsonObject.getString("Response");
                    bannerData = BannerData.createJsonInList(JsonInString);
                    pager.setAdapter(new AdapterForBanner(PriceDropBuyBid.this, bannerData));
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PriceDropBuyBid.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryName", "");
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropBuyBid.this);
        requestQueue.add(stringRequest);

    }











    public void getTopBannerAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropBuyBid.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_TopProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("PRIZEDROP_TopProduct",response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
//                        ll_topbanner.setVisibility(View.VISIBLE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);

                            name.setText(jsonObject.getString("ProductName")+"("+jsonObject.getString("ProductCode")+")");
                            code.setText("Product MRP \u20B9"+jsonObject.getString("ProductMRP"));
                            Glide.with(PriceDropBuyBid.this).load(jsonObject.getString("ProductImg")).error(R.drawable.a_logo).into(img);

                            name_one.setText(jsonObject.getString("ProductName")+"("+jsonObject.getString("ProductCode")+")");
                            code_one.setText("Product MRP \u20B9"+jsonObject.getString("ProductMRP"));
                            Glide.with(PriceDropBuyBid.this).load(jsonObject.getString("ProductImg")).error(R.drawable.a_logo).into(img_one);

                            name_three.setText(jsonObject.getString("ProductName")+"("+jsonObject.getString("ProductCode")+")");
                            code_three.setText("Product MRP \u20B9"+jsonObject.getString("ProductMRP"));
                            Glide.with(PriceDropBuyBid.this).load(jsonObject.getString("ProductImg")).error(R.drawable.a_logo).into(img_three);
                            amount.setText(("\u20B9"+jsonObject.getString("MinAmount")+" - \u20B9"+jsonObject.getString("MaxAmount")));



                            img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ViewGroup viewGroup =v.findViewById(android.R.id.content);
                                    View dialogView = LayoutInflater.from(PriceDropBuyBid.this).inflate(R.layout.bid_withdrawpopup, viewGroup, false);
                                    final AppCompatButton Submit_btn = (AppCompatButton) dialogView.findViewById(R.id.Submit_btn);
                                    EditText rupee_et=(EditText)dialogView.findViewById(R.id.rupee_et);
                                    ImageView proimg=(ImageView)dialogView.findViewById(R.id.proimg);
                                    TextView name_tv=(TextView)dialogView.findViewById(R.id.name_tv);
                                    TextView code_tv=(TextView)dialogView.findViewById(R.id.code_tv);

                                    TextView amount_tv=(TextView)dialogView.findViewById(R.id.amount_tv);
                                    TextView orderamount_tv=(TextView)dialogView.findViewById(R.id.orderamount_tv);

                                    TextView productdetails_tv1 = dialogView.findViewById(R.id.productdetails_tv);
                                    LinearLayout view_more_ll = dialogView.findViewById(R.id.view_more_ll);
                                    view_more_ll.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (k == 0) {
                                                productdetails_tv1.setVisibility(View.VISIBLE);
                                                k++;
                                            } else if (k== 1) {
                                                productdetails_tv1.setVisibility(View.GONE);
                                               k = 0;
                                            }
                                        }
                                    });

                                    try {
                                        productdetails_tv1.setText(jsonObject.getString("ProductDes"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        amount_tv.setText("\u20B9 "+jsonObject.getString("ProductMRP"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        orderamount_tv.setText(("\u20B9 "+jsonObject.getString("MinAmount")+" - \u20B9 "+jsonObject.getString("MaxAmount")));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        name_tv.setText(jsonObject.getString("ProductName"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        code_tv.setText(jsonObject.getString("ProductCode"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        Glide.with(PriceDropBuyBid.this).load(jsonObject.getString("ProductImg")).error(R.drawable.a_logo).into(proimg);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
//                                    try {
//                                        rupee_et.setFilters(new InputFilter[]{ new InputFilterMinMax(jsonObject.getString("MinAmount"), jsonObject.getString("MaxAmount"))});
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
                                    Submit_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (rupee_et.getText().toString().equalsIgnoreCase("")) {
                                                rupee_et.setError("Fields can't be blank!");
                                                rupee_et.requestFocus();
                                            } else {
                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "PRIZEDROP_BIDPlayNow", new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.d("PRIZEDROP_BIDPlayNow",response);
                                                        try {
                                                            JSONObject object = new JSONObject(response);
                                                            if (object.getString("Status").equalsIgnoreCase("true")) {
                                                                JSONArray jsonArray = object.getJSONArray("Response");
                                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                                                    Toast.makeText(PriceDropBuyBid.this, jsonObject2.getString("Msg"), Toast.LENGTH_LONG).show();
                                                                    alertDialog.dismiss();
                                                                }
                                                            }
                                                            else {
                                                                Toast.makeText(PriceDropBuyBid.this, object.getString("Message"), Toast.LENGTH_LONG).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        alertDialog.dismiss();
                                                        Toast.makeText(PriceDropBuyBid.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
                                                    }
                                                }) {
                                                    @Override
                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                        HashMap<String, String> params = new HashMap<>();
                                                        params.put("Amount", rupee_et.getText().toString());
                                                        params.put("UserId", UserId);
                                                        try {
                                                            params.put("ProductCode",jsonObject.getString("ProductCode"));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            params.put("BIDID", jsonObject.getString("BIDID"));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        return params;
                                                    }
                                                };
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                RequestQueue requestQueue = Volley.newRequestQueue(PriceDropBuyBid.this);
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    });
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PriceDropBuyBid.this);
                                    builder.setView(dialogView);
                                    alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            });

//
//                            HashMap<String, String> hashlist = new HashMap();
//                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
//                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
//                            hashlist.put("MinAmount", jsonObject.getString("MinAmount"));
//                            hashlist.put("MaxAmount", jsonObject.getString("MaxAmount"));
//                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
//                            hashlist.put("BIDID", jsonObject.getString("BIDID"));
//                            hashlist.put("ProductMRP", jsonObject.getString("ProductMRP"));
//                            arrayList1.add(hashlist);
//                            amount.setText(("\u20B9"+jsonObject.getString("MinAmount")+" - \u20B9"+jsonObject.getString("MaxAmount")));

                        }

//                        SliderAdapter customerListAdapter = new SliderAdapter(PriceDropBuyBid.this, arrayList1);
//                        viewPager2.setAdapter(customerListAdapter);
//
////                        viewPager2.setAdapter(new SliderAdapter(arrayList1, viewPager2));
//
//                        viewPager2.setClipToPadding(false);
//                        viewPager2.setClipChildren(false);
//                        viewPager2.setOffscreenPageLimit(3);
//                        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//
//                        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
//                        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
//                        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
//                            @Override
//                            public void transformPage(@NonNull View page, float position) {
//                                float r = 1 - Math.abs(position);
//                                page.setScaleY(0.85f + r * 0.15f);
//                            }
//                        });
//
//                        viewPager2.setPageTransformer(compositePageTransformer);
//
//                        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//                            @Override
//                            public void onPageSelected(int position) {
//                                super.onPageSelected(position);
//                                sliderHandler.removeCallbacks(sliderRunnable);
//                                sliderHandler.postDelayed(sliderRunnable, 2000); // slide duration 2 seconds
//                            }
//                        });



                    } else {
                        ll_topbanner.setVisibility(View.GONE);
//                        Toast.makeText(PriceDropBuyBid.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropBuyBid.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("CategoryId",getCatId);
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropBuyBid.this);
        requestQueue.add(stringRequest);

    }

}
