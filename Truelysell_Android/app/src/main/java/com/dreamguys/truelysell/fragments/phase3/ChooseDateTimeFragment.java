package com.dreamguys.truelysell.fragments.phase3;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import com.dreamguys.truelysell.ActivityBookService;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.adapters.SelectDateTimeAdapter;
import com.dreamguys.truelysell.datamodel.Phase3.DAOAvailableTimeSlots;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import retrofit2.Call;


public class ChooseDateTimeFragment extends Fragment implements RetrofitHandler.RetrofitResHandler {


    @BindView(R.id.calendarView)
    HorizontalCalendarView calendarView;
    int day, month, year;
    String currendate = "";
    ActivityBookService mActivity;
    Context mContext;
    @BindView(R.id.btn_next)
    Button btnNext;
    Unbinder unbinder;
    @BindView(R.id.rv_pick_datetime)
    RecyclerView rvPickDatetime;
    GridLayoutManager gridLayoutManager;
    ApiInterface apiInterface;
    @BindView(R.id.tv_no_records_found)
    TextView tvNoRecordsFound;


    public void mChooseDateTimeFragment(ActivityBookService mActivity) {
        this.mActivity = mActivity;
        this.mContext = mActivity.getBaseContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView;
        mView = inflater.inflate(R.layout.fragement_book_datetime, container, false);
        unbinder = ButterKnife.bind(this, mView);

        gridLayoutManager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
        rvPickDatetime.setLayoutManager(gridLayoutManager);

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnNext.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            }
        }

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Calendar startDate = Calendar.getInstance();

        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);
        currendate = String.valueOf(year + "-" + (month + 1) + "-" + day);
        mActivity.bookServiceDetails.setServiceDate(currendate);
        startDate.set(year, month, day);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.YEAR, 1);

        final HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(mView, R.id.calendarView)
                .range(startDate, endDate)
                .mode(HorizontalCalendar.Mode.DAYS)
                .datesNumberOnScreen(5)
                .configure().formatBottomText("EEE").formatMiddleText("dd").formatTopText("MMM yyyy")
                .showBottomText(true)
                .textSize(10.00f, 10.00f, 10.00f)
                .end()
                .defaultSelectedDate(startDate)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
//                bookingtype = "";
//                bookingdate = "0";
//                bookingType(bookingtype);
                currendate = String.valueOf(date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DATE));
                mActivity.bookServiceDetails.setServiceDate(currendate);
                getAvailableTimeSlots();
//                getBookingList(false);
//                bookingTypeRadioGroup.clearCheck();

            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });

        getAvailableTimeSlots();

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        mActivity.gotoNext(1);
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {

        switch (responseType) {
            case AppConstants.AVAILABLETIMESLOTS:
                DAOAvailableTimeSlots daoAvailableTimeSlots = (DAOAvailableTimeSlots) myRes;
                if (daoAvailableTimeSlots.getData().getAvailability() != null && daoAvailableTimeSlots.getData().getAvailability().size() > 0) {
                    rvPickDatetime.setVisibility(View.VISIBLE);
                    tvNoRecordsFound.setVisibility(View.GONE);
                    mActivity.bookServiceDetails.setToTime("");
                    mActivity.bookServiceDetails.setFromTime("");
                    rvPickDatetime.setAdapter(new SelectDateTimeAdapter(getActivity(), daoAvailableTimeSlots.getData().getAvailability(), mActivity.bookServiceDetails));
                } else {
                    tvNoRecordsFound.setText(daoAvailableTimeSlots.getResponseHeader().getResponseMessage());
                    rvPickDatetime.setVisibility(View.GONE);
                    tvNoRecordsFound.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.AVAILABLETIMESLOTS:
                DAOAvailableTimeSlots daoAvailableTimeSlots = (DAOAvailableTimeSlots) myRes;
                if (daoAvailableTimeSlots.getData() == null) {
                    rvPickDatetime.setVisibility(View.GONE);
                } else {
                    rvPickDatetime.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    public void getAvailableTimeSlots() {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            Call<DAOAvailableTimeSlots> classificationCall = apiInterface.getAvailableTimeSlots(PreferenceStorage.getKey(AppConstants.USER_TOKEN), mActivity.serviceID, currendate);
            RetrofitHandler.executeRetrofit(getActivity(), classificationCall, AppConstants.AVAILABLETIMESLOTS, this, false);
        }
    }
}
