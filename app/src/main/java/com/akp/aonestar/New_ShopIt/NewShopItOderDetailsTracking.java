package com.akp.aonestar.New_ShopIt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;

public class NewShopItOderDetailsTracking extends AppCompatActivity {
    ImageView back_btn;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FriendsListAdapter pdfAdapTer;
    String UserId;
    ImageView norecord_tv;
    TextView title_tv;

    int i=0;
    String GetOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shop_it_oder_details_tracking);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        GetOrderId=getIntent().getStringExtra("order_id");
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




        GetUserListAPI(GetOrderId);
    }




        public void GetUserListAPI(String OrderId) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"Shoppit_OrderDetails_New", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Shoppit_OrderDetails", response);
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
                                hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                                hashlist.put("CategoryName", jsonObject.getString("CategoryName"));
                                hashlist.put("DeliveryCharge", jsonObject.getString("DeliveryCharge"));

                                hashlist.put("ActualMrp", jsonObject.getString("ActualMrp"));
                                hashlist.put("Quantity", jsonObject.getString("Quantity"));

                                arrFriendsList.add(hashlist);
                            }
                            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                            pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                            rcvList.setLayoutManager(layoutManager);
                            rcvList.setAdapter(pdfAdapTer);
                        } else {
                            norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(NewShopItOderDetailsTracking.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(NewShopItOderDetailsTracking.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("UserId", UserId);
                    params.put("OrderId", OrderId);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(NewShopItOderDetailsTracking.this);
            requestQueue.add(stringRequest);

        }
        public class FriendsListAdapter extends RecyclerView.Adapter<NewShopItOderDetailsTracking.FriendsList> {
            ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
            public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
                data = arrFriendsList;
            }
            public NewShopItOderDetailsTracking.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
                return new NewShopItOderDetailsTracking.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_orderlist_details_shopit, parent, false));
            }

            @SuppressLint("SetTextI18n")
            public void onBindViewHolder(final NewShopItOderDetailsTracking.FriendsList holder, final int position) {
                holder.tv.setText(arrFriendsList.get(position).get("ProductName"));
                holder.tv1.setText("CategoryName :- "+arrFriendsList.get(position).get("CategoryName"));
                holder.tv2.setText("DeliveryCharge \u20B9 "+arrFriendsList.get(position).get("DeliveryCharge"));
                holder.tv3.setText("ActualMrp \u20B9 "+arrFriendsList.get(position).get("ActualMrp"));
                holder.tv4.setText(" Quantity:- "+arrFriendsList.get(position).get("Quantity"));

                if (arrFriendsList.get(position).get("ProductImg").equalsIgnoreCase("")){
                }
                else {
                    Glide.with(getApplicationContext()).load(arrFriendsList.get(position).get("ProductImg")).error(R.drawable.logo).into(holder.img);
                }

            }
            public int getItemCount() {
                return data.size();
            }
        }
        public class FriendsList extends RecyclerView.ViewHolder {
            TextView tv,tv1,tv2,tv3,tv4;
            ImageView img;

            public FriendsList(View itemView) {
                super(itemView);
                tv=itemView.findViewById(R.id.tv);
                tv1=itemView.findViewById(R.id.tv1);
                tv2=itemView.findViewById(R.id.tv2);
                tv3=itemView.findViewById(R.id.tv3);
                tv4=itemView.findViewById(R.id.tv4);
                img=itemView.findViewById(R.id.img);
            }
        }

    }