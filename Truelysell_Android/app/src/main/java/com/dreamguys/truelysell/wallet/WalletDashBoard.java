package com.dreamguys.truelysell.wallet;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.BaseActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.adapters.WalletTransactionHistoryAdapter;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOTransactionHistory;
import com.dreamguys.truelysell.datamodel.Phase3.DAOWallet;
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
import retrofit2.Call;

public class WalletDashBoard extends AppCompatActivity implements View.OnClickListener, RetrofitHandler.RetrofitResHandler {

    TextView textWalletAmount;
    ImageButton actionAddWallet;
    Button btnWithdraw;
    TextView labelTransactionHistory;
    TextView actionViewAll, tvNodata;
    RecyclerView rcvTransactionHistory;
    String walletAmount = "", currency = "", currencycode = "";
    TextView totalWalletTitle, transactionHistoryTitle;
    public LanguageResponse.Data.Language.WalletScreen walletScreenList;
    @BindView(R.id.tb_wallet)
    Toolbar toolbar;

    public static final int RESULT_BACK_ADD_WALLET = 1000;

    WalletTransactionHistoryAdapter walletTransactionHistoryAdapter;
    @BindView(R.id.fl_wallet)
    FrameLayout flWallet;
    float radius = 30.0f;
    String accountSaved = "0";
    @BindView(R.id.tv_gigs_title)
    TextView tvGigsTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_dashboard);
        ButterKnife.bind(this);
        String walletStr = PreferenceStorage.getKey(CommonLangModel.WalletScreen);
        walletScreenList = new Gson().fromJson(walletStr, LanguageResponse.Data.Language.WalletScreen.class);

        tvGigsTitle.setText(AppUtils.cleanLangStr(this,
                walletScreenList.getWalletTitle().getName(), R.string.title_wallet));

        setSupportActionBar(toolbar);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[]{AppUtils.getPrimaryAppTheme(this), AppUtils.getSecondaryAppTheme(this)});
        gd.setCornerRadii(new float[]{0, 0, 0, 0, radius, radius, radius, radius});
        toolbar.setBackgroundDrawable(gd);
        flWallet.setBackground(gd);

        rcvTransactionHistory = findViewById(R.id.rcv_transaction_history);
        actionAddWallet = findViewById(R.id.action_add_wallet);
        actionViewAll = findViewById(R.id.action_view_all);
        textWalletAmount = findViewById(R.id.text_wallet_amount);
        btnWithdraw = findViewById(R.id.btn_withdraw_wallet);
        tvNodata = findViewById(R.id.tv_no_data);
        totalWalletTitle = findViewById(R.id.text_total_wallet_balance);
        transactionHistoryTitle = findViewById(R.id.label_transaction_history);

        if (PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("2")) {
            btnWithdraw.setVisibility(View.GONE);
        } else {
            actionAddWallet.setVisibility(View.GONE);
        }

        actionAddWallet.setOnClickListener(this);
        actionViewAll.setOnClickListener(this);
        btnWithdraw.setOnClickListener(this);

        rcvTransactionHistory.setLayoutManager(new LinearLayoutManager(this));
        rcvTransactionHistory.setHasFixedSize(true);


//        ivSearch.setVisibility(View.GONE);
//        ivUserlogin.setVisibility(View.GONE);

        setLocale();
    }

    private void setLocale() {
        try {
            totalWalletTitle.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblTotalWalletBalance().getName(), R.string.text_total_wallet_balance));
            btnWithdraw.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblWithdrawFund().getName(), R.string.txt_withdraw_fund));
            transactionHistoryTitle.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblTransactionHistory().getName(), R.string.text_transaction_history));
            actionViewAll.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblViewAll().getName(), R.string.text_view_all));
            tvNodata.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblNoTransFound().getName(), R.string.no_transactions_available));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_add_wallet:
                Intent intentAddWallet = new Intent(this, CardListActivity.class);
                intentAddWallet.putExtra(AppConstants.WALLETAMOUNT, walletAmount);
                intentAddWallet.putExtra(AppConstants.CURRENCY, currency);
                intentAddWallet.putExtra(AppConstants.CURRENCYCODE, currencycode);
                intentAddWallet.putExtra(AppConstants.FROMPAGE, AppConstants.TOPUP);
//                startActivityForResult(intentAddWallet, RESULT_BACK_ADD_WALLET);
                startActivity(intentAddWallet);
                break;
            case R.id.action_view_all:
                Intent intentTransactionHistory = new Intent(this, TransactionHistoryActivity.class);
                startActivity(intentTransactionHistory);
                break;
            case R.id.btn_withdraw_wallet:
                Intent intentWithdrawWallet = new Intent(this, CardListActivity.class);
                intentWithdrawWallet.putExtra(AppConstants.WALLETAMOUNT, walletAmount);
                intentWithdrawWallet.putExtra(AppConstants.CURRENCY, currency);
                intentWithdrawWallet.putExtra(AppConstants.CURRENCYCODE, currencycode);
                intentWithdrawWallet.putExtra(AppConstants.FROMPAGE, AppConstants.WITHDRAW);
                intentWithdrawWallet.putExtra(AppConstants.ACCOUNTSETTINGS, accountSaved);
//                startActivityForResult(intentAddWallet, RESULT_BACK_ADD_WALLET);
                startActivity(intentWithdrawWallet);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_BACK_ADD_WALLET && resultCode == RESULT_OK) {
            String cashAmount = data.getStringExtra(AppConstants.CASH_AMOUNT);
            Toast.makeText(this, cashAmount, Toast.LENGTH_SHORT).show();
        }
    }

    public void getWalletDetails() {
        if (AppUtils.isNetworkAvailable(this)) {

            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            Call<DAOWallet> classificationCall = apiService.postWalletDetails(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
            RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.WALLETDETAILS, this, false);

        }
    }

    public void getWalletTransactionDetails() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService = ApiClient.getClientNoHeader().create(ApiInterface.class);
            Call<DAOTransactionHistory> classificationCall = apiService.postTransactionList(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
            RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.TRANSACTIONLIST, this, false);
        }
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.WALLETDETAILS:
                DAOWallet daoWallet = (DAOWallet) myRes;
                walletAmount = daoWallet.getData().getWalletInfo().getWalletAmt();
                currency = daoWallet.getData().getWalletInfo().getCurrency();
                currencycode = daoWallet.getData().getWalletInfo().getCurrencycode();
                textWalletAmount.setText(Html.fromHtml(daoWallet.getData().getWalletInfo().getCurrency()) + daoWallet.getData().getWalletInfo().getWalletAmt());
                getWalletTransactionDetails();
                break;
            case AppConstants.TRANSACTIONLIST:
                DAOTransactionHistory daoTransactionHistory = (DAOTransactionHistory) myRes;
                accountSaved = daoTransactionHistory.getData().getStripeBank();
                walletAmount = daoTransactionHistory.getData().getWalletInfo().getWallet().getWalletAmt();
                currency = daoTransactionHistory.getData().getWalletInfo().getWallet().getCurrency();
                currencycode = daoTransactionHistory.getData().getWalletInfo().getWallet().getCurrencyCode();
                textWalletAmount.setText(Html.fromHtml(daoTransactionHistory.getData().getWalletInfo().getWallet().getCurrency()) +
                        daoTransactionHistory.getData().getWalletInfo().getWallet().getWalletAmt());
                if (daoTransactionHistory.getData().getWalletInfo().getWalletHistory() != null) {
                    if (daoTransactionHistory.getData().getWalletInfo().getWalletHistory().size() > 0) {
                        rcvTransactionHistory.setVisibility(View.VISIBLE);
                        tvNodata.setVisibility(View.GONE);
                        walletTransactionHistoryAdapter = new WalletTransactionHistoryAdapter(this,
                                daoTransactionHistory.getData().getWalletInfo().getWalletHistory(), walletScreenList);
                        walletTransactionHistoryAdapter.bindData();
                        rcvTransactionHistory.setAdapter(walletTransactionHistoryAdapter);
                    } else {
                        rcvTransactionHistory.setVisibility(View.GONE);
                        tvNodata.setVisibility(View.VISIBLE);
                    }
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
    protected void onResume() {
        super.onResume();
        getWalletTransactionDetails();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
