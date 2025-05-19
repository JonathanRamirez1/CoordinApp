package com.jonathan.coordinapp.presentation.ui.adapter;

import static com.jonathan.coordinapp.utils.DiffCallback.DIFF;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jonathan.coordinapp.databinding.ItemScanBinding;
import com.jonathan.coordinapp.domain.model.Scan;

import java.util.function.Consumer;

public class ScanAdapter extends ListAdapter<Scan, ScanAdapter.ViewHolderScan> {

    private final Consumer<Scan> onMap;

    public ScanAdapter(Consumer<Scan> onMap) {
        super(DIFF);
        this.onMap = onMap;
    }

    @NonNull
    @Override
    public ViewHolderScan onCreateViewHolder(@NonNull ViewGroup p, int v) {
        ItemScanBinding b = ItemScanBinding.inflate(
                LayoutInflater.from(p.getContext()), p, false);
        return new ViewHolderScan(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderScan h, int i) {
        Scan d = getItem(i);

        SpannableStringBuilder etq = new SpannableStringBuilder("etiqueta1d: ");
        etq.setSpan(new StyleSpan(Typeface.BOLD), 0, etq.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        etq.setSpan(new RelativeSizeSpan(1.1f), 0, etq.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        etq.append(d.etiqueta());

        SpannableStringBuilder obs = new SpannableStringBuilder("Observación: ");
        obs.setSpan(new StyleSpan(Typeface.BOLD), 0, obs.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        obs.setSpan(new RelativeSizeSpan(1.05f), 0, obs.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        obs.append(d.observacion());

        h.b.tvEtiqueta.setText(etq);
        h.b.tvObs.setText(obs);
        h.b.btnMapa.setOnClickListener(v -> onMap.accept(d));
    }

    public static class ViewHolderScan extends RecyclerView.ViewHolder {
        final ItemScanBinding b;

        ViewHolderScan(ItemScanBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }
    }
}
