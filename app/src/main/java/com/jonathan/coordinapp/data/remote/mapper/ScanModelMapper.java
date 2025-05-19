package com.jonathan.coordinapp.data.remote.mapper;


import static com.jonathan.coordinapp.utils.ValidateText.PATTERN;

import com.jonathan.coordinapp.data.remote.model.ScanModel;
import com.jonathan.coordinapp.domain.model.Scan;

import java.util.Objects;
import java.util.regex.Matcher;

public final class ScanModelMapper {


    public static boolean isCorrect(ScanModel m) {
        return m.correcto != null &&
                m.correcto.trim().equalsIgnoreCase("estructura Correcta");
    }

    public static Scan toDomain(ScanModel m) {
        Matcher mat = PATTERN.matcher(m.rawData.trim());
        if (!mat.matches()) return null;

        return new Scan(
                mat.group(1),
                Double.parseDouble(Objects.requireNonNull(mat.group(2))),
                Double.parseDouble(Objects.requireNonNull(mat.group(3))),
                mat.group(4));
    }
}

