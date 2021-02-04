package com.dreamguys.truelysell.fragments.phase3;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamguys.truelysell.MapsGetDirections;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.adapters.HomePopularServicesAdapter;
import com.dreamguys.truelysell.adapters.SellerOtherServicesAdapter;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ServiceAboutSellerFragment extends Fragment {


    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rv_other_services)
    RecyclerView rvOtherServices;
    Unbinder unbinder;
    HomePopularServicesAdapter homePopularServicesAdapter;
    GETServiceDetails.SellerOverview sellerOverview;
    @BindView(R.id.iv_user_image)
    CircleImageView ivUserImage;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_email_address)
    TextView tvEmailAddress;
    @BindView(R.id.tv_mobile_no)
    TextView tvMobileNo;
    @BindView(R.id.iv_locations)
    ImageView ivLocations;
    @BindView(R.id.tv_provider_addr)
    TextView tvProviderAddr;
    @BindView(R.id.iv_other_services)
    ImageView ivOtherServices;
    @BindView(R.id.tv_viewonmap)
    TextView tvViewonmap;
    @BindView(R.id.rl_other_services)
    RelativeLayout rlOtherServices;
    @BindView(R.id.tv_no_records_found)
    TextView tvNoRecordsFound;
    @BindView(R.id.iv_call)
    ImageView ivCall;
    @BindView(R.id.iv_chat)
    ImageView ivChat;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.txt_other_services)
    TextView txtOtherServices;
    LanguageResponse.Data.Language.BookingDetailService bookingServiceScreenList;

    public ServiceAboutSellerFragment(GETServiceDetails.SellerOverview sellerOverview) {
        this.sellerOverview = sellerOverview;
    }

    public ServiceAboutSellerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView;
        mView = inflater.inflate(R.layout.fragment_about_the_seller, container, false);
        unbinder = ButterKnife.bind(this, mView);

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivUserImage.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                ivUserImage.setBorderColor(AppUtils.getPrimaryAppTheme(getActivity()));
                ivLocations.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                ivOtherServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                tvViewonmap.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                ivCall.setImageTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                ivChat.setImageTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvViewonmap.setTextColor(AppUtils.getPrimaryAppTheme(getActivity()));
            }
        }

        if (sellerOverview != null) {
           Picasso.get()
                    .load(AppConstants.BASE_URL + sellerOverview.getProfileImg())
                    .placeholder(R.drawable.ic_user_placeholder)
                    .error(R.drawable.ic_user_placeholder)
                    .into(ivUserImage);

            tvUsername.setText(sellerOverview.getName());
            tvEmailAddress.setText(sellerOverview.getEmail());
            tvProviderAddr.setText(sellerOverview.getLocation());
            tvMobileNo.setText("+" + sellerOverview.getCountryCode() + " " + sellerOverview.getMobileno());

            if (sellerOverview.getServices().size() > 0) {
                linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                rvOtherServices.setLayoutManager(linearLayoutManager);
                rvOtherServices.setAdapter(new SellerOtherServicesAdapter(getActivity(), sellerOverview.getServices()));
            } else {
                tvNoRecordsFound.setVisibility(View.VISIBLE);
            }

            tvViewonmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callDirectionAct = new Intent(getActivity(), MapsGetDirections.class);
                    callDirectionAct.putExtra(AppConstants.LATITUDE, sellerOverview.getLatitude());
                    callDirectionAct.putExtra(AppConstants.LONGITUDE, sellerOverview.getLongitude());
                    startActivity(callDirectionAct);
                }
            });

        }

        getLocale();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_locations)
    public void onViewClicked() {
    }

    private void getLocale() {
        try {
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.BookingDetailService);
            bookingServiceScreenList = new Gson().fromJson(commonDataStr, LanguageResponse.Data.Language.BookingDetailService.class);

            txtLocation.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblLocation().getName(),
                    R.string.txt_location));
            tvViewonmap.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblViewMap().getName(),
                    R.string.txt_view_on_map));
            txtOtherServices.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLbl_other_services().getName(),
                    R.string.txt_view_on_map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
