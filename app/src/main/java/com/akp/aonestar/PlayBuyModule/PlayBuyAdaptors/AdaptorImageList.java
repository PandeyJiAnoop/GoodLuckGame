package com.akp.aonestar.PlayBuyModule.PlayBuyAdaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.akp.aonestar.PlayBuyModule.PlayBuyModels.ModelImg;
import com.akp.aonestar.R;

import java.util.List;

public class AdaptorImageList extends RecyclerView.Adapter<AdaptorImageList.MyImageHolder> {

    Context context;
    List<ModelImg> list;

    public AdaptorImageList(Context context, List<ModelImg> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyImageHolder(LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyImageHolder holder, int position) {
        holder.image1.setImageResource(list.get(position).getImageName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyImageHolder extends RecyclerView.ViewHolder {
        ImageView image1;
        public MyImageHolder(@NonNull View itemView) {
            super(itemView);
            image1 = itemView.findViewById(R.id.image1);
        }
    }
}
