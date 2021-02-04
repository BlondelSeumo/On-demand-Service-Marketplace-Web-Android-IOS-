package com.dreamguys.truelysell;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.adapters.DialogEditCategoryListAdapter;
import com.dreamguys.truelysell.adapters.DialogEditSubCategoryListAdapter;
import com.dreamguys.truelysell.datamodel.CurrencyListResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOProviderProfile;
import com.dreamguys.truelysell.datamodel.Phase3.DAOServiceSubCategories;
import com.dreamguys.truelysell.datamodel.Phase3.DAOUserProfile;
import com.dreamguys.truelysell.datamodel.Phase3.ServiceCategories;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.dreamguys.truelysell.viewwidgets.CircleImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MyProfileActivity extends BaseActivity implements RetrofitHandler.RetrofitResHandler {

    @BindView(R.id.btn_edit)
    Button btnEdit;
    @BindView(R.id.signup_header)
    ConstraintLayout signupHeader;
    @BindView(R.id.iv_prof_pic)
    CircleImageView ivProfPic;
    @BindView(R.id.cl_header)
    RelativeLayout clHeader;
    @BindView(R.id.tiet_subscription)
    TextInputEditText tietSubscription;
    @BindView(R.id.tiet_username)
    TextInputEditText tietUsername;
    @BindView(R.id.tiet_email)
    TextInputEditText tietEmail;
    @BindView(R.id.tiet_phone)
    TextInputEditText tietPhone;
    @BindView(R.id.tiet_ic_number)
    TextInputEditText tietIcNumber;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.ll_footer)
    LinearLayout llFooter;
    @BindView(R.id.iv_prof_pic_edit)
    ImageView ivProfPicEdit;

    DAOProviderProfile profileData;
    DAOUserProfile daoUserProfile;

    private static final int RC_LOAD_IMG_CAMERA = 101;
    private static final int RC_LOAD_IMG_BROWSER = 102;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    @BindView(R.id.iv_ic_card_img)
    ImageView ivIcCardImg;
    @BindView(R.id.btn_upload_ic)
    Button btnUploadIc;
    @BindView(R.id.tv_txt_subs)
    TextView tvTxtSubs;
    @BindView(R.id.tv_txt_username)
    TextView tvTxtUsername;
    @BindView(R.id.tv_txt_email)
    TextView tvTxtEmail;
    @BindView(R.id.tv_txt_phone)
    TextView tvTxtPhone;

    @BindView(R.id.tv_txt_ic_card)
    TextView tvTxtIcCard;
    @BindView(R.id.btn_change_pwd)
    Button btnChangePwd;
    @BindView(R.id.btn_edit_subscription)
    ImageView btnEditSubscription;
    @BindView(R.id.btn_edit_mobileno)
    ImageView btnEditMobileno;
    @BindView(R.id.tiet_category)
    EditText tietCategory;
    @BindView(R.id.tiet_subcategory)
    EditText tietSubcategory;
    @BindView(R.id.btn_update_profile)
    Button btnUpdateProfile;
    @BindView(R.id.ll_subscriptionplan)
    LinearLayout llSubscriptionplan;
    @BindView(R.id.view_subscription)
    View viewSubscription;
    @BindView(R.id.ll_category)
    LinearLayout llCategory;
    @BindView(R.id.view_category)
    View viewCategory;
    @BindView(R.id.ll_subcategory)
    LinearLayout llSubcategory;
    @BindView(R.id.view_subscategory)
    View viewSubscategory;
    @BindView(R.id.btn_availability)
    Button btnAvailability;
    @BindView(R.id.tv_txt_currency)
    TextView tvTxtCurrency;
    @BindView(R.id.title_currency)
    TextView titleCurrency;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.tv_subcategory)
    TextView tvSubcategory;
    @BindView(R.id.tv_changePwd)
    TextView tvChangePwd;
    @BindView(R.id.ll_changePwd)
    LinearLayout llChangePwd;

    private BottomSheetDialog attachChooser;

    MultipartBody.Part profileImg;
    MultipartBody.Part icCardImg;
    ArrayList<MultipartBody.Part> imagePartList = new ArrayList<>();
    String type = "", cat_id = "", subCatID = "";
    DialogEditCategoryListAdapter categoryListAdapter;
    DialogEditSubCategoryListAdapter subCategoryListAdapter;
    List<ServiceCategories.CategoryList> category_list = new ArrayList<>();
    List<DAOServiceSubCategories.SubcategoryList> subcategory_list = new ArrayList<>();
    LayoutInflater CatInflater, subCatInflater;
    View catCustomView, subCatCustomView;
    RecyclerView rvCategoryList, rvsubCategoryList;
    TextView tvTitle, tvsubCatTitle;
    AlertDialog dialog, sucCatDialog;
    public Button btnCatDone, btnsubCatDone;
    int width, height;
    private String currencyCode = "USD";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
        setToolBarTitle(AppUtils.cleanLangStr(this, profileStringsList.getLblMyProfile().getName(), R.string.txt_my_profile));
        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);
        width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        height = (int) (getResources().getDisplayMetrics().heightPixels * 0.60);

        setLocale();

        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivProfPicEdit.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
                btnEditSubscription.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
                btnAvailability.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
                btnUpdateProfile.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
            }
        }

        if (PreferenceStorage.getKey(AppConstants.USER_TYPE) != null && PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("1")) {
            initCategories();
            initSubCategories();
            categoryListAdapter = new DialogEditCategoryListAdapter(this, category_list, dialog, tietCategory, tietSubcategory, cat_id, subCatID, btnCatDone);
            subCategoryListAdapter = new DialogEditSubCategoryListAdapter(this, subcategory_list, sucCatDialog, tietSubcategory, subCatID, btnsubCatDone);
            getProviderProfileData();
        } else {
            llCategory.setVisibility(View.GONE);
            llSubcategory.setVisibility(View.GONE);
            llSubscriptionplan.setVisibility(View.GONE);
            viewSubscategory.setVisibility(View.GONE);
            viewCategory.setVisibility(View.GONE);
            viewSubscription.setVisibility(View.GONE);
            btnAvailability.setVisibility(View.GONE);
            getUserProfileData();
        }

        ivSearch.setVisibility(View.GONE);
        ivUserlogin.setVisibility(View.GONE);

        tietUsername.addTextChangedListener(new ProfileTextWatcher(tietUsername));
        tietEmail.addTextChangedListener(new ProfileTextWatcher(tietEmail));
        tietPhone.addTextChangedListener(new ProfileTextWatcher(tietPhone));

        permissions = new ArrayList();
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(CAMERA);
    }

    private void setLocale() {
        try {
            tvTxtSubs.setText(AppUtils.cleanLangStr(this, profileStringsList.getTxtSubscriptionPlan().getName(),
                    R.string.txt_subscription_plan));
            tvTxtUsername.setText(AppUtils.cleanLangStr(this, profileStringsList.getTxtName().getName(),
                    R.string.txt_name));
            tvTxtEmail.setText(AppUtils.cleanLangStr(this, profileStringsList.getLblEmail().getName(),
                    R.string.email_address));
            tvTxtPhone.setText(AppUtils.cleanLangStr(this, profileStringsList.getLblMobileNumber().getName(),
                    R.string.txt_mobile_number));
            tvTxtCurrency.setText(AppUtils.cleanLangStr(this, profileStringsList.getLblChooseCurrency().getName(),
                    R.string.txt_choose_currency));
            tvCategory.setText(AppUtils.cleanLangStr(this, profileStringsList.getLblCategory().getName(),
                    R.string.txt_category));
            tvSubcategory.setText(AppUtils.cleanLangStr(this, profileStringsList.getLblSubCategory().getName(),
                    R.string.txt_subcategory));
            btnAvailability.setText(AppUtils.cleanLangStr(this, profileStringsList.getBtnAvailability().getName(),
                    R.string.txt_availability));
            btnUpdateProfile.setText(AppUtils.cleanLangStr(this, profileStringsList.getBtnUpdateProfile().getName(),
                    R.string.txt_update_profile));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSubCategories() {
        subCatInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        subCatCustomView = subCatInflater.inflate(R.layout.dialog_category, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setView(subCatCustomView);
        alertDialogBuilder.setCancelable(false);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.60);
        sucCatDialog = alertDialogBuilder.create();
        rvsubCategoryList = subCatCustomView.findViewById(R.id.rv_categorylist);
        tvsubCatTitle = subCatCustomView.findViewById(R.id.tv_title);
        btnsubCatDone = subCatCustomView.findViewById(R.id.btn_done);
        sucCatDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void initCategories() {
        CatInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        catCustomView = CatInflater.inflate(R.layout.dialog_category, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setView(catCustomView);
        alertDialogBuilder.setCancelable(false);

        dialog = alertDialogBuilder.create();
        rvCategoryList = catCustomView.findViewById(R.id.rv_categorylist);
        tvTitle = catCustomView.findViewById(R.id.tv_title);
        btnCatDone = catCustomView.findViewById(R.id.btn_done);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    private void getProviderProfileData() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.clearDialog();
            ProgressDlg.showProgressDialog(this, null, null);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<DAOProviderProfile> classificationCall = apiService.getProfileData(PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.MY_LANGUAGE));
            RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.PROFILE_DATA, this, false);
        } else {
            AppUtils.showToast(getApplicationContext(), AppUtils.cleanLangStr(this, getString(R.string.txt_enable_internet), R.string.txt_enable_internet));
        }
    }

    private void getUserProfileData() {
        if (AppUtils.isNetworkAvailable(this)) {
            try {
                ProgressDlg.clearDialog();
                ProgressDlg.showProgressDialog(this, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<DAOUserProfile> classificationCall = apiService.getUserProfileData(PreferenceStorage.getKey(AppConstants.USER_TOKEN), PreferenceStorage.getKey(AppConstants.MY_LANGUAGE));
            RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.USER_PROFILE_DATA, this, false);
        } else {
            AppUtils.showToast(getApplicationContext(), AppUtils.cleanLangStr(this, getString(R.string.txt_enable_internet), R.string.txt_enable_internet));
        }
    }

    @OnClick({R.id.btn_edit, R.id.iv_prof_pic_edit, R.id.btn_upload_ic, R.id.btn_update_profile,
            R.id.btn_change_pwd, R.id.btn_edit_subscription, R.id.tiet_category, R.id.tiet_subcategory,
            R.id.btn_availability, R.id.currencyLayout, R.id.title_currency, R.id.ll_changePwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_edit:
                permissionsToRequest = findUnAskedPermissions(permissions);
                if (permissionsToRequest != null && permissionsToRequest.size() == 0) {
                    makeProfileEdit();
                } else {
                    checkLocationPermission();
                }
                break;
            case R.id.iv_prof_pic_edit:
                type = "0";
                selectImage();
                break;
            case R.id.btn_upload_ic:
                type = "1";
                selectImage();
                break;
            case R.id.btn_update_profile:

                if (PreferenceStorage.getKey(AppConstants.USER_TYPE) != null && PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("1")) {
                    performProviderProfileUpdate();
                } else {
                    performUserProfileUpdate();
                }
                break;
            case R.id.btn_edit_subscription:
                Intent callSubscriptionAct = new Intent(MyProfileActivity.this, GoToSubscriptionActivity.class);
                callSubscriptionAct.putExtra("FromPage", AppConstants.PAGE_MY_PROFILE);
                AppUtils.appStartIntent(MyProfileActivity.this, callSubscriptionAct);
                break;
            case R.id.tiet_category:
                if (AppUtils.isNetworkAvailable(this)) {
                    ProgressDlg.showProgressDialog(this, null, null);
                    ApiInterface apiService =
                            ApiClient.getClientNoHeader().create(ApiInterface.class);
                    try {
                        Call<ServiceCategories> classificationCall = apiService.getServiceCategories(PreferenceStorage.getKey(AppConstants.USER_TOKEN));
                        RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.CATEGORIES, this, false);
                    } catch (Exception e) {
                        ProgressDlg.dismissProgressDialog();
                        e.printStackTrace();
                    }
                } else {
                    AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
                }
                break;
            case R.id.tiet_subcategory:

                if (categoryListAdapter.isAvaialble) {
                    if (!tietCategory.getText().toString().isEmpty()) {
                        if (AppUtils.isNetworkAvailable(this)) {
                            ProgressDlg.showProgressDialog(this, null, null);
                            ApiInterface apiService =
                                    ApiClient.getClientNoHeader().create(ApiInterface.class);
                            try {
                                Call<DAOServiceSubCategories> classificationCall = apiService.postServiceSubCategory(categoryListAdapter.cat_id, PreferenceStorage.getKey(AppConstants.USER_TOKEN));
                                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.SUBCATEGORIES, this, false);
                            } catch (Exception e) {
                                ProgressDlg.dismissProgressDialog();
                                e.printStackTrace();
                            }
                        } else {
                            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
                        }
                    } else {
                        Toast.makeText(this, "Select Category first", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "No Sub Category Available", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_availability:
                startActivity(new Intent(this, ChangeAvailabilityActivity.class));
                break;

            case R.id.currencyLayout:
                getCurrencyList();
                break;
            case R.id.title_currency:
                getCurrencyList();
                break;
            case R.id.ll_changePwd:
                startActivity(new Intent(MyProfileActivity.this, ChangePasswordActivity.class));
                break;
        }
    }

    private void getCurrencyList() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            String token = AppConstants.DEFAULTTOKEN;
            if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
            }
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<CurrencyListResponse> classificationCall = apiService.getCurrencyList(token);
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.GETCURRENCYLIST,
                        this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }
        } else {
            AppUtils.showToast(this, getString(R.string.txt_enable_internet));
        }
    }

    private void performUserProfileUpdate() {
        if (!validatePhoneNo()) {
            return;
        } else {
            if (profileImg != null) {
                imagePartList.add(profileImg);
            }
            if (tietUsername.getText().toString().isEmpty()) {
                Toast.makeText(this, AppUtils.cleanLangStr(this, profileStringsList.getTxtName().getValidation1(),
                        R.string.err_username), Toast.LENGTH_SHORT).show();
                return;
            }
            RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), tietUsername.getText().toString());
            RequestBody type = RequestBody.create(MediaType.parse("text/plain"), PreferenceStorage.getKey(AppConstants.USER_TYPE));
            RequestBody currencyCodeReq = RequestBody.create(MediaType.parse("text/plain"), currencyCode);
            if (AppUtils.isNetworkAvailable(this)) {

                ProgressDlg.clearDialog();
                ProgressDlg.showProgressDialog(this, null, null);
                ApiInterface apiService =
                        ApiClient.getClientNoHeader().create(ApiInterface.class);
                try {
                    String latitude = PreferenceStorage.getKey(AppConstants.MY_LATITUDE) == null ? getLatitude() : PreferenceStorage.getKey(AppConstants.MY_LATITUDE);
                    String longitude = PreferenceStorage.getKey(AppConstants.MY_LONGITUDE) == null ? getLongitude() : PreferenceStorage.getKey(AppConstants.MY_LONGITUDE);
                    Call<DAOUserProfile> classificationCall = apiService.postUpdateUserProfile(userName, type, currencyCodeReq,
                            profileImg, PreferenceStorage.getKey(AppConstants.USER_TOKEN));
                    RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.USER_PROFILE_UPDATE_DATA, this, true);
                } catch (Exception e) {
                    ProgressDlg.dismissProgressDialog();
                    e.printStackTrace();
                }
            } else {
                AppUtils.showToast(getApplicationContext(), AppUtils.cleanLangStr(this, getString(R.string.txt_enable_internet), R.string.txt_enable_internet));
            }
        }
    }

    private void performProviderProfileUpdate() {
        if (!validatePhoneNo()) {
            return;
        } else {

            if (profileImg != null) {
                imagePartList.add(profileImg);
            }

            if (tietUsername.getText().toString().isEmpty()) {
                Toast.makeText(this, AppUtils.cleanLangStr(this, profileStringsList.getTxtName().getValidation1(),
                        R.string.err_username), Toast.LENGTH_SHORT).show();
                return;
            }

            if (categoryListAdapter.cat_id.isEmpty()) {
                Toast.makeText(this, AppUtils.cleanLangStr(this, profileStringsList.getLblCategory().getValidation1(),
                        R.string.err_category), Toast.LENGTH_SHORT).show();
                return;
            }

            if (tietSubcategory.getText().toString().isEmpty()) {
                Toast.makeText(this, AppUtils.cleanLangStr(this, profileStringsList.getLblSubCategory().getValidation1(),
                        R.string.err_subcategory), Toast.LENGTH_SHORT).show();
                return;
            }

            RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), tietUsername.getText().toString());
            RequestBody categoryID = RequestBody.create(MediaType.parse("text/plain"), categoryListAdapter.cat_id);
            RequestBody subCategoryID = RequestBody.create(MediaType.parse("text/plain"), subCategoryListAdapter.subCatID);
            RequestBody currencyCodeReq = RequestBody.create(MediaType.parse("text/plain"), currencyCode);
            if (AppUtils.isNetworkAvailable(this)) {

                ProgressDlg.clearDialog();
                ProgressDlg.showProgressDialog(this, null, null);
                ApiInterface apiService =
                        ApiClient.getClientNoHeader().create(ApiInterface.class);
                try {
                    String latitude = PreferenceStorage.getKey(AppConstants.MY_LATITUDE) == null ? getLatitude() : PreferenceStorage.getKey(AppConstants.MY_LATITUDE);
                    String longitude = PreferenceStorage.getKey(AppConstants.MY_LONGITUDE) == null ? getLongitude() : PreferenceStorage.getKey(AppConstants.MY_LONGITUDE);
                    Call<DAOProviderProfile> classificationCall = apiService.postProfileUpdate(userName, categoryID, subCategoryID,
                            currencyCodeReq, profileImg, PreferenceStorage.getKey(AppConstants.USER_TOKEN));
                    RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.PROFILE_UPDATE_DATA, this, true);
                } catch (Exception e) {
                    ProgressDlg.dismissProgressDialog();
                    e.printStackTrace();
                }

            } else {
                AppUtils.showToast(getApplicationContext(), AppUtils.cleanLangStr(this, getString(R.string.txt_enable_internet), R.string.txt_enable_internet));
            }
        }
    }

    private class ProfileTextWatcher implements TextWatcher {
        private View view;

        private ProfileTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (view.getId() == R.id.tiet_username) {
                if (!tietUsername.getText().toString().isEmpty()) {
                    validateUsername();
                }
            } else if (view.getId() == R.id.tiet_email) {
                if (!tietEmail.getText().toString().isEmpty()) {
                    validateEmail();
                }
            } else if (view.getId() == R.id.tiet_phone) {
                if (!tietPhone.getText().toString().isEmpty()) {
                    validatePhoneNo();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }

    private void makeProfileEdit() {
        btnEditSubscription.setVisibility(View.VISIBLE);
        ivProfPicEdit.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
        btnUploadIc.setVisibility(View.VISIBLE);
        llFooter.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseModel) {


        switch (responseModel) {
            case AppConstants.PROFILE_DATA:
                profileData = (DAOProviderProfile) myRes;
                if (profileData != null && profileData.getData() != null) {
                    tietUsername.setText(profileData.getData().getName());
                    tietEmail.setText(profileData.getData().getEmail());
                    tietPhone.setText(profileData.getData().getCountryCode() + " " + profileData.getData().getMobileno());
                    tietCategory.setText(profileData.getData().getCategoryName());
                    tietSubcategory.setText(profileData.getData().getSubcategoryName());
                    categoryListAdapter.cat_id = profileData.getData().getCategory();
                    categoryListAdapter.subCatID = profileData.getData().getSubcategory();
                    subCategoryListAdapter.subCatID = profileData.getData().getSubcategory();
                    if (profileData.getData().getCurrencyCode() != null) {
                        titleCurrency.setText(profileData.getData().getCurrencyCode());
                        PreferenceStorage.setKey(AppConstants.CURRENCYCODE, profileData.getData().getCurrencyCode());
                    }
                    if (profileData.getData().getSubscriptionDetails() != null) {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = null;
                        try {
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
                            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            if (profileData.getData().getSubscriptionDetails().getExpiryDateTime() != null &&
                                    !profileData.getData().getSubscriptionDetails().getExpiryDateTime().isEmpty()) {
                                Date dates = simpleDateFormat.parse(profileData.getData().getSubscriptionDetails().getExpiryDateTime());
                                simpleDateFormat.setTimeZone(TimeZone.getDefault());
                                String formattedDate = simpleDateFormat2.format(dates);
                                tietSubscription.setText(profileData.getData().getSubscriptionDetails().getSubscriptionName() + "(" + formattedDate + ")");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Picasso.get()
                            .load(AppConstants.BASE_URL + profileData.getData().getProfileImg())
                            .placeholder(R.drawable.ic_user_placeholder)
                            .error(R.drawable.ic_user_placeholder)
                            .into(ivProfPic);

                    PreferenceStorage.setKey(AppConstants.PEMAIL, profileData.getData().getEmail());
                    PreferenceStorage.setKey(AppConstants.PNAME, profileData.getData().getName());
                    PreferenceStorage.setKey(AppConstants.PIMAGE, profileData.getData().getProfileImg());

                }
                break;
            case AppConstants.PROFILE_UPDATE_DATA:
                profileData = (DAOProviderProfile) myRes;
                if (profileData != null && profileData.getData() != null) {
                    PreferenceStorage.setKey(AppConstants.PEMAIL, profileData.getData().getEmail());
                    PreferenceStorage.setKey(AppConstants.PNAME, profileData.getData().getName());
                    PreferenceStorage.setKey(AppConstants.PIMAGE, profileData.getData().getProfileImg());
                    if (profileData.getData().getCurrencyCode() != null) {
                        titleCurrency.setText(profileData.getData().getCurrencyCode());
                        PreferenceStorage.setKey(AppConstants.CURRENCYCODE, profileData.getData().getCurrencyCode());
                    }
                    Toast.makeText(this, profileData.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }

                break;
            case AppConstants.CATEGORIES:
                ServiceCategories categoryList = (ServiceCategories) myRes;
                if (categoryList.getData().getCategoryList().size() > 0) {
                    showCategoryPopupWindow(categoryList.getData().getCategoryList());
                } else {
                    Toast.makeText(this, AppUtils.cleanLangStr(this, profileStringsList.getLblNoCategory().getName(),
                            R.string.txt_no_category_available), Toast.LENGTH_SHORT).show();
                }

                break;
            case AppConstants.SUBCATEGORIES:
                DAOServiceSubCategories subCategoryList = (DAOServiceSubCategories) myRes;
                if (subCategoryList.getData().getSubcategoryList() != null && subCategoryList.getData().getSubcategoryList().size() > 0) {
                    showSubCategoryPopupWindow(subCategoryList.getData().getSubcategoryList());
                } else {
                    Toast.makeText(this, AppUtils.cleanLangStr(this, profileStringsList.getLblNoSubCategory().getName(),
                            R.string.txt_no_sub_category_available), Toast.LENGTH_SHORT).show();
                }
                break;
            case AppConstants.USER_PROFILE_DATA:
                daoUserProfile = (DAOUserProfile) myRes;
                if (daoUserProfile != null && daoUserProfile.getData() != null) {
                    tietUsername.setText(daoUserProfile.getData().getName());
                    tietEmail.setText(daoUserProfile.getData().getEmail());
                    if (daoUserProfile.getData().getCurrencyCode() != null) {
                        titleCurrency.setText(daoUserProfile.getData().getCurrencyCode());
                        PreferenceStorage.setKey(AppConstants.CURRENCYCODE, daoUserProfile.getData().getCurrencyCode());
                    }
                    tietPhone.setText(daoUserProfile.getData().getCountryCode() + " " + daoUserProfile.getData().getMobileno());
                    PreferenceStorage.setKey(AppConstants.PEMAIL, daoUserProfile.getData().getEmail());
                    PreferenceStorage.setKey(AppConstants.PNAME, daoUserProfile.getData().getName());
                    PreferenceStorage.setKey(AppConstants.PIMAGE, daoUserProfile.getData().getProfile_img());

                    Picasso.get()
                            .load(AppConstants.BASE_URL + daoUserProfile.getData().getProfile_img())
                            .placeholder(R.drawable.ic_user_placeholder)
                            .error(R.drawable.ic_user_placeholder)
                            .into(ivProfPic);
                }
                break;
            case AppConstants.USER_PROFILE_UPDATE_DATA:
                daoUserProfile = (DAOUserProfile) myRes;
                if (daoUserProfile != null && daoUserProfile.getData() != null) {
                    PreferenceStorage.setKey(AppConstants.PEMAIL, daoUserProfile.getData().getEmail());
                    PreferenceStorage.setKey(AppConstants.PNAME, daoUserProfile.getData().getName());
                    PreferenceStorage.setKey(AppConstants.PIMAGE, daoUserProfile.getData().getProfile_img());
                    if (daoUserProfile.getData().getCurrencyCode() != null) {
                        titleCurrency.setText(daoUserProfile.getData().getCurrencyCode());
                        PreferenceStorage.setKey(AppConstants.CURRENCYCODE, daoUserProfile.getData().getCurrencyCode());
                    }
                    Toast.makeText(this, daoUserProfile.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case AppConstants.GETCURRENCYLIST:
                CurrencyListResponse response = (CurrencyListResponse) myRes;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                LayoutInflater inflater = this.getLayoutInflater();
                View titleView = inflater.inflate(R.layout.list_custom_alert_dialog_tiltle, null);
                builder.setCustomTitle(titleView);
                builder.setAdapter(new CurrencyAdapter(this, R.layout.list_item_language, response.getData()),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                currencyCode = response.getData().get(item).getCurrencyCode();
                                titleCurrency.setText(response.getData().get(item).getCurrencyCode());
                                dialog.dismiss();

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseModel) {
        if (imagePartList != null) {
            if (imagePartList.contains(profileImg)) {
                imagePartList.remove(profileImg);
            }
            if (imagePartList.contains(icCardImg)) {
                imagePartList.remove(icCardImg);
            }
        }
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseModel) {
        if (imagePartList != null) {
            if (imagePartList.contains(profileImg)) {
                imagePartList.remove(profileImg);
            }
            if (imagePartList.contains(icCardImg)) {
                imagePartList.remove(icCardImg);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateUsername() {
        if (tietUsername.getText().toString().isEmpty()) {
            tietUsername.setError(AppUtils.cleanLangStr(this, profileStringsList.getTxtName().getValidation1(), R.string.err_username));
            tietUsername.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        if (tietEmail.getText().toString().isEmpty()) {
            tietEmail.setError(AppUtils.cleanLangStr(this, profileStringsList.getLblEmail().getValidation1(), R.string.err_email));
            tietEmail.requestFocus();
            return false;
        } else if (!AppUtils.isValidEmail(tietEmail.getText().toString())) {
            tietEmail.setError(AppUtils.cleanLangStr(this, profileStringsList.getLblEmail().getValidation2(), R.string.err_valid_email));
            tietEmail.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validatePhoneNo() {
        if (tietPhone.getText().toString().isEmpty()) {
            tietPhone.setError(AppUtils.cleanLangStr(this, profileStringsList.getLblMobileNumber().getValidation1(), R.string.err_phone_no));
            tietPhone.requestFocus();
            return false;
        } else if (tietPhone.getText().toString().length() > 15) {
            tietPhone.setError(AppUtils.cleanLangStr(this, profileStringsList.getLblMobileNumber().getValidation2(), R.string.err_max_no));
            tietPhone.requestFocus();
            return false;
        }
        return true;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_LOAD_IMG_BROWSER) {
            onSelectFromGalleryResult(data);
        } else if (requestCode == RC_LOAD_IMG_CAMERA) {
            onCaptureImageResult(data);
        }
    }

    private void selectImage() {
        attachChooser = new BottomSheetDialog(MyProfileActivity.this);
        attachChooser.setContentView((MyProfileActivity.this).getLayoutInflater().inflate(R.layout.popup_add_attach_options,
                new LinearLayout(MyProfileActivity.this)));
        attachChooser.show();
        //TODO: lang
        LinearLayout btnStartCamera = (LinearLayout) attachChooser.findViewById(R.id.btn_from_camera);
        LinearLayout btnStartFileBrowser = (LinearLayout) attachChooser.findViewById(R.id.btn_from_local);
        btnStartCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachChooser.dismiss();
                if (AppUtils.checkCameraPermission(MyProfileActivity.this))
                    cameraIntent();
            }
        });
        btnStartFileBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachChooser.dismiss();
                if (AppUtils.checkPermission(MyProfileActivity.this))
                    galleryIntent();
            }
        });
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RC_LOAD_IMG_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(intent, RC_LOAD_IMG_BROWSER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AppConstants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                } else {
                }
                break;
            case AppConstants.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent();
                } else {
                }
                break;
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap thumbnail = null;
        Bitmap scaled = null;
        try {
            if (data != null) {
                thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                int nh = (int) (160 * (512.0 / 160));
                thumbnail = Bitmap.createScaledBitmap(thumbnail, 512, nh, true);
            }
            if (thumbnail != null) {
                scaled = thumbnail;
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                Uri selectedImage = data.getData();
                String encodeImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes.toByteArray());
                profileImg = MultipartBody.Part.createFormData("profile_img", "profile_img.jpg", requestFile);
                ivProfPic.setImageBitmap(scaled);

            }
        } catch (Exception e) {

        }

    }

    private void onCaptureImageResult(Intent data) {
        try {
            if (data != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                if (thumbnail != null) {
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    String encodeImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), bytes.toByteArray());
                profileImg = MultipartBody.Part.createFormData("profile_img", "profile_img.jpg", requestFile);
                ivProfPic.setImageBitmap(thumbnail);
            }
        } catch (Exception e) {

        }
    }

    public void showCategoryPopupWindow(List<ServiceCategories.CategoryList> categoryList) {
        tvTitle.setText(AppUtils.cleanLangStr(this, profileStringsList.getLblCategory().getName(), R.string.txt_category));
        category_list.clear();
        category_list.addAll(categoryList);

        if (categoryListAdapter.cat_id != null && !categoryListAdapter.cat_id.isEmpty())
            for (int i1 = 0; i1 < category_list.size(); i1++) {
                if (category_list.get(i1).getId().equalsIgnoreCase(categoryListAdapter.cat_id)) {
                    category_list.get(i1).setChecked(true);
                }
            }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCategoryList.setLayoutManager(linearLayoutManager);
        rvCategoryList.setAdapter(categoryListAdapter);
        categoryListAdapter.notifyDataSetChanged();
        dialog.show();
        dialog.getWindow().setLayout(width, height);
    }

    public void showSubCategoryPopupWindow(List<DAOServiceSubCategories.SubcategoryList> subCategory_list) {
        subcategory_list.clear();
        subcategory_list.addAll(subCategory_list);
        if (subCategoryListAdapter.subCatID != null && !subCategoryListAdapter.subCatID.isEmpty())
            for (int i1 = 0; i1 < subCategory_list.size(); i1++) {
                if (subCategory_list.get(i1).getId().equalsIgnoreCase(subCategoryListAdapter.subCatID)) {
                    subCategory_list.get(i1).setChecked(true);
                }
            }

        tvsubCatTitle.setText(AppUtils.cleanLangStr(this, profileStringsList.getLblSubCategory().getName(), R.string.txt_subcategory));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvsubCategoryList.setLayoutManager(linearLayoutManager);
        rvsubCategoryList.setAdapter(subCategoryListAdapter);
        subCategoryListAdapter.notifyDataSetChanged();
        sucCatDialog.show();
        sucCatDialog.getWindow().setLayout(width, height);

    }

    public void showCountrycodeDialog() {
        if (AppConstants.country_code_jsonResponse != null) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            View mShowAddProjectView = getLayoutInflater().inflate(R.layout.dialog_change_number, null);
            mBuilder.setView(mShowAddProjectView);

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);

            AlertDialog mCountryDialog = mBuilder.create();
            mCountryDialog.show();
            mCountryDialog.getWindow().setLayout(width, height);
        } else {
            Toast.makeText(this, "No Data found...", Toast.LENGTH_SHORT).show();
        }
    }

    private class CurrencyAdapter extends ArrayAdapter<CurrencyListResponse.Datum> {
        List<CurrencyListResponse.Datum> items;

        CurrencyAdapter(@NonNull Context context, int resource, List<CurrencyListResponse.Datum> items) {
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
            final LayoutInflater inflater = (LayoutInflater) getApplicationContext()
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
            holder.title.setText(items.get(position).getCurrencyCode());
//            if (position == 0)
//                holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_lang_english));
//            else
//                holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_lang_malay));

            return convertView;
        }
    }
}
