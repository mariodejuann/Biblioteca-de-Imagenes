package graphicalUserInterface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import analisisDeImagenes.Formato;
import analisisDeImagenes.Ordenaciones;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * TableView muestra una tabla con los metadatos de una lista de imágenes.
 * Permite ordenación por columnas haciendo clic en los encabezados.
 */
public class TableView extends JPanel {

    private static JTable table;
    private DefaultTableModel tableModel;
    private List<Formato> imagenes;

    private final String[] columnNames = {
            "Nombre", "Ancho", "Alto", "Fecha", "Latitud", "Longitud"
    };

    /**
     * Crea una vista en forma de tabla a partir de una lista de imágenes.
     *
     * @param imagenes Lista de objetos Formato con metadatos de imágenes.
     */
    public TableView(List<Formato> imagenes) {
        this.imagenes = imagenes;
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                ordenarPorColumna(col);
                cargarImagenes(imagenes);
            }
        });

        cargarImagenes(imagenes);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    // Ordena según la columna clicada
    private void ordenarPorColumna(int col) {
        switch (col) {
            case 0 -> Ordenaciones.ordenarPorNombre(imagenes);
            case 1 -> Ordenaciones.ordenarPorAncho(imagenes, true);
            case 3 -> Ordenaciones.ordenarPorFechaAscendente(imagenes);
        }
    }

    /**
     * Carga los datos de las imágenes en la tabla.
     *
     * @param lista Lista de objetos Formato a mostrar.
     */
    public void cargarImagenes(List<Formato> lista) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Formato img : lista) {
            tableModel.addRow(new Object[] {
                    img.getFileName(),
                    img.getWidth(),
                    img.getHeight(),
                    img.getCaptureDate() != null ? img.getCaptureDate().format(formatter) : "n/a",
                    img.getGpsLatitude() != null ? img.getGpsLatitude() : "n/a",
                    img.getGpsLongitude() != null ? img.getGpsLongitude() : "n/a"
            });
        }
    }

    /**
     * Devuelve la tabla principal del componente.
     *
     * @return JTable utilizada en la vista.
     */
    public static JTable getTable() {
        return table;
    }
}
