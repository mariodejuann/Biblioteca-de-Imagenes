package graphicalUserInterface;

import analisisDeImagenes.Formato;
import analisisDeImagenes.Filtros;
import analisisDeImagenes.Analizador;
import coleccionImagenes.FolderStructure;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Interfaz principal de la aplicación: muestra un árbol de carpetas, una tabla de imágenes
 * y un visor de imágenes. Permite filtrar, refrescar y crear carpetas/imágenes.
 */
public class TreeView {

    private JLabel label;
    private JTree tree;
    private TableView tableView;
    private ImageView imageViewer;
    private Analizador analyzer;
    private List<Formato> imagenes;
    private List<Formato> imagenesOriginales;
    private FolderStructure generarCarpetasYArchivos;
    private Path ruta = Paths.get(System.getProperty("user.dir"), "imagenes-generadas");

    /**
     * Constructor que inicializa la ventana principal, árbol, tabla, visor y menús.
     */
    public TreeView() {
        try {
            Files.createDirectories(ruta);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JMenuBar menuBar = new JMenuBar();
        JFrame frame = new JFrame("Biblioteca de Imágenes");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        imagenes = new ArrayList<>();
        tableView = new TableView(imagenes);
        imageViewer = new ImageView();
        analyzer = new Analizador();
        
        //Visualización
        tableView.getTable().getSelectionModel().addListSelectionListener(e -> {
            int fila = tableView.getTable().getSelectedRow();
            if (fila >= 0) {
                int modeloFila = tableView.getTable().convertRowIndexToModel(fila);
                Formato f = imagenes.get(modeloFila);
                imageViewer.mostrarImagen(f.getPath(), 600, 400);
            } else {
                imageViewer.limpiar();
            }
        });
        
        //Construye el árbol
        File raiz = new File(ruta.toString());
        DefaultMutableTreeNode rootNode = createNodes(raiz);
        tree = new JTree(new DefaultTreeModel(rootNode));

        //Usuario selecciona carpeta en el árbol
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Object selected = tree.getLastSelectedPathComponent();
                if (selected instanceof DefaultMutableTreeNode node) {
                    Object userObject = node.getUserObject();
                    if (userObject instanceof File f && f.isDirectory()) {
                        try {
                            imagenes.clear();
                            analyzer.analyzeDirectory(f.toPath());
                            imagenes.addAll(analyzer.getImageList());
                            imagenesOriginales = new ArrayList<>(imagenes);
                            tableView.cargarImagenes(imagenes);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else if (userObject instanceof File f && esImagen(f)) {
                        imageViewer.mostrarImagen(f.getAbsolutePath(), 600, 400);
                    }
                }
            }
        });

        //Estructura:
        JScrollPane scrollTree = new JScrollPane(tree);
        scrollTree.setPreferredSize(new Dimension(250, 600));

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableView, imageViewer);
        rightSplit.setDividerLocation(300);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTree, rightSplit);
        mainSplit.setDividerLocation(250);

        
        // Menús
        JMenu refrescarMenu = new JMenu("Refrescar");
        JMenuItem refrescarArbol = new JMenuItem("Refrescar Árbol");
        refrescarArbol.addActionListener(e -> recargarArbol());

        JMenu archivoMenu = new JMenu("Archivo");
        JMenuItem crearCarpetaEImagenes = new JMenuItem("Creación aleatoria de carpetas e imágenes");
        crearCarpetaEImagenes.addActionListener(e -> {
            generarCarpetasYArchivos.crearNivel(ruta, 3, 5, "Folder");
            frame.dispose();
            new TreeView();
        });

        JMenu filtrosMenu = new JMenu("Filtros");

        JMenuItem filtrarPorPixels = new JMenuItem("Filtrar por tamaño");
        filtrarPorPixels.addActionListener(e -> {
            String pixels = JOptionPane.showInputDialog(null, "Introduce el número mínimo de píxeles (ancho x alto):", "Filtro por tamaño", JOptionPane.QUESTION_MESSAGE);
            if (pixels != null) {
                try {
                    int pixelsMinimos = Integer.parseInt(pixels);
                    imagenes = Filtros.filtrarPorPixels(imagenes, pixelsMinimos);
                    tableView.cargarImagenes(imagenes);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, introduce un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem filtrarPorFechaExacta = new JMenuItem("Filtrar por fecha exacta");
        filtrarPorFechaExacta.addActionListener(e -> {
            String fechaStr = JOptionPane.showInputDialog(null, "Introduzca la fecha de cuándo se generó la imagen (yyyy-MM-dd):", "Filtro por fecha", JOptionPane.QUESTION_MESSAGE);
            if (fechaStr != null) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate fecha = LocalDate.parse(fechaStr, formatter);
                    imagenes = Filtros.filtrarPorFechaExacta(imagenes, fecha);
                    tableView.cargarImagenes(imagenes);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Formato incorrecto. Usa yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem filtrarConGPS = new JMenuItem("Filtrar imágenes con GPS");
        filtrarConGPS.addActionListener(e -> {
            imagenes = Filtros.filtrarConGPS(imagenes);
            tableView.cargarImagenes(imagenes);
        });

        JMenuItem filtrarPorNombreContiene = new JMenuItem("Filtrar por nombre (contiene texto)");
        filtrarPorNombreContiene.addActionListener(e -> {
            String texto = JOptionPane.showInputDialog(null, "Introduce el texto que debe contener el nombre:", "Filtro por nombre", JOptionPane.QUESTION_MESSAGE);
            if (texto != null && !texto.trim().isEmpty()) {
                imagenes = Filtros.filtrarPorNombreContiene(imagenes, texto.trim());
                tableView.cargarImagenes(imagenes);
            }
        });

        JMenuItem quitarFiltros = new JMenuItem("Quitar filtros");
        quitarFiltros.addActionListener(e -> {
            if (imagenesOriginales != null) {
                imagenes = new ArrayList<>(imagenesOriginales);
                tableView.cargarImagenes(imagenes);
            }
        });

        JMenuItem abrirPaint = new JMenuItem("Creación de imágenes desde el interfaz - Paint");
        abrirPaint.addActionListener(e -> new MiniPaint().setVisible(true));

        refrescarMenu.add(refrescarArbol);
        menuBar.add(refrescarMenu);

        archivoMenu.add(crearCarpetaEImagenes);
        archivoMenu.add(abrirPaint);
        menuBar.add(archivoMenu);

        filtrosMenu.add(filtrarPorNombreContiene);
        filtrosMenu.add(filtrarConGPS);
        filtrosMenu.add(filtrarPorPixels);
        filtrosMenu.add(filtrarPorFechaExacta);
        filtrosMenu.add(quitarFiltros);
        menuBar.add(filtrosMenu);

        frame.add(mainSplit);
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }

    
    private DefaultMutableTreeNode createNodes(File dir) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode() {
            @Override
            public String toString() {
                return dir.getName();
            }
        };
        node.setUserObject(dir);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    node.add(createNodes(f));
                } else if (esImagen(f)) {
                    DefaultMutableTreeNode nodoArchivo = new DefaultMutableTreeNode() {
                        @Override
                        public String toString() {
                            return f.getName();
                        }
                    };
                    nodoArchivo.setUserObject(f);
                    node.add(nodoArchivo);
                }
            }
        }
        return node;
    }

    
    
    private boolean esImagen(File f) {
        String name = f.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")
                || name.endsWith(".gif") || name.endsWith(".bmp") || name.endsWith(".webp");
    }

    private void recargarArbol() {
        DefaultMutableTreeNode nuevoRoot = createNodes(new File(ruta.toString()));
        DefaultTreeModel nuevoModelo = new DefaultTreeModel(nuevoRoot);
        tree.setModel(nuevoModelo);
        tree.updateUI();
    }

    /**
     * Método de entrada para lanzar la aplicación.
     */
    public static void main(String[] args) {
        new TreeView();
    }
}
