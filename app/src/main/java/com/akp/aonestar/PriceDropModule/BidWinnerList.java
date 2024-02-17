package com.akp.aonestar.PriceDropModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.RetrofitAPI.NetworkConnectionHelper;
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

public class BidWinnerList extends AppCompatActivity {
    ImageView back_img;
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    SwipeRefreshLayout srl_refresh;
    private SharedPreferences sharedPreferences;
    String Userid,UserName;
    private final ArrayList<HashMap<String, String>> arrLegalList = new ArrayList<>();
    private LegalListAdapter pdfAdapTer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_winner_list);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        Userid = sharedPreferences.getString("U_id", "");
        UserName = sharedPreferences.getString("U_name", "");
        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        srl_refresh = findViewById(R.id.srl_refresh);


        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(BidWinnerList.this)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            srl_refresh.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(BidWinnerList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        GetCommunityListAPI();


    }


    public void GetCommunityListAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+"PRIZEDROP_BIDWinnersList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("PKID", jsonObject.getString("PKID"));
                            hashlist.put("WinnerName", jsonObject.getString("WinnerName"));
                            hashlist.put("IsActive", jsonObject.getString("IsActive"));
                            hashlist.put("EntryBy", jsonObject.getString("EntryBy"));
                            hashlist.put("EntryDate", jsonObject.getString("EntryDate"));
//                            hashlist.put("Amount", jsonObject.getString("Amount"));
                            hashlist.put("Description", jsonObject.getString("Description"));
                            arrLegalList.add(hashlist);
                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                        pdfAdapTer = new LegalListAdapter(BidWinnerList.this, arrLegalList);
                        cust_recyclerView.setLayoutManager(layoutManager);
                        cust_recyclerView.setAdapter(pdfAdapTer);
                    } else {
                        Toast.makeText(BidWinnerList.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(BidWinnerList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(BidWinnerList.this);
        requestQueue.add(stringRequest);

    }
    public class LegalListAdapter extends RecyclerView.Adapter<LegalList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public LegalListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrLegalList) {
            data = arrLegalList;
        }
        public LegalList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LegalList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_bid_winnerlist, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final LegalList holder, final int position) {
            holder.title_tv.setText(data.get(position).get("WinnerName"));
            holder.des_tv.setText(data.get(position).get("EntryDate"));
            holder.user_name_tv.setText(data.get(position).get("Description"));
        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class LegalList extends RecyclerView.ViewHolder {
        TextView title_tv, des_tv, user_name_tv;

        public LegalList(View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            des_tv = itemView.findViewById(R.id.des_tv);
            user_name_tv = itemView.findViewById(R.id.user_name_tv);
        }
    }
}
