package com.akp.aonestar.New_ShopIt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.BottomOnClick;
import com.akp.aonestar.Basic.FloatingButtonGameDetails;
import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Basic.Oder_Summery_Details;
import com.akp.aonestar.GoodLuckModule.AdapterForBanner;
import com.akp.aonestar.GoodLuckModule.AnimationHelper;
import com.akp.aonestar.GoodLuckModule.BannerData;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.RetrofitAPI.NetworkConnectionHelper;
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

public class ShopIt_productBetting extends AppCompatActivity {
    private Dialog dialog;
    LinearLayout plan_ll;
    View view;
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    String UserId;
    SwipeRefreshLayout srl_refresh;
    ImageView sliding_menu;
    ImageView norecord_tv;


    CirclePageIndicator indicator;
    ViewPager pager;
    List<BannerData> bannerData = new ArrayList<>();
    private static int currentPage = 0;

    String planCode,plan_name,productMrp,saleRate,productImg,discount;
    private Dialog dialog1;
    int i=0;

    ExtendedFloatingActionButton fab_add_my_album_listing;
    TextView tobet_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_it_product_betting);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        srl_refresh=findViewById(R.id.srl_refresh);
        norecord_tv=findViewById(R.id.norecord_tv);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);
        tobet_id=findViewById(R.id.tobet_id);
        //        Floating layout call here
        FloatingButtonGameDetails f_details = (FloatingButtonGameDetails) findViewById(R.id.f_details);
        f_details.initFloating("1");

//        TextView person_name=findViewById(R.id.person_name);
//        person_name.setSelected(true);


      /*  if (UserId.equalsIgnoreCase("")){

        }
        else {
            openOfferpopup();
        }
*/


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
        plan_ll=findViewById(R.id.plan_ll);
        plan_ll.setVisibility(View.VISIBLE);
        view=findViewById(R.id.view);
        view.setVisibility(View.VISIBLE);




        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(ShopIt_productBetting.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(ShopIt_productBetting.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getHistory();


    }


    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(ShopIt_productBetting.this);
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
                            tobet_id.setText("TO BET - "+jsonObject.getString("PlanCode"));
                            arrayList1.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ShopIt_productBetting .this, 1);
                        ShopIt_productBetting.TaskEarningAdapter customerListAdapter = new ShopIt_productBetting.TaskEarningAdapter(ShopIt_productBetting.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(ShopIt_productBetting.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ShopIt_productBetting.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("CategoryId","16");
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ShopIt_productBetting.this);
        requestQueue.add(stringRequest);

    }




    public class TaskEarningAdapter extends RecyclerView.Adapter<ShopIt_productBetting.TaskEarningAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public TaskEarningAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public ShopIt_productBetting.TaskEarningAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_productbetting, viewGroup, false);
            ShopIt_productBetting.TaskEarningAdapter.VH viewHolder = new ShopIt_productBetting.TaskEarningAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ShopIt_productBetting.TaskEarningAdapter.VH vh, int i) {
            AnimationHelper.animatate(context, vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("PlanCode"));
            vh.code.setText(arrayList.get(i).get("Plan_Name")+"\nMRP:- "+arrayList.get(i).get("Mrp")+
                    "\nOUR PRICE:- "+arrayList.get(i).get("SaleRate")+"\nYOUR UNIQUE PRICE:- RS.00./");
            Glide.with(ShopIt_productBetting.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);

           /* vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductDetailsPopup(arrayList.get(i).get("PlanCode"));
                }
            });*/

            vh.Add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!UserId.equalsIgnoreCase("")){
                        Intent intent=new Intent(getApplicationContext(),SCANQRANDSUBMITDETAILS.class);
                        intent.putExtra("pro_code",arrayList.get(i).get("PlanCode"));
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Login First!",Toast.LENGTH_SHORT).show();
                    }
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
            AppCompatButton Add_button;


            public VH(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                name = itemView.findViewById(R.id.name);
                code = itemView.findViewById(R.id.code);

                Add_button= itemView.findViewById(R.id.Add_button);
            }
        }
    }

    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(ShopIt_productBetting.this);
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
                    pager.setAdapter(new AdapterForBanner(ShopIt_productBetting.this, bannerData));
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
                Toast.makeText(ShopIt_productBetting.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ID", "1");
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ShopIt_productBetting.this);
        requestQueue.add(stringRequest);

    }


    private void openOfferpopup() {
        //Create the Dialog here
        dialog = new Dialog(ShopIt_productBetting.this);
        dialog.setContentView(R.layout.custom_dialog_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        GifImageView Okay = dialog.findViewById(R.id.btn_okay);
        AppCompatButton Cancel = dialog.findViewById(R.id.btn_cancel);
        TextView textView = dialog.findViewById(R.id.textView);
        TextView textView2 = dialog.findViewById(R.id.textView2);
        ImageView imageView = dialog.findViewById(R.id.imageView);



        final ProgressDialog progressDialog = new ProgressDialog(ShopIt_productBetting.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"ShopIt_productBetting_Check", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("UserIdcheck",""+response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        planCode = jsonObject2.getString("PlanCode");
                        plan_name = jsonObject2.getString("Plan_Name");
                        productMrp = jsonObject2.getString("ProductMrp");
                        saleRate = jsonObject2.getString("SaleRate");
                        productImg = jsonObject2.getString("ProductImg");
                        discount = jsonObject2.getString("Discount");

                        textView.setText(plan_name+"("+planCode+")");
                        textView2.setText("Product Mrp- \u20B9 "+productMrp+"\nDiscount- ("+discount+") %"+"\nSale rate- \u20B9 ("+saleRate+")");

                        Glide.with(ShopIt_productBetting.this).load(productImg).error(R.drawable.a_logo).into(imageView);
                    }

                    if (jsonObject.getString("Message").equalsIgnoreCase("You have already visited.")){
                        Toast.makeText(ShopIt_productBetting.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    if (jsonObject.getString("Message").equalsIgnoreCase("No Plan Purchased")){
                        Toast.makeText(ShopIt_productBetting.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }



                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ShopIt_productBetting.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ShopIt_productBetting.this);
        requestQueue.add(stringRequest);











        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Oder_Summery_Details.class);
                intent.putExtra("p_code",planCode);
                intent.putExtra("p_userid",UserId);
                intent.putExtra("p_disamount",discount);
                intent.putExtra("p_price",saleRate);
                intent.putExtra("p_name",plan_name);
                intent.putExtra("p_img",productImg);
                intent.putExtra("class_name","Freejoining");
                startActivity(intent);
             /*   final ProgressDialog progressDialog = new ProgressDialog(ShopIt_productBetting.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"ShopIt_productBetting_Purchase", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("Response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        Toast.makeText(ShopIt_productBetting.this, jsonObject2.getString("Msg"), Toast.LENGTH_SHORT).show();

                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ShopIt_productBetting.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("UserId",UserId);
                        params.put("IsPurchase","1");
                        params.put("ProductCode",planCode);
                        params.put("Discount",discount);
                        params.put("Amount",productMrp);
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(ShopIt_productBetting.this);
                requestQueue.add(stringRequest);

*/



//                Toast.makeText(ShopIt_productBetting.this, "Sorry!! You have Missed your Chance Try Again😭😭", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(ShopIt_productBetting.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"ShopIt_productBetting_NoThanks", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("Response");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                Toast.makeText(ShopIt_productBetting.this, jsonObject2.getString("Msg"), Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ShopIt_productBetting.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("UserId",UserId);
                        return params;
//                        params.put("UserId",UserId);
//                        params.put("IsPurchase","0");
//                        params.put("ProductCode",planCode);
//                        params.put("Discount",discount);
//                        params.put("Amount",productMrp);
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(ShopIt_productBetting.this);
                requestQueue.add(stringRequest);

//                Toast.makeText(ShopIt_productBetting.this, "Successful😀😀😀😀", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show(); // Showing the dialog here
    }






    //ProductDetailspopupdetails
    private void ProductDetailsPopup(String productcode) {
        //Create the Dialog here
        dialog1 = new Dialog(ShopIt_productBetting.this);
        dialog1.setContentView(R.layout.dynamic_productdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog1.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_background));
        }
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.setCancelable(false); //Optional
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        AppCompatButton Cancel = dialog1.findViewById(R.id.btn_cancel);
        TextView name = dialog1.findViewById(R.id.name);
        TextView amount = dialog1.findViewById(R.id.amount_tv);
        TextView discount_tv = dialog1.findViewById(R.id.discount_tv);
        TextView orderamount_tv = dialog1.findViewById(R.id.orderamount_tv);
        ImageView imageView = dialog1.findViewById(R.id.imageView);
        TextView p_code = dialog1.findViewById(R.id.p_code);

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
        final ProgressDialog progressDialog = new ProgressDialog(ShopIt_productBetting.this);
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
                        Glide.with(ShopIt_productBetting.this)
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
                Toast.makeText(ShopIt_productBetting.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ShopIt_productBetting.this);
        requestQueue.add(stringRequest);
        dialog1.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails


}