package com.dreamguys.truelysell;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;
import androidx.core.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageModel;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.FontsOverride;
import com.dreamguys.truelysell.utils.LocaleUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;


public class MyApplication extends Application implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener {

    PreferenceStorage preferenceStorage;
    MyApplication servProApplication;

    //Location
    private Location mLocation;
    String Latitude, Longitude;
    protected GoogleApiClient mGoogleApiClient;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 15000;  /* 15 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    public LanguageModel.Common_used_texts commonData = new LanguageModel().new Common_used_texts();
    public int appColor = 0;
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MultiDex.install(this);
        DiscreteScrollViewOptions.init(this);
        servProApplication = this;

//        if (PreferenceStorage.getKey("locale") != null) {
//            LocaleUtils.setLocale(new Locale(PreferenceStorage.getKey("locale")));
//            LocaleUtils.updateConfig(this, getBaseContext().getResources().getConfiguration());
//        }

        preferenceStorage = new PreferenceStorage(getApplicationContext());
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/POPPINS-REGULAR.TTF");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/POPPINS-REGULAR.TTF");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/POPPINS-REGULAR.TTF");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/POPPINS-REGULAR.TTF");

        if (PreferenceStorage.getKey("locale") != null) {
            LocaleUtils.setLocale(new Locale(PreferenceStorage.getKey("locale")));
            LocaleUtils.updateConfig(this, getBaseContext().getResources().getConfiguration());
        }
        getLocaleData();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        try {
            if (!checkPlayServices()) {
                Toast.makeText(this, AppUtils.cleanLangStr(this, commonData.getLg7_please_install_(), R.string.err_txt_install_play_ser), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mLocation != null) {
            Latitude = String.valueOf(mLocation.getLatitude());
            Longitude = String.valueOf(mLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public String getLatitude() {
        String latitude = Latitude;
        return latitude;
    }

    public String getLongitude() {
        String longitude = Longitude;
        return longitude;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            Latitude = String.valueOf(mLocation.getLatitude());
            Longitude = String.valueOf(mLocation.getLongitude());
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
            //TODO:
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            Toast.makeText(getApplicationContext(), AppUtils.cleanLangStr(this, commonData.getLg7_enable_permissi(), R.string.err_txt_enable_permission), Toast.LENGTH_LONG).show();
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);


    }


    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();
        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi
                    .removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            mGoogleApiClient.disconnect();
        }
    }

    private void getLocaleData() {
        try {
            String commonDataStr = PreferenceStorage.getKey(CommonLangModel.common_used_texts);
            commonData = new Gson().fromJson(commonDataStr, LanguageModel.Common_used_texts.class);
        } catch (Exception e) {
            commonData = new LanguageModel().new Common_used_texts();
        }
    }
}
