package analisisDeImagenes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase con métodos utilitarios para aplicar filtros sobre una lista de imágenes (Formato).
 */
public class Filtros {

    /**
     * Filtra imágenes cuyo número total de píxeles (ancho x alto) sea igual o mayor al valor especificado.
     *
     * @param lista Lista original de imágenes
     * @param pixelsMinimos Número mínimo de píxeles requerido
     * @return Lista de imágenes que cumplen la condición
     */
    public static List<Formato> filtrarPorPixels(List<Formato> lista, int pixelsMinimos) {
        return lista.stream()
                .filter(img -> img.getWidth() * img.getHeight() >= pixelsMinimos)
                .collect(Collectors.toList());
    }

    /**
     * Filtra imágenes tomadas exactamente en la fecha indicada (ignorando la hora).
     *
     * @param lista Lista original de imágenes
     * @param fecha Fecha exacta para filtrar (formato yyyy-MM-dd)
     * @return Lista de imágenes con esa fecha
     */
    public static List<Formato> filtrarPorFechaExacta(List<Formato> lista, LocalDate fecha) {
        return lista.stream()
                .filter(img -> img.getCaptureDate() != null &&
                        img.getCaptureDate().toLocalDate().equals(fecha))
                .collect(Collectors.toList());
    }

    /**
     * Filtra imágenes que contienen información GPS válida (latitud y longitud).
     *
     * @param lista Lista original de imágenes
     * @return Lista de imágenes con coordenadas GPS
     */
    public static List<Formato> filtrarConGPS(List<Formato> lista) {
        return lista.stream()
                .filter(img -> img.getGpsLatitude() != null && img.getGpsLongitude() != null)
                .collect(Collectors.toList());
    }

    /**
     * Filtra imágenes cuyo nombre de archivo contiene el texto especificado (ignorando mayúsculas/minúsculas).
     *
     * @param lista Lista original de imágenes
     * @param texto Texto a buscar dentro del nombre del archivo
     * @return Lista de imágenes cuyos nombres contienen el texto
     */
    public static List<Formato> filtrarPorNombreContiene(List<Formato> lista, String texto) {
        return lista.stream()
                .filter(img -> img.getFileName() != null &&
                               img.getFileName().toLowerCase().contains(texto.toLowerCase()))
                .collect(Collectors.toList());
    }
}
