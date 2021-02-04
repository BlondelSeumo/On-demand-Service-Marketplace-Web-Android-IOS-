package com.dreamguys.truelysell.fragments.phase3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.utils.AppUtils;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.dreamguys.truelysell.ActivityBookService;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.interfaces.OnSetMyLocation;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.PreferenceStorage;


public class FragmentMapView extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMapLoadedCallback, GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMapClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    AutocompleteSupportFragment autocompleteFragment;
    SupportMapFragment mapFrag;
    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.iv_mappin)
    ImageView ivMapPin;
    Unbinder unbinder;
    private static final String TAG = "FragmentMapView";
    GoogleMap mGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationManager locationManager;
    boolean GpsStatus, isFirstTime;
    OnSetMyLocation onSetMyLocation;
    String latitude, longitude, address/*, from*/;
    ActivityBookService mActivity;
    Context mContext;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_loc_address)
    TextView tvLocAddress;


    public void mFragmentMapView(ActivityBookService createProviderActivity) {
        this.mActivity = createProviderActivity;
        this.mContext = createProviderActivity.getBaseContext();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragement_book_pick_location, container, false);
        unbinder = ButterKnife.bind(this, mView);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (AppUtils.isThemeChanged(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btnNext.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
                btnPrevious.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(getActivity())));
            }
        }

        turnGPSOn();

        tvLocAddress.setText(PreferenceStorage.getKey(AppConstants.MY_ADDRESS));

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //Location Permission already granted
            buildGoogleApiClient();
        } else {
            //Request Location Permission
            checkLocationPermission();
        }

        FragmentManager fm = getChildFragmentManager();/// getChildFragmentManager();
        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFrag == null) {
            mapFrag = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFrag).commit();
        }

        mapFrag.getMapAsync(this);

        View locationButton = ((View) mapFrag.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        locationButton.setVisibility(View.GONE);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);

        // Initialize Places.
        Places.initialize(getActivity(), getString(R.string.GooglePlacesAPIKey));
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getActivity());

        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));


        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditText) autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button)).setText("");
                autocompleteFragment.setText("");
                view.setVisibility(View.GONE);
                mGoogleMap.clear();
                latitude = "";
                longitude = "";
                address = "";
                mActivity.bookServiceDetails.setLatitude(latitude);
                mActivity.bookServiceDetails.setLongitude(longitude);
                mActivity.bookServiceDetails.setLocation(address);
            }
        });
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                //Toast.makeText(MapActivity.getActivity(), place.getLatLng().longitude + "Latitude" + place.getLatLng().latitude + "Place" + place.getName(), Toast.LENGTH_SHORT).show();
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 10.0f));
                Log.i(TAG, "Place: " + place.getLatLng().longitude + "Latitude" + place.getLatLng().latitude);
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                address = place.getAddress().toString();
                mActivity.bookServiceDetails.setLatitude(latitude);
                mActivity.bookServiceDetails.setLongitude(longitude);
                mActivity.bookServiceDetails.setLocation(address);

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


//        from = getIntent().getStringExtra("From");
        /*latitude = getIntent().getStringExtra("Latitude");
        longitude = getIntent().getStringExtra("Longitude");
        address = getIntent().getStringExtra("Address");*/

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            getActivity().sendBroadcast(poke);
        }
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.location_Permission_Needed))
                        .setMessage(getString(R.string.location_Permission_msg))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
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
        if (ContextCompat.checkSelfPermission(getActivity(),
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
//
//            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 8.0f));
//            isFirstTime = true;
//        }
    }

    @Override
    public void onCameraIdle() {
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ivMapPin.getLayoutParams());
//        lp.setMargins(0, 0, 0, 5);
//        ivMapPin.setLayoutParams(lp);

        LatLng latLng = mGoogleMap.getCameraPosition().target;
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                String addressLine = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);

                latitude = String.valueOf(latLng.latitude);
                longitude = String.valueOf(latLng.longitude);
                mActivity.bookServiceDetails.setLatitude(latitude);
                mActivity.bookServiceDetails.setLongitude(longitude);
                mActivity.bookServiceDetails.setLocation(addressLine);
                tvLocAddress.setText(addressLine);

//                mGoogleMap.clear();
//                mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(cityName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                PreferenceStorage.setKey(AppConstants.LATITUDE, String.valueOf(latLng.latitude));
//                PreferenceStorage.setKey(AppConstants.LONGITUDE, String.valueOf(latLng.longitude));
//                PreferenceStorage.setKey(AppConstants.MAPADDRESS, cityName);
//                Toast.makeText(mActivity, addressLine, Toast.LENGTH_SHORT).show();
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
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ivMapPin.getLayoutParams());
//        lp.setMargins(0, 0, 0, 5);
//        ivMapPin.setLayoutParams(lp);
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
//        if (mGoogleMap != null && mLastLocation != null) {
//            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15.0f));
//            /*fetchData(mLastLocation.getLatitude(), mLastLocation.getLongitude());*/
//            /*SessionHandler.getInstance().save(MapActivity.getActivity(), Constants.LATITUDE, String.valueOf(mLastLocation.getLatitude()));
//            SessionHandler.getInstance().save(MapActivity.getActivity(), Constants.LONGITUDE, String.valueOf(mLastLocation.getLongitude()));*/
//        }

        if (mGoogleMap != null && PreferenceStorage.getKey(AppConstants.MY_LATITUDE) != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(PreferenceStorage.getKey(AppConstants.MY_LATITUDE)), Double.parseDouble(PreferenceStorage.getKey(AppConstants.MY_LONGITUDE))), 15.0f));
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        /* mGoogleMap.setOnMapClickListener(getActivity());*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        /*Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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

    @OnClick({R.id.btn_previous, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_previous:
                mActivity.gotoNext(0);
                break;
            case R.id.btn_next:
                mActivity.gotoNext(2);
                break;
        }
    }

//    @OnClick(R.id.bt_done)
//    public void onViewClicked() {
//        try {
//
//            /*if (latitude != null && longitude != null) {
//                ((SettingsActivity) onSetMyLocation).onLocationSet(latitude, longitude, address);
//                finish();
//            }
//
//            if (PreferenceStorage.getKey(AppConstants.LATITUDE) != null && PreferenceStorage.getKey(AppConstants.LONGITUDE) != null) {
//                finish();
//            } else {
//                PreferenceStorage.setKey(AppConstants.LATITUDE, String.valueOf(mLastLocation.getLatitude()));
//                PreferenceStorage.setKey(AppConstants.LONGITUDE, String.valueOf(mLastLocation.getLongitude()));
//                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//                List<Address> addresses = null;
//
//                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
//                String cityName = "";
//                if (addresses != null && addresses.size() > 0) {
//                    cityName = addresses.get(0).getAddressLine(0);
//                    PreferenceStorage.setKey(AppConstants.MAPADDRESS, cityName);
//                }
//
//                latitude = String.valueOf(mLastLocation.getLatitude());
//                longitude = String.valueOf(String.valueOf(mLastLocation.getLongitude()));
//                address = cityName;
//
//                ((SettingsActivity) onSetMyLocation).onLocationSet(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()), cityName);
//                finish();
//            }*/
//
//            if (latitude == null || longitude == null) {
//                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//                List<Address> addresses = null;
//
//                addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
//                String cityName = "";
//                if (addresses != null && addresses.size() > 0) {
//                    cityName = addresses.get(0).getAddressLine(0);
//                }
//
//                latitude = String.valueOf(mLastLocation.getLatitude());
//                longitude = String.valueOf(String.valueOf(mLastLocation.getLongitude()));
//                address = cityName;
//            }
////            switch (from) {
////                case AppConstants.PAGE_CREATE_REQUEST:
////                    onSetMyLocation = (OnSetMyLocation) CreateRequestActivity.mContext;
////                    ((CreateRequestActivity) onSetMyLocation).onLocationSet(latitude, longitude, address);
////                    break;
////                case AppConstants.PAGE_CREATE_PROVIDER:
////                    onSetMyLocation = (OnSetMyLocation) CreateProviderActivity.mContext;
////                    ((CreateProviderActivity) onSetMyLocation).onLocationSet(latitude, longitude, address);
////
////                    break;
////                case AppConstants.PAGE_EDIT_PROVIDER:
////                    onSetMyLocation = (OnSetMyLocation) EditProviderActivity.mContext;
////                    ((EditProviderActivity) onSetMyLocation).onLocationSet(latitude, longitude, address);
////
////                    break;
////                case AppConstants.PAGE_EDIT_REQUEST:
////                    onSetMyLocation = (OnSetMyLocation) EditRequestActivity.mContext;
////                    ((EditRequestActivity) onSetMyLocation).onLocationSet(latitude, longitude, address);
////
////                    break;
////                case AppConstants.PAGE_SETTINGS:
////                    onSetMyLocation = (OnSetMyLocation) SettingsActivity.mContext;
////                    ((SettingsActivity) onSetMyLocation).onLocationSet(latitude, longitude, address);
////                    break;
////                case AppConstants.PAGE_ADVANCED_SEARCH:
////                    onSetMyLocation = (OnSetMyLocation) AdvancedSearchActivity.mContext;
////                    ((AdvancedSearchActivity) onSetMyLocation).onLocationSet(latitude, longitude, address);
////                    break;
////                case AppConstants.BOOK_PROVIDER:
////                    onSetMyLocation = (OnSetMyLocation) ProviderBookAppointment.mContext;
////                    ((ProviderBookAppointment) onSetMyLocation).onLocationSet(latitude, longitude, address);
////                    break;
////            }
//            getActivity().finish();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//    }
}
