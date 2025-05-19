package com.jonathan.coordinapp.data.remote.datasource;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jonathan.coordinapp.domain.model.User;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.inject.Singleton;

@Singleton
public class AuthRemoteDataSource {

    private final FirebaseFirestore db;

    @Inject
    public AuthRemoteDataSource(FirebaseFirestore db) {
        this.db = db;
    }

    public Single<User> login(String user, String pass) {
        return Single.<User>create(emitter ->
                        db.collection("Usuarios").document(user).get()
                                .addOnSuccessListener(snap -> {
                                    if (!snap.exists() ||
                                            !Objects.equals(snap.getString("password"), pass)) {
                                        emitter.onError(new Throwable("Credenciales incorrectas"));
                                    } else {
                                        User u = snap.toObject(User.class);
                                        if (u == null) {
                                            emitter.onError(new Throwable("Usuario no encontrado"));
                                        } else {
                                            emitter.onSuccess(u);
                                        }
                                    }
                                })
                                .addOnFailureListener(emitter::onError))
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(Single::error);
    }
}
