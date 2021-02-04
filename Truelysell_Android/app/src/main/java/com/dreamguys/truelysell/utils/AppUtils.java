package com.dreamguys.truelysell.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.ScaleAnimation;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamguys.truelysell.BuildConfig;
import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.Country;
import com.dreamguys.truelysell.datamodel.LanguageResponse;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AppUtils {

    //Check for network
    public static boolean isNetworkAvailable(final Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static String getDeviceId(Context context) {
        String androidId = Settings.Secure.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String readEncodedJsonString(Context context) throws java.io.IOException {
        String base64 = context.getResources().getString(R.string.countries_code);
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }

    public static Drawable getTintedDrawable(@NonNull Context context, @NonNull Drawable inputDrawable, @ColorInt int color) {
        Drawable wrapDrawable = DrawableCompat.wrap(inputDrawable);
        DrawableCompat.setTint(wrapDrawable, color);
        return wrapDrawable;
    }

    public static int getPrimaryAppTheme(Context mContext) {
        int appTheme = 0;
        if (PreferenceStorage.getKey(AppConstants.PRIMARYAPPTHEME) != null) {
            appTheme = Color.parseColor(PreferenceStorage.getKey(AppConstants.PRIMARYAPPTHEME));
        } else {
            appTheme = mContext.getColor(R.color.colorPrimary);
        }
        return appTheme;
    }

    public static int getSecondaryAppTheme(Context mContext) {
        int appTheme = 0;
        if (PreferenceStorage.getKey(AppConstants.SECONDARYAPPTHEME) != null) {
            appTheme = Color.parseColor(PreferenceStorage.getKey(AppConstants.SECONDARYAPPTHEME));
        } else {
            appTheme = mContext.getColor(R.color.colorYellow);
        }
        return appTheme;
    }

    public static Boolean isThemeChanged(Context mContext) {
        Boolean appTheme = false;
        if (PreferenceStorage.getKey(AppConstants.PRIMARYAPPTHEME) != null) {
            appTheme = true;
        }
        return appTheme;
    }

    public static ArrayList<Country> getCountries(Context context) {
        ArrayList<Country> toReturn = new ArrayList<>();
//        toReturn.add(new Country("RU", "Russia", "+7"));
//        toReturn.add(new Country("TJ", "Tajikistan", "+992"));
//        toReturn.add(new Country("US", "United States", "+1"));
//        return toReturn;

        try {
            JSONArray countrArray = new JSONArray(readEncodedJsonString(context));
            toReturn = new ArrayList<Country>();
            for (int i = 0; i < countrArray.length(); i++) {
                JSONObject jsonObject = countrArray.getJSONObject(i);
                String countryName = jsonObject.getString("name");
                String countryDialCode = jsonObject.getString("dial_code");
                String countryCode = jsonObject.getString("code");
                Country country = new Country(countryCode, countryName, countryDialCode);
                toReturn.add(country);
            }
            Collections.sort(toReturn, new Comparator<Country>() {
                @Override
                public int compare(Country lhs, Country rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    //Show toast message
    //@PARAM - Message
    public static void showToast(Context mContext, String message) {
        try {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            new Exception(e).printStackTrace();
        }
    }

    public static String cleanString(Context mContext, String str) {
        String str1 = "";
        if (str != null && !str.equalsIgnoreCase("")) {
            return str;
        }
        return str1;
    }

    public static void appStartIntent(Context ctx, Intent intent) {

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            ctx.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the name of given file path(String).
     *
     * @param path the path
     * @return extension Ex: file.txt
     */
    public static String getName(String path) {
        return new File(path).getName();
    }

    public static String checkFileSize(String path, Context context) {
        String fileName = new File(path).getName();
        final File filePath = new File(path);
        /* This check is for local attached file check */
        if (!path.isEmpty()) {
            if (filePath.exists()) {
                return fileSize(Long.parseLong(String.valueOf(filePath.length())));
            } else {
                return "0 KB";
            }
        } else if (path.contains("http:/") || path.contains("https:/")) {
            URL url;
            try {
                url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                return fileSize(Long.parseLong("" + urlConnection.getContentLength()));
            } catch (Exception e) {
                Log.e(null, e.getLocalizedMessage());
            }

        } else
            Toast.makeText(context, context.getString(R.string.code_invalid_url_or_file_path), Toast.LENGTH_LONG).show();

        return "0 KB";
    }

    /**
     * File size.
     *
     * @param size : a long value
     * @return - converted size with size spec(KB, MB, ....)
     */
    public static String fileSize(Long size) {
        if (size <= 0)
            return "0 Bit";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String resolveFileUri(Context context, Uri uri) {
        String path = null;
        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                } else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //TODO: If attached from drive pictures
                try {
                    path = getDataColumn(context, uri, null, null);
                } catch (Exception e) {
                    path = uri.getPath();
                    Log.e("TAG", e.getLocalizedMessage());
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                path = uri.getPath();
            } else
                path = uri.getPath();
        } catch (Exception e) {
            Log.e("TAG", e.getLocalizedMessage());
        }
        return path;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                String path = cursor.getString(column_index);
                return path;
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static int getFileType(String path) {
        try {
            MimeTypeMap map = MimeTypeMap.getSingleton();
            String ext = MimeTypeMap.getFileExtensionFromUrl(path);
            final String mimetype = map.getMimeTypeFromExtension(ext.toLowerCase(Locale.getDefault()));
            if (mimetype == null)
                return 1;
            String type = mimetype.split("/")[0];
            if (type.equals("image"))
                return 2;
            else
                return 1;
        } catch (Exception e) {
            Log.e("Error :D", "_" + e.getLocalizedMessage());
            return -1;
        }
    }

    public static String checkFileExistanceAndDownload(Context activityContext, int fileType, String path) {

        /* This check is for local attached file check */
        File destinationFile = null;
        if (path != null && !path.isEmpty() && (path.contains("http:/") || path.contains("https:/"))) {

            String fn = new File(path).getName();

            switch (fileType) {
                case 1:
                    destinationFile = new File(path);
                    break;
                case 2:
                    destinationFile = new File(path);
                    break;
            }

            if (destinationFile != null && destinationFile.exists()) {
                return destinationFile.getAbsolutePath();
            }
        } else {
            return null;
        }
        return null;
    }

    public static File createImageFile(Context context) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File output = new File(dir, "TRUELYSELL_PIC_" + System.currentTimeMillis() + ".jpg");
        return output;
    }

    public static Uri createImageFile(Context context, File output) {
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", output);
    }


    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append(",");
                        }
                        sb.append(address.getLocality()).append(",");
                        sb.append(address.getPostalCode()).append(",");
                        sb.append(address.getCountryName());
                        result = sb.toString();
                    }
                } catch (Exception e) {
                    Log.e("Location Address", "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        /*result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n\nAddress:\n" + result;*/
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", "");
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean checkCameraPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, AppConstants.MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, AppConstants.MY_PERMISSIONS_REQUEST_CAMERA);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static String cleanLangStr(Context mContext, String str1, int stringId) {
        String localeStr = mContext.getResources().getString(stringId);
        if (str1 != null && !str1.isEmpty()) {
            localeStr = str1;
        }
        return localeStr;
    }

    public static String formatDateToApp(String dateStr) {
        String date = null;
        SimpleDateFormat deviceDateFormat = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
        DateFormat serverDateFormat = new SimpleDateFormat(AppConstants.SERVER_DATE_FORMAT, Locale.ENGLISH);
        Calendar c1 = Calendar.getInstance();

        try {
            Date d1 = serverDateFormat.parse(dateStr);
            c1.setTime(d1);
            date = deviceDateFormat.format(d1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatDateToServer(String dateStr) {
        String date = null;
        SimpleDateFormat deviceDateFormat = new SimpleDateFormat(AppConstants.APP_DATE_FORMAT, Locale.ENGLISH);
        DateFormat serverDateFormat = new SimpleDateFormat(AppConstants.SERVER_DATE_FORMAT, Locale.ENGLISH);
        Calendar c1 = Calendar.getInstance();

        try {
            Date d1 = deviceDateFormat.parse(dateStr);
            c1.setTime(d1);
            date = serverDateFormat.format(d1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public synchronized static void setLangInPref(LanguageResponse myRes) {
        LanguageResponse langData = myRes;
        if (langData != null && langData.getData() != null) {
            try {
                if (langData.getData().getLanguage().getRegisterScreen() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getRegisterScreen());
                    PreferenceStorage.setKey(CommonLangModel.RegisterScreen, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getCommonStrings() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getCommonStrings());
                    PreferenceStorage.setKey(CommonLangModel.CommonString, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getHomeScreen() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getHomeScreen());
                    PreferenceStorage.setKey(CommonLangModel.HomeString, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getViewAllServices() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getViewAllServices());
                    PreferenceStorage.setKey(CommonLangModel.ViewAllServices, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getCreateService() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getCreateService());
                    PreferenceStorage.setKey(CommonLangModel.CreateService, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getTabBarTitle() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getTabBarTitle());
                    PreferenceStorage.setKey(CommonLangModel.TabTitle, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getChatScreen() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getChatScreen());
                    PreferenceStorage.setKey(CommonLangModel.ChatScreen, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getSettingsScreen() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getSettingsScreen());
                    PreferenceStorage.setKey(CommonLangModel.SettingsScreen, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getProfileScreen() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getProfileScreen());
                    PreferenceStorage.setKey(CommonLangModel.ProfileScreen, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getProviderAvailabilityScreen() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getProviderAvailabilityScreen());
                    PreferenceStorage.setKey(CommonLangModel.ProviderAvailabilityScreen, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getWalletScreen() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getWalletScreen());
                    PreferenceStorage.setKey(CommonLangModel.WalletScreen, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getSubscriptionScreen() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getSubscriptionScreen());
                    PreferenceStorage.setKey(CommonLangModel.SubscriptionScreen, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getBookingDetailService() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getBookingDetailService());
                    PreferenceStorage.setKey(CommonLangModel.BookingDetailService, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getBookingService() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getBookingService());
                    PreferenceStorage.setKey(CommonLangModel.BookingService, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getAccountSettingsScreen() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getAccountSettingsScreen());
                    PreferenceStorage.setKey(CommonLangModel.AccountSettingsScreen, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getChangePassword() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getChangePassword());
                    PreferenceStorage.setKey(CommonLangModel.ChangePasswordScreen, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (langData.getData().getLanguage().getEmailLogin() != null) {
                    String localeData = new Gson().toJson(langData.getData().getLanguage().getEmailLogin());
                    PreferenceStorage.setKey(CommonLangModel.EMAILLOGIN, localeData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public Callback getCallBack(final ImageView imageView) {
        return new Callback() {
            @Override
            public void onSuccess() {
                ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .75f, ScaleAnimation.RELATIVE_TO_SELF, .75f);
                scale.setDuration(400);
                imageView.startAnimation(scale);
            }

            @Override
            public void onError(Exception e) {

            }
        };
    }

    public static String readResponse(HttpResponse res) {
        InputStream is = null;
        String return_text = "";
        try {
            is = res.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return_text = sb.toString();
        } catch (Exception e) {

        }
        return return_text;

    }

    public static void showAlertDialog(final Activity mContext, String aMessage) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.20);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_validation);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView text = dialog.findViewById(R.id.tv_err_msg);
        text.setText(aMessage);
        TextView dialogButton = dialog.findViewById(R.id.tv_ok);
        dialogButton.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mContext.finish();
            }
        });

        dialog.show();
    }


    public static void showCustomAlertDialog(final Context mContext, String aMessage, final Intent intent) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.20);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_validation);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView text = dialog.findViewById(R.id.tv_err_msg);
        text.setText(aMessage);
        TextView dialogButton = dialog.findViewById(R.id.tv_ok);
        dialogButton.setBackgroundTintList(ColorStateList.valueOf(AppUtils.getPrimaryAppTheme(mContext)));
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static String timeAgo(String utcDateTime) {
        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date past = format.parse(utcDateTime);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());


            if (seconds < 60) {
                System.out.println(seconds + " seconds ago");
                return seconds + " seconds ago";
            } else if (minutes < 60) {
                System.out.println(minutes + " minutes ago");
                return minutes + " minutes ago";
            } else if (hours < 24) {
                System.out.println(hours + " hours ago");
                return hours + " hours ago";
            } else {
                System.out.println(days + " days ago");
                return days + " days ago";
            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return " ";
    }


}
