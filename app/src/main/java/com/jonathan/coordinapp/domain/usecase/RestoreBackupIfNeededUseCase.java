package com.jonathan.coordinapp.domain.usecase;

import com.jonathan.coordinapp.domain.repository.ScanRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

public class RestoreBackupIfNeededUseCase {
    private final ScanRepository repo;

    @Inject
    public RestoreBackupIfNeededUseCase(ScanRepository repo) {
        this.repo = repo;
    }

    public Completable execute() {
        return repo.restoreBackupIfNeeded();
    }
}

