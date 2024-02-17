package com.akp.aonestar.Ecommerce;

import static com.akp.aonestar.RetrofitAPI.API_Config.getApiClient_ByPost;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.OrderTrack.Web_OderDetailsTracking;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.ApiService;
import com.akp.aonestar.RetrofitAPI.ConnectToRetrofit;
import com.akp.aonestar.RetrofitAPI.GlobalAppApis;
import com.akp.aonestar.RetrofitAPI.RetrofitCallBackListenar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class E_OrderList extends AppCompatActivity {
    RecyclerView rcvOrdertlist;
    LinearLayoutManager layoutManager;
    RelativeLayout rlHeader;
    private ArrayList<HashMap<String, String>> arrOrderList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eorder_list);
        findViewById();
        OrderList();
    }

    private void findViewById() {
        rcvOrdertlist = findViewById(R.id.rcvOrderList);
        rlHeader = findViewById(R.id.rlHeader);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        username = sharedPreferences.getString("U_id", "");
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public ProductAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrOrderList) {
            data = arrOrderList;
        }


        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_orde_list, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final ProductHolder holder, final int position) {
            holder.orderiddetail.setText(data.get(position).get("OrderId"));
            holder.tvdatedetail.setText(data.get(position).get("OrderDate"));
            holder.tvstatusdetail.setText(data.get(position).get("OrderStatus"));


            if (data.get(position).get("DispatchStatus").equalsIgnoreCase("false")){
                holder.track_btn.setVisibility(View.VISIBLE);
                holder.track_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(), Web_OderDetailsTracking.class);
                        intent.putExtra("awb_number",data.get(position).get("awb_number"));
                        startActivity(intent);
                    }
                });}


       /*     if (data.get(position).get("OrderStatus").equalsIgnoreCase("Placed")){
                holder.cancel_tv.setVisibility(View.VISIBLE);
                holder.cancel_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(E_OrderList.this).setTitle("Order Cancellation😪😪😪")
                                .setMessage("Are you sure want to Cancel Your Order?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                        CancelList(data.get(position).get("OrderId"));                                        dialog.cancel();
                                    } }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    } });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                });
            }
            else {
                holder.cancel_tv.setVisibility(View.INVISIBLE);
            }*/

            holder.tvpricedetail.setText(data.get(position).get("TotalAmount"));

            holder.orderlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),E_OrderDetailsActivity.class);
                    intent.putExtra("OrderId",data.get(position).get("OrderId"));
                    startActivity(intent);
                }});

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class ProductHolder extends RecyclerView.ViewHolder {
        CardView orderlist;
        TextView orderiddetail, tvpricedetail, tvdatedetail, tvstatusdetail,cancel_tv;
        Button track_btn;

        public ProductHolder(View itemView) {
            super(itemView);
            orderlist = itemView.findViewById(R.id.orderlist);
            orderiddetail = itemView.findViewById(R.id.orderiddetail);
            tvpricedetail = itemView.findViewById(R.id.tvpricedetail);
            tvdatedetail = itemView.findViewById(R.id.tvdatedetail);
            tvstatusdetail = itemView.findViewById(R.id.tvstatusdetail);
            cancel_tv = itemView.findViewById(R.id.cancel_tv);
            track_btn = itemView.findViewById(R.id.track_btn);
        }
    }
    public void OrderList() {
        String otp1 = new GlobalAppApis().OrderList(username);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.OrderList_ECOM(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("OrderList_ECOM", "getPurchaseMessage: " + result);

//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    arrOrderList.clear();
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");

                    if (Status.equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("Response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("OrderDate", jsonObject2.getString("OrderDate"));
                            hm.put("OrderId", jsonObject2.getString("OrderId"));
                            hm.put("OrderStatus", jsonObject2.getString("OrderStatus"));
                            hm.put("TotalAmount", jsonObject2.getString("TotalAmount"));
                            hm.put("DispatchStatus", jsonObject2.getString("DispatchStatus"));
                            hm.put("awb_number", jsonObject2.getString("awb_number"));
                            arrOrderList.add(hm);

                        }


                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        ProductAdapter productAdapter = new ProductAdapter(getApplicationContext(), arrOrderList);
                        rcvOrdertlist.setLayoutManager(layoutManager);
                        rcvOrdertlist.setAdapter(productAdapter);


                    } else {

                        Toast.makeText(E_OrderList.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, E_OrderList.this, call1, "", true);


    }




   /* public void CancelList(String ordid) {
        String otp1 = new GlobalAppApis().OrderCancel(username,ordid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.OrderCancelAPI(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("rescancel",result);

//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("objorder");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        Toast.makeText(E_OrderList.this, jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
//                        }
                    } else {
                        Toast.makeText(E_OrderList.this, jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, E_OrderList.this, call1, "", true);


    }*/
}