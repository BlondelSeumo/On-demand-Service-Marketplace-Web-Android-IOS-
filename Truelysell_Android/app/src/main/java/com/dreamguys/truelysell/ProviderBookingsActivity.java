package com.dreamguys.truelysell;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamguys.truelysell.adapters.BookingServicesAdapter;
import com.dreamguys.truelysell.datamodel.Phase3.DAOBookingList;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class ProviderBookingsActivity extends BaseActivity implements RetrofitHandler.RetrofitResHandler {

    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rv_bookings)
    RecyclerView rvBookings;
    Unbinder unbinder;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    TextView tvStatusAll, tvStatusInProgress, tvStatusCompleted, tvStatusCancelled;
    @BindView(R.id.tv_no_records_found)
    TextView tvNoRecordsFound;
    String status = "";
    @BindView(R.id.app_bar)
    AppBarLayout appbar;
    @BindView(R.id.iv_popular_services)
    ImageView ivPopularServices;
    @BindView(R.id.txt_booking_lists)
    TextView txtBookingLists;
    @BindView(R.id.txt_bookings)
    TextView txtBookings;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bookings);
        unbinder = ButterKnife.bind(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBookings.setLayoutManager(linearLayoutManager);
        setToolBarTitle(AppUtils.cleanLangStr(this,
                homeStringsList.getLblBookingList().getName(), R.string.txt_booking_lists));
        appbar.setVisibility(View.GONE);
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);
        ivFilter.setVisibility(View.GONE);
        status = getIntent().getStringExtra(AppConstants.SERVICESTATUS);

        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivFilter.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
                ivPopularServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(this)));
            }
        }
        txtBookings.setText(AppUtils.cleanLangStr(this,
                homeStringsList.getLblBooking().getName(), R.string.txt_bookings));
        tvNoRecordsFound.setText(AppUtils.cleanLangStr(this,
                homeStringsList.getLblNoService().getName(), R.string.no_services_available));
        getHomeList(status);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  unbinder.unbind();
    }

    public void getHomeList(String status) {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<DAOBookingList> classificationCall = apiService.bookingList(PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.USER_TYPE), status);
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.BOOKINGLIST, this, false);
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
            case AppConstants.BOOKINGLIST:
                DAOBookingList daoBookingList = (DAOBookingList) myRes;
                if (daoBookingList.getData() != null && daoBookingList.getData().size() > 0) {
                    rvBookings.setVisibility(View.VISIBLE);
                    tvNoRecordsFound.setVisibility(View.GONE);
                    rvBookings.setAdapter(new BookingServicesAdapter(this, daoBookingList.getData(), bookingServiceScreenList));
                } else {
                    rvBookings.setVisibility(View.GONE);
                    tvNoRecordsFound.setVisibility(View.VISIBLE);
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

    @Override
    public void onResume() {
        super.onResume();

    }

    @OnClick(R.id.iv_filter)
    public void onViewClicked() {
        showUserLoginDialog();
    }

    public void showUserLoginDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_change_statustype, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);
        TextView tvStatusType = view.findViewById(R.id.tv_statustype);
        tvStatusType.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));

        tvStatusAll = view.findViewById(R.id.tv_status_all);
        tvStatusInProgress = view.findViewById(R.id.tv_status_inprogress);
        tvStatusCompleted = view.findViewById(R.id.tv_status_completed);
        tvStatusCancelled = view.findViewById(R.id.tv_status_cancelled);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.60);

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvStatusAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHomeList("1");
                dialog.dismiss();
            }
        });
        tvStatusInProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHomeList("2");
                dialog.dismiss();
            }
        });
        tvStatusCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHomeList("3");
                dialog.dismiss();
            }
        });
        tvStatusCancelled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHomeList("4");
                dialog.dismiss();
            }
        });

    }

}
