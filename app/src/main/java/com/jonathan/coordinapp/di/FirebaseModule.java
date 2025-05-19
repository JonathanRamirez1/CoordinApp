package com.jonathan.coordinapp.di;

import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

    @Provides
    @Singleton
    FirebaseFirestore provideFirestore() {
        return FirebaseFirestore.getInstance();
    }
}
