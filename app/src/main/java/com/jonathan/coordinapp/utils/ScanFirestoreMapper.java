package com.jonathan.coordinapp.utils;

import com.jonathan.coordinapp.domain.model.Scan;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ScanFirestoreMapper {

    public static Scan fromMap(Map<String, Object> map) {
        return new Scan(
                (String) map.get("etiqueta1d"),
                ((Number) Objects.requireNonNull(map.get("latitud"))).doubleValue(),
                ((Number) Objects.requireNonNull(map.get("longitud"))).doubleValue(),
                (String) map.get("observacion")
        );
    }

    public static Map<String, Object> toMap(Scan d) {
        Map<String, Object> map = new HashMap<>();
        map.put("etiqueta1d", d.etiqueta());
        map.put("latitud", d.lat());
        map.put("longitud", d.lng());
        map.put("observacion", d.observacion());
        return map;
    }

    private ScanFirestoreMapper(){}
}
