package com.dreamguys.truelysell.fragments.phase3;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.ViewAllServicesActivity;
import com.dreamguys.truelysell.adapters.ViewAllPopularAdapter;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOViewAllServices;
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
import butterknife.Unbinder;
import retrofit2.Call;

public class ViewAllFeaturedServicesFragment extends Fragment implements RetrofitHandler.RetrofitResHandler {


    ViewAllPopularAdapter viewAllPopularAdapter;
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rv_viewall_services)
    RecyclerView rvViewallServices;
    Unbinder unbinder;
    @BindView(R.id.tv_services)
    TextView tvServices;
    ViewAllServicesActivity mActivity;
    Context mContext;
    @BindView(R.id.iv_popular_services)
    ImageView ivPopularServices;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    LanguageResponse.Data.Language.ViewAllServices viewAllStringsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_popular_services, container, false);
        unbinder = ButterKnife.bind(this, mView);

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivPopularServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
            }
        }

        getLocaleData();
        tvServices.setText(AppUtils.cleanLangStr(getActivity(),
                viewAllStringsList.getLblFeaturedServices().getName(), R.string.txt_featured_services));
        getViewAllServices("Feature");
        return mView;
    }

    public void mViewAllFeaturedServicesFragment(ViewAllServicesActivity createProviderActivity) {
        this.mActivity = createProviderActivity;
        this.mContext = createProviderActivity.getBaseContext();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getViewAllServices(String type) {
        if (AppUtils.isNetworkAvailable(getActivity())) {
//            ProgressDlg.showProgressDialog(getActivity(), null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                String token = AppConstants.DEFAULTTOKEN;
                if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                    token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
                }
                Call<DAOViewAllServices> classificationCall = apiService.getViewAllServices(type, token, PreferenceStorage.getKey(AppConstants.MY_LATITUDE), PreferenceStorage.getKey(AppConstants.MY_LONGITUDE));
                RetrofitHandler.executeRetrofit(getActivity(), classificationCall, AppConstants.VIEWALLSERVICES, this, false);
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
            case AppConstants.VIEWALLSERVICES:
                DAOViewAllServices daoViewAllServices = (DAOViewAllServices) myRes;
                if (daoViewAllServices.getData() != null && daoViewAllServices.getData().getServiceList().size() > 0) {
                    rvViewallServices.setVisibility(View.VISIBLE);
                    linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    rvViewallServices.setLayoutManager(linearLayoutManager);
                    rvViewallServices.setAdapter(new ViewAllPopularAdapter(getActivity(), daoViewAllServices.getData().getServiceList(), mActivity, viewAllStringsList));
                } else {
                    rvViewallServices.setVisibility(View.GONE);
                    AppUtils.showToast(getActivity(), daoViewAllServices.getResponseHeader().getResponseMessage());
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

//            getUserProfile();
            // Refresh your fragment here
        }
    }

    private void getLocaleData() {
        try {
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.ViewAllServices);
            viewAllStringsList = new Gson().fromJson(commonDataStr, LanguageResponse.Data.Language.ViewAllServices.class);
        } catch (Exception e) {
            viewAllStringsList = new LanguageResponse().new Data().new Language().new ViewAllServices();
        }
    }
}
