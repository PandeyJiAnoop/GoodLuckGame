package com.akp.aonestar.Home;

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
import com.akp.aonestar.Basic.GameSelection;
import com.akp.aonestar.Basic.Oder_Summery_Details;
import com.akp.aonestar.PlayBuyModule.PlayBuy_Oder_Summery_Details;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddToCart extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
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
        setContentView(R.layout.activity_add_to_cart);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        Log.v("addadada", UserId);
        title_tv= findViewById(R.id.title_tv);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), GameSelection.class);
                startActivity(intent);
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"CartDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("CartDetails", response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            title_tv.setText("Cart Details("+Response.length()+")");
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

                            hashlist.put("Color", jsonObject.getString("Color"));
                            hashlist.put("Size", jsonObject.getString("Size"));

                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(AddToCart.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(AddToCart.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(AddToCart.this);
        requestQueue.add(stringRequest);

    }
    public class FriendsListAdapter extends RecyclerView.Adapter<AddToCart.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public AddToCart.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AddToCart.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_cartlist, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final AddToCart.FriendsList holder, final int position) {


            holder.name.setText(arrFriendsList.get(position).get("ProductName"));
            holder.code.setText("Product Code:- "+arrFriendsList.get(position).get("ProductCode"));
            Glide.with(AddToCart.this).load(arrFriendsList.get(position).get("ProductImg")).error(R.drawable.a_logo).into(holder.img);

            holder.discount_tv.setText(arrFriendsList.get(position).get("DisPer")+" %");
            holder.orderamount_tv.setText("\u20B9 "+arrFriendsList.get(position).get("ProductSaleRate"));

            holder.oderdate_tv.setText(arrFriendsList.get(position).get("AddedDate"));
            holder.amount_tv.setText("\u20B9 "+arrFriendsList.get(position).get("ProductMRP"));

            holder.cat.setText("Category:- "+arrFriendsList.get(position).get("GameCategory"));
//            holder.extra_discount_tv.setText(arrFriendsList.get(position).get("ExtraDiscount")+" %");

            holder.color_tv.setText("Color:- "+arrFriendsList.get(position).get("Color"));
            holder.size_tv.setText("Size:- "+arrFriendsList.get(position).get("Size"));

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), FullImagePage.class);
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrFriendsList.get(position).get("GameCategory1").equalsIgnoreCase("Shop It")){
                        Intent intent=new Intent(getApplicationContext(), Oder_Summery_Details.class);
                        intent.putExtra("p_code",arrFriendsList.get(position).get("ProductCode"));
                        intent.putExtra("p_userid",UserId);
                        intent.putExtra("class_name","cart");
                        startActivity(intent);
                    }
//                   else if (arrFriendsList.get(position).get("GameCategory1").equalsIgnoreCase("Price Drop")){
//                        Intent intent=new Intent(getApplicationContext(), PriceDrop_Oder_Summery_Details.class);
//                        intent.putExtra("p_code",arrFriendsList.get(position).get("ProductCode"));
//                        intent.putExtra("p_userid",UserId);
//                        intent.putExtra("class_name","cart");
//                        startActivity(intent);
//                    }
                   else if (arrFriendsList.get(position).get("GameCategory1").equalsIgnoreCase("See and Buy")){
                        Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_Summery_Details.class);
                        intent.putExtra("p_code",arrFriendsList.get(position).get("ProductCode"));
                        intent.putExtra("p_userid",UserId);
                        intent.putExtra("class_name","cart");
                        startActivity(intent);
                    }

                }
            });

            holder.btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrFriendsList.get(position).get("GameCategory1").equalsIgnoreCase("Shop It")){
                        Intent intent=new Intent(getApplicationContext(), Oder_Summery_Details.class);
                        intent.putExtra("p_code",arrFriendsList.get(position).get("ProductCode"));
                        intent.putExtra("p_userid",UserId);
                        intent.putExtra("class_name","cart");
                        startActivity(intent);
                    }
//                    else if (arrFriendsList.get(position).get("GameCategory1").equalsIgnoreCase("Price Drop")){
//                        Intent intent=new Intent(getApplicationContext(), PriceDrop_Oder_Summery_Details.class);
//                        intent.putExtra("p_code",arrFriendsList.get(position).get("ProductCode"));
//                        intent.putExtra("p_userid",UserId);
//                        intent.putExtra("class_name","cart");
//                        startActivity(intent);
//                    }
                    else if (arrFriendsList.get(position).get("GameCategory1").equalsIgnoreCase("See and Buy")){
                        Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_Summery_Details.class);
                        intent.putExtra("p_code",arrFriendsList.get(position).get("ProductCode"));
                        intent.putExtra("p_userid",UserId);
                        intent.putExtra("class_name","cart");
                        startActivity(intent);
                    }
                }
            });

                holder.delete_img.setVisibility(View.VISIBLE);


                holder.delete_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"DeleteCart", new Response.Listener<String>() {
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
                                            Toast.makeText(AddToCart.this, jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AddToCart.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String, String> params = new HashMap<>();
                                params.put("UserId", UserId);
                                params.put("CartId", arrFriendsList.get(position).get("CartId"));
                                return params;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        RequestQueue requestQueue = Volley.newRequestQueue(AddToCart.this);
                        requestQueue.add(stringRequest);
                    }
                });




        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView name,code,oderdate_tv,amount_tv,Payment_statustv,discount_tv,orderamount_tv,cat,extra_discount_tv,color_tv,size_tv;
        ImageView img,delete_img;
        LinearLayout view_more_ll,ll;
        Button btn_CreateOrder;


        public FriendsList(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            code=itemView.findViewById(R.id.code);
            img=itemView.findViewById(R.id.img);
            discount_tv=itemView.findViewById(R.id.discount_tv);
            orderamount_tv=itemView.findViewById(R.id.orderamount_tv);
            delete_img=itemView.findViewById(R.id.delete_img);
            oderdate_tv=itemView.findViewById(R.id.oderdate_tv);
            amount_tv=itemView.findViewById(R.id.amount_tv);
            Payment_statustv=itemView.findViewById(R.id.Payment_statustv);
            cat = itemView.findViewById(R.id.cat);
            view_more_ll = itemView.findViewById(R.id.view_more_ll);
            ll=itemView.findViewById(R.id.ll);
            btn_CreateOrder=itemView.findViewById(R.id.btn_CreateOrder);

            color_tv = itemView.findViewById(R.id.color_tv);
            size_tv = itemView.findViewById(R.id.size_tv);
//            extra_discount_tv= itemView.findViewById(R.id.extra_discount_tv);
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