package com.jonathan.coordinapp.domain.usecase;

import com.jonathan.coordinapp.domain.model.Scan;
import com.jonathan.coordinapp.domain.repository.ScanRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class SendScanUseCase {
    private final ScanRepository repo;

    @Inject
    public SendScanUseCase(ScanRepository repo) {
        this.repo = repo;
    }

    public Single<Scan> execute(String base64) {
        return repo.sendAndValidate(base64);
    }
}
