package com.akp.aonestar.GoodLuckModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.FloatingButtonGameDetails;
import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.GameSelection;
import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Basic.MyOrderList;
import com.akp.aonestar.Basic.Oder_PersonalDetails;
import com.akp.aonestar.Basic.Oder_Summery_Details;
import com.akp.aonestar.PlayBuyModule.NetworkManager.MyVolleySingleton;
import com.akp.aonestar.PlayBuyModule.NetworkManager.WebServices;

import com.akp.aonestar.PlayBuyModule.PlayBuyDashboard;
import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.DataResponseProduct;
import com.akp.aonestar.PlayBuyModule.ScratchCardManager.ScratchCard;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.RetrofitAPI.NetworkConnectionHelper;
import com.akp.aonestar.SplashActivity;
import com.akp.aonestar.WalletReport.AddWallet;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SeasonalSaleProduct extends AppCompatActivity {
    LinearLayout plan_ll;
    View view;

    RecyclerView cust_recyclerView, recycScratchList;
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
    List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> listProduct= new ArrayList<>();
    AdaptorScratchProductList adaptorScratchProductList;
    private final int limit = 9;

    String productImage, productName, productPrice, productCode, productDisAmt;
    private Dialog alertDialog;
    private Dialog dialog11;


    private ClipboardManager myClipboard;
    private ClipData myClip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasonal_sale_product);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        srl_refresh = findViewById(R.id.srl_refresh);
        norecord_tv = findViewById(R.id.norecord_tv);
        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        recycScratchList = findViewById(R.id.recycScratchList);
        //        Floating layout call here
        FloatingButtonGameDetails f_details = (FloatingButtonGameDetails) findViewById(R.id.f_details);
        f_details.initFloating("5");


        TextView person_name = findViewById(R.id.person_name);
        person_name.setSelected(true);
        ImageView buynow_btn = findViewById(R.id.buynow_btn);
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
        plan_ll = findViewById(R.id.plan_ll);
        plan_ll.setVisibility(View.VISIBLE);
        view = findViewById(R.id.view);
        view.setVisibility(View.VISIBLE);
        recycScratchList.setLayoutManager(new GridLayoutManager(this, 3));
        recycScratchList.setHasFixedSize(true);
        getCatProdList("1");

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(SeasonalSaleProduct.this)) {
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
                    Toast.makeText(SeasonalSaleProduct.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getHistory();


    }

    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(SeasonalSaleProduct.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "ProductListbyCategory", new Response.Listener<String>() {
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
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(SeasonalSaleProduct.this, 3);
                        TaskEarningAdapter customerListAdapter = new TaskEarningAdapter(SeasonalSaleProduct.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(SeasonalSaleProduct.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SeasonalSaleProduct.this, "Something went wrong!" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CategoryId", "5");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(SeasonalSaleProduct.this);
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
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product, viewGroup, false);
            TaskEarningAdapter.VH viewHolder = new TaskEarningAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskEarningAdapter.VH vh, int i) {
            AnimationHelper.animatate(context, vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-" + arrayList.get(i).get("PlanCode"));
            vh.code.setText(arrayList.get(i).get("Plan_Name"));
            Glide.with(SeasonalSaleProduct.this)
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

    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(SeasonalSaleProduct.this);
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
                    pager.setAdapter(new AdapterForBanner(SeasonalSaleProduct.this, bannerData));
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
                        public void onPageScrolled(int pos, float arg1, int arg2) {
                        }

                        @Override
                        public void onPageScrollStateChanged(int pos) {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(SeasonalSaleProduct.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ID", "5");
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SeasonalSaleProduct.this);
        requestQueue.add(stringRequest);

    }

    private void openAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SeasonalSaleProduct.this,R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Background);
        builder.setTitle("Alert Confirmation for Amount:- ₹ 10")
                .setMessage("Are you sure you want to Play?")
                .setCancelable(false)
                .setIcon(R.drawable.gameonegif)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getScratchProductDetails("5");
                        dialog.cancel();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                        /*Intent intent=new Intent(getApplicationContext(),PriceDropDashboard.class);
                        startActivity(intent);*/
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    /*private void openAlertDialog() {
        //Create the Dialog here
        dialog11 = new Dialog(SeasonalSaleProduct.this);
        dialog11.setContentView(R.layout.dynamic_new_bid_amount_popup);
        dialog11.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog11.setCancelable(false); //Optional
        dialog11.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        EditText utr_et = dialog11.findViewById(R.id.utr_et);
        ImageView image_qr = dialog11.findViewById(R.id.image_qr);
        TextView tvYes = dialog11.findViewById(R.id.tvYes);
        TextView tvCancel =dialog11.findViewById(R.id.tvCancel);
        TextView upiid_tv=dialog11.findViewById(R.id.upiid_tv);
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        upiid_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String text = upiid_tv.getText().toString();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Link Copied", Toast.LENGTH_SHORT).show();
            }
        });



        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebServices.QRdetailsAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject2 = Response.getJSONObject(i);
                            upiid_tv.setText(jsonObject2.getString("UPIID"));
                            Glide.with(SeasonalSaleProduct.this).load(jsonObject2.getString("QRImg")).error(R.drawable.a_logo).into(image_qr);
                        }
                    } else {
                        Toast.makeText(SeasonalSaleProduct.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SeasonalSaleProduct.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SeasonalSaleProduct.this);
        requestQueue.add(stringRequest);


        //close the popup window on button click
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog11.dismiss();
            }
        });
        //close the popup window on button click
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utr_et.getText().toString().equalsIgnoreCase("")){
                    utr_et.setError("UTR Must be Filled");
                    utr_et.requestFocus();
                }
                else {
                    getScratchProductDetails("5");
                    dialog11.dismiss();
                }

            }
        });
        dialog11.show(); // Showing the dialog here

    }*/


    private void getCatProdList(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CategoryId", id);
            jsonObject.put("Search", "");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ScratchPlayProduct, jsonObject, response -> {
            Gson gson = new Gson();
            try {
                DataResponseProduct productCategoryModel = gson.fromJson(response.toString(), DataResponseProduct.class);
                if (productCategoryModel.isStatus()) {
                    listProduct.clear();
                    listProduct = productCategoryModel.getResponse();
                    adaptorScratchProductList = new AdaptorScratchProductList(getApplicationContext(), productCategoryModel.getResponse());
                    recycScratchList.setAdapter(adaptorScratchProductList);


                } else {
                    Toast.makeText(SeasonalSaleProduct.this, "Record not found", Toast.LENGTH_SHORT).show();
                }
            } catch (JsonSyntaxException e) {
                throw new RuntimeException(e);
            }
        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public class AdaptorScratchProductList extends RecyclerView.Adapter<AdaptorScratchProductList.MyScratchHolder> {

        Context context;
        List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList;

        public AdaptorScratchProductList(Context context, List<com.akp.aonestar.PlayBuyModule.PlayBuyModels.ProductByCategory.ResponseItem> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public AdaptorScratchProductList.MyScratchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptorScratchProductList.MyScratchHolder(LayoutInflater.from(context).inflate(R.layout.scratch_layout_seasonal, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptorScratchProductList.MyScratchHolder holder, @SuppressLint("RecyclerView") int position) {
//            Glide.with(context)
//                    .load(itemList.get(position).getProductImg())
//                    .placeholder(R.drawable.logo)
//                    .error(R.drawable.logo)
//                    .into(holder.pro_img);
            holder.pro_name.setText(itemList.get(position).getProductName());
            holder.pro_code.setText(String.valueOf("₹ " + itemList.get(position).getProductSaleRate()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Toast.makeText(PlayBuyDashboard.this, ""+itemList.get(position).getProductName()+
                            "\n"+itemList.get(position).getProductSaleRate(), Toast.LENGTH_SHORT).show();*/
                    openScratchDialogBox(productImage, productName, "₹ " + productPrice);
                }
            });
        }

        @Override
        public int getItemCount() {
//             if(itemList.size() > limit){
//                return limit;
//            }
//            else
//            {
//                return itemList.size();
//            }
            return itemList.size();
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












    private void getScratchProductDetails(String catId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("CategoryId", catId);
            jsonObject.put("UserId", UserId);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.GOODLUCK_Seasonal_ScratchGetPlayNowAPI, jsonObject, response -> {
            try {
                Log.e("TAG", "getScratchProductDetails: " + response);
                if (response.getBoolean("Status")) {
                    cust_recyclerView.setVisibility(View.GONE);
                    recycScratchList.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    productImage = jsonObject1.getString("ProductImg");
                    productName = jsonObject1.getString("ProductName");
                    productCode = String.valueOf(jsonObject1.getInt("ProductCode"));
                    productPrice = String.valueOf(jsonObject1.getDouble("ProductSaleRate"));
                    productDisAmt = String.valueOf(jsonObject1.getDouble("DisAmt"));

                } else {
                    cust_recyclerView.setVisibility(View.VISIBLE);
                    recycScratchList.setVisibility(View.GONE);
                    Toast.makeText(this, ""+response.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> {
            Log.e("TAG", "onErrorResponse: " + error.toString());
        });
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    @SuppressLint("SetTextI18n")
    private void openScratchDialogBox(String productImg, String productName, String price) {
        final MediaPlayer mp = MediaPlayer.create(SeasonalSaleProduct.this, R.raw.blast_one);
        mp.start();
        alertDialog = new Dialog(SeasonalSaleProduct.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.alert_scratch_layout_seasonal);
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.trans)));
        alertDialog.setCancelable(false);
        alertDialog.show();
        //alertDialog.getWindow().setLayout((6 * width)/7, ActionBar.LayoutParams.WRAP_CONTENT);

        ImageView imgProduct = alertDialog.findViewById(R.id.imgProduct);
        TextView proName = alertDialog.findViewById(R.id.productName);
        LinearLayout linearExit = alertDialog.findViewById(R.id.linearExit);
        LinearLayout linearProceed = alertDialog.findViewById(R.id.linearProceed);
        ScratchCard scratchCard1 = alertDialog.findViewById(R.id.scratchCard1);

        Glide.with(SeasonalSaleProduct.this)
                .load(productImg)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(imgProduct);

        proName.setText(productName + "\n" + price);

        linearExit.setOnClickListener(v -> {
            alertDialog.dismiss();
            mp.stop();
            Intent intent=new Intent(SeasonalSaleProduct.this,GoodLuckGameProductView.class);
            startActivity(intent);
        });

        linearProceed.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),"Firstly Scratch !!",Toast.LENGTH_LONG).show();
            linearProceed.setVisibility(View.GONE);


            scratchCard1.setOnScratchListener(new ScratchCard.OnScratchListener() {
                @Override
                public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                    if (visiblePercent > 0.8) {
//                        scratch(true);
                        mp.stop();
                        Intent intent=new Intent(getApplicationContext(), Oder_Summery_Details.class);
                        intent.putExtra("p_code",productCode);
                        intent.putExtra("p_userid",UserId);
                        intent.putExtra("p_disamount",productDisAmt);
                        intent.putExtra("p_price",productPrice);
                        intent.putExtra("p_name",productName);
                        intent.putExtra("p_img",productImg);
                        intent.putExtra("class_name","Seasonal");
                        startActivity(intent);
                    }
                }
            });



//            getProductDetails();
        });


    }
    private void getProductDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProductCode", productCode);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Discount", productDisAmt);
            jsonObject.put("Amount", productPrice);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.GOODLUCK_Seasonal_ScratchGetPlay_PurchaseAPI, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    alertDialog.dismiss();
                    recycScratchList.setVisibility(View.GONE);
                    recycScratchList.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SeasonalSaleProduct.this, GoodLuckGameProductView.class);
                    startActivity(intent);
                } else {
                    alertDialog.dismiss();
                    Toast.makeText(this, response.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                alertDialog.dismiss();
                throw new RuntimeException(e);
            }

        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), GameSelection.class);
        startActivity(intent);
    }

























    //ProductDetailspopupdetails
    private void ProductDetailsPopup(String productcode) {
        //Create the Dialog here
        dialog = new Dialog(SeasonalSaleProduct.this);
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
        TextView productdetails_tv = dialog.findViewById(R.id.productdetails_tv);
        LinearLayout view_more_ll = dialog.findViewById(R.id.view_more_ll);


        TextView ProductColorTv = dialog.findViewById(R.id.ProductColorTv);
        TextView ProductLengthTv = dialog.findViewById(R.id.ProductLengthTv);
        ImageView outofstok_img = dialog.findViewById(R.id.outofstok_img);

        TextView ServiceChargeTv = dialog.findViewById(R.id.ServiceChargeTv);
        TextView DeliveryFeeTv = dialog.findViewById(R.id.DeliveryFeeTv);


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

        buy_btn.setText("Try to Find Out >");

        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlertDialog();
                dialog.dismiss();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(SeasonalSaleProduct.this);
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

                        if (isStock.equalsIgnoreCase("0")){
                            outofstok_img.setVisibility(View.VISIBLE);
                            imageView.setAlpha((float) 0.5);
                            buy_btn.setClickable(false);
                            buy_btn.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"Product Out of Stock",Toast.LENGTH_LONG).show();
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
                        Glide.with(SeasonalSaleProduct.this)
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
                        Toast.makeText(SeasonalSaleProduct.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SeasonalSaleProduct.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(SeasonalSaleProduct.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails





//    private void scratch(boolean isScratched) {
//        if (isScratched) {
//            mScratchCard.setVisibility(View.INVISIBLE);
//        } else {
//            mScratchCard.setVisibility(View.VISIBLE);
//        }
//    }
//    private void handleListeners() {
//        mScratchCard.setOnScratchListener(new ScratchCard.OnScratchListener() {
//            @Override
//            public void onScratch(ScratchCard scratchCard, float visiblePercent) {
//                if (visiblePercent > 0.8) {
//                    scratch(true);
//                }
//            }
//        });
//    }

}