package analisisDeImagenes;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que analiza imágenes en un directorio, extrayendo sus metadatos.
 */
public class Analizador {

    private final static List<Formato> imageList = new ArrayList<>();

    /**
     * Analiza recursivamente todos los archivos en el directorio raíz, 
     * buscando imágenes válidas con extensiones soportadas y extrayendo su información.
     *
     * @param root Ruta de inicio del análisis
     * @throws IOException si ocurre un error al recorrer el sistema de archivos
     */
    public void analyzeDirectory(Path root) throws IOException {
        imageList.clear();
        Files.walk(root)
                .filter(Files::isRegularFile)
                .filter(p -> {
                    String name = p.toString().toLowerCase();
                    return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                           name.endsWith(".png") || name.endsWith(".bmp") ||
                           name.endsWith(".gif") || name.endsWith(".webp");
                })
                .forEach(this::analyzeImage);
    }

    /**
     * Analiza una imagen individual: tamaño, nombre, fecha de captura y coordenadas GPS (si las tiene).
     *
     * @param imagePath ruta de la imagen a analizar
     */
    private void analyzeImage(Path imagePath) {
        try {
            File file = imagePath.toFile();
            BufferedImage img = ImageIO.read(file);
            if (img == null) return;

            int width = img.getWidth();
            int height = img.getHeight();

            LocalDateTime captureDate = null;
            Double gpsLat = null;
            Double gpsLon = null;

            ImageMetadata metadata = Imaging.getMetadata(file);
            if (metadata instanceof JpegImageMetadata jpegMeta) {

                TiffField dateField = jpegMeta.findEXIFValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
                if (dateField != null) {
                    String dateStr = dateField.getStringValue();
                    DateTimeFormatter exifFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
                    captureDate = LocalDateTime.parse(dateStr, exifFormat);
                }

                var gps = jpegMeta.getExif().getGPS();
                if (gps != null) {
                    gpsLat = gps.getLatitudeAsDegreesNorth();
                    gpsLon = gps.getLongitudeAsDegreesEast();
                }
            }

            imageList.add(new Formato(
                    file.getName(),
                    file.getAbsolutePath(),
                    width,
                    height,
                    captureDate,
                    gpsLat,
                    gpsLon
            ));

        } catch (IOException | ImageReadException e) {
            System.err.println("[!] Error leyendo: " + imagePath);
        }
    }

    /**
     * Devuelve la lista de imágenes analizadas.
     *
     * @return Lista de objetos Formato
     */
    public static List<Formato> getImageList() {
        return imageList;
    }
}
