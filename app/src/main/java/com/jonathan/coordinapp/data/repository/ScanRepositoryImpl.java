package com.jonathan.coordinapp.data.repository;

import android.util.Log;

import com.jonathan.coordinapp.data.local.datasource.LocalScanDataSource;
import com.jonathan.coordinapp.data.remote.datasource.AuthRemoteDataSource;
import com.jonathan.coordinapp.data.remote.datasource.RemoteBackupDataSource;
import com.jonathan.coordinapp.data.remote.datasource.RemoteScanDataSource;
import com.jonathan.coordinapp.data.remote.mapper.ScanModelMapper;
import com.jonathan.coordinapp.domain.model.Scan;
import com.jonathan.coordinapp.domain.model.User;
import com.jonathan.coordinapp.domain.repository.ScanRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class ScanRepositoryImpl implements ScanRepository {

    private final RemoteScanDataSource remote;
    private final LocalScanDataSource local;
    private final AuthRemoteDataSource auth;
    private final RemoteBackupDataSource backupRemote;

    @Inject
    public ScanRepositoryImpl(
            RemoteScanDataSource remote,
            LocalScanDataSource local,
            AuthRemoteDataSource auth,
            RemoteBackupDataSource backupRemote
    ) {
        this.remote = remote;
        this.local = local;
        this.auth = auth;
        this.backupRemote = backupRemote;
    }

    @Override
    public Single<User> login(String u, String p) {
        return auth.login(u, p);
    }

    @Override
    public Single<Scan> sendAndValidate(String base64) {
        return remote.validateBase64(base64)
                .flatMap(model -> {
                    if (!ScanModelMapper.isCorrect(model))
                        return Single.error(new Throwable("Estructura incorrecta"));

                    Scan data = ScanModelMapper.toDomain(model);
                    if (data == null)
                        return Single.error(new Throwable("Estructura incorrecta"));

                    return local.insert(data)
                            .andThen(local.count()
                                    .doOnSuccess(c -> Log.d("sendAndValidate", "Rows after insert= " + c)))
                            .flatMap(cnt -> {
                                if (cnt % 5 == 0) {
                                    return local.getAllOnce()
                                            .flatMapCompletable(list -> backupRemote.pushBackup(list)
                                                    .doOnComplete(() -> Log.d("sendAndValidate", "Push COMPLETE "))
                                                    .doOnError(e -> Log.e("sendAndValidate", "Push ERROR ", e)))
                                            .andThen(Single.just(data));
                                }
                                return Single.just(data);
                            });
                });
    }


    @Override
    public Flowable<List<Scan>> observeLocal() {
        return local.observeAll();
    }

    @Override
    public Completable restoreBackupIfNeeded() {
        return local.count()
                .flatMapCompletable(cnt -> {
                    if (cnt > 0) return Completable.complete();
                    return backupRemote.fetchBackup()
                            .flatMapCompletable(local::insertAll);
                });
    }

    @Override
    public Completable logout() {
        return Completable.mergeArray(
                backupRemote.deleteBackup(),
                local.deleteAll());
    }
}
