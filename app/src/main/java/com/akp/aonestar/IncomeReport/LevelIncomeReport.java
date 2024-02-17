package com.akp.aonestar.IncomeReport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.AppUrls;
import com.akp.aonestar.RetrofitAPI.NetworkConnectionHelper;
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
import java.util.List;
import java.util.Map;

public class LevelIncomeReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ImageView back_img;
    LinearLayout dynamic_ll;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private RecyclerView chat_recyclerView;
    private SharedPreferences sharedPreferences;
    String UserId,Username;
    TextView norecord_tv;
    SwipeRefreshLayout srl_refresh;     TextView title_tv;
    Spinner ststus_sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_income_report);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        Username = sharedPreferences.getString("U_name", "");
        dynamic_ll=findViewById(R.id.dynamic_ll);
        chat_recyclerView=findViewById(R.id.chat_recyclerView);
        norecord_tv=findViewById(R.id.norecord_tv);
        back_img=findViewById(R.id.back_img);
        title_tv= findViewById(R.id.title_tv);
        ststus_sp=findViewById(R.id.ststus_sp);

        // Spinner click listener
        ststus_sp.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Shop It");
//        categories.add("Price Drop");
        categories.add("See and Buy");
        categories.add("Shop on the Spot");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        ststus_sp.setAdapter(dataAdapter);

        srl_refresh = findViewById(R.id.srl_refresh);
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(LevelIncomeReport.this)) {
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
                    Toast.makeText(LevelIncomeReport.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        GetPostionList();
//        for (int i=0;i<2;i++){
//            GetPostionList(i);
//        }


    }

    public void GetPostionList(String cat) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"LevelIncomeReport", new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("LevelIncomeReport",response);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray Jarray  = object.getJSONArray("Response");
                    for (int i = 0; i < Jarray.length(); i++) {
                        title_tv.setText("Team Earning("+Jarray.length()+")");
                        JSONObject jsonObject1 = Jarray.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("TeamId", jsonObject1.getString("TeamId"));
                        hm.put("DiffAmount", jsonObject1.getString("DiffAmount"));
                        hm.put("Bussiness", jsonObject1.getString("Bussiness"));
                        hm.put("Levels", jsonObject1.getString("Levels"));
                        hm.put("PlanName", jsonObject1.getString("PlanName"));
                        norecord_tv.setVisibility(View.GONE);
                        arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(LevelIncomeReport.this, 1);
                    PositionrAdapter HistoryAdapte = new PositionrAdapter(LevelIncomeReport.this, arrayList);
                    chat_recyclerView.setLayoutManager(gridLayoutManager);
                    chat_recyclerView.setAdapter(HistoryAdapte);
                } catch (JSONException e) {
                    Toast.makeText(LevelIncomeReport.this, "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(LevelIncomeReport.this, "No Record Found!", Toast.LENGTH_SHORT).show();
            }
        }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId",UserId);
            params.put("GameCategory", cat);
            return params;
        }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(LevelIncomeReport.this);
        requestQueue.add(stringRequest);

    }



    public class PositionrAdapter extends RecyclerView.Adapter<PositionrAdapter.VH> {
        Context context;
        List<HashMap<String, String>> arrayList;

        public PositionrAdapter(Context context, List<HashMap<String, String>> arrayList) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public PositionrAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_levelincomereport, viewGroup, false);
            PositionrAdapter.VH viewHolder = new PositionrAdapter.VH(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PositionrAdapter.VH vh, int i) {
//        AnimationHelper.animatate(context,vh.itemView,R.anim.alfa_animation);
            vh.type_tv.setText(arrayList.get(i).get("TeamId"));
            vh.date_tv.setText("₹ "+arrayList.get(i).get("DiffAmount"));
            vh.date_tv1.setText(arrayList.get(i).get("Levels"));
            vh.amount_tv.setText("₹ "+arrayList.get(i).get("Bussiness"));
            vh.plan_tv.setText(arrayList.get(i).get("PlanName"));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            TextView type_tv, date_tv, date_tv1,amount_tv,plan_tv;

            public VH(@NonNull View itemView) {
                super(itemView);
                type_tv = itemView.findViewById(R.id.type_tv);
                date_tv = itemView.findViewById(R.id.date_tv);
                amount_tv = itemView.findViewById(R.id.amount_tv);
                date_tv1 = itemView.findViewById(R.id.date_tv1);
                plan_tv = itemView.findViewById(R.id.plan_tv);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        arrayList.clear();
        GetPostionList(item);

        // Showing selected spinner item
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}