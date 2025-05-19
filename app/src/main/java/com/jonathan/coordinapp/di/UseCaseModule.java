package com.jonathan.coordinapp.di;

import com.jonathan.coordinapp.domain.repository.ScanRepository;
import com.jonathan.coordinapp.domain.usecase.LoginUseCase;
import com.jonathan.coordinapp.domain.usecase.LogoutUseCase;
import com.jonathan.coordinapp.domain.usecase.ObserveScansUseCase;
import com.jonathan.coordinapp.domain.usecase.RestoreBackupIfNeededUseCase;
import com.jonathan.coordinapp.domain.usecase.SendScanUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UseCaseModule {

    @Provides
    @Singleton
    LoginUseCase provideLogin(ScanRepository r) {
        return new LoginUseCase(r);
    }

    @Provides
    @Singleton
    SendScanUseCase provideSend(ScanRepository r) {
        return new SendScanUseCase(r);
    }

    @Provides
    @Singleton
    ObserveScansUseCase provideObs(ScanRepository r) {
        return new ObserveScansUseCase(r);
    }

    @Provides
    @Singleton
    RestoreBackupIfNeededUseCase provideRestore(ScanRepository r) {
        return new RestoreBackupIfNeededUseCase(r);
    }

    @Provides
    @Singleton
    LogoutUseCase provideLogout(ScanRepository r) {
        return new LogoutUseCase(r);
    }
}


