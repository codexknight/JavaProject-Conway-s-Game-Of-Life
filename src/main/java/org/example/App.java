package org.example;

import javafx.application.Application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private Button stopGoButton;
    private Button nextButton;
    private Button randomButton;
    private Button clearButton;
    private Button quitButton;

    private CheckBox fastCheckbox;


    @Override
    public void start(Stage stage) {

        stopGoButton = new Button("Start");
        nextButton = new Button("One Step");
        randomButton = new Button("Random Fill");
        clearButton = new Button("Clear");
        quitButton = new Button("Quit");

        fastCheckbox = new CheckBox("Fast");

        HBox bottom = new HBox(20, stopGoButton, fastCheckbox, nextButton, randomButton, clearButton, quitButton);
        bottom.setStyle("-fx-padding:8px; -fx-border-color:darkgray; -fx-border-width:3px 0 0 0");
        bottom.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setBottom(bottom);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Game of Life");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}