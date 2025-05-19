package com.jonathan.coordinapp.data.local.mapper;

import com.jonathan.coordinapp.data.local.entity.ScanEntity;
import com.jonathan.coordinapp.domain.model.Scan;

public final class ScanEntityMapper {

    public static ScanEntity toEntity(Scan d) {
        return new ScanEntity(0, d.etiqueta(), d.lat(), d.lng(),
                d.observacion(), System.currentTimeMillis());
    }

    public static Scan toDomain(ScanEntity e) {
        return new Scan(e.etiqueta, e.lat, e.lng, e.obs);
    }
}

