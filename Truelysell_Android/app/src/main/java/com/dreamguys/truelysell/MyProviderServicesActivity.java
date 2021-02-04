package com.dreamguys.truelysell;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.adapters.MyServicesAdapter;
import com.dreamguys.truelysell.datamodel.DAOUpdateMyServiceStatus;
import com.dreamguys.truelysell.datamodel.EmptyData;
import com.dreamguys.truelysell.datamodel.Phase3.DAOMyServices;
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
import retrofit2.Call;

public class MyProviderServicesActivity extends BaseActivity implements RetrofitHandler.RetrofitResHandler {


    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.rv_myservices)
    RecyclerView rvMyservices;
    @BindView(R.id.tv_no_records_found)
    TextView tvNoRecordsFound;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    TextView tvStatusAll, tvStatusInProgress, tvStatusCompleted;
    String type = "0";
    @BindView(R.id.iv_popular_services)
    ImageView ivPopularServices;
    @BindView(R.id.txt_my_providers)
    TextView txtMyProviders;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_services);
        ButterKnife.bind(this);
        setToolBarTitle(AppUtils.cleanLangStr(this,
                homeStringsList.getLblMyServices().getName(), R.string.txt_my_providers));
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);

        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivFilter.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
                ivPopularServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(this)));
            }
        }
        txtMyProviders.setText(AppUtils.cleanLangStr(this,
                homeStringsList.getLblMyServices().getName(), R.string.txt_my_providers));
        tvNoRecordsFound.setText(AppUtils.cleanLangStr(this,
                homeStringsList.getLblNoService().getName(), R.string.no_services_available));
        getMyServicesList(type);
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {

        switch (responseType) {
            case AppConstants.MYSERVICES:
                DAOMyServices daoMyServices = (DAOMyServices) myRes;
                if (daoMyServices.getData() != null && daoMyServices.getData().size() > 0) {
                    rvMyservices.setVisibility(View.VISIBLE);
                    tvNoRecordsFound.setVisibility(View.GONE);
                    linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    rvMyservices.setLayoutManager(linearLayoutManager);
                    rvMyservices.setAdapter(new MyServicesAdapter(this, daoMyServices.getData(),
                            MyProviderServicesActivity.this, bookingServiceScreenList));
                } else {
                    rvMyservices.setVisibility(View.GONE);
                    tvNoRecordsFound.setVisibility(View.VISIBLE);
                }
                break;
            case AppConstants.DELETESERVICE:
                EmptyData emptyData = (EmptyData) myRes;
                Toast.makeText(this, emptyData.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                getMyServicesList(type);
                break;
            case AppConstants.UPDATEMYSERVICESTATUS:
                DAOUpdateMyServiceStatus emptyData1 = (DAOUpdateMyServiceStatus) myRes;
                Toast.makeText(this, emptyData1.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                getMyServicesList(type);
                break;
        }


    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.UPDATEMYSERVICESTATUS:
                DAOUpdateMyServiceStatus emptyData1 = (DAOUpdateMyServiceStatus) myRes;
                Toast.makeText(this, emptyData1.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    public void getMyServicesList(String type) {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<DAOMyServices> classificationCall = apiService.getMyServices(PreferenceStorage.getKey(AppConstants.USER_TOKEN), type);
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.MYSERVICES, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(this, getString(R.string.txt_enable_internet));
        }
    }

    public void updateMyServiceStatus(String status, String serviceID) {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<DAOUpdateMyServiceStatus> classificationCall = apiService.postUpdateMyserviceStatus(PreferenceStorage.getKey(AppConstants.USER_TOKEN), status, serviceID);
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.UPDATEMYSERVICESTATUS, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(this, getString(R.string.txt_enable_internet));
        }
    }

    public void postDeleteService(String serviceID) {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<EmptyData> classificationCall = apiService.postDeleteService(PreferenceStorage.getKey(AppConstants.USER_TOKEN), serviceID);
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.DELETESERVICE, this, false);
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

    public void showUserLoginDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_change_service_statustype, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);
        TextView tvStatusType = view.findViewById(R.id.tv_statustype);
        tvStatusType.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));

        tvStatusAll = view.findViewById(R.id.tv_status_all);
        tvStatusInProgress = view.findViewById(R.id.tv_status_inprogress);
        tvStatusCompleted = view.findViewById(R.id.tv_status_completed);

        try {
            tvStatusType.setText(AppUtils.cleanLangStr(this,
                    homeStringsList.getLblFilterStatusType().getName(), R.string.txt_change_status_type));
            tvStatusAll.setText(AppUtils.cleanLangStr(this,
                    homeStringsList.getLblFilterAll().getName(), R.string.txt_all));
            tvStatusInProgress.setText(AppUtils.cleanLangStr(this,
                    homeStringsList.getLblFilterActive().getName(), R.string.txt_active));
            tvStatusCompleted.setText(AppUtils.cleanLangStr(this,
                    homeStringsList.getLblFilterInactive().getName(), R.string.txt_inactive));
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
                type = "0";
                getMyServicesList(type);
                dialog.dismiss();
            }
        });
        tvStatusInProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "1";
                getMyServicesList(type);
                dialog.dismiss();
            }
        });
        tvStatusCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "2";
                getMyServicesList(type);
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.iv_filter)
    public void onViewClicked() {
        showUserLoginDialog();
    }

    public void showAlertDialog(String status, String serviceID, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg) //TODO:
                .setCancelable(false)
                .setPositiveButton(AppUtils.cleanLangStr(this, commonStringList.getBtnYes().getName(), R.string.txt_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { //TODO:
                        updateMyServiceStatus(status, serviceID);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(AppUtils.cleanLangStr(this, commonStringList.getBtnNo().getName(), R.string.txt_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { //TODO:
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
