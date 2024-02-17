package com.akp.aonestar.MenuWebLink;

import static com.akp.aonestar.RetrofitAPI.API_Config.getApiClient_ByPost;
import static com.akp.aonestar.RetrofitAPI.API_Config.getApiClient_ByPost1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class Menu_Gallery extends AppCompatActivity {

    ImageView sliding_menu;
    private String code;
    SwipeRefreshLayout srl_refresh;
    RecyclerView cust_recyclerView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_gallery);

        SharedPreferences sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        code= sharedPreferences.getString("U_id", "");
        sliding_menu=findViewById(R.id.sliding_menu);
        sliding_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cust_recyclerView= findViewById(R.id.cust_recyclerView);

        getData();
        srl_refresh = findViewById(R.id.srl_refresh);
        srl_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkConnectionHelper.isOnline(Menu_Gallery.this)) {
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
                    Toast.makeText(Menu_Gallery.this, "Please check your internet connection! try again...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppUrls.baseUrl+"RefferalImagesList", new  Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                String jsonString = response;
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("BannerImg", jsonObject1.getString("BannerImg"));
                        arrayList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(Menu_Gallery.this, 1);
                    Menu_GalleryListAdapter walletHistoryAdapter = new Menu_GalleryListAdapter(Menu_Gallery.this, arrayList);
                    cust_recyclerView.setLayoutManager(gridLayoutManager);
                    cust_recyclerView.setAdapter(walletHistoryAdapter);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(MainActivity.this, "msg"+error, Toast.LENGTH_SHORT).show();
                Log.d("myTag", "message:"+error);
                Toast.makeText(Menu_Gallery.this, "Internet connection is slow Or no internet connection", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Menu_Gallery.this);
        requestQueue.add(stringRequest);
    }


    public class Menu_GalleryListAdapter extends RecyclerView.Adapter<Menu_GalleryListAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public Menu_GalleryListAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }
        @NonNull
        @Override
        public Menu_GalleryListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_referral_image, viewGroup, false);
            Menu_GalleryListAdapter.VH viewHolder = new Menu_GalleryListAdapter.VH(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull Menu_GalleryListAdapter.VH vh, int i) {
            Glide.with(Menu_Gallery.this).load(arrayList.get(i).get("BannerImg")).error(R.drawable.a_logo).into(vh.ref_img);

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            ImageView  ref_img;

            public VH(@NonNull View itemView) {
                super(itemView);
                ref_img=itemView.findViewById(R.id.ref_img);
            }
        }

    }




    

}

        
      /*  FindId();
        OnClick();

    }

    private void OnClick() {
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            } });
    }

    private void FindId() {
        header=findViewById(R.id.header);
        rcvList = findViewById(R.id.rcvList);

        GetBanner();

//        norecord_tv=findViewById(R.id.norecord_tv);
    }

    public void GetBanner() {
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.RefferalImagesList();
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayr = jsonObject.getJSONArray("Response");
                    for (int i = 0; i < jsonArrayr.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayr.getJSONObject(i);
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("BannerImg", jsonObject1.getString("BannerImg"));
                        arrFriendsList.add(hm);
                    }
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(Menu_Gallery.this, 1);
                    FriendsListAdapter customerListAdapter = new FriendsListAdapter(Menu_Gallery.this, arrFriendsList);
                    rcvList.setLayoutManager(gridLayoutManager);
                    rcvList.setAdapter(customerListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Menu_Gallery.this, call1, "", true);
    }

    public class FriendsListAdapter extends RecyclerView.Adapter<Menu_Gallery.FriendsList> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public FriendsListAdapter(Context context, ArrayList<HashMap<String, String>> arrFriendsList) {
            data = arrFriendsList;
        }
        public Menu_Gallery.FriendsList onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Menu_Gallery.FriendsList(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_referral_image, parent, false));
        }
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Menu_Gallery.FriendsList holder, final int position) {
            Glide.with(Menu_Gallery.this).load(data.get(position).get("BannerImg")).error(R.drawable.a_logo).into(holder.ref_img);
            Log.d("refimg","sadsad"+data.get(position).get("BannerImg"));
//            Picasso.get().load(data.get(position).get("BannerImg")).into(holder.ref_img);



        }
        public int getItemCount() {
            return data.size();
        }
    }
    public class FriendsList extends RecyclerView.ViewHolder {
        ImageView ref_img;

        public FriendsList(View itemView) {
            super(itemView);
            ref_img=itemView.findViewById(R.id.ref_img);
        }}

}*/