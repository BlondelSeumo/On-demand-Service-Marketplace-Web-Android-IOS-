package com.dreamguys.truelysell;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import com.dreamguys.truelysell.interfaces.OnSetMyLocation;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;


/**
 * Created by Hari on 27-04-2018.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMapLoadedCallback, GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMapClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private static final String TAG = "MapActivity";
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationManager locationManager;
    boolean GpsStatus;
    int appColor;


    AutocompleteSupportFragment autocompleteFragment;
    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bt_done)
    Button btDone;

    OnSetMyLocation onSetMyLocation;
    String latitude, longitude, address, from, city;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        ButterKnife.bind(this);

        if (AppUtils.isThemeChanged(this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btDone.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(this)));
            }
        }

        if (getIntent().getStringExtra("Latitude") != null &&
                getIntent().getStringExtra("Longitude") != null) {
            latitude = getIntent().getStringExtra("Latitude");
            longitude = getIntent().getStringExtra("Longitude");
        } else {
            latitude = PreferenceStorage.getKey(AppConstants.MY_LATITUDE);
            longitude = PreferenceStorage.getKey(AppConstants.MY_LONGITUDE);
        }


        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setTitle(AppUtils.cleanLangStr(this, getString(R.string.txtchooseLocation), R.string.txtchooseLocation));
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        turnGPSOn(GpsStatus);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
        } else {
            checkLocationPermission();
        }

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        View locationButton = ((View) mapFrag.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);

        // Initialize Places.
        Places.initialize(getApplicationContext(), getString(R.string.GooglePlacesAPIKey));
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                //Toast.makeText(MapActivity.this, place.getLatLng().longitude + "Latitude" + place.getLatLng().latitude + "Place" + place.getName(), Toast.LENGTH_SHORT).show();
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 10.0f));
                Log.i(TAG, "Place: " + place.getLatLng().longitude + "Latitude" + place.getLatLng().latitude);
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                address = place.getAddress().toString();
                /*PreferenceStorage.setKey(AppConstants.LATITUDE, String.valueOf(place.getLatLng().latitude));
                PreferenceStorage.setKey(AppConstants.LONGITUDE, String.valueOf(place.getLatLng().longitude));
                PreferenceStorage.setKey(AppConstants.MAPADDRESS, place.getAddress().toString());*/
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        from = getIntent().getStringExtra("From");


    }

    @Override
    protected void onResume() {
        super.onResume();
//        getAppTheme();
//        applyThemeColor();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void turnGPSOn(boolean gpsStatus) {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!gpsStatus) { //if gps is disabled

            Toast.makeText(this, "Enable Location permission in Settings", Toast.LENGTH_SHORT).show();
            final Intent poke = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
//            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
//            poke.setData(Uri.parse("3"));
            startActivity(poke);
//            sendBroadcast(poke);
        }
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.location_Permission_Needed))
                        .setMessage(getString(R.string.location_Permission_msg))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MapActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
//        mLastLocation = location;
//
//        if (mGoogleMap != null && !isFirstTime) {
//            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10.0f));
//            isFirstTime = true;
//        }
    }

    @Override
    public void onCameraIdle() {
        LatLng latLng = mGoogleMap.getCameraPosition().target;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                String addressLine = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);

                latitude = String.valueOf(latLng.latitude);
                longitude = String.valueOf(latLng.longitude);
                address = addressLine;
                city = addresses.get(0).getSubAdminArea();
//                mActivity.bookServiceDetails.setLatitude(latitude);
//                mActivity.bookServiceDetails.setLongitude(longitude);
//                mActivity.bookServiceDetails.setLocation(addressLine);

//                mGoogleMap.clear();
//                mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(cityName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                PreferenceStorage.setKey(AppConstants.LATITUDE, String.valueOf(latLng.latitude));
//                PreferenceStorage.setKey(AppConstants.LONGITUDE, String.valueOf(latLng.longitude));
//                PreferenceStorage.setKey(AppConstants.MAPADDRESS, cityName);
//                Toast.makeText(this, addressLine, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            Log.i("TAG", "The user gestured on the map.");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
            Log.i("TAG", "The user tapped something on the map.");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
            Log.i("TAG", "The app moved the camera.");
        }
    }


    @Override
    public void onMapLoaded() {

        if (mGoogleMap != null && latitude != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)), 12.0f));
            /*fetchData(mLastLocation.getLatitude(), mLastLocation.getLongitude());*/
            /*SessionHandler.getInstance().save(MapActivity.this, Constants.LATITUDE, String.valueOf(mLastLocation.getLatitude()));
            SessionHandler.getInstance().save(MapActivity.this, Constants.LONGITUDE, String.valueOf(mLastLocation.getLongitude()));*/
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.setOnMapLoadedCallback(this);
        mGoogleMap.setOnCameraMoveStartedListener(this);
        mGoogleMap.setOnCameraMoveListener(this);
        mGoogleMap.setOnCameraMoveCanceledListener(this);
        mGoogleMap.setOnCameraIdleListener(this);
        /* mGoogleMap.setOnMapClickListener(this);*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        /*Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);

                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(cityName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                PreferenceStorage.setKey(AppConstants.LATITUDE, String.valueOf(latLng.latitude));
                PreferenceStorage.setKey(AppConstants.LONGITUDE, String.valueOf(latLng.longitude));
                PreferenceStorage.setKey(AppConstants.MAPADDRESS, cityName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @OnClick(R.id.bt_done)
    public void onViewClicked() {
        try {

            /*if (latitude != null && longitude != null) {
                ((SettingsActivity) onSetMyLocation).onLocationSet(latitude, longitude, address);
                finish();
            }

            if (PreferenceStorage.getKey(AppConstants.LATITUDE) != null && PreferenceStorage.getKey(AppConstants.LONGITUDE) != null) {
                finish();
            } else {
                PreferenceStorage.setKey(AppConstants.LATITUDE, String.valueOf(mLastLocation.getLatitude()));
                PreferenceStorage.setKey(AppConstants.LONGITUDE, String.valueOf(mLastLocation.getLongitude()));
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;

                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                String cityName = "";
                if (addresses != null && addresses.size() > 0) {
                    cityName = addresses.get(0).getAddressLine(0);
                    PreferenceStorage.setKey(AppConstants.MAPADDRESS, cityName);
                }

                latitude = String.valueOf(mLastLocation.getLatitude());
                longitude = String.valueOf(String.valueOf(mLastLocation.getLongitude()));
                address = cityName;

                ((SettingsActivity) onSetMyLocation).onLocationSet(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()), cityName);
                finish();
            }*/

            if (latitude == null || longitude == null) {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;

                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                String address1 = "";
                if (addresses != null && addresses.size() > 0) {
                    address1 = addresses.get(0).getAddressLine(0);
                }

                latitude = String.valueOf(mLastLocation.getLatitude());
                longitude = String.valueOf(String.valueOf(mLastLocation.getLongitude()));
                address = address1;
                city = addresses.get(0).getSubAdminArea();
            }
            switch (from) {
                case AppConstants.PAGE_CREATE_PROVIDER:
                    onSetMyLocation = (OnSetMyLocation) ActivityCreateService.mContext;
                    ((ActivityCreateService) onSetMyLocation).onLocationSet(latitude, longitude, address);

                    break;
                case AppConstants.PAGE_SETTINGS:
                    PreferenceStorage.setKey(AppConstants.MY_LATITUDE, latitude);
                    PreferenceStorage.setKey(AppConstants.MY_LONGITUDE, longitude);
                    PreferenceStorage.setKey(AppConstants.MY_ADDRESS, address);
                    PreferenceStorage.setKey(AppConstants.MY_CITYLOCATION, city);
//                    onSetMyLocation = (OnSetMyLocation) SettingsActivity.mContext;
//                    ((SettingsActivity) onSetMyLocation).onLocationSet(latitude, longitude, address);
                    break;
                case AppConstants.PAGE_EDIT_SERVICE:
                    onSetMyLocation = (OnSetMyLocation) EditServiceActivity.mContext;
                    ((EditServiceActivity) onSetMyLocation).onLocationSet(latitude, longitude, address);
                    break;
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void applyThemeColor() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(appColor));
        btDone.setBackgroundColor(appColor);
    }

    public int getAppTheme() {
        try {
            String themeColor = PreferenceStorage.getKey(AppConstants.APP_THEME);
            appColor = Color.parseColor(themeColor);
        } catch (Exception e) {
            appColor = getResources().getColor(R.color.colorPrimary);
        }
        return appColor;
    }
}
