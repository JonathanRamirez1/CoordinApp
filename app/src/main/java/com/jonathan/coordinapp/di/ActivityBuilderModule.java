package com.jonathan.coordinapp.di;

import com.jonathan.coordinapp.presentation.ui.login.LoginActivity;
import com.jonathan.coordinapp.presentation.ui.main.MainActivity;
import com.jonathan.coordinapp.presentation.ui.map.MapActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {


    @ContributesAndroidInjector
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract MapActivity bindMapActivity();
}

