package com.hennamehndi.mehndipatterns.bridalmehndi.designs.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hennamehndi.mehndipatterns.bridalmehndi.designs.PicShowActivity;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.TinyDB;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.adapters.FavoriteAdapter;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.view.GridSpacingItemDecoration;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements FavoriteAdapter.ItemClickListener {


    RecyclerView recyclerView;
    FavoriteAdapter favoriteAdapter;

    ArrayList<String> favoriteUrls = new ArrayList<>();
    ArrayList<Drawable> favoriteDrawableList = new ArrayList<>();

    TinyDB tinyDB;

    Context context;
    TextView noFavFound;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        noFavFound = view.findViewById(R.id.tv_noFav);




        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int spanCount = 2;
        int spacing = 40;
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);


    }

    @Override
    public void onResume() {
        super.onResume();

        favoriteUrls.clear();
        favoriteDrawableList.clear();

        tinyDB = new TinyDB(getActivity());

        ArrayList<String> urls = tinyDB.getListString("favorites");
        if (urls != null) {
            favoriteUrls.addAll(urls);
        }
        getFavDrawableList();
         favoriteAdapter = new FavoriteAdapter(context, favoriteDrawableList);
        favoriteAdapter.setClickListener(this);
        recyclerView.setAdapter(favoriteAdapter);

        if(favoriteUrls.size()==0){
            noFavFound.setVisibility(View.VISIBLE);
        }else {
            noFavFound.setVisibility(View.GONE);

        }

    }

    @Override
    public void onItemClick(View view, int position) {

        if (view instanceof ToggleButton) {

            ToggleButton likeButton = (ToggleButton) view;
            if (!likeButton.isChecked()) {

                favoriteDrawableList.remove(position);

                favoriteUrls.remove(favoriteUrls.get(position));
                tinyDB.putListString("favorites", favoriteUrls);
                favoriteAdapter.notifyItemRemoved(position);
                favoriteAdapter.notifyDataSetChanged();

            }


        } else {
            Intent intent = new Intent(getActivity(), PicShowActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("isFromFav", true);
            intent.putExtra("catName","Favorites");
            intent.putExtra("category_name", "Favorites");
            startActivity(intent);
        }
    }

    public  void getFavDrawableList(){

        for (int i=1;i<=favoriteUrls.size();i++) {
            Drawable drawable = getActivity().getResources().getDrawable(
                    getActivity().getResources().getIdentifier(
                            favoriteUrls.get(i-1),
                            "drawable",
                            getActivity().getPackageName()
                    )
            );
            favoriteDrawableList.add(drawable);
        }


    }
}