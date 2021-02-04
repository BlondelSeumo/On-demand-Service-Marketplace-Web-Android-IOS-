package com.dreamguys.truelysell.fragments.phase3;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamguys.truelysell.ActivityCreateService;
import com.dreamguys.truelysell.GoToSubscriptionActivity;
import com.dreamguys.truelysell.MyBookingRequestsActivity;
import com.dreamguys.truelysell.MyProviderServicesActivity;
import com.dreamguys.truelysell.ProviderBookingsActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOProviderDashboard;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class DashboardFragment extends Fragment implements RetrofitHandler.RetrofitResHandler {


    Unbinder unbinder;
    ApiInterface apiInterface;
    @BindView(R.id.tv_my_services)
    TextView tvMyServices;
    @BindView(R.id.tv_buyer_request)
    TextView tvBuyerRequest;
    @BindView(R.id.tv_inprogress)
    TextView tvInprogress;
    @BindView(R.id.tv_completed)
    TextView tvCompleted;
    @BindView(R.id.tv_appname)
    TextView tvAppname;
    @BindView(R.id.tv_app_service)
    TextView tvAppService;
    @BindView(R.id.fab_add_service)
    FloatingActionButton fabAddService;
    @BindView(R.id.iv_payment)
    ImageView ivPayment;
    @BindView(R.id.iv_buyer_request)
    ImageView ivBuyerRequest;
    @BindView(R.id.iv_pending_services)
    ImageView ivPendingServices;
    @BindView(R.id.iv_completed_services)
    ImageView ivCompletedServices;
    @BindView(R.id.txt_my_providers)
    TextView txtMyProviders;
    @BindView(R.id.txt_buyer_request)
    TextView txtBuyerRequest;
    @BindView(R.id.txt_inprogress_services)
    TextView txtInprogressServices;
    @BindView(R.id.txt_completed_services)
    TextView txtCompletedServices;
    LanguageResponse.Data.Language.CommonStrings commonStringsList;
    LanguageResponse.Data.Language.HomeScreen homeStringsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mVIew = inflater.inflate(R.layout.fragment_provider_dashboard, container, false);
        unbinder = ButterKnife.bind(this, mVIew);

        //String appService = "<font color='#FFFFFF'>World's Largest </font>" + "<font color='" + AppUtils.getSecondaryAppTheme(getActivity()) + "'>Marketplace</font>";
        String appName = "<font color='" + AppUtils.getSecondaryAppTheme(getActivity()) + "'>TRUELY</font>" + "<font color='#FFFFFF'>SELL</font>";

        tvAppname.setText(Html.fromHtml(appName));

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fabAddService.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                ivPayment.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                ivBuyerRequest.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                ivCompletedServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                ivPendingServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
            }
        }

        getLocaleData();
        String appService = "<font color='#FFFFFF'>" + AppUtils.cleanLangStr(getActivity(),
                commonStringsList.getLblWorldsLargest().getName(), R.string.txt_worlds_largest) + "</font>" + "<font color='" +
                AppUtils.getSecondaryAppTheme(getActivity()) + "'>" + AppUtils.cleanLangStr(getActivity(),
                commonStringsList.getLblMarketPlace().getName(), R.string.txt_marketplace) + "</font>";
        tvAppService.setText(Html.fromHtml(appService));

        txtMyProviders.setText(AppUtils.cleanLangStr(getActivity(),
                homeStringsList.getLblMyServices().getName(), R.string.txt_my_providers));
        txtBuyerRequest.setText(AppUtils.cleanLangStr(getActivity(),
                homeStringsList.getLblBuyerRequest().getName(), R.string.txt_buyer_request));
        txtInprogressServices.setText(AppUtils.cleanLangStr(getActivity(),
                homeStringsList.getLblInprogressServices().getName(), R.string.txt_inprogress_services));
        txtCompletedServices.setText(AppUtils.cleanLangStr(getActivity(),
                homeStringsList.getLblCompletedServices().getName(), R.string.txt_completed_services));

        return mVIew;
    }

  /*  @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }*/

    @OnClick({R.id.ll_my_services, R.id.ll_buyer_request, R.id.ll_pending_services, R.id.ll_completed_services, R.id.fab_add_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_my_services:
                startActivity(new Intent(getActivity(), MyProviderServicesActivity.class));
                break;
            case R.id.ll_buyer_request:
                startActivity(new Intent(getActivity(), MyBookingRequestsActivity.class));
                break;
            case R.id.ll_pending_services:
                Intent callBookingAct = new Intent(getActivity(), ProviderBookingsActivity.class);
                callBookingAct.putExtra(AppConstants.SERVICESTATUS, "2");
                startActivity(callBookingAct);
                break;
            case R.id.ll_completed_services:
                Intent callBookingAct1 = new Intent(getActivity(), ProviderBookingsActivity.class);
                callBookingAct1.putExtra(AppConstants.SERVICESTATUS, "3");
                startActivity(callBookingAct1);
                break;
            case R.id.fab_add_service:
                if (PreferenceStorage.getKey(AppConstants.ISSUBSCRIBED) != null && PreferenceStorage.getKey(AppConstants.ISSUBSCRIBED).equalsIgnoreCase("1")) {
                    startActivity(new Intent(getActivity(), ActivityCreateService.class));
                } else {
                    Intent callSubscriptionAct = new Intent(getActivity(), GoToSubscriptionActivity.class);
                    callSubscriptionAct.putExtra("FromPage", AppConstants.PAGE_MY_DASHBOARD);
                    AppUtils.appStartIntent(getActivity(), callSubscriptionAct);
                }

                break;
        }
    }

    public void getDashboardDetails() {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<DAOProviderDashboard> getCategories = apiInterface.getProviderDashboard(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
            RetrofitHandler.executeRetrofit(getActivity(), getCategories, AppConstants.PROVIDERDASHBOARD, this, false);
        }
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.PROVIDERDASHBOARD:
                DAOProviderDashboard daoProviderDashboard = (DAOProviderDashboard) myRes;
                tvMyServices.setText(daoProviderDashboard.getData().getServiceCount());
                tvBuyerRequest.setText(daoProviderDashboard.getData().getPendingServiceCount());
                tvCompleted.setText(daoProviderDashboard.getData().getCompleteServiceCount());
                tvInprogress.setText(daoProviderDashboard.getData().getInprogressServiceCount());
                break;
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getDashboardDetails();
    }

    private void getLocaleData() {
        try {
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.CommonString);
            commonStringsList = new Gson().fromJson(commonDataStr, LanguageResponse.Data.Language.CommonStrings.class);
            String homeDataStr = PreferenceStorage.getKey(CommonLangModel.HomeString);
            homeStringsList = new Gson().fromJson(homeDataStr, LanguageResponse.Data.Language.HomeScreen.class);
        } catch (Exception e) {
            commonStringsList = new LanguageResponse().new Data().new Language().new CommonStrings();
            homeStringsList = new LanguageResponse().new Data().new Language().new HomeScreen();
        }
    }
}
