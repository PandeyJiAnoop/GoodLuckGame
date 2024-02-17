package com.akp.aonestar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WalletActivity extends AppCompatActivity {
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    String UserId, StartTimet, MobileNo, GameId, ivfour;
    private final ArrayList<HashMap<String, String>> walletlist = new ArrayList<>();
    RecyclerView wallet_rec;
    TextView tvWallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        wallet_rec=findViewById(R.id.wallet_rec);
        tvWallet=findViewById(R.id.wallet_amount);
        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = login_preference.getString("UserId", "");
        wallet();
        GetWalletlist();
    }
    public void GetWalletlist() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/GetWalletlist", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                walletlist.clear();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);

                    JSONArray Jarray = object.getJSONArray("mwcls");
                    if (Jarray.length() == 0) {
                        Toast.makeText(WalletActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonObject1 = Jarray.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("CustomerId", jsonObject1.getString("CustomerId"));
                            hashlist.put("WalletAmt", jsonObject1.getString("WalletAmt"));
                            hashlist.put("AddedTime", jsonObject1.getString("AddedTime"));
                            walletlist.add(hashlist);

                        }
                    }

                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    MyOrderListAdapter adapter = new MyOrderListAdapter(getApplicationContext(), walletlist);
                    wallet_rec.setLayoutManager(layoutManager);
                    wallet_rec.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(WalletActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);


                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(WalletActivity.this);
        requestQueue.add(stringRequest);

    }
    private class MyOrderListAdapter extends RecyclerView.Adapter<OrderList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public MyOrderListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> walletlist) {
            data = walletlist;
        }


        public OrderList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OrderList(LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_history_list, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final OrderList holder, final int position) {

            holder.tvDate.setText(data.get(position).get("AddedTime"));
            holder.tvPaidAmount.setText(data.get(position).get("WalletAmt") + " added to your wallet successfully");

        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class OrderList extends RecyclerView.ViewHolder {

        TextView tvCustmore, tvDate, tvPaidAmount;

        public OrderList(View itemView) {
            super(itemView);
            tvPaidAmount = itemView.findViewById(R.id.tvPaidAmount);
            tvCustmore = itemView.findViewById(R.id.tvCustmore);
            tvDate = itemView.findViewById(R.id.tvDate);


        }
    }

    public void wallet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/GetDashboard", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();

                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {

                    JSONObject object = new JSONObject(jsonString);

                    int TimeDiff = Integer.parseInt(object.getString("TimeDiff"));


                    String WalletBalance = object.getString("WalletBalance");
                    String Status = object.getString("Status");
                    if (Status.equalsIgnoreCase("true"))
                        tvWallet.setText(" Wallet Amount Rs." + WalletBalance);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(WalletActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("CustomerId", UserId);


                Log.v("addadada", String.valueOf(params));
                return params;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(WalletActivity.this);
        requestQueue.add(stringRequest);

    }

}