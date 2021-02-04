package com.dreamguys.truelysell.fragments.phase3;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.HomeActivity;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.SearchServicesActivity;
import com.dreamguys.truelysell.ViewAllServicesActivity;
import com.dreamguys.truelysell.adapters.HomeCategoryAdapter;
import com.dreamguys.truelysell.adapters.HomeNewServicesAdapter;
import com.dreamguys.truelysell.adapters.HomePopularServicesAdapter;
import com.dreamguys.truelysell.adapters.ServiceCategoriesAdapter;
import com.dreamguys.truelysell.adapters.ServiceSubCategoriesAdapter;
import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.Country;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOGenerateOTP;
import com.dreamguys.truelysell.datamodel.Phase3.DAOLoginProfessional;
import com.dreamguys.truelysell.datamodel.Phase3.DAOServiceSubCategories;
import com.dreamguys.truelysell.datamodel.Phase3.GETHomeList;
import com.dreamguys.truelysell.datamodel.Phase3.LoginTypeResponse;
import com.dreamguys.truelysell.datamodel.Phase3.ServiceCategories;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.google.gson.Gson;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class HomeFragment extends Fragment implements RetrofitHandler.RetrofitResHandler {


    @BindView(R.id.tv_appname)
    TextView tvAppname;
    @BindView(R.id.tv_app_service)
    TextView tvAppService;
    @BindView(R.id.rv_popular_services)
    RecyclerView rvPopularServices;
    @BindView(R.id.rv_new_services)
    RecyclerView rvNewServices;
    @BindView(R.id.rv_home_categories)
    RecyclerView rvHomeCategories;
    Unbinder unbinder;
    HomeCategoryAdapter homeCategoryAdapter;
    HomePopularServicesAdapter homePopularServicesAdapter;
    HomeNewServicesAdapter homeNewServicesAdapter;
    LinearLayoutManager linearLayoutManager;
    ApiInterface apiInterface;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_userlogin)
    ImageView ivUserlogin;
    @BindView(R.id.tv_no_categories)
    TextView tvNoCategories;
    @BindView(R.id.tv_no_popular_services)
    TextView tvNoPopularServices;
    @BindView(R.id.tv_no_new_services)
    TextView tvNoNewServices;
    @BindView(R.id.iv_popular_services)
    ImageView ivPopularServices;
    @BindView(R.id.iv_new_services)
    ImageView ivNewServices;
    @BindView(R.id.tv_viewall_popular)
    TextView tvViewallPopular;
    @BindView(R.id.tv_viewall_new)
    TextView tvViewallNew;
    @BindView(R.id.ll_categories)
    LinearLayout llCategories;
    @BindView(R.id.txt_popular_services)
    TextView txtPopularServices;
    @BindView(R.id.txt_newly_added_services)
    TextView txtNewlyAddedServices;
    @BindView(R.id.txt_enable_location_service)
    TextView txtEnableLocationService;
    private BottomSheetDialog attachChooser;
    private TextView tvProfessional;
    private TextView serviceProvide, txtSubCategory;


    ImageView ivExitDialog;
    LinearLayout llDialog, llCategory, llSubcategory, llRegister, llName, llEmail, ll_referral;
    FrameLayout flOtpVerification;
    TextView tvCatetory, tvSubCatetory, tvResendCode, tvMobileNo;
    Button btnGetStarted, btnBack, btnContinue, btnRegister, btnRegisterBack, btnVerify, btnLogin;
    EditText etName, etEmailAddress, etMobileNumber;
    EditText etCountryCode, et_refferalCode;
    CheckBox cbExistingUser;
    ServiceCategories serviceCategories;
    DAOServiceSubCategories daoServiceSubCategories;
    ServiceCategoriesAdapter serviceCategoriesAdapter;
    ServiceSubCategoriesAdapter serviceSubCategoriesAdapter;
    private String catID = "", subCatID = "", userType = "";
    private AlertDialog mCountryDialog;
    private ListView vCountryList;
    SearchView searchView;
    private CountryAdapter aCountryAdapter;
    ListAdapter adapter;
    HashMap<String, String> postProviderSignup = new HashMap<>();
    HashMap<String, String> postGenerateOtp = new HashMap<>();
    HomeActivity mActivity;
    public boolean isResend;
    private boolean isLogin;
    public JSONArray mCountryList;
    LanguageResponse.Data.Language.RegisterScreen registerScreenList;
    LanguageResponse.Data.Language.CommonStrings commonStringsList;
    LanguageResponse.Data.Language.HomeScreen homeStringsList;
    LanguageResponse.Data.Language.EmailLogin emailStringsList;
    private LinearLayout ll_password, ll_mobileLayout;
    private EditText et_password;
    private TextView tv_forgotPassword;
    AlertDialog dialog1;

    public HomeFragment(HomeActivity homeActivity) {
        this.mActivity = homeActivity;
    }

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, mView);

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivPopularServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                ivNewServices.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getSecondaryAppTheme(getActivity())));
                tvViewallPopular.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvViewallNew.setCompoundDrawableTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                tvViewallNew.setTextColor(AppUtils.getPrimaryAppTheme(getActivity()));
                tvViewallPopular.setTextColor(AppUtils.getPrimaryAppTheme(getActivity()));
            }
        }

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                new int[]{AppUtils.getPrimaryAppTheme(getActivity()), AppUtils.getSecondaryAppTheme(getActivity())});
        gd.setCornerRadius(0f);

        llCategories.setBackground(gd);

        try {
            mCountryList = new JSONArray(AppUtils.readEncodedJsonString(getActivity()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
            ivUserlogin.setVisibility(View.GONE);
        }

        if (mActivity != null)
            mActivity.marshallMallowPermission();

        if (PreferenceStorage.getKey(AppConstants.MY_LATITUDE) != null) {
            getHomeList();
        }

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!v.getText().toString().isEmpty()) {
                        Intent intent = new Intent(getActivity(), SearchServicesActivity.class);
                        intent.putExtra(AppConstants.SEARCHTEXT, v.getText().toString().trim());
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });
        getLocaleData();

        String appService = "<font color='#FFFFFF'>" + AppUtils.cleanLangStr(getActivity(),
                commonStringsList.getLblWorldsLargest().getName(), R.string.txt_worlds_largest) + "</font>" + "<font color='" +
                AppUtils.getSecondaryAppTheme(getActivity()) + "'>" + AppUtils.cleanLangStr(getActivity(),
                commonStringsList.getLblMarketPlace().getName(), R.string.txt_marketplace) + "</font>";

        String appName = "<font color='" + AppUtils.getSecondaryAppTheme(getActivity()) + "'>TRUELY</font>" +
                "<font color='#FFFFFF'>SELL</font>";
        tvAppService.setText(Html.fromHtml(appService));
        tvAppname.setText(Html.fromHtml(appName));
        etSearch.setHint(AppUtils.cleanLangStr(getActivity(),
                homeStringsList.getTxtFldSearch().getName(), R.string.txt_what_are_you_looking_for));
        txtPopularServices.setText(AppUtils.cleanLangStr(getActivity(),
                commonStringsList.getLblPopularService().getName(), R.string.txt_popular_services));
        txtNewlyAddedServices.setText(AppUtils.cleanLangStr(getActivity(),
                commonStringsList.getLblNewlyAddedService().getName(), R.string.newly_added_services));
        tvViewallPopular.setText(AppUtils.cleanLangStr(getActivity(),
                commonStringsList.getLblViewAll().getName(), R.string.text_view_all));
        tvViewallNew.setText(AppUtils.cleanLangStr(getActivity(),
                commonStringsList.getLblViewAll().getName(), R.string.text_view_all));
        tvNoCategories.setText(AppUtils.cleanLangStr(getActivity(),
                homeStringsList.getLblNoCategoriesFound().getName(), R.string.txt_no_categories_found));
        tvNoPopularServices.setText(AppUtils.cleanLangStr(getActivity(),
                homeStringsList.getLblNoPopularService().getName(), R.string.no_popular_services_found));
        tvNoNewServices.setText(AppUtils.cleanLangStr(getActivity(),
                homeStringsList.getLblNoNewlyAddedService().getName(), R.string.no_newly_added_services_found));
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_userlogin, R.id.tv_viewall_popular, R.id.tv_viewall_new})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_userlogin:
                getCategoryList();
                break;
            case R.id.tv_viewall_popular:
                Intent callViewAllServicesAct = new Intent(getActivity(), ViewAllServicesActivity.class);
                callViewAllServicesAct.putExtra(AppConstants.VIEWALLTYPE, "1");
                startActivity(callViewAllServicesAct);
                break;
            case R.id.tv_viewall_new:
                Intent callViewAllServicesAct1 = new Intent(getActivity(), ViewAllServicesActivity.class);
                callViewAllServicesAct1.putExtra(AppConstants.VIEWALLTYPE, "2");
                startActivity(callViewAllServicesAct1);
                break;
        }
    }

    public void getHomeList() {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                String token = AppConstants.DEFAULTTOKEN;
                if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                    token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
                }
                Call<GETHomeList> classificationCall = apiService.getHomeList(token, PreferenceStorage.getKey(AppConstants.MY_LATITUDE), PreferenceStorage.getKey(AppConstants.MY_LONGITUDE));
                RetrofitHandler.executeRetrofit(getActivity(), classificationCall, AppConstants.HOMELIST, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(getActivity(), getString(R.string.txt_enable_internet));
        }
    }


    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {

        switch (responseType) {
            case AppConstants.HOMELIST:
                GETHomeList getHomeList = (GETHomeList) myRes;

                if (getHomeList.getData() != null) {
                    //Categories List
                    if (getHomeList.getData().getCategoryList() != null) {
                        if (getHomeList.getData().getCategoryList().size() > 0) {
                            rvHomeCategories.setVisibility(View.VISIBLE);
                            tvNoCategories.setVisibility(View.GONE);
                            linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            homeCategoryAdapter = new HomeCategoryAdapter(getContext(),
                                    getHomeList.getData().getCategoryList(), commonStringsList);
                            rvHomeCategories.setLayoutManager(linearLayoutManager);
                            rvHomeCategories.setAdapter(homeCategoryAdapter);
                        } else {
                            rvHomeCategories.setVisibility(View.GONE);
                            tvNoCategories.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rvHomeCategories.setVisibility(View.GONE);
                        tvNoCategories.setVisibility(View.VISIBLE);
                    }

                    //Popular Services
                    if (getHomeList.getData().getServiceList() != null) {
                        if (getHomeList.getData().getServiceList().size() > 0) {
                            rvPopularServices.setVisibility(View.VISIBLE);
                            tvNoPopularServices.setVisibility(View.GONE);
                            linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            homePopularServicesAdapter = new HomePopularServicesAdapter(getContext(), getHomeList.getData().getServiceList());
                            rvPopularServices.setLayoutManager(linearLayoutManager);
                            rvPopularServices.setAdapter(homePopularServicesAdapter);
                        } else {
                            rvPopularServices.setVisibility(View.GONE);
                            tvNoPopularServices.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rvPopularServices.setVisibility(View.GONE);
                        tvNoPopularServices.setVisibility(View.VISIBLE);
                    }

                    //New Services
                    if (getHomeList.getData().getNew_services() != null) {
                        if (getHomeList.getData().getNew_services().size() > 0) {
                            rvNewServices.setVisibility(View.VISIBLE);
                            tvNoNewServices.setVisibility(View.GONE);
                            linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            homeNewServicesAdapter = new HomeNewServicesAdapter(getContext(), getHomeList.getData().getNew_services());
                            rvNewServices.setLayoutManager(linearLayoutManager);
                            rvNewServices.setAdapter(homeNewServicesAdapter);
                        } else {
                            rvNewServices.setVisibility(View.GONE);
                            tvNoNewServices.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    rvHomeCategories.setVisibility(View.GONE);
                    tvNoCategories.setVisibility(View.VISIBLE);
                    rvPopularServices.setVisibility(View.GONE);
                    tvNoPopularServices.setVisibility(View.VISIBLE);
                    rvNewServices.setVisibility(View.GONE);
                    tvNoNewServices.setVisibility(View.VISIBLE);
                }
                break;
            case AppConstants.SERVICECATEGORIES:
                serviceCategories = (ServiceCategories) myRes;
                showCustomAlertDialog();
                break;
            case AppConstants.SERVICESUBCATEGORIES:
                daoServiceSubCategories = (DAOServiceSubCategories) myRes;
                break;
            case AppConstants.MOBILEOTP:
                DAOGenerateOTP daoGenerateOTP = (DAOGenerateOTP) myRes;
//                userType = daoGenerateOTP.getData().getUsertype();
//                if (daoGenerateOTP.getData().getUsertype().equalsIgnoreCase("2")) {//2 New  1 Existing
//                    llRegister.setVisibility(View.GONE);
//                    flOtpVerification.setVisibility(View.VISIBLE);
//                    Toast.makeText(getActivity(), daoGenerateOTP.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
//                } else {
//                    cbExistingUser.setChecked(true);
//                    llName.setVisibility(View.VISIBLE);
//                    llEmail.setVisibility(View.VISIBLE);
//                }

                if (PreferenceStorage.getKey(AppConstants.LOGINTYPESTR).equalsIgnoreCase("email")) {
                    postProffessionalLogin("", "", "",
                            etEmailAddress.getText().toString().trim(),
                            et_password.getText().toString().trim());
                } else {
                    Toast.makeText(getActivity(), daoGenerateOTP.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    tvMobileNo.setText(AppUtils.cleanLangStr(getActivity(),
                            homeStringsList.getLbl_default_otp_msg().getName(), R.string.default_otp_msg));
                    if (isResend) {
                        isResend = false;
                    } else {
                        llRegister.setVisibility(View.GONE);
                        flOtpVerification.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case AppConstants.PROFESSIONALLOGIN:
                DAOLoginProfessional daoLoginProfessional = (DAOLoginProfessional) myRes;

                if (daoLoginProfessional.getData().getProviderDetails() != null) {
                    Toast.makeText(getActivity(), daoLoginProfessional.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    PreferenceStorage.setKey(AppConstants.PNAME, daoLoginProfessional.getData().getProviderDetails().getName());
                    PreferenceStorage.setKey(AppConstants.PEMAIL, daoLoginProfessional.getData().getProviderDetails().getEmail());
                    PreferenceStorage.setKey(AppConstants.PMOBILENO, daoLoginProfessional.getData().getProviderDetails().getMobileno());
                    PreferenceStorage.setKey(AppConstants.PCATEGORY, daoLoginProfessional.getData().getProviderDetails().getCategory());
                    PreferenceStorage.setKey(AppConstants.PSUBCATEGORY, daoLoginProfessional.getData().getProviderDetails().getSubcategory());
                    PreferenceStorage.setKey(AppConstants.PIMAGE, daoLoginProfessional.getData().getProviderDetails().getProfileImg());
                    PreferenceStorage.setKey(AppConstants.USER_TOKEN, daoLoginProfessional.getData().getProviderDetails().getToken());
                    PreferenceStorage.setKey(AppConstants.USER_TYPE, daoLoginProfessional.getData().getProviderDetails().getType());
                    PreferenceStorage.setKey(AppConstants.ISSUBSCRIBED, daoLoginProfessional.getData().getProviderDetails().getIs_subscribed());
                    PreferenceStorage.setKey(AppConstants.SHARECODE, daoLoginProfessional.getData().getProviderDetails().getShareCode());
                    PreferenceStorage.setKey(AppConstants.USER_ID, daoLoginProfessional.getData().getProviderDetails().getId());
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), daoLoginProfessional.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case AppConstants.LOGINTYPE:
                LoginTypeResponse loginTypeResponse = (LoginTypeResponse) myRes;
                if (loginTypeResponse.getData() != null) {
                    PreferenceStorage.setKey(AppConstants.LOGINTYPESTR, loginTypeResponse.getData().getLoginType());

                    if (PreferenceStorage.getKey(AppConstants.LOGINTYPESTR).equalsIgnoreCase("email")) {
                        if (isLogin) {
                            ll_password.setVisibility(View.VISIBLE);
                            llEmail.setVisibility(View.VISIBLE);
                            ll_mobileLayout.setVisibility(View.GONE);
                            tv_forgotPassword.setVisibility(View.VISIBLE);
                        } else {
                            ll_password.setVisibility(View.VISIBLE);
                            llEmail.setVisibility(View.VISIBLE);
                            ll_mobileLayout.setVisibility(View.VISIBLE);
                            tv_forgotPassword.setVisibility(View.GONE);
                        }

                    } else {
                        if (isLogin) {
                            ll_password.setVisibility(View.GONE);
                            llEmail.setVisibility(View.GONE);
                            tv_forgotPassword.setVisibility(View.GONE);
                        } else {
                            ll_password.setVisibility(View.GONE);
                            llEmail.setVisibility(View.VISIBLE);
                            tv_forgotPassword.setVisibility(View.GONE);
                        }
                        ll_mobileLayout.setVisibility(View.VISIBLE);

                    }

                }
                break;

            case AppConstants.FORGOTPASSWORD:
                BaseResponse response = (BaseResponse) myRes;
                Toast.makeText(getActivity(), response.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                if (response.getResponseHeader().getResponseCode().equalsIgnoreCase("200")) {
                    if (dialog1.isShowing())
                        dialog1.dismiss();

                }
                break;
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {
        ProgressDlg.dismissProgressDialog();
        switch (responseType) {
            case AppConstants.MOBILEOTP:
                DAOGenerateOTP daoGenerateOTP = (DAOGenerateOTP) myRes;
//                userType = daoGenerateOTP.getData().getUsertype();
                if (daoGenerateOTP.getResponseHeader().getResponseCode().equalsIgnoreCase("201")) {
                    cbExistingUser.setChecked(true);
                    llName.setVisibility(View.VISIBLE);
                    llEmail.setVisibility(View.VISIBLE);
                    ll_referral.setVisibility(View.VISIBLE);
                    cbExistingUser.setChecked(false);
                    cbExistingUser.setVisibility(View.VISIBLE);
                    isLogin = false;
                }
                break;
        }
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {
        ProgressDlg.dismissProgressDialog();
    }

    public void showCustomAlertDialog() {
        final OtpView otpView;
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_signup_choose_category, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);

        llDialog = view.findViewById(R.id.ll_dialog);
        llCategory = view.findViewById(R.id.ll_choose_category);
        llSubcategory = view.findViewById(R.id.ll_choose_subcategory);
        llRegister = view.findViewById(R.id.ll_register);
        llName = view.findViewById(R.id.ll_name);
        llEmail = view.findViewById(R.id.ll_email);
        ll_referral = view.findViewById(R.id.ll_referral);
        et_refferalCode = view.findViewById(R.id.et_refferalCode);
        flOtpVerification = view.findViewById(R.id.fl_otp_verification);
        ivExitDialog = view.findViewById(R.id.iv_exit);
        tvCatetory = view.findViewById(R.id.tv_category);
        tvSubCatetory = view.findViewById(R.id.tv_subcategory);
        btnGetStarted = view.findViewById(R.id.btn_get_started);
        btnLogin = view.findViewById(R.id.btn_login);
        btnBack = view.findViewById(R.id.btn_back);
        btnContinue = view.findViewById(R.id.btn_continue);
        btnRegister = view.findViewById(R.id.btn_register);
        btnRegisterBack = view.findViewById(R.id.btn_reg_back);
        etName = view.findViewById(R.id.et_name);
        etEmailAddress = view.findViewById(R.id.et_email_address);
        etMobileNumber = view.findViewById(R.id.et_mobile_no);
        etCountryCode = view.findViewById(R.id.et_country_code);
        cbExistingUser = view.findViewById(R.id.cb_existing_user);
        btnVerify = view.findViewById(R.id.btn_verify);
        tvResendCode = view.findViewById(R.id.tv_resendcode);
        tvMobileNo = view.findViewById(R.id.tv_mobile_no);
        tvProfessional = view.findViewById(R.id.tv_professional);
        serviceProvide = view.findViewById(R.id.txt_what_service_do_you_provide);
        txtSubCategory = view.findViewById(R.id.txt_choose_sub_category);
        otpView = view.findViewById(R.id.otp_view);
        ll_password = view.findViewById(R.id.ll_password);
        ll_mobileLayout = view.findViewById(R.id.ll_mobileLayout);
        et_password = view.findViewById(R.id.et_password);
        tv_forgotPassword = view.findViewById(R.id.tv_forgotPassword);

        if (AppUtils.isThemeChanged(getActivity())) {
            btnBack.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            btnContinue.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            btnGetStarted.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            btnLogin.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            btnRegister.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            btnRegisterBack.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            btnVerify.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            ivExitDialog.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            cbExistingUser.setButtonTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
        }

        try {
            tvProfessional.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getLblJoinAsProfessional().getName(), R.string.txt_join_as_professional));
            serviceProvide.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getLblServiceYouProvide().getName(), R.string.txt_what_service_do_you_provide));
            tvCatetory.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getLblSelectServiceHere().getName(), R.string.txt_select_your_service_here));
            btnLogin.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getLblLogin().getName(), R.string.txt_login));
            btnGetStarted.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getLblGetStarted().getName(), R.string.txt_get_started));
            txtSubCategory.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getLblChooseSubcategory().getName(), R.string.txt_choose_sub_category));
            tvSubCatetory.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getLblSelectSubcategory().getName(), R.string.txt_select_your_subcategory));
            btnBack.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getBtnPrevious().getName(), R.string.txt_previous));
            btnContinue.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getBtnNext().getName(), R.string.txt_next));
            etName.setHint(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getTxtFldName().getPlaceholder(), R.string.txt_name));
            etEmailAddress.setHint(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getTxtFldEmail().getPlaceholder(), R.string.email));
            etCountryCode.setHint(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getBtnCode().getPlaceholder(), R.string.txt_code));
            etMobileNumber.setHint(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getTxtFldMobileNum().getPlaceholder(), R.string.txt_mobile_number));
            et_refferalCode.setHint(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getTxtFldReferenceCode().getPlaceholder(), R.string.txt_refferal_code));
            cbExistingUser.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getLblAlreadyProfessional().getName(), R.string.already_a_professional));
            btnRegisterBack.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getBtnPrevious().getName(), R.string.txt_previous));
            btnRegister.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getLblRegister().getName(), R.string.txt_register));
            tvResendCode.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getBtn_resend_otp().getName(), R.string.tv_resendcode));
            TextView tvDidntOtp = view.findViewById(R.id.txt_didn_t_receive_otp);
            tvDidntOtp.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getLbl_dint_receive_otp().getName(), R.string.txt_didn_t_receive_otp));
            btnVerify.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getLbl_verify().getName(), R.string.txt_verify));
            tv_forgotPassword.setText(AppUtils.cleanLangStr(getActivity(),
                    emailStringsList.getBtn_forgot_password().getName(), R.string.txt_forgot_password));


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

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                // do Stuff
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(otpView.getWindowToken(), 0);
                postProffessionalLogin(etMobileNumber.getText().toString(), etCountryCode.getText().toString(),
                        otp, "", "");
                Log.d("onOtpCompleted=>", otp);
            }
        });

        tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isResend = true;
                if (PreferenceStorage.getKey(AppConstants.LOGINTYPESTR).equalsIgnoreCase("email")) {
                    getMobileOtp("", "",
                            etEmailAddress.getText().toString().trim(), "", "",
                            et_password.getText().toString().trim());
                } else {
                    getMobileOtp(etCountryCode.getText().toString().trim(), etMobileNumber.getText().toString().trim(), "", "", "", "");
                }
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otpView.getText().toString().isEmpty() || otpView.getText().toString().length() < 4) {
                    Toast.makeText(getActivity(), "Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(otpView.getWindowToken(), 0);
                    //Mani
                    postProffessionalLogin(etMobileNumber.getText().toString(), etCountryCode.getText().toString(), otpView.getText().toString(),
                            "", "");
                }

            }
        });

        cbExistingUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (PreferenceStorage.getKey(AppConstants.LOGINTYPESTR) != null && PreferenceStorage.getKey(AppConstants.LOGINTYPESTR).equalsIgnoreCase("email")) {
                        llEmail.setVisibility(View.VISIBLE);
                        ll_password.setVisibility(View.VISIBLE);
                        ll_mobileLayout.setVisibility(View.GONE);
                        tv_forgotPassword.setVisibility(View.VISIBLE);
                    } else {
                        llEmail.setVisibility(View.GONE);
                        ll_password.setVisibility(View.GONE);
                        ll_mobileLayout.setVisibility(View.VISIBLE);
                        tv_forgotPassword.setVisibility(View.GONE);
                    }
                    llName.setVisibility(View.GONE);
                    // llEmail.setVisibility(View.GONE);
                    ll_referral.setVisibility(View.GONE);
                    btnRegister.setText(AppUtils.cleanLangStr(getActivity(),
                            registerScreenList.getLblLogin().getName(), R.string.txt_login));
                    tvProfessional.setText(AppUtils.cleanLangStr(getActivity(),
                            registerScreenList.getLblLoginAsProfessional().getName(), R.string.login_as_professional));
                } else {
                    if (PreferenceStorage.getKey(AppConstants.LOGINTYPESTR) != null && PreferenceStorage.getKey(AppConstants.LOGINTYPESTR).equalsIgnoreCase("email")) {
                        ll_password.setVisibility(View.VISIBLE);
                        llEmail.setVisibility(View.VISIBLE);
                        ll_mobileLayout.setVisibility(View.VISIBLE);
                        tv_forgotPassword.setVisibility(View.GONE);
                    } else {
                        ll_password.setVisibility(View.GONE);
                        llEmail.setVisibility(View.VISIBLE);
                        ll_mobileLayout.setVisibility(View.VISIBLE);
                        tv_forgotPassword.setVisibility(View.GONE);
                    }
                    llName.setVisibility(View.VISIBLE);
                    // llEmail.setVisibility(View.VISIBLE);
                    ll_referral.setVisibility(View.VISIBLE);
                    btnRegister.setText(AppUtils.cleanLangStr(getActivity(),
                            registerScreenList.getLblRegister().getName(), R.string.txt_register));
                    tvProfessional.setText(AppUtils.cleanLangStr(getActivity(),
                            registerScreenList.getLblJoinAsProfessional().getName(), R.string.txt_join_as_professional));
                }
            }
        });

        etCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCountrycodeDialog();
            }
        });

        tvCatetory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem(serviceCategories.getData().getCategoryList(), null, tvCatetory, "1"); //type 1 = category
//                selectItem(serviceCategories.getData().getCategoryList());
            }
        });

        tvSubCatetory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceCategoriesAdapter != null && daoServiceSubCategories.getData().getSubcategoryList() != null) {
                    selectItem(null, daoServiceSubCategories.getData().getSubcategoryList(), tvSubCatetory, "2"); //type 2 = subcategory
                } else {
                    Toast.makeText(getActivity(), daoServiceSubCategories.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llCategory.setVisibility(View.GONE);
                llRegister.setVisibility(View.VISIBLE);
                cbExistingUser.setChecked(true);
                cbExistingUser.setVisibility(View.GONE);
                isLogin = true;
                getLoginType();
            }
        });


        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceCategoriesAdapter != null && !serviceCategoriesAdapter.cat_id.isEmpty()) {
                    isLogin = false;
                    llCategory.setVisibility(View.GONE);
                    llSubcategory.setVisibility(View.VISIBLE);
                    getSubCategoryList(serviceCategoriesAdapter.cat_id);
                } else {
                    Toast.makeText(getActivity(),
                            AppUtils.cleanLangStr(getActivity(),
                                    registerScreenList.getBtnSelectCategory().getValidation2(),
                                    R.string.txt_enter_valid_mobile_number), Toast.LENGTH_SHORT).show();
                }


            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSubCatetory.setText(R.string.txt_select_your_subcategory);
                serviceSubCategoriesAdapter = null;
                llCategory.setVisibility(View.VISIBLE);
                llSubcategory.setVisibility(View.GONE);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceSubCategoriesAdapter != null && !serviceSubCategoriesAdapter.sub_cat_id.isEmpty()) {
                    getLoginType();
                    llSubcategory.setVisibility(View.GONE);
                    llRegister.setVisibility(View.VISIBLE);
                    cbExistingUser.setChecked(false);
                    cbExistingUser.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Choose any one Subcategory", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegisterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) {
                    etCountryCode.setText("");
                    etEmailAddress.setText("");
                    etName.setText("");
                    etMobileNumber.setText("");
                    llCategory.setVisibility(View.VISIBLE);
                    llRegister.setVisibility(View.GONE);
                    tvProfessional.setText(AppUtils.cleanLangStr(getActivity(),
                            registerScreenList.getLblJoinAsProfessional().getName(),
                            R.string.txt_join_as_professional));
                } else {
                    llSubcategory.setVisibility(View.VISIBLE);
                    llRegister.setVisibility(View.GONE);
                    tvProfessional.setText(AppUtils.cleanLangStr(getActivity(),
                            registerScreenList.getLblJoinAsProfessional().getName(),
                            R.string.txt_join_as_professional));
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showMobileOTPDialog();
                if (cbExistingUser.isChecked()) {
                    if (PreferenceStorage.getKey(AppConstants.LOGINTYPESTR).equalsIgnoreCase("email")) {
                        if (etEmailAddress.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                                    registerScreenList.getTxtFldEmail().getValidation1(), R.string.txt_enter_email), Toast.LENGTH_SHORT).show();
                        } else if (!AppUtils.isValidEmail(etEmailAddress.getText().toString())) {
                            Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                                    registerScreenList.getTxtFldEmail().getValidation2(), R.string.txt_enter_valid_email), Toast.LENGTH_SHORT).show();
                        } else if (et_password.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                                    registerScreenList.getTxtFldMobileNum().getValidation2(),
                                    R.string.err_password_empty), Toast.LENGTH_SHORT).show();
                        } else {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(et_password.getWindowToken(), 0);
                            getMobileOtp("",
                                    "",
                                    etEmailAddress.getText().toString().trim(), "", "",
                                    et_password.getText().toString().trim());
                        }
                    } else {
                        if (etCountryCode.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                                    registerScreenList.getBtnCode().getValidation1(), R.string.txt_select_country_code), Toast.LENGTH_SHORT).show();
                        } else if (etMobileNumber.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                                    registerScreenList.getTxtFldMobileNum().getValidation1(), R.string.txt_enter_mobile_number), Toast.LENGTH_SHORT).show();
                        } else if (etMobileNumber.getText().toString().length() < 9 || etMobileNumber.getText().toString().length() > 15) {
                            Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                                    registerScreenList.getTxtFldMobileNum().getValidation2(),
                                    R.string.txt_enter_valid_mobile_number), Toast.LENGTH_SHORT).show();
                        } else {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etMobileNumber.getWindowToken(), 0);
                            getMobileOtp(etCountryCode.getText().toString().trim(),
                                    etMobileNumber.getText().toString().trim(), "", "", "", "");
                        }
                    }

                } else {
                    String pass = "";

                    if (PreferenceStorage.getKey(AppConstants.LOGINTYPESTR).equalsIgnoreCase("email")) {
                        if (et_password.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                                    registerScreenList.getTxtFldMobileNum().getValidation2(),
                                    R.string.err_password_empty), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            pass = et_password.getText().toString();
                        }
                    }

                    if (etName.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(),
                                AppUtils.cleanLangStr(getActivity(),
                                        registerScreenList.getTxtFldName().getValidation1(), R.string.txt_enter_name), Toast.LENGTH_SHORT).show();
                    } else if (etEmailAddress.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                                registerScreenList.getTxtFldEmail().getValidation1(), R.string.txt_enter_email), Toast.LENGTH_SHORT).show();
                    } else if (!AppUtils.isValidEmail(etEmailAddress.getText().toString())) {
                        Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                                registerScreenList.getTxtFldEmail().getValidation2(), R.string.txt_enter_valid_email), Toast.LENGTH_SHORT).show();
                    } else if (etCountryCode.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                                registerScreenList.getBtnCode().getValidation1(), R.string.txt_select_country_code), Toast.LENGTH_SHORT).show();
                    } else if (etMobileNumber.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                                registerScreenList.getTxtFldMobileNum().getValidation1(), R.string.txt_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    } else if (etMobileNumber.getText().toString().length() < 9 || etMobileNumber.getText().toString().length() > 15) {
                        Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                                registerScreenList.getTxtFldMobileNum().getValidation2(),
                                R.string.txt_enter_valid_mobile_number), Toast.LENGTH_SHORT).show();
                    } else {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etMobileNumber.getWindowToken(), 0);
                        getMobileOtp(etCountryCode.getText().toString().trim(),
                                etMobileNumber.getText().toString().trim(), etEmailAddress.getText().toString().trim(),
                                etName.getText().toString(), et_refferalCode.getText().toString(), pass);
                    }
                }
            }
        });


        ivExitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceCategoriesAdapter = null;
                serviceSubCategoriesAdapter = null;
                dialog.dismiss();
            }
        });

        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showForgotPasswordDialog();
            }
        });
    }

    private void showForgotPasswordDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_forgot_password, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);

        dialog1 = alertDialogBuilder.create();
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.show();
        dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog1.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        EditText etEmailAddress = view.findViewById(R.id.et_email_address);
        Button btn_submit = view.findViewById(R.id.btn_submit);
        ImageView iv_exit = view.findViewById(R.id.iv_exit);
        TextView tv_forgotPasswordTitle = view.findViewById(R.id.tv_forgotPasswordTitle);

        if (AppUtils.isThemeChanged(getActivity())) {
            btn_submit.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
        }
        etEmailAddress.setHint(AppUtils.cleanLangStr(getActivity(),
                registerScreenList.getTxtFldEmail().getPlaceholder(), R.string.email));
        tv_forgotPasswordTitle.setText(AppUtils.cleanLangStr(getActivity(),
                emailStringsList.getBtn_forgot_password().getName(), R.string.txt_forgot_password));
        btn_submit.setText(AppUtils.cleanLangStr(getActivity(),
                emailStringsList.getBtn_submit().getName(), R.string.txt_submit));
        iv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmailAddress.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                            registerScreenList.getTxtFldEmail().getValidation1(), R.string.txt_enter_email), Toast.LENGTH_SHORT).show();
                } else if (!AppUtils.isValidEmail(etEmailAddress.getText().toString())) {
                    Toast.makeText(getActivity(), AppUtils.cleanLangStr(getActivity(),
                            registerScreenList.getTxtFldEmail().getValidation2(), R.string.txt_enter_valid_email), Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etMobileNumber.getWindowToken(), 0);
                    callForgotPassword(etEmailAddress.getText().toString().trim());
                }
            }
        });
    }

    private void selectItem(List<ServiceCategories.CategoryList> categoryList, List<DAOServiceSubCategories.SubcategoryList> subcategoryList, TextView tvCatetory, String type) {
        attachChooser = new BottomSheetDialog(getActivity(), R.style.AppBottomSheetDialogTheme);
        attachChooser.setContentView((getActivity()).getLayoutInflater().inflate(R.layout.popup_categories,
                new LinearLayout(getActivity())));
//        attachChooser.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        attachChooser.show();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        TextView tvTitle = attachChooser.findViewById(R.id.tv_title);
        RecyclerView rvCategoryList = attachChooser.findViewById(R.id.rv_categorylist);
        rvCategoryList.setLayoutManager(linearLayoutManager);

        if (type.equalsIgnoreCase("1")) {
            tvTitle.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getBtnSelectCategory().getName(),
                    R.string.txt_choose_category));
            serviceCategoriesAdapter = new ServiceCategoriesAdapter(getActivity(), categoryList, tvCatetory, attachChooser, catID);
            rvCategoryList.setAdapter(serviceCategoriesAdapter);
        } else {
            tvTitle.setText(AppUtils.cleanLangStr(getActivity(),
                    registerScreenList.getLblChooseSubcategory().getName(),
                    R.string.txt_choose_sub_category));
            serviceSubCategoriesAdapter = new ServiceSubCategoriesAdapter(getActivity(), subcategoryList, tvCatetory, attachChooser, subCatID);
            rvCategoryList.setAdapter(serviceSubCategoriesAdapter);
        }
    }


    public void getCategoryList() {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ServiceCategories> getCategories = apiInterface.getServiceCategories(AppConstants.DEFAULTTOKEN);
            RetrofitHandler.executeRetrofit(getActivity(), getCategories, AppConstants.SERVICECATEGORIES, this, false);
        }
    }


    public void getSubCategoryList(String subCatID) {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<DAOServiceSubCategories> getCategories = apiInterface.postServiceSubCategory(subCatID, AppConstants.DEFAULTTOKEN);
            RetrofitHandler.executeRetrofit(getActivity(), getCategories, AppConstants.SERVICESUBCATEGORIES, this, false);
        }
    }


    public void showMobileOTPDialog() {
        final OtpView otpView;
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_otp_verification, null);
        ivExitDialog = view.findViewById(R.id.iv_exit);

        otpView = view.findViewById(R.id.otp_view);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                // do Stuff
                Log.d("onOtpCompleted=>", otp);
            }
        });

        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.60);

        final AlertDialog dialog = alertDialogBuilder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ivExitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceCategoriesAdapter = null;
                serviceSubCategoriesAdapter = null;
                dialog.dismiss();
            }
        });
    }

    public class CountryAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater mInflater;
        private final ArrayList<Country> mCountry;

        public CountryAdapter(Context mContext, ArrayList<Country> mCountry) {
            this.mContext = mContext;
            this.mCountry = mCountry;
            mInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mCountry.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.adapter_labels, null);
                mHolder.mName = (TextView) convertView.findViewById(R.id.inputName);
                mHolder.mCountryCode = (TextView) convertView.findViewById(R.id.inputCode);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            try {
                mHolder.mName.setText(mCountry.get(position).getName());
                mHolder.mCountryCode.setText(mCountry.get(position).getCode());


//                if (mCountry.getJSONObject(position).getJSONArray("callingCodes").length() != 0 && !mCountry.getJSONObject(position).getJSONArray("callingCodes").get(0).toString().isEmpty()) {
//                    String name = mCountry.getJSONObject(position).getString("callingCodes").replace("[", "").replace("]", "").replace("\"", "");
//                    mHolder.mName.setText(mCountry.getJSONObject(position).getString("name"));
//                    mHolder.mCountryCode.setText(mCountry.getJSONObject(position).getJSONArray("callingCodes").get(0).toString());
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        etCountryCode.setText(mCountry.get(position).getCode());
//                        Config.countryAlphaCode = mCountry.getJSONObject(position).getString("alpha2Code").replace("[", "").replace("]", "").replace("\"", "");
//                        inputMobile_no.addTextChangedListener(new PhoneNumberFormattingTextWatcher(Config.countryAlphaCode));
                        mCountryDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return convertView;
        }

        private class ViewHolder {
            TextView mName, mCountryCode;
        }
    }

    public void showCountrycodeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mShowAddProjectView = getLayoutInflater().inflate(R.layout.dialog_country, null);
        mBuilder.setView(mShowAddProjectView);
        vCountryList = (ListView) mShowAddProjectView.findViewById(R.id.inputcountrylists);
        searchView = (SearchView) mShowAddProjectView.findViewById(R.id.searchview);
        TextView tvStatusType = mShowAddProjectView.findViewById(R.id.tv_statustype);
//        aCountryAdapter = new CountryAdapter(getActivity(), AppUtils.getCountries(getActivity()));
        adapter = new ListAdapter(getActivity(), AppUtils.getCountries(getActivity()));
        vCountryList.setAdapter(adapter);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.70);
        tvStatusType.setText(AppUtils.cleanLangStr(getActivity(),
                registerScreenList.getLblSelectCountry().getName(),
                R.string.txt_choose_country_code));
        mCountryDialog = mBuilder.create();
        mCountryDialog.show();
        mCountryDialog.getWindow().setLayout(width, height);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

//                if (aCountryAdapter.mCountry.contains(query)) {
//                    adapter.getFilter().filter(query);
//                } else {
//                    Toast.makeText(getActivity(), "No Match found", Toast.LENGTH_LONG).show();
//                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    public void getMobileOtp(String country_code, String mobile_no, String email,
                             String name, String referralCode, String password) {
        if (AppUtils.isNetworkAvailable(getActivity())) {

            ProgressDlg.showProgressDialog(getActivity(), null, null);
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            postGenerateOtp = new HashMap<>();
            postGenerateOtp.put("country_code", country_code);
            postGenerateOtp.put("name", name);
            postGenerateOtp.put("mobileno", mobile_no);
            postGenerateOtp.put("email", email);
            if (serviceCategoriesAdapter != null && serviceSubCategoriesAdapter != null) {
                postGenerateOtp.put("category", serviceCategoriesAdapter.cat_id);
                postGenerateOtp.put("subcategory", serviceSubCategoriesAdapter.sub_cat_id);
            }
            postGenerateOtp.put("device_type", AppConstants.deviceType);
            if (PreferenceStorage.getKey(AppConstants.refreshedToken) != null) {
                postGenerateOtp.put("device_id", PreferenceStorage.getKey(AppConstants.refreshedToken));
            }
            if (!referralCode.isEmpty())
                postGenerateOtp.put("get_code", referralCode);
            else
                postGenerateOtp.put("get_code", "");
            postGenerateOtp.put("password", password);
            Call<DAOGenerateOTP> getCategories = apiInterface.postMobileOTP(postGenerateOtp, AppConstants.DEFAULTTOKEN);
            RetrofitHandler.executeRetrofit(getActivity(), getCategories, AppConstants.MOBILEOTP, this, false);
        }
    }

    public void postProffessionalLogin(String mobile_no, String country_code, String otp, String email, String password) {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            postProviderSignup.put("mobileno", mobile_no);
            postProviderSignup.put("country_code", country_code);
            postProviderSignup.put("otp", otp);
            postProviderSignup.put("email", email);
            postProviderSignup.put("password", password);
            Call<DAOLoginProfessional> getCategories = apiInterface.postProfessionalLogin(postProviderSignup, AppConstants.DEFAULTTOKEN);
            RetrofitHandler.executeRetrofit(getActivity(), getCategories, AppConstants.PROFESSIONALLOGIN, this, false);
        }
    }

    public class ListAdapter extends BaseAdapter implements Filterable {

        List<Country> mData;
        List<Country> mStringFilterList;
        ValueFilter valueFilter;
        private LayoutInflater inflater;
        Context mContext;

        public ListAdapter(Context mContext, List<Country> cancel_type) {
            this.mContext = mContext;
            mData = cancel_type;
            mStringFilterList = cancel_type;
            inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Country getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {

            ViewHolder mHolder;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.adapter_labels, null);
                mHolder.mName = (TextView) convertView.findViewById(R.id.inputName);
                mHolder.mCountryCode = (TextView) convertView.findViewById(R.id.inputCode);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }
            try {
                mHolder.mName.setText(mData.get(position).getName());
                mHolder.mCountryCode.setText(mData.get(position).getDialCode());


//                if (mCountry.getJSONObject(position).getJSONArray("callingCodes").length() != 0 && !mCountry.getJSONObject(position).getJSONArray("callingCodes").get(0).toString().isEmpty()) {
//                    String name = mCountry.getJSONObject(position).getString("callingCodes").replace("[", "").replace("]", "").replace("\"", "");
//                    mHolder.mName.setText(mCountry.getJSONObject(position).getString("name"));
//                    mHolder.mCountryCode.setText(mCountry.getJSONObject(position).getJSONArray("callingCodes").get(0).toString());
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        etCountryCode.setText(mData.get(position).getDialCode());
//                        Config.countryAlphaCode = mCountry.getJSONObject(position).getString("alpha2Code").replace("[", "").replace("]", "").replace("\"", "");
//                        inputMobile_no.addTextChangedListener(new PhoneNumberFormattingTextWatcher(Config.countryAlphaCode));
                        mCountryDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (valueFilter == null) {
                valueFilter = new ValueFilter();
            }
            return valueFilter;
        }

        private class ValueFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null && constraint.length() > 0) {
                    List<Country> filterList = new ArrayList<>();
                    for (int i = 0; i < mStringFilterList.size(); i++) {
                        if ((mStringFilterList.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase()) ||
                                (mStringFilterList.get(i).getDialCode()).contains(constraint.toString())) {
                            filterList.add(mStringFilterList.get(i));
                        }
                    }
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    results.count = mStringFilterList.size();
                    results.values = mStringFilterList;
                }
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                mData = (List<Country>) results.values;
                notifyDataSetChanged();
            }

        }

        private class ViewHolder {
            TextView mName, mCountryCode;
        }

    }

    private void getLocaleData() {
        try {
            String registerDataStr = PreferenceStorage.getKey(CommonLangModel.RegisterScreen);
            registerScreenList = new Gson().fromJson(registerDataStr, LanguageResponse.Data.Language.RegisterScreen.class);
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.CommonString);
            commonStringsList = new Gson().fromJson(commonDataStr, LanguageResponse.Data.Language.CommonStrings.class);
            String homeDataStr = PreferenceStorage.getKey(CommonLangModel.HomeString);
            homeStringsList = new Gson().fromJson(homeDataStr, LanguageResponse.Data.Language.HomeScreen.class);
            String emailDataStr = PreferenceStorage.getKey(CommonLangModel.EMAILLOGIN);
            emailStringsList = new Gson().fromJson(emailDataStr, LanguageResponse.Data.Language.EmailLogin.class);
        } catch (Exception e) {
            registerScreenList = new LanguageResponse().new Data().new Language().new RegisterScreen();
            commonStringsList = new LanguageResponse().new Data().new Language().new CommonStrings();
            homeStringsList = new LanguageResponse().new Data().new Language().new HomeScreen();
            emailStringsList = new LanguageResponse().new Data().new Language().new EmailLogin();
        }
    }

    private void getLoginType() {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                String token = AppConstants.DEFAULTTOKEN;
                if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                    token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
                }
                Call<LoginTypeResponse> classificationCall = apiService.getLoginType(token);
                RetrofitHandler.executeRetrofit(getActivity(), classificationCall, AppConstants.LOGINTYPE, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(getActivity(), getString(R.string.txt_enable_internet));
        }
    }

    private void callForgotPassword(String email) {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            ProgressDlg.showProgressDialog(getActivity(), null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                String token = AppConstants.DEFAULTTOKEN;
                if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                    token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
                }
                Call<BaseResponse> classificationCall = apiService.callForgotPassword(token, email, "1");
                RetrofitHandler.executeRetrofit(getActivity(), classificationCall, AppConstants.FORGOTPASSWORD, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(getActivity(), getString(R.string.txt_enable_internet));
        }
    }

}
