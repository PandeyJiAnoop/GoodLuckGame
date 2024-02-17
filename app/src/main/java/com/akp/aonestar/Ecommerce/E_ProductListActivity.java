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
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class E_ProductListActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rcvProductlist;
    LinearLayoutManager layoutManager;
    RelativeLayout rlHeader;
    private ArrayList<HashMap<String, String>> arrProductlist = new ArrayList<>();
    String CatId;

    private SharedPreferences sharedPreferences;
    String username,GetSubCategoryName;
    ImageView iv_wishlist,iv_cart;
    TextView title_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eproduct_list);
        findViewById();
        setListner();
    }

    private void setListner() {
        rlHeader.setOnClickListener(this);
        iv_wishlist.setOnClickListener(this);
        iv_cart.setOnClickListener(this);
    }
    private void findViewById() {
        rcvProductlist = findViewById(R.id.rcvProductList);
        rlHeader = findViewById(R.id.rlHeader);
        iv_wishlist = findViewById(R.id.iv_wishlist);
        sharedPreferences = getSharedPreferences("login_preference",MODE_PRIVATE);
        username = sharedPreferences.getString("U_id", "");
        CatId = getIntent().getStringExtra("CatId");
        GetSubCategoryName=getIntent().getStringExtra("SubCategoryName");
        title_tv=findViewById(R.id.title_tv);
        title_tv.setText(GetSubCategoryName);

        iv_cart=findViewById(R.id.iv_cart);

        ProductListbycategoryid();


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlHeader:
                finish();
                break;

            case R.id.iv_wishlist:
//                startActivity(new Intent(getApplicationContext(),WishlistActivity.class));
                break;

            case R.id.iv_cart:
                startActivity(new Intent(getApplicationContext(),E_CartActivity.class));
                break;
        }

    }
    public void ProductListbycategoryid() {
        String otp1 = new GlobalAppApis().ProductListbycategoryid(CatId,username);
        Log.v("otp1", otp1);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.ProductListbycategoryid(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    arrProductlist.clear();
                    Log.v("ProductListbycategoryid", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String Status = jsonObject.getString("Status");
                    JSONArray jsonArray = jsonObject.getJSONArray("Response");
                    if (Status.equalsIgnoreCase("true")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("CashBackAmt", jsonObject2.getString("CashBackAmt"));
                            hm.put("CatId", jsonObject2.getString("CatId"));
                            hm.put("ExdiscountAmt", jsonObject2.getString("ExdiscountAmt"));
                            hm.put("IsMultiVariant", jsonObject2.getString("IsMultiVariant"));
                            hm.put("ProductCode", jsonObject2.getString("ProductCode"));
                            hm.put("RP", jsonObject2.getString("RP"));
                            hm.put("MainImgFile", jsonObject2.getString("MainImgFile"));
                            hm.put("ProductMRP", jsonObject2.getString("ProductMRP"));
                            hm.put("ProductTitle", jsonObject2.getString("ProductTitle"));
                            hm.put("SaleRate", jsonObject2.getString("SaleRate"));
                            hm.put("WishList", jsonObject2.getString("WishList"));
                            hm.put("Exdiscounttype", jsonObject2.getString("Exdiscounttype"));
                            hm.put("OfferDis", jsonObject2.getString("OfferDis"));
                            arrProductlist.add(hm);
                        }
                        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                        ProductAdapter productAdapter = new ProductAdapter(getApplicationContext(), arrProductlist);
                        rcvProductlist.setLayoutManager(layoutManager);
                        rcvProductlist.setAdapter(productAdapter);
                        // Pager listener over indicator
                    }
                    else {
                        Toast.makeText(E_ProductListActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, E_ProductListActivity.this, call1, "", true);


    }
    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        public ProductAdapter(Context applicationContext, ArrayList<HashMap<String, String>> arrProductlist) {
            data = arrProductlist;
        }

        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_product, parent, false));
        }

        @SuppressLint("SetTextI18n")
        public void onBindViewHolder(final ProductHolder holder, final int position) {
            Picasso.get().load(data.get(position).get("MainImgFile")).into(holder.img_product);
            holder.txt_Name.setText(data.get(position).get("ProductTitle"));


            if(data.get(position).get("WishList").equalsIgnoreCase("1"))
            {
                holder.ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
            }
            else {
                holder.ic_home_fav.setImageResource(R.drawable.favorite_unlike);

            }
            if(data.get(position).get("Exdiscounttype").equalsIgnoreCase("Percentage"))
            {
                if(data.get(position).get("ProductMRP").equalsIgnoreCase(data.get(position).get("SaleRate")))
                {
                    holder.tv_product_price.setText("\u20b9"+(data.get(position).get("SaleRate")));
                    holder.tv_actual_price.setVisibility(View.GONE);
                    holder.tv_offer_price.setVisibility(View.GONE);
                }
                else {
                    holder.tv_actual_price.setVisibility(View.VISIBLE);
                    holder.tv_offer_price.setVisibility(View.VISIBLE);
                    holder.tv_product_price.setText("\u20b9"+(data.get(position).get("SaleRate")));
                    holder.tv_actual_price.setText("MRP."+(data.get(position).get("ProductMRP")));
                    holder.tv_offer_price.setText(data.get(position).get("OfferDis")+"%");

                }
            }
            else {
                if(data.get(position).get("ProductMRP").equalsIgnoreCase(data.get(position).get("SaleRate")))
                {
                    holder.tv_product_price.setText("\u20b9"+(data.get(position).get("SaleRate")));
                    holder.tv_actual_price.setVisibility(View.GONE);
                    holder.tv_offer_price.setVisibility(View.GONE);
                }
                else {
                    holder.tv_actual_price.setVisibility(View.VISIBLE);
                    holder.tv_offer_price.setVisibility(View.VISIBLE);
                    holder.tv_product_price.setText("\u20b9"+(data.get(position).get("SaleRate")));
                    holder.tv_actual_price.setText("MRP."+(data.get(position).get("ProductMRP")));
                    holder.tv_offer_price.setText(data.get(position).get("OfferDis")+" Save");
                }
            }
            holder.ic_home_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddToFevorite(data.get(position).get("ProductCode"),holder.ic_home_fav);
                }
            });
            holder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), E_ProductDetailsActivity.class);
                    intent.putExtra("ProductCode",data.get(position).get("ProductCode"));
                    startActivity(intent);
                }
            });
        }

        public int getItemCount() {
            return data.size();
        }
    }
    public class ProductHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        ImageView img_product,ic_home_fav;
        TextView txt_Name;
        TextView tv_product_price, tv_actual_price, tv_offer_price;

        public ProductHolder(View itemView) {
            super(itemView);
            cardview = itemView.findViewById(R.id.cardview);
            img_product = itemView.findViewById(R.id.img_product);
            txt_Name = itemView.findViewById(R.id.txt_Name);
            tv_product_price =  itemView.findViewById(R.id.tv_product_price);
            tv_actual_price =  itemView.findViewById(R.id.tv_actual_price);
            tv_offer_price =  itemView.findViewById(R.id.tv_offer_price);
            ic_home_fav =  itemView.findViewById(R.id.ic_home_fav);
        }
    }
    public void AddToFevorite(String propetyId, ImageView ic_home_fav) {
        String otp1 = new GlobalAppApis().AddFav(propetyId,username);
        ApiService client1 = getApiClient_ByPost();
        Call<String> call1 = client1.AddtowishList(otp1);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                Toast.makeText(getApplicationContext(),""+result,Toast.LENGTH_LONG).show();
                try {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("Status");
                    String Message = object.getString("Msg");
                    if (status.equalsIgnoreCase("true")) {
                        if (Message.equalsIgnoreCase("Added Successfully.")) {
                            ic_home_fav.setImageResource(R.drawable.ic_fav_hover);
                            Toast.makeText(getApplicationContext(), "Added Wishlist Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            ic_home_fav.setImageResource(R.drawable.favorite_unlike);
                            Toast.makeText(getApplicationContext(), "Remove Wishlist Successfully", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, E_ProductListActivity.this, call1, "", true);


    }
}