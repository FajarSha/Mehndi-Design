package com.hennamehndi.mehndipatterns.bridalmehndi.designs;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.Ads_Util.InAppBilling;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.fragments.FavoriteFragment;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.fragments.HomeFragment;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.fragments.SavedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    HomeFragment homeFragment;
    FavoriteFragment favoriteFragment;
    SavedFragment savedFragment;
    MenuItem prevMenuItem;

    ImageView navibtn;
    private DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle mDrawerToggle;
    ShimmerFrameLayout container;
    Dialog adBlockdialog;
    CardView subBtn;
    InAppBilling inAppBilling = new InAppBilling(this, this);
    boolean isInAppPurchased = false;
    ImageView adInAppBtn;
    public static boolean isInAppShow = false;
    ShimmerFrameLayout shimmerFrameLayoutAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inAppBilling = new InAppBilling(this, this);
        isInAppPurchased = inAppBilling.hasUserBoughtInApp();
        shimmerFrameLayoutAd=findViewById(R.id.shimmer_view_container);

        navigationView = findViewById(R.id.nav_drawer_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        navibtn = findViewById(R.id.navibtn);
        drawer = findViewById(R.id.drawerLayout);
        adInAppBtn = findViewById(R.id.ad_remover_btn);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);

        navibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  drawer.openDrawer(Gravity.START);
                if (!drawer.isDrawerOpen(GravityCompat.START)) drawer.openDrawer(Gravity.LEFT);
                else drawer.closeDrawer(Gravity.RIGHT);
            }
        });

        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_favorite:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_menu:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(1).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

                if(!isInAppPurchased){
                    App.showInterstitialSeldom(MainActivity.this);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        Log.d("TAG", "onCreate: "+isInAppPurchased);

        /*if (isInAppPurchased) {
            adInAppBtn.setVisibility(View.GONE);
            shimmerFrameLayoutAd.setVisibility(View.GONE);
        } else {
            App.setBanner(MainActivity.this);
            adInAppBtn.setVisibility(View.VISIBLE);
            shimmerFrameLayoutAd.setVisibility(View.VISIBLE);
            showInappDialog();
            if (adBlockdialog != null && !isInAppShow) {
                adBlockdialog.show();
            }
        }*/

        setupViewPager(viewPager);
        viewPager.setCurrentItem(1);


//        Util.initExitDialog(MainActivity.this);
        Util.showNativeAdExitDialog(MainActivity.this, MainActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        inAppBilling = new InAppBilling(this, this);
        boolean isPurchased = inAppBilling.isCurrentpurchased();

        if (isPurchased) {
            inAppBilling.is_Current_Purchased = false;
            Intent i = new Intent(this, SplashActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        } else {
            isInAppPurchased = inAppBilling.hasUserBoughtInApp();
            if(isInAppPurchased){
                shimmerFrameLayoutAd.setVisibility(View.GONE);
            }else{
                App.setBanner(MainActivity.this);
                adInAppBtn.setVisibility(View.VISIBLE);
                shimmerFrameLayoutAd.setVisibility(View.VISIBLE);
                showInappDialog();
                if (adBlockdialog != null && !isInAppShow) {
                    adBlockdialog.show();
                }
            }

        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            Util.showExitDialog(MainActivity.this);
        }


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        favoriteFragment = new FavoriteFragment();
        savedFragment = new SavedFragment();

        adapter.addFragment(favoriteFragment);
        adapter.addFragment(homeFragment);
        adapter.addFragment(savedFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.rating) {
            try {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        } else if (id == R.id.more_app) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Bhagowal+Hood")));
        } else if (id == R.id.privacy_app) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/bhagowalhood/home"));
            startActivity(browserIntent);
        }
//        else if (id == R.id.removeads_menu) {
//            Util.showPurchaseDialog(MainActivity.this, MainActivity.this);
//        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

    }

    private void showInappDialog() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_scale);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        adBlockdialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        View view1 = getLayoutInflater().inflate(R.layout.in_app_layout, null);
        adBlockdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        adBlockdialog.setContentView(view1);
        Window window = adBlockdialog.getWindow();


        WindowManager.LayoutParams wlp = window.getAttributes();
        TextView closebtn = view1.findViewById(R.id.cross_btn);
        subBtn = view1.findViewById(R.id.subscribeNow);
        subBtn.startAnimation(animation);

        closebtn.setOnClickListener(view -> {
            if (adBlockdialog.isShowing()) {
                adBlockdialog.dismiss();
            }
            isInAppShow = true;
        });
        subBtn.setOnClickListener(view -> {

            inAppBilling.purchase(view);
        });
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        adBlockdialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        adInAppBtn.setOnClickListener(view -> {
            if (adBlockdialog != null) {
                adBlockdialog.show();
            }
        });
    }



}