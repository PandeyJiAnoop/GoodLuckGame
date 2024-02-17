package com.akp.aonestar.Ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.New_ShopIt.BasicPrimeCartPayment;
import com.akp.aonestar.PhonpayWeb.PhonePay_AddWallet;
import com.akp.aonestar.PhonpayWeb.PhonePay_EcommerceOrderPayment;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class E_Adress_PlaceOrder extends AppCompatActivity {
    private String UserId,UserName;
    EditText name_et,mob_et,email_et,address_et,pincode_et,street_et,loc_et,utr_et;
    AppCompatButton Add_button;
    private ClipboardManager myClipboard;
    private RelativeLayout referral_rl;
    private ClipData myClip;
    TextView upiid_tv;
    RadioButton rb_COD,rb_payNow,rb_cash;
    TextView service_tv,tvPayAbleAmmount;
    String Getservice_tv,GettvPayAbleAmmount,Getonlyamount;

    ImageView image_qr;
    ImageView ivBack;
    String AddressID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eadress_place_order);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        UserName= sharedPreferences.getString("U_name", "");

        Getservice_tv=getIntent().getStringExtra("service_tv");
        GettvPayAbleAmmount=getIntent().getStringExtra("tvPayAbleAmmount");
        Getonlyamount=getIntent().getStringExtra("onlyamount");
        FindId();
        OnClick();
        GETQRDetails();
        GetMemberAddressDetails();

    }

    private void OnClick() {
        rb_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Your are not a Prime Member,So this facility is not available to you!",Toast.LENGTH_LONG).show();
            }
        });
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

                    getProductDetails();

                   /* if(utr_et.getText().toString().equalsIgnoreCase("")){
                        utr_et.setError("Fields can't be blank!");
                        utr_et.requestFocus();
                    }
                    else {
                        getProductDetails("1","Cash",AddressID,
                                "","","",GettvPayAbleAmmount);
                    }*/
                }
            }
        });
        referral_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String text = upiid_tv.getText().toString();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Link Copied", Toast.LENGTH_SHORT).show();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void FindId() {
        ivBack= findViewById(R.id.ivBack);
        image_qr= findViewById(R.id.image_qr);
        rb_payNow = findViewById(R.id.rb_payNow);
        rb_COD = findViewById(R.id.rb_COD);
        rb_cash = findViewById(R.id.rb_cash);
        name_et=findViewById(R.id.name_et);
        mob_et=findViewById(R.id.mob_et);
        email_et=findViewById(R.id.email_et);
        address_et=findViewById(R.id.address_et);
        pincode_et=findViewById(R.id.pincode_et);
        street_et=findViewById(R.id.street_et);
        loc_et=findViewById(R.id.loc_et);
        utr_et=findViewById(R.id.utr_et);
        upiid_tv=findViewById(R.id.upiid_tv);

        service_tv=findViewById(R.id.service_tv);
        tvPayAbleAmmount=findViewById(R.id.tvPayAbleAmmount);
        referral_rl=findViewById(R.id.referral_rl);

        service_tv.setText(Getservice_tv);
        tvPayAbleAmmount.setText(GettvPayAbleAmmount);
        Add_button=findViewById(R.id.btn_CreateOrder);

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

    }

    public void GetMemberAddressDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "MemberAddressDetails", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("add_res",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            AddressID       = jsonobject.getString("AddressID");
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
                Toast.makeText(E_Adress_PlaceOrder.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(E_Adress_PlaceOrder.this);
        requestQueue.add(stringRequest);
    }

    private void getProductDetails(String Action,String paymentmode,String addressid,String paymentStatus,
                                   String tempOrderId,String ReferenceId,String amount) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Action", Action);
            jsonObject.put("CustomerId", UserId);
            jsonObject.put("paymentmode", paymentmode);
            jsonObject.put("addressid",  addressid);
            jsonObject.put("paymentStatus",paymentStatus);
            jsonObject.put("tempOrderId", tempOrderId);
            jsonObject.put("ReferenceId", ReferenceId);
            jsonObject.put("amount", amount);
            Log.d("psadd",""+jsonObject);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.OrderPlaced_ECOM, jsonObject, response -> {
            try {
                Log.d("OrderPlaced_ECOM", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(E_Adress_PlaceOrder.this, E_HomePage.class);
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
    private void GETQRDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebServices.QRdetailsAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject2 = Response.getJSONObject(i);
                            upiid_tv.setText(jsonObject2.getString("UPIID"));
                            Glide.with(E_Adress_PlaceOrder.this).load(jsonObject2.getString("QRImg")).error(R.drawable.a_logo).into(image_qr);
                        }
                    } else {
                        Toast.makeText(E_Adress_PlaceOrder.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(E_Adress_PlaceOrder.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            } });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(E_Adress_PlaceOrder.this);
        requestQueue.add(stringRequest);
    }




    private void getProductDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", UserId);
           jsonObject.put("Name", name_et.getText().toString());
            jsonObject.put("MobileNo", mob_et.getText().toString());
            jsonObject.put("EmialId", email_et.getText().toString());
            jsonObject.put("Address", address_et.getText().toString());
            jsonObject.put("Pincode", pincode_et.getText().toString());
            jsonObject.put("Street", street_et.getText().toString());
            jsonObject.put("Landmark", loc_et.getText().toString());
            Log.d("psadd",""+jsonObject);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.MemberAddressInsertUpdate, jsonObject, response -> {
            try {
                Log.d("Shoppit_PlaceOrder_New", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
//                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(), PhonePay_EcommerceOrderPayment.class);
                    intent.putExtra("amount",Getonlyamount);
                    intent.putExtra("addid",jsonObject1.getString("AddressId"));
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

}