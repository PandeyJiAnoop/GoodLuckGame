package com.akp.aonestar.PriceDropModule.Adaptors;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.akp.aonestar.PriceDropModule.Models.SliderItems;
import com.akp.aonestar.PriceDropModule.PriceDropBuyBid;
import com.akp.aonestar.PriceDropModule.PriceDropProductContentChalange;
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

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
//    private List<SliderItems> sliderItems;
//    private ViewPager2 viewPager2;
Context context;
    List<HashMap<String,String>> arrayList;
    private AlertDialog alertDialog;
    String UserId;

    public SliderAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
        SharedPreferences sharedPreferences =context.getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int i) {
//        holder.setImage(arrayList.get(position));
        holder.name.setText((arrayList.get(i).get("ProductName")+"("+arrayList.get(i).get("ProductCode")+")"));
//        holder.amount.setText(("\u20B9"+arrayList.get(i).get("MinAmount")+"- \u20B9"+arrayList.get(i).get("MaxAmount")));
        holder.code.setText("Product MRP \u20B9"+arrayList.get(i).get("ProductMRP"));
        Glide.with(context)
                .load(arrayList.get(i).get("ProductImg")).error(R.drawable.a_logo)
                .into(holder.imageView);
        if (i == arrayList.size()- 2){
            holder.imageView.post(runnable);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup =v.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.bid_withdrawpopup, viewGroup, false);
                final AppCompatButton Submit_btn = (AppCompatButton) dialogView.findViewById(R.id.Submit_btn);
                EditText rupee_et=(EditText)dialogView.findViewById(R.id.rupee_et);

                Submit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (rupee_et.getText().toString().equalsIgnoreCase("")) {
                            rupee_et.setError("Fields can't be blank!");
                            rupee_et.requestFocus();
                        } else {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl + "PRIZEDROP_BIDPlayNow", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("PRIZEDROP_BIDPlayNow",response);
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        if (object.getString("Status").equalsIgnoreCase("true")) {
                                            JSONArray jsonArray = object.getJSONArray("Response");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                                Toast.makeText(context, jsonObject2.getString("Msg"), Toast.LENGTH_LONG).show();
                                                alertDialog.dismiss();
                                            }
                                        }
                                        else {
                                            Toast.makeText(context, object.getString("Message"), Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    alertDialog.dismiss();
                                    Toast.makeText(context, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("Amount", rupee_et.getText().toString());
                                    params.put("UserId", UserId);
                                    params.put("ProductCode",arrayList.get(i).get("ProductCode"));
                                    params.put("BIDID", arrayList.get(i).get("BIDID"));
                                    return params;
                                }
                            };
                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);
                        }
                    }
                });
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setView(dialogView);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;
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









}
