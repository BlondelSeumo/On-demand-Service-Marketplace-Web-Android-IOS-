package com.dreamguys.truelysell;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.adapters.CheckAvailabilityAdapter;
import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.dreamguys.truelysell.datamodel.Phase3.AddAvailabilityModel;
import com.dreamguys.truelysell.datamodel.Phase3.ResponseData;
import com.dreamguys.truelysell.datamodel.ProvAvailData;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

public class ChangeAvailabilityActivity extends BaseActivity implements RetrofitHandler.RetrofitResHandler {

    @BindView(R.id.tv_txt_availability)
    TextView tvTxtAvailability;
    @BindView(R.id.tv_txt_from)
    TextView tvTxtFrom;
    @BindView(R.id.tv_txt_to)
    TextView tvTxtTo;
    @BindView(R.id.rv_avail_parent)
    RecyclerView rvAvailParent;
    @BindView(R.id.btn_provider_submit)
    Button btnProviderSubmit;

    Unbinder unbinder;
    CheckAvailabilityAdapter mAdapter;

    List<Integer> dayIndex = new ArrayList<>();
    ArrayList<ProvAvailData> provAvailData = new ArrayList<>();
    private String response;
    String[] availList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_provider_avail);
        unbinder = ButterKnife.bind(this);
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);
        setToolBarTitle(AppUtils.cleanLangStr(this, availabilityScreenList.getLblProviderBusinessHrs().getName(),
                R.string.txt_business_hours));

        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnProviderSubmit.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
            }
        }

        setLocale();

        for (int i = 0; i < availList.length; i++) {
            ProvAvailData pvd = new ProvAvailData();
            pvd.setDayText(availList[i]);
            provAvailData.add(pvd);
        }

        getAvailability();
    }

    private void setLocale() {
        try {
            availList = new String[]{AppUtils.cleanLangStr(this, availabilityScreenList.getLblAllDays().getName(), R.string.txt_all_day),
                    AppUtils.cleanLangStr(this, availabilityScreenList.getLblMonday().getName(), R.string.txt_monday),
                    AppUtils.cleanLangStr(this, availabilityScreenList.getLblTuesday().getName(), R.string.txt_tuesday),
                    AppUtils.cleanLangStr(this, availabilityScreenList.getLblWednesday().getName(), R.string.txt_wednesday),
                    AppUtils.cleanLangStr(this, availabilityScreenList.getLblThursday().getName(), R.string.txt_thursday),
                    AppUtils.cleanLangStr(this, availabilityScreenList.getLblFriday().getName(), R.string.txt_friday),
                    AppUtils.cleanLangStr(this, availabilityScreenList.getLblSaturday().getName(), R.string.txt_saturday),
                    AppUtils.cleanLangStr(this, availabilityScreenList.getLblSunday().getName(), R.string.txt_sunday)};

            tvTxtFrom.setText(AppUtils.cleanLangStr(this, availabilityScreenList.getLbl_from().getName(), R.string.txt_from));
            tvTxtTo.setText(AppUtils.cleanLangStr(this, availabilityScreenList.getLbl_to().getName(), R.string.txt_to));
            btnProviderSubmit.setText(AppUtils.cleanLangStr(this, availabilityScreenList.getBtn_submit().getName(), R.string.txt_submit));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAvailability() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.clearDialog();
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<AddAvailabilityModel> addAvailabilityCall = apiService.getAvailability(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
                RetrofitHandler.executeRetrofit(this, addAvailabilityCall, AppConstants.GET_AVAILABILITY,
                        this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(getApplicationContext(), AppUtils.cleanLangStr(this,
                    getString(R.string.txt_enable_internet), R.string.txt_enable_internet));
        }
    }

    private void parseJSON(String response) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ResponseData>>() {
        }.getType();
        ArrayList<ProvAvailData> provAvailDataList = new ArrayList<>();
        List<ResponseData> contactList = gson.fromJson(response, type);

        if (contactList != null) {
            for (int i = 0; i < provAvailData.size(); i++) {
                boolean found = false;
                ProvAvailData provAvailDataItems = new ProvAvailData();
                for (ResponseData person1 : contactList) {
                    if (i == Integer.parseInt(person1.getDay())) {
                        found = true;
                        provAvailDataItems.setChecked(true);
                        provAvailDataItems.setEnabled(true);
                        provAvailDataItems.setDayText(availList[i]);
                        provAvailDataItems.setFromTime(person1.getFromTime());
                        provAvailDataItems.setToTime(person1.getToTime());
                    }
                }
                if (found) {
                    provAvailDataList.add(provAvailDataItems);
                } else {
                    ProvAvailData pvd = new ProvAvailData();
                    pvd.setDayText(availList[i]);
                    provAvailDataList.add(pvd);
                }
            }
        }
        mAdapter = new CheckAvailabilityAdapter(ChangeAvailabilityActivity.this, provAvailDataList, 0);
        rvAvailParent.setAdapter(mAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_provider_submit)
    public void onViewClicked() {
        int count = rvAvailParent.getAdapter().getItemCount();
        ArrayList<JSONObject> provAvail = new ArrayList<>();
        dayIndex = new ArrayList<>();
        try {
            for (int i = 0; i < count; i++) {
                RecyclerView.ViewHolder vh = rvAvailParent.findViewHolderForAdapterPosition(i);
                Switch switchAvail = vh.itemView.findViewById(R.id.switch_select);
                TextView tvFrom = vh.itemView.findViewById(R.id.tv_from_time);
                TextView tvTo = vh.itemView.findViewById(R.id.tv_to_time);
                if (switchAvail.isEnabled() && switchAvail.isChecked()) {
                    switch (i) {
                        case 0:
                            for (int j = 1; j <= 7; j++) {
                                try {
                                    JSONObject availData = new JSONObject();
                                    availData.put("day", String.valueOf(j));
                                    availData.put("from_time", tvFrom.getText().toString());
                                    availData.put("to_time", tvTo.getText().toString());
                                    provAvail.add(availData);
                                    dayIndex.add(j);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                            try {
                                JSONObject availData = new JSONObject();
                                availData.put("day", String.valueOf(i));
                                availData.put("from_time", tvFrom.getText().toString());
                                availData.put("to_time", tvTo.getText().toString());
                                provAvail.add(availData);
                                dayIndex.add(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (provAvail.size() <= 0) {
            AppUtils.showToast(ChangeAvailabilityActivity.this, AppUtils.cleanLangStr(this,
                    availabilityScreenList.getLblAnyOne().getName(), R.string.txt_any_one_option));
            return;
        }
        JSONArray availFinalData = new JSONArray(provAvail);
        Log.e("JSONArray", availFinalData.toString());

        callSubmit(availFinalData.toString());

    }

    private void callSubmit(String availFinalData) {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.clearDialog();
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<BaseResponse> addAvailabilityCall = apiService.addAvailability(PreferenceStorage.getKey(AppConstants.USER_TOKEN), availFinalData);
                RetrofitHandler.executeRetrofit(this, addAvailabilityCall, AppConstants.ADD_AVAILABILITY,
                        this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(getApplicationContext(), AppUtils.cleanLangStr(this,
                    getString(R.string.txt_enable_internet), R.string.txt_enable_internet));
        }
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.GET_AVAILABILITY:
                AddAvailabilityModel res = (AddAvailabilityModel) myRes;
                if (res.getData().getAvailability().isEmpty()) {
                    if (rvAvailParent.getAdapter() == null) {
                        mAdapter = new CheckAvailabilityAdapter(this, provAvailData, 0);
                        rvAvailParent.setAdapter(mAdapter);
                    }
                } else {
                    parseJSON(res.getData().getAvailability());
                }


                break;
            case AppConstants.ADD_AVAILABILITY:
                BaseResponse baseResponse = (BaseResponse) myRes;
                Toast.makeText(ChangeAvailabilityActivity.this,
                        baseResponse.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }


}
