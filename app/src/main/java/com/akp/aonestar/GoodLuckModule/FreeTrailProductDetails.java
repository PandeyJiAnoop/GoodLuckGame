package com.akp.aonestar.GoodLuckModule;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.FloatingButtonGameDetails;
import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Basic.LoginScreenAkp;
import com.akp.aonestar.Basic.Oder_Summery_Details;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FreeTrailProductDetails extends AppCompatActivity {
    private TextView msg;
    private ImageView img1, img2;
    private Wheel wheel1, wheel2, wheel3;
    private Button btn;
    private boolean isStarted;

    public static final Random RANDOM = new Random();
    private AlertDialog alertDialog5;
    private Dialog dialog;

    public static long randomLong(long lower, long upper) {
        return lower + (long) (RANDOM.nextDouble() * (upper - lower));
    }




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

    int i=0;
    ExtendedFloatingActionButton fab_add_my_album_listing;
    RelativeLayout main_rl;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_trail_product_details);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
//        srl_refresh=findViewById(R.id.srl_refresh);
        norecord_tv=findViewById(R.id.norecord_tv);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);

        //        Floating layout call here
        FloatingButtonGameDetails f_details = (FloatingButtonGameDetails) findViewById(R.id.f_details);
        f_details.initFloating("2");
        
        TextView person_name=findViewById(R.id.person_name);
        person_name.setSelected(true);
        ImageView buynow_btn=findViewById(R.id.buynow_btn);
        buynow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), GoodLuckGameProductView.class);
                startActivity(intent);
            }
        });


        img1 = findViewById(R.id.img1);
        img2 =findViewById(R.id.img2);
        btn = (Button) findViewById(R.id.btn);
        msg = (TextView) findViewById(R.id.msg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserId.equalsIgnoreCase("")){
                    Intent intent=new Intent(getApplicationContext(),LoginScreenAkp.class);
                    startActivity(intent);
                }
                else {
                    PlayNowButtonAPI();



//                    wheel1 = new Wheel(new Wheel.WheelListener() {
//                        @Override
//                        public void newImage(final int img) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    img1.setText(img);
////                                    img1.setImageResource(img);
//                                }
//                            });
//                        }
//                    }, 200, randomLong(0, 200));
//
//                    wheel1.start();
//
//                    wheel2 = new Wheel(new Wheel.WheelListener() {
//                        @Override
//                        public void newImage(final int img) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    img2.setText(img);
////                                    img2.setImageResource(img);
//                                }
//                            });
//                        }
//                    }, 200, randomLong(150, 400));
//
//                    wheel2.start();
//
//                    wheel3 = new Wheel(new Wheel.WheelListener() {
//                        @Override
//                        public void newImage(final int img) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                }
//                            });
//                        }
//                    }, 200, randomLong(150, 400));
//
//                    wheel3.start();
//
//
//                    btn.setVisibility(View.GONE);
////                btn.setText("Stop");
//                    msg.setText("");
//                    isStarted = true;
//
//                    final int intervalTime = 5000; // 10 sec
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable()  {
//                        @Override
//                        public void run() {
//                            wheel1.stopWheel();
//                            wheel2.stopWheel();
//                            wheel3.stopWheel();
//                            if (wheel1.currentIndex == wheel2.currentIndex && wheel2.currentIndex == wheel3.currentIndex) {
//                                msg.setText("You win the big prize");
//                            } else if (wheel1.currentIndex == wheel2.currentIndex || wheel2.currentIndex == wheel3.currentIndex
//                                    || wheel1.currentIndex == wheel3.currentIndex) {
//                                msg.setText("Little Prize");
//                            } else {
//                                msg.setText("You lose");
//                            }
//                            btn.setText("Play");
//                            btn.setVisibility(View.VISIBLE);
//                            isStarted = false;
//                            winner();
//                        }
//                    }, intervalTime);
                }



            }
        });



//        *Baneer Code*
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager1);
        getBanner();

        //        header layout call here
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();
        plan_ll=findViewById(R.id.plan_ll);
        plan_ll.setVisibility(View.VISIBLE);
        view=findViewById(R.id.view);
        view.setVisibility(View.VISIBLE);


//        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (NetworkConnectionHelper.isOnline(FreeTrailProductDetails.this)) {
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
//                    Toast.makeText(FreeTrailProductDetails.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        getHistory();



       /* btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStarted) {
                    wheel1.stopWheel();
                    wheel2.stopWheel();
                    wheel3.stopWheel();

                    if (wheel1.currentIndex == wheel2.currentIndex && wheel2.currentIndex == wheel3.currentIndex) {
                        msg.setText("You win the big prize");
                    } else if (wheel1.currentIndex == wheel2.currentIndex || wheel2.currentIndex == wheel3.currentIndex
                            || wheel1.currentIndex == wheel3.currentIndex) {
                        msg.setText("Little Prize");
                    } else {
                        msg.setText("You lose");
                    }
                    btn.setText("Play");
                    isStarted = false;

                } else {
                    wheel1 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img1.setImageResource(img);
                                }
                            });
                        }
                    }, 200, randomLong(0, 200));

                    wheel1.start();

                    wheel2 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img2.setImageResource(img);
                                }
                            });
                        }
                    }, 200, randomLong(150, 400));

                    wheel2.start();

                    wheel3 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(final int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        }
                    }, 200, randomLong(150, 400));

                    wheel3.start();


                    btn.setText("Stop");
                    msg.setText("");
                    isStarted = true;
                }
            }
        });*/


    }


//    private void winner() {
//
//        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//        //then we will inflate the custom alert dialog xml that we created
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.congratulation, viewGroup, false);
//        Button rlBack = (Button) dialogView.findViewById(R.id.btnDialog);
//        TextView id_tv = (TextView) dialogView.findViewById(R.id.id_tv);
////        id_tv.setText("is " +colorName+" Name"+ customerName);
//
//        rlBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                alertDialog5.dismiss();
//            }
//        });
//
//        //Now we need an AlertDialog.Builder object
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        //setting the view of the builder to our custom view that we already inflated
//        builder.setView(dialogView);
//        //finally creating the alert dialog and displaying it
//        alertDialog5 = builder.create();
//        alertDialog5.show();
//    }












    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(FreeTrailProductDetails.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"ProductListbyCategory", new Response.Listener<String>() {
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
                            hashlist.put("PKID", jsonObject.getString("PKID"));
                            hashlist.put("PlanCode", jsonObject.getString("PlanCode"));
                            hashlist.put("PlanType", jsonObject.getString("PlanType"));
                            hashlist.put("Plan_Name", jsonObject.getString("Plan_Name"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("Mrp", jsonObject.getString("Mrp"));
                            hashlist.put("SaleRate", jsonObject.getString("SaleRate"));
                            arrayList1.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(FreeTrailProductDetails .this, 3);
                        TaskEarningAdapter customerListAdapter = new TaskEarningAdapter(FreeTrailProductDetails.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(FreeTrailProductDetails.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FreeTrailProductDetails.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("CategoryId","2");
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(FreeTrailProductDetails.this);
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
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product, viewGroup, false);
            TaskEarningAdapter.VH viewHolder = new TaskEarningAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskEarningAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("PlanCode"));
            vh.code.setText(arrayList.get(i).get("Plan_Name"));
            Glide.with(FreeTrailProductDetails.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDetailsPopup(arrayList.get(i).get("PlanCode"));
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
        final ProgressDialog progressDialog = new ProgressDialog(FreeTrailProductDetails.this);
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
                    pager.setAdapter(new AdapterForBanner(FreeTrailProductDetails.this, bannerData));
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
                Toast.makeText(FreeTrailProductDetails.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ID", "2");
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(FreeTrailProductDetails.this);
        requestQueue.add(stringRequest);

    }


    //ProductDetailspopupdetails
    private void ProductDetailsPopup(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(FreeTrailProductDetails.this);
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
        final ProgressDialog progressDialog = new ProgressDialog(FreeTrailProductDetails.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"ProductDeatilsbyId", new Response.Listener<String>() {
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
                        Glide.with(FreeTrailProductDetails.this)
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


                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(FreeTrailProductDetails.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(FreeTrailProductDetails.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails









    public void PlayNowButtonAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(FreeTrailProductDetails.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"FreeTrailPlayNowProduct", new Response.Listener<String>() {
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
                            wheel1 = new Wheel(new Wheel.WheelListener() {
                                @Override
                                public void newImage(final int img) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            try {
//                                                img1.setText(jsonObject.getString("PlanCode"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
                                           img1.setImageResource(img);
                                        }
                                    });
                                }
                            }, 200, randomLong(0, 200));

                            wheel1.start();

                            wheel2 = new Wheel(new Wheel.WheelListener() {
                                @Override
                                public void newImage(final int img) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            try {
//                                                img2.setText(jsonObject.getString("PlanCode"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
                                    img2.setImageResource(img);
                                        }
                                    });
                                }
                            }, 200, randomLong(150, 400));

                            wheel2.start();

                            wheel3 = new Wheel(new Wheel.WheelListener() {
                                @Override
                                public void newImage(final int img) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                        }
                                    });
                                }
                            }, 200, randomLong(150, 400));

                            wheel3.start();


                            btn.setVisibility(View.GONE);
//                btn.setText("Stop");
                            msg.setText("");
                            isStarted = true;

                            final int intervalTime = 5000; // 10 sec
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable()  {
                                @Override
                                public void run() {
                                    wheel1.stopWheel();
                                    wheel2.stopWheel();
                                    wheel3.stopWheel();
                                    if (wheel1.currentIndex == wheel2.currentIndex && wheel2.currentIndex == wheel3.currentIndex) {
                                        msg.setText("You win the big prize");
                                    } else if (wheel1.currentIndex == wheel2.currentIndex || wheel2.currentIndex == wheel3.currentIndex
                                            || wheel1.currentIndex == wheel3.currentIndex) {
                                        msg.setText("Little Prize");
                                    } else {
                                        msg.setText("You lose");
                                    }
                                    btn.setText("Play");
                                    btn.setVisibility(View.VISIBLE);
                                    isStarted = false;
                                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                                    ViewGroup viewGroup = findViewById(android.R.id.content);
                                    //then we will inflate the custom alert dialog xml that we created
                                    View dialogView = LayoutInflater.from(FreeTrailProductDetails.this).inflate(R.layout.congratulation, viewGroup, false);
                                    Button rlBack = (Button) dialogView.findViewById(R.id.btnDialog);
                                    TextView id_tv = (TextView) dialogView.findViewById(R.id.id_tv);
                                    ImageView img_pro=dialogView.findViewById(R.id.img_pro);
                                    TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);




                                    AppCompatButton btncancel=dialogView.findViewById(R.id.btncancel);
                                    btncancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(FreeTrailProductDetails.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
                                            builder.setTitle("Alert Confirmation!")
                                                    .setMessage("Are you sure you Don't want to Buy It?")
                                                    .setCancelable(false)
                                                    .setIcon(R.drawable.a_logo)
                                                    .setPositiveButton("Yes😭😭😭", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            Intent intent=new Intent(getApplicationContext(), FreeTrailProductDetails.class);
                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            builder.create().show();
                                        }
                                    });

                                    TextView offer_tv=dialogView.findViewById(R.id.offer_tv);
                                    txt_msg.setText("Congratulation You Won!");
                                    try {
                                        offer_tv.setText(jsonObject.getString("Discount")+" %");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_animation);
                                    offer_tv.startAnimation(animation);


                                    try {
                                        id_tv.setText(jsonObject.getString("Plan_Name")+"("+jsonObject.getString("PlanCode")+")\nProduct Mrp:- \u20B9 "+jsonObject.getString("ProductMrp")+"\nSale Rate:- \u20B9 "+jsonObject.getString("SaleRate"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        Glide.with(FreeTrailProductDetails.this)
                                                .load(jsonObject.getString("ProductImg")).error(R.drawable.a_logo)
                                                .into(img_pro);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    rlBack.setText("Buy Now >>");
                                    rlBack.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                Intent intent=new Intent(getApplicationContext(), Oder_Summery_Details.class);
                                                intent.putExtra("p_code",jsonObject.getString("PlanCode"));
                                                intent.putExtra("p_userid",UserId);
                                                intent.putExtra("p_disamount",jsonObject.getString("Discount"));
                                                intent.putExtra("p_price",jsonObject.getString("SaleRate"));
                                                intent.putExtra("p_name",jsonObject.getString("Plan_Name"));
                                                intent.putExtra("p_img",jsonObject.getString("ProductImg"));
                                                intent.putExtra("class_name","Freetrail");
                                                startActivity(intent);
                                                dialog.dismiss();

//                                                ProceedToBuyAPI(jsonObject.getString("PlanCode"),jsonObject.getString("Discount"),jsonObject.getString("SaleRate"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    //Now we need an AlertDialog.Builder object
                                    AlertDialog.Builder builder = new AlertDialog.Builder(FreeTrailProductDetails.this);
                                    //setting the view of the builder to our custom view that we already inflated
                                    builder.setView(dialogView);
                                    //finally creating the alert dialog and displaying it
                                    alertDialog5 = builder.create();
                                    alertDialog5.show();
                                }
                            }, intervalTime);

                    }

                    } else {
                        Toast.makeText(FreeTrailProductDetails.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myTag", "message:"+error);
                Toast.makeText(FreeTrailProductDetails.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("CategoryId","2");
            params.put("UserId",UserId);
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(FreeTrailProductDetails.this);
        requestQueue.add(stringRequest);

    }

    private void ProceedToBuyAPI(String str_procode,String str_dis,String str_amount) {
        final ProgressDialog progressDialog = new ProgressDialog(FreeTrailProductDetails.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"FreeTrailProduct_Purchase", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("sfgsgsgsfg",response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            Toast.makeText(FreeTrailProductDetails.this, jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
//                            alertDialog5.dismiss();
                            Intent intent=new Intent(getApplicationContext(), FreeTrailProductDetails.class);
                            startActivity(intent);
                        }

                    } else {
                        alertDialog5.dismiss();
                        Toast.makeText(FreeTrailProductDetails.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myTag", "message:"+error);
                Toast.makeText(FreeTrailProductDetails.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(FreeTrailProductDetails.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), GoodLuckGameProductView.class);
        startActivity(intent);
    }
}