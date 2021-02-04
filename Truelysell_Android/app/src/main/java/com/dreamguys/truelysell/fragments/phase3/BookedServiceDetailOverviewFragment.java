package com.dreamguys.truelysell.fragments.phase3;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dreamguys.truelysell.DiscreteScrollViewOptions;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.adapters.ForecastAdapter;
import com.dreamguys.truelysell.adapters.ServiceImagesAdapter;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOBookingDetail;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.google.gson.Gson;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.json.JSONArray;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookedServiceDetailOverviewFragment extends Fragment {


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
    @BindView(R.id.ll_prov_desc_detail)
    LinearLayout llProvDescDetail;
    Unbinder unbinder;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.serviceOffered)
    TextView serviceOffered;
    private DiscreteScrollView itemPicker;
    ForecastAdapter forecastAdapter;
    InfiniteScrollAdapter infiniteAdapter;
    DAOBookingDetail.ServiceDetails serviceDetails;
    LanguageResponse.Data.Language.BookingDetailService bookingServiceScreenList;

    public BookedServiceDetailOverviewFragment() {

    }

    public BookedServiceDetailOverviewFragment(DAOBookingDetail.ServiceDetails serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView;
        mView = inflater.inflate(R.layout.fragment_service_overview, container, false);
        unbinder = ButterKnife.bind(this, mView);

        ViewPager pager = (ViewPager) mView.findViewById(R.id.viewpager);
        PagerAdapter adapter = new ServiceImagesAdapter(getActivity());
        pager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) mView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager, true);

        itemPicker = (DiscreteScrollView) mView.findViewById(R.id.forecast_city_picker);
        itemPicker.setSlideOnFling(true);
        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);

        forecastAdapter = new ForecastAdapter(serviceDetails.getServiceImage());
        infiniteAdapter = InfiniteScrollAdapter.wrap(forecastAdapter);

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tvCategory.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
            }
        }

        if (serviceDetails != null) {
            itemPicker.setAdapter(infiniteAdapter);
            itemPicker.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
            itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                    .setMinScale(0.8f)
                    .build());

            getLocale();
            tvServiceName.setText(serviceDetails.getServiceTitle());
            tvServicePrice.setText(Html.fromHtml(serviceDetails.getCurrency_code()) + serviceDetails.getServiceAmount());
            tvCategory.setText(serviceDetails.getCategoryName());
            rbRating.setRating(Float.parseFloat(serviceDetails.getRating()));
            tvDescription.setText(Html.fromHtml(serviceDetails.getAbout()));
            tvRatingCount.setText("(" + serviceDetails.getRatingCount() + ")");
            tvViews.setText(serviceDetails.getTotalViews() + " " + AppUtils.cleanLangStr(getActivity(),
                    bookingServiceScreenList.getLblViews().getName(), R.string.txt_view));

            if (serviceDetails.getServiceOffered() != null) {
                try {
                    JSONArray descList = new JSONArray(serviceDetails.getServiceOffered());
                    for (int i = 0; i < descList.length(); i++) {
                        addDescView(i, descList.getString(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return mView;
    }

    private synchronized void addDescView(int i, String descVal) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View inflatedLayout = inflater.inflate(R.layout.layout_bullet_textview, null, false);
        TextView tvTxtDesc = (TextView) inflatedLayout.findViewById(R.id.tv_bullet);
        tvTxtDesc.setText(descVal);
        llProvDescDetail.addView(inflatedLayout);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getLocale() {
        try {
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.BookingDetailService);
            bookingServiceScreenList = new Gson().fromJson(commonDataStr, LanguageResponse.Data.Language.BookingDetailService.class);

            description.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblDescription().getName(),
                    R.string.txt_description));
            serviceOffered.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblServiceOffered().getName(),
                    R.string.txt_service_offered));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
