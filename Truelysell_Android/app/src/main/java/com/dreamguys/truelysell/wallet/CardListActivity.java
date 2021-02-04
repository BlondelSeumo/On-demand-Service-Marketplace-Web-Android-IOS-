package com.dreamguys.truelysell.wallet;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.dreamguys.truelysell.BaseActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.StripeSettingsActivity;
import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.PaypalResponseToken;
import com.dreamguys.truelysell.datamodel.StripeDetailsModel;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;
import com.stripe.model.Order;

import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

import static com.dreamguys.truelysell.utils.AppConstants.isFromWhichCard;

public class CardListActivity extends AppCompatActivity implements View.OnClickListener, RetrofitHandler.RetrofitResHandler,
        PaymentMethodNonceCreatedListener, PaymentResultListener {

    EditText editTextCVV, editTextCardNumber, editTextMM;
    EditText editTextAmount;
    TextView textViewCurrentBalance, action1000, action2000, action3000;
    Button actionAddCashSecurely;
    String publicKey = "", secretKey = "", str_expiry_date = "", braintreeKey = "", razorPayAPIKey = "";
    int previousLength;
    private static final String EXP_DATE_REGAX = "(0[1-9]|1[0-2])[0-9]{2}";
    //    CustomProgressDialog customProgressDialog;
    ProgressDlg progressDlg;
    RadioGroup radioGroupAddNewCards;
    RadioButton radioButtonDebitCreditCard;
    RadioButton paypal;
    RadioButton razorPay;
    LinearLayout linearLayoutAddNewCardPanel;
    private static final char space = ' ';
    String walletAmount = "", currency = "", cashAmount = "", currencycode = "", fromPage = "", accountSaved = "";
    RecyclerView rvSavedCards;
    @BindView(R.id.title_wallet)
    TextView titleWallet;
    @BindView(R.id.tv_gigs_title)
    TextView tvGigsTitle;
    @BindView(R.id.text_current_balance)
    TextView textCurrentBalance;
    @BindView(R.id.txt_minimum_amount_of_50)
    TextView txtMinimumAmountOf50;
    @BindView(R.id.label_add_card)
    TextView labelAddCard;
    @BindView(R.id.text_card_expiry)
    TextView textCardExpiry;
    @BindView(R.id.text_cvv)
    TextView textCvv;
    @BindView(R.id.txt_card_secure)
    TextView txtCardSecure;
    @BindView(R.id.tb_wallet)
    Toolbar toolbar;

    TextView lblCurrentBal;
    TextView txtAddCash;
    private static final int REQUEST_CODE = 123;
    private static final int SAVE_ACCOUNT_REQUEST_CODE = 124;
    BraintreeFragment mBraintreeFragment = null;
    String paymentType = "";
    public LanguageResponse.Data.Language.WalletScreen walletScreenList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        ButterKnife.bind(this);
        String walletStr = PreferenceStorage.getKey(CommonLangModel.WalletScreen);
        walletScreenList = new Gson().fromJson(walletStr, LanguageResponse.Data.Language.WalletScreen.class);
//        ivSearch.setVisibility(View.GONE);
//        ivUserlogin.setVisibility(View.GONE);
        actionAddCashSecurely = findViewById(R.id.action_add_cash_securely);
        textViewCurrentBalance = findViewById(R.id.text_current_balance);
        editTextMM = findViewById(R.id.edit_mm_yy);
        editTextCVV = findViewById(R.id.cet_cvv);
        editTextCardNumber = findViewById(R.id.cet_enter_card_number);
        radioGroupAddNewCards = findViewById(R.id.rg_card_details);
        radioButtonDebitCreditCard = findViewById(R.id.rb_debit_credit_cards);
        paypal = findViewById(R.id.paypal);
        razorPay = findViewById(R.id.razorPay);
        linearLayoutAddNewCardPanel = findViewById(R.id.ll_card_panel);
        editTextAmount = findViewById(R.id.edit_amount);
        action1000 = findViewById(R.id.action_1000);
        action2000 = findViewById(R.id.action_2000);
        action3000 = findViewById(R.id.action_3000);
        rvSavedCards = findViewById(R.id.rcv_saved_cards);
        lblCurrentBal = findViewById(R.id.label_current_balance);
        txtAddCash = findViewById(R.id.txt_add_cash);
        action1000.setOnClickListener(this);
        action2000.setOnClickListener(this);
        action3000.setOnClickListener(this);
        actionAddCashSecurely.setOnClickListener(this);

        actionAddCashSecurely.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
        toolbar.setBackgroundColor(AppUtils.getPrimaryAppTheme(this));
        getStripeDetails();

//        rvSavedCards.setNestedScrollingEnabled(false);
//        rvSavedCards.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        rvSavedCards.setAdapter(new SavedCardsAdapter());
//        rvSavedCards.setHasFixedSize(true);

        walletAmount = getIntent().getStringExtra(AppConstants.WALLETAMOUNT);
        currency = getIntent().getStringExtra(AppConstants.CURRENCY);
        cashAmount = getIntent().getStringExtra(AppConstants.CASH_AMOUNT);
        currencycode = getIntent().getStringExtra(AppConstants.CURRENCYCODE);
        fromPage = getIntent().getStringExtra(AppConstants.FROMPAGE);
        accountSaved = getIntent().getStringExtra(AppConstants.ACCOUNTSETTINGS);
        textViewCurrentBalance.setText(Html.fromHtml(currency) + walletAmount);
        setLocale();
        if (fromPage.equalsIgnoreCase(AppConstants.WITHDRAW)) {
            tvGigsTitle.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblWithdrawWallet().getName(), R.string.txt_withdraw_wallet));
//            setToolBarTitle(AppUtils.cleanLangStr(this,
//                    walletScreenList.getLblWithdrawWallet().getName(), R.string.txt_withdraw_wallet));
            linearLayoutAddNewCardPanel.setVisibility(View.GONE);
            radioGroupAddNewCards.setVisibility(View.GONE);
            labelAddCard.setVisibility(View.GONE);
            actionAddCashSecurely.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblWithdrawFund().getName(), R.string.txt_withdraw_fund));
        } else {
            tvGigsTitle.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblWithdrawWallet().getName(), R.string.txt_withdraw_wallet));
//            setToolBarTitle(AppUtils.cleanLangStr(this,
//                    walletScreenList.getLblTopupWallet().getName(), R.string.txt_topup_wallet));
//            linearLayoutAddNewCardPanel.setVisibility(View.VISIBLE);
            actionAddCashSecurely.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getBtnAddCash().getName(), R.string.text_add_cash_securely));
        }
        setSupportActionBar(toolbar);

//        if (SessionHandler.getInstance().get(this, AppConstants.TOKEN_ID) != null) {
//            if (NetworkChangeReceiver.isConnected()) {
//                customProgressDialog.showDialog();
//                final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//                apiInterface.getStripeConfiDetails(SessionHandler.getInstance().get(this, AppConstants.TOKEN_ID), SessionHandler.getInstance().get(this, AppConstants.Language))
//                        .enqueue(new Callback<GETStripeConfig>() {
//                            @Override
//                            public void onResponse(Call<GETStripeConfig> call, Response<GETStripeConfig> response) {
//                                customProgressDialog.dismiss();
//                                if (response.body().getCode().equals(200)) {
//                                    if (response.body().getData().getStripePublicKey() != null && response.body().getData().getStripeSecretKey() != null) {
//                                        if (!response.body().getData().getStripeSecretKey().isEmpty() && !response.body().getData().getStripePublicKey().isEmpty()) {
//                                            publicKey = response.body().getData().getStripePublicKey();
//                                            secretKey = response.body().getData().getStripeSecretKey();
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<GETStripeConfig> call, Throwable t) {
//                                customProgressDialog.dismiss();
//                            }
//                        });
//            } else {
//                Toast.makeText(this, getString(R.string.err_internet_connection), Toast.LENGTH_SHORT).show();
//            }
//        }

        actionAddCashSecurely.setOnClickListener(this);

        radioGroupAddNewCards.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                /*int checkId = radioGroupAddNewCards.getCheckedRadioButtonId();
                if (checkId == radioButtonDebitCreditCard.getId()) {
                    isFromWhichCard = 0;
                    linearLayoutAddNewCardPanel.setVisibility(View.VISIBLE);
                }*/

                if (checkedId == radioButtonDebitCreditCard.getId()) {
                    isFromWhichCard = 0;
                    paymentType = "1";
                    linearLayoutAddNewCardPanel.setVisibility(View.VISIBLE);
                } else if (checkedId == paypal.getId()) {
                    paymentType = "2";
                    linearLayoutAddNewCardPanel.setVisibility(View.GONE);
                } else if (checkedId == paypal.getId()) {
                    paymentType = "3";
                    linearLayoutAddNewCardPanel.setVisibility(View.GONE);
                }
            }
        });

        editTextCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start == 18) {
                    editTextMM.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                cardNumberPattern(editable);
            }
        });

        editTextMM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousLength = editTextMM.getText().toString().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = editTextMM.getText().toString().trim().length();

                if (previousLength <= length && length < 3) {
                    int month = Integer.parseInt(editTextMM.getText().toString());
                    if (length == 1 && month >= 2) {
                        String autoFixStr = "0" + month + "/";
                        editTextMM.setText(autoFixStr);
                        editTextMM.setSelection(3);
                    } else if (length == 2 && month <= 12) {
                        String autoFixStr = editTextMM.getText().toString() + "/";
                        editTextMM.setText(autoFixStr);
                        editTextMM.setSelection(3);
                    } else if (length == 2 && month > 12) {
                        editTextMM.setText("1");
                        editTextMM.setSelection(1);
                    }
                } else if (length == 5 && before == 0) {
                    String enteredYear = s.toString().substring(3);
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100; //getting last 2 digits of current year i.e. 2018 % 100 = 18
                    if (Integer.parseInt(enteredYear) < currentYear) {
                        Toast.makeText(CardListActivity.this, "Invalid Expiry Date", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    editTextCVV.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setLocale() {
        try {
            lblCurrentBal.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblCurrentBal().getName(), R.string.txt_current_balance));
            if (fromPage.equalsIgnoreCase(AppConstants.WITHDRAW)) {
                txtAddCash.setText(AppUtils.cleanLangStr(this,
                        walletScreenList.getLblEnterCash().getName(), R.string.txt_enter_withdraw_amount));
            } else {
                txtAddCash.setText(AppUtils.cleanLangStr(this,
                        walletScreenList.getLblAddCash().getName(), R.string.txt_add_cash));
            }

            labelAddCard.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblAddCard().getName(), R.string.text_add_a_card));
            radioButtonDebitCreditCard.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblDebitCreditCard().getName(), R.string.text_debit_card_credit_card));
            editTextCardNumber.setHint(AppUtils.cleanLangStr(this,
                    walletScreenList.getTxtFldCardNum().getPlaceholder(), R.string.txt_card_number));
            textCardExpiry.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblCardExpiry().getPlaceholder(), R.string.text_card_expiry));
            textCardExpiry.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblCardExpiry().getName(), R.string.text_card_expiry));
            editTextMM.setHint(AppUtils.cleanLangStr(this,
                    walletScreenList.getTxtFldExpMnth().getPlaceholder(), R.string.text_mm_yy));
            textCvv.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblCvv().getName(), R.string.text_cvv));
            txtCardSecure.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getLblPrivacyMsg().getName(), R.string.txt_card_secure));
           /* actionAddCashSecurely.setText(AppUtils.cleanLangStr(this,
                    walletScreenList.getBtnAddCash().getName(), R.string.text_add_cash_securely));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void cardNumberPattern(Editable s) {
        int pos = 0;
        while (true) {
            if (pos >= s.length()) break;
            if (space == s.charAt(pos) && (((pos + 1) % 5) != 0 || pos + 1 == s.length())) {
                s.delete(pos, pos + 1);
            } else {
                pos++;
            }
        }

        // Insert char where needed.
        pos = 4;
        while (true) {
            if (pos >= s.length()) break;
            final char c = s.charAt(pos);
            // Only if its a digit where there should be a space we insert a space
            if ("0123456789".indexOf(c) >= 0) {
                s.insert(pos, "" + space);
            }
            pos += 5;
        }
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.action_add_cash_securely) {
//            if (isFromWhichCard == -1) {
//                Toast.makeText(this, "Please select one card or add new card to proceed.", Toast.LENGTH_SHORT).show();
//                return;
//            } else if (isFromWhichCard == 0) {

//                if (SessionHandler.getInstance().get(this, AppConstants.TOKEN_ID) == null) {
//                    Toast.makeText(this, "Please login to proceed", Toast.LENGTH_SHORT).show();
//                    return;
//                }

        switch (v.getId()) {
            case R.id.action_1000:
                editTextAmount.setText("1000");
                break;
            case R.id.action_2000:
                editTextAmount.setText("2000");
                break;
            case R.id.action_3000:
                editTextAmount.setText("3000");
                break;
//            case R.id.action_add_cash:
//                if (!editTextAmount.getText().toString().isEmpty()) {
//                    Intent intentBacktoWalletDashBoard = new Intent(this, CardListActivity.class);
//                    intentBacktoWalletDashBoard.putExtra(AppConstants.CASH_AMOUNT, editTextAmount.getText().toString());
//                    intentBacktoWalletDashBoard.putExtra(AppConstants.WALLETAMOUNT, walletAmount);
//                    intentBacktoWalletDashBoard.putExtra(AppConstants.CURRENCY, currency);
//                    intentBacktoWalletDashBoard.putExtra(AppConstants.CURRENCYCODE, currencycode);
//                    startActivity(intentBacktoWalletDashBoard);
//                    finish();
//                } else {
//                    Toast.makeText(this, "Please enter the amount to proceed...", Toast.LENGTH_SHORT).show();
//                }
//                break;
            case R.id.action_add_cash_securely:
                if (fromPage.equalsIgnoreCase(AppConstants.WITHDRAW)) {

                    if (accountSaved.equalsIgnoreCase("0")) {
                        startActivityForResult(new Intent(this, StripeSettingsActivity.class)
                                        .putExtra("fromPage", "withdraw"),
                                SAVE_ACCOUNT_REQUEST_CODE);
                        return;
                    }

                    if (editTextAmount.getText().toString().isEmpty()) {
                        Toast.makeText(this, AppUtils.cleanLangStr(this,
                                walletScreenList.getLblEnterAmtToProceed().getName(), R.string.txt_enter_amount), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (Double.parseDouble(editTextAmount.getText().toString()) < 1) {
                        Toast.makeText(this, AppUtils.cleanLangStr(this,
                                walletScreenList.getLblEnterLessThanOne().getName(), R.string.txt_amount_greater), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (Double.parseDouble(editTextAmount.getText().toString()) > Double.parseDouble(walletAmount)) {
                        Toast.makeText(this, AppUtils.cleanLangStr(this,
                                walletScreenList.getLblEnterAmtLessThanWallet().getName(), R.string.txt_enter_amount_less_than), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    postWithdrawWallet();

                } else {
                    if (radioButtonDebitCreditCard.isChecked()) {

                        if (editTextAmount.getText().toString().isEmpty()) {
                            Toast.makeText(this, AppUtils.cleanLangStr(this,
                                    walletScreenList.getLblEnterAmtToProceed().getName(), R.string.txt_enter_amount), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (Double.parseDouble(editTextAmount.getText().toString()) < 1) {
                            Toast.makeText(this, AppUtils.cleanLangStr(this,
                                    walletScreenList.getLblEnterLessThanOne().getName(), R.string.txt_amount_greater), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (fromPage.equalsIgnoreCase(AppConstants.WITHDRAW) && Double.parseDouble(editTextAmount.getText().toString()) > Double.parseDouble(walletAmount)) {
                            Toast.makeText(this, AppUtils.cleanLangStr(this,
                                    walletScreenList.getLblEnterAmtLessThanWallet().getName(), R.string.txt_enter_amount_less_than), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (editTextCardNumber.getText().toString().isEmpty()) {
                            Toast.makeText(this, AppUtils.cleanLangStr(this,
                                    walletScreenList.getLblCardNumberProceed().getName(), R.string.txt_enter_card_number_proceed), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (editTextMM.getText().toString().isEmpty()) {
                            Toast.makeText(this, AppUtils.cleanLangStr(this,
                                    walletScreenList.getLblExpiryMonthYear().getName(), R.string.txt_enter_month_year_proceed), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (editTextCVV.getText().toString().isEmpty()) {
                            Toast.makeText(this, AppUtils.cleanLangStr(this,
                                    walletScreenList.getLblEnterCvv().getName(), R.string.txt_enter_cvv_proceed), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String[] spiltExpiryDateStr = editTextMM.getText().toString().split("/");
                        int month = Integer.parseInt(spiltExpiryDateStr[0]);
                        int year = Integer.parseInt(spiltExpiryDateStr[1]);

                        Card card = new Card(editTextCardNumber.getText().toString(), month, year, editTextCVV.getText().toString());
                        card.setCurrency(currencycode);

                        progressDlg.showProgressDialog(this, null, null);

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (card != null) {
                                    if (card.validateCard() && card.validateCVC() && card.validateExpiryDate()) {
                                        try {
                                            if (!publicKey.isEmpty()) {
//                                        Stripe stripe = new Stripe(publicKey);
                                                Stripe stripe = new Stripe(publicKey);
                                                stripe.createToken(card, new TokenCallback() {
                                                    @Override
                                                    public void onError(Exception error) {
                                                        progressDlg.dismissProgressDialog();
//                                                Log.i("TAG_ERROR", error.getMessage());
                                                    }

                                                    @Override
                                                    public void onSuccess(Token token) {
//                                                progressDlg.dismissProgressDialog();
                                                   /* if (fromPage.equalsIgnoreCase(AppConstants.WITHDRAW)) {
                                                        postWithdrawWallet(token.getId());
                                                    } else {*/
                                                        postTopupWallet(token.getId(), "stripe");
                                                        //  }
                                                        Log.i("TAG_TOKEN", token.getId());
                                                    }
                                                });
                                            } else {
                                                progressDlg.dismissProgressDialog();
                                                Toast.makeText(CardListActivity.this, "please contact the system administrator...", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (AuthenticationException e) {
                                            e.printStackTrace();
                                            progressDlg.dismissProgressDialog();
                                        }

                                    } else {
                                        progressDlg.dismissProgressDialog();
                                        Toast.makeText(CardListActivity.this, AppUtils.cleanLangStr(CardListActivity.this,
                                                walletScreenList.getLblValidCardDetails().getName(), R.string.txt_enter_valid_card_details), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

                    } else if (paypal.isChecked()) {
                        if (editTextAmount.getText().toString().isEmpty()) {
                            Toast.makeText(this, AppUtils.cleanLangStr(this,
                                    walletScreenList.getLblEnterAmtToProceed().getName(), R.string.txt_enter_amount), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (Double.parseDouble(editTextAmount.getText().toString()) < 1) {
                            Toast.makeText(this, AppUtils.cleanLangStr(this,
                                    walletScreenList.getLblEnterLessThanOne().getName(), R.string.txt_amount_greater), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // onBraintreeSubmit(getString(R.string.test_client_token));
//                        onBrainTreeSubmit();
                        try {
                            mBraintreeFragment = BraintreeFragment.newInstance(this, braintreeKey);
                            PayPalRequest request = new PayPalRequest(editTextAmount.getText().toString())
                                    .currencyCode(currencycode)
                                    .intent(PayPalRequest.INTENT_AUTHORIZE);
                            PayPal.requestOneTimePayment(mBraintreeFragment, request);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (razorPay.isChecked()) {
                        if (editTextAmount.getText().toString().isEmpty()) {
                            Toast.makeText(this, AppUtils.cleanLangStr(this,
                                    walletScreenList.getLblEnterAmtToProceed().getName(), R.string.txt_enter_amount), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (Double.parseDouble(editTextAmount.getText().toString()) < 1) {
                            Toast.makeText(this, AppUtils.cleanLangStr(this,
                                    walletScreenList.getLblEnterLessThanOne().getName(), R.string.txt_amount_greater), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        startPayment();
                    } else {
                        Toast.makeText(CardListActivity.this, AppUtils.cleanLangStr(CardListActivity.this,
                                "Select any one payment option", R.string.txt_enter_valid_card_details), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }


//            }
//        }
    }

    public void onBrainTreeSubmit() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            Call<PaypalResponseToken> classificationCall = apiService
                    .getPaypalToken(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
            RetrofitHandler.executeRetrofit(this, classificationCall,
                    AppConstants.GETPAYPALTOKEN, this, false);
        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
      /*  DropInRequest dropInRequest = new DropInRequest()
                .clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);*/
    }

    public void setVisibilityNewCard() {
        try {
            editTextCardNumber.getText().clear();
            editTextMM.getText().clear();
            editTextCVV.getText().clear();

            LinearLayout linearLayout = (LinearLayout) radioGroupAddNewCards.getChildAt(1);
            RadioButton radioButton = (RadioButton) radioGroupAddNewCards.getChildAt(0);

            if (radioButton.isChecked()) {
                radioGroupAddNewCards.clearCheck();
                linearLayout.setVisibility(View.GONE);
            }
            linearLayoutAddNewCardPanel.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postTopupWallet(String tokenid, String type) {
        if (AppUtils.isNetworkAvailable(this)) {
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            Call<BaseResponse> classificationCall = apiService.postTopupWallet(PreferenceStorage.getKey(AppConstants.USER_TOKEN),
                    tokenid, editTextAmount.getText().toString(), currencycode, type);
            RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.TOPUPWALLET, this, false);
        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }

    public void postWithdrawWallet() {
        if (AppUtils.isNetworkAvailable(this)) {
            /*ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            Call<BaseResponse> classificationCall = apiService.postWithdrawWallet(PreferenceStorage.getKey(AppConstants.USER_TOKEN), tokenid, editTextAmount.getText().toString());
            RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.WITHDRAWWALLET, this, false);*/
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            Call<BaseResponse> classificationCall = apiService.walletWithdraw(PreferenceStorage.getKey(AppConstants.USER_TOKEN),
                    editTextAmount.getText().toString(), currencycode);
            RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.WITHDRAWWALLET, this, false);
        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
//        super.onSuccess(myRes, isLoadMore, responseType);
        switch (responseType) {
            case AppConstants.TOPUPWALLET:
            case AppConstants.BRAINTREEADDWALLET:
                BaseResponse baseResponse = (BaseResponse) myRes;
                if (baseResponse.getResponseHeader().getResponseCode().equalsIgnoreCase("200")) {
                    finish();
                }
                break;
            case AppConstants.WITHDRAWWALLET:
                try {
                    BaseResponse baseResponse1 = (BaseResponse) myRes;
                    if (baseResponse1.getResponseHeader().getResponseCode().equalsIgnoreCase("200")) {
                        finish();
                    }
                    Toast.makeText(this, baseResponse1.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case AppConstants.STRIPEDETAILS:
                StripeDetailsModel stripeDetailsModel = (StripeDetailsModel) myRes;
                publicKey = stripeDetailsModel.getData().getPublishable_key();
                braintreeKey = stripeDetailsModel.getData().getBraintreeKey();
                razorPayAPIKey = stripeDetailsModel.getData().getRazorpayApikey();
                if (!stripeDetailsModel.getData().getRazorOption().equalsIgnoreCase("1")) {
                    razorPay.setVisibility(View.GONE);
                }
                if (!stripeDetailsModel.getData().getPaypalOption().equalsIgnoreCase("1")) {
                    paypal.setVisibility(View.GONE);
                }
                if (!stripeDetailsModel.getData().getStripeOption().equalsIgnoreCase("1")) {
                    radioButtonDebitCreditCard.setVisibility(View.GONE);
                }

                break;

            case AppConstants.GETPAYPALTOKEN:
                PaypalResponseToken token = (PaypalResponseToken) myRes;
                if (token.getResponseHeader().getResponseCode().equalsIgnoreCase("200")) {
                    /*DropInRequest dropInRequest = new DropInRequest()
                            .amount("1")
                            .clientToken(token.getData().getBraintreeKey());
                    startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);*/

                    try {
                        mBraintreeFragment = BraintreeFragment.newInstance(this,
                                token.getData().getBraintreeKey());
                        PayPalRequest request = new PayPalRequest(editTextAmount.getText().toString())
                                .currencyCode(currencycode)
                                .intent(PayPalRequest.INTENT_AUTHORIZE);
                        PayPal.requestOneTimePayment(mBraintreeFragment, request);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {
//        super.onRequestFailure(myRes, isLoadMore, responseType);
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {
//        super.onResponseFailure(myRes, isLoadMore, responseType);
    }

    public void getStripeDetails() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<StripeDetailsModel> subscriptionSuccessModelCall = apiInterface.getStripeDetails(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
        RetrofitHandler.executeRetrofit(CardListActivity.this, subscriptionSuccessModelCall, AppConstants.STRIPEDETAILS, CardListActivity.this, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String payment_method_nonce = result.getPaymentMethodNonce().getNonce();
                Log.d("Nonce", payment_method_nonce);
            } else if (resultCode == Activity.RESULT_CANCELED) {

            } else {

                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.e("Error", error.getMessage());
            }
        } else if (requestCode == SAVE_ACCOUNT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (editTextAmount.getText().toString().isEmpty()) {
                    Toast.makeText(this, AppUtils.cleanLangStr(this,
                            walletScreenList.getLblEnterAmtToProceed().getName(), R.string.txt_enter_amount), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Double.parseDouble(editTextAmount.getText().toString()) < 1) {
                    Toast.makeText(this, AppUtils.cleanLangStr(this,
                            walletScreenList.getLblEnterLessThanOne().getName(), R.string.txt_amount_greater), Toast.LENGTH_SHORT).show();
                    return;
                }
                postWithdrawWallet();
            }
        }
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        Log.d("paymentMethodNonce :", paymentMethodNonce.toString());
        String nonce = paymentMethodNonce.getNonce();
        callBrainTreeAddWallet(editTextAmount.getText().toString(), nonce,
                ((PayPalAccountNonce) paymentMethodNonce).getClientMetadataId());
    }

    private void callBrainTreeAddWallet(String amount, String nonce, String orderId) {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            Call<BaseResponse> classificationCall = apiService.callBrainTreeAddWallet
                    (PreferenceStorage.getKey(AppConstants.USER_TOKEN),
                            amount, orderId, nonce);
            RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.BRAINTREEADDWALLET,
                    this, false);
        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }

    public void startPayment() {
        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID(razorPayAPIKey.trim());
        try {
            JSONObject options = new JSONObject();
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", currencycode);
            options.put("amount", String.valueOf(Double.parseDouble(editTextAmount.getText().toString()) * 100));
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            postTopupWallet(razorpayPaymentID, "razorpay");
//            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentSuccess", e);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + response + " " + response, Toast.LENGTH_SHORT).show();
            Log.i("messaged", "Payment failed: " + code + " " + response);
        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentError", e);
        }
    }
}



