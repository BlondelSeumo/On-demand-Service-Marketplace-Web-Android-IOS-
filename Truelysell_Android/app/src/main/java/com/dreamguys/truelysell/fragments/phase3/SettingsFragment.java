package com.dreamguys.truelysell.fragments.phase3;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dreamguys.truelysell.BuildConfig;
import com.dreamguys.truelysell.HomeActivity;
import com.dreamguys.truelysell.MapActivity;
import com.dreamguys.truelysell.MyNotificationActivity;
import com.dreamguys.truelysell.MyProfileActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.StripeSettingsActivity;
import com.dreamguys.truelysell.WebViewActivity;
import com.dreamguys.truelysell.adapters.AppThemeListAdapter;
import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.DAOAppTheme;
import com.dreamguys.truelysell.datamodel.LanguageListResponse;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.LogoutData;
import com.dreamguys.truelysell.datamodel.Phase3.DAOProviderProfile;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.LocaleUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;
import com.dreamguys.truelysell.wallet.WalletDashBoard;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class SettingsFragment extends Fragment implements RetrofitHandler.RetrofitResHandler {

    Unbinder unbinder;
    @BindView(R.id.iv_user_image)
    CircleImageView ivUserImage;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_email_address)
    TextView tvEmailAddress;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.iv_userlogin)
    ImageView ivUserlogin;
    HomeActivity homeActivity;
    @BindView(R.id.rl_regional)
    RelativeLayout rlRegional;
    @BindView(R.id.ll_change_location)
    LinearLayout llChangeLocation;
    @BindView(R.id.sv_settings)
    NestedScrollView svSettings;
    @BindView(R.id.rl_change_location)
    RelativeLayout rlChangeLocation;
    @BindView(R.id.rl_notifications)
    RelativeLayout rlNotifications;
    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.tv_edit_profile)
    TextView tvEditProfile;
    @BindView(R.id.tv_my_services)
    TextView tvMyServices;
    @BindView(R.id.tv_notifications)
    TextView tvNotifications;
    @BindView(R.id.tv_wallet)
    TextView tvWallet;
    @BindView(R.id.iv_wallet)
    ImageView ivWallet;
    @BindView(R.id.rl_wallet)
    RelativeLayout rlWallet;
    @BindView(R.id.tv_change_location)
    TextView tvChangeLocation;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.tv_change_language)
    TextView tvChangeLanguage;
    @BindView(R.id.iv_lang)
    ImageView ivLang;
    @BindView(R.id.iv_others)
    ImageView ivOthers;
    @BindView(R.id.tv_suggestion)
    TextView tvSuggestion;
    @BindView(R.id.tv_termscondition)
    TextView tvTermscondition;
    @BindView(R.id.tv_shareapp)
    TextView tvShareapp;
    @BindView(R.id.tv_rateapp)
    TextView tvRateapp;
    @BindView(R.id.iv_rate)
    ImageView ivRate;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.iv_regional)
    ImageView ivRegional;
    @BindView(R.id.iv_apptheme)
    ImageView ivApptheme;
    String appColor = "", secColor = "";
    List<DAOAppTheme> mAppTheme = new ArrayList<>();
    List<DAOAppTheme> mAppTheme1 = new ArrayList<>();
    @BindView(R.id.tv_change_apptheme)
    TextView tvApptheme;
    @BindView(R.id.txt_settings)
    TextView txtSettings;
    @BindView(R.id.my_history)
    TextView myHistory;
    @BindView(R.id.txt_regional)
    TextView txtRegional;
    @BindView(R.id.tv_apptheme)
    TextView tv_Apptheme;
    @BindView(R.id.txt_others)
    TextView txtOthers;
    @BindView(R.id.txt_delete_account)
    TextView txtDeleteAccount;
    String lang = "";
    LanguageResponse.Data.Language.SettingsScreen settingScreenList;
    LanguageResponse.Data.Language.CommonStrings commonStringsList;
    List<LanguageListResponse.Data> languageList = new ArrayList<>();
    @BindView(R.id.tv_accountSettings)
    TextView tvAccountSettings;
    @BindView(R.id.rl_account_settings)
    RelativeLayout rlAccountSettings;
    @BindView(R.id.iv_contact_us)
    ImageView ivContactUs;
    @BindView(R.id.rl_make_suggestion)
    RelativeLayout rlMakeSuggestion;
    @BindView(R.id.iv_terms)
    ImageView ivTerms;
    @BindView(R.id.rl_terms_conditions)
    RelativeLayout rlTermsConditions;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.rl_share_app)
    RelativeLayout rlShareApp;
    @BindView(R.id.rl_rate_app)
    RelativeLayout rlRateApp;
    @BindView(R.id.image_logout)
    ImageView imageLogout;
    @BindView(R.id.rl_logout)
    RelativeLayout rlLogout;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.rl_delete_account)
    RelativeLayout rlDeleteAccount;

    public SettingsFragment(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    public SettingsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, mView);

        ivUserlogin.setVisibility(View.GONE);

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivUserImage.setBorderColor(AppUtils.getPrimaryAppTheme(getActivity()));
                ivRegional.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                ivOthers.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                tvNotifications.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvWallet.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvChangeLanguage.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvChangeLocation.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvAccountSettings.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvSuggestion.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvTermscondition.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvShareapp.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvRateapp.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvLogout.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvApptheme.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            }
        }

        if (PreferenceStorage.getKey(AppConstants.USER_TYPE) != null &&
                PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("1")) {
            rlAccountSettings.setVisibility(View.VISIBLE);
            getProviderProfileData();
        }

        try {
            if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                svSettings.setVisibility(View.VISIBLE);
                if (PreferenceStorage.getKey(AppConstants.PNAME) != null && PreferenceStorage.getKey(AppConstants.PEMAIL) != null) {
                    tvUsername.setText(PreferenceStorage.getKey(AppConstants.PNAME));
                    tvEmailAddress.setText(PreferenceStorage.getKey(AppConstants.PEMAIL));
                   Picasso.get()
                            .load(AppConstants.BASE_URL + PreferenceStorage.getKey(AppConstants.PIMAGE))
                            .placeholder(R.drawable.ic_user_placeholder)
                            .error(R.drawable.ic_user_placeholder)
                            .into(ivUserImage);
                }

                if (PreferenceStorage.getKey(AppConstants.MY_CITYLOCATION) != null) {
                    tvLocation.setText(PreferenceStorage.getKey(AppConstants.MY_CITYLOCATION));
                }

                if (PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("1")) {
                    //                rlRegional.setVisibility(View.GONE);
                    rlChangeLocation.setVisibility(View.GONE);
                }

            } else {
                svSettings.setVisibility(View.INVISIBLE);
//                homeActivity.showUserLoginDialog();
                homeActivity.getLoginType();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getLocaleData();

        try {
            txtSettings.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblSettingsTitle().getName(), R.string.txt_settings));
            tvEditProfile.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblEditProfile().getName(), R.string.txt_edit_profile));
            txtRegional.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblRegional().getName(), R.string.txt_regional));
            tvNotifications.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblNotifications().getName(), R.string.txt_notifications));
            tvWallet.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblWallet().getName(), R.string.txt_wallet));
            tvChangeLocation.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblChangeLocation().getName(), R.string.txt_change_location));
            tvChangeLanguage.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblChooseLang().getName(), R.string.txt_change_language));
            tvApptheme.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblChangeColor().getName(), R.string.txt_change_apptheme));
            txtOthers.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblOthers().getName(), R.string.txt_others));
            tvSuggestion.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblSuggession().getName(), R.string.txt_make_a_suggestion));
            tvTermscondition.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblTearmsAndCondition().getName(), R.string.terms_and_conditions));
            tvShareapp.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblShareApp().getName(), R.string.share_app));
            tvRateapp.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblRateApp().getName(), R.string.txt_rate_our_app));
            tvLogout.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblLogout().getName(), R.string.txt_logout));
            tvAccountSettings.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLbl_account_settings().getName(), R.string.txt_account_settings));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        setLanguageSettings();
        return mView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_edit_profile, R.id.rl_change_language, R.id.rl_change_location, R.id.rl_change_apptheme, R.id.rl_make_suggestion,
            R.id.rl_terms_conditions, R.id.rl_share_app, R.id.rl_rate_app, R.id.rl_logout, R.id.rl_delete_account,
            R.id.rl_notifications, R.id.rl_wallet, R.id.rl_account_settings})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_profile:
                startActivity(new Intent(getActivity(), MyProfileActivity.class));
                break;
            case R.id.rl_change_language:
                showLanguageDialog();
                break;
            case R.id.rl_change_location:
                Intent callMapActivity = new Intent(getActivity(), MapActivity.class);
                callMapActivity.putExtra("From", AppConstants.PAGE_SETTINGS);
                AppUtils.appStartIntent(getActivity(), callMapActivity);
                break;
            case R.id.rl_change_apptheme:
                loadAppThemeDialog();
                break;
            case R.id.rl_make_suggestion:
                onContactUsClicked();
                break;
            case R.id.rl_terms_conditions:
                onTermsandConditionsClicked();
                break;
            case R.id.rl_share_app:
                onShareAppClicked();
                break;
            case R.id.rl_rate_app:
                onRateUsClicked();
                break;
            case R.id.rl_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage(AppUtils.cleanLangStr(getActivity(), settingScreenList.getLblLogout().getName(),
                        R.string.txt_logout_confirm)) //TODO:
                        .setCancelable(false)
                        .setPositiveButton(AppUtils.cleanLangStr(getActivity(), commonStringsList.getBtnYes().getName(), R.string.txt_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { //TODO:
//                                PreferenceStorage.clearPref();
//                                dialog.dismiss();
                                userLogout(dialog);
//                                Intent callMainActivity = new Intent(getActivity(), HomeActivity.class);
//                                callMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(callMainActivity);
//                                userLogout(); //TODO:
                            }
                        })
                        .setNegativeButton(AppUtils.cleanLangStr(getActivity(), commonStringsList.getBtnNo().getName(), R.string.txt_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { //TODO:
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.rl_delete_account:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage(AppUtils.cleanLangStr(getActivity(), "", R.string.txt_delete_acc)) //TODO:
                        .setCancelable(false)
                        .setPositiveButton(AppUtils.cleanLangStr(getActivity(), "", R.string.txt_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { //TODO:
                                userDeleteAccount(dialog);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(AppUtils.cleanLangStr(getActivity(), "", R.string.txt_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) { //TODO:
                                dialog.cancel();
                            }
                        });
                AlertDialog alert1 = builder1.create();
                alert1.show();
                break;
            case R.id.rl_notifications:
                startActivity(new Intent(getActivity(), MyNotificationActivity.class));
                break;
            case R.id.rl_wallet:
                startActivity(new Intent(getActivity(), WalletDashBoard.class));
                break;
            case R.id.rl_account_settings:
                showAccountSettingDialog();
                break;
        }
    }

    private void showAccountSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View titleView = inflater.inflate(R.layout.list_custom_alert_dialog_tiltle, null);
        String[] types = {"Stripe"};
        TextView textView = titleView.findViewById(R.id.tv_title);
        textView.setText(AppUtils.cleanLangStr(getActivity(),
                settingScreenList.getLbl_account_type().getName(), R.string.txt_account_type));
        builder.setCustomTitle(titleView).setTitle(AppUtils.cleanLangStr(getActivity(),
                settingScreenList.getLbl_account_type().getName(), R.string.txt_account_type));
        builder.setAdapter(new TypeAdapter(getActivity(), R.layout.list_item_language, types),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (types[item].equalsIgnoreCase("stripe")) {
                            startActivity(new Intent(getActivity(), StripeSettingsActivity.class)
                                    .putExtra("fromPage", "settings"));
                        }
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onRateUsClicked() {
        final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void onShareAppClicked() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);

            String shareMessage = "\n" + AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblReferenceText().getName(), R.string.txt_share) + "\n\n";
            String referralCode = " ";
            if (PreferenceStorage.getKey(AppConstants.SHARECODE) != null &&
                    !PreferenceStorage.getKey(AppConstants.SHARECODE).isEmpty()) {
                referralCode = "Referral Code: " + PreferenceStorage.getKey(AppConstants.SHARECODE);
            }

            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" +
                    BuildConfig.APPLICATION_ID + "\n\n" + referralCode;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share Via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onContactUsClicked() {
        Intent i = new Intent(getActivity(), WebViewActivity.class);
        i.putExtra(AppConstants.TbTitle, AppUtils.cleanLangStr(getActivity(),
                settingScreenList.getLblSuggession().getName(), R.string.txt_make_a_suggestion));
        i.putExtra(AppConstants.URL, AppConstants.ContactUsURL);
        startActivity(i);
    }

    public void onTermsandConditionsClicked() {
        Intent i = new Intent(getActivity(), WebViewActivity.class);
        i.putExtra(AppConstants.TbTitle, AppUtils.cleanLangStr(getActivity(),
                settingScreenList.getLblTearmsAndCondition().getName(), R.string.terms_and_conditions));
        i.putExtra(AppConstants.URL, AppConstants.TermsConditionsURL);
        startActivity(i);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (PreferenceStorage.getKey(AppConstants.PNAME) != null && PreferenceStorage.getKey(AppConstants.PEMAIL) != null) {
                tvUsername.setText(PreferenceStorage.getKey(AppConstants.PNAME));
                tvEmailAddress.setText(PreferenceStorage.getKey(AppConstants.PEMAIL));
               Picasso.get()
                        .load(AppConstants.BASE_URL + PreferenceStorage.getKey(AppConstants.PIMAGE))
                        .placeholder(R.drawable.ic_user_placeholder)
                        .error(R.drawable.ic_user_placeholder)
                        .into(ivUserImage);
            }

            if (PreferenceStorage.getKey(AppConstants.MY_CITYLOCATION) != null) {
                tvLocation.setText(PreferenceStorage.getKey(AppConstants.MY_CITYLOCATION));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userLogout(final DialogInterface dialog) {
        if (AppUtils.isNetworkAvailable(getActivity())) {

            ProgressDlg.showProgressDialog(getActivity(), null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<LogoutData> classificationCall = apiService.postUserLogout(AppConstants.deviceType, PreferenceStorage.getKey(AppConstants.refreshedToken), PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.USER_TYPE));
                classificationCall.enqueue(new Callback<LogoutData>() {
                    @Override
                    public void onResponse(Call<LogoutData> call, Response<LogoutData> response) {
                        ProgressDlg.dismissProgressDialog();
                        if (response.isSuccessful()) {
                            if (response != null && response.body() != null
                                    && response.body().getResponse() != null
                                    && response.body().getResponse().getResponseCode().equalsIgnoreCase("200")) {
                                PreferenceStorage.removeKey(AppConstants.USER_TOKEN);
                                PreferenceStorage.removeKey(AppConstants.USER_TYPE);
                                PreferenceStorage.removeKey(AppConstants.USER_NAME);
                                PreferenceStorage.removeKey(AppConstants.USER_SUBS_TYPE);
                                PreferenceStorage.removeKey(AppConstants.USER_EMAIL);
                                PreferenceStorage.removeKey(AppConstants.USER_PROFILE_IMG);
                                PreferenceStorage.removeKey(AppConstants.USER_PHONE);
//                                PreferenceStorage.removeKey(AppConstants.MY_ADDRESS);
//                                PreferenceStorage.removeKey(AppConstants.MY_LATITUDE);
//                                PreferenceStorage.removeKey(AppConstants.MY_LONGITUDE);
//                                PreferenceStorage.clearPref();
                                dialog.dismiss();
                                Intent a = new Intent(getActivity(), HomeActivity.class);
                                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                AppUtils.appStartIntent(getActivity(), a);
                                getActivity().finish();
                            } else {
                                //AppUtils.showToast(getCurrentActivity(), AppUtils.cleanLangStr(getCurrentActivity(), commonData.getLg7_no_data_were_fo(), R.string.txt_logout_fail));//TODO:
                            }
                        } else {
                            //AppUtils.showToast(getCurrentActivity(), AppUtils.cleanLangStr(getCurrentActivity(), commonData.getLg7_no_data_were_fo(), R.string.txt_logout_fail));//TODO:
                        }
                    }

                    @Override
                    public void onFailure(Call<LogoutData> call, Throwable t) {
                        ProgressDlg.dismissProgressDialog();
                        //AppUtils.showToast(getCurrentActivity(), AppUtils.cleanLangStr(getCurrentActivity(), commonData.getLg7_no_data_were_fo(), R.string.txt_logout_fail));//TODO:

                    }
                });
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(getActivity(), getString(R.string.txt_enable_internet));
        }
    }


    public void userDeleteAccount(final DialogInterface dialog) {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<BaseResponse> classificationCall = apiService.postDeleteAccount(PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.USER_TYPE));
                classificationCall.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        ProgressDlg.dismissProgressDialog();
                        if (response.isSuccessful()) {
                            if (response != null && response.body() != null
                                    && response.body().getResponseHeader() != null
                                    && response.body().getResponseHeader().getResponseCode().equalsIgnoreCase("200")) {
                                PreferenceStorage.removeKey(AppConstants.USER_TOKEN);
                                PreferenceStorage.removeKey(AppConstants.USER_TYPE);
                                PreferenceStorage.removeKey(AppConstants.USER_NAME);
                                PreferenceStorage.removeKey(AppConstants.USER_SUBS_TYPE);
                                PreferenceStorage.removeKey(AppConstants.USER_EMAIL);
                                PreferenceStorage.removeKey(AppConstants.USER_PROFILE_IMG);
                                PreferenceStorage.removeKey(AppConstants.USER_PHONE);
//                                PreferenceStorage.removeKey(AppConstants.MY_ADDRESS);
//                                PreferenceStorage.removeKey(AppConstants.MY_LATITUDE);
//                                PreferenceStorage.removeKey(AppConstants.MY_LONGITUDE);
//                                PreferenceStorage.clearPref();
                                dialog.dismiss();
                                Intent a = new Intent(getActivity(), HomeActivity.class);
                                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                AppUtils.appStartIntent(getActivity(), a);
                                getActivity().finish();
                            } else {
                                //AppUtils.showToast(getCurrentActivity(), AppUtils.cleanLangStr(getCurrentActivity(), commonData.getLg7_no_data_were_fo(), R.string.txt_logout_fail));//TODO:
                            }
                        } else {
                            //AppUtils.showToast(getCurrentActivity(), AppUtils.cleanLangStr(getCurrentActivity(), commonData.getLg7_no_data_were_fo(), R.string.txt_logout_fail));//TODO:
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        ProgressDlg.dismissProgressDialog();
                        //AppUtils.showToast(getCurrentActivity(), AppUtils.cleanLangStr(getCurrentActivity(), commonData.getLg7_no_data_were_fo(), R.string.txt_logout_fail));//TODO:

                    }
                });
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(getActivity(), getString(R.string.txt_enable_internet));
        }
    }

    private class LanguageAdapter extends ArrayAdapter<LanguageListResponse.Data> {
        List<LanguageListResponse.Data> items;

        LanguageAdapter(@NonNull Context context, int resource, List<LanguageListResponse.Data> items) {
            super(context, resource, items);
            this.items = items;
        }

        ViewHolder holder;

        class ViewHolder {
            ImageView icon;
            TextView title;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            final LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_language, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.iv_lang_icon);
                holder.title = (TextView) convertView.findViewById(R.id.tv_lang_txt);
                convertView.setTag(holder);
            } else {
                // view already defined, retrieve view holder
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(items.get(position).getLanguage());
//            if (position == 0)
//                holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_lang_english));
//            else
//                holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_lang_malay));

            return convertView;
        }
    }

    private class TypeAdapter extends ArrayAdapter<String> {
        String[] items;

        TypeAdapter(@NonNull Context context, int resource, String[] items) {
            super(context, resource, items);
            this.items = items;
        }

        ViewHolder holder;

        class ViewHolder {
            ImageView icon;
            TextView title;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            final LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_language, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.iv_lang_icon);
                holder.title = (TextView) convertView.findViewById(R.id.tv_lang_txt);
                convertView.setTag(holder);
            } else {
                // view already defined, retrieve view holder
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(items[position]);
//            if (position == 0)
//                holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_lang_english));
//            else
//                holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_lang_malay));

            return convertView;
        }
    }

    public void setLanguageSettings() {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            String token = AppConstants.DEFAULTTOKEN;
            if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
            }
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<LanguageListResponse> classificationCall = apiService.getLanguageList(token);
                RetrofitHandler.executeRetrofit(getActivity(), classificationCall, AppConstants.GETLANGUAGELIST,
                        this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }
        } else {
            AppUtils.showToast(getActivity(), getString(R.string.txt_enable_internet));
        }
    }

    public void setLocale(String localeName) {
        PreferenceStorage.setKey("locale", localeName);
        PreferenceStorage.setKey("localechanged", "true");
        PreferenceStorage.setKey(AppConstants.LANGUAGE_SET, "true");
        PreferenceStorage.setKey(AppConstants.Language, localeName);
        AppConstants.localeName = localeName;
        LocaleUtils.setLocale(new Locale(localeName));
        LocaleUtils.updateConfigActivity(getActivity(), getActivity().getBaseContext().getResources().getConfiguration());
//        marshallMallowPermission();
        Intent callMainAct = new Intent(getActivity(), HomeActivity.class);
        callMainAct.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(callMainAct);
        getActivity().finish();
    }

    public void loadAppThemeDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_change_apptheme, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);
        RecyclerView rvAppcolors = view.findViewById(R.id.rv_appcolors);
        RecyclerView rvSecAppcolors = view.findViewById(R.id.rv_secondarycolor);
        TextView tvApptheme = view.findViewById(R.id.tv_apptheme);
        TextView primaryColor = view.findViewById(R.id.txt_primary_color);
        TextView secondaryColor = view.findViewById(R.id.txt_secondary_color);
        Button btnApply = view.findViewById(R.id.btn_apply);
        ImageView ivAppTheme = view.findViewById(R.id.iv_apptheme);
        AppThemeListAdapter appThemeListAdapter;
        AppThemeListAdapter appThemeListAdapter1;
        try {
            tvApptheme.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblChangeColor().getName(), R.string.txt_change_apptheme));
            primaryColor.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblPrimaryColor().getName(), R.string.txt_primary_color));
            secondaryColor.setText(AppUtils.cleanLangStr(getActivity(),
                    settingScreenList.getLblPrimaryColor().getName(), R.string.txt_primary_color));
            btnApply.setText(AppUtils.cleanLangStr(getActivity(),
                    commonStringsList.getBtnApply().getName(), R.string.txt_apply));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (AppUtils.isThemeChanged(getActivity())) {
            tvApptheme.setTextColor(AppUtils.getPrimaryAppTheme(getActivity()));
            btnApply.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            ivAppTheme.setImageTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
        }

        setValues();
        setValues1();
        if (PreferenceStorage.getKey(AppConstants.PRIMARYAPPTHEME) != null) {
            appColor = PreferenceStorage.getKey(AppConstants.PRIMARYAPPTHEME);
        }

        if (PreferenceStorage.getKey(AppConstants.SECONDARYAPPTHEME) != null) {
            secColor = PreferenceStorage.getKey(AppConstants.SECONDARYAPPTHEME);
        }

        for (int i = 0; i < mAppTheme.size(); i++) {
            if (mAppTheme.get(i).getAppColor().equalsIgnoreCase(appColor)) {
                mAppTheme.get(i).setSelected(true);
            }
        }

        for (int i = 0; i < mAppTheme1.size(); i++) {
            if (mAppTheme1.get(i).getAppColor().equalsIgnoreCase(secColor)) {
                mAppTheme1.get(i).setSelected(true);
            }
        }

        appThemeListAdapter = new AppThemeListAdapter(getActivity(), mAppTheme, "0");//Primary
        rvAppcolors.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvAppcolors.setAdapter(appThemeListAdapter);

        appThemeListAdapter1 = new AppThemeListAdapter(getActivity(), mAppTheme1, "1");//Secondary
        rvSecAppcolors.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvSecAppcolors.setAdapter(appThemeListAdapter1);


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.80);

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (appThemeListAdapter.apptheme.isEmpty() && appThemeListAdapter1.secTheme.isEmpty()) {
                    dialog.dismiss();
                } else if (PreferenceStorage.getKey(AppConstants.PRIMARYAPPTHEME) == null || PreferenceStorage.getKey(AppConstants.SECONDARYAPPTHEME) == null) {
                    if (!appThemeListAdapter.apptheme.isEmpty()) {
                        PreferenceStorage.setKey(AppConstants.PRIMARYAPPTHEME, appThemeListAdapter.apptheme);
                    }

                    if (!appThemeListAdapter1.secTheme.isEmpty()) {
                        PreferenceStorage.setKey(AppConstants.SECONDARYAPPTHEME, appThemeListAdapter1.secTheme);
                    }

                    if (!appThemeListAdapter.apptheme.isEmpty() || !appThemeListAdapter1.secTheme.isEmpty()) {
                        dialog.dismiss();
                        Intent callHomeAct = new Intent(getActivity(), HomeActivity.class);
                        startActivity(callHomeAct);
                        getActivity().finish();
                    } else {
                        dialog.dismiss();
                    }

                } else if (AppUtils.isThemeChanged(getActivity()) &&
                        !appThemeListAdapter.apptheme.equalsIgnoreCase(
                                PreferenceStorage.getKey(AppConstants.PRIMARYAPPTHEME)) || !appThemeListAdapter1.secTheme.equalsIgnoreCase(
                        PreferenceStorage.getKey(AppConstants.SECONDARYAPPTHEME))) {
                    if (!appThemeListAdapter.apptheme.isEmpty()) {
                        PreferenceStorage.setKey(AppConstants.PRIMARYAPPTHEME, appThemeListAdapter.apptheme);
                    }
                    if (!appThemeListAdapter1.secTheme.isEmpty()) {
                        PreferenceStorage.setKey(AppConstants.SECONDARYAPPTHEME, appThemeListAdapter1.secTheme);
                    }
                    dialog.dismiss();
                    Intent callHomeAct = new Intent(getActivity(), HomeActivity.class);
                    startActivity(callHomeAct);
                    getActivity().finish();
                } else {
                    dialog.dismiss();
                }

            }
        });

    }

    public void setValues() {
        mAppTheme = new ArrayList<>();
        DAOAppTheme daoAppTheme = new DAOAppTheme();
        daoAppTheme.setAppColor("#FF0080");
        mAppTheme.add(daoAppTheme);
        DAOAppTheme daoAppTheme1 = new DAOAppTheme();
        daoAppTheme1.setAppColor("#0090ff");
        mAppTheme.add(daoAppTheme1);
        DAOAppTheme daoAppTheme2 = new DAOAppTheme();
        daoAppTheme2.setAppColor("#FFFFC107");
        mAppTheme.add(daoAppTheme2);
        DAOAppTheme daoAppTheme3 = new DAOAppTheme();
        daoAppTheme3.setAppColor("#ff6000");
        mAppTheme.add(daoAppTheme3);
        DAOAppTheme daoAppTheme4 = new DAOAppTheme();
        daoAppTheme4.setAppColor("#db0000");
        mAppTheme.add(daoAppTheme4);
    }

    public void setValues1() {
        mAppTheme1 = new ArrayList<>();
        DAOAppTheme daoAppTheme = new DAOAppTheme();
        daoAppTheme.setAppColor("#db0000");
        mAppTheme1.add(daoAppTheme);
        DAOAppTheme daoAppTheme1 = new DAOAppTheme();
        daoAppTheme1.setAppColor("#FFFFC107");
        mAppTheme1.add(daoAppTheme1);
        DAOAppTheme daoAppTheme2 = new DAOAppTheme();
        daoAppTheme2.setAppColor("#ff6000");
        mAppTheme1.add(daoAppTheme2);
        DAOAppTheme daoAppTheme3 = new DAOAppTheme();
        daoAppTheme3.setAppColor("#0090ff");
        mAppTheme1.add(daoAppTheme3);
        DAOAppTheme daoAppTheme4 = new DAOAppTheme();
        daoAppTheme4.setAppColor("#FF0080");
        mAppTheme1.add(daoAppTheme4);
    }


    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
        if (responseType.equalsIgnoreCase(AppConstants.GETLANGUAGELIST)) {
            LanguageListResponse response = (LanguageListResponse) myRes;
            languageList = response.getData();

            if (PreferenceStorage.getKey(AppConstants.MY_LANGUAGE) != null) {
                for (int i = 0; i < languageList.size(); i++) {
                    if (PreferenceStorage.getKey(AppConstants.MY_LANGUAGE)
                            .equalsIgnoreCase(languageList.get(i).getLanguageValue())) {
                        tvLanguage.setText(languageList.get(i).getLanguage());
                    }
                }
            }

        } else if (responseType.equalsIgnoreCase(AppConstants.GETLANGUAGEAPPDATA)) {
            PreferenceStorage.setKey(AppConstants.MY_LANGUAGE, lang);
            AppUtils.setLangInPref((LanguageResponse) myRes);
            setLocale(lang);
        } else if (responseType.equalsIgnoreCase(AppConstants.PROFILE_DATA)) {
            ProgressDlg.dismissProgressDialog();
            DAOProviderProfile profileData = (DAOProviderProfile) myRes;
            DAOProviderProfile.StripeDetails stripeDetails = profileData.getData().getStripeDetails();
            if (stripeDetails != null) {
                PreferenceStorage.setKey(AppConstants.stripe_Acc_name, stripeDetails.getAccountHolderName());
                PreferenceStorage.setKey(AppConstants.stripe_Acc_num, stripeDetails.getAccountNumber());
                PreferenceStorage.setKey(AppConstants.stripe_Iban, stripeDetails.getAccountIban());
                PreferenceStorage.setKey(AppConstants.stripe_bank_name, stripeDetails.getBankName());
                PreferenceStorage.setKey(AppConstants.stripe_bank_address, stripeDetails.getBankAddress());
                PreferenceStorage.setKey(AppConstants.stripe_sort_code, stripeDetails.getSortCode());
                PreferenceStorage.setKey(AppConstants.stripe_routing_number, stripeDetails.getRoutingNumber());
                PreferenceStorage.setKey(AppConstants.stripe_account_ifsc, stripeDetails.getAccountIfsc());
            }

        }
    }


    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    private void showLanguageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View titleView = inflater.inflate(R.layout.list_custom_alert_dialog_tiltle, null);
        builder.setCustomTitle(titleView).setTitle(AppUtils.cleanLangStr(getActivity(),
                settingScreenList.getLblChooseLang().getName(), R.string.txt_choose_language));
        builder.setAdapter(new LanguageAdapter(getActivity(), R.layout.list_item_language, languageList),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        lang = languageList.get(item).getLanguageValue();
                        dialog.dismiss();
                        getAppLanguageData(lang);

                        //   setLocale(lang);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getAppLanguageData(String languageCode) {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            String token = AppConstants.DEFAULTTOKEN;
            if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
            }
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<LanguageResponse> classificationCall = apiService.getAppLanguageData(token,
                        languageCode);
                RetrofitHandler.executeRetrofit(getActivity(), classificationCall,
                        AppConstants.GETLANGUAGEAPPDATA, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }
        } else {
            AppUtils.showToast(getActivity(), getString(R.string.txt_enable_internet));
        }
    }

    private void getLocaleData() {
        try {
            String settingsStr = PreferenceStorage.getKey(CommonLangModel.SettingsScreen);
            settingScreenList = new Gson().fromJson(settingsStr, LanguageResponse.Data.Language.SettingsScreen.class);
            String commonStr = PreferenceStorage.getKey(CommonLangModel.CommonString);
            commonStringsList = new Gson().fromJson(commonStr, LanguageResponse.Data.Language.CommonStrings.class);

        } catch (Exception e) {
            settingScreenList = new LanguageResponse().new Data().new Language().new SettingsScreen();
            commonStringsList = new LanguageResponse().new Data().new Language().new CommonStrings();
        }
    }

    private void getProviderProfileData() {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<DAOProviderProfile> classificationCall = apiService.getProfileData(PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.MY_LANGUAGE));
            RetrofitHandler.executeRetrofit(getActivity(), classificationCall, AppConstants.PROFILE_DATA, this, false);
        } else {
            AppUtils.showToast(getActivity(), AppUtils.cleanLangStr(getActivity(),
                    getString(R.string.txt_enable_internet), R.string.txt_enable_internet));
        }
    }

}
