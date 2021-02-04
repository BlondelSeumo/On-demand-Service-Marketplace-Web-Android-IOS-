package com.dreamguys.truelysell;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.braintreepayments.cardform.view.CardForm;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.dreamguys.truelysell.adapters.ViewPagerAdapter;
import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.dreamguys.truelysell.datamodel.EmptyData;
import com.dreamguys.truelysell.datamodel.Phase3.DAOCheckAccountDetails;
import com.dreamguys.truelysell.datamodel.Phase3.DAOWallet;
import com.dreamguys.truelysell.datamodel.StripeDetailsModel;
import com.dreamguys.truelysell.fragments.phase3.ChooseDateTimeFragment;
import com.dreamguys.truelysell.fragments.phase3.FragmentMapView;
import com.dreamguys.truelysell.fragments.phase3.ServiceDescriptionFragment;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.dreamguys.truelysell.viewwidgets.CustomViewPager;
import com.dreamguys.truelysell.wallet.WalletDashBoard;
import retrofit2.Call;


public class ActivityBookService extends BaseActivity implements TabLayout.OnTabSelectedListener, RetrofitHandler.RetrofitResHandler {


    @BindView(R.id.tab_book_service)
    TabLayout tabServiceDetail;
    @BindView(R.id.viewpager_book_service)
    CustomViewPager viewpagerServiceDetail;
    ChooseDateTimeFragment chooseDateTimeFragment;
    FragmentMapView fragmentMapView;
    ServiceDescriptionFragment serviceDescriptionFragment;
    public String serviceID = "", serviceAmount = "", serviceTitle = "";
    public BookService bookServiceDetails;
    public String public_key = "", secret_key = "", currency = "", account_details = "", walletAmount = "",currencyCode = "";
    private HashMap<String, String> postBookServiceParams = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_service);
        ButterKnife.bind(this);

        bookServiceDetails = new BookService();

        setToolBarTitle("Food Services");
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);

        if (AppUtils.isThemeChanged(this)) {
            tabServiceDetail.setSelectedTabIndicatorColor(AppUtils.getPrimaryAppTheme(this));
            tabServiceDetail.setTabTextColors(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
        }

        serviceID = getIntent().getStringExtra(AppConstants.SERVICEID);
        serviceAmount = getIntent().getStringExtra(AppConstants.SERVICEAMOUNT);
        serviceTitle = getIntent().getStringExtra(AppConstants.SERVICETITLE);

        bookServiceDetails.setServiceID(serviceID);
        bookServiceDetails.setAmount(serviceAmount);


        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        chooseDateTimeFragment = new ChooseDateTimeFragment();
        chooseDateTimeFragment.mChooseDateTimeFragment(this);
        adapter.addFragment(chooseDateTimeFragment, AppUtils.cleanLangStr(this, "Time and Date", R.string.txt_request));

        fragmentMapView = new FragmentMapView();
        fragmentMapView.mFragmentMapView(this);
        adapter.addFragment(fragmentMapView, AppUtils.cleanLangStr(this, "Locations", R.string.txt_request));

        serviceDescriptionFragment = new ServiceDescriptionFragment();
        serviceDescriptionFragment.mServiceDescriptionFragment(this);
        adapter.addFragment(serviceDescriptionFragment, AppUtils.cleanLangStr(this, "Description", R.string.txt_request));

        viewpagerServiceDetail.setAdapter(adapter);
        viewpagerServiceDetail.setOffscreenPageLimit(3);
        viewpagerServiceDetail.disableScroll(true);

        //Tab
        tabServiceDetail.setupWithViewPager(viewpagerServiceDetail);
        tabServiceDetail.setOnTabSelectedListener(this);

        LinearLayout tabStrip = ((LinearLayout) tabServiceDetail.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

        viewpagerServiceDetail.setOnPageChangeListener(new CustomViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    viewpagerServiceDetail.disableScroll(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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

    public void gotoNext(int position) {
        viewpagerServiceDetail.setCurrentItem(position);
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
        if (myRes instanceof StripeDetailsModel) {
            StripeDetailsModel stripeDetailsModel = (StripeDetailsModel) myRes;
            secret_key = stripeDetailsModel.getData().getSecret_key();
            public_key = stripeDetailsModel.getData().getPublishable_key();
        } else if (myRes instanceof EmptyData) {
            EmptyData emptyData = (EmptyData) myRes;
            AppUtils.showAlertDialog(ActivityBookService.this, emptyData.getResponseHeader().getResponseMessage());
        } else if (myRes instanceof DAOCheckAccountDetails) {
            DAOCheckAccountDetails daoCheckAccountDetails = (DAOCheckAccountDetails) myRes;
            if (daoCheckAccountDetails.getData() != null && daoCheckAccountDetails.getData().getAccountDetails() != null) {
                account_details = daoCheckAccountDetails.getData().getAccountDetails();
            }
        } else if (myRes instanceof DAOWallet) {
            DAOWallet daoWallet = (DAOWallet) myRes;
            walletAmount = daoWallet.getData().getWalletInfo().getWalletAmt();
            currency = daoWallet.getData().getWalletInfo().getCurrency();
            currencyCode = daoWallet.getData().getWalletInfo().getCurrencycode();
            if (serviceDescriptionFragment != null)
                serviceDescriptionFragment.tvCurrentBalance.setText(Html.fromHtml(currency) + walletAmount);
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {
        if (responseType.equalsIgnoreCase(AppConstants.BOOKSERVICE)) {
            BaseResponse baseResponse = (BaseResponse) myRes;
            if (baseResponse.getResponseHeader().getResponseCode().equalsIgnoreCase("201")) {
                Intent callAccDetailsAct = new Intent(this, WalletDashBoard.class);
                AppUtils.showCustomAlertDialog(this, baseResponse.getResponseHeader().getResponseMessage(), callAccDetailsAct);
            }
        }
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    public class BookService {
        private String fromTime = "";
        private String toTime = "";
        private String serviceID = "";
        private String serviceDate = "";
        private String latitude = "";
        private String longitude = "";
        private String location = "";
        private String notes = "";
        private String amount = "";
        private String stipeToken = "";


        public String getFromTime() {
            return fromTime;
        }

        public void setFromTime(String fromTime) {
            this.fromTime = fromTime;
        }

        public String getToTime() {
            return toTime;
        }

        public void setToTime(String toTime) {
            this.toTime = toTime;
        }

        public String getServiceID() {
            return serviceID;
        }

        public void setServiceID(String serviceID) {
            this.serviceID = serviceID;
        }

        public String getServiceDate() {
            return serviceDate;
        }

        public void setServiceDate(String serviceDate) {
            this.serviceDate = serviceDate;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStipeToken() {
            return stipeToken;
        }

        public void setStipeToken(String stipeToken) {
            this.stipeToken = stipeToken;
        }
    }

    public void generateCardPayment(CardForm cardForm) {
        ProgressDlg.showProgressDialog(this, null, null);

        postBookServiceParams.put("service_id", bookServiceDetails.getServiceID());
        postBookServiceParams.put("from_time", bookServiceDetails.getFromTime());
        postBookServiceParams.put("to_time", bookServiceDetails.getToTime());
        postBookServiceParams.put("service_date", bookServiceDetails.getServiceDate());
        postBookServiceParams.put("latitude", bookServiceDetails.getLatitude());
        postBookServiceParams.put("longitude", bookServiceDetails.getLongitude());
        postBookServiceParams.put("location", bookServiceDetails.getLocation());
        postBookServiceParams.put("notes", bookServiceDetails.getNotes());
        postBookServiceParams.put("amount", bookServiceDetails.getAmount());

        bookService();
    }

    public void bookService() {
        if (AppUtils.isNetworkAvailable(this)) {
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<EmptyData> classificationCall = apiService.postBookService(postBookServiceParams, PreferenceStorage.getKey(AppConstants.USER_TOKEN));
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.BOOKSERVICE, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }
        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }

    public void getWalletDetails() {
        if (AppUtils.isNetworkAvailable(this)) {
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            Call<DAOWallet> classificationCall = apiService.postWalletDetails(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
            RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.WALLETDETAILS, this, false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWalletDetails();
    }
}
