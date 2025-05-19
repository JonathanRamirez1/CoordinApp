package com.jonathan.coordinapp.data.remote.datasource;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jonathan.coordinapp.domain.model.User;
import java.util.Objects;
import javax.inject.Inject;
import io.reactivex.rxjava3.core.Single;

public class AuthRemoteDataSource {

    private final FirebaseFirestore db;

    @Inject
    public AuthRemoteDataSource(FirebaseFirestore db) {
        this.db = db;
    }

    public Single<User> login(String user, String pass) {
        return Single.create(emitter -> {
            db.collection("Usuarios")
                    .document(user)
                    .get()
                    .addOnSuccessListener(snap -> {
                        if (!snap.exists() ||
                                !Objects.equals(snap.getString("password"), pass)) {
                            emitter.onError(new Throwable("Credenciales incorrectas"));
                        } else {
                            emitter.onSuccess(snap.toObject(User.class));
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }
}
