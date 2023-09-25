package com.hennamehndi.mehndipatterns.bridalmehndi.designs.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.Ads_Util.InAppBilling;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.App;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.adapters.CategoriesAdapter;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.banner.BannerLayout;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.banner.BaseBannerAdapter;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.AllDesignActivity;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.PicShowActivity;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.R;
import com.hennamehndi.mehndipatterns.bridalmehndi.designs.view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment implements CategoriesAdapter.ItemClickListener {

    Context context;

    RecyclerView recyclerView;
    CategoriesAdapter categoriesAdapter;
    ArrayList<CatModel> categoriesList = new ArrayList<>();

    ArrayList<Drawable> popularUrls = new ArrayList<>();
    ArrayList<String> categoriesNameList = new ArrayList<>(Arrays.asList("Arabic mehndi designs", "Full Feet mehndi designs","Indian mehndi designs","Pakistan mehndi designs","Wedding mehndi designs","Simple mehndi Design","Full Arm mehndi Design","Eid mehndi design"));
    ArrayList<String> hiddenNameList = new ArrayList<>(Arrays.asList("arabic_designs", "feet_designs","indian_designs","pakistani_designs","wedding_designs","simple_design","full_arm","eid_designs"));
    BaseBannerAdapter webBannerAdapter;
    BannerLayout bannerLayout;
    InAppBilling inAppBilling=new InAppBilling(getContext(),getActivity());
    boolean isInAppPurchased=false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        inAppBilling = new InAppBilling(getContext(), getActivity());
        isInAppPurchased = inAppBilling.hasUserBoughtInApp();
        context = container.getContext();
        getCategoriesList();
        getPopularList();

        bannerLayout = view.findViewById(R.id.bannerLayout);
        webBannerAdapter = new BaseBannerAdapter(context, popularUrls);
        webBannerAdapter.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(context, PicShowActivity.class);
                intent.putExtra("position", 19);
                intent.putExtra("catName", hiddenNameList.get(position));
                intent.putExtra("category_name", "Popular");
                startActivity(intent);

            }
        });
        bannerLayout.setAdapter(webBannerAdapter);

        recyclerView = view.findViewById(R.id.recyclerView);

        int spanCount = 2;
        int spacing = 80;
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);

        categoriesAdapter = new CategoriesAdapter(getActivity(), categoriesList);
        categoriesAdapter.setClickListener(this);
        recyclerView.setAdapter(categoriesAdapter);

        return view;
    }




    @Override
    public void onItemClick(View view, int position) {

        if (getActivity() != null) {
            inAppBilling=new InAppBilling(getContext(),getActivity());
            isInAppPurchased=inAppBilling.hasUserBoughtInApp();
            if(!isInAppPurchased){
                App.showInterRecycler(getActivity(),hiddenNameList.get(position),categoriesNameList.get(position));

            }
            else{
                Intent intent = new Intent(getActivity(), AllDesignActivity.class);
                intent.putExtra("catName", hiddenNameList.get(position));
                intent.putExtra("name", categoriesNameList.get(position));
                startActivity(intent);
            }

          /*  Intent intent = new Intent(getActivity(), AllDesignActivity.class);
            intent.putExtra("catName", hiddenNameList.get(position));
            intent.putExtra("name", categoriesNameList.get(position));
            startActivity(intent);*/

        }
    }

    public  void getCategoriesList(){

        for (int i=1;i<=8;i++) {
            Drawable drawable = getActivity().getResources().getDrawable(
                    getResources().getIdentifier(
                            "menu_"+i,
                            "drawable",
                            getActivity().getPackageName()
                    )
            );
            categoriesList.add(new CatModel(categoriesNameList.get(i-1),drawable));
        }


    }
    public  void getPopularList(){

        for (int i=1;i<=4;i++) {
            Drawable drawable = getActivity().getResources().getDrawable(
                    getResources().getIdentifier(
                            "popular_"+i,
                            "drawable",
                            getActivity().getPackageName()
                    )
            );
            popularUrls.add(drawable);
        }


    }

}