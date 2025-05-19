package com.jonathan.coordinapp.data.remote.mapper;

import static org.junit.Assert.*;

import com.jonathan.coordinapp.data.remote.model.ScanModel;
import com.jonathan.coordinapp.domain.model.Scan;
import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScanModelMapperTest {

    @Test
    public void toDomain_mapsFields() {
        ScanModel m = new ScanModel();
        m.correcto = "estructura Correcta";
        m.rawData  = "etiqueta1d:X-latitud:5-longitud:-6-observacion:prueba";

        Scan d = ScanModelMapper.toDomain(m);

        assertThat(d.etiqueta()).isEqualTo("X");
        assertThat(d.lat()).isWithin(0.0001).of(5);
        assertThat(d.lng()).isWithin(0.0001).of(-6);
        assertThat(d.observacion()).isEqualTo("prueba");
    }
}
