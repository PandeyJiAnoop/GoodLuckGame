package com.akp.aonestar.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.HashMap;
import java.util.Map;

public class OderDetailsTracking extends AppCompatActivity {
    String OderId;
    String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_details_tracking);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        OderId=getIntent().getStringExtra("order_id");
        CardView rlHeader=findViewById(R.id.rlHeader);
        TextView name =findViewById(R.id.name);
        TextView amount =findViewById(R.id.amount_tv);
        TextView discount_tv =findViewById(R.id.discount_tv);
        TextView orderamount_tv =findViewById(R.id.orderamount_tv);
        TextView p_code =findViewById(R.id.p_code);
        TextView OrderStatustv =findViewById(R.id.OrderStatustv);
        TextView UpdateDatetv =findViewById(R.id.UpdateDatetv);

        TextView title =findViewById(R.id.title);
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ProgressDialog progressDialog = new ProgressDialog(OderDetailsTracking.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GetOrderStatusbyOrderId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("GetOrderStatusbyOrderId",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String OrderId = jsonObject2.getString("OrderId");
                        String CourierName = jsonObject2.getString("CourierName");
                        String TrackingNumber = jsonObject2.getString("TrackingNumber");
                        String TrackingLink = jsonObject2.getString("TrackingLink");
                        String Note = jsonObject2.getString("Note");
                        String OrderStatus = jsonObject2.getString("OrderStatus");
                        String UpdateDate = jsonObject2.getString("UpdateDate");
                        name.setText(OrderId);
                        p_code.setText(CourierName);
                        amount.setText(TrackingNumber);
                        discount_tv.setText(TrackingLink);
                        orderamount_tv.setText(Note);
                        OrderStatustv.setText(OrderStatus);
                        UpdateDatetv.setText(UpdateDate);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(OderDetailsTracking.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",UserId);
                params.put("OrderId",OderId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(OderDetailsTracking.this);
        requestQueue.add(stringRequest);
    }


}