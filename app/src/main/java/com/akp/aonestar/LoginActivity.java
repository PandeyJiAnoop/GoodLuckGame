package com.akp.aonestar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.akp.aonestar.Basic.GameSelection;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import pl.droidsonroids.gif.GifImageView;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rlLoginPopup;
    AlertDialog alertDialog;
    AlertDialog alertDialog2;
    String otp = "";
    private SharedPreferences login_preference;
    private SharedPreferences.Editor login_editor;
    private static final int SPLASH_TIME_OUT = 1500;
    int progresscount = 0;
    TextView person_name;
    GifImageView ivthree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        findViewById();
        setListner();
        ivthree=findViewById(R.id.ivthree);
        person_name=findViewById(R.id.person_name);
        person_name.setSelected(true);

        ivthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameSelection.class));
            } });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progresscount == 100) {
                    /**/
                } else {
                    handler.postDelayed(this, 30);
                    progresscount++;
                } }
        }, 200);

        Timer RunSplash = new Timer();
        // Task to do when the timer ends
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), GameSelection.class));
//                setAlertDialog();
            }
        }, SPLASH_TIME_OUT);
    }

    private void setListner() {
        rlLoginPopup.setOnClickListener(this);
    }
    private void findViewById() {
        rlLoginPopup = findViewById(R.id.rlLoginPopup);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlLoginPopup:
                setAlertDialog();
                break;
//            case R.id.ivWhatsp:
//                try {
//                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                    shareIntent.setType("text/plain");
//                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "RSJSS");
//                    String shareMessage = "\nLet me recommend you this application\n\n";
//                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
//                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//                    startActivity(Intent.createChooser(shareIntent, "choose one"));
//                } catch (Exception e) {
//                    //e.toString();
//                }
//                break;
        }
    }

    private void setAlertDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.login_popup, viewGroup, false);
        RelativeLayout rlBack = (RelativeLayout) dialogView.findViewById(R.id.rlBack);
        Button btnLogin = (Button) dialogView.findViewById(R.id.btnLogin);
        Button btnRegister = (Button) dialogView.findViewById(R.id.btnRegister);
        Button btnForgot = (Button) dialogView.findViewById(R.id.btnForgot);
        EditText edtEntrNumber = (EditText) dialogView.findViewById(R.id.edtEntrNumber);

        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            } });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEntrNumber.getText().toString().equalsIgnoreCase("")) {
                    edtEntrNumber.setError("Fields can't be blank!");
                    edtEntrNumber.requestFocus();
                } else {
//                    Login(edtEntrNumber.getText().toString());
                    forgotpassword(otp, "");
                }} });

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void forgotpassword(String otp, String number) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.forget_layout, viewGroup, false);
        RelativeLayout rlBack = (RelativeLayout) dialogView.findViewById(R.id.rlBack);
        Button btnLogin = (Button) dialogView.findViewById(R.id.btnLogin);
        EditText edtOtp = (EditText) dialogView.findViewById(R.id.edtOtp);
        edtOtp.setText(otp);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();
            }});
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtOtp.getText().toString().equalsIgnoreCase("")) {
                    edtOtp.setError("Fields can't be blank!");
                    edtOtp.requestFocus();
                } else {
//                    Verifyotp(edtOtp.getText().toString(), number);
                    startActivity(new Intent(getApplicationContext(), GameSelection.class));
                }}});

       /* rupee_et = (EditText) dialogView.findViewById(R.id.rupee_et);

        Button ok = (Button) dialogView.findViewById(R.id.buttonOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTranferAmount();
                alertDialog.dismiss();
            }
        });*/
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        alertDialog2 = builder.create();
        alertDialog2.show();
    }

    public void Login(String s) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/Login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String Msg = object.getString("Msg");
                    String Status = object.getString("Status");
                    otp = object.getString("otp");
                    if (Status.equalsIgnoreCase("true")) {
                        forgotpassword(otp, s);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MobileNo", s);
                Log.v("addadada", String.valueOf(params));
                return params;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    public void Verifyotp(String s, String number) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://ramshyamgame.signaturesoftware.org/WebService1.asmx/Verifyotp", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                jsonString = jsonString.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", " ");
                jsonString = jsonString.replace("<string xmlns=\"http://tempuri.org/\">", " ");
                jsonString = jsonString.replace("</string>", " ");
                Log.d("test", jsonString);
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String UserName = object.getString("UserName");
                    String UserId = object.getString("UserId");
                    String Status = object.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {
                        login_preference = getSharedPreferences("login_preference", MODE_PRIVATE);
                        login_editor = login_preference.edit();
                        login_editor.putString("username", UserName);
                        login_editor.putString("UserId", UserId);
                        login_editor.commit();
                        startActivity(new Intent(getApplicationContext(), TYpesGame.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Something went Wrong:-" + error, Toast.LENGTH_SHORT).show();
            } }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MobileNo", number);
                params.put("OTP", s);
                Log.v("addadada", String.valueOf(params));
                return params;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

}