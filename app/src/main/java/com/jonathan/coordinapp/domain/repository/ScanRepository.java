package com.jonathan.coordinapp.domain.repository;

import com.jonathan.coordinapp.domain.model.Scan;
import com.jonathan.coordinapp.domain.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface ScanRepository {

    Single<User> login(String user, String pass);

    Single<Scan> sendAndValidate(String base64);

    Flowable<List<Scan>> observeLocal();

    Completable restoreBackupIfNeeded();

    Completable logout();
}
