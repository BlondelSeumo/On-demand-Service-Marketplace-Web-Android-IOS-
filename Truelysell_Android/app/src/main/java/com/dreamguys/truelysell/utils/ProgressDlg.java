package com.dreamguys.truelysell.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import com.dreamguys.truelysell.R;
import com.dreamguys.truelysell.datamodel.CommonLangModel;
import com.dreamguys.truelysell.datamodel.LanguageModel;

public class ProgressDlg {

    private static ProgressDialog progressDialog = null;
    private static Dialog mDialog;
    private static KProgressHUD kProgressHUD;


    public static void showProgressDialog(Context context, String title, String message) {

        String content = message;
//        try {
//            LanguageModel.Common_used_texts commonData = new LanguageModel().new Common_used_texts();
//            try {
//                String commonDataStr = PreferenceStorage.getKey(CommonLangModel.common_used_texts);
//                commonData = new Gson().fromJson(commonDataStr, LanguageModel.Common_used_texts.class);
//            } catch (Exception e) {
//                commonData = new LanguageModel().new Common_used_texts();
//            }
//            progressDialog = new ProgressDialog(context);
//            //title = title == null || title.isEmpty() ? AppUtils.cleanLangStr(context, commonData.getLg7_loading(), R.string.txt_loading) : title;
//            try {
//                message = message == null || title.isEmpty() ? AppUtils.cleanLangStr(context, commonData.getLg7_please_wait(), R.string.txt_load_msg) : message;
//            } catch (Exception e) {
//                //e.printStackTrace();
//                message = context.getResources().getString(R.string.txt_load_msg);
//            }
//            /* progressDialog.setTitle(title);*/
//            progressDialog.setMessage(message);
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setCancelable(false);
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        mDialog = new Dialog(context, R.style.CustomProgressBarTheme);
//        // no tile for the dialog
//        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mDialog.setContentView(R.layout.prograss_bar_dialog);

        if (message != null) {
            kProgressHUD = KProgressHUD
                    .create(context)
                    .setDetailsLabel(message)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false)
                    .setSize(150, 150)
                    .setAnimationSpeed(1)
                    .setDimAmount(0.5f);
        } else {
            kProgressHUD = KProgressHUD
                    .create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false)
                    .setSize(100, 100)
                    .setAnimationSpeed(1)
                    .setDimAmount(0.5f);
        }

        if (kProgressHUD != null && !kProgressHUD.isShowing()) {
            kProgressHUD.show();
        }
//        mDialog.setCancelable(false);
//        mDialog.setCanceledOnTouchOutside(false);
//        mDialog.show();


    }

    public static void dismissProgressDialog() {
//        try {
//            if (progressDialog != null || progressDialog.isShowing())
//                progressDialog.dismiss();
//        } catch (Exception e) {
//        }

        try {
            if (kProgressHUD != null && kProgressHUD.isShowing()) {
                kProgressHUD.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void clearDialog() {
//        try {
//            if (progressDialog != null)
//                progressDialog.dismiss();
//        } catch (Exception e) {
//        }
//        progressDialog = null;

        try {
            if (kProgressHUD != null && kProgressHUD.isShowing()) {
                kProgressHUD.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        kProgressHUD = null;

    }
}