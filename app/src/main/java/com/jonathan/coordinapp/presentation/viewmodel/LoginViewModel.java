package com.jonathan.coordinapp.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jonathan.coordinapp.domain.model.User;
import com.jonathan.coordinapp.domain.usecase.LoginUseCase;
import com.jonathan.coordinapp.domain.usecase.RestoreBackupIfNeededUseCase;
import com.jonathan.coordinapp.utils.LocalDbException;
import com.jonathan.coordinapp.utils.NetworkException;
import com.jonathan.coordinapp.utils.Resource;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private final LoginUseCase loginUseCase;

    private final RestoreBackupIfNeededUseCase restoreUseCase;
    private final CompositeDisposable cd = new CompositeDisposable();
    private final MutableLiveData<Resource<User>> loginState = new MutableLiveData<>();

    @Inject
    public LoginViewModel(LoginUseCase loginUseCase,
                          RestoreBackupIfNeededUseCase restoreUseCase) {
        this.loginUseCase = loginUseCase;
        this.restoreUseCase = restoreUseCase;
    }

    public LiveData<Resource<User>> getLoginState() {
        return loginState;
    }

    public void login(String u, String p) {
        loginState.setValue(Resource.loading());
        cd.add(
                loginUseCase.execute(u, p)
                        .flatMap(user ->
                                restoreUseCase.execute().andThen(Single.just(user))
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                user -> loginState.setValue(Resource.success(user)),
                                err -> {
                                    String msg;
                                    if (err instanceof NetworkException)
                                        msg = "Error de red. Inténtalo de nuevo.";
                                    else if (err instanceof LocalDbException)
                                        msg = "No se pudo acceder a la BD local.";
                                    else
                                        msg = err.getMessage();

                                    loginState.setValue(Resource.error(new Throwable(msg)));
                                }
                        )
        );
    }

    @Override
    protected void onCleared() {
        cd.clear();
    }
}
