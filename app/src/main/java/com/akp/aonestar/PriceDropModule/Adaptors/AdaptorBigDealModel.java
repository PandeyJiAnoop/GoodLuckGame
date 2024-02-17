package com.akp.aonestar.PriceDropModule.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.akp.aonestar.PriceDropModule.Models.BigDealModel;
import com.akp.aonestar.R;

import java.util.List;

public class AdaptorBigDealModel extends RecyclerView.Adapter<AdaptorBigDealModel.MyBigDealHolder> {

    Context context;
    List<BigDealModel> dealModels;

    public AdaptorBigDealModel(Context context, List<BigDealModel> dealModels) {
        this.context = context;
        this.dealModels = dealModels;
    }

    @NonNull
    @Override
    public MyBigDealHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyBigDealHolder(LayoutInflater.from(context).inflate(R.layout.layout_big_deal, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyBigDealHolder holder, int position) {
        holder.imgProImage.setImageResource(dealModels.get(position).getImageName());
        holder.tvProName.setText(dealModels.get(position).getProductName());
        holder.tvProPrice.setText(dealModels.get(position).getProductPrice());

    }

    @Override
    public int getItemCount() {
        return dealModels.size();
    }

    public class MyBigDealHolder extends RecyclerView.ViewHolder {
        ImageView imgProImage;
        TextView tvProName, tvProPrice;
        public MyBigDealHolder(@NonNull View itemView) {
            super(itemView);
            imgProImage = itemView.findViewById(R.id.imgProImage);
            tvProName = itemView.findViewById(R.id.tvProName);
            tvProPrice = itemView.findViewById(R.id.tvProPrice);
        }
    }
}
