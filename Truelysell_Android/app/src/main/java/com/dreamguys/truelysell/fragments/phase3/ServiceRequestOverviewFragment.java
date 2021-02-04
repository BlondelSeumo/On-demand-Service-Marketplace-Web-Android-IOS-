package com.dreamguys.truelysell.fragments.phase3;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dreamguys.truelysell.utils.AppUtils;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.dreamguys.truelysell.R;

public class ServiceRequestOverviewFragment extends Fragment {


    @BindView(R.id.tv_service_name)
    TextView tvServiceName;
    @BindView(R.id.tv_service_price)
    TextView tvServicePrice;
    @BindView(R.id.tv_views)
    TextView tvViews;
    @BindView(R.id.forecast_city_picker)
    DiscreteScrollView forecastCityPicker;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.rb_rating)
    RatingBar rbRating;
    @BindView(R.id.tv_rating_count)
    TextView tvRatingCount;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.iv_popular_services)
    ImageView ivPopularServices;
    Unbinder unbinder;
    private DiscreteScrollView itemPicker;
    GETServiceDetails.ServiceOverview serviceOverview;

    public ServiceRequestOverviewFragment() {

    }

    public ServiceRequestOverviewFragment(GETServiceDetails.ServiceOverview serviceOverview) {
        this.serviceOverview = serviceOverview;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView;
        mView = inflater.inflate(R.layout.fragment_service_overview, container, false);
        unbinder = ButterKnife.bind(this, mView);

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvCategory.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            }
        }

//        ViewPager pager = (ViewPager) mView.findViewById(R.id.viewpager);
//        PagerAdapter adapter = new ServiceImagesAdapter(getActivity());
//        pager.setAdapter(adapter);
//
//        TabLayout tabLayout = (TabLayout) mView.findViewById(R.id.tab_layout);
//        tabLayout.setupWithViewPager(pager, true);
//
//        itemPicker = (DiscreteScrollView) mView.findViewById(R.id.forecast_city_picker);
//        itemPicker.setSlideOnFling(true);
//        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
////        infiniteAdapter = InfiniteScrollAdapter.wrap(new ForecastAdapter(data));
//        itemPicker.setAdapter(new ForecastAdapter(serviceOverview.getServiceImage()));
//        itemPicker.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
//        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
//                .setMinScale(0.9f)
//                .build());
//
//
//        tvServiceName.setText(serviceOverview.getServiceTitle());
//        tvServicePrice.setText(Html.fromHtml(serviceOverview.getCurrency()) + serviceOverview.getServiceAmount());
//        tvCategory.setText(serviceOverview.getCategoryName());
//        rbRating.setRating(Float.parseFloat(serviceOverview.getRatings()));
//        tvDescription.setText(Html.fromHtml(serviceOverview.getAbout()));


        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
