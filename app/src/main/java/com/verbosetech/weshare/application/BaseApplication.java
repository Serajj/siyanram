package com.verbosetech.weshare.application;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.onesignal.OneSignal;

/**
 * Created by sera on 4/7/20.
 */
public class BaseApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.provideUserConsent(true);
        OneSignal.startInit(this)
               .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
               .init();
    }
}
