package embroidery;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {

    private JButton colorButton;
    private Ornament mainPanel;
    private JLabel titleLabel;
    private JButton startButton;
    private final Grid grid;

    private Panel panel;

    private Color selectedColor = Color.RED;

    public MainWindow() {

        grid = new Grid();

        setTitle("Піксельна вишивка|Редактор орнаменту|Бурмецька Вероніка");
        setSize(900, 900);
        setResizable(false);
         setLocationRelativeTo(null);

        String backUrl = "https://img.magnific.com/premium-vector/embroidery-ukrainian-national-ornament-decoration-vector-illustration_324757-1245.jpg?semt=ais_hybrid&w=740&q=80";
        mainPanel = new Ornament(backUrl, this);

        mainPanel.setLayout(null);

        titleLabel = new JLabel("Редактор орнаменту");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        titleLabel.setForeground(Color.red);
        titleLabel.setBounds(314, 110, 300, 30);

        mainPanel.add(titleLabel);

        startButton = new JButton("Почати");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        startButton.setForeground(Color.red);
        startButton.setBounds(400, 716, 100, 36);

        startButton.setEnabled(false);

        startButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if(startButton.isEnabled()) {
                    startButton.setForeground(Color.black);
                }
            }
            public void mouseExited(MouseEvent e) {
                if(startButton.isEnabled()) {
                    startButton.setForeground(Color.red);
                }
            }
            public void mouseClicked(MouseEvent e) {
                if(startButton.isEnabled()) {
                    newPage();
                }
            }
        });
        mainPanel.add(startButton);
        add(mainPanel);

        mainPanel.animation();
    }

    public void actBut() {
        if(startButton != null) {
            startButton.setEnabled(true);
            startButton.setForeground(Color.black);
        }
    }

    public void newPage() {
        mainPanel.removeAll();
        mainPanel.page=true;

        colorButton = new JButton("Вибрати колір");
        colorButton.setBounds(20, 20, 150, 30);
        colorButton.setBackground(selectedColor);
        colorButton.setForeground(Color.black);
        colorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Оберіть колір вишивки", selectedColor);
            if (color != null) {
                selectedColor = color;
                colorButton.setBackground(selectedColor);
            }
        });
        mainPanel.add(colorButton);

        panel = new Panel(this);
        mainPanel.add(panel);

        JButton saveButton = new JButton("Зберегти PNG");
        saveButton.setBounds(680, 20, 150, 30);
        saveButton.addActionListener(e -> saveToPNG());
        mainPanel.add(saveButton);

        JButton openButton = new JButton("Відкрити PNG");
        openButton.setBounds(680, 60, 150, 30);
        openButton.addActionListener(e -> openFromPNG());
        mainPanel.add(openButton);

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleGridClick(e);
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                handleGridClick(e);
            }
        };
        mainPanel.addMouseListener(mouseHandler);
        mainPanel.addMouseMotionListener(mouseHandler);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void handleGridClick(MouseEvent e) {
        int totalWidth = grid.cols * mainPanel.cellSize;
        int totalHeight = grid.rows * mainPanel.cellSize;
        int startX = (mainPanel.getWidth() - totalWidth) / 2;
        int startY = (mainPanel.getHeight() - totalHeight) / 2;

        int col = (e.getX() - startX) / mainPanel.cellSize;
        int row = (e.getY() - startY) / mainPanel.cellSize;

        if (row >= 0 && row < grid.rows && col >= 0 && col < grid.cols) {
            Color colorToApply = (panel.isEraserAvailable() || SwingUtilities.isRightMouseButton(e)) ? null : selectedColor;

            grid.setColor(row, col, colorToApply);
            if (panel.getVerticalReflection()) grid.setColor(row, grid.cols - 1 - col, colorToApply);
            if (panel.getHorizontalReflection()) grid.setColor(grid.rows - 1 - row, col, colorToApply);
            if (panel.getVerticalReflection() && panel.getHorizontalReflection()) grid.setColor(grid.rows - 1 - row, grid.cols - 1 - col, colorToApply);

            mainPanel.repaint();
        }
    }

    public void setSelectedColor(Color color) {
        this.selectedColor = color;
    }
    public Grid getGrid() {
        return grid;
    }
    public Panel getPanel() {
        return panel;
    }
    public Ornament getMainPanel() {
        return mainPanel;
    }

    private void saveToPNG() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".png")) file = new File(file.getAbsolutePath() + ".png");
            BufferedImage bImg = new BufferedImage(mainPanel.getWidth(), mainPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bImg.createGraphics();
            mainPanel.paintComponent(g2);
            g2.dispose();
            try {
                ImageIO.write(bImg, "png", file);
                JOptionPane.showMessageDialog(this, "Схему успішно збережено!");
            } catch (IOException ex) {
               // ex.printStackTrace();
            }
        }
    }

    private void openFromPNG() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage loadedImage = ImageIO.read(fileChooser.getSelectedFile());
                mainPanel.backgroundImage = loadedImage;
                mainPanel.repaint();
               JOptionPane.showMessageDialog(this, "Схему завантажено!");
            } catch (IOException ex) {}
            //{ ex.printStackTrace(); }
        }
    }
}