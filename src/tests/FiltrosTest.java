package tests;

import org.junit.jupiter.api.Test;

import analisisDeImagenes.Filtros;
import analisisDeImagenes.Formato;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FiltrosTest {

    @Test
    public void testFiltrarPorPixels() {
        List<Formato> lista = new ArrayList<>();
        lista.add(new Formato("img1.jpg", "", 100, 100, null, null, null)); // 10_000 px
        lista.add(new Formato("img2.jpg", "", 400, 300, null, null, null)); // 120_000 px
        lista.add(new Formato("img3.jpg", "", 10, 10, null, null, null));   // 100 px

        List<Formato> resultado = Filtros.filtrarPorPixels(lista, 10_000);

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(f -> f.getFileName().equals("img1.jpg")));
        assertTrue(resultado.stream().anyMatch(f -> f.getFileName().equals("img2.jpg")));
    }

    @Test
    public void testFiltrarConGPS() {
        List<Formato> lista = new ArrayList<>();
        lista.add(new Formato("conGPS.jpg", "", 0, 0, null, 40.0, -3.0));
        lista.add(new Formato("sinGPS.jpg", "", 0, 0, null, null, null));

        List<Formato> resultado = Filtros.filtrarConGPS(lista);

        assertEquals(1, resultado.size());
        assertEquals("conGPS.jpg", resultado.get(0).getFileName());
    }

    @Test
    public void testFiltrarPorNombreContiene() {
        List<Formato> lista = new ArrayList<>();
        lista.add(new Formato("vacaciones.jpg", "", 0, 0, null, null, null));
        lista.add(new Formato("cumpleaños.jpg", "", 0, 0, null, null, null));
        lista.add(new Formato("foto_123.jpg", "", 0, 0, null, null, null));

        List<Formato> resultado = Filtros.filtrarPorNombreContiene(lista, "cion");

        assertEquals(1, resultado.size());
        assertEquals("vacaciones.jpg", resultado.get(0).getFileName());
    }
}
