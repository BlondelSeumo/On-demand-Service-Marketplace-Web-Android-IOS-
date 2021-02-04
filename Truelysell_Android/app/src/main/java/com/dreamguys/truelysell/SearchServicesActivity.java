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

import com.dreamguys.truelysell.adapters.SearchServicesAdapter;
import com.dreamguys.truelysell.datamodel.Phase3.DAOSearchServices;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class SearchServicesActivity extends BaseActivity implements RetrofitHandler.RetrofitResHandler {


    @BindView(R.id.rv_search_services)
    RecyclerView rvSearchServices;
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.tv_no_records_found)
    TextView tvNoRecordsFound;
    @BindView(R.id.iv_popular_services)
    ImageView ivPopularServices;
    @BindView(R.id.txt_no_notifications_found)
    TextView txtNoNotificationsFound;
    private String value = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_services);
        ButterKnife.bind(this);
        value = getIntent().getStringExtra(AppConstants.SEARCHTEXT);
        setToolBarTitle(value);
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);

        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivPopularServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(this)));
            }
        }

        getSearchList(value);

        txtNoNotificationsFound.setText(
                AppUtils.cleanLangStr(this,
                        homeStringsList.getLblRelatedServices().getName(), R.string.text_related_services));
        tvNoRecordsFound.setText(
                AppUtils.cleanLangStr(this,
                        homeStringsList.getLblNoRecordsFound().getName(), R.string.txt_no_records_found));

    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {

        switch (responseType) {
            case AppConstants.SEARCHSERVICES:
                DAOSearchServices daoSearchServices = (DAOSearchServices) myRes;
                if (daoSearchServices.getData() != null && daoSearchServices.getData().size() > 0) {
                    rvSearchServices.setVisibility(View.VISIBLE);
                    tvNoRecordsFound.setVisibility(View.GONE);
                    linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    rvSearchServices.setLayoutManager(linearLayoutManager);
                    rvSearchServices.setAdapter(new SearchServicesAdapter(this, daoSearchServices.getData(), homeStringsList));
                } else {
                    rvSearchServices.setVisibility(View.GONE);
                    tvNoRecordsFound.setVisibility(View.VISIBLE);
                    AppUtils.showToast(this, daoSearchServices.getResponseHeader().getResponseMessage());
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

    public void getSearchList(String searchText) {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<DAOSearchServices> classificationCall = apiService.searchServices(AppConstants.DEFAULTTOKEN, searchText, PreferenceStorage.getKey(AppConstants.MY_LATITUDE), PreferenceStorage.getKey(AppConstants.MY_LONGITUDE));
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.SEARCHSERVICES, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }
        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }

}
