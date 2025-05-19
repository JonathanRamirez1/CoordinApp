package com.jonathan.coordinapp.data.local.datasource;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jonathan.coordinapp.data.local.entity.ScanEntity;
import com.jonathan.coordinapp.data.local.mapper.ScanEntityMapper;
import com.jonathan.coordinapp.domain.model.Scan;
import com.jonathan.coordinapp.utils.LocalDbException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableTransformer;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.inject.Singleton;

@Singleton
public class LocalScanDataSource {

    private final SQLiteOpenHelper helper;

    private static final CompletableTransformer DB_ERROR_WRAP =
            upstream -> upstream.onErrorResumeNext(e -> Completable.error(new LocalDbException(e)));

    @Inject
    public LocalScanDataSource(SQLiteOpenHelper h) {
        this.helper = h;
    }

    public Completable insert(Scan d) {
        return Completable.fromAction(() -> {
                    ScanEntity e = ScanEntityMapper.toEntity(d);
                    ContentValues cv = new ContentValues();
                    cv.put("etiqueta1d", e.etiqueta);
                    cv.put("latitud", e.lat);
                    cv.put("longitud", e.lng);
                    cv.put("observacion", e.obs);
                    cv.put("timestamp", e.timestamp);
                    helper.getWritableDatabase()
                            .insertOrThrow("backup_local", null, cv);
                }).subscribeOn(Schedulers.io())
                .compose(DB_ERROR_WRAP);
    }

    public Completable insertAll(List<Scan> list) {
        return Completable.fromAction(() -> {
                    SQLiteDatabase db = helper.getWritableDatabase();
                    db.beginTransaction();
                    try {
                        for (Scan d : list) {
                            ScanEntity e = ScanEntityMapper.toEntity(d);
                            ContentValues cv = new ContentValues();
                            cv.put("etiqueta1d", e.etiqueta);
                            cv.put("latitud", e.lat);
                            cv.put("longitud", e.lng);
                            cv.put("observacion", e.obs);
                            cv.put("timestamp", e.timestamp);
                            db.insert("backup_local", null, cv);
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                }).subscribeOn(Schedulers.io())
                .compose(DB_ERROR_WRAP);
    }

    public Flowable<List<Scan>> observeAll() {
        return Flowable.interval(0, 2, TimeUnit.SECONDS, Schedulers.io())
                .map(t -> queryAll());
    }

    private List<Scan> queryAll() {
        List<Scan> list = new ArrayList<>();
        Cursor c = helper.getReadableDatabase().query(
                "backup_local", null, null, null,
                null, null, "timestamp DESC");
        while (c.moveToNext()) {
            list.add(new Scan(
                    c.getString(c.getColumnIndexOrThrow("etiqueta1d")),
                    c.getDouble(c.getColumnIndexOrThrow("latitud")),
                    c.getDouble(c.getColumnIndexOrThrow("longitud")),
                    c.getString(c.getColumnIndexOrThrow("observacion"))));
        }
        c.close();
        return list;
    }

    public Single<List<Scan>> getAllOnce() {
        return Single.fromCallable(this::queryAll)
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(e -> Single.error(new LocalDbException(e)));
    }

    public Single<Long> count() {
        return Single.fromCallable(() ->
                        DatabaseUtils.queryNumEntries(
                                helper.getReadableDatabase(), "backup_local"))
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(e -> Single.error(new LocalDbException(e)));
    }

    public Completable deleteAll() {
        return Completable.fromAction(() ->
                        helper.getWritableDatabase().delete("backup_local", null, null))
                .subscribeOn(Schedulers.io());
    }
}
