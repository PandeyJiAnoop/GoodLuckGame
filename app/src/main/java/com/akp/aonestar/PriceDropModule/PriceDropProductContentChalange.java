package com.akp.aonestar.PriceDropModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.Gravity;
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

import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Basic.LoginScreenAkp;
import com.akp.aonestar.Basic.Oder_Summery_Details;
import com.akp.aonestar.GoodLuckModule.AnimationHelper;
import com.akp.aonestar.GoodLuckModule.ChooseDiscountProduct;
import com.akp.aonestar.PriceDropModule.Adaptors.AdaptorProductChallenge;
import com.akp.aonestar.PriceDropModule.Models.ModelProCha;
import com.akp.aonestar.PriceDropModule.Models.PriceModel;
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
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class PriceDropProductContentChalange extends AppCompatActivity {

    List<ModelProCha> list = new ArrayList<>();
    RecyclerView recyclerPriceList;

    AdaptorProductChallenge adaptorProductChallenge;
    AdaptorPriceList adaptorPriceList;
    List<PriceModel> modelList = new ArrayList<>();


    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    String UserId;
    ImageView norecord_tv;
    LinearLayout recyclerProduct1, recyclerProduct2;


    ImageView imgPlay;
    private Dialog dialog;
    int i = 0,j=0;
    String getCatId;
    TextView price_tv,pro_1,pro_2,pro_3,pro_4,a_pro_1,a_pro_2,a_pro_3,a_pro_4,a_pro_price;
    private AlertDialog alertDialog5;
    String getProductCode1,getProductCode2,getProductCode3,getProductCode4;
    GifImageView contest_gif;
    RelativeLayout play_contest_rl,main_rl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_drop_product_content_chalange);
        getCatId = getIntent().getStringExtra("cat_id");
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        norecord_tv = findViewById(R.id.norecord_tv);
        cust_recyclerView = findViewById(R.id.cust_recyclerView);

        //        Floating layout call here
        PriceDropFloatingButtonGameDetails f_details = (PriceDropFloatingButtonGameDetails) findViewById(R.id.f_details);
        f_details.initFloating(getCatId);
        // header onclick
        HeaderOnClick header = (HeaderOnClick) findViewById(R.id.header);
        header.initHeader();

        imgPlay = findViewById(R.id.imgPlay);
        recyclerProduct1 = findViewById(R.id.recyclerProduct1);
        recyclerProduct2 = findViewById(R.id.recyclerProduct2);
        recyclerPriceList = findViewById(R.id.recyclerPriceList);
        recyclerPriceList.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerPriceList.setHasFixedSize(true);


        pro_1=findViewById(R.id.pro_1);
        pro_2=findViewById(R.id.pro_2);
        pro_3=findViewById(R.id.pro_3);
        pro_4=findViewById(R.id.pro_4);
        price_tv=findViewById(R.id.price_tv);
        a_pro_1=findViewById(R.id.a_pro_1);
        a_pro_2=findViewById(R.id.a_pro_2);
        a_pro_3=findViewById(R.id.a_pro_3);
        a_pro_4=findViewById(R.id.a_pro_4);
        a_pro_price=findViewById(R.id.a_pro_price);
        contest_gif=findViewById(R.id.contest_gif);
        play_contest_rl=findViewById(R.id.play_contest_rl);
        main_rl=findViewById(R.id.main_rl);





//API Calling
        getData();
        getHistory();



        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserId.equalsIgnoreCase("")){
                    Intent intent=new Intent(getApplicationContext(), LoginScreenAkp.class);
                    startActivity(intent);
                }
                else {
                    if (pro_1.getText().toString().equalsIgnoreCase("Code")){
                        Toast.makeText(getApplicationContext(),"Choose Your Product!",Toast.LENGTH_LONG).show();
                    }
                    else if (pro_2.getText().toString().equalsIgnoreCase("Code")){
                        Toast.makeText(getApplicationContext(),"Choose Your Product!",Toast.LENGTH_LONG).show();
                    }
                    else if (pro_3.getText().toString().equalsIgnoreCase("Code")){
                        Toast.makeText(getApplicationContext(),"Choose Your Product!",Toast.LENGTH_LONG).show();
                    }
                    else if (pro_4.getText().toString().equalsIgnoreCase("Code")){
                        Toast.makeText(getApplicationContext(),"Choose Your Product!",Toast.LENGTH_LONG).show();
                    }
                    else {
//                    GetRandomProductAPI();
                        Toast.makeText(getApplicationContext(),"Select Bid Amount!",Toast.LENGTH_LONG).show();

                    }
                }
            }
        });


    }

    private void getData() {
        modelList.add(new PriceModel("10"));
        modelList.add(new PriceModel("20"));
        modelList.add(new PriceModel("50"));
        modelList.add(new PriceModel("100"));

        adaptorPriceList = new AdaptorPriceList(PriceDropProductContentChalange.this, modelList);
        recyclerPriceList.setAdapter(adaptorPriceList);

        ImageView buynow_btn = findViewById(R.id.buynow_btn);
        buynow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropProductContentChalange.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "PRIZEDROP_ProductbyRootCategory", new Response.Listener<String>() {
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
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PriceDropProductContentChalange.this, 3);
                        TaskEarningAdapter customerListAdapter = new TaskEarningAdapter(PriceDropProductContentChalange.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(PriceDropProductContentChalange.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropProductContentChalange.this, "Something went wrong!" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryId", getCatId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropProductContentChalange.this);
        requestQueue.add(stringRequest);

    }


    public class TaskEarningAdapter extends RecyclerView.Adapter<TaskEarningAdapter.VH> {
        Context context;
        List<HashMap<String, String>> arrayList;

        public TaskEarningAdapter(Context context, List<HashMap<String, String>> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public TaskEarningAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_pricedrop_contestchallange, viewGroup, false);
            TaskEarningAdapter.VH viewHolder = new TaskEarningAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskEarningAdapter.VH vh, int i) {
            AnimationHelper.animatate(context, vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-" + arrayList.get(i).get("ProductCode"));
            vh.code.setText(arrayList.get(i).get("ProductName"));
            Glide.with(PriceDropProductContentChalange.this)
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
            TextView name, code;
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
        dialog = new Dialog(PriceDropProductContentChalange.this);
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


        buy_btn.setVisibility(View.GONE);
        Cancel.setText("Select Product > ");
        Cancel.setBackgroundResource(R.color.green);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pro_1.getText().toString().equalsIgnoreCase("Code")){
                    pro_1.setText("Code- "+productcode);
                }
                else if (pro_2.getText().toString().equalsIgnoreCase("Code")){
                    pro_2.setText("Code- "+productcode);

                }
                else if (pro_3.getText().toString().equalsIgnoreCase("Code")){
                    pro_3.setText("Code- "+productcode);

                }
                else if (pro_4.getText().toString().equalsIgnoreCase("Code")){
                    pro_4.setText("Code- "+productcode);

                }
                dialog.dismiss();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropProductContentChalange.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "PRIZEDROP_ProductDeatilsbyId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ProductDeatilsbyId", response);
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
                        Glide.with(PriceDropProductContentChalange.this)
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
                        Toast.makeText(PriceDropProductContentChalange.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropProductContentChalange.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropProductContentChalange.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails






    public class AdaptorPriceList extends RecyclerView.Adapter<AdaptorPriceList.MyPrice> {

        Context context;
        List<PriceModel> modelList;

        public AdaptorPriceList(Context context, List<PriceModel> modelList) {
            this.context = context;
            this.modelList = modelList;
        }

        @NonNull
        @Override
        public AdaptorPriceList.MyPrice onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptorPriceList.MyPrice(LayoutInflater.from(context).inflate(R.layout.price_list_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptorPriceList.MyPrice holder, int position) {
            holder.tvTextPrice.setText("₹ "+modelList.get(position).getPrice());

//            if (j == 0) {
//                holder.tvTextPrice.setTextColor(Color.GREEN);
//                j++;
//            } else if (j == 1) {
//                holder.tvTextPrice.setTextColor(Color.BLACK);
//                j = 0;
//            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pro_1.getText().toString().equalsIgnoreCase("Code")){
                        Toast.makeText(getApplicationContext(),"Choose Your Product!",Toast.LENGTH_LONG).show();
                    }
                    else if (pro_2.getText().toString().equalsIgnoreCase("Code")){
                        Toast.makeText(getApplicationContext(),"Choose Your Product!",Toast.LENGTH_LONG).show();
                    }
                    else if (pro_3.getText().toString().equalsIgnoreCase("Code")){
                        Toast.makeText(getApplicationContext(),"Choose Your Product!",Toast.LENGTH_LONG).show();
                    }
                    else if (pro_4.getText().toString().equalsIgnoreCase("Code")){
                        Toast.makeText(getApplicationContext(),"Choose Your Product!",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Random r = new Random();
                        int low = 200;
                        int high = 1000;
                        int result = r.nextInt(high-low) + low;
                        String random_amount= String.valueOf(result);
                        a_pro_price.setText(random_amount);
                        GetRandomProductAPI();
                        AlertDialog.Builder builder = new AlertDialog.Builder(PriceDropProductContentChalange.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
                        builder.setTitle("Alert Confirmation for Amount:- "+holder.tvTextPrice.getText().toString())
                                .setMessage("Are you sure you want to Play?")
                                .setCancelable(false)
                                .setIcon(R.drawable.a_logo)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        contest_gif.setVisibility(View.VISIBLE);
                                        play_contest_rl.setVisibility(View.GONE);
                                        PlayGameAPI(modelList.get(position).getPrice());
                                        dialog.cancel();
                                    }
                                }) .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent=new Intent(getApplicationContext(),PriceDropDashboard.class);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
// set the gravity
                        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
// set the margin
                        alertDialog.getWindow().getAttributes().verticalMargin = 0.2F;
                        alertDialog.show();
//                        builder.create().show();
                    }


//                    price_tv.setText(holder.tvTextPrice.getText().toString());
                }
            });
        }

        @Override
        public int getItemCount() {
            return modelList.size();
        }

        public class MyPrice extends RecyclerView.ViewHolder {
            TextView tvTextPrice;

            public MyPrice(@NonNull View itemView) {
                super(itemView);
                tvTextPrice = itemView.findViewById(R.id.tvTextPrice);
            }
        }
    }








    public void GetRandomProductAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl + "PRIZEDROP_ProdcutContestProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        getProductCode1=object.getString("ProductCode");
                        getProductCode2=object.getString("ProductCode1");
                        getProductCode3=object.getString("ProductCode2");
                        getProductCode4=object.getString("ProductCode3");
                        a_pro_1.setText("Code-"+object.getString("ProductCode"));
                        a_pro_2.setText("Code-"+object.getString("ProductCode1"));
                        a_pro_3.setText("Code-"+object.getString("ProductCode2"));
                        a_pro_4.setText("Code-"+object.getString("ProductCode3"));
//                        JSONArray Response = object.getJSONArray("Response");
//                        for (int i = 0; i < Response.length(); i++) {
//                            JSONObject jsonObject = Response.getJSONObject(i);
//
//                        }

                    } else {
                        Toast.makeText(PriceDropProductContentChalange.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myTag", "message:" + error);
                Toast.makeText(PriceDropProductContentChalange.this, "Something went wrong!" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropProductContentChalange.this);
        requestQueue.add(stringRequest);

    }


    public void PlayGameAPI(String str_bid_amount) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_ProductContestPlayNow", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ProductContestPlayNow",   response);
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable(){
                                public void run(){
//ToDo your function
                                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                                    ViewGroup viewGroup = findViewById(android.R.id.content);
                                    //then we will inflate the custom alert dialog xml that we created
                                    View dialogView = LayoutInflater.from(PriceDropProductContentChalange.this).inflate(R.layout.congratulation, viewGroup, false);
                                    Button rlBack = (Button) dialogView.findViewById(R.id.btnDialog);
                                    TextView id_tv = (TextView) dialogView.findViewById(R.id.id_tv);
                                    ImageView img_pro=dialogView.findViewById(R.id.img_pro);
                                    TextView txt_msg = (TextView) dialogView.findViewById(R.id.txt_msg);

                                    TextView offer_tv=dialogView.findViewById(R.id.offer_tv);
                                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in_animation);
                                    offer_tv.startAnimation(animation);

                                    try {
                                        offer_tv.setText(jsonObject.getString("DisPer")+" %");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    txt_msg.setText("Congratulation You Won!");
                                    try {
                                        id_tv.setText(jsonObject.getString("ProductName")+"("+jsonObject.getString("ProductCode")+")"+"\n\nProduct MRP:- ₹ "+jsonObject.getString("ProductMRP")+"\nProduct Sale Rate:- ₹ "+jsonObject.getString("ProductSaleRate"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
//                            img_pro.setImageDrawable(product_img.getDrawable());
                                    try {
                                        Glide.with(PriceDropProductContentChalange.this)
                                                .load(jsonObject.getString("ProductImg")).error(R.drawable.a_logo)
                                                .into(img_pro);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    AppCompatButton btncancel=dialogView.findViewById(R.id.btncancel);
                                    btncancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(PriceDropProductContentChalange.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
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
                                            Intent intent = new Intent(getApplicationContext(), PriceDrop_Oder_Summery_Details.class);
                                            try {
                                                intent.putExtra("p_code", jsonObject.getString("ProductCode"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            intent.putExtra("p_userid", UserId);
                                            intent.putExtra("p_disamount", "");
                                            intent.putExtra("p_price", "");
                                            intent.putExtra("p_name", "");
                                            intent.putExtra("p_img", "");
                                            intent.putExtra("class_name","productcontent");
                                            startActivity(intent);
                                            alertDialog5.dismiss();

//                                                ProceedToBuyAPI(jsonObject.getString("ProductCode"),jsonObject.getString("DisPer"),jsonObject.getString("ProductSaleRate"));
                                        }
                                    });
                                    //Now we need an AlertDialog.Builder object
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PriceDropProductContentChalange.this);
                                    //setting the view of the builder to our custom view that we already inflated
                                    builder.setView(dialogView);
                                    //finally creating the alert dialog and displaying it
                                    alertDialog5 = builder.create();
                                    alertDialog5.show();
                                }
                            },1500);




                    }

                    } else {
                        contest_gif.setVisibility(View.GONE);
                        play_contest_rl.setVisibility(View.VISIBLE);
                        Toast.makeText(PriceDropProductContentChalange.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myTag", "message:" + error);
                Toast.makeText(PriceDropProductContentChalange.this, "Something went wrong!" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ProductCode", getProductCode1);
                params.put("ProductCode1", getProductCode2);
                params.put("ProductCode2", getProductCode3);
                params.put("ProductCode3", getProductCode4);
                params.put("UserId", UserId);
                params.put("Amount", str_bid_amount);

                Log.d("params123", "message:" + params);


                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropProductContentChalange.this);
        requestQueue.add(stringRequest);

    }




    private void ProceedToBuyAPI(String str_procode,String str_dis,String str_amount) {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropProductContentChalange.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_ProductContest_Purchase", new Response.Listener<String>() {
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
                            Toast.makeText(PriceDropProductContentChalange.this, jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                            alertDialog5.dismiss();
                            Intent intent=new Intent(getApplicationContext(),PriceDropDashboard.class);
                            startActivity(intent);
                        }

                    } else {
                        alertDialog5.dismiss();
                        Toast.makeText(PriceDropProductContentChalange.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),PriceDropDashboard.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myTag", "message:"+error);
                Toast.makeText(PriceDropProductContentChalange.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropProductContentChalange.this);
        requestQueue.add(stringRequest);
    }
}