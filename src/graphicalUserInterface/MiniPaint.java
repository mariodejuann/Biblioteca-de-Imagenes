package graphicalUserInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * MiniPaint es una aplicación simple de dibujo con herramientas básicas
 * como elegir color, limpiar el lienzo y guardar la imagen.
 */
public class MiniPaint extends JFrame {
    private BufferedImage canvas;
    private Graphics2D g2d;
    private int prevX, prevY;
    private Color currentColor = Color.BLACK;

    /**
     * Crea una nueva ventana de dibujo con opciones para pintar,
     * cambiar color, limpiar y guardar el resultado.
     */
    public MiniPaint() {
        setTitle("Mini Paint");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        canvas = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        g2d = canvas.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.setColor(currentColor);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(canvas, 0, 0, null);
            }
        };

        panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                g2d.drawLine(prevX, prevY, x, y);
                prevX = x;
                prevY = y;
                panel.repaint();
            }
        });

        JButton clearButton = new JButton("Limpiar");
        clearButton.addActionListener(e -> {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            g2d.setColor(currentColor);
            panel.repaint();
        });

        JButton colorButton = new JButton("Color");
        colorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Elige un color", currentColor);
            if (color != null) {
                currentColor = color;
                g2d.setColor(color);
            }
        });

        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> {
            try {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    ImageIO.write(canvas, "png", file);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JPanel buttons = new JPanel();
        buttons.add(clearButton);
        buttons.add(colorButton);
        buttons.add(saveButton);

        add(buttons, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}
