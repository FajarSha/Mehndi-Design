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

import com.hennamehndi.mehndipatterns.bridalmehndi.designs.adapters.SavedAdapter;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.SavedPicShowActivity;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.TinyDB;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.view.GridSpacingItemDecoration;

import java.util.ArrayList;

public class SavedFragment extends Fragment implements SavedAdapter.ItemClickListener {


    RecyclerView recyclerView;
    SavedAdapter savedAdapter;

    ArrayList<String> savedUrls = new ArrayList<>();
    ArrayList<Drawable> savedDrawableList = new ArrayList<>();

    TinyDB tinyDB;
    Context context;
    TextView nothingSavedText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        nothingSavedText = view.findViewById(R.id.tv_noSaved);

        int spanCount = 2;
        int spacing = 40;
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);

        savedAdapter = new SavedAdapter(context, savedDrawableList);
        savedAdapter.setClickListener(this);
        recyclerView.setAdapter(savedAdapter);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tinyDB = new TinyDB(getActivity());


    }

    @Override
    public void onResume() {
        super.onResume();

        savedUrls.clear();
        savedDrawableList.clear();

        ArrayList<String> urls = tinyDB.getListString("saved");
        if (urls != null) {
            savedUrls.addAll(urls);
        }
        getSavedDrawableList();

        savedAdapter.notifyDataSetChanged();
        if(savedUrls.size()==0){
            nothingSavedText.setVisibility(View.VISIBLE);
        }else {
            nothingSavedText.setVisibility(View.GONE);

        }

    }

    @Override
    public void onItemClick(View view, int position) {

        if (view instanceof ToggleButton) {

            ToggleButton likeButton = (ToggleButton) view;
            if (!likeButton.isChecked()) {
                savedUrls.remove(savedUrls.get(position));
                tinyDB.putListString("favorites", savedUrls);
            }

            savedAdapter.notifyDataSetChanged();

        } else {
            Intent intent = new Intent(getActivity(), SavedPicShowActivity.class);
            intent.putExtra("position", position);
            intent.putStringArrayListExtra("urls", savedUrls);
            intent.putExtra("category_name", "Saved");
            startActivity(intent);
        }
    }

    public  void getSavedDrawableList(){

        for (int i=1;i<=savedUrls.size();i++) {
            Drawable drawable = getActivity().getResources().getDrawable(
                    getActivity().getResources().getIdentifier(
                            savedUrls.get(i-1),
                            "drawable",
                            getActivity().getPackageName()
                    )
            );
            savedDrawableList.add(drawable);
        }


    }
}