package tests;

import analisisDeImagenes.Formato;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FormatoTest {

    @Test
    public void testToStringConNulls() {
        Formato f = new Formato("foto.jpg", "/ruta/foto.jpg", 640, 480, null, null, null);
        String resultado = f.toString();

        assertTrue(resultado.contains("n/a"));
    }
}


