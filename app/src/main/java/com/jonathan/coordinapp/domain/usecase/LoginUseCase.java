package com.jonathan.coordinapp.domain.usecase;

import com.jonathan.coordinapp.domain.model.User;
import com.jonathan.coordinapp.domain.repository.ScanRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class LoginUseCase {
    private final ScanRepository repo;

    @Inject
    public LoginUseCase(ScanRepository repo) {
        this.repo = repo;
    }

    public Single<User> execute(String u, String p) {
        return repo.login(u, p);
    }
}
