package com.akp.aonestar.PriceDropModule.Adaptors;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.PriceDropModule.Models.SliderItems;
import com.akp.aonestar.PriceDropModule.PriceDropTrailProduct;
import com.akp.aonestar.PriceDropModule.PriceDrop_Oder_Summery_Details;
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
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class SliderAdapterA extends RecyclerView.Adapter<SliderAdapterA.SliderViewHolder> {
    //    private List<SliderItems> sliderItems;
//    private ViewPager2 viewPager2;
    Context context;
    List<HashMap<String,String>> arrayList;
    private AlertDialog alertDialog;
    String UserId;
    private Dialog dialog1;
    int i=0;

    public SliderAdapterA(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
        SharedPreferences sharedPreferences =context.getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container_freetrail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int i) {
//        holder.setImage(arrayList.get(position));
        holder.name.setText((arrayList.get(i).get("ProductName")+"\nProduct Code:- "+arrayList.get(i).get("ProductCode")));
//        holder.amount.setText(("\u20B9"+arrayList.get(i).get("MinAmount")+"- \u20B9"+arrayList.get(i).get("MaxAmount")));
        holder.code.setText("Product MRP \u20B9"+arrayList.get(i).get("ProductMRP"));
        Glide.with(context).load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo).into(holder.imageView);
        if (i == arrayList.size()- 2){
            holder.imageView.post(runnable);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOfferpopup1(arrayList.get(i).get("ProductCode"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        TextView name,code;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            name =itemView.findViewById(R.id.name);
            code =itemView.findViewById(R.id.code);
        }

        void setImage(SliderItems sliderItems){

//use glide or picasso in case you get image from internet
            imageView.setImageResource(sliderItems.getImage());
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            arrayList.addAll(arrayList);
            notifyDataSetChanged();
        }
    };






    //ProductDetailspopupdetails
    private void openOfferpopup1(String productcode) {
        //Create the Dialog here
        dialog1 = new Dialog(context);
        dialog1.setContentView(R.layout.dynamic_productdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog1.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.custom_dialog_background));
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

        final ProgressDialog progressDialog = new ProgressDialog(context);
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
                        Glide.with(context)
                                .load(productImg).error(R.drawable.a_logo)
                                .into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(context, FullImagePage.class);
                                try {
                                    intent.putExtra("path",jsonObject2.getString("ProductImg"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                context.startActivity(intent);
                            }
                        });

                        buy_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(context, PriceDrop_Oder_Summery_Details.class);
                                intent.putExtra("p_code",planCode);
                                intent.putExtra("p_userid",UserId);
                                intent.putExtra("p_disamount",discount);
                                intent.putExtra("p_price",saleRate);
                                intent.putExtra("p_name",plan_name);
                                intent.putExtra("p_img",productImg);
                                intent.putExtra("class_name","trail_product");
                                context.startActivity(intent);
                                dialog1.dismiss();
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
                Toast.makeText(context, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        dialog1.show(); // Showing the dialog here
    }
//End ProductDetailspopupdetails



}
