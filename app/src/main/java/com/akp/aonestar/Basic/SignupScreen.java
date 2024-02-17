package com.akp.aonestar.Basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupScreen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] courses = { "Direction","Left","Right" };
    EditText sponser_et,name_et,mob_et,email_et,pass_et,c_pass_et,state_et,otp_et,pincode_et,city_et;
    TextView signin_tv;
    AppCompatButton signup_btn;
    // creating a variable for request queue.
    private RequestQueue mRequestQueue;
    TextView send_otp_tv,verify_otp_tv;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    private AlertDialog alertDialog;
    private String Referral_id;
    // in the below line, we are creating variables
    final int REQUEST_CODE = 101;
    String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);
        SharedPreferences sharedPreferences = getSharedPreferences("refer_preference",MODE_PRIVATE);
        Referral_id= sharedPreferences.getString("referral_id", "");
        // Check for permission to access device information
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            } else {
                // If permission is granted, retrieve IMEI number
                getIMEI();
            }
        }
//        Toast.makeText(getApplicationContext(),"sff"+Referral_id,Toast.LENGTH_LONG).show();
        sponser_et=findViewById(R.id.ref_et);
        name_et=findViewById(R.id.name_et);
        mob_et=findViewById(R.id.mob_et);
        email_et=findViewById(R.id.email_et);
        pass_et=findViewById(R.id.pass_et);
        c_pass_et=findViewById(R.id.c_pass_et);

        otp_et=findViewById(R.id.otp_et);
        pincode_et=findViewById(R.id.pincode_et);
        city_et=findViewById(R.id.city_et);
        state_et=findViewById(R.id.state_et);


        signup_btn=findViewById(R.id.signup_btn);
        signin_tv=findViewById(R.id.signin_tv);

        send_otp_tv=findViewById(R.id.send_otp_tv);
        verify_otp_tv=findViewById(R.id.verify_otp_tv);


        if (Referral_id.equalsIgnoreCase("")){
            sponser_et.setClickable(true);
            sponser_et.setFocusable(true);
        }
        else {
            sponser_et.setClickable(false);
            sponser_et.setFocusable(false);
            sponser_et.setText(Referral_id);
        }

        pincode_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        city_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        send_otp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendOTPAPI();
            }
        });

        verify_otp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyOTPAPI();
            }
        });



        signup_btn.setOnClickListener(new View.OnClickListener() {
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
                else if (email_et.getText().toString().equalsIgnoreCase("")){
                    email_et.setError("Fields can't be blank!");
                    email_et.requestFocus();
                }
                else if (pass_et.getText().toString().equalsIgnoreCase("")){
                    pass_et.setError("Fields can't be blank!");
                    pass_et.requestFocus();
                }
                else if (c_pass_et.getText().toString().equalsIgnoreCase("")){
                    c_pass_et.setError("Fields can't be blank!");
                    c_pass_et.requestFocus();
                }

                else if(!pass_et.getText().toString().equals(c_pass_et.getText().toString())){
                    //Toast is the pop up message
                    Toast.makeText(getApplicationContext(), "Password Not matched!", Toast.LENGTH_LONG).show();
                }
                else if (state_et.getText().toString().equalsIgnoreCase("")){
                    state_et.setError("Fields can't be blank!");
                    state_et.requestFocus();
                }
                else {
                    RegistrationService();
                }
            }
        });

        signin_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LoginScreenAkp.class);
                startActivity(intent);
            }
        });

        Spinner spin = findViewById(R.id.direction_sp);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, courses);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(ad);
    }
    // Performing action when ItemSelected
    // from spinner, Overriding onItemSelected method
    @Override
    public void onItemSelected(AdapterView arg0, View arg1, int position, long id)
    {
        Toast.makeText(getApplicationContext(),courses[position], Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView arg0)
    {
        // Auto-generated method stub
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void RegistrationService() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "Registration", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("reg_response",""+response);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            String userName = jsonObject.getString("UserName");
                            String name = jsonObject.getString("Name");
                            String mobileNo = jsonObject.getString("MobileNo");
                            String emialId = jsonObject.getString("EmialId");
                            String password = jsonObject.getString("Password");
                            login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                            login_editor = login_preference.edit();
                            login_editor.putString("U_id",userName);
                            login_editor.putString("U_name",name);
                            login_editor.commit();
                            Toast.makeText(SignupScreen.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            //then we will inflate the custom alert dialog xml that we created
                            View dialogView = LayoutInflater.from(SignupScreen.this).inflate(R.layout.successfullycreated_popup, viewGroup, false);
                            Button ok = (Button) dialogView.findViewById(R.id.btnDialog);
                            TextView txt_msg=dialogView.findViewById(R.id.txt_msg);
                            txt_msg.setText("Your User Id -("+userName+")"+"\nPassword Is - ("+password+")");
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(getApplicationContext(),LoginScreenAkp.class);
                                    startActivity(intent);
                                    alertDialog.dismiss();
                                }
                            });
                            //Now we need an AlertDialog.Builder object
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupScreen.this);
                            //setting the view of the builder to our custom view that we already inflated
                            builder.setView(dialogView);
                            //finally creating the alert dialog and displaying it
                            alertDialog = builder.create();
                            alertDialog.show();

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
                progressDialog.dismiss();
                Toast.makeText(SignupScreen.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("RefrerralId", sponser_et.getText().toString());
                params.put("MemberName",name_et.getText().toString());
                params.put("MobileNo",mob_et.getText().toString());
                params.put("EmialId",email_et.getText().toString());
                params.put("Password",pass_et.getText().toString());
                params.put("ConfirmPassword",c_pass_et.getText().toString());
                params.put("PinCode",pincode_et.getText().toString());
                params.put("City",city_et.getText().toString());
                params.put("State",state_et.getText().toString());
                params.put("IMEI",imei);
                Log.d("param", String.valueOf(params));
                return params;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SignupScreen.this);
        requestQueue.add(stringRequest);
    }


    private void getDataFromPinCode(String pinCode) {
        // below is the url from where we will be getting
        // our response in the json format.
        String url = "http://www.postalpincode.in/api/pincode/" + pinCode;

        // below line is use to initialize our request queue.
        RequestQueue queue = Volley.newRequestQueue(SignupScreen.this);

        // in below line we are creating a
        // object request using volley.
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // inside this method we will get two methods
                // such as on response method
                // inside on response method we are extracting
                // data from the json format.
                try {
                    // we are getting data of post office
                    // in the form of JSON file.
                    JSONArray postOfficeArray = response.getJSONArray("PostOffice");
                    if (response.getString("Status").equals("Error")) {
                        // validating if the response status is success or failure.
                        // in this method the response status is having error and
                        // we are setting text to TextView as invalid pincode.
                        pincode_et.setText("Pin code is not valid.");
                    } else {
                        // if the status is success we are calling this method
                        // in which we are getting data from post office object
                        // here we are calling first object of our json array.
                        JSONObject obj = postOfficeArray.getJSONObject(0);

                        // inside our json array we are getting district name,
                        // state and country from our data.
                        String district = obj.getString("District");
                        String state = obj.getString("State");
                        String country = obj.getString("Country");

                        // after getting all data we are setting this data in
                        // our text view on below line.
                        city_et.setText(district);
                        state_et.setText(state + ","+ country);
                    }
                } catch (JSONException e) {
                    // if we gets any error then it
                    // will be printed in log cat.
                    e.printStackTrace();
                    pincode_et.setText("Pin code is not valid");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // below method is called if we get
                // any error while fetching data from API.
                // below line is use to display an error message.
                Toast.makeText(SignupScreen.this, "Pin code is not valid.", Toast.LENGTH_SHORT).show();
                pincode_et.setText("Pin code is not valid");
            }
        });
        // below line is use for adding object
        // request to our request queue.
        queue.add(objectRequest);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        getDataFromPinCode(pincode_et.getText().toString());
    }




        public void VerifyOTPAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"OtpVerify", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("OtpVerify",""+response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            Toast.makeText(getApplicationContext(),jsonobject.getString("Msg"),Toast.LENGTH_LONG).show();
                        } }
                    else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupScreen.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }})
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MobileNumber",mob_et.getText().toString());
                params.put("Action","");
                params.put("Otp",otp_et.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SignupScreen.this);
        requestQueue.add(stringRequest);
    }




    public void SendOTPAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"SendOTP", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("SendOTP",""+response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            String id = jsonobject.getString("Id");
                            String mobileNo = jsonobject.getString("MobileNo");
                            String userId = jsonobject.getString("UserId");
                            String otp = jsonobject.getString("OTP");
                            String roleName = jsonobject.getString("RoleType");
//                            otp_et.setText(otp);
                            Toast.makeText(getApplicationContext(),"Enter Your OTP Send to Your Registered Mobile Number",Toast.LENGTH_LONG).show();
                        } }
                    else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignupScreen.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }})
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MobileNumber",mob_et.getText().toString());
                params.put("Action","");
                params.put("Otp","");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SignupScreen.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get IMEI
                getIMEI();
            } else {
                Toast.makeText(this, "Permission denied. Cannot retrieve IMEI.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void getIMEI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    imei = mTelephony.getImei();
//                    Toast.makeText(this, "IMEI:3 " + imei, Toast.LENGTH_SHORT).show();
                }else {
                    imei = mTelephony.getDeviceId();
//                    Toast.makeText(this, "IMEI: 2 " + imei, Toast.LENGTH_SHORT).show();
                }
            } else {
                imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//                Toast.makeText(this, "IMEI:1 " + imei, Toast.LENGTH_SHORT).show();
            }
        }
        Log.d("deviceId", imei);
//        Toast.makeText(this, "IMEI: " + imei, Toast.LENGTH_SHORT).show();

    }
}
