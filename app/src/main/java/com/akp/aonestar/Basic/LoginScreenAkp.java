package com.akp.aonestar.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginScreenAkp extends AppCompatActivity {
    AppCompatButton btnSubmit,btn_reg;
    EditText mobile_number;
    EditText ref_et,pass_et;
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    CheckBox chk;
    RelativeLayout ref_rl,mail_rl;
    TextView checkref_img;
    String Getreferral_id;
    TextView forget_tv;
    private PopupWindow popupWindow1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen_akp);
        SharedPreferences sharedPreferences = getSharedPreferences("refer_preference",MODE_PRIVATE);
        Getreferral_id= sharedPreferences.getString("referral_id", "");
        btn_reg = findViewById(R.id.btn_reg);
        btnSubmit = findViewById(R.id.btnSubmit);
        mobile_number = findViewById(R.id.mob_et);
        pass_et=findViewById(R.id.pass_et);
        ref_et=findViewById(R.id.ref_et);
        ref_rl=findViewById(R.id.ref_rl);
        checkref_img=findViewById(R.id.checkref_img);
        forget_tv=findViewById(R.id.forget_tv);
        mail_rl=findViewById(R.id.mail_rl);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupScreen.class);
                intent.putExtra("referral_id",Getreferral_id);
                startActivity(intent);
            }
        });
        forget_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(getApplicationContext(),ForgotPassword.class);
               startActivity(intent);
            }
        });
        chk = (CheckBox) findViewById(R.id.chk1);
        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    ref_rl.setVisibility(View.VISIBLE);
                    // Do your coding
                }
                else{
                    ref_rl.setVisibility(View.GONE);
                    // Do your coding
                }
            }
        });

        ref_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
//                    hideKeyboard(v);
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mobile_number.getText().toString().equals("")){
                    mobile_number.setError("Fields can't be blank!");
                    mobile_number.requestFocus();
                }
                if(pass_et.getText().toString().equals("")){
                    pass_et.setError("Fields can't be blank!");
                    pass_et.requestFocus();
                }
//                else if(mobile_number.getText().length()<10)
//                    Toast.makeText(getApplicationContext(),"Please enter correct mobile no.",Toast.LENGTH_SHORT).show();
                else{
                    sendOtp();
//                    Intent intent = new Intent(getApplicationContext(),GoodLuckGameProductView.class);
//                    startActivity(intent);

                }

            }
        });

    }
    public void sendOtp() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"Login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("dfdff",""+response);
//                Toast.makeText(LoginScreenAkp.this, response, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray resjsn = object.getJSONArray("Response");
                        for (int i = 0; i < resjsn.length(); i++) {
                            JSONObject jsonobject = resjsn.getJSONObject(i);
                            String id = jsonobject.getString("id");
                            String userName = jsonobject.getString("UserName");
                            String password = jsonobject.getString("Password");
                            String name = jsonobject.getString("Name");
                            String roleName = jsonobject.getString("RoleName");
                            login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                            login_editor = login_preference.edit();
                            login_editor.putString("U_id",userName);
                            login_editor.putString("U_name",name);
                            login_editor.commit();

                            UserOnlineOfflineAPI(userName,"1");
                            Toast.makeText(LoginScreenAkp.this, jsonobject.getString("msg"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), GameSelection.class);
                            startActivity(intent);
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
                Toast.makeText(LoginScreenAkp.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MobileNumber",mobile_number.getText().toString());
                params.put("Action","");
                params.put("Password",pass_et.getText().toString());
                params.put("Token","firebasetokengenrate");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LoginScreenAkp.this);
        requestQueue.add(stringRequest);
    }










    public void UserOnlineOfflineAPI(String uid,String uaction) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"UserOnlineOffline", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("dfdff",""+response);
//                Toast.makeText(LoginScreenAkp.this, response, Toast.LENGTH_SHORT).show();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray resjsn = object.getJSONArray("Response");
                        for (int i = 0; i < resjsn.length(); i++) {
                            JSONObject jsonobject = resjsn.getJSONObject(i);
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
                Toast.makeText(LoginScreenAkp.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",uid);
                params.put("Action",uaction);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LoginScreenAkp.this);
        requestQueue.add(stringRequest);
    }















//    public void verifyReferralAPI() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"VerifyMemberRefrerral", new Response.Listener<String>() {
//            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject object = new JSONObject(response);
//                    if (object.getString("Status").equalsIgnoreCase("true")){
//                        JSONArray Jarray = object.getJSONArray("Response");
//                        for (int i = 0; i < Jarray.length(); i++) {
//                            JSONObject jsonobject = Jarray.getJSONObject(i);
//                            checkref_img.setTextColor(Color.GREEN);
//                            checkref_img.setText(jsonobject.getString("Msg"));
//                            Toast.makeText(getApplicationContext(),jsonobject.getString("Msg"),Toast.LENGTH_LONG).show();
//                        } }
//                    else {
//                        checkref_img.setTextColor(Color.RED);
//                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
//                        checkref_img.setText(object.getString("Message"));
//                        ref_et.setText("");
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(JanuaryLogin.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
//            }})
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<>();
//                params.put("UserId",ref_et.getText().toString());
//                return params;
//            }
//        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        RequestQueue requestQueue = Volley.newRequestQueue(JanuaryLogin.this);
//        requestQueue.add(stringRequest);
//    }
//    public void hideKeyboard(View view) {
//        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        verifyReferralAPI();
//    }
}