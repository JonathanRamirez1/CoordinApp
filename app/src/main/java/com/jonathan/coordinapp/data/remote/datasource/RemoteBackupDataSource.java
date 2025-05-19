package com.jonathan.coordinapp.data.remote.datasource;

import android.os.Build;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jonathan.coordinapp.domain.model.Scan;
import com.jonathan.coordinapp.utils.DeviceInfoProvider;
import com.jonathan.coordinapp.utils.NetworkException;
import com.jonathan.coordinapp.utils.ScanFirestoreMapper;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class RemoteBackupDataSource {

    private final FirebaseFirestore db;
    private final DeviceInfoProvider device;

    @Inject
    public RemoteBackupDataSource(FirebaseFirestore db, DeviceInfoProvider device) {
        this.db = db;
        this.device = device;
    }

    public Completable pushBackup(List<Scan> list) {
        return Completable.create(emitter -> {
                    List<Map<String, Object>> plain = new ArrayList<>();
                    for (Scan d : list) plain.add(ScanFirestoreMapper.toMap(d));

                    Map<String, Object> doc = new HashMap<>();
                    doc.put("data", plain);
                    doc.put("serial", device.getSerial());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        doc.put("fecha", LocalDate.now().toString());
                    }
                    db.collection("backup")
                            .document(device.getSerial())
                            .set(doc)
                            .addOnSuccessListener(v -> {
                                emitter.onComplete();
                            })
                            .addOnFailureListener(emitter::onError);
                }).subscribeOn(Schedulers.io())
                .onErrorResumeNext(e -> Completable.error(new NetworkException(e)));
    }

    public Single<List<Scan>> fetchBackup() {
        return Single.<List<Scan>>create(emitter -> {
                    db.collection("backup")
                            .document(device.getSerial())
                            .get()
                            .addOnSuccessListener(snap -> {
                                if (!snap.exists()) {
                                    emitter.onSuccess(Collections.emptyList());
                                    return;
                                }

                                Object dataObj = snap.get("data");
                                List<Scan> list = new ArrayList<>();

                                if (dataObj != null) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(dataObj);
                                    Type type = new TypeToken<List<Map<String, Object>>>() {
                                    }.getType();
                                    List<Map<String, Object>> raw = gson.fromJson(json, type);

                                    for (Map<String, Object> m : raw) {
                                        list.add(ScanFirestoreMapper.fromMap(m));
                                    }
                                }

                                emitter.onSuccess(list);
                            })
                            .addOnFailureListener(emitter::onError);
                }).subscribeOn(Schedulers.io())
                .onErrorResumeNext(e -> Single.error(new NetworkException(e)));
    }

    public Completable deleteBackup() {
        return Completable.create(emitter ->
                        db.collection("backup")
                                .document(device.getSerial())
                                .delete()
                                .addOnSuccessListener(v -> emitter.onComplete())
                                .addOnFailureListener(emitter::onError)
                ).subscribeOn(Schedulers.io())
                .onErrorResumeNext(e -> Completable.error(new NetworkException(e)));
    }
}
