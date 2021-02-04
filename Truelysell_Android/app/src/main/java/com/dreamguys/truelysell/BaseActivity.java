package com.dreamguys.truelysell;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.Country;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOGenerateOTP;
import com.dreamguys.truelysell.datamodel.Phase3.DAOLoginProfessional;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, RetrofitHandler.RetrofitResHandler {

    @BindView(R.id.tv_titlename)
    TextView tvTitlename;
    @BindView(R.id.iv_userlogin)
    public ImageView ivUserlogin;
    @BindView(R.id.iv_search)
    public ImageView ivSearch;
    ImageView ivBack;
    private LinearLayout view_stub; //This is the framelayout to keep your content view
    Toolbar mToolbar;
    public Activity currentActivity;
    private Location mLocation;
    String Latitude, Longitude;
    protected GoogleApiClient mGoogleApiClient;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 15000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    public ArrayList permissionsToRequest;
    public ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    public LanguageModel.Common_used_texts commonData = new LanguageModel().new Common_used_texts();
    LanguageModel.Navigation navData = new LanguageModel().new Navigation();
    public int appColor = 0;

    public Button btnRegister, btnVerify;
    public EditText etName, etEmailAddress, etMobileNumber, et_refferalCode;
    public EditText etCountryCode;
    public AlertDialog mCountryDialog;
    public ListView vCountryList;
    public CountryAdapter aCountryAdapter;
    public HashMap<String, String> postUserSignup = new HashMap<>();
    public HashMap<String, String> postGenerateOtp = new HashMap<>();
    public ImageView ivExitDialog;
    public LinearLayout llDialog, llRegister, llName, llEmail, ll_referral;
    public FrameLayout flOtpVerification;
    public TextView tvResendCode, tvMobileNo, tvLoginUser;
    public ApiInterface apiInterface;
    public String userType = "";
    public CheckBox cbExistingUser;
    public AlertDialog.Builder alertDialogBuilder;
    AlertDialog userLoginDialog;
    public boolean isResend;
    SearchView searchView;
    ListAdapter adapter;
    LanguageResponse.Data.Language.RegisterScreen registerScreenList;
    LanguageResponse.Data.Language.HomeScreen homeStringsList;
    LanguageResponse.Data.Language.CreateService createServiceStringsList;
    LanguageResponse.Data.Language.ProfileScreen profileStringsList;
    LanguageResponse.Data.Language.ProviderAvailabilityScreen availabilityScreenList;
    LanguageResponse.Data.Language.SettingsScreen settingScreenList;
    public LanguageResponse.Data.Language.WalletScreen walletScreenList;
    public LanguageResponse.Data.Language.SubscriptionScreen subscriptionScreenList;
    public LanguageResponse.Data.Language.BookingDetailService bookingDetailServiceScreenList;
    public LanguageResponse.Data.Language.BookingService bookingServiceScreenList;
    public LanguageResponse.Data.Language.AccountSettingsScreen accountSettingsScreenList;
    public LanguageResponse.Data.Language.CommonStrings commonStringList;
    private LinearLayout ll_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.setContentView(R.layout.activity_base);

        view_stub = (LinearLayout) findViewById(R.id.view_stub);
        mToolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        checkLocationPermission();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getLocaleData();
    }

    public void checkLocationPermission() {
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (view_stub != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, view_stub, false);
            view_stub.addView(stubView, lp);
        }
    }

    @Override
    public void setContentView(View view) {
        if (view_stub != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view_stub.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view_stub != null) {
            view_stub.addView(view, params);
        }
    }

    public void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public void setToolBarTitle(String title) {
        tvTitlename.setText(title);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        if (mLocation != null) {
            Latitude = String.valueOf(mLocation.getLatitude());
            Longitude = String.valueOf(mLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            (this).getWindow().getDecorView().setLayoutDirection(PreferenceStorage.getKey(AppConstants.MY_LANGUAGE).equalsIgnoreCase("ar")
                    ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("TAG_BASE", "Base Resumed");
        if (!checkPlayServices()) {
            Toast.makeText(this, AppUtils.cleanLangStr(this, commonData.getLg7_please_install_(), R.string.err_txt_install_play_ser), Toast.LENGTH_SHORT).show();
        }
        if (mGoogleApiClient != null) {
            if (!mGoogleApiClient.isConnected())
                mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    public String getLatitude() {
        String latitude = Latitude;
        return latitude;
    }

    public String getLongitude() {
        String longitude = Longitude;
        return longitude;
    }

    public String getLocation() {
        String location = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            if (mLocation != null) {
                addresses = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                    location = addresses.get(0).getAddressLine(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLocation != null) {
            Latitude = String.valueOf(mLocation.getLatitude());
            Longitude = String.valueOf(mLocation.getLongitude());
            if (PreferenceStorage.getKey(AppConstants.MY_LATITUDE) == null
                    && PreferenceStorage.getKey(AppConstants.MY_LONGITUDE) == null) {
                PreferenceStorage.setKey(AppConstants.MY_LATITUDE, Latitude);
                PreferenceStorage.setKey(AppConstants.MY_LONGITUDE, Longitude);
                PreferenceStorage.setKey(AppConstants.MY_ADDRESS, getLocation());
            }
        }

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else
                finish();

            return false;
        }
        return true;
    }

    @SuppressLint("RestrictedApi")
    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            Toast.makeText(getApplicationContext(), AppUtils.cleanLangStr(this, commonData.getLg7_enable_permissi(), R.string.err_txt_enable_permission), Toast.LENGTH_LONG).show();

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);


    }


    public ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    public boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    public boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }

    @OnClick({R.id.iv_userlogin, R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_userlogin:
                break;
            case R.id.iv_search:
                break;
        }
    }

    public void checkUserLogin(String serviceId, String serviceAmount, String serviceTitle, String currency) {
        if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
            Intent callBookServiceAct = new Intent(this, ActivityBookService.class);
            callBookServiceAct.putExtra(AppConstants.SERVICEID, serviceId);
            callBookServiceAct.putExtra(AppConstants.SERVICEAMOUNT, serviceAmount);
            callBookServiceAct.putExtra(AppConstants.SERVICETITLE, serviceTitle);
            callBookServiceAct.putExtra(AppConstants.CURRENCY, Html.fromHtml(currency));
            startActivity(callBookServiceAct);
        } else {
            showUserLoginDialog();
        }
    }

    public void showUserLoginDialog() {
        final OtpView otpView;
        alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_user_signup, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);

        tvLoginUser = view.findViewById(R.id.tv_loginuser);
        llDialog = view.findViewById(R.id.ll_dialog);
        llRegister = view.findViewById(R.id.ll_register);
        llName = view.findViewById(R.id.ll_name);
        llEmail = view.findViewById(R.id.ll_email);
        ll_referral = view.findViewById(R.id.ll_referral);
        et_refferalCode = view.findViewById(R.id.et_refferalCode);
        flOtpVerification = view.findViewById(R.id.fl_otp_verification);
        ivExitDialog = view.findViewById(R.id.iv_exit);
        btnRegister = view.findViewById(R.id.btn_register);
        etName = view.findViewById(R.id.et_name);
        etEmailAddress = view.findViewById(R.id.et_email_address);
        etMobileNumber = view.findViewById(R.id.et_mobile_no);
        etCountryCode = view.findViewById(R.id.et_country_code);
        cbExistingUser = view.findViewById(R.id.cb_existing_user);
        btnVerify = view.findViewById(R.id.btn_verify);
        tvResendCode = view.findViewById(R.id.tv_resendcode);
        tvMobileNo = view.findViewById(R.id.tv_mobile_no);
        ll_password = view.findViewById(R.id.ll_password);
        otpView = view.findViewById(R.id.otp_view);

        if (AppUtils.isThemeChanged(this)) {
            btnRegister.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
            btnVerify.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
            ivExitDialog.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
            cbExistingUser.setButtonTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
        }

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.60);

        userLoginDialog = alertDialogBuilder.create();
        userLoginDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        userLoginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        userLoginDialog.show();
        userLoginDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        userLoginDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        userLoginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        try {
            tvLoginUser.setText(AppUtils.cleanLangStr(this,
                    registerScreenList.getJoin_as_user().getName(), R.string.txt_join_as_user));
            etName.setHint(AppUtils.cleanLangStr(this,
                    registerScreenList.getTxtFldName().getPlaceholder(), R.string.txt_name));
            etEmailAddress.setHint(AppUtils.cleanLangStr(this,
                    registerScreenList.getTxtFldEmail().getPlaceholder(), R.string.email));
            etCountryCode.setHint(AppUtils.cleanLangStr(this,
                    registerScreenList.getBtnCode().getPlaceholder(), R.string.txt_code));
            etMobileNumber.setHint(AppUtils.cleanLangStr(this,
                    registerScreenList.getTxtFldMobileNum().getPlaceholder(), R.string.txt_mobile_number));
            et_refferalCode.setHint(AppUtils.cleanLangStr(this,
                    registerScreenList.getTxtFldReferenceCode().getPlaceholder(), R.string.txt_refferal_code));
            cbExistingUser.setText(AppUtils.cleanLangStr(this,
                    registerScreenList.getLbl_already_user().getName(), R.string.txt_already_a_user));
            btnRegister.setText(AppUtils.cleanLangStr(this,
                    registerScreenList.getLblRegister().getName(), R.string.txt_register));
            tvResendCode.setText(AppUtils.cleanLangStr(this,
                    registerScreenList.getBtn_resend_otp().getName(), R.string.tv_resendcode));
            TextView tvDidntOtp = view.findViewById(R.id.txt_didn_t_receive_otp);
            tvDidntOtp.setText(AppUtils.cleanLangStr(this,
                    registerScreenList.getLbl_dint_receive_otp().getName(), R.string.txt_didn_t_receive_otp));
            btnVerify.setText(AppUtils.cleanLangStr(this,
                    registerScreenList.getLbl_verify().getName(), R.string.txt_verify));
        } catch (Exception e) {
            e.printStackTrace();
        }

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                // do Stuff
                InputMethodManager imm = (InputMethodManager) BaseActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(otpView.getWindowToken(), 0);
                BaseActivity.this.postUserLogin(etMobileNumber.getText().toString(), etCountryCode.getText().toString(), otp);
                Log.d("onOtpCompleted=>", otp);
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if (otpView.getText().toString().isEmpty() || otpView.getText().toString().length() < 4) {
                    Toast.makeText(BaseActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager imm = (InputMethodManager) BaseActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(otpView.getWindowToken(), 0);
                    BaseActivity.this.postUserLogin(etMobileNumber.getText().toString(), etCountryCode.getText().toString(), otpView.getText().toString());
                }
            }
        });

        cbExistingUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llName.setVisibility(View.GONE);
                    llEmail.setVisibility(View.GONE);
                    ll_referral.setVisibility(View.GONE);
                } else {
                    llName.setVisibility(View.VISIBLE);
                    llEmail.setVisibility(View.VISIBLE);
                    ll_referral.setVisibility(View.VISIBLE);
                }
            }
        });

        tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isResend = true;
                getMobileOtp(etCountryCode.getText().toString().trim(), etMobileNumber.getText().toString().trim(),
                        "", "", "");
            }
        });

        etCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view12) {
                BaseActivity.this.showCountrycodeDialog();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view13) {
                if (cbExistingUser.isChecked()) {
                    if (etCountryCode.getText().toString().isEmpty()) {
                        Toast.makeText(BaseActivity.this, AppUtils.cleanLangStr(BaseActivity.this,
                                registerScreenList.getBtnCode().getValidation1(), R.string.txt_select_country_code), Toast.LENGTH_SHORT).show();
                    } else if (etMobileNumber.getText().toString().isEmpty()) {
                        Toast.makeText(BaseActivity.this, AppUtils.cleanLangStr(BaseActivity.this,
                                registerScreenList.getTxtFldMobileNum().getValidation1(), R.string.txt_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    } else if (etMobileNumber.getText().toString().length() < 9 || etMobileNumber.getText().toString().length() > 15) {
                        Toast.makeText(BaseActivity.this, AppUtils.cleanLangStr(BaseActivity.this,
                                registerScreenList.getTxtFldMobileNum().getValidation2(),
                                R.string.txt_enter_valid_mobile_number), Toast.LENGTH_SHORT).show();
                    } else {
                        InputMethodManager imm = (InputMethodManager) BaseActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etMobileNumber.getWindowToken(), 0);
                        BaseActivity.this.getMobileOtp(etCountryCode.getText().toString().trim(),
                                etMobileNumber.getText().toString().trim(), "", "", "");
                    }
                } else {
                    if (etName.getText().toString().isEmpty()) {
                        Toast.makeText(BaseActivity.this, AppUtils.cleanLangStr(BaseActivity.this,
                                registerScreenList.getTxtFldName().getValidation1(), R.string.txt_enter_name), Toast.LENGTH_SHORT).show();
                    } else if (etEmailAddress.getText().toString().isEmpty()) {
                        Toast.makeText(BaseActivity.this, AppUtils.cleanLangStr(BaseActivity.this,
                                registerScreenList.getTxtFldEmail().getValidation1(), R.string.txt_enter_email), Toast.LENGTH_SHORT).show();
                    } else if (!AppUtils.isValidEmail(etEmailAddress.getText().toString())) {
                        Toast.makeText(BaseActivity.this, AppUtils.cleanLangStr(BaseActivity.this,
                                registerScreenList.getTxtFldEmail().getValidation1(), R.string.txt_enter_valid_email), Toast.LENGTH_SHORT).show();
                    } else if (etCountryCode.getText().toString().isEmpty()) {
                        Toast.makeText(BaseActivity.this, AppUtils.cleanLangStr(BaseActivity.this,
                                registerScreenList.getBtnCode().getValidation1(), R.string.txt_select_country_code), Toast.LENGTH_SHORT).show();
                    } else if (etMobileNumber.getText().toString().isEmpty()) {
                        Toast.makeText(BaseActivity.this, AppUtils.cleanLangStr(BaseActivity.this,
                                registerScreenList.getTxtFldMobileNum().getValidation1(), R.string.txt_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    } else if (etMobileNumber.getText().toString().length() < 9 || etMobileNumber.getText().toString().length() > 15) {
                        Toast.makeText(BaseActivity.this, AppUtils.cleanLangStr(BaseActivity.this,
                                registerScreenList.getTxtFldMobileNum().getValidation2(),
                                R.string.txt_enter_valid_mobile_number), Toast.LENGTH_SHORT).show();
                    } else {
                        InputMethodManager imm = (InputMethodManager) BaseActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etMobileNumber.getWindowToken(), 0);
                        BaseActivity.this.getMobileOtp(etCountryCode.getText().toString().trim(),
                                etMobileNumber.getText().toString().trim(), etEmailAddress.getText().toString().trim(),
                                etName.getText().toString(), et_refferalCode.getText().toString());
                    }
                }
            }
        });

        ivExitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLoginDialog.dismiss();
            }
        });
    }

    public void showCountrycodeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mShowAddProjectView = getLayoutInflater().inflate(R.layout.dialog_country, null);
        mBuilder.setView(mShowAddProjectView);
        vCountryList = (ListView) mShowAddProjectView.findViewById(R.id.inputcountrylists);
        searchView = (SearchView) mShowAddProjectView.findViewById(R.id.searchview);
        TextView tvStatusType = mShowAddProjectView.findViewById(R.id.tv_statustype);
        tvStatusType.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
        tvStatusType.setText(AppUtils.cleanLangStr(BaseActivity.this,
                registerScreenList.getLblSelectCountry().getName(),
                R.string.txt_choose_country_code));
//            aCountryAdapter = new CountryAdapter(HomeActivity.this, AppConstants.country_code_jsonResponse);
        adapter = new ListAdapter(this, AppUtils.getCountries(this));
        vCountryList.setAdapter(adapter);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.70);

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

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.MOBILEOTP:
                DAOGenerateOTP daoGenerateOTP = (DAOGenerateOTP) myRes;
                if (daoGenerateOTP.getResponseHeader().getResponseMessage().equalsIgnoreCase("Share Code Invalid")) {
                    Toast.makeText(this, daoGenerateOTP.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, daoGenerateOTP.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
//                tvMobileNo.setText("We have sent you access code Via SMS to " + etCountryCode.getText().toString() + " " + etMobileNumber.getText().toString() + " for mobile number verification");
                    tvMobileNo.setText(AppUtils.cleanLangStr(this,
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
                    Toast.makeText(this, daoLoginProfessional.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    PreferenceStorage.setKey(AppConstants.PNAME, daoLoginProfessional.getData().getProviderDetails().getName());
                    PreferenceStorage.setKey(AppConstants.PEMAIL, daoLoginProfessional.getData().getProviderDetails().getEmail());
                    PreferenceStorage.setKey(AppConstants.PMOBILENO, daoLoginProfessional.getData().getProviderDetails().getMobileno());
                    PreferenceStorage.setKey(AppConstants.PIMAGE, daoLoginProfessional.getData().getProviderDetails().getProfileImg());
                    PreferenceStorage.setKey(AppConstants.USER_TOKEN, daoLoginProfessional.getData().getProviderDetails().getToken());
                    PreferenceStorage.setKey(AppConstants.USER_TYPE, daoLoginProfessional.getData().getProviderDetails().getType());
                    PreferenceStorage.setKey(AppConstants.SHARECODE, daoLoginProfessional.getData().getProviderDetails().getShareCode());
                    startActivity(new Intent(this, HomeActivity.class));
                    this.finish();
                } else {
                    Toast.makeText(this, daoLoginProfessional.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.MOBILEOTP:
                cbExistingUser.setChecked(false);
                llName.setVisibility(View.VISIBLE);
                llEmail.setVisibility(View.VISIBLE);
                ll_referral.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {

    }

    public class CountryAdapter extends BaseAdapter {

        Context mContext;
        LayoutInflater mInflater;
        private final JSONArray mCountry;

        public CountryAdapter(Context mContext, JSONArray mCountry) {
            this.mContext = mContext;
            this.mCountry = mCountry;
            mInflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mCountry.length();
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
                if (mCountry.getJSONObject(position).getJSONArray("callingCodes").length() != 0 && !mCountry.getJSONObject(position).getJSONArray("callingCodes").get(0).toString().isEmpty()) {
                    String name = mCountry.getJSONObject(position).getString("callingCodes").replace("[", "").replace("]", "").replace("\"", "");
                    mHolder.mName.setText(mCountry.getJSONObject(position).getString("name"));
                    mHolder.mCountryCode.setText(mCountry.getJSONObject(position).getJSONArray("callingCodes").get(0).toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        etCountryCode.setText(mCountry.getJSONObject(position).getString("callingCodes").replace("[", "").replace("]", "").replace("\"", ""));
                        mCountryDialog.dismiss();
                    } catch (JSONException e) {
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

    public void getMobileOtp(String country_code, String mobile_no, String email, String name, String referralCode) {
        if (AppUtils.isNetworkAvailable(this)) {

            ProgressDlg.showProgressDialog(this, null, null);
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            postGenerateOtp = new HashMap<>();
            postGenerateOtp.put("country_code", country_code);
            postGenerateOtp.put("mobileno", mobile_no);
            postGenerateOtp.put("email", email);
            postGenerateOtp.put("name", name);

            if (PreferenceStorage.getKey(AppConstants.refreshedToken) != null) {
                postGenerateOtp.put("device_id", PreferenceStorage.getKey(AppConstants.refreshedToken));
            }
            postGenerateOtp.put("device_type", "Android");

            if (!referralCode.isEmpty())
                postGenerateOtp.put("get_code", referralCode);
            else
                postGenerateOtp.put("get_code", "");


            Call<DAOGenerateOTP> getCategories = apiInterface.postUserMobileOTP(postGenerateOtp, AppConstants.DEFAULTTOKEN);
            RetrofitHandler.executeRetrofit(this, getCategories, AppConstants.MOBILEOTP, this, false);
        }
    }

    public void postUserLogin(String mobile_no, String country_code, String otp) {
        if (AppUtils.isNetworkAvailable(this)) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            ProgressDlg.showProgressDialog(this, null, null);
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            postUserSignup.put("mobileno", mobile_no);
            postUserSignup.put("country_code", country_code);
            postUserSignup.put("otp", otp);

            Call<DAOLoginProfessional> getCategories = apiInterface.postUserLogin(postUserSignup, AppConstants.DEFAULTTOKEN);
            RetrofitHandler.executeRetrofit(this, getCategories, AppConstants.PROFESSIONALLOGIN, this, false);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }


    private void getLocaleData() {
        try {
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.RegisterScreen);
            registerScreenList = new Gson().fromJson(commonDataStr, LanguageResponse.Data.Language.RegisterScreen.class);
            String homeDataStr = PreferenceStorage.getKey(CommonLangModel.HomeString);
            homeStringsList = new Gson().fromJson(homeDataStr, LanguageResponse.Data.Language.HomeScreen.class);
            String createDataStr = PreferenceStorage.getKey(CommonLangModel.CreateService);
            createServiceStringsList = new Gson().fromJson(createDataStr, LanguageResponse.Data.Language.CreateService.class);
            String profileDataStr = PreferenceStorage.getKey(CommonLangModel.ProfileScreen);
            profileStringsList = new Gson().fromJson(profileDataStr, LanguageResponse.Data.Language.ProfileScreen.class);
            String providerAvailabilityScreenDataStr = PreferenceStorage.getKey(CommonLangModel.ProviderAvailabilityScreen);
            availabilityScreenList = new Gson().fromJson(providerAvailabilityScreenDataStr,
                    LanguageResponse.Data.Language.ProviderAvailabilityScreen.class);
            String settingsStr = PreferenceStorage.getKey(CommonLangModel.SettingsScreen);
            settingScreenList = new Gson().fromJson(settingsStr, LanguageResponse.Data.Language.SettingsScreen.class);
            String walletStr = PreferenceStorage.getKey(CommonLangModel.WalletScreen);
            walletScreenList = new Gson().fromJson(walletStr, LanguageResponse.Data.Language.WalletScreen.class);
            String subscriptionStr = PreferenceStorage.getKey(CommonLangModel.SubscriptionScreen);
            subscriptionScreenList = new Gson().fromJson(subscriptionStr, LanguageResponse.Data.Language.SubscriptionScreen.class);
            String bookingDetailStr = PreferenceStorage.getKey(CommonLangModel.BookingDetailService);
            bookingDetailServiceScreenList = new Gson().fromJson(bookingDetailStr, LanguageResponse.Data.Language.BookingDetailService.class);
            String bookingStr = PreferenceStorage.getKey(CommonLangModel.BookingService);
            bookingServiceScreenList = new Gson().fromJson(bookingStr, LanguageResponse.Data.Language.BookingService.class);
            String accountSettingsStr = PreferenceStorage.getKey(CommonLangModel.AccountSettingsScreen);
            accountSettingsScreenList = new Gson().fromJson(accountSettingsStr, LanguageResponse.Data.Language.AccountSettingsScreen.class);
            String commonStr = PreferenceStorage.getKey(CommonLangModel.CommonString);
            commonStringList = new Gson().fromJson(commonStr, LanguageResponse.Data.Language.CommonStrings.class);
        } catch (Exception e) {
            registerScreenList = new LanguageResponse().new Data().new Language().new RegisterScreen();
            homeStringsList = new LanguageResponse().new Data().new Language().new HomeScreen();
            createServiceStringsList = new LanguageResponse().new Data().new Language().new CreateService();
            profileStringsList = new LanguageResponse().new Data().new Language().new ProfileScreen();
            availabilityScreenList = new LanguageResponse().new Data().new Language().new ProviderAvailabilityScreen();
            settingScreenList = new LanguageResponse().new Data().new Language().new SettingsScreen();
            walletScreenList = new LanguageResponse().new Data().new Language().new WalletScreen();
            subscriptionScreenList = new LanguageResponse().new Data().new Language().new SubscriptionScreen();
            bookingDetailServiceScreenList = new LanguageResponse().new Data().new Language().new BookingDetailService();
            bookingServiceScreenList = new LanguageResponse().new Data().new Language().new BookingService();
            commonStringList = new LanguageResponse().new Data().new Language().new CommonStrings();
        }
    }
}
