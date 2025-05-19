package com.jonathan.coordinapp;

import android.app.Application;

import com.jonathan.coordinapp.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class CoordiApp extends Application implements HasAndroidInjector {

    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.factory().create(this).inject(this);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
}
