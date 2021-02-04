package com.dreamguys.truelysell.fragments.phase3;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
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

import com.dreamguys.truelysell.ChatDetailActivity;
import com.dreamguys.truelysell.MapsGetDirections;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.adapters.HomePopularServicesAdapter;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOBookingDetail;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ServiceAboutBuyerFragment extends Fragment {


    LinearLayoutManager linearLayoutManager;
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
    TextView tvViewonMap;
    Unbinder unbinder;
    HomePopularServicesAdapter homePopularServicesAdapter;
    DAOBookingDetail.UserDetails userDetails;
    @BindView(R.id.rl_other_services)
    RelativeLayout rlOtherServices;
    @BindView(R.id.rv_other_services)
    RecyclerView rvOtherServices;
    @BindView(R.id.iv_call)
    ImageView ivCall;
    @BindView(R.id.iv_chat)
    ImageView ivChat;
    @BindView(R.id.tv_no_records_found)
    TextView tvNoRecordsFound;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.txt_other_services)
    TextView txtOtherServices;
    LanguageResponse.Data.Language.BookingDetailService bookingServiceScreenList;

    public ServiceAboutBuyerFragment() {
    }

    public ServiceAboutBuyerFragment(DAOBookingDetail.UserDetails userDetails) {
        this.userDetails = userDetails;
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
                tvViewonMap.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvViewonMap.setTextColor(AppUtils.getPrimaryAppTheme(getActivity()));
                ivCall.setImageTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                ivChat.setImageTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            }
        }


//        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
////        homePopularServicesAdapter = new HomePopularServicesAdapter(getContext(), getHomeList.getData().getServiceList());
//        rvOtherServices.setLayoutManager(linearLayoutManager);
//        rvOtherServices.setAdapter(new SellerOtherServicesAdapter(getActivity()));

        if (userDetails != null) {
           Picasso.get()
                    .load(AppConstants.BASE_URL + userDetails.getProfileImg())
                    .placeholder(R.drawable.ic_user_placeholder)
                    .error(R.drawable.ic_user_placeholder)
                    .into(ivUserImage);

            tvUsername.setText(userDetails.getName());
            tvEmailAddress.setText(userDetails.getEmail());
            tvProviderAddr.setText(userDetails.getLocation());
            tvMobileNo.setText("+" + userDetails.getCountry_code() + " " + userDetails.getMobileno());

            rlOtherServices.setVisibility(View.GONE);
            rvOtherServices.setVisibility(View.GONE);
            ivCall.setVisibility(View.VISIBLE);
            ivChat.setVisibility(View.VISIBLE);

            tvViewonMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callDirectionAct = new Intent(getActivity(), MapsGetDirections.class);
                    callDirectionAct.putExtra(AppConstants.LATITUDE, userDetails.getLatitude());
                    callDirectionAct.putExtra(AppConstants.LONGITUDE, userDetails.getLongitude());
                    startActivity(callDirectionAct);
                }
            });

            ivChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callChatDetailAct = new Intent(getActivity(), ChatDetailActivity.class);
                    callChatDetailAct.putExtra(AppConstants.chatFrom, userDetails.getToken());
                    callChatDetailAct.putExtra(AppConstants.chatUsername, userDetails.getName());
                    callChatDetailAct.putExtra(AppConstants.chatImg, userDetails.getProfileImg());
                    AppUtils.appStartIntent(getActivity(), callChatDetailAct);
                }
            });

            ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", userDetails.getMobileno(), null));
                        startActivity(intent);
                    } catch (Exception e) {
                        if (e instanceof ActivityNotFoundException) {
                            AppUtils.showToast(getActivity(), "Dialing not supported");//TODO:
                        }
                    }
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

    private void getLocale() {
        try {
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.BookingDetailService);
            bookingServiceScreenList = new Gson().fromJson(commonDataStr, LanguageResponse.Data.Language.BookingDetailService.class);

            txtLocation.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblLocation().getName(),
                    R.string.txt_location));
            tvViewonMap.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblViewMap().getName(),
                    R.string.txt_view_on_map));
            txtOtherServices.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLbl_other_services().getName(),
                    R.string.txt_other_services));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
