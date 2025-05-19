package com.jonathan.coordinapp.domain.usecase;

import com.jonathan.coordinapp.domain.repository.ScanRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class LogoutUseCase {
    private final ScanRepository repo;

    @Inject
    public LogoutUseCase(ScanRepository repo) {
        this.repo = repo;
    }

    public Completable execute() {
        return repo.logout();
    }
}
