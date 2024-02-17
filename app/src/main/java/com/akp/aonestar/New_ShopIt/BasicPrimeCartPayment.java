package com.akp.aonestar.New_ShopIt;

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
import com.akp.aonestar.Basic.Oder_PersonalDetails;
import com.akp.aonestar.Basic.Oder_Summery_Details;
import com.akp.aonestar.PhonpayWeb.PhonePay_BasicPrimeCartPayment;
import com.akp.aonestar.PlayBuyModule.NetworkManager.MyVolleySingleton;
import com.akp.aonestar.PlayBuyModule.NetworkManager.WebServices;
import com.akp.aonestar.PlayBuyModule.PlayBuy_Oder_Summery_Details;
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

public class BasicPrimeCartPayment extends AppCompatActivity {
    private String UserId,UserName;
    EditText name_et,mob_et,email_et,address_et,pincode_et,street_et,loc_et,utr_et;
    AppCompatButton Add_button;
    private ClipboardManager myClipboard;
    private RelativeLayout referral_rl;
    private ClipData myClip;
    TextView upiid_tv;
    RadioButton rb_COD,rb_payNow,rb_cash;
    TextView total_price_tv,service_tv,tvPayAbleAmmount;
    String Gettotal_price_tv,Getservice_tv,GettvPayAbleAmmount,GetGameCategory;

ImageView image_qr;
ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_prime_cart_payment);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        UserName= sharedPreferences.getString("U_name", "");

        Gettotal_price_tv=getIntent().getStringExtra("total_price_tv");
        Getservice_tv=getIntent().getStringExtra("service_tv");
        GettvPayAbleAmmount=getIntent().getStringExtra("tvPayAbleAmmount");
        GetGameCategory=getIntent().getStringExtra("GameCategory");

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
                    if (tvPayAbleAmmount.getText().toString().equalsIgnoreCase("0")){
                        getPlaceOrder("0",Gettotal_price_tv,GettvPayAbleAmmount,"Wallet",service_tv.getText().toString(),"0",GetGameCategory);
                    }
                    else {
                        getPlaceOrder("0",Gettotal_price_tv,GettvPayAbleAmmount,"Online",service_tv.getText().toString(),"0",GetGameCategory);
                    }
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

        total_price_tv=findViewById(R.id.total_price_tv);
        service_tv=findViewById(R.id.service_tv);
        tvPayAbleAmmount=findViewById(R.id.tvPayAbleAmmount);
        referral_rl=findViewById(R.id.referral_rl);

        total_price_tv.setText(Gettotal_price_tv);
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
                Toast.makeText(BasicPrimeCartPayment.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(BasicPrimeCartPayment.this);
        requestQueue.add(stringRequest);
    }

    private void getPlaceOrder(String ProductCode,String Amount,String OrderAmount,
                               String iswallet,String p_service,String p_del,String gamecat) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", UserId);
            jsonObject.put("ProductCode", ProductCode);
            jsonObject.put("Discount", "0");
            jsonObject.put("Amount",  Amount);
            jsonObject.put("OrderAmount",OrderAmount);
            jsonObject.put("IsWallet", iswallet);
            jsonObject.put("UtrNo", "");
            jsonObject.put("Name", name_et.getText().toString());
            jsonObject.put("MobileNo", mob_et.getText().toString());
            jsonObject.put("EmialId", email_et.getText().toString());
            jsonObject.put("Address", address_et.getText().toString());
            jsonObject.put("Pincode", pincode_et.getText().toString());
            jsonObject.put("Street", street_et.getText().toString());
            jsonObject.put("Landmark", loc_et.getText().toString());
            jsonObject.put("ServiceCharge", p_service);
            jsonObject.put("DeliveryFee", p_del);
            jsonObject.put("GameCategory", gamecat);
            Log.d("psadd",""+jsonObject);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.Shoppit_PlaceOrder_New, jsonObject, response -> {
            try {
                Log.d("Shoppit_PlaceOrder_New", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String OrderId=jsonObject1.getString("OrderId");
                    String mobileNumber=jsonObject1.getString("mobileNumber");
                    String PaymentType=jsonObject1.getString("PaymentType");



                    if (PaymentType.equalsIgnoreCase("Wallet")){
                    Intent intent=new Intent(BasicPrimeCartPayment.this, GoodLuckGameProductView.class);
                    startActivity(intent);
                    }
                    else {
                        Intent intent=new Intent(getApplicationContext(),PhonePay_BasicPrimeCartPayment.class);
                        intent.putExtra("orderid",OrderId);
                        intent.putExtra("amount",tvPayAbleAmmount.getText().toString());
                        intent.putExtra("mobile",mobileNumber);
                        startActivity(intent);
                    }
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();

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
                            Glide.with(BasicPrimeCartPayment.this).load(jsonObject2.getString("QRImg")).error(R.drawable.a_logo).into(image_qr);
                        }
                    } else {
                        Toast.makeText(BasicPrimeCartPayment.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BasicPrimeCartPayment.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            } });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(BasicPrimeCartPayment.this);
        requestQueue.add(stringRequest);
    }



}