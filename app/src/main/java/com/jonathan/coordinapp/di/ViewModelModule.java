package com.jonathan.coordinapp.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.jonathan.coordinapp.presentation.viewmodel.LoginViewModel;
import com.jonathan.coordinapp.presentation.viewmodel.ScanViewModel;
import com.jonathan.coordinapp.presentation.viewmodel.SummaryViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindFactory(ViewModelFactory factory);

    @Binds @IntoMap @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginVM(LoginViewModel vm);

    @Binds @IntoMap @ViewModelKey(ScanViewModel.class)
    abstract ViewModel bindScanVM(ScanViewModel vm);

    @Binds @IntoMap @ViewModelKey(SummaryViewModel.class)
    abstract ViewModel bindSummaryVM(SummaryViewModel vm);
}
