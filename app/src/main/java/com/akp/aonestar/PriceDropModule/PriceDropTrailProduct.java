package com.akp.aonestar.PriceDropModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.FloatingButtonGameDetails;
import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Basic.Oder_Summery_Details;
import com.akp.aonestar.GoodLuckModule.AnimationHelper;
import com.akp.aonestar.GoodLuckModule.QuizJackpot;
import com.akp.aonestar.PriceDropModule.Adaptors.AdaptorBigDealModel;
import com.akp.aonestar.PriceDropModule.Adaptors.SliderAdapterA;
import com.akp.aonestar.PriceDropModule.Models.BigDealModel;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceDropTrailProduct extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    RecyclerView cust_recyclerView,cust_recyclerView1;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList5 = new ArrayList<>();
    ImageView norecord_tv;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    ImageView norecord_tv1;
    private Dialog dialog,dialog1;
    int i=0;
    String getCatId;
    private String UserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_drop_trail_product);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        getCatId=getIntent().getStringExtra("cat_id");

        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();

        initViews();

        //        Floating layout call here
        PriceDropFloatingButtonGameDetails f_details = (PriceDropFloatingButtonGameDetails) findViewById(R.id.f_details);
        f_details.initFloating(getCatId);
    }

    private void initViews() {
        viewPager2 = findViewById(R.id.viewPagerImageSlider);
        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        cust_recyclerView1 = findViewById(R.id.cust_recyclerView1);
        norecord_tv = findViewById(R.id.norecord_tv);
        norecord_tv1 = findViewById(R.id.norecord_tv1);



//        List<SliderItems> sliderItems = new ArrayList<>();
//        sliderItems.add(new SliderItems(R.drawable.slider_img));
//        sliderItems.add(new SliderItems(R.drawable.slider_img));
//        sliderItems.add(new SliderItems(R.drawable.slider_img));
//        sliderItems.add(new SliderItems(R.drawable.slider_img));
//        sliderItems.add(new SliderItems(R.drawable.slider_img));
//
//        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));
//
//        viewPager2.setClipToPadding(false);
//        viewPager2.setClipChildren(false);
//        viewPager2.setOffscreenPageLimit(3);
//        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
//
//        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
//        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
//        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                float r = 1 - Math.abs(position);
//                page.setScaleY(0.85f + r * 0.15f);
//            }
//        });
//
//        viewPager2.setPageTransformer(compositePageTransformer);
//
//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                sliderHandler.removeCallbacks(sliderRunnable);
//                sliderHandler.postDelayed(sliderRunnable, 2000); // slide duration 2 seconds
//            }
//        });



        ImageView buynow_btn=findViewById(R.id.buynow_btn);
        buynow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getHistory();
        getHistory1();
        getTopBannerAPI();

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








    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropTrailProduct.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_BiggestDealProduct", new Response.Listener<String>() {
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
                            hashlist.put("ProductNewSaleRate", jsonObject.getString("ProductNewSaleRate"));
                            arrayList5.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PriceDropTrailProduct .this, 1,GridLayoutManager.HORIZONTAL, false);
                        TaskEarningAdapter customerListAdapter = new TaskEarningAdapter(PriceDropTrailProduct.this, arrayList5);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(PriceDropTrailProduct.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropTrailProduct.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropTrailProduct.this);
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
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_pricedrop_biggestdeal, viewGroup, false);
            TaskEarningAdapter.VH viewHolder = new TaskEarningAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskEarningAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(PriceDropTrailProduct.this)
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
        dialog = new Dialog(PriceDropTrailProduct.this);
        dialog.setContentView(R.layout.dynamic_productdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        AppCompatButton Cancel = dialog.findViewById(R.id.btn_cancel);
        AppCompatButton buy_btn = dialog.findViewById(R.id.buy_btn);
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


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropTrailProduct.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_ProductDeatilsbyId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ProductDeatilsbyId",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String planCode = jsonObject2.getString("PlanCode");
                        String plan_name = jsonObject2.getString("Plan_Name");
                        String productMrp = jsonObject2.getString("Mrp");
                        String saleRate = jsonObject2.getString("SaleRate");
                        String productImg = jsonObject2.getString("ProductImg");
                        String discount = jsonObject2.getString("Discount");



                        String productSizeUnit = jsonObject2.getString("ProductSizeUnit");
                        String productSizeValue = jsonObject2.getString("ProductSizeValue");

                        String productColor = jsonObject2.getString("ProductColor");
                        String productLength = jsonObject2.getString("ProductLength");
                        String isStock = jsonObject2.getString("IsStock");

                        buy_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(), PriceDrop_Oder_Summery_Details.class);
                                intent.putExtra("p_code",planCode);
                                intent.putExtra("p_userid",UserId);
                                intent.putExtra("p_disamount",discount);
                                intent.putExtra("p_price",saleRate);
                                intent.putExtra("p_name",plan_name);
                                intent.putExtra("p_img",productImg);
                                intent.putExtra("class_name","trail_product");
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });

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
                        Glide.with(PriceDropTrailProduct.this)
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
                        Toast.makeText(PriceDropTrailProduct.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropTrailProduct.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropTrailProduct.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails
















    public void getHistory1() {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropTrailProduct.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_TodayDealProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                            hashlist.put("ProductNewSaleRate", jsonObject.getString("ProductNewSaleRate"));
                            arrayList.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PriceDropTrailProduct .this, 1,GridLayoutManager.HORIZONTAL, false);
//                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PriceDropTrailProduct .this, 3);
                        TaskEarningAdapter1 customerListAdapter = new TaskEarningAdapter1(PriceDropTrailProduct.this, arrayList);
                        cust_recyclerView1.setLayoutManager(gridLayoutManager);
                        cust_recyclerView1.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv1.setVisibility(View.VISIBLE);
//                        Toast.makeText(PriceDropTrailProduct.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropTrailProduct.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropTrailProduct.this);
        requestQueue.add(stringRequest);

    }

    public class TaskEarningAdapter1 extends RecyclerView.Adapter<TaskEarningAdapter1.VH> {
        Context context;
        List<HashMap<String,String>> arrayList2;
        public TaskEarningAdapter1(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList2=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public TaskEarningAdapter1.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_productdetails_pricedrop_todayhotdeal, viewGroup, false);
            TaskEarningAdapter1.VH viewHolder = new TaskEarningAdapter1.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskEarningAdapter1.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList2.get(i).get("ProductCode"));
            vh.code.setText(arrayList2.get(i).get("ProductName"));
            Glide.with(PriceDropTrailProduct.this)
                    .load(arrayList2.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup1(arrayList2.get(i).get("ProductCode"));
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
        dialog1 = new Dialog(PriceDropTrailProduct.this);
        dialog1.setContentView(R.layout.dynamic_productdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog1.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.setCancelable(false); //Optional
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        AppCompatButton Cancel = dialog1.findViewById(R.id.btn_cancel);
        AppCompatButton buy_btn = dialog1.findViewById(R.id.buy_btn);
        TextView name = dialog1.findViewById(R.id.name);
        TextView amount = dialog1.findViewById(R.id.amount_tv);
        TextView discount_tv = dialog1.findViewById(R.id.discount_tv);
        TextView orderamount_tv = dialog1.findViewById(R.id.orderamount_tv);
        ImageView imageView = dialog1.findViewById(R.id.imageView);
        TextView p_code1 = dialog1.findViewById(R.id.p_code);
        TextView ProductSizeUnittv = dialog1.findViewById(R.id.ProductSizeUnittv);
        TextView ProductSizeValuetv = dialog1.findViewById(R.id.ProductSizeValuetv);

        TextView ProductColorTv = dialog1.findViewById(R.id.ProductColorTv);
        TextView ProductLengthTv = dialog1.findViewById(R.id.ProductLengthTv);
        ImageView outofstok_img = dialog1.findViewById(R.id.outofstok_img);

        TextView ServiceChargeTv = dialog1.findViewById(R.id.ServiceChargeTv);
        TextView DeliveryFeeTv = dialog1.findViewById(R.id.DeliveryFeeTv);

        TextView productdetails_tv = dialog1.findViewById(R.id.productdetails_tv);
        LinearLayout view_more_ll = dialog1.findViewById(R.id.view_more_ll);
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
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        final ProgressDialog progressDialog = new ProgressDialog(PriceDropTrailProduct.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_ProductDeatilsbyId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ProductDeatilsbyId",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String plan_name = jsonObject2.getString("Plan_Name");
                        String planCode1 = jsonObject2.getString("PlanCode");
                        String productMrp = jsonObject2.getString("Mrp");
                        String saleRate = jsonObject2.getString("SaleRate");
                        String productImg = jsonObject2.getString("ProductImg");
                        String discount = jsonObject2.getString("Discount");

                        String productSizeUnit = jsonObject2.getString("ProductSizeUnit");
                        String productSizeValue = jsonObject2.getString("ProductSizeValue");

                        String productColor = jsonObject2.getString("ProductColor");
                        String productLength = jsonObject2.getString("ProductLength");
                        String isStock = jsonObject2.getString("IsStock");

                        buy_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(), PriceDrop_Oder_Summery_Details.class);
                                intent.putExtra("p_code",planCode1);
                                intent.putExtra("p_userid",UserId);
                                intent.putExtra("p_disamount",discount);
                                intent.putExtra("p_price",saleRate);
                                intent.putExtra("p_name",plan_name);
                                intent.putExtra("p_img",productImg);
                                intent.putExtra("class_name","trail_product");
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });



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
                        p_code1.setText(planCode1);
                        amount.setText("\u20B9 "+productMrp);
                        discount_tv.setText(discount+" %");
                        orderamount_tv.setText("\u20B9 "+saleRate);
                        Glide.with(PriceDropTrailProduct.this)
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
                        Toast.makeText(PriceDropTrailProduct.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropTrailProduct.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropTrailProduct.this);
        requestQueue.add(stringRequest);
        dialog1.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails








    public void getTopBannerAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropTrailProduct.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_TopTrialProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res",response);
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
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("ProductMRP", jsonObject.getString("ProductMRP"));
                            hashlist.put("ProductNewSaleRate", jsonObject.getString("ProductNewSaleRate"));
                            arrayList1.add(hashlist);

                        }

                        SliderAdapterA customerListAdapter = new SliderAdapterA(PriceDropTrailProduct.this, arrayList1);
                        viewPager2.setAdapter(customerListAdapter);

//                        viewPager2.setAdapter(new SliderAdapter(arrayList1, viewPager2));

                        viewPager2.setClipToPadding(false);
                        viewPager2.setClipChildren(false);
                        viewPager2.setOffscreenPageLimit(3);
                        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                            @Override
                            public void transformPage(@NonNull View page, float position) {
                                float r = 1 - Math.abs(position);
                                page.setScaleY(0.85f + r * 0.15f);
                            }
                        });

                        viewPager2.setPageTransformer(compositePageTransformer);

                        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                            @Override
                            public void onPageSelected(int position) {
                                super.onPageSelected(position);
                                sliderHandler.removeCallbacks(sliderRunnable);
                                sliderHandler.postDelayed(sliderRunnable, 5000); // slide duration 2 seconds
                            }
                        });


//                            name.setText(jsonObject.getString("ProductName")+"("+jsonObject.getString("ProductCode")+")");
//                            code.setText("Rs. "+jsonObject.getString("ProductNewSaleRate"));
//                            Glide.with(PriceDropBuyBid.this).load(object.getString("ProductImg")).error(R.drawable.a_logo).into(img);

                    } else {
                        Toast.makeText(PriceDropTrailProduct.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropTrailProduct.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropTrailProduct.this);
        requestQueue.add(stringRequest);

    }





}