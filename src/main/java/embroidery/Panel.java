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
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));

        eraserButton = new JButton("Гумка: off");
         eraserButton.setPreferredSize(new Dimension(94, 30));
        eraserButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        eraserButton.addActionListener(e -> toggleEraser());
        add(eraserButton);

        JButton duplicateButton = new JButton("Дублювати");
        duplicateButton.setPreferredSize(new Dimension(104, 30));
        duplicateButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        duplicateButton.addActionListener(e -> {
            frame.getGrid().dublicate();
            frame.getMainPanel().repaint();
        });
        add(duplicateButton);

        verticalReflection = new JCheckBox("Вертикальна сим.");
        verticalReflection.setFont(new Font("Segoe UI", Font.BOLD, 13));
        verticalReflection.setOpaque(false);
        add(verticalReflection);

        horizontalReflection = new JCheckBox("Горизонтальна сим.");
        horizontalReflection.setFont(new Font("Segoe UI", Font.BOLD, 13));
        horizontalReflection.setOpaque(false);
        add(horizontalReflection);

        JButton clearButton = new JButton("Очистити все");
        clearButton.setPreferredSize(new Dimension(124, 30));
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        clearButton.addActionListener(e -> {
            frame.getGrid().clear();
            frame.getMainPanel().repaint();
        });
        add(clearButton);
    }

    private void toggleEraser() {
        eraserAvailable = !eraserAvailable;
        if (eraserAvailable) {
            eraserButton.setText("Гумка: on");
            eraserButton.setForeground(Color.BLACK);
        }
        else {
            disableEraser();
        }
    }

    public void disableEraser() {
        eraserAvailable = false;
        if (eraserButton != null) {
            eraserButton.setText("Гумка: off");
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