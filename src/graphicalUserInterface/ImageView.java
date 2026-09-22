package graphicalUserInterface;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Componente Swing que muestra la imagen seleccionada,
 * adaptándola proporcionalmente al espacio disponible.
 */
public class ImageView extends JPanel {

    private JLabel imageLabel;

    public ImageView() {
        setLayout(new BorderLayout());
        imageLabel = new JLabel("No hay imagen seleccionada", SwingConstants.CENTER);
        add(imageLabel, BorderLayout.CENTER);
    }

    /**
     * Muestra la imagen de la ruta indicada, escalándola proporcionalmente
     * dentro de los límites dados.
     *
     * @param ruta     Ruta absoluta del archivo de imagen.
     * @param maxAncho Ancho máximo permitido.
     * @param maxAlto  Alto máximo permitido.
     */
    public void mostrarImagen(String ruta, int maxAncho, int maxAlto) {
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            imageLabel.setText("Imagen no encontrada");
            return;
        }

        ImageIcon iconoOriginal = new ImageIcon(ruta);
        Image imagenOriginal = iconoOriginal.getImage();

        int originalWidth = iconoOriginal.getIconWidth();
        int originalHeight = iconoOriginal.getIconHeight();

        double escala = Math.min((double) maxAncho / originalWidth, (double) maxAlto / originalHeight);
        int nuevoAncho = (int) (originalWidth * escala);
        int nuevoAlto = (int) (originalHeight * escala);

        Image imagenEscalada = imagenOriginal.getScaledInstance(nuevoAncho, nuevoAlto, Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagenEscalada);

        imageLabel.setIcon(iconoEscalado);
        imageLabel.setText(null);
    }

    /**
     * Limpia el área de visualización y muestra un mensaje por defecto.
     */
    public void limpiar() {
        imageLabel.setIcon(null);
        imageLabel.setText("No hay imagen seleccionada");
    }
}
