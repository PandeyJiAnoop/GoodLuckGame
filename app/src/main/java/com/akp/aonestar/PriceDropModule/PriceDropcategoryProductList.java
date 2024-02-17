package com.akp.aonestar.PriceDropModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.GoodLuckModule.AnimationHelper;
import com.akp.aonestar.GoodLuckModule.QuizJackpot;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceDropcategoryProductList extends AppCompatActivity {
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    String UserId;
    SwipeRefreshLayout srl_refresh;
    ImageView sliding_menu;
    ImageView norecord_tv;
    NotificationListAdapter customerListAdapter;
    SearchView et_search;
    String GetcatId;
    int i=0;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_dropcategory_product_list);
        GetcatId=getIntent().getStringExtra("cat_id");
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        srl_refresh=findViewById(R.id.srl_refresh);
        norecord_tv=findViewById(R.id.norecord_tv);


        et_search=findViewById(R.id.search);
        sliding_menu=findViewById(R.id.menuImg);
        cust_recyclerView=findViewById(R.id.cust_recyclerView);

        if (GetcatId == null){
            getHistory("0","");
        }
        else {
            getHistory(GetcatId,"");
        }

        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(PriceDropcategoryProductList.this)) {
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
                    Toast.makeText(PriceDropcategoryProductList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });




        et_search.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 2) {
                    arrayList1.clear();
                    customerListAdapter.notifyDataSetChanged();
                    if (GetcatId == null){
                        getHistory("0",newText);
                    }
                    else {
                        getHistory(GetcatId,newText);
                    }
                }
                else {
                    arrayList1.clear();
                    customerListAdapter.notifyDataSetChanged();
                    if (GetcatId == null){
                        getHistory("0","");
                    }
                    else {
                        getHistory(GetcatId,"");
                    }
                }
                return true;
            }
        });


//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                arrayList1.clear();
//                customerListAdapter.notifyDataSetChanged();
////reset adapter with empty array list (it did the trick animation)
//                getHistory(search_et.getText().toString());
//            }
//        });

    }
    public void getHistory(String catid,String ser) {
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropcategoryProductList.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PRIZEDROP_ProductbyCategory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resssss",response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        cust_recyclerView.setVisibility(View.VISIBLE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("ProductId", jsonObject.getString("ProductId"));
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("ProductMRP", jsonObject.getString("ProductMRP"));
                            hashlist.put("ProductSaleRate", jsonObject.getString("ProductSaleRate"));
                            hashlist.put("DisAmt", jsonObject.getString("DisAmt"));
                            hashlist.put("DisPer", jsonObject.getString("DisPer"));
                            hashlist.put("EntryBy", jsonObject.getString("EntryBy"));
                            hashlist.put("Description", jsonObject.getString("Description"));

                            hashlist.put("EntryDate", jsonObject.getString("EntryDate"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("CategoryId", jsonObject.getString("CategoryId"));
                            hashlist.put("CategoryName", jsonObject.getString("CategoryName"));
                            arrayList1.add(hashlist);
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(PriceDropcategoryProductList .this, 1);
                        customerListAdapter = new NotificationListAdapter(PriceDropcategoryProductList.this, arrayList1);
                        cust_recyclerView.setLayoutManager(gridLayoutManager);
                        cust_recyclerView.setAdapter(customerListAdapter);
                    } else {
                        cust_recyclerView.setVisibility(View.GONE);
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(PriceDropcategoryProductList.this, "No data found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PriceDropcategoryProductList.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("CategoryId",catid);
            params.put("Search",ser);
            return params; }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropcategoryProductList.this);
        requestQueue.add(stringRequest);

    }




    public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public NotificationListAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }

        @NonNull
        @Override
        public NotificationListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_pricedrop_cat_productlist, viewGroup, false);
            NotificationListAdapter.VH viewHolder = new NotificationListAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationListAdapter.VH holder, int position) {
            AnimationHelper.animatate(context,holder.itemView, R.anim.alfa_animation);
            holder.name.setText(arrayList.get(position).get("ProductName"));
            holder.code.setText("Code- "+arrayList.get(position).get("ProductCode"));
            Glide.with(PriceDropcategoryProductList.this)
                    .load(arrayList.get(position).get("ProductImg")).error(R.drawable.a_logo)
                    .into(holder.img);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),PriceDropSearchCategoryDetails.class);
                    intent.putExtra("code",arrayList.get(position).get("ProductCode"));
                    intent.putExtra("name",arrayList.get(position).get("ProductName"));
                    startActivity(intent);
//                    ProductDetailspopup("Product "+arrayList.get(position).get("ProductCode"));
                }
            });
            holder.cat.setText("Category:- "+arrayList.get(position).get("CategoryName"));
            holder.mrp.setText("Mrp:-\u20B9 "+arrayList.get(position).get("ProductMRP")+"("+arrayList.get(position).get("DisPer")+" %)");

            holder.saleprice.setText("Sale Rate:- \u20B9 "+arrayList.get(position).get("ProductSaleRate"));
//            holder.amount_tv.setText("\u20B9 "+arrayList.get(position).get("ProductMRP"));
//
//
//            if (arrayList.get(position).get("Description").equalsIgnoreCase("")){
//                holder.productdetails_tv.setText("Details Not Found!!");
//            }
//            else{
//                holder.productdetails_tv.setText(arrayList.get(position).get("Description"));
//            }
//
//
//
//            holder.view_more_ll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (i == 0) {
//                        holder.ll.setVisibility(View.VISIBLE);
//                        i++;
//                    } else if (i == 1) {
//                        holder.ll.setVisibility(View.GONE);
//
//                        i = 0;
//                    }
//                }
//            });

        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView name,code,cat,mrp,saleprice,orderamount_tv,productdetails_tv;
            ImageView img;

//            TextView name,code,orderid_tv,amount_tv,discount_tv,orderamount_tv,productdetails_tv;
//            ImageView img;
//            LinearLayout view_more_ll,ll;

            public VH(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.name);
                code=itemView.findViewById(R.id.code);
                img=itemView.findViewById(R.id.img);
                cat=itemView.findViewById(R.id.cat);
                mrp=itemView.findViewById(R.id.mrp);
                saleprice=itemView.findViewById(R.id.saleprice);
//
//                orderid_tv=itemView.findViewById(R.id.orderid_tv);
//                amount_tv=itemView.findViewById(R.id.amount_tv);
//                productdetails_tv = itemView.findViewById(R.id.productdetails_tv);
//                view_more_ll = itemView.findViewById(R.id.view_more_ll);


            }
        }
    }

    private void ProductDetailspopup(String pr_code) {
        //Create the Dialog here
        dialog = new Dialog(PriceDropcategoryProductList.this);
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




        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final ProgressDialog progressDialog = new ProgressDialog(PriceDropcategoryProductList.this);
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
                        Glide.with(PriceDropcategoryProductList.this)
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

                        Cancel.setText("Buy Now >>");
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(PriceDropcategoryProductList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ProductId",pr_code);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PriceDropcategoryProductList.this);
        requestQueue.add(stringRequest);
        dialog.show(); // Showing the dialog here
    }


}