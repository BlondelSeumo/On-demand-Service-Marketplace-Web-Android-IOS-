package com.dreamguys.truelysell;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dreamguys.truelysell.adapters.NotificationAdapter;
import com.dreamguys.truelysell.datamodel.Phase3.NotificationResponse;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;

import butterknife.ButterKnife;
import retrofit2.Call;

public class MyNotificationActivity extends BaseActivity implements RetrofitHandler.RetrofitResHandler {

    private RecyclerView rvNotificationList;
    private TextView noRecordsFound;
    private SwipeRefreshLayout swipeToRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        uiInit();
    }

    private void uiInit() {
        ButterKnife.bind(this);

        setToolBarTitle(AppUtils.cleanLangStr(this,
                settingScreenList.getTxtNotification().getName(), R.string.txt_notifications));

        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);
        rvNotificationList = findViewById(R.id.rv_notification_list);
        noRecordsFound = findViewById(R.id.tv_no_records_found);
        swipeToRefresh = findViewById(R.id.swipeToRefresh);
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent);
        noRecordsFound.setText(AppUtils.cleanLangStr(this,
                settingScreenList.getTxtNotification().getValidation1(), R.string.txt_no_notifications_found));
        getNotificationList();

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotificationList();
                swipeToRefresh.setRefreshing(false);
            }
        });
    }

    public void getNotificationList() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<NotificationResponse> classificationCall = apiService.getNotificationList(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.NOTIFICATION_LIST, this, false);
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
            case AppConstants.NOTIFICATION_LIST:
                NotificationResponse notificationResponse = (NotificationResponse) myRes;
                if (notificationResponse.getData() != null && notificationResponse.getData().getNotificationList().size() > 0) {
                    swipeToRefresh.setVisibility(View.VISIBLE);
                    noRecordsFound.setVisibility(View.GONE);
                    rvNotificationList.setAdapter(new NotificationAdapter(this, notificationResponse.getData().getNotificationList()));
                } else {
                    swipeToRefresh.setVisibility(View.GONE);
                    noRecordsFound.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {
        callError();
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {
        callError();
    }

    private void callError() {
        swipeToRefresh.setVisibility(View.GONE);
        noRecordsFound.setVisibility(View.VISIBLE);
        AppUtils.showToast(getApplicationContext(), getString(R.string.try_again));
    }
}
