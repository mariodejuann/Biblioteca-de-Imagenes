package analisisDeImagenes;

import java.time.LocalDateTime;

/**
 * Clase que representa una imagen con sus metadatos asociados.
 */
public class Formato {
    private String fileName;
    private String path;
    private int width;
    private int height;
    private LocalDateTime captureDate;
    private Double gpsLatitude;
    private Double gpsLongitude;

    /**
     * Constructor para crear un objeto Formato con todos los atributos.
     *
     * @param fileName      Nombre del archivo.
     * @param path          Ruta absoluta del archivo.
     * @param width         Ancho de la imagen en píxeles.
     * @param height        Alto de la imagen en píxeles.
     * @param captureDate   Fecha y hora de captura.
     * @param gpsLatitude   Latitud GPS (puede ser null).
     * @param gpsLongitude  Longitud GPS (puede ser null).
     */
    public Formato(String fileName, String path, int width, int height,
                   LocalDateTime captureDate, Double gpsLatitude, Double gpsLongitude) {
        this.fileName = fileName;
        this.path = path;
        this.width = width;
        this.height = height;
        this.captureDate = captureDate;
        this.gpsLatitude = gpsLatitude;
        this.gpsLongitude = gpsLongitude;
    }

    public String getFileName() { return fileName; }

    public String getPath() { return path; }

    public int getWidth() { return width; }

    public int getHeight() { return height; }

    public LocalDateTime getCaptureDate() { return captureDate; }

    public Double getGpsLatitude() { return gpsLatitude; }

    public Double getGpsLongitude() { return gpsLongitude; }

    @Override
    public String toString() {
        return String.format("%s [%dx%d] - Fecha: %s - GPS: (%s, %s)",
                fileName, width, height,
                captureDate != null ? captureDate.toString() : "n/a",
                gpsLatitude != null ? gpsLatitude : "n/a",
                gpsLongitude != null ? gpsLongitude : "n/a");
    }
}
