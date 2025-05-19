package com.jonathan.coordinapp.presentation.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.google.common.truth.Truth;

import com.jonathan.coordinapp.domain.model.User;
import com.jonathan.coordinapp.domain.usecase.LoginUseCase;
import com.jonathan.coordinapp.domain.usecase.RestoreBackupIfNeededUseCase;
import com.jonathan.coordinapp.utils.LocalDbException;
import com.jonathan.coordinapp.utils.NetworkException;
import com.jonathan.coordinapp.utils.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static org.mockito.Mockito.*;

public class LoginViewModelTest {

    @Rule public InstantTaskExecutorRule instant = new InstantTaskExecutorRule();

    private LoginUseCase loginUseCase;
    private RestoreBackupIfNeededUseCase restoreUseCase;
    private LoginViewModel viewModel;
    private Observer<Resource<User>> observer;

    @Before
    public void setUp() {
        RxJavaPlugins.setIoSchedulerHandler(s -> Schedulers.trampoline());
        RxJavaPlugins.setComputationSchedulerHandler(s -> Schedulers.trampoline());
        RxJavaPlugins.setNewThreadSchedulerHandler(s -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(s -> Schedulers.trampoline());
        RxAndroidPlugins.setMainThreadSchedulerHandler(s -> Schedulers.trampoline());

        loginUseCase   = mock(LoginUseCase.class);
        restoreUseCase = mock(RestoreBackupIfNeededUseCase.class);

        viewModel = new LoginViewModel(loginUseCase, restoreUseCase);
        observer  = mock(Observer.class);
        viewModel.getLoginState().observeForever(observer);
    }

    @After
    public void tearDown() {
        RxJavaPlugins.reset();
        viewModel.getLoginState().removeObserver(observer);
    }

    @Test
    public void login_success_emitsLoadingAndSuccess() {
        User fake = new User();
        fake.setUsuario("demo");
        fake.setNombre("Demo");

        when(loginUseCase.execute("demo", "123"))
                .thenReturn(Single.just(fake));
        when(restoreUseCase.execute())
                .thenReturn(Completable.complete());

        viewModel.login("demo", "123");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Resource<User>> captor = ArgumentCaptor.forClass(Resource.class);
        verify(observer, times(2)).onChanged(captor.capture());

        Truth.assertThat(captor.getAllValues().get(0).status).isEqualTo(Resource.Status.LOADING);
        Truth.assertThat(captor.getAllValues().get(1).status).isEqualTo(Resource.Status.SUCCESS);
        Truth.assertThat(captor.getValue().data).isEqualTo(fake);
    }

    @Test
    public void login_networkError_emitsErrorMsg() {
        when(loginUseCase.execute("demo", "bad"))
                .thenReturn(Single.error(new NetworkException(new Throwable())));

        viewModel.login("demo", "bad");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Resource<User>> captor = ArgumentCaptor.forClass(Resource.class);
        verify(observer, times(2)).onChanged(captor.capture());

        Resource<User> last = captor.getValue();
        Truth.assertThat(last.status).isEqualTo(Resource.Status.ERROR);
        Truth.assertThat(last.error.getMessage()).isEqualTo("Error de red. Inténtalo de nuevo.");
    }

    @Test
    public void login_localDbError_emitsErrorMsg() {
        when(loginUseCase.execute("demo", "ok"))
                .thenReturn(Single.error(new LocalDbException(new Throwable())));

        viewModel.login("demo", "ok");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Resource<User>> captor = ArgumentCaptor.forClass(Resource.class);
        verify(observer, times(2)).onChanged(captor.capture());

        Resource<User> last = captor.getValue();
        Truth.assertThat(last.status).isEqualTo(Resource.Status.ERROR);
        Truth.assertThat(last.error.getMessage()).isEqualTo("No se pudo acceder a la BD local.");
    }
}
