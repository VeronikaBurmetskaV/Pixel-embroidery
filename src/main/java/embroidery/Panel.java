package embroidery;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    private final MainWindow frame;
    private JButton eraserButton;
    private JCheckBox verticalReflection;
    private JCheckBox horizontalReflection;
    private boolean eraserAvailable = false;

    public Panel(MainWindow frame) {
        this.frame = frame;
        setBounds(20, 180, 180, 250);
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 8));

        eraserButton = new JButton("Гумка: вимкнено");
        eraserButton.setPreferredSize(new Dimension(180, 30));
        eraserButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        eraserButton.addActionListener(e -> toggleEraser());
        add(eraserButton);

        JButton duplicateButton = new JButton("Дублювати");
        duplicateButton.setPreferredSize(new Dimension(180, 30));
        duplicateButton.addActionListener(e -> {
            frame.getGrid().dublicate();
            frame.getMainPanel().repaint();
        });
        add(duplicateButton);

        verticalReflection = new JCheckBox("Вертикальна симетрія");
        verticalReflection.setPreferredSize(new Dimension(180, 25));
        verticalReflection.setOpaque(false);
        add(verticalReflection);

        horizontalReflection = new JCheckBox("Горизонтальна симетрія");
        horizontalReflection.setPreferredSize(new Dimension(180, 25));
        horizontalReflection.setOpaque(false);
        add(horizontalReflection);

        JButton clearButton = new JButton("Очистити все");
        clearButton.setPreferredSize(new Dimension(180, 30));
        clearButton.addActionListener(e -> {
            frame.getGrid().clear();
            frame.getMainPanel().repaint();
        });
        add(clearButton);
    }

    private void toggleEraser() {
        eraserAvailable = !eraserAvailable;
        if (eraserAvailable) {
            eraserButton.setText("Гумка: увімкнено");
            eraserButton.setBackground(Color.PINK);
            eraserButton.setForeground(Color.BLACK);
        }
        else {
            disableEraser();
        }
    }

    public void disableEraser() {
        eraserAvailable = false;
        if (eraserButton != null) {
            eraserButton.setText("Гумка: вимкнено");
            eraserButton.setBackground(null);
            eraserButton.setForeground(Color.BLACK);
        }
    }

    public boolean isEraserAvailable() {
        return eraserAvailable;
    }
    public boolean getVerticalReflection() {
        return verticalReflection.isSelected();
    }
    public boolean getHorizontalReflection() {
        return horizontalReflection.isSelected();
    }
}