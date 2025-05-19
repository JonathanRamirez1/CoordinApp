package com.jonathan.coordinapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import javax.inject.Inject;

public class DeviceInfoProvider {

    private final Context context;

    @Inject
    public DeviceInfoProvider(Context context) {
        this.context = context;
    }

    @SuppressLint("HardwareIds")
    public String getSerial() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            return Build.SERIAL;
        }
    }
}
