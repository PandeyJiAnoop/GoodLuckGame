package com.akp.aonestar.WalletReport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.LoginScreenAkp;
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
import com.google.firebase.firestore.auth.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WalletHistory extends AppCompatActivity {

    ImageView menuImg;
    String UserId;
    TextView wallet_amount_tv,credit_tv,debit_tv,coin_tv;

    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private FriendsListAdapter pdfAdapTer;
    ImageView norecord_tv;
    TextView raise_add_tv;

    AppCompatButton fund_tranfer_friend_btn;

    private android.app.AlertDialog alertDialog;
    TextView status_tv,withdraw_tv,smart_tv;
    EditText rupee_et,mobile_et,w_rupee_et;
    String FriendUserId;
    LinearLayout amount_ll;
    private AlertDialog alertDialog1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_history);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        menuImg=findViewById(R.id.menuImg);
        raise_add_tv=findViewById(R.id.raise_add_tv);
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        wallet_amount_tv=findViewById(R.id.wallet_amount_tv);
        credit_tv=findViewById(R.id.credit_tv);
        debit_tv=findViewById(R.id.debit_tv);
        coin_tv=findViewById(R.id.coin_tv);
        smart_tv=findViewById(R.id.smart_tv);
        GetWalletAPI();
        rcvList = findViewById(R.id.rcvList);
        norecord_tv=findViewById(R.id.norecord_tv);
        WalletHistoryAPI();
        fund_tranfer_friend_btn=findViewById(R.id.fund_tranfer_friend_btn);
        withdraw_tv=findViewById(R.id.withdraw_tv);
        raise_add_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserId.equalsIgnoreCase("")){
                    Intent intent=new Intent(getApplicationContext(), LoginScreenAkp.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(getApplicationContext(),AddWallet.class);
                    startActivity(intent);
                }
            }
        });

        fund_tranfer_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (UserId.equalsIgnoreCase("")){
                    Intent intent=new Intent(getApplicationContext(), LoginScreenAkp.class);
                    startActivity(intent);
                }
                else {
                    fundfriendttransferpopup();
                }


            }
        });


        withdraw_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserId.equalsIgnoreCase("")){
                    Intent intent=new Intent(getApplicationContext(), LoginScreenAkp.class);
                    startActivity(intent);
                }
                else {
                    withdrawpopupShow();
                }



            }
        });
    }

    public void GetWalletAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GetWalletAmount", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String member_id = jsonObject2.getString("Member_ID");
                        String mainWallet = jsonObject2.getString("MainWallet");
                        String memberName = jsonObject2.getString("MemberName");
                        String emiWallet = jsonObject2.getString("EMIWallet");
                        String smartWallet = jsonObject2.getString("ShoppingWallet");

                        wallet_amount_tv.setText("\u20B9 "+mainWallet);
                        debit_tv.setText("\u20B9 "+emiWallet);
                        smart_tv.setText("\u20B9 "+smartWallet);
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(WalletHistory.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    public void WalletHistoryAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"WalletHistory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sadasdfa","sadsad"+response);
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("PKID", jsonObject.getString("PKID"));
                            hashlist.put("FromMemberId", jsonObject.getString("FromMemberId"));
                            hashlist.put("ToMemberId", jsonObject.getString("ToMemberId"));
                            hashlist.put("TransType", jsonObject.getString("TransType"));
                            hashlist.put("Amount", jsonObject.getString("Amount"));
                            hashlist.put("Remark", jsonObject.getString("Remark"));
                            hashlist.put("EntryBy", jsonObject.getString("EntryBy"));
                            hashlist.put("EntryDate", jsonObject.getString("EntryDate"));
                            hashlist.put("TransFor", jsonObject.getString("TransFor"));

                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(WalletHistory.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(WalletHistory.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(WalletHistory.this);
        requestQueue.add(stringRequest);
    }
    public class FriendsListAdapter extends RecyclerView.Adapter<FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_transactions, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final FriendsList holder, final int position) {
            holder.tv1.setText(data.get(position).get("TransType")+"  \u20B9 "+data.get(position).get("Amount"));
            holder.tv2.setText("Remark :- "+data.get(position).get("Remark"));
            holder.tv3.setText(data.get(position).get("EntryDate"));
//            if (data.get(position).get("TransType").equalsIgnoreCase("Cr")){
//                holder.tv4.setText("Credit");
//                holder.tv4.setTextColor(Color.GREEN);
//            }
//            else {
//                holder.tv4.setText("Debit");
//                holder.tv4.setTextColor(Color.RED);
//            }



        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView tv1,tv2,tv3,tv4;

        public FriendsList(View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.tv1);
            tv2=itemView.findViewById(R.id.tv2);

            tv3=itemView.findViewById(R.id.tv3);
            tv4=itemView.findViewById(R.id.tv4);

        }
    }





    private void fundfriendttransferpopup() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.friend_withdrawpopup, viewGroup, false);
        final AppCompatButton Submit_btn = (AppCompatButton) dialogView.findViewById(R.id.Submit_btn);
        final AppCompatButton Submit_btn1 = (AppCompatButton) dialogView.findViewById(R.id.Submit_btn1);
        rupee_et=(EditText)dialogView.findViewById(R.id.rupee_et);
        mobile_et=dialogView.findViewById(R.id.mobile_et);
        status_tv=dialogView.findViewById(R.id.status_tv);
        amount_ll=dialogView.findViewById(R.id.amount_ll);

        Submit_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"GetVerifyMemberID", new  Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("GetVerifyMemberID",""+response);
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getString("Status").equalsIgnoreCase("true")){
                                JSONArray Jarray = object.getJSONArray("Response");
                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject jsonobject = Jarray.getJSONObject(i);
                                    status_tv.setText("Account Verified("+jsonobject.getString("MemberName")+")");
                                    FriendUserId = jsonobject.getString("UserID");
                                    Submit_btn1.setVisibility(View.GONE);
                                    amount_ll.setVisibility(View.VISIBLE);
                                    status_tv.setTextColor(Color.GREEN);

                                }
                            }
                            else {
                                status_tv.setText(object.getString("Message"));
                                status_tv.setTextColor(Color.RED);
                                Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                                Submit_btn1.setVisibility(View.VISIBLE);
                                amount_ll.setVisibility(View.GONE);

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WalletHistory.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<>();
//                        params.put("MobileNo",mobile_et.getText().toString());
                        params.put("UserId",mobile_et.getText().toString());
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestQueue = Volley.newRequestQueue(WalletHistory.this);
                requestQueue.add(stringRequest);
            }
        });

        Submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransferMoney(FriendUserId,rupee_et.getText().toString());
            }
        });
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();
    }





    private void withdrawpopupShow() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.withdrawpopup, viewGroup, false);
        final AppCompatButton w_submit_btn = (AppCompatButton) dialogView.findViewById(R.id.w_submit_btn);
        w_rupee_et=dialogView.findViewById(R.id.w_rupee_et);

        w_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (w_rupee_et.getText().toString().equalsIgnoreCase("")){
                    w_rupee_et.setError("Fields can't be blank!");
                    w_rupee_et.requestFocus();
                }
                else {
                    WithdrawAPI();
                }
            }
        });
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(dialogView);
        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    private void WithdrawAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"WithdrawlRequest", new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) { Log.d("WithdrawlRequest",""+response);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("Status").equalsIgnoreCase("true")){
                        JSONArray Jarray = object.getJSONArray("Response");
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject jsonobject = Jarray.getJSONObject(i);
                            Toast.makeText(getApplicationContext(),jsonobject.getString("Msg"),Toast.LENGTH_LONG).show();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),object.getString("Message"),Toast.LENGTH_LONG).show();
                        alertDialog1.dismiss();
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
                Toast.makeText(WalletHistory.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("MemberId",UserId);
                params.put("Amount",w_rupee_et.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(WalletHistory.this);
        requestQueue.add(stringRequest);
    }


    public void TransferMoney(final String fid,final String amount){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"WalletToWalletTransfer", new  Response.Listener<String>() {
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
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
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
                Toast.makeText(WalletHistory.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("TransferId",fid);
                params.put("Amount",amount);
                params.put("UserId",UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(WalletHistory.this);
        requestQueue.add(stringRequest);
    }

}