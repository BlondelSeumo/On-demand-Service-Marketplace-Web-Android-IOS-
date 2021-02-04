package com.dreamguys.truelysell;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StripeSettingsActivity extends BaseActivity {

    @BindView(R.id.et_acc_holder_name)
    EditText editAcc_Holder_name;
    @BindView(R.id.et_acc_num)
    EditText editAcc_num;
    @BindView(R.id.et_IBAN_num)
    EditText editIBAN;
    @BindView(R.id.et_bank_name)
    EditText editBankName;
    @BindView(R.id.et_bank_addr)
    EditText editBankAddress;
    @BindView(R.id.et_sort_code)
    EditText editSortCode;
    @BindView(R.id.et_routing_num)
    EditText editRoutingNum;
    @BindView(R.id.et_IFSC_code)
    EditText editIFSCCode;
    @BindView(R.id.bt_update)
    Button btUpdate;
    @BindView(R.id.ctv_acc_holder_name)
    TextView ctvAccHolderName;
    @BindView(R.id.ctv_acc_num)
    TextView ctvAccNum;
    @BindView(R.id.ctv_iban)
    TextView ctvIban;
    @BindView(R.id.ctv_bank_name)
    TextView ctvBankName;
    @BindView(R.id.ctv_bank_address)
    TextView ctvBankAddress;
    @BindView(R.id.ctv_sort_code)
    TextView ctvSortCode;
    @BindView(R.id.ctv_swift_code)
    TextView ctvSwiftCode;
    @BindView(R.id.ctv_ifsc_code)
    TextView ctvIfscCode;
    private HashMap<String, String> postStripeRegisterData = new HashMap<String, String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_settings);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);
        setToolBarTitle(AppUtils.cleanLangStr(this,
                accountSettingsScreenList.getLblTitleAccountSettings().getName(), R.string.txt_account_settings));
        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btUpdate.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
            }
        }

        if (PreferenceStorage.getKey(AppConstants.stripe_Acc_name) != null) {
            editAcc_Holder_name.setText(PreferenceStorage.getKey(AppConstants.stripe_Acc_name));
            editAcc_num.setText(PreferenceStorage.getKey(AppConstants.stripe_Acc_num));
            if (PreferenceStorage.getKey(AppConstants.stripe_Iban) != null && !PreferenceStorage.getKey(AppConstants.stripe_Iban).isEmpty()) {
                editIBAN.setText(PreferenceStorage.getKey(AppConstants.stripe_Iban));
            }
            editBankName.setText(PreferenceStorage.getKey(AppConstants.stripe_bank_name));
            editBankAddress.setText(PreferenceStorage.getKey(AppConstants.stripe_bank_address));
            editSortCode.setText(PreferenceStorage.getKey(AppConstants.stripe_sort_code));
            editRoutingNum.setText(PreferenceStorage.getKey(AppConstants.stripe_routing_number));
            editIFSCCode.setText(PreferenceStorage.getKey(AppConstants.stripe_account_ifsc));
        }
        setLanguageValues();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.bt_update)
    public void onViewClicked() {
        if (editAcc_Holder_name.getText().toString().isEmpty()) {
            //editAcc_Holder_name.setError(stripePaymentScreen.getTxt_fld_acc_name().getValidation1());
            AppUtils.showToast(this, AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblAccountHolderName().getValidation1(), R.string.err_acc_holder_name));

            editAcc_Holder_name.requestFocus();
        } else if (editAcc_num.getText().toString().isEmpty()) {
            AppUtils.showToast(this, AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblAccNumber().getValidation1(), R.string.err_acc_number));
            editAcc_num.requestFocus();
        } else if (editBankName.getText().toString().isEmpty()) {
            AppUtils.showToast(this, AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblBankName().getValidation1(), R.string.err_bank_name));
            editBankName.requestFocus();
        } else if (editBankAddress.getText().toString().isEmpty()) {
            AppUtils.showToast(this, AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblBanAddress().getValidation1(), R.string.err_bank_address));
            editBankAddress.requestFocus();
        } else if (editSortCode.getText().toString().isEmpty() && editRoutingNum.getText().toString().isEmpty() && editIFSCCode.getText().toString().isEmpty()) {
            AppUtils.showToast(this, AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblBtnUpdate().getValidation1(), R.string.err_code));
        } else {
            ProgressDlg.clearDialog();
            ProgressDlg.showProgressDialog(this, null, null);

            postStripeRegisterData.put("account_holder_name", editAcc_Holder_name.getText().toString());
            postStripeRegisterData.put("account_number", editAcc_num.getText().toString());
            postStripeRegisterData.put("account_iban", editIBAN.getText().toString());
            postStripeRegisterData.put("bank_name", editBankName.getText().toString());
            postStripeRegisterData.put("bank_address", editBankAddress.getText().toString());
            postStripeRegisterData.put("sort_code", editSortCode.getText().toString());
            postStripeRegisterData.put("routing_number", editRoutingNum.getText().toString());
            postStripeRegisterData.put("account_ifsc", editIFSCCode.getText().toString());
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            String token = AppConstants.DEFAULTTOKEN;
            if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
            }
            apiInterface.postRegisterStripeDetails(postStripeRegisterData,
                    token).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    ProgressDlg.dismissProgressDialog();
                    if (response.isSuccessful() &&
                            response.body().getResponseHeader().getResponseCode().equalsIgnoreCase("200")) {
                        ProgressDlg.dismissProgressDialog();
                        if (response.body().getResponseHeader().getResponseMessage() != null)
                            Toast.makeText(StripeSettingsActivity.this, response.body().getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                        PreferenceStorage.setKey(AppConstants.stripe_Acc_name, editAcc_Holder_name.getText().toString());
                        PreferenceStorage.setKey(AppConstants.stripe_Acc_num, editAcc_num.getText().toString());
                        PreferenceStorage.setKey(AppConstants.stripe_Iban, editIBAN.getText().toString());
                        PreferenceStorage.setKey(AppConstants.stripe_bank_name, editBankName.getText().toString());
                        PreferenceStorage.setKey(AppConstants.stripe_bank_address, editBankAddress.getText().toString());
                        PreferenceStorage.setKey(AppConstants.stripe_sort_code, editSortCode.getText().toString());
                        PreferenceStorage.setKey(AppConstants.stripe_routing_number, editRoutingNum.getText().toString());
                        PreferenceStorage.setKey(AppConstants.stripe_account_ifsc, editIFSCCode.getText().toString());

                        if (getIntent() != null && getIntent().getExtras().getString("fromPage")
                                .equalsIgnoreCase("withdraw")) {
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                        }

                        finish();
                        //}
                    /*else if (response.body().getCode().equals(AppConstants.INVALID_RESPONSE_CODE)) {
                        mCustomProgressDialog.dismiss();
                        NetworkAlertDialog.networkAlertDialog(StripeSettings.this, "", response.body().getMessage(), null, null);
                    } else {
                        mCustomProgressDialog.dismiss();
                        Toast.makeText(StripeSettings.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }*/
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    ProgressDlg.dismissProgressDialog();
                }
            });
                /*@Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                }

                @Override
                public void onFailure(Call<POSTPaymentSuccess> call, Throwable t) {
                    mCustomProgressDialog.dismiss();
                }*/

        }

    }

    public void setLanguageValues() {

        try {
            ctvAccHolderName.setText(AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblAccountHolderName().getName(), R.string.acc_name));
            ctvAccNum.setText(AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblAccNumber().getName(), R.string.acc_name));
            ctvBankAddress.setText(AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblBanAddress().getName(), R.string.bank_addr));
            ctvBankName.setText(AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblBankName().getName(), R.string.bank_name));
            ctvIban.setText(AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblIban().getName(), R.string.IBAN));
            ctvIfscCode.setText(AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblIfscCode().getName(), R.string.IFSC_code));
            ctvSortCode.setText(AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblSortCode().getName(), R.string.sort_code));
            ctvSwiftCode.setText(AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblSwiftCode().getName(), R.string.routing_num));
            btUpdate.setText(AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblBtnUpdate().getName(), R.string.text_update));

            editSortCode.setHint(AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblSortCode().getPlaceholder(), R.string.uk_bank_code));
            editRoutingNum.setHint(AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblSwiftCode().getPlaceholder(), R.string.hint_routing_num));
            editIFSCCode.setHint(AppUtils.cleanLangStr(this,
                    accountSettingsScreenList.getLblIfscCode().getPlaceholder(), R.string.hint_ifsc_code));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}