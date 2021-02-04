package com.dreamguys.truelysell;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamguys.truelysell.adapters.MyRequestsAdapter;
import com.dreamguys.truelysell.datamodel.Phase3.DAOMyRequests;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class MyBookingRequestsActivity extends BaseActivity implements RetrofitHandler.RetrofitResHandler {


    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rv_myservices)
    RecyclerView rvMyservices;
    @BindView(R.id.tv_no_records_found)
    TextView tvNoRecordsFound;
    HashMap<String, String> postUpdateBookingStatus = new HashMap<>();
    @BindView(R.id.iv_popular_services)
    ImageView ivPopularServices;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.txt_my_providers)
    TextView txtMyProviders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_services);
        ButterKnife.bind(this);

        setToolBarTitle(AppUtils.cleanLangStr(this,
                homeStringsList.getLblBuyerRequest().getName(), R.string.txt_buyer_request));
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);


        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivFilter.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
                ivPopularServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(this)));
            }
        }

        txtMyProviders.setText(AppUtils.cleanLangStr(this,
                homeStringsList.getLblBuyerRequest().getName(), R.string.txt_buyer_request));
        tvNoRecordsFound.setText(AppUtils.cleanLangStr(this,
                homeStringsList.getLblNoRecordsFound().getName(), R.string.txt_no_records_found));

    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.MYREQUESTS:
                DAOMyRequests daoMyRequests = (DAOMyRequests) myRes;
                if (daoMyRequests.getData() != null && daoMyRequests.getData().size() > 0) {
                    rvMyservices.setVisibility(View.VISIBLE);
                    tvNoRecordsFound.setVisibility(View.GONE);
                    linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    rvMyservices.setLayoutManager(linearLayoutManager);
                    rvMyservices.setAdapter(new MyRequestsAdapter(this, MyBookingRequestsActivity.this, daoMyRequests.getData()));
                } else {
                    rvMyservices.setVisibility(View.GONE);
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

    public void getMyServicesList() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<DAOMyRequests> classificationCall = apiService.getMyBookingRequest(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.MYREQUESTS, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(this, getString(R.string.txt_enable_internet));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getMyServicesList();
    }
}
