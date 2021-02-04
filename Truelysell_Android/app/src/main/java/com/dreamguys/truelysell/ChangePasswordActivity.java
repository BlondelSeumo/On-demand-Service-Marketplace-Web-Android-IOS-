package com.dreamguys.truelysell;

import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
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
import retrofit2.Call;


public class ChangePasswordActivity extends AppCompatActivity implements RetrofitHandler.RetrofitResHandler {

    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_titlename)
    TextView tvTitlename;
    @BindView(R.id.et_currentPwd)
    EditText etCurrentPwd;
    @BindView(R.id.et_newPwd)
    EditText etNewPwd;
    @BindView(R.id.et_confirmPwd)
    EditText etConfirmPwd;
    public LanguageResponse.Data.Language.ChangePassword changePasswordData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        getLocaleData();

    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseModel) {
        BaseResponse emptyData = (BaseResponse) myRes;
        Toast.makeText(this, emptyData.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
        if (emptyData.getResponseHeader().getResponseCode().equalsIgnoreCase("200")) {
            ChangePasswordActivity.this.finish();
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseModel) {

    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseModel) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.btn_submit, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submit:
                checkValidation();
                break;
        }
    }

    private void checkValidation() {
        if (etCurrentPwd.getText().toString().isEmpty()) {
            AppUtils.showToast(ChangePasswordActivity.this, AppUtils.cleanLangStr(this,
                    changePasswordData.getTxtCurrentPassword().getValidation1(), R.string.err_current_password));
        } else if (etNewPwd.getText().toString().isEmpty()) {
            AppUtils.showToast(ChangePasswordActivity.this, AppUtils.cleanLangStr(this,
                    changePasswordData.getTxtNewPassword().getValidation1(), R.string.err_new_password));
        } else if (etCurrentPwd.getText().toString().equals(etNewPwd.getText().toString())) {
            AppUtils.showToast(ChangePasswordActivity.this, AppUtils.cleanLangStr(this,
                    changePasswordData.getTxtCurrentPassword().getValidation2(), R.string.err_current_new_password));
        } else if (etConfirmPwd.getText().toString().isEmpty()) {
            AppUtils.showToast(ChangePasswordActivity.this, AppUtils.cleanLangStr(this,
                    changePasswordData.getTxtConfirmPassword().getValidation1(), R.string.err_confirm_password));
        } else if (!etNewPwd.getText().toString().equals(etConfirmPwd.getText().toString())) {
            AppUtils.showToast(ChangePasswordActivity.this, AppUtils.cleanLangStr(this,
                    changePasswordData.getTxtNewPassword().getValidation2(), R.string.err_new_confirm_password));
        } else {
            updatePassword();
        }
    }

    private void updatePassword() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.clearDialog();
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                String token = AppConstants.DEFAULTTOKEN;
                if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                    token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
                }
                String type = "";
                if (PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("1")) {
                    type = "provider";
                } else {
                    type = "user";
                }

                Call<BaseResponse> classificationCall =
                        apiService.changePassword(token, PreferenceStorage.getKey(AppConstants.USER_ID),
                                type,
                                etCurrentPwd.getText().toString().trim(),
                                etConfirmPwd.getText().toString().trim()
                        );
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.CHANGEPASSWORD, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(ChangePasswordActivity.this, getString(R.string.txt_enable_internet));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getLocaleData() {
        try {
            String changePwdDataStr = PreferenceStorage.getKey(CommonLangModel.ChangePasswordScreen);
            changePasswordData = new Gson().fromJson(changePwdDataStr,
                    LanguageResponse.Data.Language.ChangePassword.class);
            updateText();
        } catch (Exception e) {
            changePasswordData = new LanguageResponse().new Data().new Language().new ChangePassword();
        }
    }

    private void updateText() {
        etCurrentPwd.setHint(AppUtils.cleanLangStr(this, changePasswordData.getTxtCurrentPassword().getName(), R.string.txt_current_password));
        etNewPwd.setHint(AppUtils.cleanLangStr(this, changePasswordData.getTxtNewPassword().getName(), R.string.txt_new_password));
        etConfirmPwd.setHint(AppUtils.cleanLangStr(this, changePasswordData.getTxtConfirmPassword().getName(), R.string.txt_confirm_password));
        tvTitlename.setText(AppUtils.cleanLangStr(this, changePasswordData.getLblChangePassword().getName(), R.string.txt_change_pwd));
        btnSubmit.setText(AppUtils.cleanLangStr(this, changePasswordData.getBtnSubmit().getName(), R.string.txt_submit));
        btnSubmit.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(ChangePasswordActivity.this)));
    }
}
