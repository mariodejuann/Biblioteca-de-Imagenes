package coleccionImagenes;

import java.util.Random;
import javax.imageio.ImageIO;

import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;

/**
 * Clase utilitaria para generar imágenes aleatorias con metadatos EXIF y GPS.
 */
public class CreateImages {

    private static final Random random = new Random();

    /**
     * Crea una imagen aleatoria en la ruta especificada con el ancho y alto indicados.
     * Se añaden también metadatos EXIF con fecha de creación y coordenadas GPS aleatorias.
     *
     * @param path Ruta donde se guardará la imagen final.
     * @param width Ancho de la imagen (mínimo 1 píxel).
     * @param height Alto de la imagen (mínimo 1 píxel).
     */
    public static void createRandomImage(String path, int width, int height) {
        BufferedImage image = new BufferedImage(Math.max(1, width), Math.max(1, height), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        Color backgroundColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        g2d.setBackground(backgroundColor);
        g2d.fillRect(0, 0, width, height);
        
        //Óvalo
        for (int i = 0; i < 5; i++) {
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int w = random.nextInt(Math.max(1, width / 2));
            int h = random.nextInt(Math.max(1, height / 2));
            g2d.fillOval(x, y, w, h);
        }

        g2d.dispose();

        try {
            File tempFile = new File("temp_" + System.currentTimeMillis() + ".jpg");
            ImageIO.write(image, "jpg", tempFile);

            TiffOutputSet outputSet = new TiffOutputSet();
            String fecha = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));

            TiffOutputDirectory exifDir = outputSet.getOrCreateExifDirectory();
            exifDir.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, fecha);
            exifDir.add(TiffTagConstants.TIFF_TAG_DATE_TIME, fecha);

            double lat = -90 + 180 * random.nextDouble();
            double lon = -180 + 360 * random.nextDouble();

            TiffOutputDirectory gpsDir = outputSet.getOrCreateGPSDirectory();
            gpsDir.add(GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF, lat >= 0 ? "N" : "S");
            gpsDir.add(GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF, lon >= 0 ? "E" : "W");
            gpsDir.add(GpsTagConstants.GPS_TAG_GPS_LATITUDE, toRationalTriplet(Math.abs(lat)));
            gpsDir.add(GpsTagConstants.GPS_TAG_GPS_LONGITUDE, toRationalTriplet(Math.abs(lon)));

            File finalFile = new File(path);
            try (FileOutputStream fos = new FileOutputStream(finalFile);
                 FileInputStream fis = new FileInputStream(tempFile)) {
                new ExifRewriter().updateExifMetadataLossless(fis, fos, outputSet);
            }

            tempFile.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Convierte una coordenada decimal a un triplete racional (grados, minutos, segundos).
     *
     * @param coord Coordenada en formato decimal.
     * @return Array de RationalNumber que representa la coordenada.
     */
    private static RationalNumber[] toRationalTriplet(double coord) {
        int grados = (int) coord;
        double minutosDecimal = (coord - grados) * 60;
        int minutos = (int) minutosDecimal;
        double segundos = (minutosDecimal - minutos) * 60;

        return new RationalNumber[]{
            new RationalNumber(grados, 1),
            new RationalNumber(minutos, 1),
            RationalNumber.valueOf(segundos)
        };
    }
}
