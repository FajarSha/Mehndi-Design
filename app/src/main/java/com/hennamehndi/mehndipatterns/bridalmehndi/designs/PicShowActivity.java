package com.hennamehndi.mehndipatterns.bridalmehndi.designs;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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


public class PicShowActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<String> favoriteUrls = new ArrayList<>();
    ArrayList<Drawable> favoriteDrawableList = new ArrayList<>();
    ArrayList<String> allUrls = new ArrayList<>();
    ArrayList<String> savedUrls = new ArrayList<>();

    TinyDB tinyDB;
    int currentPosition = 0;
    ToggleButton likeButton;
    String categoryName;
    Boolean intentFromFavFragment;
    ArrayList<Drawable> categoriesList = new ArrayList<>();
    PicShowAdapter adapterImages;
    InAppBilling inAppBilling=new InAppBilling(this,this);
    boolean isInAppPurchased=false;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onResume() {
        super.onResume();
        inAppBilling=new InAppBilling(this,this);
        isInAppPurchased=inAppBilling.hasUserBoughtInApp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_show);
        inAppBilling=new InAppBilling(this,this);
        isInAppPurchased=inAppBilling.hasUserBoughtInApp();
        shimmerFrameLayout=findViewById(R.id.shimmer_view_container);

        intentFromFavFragment = getIntent().getBooleanExtra("isFromFav", false);
        categoryName = getIntent().getStringExtra("catName");

        currentPosition = getIntent().getIntExtra("position", 0);
        String categoryName = getIntent().getStringExtra("category_name");


        tinyDB = new TinyDB(PicShowActivity.this);
        ArrayList<String> urls = tinyDB.getListString("favorites");
        if (urls != null) {
            favoriteUrls.addAll(urls);
        }

        ArrayList<String> urls2 = tinyDB.getListString("saved");
        if (urls2 != null) {
            savedUrls.addAll(urls2);
        }
        likeButton = findViewById(R.id.likeButton);

        recyclerView = findViewById(R.id.recyclerView);
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(categoryName);
        if (intentFromFavFragment) {
            getFavDrawableList();
            if (favoriteDrawableList == null || favoriteDrawableList.size() == 0) {
                finish();

            }
            adapterImages = new PicShowAdapter(PicShowActivity.this, favoriteDrawableList);

        } else {
            getCategoriesList();
            if (categoriesList == null || categoriesList.size() == 0) {
                finish();
            }
            adapterImages = new PicShowAdapter(PicShowActivity.this, categoriesList);

        }


        recyclerView.setAdapter(adapterImages);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        recyclerView.scrollToPosition(currentPosition);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager lManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstElementPosition = lManager.findFirstVisibleItemPosition();

                Log.i("salman", "position " + firstElementPosition);

                currentPosition = firstElementPosition;

                if (firstElementPosition >= 0 && firstElementPosition < categoriesList.size()) {
                    if (favoriteUrls.contains(categoriesList.get(firstElementPosition))) {
                        likeButton.setChecked(true);
                    } else {
                        likeButton.setChecked(false);
                    }
                }

                // App.showInterstitialVerySeldom(PicShowActivity.this);
            }
        });

        if(isInAppPurchased){
            shimmerFrameLayout.setVisibility(View.GONE);
        }
        else {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            App.setBanner(PicShowActivity.this);
            App.showInterstitialSeldom(PicShowActivity.this);
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

    public void likeClickListener(View view) {

        ToggleButton likeButton = (ToggleButton) view;
        int savingPosition = currentPosition - 1;
        if (likeButton.isChecked()) {
            favoriteUrls.add(categoryName + "_" + savingPosition);
            tinyDB.putListString("favorites", favoriteUrls);
        } else {

            Log.d("TAG", "likeClickListener: current position " + currentPosition + " array list size" + favoriteUrls.size());


//                categoriesList.remove(currentPosition);
            favoriteUrls.remove(getFavUrls(savingPosition));


            tinyDB.putListString("favorites", favoriteUrls);

//

        }
        if(!isInAppPurchased){
            App.showInterstitialSeldom(PicShowActivity.this);

        }

    }

    private int getFavUrls(int Value) {
        int position = 0;
        for (int n = 0; n < favoriteUrls.size(); n++) {
            String str = favoriteUrls.get(n);
            if (str.equals(categoryName + "_" + Value)) {
                position = n;
            }
        }
        return position;
    }

    public void saveListener(View view) {
        int savingPosition = currentPosition + 1;

        if (savedUrls.contains(categoryName + "_" + savingPosition)) {
            Toast.makeText(this, "Already Saved", Toast.LENGTH_SHORT).show();
        } else {
            savedUrls.add(categoryName + "_" + savingPosition);
            tinyDB.putListString("saved", savedUrls);
            Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
        }
        if(!isInAppPurchased){
            App.showInterstitialSeldom(PicShowActivity.this);
        }
    }

    public void getCategoriesList() {
        int maxSize = 20;
        for (int i = 1; i <= maxSize; i++) {
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

    public void getFavDrawableList() {

        for (int i = 1; i <= favoriteUrls.size(); i++) {
            Drawable drawable = getResources().getDrawable(
                    getResources().getIdentifier(
                            favoriteUrls.get(i - 1),
                            "drawable",
                            getPackageName()
                    )
            );
            favoriteDrawableList.add(drawable);
        }


    }


}