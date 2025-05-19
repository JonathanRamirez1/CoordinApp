package com.jonathan.coordinapp.di;

import android.app.Application;

import com.jonathan.coordinapp.CoordiApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        NetworkModule.class,
        DatabaseModule.class,
        RepositoryModule.class,
        UseCaseModule.class,
        ViewModelModule.class,
        FirebaseModule.class,
        ActivityBuilderModule.class
})
public interface AppComponent extends AndroidInjector<CoordiApp> {

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application);
    }
}
