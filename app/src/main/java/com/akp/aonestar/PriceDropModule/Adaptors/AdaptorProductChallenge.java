package com.akp.aonestar.PriceDropModule.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.aonestar.PriceDropModule.Models.ModelProCha;
import com.akp.aonestar.R;

import java.util.List;

public class AdaptorProductChallenge extends RecyclerView.Adapter<AdaptorProductChallenge.MyProCha> {

    Context context;
    List<ModelProCha> list;

    public AdaptorProductChallenge(Context context, List<ModelProCha> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyProCha onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyProCha(LayoutInflater.from(context).inflate(R.layout.layout_product_challenge, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyProCha holder, int position) {
        holder.tvNumCount.setText(list.get(position).getContNum());
        holder.tvProName.setText(list.get(position).getProName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyProCha extends RecyclerView.ViewHolder {
        TextView tvNumCount, tvProName;
        public MyProCha(@NonNull View itemView) {
            super(itemView);
            tvNumCount = itemView.findViewById(R.id.tvNumCount);
            tvProName = itemView.findViewById(R.id.tvProName);
        }
    }
}
