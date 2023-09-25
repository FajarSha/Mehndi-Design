package com.hennamehndi.mehndipatterns.bridalmehndi.designs;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.Ads_Util.InAppBilling;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.Ads_Util.MyApplication;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;

import java.util.Date;

public class App extends Application implements Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private static Context sContext;
    public static InterstitialAd mInterstitialAd;
    public static int i = 0;
    public static int recyclerCounter = 0;
    public static int j = 0;
    public Activity activity;
    private App.AppOpenAdManager appOpenAdManager;
    private Activity currentActivity;
    private static final String TAG = "MyApplication";
    static Dialog loadindAdDialogue;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

        MobileAds.initialize(this);

        loadInterstitial();
        this.registerActivityLifecycleCallbacks((ActivityLifecycleCallbacks) this);

        // Log the Mobile Ads SDK version.


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        ProcessLifecycleOwner.get().getLifecycle().addObserver((LifecycleObserver) this);
        appOpenAdManager = new App.AppOpenAdManager();
        appOpenAdManager = new App.AppOpenAdManager();

    }

    private static void showAdLoadingDialogue(Activity activity1) {

        loadindAdDialogue = new Dialog(activity1, android.R.style.Theme_Translucent_NoTitleBar);
        LayoutInflater inflater = (LayoutInflater) sContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view1 = inflater.inflate(R.layout.loading_layout, null);
        loadindAdDialogue.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadindAdDialogue.setContentView(view1);
        Window window = loadindAdDialogue.getWindow();

        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        loadindAdDialogue.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        loadindAdDialogue.show();
    }

    public static void showInterstitialSeldom(Activity activity) {
        if (i >= 3) {
            showAdLoadingDialogue(activity);
            showInterstitial(activity);
            i = 0;

        } else {
            i++;
        }
    }

    public static void showInterRecycler(Activity activity, String hiddenNames, String categoryName) {
        if (recyclerCounter >= 1) {
            showAdLoadingDialogue(activity);
            showHomeInterstitial(activity, hiddenNames, categoryName);
            recyclerCounter = 0;

        } else {

            recyclerCounter++;
            Intent intent = new Intent(activity, AllDesignActivity.class);
            intent.putExtra("catName", hiddenNames);
            intent.putExtra("name", categoryName);
            activity.startActivity(intent);
        }
    }

    private static void showHomeInterstitial(Activity activity, String str1, String str2) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(sContext, sContext.getString(R.string.interstial_highId), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                                    loadindAdDialogue.dismiss();
                                }
                                mInterstitialAd = null;
                                Intent intent = new Intent(activity, AllDesignActivity.class);
                                intent.putExtra("catName", str1);
                                intent.putExtra("name", str2);
                                activity.startActivity(intent);
//                                loadInterstitial();

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                                    loadindAdDialogue.dismiss();
                                }
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {

                            }
                        });
                        interstitialAd.show(activity);
//                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        showHomeMediumInterstitial(activity, str1, str2);
                    }

                });

    }

    private static void showHomeMediumInterstitial(Activity activity, String str1, String str2) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(sContext, sContext.getString(R.string.interstial_mediumid), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {


                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                                    loadindAdDialogue.dismiss();
                                }
                                mInterstitialAd = null;
                                Intent intent = new Intent(activity, AllDesignActivity.class);
                                intent.putExtra("catName", str1);
                                intent.putExtra("name", str2);
                                activity.startActivity(intent);
//                                loadInterstitial();

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                                    loadindAdDialogue.dismiss();
                                }
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {

                            }
                        });
                        interstitialAd.show(activity);
//                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        showHomeLowInterstitial(activity, str1, str2);
                    }

                });

    }

    private static void showHomeLowInterstitial(Activity activity, String str1, String str2) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(sContext, sContext.getString(R.string.interstial_lowId), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                                    loadindAdDialogue.dismiss();
                                }
                                mInterstitialAd = null;
                                Intent intent = new Intent(activity, AllDesignActivity.class);
                                intent.putExtra("catName", str1);
                                intent.putExtra("name", str2);
                                activity.startActivity(intent);
//                                loadInterstitial();

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                                    loadindAdDialogue.dismiss();
                                }
                                mInterstitialAd = null;
                            }

                        });
                        interstitialAd.show(activity);
//                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                            loadindAdDialogue.dismiss();
                        }
                        // Handle the error
                        Intent intent = new Intent(activity, AllDesignActivity.class);
                        intent.putExtra("catName", str1);
                        intent.putExtra("name", str2);
                        activity.startActivity(intent);
                    }

                });

    }


    public static void showInterstitialVerySeldom(Activity activity) {
        if (j >= 160) {
            showInterstitial(activity);
            j = 0;

        } else {
            j++;
        }
    }


    public static void showInterstitial(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(sContext, sContext.getString(R.string.interstial_highId), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                                    loadindAdDialogue.dismiss();
                                }
                                mInterstitialAd = null;
//                                loadInterstitial();

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                                    loadindAdDialogue.dismiss();
                                }
                                mInterstitialAd = null;
                            }

                        });
                        interstitialAd.show(activity);
//                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                        showMediumInterstitial(activity);

                    }

                });

    }

    public static void showMediumInterstitial(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(sContext, sContext.getString(R.string.interstial_mediumid), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                                    loadindAdDialogue.dismiss();
                                }
                                mInterstitialAd = null;
//                                loadInterstitial();

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                                    loadindAdDialogue.dismiss();
                                }
                                mInterstitialAd = null;
                            }


                        });
                        interstitialAd.show(activity);
//                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        showLowInterstitial(activity);
                        // Handle the error
                        mInterstitialAd = null;
                    }

                });

    }

    public static void showLowInterstitial(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(sContext, sContext.getString(R.string.interstial_lowId), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                            public void onAdDismissedFullScreenContent() {
                                if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                                    loadindAdDialogue.dismiss();
                                }
                                mInterstitialAd = null;
//                                loadInterstitial();

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                                    loadindAdDialogue.dismiss();
                                }
                                mInterstitialAd = null;
                            }

                        });
                        interstitialAd.show(activity);
//                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                            loadindAdDialogue.dismiss();
                        }
                        // Handle the error
                        mInterstitialAd = null;
                    }

                });

    }

    public static void setBanner(Activity activity) {
//        AdView mAdView = activity.findViewById(R.id.adView);
        InAppBilling inAppBilling=new InAppBilling(activity,activity);
        boolean isInAppPurchased=inAppBilling.hasUserBoughtInApp();
        ShimmerFrameLayout shimmerFrameLayout=activity.findViewById(R.id.shimmer_view_container);

        if(isInAppPurchased){
            shimmerFrameLayout.setVisibility(View.GONE);
        }
        else{
            AdView mAdView = new AdView(activity);
            mAdView.setAdUnitId(activity.getString(R.string.banner_highId));
            mAdView.setAdSize(AdSize.BANNER);

            ShimmerFrameLayout container = activity.findViewById(R.id.shimmer_view_container);
            ConstraintLayout adLayout = activity.findViewById(R.id.adLayout);

            container.showShimmer(true);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError adError) {
                    // Code to be executed when an ad request fails.
                    mediumBanner(activity);
                }

                @Override
                public void onAdLoaded() {
                    container.hideShimmer();
                }


            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            adLayout.addView(mAdView, params);
            mAdView.loadAd(adRequest);
        }

    }

    private static void mediumBanner(Activity activity) {
        AdView mAdView = new AdView(activity);
        mAdView.setAdUnitId(activity.getString(R.string.banner_mediumid));
        mAdView.setAdSize(AdSize.BANNER);

        ShimmerFrameLayout container = activity.findViewById(R.id.shimmer_view_container);
        ConstraintLayout adLayout = activity.findViewById(R.id.adLayout);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                lowBanner(activity);
            }

            @Override
            public void onAdLoaded() {
                container.hideShimmer();
            }


        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        adLayout.addView(mAdView, params);
        mAdView.loadAd(adRequest);
    }

    private static void lowBanner(Activity activity) {
        AdView mAdView = new AdView(activity);
        mAdView.setAdUnitId(activity.getString(R.string.banner_lowId));
        mAdView.setAdSize(AdSize.BANNER);

        ShimmerFrameLayout container = activity.findViewById(R.id.shimmer_view_container);
        ConstraintLayout adLayout = activity.findViewById(R.id.adLayout);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.

            }

            @Override
            public void onAdLoaded() {
                container.hideShimmer();
            }


        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        adLayout.addView(mAdView, params);
        mAdView.loadAd(adRequest);
    }


    public static void loadInterstitial() {
        // mInterstitialAd=null;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(sContext, sContext.getString(R.string.interstial_mediumid), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        if (loadindAdDialogue != null && loadindAdDialogue.isShowing()) {
                            loadindAdDialogue.dismiss();
                        }
                        // Handle the error
                        mInterstitialAd = null;
                    }

                });
    }


    /**
     * LifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onMoveToForeground() {

    }

    /**
     * ActivityLifecycleCallback methods.
     */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.


        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity;
        }
        if (currentActivity instanceof TutorialActivity) {
            appOpenAdManager.showAdIfAvailable(currentActivity);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        appOpenAdManager.appOpenAd = null;
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
        appOpenAdManager.appOpenAd = null;
    }

    /**
     * Shows an app open ad.
     *
     * @param activity                 the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    public void showAdIfAvailable(@NonNull Activity activity, @NonNull MyApplication.OnShowAdCompleteListener onShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener);
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete
     * (i.e. dismissed or fails to show).
     */
    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    /**
     * Inner class that loads and shows app open ads.
     */
    private class AppOpenAdManager {

        private static final String LOG_TAG = "AppOpenAdManager";
        private String AD_UNIT_ID = String.valueOf(R.string.open_app_highId);
        private String High_Unit_ID = String.valueOf(R.string.open_app_highId);
        private String Medium_Unit_ID = String.valueOf(R.string.open_app_mediumid);
        private String Low_Unit_ID = String.valueOf(R.string.open_app_lowId);

        private AppOpenAd appOpenAd = null;
        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;
        boolean dismisedAd = false;
        private long secondsRemaining;
        boolean showfullscreen = false;
        private InAppBilling inAppBilling;
        boolean isInAppPurchased;
        private Dialog adBlockdialog;

        private long loadTime = 0;


        public AppOpenAdManager() {
        }


        // for high unit id
        private void loadAd(Context context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();

            AppOpenAd.load(context, getString(R.string.open_app_mediumid), request, new AppOpenAd.AppOpenAdLoadCallback() {
                /**
                 * Called when an app open ad has loaded.
                 *
                 * @param ad the loaded app open ad.
                 */
                @Override
                public void onAdLoaded(AppOpenAd ad) {
                    appOpenAd = ad;
                    isLoadingAd = false;
                    loadTime = (new Date()).getTime();
//
                    Log.d(LOG_TAG, "onAdLoaded.");
                    // dismisAdif();
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    isLoadingAd = false;
                    Log.d(LOG_TAG, "onAdFailedToLoad: app open " + loadAdError.getMessage());
                    forLowloadAd(context);
                }
            });


        }


        // for medium unit id
        private void forMediumloadAd(Context context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();
            AppOpenAd.load(context, getString(R.string.open_app_mediumid), request, new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(AppOpenAd ad) {
                    appOpenAd = ad;
                    isLoadingAd = false;
                    loadTime = (new Date()).getTime();
//                    startMainActivity();
//                    createTimer(COUNTER_TIME);
                    // dismisAdif();
                    Log.d(LOG_TAG, "onAdLoaded.");
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    isLoadingAd = false;
                    forLowloadAd(context);
                    Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                }
            });
        }

        // for low unit id
        private void forLowloadAd(Context context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();
            AppOpenAd.load(context, getString(R.string.open_app_lowId), request, new AppOpenAd.AppOpenAdLoadCallback() {

                @Override
                public void onAdLoaded(AppOpenAd ad) {
                    appOpenAd = ad;
                    isLoadingAd = false;
                    // dismisAdif();
                    loadTime = (new Date()).getTime();

                    Log.d(LOG_TAG, "onAdLoaded.");
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    isLoadingAd = false;

                    Log.d(LOG_TAG, "onAdFailedToLoad: it is low ad " + loadAdError.getMessage());
                }
            });
        }

        private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * numHours));
        }


        private boolean isAdAvailable() {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
        }

        private void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(activity, new MyApplication.OnShowAdCompleteListener() {
                @Override
                public void onShowAdComplete() {
                    // Empty because the user will go back to the activity that shows the ad.
                }
            });
        }

        private void showAdIfAvailable(@NonNull final Activity activity, @NonNull MyApplication.OnShowAdCompleteListener onShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.");
                return;
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.");
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity);
                return;
            }

            Log.d(LOG_TAG, "Will show ad.");

            appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                /** Called when full screen content is dismissed. */
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Set the reference to null so isAdAvailable() returns false.
                    appOpenAd = null;
                    isShowingAd = false;
//                    startMainActivity();
                    onShowAdCompleteListener.onShowAdComplete();
                    loadAd(activity);
//
                    dismisedAd = true;
                    //startMainActivity();
                }

                /** Called when fullscreen content failed to show. */
                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    appOpenAd = null;
                    isShowingAd = false;

                    Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());

//                    onShowAdCompleteListener.onShowAdComplete();
                    loadAd(activity);
                }

                /** Called when fullscreen content is shown. */
                @Override
                public void onAdShowedFullScreenContent() {
                    showfullscreen = true;

                    Log.d(LOG_TAG, "onAdShowedFullScreenContent.");
                }
            });

            isShowingAd = true;
            InAppBilling inAppBilling1 = new InAppBilling(activity,activity);
            boolean isInappPurchased=inAppBilling1.hasUserBoughtInApp();
            if (!isInappPurchased) {
                appOpenAd.show(activity);
            }

        }

        public void releaseAppOpenAd() {
            appOpenAd = null;
        }
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

}
