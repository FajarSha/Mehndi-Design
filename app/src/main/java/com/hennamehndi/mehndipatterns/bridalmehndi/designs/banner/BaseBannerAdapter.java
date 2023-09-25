package com.hennamehndi.mehndipatterns.bridalmehndi.designs.banner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;

import java.util.List;

/**
 * Created by test on 2017/11/22.
 */


public class BaseBannerAdapter extends RecyclerView.Adapter<BaseBannerAdapter.MzViewHolder> {

    private Context context;
    private List<Drawable> urlList;
    private BannerLayout.OnBannerItemClickListener onBannerItemClickListener;

    public BaseBannerAdapter(Context context, List<Drawable> urlList) {
        this.context = context;
        this.urlList = urlList;
    }

    public void setOnBannerItemClickListener(BannerLayout.OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    @Override
    public MzViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MzViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MzViewHolder holder, final int position) {
        if (urlList == null || urlList.isEmpty())
            return;
        final int P = position % urlList.size();
        Drawable url = urlList.get(P);
        ImageView img = (ImageView) holder.imageView;
        Glide.with(context).load(url).placeholder(R.drawable.place_holder).error(R.drawable.place_holder).into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBannerItemClickListener != null) {
                    onBannerItemClickListener.onItemClick(P);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (urlList != null) {
           return urlList.size();
        }
       return 0;
    }


    class MzViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        MzViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

}
