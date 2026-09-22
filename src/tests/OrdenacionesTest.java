package tests;


import org.junit.jupiter.api.Test;

import analisisDeImagenes.Formato;
import analisisDeImagenes.Ordenaciones;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrdenacionesTest {

    @Test
    public void testOrdenarPorNombre() {
        List<Formato> lista = new ArrayList<>();
        lista.add(new Formato("zzz.jpg", "", 100, 100, null, null, null));
        lista.add(new Formato("aaa.jpg", "", 100, 100, null, null, null));
        lista.add(new Formato("mmm.jpg", "", 100, 100, null, null, null));

        Ordenaciones.ordenarPorNombre(lista);

        assertEquals("aaa.jpg", lista.get(0).getFileName());
        assertEquals("mmm.jpg", lista.get(1).getFileName());
        assertEquals("zzz.jpg", lista.get(2).getFileName());
    }

    @Test
    public void testOrdenarPorAnchoAscendente() {
        List<Formato> lista = new ArrayList<>();
        lista.add(new Formato("img1.jpg", "", 800, 100, null, null, null));
        lista.add(new Formato("img2.jpg", "", 400, 100, null, null, null));
        lista.add(new Formato("img3.jpg", "", 1200, 100, null, null, null));

        Ordenaciones.ordenarPorAncho(lista, true);

        assertEquals(400, lista.get(0).getWidth());
        assertEquals(800, lista.get(1).getWidth());
        assertEquals(1200, lista.get(2).getWidth());
    }

    @Test
    public void testOrdenarPorFechaAscendente() {
        List<Formato> lista = new ArrayList<>();
        lista.add(new Formato("img1.jpg", "", 0, 0, LocalDateTime.of(2025, 5, 20, 10, 0), null, null));
        lista.add(new Formato("img2.jpg", "", 0, 0, LocalDateTime.of(2024, 12, 25, 15, 0), null, null));
        lista.add(new Formato("img3.jpg", "", 0, 0, LocalDateTime.of(2025, 1, 1, 12, 0), null, null));

        Ordenaciones.ordenarPorFechaAscendente(lista);

        assertEquals("img2.jpg", lista.get(0).getFileName()); // más antigua
        assertEquals("img3.jpg", lista.get(1).getFileName());
        assertEquals("img1.jpg", lista.get(2).getFileName()); // más reciente
    }
}