package com.dreamguys.truelysell.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import com.dreamguys.truelysell.HomeActivity;
import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.NotificationUtils;
import com.dreamguys.truelysell.utils.PreferenceStorage;

/**
 * Created by Hari on 09-05-2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    private String title = "", message = "", timestamp = "";
    public static final String EXTRA_KEY_UPDATE = "EXTRA_UPDATE";
    public static final String MESSAGE = "MESSAGE";
    public static final String ACTION_MyUpdate = "com.dreamguys.truelysell.UPDATE";


    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(final String token) {
        // sending token to server
        Log.e("RefreshedToken", "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        PreferenceStorage.setKey(AppConstants.refreshedToken, token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData());

                if (PreferenceStorage.getKey(AppConstants.currentActivity) != null && !PreferenceStorage.getKey(AppConstants.currentActivity).isEmpty() && PreferenceStorage.getKey(AppConstants.currentActivity).equalsIgnoreCase("yes")) {
                    if (json != null) {
                        Intent intentUpdate = new Intent();
                        intentUpdate.setAction(ACTION_MyUpdate);
                        intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
                        intentUpdate.putExtra(EXTRA_KEY_UPDATE, json.toString());
                        intentUpdate.putExtra(MESSAGE, remoteMessage.getData().get("message"));
                        sendBroadcast(intentUpdate);
                    }
                } else {
                    handleDataMessage(json);
                }


                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
                pushNotification.putExtra("data", json.toString());
                //LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        } else {
            Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
            resultIntent.putExtra("message", message);
            showNotificationMessage(getApplicationContext(), title, message, "", resultIntent);
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject data) {
        try {
            String title = data.getString("title");
            String message = data.getString("message");
            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                //Show Notification
                Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                resultIntent.putExtra("message", message);
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                resultIntent.putExtra("message", message);
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

}
