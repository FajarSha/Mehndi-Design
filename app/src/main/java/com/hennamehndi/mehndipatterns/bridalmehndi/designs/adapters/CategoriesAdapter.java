package com.hennamehndi.mehndipatterns.bridalmehndi.designs.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.fragments.CatModel;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    public ArrayList<CatModel> categories = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    public CategoriesAdapter(Context context, ArrayList<CatModel> categories) {
        this.mInflater = LayoutInflater.from(context);
        this.categories = categories;
        this.context = context;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.categories_row, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CatModel categoryModel = categories.get(position);
        holder.textView.setText(categoryModel.name);


        if (categoryModel.icon != null ) {
            Glide.with(context).load(categoryModel.icon).placeholder(R.drawable.place_holder).error(R.drawable.place_holder).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(0);
        }

    }


    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public CatModel getItem(int id) {
        return categories.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}