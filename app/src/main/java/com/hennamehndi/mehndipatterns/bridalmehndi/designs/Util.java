package com.hennamehndi.mehndipatterns.bridalmehndi.designs;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.ads.AdSize;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.Ads_Util.InAppBilling;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.model.CategoryModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Util {

    static Dialog alertDialog;
    static Dialog exitDialog;

    public static ArrayList<CategoryModel> getAllData(String s) {

        ArrayList<CategoryModel> categoryModels = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(s);

            for (int i = 0; i < jsonArray.length(); i++) {
                CategoryModel categoryModel = new CategoryModel();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (!jsonObject.getString("name").equals("Popular")) {
                    categoryModel.name = jsonObject.getString("name");
                    categoryModel.icon = jsonObject.getString("icon");

                    JSONArray array = jsonObject.getJSONArray("images");

                    for (int j = 0; j < array.length(); j++) {
                        categoryModel.images.add(array.getString(j));
                    }

                    categoryModels.add(categoryModel);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return categoryModels;

    }

    public static ArrayList<String> getPopularUrls(String s) {

        ArrayList<String> urls = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(s);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.getString("name").equals("Popular")) {

                    JSONArray array = jsonObject.getJSONArray("images");

                    for (int j = 0; j < array.length(); j++) {
                        urls.add(array.getString(j));
                    }
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return urls;

    }

    public static void initExitDialog(Activity activity) {

        alertDialog = new Dialog(activity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View dialogView = View.inflate(activity, R.layout.my_exit_dialog, null);
        ConstraintLayout adLayout=dialogView.findViewById(R.id.adlayoutNative);
        CardView exitCardView = (CardView) dialogView.findViewById(R.id.yesBtn);
//        TextView rateUsTextView = (TextView) dialogView.findViewById(R.id.exit_dialog_rateTextView);
        CardView noCardView = (CardView) dialogView.findViewById(R.id.Nobtn);
//        final TextView sponTextView = (TextView) dialogView.findViewById(R.id.exit_dialog_sponTextView);

//         AdView mAdView2 = dialogView.findViewById(R.id.exit_dialog_adView);
        AdView adView = new AdView(activity);
        adView.setAdUnitId(activity.getString(R.string.banner_lowId));
        adView.setAdSize(AdSize.BANNER);

        AdRequest adRequest2 = new AdRequest.Builder().build();

        adView.loadAd(adRequest2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        adLayout.addView(adView, params);
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
//                sponTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                adView.setVisibility(View.GONE);
//                sponTextView.setVisibility(View.GONE);
            }

        });

        exitCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finishAffinity();
            }
        });

        noCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

      /*  rateUsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
                } catch (Exception e) {
                    Toast.makeText(activity, "Operation Not Supported", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        alertDialog.setContentView(dialogView);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public static void showExitDialog(final Activity activity) {
//        if (alertDialog != null && !alertDialog.isShowing()) {
//            alertDialog.show();
//        }
        if (exitDialog != null && !exitDialog.isShowing()) {
            exitDialog.show();
        }

    }
    public static void showNativeAdExitDialog(Activity activity, Context context) {
        exitDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        View view1 = LayoutInflater.from(context).inflate(R.layout.my_exit_dialog, null);
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setContentView(view1);
        InAppBilling inAppBilling = new InAppBilling(context, activity);
        boolean isInAppPurchased = inAppBilling.hasUserBoughtInApp();

        Window window = exitDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        exitDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        ConstraintLayout adnativeLayout = view1.findViewById(R.id.adlayoutNative);


       SharedPrefrenceValue sharedPrefrenceValue=new SharedPrefrenceValue(context);
       boolean is_In_App_Purchased=sharedPrefrenceValue.hasUserBoughtInApp();
//        Log.d(TAG, "showNativeAdExitDialog: "+is_In_App_Purchased);
        if (isInAppPurchased) {
            adnativeLayout.setVisibility(View.GONE);

        } else {
            showBannerAd(adnativeLayout,context);
            adnativeLayout.setVisibility(View.VISIBLE);
        }
        CardView yesBtn, NoBtn;
        yesBtn = view1.findViewById(R.id.yesBtn);
        NoBtn = view1.findViewById(R.id.Nobtn);

        yesBtn.setOnClickListener(view -> {
            if (exitDialog.isShowing()) {
                exitDialog.dismiss();
            }
//            super.onBackPressed();
           activity.finishAffinity();
        });
        NoBtn.setOnClickListener(view -> {
            exitDialog.dismiss();
        });

//        Native_adLayout = view1.findViewById(R.id.native_adLayout);


    }
    private static void showBannerAd(ConstraintLayout adLayout, Context context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = new AdView(context);
        adView.setAdUnitId(context.getString(R.string.banner_lowId));
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.loadAd(adRequest);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        adLayout.addView(adView, params);
//       this.adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                if (loadAdError != null && loadAdError.getCode() == AdRequest.ERROR_CODE_NO_FILL) {
                    Log.d(MotionEffect.TAG, "onAdFailedToLoad: banner Ad Error for " + loadAdError);

                }
            }
        });
    }

}
