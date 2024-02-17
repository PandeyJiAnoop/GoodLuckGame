package com.akp.aonestar.PriceDropModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.GameSelection;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.GoodLuckModule.AdapterForBanner;
import com.akp.aonestar.GoodLuckModule.AnimationHelper;
import com.akp.aonestar.GoodLuckModule.BannerData;
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

public class PriceDropDashboard extends AppCompatActivity {
    RecyclerView recyclerCategory;
    CirclePageIndicator indicator;
    ViewPager pager;
    List<BannerData> bannerData = new ArrayList<>();
    private static int currentPage = 0;
    ImageView bid_img,bid_img1;
    EditText search_tv;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private RecyclerView cust_recyclerView;

    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_drop_dashboard);


        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();
        initViews();

        GetCategoryAPI();
        GetSubCategoryAPI();


        //        Floating layout call here
        PriceDropFloatingButtonGameDetails f_details = (PriceDropFloatingButtonGameDetails) findViewById(R.id.f_details);
        f_details.initFloating("PrizeDrop");

}

private void initViews() {
    cust_recyclerView=findViewById(R.id.chat_recyclerView);
    recyclerCategory = findViewById(R.id.recyclerCategory);
    search_tv = findViewById(R.id.search_tv);

    ImageView buynow_btn=findViewById(R.id.buynow_btn);
    buynow_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getApplicationContext(), GameSelection.class);
            startActivity(intent);
        }
    });

    search_tv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getApplicationContext(),PriceDropcategoryProductList.class);
            startActivity(intent);
        }
    });



//        *Baneer Code*
    indicator = (CirclePageIndicator) findViewById(R.id.indicator);
    pager = (ViewPager) findViewById(R.id.pager1);
//        *Baneer Code*


    getBanner();

    bid_img=findViewById(R.id.bid_img);
    bid_img1=findViewById(R.id.bid_img1);
    Next_Week_BidBannerAPI();
}


    public void getBanner() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_BannerList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String JsonInString = jsonObject.getString("Response");
                    bannerData = BannerData.createJsonInList(JsonInString);
                    pager.setAdapter(new AdapterForBanner(PriceDropDashboard.this, bannerData));
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
                Toast.makeText(PriceDropDashboard.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryName", "Main");
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropDashboard.this);
        requestQueue.add(stringRequest);

    }


    public void GetCategoryAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropDashboard.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+"PRIZEDROP_RootCategoryList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("CategoryName", jsonObject.getString("CategoryName"));
                            hashlist.put("Entryby", jsonObject.getString("Entryby"));
                            hashlist.put("EntryDate", jsonObject.getString("EntryDate"));
                            hashlist.put("CatImg", jsonObject.getString("CatImg"));
                            hashlist.put("PKID", jsonObject.getString("PKID"));
                            arrayList.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PriceDropDashboard .this, 2);
                        TaskEarningAdapter customerListAdapter = new TaskEarningAdapter(PriceDropDashboard.this, arrayList);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    } else {
                        Toast.makeText(PriceDropDashboard.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropDashboard.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropDashboard.this);
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
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_pricedropmaincat, viewGroup, false);
            TaskEarningAdapter.VH viewHolder = new TaskEarningAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskEarningAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            Glide.with(PriceDropDashboard.this)
                    .load(arrayList.get(i).get("CatImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayList.get(i).get("PKID").equalsIgnoreCase("1")){
                        Intent intent=new Intent(getApplicationContext(),PriceDropTrailProduct.class);
                        intent.putExtra("cat_id","1");
                        startActivity(intent);
                    }
                    else if (arrayList.get(i).get("PKID").equalsIgnoreCase("2")){
                        Intent intent=new Intent(getApplicationContext(),PriceDropApnaMart.class);
                        intent.putExtra("cat_id","2");
                        startActivity(intent);

                    }
                   else if (arrayList.get(i).get("PKID").equalsIgnoreCase("3")){
                        Intent intent=new Intent(getApplicationContext(),PriceDropProductContentChalange.class);
                        intent.putExtra("cat_id","3");
                        startActivity(intent);

                    }
                   else if (arrayList.get(i).get("PKID").equalsIgnoreCase("4")){
                        Intent intent=new Intent(getApplicationContext(),PriceDropBuyBid.class);
                        intent.putExtra("cat_id","4");
                        startActivity(intent);
                    }
                }
            });
        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            ImageView img;
            public VH(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
            }
        }
    }













    public void GetSubCategoryAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropDashboard.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+"PRIZEDROP_SubCategoryList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("CategoryName", jsonObject.getString("CategoryName"));
                            hashlist.put("Entryby", jsonObject.getString("Entryby"));
                            hashlist.put("EntryDate", jsonObject.getString("EntryDate"));
                            hashlist.put("CatImg", jsonObject.getString("CatImg"));
                            hashlist.put("PKID", jsonObject.getString("PKID"));
                            arrayList1.add(hashlist);
                        }

                        AdaptorCategory customerListAdapter = new AdaptorCategory(PriceDropDashboard.this, arrayList1);
                        recyclerCategory.setLayoutManager(new LinearLayoutManager(PriceDropDashboard.this, LinearLayoutManager.HORIZONTAL, false));
                        recyclerCategory.setAdapter(customerListAdapter);
                    } else {
                        Toast.makeText(PriceDropDashboard.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropDashboard.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropDashboard.this);
        requestQueue.add(stringRequest);

    }




    public class AdaptorCategory extends RecyclerView.Adapter<AdaptorCategory.VH> {
        Context context;
        List<HashMap<String,String>> arrayList1;
        public AdaptorCategory(Context context, List<HashMap<String,String>> arrayList1) {
            this.arrayList1=arrayList1;
            this.context=context;
        }

        @NonNull
        @Override
        public AdaptorCategory.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.category_layout, viewGroup, false);
            AdaptorCategory.VH viewHolder = new AdaptorCategory.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptorCategory.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            Glide.with(PriceDropDashboard.this).load(arrayList1.get(i).get("CatImg")).error(R.drawable.a_logo).into(vh.imageName);
            vh.tvCatName.setText(arrayList1.get(i).get("CategoryName"));

            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),PriceDropcategoryProductList.class);
                    intent.putExtra("cat_id",arrayList1.get(i).get("PKID"));
                    startActivity(intent);
                }
            });
        }



        @Override
        public int getItemCount() {
            return arrayList1.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            ImageView imageName;
            TextView tvCatName;
            public VH(@NonNull View itemView) {
                super(itemView);
                imageName = itemView.findViewById(R.id.imageName);
                tvCatName = itemView.findViewById(R.id.tvCatName);            }
        }
    }


    public void Next_Week_BidBannerAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+"PRIZEDROP_Main2BannerList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");

                    Glide.with(PriceDropDashboard.this).load(object.getString("Img1")).error(R.drawable.a_logo).into(bid_img);
                    Glide.with(PriceDropDashboard.this).load(object.getString("Img2")).error(R.drawable.a_logo).into(bid_img1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myTag", "message:"+error);
                Toast.makeText(PriceDropDashboard.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropDashboard.this);
        requestQueue.add(stringRequest);

    }
}