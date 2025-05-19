package com.jonathan.coordinapp.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.jonathan.coordinapp.domain.model.Scan;

public class DiffCallback {
    public static final DiffUtil.ItemCallback<Scan> DIFF =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Scan oldItem, @NonNull Scan newItem) {
                    return oldItem.etiqueta().equals(newItem.etiqueta());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Scan oldItem, @NonNull Scan newItem) {
                    return oldItem.equals(newItem);
                }
            };
}
