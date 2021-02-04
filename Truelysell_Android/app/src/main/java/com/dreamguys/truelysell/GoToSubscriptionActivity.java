package com.dreamguys.truelysell;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoToSubscriptionActivity extends AppCompatActivity {

    @BindView(R.id.tv_buy_subs)
    TextView tvBuySubs;
    @BindView(R.id.tv_thankyou)
    TextView tvThankyou;
    @BindView(R.id.bt_goto_subs)
    Button btGotoSubs;
    String fromPage;
    Window window;
    public int appColor = 0;
    public LanguageModel.Subscription subscription_used_texts = new LanguageModel().new Subscription();
    @BindView(R.id.iv_subs)
    ImageView ivSubs;
    public LanguageResponse.Data.Language.SubscriptionScreen subscriptionScreenList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goto_subscription);
        ButterKnife.bind(this);
        fromPage = getIntent().getStringExtra("FromPage");

        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivSubs.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
                btGotoSubs.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
                tvBuySubs.setTextColor(AppUtils.getPrimaryAppTheme(this));
            }
        }
    }

    @OnClick(R.id.bt_goto_subs)
    public void onViewClicked() {
        Intent callSubscriptionAct = new Intent(GoToSubscriptionActivity.this, SubscriptionActivity.class);
        callSubscriptionAct.putExtra("FromPage", fromPage);
        AppUtils.appStartIntent(GoToSubscriptionActivity.this, callSubscriptionAct);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            getLocale();
            tvThankyou.setText(AppUtils.cleanLangStr(GoToSubscriptionActivity.this,
                    subscriptionScreenList.getLblThankYouUpgrade().getName(), R.string.thank_you_upgrade));
            tvBuySubs.setText(AppUtils.cleanLangStr(GoToSubscriptionActivity.this,
                    subscriptionScreenList.getLblBuySubscription().getName(), R.string.buy_subscription));
            btGotoSubs.setText(AppUtils.cleanLangStr(GoToSubscriptionActivity.this,
                    subscriptionScreenList.getBtnGoSubscription().getName(), R.string.gotoSubscription));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getLocale() {
        try {
            String subscriptionStr = PreferenceStorage.getKey(CommonLangModel.SubscriptionScreen);
            subscriptionScreenList = new Gson().fromJson(subscriptionStr, LanguageResponse.Data.Language.SubscriptionScreen.class);
        } catch (Exception e) {
            subscriptionScreenList = new LanguageResponse().new Data().new Language().new SubscriptionScreen();
        }
    }
}
