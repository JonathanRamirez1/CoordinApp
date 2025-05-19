package com.jonathan.coordinapp.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class ValidateText {

    public static final Pattern PATTERN = Pattern.compile(
            "etiqueta1d:([^\\-]+)-latitud:([-+]?[0-9.]+)-longitud:([-+]?[0-9.]+)-observacion:(.+)",
            Pattern.CASE_INSENSITIVE);


    public static String toBase64IfValid(String raw) throws IllegalArgumentException {
        if (raw == null || raw.trim().isEmpty()) throw new IllegalArgumentException("Vacío");
        if (!PATTERN.matcher(raw.trim()).matches())
            throw new IllegalArgumentException("Formato incorrecto");
        return Base64.encodeToString(raw.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
    }
}
