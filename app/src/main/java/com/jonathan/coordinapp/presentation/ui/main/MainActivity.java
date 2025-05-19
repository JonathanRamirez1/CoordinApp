package com.jonathan.coordinapp.presentation.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.jonathan.coordinapp.R;
import com.jonathan.coordinapp.databinding.ActivityMainBinding;
import com.jonathan.coordinapp.presentation.ui.adapter.ScanAdapter;
import com.jonathan.coordinapp.presentation.ui.login.LoginActivity;
import com.jonathan.coordinapp.presentation.ui.map.MapActivity;
import com.jonathan.coordinapp.presentation.viewmodel.ScanViewModel;
import com.jonathan.coordinapp.presentation.viewmodel.SummaryViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProvider.Factory factory;
    private ScanViewModel scanVM;
    private SummaryViewModel summaryVM;
    private ActivityMainBinding bind;
    private GmsBarcodeScanner gScanner;
    private final CompositeDisposable disposables = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        scanVM = new ViewModelProvider(this, factory).get(ScanViewModel.class);
        summaryVM = new ViewModelProvider(this, factory).get(SummaryViewModel.class);

        setupToolbar();
        setupScanner();
        setupInputField();
        setupRecycler();
        observeViewModel();
    }

    private void setupToolbar() {
        bind.toolbar.setTitle("CoordinApp");
        bind.toolbar.setOnMenuItemClickListener(i -> {
            if (i.getItemId() == R.id.action_logout) {
                disposables.add(
                        scanVM.logout()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    startActivity(new Intent(this, LoginActivity.class));
                                    finish();
                                })
                );
                return true;
            }
            return false;
        });
    }

    private void setupScanner() {
        gScanner = GmsBarcodeScanning.getClient(this,
                new GmsBarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_QR_CODE).build());

        bind.tilManual.setEndIconOnClickListener(v ->
                gScanner.startScan()
                        .addOnSuccessListener(bc -> scanVM.processRaw(Objects.requireNonNull(bc.getRawValue())))
                        .addOnCanceledListener(() ->
                                Snackbar.make(bind.getRoot(), "Escaneo cancelado", Snackbar.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Snackbar.make(bind.getRoot(), "Error: " + e.getMessage(), Snackbar.LENGTH_LONG).show())
        );
    }

    private void setupInputField() {
        bind.etManual.setOnEditorActionListener((tv, action, ev) -> {
            if (action == EditorInfo.IME_ACTION_DONE) {
                scanVM.processRaw(tv.getText().toString());
                tv.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) imm.hideSoftInputFromWindow(tv.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }

    private void setupRecycler() {
        ScanAdapter adapter = new ScanAdapter(d -> {
            Intent i = new Intent(this, MapActivity.class);
            i.putExtra("lat", d.lat());
            i.putExtra("lng", d.lng());
            startActivity(i);
        });
        bind.recycler.setLayoutManager(new LinearLayoutManager(this));
        bind.recycler.setAdapter(adapter);

        disposables.add(
                summaryVM.getScans()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(adapter::submitList)
        );
    }

    private void observeViewModel() {
        scanVM.getResult().observe(this, res -> {
            switch (res.status) {
                case LOADING -> {
                    bind.pbLoading.setVisibility(View.VISIBLE);
                    bind.tilManual.setHelperText(null);
                }
                case SUCCESS -> {
                    bind.pbLoading.setVisibility(View.GONE);
                    Snackbar.make(bind.getRoot(), "Enviado correctamente", Snackbar.LENGTH_SHORT).show();
                }
                case ERROR -> {
                    bind.pbLoading.setVisibility(View.GONE);
                    bind.tilManual.setHelperText("Estructura incorrecta");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }
}
