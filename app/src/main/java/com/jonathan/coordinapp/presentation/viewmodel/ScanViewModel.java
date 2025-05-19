package com.jonathan.coordinapp.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jonathan.coordinapp.domain.model.Scan;
import com.jonathan.coordinapp.domain.usecase.LogoutUseCase;
import com.jonathan.coordinapp.domain.usecase.SendScanUseCase;
import com.jonathan.coordinapp.utils.LocalDbException;
import com.jonathan.coordinapp.utils.NetworkException;
import com.jonathan.coordinapp.utils.Resource;
import com.jonathan.coordinapp.utils.ValidateText;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ScanViewModel extends ViewModel {

    private final SendScanUseCase validate;
    private final CompositeDisposable cd = new CompositeDisposable();
    private final MutableLiveData<Resource<Scan>> result = new MutableLiveData<>();

    @Inject
    LogoutUseCase logoutUseCase;

    @Inject
    public ScanViewModel(SendScanUseCase validate) {
        this.validate = validate;
    }

    public LiveData<Resource<Scan>> getResult() {
        return result;
    }

    public void validateQr(String base64) {
        result.setValue(Resource.loading());
        cd.add(
                validate.execute(base64)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                scan -> result.setValue(Resource.success(scan)),
                                error -> {
                                    String msg;
                                    if (error instanceof NetworkException) {
                                        msg = "Error de red. Verifica conexión e inténtalo de nuevo.";
                                    } else if (error instanceof LocalDbException) {
                                        msg = "No se pudo guardar en la base local.";
                                    } else {
                                        msg = error.getMessage();
                                    }
                                    result.setValue(Resource.error(new Throwable(msg)));
                                }
                        )
        );
    }

    public void processRaw(String raw) {
        try {
            String base64 = raw.contains(":") ? ValidateText.toBase64IfValid(raw) : raw;
            validateQr(base64);
        } catch (IllegalArgumentException e) {
            result.setValue(Resource.error(e));
        }
    }

    public Completable logout() {
        return logoutUseCase.execute();
    }

    @Override
    protected void onCleared() {
        cd.clear();
    }
}
