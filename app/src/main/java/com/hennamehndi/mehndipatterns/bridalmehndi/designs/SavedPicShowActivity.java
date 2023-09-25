package com.hennamehndi.mehndipatterns.bridalmehndi.designs;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.Ads_Util.InAppBilling;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.adapters.PicShowAdapter;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;

import java.util.ArrayList;


public class SavedPicShowActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<String> savedUrls = new ArrayList<>();
    PicShowAdapter savedAdapter;

    TinyDB tinyDB;
    int currentPosition = 0;
    ArrayList<Drawable> savedDrawableList = new ArrayList<>();
    InAppBilling inAppBilling = new InAppBilling(this, this);
    boolean isInAppPurchased = false;
    ShimmerFrameLayout shimmerFrameLayout;

    protected void onResume() {
        super.onResume();
        inAppBilling = new InAppBilling(this, this);
        isInAppPurchased = inAppBilling.hasUserBoughtInApp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_show_saved);

        inAppBilling = new InAppBilling(this, this);
        isInAppPurchased = inAppBilling.hasUserBoughtInApp();
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

        savedUrls = getIntent().getStringArrayListExtra("urls");
        currentPosition = getIntent().getIntExtra("position", 0);
        String categoryName = getIntent().getStringExtra("category_name");
        getSavedDrawableList();

        if (savedUrls == null || savedUrls.size() == 0) {
            finish();
        }

        tinyDB = new TinyDB(SavedPicShowActivity.this);

        recyclerView = findViewById(R.id.recyclerView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(categoryName);

        savedAdapter = new PicShowAdapter(SavedPicShowActivity.this, savedDrawableList);
        recyclerView.setAdapter(savedAdapter);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        recyclerView.scrollToPosition(currentPosition);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager lManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstElementPosition = lManager.findFirstVisibleItemPosition();

                currentPosition = firstElementPosition;

                //      App.showInterstitialVerySeldom(SavedPicShowActivity.this);
            }
        });
        if (isInAppPurchased) {
            shimmerFrameLayout.setVisibility(View.GONE);
        }
        else {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            App.setBanner(SavedPicShowActivity.this);
            App.showInterstitialSeldom(SavedPicShowActivity.this);
        }

    }


    public void shareListener(View view) {

        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }


    public void deleteListener(View view) {
        savedDrawableList.remove(currentPosition);
        savedUrls.remove(currentPosition);

        tinyDB.putListString("saved", savedUrls);
        savedAdapter.notifyItemRemoved(currentPosition);
        savedAdapter.notifyDataSetChanged();
        if(!isInAppPurchased){
            App.showInterstitialSeldom(SavedPicShowActivity.this);
        }

    }

    public void getSavedDrawableList() {

        for (int i = 1; i <= savedUrls.size(); i++) {
            Drawable drawable = getResources().getDrawable(
                    getResources().getIdentifier(
                            savedUrls.get(i - 1),
                            "drawable",
                            getPackageName()
                    )
            );
            savedDrawableList.add(drawable);
        }


    }
}