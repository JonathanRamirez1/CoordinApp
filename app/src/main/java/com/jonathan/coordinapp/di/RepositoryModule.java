package com.jonathan.coordinapp.di;

import com.jonathan.coordinapp.data.repository.ScanRepositoryImpl;
import com.jonathan.coordinapp.domain.repository.ScanRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {

    @Binds
    @Singleton
    public abstract ScanRepository bindScanRepository(ScanRepositoryImpl impl);
}

