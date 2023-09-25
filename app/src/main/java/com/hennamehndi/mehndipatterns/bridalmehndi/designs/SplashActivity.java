package com.hennamehndi.mehndipatterns.bridalmehndi.designs;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.Ads_Util.InAppBilling;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;

public class SplashActivity extends AppCompatActivity {
    InAppBilling inAppBilling;
    ConstraintLayout adLayout;
    AdRequest adRequest;
    boolean isInAppPurchase=false;
    TextView adText;
    ShimmerFrameLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
         container = findViewById(R.id.shimmer_view_container);
        container.showShimmer(true);
        adLayout=findViewById(R.id.adLayout);
        adText=findViewById(R.id.tv_load);
        inAppBilling=new InAppBilling(this,this);
        isInAppPurchase=inAppBilling.hasUserBoughtInApp();
        if (isInAppPurchase) {
            adText.setText("Loading ");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,TutorialActivity.class));
                }
            },4000L);
        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,TutorialActivity.class));
                }
            },5000L);
        }

    }

    private void loadBannerAd(String unitId) {
        AdView adView = new AdView(this);
        adView.setAdUnitId(unitId);
        adView.setAdSize(AdSize.BANNER);
//        adView.loadAd(adRequest);
        Bundle extras = new Bundle();
        extras.putString("collapsible", "bottom");
        adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        adView.loadAd(adRequest);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        adLayout.addView(adView, params);
//       this.adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                container.hideShimmer();
                Log.d(TAG, "onAdLoaded: Banner  Ad on Splash loaded ");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                if (loadAdError != null && loadAdError.getCode() == AdRequest.ERROR_CODE_NO_FILL) {
                    Log.d(TAG, "onAdFailedToLoad: banner Ad on Splash Error for " + loadAdError);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        inAppBilling=new InAppBilling(this,this);
        isInAppPurchase=inAppBilling.hasUserBoughtInApp();

        if(isInAppPurchase){
            adLayout.setVisibility(View.GONE);
        }
        else{
            adLayout.setVisibility(View.VISIBLE);
            loadBannerAd(getString(R.string.collapsable_test_banner));
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }



}






