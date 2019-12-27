package org.example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MosaicCanvas extends Canvas {

    private int rows;
    private int columns;
    private Color defaultColor;

    private Color groutingColor;

    private boolean alwaysDrawGrouting;

    private boolean use3D = true;

    private boolean autopaint = true;

    private Color[][] grid;

    private GraphicsContext g;


    public MosaicCanvas() {
        this(42, 42);
    }


    public MosaicCanvas(int rows, int columns) {
        this(rows, columns, 16, 16);
    }

    public MosaicCanvas(int rows, int columns, int prefBlockHeight, int prefBlockWidth) {
        this.rows = rows;
        this.columns = columns;
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("Rows and Columns must be greater than zero.");
        }
        prefBlockHeight = Math.max(prefBlockHeight, 5);//at least 5
        prefBlockWidth = Math.max(prefBlockWidth, 5);
        grid = new Color[rows][columns];
        defaultColor = Color.BLACK;
        groutingColor = Color.GRAY;
        alwaysDrawGrouting = false;
        setWidth(prefBlockWidth * columns);
        setHeight(prefBlockHeight * rows);
        g = getGraphicsContext2D();
    }
}
