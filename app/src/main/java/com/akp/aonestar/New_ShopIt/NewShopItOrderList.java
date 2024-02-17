package com.akp.aonestar.New_ShopIt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.OderDetailsTracking;
import com.akp.aonestar.OrderTrack.Web_OderDetailsTracking;
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

public class NewShopItOrderList extends AppCompatActivity {
    ImageView back_btn;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FriendsListAdapter pdfAdapTer;
    String UserId;
    ImageView norecord_tv;
    TextView title_tv;

    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shop_it_order_list);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        Log.v("addadada", UserId);
        title_tv= findViewById(R.id.title_tv);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcvList = findViewById(R.id.rcvList);
        norecord_tv=findViewById(R.id.norecord_tv);




        GetUserListAPI();
    }



    public void GetUserListAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"Shoppit_AllOrderList_New", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("ddres", response);
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            title_tv.setText("Order Details(ShopIt)("+Response.length()+")");
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("ProductName", jsonObject.getString("ProductName"));
                            hashlist.put("OrderId", jsonObject.getString("OrderId"));
                            hashlist.put("OrderDate", jsonObject.getString("OrderDate"));
                            hashlist.put("DeliveryCharge", jsonObject.getString("DeliveryCharge"));

                            hashlist.put("PaymentMode", jsonObject.getString("PaymentMode"));
                            hashlist.put("OrderStatus", jsonObject.getString("OrderStatus"));
                            hashlist.put("Amount", jsonObject.getString("Amount"));
                            hashlist.put("Discount", jsonObject.getString("Discount"));
                            hashlist.put("fullAddress", jsonObject.getString("fullAddress"));
                            hashlist.put("PaymentStatus", jsonObject.getString("PaymentStatus"));

                            hashlist.put("DispatchStatus", jsonObject.getString("DispatchStatus"));
                            hashlist.put("awb_number", jsonObject.getString("awb_number"));
                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(NewShopItOrderList.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(NewShopItOrderList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(NewShopItOrderList.this);
        requestQueue.add(stringRequest);

    }
    public class FriendsListAdapter extends RecyclerView.Adapter<NewShopItOrderList.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public NewShopItOrderList.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NewShopItOrderList.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_orderlist_shopit, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final NewShopItOrderList.FriendsList holder, final int position) {
            holder.tv.setText(arrFriendsList.get(position).get("ProductName"));
            holder.tv1.setText(" OrderId:- "+arrFriendsList.get(position).get("OrderId"));

            holder.tv2.setText(arrFriendsList.get(position).get("Discount")+" %");
            holder.tv3.setText("\u20B9 "+arrFriendsList.get(position).get("Amount"));

            holder.tv4.setText(arrFriendsList.get(position).get("OrderDate"));
            holder.tv5.setText(arrFriendsList.get(position).get("DeliveryCharge"));


            holder.tv6.setText(arrFriendsList.get(position).get("PaymentMode"));
            holder.tv7.setText(arrFriendsList.get(position).get("OrderStatus"));
            holder.tv8.setText(arrFriendsList.get(position).get("fullAddress"));



            holder.view_more_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i == 0) {
                        holder.ll.setVisibility(View.VISIBLE);
                        i++;
                    } else if (i == 1) {
                        holder.ll.setVisibility(View.GONE);

                        i = 0;
                    }
                }
            });
            if (arrFriendsList.get(position).get("DispatchStatus").equalsIgnoreCase("false")){
                holder.track_btn.setVisibility(View.VISIBLE);
                holder.track_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(), Web_OderDetailsTracking.class);
                        intent.putExtra("awb_number",arrFriendsList.get(position).get("awb_number"));
                        startActivity(intent);
                    }
                });}

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), NewShopItOderDetailsTracking.class);
                    intent.putExtra("order_id",arrFriendsList.get(position).get("OrderId"));
                    startActivity(intent);
                }
            });

            if (arrFriendsList.get(position).get("OrderStatus").equalsIgnoreCase("Pending")){
                holder.tv9.setText(arrFriendsList.get(position).get("PaymentStatus"));
                holder.tv9.setTextColor(Color.RED);
              /*  holder.delete_img.setVisibility(View.VISIBLE);


                holder.delete_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"DeleteOrder", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.v("ddres", response);
                                String jsonString = response;
                                try {
                                    JSONObject object = new JSONObject(jsonString);
                                    String status = object.getString("Status");
                                    if (status.equalsIgnoreCase("true")) {
                                        JSONArray Response = object.getJSONArray("Response");
                                        for (int i = 0; i < Response.length(); i++) {
                                            JSONObject jsonObject = Response.getJSONObject(i);
                                            finish();
                                             overridePendingTransition(0, 0);
                                             startActivity(getIntent());
                                            overridePendingTransition(0, 0);
                                            arrFriendsList.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, arrFriendsList.size());
                                            Toast.makeText(NewShopItOrderList.this, jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(NewShopItOrderList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("UserId", UserId);
                                params.put("OrderId", arrFriendsList.get(position).get("OrderId"));
                                return params;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        RequestQueue requestQueue = Volley.newRequestQueue(NewShopItOrderList.this);
                        requestQueue.add(stringRequest);
                    }
                });

*/

            }
            else {
                holder.tv9.setText(arrFriendsList.get(position).get("PaymentStatus"));
                holder.tv9.setTextColor(Color.GREEN);

            }

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView tv,tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9;

        LinearLayout view_more_ll,ll;
        Button track_btn;
        public FriendsList(View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv);
            tv1=itemView.findViewById(R.id.tv1);

            ll=itemView.findViewById(R.id.ll);
            tv2=itemView.findViewById(R.id.tv2);
            tv3=itemView.findViewById(R.id.tv3);
            tv4=itemView.findViewById(R.id.tv4);
            tv5=itemView.findViewById(R.id.tv5);
            tv6=itemView.findViewById(R.id.tv6);
            tv7 = itemView.findViewById(R.id.tv7);
            view_more_ll = itemView.findViewById(R.id.view_more_ll);
            tv8 = itemView.findViewById(R.id.tv8);
            tv9 = itemView.findViewById(R.id.tv9);
            track_btn = itemView.findViewById(R.id.track_btn);
        }
    }

}