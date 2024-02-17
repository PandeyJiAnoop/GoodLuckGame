package com.akp.aonestar.GoodLuckModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akp.aonestar.Basic.FullImagePage;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class DoWantAnythingElse extends AppCompatActivity {
    ImageView menuImg;
    String userId;
    TextView title_tv;
    RecyclerView rcvList;
    private final ArrayList<HashMap<String, String>> arrFriendsList = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private FriendsListAdapter pdfAdapTer;
    ImageView norecord_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_want_anything_else);
        menuImg=findViewById(R.id.menuImg);
        title_tv=findViewById(R.id.title_tv);

        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        rcvList = findViewById(R.id.rcvList);
        norecord_tv=findViewById(R.id.norecord_tv);
        DoWantAnythingElseAPI();
    }






    public void DoWantAnythingElseAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(DoWantAnythingElse.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+"DoYouThingElseGameMaster", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject object = new JSONObject(jsonString);
                    String status = object.getString("Message");
                    if (status.equalsIgnoreCase("Success")) {
                        norecord_tv.setVisibility(View.GONE);
                        JSONArray Response = object.getJSONArray("Response");
                        for (int i = 0; i < Response.length(); i++) {
//                            title_tv.setText("External Link Report("+Response.length()+")");
                            JSONObject jsonObject = Response.getJSONObject(i);
                            HashMap<String, String> hashlist = new HashMap();
                            hashlist.put("BannerImg", jsonObject.getString("BannerImg"));
                            hashlist.put("WebURL", jsonObject.getString("WebURL"));

                            arrFriendsList.add(hashlist);
                        }
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        pdfAdapTer = new FriendsListAdapter(getApplicationContext(), arrFriendsList);
                        rcvList.setLayoutManager(layoutManager);
                        rcvList.setAdapter(pdfAdapTer);
                    } else {
                        norecord_tv.setVisibility(View.VISIBLE);
                        Toast.makeText(DoWantAnythingElse.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("myTag", "message:"+error);
                Toast.makeText(DoWantAnythingElse.this, "Something went wrong!"+error, Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(DoWantAnythingElse.this);
        requestQueue.add(stringRequest);

    }

    public class FriendsListAdapter extends RecyclerView.Adapter<DoWantAnythingElse.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public DoWantAnythingElse.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DoWantAnythingElse.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.dynamic_anythingelse, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final DoWantAnythingElse.FriendsList holder, final int position) {
//            holder.tv.setText(data.get(position).get("WebURL"));
            if (data.get(position).get("BannerImg").equalsIgnoreCase("")){
                Toast.makeText(getApplicationContext(),"Image not found!", Toast.LENGTH_LONG).show();
            }
            else {
                Glide.with(DoWantAnythingElse.this).load(data.get(position).get("BannerImg")).into(holder.ext_img);
            }

            holder.ext_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), FullImagePage.class);
                    intent.putExtra("path",data.get(position).get("BannerImg"));
                    startActivity(intent);
                }
            });

            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = data.get(position).get("WebURL");
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        AppCompatButton tv;
        ImageView ext_img;


        public FriendsList(View itemView) {
            super(itemView);
            ext_img=itemView.findViewById(R.id.ext_img);
            tv=itemView.findViewById(R.id.tv);
        }
    }

}