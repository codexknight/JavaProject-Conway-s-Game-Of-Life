package org.example;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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


//////////////////////////////////////////////////////
    @Override
    public void start(Stage stage) {
//****************************************************
        int cellSize = 800 / GRID_SIZE; //8px
        lifeBoardMosaicCanvasObj = new MosaicCanvas(GRID_SIZE, GRID_SIZE, cellSize, cellSize);
        lifeBoardMosaicCanvasObj.setStyle("-fx-border-color:darkgray; -fx-border-width:3px");
//****************************************************
        stopGoButton = new Button("Start");
        nextButton = new Button("One Step");
        randomButton = new Button("Random Fill");

        clearButton = new Button("Clear");
        clearButton.setOnAction(e->{
            aliveBoolArray = new boolean[GRID_SIZE][GRID_SIZE];
            showBoard();
        });

        quitButton = new Button("Quit");
        quitButton.setOnAction(e -> System.exit(0));

        fastCheckbox = new CheckBox("Fast");
//*****************************************************
        HBox bottom = new HBox(20, stopGoButton, fastCheckbox, nextButton, randomButton, clearButton, quitButton);
        bottom.setStyle("-fx-padding:8px; -fx-border-color:darkgray; -fx-border-width:3px 0 0 0");
        bottom.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setBottom(bottom);
        root.setCenter(lifeBoardMosaicCanvasObj);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Game of Life");
        stage.setResizable(false);
        stage.show();
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