package com.jonathan.coordinapp.presentation.viewmodel;

import androidx.lifecycle.ViewModel;

import com.jonathan.coordinapp.domain.model.Scan;
import com.jonathan.coordinapp.domain.usecase.ObserveScansUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;

public class SummaryViewModel extends ViewModel {

    private final ObserveScansUseCase observeUseCase;

    @Inject
    public SummaryViewModel(ObserveScansUseCase observeUseCase) {
        this.observeUseCase = observeUseCase;
    }

    public Flowable<List<Scan>> getScans() {
        return observeUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread());
    }
}
