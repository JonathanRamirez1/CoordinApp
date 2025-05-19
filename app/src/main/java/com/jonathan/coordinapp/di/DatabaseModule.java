package com.jonathan.coordinapp.di;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.jonathan.coordinapp.data.local.db.AppDbHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
@Module
public class DatabaseModule {

    @Provides @Singleton
    SQLiteOpenHelper provideDb(Context context) {
        return new AppDbHelper(context);
    }
}
