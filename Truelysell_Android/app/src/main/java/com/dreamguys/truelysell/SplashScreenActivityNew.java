package com.dreamguys.truelysell;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.datamodel.EmptyData;
import com.dreamguys.truelysell.datamodel.LanguageListResponse;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.dreamguys.truelysell.datamodel.Phase3.DAOCountryCodeList;
import com.dreamguys.truelysell.network.ApiClient;
import com.dreamguys.truelysell.network.ApiInterface;
import com.dreamguys.truelysell.service.GPSTracker;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.AppUtils;
import com.dreamguys.truelysell.utils.LocaleUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;
import com.dreamguys.truelysell.utils.ProgressDlg;
import com.dreamguys.truelysell.utils.RetrofitHandler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

import static com.dreamguys.truelysell.utils.AppConstants.REQUEST_PERMISSIONS;

public class SplashScreenActivityNew extends AppCompatActivity implements RetrofitHandler.RetrofitResHandler {

    ApiInterface apiInterface;
    List<DAOCountryCodeList.CountryCodes> daoCountryCodeLists = new ArrayList<>();
    GPSTracker gpsTracker;
    public AlertDialog settingsDialog;
    String lang = "en";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        gpsTracker = new GPSTracker(this);

//        try {
//            readEncodedJsonString(this);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        if (PreferenceStorage.getKey(AppConstants.refreshedToken) == null) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashScreenActivityNew.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String token = instanceIdResult.getToken();
                    Log.i("FCM Token", token);
                    SplashScreenActivityNew.this.storeRegIdInPref(token);
                    marshallMallowPermission();
//                    setLanguageSettings();
                }
            });
        } else {
            updateViews();
            marshallMallowPermission();
        }
    }

    private void storeRegIdInPref(String token) {
        PreferenceStorage.setKey(AppConstants.refreshedToken, token);
        updateViews();
    }

    private void marshallMallowPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.SET_ALARM,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.INTERNET};
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSIONS);
            } else {
                if (gpsTracker.canGetLocation()) {
                    GPSTracker.isFromSetting = false;
                    if (PreferenceStorage.getKey(AppConstants.LANGUAGE_SET) != null && PreferenceStorage.getKey(AppConstants.LANGUAGE_SET).equalsIgnoreCase("true")) {
                        moveToNext();
                    } else {
                        setLanguageSettings();
                    }

//                    moveToNext();
                } else {
                    showSettingsAlert();
                }
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

    @Override
    public void onSuccess(Object myRes, boolean isLoadMore, String responseType) {
        if (responseType.equalsIgnoreCase(AppConstants.GETLANGUAGELIST)) {
            LanguageListResponse response = (LanguageListResponse) myRes;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            LayoutInflater inflater = this.getLayoutInflater();
            View titleView = inflater.inflate(R.layout.list_custom_alert_dialog_tiltle, null);
            builder.setCustomTitle(titleView);
            builder.setAdapter(new LanguageAdapter(this, R.layout.list_item_language, response.getData()),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            lang = response.getData().get(item).getLanguageValue();
                            dialog.dismiss();
                            getAppLanguageData(lang);
                            //setLocale(lang);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (responseType.equalsIgnoreCase(AppConstants.GETLANGUAGEAPPDATA)) {
            PreferenceStorage.setKey(AppConstants.LANGUAGE_SET, "true");
            PreferenceStorage.setKey(AppConstants.MY_LANGUAGE, lang);
            AppUtils.setLangInPref((LanguageResponse) myRes);
            setLocale(lang);
        }

    }

    private void getAppLanguageData(String languageCode) {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            String token = AppConstants.DEFAULTTOKEN;
            if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
            }
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<LanguageResponse> classificationCall = apiService.getAppLanguageData(token,
                        languageCode);
                RetrofitHandler.executeRetrofit(this, classificationCall,
                        AppConstants.GETLANGUAGEAPPDATA, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }
        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }

    @Override
    public void onResponseFailure(Object myRes, boolean isLoadMore, String responseType) {
    }

    @Override
    public void onRequestFailure(Object myRes, boolean isLoadMore, String responseType) {
    }

    private class CountryCode extends AsyncTask<String, String, String> {
        String result = "";


        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpGet httpget = new HttpGet("https://restcountries.eu/rest/v2/all?fields=callingCodes;name");
            HttpClient httpclient = new DefaultHttpClient();
            try {
                HttpResponse response = httpclient.execute(httpget);
                result = AppUtils.readResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //fetching json array
            try {
                final JSONArray jsonArray = new JSONArray(result);
                if (!jsonArray.toString().isEmpty()) {
                    AppConstants.country_code_jsonResponse = jsonArray;
                    startActivity(new Intent(SplashScreenActivityNew.this, HomeActivity.class));
                    finish();
                    LocaleUtils.setLocale(new Locale(PreferenceStorage.getKey("locale")));
                    LocaleUtils.updateConfigActivity(SplashScreenActivityNew.this, getBaseContext().getResources().getConfiguration());
                } else if (jsonArray.toString().isEmpty()) {
                    Toast.makeText(SplashScreenActivityNew.this, "empty", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GPSTracker.isFromSetting = false;
                    if (gpsTracker.isGPSEnabled) {
                        GPSTracker.isFromSetting = false;
//                        moveToNext();
                        if (PreferenceStorage.getKey(AppConstants.LANGUAGE_SET) != null && PreferenceStorage.getKey(AppConstants.LANGUAGE_SET).equalsIgnoreCase("true")) {
                            moveToNext();
                        } else {
                            setLanguageSettings();
                        }
                    } else {
                        showSettingsAlert();
                    }
                } else {
                    if (PreferenceStorage.getKey(AppConstants.LANGUAGE_SET) != null && PreferenceStorage.getKey(AppConstants.LANGUAGE_SET).equalsIgnoreCase("true")) {
                        moveToNext();
                    } else {
                        setLanguageSettings();
                    }
//                    moveToNext();
                }
                break;
        }
    }

    private void moveToNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CountryCode().execute();
            }
        }, 2000);
    }

    public void updateViews() {
        if (AppUtils.isNetworkAvailable(this)) {
            String token = AppConstants.DEFAULTTOKEN;
            if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
            }
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<EmptyData> classificationCall = apiService.updateDeviceToken(token,
                        "Android", PreferenceStorage.getKey(AppConstants.refreshedToken));
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.UPDATETOKEN, this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }
        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (gpsTracker != null && gpsTracker.canGetLocation()) {

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
                    settingsDialog.dismiss();
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
//                        moveToNext();
                        if (PreferenceStorage.getKey(AppConstants.LANGUAGE_SET) != null && PreferenceStorage.getKey(AppConstants.LANGUAGE_SET).equalsIgnoreCase("true")) {
                            moveToNext();
                        } else {
                            setLanguageSettings();
                        }
                    } else {
                        Toast.makeText(this, "Enable Location to view nearby services", Toast.LENGTH_SHORT).show();
//                        moveToNext();
                        if (PreferenceStorage.getKey(AppConstants.LANGUAGE_SET) != null && PreferenceStorage.getKey(AppConstants.LANGUAGE_SET).equalsIgnoreCase("true")) {
                            moveToNext();
                        } else {
                            setLanguageSettings();
                        }
                    }
                    break;
            }
        }
    }

    private class LanguageAdapter extends ArrayAdapter<LanguageListResponse.Data> {
        List<LanguageListResponse.Data> items;
//        List<GETLanguageList.Datum> languageList = new ArrayList<>();

        public LanguageAdapter(@NonNull Context context, int resource, List<LanguageListResponse.Data> items) {
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
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

    public void setLanguageSettings() {
        if (AppUtils.isNetworkAvailable(this)) {
            ProgressDlg.showProgressDialog(this, null, null);
            String token = AppConstants.DEFAULTTOKEN;
            if (PreferenceStorage.getKey(AppConstants.USER_TOKEN) != null) {
                token = PreferenceStorage.getKey(AppConstants.USER_TOKEN);
            }
            ApiInterface apiService =
                    ApiClient.getClientNoHeader().create(ApiInterface.class);
            try {
                Call<LanguageListResponse> classificationCall = apiService.getLanguageList(token);
                RetrofitHandler.executeRetrofit(this, classificationCall, AppConstants.GETLANGUAGELIST,
                        this, false);
            } catch (Exception e) {
                ProgressDlg.dismissProgressDialog();
                e.printStackTrace();
            }
        } else {
            AppUtils.showToast(getApplicationContext(), getString(R.string.txt_enable_internet));
        }
    }

    public void setLocale(String localeName) {
        PreferenceStorage.setKey("locale", localeName);
        PreferenceStorage.setKey("localechanged", "true");
        PreferenceStorage.setKey(AppConstants.LANGUAGE_SET, "true");
        PreferenceStorage.setKey(AppConstants.Language, localeName);
        AppConstants.localeName = localeName;
        LocaleUtils.setLocale(new Locale(localeName));
        LocaleUtils.updateConfigActivity(this, getBaseContext().getResources().getConfiguration());
//        marshallMallowPermission();
        moveToNext();
    }
}
