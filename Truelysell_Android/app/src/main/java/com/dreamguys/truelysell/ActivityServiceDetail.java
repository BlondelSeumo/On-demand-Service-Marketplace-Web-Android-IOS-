package com.dreamguys.truelysell;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.adapters.ViewPagerAdapter;
import com.dreamguys.truelysell.datamodel.EmptyData;
import com.dreamguys.truelysell.datamodel.Phase3.DAOGenerateOTP;
import com.dreamguys.truelysell.datamodel.Phase3.DAOLoginProfessional;
import com.dreamguys.truelysell.fragments.phase3.GETServiceDetails;
import com.dreamguys.truelysell.fragments.phase3.ServiceAboutSellerFragment;
import com.dreamguys.truelysell.fragments.phase3.ServiceCommentsFragment;
import com.dreamguys.truelysell.fragments.phase3.ServiceDetailOverviewFragment;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.dreamguys.truelysell.viewwidgets.CustomViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class ActivityServiceDetail extends BaseActivity implements TabLayout.OnTabSelectedListener, RetrofitHandler.RetrofitResHandler {


    @BindView(R.id.tab_service_detail)
    TabLayout tabServiceDetail;
    @BindView(R.id.viewpager_service_detail)
    CustomViewPager viewpagerServiceDetail;
    String serviceId = "", serviceTitle = "", serviceAmount = "", currency = "";
    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;
    @BindView(R.id.btn_book)
    Button btnBook;
    GETServiceDetails getServiceDetails;
    @BindView(R.id.txt_mark_as_completed)
    TextView txtMarkAsCompleted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        ButterKnife.bind(this);

        ivSearch.setVisibility(View.GONE);

        if (AppUtils.isThemeChanged(this)) {
            btnBook.setBackgroundColor(AppUtils.getPrimaryAppTheme(this));
            tabServiceDetail.setSelectedTabIndicatorColor(AppUtils.getPrimaryAppTheme(this));
            tabServiceDetail.setTabTextColors(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
        }

        if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
            ivUserlogin.setVisibility(View.GONE);
        }

        if (getIntent().getStringExtra(AppConstants.FROMPAGE).equalsIgnoreCase(AppConstants.MYSERVICES)) {
            rlParent.setVisibility(View.GONE);
            btnBook.setVisibility(View.GONE);
        }

        serviceId = getIntent().getStringExtra(AppConstants.SERVICEID);
        serviceTitle = getIntent().getStringExtra(AppConstants.SERVICETITLE);
        setToolBarTitle(serviceTitle);

        getServiceDetails();
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

    public void getServiceDetails() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                String token = AppConstants.DEFAULTTOKEN;
                if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                    token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
                }
                Call<GETServiceDetails> classificationCall = apiService.getServiceDetails(serviceId, token);
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.SERVICEDETAIL, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.MOBILEOTP:
                DAOGenerateOTP daoGenerateOTP = (DAOGenerateOTP) myRes;
                llRegister.setVisibility(View.GONE);
                flOtpVerification.setVisibility(View.VISIBLE);
                Toast.makeText(this, daoGenerateOTP.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();

                break;
            case AppConstants.PROFESSIONALLOGIN:
                DAOLoginProfessional daoLoginProfessional = (DAOLoginProfessional) myRes;

                if (daoLoginProfessional.getData().getProviderDetails() != null) {
                    Toast.makeText(this, daoLoginProfessional.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    PreferenceStorage.setKey(AppConstants.PNAME, daoLoginProfessional.getData().getProviderDetails().getName());
                    PreferenceStorage.setKey(AppConstants.PEMAIL, daoLoginProfessional.getData().getProviderDetails().getEmail());
                    PreferenceStorage.setKey(AppConstants.PMOBILENO, daoLoginProfessional.getData().getProviderDetails().getMobileno());
                    PreferenceStorage.setKey(AppConstants.PIMAGE, daoLoginProfessional.getData().getProviderDetails().getProfileImg());
                    PreferenceStorage.setKey(AppConstants.USER_TOKEN, daoLoginProfessional.getData().getProviderDetails().getToken());
                    PreferenceStorage.setKey(AppConstants.USER_TYPE, daoLoginProfessional.getData().getProviderDetails().getType());

                    if (userLoginDialog != null) {
                        userLoginDialog.dismiss();
                    }

                    Intent callBookServiceAct = new Intent(this, ActivityBookService.class);
                    callBookServiceAct.putExtra(AppConstants.SERVICEID, serviceId);
                    callBookServiceAct.putExtra(AppConstants.SERVICEAMOUNT, serviceAmount);
                    callBookServiceAct.putExtra(AppConstants.SERVICETITLE, serviceTitle);
                    callBookServiceAct.putExtra(AppConstants.CURRENCY, Html.fromHtml(currency));
                    startActivity(callBookServiceAct);

                } else {
                    Toast.makeText(this, daoLoginProfessional.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case AppConstants.SERVICEDETAIL:
                getServiceDetails = ((GETServiceDetails) myRes);
                if (PreferenceStorage.getKey(AppConstants.USER_TYPE) != null && PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("2")) {
                    updateViews();
                }
                serviceAmount = getServiceDetails.getData().getServiceOverview().getServiceAmount();
                currency = getServiceDetails.getData().getServiceOverview().getCurrency();

                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                adapter.addFragment(new ServiceDetailOverviewFragment(getServiceDetails.getData().getServiceOverview()), AppUtils.cleanLangStr(this,
                        bookingDetailServiceScreenList.getLblOverview().getName(), R.string.txt_Overview));
                adapter.addFragment(new ServiceAboutSellerFragment(getServiceDetails.getData().getSellerOverview()),
                        AppUtils.cleanLangStr(this, bookingDetailServiceScreenList.getLblAboutSeller().getName(), R.string.txt_about_seller));
                adapter.addFragment(new ServiceCommentsFragment(getServiceDetails.getData().getReviews()), AppUtils.cleanLangStr(this,
                        bookingServiceScreenList.getLblReviews().getName(), R.string.txt_reviews));

                viewpagerServiceDetail.setAdapter(adapter);
                viewpagerServiceDetail.setOffscreenPageLimit(3);
                viewpagerServiceDetail.disableScroll(true);

                //Tab
                tabServiceDetail.setupWithViewPager(viewpagerServiceDetail);
                tabServiceDetail.setOnTabSelectedListener(this);
                break;
            case AppConstants.VIEWS:
                EmptyData emptyData = (EmptyData) myRes;
                break;
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.MOBILEOTP:
                DAOGenerateOTP daoGenerateOTP = (DAOGenerateOTP) myRes;
                break;
        }
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @OnClick(R.id.btn_book)
    public void onViewClicked() {
        checkUserLogin(serviceId, serviceAmount, serviceTitle, currency);
    }

    public void updateViews() {
        if (AppUtils.isNetworkAvailable(this)) {
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<EmptyData> classificationCall = apiService.updateViews(PreferenceStorage.getKey(AppConstants.USER_TOKEN), serviceId);
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.VIEWS, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }

}
