package com.akp.aonestar.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    EditText name_et,mobile_et,email_et,reg_et,pin_code_et,city_et,state_et,Pantv,upiid_et;
    RelativeLayout header;
    AppCompatButton btn_submit;
    String UserId,UserName;
    TextView name_tv,mobile_tv;
    ImageView back_img;

    EditText AccountNumbertv,BankNametv,AccountHolderNametv,IfscCodetv,BranchNametv;


    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String temp;
    CircleImageView profile_image;
    String encodedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        UserName= sharedPreferences.getString("U_name", "");
        name_tv=findViewById(R.id.name_tv);
        mobile_tv=findViewById(R.id.mobile_tv);
        header=findViewById(R.id.header);
        btn_submit=findViewById(R.id.btn_submit);
        name_et=findViewById(R.id.name_et);
        mobile_et=findViewById(R.id.mobile_et);
        email_et=findViewById(R.id.email_et);
        reg_et=findViewById(R.id.reg_et);
        upiid_et=findViewById(R.id.upiid_et);
        pin_code_et=findViewById(R.id.pin_code_et);
        city_et=findViewById(R.id.city_et);
        state_et=findViewById(R.id.state_et);
        back_img=findViewById(R.id.back_img);
        Pantv=findViewById(R.id.Pantv);
        profile_image=findViewById(R.id.profile_image);


        AccountNumbertv=findViewById(R.id.AccountNumbertv);
        BankNametv=findViewById(R.id.BankNametv);
        AccountHolderNametv=findViewById(R.id.AccountHolderNametv);
        IfscCodetv=findViewById(R.id.IfscCodetv);
        BranchNametv=findViewById(R.id.BranchNametv);
        verifyStoragePermission(EditProfile.this);


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });











        GetProfile();
        pin_code_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profile_image.getDrawable() == null){
                    UpdateProfile("");
                }
                else if (temp == null){
                    BitmapDrawable drawable = (BitmapDrawable) profile_image.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    encodedImage = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
                    UpdateProfile(encodedImage);
                }
                else {
                    UpdateProfile(temp);
                }
            }
        });

    }

    public void GetProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "ProfileView", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
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


                            String AccountNumber       = jsonobject.getString("AccountNumber");
                            String BankName    = jsonobject.getString("BankName");
                            String AccountHolderName       = jsonobject.getString("AccountHolderName");
                            String IfscCode       = jsonobject.getString("IfscCode");
                            String BranchName    = jsonobject.getString("BranchName");
                            String PanNo    = jsonobject.getString("PanNo");
                            String UPIID    = jsonobject.getString("UPIID");


                            if (jsonobject.getString("ProfilePic").equalsIgnoreCase("")){
                            }
                            else {
                                Glide.with(EditProfile.this).load(jsonobject.getString("ProfilePic")).into(profile_image);
                            }

                           /* if (AccountNumbertv.getText().toString().equalsIgnoreCase("")){
                                AccountNumbertv.setClickable(true);
                                AccountNumbertv.setFocusable(true);
                                AccountNumbertv.setEnabled(true);
                            }
                            else {
                                AccountNumbertv.setClickable(false);
                                AccountNumbertv.setFocusable(false);
                                AccountNumbertv.setEnabled(false);
                            }
                            if (BankNametv.getText().toString().equalsIgnoreCase("")){
                                BankNametv.setClickable(true);
                                BankNametv.setFocusable(true);
                                BankNametv.setEnabled(true);
                            }
                            else {
                                BankNametv.setClickable(false);
                                BankNametv.setFocusable(false);
                                BankNametv.setEnabled(false);
                            }
                            if (AccountHolderNametv.getText().toString().equalsIgnoreCase("")){
                                AccountHolderNametv.setClickable(true);
                                AccountHolderNametv.setFocusable(true);
                                AccountHolderNametv.setEnabled(true);
                            }
                            else {
                                AccountHolderNametv.setClickable(false);
                                AccountHolderNametv.setFocusable(false);
                                AccountHolderNametv.setEnabled(false);
                            }
                            if (IfscCodetv.getText().toString().equalsIgnoreCase("")){
                                IfscCodetv.setClickable(true);
                                IfscCodetv.setFocusable(true);
                                IfscCodetv.setEnabled(true);
                            }
                            else {
                                IfscCodetv.setClickable(false);
                                IfscCodetv.setFocusable(false);
                                IfscCodetv.setEnabled(false);
                            }
                             if (BranchNametv.getText().toString().equalsIgnoreCase("")){
                                BranchNametv.setClickable(true);
                                BranchNametv.setFocusable(true);
                                 BranchNametv.setEnabled(true);
                            }
                             else {
                                 BranchNametv.setClickable(false);
                                 BranchNametv.setFocusable(false);
                                 BranchNametv.setEnabled(false);
                             }*/

                            AccountNumbertv.setText(AccountNumber);
                            BankNametv.setText(BankName);
                            AccountHolderNametv.setText(AccountHolderName);
                            IfscCodetv.setText(IfscCode);
                            BranchNametv.setText(BranchName);

                            name_et.setText(MemberName);
                            mobile_et.setText(MobileNo);
                            email_et.setText(EmaiLID);
                            reg_et.setText(RegDate);
                            state_et.setText(state);
                            city_et.setText(city);
                            pin_code_et.setText(pinCode);

                            name_tv.setText(MemberName);
                            mobile_tv.setText(MobileNo);
                            Pantv.setText(PanNo);
                            upiid_et.setText(UPIID);

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
                progressDialog.dismiss();
                Toast.makeText(EditProfile.this, "Something Went Wrong:-\n" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);
    }




    public void UpdateProfile(String profile) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+ "ProfileUpdate", new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            Toast.makeText(getApplicationContext(),jsonobject.getString("Msg"),Toast.LENGTH_LONG).show();
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
                progressDialog.dismiss();
                Toast.makeText(EditProfile.this, "Something Went Wrong:-\n" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId",UserId);
                params.put("MemberName",name_et.getText().toString());
                params.put("MobileNo",mobile_et.getText().toString());
                params.put("EmialId",email_et.getText().toString());
                params.put("PinCode",pin_code_et.getText().toString());
                params.put("City",city_et.getText().toString());
                params.put("State",state_et.getText().toString());
                params.put("UPIID",upiid_et.getText().toString());
                params.put("AccountNumber",AccountNumbertv.getText().toString());
                params.put("BankName",BankNametv.getText().toString());
                params.put("AccountHolderName",AccountHolderNametv.getText().toString());
                params.put("IfscCode",IfscCodetv.getText().toString());
                params.put("BranchName",BranchNametv.getText().toString());
                params.put("PanNo",Pantv.getText().toString());
                params.put("ProfilePic",profile);

                Log.d("res_profile", String.valueOf(params));

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);
    }








    public void CheckIFSCAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://www.postalpincode.in/api/pincode/"+pin_code_et.getText().toString(), new Response.Listener<String>() {
            //        StringRequest stringRequest = new StringRequest(Request.Method.POST, Api_Urls.Signature_BASE_URL + url, new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray Jarray = object.getJSONArray("PostOffice");
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject jsonobject = Jarray.getJSONObject(i);
                        city_et.setText(jsonobject.getString("District"));
                        state_et.setText(jsonobject.getString("State"));
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(EditProfile.this, "IFSC Code Invalid", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        CheckIFSCAPI();
    }


    private void selectImage() {
        final CharSequence[] items = {"Choose from Library"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                galleryIntent();

                boolean result= Utility.checkPermission(EditProfile.this);
//                if (items[item].equals("Choose from Library")) {
//                    userChoosenTask ="Choose from Library";
//                    if(result)
//                        galleryIntent();
//
//                } else if (items[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
            }
        });
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
        profile_image.setImageBitmap(bm);
        Bitmap immagex=bm;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] b = baos.toByteArray();
        temp = Base64.encodeToString(b,Base64.DEFAULT);
    }

    //Permissions Check
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE, };

    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity, PERMISSION_STORAGE,REQUEST_EXTERNAL_STORAGE);}}

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

}


