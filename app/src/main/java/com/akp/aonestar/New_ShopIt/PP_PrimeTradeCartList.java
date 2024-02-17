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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.HeaderOnClick;
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
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PP_PrimeTradeCartList extends AppCompatActivity {
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
    private Dialog dialog;
    int i = 0;
    TextView title_tv;

    AppCompatButton btnSave;
    TextView total_price_tv,service_tv,tvPayAbleAmmount;
    Button make_pay_btn;

    ArrayList<String> priceList = new ArrayList<>();
    private String final_payablemaount;
    private String final_servicedelivery;
    double totalMrp = 0.0;
    TextView smart_tv,smartbalnace_price_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pp_prime_trade_cart_list);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        srl_refresh = findViewById(R.id.srl_refresh);
        norecord_tv = findViewById(R.id.norecord_tv);
        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        title_tv = findViewById(R.id.title_tv);
        btnSave=findViewById(R.id.btnSave);
        total_price_tv=findViewById(R.id.total_price_tv);
        service_tv=findViewById(R.id.service_tv);
        tvPayAbleAmmount=findViewById(R.id.tvPayAbleAmmount);

        make_pay_btn=findViewById(R.id.make_pay_btn);
        smart_tv=findViewById(R.id.smart_tv);
        smartbalnace_price_tv=findViewById(R.id.smartbalnace_price_tv);

//        *Baneer Code*
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager1);

        //        header layout call here
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();
        GetWalletAPI();

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(PP_PrimeTradeCartList.this)) {
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
                    Toast.makeText(PP_PrimeTradeCartList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getHistory();


    }
    //    Consolation_200_ProductList
    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(PP_PrimeTradeCartList.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "ShoppitTradingCartDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ShoppitCartDetails", "" + response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            title_tv.setText("* Choose One product and avoid participating in the race. (" + Response.length() + " items) *");
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("CartId", jsonObject.getString("CartId"));
                            hashlist.put("EntryDate", jsonObject.getString("EntryDate"));
                            hashlist.put("ProductMRP", jsonObject.getString("ProductMRP"));
                            hashlist.put("DisPer", jsonObject.getString("DisPer"));
                            hashlist.put("ProductSaleRate", jsonObject.getString("ProductSaleRate"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("CategoryName", jsonObject.getString("CategoryName"));
                            hashlist.put("ServiceCharge", jsonObject.getString("ServiceCharge"));
                            hashlist.put("DeliveryCharge", jsonObject.getString("DeliveryCharge"));
                            hashlist.put("GameCategory", jsonObject.getString("GameCategory"));
                            hashlist.put("AddedDate", jsonObject.getString("AddedDate"));
//                            hashlist.put("ExtraDiscount", jsonObject.getString("ExtraDiscount"));
                            hashlist.put("GameCategory1", jsonObject.getString("GameCategory1"));

                            hashlist.put("ProductColor", jsonObject.getString("ProductColor"));
                            hashlist.put("ProductSizeUnit", jsonObject.getString("ProductSizeUnit"));
                            hashlist.put("DoubleSmartWallet", jsonObject.getString("DoubleSmartWallet"));

                            smartbalnace_price_tv.setText(jsonObject.getString("DoubleSmartWallet"));
                            arrayList1.add(hashlist);



                            make_pay_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), BasicPrimeCartPayment.class);
                                    intent.putExtra("total_price_tv",total_price_tv.getText().toString());
                                    intent.putExtra("service_tv",service_tv.getText().toString());
                                    intent.putExtra("tvPayAbleAmmount",tvPayAbleAmmount.getText().toString());
                                    intent.putExtra("GameCategory","PrimeTrading");
                                    startActivity(intent);
                                }
                            });
                            double new_mrp = jsonObject.getDouble("ProductMRP");
                            totalMrp += new_mrp;


                            String del=jsonObject.getString("DeliveryCharge");
                            String ser=jsonObject.getString("ServiceCharge");
                            String smart=jsonObject.getString("DoubleSmartWallet");
                            double akp_txt_payableAmount=totalMrp+Double.valueOf(ser)+Double.valueOf(del);
                            double akp_txt_payableAmountsmart=akp_txt_payableAmount - Double.valueOf(smart);
                            final_payablemaount= String.valueOf(akp_txt_payableAmountsmart);



                            double akp_txt_serdel=Double.valueOf(ser)+Double.valueOf(del);
                            final_servicedelivery= String.valueOf(akp_txt_serdel);


                            total_price_tv.setText(String.valueOf(totalMrp));


                            if (akp_txt_payableAmountsmart < 0) {
                                akp_txt_payableAmountsmart = 0; // Set value to 0 if it's negative
                            }
                            DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Replace with your desired format pattern
                            String formattedValue = decimalFormat.format(akp_txt_payableAmountsmart);
                            tvPayAbleAmmount.setText(formattedValue);

//                            tvPayAbleAmmount.setText(final_payablemaount);

                            service_tv.setText(final_servicedelivery);
                        }

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PP_PrimeTradeCartList.this, 1);
                        PP_PrimeTradeCartList.TaskEarningAdapter customerListAdapter = new PP_PrimeTradeCartList.TaskEarningAdapter(PP_PrimeTradeCartList.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(PP_PrimeTradeCartList.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PP_PrimeTradeCartList.this, "Something went wrong!" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                params.put("GameCategory", "PrimeTrading");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PP_PrimeTradeCartList.this);
        requestQueue.add(stringRequest);
    }


    public class TaskEarningAdapter extends RecyclerView.Adapter<PP_PrimeTradeCartList.TaskEarningAdapter.VH> {
        Context context;
        List<HashMap<String, String>> arrayList;

        private static final int MAX_SELECTION_COUNT = 4;
        private Set<Integer> selectedItems = new HashSet<>();

        public TaskEarningAdapter(Context context, List<HashMap<String, String>> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public PP_PrimeTradeCartList.TaskEarningAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_primebasic_product_tradecart, viewGroup, false);
            PP_PrimeTradeCartList.TaskEarningAdapter.VH viewHolder = new PP_PrimeTradeCartList.TaskEarningAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PP_PrimeTradeCartList.TaskEarningAdapter.VH vh, int i) {
            AnimationHelper.animatate(context, vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-" + arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            vh.new_price_tv.setText("You Save Rs. "+arrayList.get(i).get("ProductMRP"));
            vh.checkBox.setVisibility(View.GONE);
            Glide.with(PP_PrimeTradeCartList.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            vh.checkBox.setVisibility(View.GONE);


        }


        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            TextView name, code,new_price_tv;
            ImageView img, img1;
            CheckBox checkBox;

            public VH(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                name = itemView.findViewById(R.id.name);
                code = itemView.findViewById(R.id.code);
                img1 = itemView.findViewById(R.id.img1);
                checkBox = itemView.findViewById(R.id.checkbox);
                new_price_tv = itemView.findViewById(R.id.new_price_tv);
            }
        }
    }

    //ProductDetailspopupdetails
    private void openOfferpopup(String productcode, String selected) {
        //Create the Dialog here
        dialog = new Dialog(PP_PrimeTradeCartList.this);
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
        TextView p_code = dialog.findViewById(R.id.p_code);
        TextView amount = dialog.findViewById(R.id.amount_tv);
        TextView discount_tv = dialog.findViewById(R.id.discount_tv);
        TextView orderamount_tv = dialog.findViewById(R.id.orderamount_tv);
        ImageView imageView = dialog.findViewById(R.id.imageView);
        TextView ProductSizeUnittv = dialog.findViewById(R.id.ProductSizeUnittv);
        TextView ProductSizeValuetv = dialog.findViewById(R.id.ProductSizeValuetv);

        TextView ProductColorTv = dialog.findViewById(R.id.ProductColorTv);
        TextView ProductLengthTv = dialog.findViewById(R.id.ProductLengthTv);
        ImageView outofstok_img = dialog.findViewById(R.id.outofstok_img);

        TextView ServiceChargeTv = dialog.findViewById(R.id.ServiceChargeTv);
        TextView DeliveryFeeTv = dialog.findViewById(R.id.DeliveryFeeTv);


        TextView productdetails_tv = dialog.findViewById(R.id.productdetails_tv);
        LinearLayout view_more_ll = dialog.findViewById(R.id.view_more_ll);


        buy_btn.setVisibility(View.GONE);

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


        final ProgressDialog progressDialog = new ProgressDialog(PP_PrimeTradeCartList.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "ProductDeatilsbyId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressaws", response);
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

                        String serviceCharge = jsonObject2.getString("ServiceCharge");
                        String deliveryCharge = jsonObject2.getString("DeliveryCharge");


                        ServiceChargeTv.setText(serviceCharge);
                        DeliveryFeeTv.setText(deliveryCharge);


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
                        Glide.with(PP_PrimeTradeCartList.this)
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
                        Toast.makeText(PP_PrimeTradeCartList.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PP_PrimeTradeCartList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PP_PrimeTradeCartList.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails

    public void GetWalletAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GetWalletAmount", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String member_id = jsonObject2.getString("Member_ID");
                        String mainWallet = jsonObject2.getString("MainWallet");
                        String memberName = jsonObject2.getString("MemberName");
                        String emiWallet = jsonObject2.getString("EMIWallet");
                        String smartWallet = jsonObject2.getString("ShoppingWallet");
                        smart_tv.setText("\u20B9 "+smartWallet);
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(PP_PrimeTradeCartList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}