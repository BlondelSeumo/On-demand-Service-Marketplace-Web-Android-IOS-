package com.dreamguys.truelysell;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;

/**
 * Created by Hari on 16-05-2018.
 */

public class SubscriptionThankYouActivity extends AppCompatActivity {


    @BindView(R.id.bt_getStarted)
    Button btGetStarted;
    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    String fromPage;
    public int appColor = 0;
    LanguageModel.Subscription subscriptionTexts = new LanguageModel().new Subscription();
    @BindView(R.id.iv_subs)
    ImageView ivSubs;
    @BindView(R.id.tv_subs_thank_you)
    TextView tvSubsThankYou;
    @BindView(R.id.tv_subs_msg)
    TextView tvSubsMsg;
    LanguageResponse.Data.Language.SubscriptionScreen subscriptionScreenList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_thankyou);
        ButterKnife.bind(this);
        fromPage = getIntent().getStringExtra("FromPage");

        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mToolbar.setBackgroundColor(AppUtils.getPrimaryAppTheme(this));
                ivSubs.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
                btGetStarted.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
                tvSubsThankYou.setTextColor(AppUtils.getPrimaryAppTheme(this));
            }
        }
    }

    @OnClick(R.id.bt_getStarted)
    public void onViewClicked() {
        Intent callSubDetailAct = new Intent(this, HomeActivity.class);
        callSubDetailAct.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        callSubDetailAct.putExtra("FromPage", fromPage);
        AppUtils.appStartIntent(this, callSubDetailAct);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocale();
        setSupportActionBar(mToolbar);
        try {
            mToolbar.setTitle(AppUtils.cleanLangStr(this, subscriptionScreenList.getLblTitleSubscription().getName()
                    , R.string.hint_subscription));
            tvSubsMsg.setText(AppUtils.cleanLangStr(this, subscriptionScreenList.getLblThankyouDesc().getName()
                    , R.string.thankyou_desc));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mToolbar.setBackgroundColor(appColor);
            btGetStarted.setBackgroundColor(appColor);
            tvSubsThankYou.setTextColor(appColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getLocale(){
        try {
            String subscriptionStr = PreferenceStorage.getKey(CommonLangModel.SubscriptionScreen);
            subscriptionScreenList = new Gson().fromJson(subscriptionStr, LanguageResponse.Data.Language.SubscriptionScreen.class);

            tvSubsMsg.setText(AppUtils.cleanLangStr(this, subscriptionScreenList.getLblTitleSubscription().getName()
                    , R.string.hint_subscription));
        } catch (Exception e) {
            subscriptionScreenList = new LanguageResponse().new Data().new Language().new SubscriptionScreen();
        }
    }

}
