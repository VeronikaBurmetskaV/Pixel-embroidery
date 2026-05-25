package embroidery;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow program = new MainWindow();
            program.setVisible(true);
        });
    }
}