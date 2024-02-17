package com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ModelProduct;
import com.akp.aonestar.R;

import java.util.List;

public class AdaptorProductList extends RecyclerView.Adapter<AdaptorProductList.MyProHolder> {

    Context context;
    List<ModelProduct> dealModels;

    public AdaptorProductList(Context context, List<ModelProduct> dealModels) {
        this.context = context;
        this.dealModels = dealModels;
    }

    @NonNull
    @Override
    public MyProHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyProHolder(LayoutInflater.from(context).inflate(R.layout.playbuy_product_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyProHolder holder, int position) {
        holder.imgProImage.setImageResource(dealModels.get(position).getImageName());
        holder.tvProName.setText(dealModels.get(position).getProductName());
        holder.tvProPrice.setText(dealModels.get(position).getProductPrice());
    }

    @Override
    public int getItemCount() {
        return dealModels.size();
    }

    public class MyProHolder extends RecyclerView.ViewHolder {
        ImageView imgProImage;
        TextView tvProName, tvProPrice;
        public MyProHolder(@NonNull View itemView) {
            super(itemView);
            imgProImage = itemView.findViewById(R.id.imgProImage);
            tvProName = itemView.findViewById(R.id.tvProName);
            tvProPrice = itemView.findViewById(R.id.tvProPrice);
        }
    }
}
