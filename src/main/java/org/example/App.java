package org.example;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//////////////////////////////////////////////////////
public class App extends Application {

    private Button stopGoButton;
    private Button nextButton;
    private Button randomButton;
    private Button clearButton;
    private Button quitButton;

    private CheckBox fastCheckbox;

    private static final int GRID_SIZE = 100;

    private MosaicCanvas lifeBoardMosaicCanvasObj;

    private boolean[][] aliveBoolArray;
    private boolean animationIsRunning;

    private AnimationTimer timer;

    //////////////////////////////////////////////////////
    @Override
    public void start(Stage stage) {
//****************************************************
        int cellSize = 800 / GRID_SIZE; //8px
        lifeBoardMosaicCanvasObj = new MosaicCanvas(GRID_SIZE, GRID_SIZE, cellSize, cellSize);
        lifeBoardMosaicCanvasObj.setStyle("-fx-border-color:darkgray; -fx-border-width:3px");
        lifeBoardMosaicCanvasObj.setUse3D(false);
        if (cellSize < 5) {
            lifeBoardMosaicCanvasObj.setGroutingColor(null);
        }
        lifeBoardMosaicCanvasObj.setOnMousePressed(e -> mousePressed(e));
        lifeBoardMosaicCanvasObj.setOnMouseDragged(e -> mouseDragged(e));

//****************************************************
        stopGoButton = new Button("Start");
        nextButton = new Button("One Step");

        randomButton = new Button("Random Fill");
        randomButton.setOnAction(e -> doRandom());

        clearButton = new Button("Clear");
        clearButton.setOnAction(e->{
            aliveBoolArray = new boolean[GRID_SIZE][GRID_SIZE];
            showBoard();
        });

        quitButton = new Button("Quit");
        quitButton.setOnAction(e -> System.exit(0));

        fastCheckbox = new CheckBox("Fast");
        stopGoButton.setOnAction(e->doStopGo());

//*****************************************************
        timer = new AnimationTimer() {
            final double oneTSec = 1e8;
            long previousTime;

            @Override
            public void handle(long time) {
                if ((time - previousTime) > 0.975 * oneTSec || fastCheckbox.isSelected()) {
                    doFrame();
                    showBoard();
                    previousTime = time;
                }
            }

        };
        HBox bottom = new HBox(20, stopGoButton, fastCheckbox, nextButton, randomButton, clearButton, quitButton);
        bottom.setStyle("-fx-padding:8px; -fx-border-color:darkgray; -fx-border-width:3px 0 0 0");
        bottom.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setBottom(bottom);
        root.setCenter(lifeBoardMosaicCanvasObj);

        aliveBoolArray = new boolean[GRID_SIZE][GRID_SIZE];
        aliveBoolArray[49][49] = true;
        aliveBoolArray[50][49] = true;
        aliveBoolArray[51][49] = true;
        aliveBoolArray[49][50] = true;
        aliveBoolArray[50][48] = true;
        showBoard();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Game of Life");
        stage.setResizable(false);
        stage.show();
    }

    private void doStopGo() {
        if (animationIsRunning) {
            timer.stop();
            clearButton.setDisable(false);
            randomButton.setDisable(false);
            nextButton.setDisable(false);
            stopGoButton.setText("Start");
            animationIsRunning = false;
        } else {
            timer.start();
            clearButton.setDisable(true);
            randomButton.setDisable(true);
            nextButton.setDisable(true);
            stopGoButton.setText("Stop");
            animationIsRunning = true;
        }
    }

    private void doFrame() {
        boolean[][] newBoard = new boolean[GRID_SIZE][GRID_SIZE];
        for (int r = 0; r < GRID_SIZE; r++) {
            int above, below;
            int right, left;
            above = r > 0 ? r - 1 : GRID_SIZE - 1;
            below = r < GRID_SIZE - 1 ? r + 1 : 0;
            for (int c = 0; c < GRID_SIZE; c++) {
                left = c > 0 ? c - 1 : GRID_SIZE - 1;
                right = c < GRID_SIZE - 1 ? c + 1 : 0;
                int n = 0;
                if (aliveBoolArray[above][left]) {
                    n++;
                }
                if (aliveBoolArray[above][c]) {
                    n++;
                }
                if (aliveBoolArray[above][right]) {
                    n++;
                }
                if (aliveBoolArray[r][left]) {
                    n++;
                }
                if (aliveBoolArray[r][right]) {
                    n++;
                }
                if (aliveBoolArray[below][left]) {
                    n++;
                }
                if (aliveBoolArray[below][c]) {
                    n++;
                }
                if (aliveBoolArray[below][right]) {
                    n++;
                }
                if (n == 3 || (aliveBoolArray[r][c] && n == 2)) {
                    newBoard[r][c] = true;
                } else {
                    newBoard[r][c] = false;
                }
            }
        }
        aliveBoolArray = newBoard;
    }

    private void mousePressed(MouseEvent e) {
        if (animationIsRunning) {
            return;
        }
        int row = lifeBoardMosaicCanvasObj.yCoordToRowNumber(e.getY());
        int col = lifeBoardMosaicCanvasObj.xCoordToColNumber(e.getX());
        if (row >= 0 && row < lifeBoardMosaicCanvasObj.getRowCount()
                && col >= 0 && col < lifeBoardMosaicCanvasObj.getColumnCount()) {
            if (e.getButton() == MouseButton.SECONDARY) {
                lifeBoardMosaicCanvasObj.setColor(row, col, null);
                aliveBoolArray[row][col] = false;
            } else {
                lifeBoardMosaicCanvasObj.setColor(row, col, Color.WHITE);
                aliveBoolArray[row][col] = true;
            }

        }
    }

    private void mouseDragged(MouseEvent event) {

        mousePressed(event);
    }

    private void doRandom() {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                aliveBoolArray[r][c] = (Math.random() < 0.25);
            }
        }
        showBoard();
    }

    private void showBoard() {
        lifeBoardMosaicCanvasObj.setAutopaint(false);
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (aliveBoolArray[r][c]) {
                    lifeBoardMosaicCanvasObj.setColor(r, c, Color.WHITE);
                } else {
                    lifeBoardMosaicCanvasObj.setColor(r, c, null);
                }
            }
        }
        lifeBoardMosaicCanvasObj.setAutopaint(true);
    }

    public static void main(String[] args) {
        launch();
    }

}