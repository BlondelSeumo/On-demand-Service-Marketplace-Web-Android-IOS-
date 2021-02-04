package com.dreamguys.truelysell;

import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

import com.dreamguys.truelysell.adapters.ViewPagerAdapter;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOViewAllServices;
import com.dreamguys.truelysell.fragments.phase3.ViewAllFeaturedServicesFragment;
import com.dreamguys.truelysell.fragments.phase3.ViewAllNewServicesFragment;
import com.dreamguys.truelysell.fragments.phase3.ViewAllPopularServicesFragment;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.dreamguys.truelysell.viewwidgets.CustomViewPager;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAllServicesActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, RetrofitHandler.RetrofitResHandler {


    @BindView(R.id.tab_services)
    TabLayout tabServices;
    @BindView(R.id.viewpager_services)
    CustomViewPager viewpagerServices;
    ViewAllPopularServicesFragment viewAllPopularServicesFragment;
    ViewAllFeaturedServicesFragment viewAllFeaturedServicesFragment;
    ViewAllNewServicesFragment viewAllNewServicesFragment;
    LanguageResponse.Data.Language.ViewAllServices viewAllStringsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewall_services);
        ButterKnife.bind(this);
        getLocaleData();


        if (AppUtils.isThemeChanged(this)) {
            tabServices.setSelectedTabIndicatorColor(AppUtils.getPrimaryAppTheme(this));
            tabServices.setTabTextColors(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewAllPopularServicesFragment = new ViewAllPopularServicesFragment();
        viewAllPopularServicesFragment.mViewAllFeaturedServicesFragment(this);
        adapter.addFragment(viewAllPopularServicesFragment,
                AppUtils.cleanLangStr(this,
                        viewAllStringsList.getLblPopular().getName(), R.string.txt_popular));

        viewAllFeaturedServicesFragment = new ViewAllFeaturedServicesFragment();
        viewAllFeaturedServicesFragment.mViewAllFeaturedServicesFragment(this);
        adapter.addFragment(viewAllFeaturedServicesFragment,
                AppUtils.cleanLangStr(this,
                        viewAllStringsList.getFEATURED().getName(), R.string.txt_featured));

        viewAllNewServicesFragment = new ViewAllNewServicesFragment();
        viewAllNewServicesFragment.mViewAllFeaturedServicesFragment(this);
        adapter.addFragment(new ViewAllNewServicesFragment(), AppUtils.cleanLangStr(this,
                viewAllStringsList.getLblNewest().getName(), R.string.txt_newest));

        viewpagerServices.setAdapter(adapter);
        viewpagerServices.setOffscreenPageLimit(3);
        viewpagerServices.disableScroll(false);

        if (getIntent().getStringExtra(AppConstants.VIEWALLTYPE).equalsIgnoreCase("1")) {
            viewpagerServices.setCurrentItem(0);
            setToolBarTitle(AppUtils.cleanLangStr(this,
                    viewAllStringsList.getToolbarTitle().getName(), R.string.txt_popular_services));
        } else {
            viewpagerServices.setCurrentItem(1);
            setToolBarTitle(AppUtils.cleanLangStr(this,
                    viewAllStringsList.getLbl_newly_added_service().getName(), R.string.newly_added_services));
        }

        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);

        //Tab
        tabServices.setupWithViewPager(viewpagerServices);
        tabServices.setOnTabSelectedListener(this);

        viewpagerServices.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {

        switch (responseType) {
            case AppConstants.VIEWALLSERVICES:
                DAOViewAllServices daoViewAllServices = (DAOViewAllServices) myRes;
                break;
        }

    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    private void getLocaleData() {
        try {
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.ViewAllServices);
            viewAllStringsList = new Gson().fromJson(commonDataStr, LanguageResponse.Data.Language.ViewAllServices.class);
        } catch (Exception e) {
            viewAllStringsList = new LanguageResponse().new Data().new Language().new ViewAllServices();
        }
    }
}
