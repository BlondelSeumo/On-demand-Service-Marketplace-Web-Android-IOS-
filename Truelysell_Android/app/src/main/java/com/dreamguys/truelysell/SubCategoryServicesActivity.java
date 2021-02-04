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

import com.dreamguys.truelysell.adapters.SubCategoryServicesAdapter;
import com.dreamguys.truelysell.datamodel.Phase3.DAOSubCategoryServices;
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

public class SubCategoryServicesActivity extends BaseActivity implements RetrofitHandler.RetrofitResHandler {


    @BindView(R.id.rv_categorylist)
    RecyclerView rvSubCategorylist;
    LinearLayoutManager linearLayoutManager;
    SubCategoryServicesAdapter subCategoryListAdapter;
    String CatID = "";
    @BindView(R.id.tv_no_records_found)
    TextView tvNoRecordsFound;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_popular_services)
    ImageView ivPopularServices;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory_list);
        ButterKnife.bind(this);
        setToolBarTitle(getIntent().getStringExtra(AppConstants.CATNAME));
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);
        tvTitle.setText("Related Services");
        CatID = getIntent().getStringExtra(AppConstants.SubCatID);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivPopularServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(this)));
            }
        }

        if (AppUtils.isNetworkAvailable(this)) {

            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                String token = AppConstants.DEFAULTTOKEN;
                if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                    token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
                }
                Call<DAOSubCategoryServices> classificationCall = apiService.getSubCategoryServices(CatID, token, PreferenceStorage.getKey(AppConstants.MY_LATITUDE), PreferenceStorage.getKey(AppConstants.MY_LONGITUDE));
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.SUBCATEGORIESSERVICES, this, false);
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

        DAOSubCategoryServices response = (DAOSubCategoryServices) myRes;
        if (response.getData() != null && response.getData().size() > 0) {
            rvSubCategorylist.setVisibility(View.VISIBLE);
            tvNoRecordsFound.setVisibility(View.GONE);
            rvSubCategorylist.setLayoutManager(linearLayoutManager);
            subCategoryListAdapter = new SubCategoryServicesAdapter(this, response.getData(), this);
            rvSubCategorylist.setAdapter(subCategoryListAdapter);
        } else {
            rvSubCategorylist.setVisibility(View.GONE);
            tvNoRecordsFound.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }
}
