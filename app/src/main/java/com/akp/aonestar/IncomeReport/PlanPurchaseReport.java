package com.akp.aonestar.IncomeReport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlanPurchaseReport extends AppCompatActivity {
    ImageView back_btn;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FriendsListAdapter pdfAdapTer;
    String UserId;
    ImageView norecord_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_purchase_report);
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        UserId= sharedPreferences.getString("U_id", "");
        Log.v("addadada", UserId);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rcvList = findViewById(R.id.rcvList);
        norecord_tv=findViewById(R.id.norecord_tv);

        GetUserListAPI();
    }



    public void GetUserListAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"PlanPurchaseList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("ddres", response);
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
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
                            hashlist.put("Member_Id", jsonObject.getString("Member_Id"));
                            hashlist.put("AccountNo", jsonObject.getString("AccountNo"));
                            hashlist.put("MemberName", jsonObject.getString("MemberName"));
                            hashlist.put("PackagePrice", jsonObject.getString("PackagePrice"));
                            hashlist.put("TotalEI", jsonObject.getString("TotalEI"));
                            hashlist.put("RegDate", jsonObject.getString("RegDate"));
                            hashlist.put("MobileNo", jsonObject.getString("MobileNo"));
                            hashlist.put("EmaiLID", jsonObject.getString("EmaiLID"));
                            hashlist.put("ActivationDate", jsonObject.getString("ActivationDate"));
                            hashlist.put("Package_name", jsonObject.getString("Package_name"));

                            hashlist.put("State", jsonObject.getString("State"));
                            hashlist.put("City", jsonObject.getString("City"));
                            hashlist.put("Pincode", jsonObject.getString("Pincode"));

                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
//                        Toast.makeText(PlanPurchaseReport.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(PlanPurchaseReport.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("UserId", UserId);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PlanPurchaseReport.this);
        requestQueue.add(stringRequest);

    }

    public class FriendsListAdapter extends RecyclerView.Adapter<PlanPurchaseReport.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public PlanPurchaseReport.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PlanPurchaseReport.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_planpurchase, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final PlanPurchaseReport.FriendsList holder, final int position) {
            holder.Member_IDtv.setText("Account No:- "+data.get(position).get("AccountNo"));
            holder.MemberNametv.setText(data.get(position).get("MemberName"));
            holder.Package_nametv.setText(data.get(position).get("Package_name"));

            holder.ActivationDatetv.setText(data.get(position).get("ActivationDate"));
            holder.PackagePricetv.setText(data.get(position).get("PackagePrice"));
            holder.TotalEItv.setText(data.get(position).get("TotalEI"));
//            Picasso.get().load(data.get(position).get("ProfilePhoto")).into(holder.user_profile_img);
            holder.emi_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), DDFDEMIChart.class);
                    intent.putExtra("acno",data.get(position).get("AccountNo"));
                    startActivity(intent);
                }
            });
        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        TextView Member_IDtv,MemberNametv,Package_nametv,TotalDaytv,emi_tv,ActivationDatetv,PackagePricetv,TotalEItv;

        public FriendsList(View itemView) {
            super(itemView);
            Member_IDtv=itemView.findViewById(R.id.Member_IDtv);
            MemberNametv=itemView.findViewById(R.id.MemberNametv);
            PackagePricetv=itemView.findViewById(R.id.PackagePricetv);
            Package_nametv=itemView.findViewById(R.id.Package_nametv);
            TotalDaytv=itemView.findViewById(R.id.TotalDaytv);
            ActivationDatetv=itemView.findViewById(R.id.ActivationDatetv);
            emi_tv=itemView.findViewById(R.id.emi_tv);
            TotalEItv=itemView.findViewById(R.id.TotalEItv);
        } }

}