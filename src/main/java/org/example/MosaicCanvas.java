package org.example;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MosaicCanvas extends Canvas {
    //***********************************************************************************
    private int rows; //number of rows
    private int columns;//number of col
    private Color defaultColor;  //default color of any rectangle in the mosaic

    private Color groutingColor;

    private boolean alwaysDrawGrouting;

    private boolean use3D = false;

    private boolean autopaint = true;

    private Color[][] grid2DColor; //2D array with size of rows*columns

    private GraphicsContext g; //the graphicsContext obj associated with the canvas
//***********************************************************************************

    public MosaicCanvas() {
        this(42, 42); //call the next constructor
    }


    public MosaicCanvas(int rows, int columns) {
        this(rows, columns, 16, 16);//call the next constructor
    }

    public MosaicCanvas(int rows, int columns, int prefBlockHeight, int prefBlockWidth) {
        this.rows = rows;
        this.columns = columns;
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Rows and Columns must be greater than zero.");
        }
        prefBlockHeight = Math.max(prefBlockHeight, 5);//at least 5
        prefBlockWidth = Math.max(prefBlockWidth, 5);
        grid2DColor = new Color[rows][columns];
        defaultColor = Color.BLACK;
        groutingColor = Color.GRAY;
        alwaysDrawGrouting = false;
        setWidth(prefBlockWidth * columns);
        setHeight(prefBlockHeight * rows);
        g = getGraphicsContext2D();
    }

    //**********************************************************************************

    public void setDefaultColor(Color color) {

        if (color == null) {
            color = Color.BLACK;
        }
        if (!color.equals(defaultColor)) {
            defaultColor = color;
            forceRedraw();
        }
    }

    private void forceRedraw() {
        drawAllSquares();
    }

    private void drawAllSquares() {
        if (Platform.isFxApplicationThread()) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    drawOneSquare(r, c);
                }
            }
        } else {
            Platform.runLater(() -> {
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < columns; c++) {
                        drawOneSquare(r, c);
                    }
                }
            });
        }
        try {
            Thread.sleep(1);

        } catch (InterruptedException e) {
        }
    }

    private void drawOneSquare(int row, int col) {

        double rowHeight = getHeight() / rows;
        double colWidth = getWidth() / columns;
        int y = (int) Math.round(rowHeight * row);
        int h = Math.max(1, (int) Math.round(rowHeight * (row + 1)) - y);
        int x = (int) Math.round(colWidth * col);
        int w = Math.max(1, (int) Math.round(colWidth * (col + 1)) - x);

        Color c = grid2DColor[row][col];
        g.setFill((c == null) ? defaultColor : c);
        if (groutingColor == null || (c == null && !alwaysDrawGrouting)) {
            if (!use3D || c == null) {
                g.fillRect(x, y, w, h);
            } else
                fill3DRect(c, x, y, w, h);
        } else {
            if (!use3D || c == null)
                g.fillRect(x + 1, y + 1, w - 2, h - 2);
            else
                fill3DRect(c, x + 1, y + 1, w - 2, h - 2);
            g.setStroke(groutingColor);
            g.strokeRect(x + 0.5, y + 0.5, w - 1, h - 1);
        }
    }

    private void fill3DRect(Color color, int x, int y, int width, int height) {
        double h = color.getHue();
        double b = color.getBrightness();
        double s = color.getSaturation();
        if (b > 0.8) {
            b = 0.8;
            g.setFill(Color.hsb(h, s, b));
        } else if (b < 0.2) {
            b = 0.2;
            g.setFill(Color.hsb(h, s, b));
        }
        g.fillRect(x, y, width, height);
        g.setStroke(Color.hsb(h, s, b + 0.2));
        g.strokeLine(x + 0.5, y + 0.5, x + width - 0.5, y + 0.5);
        g.strokeLine(x + 0.5, y + 0.5, x + 0.5, y + height - 0.5);
        g.setStroke(Color.hsb(h, s, b - 0.2));
        g.strokeLine(x + width - 0.5, y + 1.5, x + width - 0.5, y + height - 0.5);
        g.strokeLine(x + 1.5, y + height - 0.5, x + width - 0.5, y + height - 0.5);
    }

    public void setColor(int r, int c, Color color) {
        if (r >= 0 && r < rows && c >= 0 && c < columns) {
            grid2DColor[r][c] = color;
            drawSquare(r, c);
        }
    }

    private void drawSquare(int r, int c) {
        if (autopaint) {
            if (Platform.isFxApplicationThread()) {
                drawOneSquare(r, c);
            } else {
                Platform.runLater(() -> drawOneSquare(r, c));
            }
            try {
                Thread.sleep(1);

            } catch (InterruptedException e) {

            }
        }
    }

    public void setAutopaint(boolean b) {
        if (this.autopaint == b) {

            return;
        }
        this.autopaint = b;
        if (autopaint) {
            forceRedraw();
        }
    }
}










