package com.jonathan.coordinapp.presentation.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.jonathan.coordinapp.databinding.ActivityLoginBinding;
import com.jonathan.coordinapp.presentation.ui.main.MainActivity;
import com.jonathan.coordinapp.presentation.viewmodel.LoginViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class LoginActivity extends DaggerAppCompatActivity {

    @Inject ViewModelProvider.Factory factory;
    private LoginViewModel vm;
    private ActivityLoginBinding bind;

    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        bind = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        vm = new ViewModelProvider(this,factory).get(LoginViewModel.class);

        bind.btnLogin.setOnClickListener(v ->
                vm.login(Objects.requireNonNull(bind.etUser.getText()).toString(),
                        Objects.requireNonNull(bind.etPass.getText()).toString())
        );

        vm.getLoginState().observe(this,res -> {
            switch(res.status){
                case LOADING -> bind.progress.setVisibility(View.VISIBLE);
                case SUCCESS -> {
                    bind.progress.setVisibility(View.GONE);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                case ERROR -> {
                    bind.progress.setVisibility(View.GONE);
                    Toast.makeText(this,res.error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
