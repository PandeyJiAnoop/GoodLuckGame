package com.akp.aonestar.WalletReport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.GoodLuckModule.ChooseDiscountProduct;
import com.akp.aonestar.PhonpayWeb.PhonePay_AddWallet;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.Utility;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddWallet extends AppCompatActivity {
    ImageView menuImg;
    String UserId;
    ImageView image_qr;
    TextView select_tv;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;
    ImageView img_showProfile;
    EditText amount_et,transcation_number_et,date_time_et;
    AppCompatButton submit_btn;
    RecyclerView rcvList;
    ImageView norecord_tv;
    TextView tvOnee,tvthree, tvfour, tvfive, tvthrees;
    private RadioGroup radioSexGroup;
    String Wallet_Type="MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        menuImg=findViewById(R.id.menuImg);
        image_qr=findViewById(R.id.image_qr);
        amount_et=findViewById(R.id.amount_et);
        transcation_number_et=findViewById(R.id.transcation_number_et);
        date_time_et=findViewById(R.id.date_time_et);
        submit_btn=findViewById(R.id.Add_button);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);

        tvOnee = findViewById(R.id.tvOne);
//        tvtwo = findViewById(R.id.tvtwo);
        tvthree = findViewById(R.id.tvthree);
        tvfour = findViewById(R.id.tvfour);
        tvfive = findViewById(R.id.tvfive);
        tvthrees = findViewById(R.id.tvthrees);

        tvOnee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_et.setText("100");
                tvOnee.setBackgroundResource(R.drawable.selectsizebackground);
//                tvtwo.setBackgroundResource(R.drawable.backgroundd);
                tvthree.setBackgroundResource(R.drawable.backgroundd);
                tvfour.setBackgroundResource(R.drawable.backgroundd);
                tvfive.setBackgroundResource(R.drawable.backgroundd);
                tvthrees.setBackgroundResource(R.drawable.backgroundd);
            }});

//        tvtwo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                amount_et.setText("150");
//                tvOnee.setBackgroundResource(R.drawable.backgroundd);
//                tvtwo.setBackgroundResource(R.drawable.selectsizebackground);
//                tvthree.setBackgroundResource(R.drawable.backgroundd);
//                tvfour.setBackgroundResource(R.drawable.backgroundd);
//                tvfive.setBackgroundResource(R.drawable.backgroundd);
//                tvthrees.setBackgroundResource(R.drawable.backgroundd);
//
//            }
//        });
        tvthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_et.setText("200");
                tvOnee.setBackgroundResource(R.drawable.backgroundd);
//                tvtwo.setBackgroundResource(R.drawable.backgroundd);
                tvthree.setBackgroundResource(R.drawable.selectsizebackground);
                tvfour.setBackgroundResource(R.drawable.backgroundd);
                tvfive.setBackgroundResource(R.drawable.backgroundd);
                tvthrees.setBackgroundResource(R.drawable.backgroundd);
            } });
        tvfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_et.setText("500");
                tvOnee.setBackgroundResource(R.drawable.backgroundd);
//                tvtwo.setBackgroundResource(R.drawable.backgroundd);
                tvthree.setBackgroundResource(R.drawable.backgroundd);
                tvfour.setBackgroundResource(R.drawable.selectsizebackground);
                tvfive.setBackgroundResource(R.drawable.backgroundd);
                tvthrees.setBackgroundResource(R.drawable.backgroundd);
            } });
        tvfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_et.setText("2000");
                tvOnee.setBackgroundResource(R.drawable.backgroundd);
//                tvtwo.setBackgroundResource(R.drawable.backgroundd);
                tvthree.setBackgroundResource(R.drawable.backgroundd);
                tvfour.setBackgroundResource(R.drawable.backgroundd);
                tvthrees.setBackgroundResource(R.drawable.backgroundd);
                tvfive.setBackgroundResource(R.drawable.selectsizebackground);
            } });
        tvthrees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_et.setText("5000");
                tvOnee.setBackgroundResource(R.drawable.backgroundd);
//                tvtwo.setBackgroundResource(R.drawable.backgroundd);
                tvthree.setBackgroundResource(R.drawable.backgroundd);
                tvfour.setBackgroundResource(R.drawable.backgroundd);
                tvfive.setBackgroundResource(R.drawable.backgroundd);
                tvthrees.setBackgroundResource(R.drawable.selectsizebackground);
            } });

        rcvList = findViewById(R.id.rcvList);
        norecord_tv=findViewById(R.id.norecord_tv);
//        WalletHistoryAPI();

//        amount_et.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (amount_et.getText().toString().equalsIgnoreCase("")){
//                    amount_et.setError("Fields can't be blank!");
//                    amount_et.requestFocus();
//                }
//                else {
//                    if((Long.parseLong(amount_et.getText().toString().trim())<100) || (amount_et.length()==0) /*||(TextUtils.isEmpty(ets2.getText().toString()))*/){
//                        amount_et.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                        amount_et.setError("Please enter amount \n Greater than 100");
//                    }
//                }
//               }
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });

        Glide.with(AddWallet.this)
                .load("https://aoneshop.co.in/assets/Qr_AoneShop.png").error(R.drawable.a_logo)
                .into(image_qr);


        radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioMale:
                        Wallet_Type="MAIN";
                        // do operations specific to this selection
                        break;
                    case R.id.radioFemale:
                        Wallet_Type="EMI";
                        // do operations specific to this selection
                        break;
                    case R.id.radiosmart:
                        Wallet_Type="SMART";
                        // do operations specific to this selection
                        break;
                }}});

            amount_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (amount_et.getText().toString().equalsIgnoreCase("")) {
                        amount_et.setError("Please enter amount \n Greater than 100");
                        Toast.makeText(getApplicationContext(),"Please enter amount Greater than 100",Toast.LENGTH_LONG).show();
                    } else {
                        if ((Long.parseLong(amount_et.getText().toString().trim()) < 100) || (amount_et.length() == 0) /*||(TextUtils.isEmpty(ets2.getText().toString()))*/) {
                            amount_et.setError("Please enter amount \n Greater than 100");
                        } else {
                            submit_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (amount_et.getText().toString().equalsIgnoreCase("")){
                                        amount_et.setError("Fields can't be blank!");
                                        amount_et.requestFocus();
                                    }
                                    else {
                                        Intent intent=new Intent(getApplicationContext(), PhonePay_AddWallet.class);
                                        intent.putExtra("amount",amount_et.getText().toString());
                                        intent.putExtra("type",Wallet_Type);
                                        startActivity(intent);
                                   /* else if (transcation_number_et.getText().toString().equalsIgnoreCase("")){
                                        transcation_number_et.setError("Fields can't be blank!");
                                        transcation_number_et.requestFocus();

                                    }
                                    else if (date_time_et.getText().toString().equalsIgnoreCase("")){
                                        date_time_et.setError("Fields can't be blank!");
                                        date_time_et.requestFocus();
                                    }
                                    else {
                                        if (temp == null){
                                            ProceedToaddFundAPI("",Wallet_Type);
                                        }
                                        else {
                                            ProceedToaddFundAPI(temp,Wallet_Type);
                                        }*/

//                    Toast.makeText(getApplicationContext()," wait for response",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                }
                @Override
                public void afterTextChanged(Editable editable) {
                }
            });



        submit_btn.setOnClickListener(v -> {
            if (amount_et.getText().toString().equalsIgnoreCase("")) {
                amount_et.setError("Please enter amount \n Greater than 100");
                Toast.makeText(getApplicationContext(),"Please enter amount Greater than 100",Toast.LENGTH_LONG).show();
            } else {
                if ((Long.parseLong(amount_et.getText().toString().trim()) < 100) || (amount_et.length() == 0) /*||(TextUtils.isEmpty(ets2.getText().toString()))*/) {
                    amount_et.setError("Please enter amount \n Greater than 100");
                } else {
                    submit_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (amount_et.getText().toString().equalsIgnoreCase("")){
                                amount_et.setError("Fields can't be blank!");
                                amount_et.requestFocus();
                            }
                            else {
                                Intent intent=new Intent(getApplicationContext(), PhonePay_AddWallet.class);
                                intent.putExtra("amount",amount_et.getText().toString());
                                intent.putExtra("type",Wallet_Type);
                                startActivity(intent);
                           /* else if (transcation_number_et.getText().toString().equalsIgnoreCase("")){
                                transcation_number_et.setError("Fields can't be blank!");
                                transcation_number_et.requestFocus();
                            }
                            else if (date_time_et.getText().toString().equalsIgnoreCase("")){
                                date_time_et.setError("Fields can't be blank!");
                                date_time_et.requestFocus();
                            }
                            else {
                                if (temp == null){
                                    ProceedToaddFundAPI("",Wallet_Type);
                                }
                                else {
                                    ProceedToaddFundAPI(temp,Wallet_Type);
                                }*/
//                    Toast.makeText(getApplicationContext()," wait for response",Toast.LENGTH_LONG).show();
                            }}});}}});
        select_tv=findViewById(R.id.select_tv);
        img_showProfile=findViewById(R.id.img_showProfile);
        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        date_time_et.setText(date_n);
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        WalletAPI("",UserId);
        select_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }
    private void selectImage() {
        final CharSequence[] items = {"Choose from Library","Cancel" };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddWallet.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(AddWallet.this);
                if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                } }});
        builder.show();
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Toast.makeText(getApplicationContext(),""+bm,Toast.LENGTH_LONG).show();
        img_showProfile.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }




    public void ProceedToaddFundAPI(String img,String type) {
        final ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Loading...");
        progressDialog1.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"WalletAddRequest", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog1.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            Toast.makeText(AddWallet.this, jsonObject.getString("Msg"), Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddWallet.this).setTitle("Request Saved Successfully..")
                                    .setMessage("Request Sent to Admin Please Wait for Approved!!").setCancelable(false)
                                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                           finish();
                                            //startActivity(i);
                                        }});
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    } else {
                        Toast.makeText(AddWallet.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog1.dismiss();
                Toast.makeText(AddWallet.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MemberId", UserId);
                params.put("Amount", amount_et.getText().toString());
                params.put("TransactionNo", transcation_number_et.getText().toString());
                params.put("RecpFile", img);
                params.put("PaymentDateTime", date_time_et.getText().toString());
                params.put("WalletType", type);
                Log.e("TAG", "addWalletRequest: "+params);
                return params;
            } };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(AddWallet.this);
        requestQueue.add(stringRequest);

    }

}