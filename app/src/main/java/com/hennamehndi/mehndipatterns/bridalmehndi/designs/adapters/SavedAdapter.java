package com.hennamehndi.mehndipatterns.bridalmehndi.designs.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;

import java.util.ArrayList;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.ViewHolder> {

    public ArrayList<Drawable> savedUrls = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    public SavedAdapter(Context context, ArrayList<Drawable> savedUrls) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.savedUrls = savedUrls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.saved_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Drawable url = savedUrls.get(position);

        if (url != null ) {
            Glide.with(context).load(url).placeholder(R.drawable.place_holder).error(R.drawable.place_holder).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(0);
        }

    }

    @Override
    public int getItemCount() {
        return savedUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Drawable getItem(int id) {
        return savedUrls.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}