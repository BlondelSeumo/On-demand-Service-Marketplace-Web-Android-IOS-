package com.dreamguys.truelysell.utils;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.LayoutDirection;
import android.view.ContextThemeWrapper;
import android.view.View;

import java.util.Locale;

public class LocaleUtils {

    private static Locale sLocale;

    public static void setLocale(Locale locale) {
        sLocale = locale;
        if (sLocale != null) {
            Locale.setDefault(sLocale);
        }
    }

    public static void updateConfig(ContextThemeWrapper wrapper) {
        if (sLocale != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = new Configuration();
            configuration.setLayoutDirection(sLocale);
            configuration.setLocale(sLocale);
            wrapper.applyOverrideConfiguration(configuration);
        }
    }

//    public static void updateConfig(Application app, Configuration configuration) {
//        if (sLocale != null) {
//            //Wrapping the configuration to avoid Activity endless loop
//            Configuration config = new Configuration(configuration);
//            config.locale = sLocale;
//            if (sLocale.getLanguage().equalsIgnoreCase("en")) {
//                config.setLayoutDirection(Locale.ENGLISH);
//            } else if (sLocale.getLanguage().equalsIgnoreCase("ma")) {
//                config.setLayoutDirection(Locale.ENGLISH);
//            } else {
//                config.setLayoutDirection(new Locale("ar"));
//            }
//
//            Resources res = app.getBaseContext().getResources();
//            res.updateConfiguration(config, res.getDisplayMetrics());
//
//
//        }
//    }
//
//    public static void updateConfigActivity(Activity app, Configuration configuration) {
//        if (sLocale != null) {
//            //Wrapping the configuration to avoid Activity endless loop
//            Configuration config = new Configuration(configuration);
//            config.locale = sLocale;
//            if (sLocale.getLanguage().equalsIgnoreCase("en")) {
//                config.setLayoutDirection(Locale.ENGLISH);
//            } else if (sLocale.getLanguage().equalsIgnoreCase("ma")) {
//                config.setLayoutDirection(Locale.ENGLISH);
//            } else {
//                config.setLayoutDirection(new Locale("ar"));
//            }
////            config.setLayoutDirection(sLocale);
//            Resources res = app.getBaseContext().getResources();
//            res.updateConfiguration(config, res.getDisplayMetrics());
//
//            //for API 25
//            Configuration configuration1 = res.getConfiguration();
//            configuration.setLocale(sLocale);
//            app.getApplicationContext().createConfigurationContext(configuration);
//            app.createConfigurationContext(configuration);
//
//            ((Activity) app).recreate();
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                ((Activity) app).getWindow().getDecorView().setLayoutDirection(Locale.getDefault().getLanguage().equalsIgnoreCase("ar")
//                        ? View.LAYOUT_DIRECTION_RTL : View.LAYOUT_DIRECTION_LTR);
//            }
//        }
//    }


    public static void updateConfig(Application app, Configuration configuration) {
        if (sLocale != null) {
            //Wrapping the configuration to avoid Activity endless loop
            Configuration config = new Configuration(configuration);
            config.locale = sLocale;
            config.setLayoutDirection(sLocale);
            Resources res = app.getBaseContext().getResources();
            res.updateConfiguration(config, null);
        }
    }

    public static void updateConfigActivity(Activity app, Configuration configuration) {
        if (sLocale != null) {
            //Wrapping the configuration to avoid Activity endless loop
            Configuration config = new Configuration(configuration);
            config.locale = sLocale;
            config.setLayoutDirection(sLocale);
            Resources res = app.getBaseContext().getResources();
            res.updateConfiguration(config, null);
        }
    }

}