package com.akp.aonestar.PriceDropModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Basic.LoginScreenAkp;
import com.akp.aonestar.Basic.Oder_Summery_Details;
import com.akp.aonestar.GoodLuckModule.AdapterForBanner;
import com.akp.aonestar.GoodLuckModule.AnimationHelper;
import com.akp.aonestar.GoodLuckModule.BannerData;
import com.akp.aonestar.GoodLuckModule.ChooseDiscountProduct;
import com.akp.aonestar.GoodLuckModule.OfferAdapter;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PriceDropApnaMart extends AppCompatActivity {
    RecyclerView recyclerView;
    // Using ArrayList to store images data
    ArrayList images = new ArrayList<>(Arrays.asList(R.drawable.question, R.drawable.p_a, R.drawable.question));
    private int position;
    HorizontalScrollView horizontalScrollView;
    private boolean scrollingLeft = false;

    LinearLayout plan_ll;
    View view;

    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    String UserId;
    //    SwipeRefreshLayout srl_refresh;
    ImageView sliding_menu;
    ImageView norecord_tv;

    CirclePageIndicator indicator;
    ViewPager pager;
    List<BannerData> bannerData = new ArrayList<>();
    private static int currentPage = 0;
    private Dialog dialog;

    TextView product_code,offer_one,offer_two,offer_three,offer_four,offer_five,offer_six,product_name_tv;
    ImageView product_img;

    AppCompatButton btn;
    private LinearLayout dynamic_ll;
    private ArrayList<OfferAdapter> offer_dataArray;
    private Dialog alertDialog5;
    private String getamount;
    int i=0;

    int currentPage1 =0; //compute scrolling amount


    String getCatId;

    private static final int SPLASH_TIME_OUT = 4000;
    int progresscount = 0;
    private AlertDialog alertDialog6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_drop_apna_mart);
        getCatId=getIntent().getStringExtra("cat_id");
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");

//        srl_refresh=findViewById(R.id.srl_refresh);
        norecord_tv=findViewById(R.id.norecord_tv);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);


        //        Floating layout call here
        PriceDropFloatingButtonGameDetails f_details = (PriceDropFloatingButtonGameDetails) findViewById(R.id.f_details);
        f_details.initFloating(getCatId);

        product_code=findViewById(R.id.product_code);
        product_name_tv=findViewById(R.id.product_name_tv);
        product_img=findViewById(R.id.product_img);
        offer_one=findViewById(R.id.offer_one);
        offer_two=findViewById(R.id.offer_two);
        offer_three=findViewById(R.id.offer_three);
        offer_four=findViewById(R.id.offer_four);
        offer_five=findViewById(R.id.offer_five);
        offer_six=findViewById(R.id.offer_six);
        btn=findViewById(R.id.btn);
        dynamic_ll = (LinearLayout) findViewById(R.id.dynamic_ll);
        getOfferListAPI();

        horizontalScrollView=findViewById(R.id.horizontalScrollView);
        TextView person_name=findViewById(R.id.person_name);
        person_name.setSelected(true);
        ImageView buynow_btn=findViewById(R.id.buynow_btn);
        buynow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



//        *Baneer Code*
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager1);
        getBanner();

        //        header layout call here
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserId.equalsIgnoreCase("")){
                    Intent intent=new Intent(getApplicationContext(), LoginScreenAkp.class);
                    startActivity(intent);
                }
                else {
                    if (product_code.getText().toString().equalsIgnoreCase("NA")){
                        Toast.makeText(getApplicationContext(),"Select Product Firstly !",Toast.LENGTH_LONG).show();
                    }
                    else {
                        PlayNowButtonAPI();
                    }
                }



            }
        });

        Timer timer = new Timer("horizontalScrollViewTimer");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (scrollingLeft) {
                            if (horizontalScrollView.getScrollX() == 0) {
                                horizontalScrollView.smoothScrollBy(5, 0);
                                scrollingLeft = false;
                            } else {
                                horizontalScrollView.smoothScrollBy(-5, 0);
                            }
                        } else {
                            if (horizontalScrollView.canScrollHorizontally(View.FOCUS_RIGHT)) {
                                horizontalScrollView.smoothScrollBy(5, 0);
                            } else {
                                horizontalScrollView.smoothScrollBy(-5, 0);
                                scrollingLeft = true;
                            }
                        }
                    }
                });
            }
        }, 1000, 100);
//        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (NetworkConnectionHelper.isOnline(PriceDropApnaMart.this)) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());
//                            overridePendingTransition(0, 0);
//                            srl_refresh.setRefreshing(false);
//                        }
//                    }, 2000);
//                } else {
//                    Toast.makeText(PriceDropApnaMart.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        getHistory();



//        // Getting reference of recyclerView
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        // Setting the layout as Staggered Grid for vertical orientation
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
//        gridLayoutManager.setSpanSizeLookup(
//                new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        return (position == 0 ? 3 : 1);
//                    }
//                });
//
//
//
//        recyclerView.setLayoutManager(gridLayoutManager);
//        // Sending reference and data to Adapter
//        AdapterAkp adapter1 = new AdapterAkp(ChoosePriceDropApnaMart.this, images);
//        // Setting Adapter to RecyclerView
//        recyclerView.setAdapter(adapter1);
    }



    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropApnaMart.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_ProductbyRootCategory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                            arrayList1.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PriceDropApnaMart .this, 3);
                        PriceDropApnaMart.TaskEarningAdapter customerListAdapter = new PriceDropApnaMart.TaskEarningAdapter(PriceDropApnaMart.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(PriceDropApnaMart.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropApnaMart.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropApnaMart.this);
        requestQueue.add(stringRequest);

    }




    public class TaskEarningAdapter extends RecyclerView.Adapter<PriceDropApnaMart.TaskEarningAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public PriceDropApnaMart.TaskEarningAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product, viewGroup, false);
            PriceDropApnaMart.TaskEarningAdapter.VH viewHolder = new PriceDropApnaMart.TaskEarningAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PriceDropApnaMart.TaskEarningAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(PriceDropApnaMart.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getamount=arrayList.get(i).get("ProductNewSaleRate");
                    product_code.setText(arrayList.get(i).get("ProductName"));
                    product_name_tv.setText(arrayList.get(i).get("ProductCode"));
                    Glide.with(PriceDropApnaMart.this)
                            .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                            .into(product_img);
                    ProductDetailsPopup(arrayList.get(i).get("ProductCode"));

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

    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropApnaMart.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"BannerList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String JsonInString = jsonObject.getString("Response");
                    bannerData = BannerData.createJsonInList(JsonInString);
                    pager.setAdapter(new AdapterForBanner(PriceDropApnaMart.this, bannerData));
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
                progressDialog.dismiss();
                Toast.makeText(PriceDropApnaMart.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ID", "4");
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropApnaMart.this);
        requestQueue.add(stringRequest);

    }




    //ProductDetailspopupdetails
    private void ProductDetailsPopup(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(PriceDropApnaMart.this);
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
        TextView ProductColorTv = dialog.findViewById(R.id.ProductColorTv);
        TextView ProductLengthTv = dialog.findViewById(R.id.ProductLengthTv);
        ImageView outofstok_img = dialog.findViewById(R.id.outofstok_img);
        TextView ProductSizeUnittv = dialog.findViewById(R.id.ProductSizeUnittv);
        TextView ProductSizeValuetv = dialog.findViewById(R.id.ProductSizeValuetv);
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




        buy_btn.setVisibility(View.GONE);






        Cancel.setText("Select Product");
        Cancel.setBackgroundResource(R.color.green);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropApnaMart.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_ProductDeatilsbyId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressaws",response);
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
                        Glide.with(PriceDropApnaMart.this)
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
                        Toast.makeText(PriceDropApnaMart.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropApnaMart.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropApnaMart.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails










    public void PlayNowButtonAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropApnaMart.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_ApnaMartPlayNow", new Response.Listener<String>() {
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

                            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            //then we will inflate the custom alert dialog xml that we created
                            View dialogView = LayoutInflater.from(PriceDropApnaMart.this).inflate(R.layout.congratulation, viewGroup, false);
                            Button rlBack = (Button) dialogView.findViewById(R.id.btnDialog);
                            TextView id_tv = (TextView) dialogView.findViewById(R.id.id_tv);
                            ImageView img_pro=dialogView.findViewById(R.id.img_pro);
                            TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);
                            TextView offer_tv=dialogView.findViewById(R.id.offer_tv);
                            txt_msg.setText("Congratulation You Won!");
                            try {
                                offer_tv.setText(jsonObject.getString("Discount")+" %");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            id_tv.setText(product_code.getText().toString()+"("+product_name_tv.getText().toString()+")");
                            img_pro.setImageDrawable(product_img.getDrawable());
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_animation);
                            offer_tv.startAnimation(animation);


                            AppCompatButton btncancel=dialogView.findViewById(R.id.btncancel);
                            btncancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PriceDropApnaMart.this);
                                    builder.setTitle("Alert Confirmation!")
                                            .setMessage("Are you sure you Don't want to Buy It?")
                                            .setCancelable(false)
                                            .setCancelable(false)
                                            .setIcon(R.drawable.a_logo)
                                            .setPositiveButton("Yes😭😭😭", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    alertDialog5.dismiss();
                                                    Intent intent=new Intent(getApplicationContext(),PriceDropDashboard.class);
                                                    startActivity(intent);
                                                    dialog.cancel();
                                                }
                                            }) .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                                    builder.create().show();
                                }
                            });



                            rlBack.setText("Buy Now>>");
                            rlBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), PriceDrop_Oder_Summery_Details.class);
                                    intent.putExtra("p_code", product_name_tv.getText().toString());
                                    intent.putExtra("p_userid", UserId);
                                    try {
                                        intent.putExtra("p_extradisamount",jsonObject.getString("Discount"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    intent.putExtra("p_price", "");
                                    intent.putExtra("p_name", "");
                                    intent.putExtra("p_img", "");
                                    intent.putExtra("class_name","apnamart");
                                    startActivity(intent);
                                    alertDialog5.dismiss();

//                                        ProceedToBuyAPI(product_name_tv.getText().toString(),jsonObject.getString("Discount"),getamount);
                                }
                            });
                            //Now we need an AlertDialog.Builder object
                            AlertDialog.Builder builder = new AlertDialog.Builder(PriceDropApnaMart.this);
                            //setting the view of the builder to our custom view that we already inflated
                            builder.setView(dialogView);
                            //finally creating the alert dialog and displaying it
                            alertDialog5 = builder.create();
                            alertDialog5.show();


/*                            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                            ViewGroup viewGroup11 = findViewById(android.R.id.content);
                            //then we will inflate the custom alert dialog xml that we created
                            View dialogView11 = LayoutInflater.from(PriceDropApnaMart.this).inflate(R.layout.discount_gift_popup, viewGroup11, false);
                            TextView offer_tv1=dialogView11.findViewById(R.id.offer_tv1);
                            try {
                                offer_tv1.setText(jsonObject.getString("Discount")+" %");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (progresscount == 100) {
                                        *//**//*
                                    } else {
                                        handler.postDelayed(this, 30);
                                        progresscount++;
                                    }
                                }
                            }, 200);

                            Timer RunSplash = new Timer();
                            // Task to do when the timer ends
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                                    ViewGroup viewGroup = findViewById(android.R.id.content);
                                    //then we will inflate the custom alert dialog xml that we created
                                    View dialogView = LayoutInflater.from(PriceDropApnaMart.this).inflate(R.layout.congratulation, viewGroup, false);
                                    Button rlBack = (Button) dialogView.findViewById(R.id.btnDialog);
                                    TextView id_tv = (TextView) dialogView.findViewById(R.id.id_tv);
                                    ImageView img_pro=dialogView.findViewById(R.id.img_pro);
                                    TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);
                                    TextView offer_tv=dialogView.findViewById(R.id.offer_tv);
                                    txt_msg.setText("Congratulation You Won!");
                                    try {
                                        offer_tv.setText(jsonObject.getString("Discount")+" %");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    id_tv.setText(product_code.getText().toString()+"("+product_name_tv.getText().toString()+")");
                                    img_pro.setImageDrawable(product_img.getDrawable());
                                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_animation);
                                    offer_tv.startAnimation(animation);


                                    AppCompatButton btncancel=dialogView.findViewById(R.id.btncancel);
                                    btncancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(PriceDropApnaMart.this);
                                            builder.setTitle("Alert Confirmation!")
                                                    .setMessage("Are you sure you Don't want to Buy It?")
                                                    .setCancelable(false)
                                                    .setIcon(R.drawable.a_logo)
                                                    .setPositiveButton("Yes😭😭😭", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            alertDialog5.dismiss();
                                                            Intent intent=new Intent(getApplicationContext(),PriceDropDashboard.class);
                                                            startActivity(intent);
                                                            dialog.cancel();
                                                        }
                                                    }) .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                            builder.create().show();
                                        }
                                    });



                                    rlBack.setText("Buy Now>>");
                                    rlBack.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                ProceedToBuyAPI(product_name_tv.getText().toString(),jsonObject.getString("Discount"),getamount);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    //Now we need an AlertDialog.Builder object
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PriceDropApnaMart.this);
                                    //setting the view of the builder to our custom view that we already inflated
                                    builder.setView(dialogView);
                                    //finally creating the alert dialog and displaying it
                                    alertDialog5 = builder.create();
                                    alertDialog5.show();
                                }
                        }, SPLASH_TIME_OUT);


                        //Now we need an AlertDialog.Builder object
                        AlertDialog.Builder builder = new AlertDialog.Builder(PriceDropApnaMart.this);
                        //setting the view of the builder to our custom view that we already inflated
                        builder.setView(dialogView11);
                        //finally creating the alert dialog and displaying it
                        alertDialog6 = builder.create();
                        alertDialog6.show();*/


                    }

                    } else {
                        Toast.makeText(PriceDropApnaMart.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myTag", "message:"+error);
                Toast.makeText(PriceDropApnaMart.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("CategoryId","4");
            params.put("UserId",UserId);
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropApnaMart.this);
        requestQueue.add(stringRequest);

    }

    private void ProceedToBuyAPI(String str_procode,String str_dis,String str_amount) {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropApnaMart.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_ApnaMart_Purchase", new Response.Listener<String>() {
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
                            Toast.makeText(PriceDropApnaMart.this, jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                            alertDialog5.dismiss();
                            Intent intent=new Intent(getApplicationContext(),PriceDropDashboard.class);
                            startActivity(intent);
                        }

                    } else {
                        alertDialog5.dismiss();
                        Toast.makeText(PriceDropApnaMart.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myTag", "message:"+error);
                Toast.makeText(PriceDropApnaMart.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId",UserId);
            params.put("ProductCode",str_procode);
            params.put("Discount",str_dis);
            params.put("Amount",str_amount);
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropApnaMart.this);
        requestQueue.add(stringRequest);
    }


    void getOfferListAPI() {
        offer_dataArray = new ArrayList<OfferAdapter>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"DiscountListbyCategory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("DiscountListbyCategory",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String Discount = jsonObject2.getString("Discount");
                        String Photo = jsonObject2.getString("Photo");
                        OfferAdapter noti_screen_lists = new OfferAdapter(Discount,Photo);
                        offer_dataArray.add(noti_screen_lists);
                        GetOfferList(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryId", "2");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void GetOfferList(final int var) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.offer_list, null);
        dynamic_ll.addView(convertView);
        TextView offertv = (TextView) convertView.findViewById(R.id.offer_tv);
        ImageView img = (ImageView) convertView.findViewById(R.id.img);
//        offertv.setText(offer_dataArray.get(var).getDiscount()+"%");
        Glide.with(PriceDropApnaMart.this)
                .load(offer_dataArray.get(var).getPhoto()).error(R.drawable.a_logo)
                .into(img);
        View view = new View(PriceDropApnaMart.this);
        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
        view.setLayoutParams(params_view);
        dynamic_ll.addView(view);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), GoodLuckGameProductView.class);
        startActivity(intent);
    }
}
