package com.akp.aonestar.GoodLuckModule;

import static com.akp.aonestar.RetrofitAPI.API_Config.getApiClient_ByPost;

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
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.BottomOnClick;
import com.akp.aonestar.Basic.FloatingButtonGameDetails;
import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.HeaderOnClick;
import com.akp.aonestar.Basic.LoginScreenAkp;
import com.akp.aonestar.Basic.Oder_Summery_Details;
import com.akp.aonestar.IncomeReport.DDFDEMIChart;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.ApiService;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.RetrofitAPI.ConnectToRetrofit;
import com.akp.aonestar.RetrofitAPI.GlobalAppApis;
import com.akp.aonestar.RetrofitAPI.NetworkConnectionHelper;
import com.akp.aonestar.RetrofitAPI.RetrofitCallBackListenar;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.auth.User;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

public class ConsolationProduct extends AppCompatActivity {
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
    int i=0;
    TextView title_tv;

    private ArrayList<String> arrRepaymentListt = new ArrayList<String>();
    AppCompatButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consolation_product);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        srl_refresh=findViewById(R.id.srl_refresh);
        norecord_tv=findViewById(R.id.norecord_tv);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);
        title_tv=findViewById(R.id.title_tv);
        btnSave=findViewById(R.id.btnSave);
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
        BottomOnClick bottom = (BottomOnClick) findViewById(R.id.bottom);
        bottom.initHeader();
        plan_ll=findViewById(R.id.plan_ll);
        plan_ll.setVisibility(View.VISIBLE);
        view=findViewById(R.id.view);
        view.setVisibility(View.VISIBLE);


        //        Floating layout call here
        FloatingButtonGameDetails f_details = (FloatingButtonGameDetails) findViewById(R.id.f_details);
        f_details.initFloating("3");




        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(ConsolationProduct.this)) {
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
                    Toast.makeText(ConsolationProduct.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getHistory();


    }

    public void getHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(ConsolationProduct.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"ConsolationProductList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ConsolationProductList",""+response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            title_tv.setText("* Choose three products and avoid participating in the race. ("+Response.length()+") *");
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("PKID", jsonObject.getString("PKID"));
                            hashlist.put("PlanCode", jsonObject.getString("PlanCode"));
                            hashlist.put("PlanType", jsonObject.getString("PlanType"));
                            hashlist.put("Plan_Name", jsonObject.getString("Plan_Name"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("Mrp", jsonObject.getString("Mrp"));
                            hashlist.put("SaleRate", jsonObject.getString("SaleRate"));
                            hashlist.put("IsSaved", jsonObject.getString("IsSaved"));
                            hashlist.put("SelectedProduct", jsonObject.getString("SelectedProduct"));

                            arrayList1.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ConsolationProduct .this, 3);
                        TaskEarningAdapter customerListAdapter = new TaskEarningAdapter(ConsolationProduct.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(ConsolationProduct.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ConsolationProduct.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId",UserId);
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(ConsolationProduct.this);
        requestQueue.add(stringRequest);

    }




    public class TaskEarningAdapter extends RecyclerView.Adapter<TaskEarningAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;

        private static final int MAX_SELECTION_COUNT = 3;
        private Set<Integer> selectedItems = new HashSet<>();
//        private AdapterView.OnItemSelectedListener listener;



        public TaskEarningAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public TaskEarningAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_product_consolation, viewGroup, false);
            TaskEarningAdapter.VH viewHolder = new TaskEarningAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TaskEarningAdapter.VH vh, int i) {
            AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
            vh.name.setText("Pro.Code-"+arrayList.get(i).get("PlanCode"));
            vh.code.setText(arrayList.get(i).get("Plan_Name"));
            Glide.with(ConsolationProduct.this)
                    .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                    .into(vh.img);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOfferpopup(arrayList.get(i).get("PlanCode"),arrayList.get(i).get("IsSaved"));
                }
            });


            if (arrayList.get(i).get("IsSaved").equals("0")){
                vh.checkBox.setChecked(false);
                vh.checkBox.setClickable(true);
                vh.checkBox.setEnabled(true);
            }
            else {
                vh.checkBox.setChecked(true);
                vh.checkBox.setClickable(false);
                vh.checkBox.setEnabled(false);
            }


            if (arrayList.get(i).get("SelectedProduct").equals("0")){
                vh.img1.setVisibility(View.GONE);
            }
            else {
                vh.img1.setVisibility(View.VISIBLE);
                vh.checkBox.setChecked(true);
                vh.checkBox.setClickable(false);
                vh.checkBox.setEnabled(false);
                if (vh.checkBox.isChecked())
                {
                    vh.checkBox.setChecked(true);
                    vh.checkBox.setClickable(false);
                    vh.checkBox.setEnabled(false);
                }
            }

            vh.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (selectedItems.size() < MAX_SELECTION_COUNT) {
                        selectedItems.add(i);
//                        listener.onItemSelected(i);
                    } else {
                        // Uncheck the checkbox and show a message
                        buttonView.setChecked(false);
                        Toast.makeText(vh.itemView.getContext(), "Only three selections are allowed.", Toast.LENGTH_SHORT).show();
                        SelectedProductAPI();
                    }
                } else {
                    selectedItems.remove(i);
//                    listener.onItemDeselected(i);
                }
            });

            // Set checkbox state based on selection
//            vh.checkBox.setChecked(selectedItems.contains(i));  anoop yha comment kiye hai 11-08-2023



            vh.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vh.checkBox.isChecked()) {
                        arrRepaymentListt.add(arrayList.get(i).get("PlanCode"));
                    } else {
                        arrRepaymentListt.remove(arrayList.get(i).get("PlanCode"));
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
            ImageView img,img1;
            CheckBox checkBox;
            public VH(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                img1 = itemView.findViewById(R.id.img1);
                checkBox = itemView.findViewById(R.id.checkbox);

                name = itemView.findViewById(R.id.name);
                code = itemView.findViewById(R.id.code);
            }
        }
    }

    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(ConsolationProduct.this);
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
                    pager.setAdapter(new AdapterForBanner(ConsolationProduct.this, bannerData));
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
                Toast.makeText(ConsolationProduct.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ID", "3");
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ConsolationProduct.this);
        requestQueue.add(stringRequest);

    }





    //ProductDetailspopupdetails
    private void openOfferpopup(String productcode,String selected) {
        //Create the Dialog here
        dialog = new Dialog(ConsolationProduct.this);
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







        final ProgressDialog progressDialog = new ProgressDialog(ConsolationProduct.this);
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

                        String serviceCharge = jsonObject2.getString("ServiceCharge");
                        String deliveryCharge = jsonObject2.getString("DeliveryCharge");

                        if (title_tv.getText().toString().equalsIgnoreCase("* Choose three products and avoid participating in the race. (1) *")){
//                            Cancel.setVisibility(View.GONE);
                            Cancel.setText("You Got This Product");
                            Cancel.setBackgroundResource(R.color.green);
                            Cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (UserId.equalsIgnoreCase("")){
                                        Intent intent=new Intent(getApplicationContext(), LoginScreenAkp.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }
                                    else{
                                        Intent intent=new Intent(getApplicationContext(), Oder_Summery_Details.class);
                                        intent.putExtra("p_code",planCode);
                                        intent.putExtra("p_userid",UserId);
                                        intent.putExtra("p_disamount",discount);
                                        intent.putExtra("p_price",saleRate);
                                        intent.putExtra("p_name",plan_name);
                                        intent.putExtra("p_img",productImg);
                                        intent.putExtra("class_name","Consolation");
                                        startActivity(intent);
                                    }
                                }
                            });

                        }
                        else {
                            if (UserId.equalsIgnoreCase("")){
                                Cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                           /* else {
                                if (selected.equals("0")){
                                    Cancel.setText("Click to Saved Selected Product");
                                    Cancel.setBackgroundResource(R.color.green);
                                    Cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            SelectedProductAPI(); }
                                    });

                                }
                                else {
                                    Cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss(); }
                                    });
                                }}*/
                            else {
                                Cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                            }

                        }

                        ServiceChargeTv.setText(serviceCharge);
                        DeliveryFeeTv.setText(deliveryCharge);


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
                        Glide.with(ConsolationProduct.this)
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
                        Toast.makeText(ConsolationProduct.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ConsolationProduct.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(ConsolationProduct.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails




    public void SelectedProductAPI() {
        String otp1 = new GlobalAppApis().SavedAllTypeConsolationProduct_New(arrRepaymentListt,UserId,"3");
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.SavedAllTypeConsolationProduct_NewAPI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    Log.v("rsavedesponseeeee", String.valueOf(jsonObject));
                    if(jsonObject.getString("Status").equalsIgnoreCase("true")) {
                        JSONArray Response = jsonObject.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject1 = Response.getJSONObject(i);
                            Toast.makeText(ConsolationProduct.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ConsolationProduct.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, ConsolationProduct.this, call1, "", true);

    }

//
//    public static class VolleyRequestManager {
//        private static final String BASE_URL = "https://aoneshop.co.in/api/A1Shop/";
//        public static void makeJSONRequest(Context context, String userId, String[] productCodes) {
//            String url = BASE_URL + "SavedAllTypeConsolationProduct_New";
//            try {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("MemberId", userId);
//                jsonObject.put("PlanId", 3);
//                JSONArray objemiArray = new JSONArray();
//                for (String productCode : productCodes) {
//                    JSONObject product = new JSONObject();
//                    product.put("ProductCode", productCode);
//                    objemiArray.put(product);
//                }
//                jsonObject.put("objemi", objemiArray);
//
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
//                        response -> {
//                            // Handle the response here
//                            // Parse and handle the response JSON
//                            String responseData = response.toString();
//                            try {
//                                JSONObject jsonObject1 = new JSONObject(responseData);
//                                Log.v("responseeeee", String.valueOf(jsonObject1));
//                                if(jsonObject1.getString("Status").equalsIgnoreCase("true")) {
//                                    JSONArray Response1 = jsonObject1.getJSONArray("Response");
//                                    for (int i = 0; i < Response1.length(); i++) {
//                                        JSONObject jsonObject11 = Response1.getJSONObject(i);
//                                        Toast.makeText(context, jsonObject11.getString("msg"), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                                else {
//                                    Toast.makeText(context, jsonObject1.getString("Message"), Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            Log.d("conspol",responseData);
//                            // Handle responseData as needed
//                        },
//                        error -> {
//                            // Handle error here
//                            error.printStackTrace();
//                        });
//
//                // Add the request to the Volley request queue
//                RequestQueue requestQueue = Volley.newRequestQueue(context);
//                requestQueue.add(jsonObjectRequest);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }


//    public void onItemSelected(int i) {
//        // Handle item selection
//        // For example, you can update UI, perform actions, etc.
//    }
//
//    public void onItemDeselected(int i) {
//        // Handle item deselection
//        // For example, you can update UI, perform actions, etc.
//    }

    public void onItemSelected(int position) {
        // Handle item selection
        // For example, you can update UI, perform actions, etc.
    }

    public void onItemDeselected(int position) {
        // Handle item deselection
        // For example, you can update UI, perform actions, etc.
    }

}