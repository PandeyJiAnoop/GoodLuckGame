package com.akp.aonestar.Ecommerce;

import static com.akp.aonestar.RetrofitAPI.API_Config.getApiClient_ByPost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.ApiService;
import com.akp.aonestar.RetrofitAPI.ConnectToRetrofit;
import com.akp.aonestar.RetrofitAPI.GlobalAppApis;
import com.akp.aonestar.RetrofitAPI.RetrofitCallBackListenar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

public class E_SuCategoryListActivity extends AppCompatActivity {
    LinearLayoutManager layoutManager;
    private ArrayList<HashMap<String, String>> arrSubCategoryList = new ArrayList<>();
    RelativeLayout rlHeader;
    RecyclerView rcvSubCategoryList;
    String rootcategoryid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esu_category_list);
        findViewById();
        getCategorymaster(rootcategoryid);

    }

    private void findViewById() {
        rlHeader=findViewById(R.id.rlHeader);
        rcvSubCategoryList=findViewById(R.id.rcvSubCategoryList);
        rootcategoryid=getIntent().getStringExtra("rootcategoryid");
        rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getCategorymaster(String rootcategoryid) {
        String otp1 = new GlobalAppApis().getCategorymaster(rootcategoryid);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.getCategorymaster(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    arrSubCategoryList.clear();
                    Log.v("getStringgg", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    if (Status.equalsIgnoreCase("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("CategoryName", jsonObject2.getString("CategoryName"));
                            hm.put("ImgPath", jsonObject2.getString("ImgPath"));
                            hm.put("CategoryId", jsonObject2.getString("CategoryId"));



                            arrSubCategoryList.add(hm);
                        }
                        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        SubCategoryAdapter adapterr = new SubCategoryAdapter(getApplicationContext(), arrSubCategoryList);
                        rcvSubCategoryList.setLayoutManager(layoutManager);
                        rcvSubCategoryList.setAdapter(adapterr);
                        // Pager listener over indicator
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, E_SuCategoryListActivity.this, call1, "", true);


    }

    private class SubCategoryAdapter extends RecyclerView.Adapter<Category> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        public SubCategoryAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrSubCategoryList) {
            data = arrSubCategoryList;
        }
        /* public SubCategoryAdapter( activity, ArrayList<HashMap<String, String>> rootCategoryList) {
             data = rootCategoryList;
         }*/
        public Category onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Category(LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategorylist, parent, false));
        }
        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final Category holder, final int position) {
            Picasso.get().load(data.get(position).get("ImgPath")).error(R.drawable.ban).into(holder.catimg);
            holder.catname.setText(data.get(position).get("CategoryName"));
            holder.llCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), E_SubCategoryListAKP.class);
                    intent.putExtra("CatId",data.get(position).get("CategoryId"));
                    intent.putExtra("CatName",data.get(position).get("RootCategoryName"));
                    startActivity(intent);
                } });

        }

        public int getItemCount() {
            return data.size();
        }
    }

    public class Category extends RecyclerView.ViewHolder {
        ImageView catimg;
        TextView catname;
        CardView llCategory;

        public Category(View itemView) {
            super(itemView);
            catimg = itemView.findViewById(R.id.catimg);
            catname = itemView.findViewById(R.id.catname);
            llCategory = itemView.findViewById(R.id.llCategory);


        }
    }
}