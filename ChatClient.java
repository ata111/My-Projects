package book;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author ata
 */
public class ChatClient extends Application {

    TextArea ta = new TextArea();
    String fromServer = " ";

    public static void main(String[] s) {

        launch(s);
    }

    public void start(Stage p) throws Exception {
  
        Socket client = new Socket("localhost", 8000);
        DataInputStream recieve = new DataInputStream(client.getInputStream());
        DataOutputStream send = new DataOutputStream(client.getOutputStream());
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setVgap(10);
        pane.setPadding(new Insets(5));

        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(10);
        TextField name = new TextField();
        TextField text = new TextField();
        // sending texts ( chat) in the JavaFx dispatching thread
        text.setOnAction(e -> {
            try {
                String n = name.getText();
                String t = text.getText();
                send.writeUTF(n);
                send.writeUTF(t);
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        // thread for recieving chat from the server
        new Thread(() -> {
           
            try {
                while (true) {
                    fromServer = recieve.readUTF();
                    Platform.runLater(()->{
                        ta.appendText(fromServer + "\n");
                   
                    });
                }
            } catch (IOException ex) {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }

        }).start();

        grid.add(new Label("Your Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Text:"), 0, 1);
        grid.add(text, 1, 1);
        pane.add(grid, 0, 0);
        pane.add(new ScrollPane(ta), 0, 1);
        Scene scene = new Scene(pane, 600, 250);
        p.setScene(scene);
        p.setTitle("Chat Client");
        p.show();
    }

}
