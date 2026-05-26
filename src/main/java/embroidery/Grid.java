package embroidery;

import java.awt.*;

public class Grid {

    public int rows = 25;
    public int cols = 25;

    private Color[][] editGrid = new Color[rows][cols];

    public void resize(int newRows, int newCols) {
        this.rows= newRows;
        this.cols= newCols;

        this.editGrid = new Color[newRows][newCols];
    }

    public void clear() {
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                editGrid[row][col] = null;
            }
        }
    }

    public Color getColor(int row, int col) {
        if(row >= 0 && row < rows && col >= 0 && col < cols) {
            return editGrid[row][col];
        }
        return null;
    }

    public void setColor(int row, int col, Color color) {
        if(row >= 0 && row < rows && col >= 0 && col < cols) {
            editGrid[row][col] = color;
        }
    }

    public void dublicate(){
        int minRow = rows, maxRow = - 1;
        int minCol = cols, maxCol = - 1;

        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                if(editGrid[row][col] != null) {
                    if(row<minRow) minRow = row;
                    if(col<minCol) minCol = col;
                    if(row>maxRow) maxRow = row;
                    if(col>maxCol) maxCol = col;
                }
            }
        }

        if(maxRow == -1|| maxCol == -1){
            return;
        }

        int patternHeight = maxRow - minRow + 1;
        int patternWidth = maxCol - minCol + 1;

        Color [][] sample = new Color[patternHeight][patternWidth];
        for(int row = minRow; row <= maxRow; row++) {
            for(int col = minCol; col <= maxCol; col++) {
                sample[row-minRow][col-minCol] = editGrid[row][col];
            }
        }

        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                int relativeRow = (row - minRow)%patternHeight;
                int relativeCol = (col - minCol)%patternWidth;

                if(relativeRow<0){
                    relativeRow += patternHeight;
                }
                if(relativeCol<0){
                    relativeCol += patternWidth;
                }
                if(sample[relativeRow][relativeCol] != null) {
                    editGrid[row][col] = sample[relativeRow][relativeCol];
                }
            }
        }
    }
}