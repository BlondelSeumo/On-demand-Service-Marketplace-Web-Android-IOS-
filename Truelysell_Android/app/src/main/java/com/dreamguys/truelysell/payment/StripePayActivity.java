package com.dreamguys.truelysell.payment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.braintreepayments.cardform.view.CardForm;
import com.dreamguys.truelysell.BaseActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.SubscriptionThankYouActivity;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.StripeDetailsModel;
import com.dreamguys.truelysell.datamodel.SubscriptionSuccessModel;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.google.gson.Gson;
import com.stripe.android.BuildConfig;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;

public class StripePayActivity extends BaseActivity implements RetrofitHandler.RetrofitResHandler {

    CardForm cardForm;
    Button buy;
    String transaction_id = "";
    //    Toolbar mToolbar;
    String price, subsId, subsName, fromPage, public_key = "", secret_key = "";
    public LanguageModel.Subscription subscription_used_texts = new LanguageModel().new Subscription();
    public int appColor = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.btnBuy);
//        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        buy.setText(AppUtils.cleanLangStr(this, subscriptionScreenList.getLblSubscripeNow().getName(),
                R.string.subscribe_now));
//        setToolBarTitle(getString(R.string.hint_subscription));

        price = getIntent().getStringExtra(AppConstants.StripePrice);
        subsId = getIntent().getStringExtra(AppConstants.StripeSubId);
        subsName = getIntent().getStringExtra(AppConstants.StripeSubName);
        fromPage = getIntent().getStringExtra("FromPage");
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .setup(this);


    }

    public void submitCard(View view) {
        // TODO: replace with your own test key
        /*final String publishableApiKey = BuildConfig.DEBUG ?
                "pk_test_6pRNASCoBOKtIshFeQd4XMUh" :
                getString(R.string.com_stripe_publishable_key);*/


        if (!cardForm.getCardNumber().isEmpty() && !cardForm.getExpirationMonth().isEmpty() && !cardForm.getExpirationYear().isEmpty()
                && !cardForm.getCvv().isEmpty()) {
            ProgressDlg.showProgressDialog(StripePayActivity.this, null, null);
            getStripeDetails();

        } else {
            AppUtils.showToast(StripePayActivity.this, getString(R.string.err_payment));
        }


    }

    private void generateCardPayment() {
        final String publishableApiKey = BuildConfig.DEBUG ?
                "" :
                public_key;

        Card card = new Card(cardForm.getCardNumber(),
                Integer.parseInt(cardForm.getExpirationMonth()),
                Integer.parseInt(cardForm.getExpirationYear()),
                cardForm.getCvv());
        card.setCurrency(AppConstants.DefaultCurrency);

        Stripe stripe = new Stripe();
        stripe.createToken(card, publishableApiKey, new TokenCallback() {
            public void onSuccess(Token token) {
                // TODO: Send Token information to your backend to initiate a charge
                com.stripe.Stripe.apiKey = secret_key;
                /*Toast.makeText(
                        getApplicationContext(),
                        "Token created: " + token.getId(),
                        Toast.LENGTH_LONG).show();*/
                new SubscriptionTask().execute(token.getId());
            }

            public void onError(Exception error) {
                ProgressDlg.dismissProgressDialog();
                Log.d("Stripe", error.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
        if (myRes instanceof SubscriptionSuccessModel) {
            ProgressDlg.dismissProgressDialog();
            SubscriptionSuccessModel subscriptionSuccessModel = (SubscriptionSuccessModel) myRes;
            Intent callStipeSuccessAct = new Intent(StripePayActivity.this, SubscriptionThankYouActivity.class);
            callStipeSuccessAct.putExtra("FromPage", fromPage);
            AppUtils.appStartIntent(StripePayActivity.this, callStipeSuccessAct);
            PreferenceStorage.setKey(AppConstants.USER_SUBS_TYPE, Integer.parseInt(subscriptionSuccessModel.getData().getSubscriberId()));
            PreferenceStorage.setKey(AppConstants.ISSUBSCRIBED, subscriptionSuccessModel.getData().getIs_subscribed());
            finish();
        } else if (myRes instanceof StripeDetailsModel) {
            StripeDetailsModel stripeDetailsModel = (StripeDetailsModel) myRes;
            secret_key = stripeDetailsModel.getData().getSecret_key();
            public_key = stripeDetailsModel.getData().getPublishable_key();
            generateCardPayment();
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {
        ProgressDlg.dismissProgressDialog();
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {
        ProgressDlg.dismissProgressDialog();
    }

    public class SubscriptionTask extends AsyncTask<String, String, Charge> {
        Charge charge;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Charge doInBackground(String... strings) {
            DecimalFormat decimalFormat = new DecimalFormat("##.##");
            decimalFormat.applyPattern(price);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("amount", price.replace(".", ""));
            params.put("currency", AppConstants.DefaultCurrency);
            params.put("description", subsName);
            params.put("source", strings[0]);
            try {
                charge = Charge.create(params);
            } catch (AuthenticationException e) {
                e.printStackTrace();
            } catch (InvalidRequestException e) {
                e.printStackTrace();
            } catch (APIConnectionException e) {
                e.printStackTrace();
            } catch (CardException e) {
                e.printStackTrace();
            } catch (APIException e) {
                e.printStackTrace();
            }
            return charge;
        }

        @Override
        protected void onPostExecute(Charge charge) {
            super.onPostExecute(charge);

            if (charge != null) {
                transaction_id = charge.getId();
                if (charge.getStatus().equalsIgnoreCase("succeeded")) {

                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

                    Call<SubscriptionSuccessModel> subscriptionSuccessModelCall = apiInterface.postSuccessSubscription(subsId, transaction_id, "", PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.MY_LANGUAGE));
                    RetrofitHandler.executeRetrofit(StripePayActivity.this, subscriptionSuccessModelCall, AppConstants.SUBSCRIPTIONSUCCESS, StripePayActivity.this, false);


                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setAppTheme();
//        getLocaleData();
//        buy.setBackgroundColor(appColor);

//        setSupportActionBar(mToolbar);
//        mToolbar.setTitle(AppUtils.cleanLangStr(this, getString(R.string.hint_subscription), R.string.hint_subscription));
//        mToolbar.setBackgroundColor(appColor);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void getLocaleData() {
        try {
            String subDataStr = PreferenceStorage.getKey(CommonLangModel.subscription);
            subscription_used_texts = new Gson().fromJson(subDataStr, LanguageModel.Subscription.class);
        } catch (Exception e) {
            subscription_used_texts = new LanguageModel().new Subscription();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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


    public void getStripeDetails() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<StripeDetailsModel> subscriptionSuccessModelCall = apiInterface.getStripeDetails(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
        RetrofitHandler.executeRetrofit(StripePayActivity.this, subscriptionSuccessModelCall, AppConstants.STRIPEDETAILS, StripePayActivity.this, false);

    }
}
