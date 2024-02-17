package com.akp.aonestar.Basic;

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

public class MyOrderList extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ImageView back_btn;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FriendsListAdapter pdfAdapTer;
    String UserId;
    ImageView norecord_tv;
    TextView title_tv;
    Spinner ststus_sp;

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);
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
        ststus_sp=findViewById(R.id.ststus_sp);

        // Spinner click listener
        ststus_sp.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Shop It");
//        categories.add("Price Drop");
        categories.add("See and Buy");
        categories.add("Shop on the Spot");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        ststus_sp.setAdapter(dataAdapter);



//        GetUserListAPI("Good Luck");
    }



    public void GetUserListAPI(String cat) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"OrderHistroy", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("OrderHistroy", response);
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
                            title_tv.setText("Order Details("+Response.length()+")");
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("ProductCode", jsonObject.getString("ProductCode"));
                            hashlist.put("Plan_Name", jsonObject.getString("Plan_Name"));
                            hashlist.put("OrderId", jsonObject.getString("OrderId"));
                            hashlist.put("OrderDate", jsonObject.getString("OrderDate"));
                            hashlist.put("Amount", jsonObject.getString("Amount"));
                            hashlist.put("Discount", jsonObject.getString("Discount"));
                            hashlist.put("OrderAmt", jsonObject.getString("OrderAmt"));
                            hashlist.put("PaymentStatus", jsonObject.getString("PaymentStatus"));
                            hashlist.put("ProductImg", jsonObject.getString("ProductImg"));
                            hashlist.put("CatName", jsonObject.getString("CatName"));
                            hashlist.put("Description", jsonObject.getString("Description"));
                            hashlist.put("GameCategoryName", jsonObject.getString("GameCategoryName"));
                            hashlist.put("ExtraDiscount", jsonObject.getString("ExtraDiscount"));

                            hashlist.put("Color", jsonObject.getString("Color"));
                            hashlist.put("Size", jsonObject.getString("Size"));
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
//                        Toast.makeText(MyOrderList.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MyOrderList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("GameCategory", cat);
                params.put("UserId", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MyOrderList.this);
        requestQueue.add(stringRequest);

    }
    public class FriendsListAdapter extends RecyclerView.Adapter<FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_orderlist, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final FriendsList holder, final int position) {
            holder.name.setText(arrFriendsList.get(position).get("Plan_Name"));
            holder.code.setText("Product Code:- "+arrFriendsList.get(position).get("ProductCode"));
            Glide.with(MyOrderList.this).load(arrFriendsList.get(position).get("ProductImg")).error(R.drawable.a_logo).into(holder.img);

            holder.discount_tv.setText(arrFriendsList.get(position).get("Discount")+" %");
            holder.orderamount_tv.setText("\u20B9 "+arrFriendsList.get(position).get("OrderAmt"));

            holder.oderdate_tv.setText(arrFriendsList.get(position).get("OrderDate"));
            holder.orderid_tv.setText(arrFriendsList.get(position).get("OrderId"));
            holder.amount_tv.setText("\u20B9 "+arrFriendsList.get(position).get("Amount"));

            holder.cat.setText("Category:- "+arrFriendsList.get(position).get("GameCategoryName"));
            holder.extra_discount_tv.setText(arrFriendsList.get(position).get("ExtraDiscount")+" %");


            holder.color_tv.setText("Color:- "+arrFriendsList.get(position).get("Color"));
            holder.size_tv.setText("Size:- "+arrFriendsList.get(position).get("Size"));


            if (arrFriendsList.get(position).get("Description").equalsIgnoreCase("")){
                holder.productdetails_tv.setText("Details Not Found!!");
            }
            else{
                holder.productdetails_tv.setText(arrFriendsList.get(position).get("Description"));
            }


            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),FullImagePage.class);
                    intent.putExtra("path",arrFriendsList.get(position).get("ProductImg"));
                    startActivity(intent);
                }
            });

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
                    Intent intent=new Intent(getApplicationContext(),OderDetailsTracking.class);
                    intent.putExtra("order_id",arrFriendsList.get(position).get("OrderId"));
                    startActivity(intent);
                }
            });

            if (arrFriendsList.get(position).get("PaymentStatus").equalsIgnoreCase("Pending")){
                holder.Payment_statustv.setText(arrFriendsList.get(position).get("PaymentStatus"));
                holder.Payment_statustv.setTextColor(Color.RED);
                holder.delete_img.setVisibility(View.GONE);
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
                                            Toast.makeText(MyOrderList.this, jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(MyOrderList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
                        RequestQueue requestQueue = Volley.newRequestQueue(MyOrderList.this);
                        requestQueue.add(stringRequest);
                    }
                });

*/

            }
            else {
                holder.Payment_statustv.setText(arrFriendsList.get(position).get("PaymentStatus"));
                holder.Payment_statustv.setTextColor(Color.GREEN);

                holder.delete_img.setVisibility(View.GONE);
            }

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView name,code,oderdate_tv,orderid_tv,amount_tv,Payment_statustv,discount_tv,orderamount_tv,productdetails_tv,cat,extra_discount_tv,color_tv,size_tv;
        ImageView img,delete_img;
        LinearLayout view_more_ll,ll;
        Button track_btn;


        public FriendsList(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            code=itemView.findViewById(R.id.code);
            img=itemView.findViewById(R.id.img);
            ll=itemView.findViewById(R.id.ll);
            discount_tv=itemView.findViewById(R.id.discount_tv);
            orderamount_tv=itemView.findViewById(R.id.orderamount_tv);
            delete_img=itemView.findViewById(R.id.delete_img);
            oderdate_tv=itemView.findViewById(R.id.oderdate_tv);
            orderid_tv=itemView.findViewById(R.id.orderid_tv);
            amount_tv=itemView.findViewById(R.id.amount_tv);
            Payment_statustv=itemView.findViewById(R.id.Payment_statustv);
            productdetails_tv = itemView.findViewById(R.id.productdetails_tv);
           view_more_ll = itemView.findViewById(R.id.view_more_ll);
            cat = itemView.findViewById(R.id.cat);
            extra_discount_tv= itemView.findViewById(R.id.extra_discount_tv);

            color_tv = itemView.findViewById(R.id.color_tv);
            size_tv = itemView.findViewById(R.id.size_tv);
            track_btn = itemView.findViewById(R.id.track_btn);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        arrFriendsList.clear();
        GetUserListAPI(item);

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}