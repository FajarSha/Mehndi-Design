package com.hennamehndi.mehndipatterns.bridalmehndi.designs;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.Ads_Util.InAppBilling;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.adapters.AllDesignAdapter;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.view.GridSpacingItemDecoration;

import java.util.ArrayList;

public class AllDesignActivity extends AppCompatActivity implements AllDesignAdapter.ItemClickListener {

    RecyclerView recyclerView;
    AllDesignAdapter allDesignAdapter;
    ArrayList<String> allUrls = new ArrayList<>();

    TextView titleTextView;
    ArrayList<String> favoriteUrls = new ArrayList<>();
    TinyDB tinyDB;
    String categoryName;

    ArrayList<Drawable> categoriesList = new ArrayList<>();
    InAppBilling inAppBilling = new InAppBilling(this, this);
    boolean isInAppPurchased = false;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onResume() {
        super.onResume();
        inAppBilling = new InAppBilling(this, this);
        isInAppPurchased = inAppBilling.hasUserBoughtInApp();
        if (isInAppPurchased) {
            shimmerFrameLayout.setVisibility(View.GONE);
        } else {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            App.setBanner(AllDesignActivity.this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_design);
        inAppBilling = new InAppBilling(this, this);
        isInAppPurchased = inAppBilling.hasUserBoughtInApp();
        categoryName = getIntent().getStringExtra("catName");
        getCategoriesList();
        if (categoriesList == null || categoriesList.size() == 0) {
            finish();
        }

        tinyDB = new TinyDB(AllDesignActivity.this);
        ArrayList<String> urls = tinyDB.getListString("favorites");
        if (urls != null) {
            favoriteUrls.addAll(urls);
        }

        String name = getIntent().getStringExtra("name");

        recyclerView = findViewById(R.id.recyclerView);
        titleTextView = findViewById(R.id.titleTextView);
       shimmerFrameLayout= findViewById(R.id.shimmer_view_container);

        titleTextView.setText(name);

        int spanCount = 2;
        int spacing = 40;
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(AllDesignActivity.this, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);

        // allDesignAdapter = new AllDesignAdapter(AllDesignActivity.this, allUrls, favoriteUrls);
        allDesignAdapter = new AllDesignAdapter(AllDesignActivity.this, categoriesList, favoriteUrls, categoryName);
        allDesignAdapter.setClickListener(this);
        recyclerView.setAdapter(allDesignAdapter);
        if (isInAppPurchased) {
//            shimmerFrameLayout.setVisibility(View.GONE);
        } else {
//            shimmerFrameLayout=findViewById(R.id.shimmer);
//            shimmerFrameLayout.setVisibility(View.VISIBLE);
            App.setBanner(AllDesignActivity.this);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        if (view instanceof ToggleButton) {

            ToggleButton likeButton = (ToggleButton) view;
            int savingPos = position + 1;
            if (likeButton.isChecked()) {
                favoriteUrls.add(categoryName + "_" + savingPos);
                tinyDB.putListString("favorites", favoriteUrls);
            } else {
                favoriteUrls.remove(categoryName + "_" + savingPos);
                tinyDB.putListString("favorites", favoriteUrls);
            }

        } else {
            Intent intent = new Intent(AllDesignActivity.this, PicShowActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("catName", categoryName);
            intent.putStringArrayListExtra("urls", allUrls);
            intent.putExtra("category_name", titleTextView.getText().toString());
            startActivity(intent);
        }
    }

    public void getCategoriesList() {

        for (int i = 1; i <= 20; i++) {
            Drawable drawable = getResources().getDrawable(
                    getResources().getIdentifier(
                            categoryName + "_" + i,
                            "drawable",
                            getPackageName()
                    )
            );
            categoriesList.add(drawable);
        }


    }

}