package com.jonathan.coordinapp.utils;


import static com.google.common.truth.Truth.assertThat;
import android.os.Build;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.P)
public class ValidateTextTest {

    @Test
    public void validString_passes() {
        String s  = "etiqueta1d:123-latitud:1.1-longitud:-2.2-observacion:ok";
        String b64 = ValidateText.toBase64IfValid(s);

        String decoded = new String(
                android.util.Base64.decode(b64, android.util.Base64.NO_WRAP));

        assertThat(decoded).isEqualTo(s);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidString_throws() {
        String s = "etiqueta1d:123,latitud:1.1-longitud:-2.2-observacion:ok";
        ValidateText.toBase64IfValid(s);
    }
}
