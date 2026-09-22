package coleccionImagenes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

/**
 * Clase para generar una estructura de carpetas e imágenes de forma recursiva.
 */
public class FolderStructure {

    private static final Random random = new Random();

    /**
     * Crea recursivamente una estructura de carpetas e imágenes.
     *
     * @param pathActual        Ruta actual donde crear las carpetas.
     * @param maxCarpetasNivel  Número máximo de subcarpetas por nivel.
     * @param numNivelesMax     Profundidad máxima de la estructura.
     * @param nombreCarpetas    Prefijo del nombre de las carpetas.
     */
    public static void crearNivel(Path pathActual, int maxCarpetasNivel, int numNivelesMax, String nombreCarpetas) {
        if (numNivelesMax == 0) {
            return;
        }

        int numCarpetas = random.nextInt(maxCarpetasNivel) + 1;

        for (int i = 0; i < numCarpetas; i++) {
            Path nuevaCarpeta = pathActual.resolve(nombreCarpetas + "_" + random.nextInt(1000));

            try {
                Files.createDirectories(nuevaCarpeta);
                System.out.println("Carpeta creada: " + nuevaCarpeta.toAbsolutePath());

                int numImagenes = random.nextInt(20) + 1;

                for (int j = 0; j < numImagenes; j++) {
                    Path nuevaFoto = nuevaCarpeta.resolve("Foto_" + random.nextInt(1000) + ".jpg");
                    CreateImages.createRandomImage(
                        nuevaFoto.toString(),
                        1 + random.nextInt(1920),
                        1 + random.nextInt(1080)
                    );
                }

                crearNivel(nuevaCarpeta, maxCarpetasNivel, numNivelesMax - 1, nombreCarpetas);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
