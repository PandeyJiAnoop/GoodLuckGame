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

public class DirectIncomeReport extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    ImageView back_img;
    LinearLayout dynamic_ll;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private RecyclerView chat_recyclerView;
    private SharedPreferences sharedPreferences;
    String UserId,Username;
    TextView norecord_tv;
    SwipeRefreshLayout srl_refresh;
    Spinner ststus_sp;     TextView title_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_income_report);
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
                if (NetworkConnectionHelper.isOnline(DirectIncomeReport.this)) {
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
                    Toast.makeText(DirectIncomeReport.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            } });

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppUrls.baseUrl+"DirectIncomeReport", new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resdddd",response);
                progressDialog.dismiss();
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray Jarray  = object.getJSONArray("Response");
                    Log.d("myTdfdsfsfag", "message:"+Jarray);
                    for (int i = 0; i < Jarray.length(); i++) {
                        title_tv.setText("Referral Earning("+Jarray.length()+")");
                        JSONObject jsonObject1 = Jarray.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("Income", jsonObject1.getString("Income"));
                        hm.put("Payable", jsonObject1.getString("Payable"));
                        hm.put("EntryDate", jsonObject1.getString("EntryDate"));
                        hm.put("PlanName", jsonObject1.getString("PlanName"));
                        hm.put("ReferenceId", jsonObject1.getString("ReferenceId"));
                        hm.put("ReferenceMemberName", jsonObject1.getString("ReferenceMemberName"));
                        norecord_tv.setVisibility(View.GONE);
                        arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(DirectIncomeReport.this, 1);
                    PositionrAdapter HistoryAdapte = new PositionrAdapter(DirectIncomeReport.this, arrayList);
                    chat_recyclerView.setLayoutManager(gridLayoutManager);
                    chat_recyclerView.setAdapter(HistoryAdapte);
                } catch (JSONException e) {
                    Toast.makeText(DirectIncomeReport.this, "No Record Found!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();}}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(DirectIncomeReport.this, "No Record Found!", Toast.LENGTH_SHORT).show();
            } }) {  @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            HashMap<String, String> params = new HashMap<>();
            params.put("UserId",UserId);
            params.put("GameCategory", cat);
            return params;
        }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(DirectIncomeReport.this);
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
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_referalearning, viewGroup, false);
            PositionrAdapter.VH viewHolder = new PositionrAdapter.VH(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull PositionrAdapter.VH vh, int i) {
//        AnimationHelper.animatate(context,vh.itemView,R.anim.alfa_animation);
            vh.earning_tv.setText("₹ "+arrayList.get(i).get("Income"));
            if (arrayList.get(i).get("EntryDate").equalsIgnoreCase("")){
                vh.date_tv.setText("00/00/0000");
            }
            else {
                vh.date_tv.setText(arrayList.get(i).get("EntryDate"));
            }
            vh.amount_tv.setText("₹ "+arrayList.get(i).get("Payable"));
            vh.name.setText(arrayList.get(i).get("ReferenceMemberName")+"\n("+arrayList.get(i).get("ReferenceId")+")");
            vh.plan_tv.setText(arrayList.get(i).get("PlanName"));

        }
        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class VH extends RecyclerView.ViewHolder {
            TextView date_tv, amount_tv,name,earning_tv,plan_tv;

            public VH(@NonNull View itemView) {
                super(itemView);
                date_tv = itemView.findViewById(R.id.date_tv);
                amount_tv = itemView.findViewById(R.id.amount_tv);
                name = itemView.findViewById(R.id.name_tv);
                earning_tv = itemView.findViewById(R.id.earning_tv);
                plan_tv = itemView.findViewById(R.id.plan_tv);
            } }
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