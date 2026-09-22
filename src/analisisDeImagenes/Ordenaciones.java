package analisisDeImagenes;

import java.util.Comparator;
import java.util.List;

/**
 * Clase utilitaria para ordenar listas de objetos {@link Formato} según diferentes criterios.
 */
public class Ordenaciones {

    /**
     * Ordena la lista por fecha de captura en orden ascendente.
     * Las fechas nulas se colocan al final.
     *
     * @param lista Lista de objetos Formato a ordenar.
     */
    public static void ordenarPorFechaAscendente(List<Formato> lista) {
        lista.sort(Comparator.comparing(Formato::getCaptureDate,
                Comparator.nullsLast(Comparator.naturalOrder())));
    }

    /**
     * Ordena la lista por el ancho de la imagen.
     *
     * @param lista      Lista de objetos Formato a ordenar.
     * @param ascendente true para orden ascendente, false para descendente.
     */
    public static void ordenarPorAncho(List<Formato> lista, boolean ascendente) {
        if (ascendente) {
            lista.sort(Comparator.comparingInt(Formato::getWidth));
        } else {
            lista.sort(Comparator.comparingInt(Formato::getWidth).reversed());
        }
    }

    /**
     * Ordena la lista por el nombre del archivo en orden alfabético.
     * Ignora diferencias entre mayúsculas y minúsculas.
     *
     * @param lista Lista de objetos Formato a ordenar.
     */
    public static void ordenarPorNombre(List<Formato> lista) {
        lista.sort(Comparator.comparing(Formato::getFileName,
                Comparator.nullsLast(String::compareToIgnoreCase)));
    }
}
