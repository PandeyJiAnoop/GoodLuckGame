package com.akp.aonestar.GoodLuckModule;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.aonestar.R;

// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class AdapterAkp extends RecyclerView.Adapter<AdapterAkp.ViewHolder> {
    ArrayList images;
    Context context;

    // Constructor for initialization
    public AdapterAkp(Context context, ArrayList images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public AdapterAkp.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml layout file into View object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        // Passing view to ViewHolder
        AdapterAkp.ViewHolder viewHolder = new AdapterAkp.ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull AdapterAkp.ViewHolder holder, int position) {
        // TypeCast Object to int type
        int res = (int) images.get(position);
        holder.images.setImageResource(res);
    }

    @Override
    public int getItemCount() {
        // Returns number of items currently available in Adapter
        return images.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;

        public ViewHolder(View view) {
            super(view);
            images = (ImageView) view.findViewById(R.id.imageView);
        }
    }
}