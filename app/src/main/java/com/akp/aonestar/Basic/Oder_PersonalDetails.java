package com.akp.aonestar.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akp.aonestar.GoodLuckModule.SeasonalSaleProduct;
import com.akp.aonestar.PlayBuyModule.NetworkManager.MyVolleySingleton;
import com.akp.aonestar.PlayBuyModule.NetworkManager.WebServices;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Oder_PersonalDetails extends AppCompatActivity {
    String GetproductCode,GetUserId,GetproductDisAmt,GetproductPrice,Getproductname,GetproductImage,GetProductDiscount;
    AppCompatButton Add_button;
    private String UserId,UserName,Getclass_name,GetColor,GetSize;
EditText name_et,mob_et,email_et,address_et,pincode_et,street_et,loc_et;
RelativeLayout header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder__personal_details);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        UserName= sharedPreferences.getString("U_name", "");
        name_et=findViewById(R.id.name_et);
        mob_et=findViewById(R.id.mob_et);
        email_et=findViewById(R.id.email_et);
        address_et=findViewById(R.id.address_et);
        pincode_et=findViewById(R.id.pincode_et);
        street_et=findViewById(R.id.street_et);
        loc_et=findViewById(R.id.loc_et);
        header=findViewById(R.id.header);
        Add_button=findViewById(R.id.Add_button);

        GetproductCode=getIntent().getStringExtra("p_code");
        Getclass_name=getIntent().getStringExtra("class_name");
        GetColor=getIntent().getStringExtra("color");
        GetSize=getIntent().getStringExtra("size");
        GetProductDiscount=getIntent().getStringExtra("p_extradisamount");
        GetProfile();
        GetMemberAddressDetails();
        Add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_et.getText().toString().equalsIgnoreCase("")){
                    name_et.setError("Fields can't be blank!");
                    name_et.requestFocus();
                }
                else if (mob_et.getText().toString().equalsIgnoreCase("")){
                    mob_et.setError("Fields can't be blank!");
                    mob_et.requestFocus();
                }
                 else if (address_et.getText().toString().equalsIgnoreCase("")){
                    address_et.setError("Fields can't be blank!");
                    address_et.requestFocus();
                } else if (pincode_et.getText().toString().equalsIgnoreCase("")){
                    pincode_et.setError("Fields can't be blank!");
                    pincode_et.requestFocus();
                } else if (street_et.getText().toString().equalsIgnoreCase("")){
                    street_et.setError("Fields can't be blank!");
                    street_et.requestFocus();
                } else if (loc_et.getText().toString().equalsIgnoreCase("")){
                    loc_et.setError("Fields can't be blank!");
                    loc_et.requestFocus();
                }
                else {
                    Intent intent=new Intent(getApplicationContext(), Oder_Summery_Details.class);
                    intent.putExtra("p_code",GetproductCode);
                    intent.putExtra("address_et",address_et.getText().toString());
                    intent.putExtra("pincode_et",pincode_et.getText().toString());
                    intent.putExtra("street_et",street_et.getText().toString());
                    intent.putExtra("loc_et",loc_et.getText().toString());
                    intent.putExtra("name_et",name_et.getText().toString());
                    intent.putExtra("email_et",email_et.getText().toString());
                    intent.putExtra("mob_et",mob_et.getText().toString());
                    intent.putExtra("class_name",Getclass_name);
                    intent.putExtra("p_extradisamount",GetProductDiscount);
                    intent.putExtra("type","add");
                    intent.putExtra("color",GetColor);
                    intent.putExtra("size",GetSize);
                    startActivity(intent);
                }
            }
        });

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        getProductDetails();

    }

    private void getProductDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProductCode", GetproductCode);
            jsonObject.put("UserId", GetUserId);
            jsonObject.put("Discount", GetproductDisAmt);
            jsonObject.put("Amount", GetproductPrice);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.GOODLUCK_Seasonal_ScratchGetPlay_PurchaseAPI, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Oder_PersonalDetails.this, GoodLuckGameProductView.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, response.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> Log.e("TAG", "onErrorResponse: " + error.toString()));
        MyVolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }






    public void GetProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "ProfileView", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            String UserID       = jsonobject.getString("UserID");
                            String MemberName    = jsonobject.getString("MemberName");
                            String MobileNo      = jsonobject.getString("MobileNo");
                            String EmaiLID       = jsonobject.getString("EmaiLID");
                            String RefrerralId       = jsonobject.getString("RefrerralId");
                            String RegDate    = jsonobject.getString("RegDate");

                            String state       = jsonobject.getString("State");
                            String city       = jsonobject.getString("City");
                            String pinCode    = jsonobject.getString("PinCode");
                            name_et.setText(MemberName);
                            mob_et.setText(MobileNo);
                            email_et.setText(EmaiLID);
                            loc_et.setText(city);
                            pincode_et.setText(pinCode);
                            Toast.makeText(getApplicationContext(),object.getString("Msg"),Toast.LENGTH_LONG).show();

                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Oder_PersonalDetails.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Oder_PersonalDetails.this);
        requestQueue.add(stringRequest);
    }



    public void GetMemberAddressDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "MemberAddressDetails", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            String MemberId       = jsonobject.getString("MemberId");
                            String Name    = jsonobject.getString("Name");
                            String MobileNo      = jsonobject.getString("MobileNo");
                            String EmialId       = jsonobject.getString("EmialId");
                            String Address       = jsonobject.getString("Address");
                            String Pincode    = jsonobject.getString("Pincode");
                            String Street       = jsonobject.getString("Street");
                            String Landmark       = jsonobject.getString("Landmark");

                            name_et.setText(Name);
                            mob_et.setText(MobileNo);
                            email_et.setText(EmialId);
                            address_et.setText(Address);
                            pincode_et.setText(Pincode);
                            street_et.setText(Street);
                            loc_et.setText(Landmark);
                            Toast.makeText(getApplicationContext(),object.getString("Msg"),Toast.LENGTH_LONG).show();

                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Oder_PersonalDetails.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Oder_PersonalDetails.this);
        requestQueue.add(stringRequest);
    }

}