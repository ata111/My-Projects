package book;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Ellipse;

public class Tic extends Application {
    // Indicate which player has a turn, initially it is the X player

    private String status = "";
    private volatile boolean play = false;
    private char myToken = ' ';
    private Socket player;
    private DataInputStream recieve;
    private DataOutputStream send;
    private volatile boolean gameOver = false;

    // initializing game board
    private Cell[][] cell = new Cell[3][3];

    // Create and initialize a status label
    private Label lblStatus = new Label("X's turn to play");

    public void setEnemyToken(int row, int col) {
        if (myToken == 'O') {
            cell[row][col].setToken('X');
        } else {
            cell[row][col].setToken('O');
        }
    }

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {

        try {
            // Pane to hold cell
            player = new Socket("localhost", 8000);

            recieve = new DataInputStream(player.getInputStream());
            send = new DataOutputStream(player.getOutputStream());
            System.out.println("hey");

        } catch (IOException ex) {
            Logger.getLogger(Tic.class.getName()).log(Level.SEVERE, null, ex);
        }
        // creating a new thread for communicating with the server 
        new Thread(()
                -> {
            try {
                // get my Token either a X or an O
                myToken = recieve.readChar();
                primaryStage.setTitle("I'm " + myToken);
            } catch (Exception ex) {
                Logger.getLogger(Tic.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                while (true) {
                    // get the Status from the server
                    status = recieve.readUTF();
                    if (status.equalsIgnoreCase("Draw") || status.equalsIgnoreCase("X Won") || status.equalsIgnoreCase("O Won")) {
                        gameOver = true;
                    }
                    
                    Platform.runLater(() -> {
                        lblStatus.setText(status);

                    });
                    // get this boolean from the server so i know if it's my turn ( true ) or not (false)
                    play = recieve.readBoolean();
                    if (!play && !gameOver) {
                         // recieving enemy's rows , column to mark them in my board
                        int row = recieve.readInt();
                        int col = recieve.readInt();
                        // setting the Enemy  Token must be done on  Java FX dispatching thread
                        Platform.runLater(() -> {
                            setEnemyToken(row, col);

                        });

                    }
                }

            } catch (Exception ex) {
                Logger.getLogger(Tic.class.getName()).log(Level.SEVERE, null, ex);
            }

        }).start();
        // add the cells ( board) to the grid pane
        GridPane pane = new GridPane();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // the cell constructor has i = row, j = col so later i can send them to the server to
                //mark them in the enemy board
                pane.add(cell[i][j] = new Cell(i, j), j, i);
            }
        }

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);
        borderPane.setBottom(lblStatus);

        // Create a scene and place it in the stage
        Scene scene = new Scene(borderPane, 450, 170);
        primaryStage.setTitle("TicTacToe"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage   
    }

    /**
     * Determine if the cell are all occupied
     */
    // An inner class for a cell
    public class Cell extends Pane {
        // Token used for this cell

        private char token = ' ';
        private int row;
        private int column;

        public Cell(int r, int c) {
            setStyle("-fx-border-color: black");
            this.setPrefSize(2000, 2000);
            this.setOnMouseClicked(e -> handleMouseClick());
            row = r;
            column = c;
        }

        /**
         * Return token
         */
        public char getToken() {
            return token;
        }

        /**
         * Set a new token
         */
        
        public void setToken(char c) {
            token = c;

            if (token == 'X') {
                // draw a Two line to represent  X
                Line line1 = new Line(10, 10,
                        this.getWidth() - 10, this.getHeight() - 10);
                line1.endXProperty().bind(this.widthProperty().subtract(10));
                line1.endYProperty().bind(this.heightProperty().subtract(10));
                Line line2 = new Line(10, this.getHeight() - 10,
                        this.getWidth() - 10, 10);
                line2.startYProperty().bind(
                        this.heightProperty().subtract(10));
                line2.endXProperty().bind(this.widthProperty().subtract(10));

                // Add the lines to the pane
                this.getChildren().addAll(line1, line2);
                // Elipse for O
            } else if (token == 'O') {
                Ellipse ellipse = new Ellipse(this.getWidth() / 2,
                        this.getHeight() / 2, this.getWidth() / 2 - 10,
                        this.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(
                        this.widthProperty().divide(2));
                ellipse.centerYProperty().bind(
                        this.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(
                        this.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(
                        this.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(Color.WHITE);

                getChildren().add(ellipse); // Add the ellipse to the pane
            }
        }

        /* Handle a mouse click event */
        private void handleMouseClick() {
            // If cell is empty and game is not over

            try {
                // send the row and column in order to mark them in the enemy board , and then mark it on my board
                if (play) {
                    send.writeInt(row);
                    send.writeInt(column);
                    setToken(myToken);

                }

            } catch (IOException ex) {
                Logger.getLogger(Tic.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * The main method is only needed for the IDE with limited JavaFX support.
     * Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
