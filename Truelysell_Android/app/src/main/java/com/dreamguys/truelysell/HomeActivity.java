package com.dreamguys.truelysell;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

import com.dreamguys.truelysell.TabBar.CustomTabBar;
import com.dreamguys.truelysell.TabBar.OnTabSelectListener;
import com.dreamguys.truelysell.datamodel.BaseResponse;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.Country;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOGenerateOTP;
import com.dreamguys.truelysell.datamodel.Phase3.DAOLoginProfessional;
import com.dreamguys.truelysell.datamodel.Phase3.LoginTypeResponse;
import com.dreamguys.truelysell.fragments.phase3.BookingsFragment;
import com.dreamguys.truelysell.fragments.phase3.ChatHistoryListFragment;
import com.dreamguys.truelysell.fragments.phase3.DashboardFragment;
import com.dreamguys.truelysell.fragments.phase3.HomeFragment;
import com.dreamguys.truelysell.fragments.phase3.SettingsFragment;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.service.GPSTracker;
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
import butterknife.ButterKnife;
import retrofit2.Call;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.dreamguys.truelysell.utils.AppConstants.REQUEST_PERMISSIONS;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, RetrofitHandler.RetrofitResHandler {

    @BindView(R.id.fragment_frame_layout)
    FrameLayout fragmentFrameLayout;
    @BindView(R.id.ctb_tabbar)
    CustomTabBar customTabBar;
    public FragmentManager aFragmentManager;

    private Location mLocation;
    String Latitude, Longitude;
    protected GoogleApiClient mGoogleApiClient;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 15000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    public ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    public ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;

    public Button btnRegister, btnVerify;
    public EditText etName, etEmailAddress, etMobileNumber, et_refferalCode;
    private TextView tvLoginUser;
    public EditText etCountryCode;
    public AlertDialog mCountryDialog, mLocationDialog, settingsDialog;
    public ListView vCountryList;
    public HomeActivity.CountryAdapter aCountryAdapter;
    ListAdapter adapter;
    public HashMap<String, String> postUserSignup = new HashMap<>();
    public HashMap<String, String> postGenerateOtp = new HashMap<>();
    public ImageView ivExitDialog;
    public LinearLayout llDialog, llCategory, llSubcategory, llRegister, llName, llEmail, ll_referral;
    public FrameLayout flOtpVerification;
    public TextView tvCatetory, tvSubCatetory, tvResendCode, tvMobileNo;
    public ApiInterface apiInterface;
    public String userType = "";
    public CheckBox cbExistingUser;
    GPSTracker gpsTracker;
    public boolean isResend;
    SearchView searchView;
    private LinearLayout ll_password, ll_mobileLayout;
    private EditText et_password;
    private TextView tv_forgotPassword;
    LanguageResponse.Data.Language.RegisterScreen registerScreenList;
    LanguageResponse.Data.Language.TabBarTitle tabBarTitleList;
    LanguageResponse.Data.Language.HomeScreen homeStringsList;
    AlertDialog dialog1;
    LanguageResponse.Data.Language.EmailLogin emailStringsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        gpsTracker = new GPSTracker(this);
        customTabBar = findViewById(R.id.ctb_tabbar);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                customTabBar.getMiddleView().setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
            }
        }

//        marshallMallowPermission();

//        if (PreferenceStorage.getKey(AppConstants.MY_LATITUDE) == null) {
//
//            ProgressDlg.showProgressDialog(this, null, null);
//        } else {
//            ProgressDlg.dismissProgressDialog();
//        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        getLocaleData();
        customTabBar
                .setNormalIcons(R.drawable.ic_menu_home_normal, R.drawable.ic_menu_chat_normal, R.drawable.ic_menu_booking_normal, R.drawable.ic_menu_settings_normal)
                .setSelectedIcons(R.drawable.ic_menu_home_selected, R.drawable.ic_menu_chat_selected, R.drawable.ic_menu_bookings_selected, R.drawable.ic_menu_settings_selected)
                // .setTitles("Home", "Chat", "Bookings", "Settings")
                .setTitles(AppUtils.cleanLangStr(this,
                        tabBarTitleList.getTabHome().getName(), R.string.txt_home),
                        AppUtils.cleanLangStr(this,
                                tabBarTitleList.getTabChat().getName(), R.string.txt_chat),
                        AppUtils.cleanLangStr(this,
                                tabBarTitleList.getTabBookings().getName(), R.string.txt_bookings),
                        AppUtils.cleanLangStr(this,
                                tabBarTitleList.getTabSettings().getName(), R.string.txt_settings))
                .generate();

        customTabBar.setNormalColor(getResources().getColor(R.color.colorDarkRed));
        customTabBar.setSelectedColor(getResources().getColor(R.color.colorDarkRed));

        if (PreferenceStorage.getKey(AppConstants.USER_TYPE) != null && PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("2")) {
            updateContent(new HomeFragment(HomeActivity.this), AppConstants.HOMEFRAGMENT, null, R.id.fragment_frame_layout);
        } else if (PreferenceStorage.getKey(AppConstants.USER_TYPE) != null && PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("1")) {
            updateContent(new DashboardFragment(), AppConstants.PROVIDERDASHBOARD, null, R.id.fragment_frame_layout);
        } else {
            updateContent(new HomeFragment(HomeActivity.this), AppConstants.HOMEFRAGMENT, null, R.id.fragment_frame_layout);
        }

        customTabBar.getMiddleView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        customTabBar.setTabListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position) {
                    case 0:
                        if (PreferenceStorage.getKey(AppConstants.USER_TYPE) != null && PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("2")) {
                            updateContent(new HomeFragment(HomeActivity.this), AppConstants.HOMEFRAGMENT, null, R.id.fragment_frame_layout);
                        } else if (PreferenceStorage.getKey(AppConstants.USER_TYPE) != null && PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("1")) {
                            updateContent(new DashboardFragment(), AppConstants.PROVIDERDASHBOARD, null, R.id.fragment_frame_layout);
                        } else {
                            updateContent(new HomeFragment(HomeActivity.this), AppConstants.HOMEFRAGMENT, null, R.id.fragment_frame_layout);
                        }
                        break;
                    case 1:
                        updateContent(new ChatHistoryListFragment(HomeActivity.this), AppConstants.CHATFRAGMENT, null, R.id.fragment_frame_layout);
                        break;
                    case 2:
                        updateContent(new BookingsFragment(HomeActivity.this), AppConstants.BOOKINGFRAGMENT, null, R.id.fragment_frame_layout);
                        break;
                    case 3:
                        updateContent(new SettingsFragment(HomeActivity.this), AppConstants.SETTINGSFRAGMENT, null, R.id.fragment_frame_layout);
                        break;

                }
            }

            @Override
            public boolean onInterruptSelect(int index) {
                return false;
            }
        });

    }

    public void updateContent(Fragment aFragment, String tag, Bundle aBundle, int containerId) {
        try {

            Log.e("TAG Screen name", tag);
            // Initialise Fragment Manager
            aFragmentManager = getSupportFragmentManager();
            FragmentTransaction aTransaction = aFragmentManager.beginTransaction();
            if (aBundle != null) {
                aFragment.setArguments(aBundle);
            }

            if (!tag.equalsIgnoreCase(AppConstants.PROVIDERDASHBOARD) && !tag.equalsIgnoreCase(AppConstants.HOMEFRAGMENT) && !tag.equalsIgnoreCase(AppConstants.CHATFRAGMENT) && !tag.equalsIgnoreCase(AppConstants.SETTINGSFRAGMENT) && !tag.equalsIgnoreCase(AppConstants.BOOKINGFRAGMENT)) {
                aTransaction.addToBackStack(tag);
                aTransaction.add(containerId, aFragment, tag);
            } else {
                aTransaction.replace(containerId, aFragment, null);
            }
            aTransaction.commitAllowingStateLoss();
            Log.i("TAG", "FRAGMENT_COUNT" + aFragmentManager.getFragments().size());
        } catch (StackOverflowError | Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;

        if (PreferenceStorage.getKey(AppConstants.MY_LATITUDE) == null
                && PreferenceStorage.getKey(AppConstants.MY_LONGITUDE) == null) {
            ProgressDlg.dismissProgressDialog();
            if (mLocation != null) {
                Latitude = String.valueOf(mLocation.getLatitude());
                Longitude = String.valueOf(mLocation.getLongitude());
                PreferenceStorage.setKey(AppConstants.MY_LATITUDE, Latitude);
                PreferenceStorage.setKey(AppConstants.MY_LONGITUDE, Longitude);
                PreferenceStorage.setKey(AppConstants.MY_ADDRESS, getLocation());
                PreferenceStorage.setKey(AppConstants.MY_CITYLOCATION, getLocationArea());

                if (PreferenceStorage.getKey(AppConstants.USER_TYPE) != null && PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("2")) {
                    updateContent(new HomeFragment(this), AppConstants.HOMEFRAGMENT, null, R.id.fragment_frame_layout);
                } else if (PreferenceStorage.getKey(AppConstants.USER_TYPE) != null && PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("1")) {
                    updateContent(new DashboardFragment(), AppConstants.PROVIDERDASHBOARD, null, R.id.fragment_frame_layout);
                } else {
                    updateContent(new HomeFragment(this), AppConstants.HOMEFRAGMENT, null, R.id.fragment_frame_layout);
                }
            }
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
        Log.d("TAG_BASE", "Base Resumed");
        if (!checkPlayServices()) {
            Toast.makeText(this, AppUtils.cleanLangStr(this, getString(R.string.err_txt_install_play_ser), R.string.err_txt_install_play_ser), Toast.LENGTH_SHORT).show();
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

    public String getLocationArea() {
        String city = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            if (mLocation != null) {
                addresses = geocoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                    city = addresses.get(0).getSubAdminArea();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return city;
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
                PreferenceStorage.setKey(AppConstants.MY_CITYLOCATION, getLocationArea());
                ProgressDlg.dismissProgressDialog();
                if (PreferenceStorage.getKey(AppConstants.USER_TYPE) != null && PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("2")) {
                    updateContent(new HomeFragment(this), AppConstants.HOMEFRAGMENT, null, R.id.fragment_frame_layout);
                } else if (PreferenceStorage.getKey(AppConstants.USER_TYPE) != null && PreferenceStorage.getKey(AppConstants.USER_TYPE).equalsIgnoreCase("1")) {
                    updateContent(new DashboardFragment(), AppConstants.PROVIDERDASHBOARD, null, R.id.fragment_frame_layout);
                } else {
                    updateContent(new HomeFragment(this), AppConstants.HOMEFRAGMENT, null, R.id.fragment_frame_layout);
                }
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
            Toast.makeText(getApplicationContext(), AppUtils.cleanLangStr(this, getString(R.string.err_txt_enable_permission), R.string.err_txt_enable_permission), Toast.LENGTH_LONG).show();

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);


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

    public void showUserLoginDialog() {
        final OtpView otpView;
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_user_signup, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);

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
        tvLoginUser = view.findViewById(R.id.tv_loginuser);
        ll_password = view.findViewById(R.id.ll_password);
        ll_mobileLayout = view.findViewById(R.id.ll_mobileLayout);
        et_password = view.findViewById(R.id.et_password);
        otpView = view.findViewById(R.id.otp_view);
        tv_forgotPassword = view.findViewById(R.id.tv_forgotPassword);

        if (PreferenceStorage.getKey(AppConstants.LOGINTYPESTR).equalsIgnoreCase("email")) {
            ll_password.setVisibility(View.VISIBLE);
            llEmail.setVisibility(View.VISIBLE);
            ll_mobileLayout.setVisibility(View.VISIBLE);

        } else {
            ll_password.setVisibility(View.GONE);
            llEmail.setVisibility(View.VISIBLE);
            ll_mobileLayout.setVisibility(View.VISIBLE);
        }

        if (AppUtils.isThemeChanged(this)) {
            btnRegister.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
            btnVerify.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
            ivExitDialog.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
            cbExistingUser.setButtonTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
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
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(otpView.getWindowToken(), 0);
                postUserLogin(etMobileNumber.getText().toString(), etCountryCode.getText().toString(), otp, "", "");
                Log.d("onOtpCompleted=>", otp);
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (otpView.getText().toString().length() < 4) {
                    Toast.makeText(HomeActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(otpView.getWindowToken(), 0);
                    postUserLogin(etMobileNumber.getText().toString(),
                            etCountryCode.getText().toString(), otpView.getText().toString(), "", "");
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
                    ll_referral.setVisibility(View.GONE);

                    btnRegister.setText(AppUtils.cleanLangStr(HomeActivity.this,
                            registerScreenList.getLblLogin().getName(), R.string.txt_login));
                    tvLoginUser.setText(AppUtils.cleanLangStr(HomeActivity.this,
                            registerScreenList.getLbl_login_as_user().getName(), R.string.txt_login_as_user));
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
                    ll_referral.setVisibility(View.VISIBLE);
                    btnRegister.setText(AppUtils.cleanLangStr(HomeActivity.this,
                            registerScreenList.getLblRegister().getName(), R.string.txt_login_as_user));
                    tvLoginUser.setText(AppUtils.cleanLangStr(HomeActivity.this,
                            registerScreenList.getJoin_as_user().getName(), R.string.txt_join_as_user));
                }
            }
        });

        etCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCountrycodeDialog();
            }
        });

        tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isResend = true;
                if (PreferenceStorage.getKey(AppConstants.LOGINTYPESTR).equalsIgnoreCase("email")) {
                    getMobileOtp("", "",
                            etEmailAddress.getText().toString().trim(), "",
                            "", et_password.getText().toString().trim());
                } else {
                    getMobileOtp(etCountryCode.getText().toString().trim(), etMobileNumber.getText().toString().trim(),
                            "", "", "", "");
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbExistingUser.isChecked()) {
                    if (PreferenceStorage.getKey(AppConstants.LOGINTYPESTR).equalsIgnoreCase("email")) {
                        if (etEmailAddress.getText().toString().isEmpty()) {
                            Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                                    registerScreenList.getTxtFldEmail().getValidation1(), R.string.txt_enter_email), Toast.LENGTH_SHORT).show();
                        } else if (et_password.getText().toString().isEmpty()) {
                            Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                                    registerScreenList.getTxtFldMobileNum().getValidation2(),
                                    R.string.err_password_empty), Toast.LENGTH_SHORT).show();
                        } else {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etMobileNumber.getWindowToken(), 0);
                            getMobileOtp("", "",
                                    etEmailAddress.getText().toString().trim(),
                                    "", "",
                                    et_password.getText().toString().trim());
                        }
                    } else {
                        if (etCountryCode.getText().toString().isEmpty()) {
                            Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                                    registerScreenList.getBtnCode().getValidation1(), R.string.txt_select_country_code), Toast.LENGTH_SHORT).show();
                        } else if (etMobileNumber.getText().toString().isEmpty()) {
                            Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                                    registerScreenList.getTxtFldMobileNum().getValidation1(), R.string.txt_enter_mobile_number), Toast.LENGTH_SHORT).show();
                        } else if (etMobileNumber.getText().toString().length() < 9 || etMobileNumber.getText().toString().length() > 15) {
                            Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                                    registerScreenList.getTxtFldMobileNum().getValidation2(),
                                    R.string.txt_enter_valid_mobile_number), Toast.LENGTH_SHORT).show();
                        } else {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(etMobileNumber.getWindowToken(), 0);
                            getMobileOtp(etCountryCode.getText().toString().trim(), etMobileNumber.getText().toString().trim(),
                                    "", "", "", "");
                        }
                    }

                } else {
                    String pass = "";

                    if (PreferenceStorage.getKey(AppConstants.LOGINTYPESTR).equalsIgnoreCase("email")) {
                        if (et_password.getText().toString().isEmpty()) {
                            Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                                    registerScreenList.getTxtFldMobileNum().getValidation2(),
                                    R.string.err_password_empty), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            pass = et_password.getText().toString();
                        }
                    }


                    if (etName.getText().toString().isEmpty()) {
                        Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                                registerScreenList.getTxtFldName().getValidation1(), R.string.txt_enter_name), Toast.LENGTH_SHORT).show();
                    } else if (etEmailAddress.getText().toString().isEmpty()) {
                        Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                                registerScreenList.getTxtFldEmail().getValidation1(), R.string.txt_enter_email), Toast.LENGTH_SHORT).show();
                    } else if (!AppUtils.isValidEmail(etEmailAddress.getText().toString())) {
                        Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                                registerScreenList.getTxtFldEmail().getValidation1(), R.string.txt_enter_valid_email), Toast.LENGTH_SHORT).show();
                    } else if (etCountryCode.getText().toString().isEmpty()) {
                        Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                                registerScreenList.getBtnCode().getValidation1(), R.string.txt_select_country_code), Toast.LENGTH_SHORT).show();
                    } else if (etMobileNumber.getText().toString().isEmpty()) {
                        Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                                registerScreenList.getTxtFldMobileNum().getValidation1(), R.string.txt_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    } else if (etMobileNumber.getText().toString().length() < 9 || etMobileNumber.getText().toString().length() > 15) {
                        Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                                registerScreenList.getTxtFldMobileNum().getValidation2(),
                                R.string.txt_enter_valid_mobile_number), Toast.LENGTH_SHORT).show();
                    } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etMobileNumber.getWindowToken(), 0);
                        getMobileOtp(etCountryCode.getText().toString().trim(),
                                etMobileNumber.getText().toString().trim(),
                                etEmailAddress.getText().toString().trim(), etName.getText().toString(),
                                et_refferalCode.getText().toString().trim(), pass);
                    }
                }
            }
        });


        ivExitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                customTabBar.setSelectTab(0);
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

    public void showCountrycodeDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mShowAddProjectView = getLayoutInflater().inflate(R.layout.dialog_country, null);
        mBuilder.setView(mShowAddProjectView);
        vCountryList = (ListView) mShowAddProjectView.findViewById(R.id.inputcountrylists);
        searchView = (SearchView) mShowAddProjectView.findViewById(R.id.searchview);
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

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.MOBILEOTP:
                DAOGenerateOTP daoGenerateOTP = (DAOGenerateOTP) myRes;
                try {
                    if (daoGenerateOTP.getResponseHeader().getResponseCode().equalsIgnoreCase("201")) {
                        Toast.makeText(this, daoGenerateOTP.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                        if (PreferenceStorage.getKey(AppConstants.LOGINTYPESTR).equalsIgnoreCase("email")) {
                            postUserLogin("", "", "",
                                    etEmailAddress.getText().toString().trim(),
                                    et_password.getText().toString().trim());
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case AppConstants.PROFESSIONALLOGIN:
                DAOLoginProfessional daoLoginProfessional = (DAOLoginProfessional) myRes;

                if (daoLoginProfessional.getData().getProviderDetails1() != null) {
                    Toast.makeText(this, daoLoginProfessional.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                    PreferenceStorage.setKey(AppConstants.PNAME, daoLoginProfessional.getData().getProviderDetails1().getName());
                    PreferenceStorage.setKey(AppConstants.PEMAIL, daoLoginProfessional.getData().getProviderDetails1().getEmail());
                    PreferenceStorage.setKey(AppConstants.PMOBILENO, daoLoginProfessional.getData().getProviderDetails1().getMobileno());
                    PreferenceStorage.setKey(AppConstants.PIMAGE, daoLoginProfessional.getData().getProviderDetails1().getProfileImg());
                    PreferenceStorage.setKey(AppConstants.USER_TOKEN, daoLoginProfessional.getData().getProviderDetails1().getToken());
                    PreferenceStorage.setKey(AppConstants.USER_TYPE, daoLoginProfessional.getData().getProviderDetails1().getType());
                    PreferenceStorage.setKey(AppConstants.SHARECODE, daoLoginProfessional.getData().getProviderDetails1().getShareCode());
                    startActivity(new Intent(this, HomeActivity.class));
                    this.finish();
                } else {
                    Toast.makeText(this, daoLoginProfessional.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

            case AppConstants.LOGINTYPE:
                LoginTypeResponse loginTypeResponse = (LoginTypeResponse) myRes;
                if (loginTypeResponse.getData() != null) {
                    PreferenceStorage.setKey(AppConstants.LOGINTYPESTR, loginTypeResponse.getData().getLoginType());
                    showUserLoginDialog();
                }
                break;

            case AppConstants.FORGOTPASSWORD:
                BaseResponse response = (BaseResponse) myRes;
                Toast.makeText(this, response.getResponseHeader().getResponseMessage(), Toast.LENGTH_SHORT).show();
                if (response.getResponseHeader().getResponseCode().equalsIgnoreCase("200")) {
                    if (dialog1.isShowing())
                        dialog1.dismiss();
                    customTabBar.setSelectTab(0);
                }
                break;
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {
        switch (responseType) {
            case AppConstants.MOBILEOTP:

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

    public void getMobileOtp(String country_code, String mobile_no, String email, String name,
                             String referralCode, String password) {
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
            } else {
                postGenerateOtp.put("device_id", "dfdfsdfsdfsdfsdfsdfsdfdsfds");
            }
            postGenerateOtp.put("device_type", "Android");

            if (!referralCode.isEmpty())
                postGenerateOtp.put("get_code", referralCode);
            else
                postGenerateOtp.put("get_code", "");
            postGenerateOtp.put("password", password);

            Call<DAOGenerateOTP> getCategories = apiInterface.postUserMobileOTP(postGenerateOtp, AppConstants.DEFAULTTOKEN);
            RetrofitHandler.executeRetrofit(this, getCategories, AppConstants.MOBILEOTP, this, false);
        }
    }

    public void postUserLogin(String mobile_no, String country_code, String otp, String email,
                              String password) {
        if (AppUtils.isNetworkAvailable(this)) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            ProgressDlg.showProgressDialog(this, null, null);
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            postUserSignup.put("mobileno", mobile_no);
            postUserSignup.put("country_code", country_code);
            postUserSignup.put("otp", otp);
            postUserSignup.put("email", email);
            postUserSignup.put("password", password);
            Call<DAOLoginProfessional> getCategories = apiInterface.postUserLogin(postUserSignup, AppConstants.DEFAULTTOKEN);
            RetrofitHandler.executeRetrofit(this, getCategories, AppConstants.PROFESSIONALLOGIN, this, false);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mLocationDialog != null) {
                        mLocationDialog.dismiss();
                    }
                    ProgressDlg.dismissProgressDialog();
                    startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                    finish();
                } else {
                    if (mLocationDialog != null) {
                        mLocationDialog.dismiss();
                    }
                    showLocationPermissionDialog();
                }
                break;
        }
    }

    public void marshallMallowPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE};
            if (!hasPermissions(this, PERMISSIONS)) {
                showLocationPermissionDialog();
            } else {
                if (gpsTracker.canGetLocation()) {
                    GPSTracker.isFromSetting = false;
//                    moveToNext();
                } else {
                    showSettingsAlert();
                }
            }
        }
    }


    public void showSettingsAlert() {
        try {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_location_permission, null);
            alertDialogBuilder.setView(view);
            alertDialogBuilder.setCancelable(false);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);
            Button btnTurnOnLoc = view.findViewById(R.id.bt_turn_loc);
            settingsDialog = alertDialogBuilder.create();
            settingsDialog.show();
            settingsDialog.getWindow().setLayout(width, height);

            btnTurnOnLoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (requestCode) {
                case 1:
                    gpsTracker = new GPSTracker(this);
                    if (gpsTracker.canGetLocation()) {
                        if (settingsDialog.isShowing())
                            settingsDialog.dismiss();
                    } else {
                        Toast.makeText(this, "Enable Location to view nearby services", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void showLocationPermissionDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_location_permission, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);
        Button btnTurnOnLoc = view.findViewById(R.id.bt_turn_loc);
//        Button btnCancel = view.findViewById(R.id.bt_cancel);
//        btnCancel.setVisibility(View.VISIBLE);
        mLocationDialog = alertDialogBuilder.create();
        mLocationDialog.show();
        mLocationDialog.getWindow().setLayout(width, height);

        btnTurnOnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE};
                ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS, REQUEST_PERMISSIONS);
            }
        });

//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

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
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.RegisterScreen);
            registerScreenList = new Gson().fromJson(commonDataStr, LanguageResponse.Data.Language.RegisterScreen.class);
            String tabBarTitleStr = PreferenceStorage.getKey(CommonLangModel.TabTitle);
            tabBarTitleList = new Gson().fromJson(tabBarTitleStr, LanguageResponse.Data.Language.TabBarTitle.class);
            String homeDataStr = PreferenceStorage.getKey(CommonLangModel.HomeString);
            homeStringsList = new Gson().fromJson(homeDataStr, LanguageResponse.Data.Language.HomeScreen.class);
            String emailDataStr = PreferenceStorage.getKey(CommonLangModel.EMAILLOGIN);
            emailStringsList = new Gson().fromJson(emailDataStr, LanguageResponse.Data.Language.EmailLogin.class);
        } catch (Exception e) {
            registerScreenList = new LanguageResponse().new Data().new Language().new RegisterScreen();
            tabBarTitleList = new LanguageResponse().new Data().new Language().new TabBarTitle();
            homeStringsList = new LanguageResponse().new Data().new Language().new HomeScreen();
            emailStringsList = new LanguageResponse().new Data().new Language().new EmailLogin();
        }
    }

    public void getLoginType() {
        if (AppUtils.isNetworkAvailable(HomeActivity.this)) {
            ProgressDlg.showProgressDialog(HomeActivity.this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                String token = AppConstants.DEFAULTTOKEN;
                if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                    token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
                }
                Call<LoginTypeResponse> classificationCall = apiService.getLoginType(token);
                RetrofitHandler.executeRetrofit(HomeActivity.this, classificationCall, AppConstants.LOGINTYPE, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(HomeActivity.this, getString(R.string.txt_enable_internet));
        }
    }

    private void showForgotPasswordDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        LayoutInflater inflater = (LayoutInflater) HomeActivity.this
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
        if (AppUtils.isThemeChanged(HomeActivity.this)) {
            btn_submit.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(HomeActivity.this)));
        }

        etEmailAddress.setHint(AppUtils.cleanLangStr(HomeActivity.this,
                registerScreenList.getTxtFldEmail().getPlaceholder(), R.string.email));
        tv_forgotPasswordTitle.setText(AppUtils.cleanLangStr(HomeActivity.this,
                emailStringsList.getBtn_forgot_password().getName(), R.string.txt_forgot_password));
        btn_submit.setText(AppUtils.cleanLangStr(HomeActivity.this,
                emailStringsList.getBtn_submit().getName(), R.string.txt_submit));

        iv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                customTabBar.setSelectTab(0);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmailAddress.getText().toString().isEmpty()) {
                    Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                            registerScreenList.getTxtFldEmail().getValidation1(), R.string.txt_enter_email), Toast.LENGTH_SHORT).show();
                } else if (!AppUtils.isValidEmail(etEmailAddress.getText().toString())) {
                    Toast.makeText(HomeActivity.this, AppUtils.cleanLangStr(HomeActivity.this,
                            registerScreenList.getTxtFldEmail().getValidation2(), R.string.txt_enter_valid_email), Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager imm = (InputMethodManager) HomeActivity.this.getSystemService(HomeActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etMobileNumber.getWindowToken(), 0);
                    callForgotPassword(etEmailAddress.getText().toString().trim());
                }
            }
        });
    }

    private void callForgotPassword(String email) {
        if (AppUtils.isNetworkAvailable(HomeActivity.this)) {
            ProgressDlg.showProgressDialog(HomeActivity.this, null, null);
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                String token = AppConstants.DEFAULTTOKEN;
                if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                    token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
                }
                Call<BaseResponse> classificationCall = apiService.callForgotPassword(token, email, "2");
                RetrofitHandler.executeRetrofit(HomeActivity.this, classificationCall, AppConstants.FORGOTPASSWORD, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }

        } else {
            AppUtils.showToast(HomeActivity.this, getString(R.string.txt_enable_internet));
        }
    }
}
