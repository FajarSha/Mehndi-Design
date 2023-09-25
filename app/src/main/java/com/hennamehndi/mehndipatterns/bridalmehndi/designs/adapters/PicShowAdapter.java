package com.hennamehndi.mehndipatterns.bridalmehndi.designs.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;
import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;

public class PicShowAdapter extends RecyclerView.Adapter<PicShowAdapter.ViewHolder> {

    public ArrayList<Drawable> urls = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    public PicShowAdapter(Context context, ArrayList<Drawable> urls) {
        this.mInflater = LayoutInflater.from(context);
        this.urls = urls;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.pic_show_row, parent, false);
        return new ViewHolder(parent, view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drawable url = urls.get(position);

        if (url != null ) {
            Glide.with(context).load(url).placeholder(R.drawable.place_holder).error(R.drawable.place_holder).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(0);
        }
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TouchImageView imageView;
        ToggleButton likeButton;

        ViewHolder(ViewGroup parent, View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            likeButton = itemView.findViewById(R.id.likeButton);

            itemView.setOnClickListener(this);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    boolean result = true;
                    //can scroll horizontally checks if there's still a part of the image
                    //that can be scrolled until you reach the edge
                    if (event.getPointerCount() >= 2 || v.canScrollHorizontally(1) && v.canScrollHorizontally(-1)) {
                        //multi-touch event
                        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                            parent.requestDisallowInterceptTouchEvent(true);
                            return false;
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            parent.requestDisallowInterceptTouchEvent(false);
                            return true;
                        } else {
                            return true;
                        }
                    }

                    return result;
                }
            });

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Drawable getItem(int id) {
        return urls.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}