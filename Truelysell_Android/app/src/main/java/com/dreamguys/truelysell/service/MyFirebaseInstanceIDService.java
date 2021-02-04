package com.dreamguys.truelysell.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import com.dreamguys.truelysell.utils.AppConstants;
import com.dreamguys.truelysell.utils.PreferenceStorage;

/**
 * Created by Hari on 09-05-2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

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

}
