package com.jonathan.coordinapp.di;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    @Provides @Singleton
    static Context provideContext(Application app) {
        return app.getApplicationContext();
    }

    @Provides @Singleton
    static Gson provideGson() {
        return new GsonBuilder().setLenient().create();
    }

    @Provides @Singleton
    static RequestQueue provideRequestQueue(Context ctx) {
        return Volley.newRequestQueue(ctx);
    }
}
