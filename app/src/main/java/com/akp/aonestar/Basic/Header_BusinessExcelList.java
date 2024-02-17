package com.akp.aonestar.Basic;

import static com.akp.aonestar.RetrofitAPI.API_Config.getApiClient_ByPost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.akp.aonestar.R;
import com.akp.aonestar.RetrofitAPI.ApiService;
import com.akp.aonestar.RetrofitAPI.ConnectToRetrofit;
import com.akp.aonestar.RetrofitAPI.GlobalAppApis;
import com.akp.aonestar.RetrofitAPI.RetrofitCallBackListenar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

public class Header_BusinessExcelList extends AppCompatActivity {
    ImageView menuImg;
    RecyclerView history_rec;
    String UserId;
    ArrayList<HashMap<String, String>> arrayList2 = new ArrayList<>();
    ImageView norecord_img;
    TextView title_tv;
    ImageButton search_img;
    EditText search_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_business_excel_list);
        FindId();
        History("1");

            search_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (search_et.getText().toString().equalsIgnoreCase("")){
                        search_et.setError("Fields can't be blank!");
                        search_et.requestFocus();
                    }
                    else {
                        arrayList2.clear();
                        History(search_et.getText().toString());
                    } }});




    }

    private void FindId() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_preference", MODE_PRIVATE);
        UserId = sharedPreferences.getString("U_id", "");
        Log.d("UserId",UserId);
        title_tv=findViewById(R.id.title_tv);
        norecord_img=findViewById(R.id.norecord_img);
        menuImg = findViewById(R.id.menuImg);
        history_rec = findViewById(R.id.history_rec);
        search_img = findViewById(R.id.search_img);
        search_et= findViewById(R.id.search_et);
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void History(String member) {
        String otp = new GlobalAppApis().GetExcelDataNew(member);
        ApiService client = getApiClient_ByPost();
        Call<String> call = client.GetExcelDataNewAPI(otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                Log.d("resss",result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString("Status").equals("false")){
                        norecord_img.setVisibility(View.VISIBLE);
                    }
                    else{
                        norecord_img.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("Response");
                        Log.d("hh",""+jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {
//                            title_tv.setText("B-View("+jsonArray.length()+")");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Log.d("hh",""+jsonObject1);
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put("Name", jsonObject1.getString("Name"));
                            hm.put("MemberCount", jsonObject1.getString("MemberCount"));
                            hm.put("Bussiness", jsonObject1.getString("Bussiness"));
                            hm.put("PerValue", jsonObject1.getString("PerValue"));
                            hm.put("Months", jsonObject1.getString("Months"));
                            hm.put("FirstTenure", jsonObject1.getString("FirstTenure"));
                            hm.put("SevenTenure", jsonObject1.getString("SevenTenure"));
                            hm.put("Reward", jsonObject1.getString("Reward"));
                            hm.put("Offer", jsonObject1.getString("Offer"));
                            arrayList2.add(hm);
                        }
                        LinearLayoutManager HorizontalLayout1 = new LinearLayoutManager(Header_BusinessExcelList.this, LinearLayoutManager.VERTICAL, false);
                        DasAdapter customerListAdapter = new DasAdapter(Header_BusinessExcelList.this, arrayList2);
                        history_rec.setLayoutManager(HorizontalLayout1);
                        history_rec.setAdapter(customerListAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Header_BusinessExcelList.this, call, "", true);
    }


    public class DasAdapter extends RecyclerView.Adapter<DasAdapter.VH> {
        Context context;
        List<HashMap<String,String>> arrayList;
        public DasAdapter(Context context, List<HashMap<String,String>> arrayList) {
            this.arrayList=arrayList;
            this.context=context;
        }
        @NonNull
        @Override
        public DasAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.dynamic_bexcel, viewGroup, false);
            DasAdapter.VH viewHolder = new DasAdapter.VH(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(@NonNull DasAdapter.VH vh, int i) {
            vh.tv.setText(arrayList.get(i).get("Name"));
            vh.tv1.setText(arrayList.get(i).get("MemberCount"));
//            vh.tv3.setText(arrayList.get(i).get("PerValue")+"%");

            String PerValue = arrayList.get(i).get("PerValue");
            double PerV = Double.parseDouble(PerValue);
            DecimalFormat decimalFormatpv = new DecimalFormat("#.####"); // Change the pattern as per your requirement
            String formattedBussinessp = decimalFormatpv.format(PerV);
            vh.tv3.setText(formattedBussinessp+"%");


            String bussinessValue = arrayList.get(i).get("Bussiness");
            double bussiness = Double.parseDouble(bussinessValue);
            DecimalFormat decimalFormat = new DecimalFormat("#.####"); // Change the pattern as per your requirement
            String formattedBussiness = decimalFormat.format(bussiness);
            vh.tv2.setText(formattedBussiness);

            String FirstTenure = arrayList.get(i).get("FirstTenure");
            double First = Double.parseDouble(FirstTenure);
            DecimalFormat decimalFormat1 = new DecimalFormat("#.####"); // Change the pattern as per your requirement
            String formattedBussiness1= decimalFormat1.format(First);
            vh.tv5.setText(formattedBussiness1);

            String SevenTenure = arrayList.get(i).get("SevenTenure");
            double Seven = Double.parseDouble(SevenTenure);
            DecimalFormat decimalFormat2 = new DecimalFormat("#.####"); // Change the pattern as per your requirement
            String formattedBussiness2= decimalFormat2.format(Seven);
            vh.tv6.setText(formattedBussiness2);

            String Reward = arrayList.get(i).get("Reward");
            double Reward1 = Double.parseDouble(Reward);
            DecimalFormat decimalFormat3 = new DecimalFormat("#.####"); // Change the pattern as per your requirement
            String formattedBussiness3= decimalFormat3.format(Reward1);
            vh.tv7.setText(formattedBussiness3);

            String Months = arrayList.get(i).get("Months");
            double Months1 = Double.parseDouble(Months);
            DecimalFormat decimalFormat4 = new DecimalFormat("#.####"); // Change the pattern as per your requirement
            String formattedBussiness4= decimalFormat4.format(Months1);
            vh.tv4.setText(formattedBussiness4);

            String Offer = arrayList.get(i).get("Offer");
            double Offer1 = Double.parseDouble(Offer);
            DecimalFormat decimalFormat5 = new DecimalFormat("#.####"); // Change the pattern as per your requirement
            String formattedBussiness5= decimalFormat5.format(Offer1);
            vh.tv8.setText(formattedBussiness5);

        }
        @Override
        public int getItemCount() {
            return arrayList.size();
        }
        public class VH extends RecyclerView.ViewHolder {
            TextView tv,tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;

            public VH(@NonNull View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv);
                tv1 = itemView.findViewById(R.id.tv1);
                tv2 = itemView.findViewById(R.id.tv2);
                tv3 = itemView.findViewById(R.id.tv3);
                tv4 = itemView.findViewById(R.id.tv4);
                tv5 = itemView.findViewById(R.id.tv5);
                tv6 = itemView.findViewById(R.id.tv6);
                tv7 = itemView.findViewById(R.id.tv7);
                tv8 = itemView.findViewById(R.id.tv8);

            }}
    }
}