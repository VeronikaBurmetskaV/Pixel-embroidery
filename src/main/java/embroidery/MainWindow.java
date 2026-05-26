package embroidery;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class MainWindow extends JFrame {

    private JButton colorButton;
    private Ornament mainPanel;
    private JLabel titleLabel;
    private JButton startButton;
    private final Grid grid;

    private JComboBox<Integer> rowsComboBox;
    private JComboBox<Integer> colsComboBox;

    private Panel panel;

    private Color selectedColor = Color.red;

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

        new Thread(() -> {
            try{
                String back2Url = "https://img.freepik.com/premium-vector/vector-frame-with-ukrainian-ornament-square-seamless-pattern-square-frame_539065-130.jpg?semt=ais_hybrid&w=740&q=80";
                URL url2 = new URL(back2Url);
                Image image2 = new ImageIcon(url2).getImage();

                mainPanel.backgroundImage = image2;
                SwingUtilities.invokeLater(()-> mainPanel.repaint());
            }
            catch(Exception e){}

        }).start();

        colorButton = new JButton("Вибрати колір");
        colorButton.setBounds(390, 696, 130, 30);
        colorButton.setBackground(selectedColor);
        colorButton.setForeground(Color.black);
        colorButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        colorButton.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Оберіть колір", selectedColor);
            if (color != null) {
                selectedColor = color;
                colorButton.setBackground(selectedColor);
            }
        });
        mainPanel.add(colorButton);

        JLabel sizeLabel = new JLabel("Розмір:");
        sizeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        sizeLabel.setBounds(166, 340, 70, 30);
        mainPanel.add(sizeLabel);

        Integer[] sizes = {10, 15, 20, 25};

        rowsComboBox = new JComboBox<>(sizes);
        rowsComboBox.setSelectedItem(getGrid().rows);
        rowsComboBox.setBounds(160, 380, 70, 30);
        mainPanel.add(rowsComboBox);

        JLabel xLabel = new JLabel("x");
        xLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        xLabel.setBounds(190, 400, 50, 30);
        mainPanel.add(xLabel);

        colsComboBox = new JComboBox<>(sizes);
        colsComboBox.setSelectedItem(getGrid().cols);
        colsComboBox.setBounds(160, 424, 70, 30);
        mainPanel.add(colsComboBox);

        JButton resizeButton = new JButton("ok");
        resizeButton.setBounds(170,464,50,30);
        resizeButton.addActionListener(e -> {
            int newRows = (int) rowsComboBox.getSelectedItem();
            int newCols = (int) colsComboBox.getSelectedItem();

            getGrid().resize(newRows, newCols);

            getMainPanel().repaint();
        });
        mainPanel.add(resizeButton);

        JButton saveButton = new JButton("Зберегти PNG");
        saveButton.setBounds(250, 170, 120, 30);
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveButton.addActionListener(e -> saveToPNG());
        mainPanel.add(saveButton);

        JButton openButton = new JButton("Відкрити PNG");
        openButton.setBounds(528, 170, 120, 30);
        openButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        openButton.addActionListener(e -> openFromPNG());
        mainPanel.add(openButton);

        panel = new Panel(this);
        panel.setBounds(30, 630, 860, 60);
        mainPanel.add(panel);

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

    public Grid getGrid() {
        return grid;
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
                JOptionPane.showMessageDialog(this, "Схему збережено!");
            } catch (IOException ex) {}
        }
    }

    private void openFromPNG() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage loadedImage = ImageIO.read(fileChooser.getSelectedFile());

                getGrid().clear();

                int totalWidth = grid.cols * mainPanel.cellSize;
                int totalHeight = grid.rows * mainPanel.cellSize;
                int startX = (mainPanel.getWidth() - totalWidth) / 2;
                int startY = (mainPanel.getHeight() - totalHeight) / 2;

                for (int r = 0; r < grid.rows; r++) {
                    for (int c = 0; c < grid.cols; c++) {

                        int centerX = startX + c * mainPanel.cellSize + (mainPanel.cellSize / 2);
                        int centerY = startY + r * mainPanel.cellSize + (mainPanel.cellSize / 2);

                        if (centerX >= 0 && centerX < loadedImage.getWidth() &&
                                centerY >= 0 && centerY < loadedImage.getHeight()) {

                            int rgb = loadedImage.getRGB(centerX, centerY);
                            Color color = new Color(rgb, true);

                            if (color.getAlpha() > 10 && !(color.getRed() > 240 && color.getGreen() > 240 && color.getBlue() > 240)) {
                                grid.setColor(r, c, color);
                            }
                        }
                    }
                }

                mainPanel.repaint();
               JOptionPane.showMessageDialog(this, "Схему завантажено!");
            } catch (IOException ex) {}
        }
    }
}