package com.akp.aonestar.PlayBuyModule;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.FullImagePage;
import com.akp.aonestar.Basic.GameSelection;
import com.akp.aonestar.Basic.GoodLuckGameProductView;
import com.akp.aonestar.Basic.LoginScreenAkp;
import com.akp.aonestar.Basic.Oder_Summery_Details;
import com.akp.aonestar.Home.AddToCart;
import com.akp.aonestar.PlayBuyModule.NetworkManager.MyVolleySingleton;
import com.akp.aonestar.PlayBuyModule.NetworkManager.WebServices;
import com.akp.aonestar.PriceDropModule.PriceDrop_Oder_PersonalDetails;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayBuy_Oder_Summery_Details extends AppCompatActivity {
    Button btn_CreateOrder,btn_addtocart;
    String GetproductCode,GetUserId;
    ImageView img,image_qr,ivBack;
    TextView name,code,amount,mrp,add_1,add_2,add_3,total_price_tv,dis_price_tv,service_tv,delivery_tv,txt_payableAmount,upiid_tv;
    String Getaddress_et,Getpincode_et,Getstreet_et,Getloc_et,Getname_et,Getemail_et,Getmob_et;
    private ClipboardManager myClipboard;
    private RelativeLayout referral_rl;
    private ClipData myClip;
    LinearLayout llAddress;   RadioButton rb_COD,rb_payNow,rb_cash;
    String UserId,Gettype;   String PaymentMethod="Online",paymentmode="Wallet";
    LinearLayout qr_layout_ll;
    EditText utr_et;
    String final_payablemaount,Getclass_name,productMrp,discount,GetColor,GetSize;
    ImageView edit_img;
    Spinner size_et,color_et;
    ArrayList<String> ProductSizeValue = new ArrayList<>();
    ArrayList<String> ProductColor = new ArrayList<>();
    private boolean alreadyExecuted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_buy__oder__summery__details);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        GetproductCode=getIntent().getStringExtra("p_code");
        Getclass_name=getIntent().getStringExtra("class_name");


//        Toast.makeText(getApplicationContext(), GetproductCode, Toast.LENGTH_SHORT).show();


        GetUserId=getIntent().getStringExtra("p_userid");
        Gettype=getIntent().getStringExtra("playbuy_type");

//        GetproductPrice=getIntent().getStringExtra("p_price");
//
//        Getproductname=getIntent().getStringExtra("p_name");
//        GetproductImage=getIntent().getStringExtra("p_img");

        Getaddress_et=getIntent().getStringExtra("address_et");
        Getpincode_et=getIntent().getStringExtra("pincode_et");
        Getstreet_et=getIntent().getStringExtra("street_et");
        Getloc_et=getIntent().getStringExtra("loc_et");
        Getname_et=getIntent().getStringExtra("name_et");
        Getemail_et=getIntent().getStringExtra("email_et");
        Getmob_et=getIntent().getStringExtra("mob_et");
        GetColor=getIntent().getStringExtra("color");
        GetSize=getIntent().getStringExtra("size");
        findViewId();
        GETQRDetails();

        btn_addtocart=findViewById(R.id.btn_addtocart);
        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToCartAPI(UserId,GetproductCode);
            }
        });

        GetSize();
        GetColor();
        size_et.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String imc_met=size_et.getSelectedItem().toString();
//                Toast.makeText(Oder_Summery_Details.this, imc_met + " selected size", Toast.LENGTH_SHORT).show();


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        color_et.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String imc_met1=color_et.getSelectedItem().toString();
//                Toast.makeText(Oder_Summery_Details.this, imc_met1 + " selected color", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        if (Gettype == null){

        }
        else {
            btn_addtocart.setVisibility(View.INVISIBLE);
            btn_CreateOrder.setText("Place Order >>");
        }

        if (GetSize == null){

        }
        else {
//            size_et.setText(GetSize);

            ProductSizeValue.add(GetSize);
            size_et.setClickable(false);
            size_et.setFocusable(false);
            size_et.setEnabled(false);
        }

        if (GetColor == null){

        }
        else {
//            color_et.setText(GetColor);
//            ProductColor.clear();
            ProductColor.add(GetColor);
            color_et.setClickable(false);
            color_et.setFocusable(false);
            color_et.setEnabled(false);
        }

        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
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
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), GameSelection.class);
                startActivity(intent);
            }
        });
        if (Getaddress_et == null){
        }
        else {
            llAddress.setVisibility(View.VISIBLE);
        }

        rb_COD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qr_layout_ll.setVisibility(View.GONE);
                PaymentMethod="1";
                paymentmode="Wallet";
                if (UserId.equalsIgnoreCase("")){
                    Intent intents=new Intent(getApplicationContext(), LoginScreenAkp.class);
                    startActivity(intents);
                }
                else {
//                    Intent intent1=new Intent(getApplicationContext(),OnlinePaymentActivity.class);
//                    intent1.putExtra("PayableAmt",PayableAmt);
//                    intent1.putExtra("token_gen",genrated_token);
//                    intent1.putExtra("orderid",ProductId);
//                    intent1.putExtra("userName",username);
//                    intent1.putExtra("phonenumber",phonenumber);
//                    startActivity(intent1);
                }
            }
        });
        rb_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Your are not a Prime Member,So this facility is not available to you!",Toast.LENGTH_LONG).show();

               /* new AlertDialog.Builder(PlayBuy_Oder_Summery_Details.this)
                        .setIcon(R.drawable.logo)
                        .setTitle("COD NOT AVAILABLE")
                        .setMessage("Your are not a Prime Member,So this facility is not available to you!").setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            } }).show();*/
            }
        });

        rb_payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qr_layout_ll.setVisibility(View.VISIBLE);
                PaymentMethod="2";
                paymentmode="Online";
                if (UserId.equalsIgnoreCase("")){
                    Intent intents=new Intent(getApplicationContext(), LoginScreenAkp.class);
                    startActivity(intents);
                }
                else {
//                    Intent intent1=new Intent(getApplicationContext(),OnlinePaymentActivity.class);
//                    intent1.putExtra("PayableAmt",PayableAmt);
//                    intent1.putExtra("token_gen",genrated_token);
//                    intent1.putExtra("orderid",ProductId);
//                    intent1.putExtra("userName",username);
//                    intent1.putExtra("phonenumber",phonenumber);
//                    startActivity(intent1);
                }
            }
        });
    }


    private void findViewId() {
        utr_et=findViewById(R.id.utr_et);

        qr_layout_ll=findViewById(R.id.qr_layout_ll);
        rb_payNow = findViewById(R.id.rb_payNow);
        rb_COD = findViewById(R.id.rb_COD);
        llAddress=findViewById(R.id.llAddress);
        rb_cash=findViewById(R.id.rb_cash);
        ivBack=findViewById(R.id.ivBack);
        referral_rl=findViewById(R.id.referral_rl);
        image_qr=findViewById(R.id.image_qr);
        img=findViewById(R.id.img);
        name=findViewById(R.id.name);
        code=findViewById(R.id.code);
        amount=findViewById(R.id.amount);
        mrp=findViewById(R.id.mrp);
        edit_img=findViewById(R.id.edit_img);
        add_1=findViewById(R.id.add_1);
        add_2=findViewById(R.id.add_2);
        add_3=findViewById(R.id.add_3);
        upiid_tv=findViewById(R.id.upiid_tv);
        total_price_tv=findViewById(R.id.total_price_tv);
        dis_price_tv=findViewById(R.id.dis_price_tv);
        service_tv=findViewById(R.id.service_tv);
        delivery_tv=findViewById(R.id.delivery_tv);
        txt_payableAmount=findViewById(R.id.txt_payableAmount);

        size_et=findViewById(R.id.size_et);
        color_et=findViewById(R.id.color_et);





//
//        edit_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_PersonalDetails.class);
//                intent.putExtra("p_code",GetproductCode);
//                intent.putExtra("class_name",Getclass_name);
//                startActivity(intent);
//            }
//        });





        final ProgressDialog progressDialog = new ProgressDialog(PlayBuy_Oder_Summery_Details.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PLAYBUYProductDeatilsbyId", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ressaws",response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String planCode = jsonObject2.getString("ProductCode");
                        String plan_name = jsonObject2.getString("ProductName");
                        productMrp = jsonObject2.getString("ProductMRP");
                        String saleRate = jsonObject2.getString("ProductSaleRate");
                        String productImg = jsonObject2.getString("ProductImg");
                        discount = jsonObject2.getString("DisPer");

                        String productSizeUnit = jsonObject2.getString("ProductSizeUnit");
                        String productSizeValue = jsonObject2.getString("ProductSizeValue");

                        String productColor = jsonObject2.getString("ProductColor");
                        String productLength = jsonObject2.getString("ProductLength");
                        String isStock = jsonObject2.getString("IsStock");

                        String serviceCharge = jsonObject2.getString("ServiceCharge");
                        String deliveryCharge = jsonObject2.getString("DeliveryCharge");



//                            ProductColorTv.setText(productColor);
//                            ProductLengthTv.setText(productLength);
//
//                            if (isStock.equalsIgnoreCase("0")){
//                                outofstok_img.setVisibility(View.VISIBLE);
//                                imageView.setAlpha((float) 0.5);
//                            }
//                            else {
//                                outofstok_img.setVisibility(View.GONE);
//
//                            }
//
//
//                            ProductSizeUnittv.setText(productSizeUnit);
//                            ProductSizeValuetv.setText(productSizeValue);
//
//                            String description = jsonObject2.getString("Description");
//                            if (description.equalsIgnoreCase("")){
//                                productdetails_tv.setText("Details Not Found!!");
//                            }
//                            else{
//                                productdetails_tv.setText(description);
//                            }

                        name.setText("Product Name:- "+plan_name);
                        code.setText("Product Code:- "+planCode);
                        amount.setText(productMrp);
                        mrp.setText(discount+" %");
                        total_price_tv.setText(saleRate);
                        service_tv.setText(serviceCharge);
                        delivery_tv.setText(deliveryCharge);
                        Glide.with(PlayBuy_Oder_Summery_Details.this)
                                .load(productImg).error(R.drawable.a_logo)
                                .into(img);

                        double akp_txt_payableAmount=Double.valueOf(saleRate)+Double.valueOf(serviceCharge)+Double.valueOf(deliveryCharge);
                        final_payablemaount= String.valueOf(akp_txt_payableAmount);
                        txt_payableAmount.setText(final_payablemaount);

                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getApplicationContext(), FullImagePage.class);
                                try {
                                    intent.putExtra("path",jsonObject2.getString("ProductImg"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                            }
                        });


                    }

                    if (jsonObject.getString("Message").equalsIgnoreCase("You have already visited.")){
                        Toast.makeText(PlayBuy_Oder_Summery_Details.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(PlayBuy_Oder_Summery_Details.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ProductId",GetproductCode);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PlayBuy_Oder_Summery_Details.this);
        requestQueue.add(stringRequest);







        add_1.setText(Getname_et+"\n"+"Mobile:- +91-"+Getmob_et+"\nEmail Id:- "+Getemail_et);
        add_2.setText("Address:- "+Getaddress_et+"\nStreet:- "+Getstreet_et+"\nLocation:- "+Getloc_et);
        add_3.setText("Pin Code:- "+ Getpincode_et);



        btn_CreateOrder=findViewById(R.id.btn_CreateOrder);



        if (Getclass_name == null){
            btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (add_3.getText().toString().equalsIgnoreCase("Pin Code:- null")){
                        Toast.makeText(getApplicationContext(),"Add Address Firstly!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_PersonalDetails.class);
                        intent.putExtra("p_code",GetproductCode);
                        intent.putExtra("class_name",Getclass_name);
                        intent.putExtra("color",color_et.getSelectedItem().toString());
                        intent.putExtra("size",size_et.getSelectedItem().toString());
                        startActivity(intent);
                    }
                    else {

                        llAddress.setVisibility(View.VISIBLE);
                        btn_addtocart.setVisibility(View.INVISIBLE);
                        btn_CreateOrder.setText("Place Order >>");
                        btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(utr_et.getText().toString().equalsIgnoreCase("")){
                                    utr_et.setError("Fields can't be blank!");
                                    utr_et.requestFocus();
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetails(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }
                                }
                               /* if (paymentmode.equalsIgnoreCase("Online")){
                                    if(utr_et.getText().toString().equalsIgnoreCase("")){
                                        utr_et.setError("Fields can't be blank!");
                                        utr_et.requestFocus();
                                    }
                                    else {
                                        if(!alreadyExecuted) {
                                            getProductDetails(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                            alreadyExecuted = true;
                                        }
                                    }
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetails(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }
                                }*/
                            }
                        });

                    }


                }
            });
        }




        if (Getclass_name.equalsIgnoreCase("cart")){
            btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (add_3.getText().toString().equalsIgnoreCase("Pin Code:- null")){
                        Toast.makeText(getApplicationContext(),"Add Address Firstly!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_PersonalDetails.class);
                        intent.putExtra("p_code",GetproductCode);
                        intent.putExtra("class_name",Getclass_name);
                        intent.putExtra("color",color_et.getSelectedItem().toString());
                        intent.putExtra("size",size_et.getSelectedItem().toString());
                        startActivity(intent);
                    }
                    else {
                        llAddress.setVisibility(View.VISIBLE);
                        btn_addtocart.setVisibility(View.INVISIBLE);
                        btn_CreateOrder.setText("Place Order >>");
                        btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(utr_et.getText().toString().equalsIgnoreCase("")){
                                    utr_et.setError("Fields can't be blank!");
                                    utr_et.requestFocus();
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetails(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }

                                }
                              /*  if (paymentmode.equalsIgnoreCase("Online")){
                                    if(utr_et.getText().toString().equalsIgnoreCase("")){
                                        utr_et.setError("Fields can't be blank!");
                                        utr_et.requestFocus();
                                    }
                                    else {
                                        if(!alreadyExecuted) {
                                            getProductDetails(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                            alreadyExecuted = true;
                                        }

                                    }
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetails(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }

                                }*/
                            }
                        });
                    }
                }
            });
        }


        if (Getclass_name.equalsIgnoreCase("scretchget")){
            btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (add_3.getText().toString().equalsIgnoreCase("Pin Code:- null")){
                        Toast.makeText(getApplicationContext(),"Add Address Firstly!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_PersonalDetails.class);
                        intent.putExtra("p_code",GetproductCode);
                        intent.putExtra("class_name",Getclass_name);
                        intent.putExtra("color",color_et.getSelectedItem().toString());
                        intent.putExtra("size",size_et.getSelectedItem().toString());
                        startActivity(intent);
                    }
                    else {
                        
                        llAddress.setVisibility(View.VISIBLE);
                        btn_addtocart.setVisibility(View.INVISIBLE);
                        btn_CreateOrder.setText("Place Order >>");
                        btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(utr_et.getText().toString().equalsIgnoreCase("")){
                                    utr_et.setError("Fields can't be blank!");
                                    utr_et.requestFocus();
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetails(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }

                                }
                              /*  if (paymentmode.equalsIgnoreCase("Online")){
                                    if(utr_et.getText().toString().equalsIgnoreCase("")){
                                        utr_et.setError("Fields can't be blank!");
                                        utr_et.requestFocus();
                                    }
                                    else {
                                        if(!alreadyExecuted) {
                                            getProductDetails(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                            alreadyExecuted = true;
                                        }

                                    }
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetails(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }

                                }*/
                            }
                        });

                    }


                }
            });
        }


        if (Getclass_name.equalsIgnoreCase("forthewin")){
            btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (add_3.getText().toString().equalsIgnoreCase("Pin Code:- null")){
                        Toast.makeText(getApplicationContext(),"Add Address Firstly!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_PersonalDetails.class);
                        intent.putExtra("p_code",GetproductCode);
                        intent.putExtra("class_name",Getclass_name);
                        intent.putExtra("color",color_et.getSelectedItem().toString());
                        intent.putExtra("size",size_et.getSelectedItem().toString());
                        startActivity(intent);
                    }
                    else { 
                        llAddress.setVisibility(View.VISIBLE);
                        btn_addtocart.setVisibility(View.INVISIBLE);
                        btn_CreateOrder.setText("Place Order >>");
                        btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(utr_et.getText().toString().equalsIgnoreCase("")){
                                    utr_et.setError("Fields can't be blank!");
                                    utr_et.requestFocus();
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetailsForthewin(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }

                                }
                                /*if (paymentmode.equalsIgnoreCase("Online")){
                                    if(utr_et.getText().toString().equalsIgnoreCase("")){
                                        utr_et.setError("Fields can't be blank!");
                                        utr_et.requestFocus();
                                    }
                                    else {
                                        if(!alreadyExecuted) {
                                            getProductDetailsForthewin(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                            alreadyExecuted = true;
                                        }

                                    }
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetailsForthewin(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }

                                }*/
                            }
                        });

                    }


                }
            });
        }


        if (Getclass_name.equalsIgnoreCase("tossboss")){
            btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (add_3.getText().toString().equalsIgnoreCase("Pin Code:- null")){
                        Toast.makeText(getApplicationContext(),"Add Address Firstly!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_PersonalDetails.class);
                        intent.putExtra("p_code",GetproductCode);
                        intent.putExtra("class_name",Getclass_name);
                        intent.putExtra("color",color_et.getSelectedItem().toString());
                        intent.putExtra("size",size_et.getSelectedItem().toString());
                        startActivity(intent);
                    }
                    else { 
                        llAddress.setVisibility(View.VISIBLE);
                        btn_addtocart.setVisibility(View.INVISIBLE);
                        btn_CreateOrder.setText("Place Order >>");
                        btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(utr_et.getText().toString().equalsIgnoreCase("")){
                                    utr_et.setError("Fields can't be blank!");
                                    utr_et.requestFocus();
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetailsTossboss(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }
                                }
                               /* if (paymentmode.equalsIgnoreCase("Online")){
                                    if(utr_et.getText().toString().equalsIgnoreCase("")){
                                        utr_et.setError("Fields can't be blank!");
                                        utr_et.requestFocus();
                                    }
                                    else {
                                        if(!alreadyExecuted) {
                                            getProductDetailsTossboss(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                            alreadyExecuted = true;
                                        }
                                    }
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetailsTossboss(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }

                                }*/
                            }
                        });

                    }


                }
            });
        }


        if (Getclass_name.equalsIgnoreCase("matchget")){
            btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (add_3.getText().toString().equalsIgnoreCase("Pin Code:- null")){
                        Toast.makeText(getApplicationContext(),"Add Address Firstly!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_PersonalDetails.class);
                        intent.putExtra("p_code",GetproductCode);
                        intent.putExtra("class_name",Getclass_name);
                        intent.putExtra("color",color_et.getSelectedItem().toString());
                        intent.putExtra("size",size_et.getSelectedItem().toString());
                        startActivity(intent);
                    }
                    else { 
                        llAddress.setVisibility(View.VISIBLE);
                        btn_addtocart.setVisibility(View.INVISIBLE);
                        btn_CreateOrder.setText("Place Order >>");
                        btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(utr_et.getText().toString().equalsIgnoreCase("")){
                                    utr_et.setError("Fields can't be blank!");
                                    utr_et.requestFocus();
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetailsMatchandget(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }

                                }

                               /* if (paymentmode.equalsIgnoreCase("Online")){
                                    if(utr_et.getText().toString().equalsIgnoreCase("")){
                                        utr_et.setError("Fields can't be blank!");
                                        utr_et.requestFocus();
                                    }
                                    else {
                                        if(!alreadyExecuted) {
                                            getProductDetailsMatchandget(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                            alreadyExecuted = true;
                                        }

                                    }
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetailsMatchandget(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }

                                }*/
                            }
                        });

                    }


                }
            });
        }

        if (Getclass_name.equalsIgnoreCase("SundaySpecial")){
            btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (add_3.getText().toString().equalsIgnoreCase("Pin Code:- null")){
                        Toast.makeText(getApplicationContext(),"Add Address Firstly!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_PersonalDetails.class);
                        intent.putExtra("p_code",GetproductCode);
                        intent.putExtra("class_name",Getclass_name);
                        intent.putExtra("color",color_et.getSelectedItem().toString());
                        intent.putExtra("size",size_et.getSelectedItem().toString());
                        startActivity(intent);
                    }
                    else {
                        llAddress.setVisibility(View.VISIBLE);
                        btn_addtocart.setVisibility(View.INVISIBLE);
                        btn_CreateOrder.setText("Place Order >>");
                        btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(utr_et.getText().toString().equalsIgnoreCase("")){
                                    utr_et.setError("Fields can't be blank!");
                                    utr_et.requestFocus();
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetailsSpecialSun(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }
                                }
                               /* if (paymentmode.equalsIgnoreCase("Online")){
                                    if(utr_et.getText().toString().equalsIgnoreCase("")){
                                        utr_et.setError("Fields can't be blank!");
                                        utr_et.requestFocus();
                                    }
                                    else {
                                        if(!alreadyExecuted) {
                                            getProductDetailsSpecialSun(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                            alreadyExecuted = true;
                                        }
                                    }
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetailsSpecialSun(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }
                                }*/
                            }
                        });

                    }


                }
            });
        }



        if (Getclass_name.equalsIgnoreCase("threeinone")){
            btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (add_3.getText().toString().equalsIgnoreCase("Pin Code:- null")){
                        Toast.makeText(getApplicationContext(),"Add Address Firstly!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), PlayBuy_Oder_PersonalDetails.class);
                        intent.putExtra("p_code",GetproductCode);
                        intent.putExtra("class_name",Getclass_name);
                        intent.putExtra("color",color_et.getSelectedItem().toString());
                        intent.putExtra("size",size_et.getSelectedItem().toString());
                        startActivity(intent);
                    }
                    else {
                        
                        llAddress.setVisibility(View.VISIBLE);
                        btn_addtocart.setVisibility(View.INVISIBLE);
                        btn_CreateOrder.setText("Place Order >>");
                        btn_CreateOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(utr_et.getText().toString().equalsIgnoreCase("")){
                                    utr_et.setError("Fields can't be blank!");
                                    utr_et.requestFocus();
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetailsThreeinone(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }
                                }
                               /* if (paymentmode.equalsIgnoreCase("Online")){
                                    if(utr_et.getText().toString().equalsIgnoreCase("")){
                                        utr_et.setError("Fields can't be blank!");
                                        utr_et.requestFocus();
                                    }
                                    else {
                                        if(!alreadyExecuted) {
                                            getProductDetailsThreeinone(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                            alreadyExecuted = true;
                                        }
                                    }
                                }
                                else {
                                    if(!alreadyExecuted) {
                                        getProductDetailsThreeinone(discount,productMrp,service_tv.getText().toString(),delivery_tv.getText().toString());
                                        alreadyExecuted = true;
                                    }
                                }*/
                            }
                        });

                    }


                }
            });
        }








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
                            Glide.with(PlayBuy_Oder_Summery_Details.this).load(jsonObject2.getString("QRImg")).error(R.drawable.a_logo).into(image_qr);
                        }
                    } else {
                        Toast.makeText(PlayBuy_Oder_Summery_Details.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlayBuy_Oder_Summery_Details.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PlayBuy_Oder_Summery_Details.this);
        requestQueue.add(stringRequest);
    }

    private void getProductDetails(String p_dis,String p_price,String p_service,String p_del) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProductCode", GetproductCode);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Discount", p_dis);
            jsonObject.put("Amount",  p_price);

            jsonObject.put("OrderAmount", txt_payableAmount.getText().toString());
            jsonObject.put("IsWallet", "Online");
            jsonObject.put("UtrNo", utr_et.getText().toString());
            jsonObject.put("Name", Getname_et);
            jsonObject.put("MobileNo", Getmob_et);
            jsonObject.put("EmialId", Getemail_et);
            jsonObject.put("Address", Getaddress_et);
            jsonObject.put("Pincode", Getpincode_et);

            jsonObject.put("Street", Getstreet_et);
            jsonObject.put("Landmark", Getloc_et);
            jsonObject.put("ServiceCharge", p_service);
            jsonObject.put("DeliveryFee", p_del);
            jsonObject.put("Size", size_et.getSelectedItem().toString());
            jsonObject.put("Color", color_et.getSelectedItem().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ScratchGetPlay_Purchase, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(PlayBuy_Oder_Summery_Details.this, GoodLuckGameProductView.class);
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

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),GameSelection.class);
        startActivity(intent);
    }

    public void AddToCartAPI(String uid,String pcode) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "Product_AddToCart", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            Toast.makeText(getApplicationContext(),jsonobject.getString("Msg"),Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(), AddToCart.class);
                            startActivity(intent);

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
                Toast.makeText(PlayBuy_Oder_Summery_Details.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",uid);
                params.put("ProductCode",pcode);
                params.put("Color",color_et.getSelectedItem().toString());
                params.put("Size",size_et.getSelectedItem().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PlayBuy_Oder_Summery_Details.this);
        requestQueue.add(stringRequest);
    }














    private void getProductDetailsForthewin(String p_dis,String p_price,String p_service,String p_del) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProductCode", GetproductCode);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Discount", p_dis);
            jsonObject.put("Amount",  p_price);

            jsonObject.put("OrderAmount", txt_payableAmount.getText().toString());
            jsonObject.put("IsWallet", "Online");
            jsonObject.put("UtrNo", utr_et.getText().toString());
            jsonObject.put("Name", Getname_et);
            jsonObject.put("MobileNo", Getmob_et);
            jsonObject.put("EmialId", Getemail_et);
            jsonObject.put("Address", Getaddress_et);
            jsonObject.put("Pincode", Getpincode_et);

            jsonObject.put("Street", Getstreet_et);
            jsonObject.put("Landmark", Getloc_et);
            jsonObject.put("ServiceCharge", p_service);
            jsonObject.put("DeliveryFee", p_del);
            jsonObject.put("Size", size_et.getSelectedItem().toString());
            jsonObject.put("Color", color_et.getSelectedItem().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_Forthewin_Purchase, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(PlayBuy_Oder_Summery_Details.this, GoodLuckGameProductView.class);
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


    private void getProductDetailsMatchandget(String p_dis,String p_price,String p_service,String p_del) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProductCode", GetproductCode);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Discount", p_dis);
            jsonObject.put("Amount",  p_price);

            jsonObject.put("OrderAmount", txt_payableAmount.getText().toString());
            jsonObject.put("IsWallet", "Online");
            jsonObject.put("UtrNo", utr_et.getText().toString());
            jsonObject.put("Name", Getname_et);
            jsonObject.put("MobileNo", Getmob_et);
            jsonObject.put("EmialId", Getemail_et);
            jsonObject.put("Address", Getaddress_et);
            jsonObject.put("Pincode", Getpincode_et);

            jsonObject.put("Street", Getstreet_et);
            jsonObject.put("Landmark", Getloc_et);
            jsonObject.put("ServiceCharge", p_service);
            jsonObject.put("DeliveryFee", p_del);
            jsonObject.put("Size", size_et.getSelectedItem().toString());
            jsonObject.put("Color", color_et.getSelectedItem().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_MacthGet_Purchase, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(PlayBuy_Oder_Summery_Details.this, GoodLuckGameProductView.class);
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

    private void getProductDetailsSpecialSun(String p_dis,String p_price,String p_service,String p_del) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProductCode", GetproductCode);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Discount", p_dis);
            jsonObject.put("Amount",  p_price);

            jsonObject.put("OrderAmount", txt_payableAmount.getText().toString());
            jsonObject.put("IsWallet", "Online");
            jsonObject.put("UtrNo", utr_et.getText().toString());
            jsonObject.put("Name", Getname_et);
            jsonObject.put("MobileNo", Getmob_et);
            jsonObject.put("EmialId", Getemail_et);
            jsonObject.put("Address", Getaddress_et);
            jsonObject.put("Pincode", Getpincode_et);

            jsonObject.put("Street", Getstreet_et);
            jsonObject.put("Landmark", Getloc_et);
            jsonObject.put("ServiceCharge", p_service);
            jsonObject.put("DeliveryFee", p_del);
            jsonObject.put("Size", size_et.getSelectedItem().toString());
            jsonObject.put("Color", color_et.getSelectedItem().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_SundaySpecial_Purchase, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(PlayBuy_Oder_Summery_Details.this, GoodLuckGameProductView.class);
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


    private void getProductDetailsThreeinone(String p_dis,String p_price,String p_service,String p_del) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProductCode", GetproductCode);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Discount", p_dis);
            jsonObject.put("Amount",  p_price);

            jsonObject.put("OrderAmount", txt_payableAmount.getText().toString());
            jsonObject.put("IsWallet", "Online");
            jsonObject.put("UtrNo", utr_et.getText().toString());
            jsonObject.put("Name", Getname_et);
            jsonObject.put("MobileNo", Getmob_et);
            jsonObject.put("EmialId", Getemail_et);
            jsonObject.put("Address", Getaddress_et);
            jsonObject.put("Pincode", Getpincode_et);

            jsonObject.put("Street", Getstreet_et);
            jsonObject.put("Landmark", Getloc_et);
            jsonObject.put("ServiceCharge", p_service);
            jsonObject.put("DeliveryFee", p_del);
            jsonObject.put("Size", size_et.getSelectedItem().toString());
            jsonObject.put("Color", color_et.getSelectedItem().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_ThreeInonePlay_Purchase, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(PlayBuy_Oder_Summery_Details.this, GoodLuckGameProductView.class);
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

    private void getProductDetailsTossboss(String p_dis,String p_price,String p_service,String p_del) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ProductCode", GetproductCode);
            jsonObject.put("UserId", UserId);
            jsonObject.put("Discount", p_dis);
            jsonObject.put("Amount",  p_price);

            jsonObject.put("OrderAmount", txt_payableAmount.getText().toString());
            jsonObject.put("IsWallet", "Online");
            jsonObject.put("UtrNo", utr_et.getText().toString());
            jsonObject.put("Name", Getname_et);
            jsonObject.put("MobileNo", Getmob_et);
            jsonObject.put("EmialId", Getemail_et);
            jsonObject.put("Address", Getaddress_et);
            jsonObject.put("Pincode", Getpincode_et);

            jsonObject.put("Street", Getstreet_et);
            jsonObject.put("Landmark", Getloc_et);
            jsonObject.put("ServiceCharge", p_service);
            jsonObject.put("DeliveryFee", p_del);
            jsonObject.put("Size", size_et.getSelectedItem().toString());
            jsonObject.put("Color", color_et.getSelectedItem().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, WebServices.PLAYBUY_TossBoss_Purchase, jsonObject, response -> {
            try {
                Log.e("TAG", "getPurchaseMessage: " + response);
                if (response.getBoolean("Status")) {
                    JSONArray jsonArray = response.getJSONArray("Response");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    Toast.makeText(this, "" + jsonObject1.getString("Msg"), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(PlayBuy_Oder_Summery_Details.this, GoodLuckGameProductView.class);
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








    void GetSize() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://aoneshop.co.in/api/A1Shop/AllProductSizeList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String productSizeValue = jsonObject1.getString("ProductSizeValue");
                        ProductSizeValue.add(productSizeValue);
                    }

                    size_et.setAdapter(new ArrayAdapter<String>(PlayBuy_Oder_Summery_Details.this, android.R.layout.simple_spinner_dropdown_item, ProductSizeValue));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ProductId",GetproductCode);
                return params;
//                "MS000002"
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PlayBuy_Oder_Summery_Details.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    void GetColor() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://aoneshop.co.in/api/A1Shop/AllProductColorList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String productColor = jsonObject1.getString("ProductColor");
                        ProductColor.add(productColor);
                    }

                    color_et.setAdapter(new ArrayAdapter<String>(PlayBuy_Oder_Summery_Details.this, android.R.layout.simple_spinner_dropdown_item, ProductColor));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("ProductId",GetproductCode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PlayBuy_Oder_Summery_Details.this);
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }




}