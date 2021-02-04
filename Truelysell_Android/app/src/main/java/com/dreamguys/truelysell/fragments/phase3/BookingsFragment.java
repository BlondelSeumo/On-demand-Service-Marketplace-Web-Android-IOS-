package com.dreamguys.truelysell.fragments.phase3;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.HomeActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.adapters.BookingServicesAdapter;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOBookingList;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class BookingsFragment extends Fragment implements RetrofitHandler.RetrofitResHandler {

    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rv_bookings)
    RecyclerView rvBookings;
    Unbinder unbinder;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    TextView tvStatusAll, tvStatusInProgress, tvStatusCompleted, tvStatusCancelled, tv_statustype;
    @BindView(R.id.tv_no_records_found)
    TextView tvNoRecordsFound;
    HomeActivity homeActivity;
    @BindView(R.id.iv_popular_services)
    ImageView ivPopularServices;
    @BindView(R.id.txt_booking_lists)
    TextView txtBookingLists;
    @BindView(R.id.iv_userlogin)
    ImageView ivUserlogin;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tb_toolbar)
    Toolbar tbToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.txt_bookings)
    TextView txtBookings;
    public LanguageResponse.Data.Language.BookingService bookingServiceScreenList;
    public LanguageResponse.Data.Language.HomeScreen homeScreenList;

    public BookingsFragment(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    public BookingsFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_bookings, container, false);
        unbinder = ButterKnife.bind(this, mView);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvBookings.setLayoutManager(linearLayoutManager);

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivPopularServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                ivFilter.setImageTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            }
        }

        return mView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getHomeList(String status) {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<DAOBookingList> classificationCall = apiService.bookingList(PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.USER_TYPE), status);
                RetrofitHandler.executeRetrofit(getActivity(), classificationCall, AppConstants.BOOKINGLIST, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(getActivity(), getString(R.string.txt_enable_internet));
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
                    rvBookings.setAdapter(new BookingServicesAdapter(getActivity(), daoBookingList.getData(),
                            bookingServiceScreenList));
                } else {
                    rvBookings.setVisibility(View.GONE);
                    tvNoRecordsFound.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), daoBookingList.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
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
        getLocale();
        if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
            getHomeList("1");
        } else {
//            homeActivity.showUserLoginDialog();
            homeActivity.getLoginType();
        }


    }

    @OnClick(R.id.iv_filter)
    public void onViewClicked() {
        showUserLoginDialog();
    }

    public void showUserLoginDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_change_statustype, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        tvStatusAll = view.findViewById(R.id.tv_status_all);
        tv_statustype = view.findViewById(R.id.tv_statustype);
        tvStatusInProgress = view.findViewById(R.id.tv_status_inprogress);
        tvStatusCompleted = view.findViewById(R.id.tv_status_completed);
        tvStatusCancelled = view.findViewById(R.id.tv_status_cancelled);

        try {
            tv_statustype.setText(AppUtils.cleanLangStr(getActivity(), homeScreenList.getLblFilterStatusType().getName(),
                    R.string.txt_change_status_type));
            tvStatusAll.setText(AppUtils.cleanLangStr(getActivity(), homeScreenList.getLblFilterAll().getName(),
                    R.string.txt_all));
            tvStatusInProgress.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblInprogress().getName(),
                    R.string.txt_inprogress));
            tvStatusCompleted.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblCompleted().getName(),
                    R.string.txt_completed));
            tvStatusCancelled.setText(AppUtils.cleanLangStr(getActivity(), bookingServiceScreenList.getLblCancelled().getName(),
                    R.string.txt_cancelled));
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private void getLocale() {
        try {
            String bookingStr = PreferenceStorage.getKey(CommonLangModel.BookingService);
            bookingServiceScreenList = new Gson().fromJson(bookingStr, LanguageResponse.Data.Language.BookingService.class);
            String homeStr = PreferenceStorage.getKey(CommonLangModel.HomeString);
            homeScreenList = new Gson().fromJson(homeStr, LanguageResponse.Data.Language.HomeScreen.class);

            txtBookingLists.setText(AppUtils.cleanLangStr(getActivity(), homeScreenList.getLblBookingList().getName(),
                    R.string.txt_booking_lists));
            txtBookings.setText(AppUtils.cleanLangStr(getActivity(), homeScreenList.getLblBooking().getName(),
                    R.string.txt_bookings));
            tvNoRecordsFound.setText(AppUtils.cleanLangStr(getActivity(), homeScreenList.getLblNoRecordsFound().getName(),
                    R.string.txt_no_records_found));
        } catch (Exception e) {
            bookingServiceScreenList = new LanguageResponse().new Data().new Language().new BookingService();
            homeScreenList = new LanguageResponse().new Data().new Language().new HomeScreen();
        }
    }
}
