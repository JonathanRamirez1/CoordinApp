package com.jonathan.coordinapp.domain.usecase;

import com.jonathan.coordinapp.domain.model.Scan;
import com.jonathan.coordinapp.domain.repository.ScanRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

public class ObserveScansUseCase {
    private final ScanRepository repo;

    @Inject
    public ObserveScansUseCase(ScanRepository repo) {
        this.repo = repo;
    }

    public Flowable<List<Scan>> execute() {
        return repo.observeLocal();
    }
}
