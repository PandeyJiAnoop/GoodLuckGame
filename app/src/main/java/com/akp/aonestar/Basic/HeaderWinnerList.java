package com.akp.aonestar.Basic;

import static com.akp.aonestar.RetrofitAPI.API_Config.getApiClient_ByPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.GoodLuckModule.ConsolationNew1;
import com.akp.aonestar.LoginActivity;
import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.ApiService;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.RetrofitAPI.ConnectToRetrofit;
import com.akp.aonestar.RetrofitAPI.GlobalAppApis;
import com.akp.aonestar.RetrofitAPI.NetworkConnectionHelper;
import com.akp.aonestar.RetrofitAPI.RetrofitCallBackListenar;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class HeaderWinnerList extends AppCompatActivity {
    ImageView back_img;
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    SwipeRefreshLayout srl_refresh;
    private SharedPreferences sharedPreferences;
    String Userid,UserName;
    private final ArrayList<HashMap<String, String>> arrLegalList = new ArrayList<>();
    private LegalListAdapter pdfAdapTer;
TextView norecord_tv;
String GetId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_winner_list);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        Userid = sharedPreferences.getString("U_id", "");
        UserName = sharedPreferences.getString("U_name", "");
        GetId=getIntent().getStringExtra("F_id");
//        Toast.makeText(getApplicationContext(),GetId,Toast.LENGTH_LONG).show();
        back_img = findViewById(R.id.back_img);
        norecord_tv = findViewById(R.id.norecord_tv);
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
                if (NetworkConnectionHelper.isOnline(HeaderWinnerList.this)) {
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
                    Toast.makeText(HeaderWinnerList.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        GetCommunityListAPI();


    }


    public void GetCommunityListAPI() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"BChartFolderDetailsList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BChartFolderDetailsList", response);
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
                            norecord_tv.setVisibility(View.GONE);
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("Images", jsonObject.getString("Images"));
                            arrLegalList.add(hashlist);
                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(HeaderWinnerList.this, 2);
                        pdfAdapTer = new LegalListAdapter(HeaderWinnerList.this, arrLegalList);
                        cust_recyclerView.setLayoutManager(layoutManager);
                        cust_recyclerView.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(HeaderWinnerList.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HeaderWinnerList.this, "Something went wrong:-" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("FolderId", GetId);
                Log.v("addadada", String.valueOf(params));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(HeaderWinnerList.this);
        requestQueue.add(stringRequest);
    }
    public class LegalListAdapter extends RecyclerView.Adapter<LegalList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public LegalListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrLegalList) {
            data = arrLegalList;
        }
        public LegalList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new LegalList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_winnerlist, parent, false));
        }


        public void onBindViewHolder(final LegalList holder, final int i) {
            String imgurl=data.get(i).get("Images");
            Glide.with(HeaderWinnerList.this)
                    .load(imgurl).error(R.drawable.a_logo)
                    .into(holder.img1);
            Log.d("WinnersListimg",data.get(i).get("Images"));
            holder.img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), FullImagePage.class);
                    intent.putExtra("path",data.get(i).get("Images"));
                    startActivity(intent);
                }
            });

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class LegalList extends RecyclerView.ViewHolder {
        ImageView img1;

        public LegalList(View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.img);


        }
    }
}
