package com.dreamguys.truelysell;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.adapters.SubscriptionAdapter;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.SubscriptionData;
import com.dreamguys.truelysell.datamodel.SubscriptionSuccessModel;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class SubscriptionActivity extends BaseActivity implements RetrofitHandler.RetrofitResHandler {

    @BindView(R.id.rv_subs_list)
    RecyclerView rvSubsList;
    @BindView(R.id.tv_skipnow)
    TextView tvSkipnow;

    LinearLayoutManager mLayoutManager;
    SubscriptionAdapter subscriptionAdapter;
    ArrayList<SubscriptionData.SubscriptionList> subsList = new ArrayList<>();

    String fromPage = null;
    Window window;
    public int appColor = 0;
    public LanguageModel.Subscription subscription_used_texts = new LanguageModel().new Subscription();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        ButterKnife.bind(this);
        try {
            setToolBarTitle(AppUtils.cleanLangStr(this,
                    subscriptionScreenList.getLblTitleSubscription().getName(), R.string.hint_subscription));
            tvSkipnow.setText(AppUtils.cleanLangStr(this,
                    subscriptionScreenList.getLblSkipNow().getName(), R.string.txt_skipnow));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);

        fromPage = getIntent().getStringExtra("FromPage");
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSubsList.setLayoutManager(mLayoutManager);
        subscriptionAdapter = new SubscriptionAdapter(this, new ArrayList<SubscriptionData.SubscriptionList>(), fromPage);
        rvSubsList.setAdapter(subscriptionAdapter);

    }

    private void getSubsDataList() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.clearDialog();
            ProgressDlg.showProgressDialog(SubscriptionActivity.this, null, null);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            try {
                Call<SubscriptionData> classificationCall = apiService.getSubsDetails(PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.MY_LANGUAGE));
                RetrofitHandler.executeRetrofit(SubscriptionActivity.this, classificationCall, AppConstants.SUBSCRIPTION_DATA, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }

    @OnClick(R.id.tv_skipnow)
    public void onViewClicked() {
        moveToDashboard();
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseModel) {

        if (myRes instanceof SubscriptionSuccessModel) {
            ProgressDlg.dismissProgressDialog();
            SubscriptionSuccessModel subscriptionSuccessModel = (SubscriptionSuccessModel) myRes;

            PreferenceStorage.setKey(AppConstants.USER_SUBS_TYPE, Integer.parseInt(subscriptionSuccessModel.getData().getSubscriberId()));
            PreferenceStorage.setKey(AppConstants.ISSUBSCRIBED, subscriptionSuccessModel.getData().getIs_subscribed());
            Toast.makeText(SubscriptionActivity.this, subscriptionSuccessModel.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
            Intent callStipeSuccessAct = new Intent(SubscriptionActivity.this, HomeActivity.class);
            AppUtils.appStartIntent(SubscriptionActivity.this, callStipeSuccessAct);

            finish();
        } else if (myRes instanceof SubscriptionData) {
            SubscriptionData myData = (SubscriptionData) myRes;
            if (myData.getData() != null && myData.getData().getSubscriptionList() != null && myData.getData().getSubscriptionList().size() > 0) {
                subsList = myData.getData().getSubscriptionList();
                subscriptionAdapter.updateRecyclerView(SubscriptionActivity.this, subsList);
            } else {
                AppUtils.showToast(SubscriptionActivity.this, "Currently no subscriptions available. So you are entering into free mode!");
                moveToDashboard();
            }
        }


    }

    private void moveToDashboard() {
        Intent gotoDashboard = null;
        if (fromPage != null) {
            switch (fromPage) {
                case AppConstants.PAGE_LOGIN:
                    gotoDashboard = new Intent(SubscriptionActivity.this, HomeActivity.class);
                    break;
            }
            if (gotoDashboard != null)
                AppUtils.appStartIntent(SubscriptionActivity.this, gotoDashboard);
        }

        finish();
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseModel) {
        AppUtils.showToast(SubscriptionActivity.this, "Currently no subscriptions available. So you are entering into free mode!");
        moveToDashboard();
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseModel) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            tvSkipnow.setText(AppUtils.cleanLangStr(this,
                    subscriptionScreenList.getLblSkipNow().getName(), R.string.txt_skipnow));
            getSubsDataList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAppTheme() {
        appColor = 0;
        try {
            String themeColor = PreferenceStorage.getKey(AppConstants.APP_THEME);
            appColor = Color.parseColor(themeColor);
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(appColor);
            }
        } catch (Exception e) {
            appColor = getResources().getColor(R.color.colorPrimary);
        }
    }

    public void subscribeUser(String subID) {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.clearDialog();
            ProgressDlg.showProgressDialog(SubscriptionActivity.this, null, null);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<SubscriptionSuccessModel> subscriptionSuccessModelCall = apiInterface.postSuccessSubscription(subID, "free", "", PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.MY_LANGUAGE));
            RetrofitHandler.executeRetrofit(SubscriptionActivity.this, subscriptionSuccessModelCall, AppConstants.SUBSCRIPTIONSUCCESS, SubscriptionActivity.this, false);
        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }


    }


}