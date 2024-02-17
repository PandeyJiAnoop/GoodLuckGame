package com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.aonestar.PlayBuyModule.PlayBuyModels.Banner.ResponseItem;
import com.akp.aonestar.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdaptorImageSlider extends RecyclerView.Adapter<AdaptorImageSlider.MySliderHolder> {

    Context context;
    List<ResponseItem> list;

    public AdaptorImageSlider(Context context, List<ResponseItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MySliderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MySliderHolder(LayoutInflater.from(context).inflate(R.layout.banner_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MySliderHolder holder, int position) {
        Glide.with(context)
                .load(list.get(position).getBannerImg())
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(holder.imgBanner);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MySliderHolder extends RecyclerView.ViewHolder {

        ImageView imgBanner;
        public MySliderHolder(@NonNull View itemView) {
            super(itemView);

            imgBanner = itemView.findViewById(R.id.imgBanner);
        }
    }
}
