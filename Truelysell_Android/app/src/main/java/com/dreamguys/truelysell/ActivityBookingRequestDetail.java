package com.dreamguys.truelysell;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.adapters.ViewPagerAdapter;
import com.dreamguys.truelysell.datamodel.EmptyData;
import com.dreamguys.truelysell.datamodel.Phase3.DAOBookingDetail;
import com.dreamguys.truelysell.datamodel.Phase3.DAOCheckAccountDetails;
import com.dreamguys.truelysell.fragments.phase3.BookedServiceDetailOverviewFragment;
import com.dreamguys.truelysell.fragments.phase3.HistoryBookingInfoFragment;
import com.dreamguys.truelysell.fragments.phase3.ServiceAboutBuyerFragment;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.dreamguys.truelysell.viewwidgets.CustomViewPager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class ActivityBookingRequestDetail extends BaseActivity implements TabLayout.OnTabSelectedListener, RetrofitHandler.RetrofitResHandler {


    @BindView(R.id.tab_service_detail)
    TabLayout tabServiceDetail;
    @BindView(R.id.viewpager_service_detail)
    CustomViewPager viewpagerServiceDetail;
    public String bookingId = "", serviceTitle = "", serviceID = "", account_details = "";
    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;
    @BindView(R.id.tv_mark_completed)
    TextView tvMarkCompleted;
    @BindView(R.id.btn_accept)
    Button btnAccept;
    @BindView(R.id.btn_reject)
    Button btnReject;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.iv_reject)
    ImageView ivReject;
    @BindView(R.id.iv_mark_accept)
    ImageView ivMarkAccept;
    @BindView(R.id.iv_complete_accept)
    ImageView ivCompleteAccept;
    @BindView(R.id.iv_complete_reject)
    ImageView ivCompleteReject;
    @BindView(R.id.rl_acceptcompleted)
    RelativeLayout rlAcceptcompleted;
    ApiInterface apiInterface;
    HashMap<String, String> postUpdateBookingStatus = new HashMap<>();
    ImageView ivExit;
    EditText etComments;
    Button btnSubmit;
    @BindView(R.id.btn_ratenow)
    Button btnRatenow;
    @BindView(R.id.btn_cancel)
    Button btnCancelService;
    String a = "";
    @BindView(R.id.txt_provider_marked_as_completed)
    TextView txtProviderMarkedAsCompleted;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_request_detail);
        ButterKnife.bind(this);

        if (AppUtils.isThemeChanged(this)) {
            tabServiceDetail.setSelectedTabIndicatorColor(AppUtils.getPrimaryAppTheme(this));
            tabServiceDetail.setTabTextColors(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
        }

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);
        rlParent.setVisibility(View.GONE);

        bookingId = getIntent().getStringExtra(AppConstants.BOOKINGID);
        serviceTitle = getIntent().getStringExtra(AppConstants.SERVICETITLE);
        setToolBarTitle(serviceTitle);

        getBookingDetails();
        setLocale();
    }

    private void setLocale() {
        try {
            btnAccept.setText(AppUtils.cleanLangStr(this,
                    bookingDetailServiceScreenList.getBtnAcceptRequest().getName(), R.string.txt_accept_request));
            btnReject.setText(AppUtils.cleanLangStr(this,
                    bookingDetailServiceScreenList.getBtnRejectRequest().getName(), R.string.txt_reject_request));
            btnRatenow.setText(AppUtils.cleanLangStr(this,
                    bookingDetailServiceScreenList.getLblRateNow().getName(), R.string.txt_ratenow));
            btnCancelService.setText(AppUtils.cleanLangStr(this,
                    bookingDetailServiceScreenList.getLblCancelService().getName(), R.string.txt_cancel_the_service));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void getBookingDetails() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<DAOBookingDetail> classificationCall = apiService.bookingDetail(PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.USER_TYPE), bookingId);
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.BOOKINGDETAIL, this, false);
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
            case AppConstants.BOOKINGDETAIL:
                String about = "";
                DAOBookingDetail daoBookingDetail = ((DAOBookingDetail) myRes);
                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

                serviceID = daoBookingDetail.getData().getServiceDetails().getService_id();

                if (PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("1")) {
                    if (daoBookingDetail.getData().getBookingDetails().getStatus().equalsIgnoreCase("1")) {
                        llBottom.setVisibility(View.VISIBLE);
                    } else if (daoBookingDetail.getData().getBookingDetails().getStatus().equalsIgnoreCase("2")) {
                        rlParent.setVisibility(View.VISIBLE);
                    }
                } else if (PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("2")) {
                    if (daoBookingDetail.getData().getBookingDetails().getStatus().equalsIgnoreCase("1")) {
                        btnCancelService.setVisibility(View.VISIBLE);
                    } else if (daoBookingDetail.getData().getBookingDetails().getStatus().equalsIgnoreCase("3")) {
                        rlAcceptcompleted.setVisibility(View.VISIBLE);
                    } else if (daoBookingDetail.getData().getBookingDetails().getStatus().equalsIgnoreCase("6") &&
                            daoBookingDetail.getData().getServiceDetails().getIs_rated().equalsIgnoreCase("0")) {
                        btnRatenow.setVisibility(View.VISIBLE);
                    }

                 //   if (daoBookingDetail.getData().getBookingDetails().getStatus().equalsIgnoreCase("6"))
                     //   btnCancelService.setVisibility(View.GONE);
                }

                if (PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("1")) {
                    about = bookingDetailServiceScreenList.getLblAboutBuyer().getName();
                } else {
                    about = bookingDetailServiceScreenList.getLblAboutSeller().getName();
                }

                adapter.addFragment(new BookedServiceDetailOverviewFragment(daoBookingDetail.getData().getServiceDetails()), AppUtils.cleanLangStr(this,
                        bookingDetailServiceScreenList.getLblOverview().getName(), R.string.txt_Overview));
                adapter.addFragment(new ServiceAboutBuyerFragment(daoBookingDetail.getData().getUserDetails()), AppUtils.cleanLangStr(this, about, R.string.txt_request));
                adapter.addFragment(new HistoryBookingInfoFragment(daoBookingDetail.getData().getBookingDetails()), AppUtils.cleanLangStr(this,
                        bookingDetailServiceScreenList.getLblBookingInfo().getName(), R.string.txt_Booking_info));

                viewpagerServiceDetail.setAdapter(adapter);
                viewpagerServiceDetail.setOffscreenPageLimit(3);
                viewpagerServiceDetail.disableScroll(true);

                //Tab
                tabServiceDetail.setupWithViewPager(viewpagerServiceDetail);
                tabServiceDetail.setOnTabSelectedListener(this);
                break;

            case AppConstants.BOOKINGSTATUS:
                EmptyData emptyData = (EmptyData) myRes;
                Toast.makeText(this, emptyData.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                finish();
                break;

            case AppConstants.CHECKACCOUNTDETAILS:
                DAOCheckAccountDetails daoCheckAccountDetails = (DAOCheckAccountDetails) myRes;
                if (daoCheckAccountDetails.getData() != null && daoCheckAccountDetails.getData().getAccountDetails() != null) {
                    account_details = daoCheckAccountDetails.getData().getAccountDetails();
                }
                break;
        }

    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }


    @OnClick({R.id.iv_reject, R.id.btn_accept, R.id.btn_reject, R.id.iv_mark_accept, R.id.iv_complete_accept, R.id.iv_complete_reject, R.id.btn_ratenow, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_accept:
                updateserviceStatus("1", ""); // provider accept
                break;
            case R.id.btn_reject:
                updateserviceStatus("2", ""); // provider reject
                break;
            case R.id.iv_mark_accept:
                updateserviceStatus("3", ""); // Provider complete
                break;
            case R.id.iv_complete_accept:
                updateserviceStatus("4", ""); // User complete accept
                break;
            case R.id.iv_complete_reject:
                showRejectServiceDialog(); // User complete reject
                break;
            case R.id.btn_ratenow:
                callBookNowAct();
                break;
            case R.id.btn_cancel:
                updateserviceStatus("2", "");
                break;
        }
    }

    public void updateserviceStatus(String status, String reason) {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            postUpdateBookingStatus = new HashMap<>();
            if (status.equalsIgnoreCase("5")) {
                postUpdateBookingStatus.put("reason", reason);
            }
            postUpdateBookingStatus.put("id", bookingId);
            postUpdateBookingStatus.put("status", status);
            postUpdateBookingStatus.put("type", PreferenceStorage.getKey(AppConstants.USER_TYPE));

            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<EmptyData> classificationCall = apiService.updateBookingStatus(PreferenceStorage.getKey(AppConstants.USER_TOKEN), postUpdateBookingStatus);
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.BOOKINGSTATUS, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }


    public void showRejectServiceDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_rejectservice_reviews, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);

        ivExit = view.findViewById(R.id.iv_exit);
        btnSubmit = view.findViewById(R.id.btn_submit);
        etComments = view.findViewById(R.id.et_comments);

        ivExit.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
        btnSubmit.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etComments.getText().toString().isEmpty()) {
                    AppUtils.showToast(ActivityBookingRequestDetail.this, "Enter comments");
                } else {
                    updateserviceStatus("5", etComments.getText().toString().trim());
                    dialog.dismiss();
                }
            }
        });

        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void callBookNowAct() {
        Intent callRatingAct = new Intent(this, RateProviderActivity.class);
        callRatingAct.putExtra(AppConstants.SERVICEID, serviceID);
        callRatingAct.putExtra(AppConstants.BOOKINGID, bookingId);
        startActivity(callRatingAct);
        finish();
    }

    public void checkAccounDetails() {
        if (AppUtils.isNetworkAvailable(this)) {
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<DAOCheckAccountDetails> classificationCall = apiService.checkAccountDetails(PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.USER_TYPE));
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.CHECKACCOUNTDETAILS, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }
        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAccounDetails();
    }
}
