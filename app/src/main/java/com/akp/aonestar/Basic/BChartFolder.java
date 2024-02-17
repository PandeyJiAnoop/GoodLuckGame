package com.akp.aonestar.Basic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
public class BChartFolder extends AppCompatActivity {
    ImageView back_img;
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();
    SwipeRefreshLayout srl_refresh;
    private SharedPreferences sharedPreferences;
    String Userid,UserName;
    private final ArrayList<HashMap<String, String>> arrLegalList = new ArrayList<>();
    private LegalListAdapter pdfAdapTer;
    TextView norecord_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bchart_folder);
        sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        Userid = sharedPreferences.getString("U_id", "");
        UserName = sharedPreferences.getString("U_name", "");
        back_img = findViewById(R.id.back_img);
        norecord_tv = findViewById(R.id.norecord_tv);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript for proper rendering

        // Replace "file_url" with the URL of your .xlsx file (can be a direct link or Google Drive link)
        String fileUrl = "https://docs.google.com/spreadsheets/d/1cjj3tOv1AMdJDvxTpZg1HDjTU2QiOZOZv38TxkX3xOU/edit#gid=1713260425";

        // Load the Google Docs Viewer URL containing the .xlsx file into the WebView
        webView.loadUrl(fileUrl);
        cust_recyclerView = findViewById(R.id.cust_recyclerView);
        srl_refresh = findViewById(R.id.srl_refresh);


        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(BChartFolder.this)) {
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
                    Toast.makeText(BChartFolder.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });


        GetCommunityListAPI();


    }


    public void GetCommunityListAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+"BChartFolderList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("WinnersList",response);
//                 Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
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
                            hashlist.put("FolderName", jsonObject.getString("FolderName"));
                            hashlist.put("FolderId", jsonObject.getString("FolderId"));
//                            hashlist.put("Amount", jsonObject.getString("Amount"));
                            arrLegalList.add(hashlist);
                        }
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(BChartFolder.this, 3);
                        pdfAdapTer = new LegalListAdapter(BChartFolder.this, arrLegalList);
                        cust_recyclerView.setLayoutManager(layoutManager);
                        cust_recyclerView.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(BChartFolder.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(BChartFolder.this, "Something Went Wrong:-\n" + error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(BChartFolder.this);
        requestQueue.add(stringRequest);

    }
    public class LegalListAdapter extends RecyclerView.Adapter<BChartFolder.LegalList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public LegalListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrLegalList) {
            data = arrLegalList;
        }
        public BChartFolder.LegalList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BChartFolder.LegalList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_bchartfolder, parent, false));
        }


        public void onBindViewHolder(final BChartFolder.LegalList holder, final int i) {
            holder.title_tv.setText(data.get(i).get("FolderName"));
//            holder.des_tv.setText(data.get(position).get("EntryDate"));
//            holder.user_name_tv.setText(data.get(position).get("Description"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), HeaderWinnerList.class);
                    intent.putExtra("F_id",data.get(i).get("FolderId"));
                    startActivity(intent);
                }
            });

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class LegalList extends RecyclerView.ViewHolder {
        TextView title_tv;

        public LegalList(View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
        }}




    private String readRawFile(int resourceId) {
        StringBuilder stringBuilder = new StringBuilder();

        InputStream inputStream = getResources().openRawResource(resourceId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}