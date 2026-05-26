package embroidery;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

    public class Ornament extends JPanel {

        public boolean page = false;

        private Image ornamentImage;
        public Image backgroundImage;

        private MainWindow frame;

        public final int cellSize =16;
        private final int thickness=4;
        private int colVirtual = 0;
        private int rowVirtual = 0;
        private Timer timer;

        private final int [][] pattern = {
                {1,1,1,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,1,1,1},
                {1,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1},
                {1,0,2,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,2,0,1},
                {0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,2,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,2,0,0,0,0},
                {0,0,0,0,0,2,0,0,0,0,0,2,0,2,0,0,0,0,0,2,0,0,0,0,0},
                {0,0,0,0,0,0,2,1,2,0,0,2,2,2,0,0,2,1,2,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,2,1,1,2,2,1,2,2,1,1,2,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,2,1,1,2,1,0,1,0,1,2,1,1,2,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,2,1,1,1,0,1,0,1,1,1,2,0,0,0,0,0,0,0},
                {1,0,1,0,1,2,2,2,2,0,0,0,1,0,0,0,2,2,2,2,1,0,1,0,1},
                {0,1,1,1,0,1,1,2,1,1,1,1,0,1,1,1,1,2,1,1,0,1,1,1,0},
                {1,0,1,1,0,2,2,2,2,0,0,0,1,0,0,0,2,2,2,2,0,1,1,0,1},
                {0,0,0,0,1,1,1,1,2,1,1,0,1,0,1,1,2,1,1,1,1,0,0,0,0},
                {0,0,0,0,0,0,2,1,1,2,1,0,1,0,1,2,1,1,2,0,0,0,0,0,0},
                {0,0,0,0,0,0,1,2,1,1,2,2,1,2,2,1,1,2,1,0,0,0,0,0,0},
                {0,0,0,0,0,0,2,1,2,0,0,2,2,2,0,0,2,1,2,0,0,0,0,0,0},
                {0,0,0,0,0,2,0,0,0,0,0,2,0,2,0,0,0,0,0,2,0,0,0,0,0},
                {0,0,0,0,2,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,2,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,0,2,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,2,0,1},
                {1,1,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1},
                {1,1,1,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,1,1,1},
        };

        public Ornament (String urlString, MainWindow frame) {
            this.frame=frame;
            try {
                URL url = new URL(urlString);
                ornamentImage = new ImageIcon(url).getImage();
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        public void animation() {
            timer = new Timer(5, e -> {
                colVirtual++;
                if(colVirtual >= pattern[0].length) {

                    colVirtual = 0;
                    rowVirtual++;
                }
                if(rowVirtual >= pattern.length) {
                    timer.stop();

                    frame.actBut();
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            Grid model = frame.getGrid();

            int totalWidth = (page ? model.cols : pattern[0].length) * cellSize;
            int totalHeight = (page ? model.rows : pattern.length) * cellSize;
            int startX = (getWidth() - totalWidth) / 2;
            int startY = (getHeight() - totalHeight) / 2;

            if(page){
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
                else{
                    g.setColor(Color.white);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }

                for (int r = 0; r < model.rows; r++) {
                    for (int c = 0; c < model.cols; c++) {
                        int x = startX + c * cellSize;
                        int y = startY + r * cellSize;

                        g2d.setStroke(new BasicStroke(1));
                        g2d.setColor(Color.LIGHT_GRAY);
                        g2d.drawRect(x, y, cellSize, cellSize);

                        Color cellColor = frame.getGrid().getColor(r, c);
                        if (cellColor != null) {
                            g2d.setColor(cellColor);
                            g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                            int p = 4;
                            g2d.drawLine(x + p, y + p, x + cellSize - p, y + cellSize - p);
                            g2d.drawLine(x + cellSize - p, y + p, x + p, y + cellSize - p);
                        }
                    }
                }
                return;
            }
            if (ornamentImage != null) {
                g.drawImage(ornamentImage, 0, 0, getWidth(), getHeight(), this);
            }
            for (int row = 0; row < pattern.length; row++) {
                for (int col = 0; col < pattern[row].length; col++) {
                    if (row < rowVirtual || (row == rowVirtual && col < colVirtual)) {
                        int type = pattern[row][col];

                        if (type == 1) {
                            g2d.setColor(new Color(195, 22, 22));
                        }
                        else if (type == 2) {
                            g2d.setColor(new Color(45, 48, 52));
                        }

                        if (type != 0) {
                            int x = startX + col * cellSize;
                            int y = startY + row * cellSize;

                            int p = 2;

                            g2d.drawLine(x + p, y + p, x + cellSize - p, y + cellSize - p);
                            g2d.drawLine(x + cellSize - p, y + p, x + p, y + cellSize - p);
                        }
                    }
                }
            }
        }
    }