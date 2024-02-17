package com.akp.aonestar.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.WalletReport.WalletHistory;
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

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {
    AppCompatButton btnExit,btnSubmit;
    EditText email_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        findViewId();
        Clicklistner(); }
    private void Clicklistner() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_et.getText().toString().equalsIgnoreCase("")) {
                    email_et.setError("Fields can't be blank!");
                    email_et.requestFocus();
                } else {
                    ForgotAPI();
                }
            }
        });
    }

    public void ForgotAPI(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"ForgetPasswordSendSms", new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) { Log.d("WalletToWalletTransfer",""+response);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            Toast.makeText(getApplicationContext(),jsonobject.getString("Msg"),Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),LoginScreenAkp.class);
                            startActivity(intent);

                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(ForgotPassword.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MobileNumber",email_et.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
        requestQueue.add(stringRequest);
    }

    private void findViewId() {
        btnExit=findViewById(R.id.btnExit);
        btnSubmit=findViewById(R.id.btnSubmit);
        email_et=findViewById(R.id.email_et);
    }
}