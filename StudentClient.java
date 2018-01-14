/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author ata
 */
public class StudentClient extends Application {
    public static void main(String []s)
    {
        launch(s);
    }

    Socket client;
    ObjectOutputStream out;
    public void start(Stage p) 
    {
        try {
            client = new Socket("localhost",8000);
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(StudentClient.class.getName()).log(Level.SEVERE, null, ex);
        }
       
            
       
        GridPane grid = new GridPane();
        Button send = new Button("Send");
        TextField nameText = new TextField();
        TextField cityText = new TextField();
        grid.setAlignment(Pos.CENTER);
        grid.add(new Label("Name :"), 0, 0);
        grid.add(nameText,1,0);
        grid.add(new Label("City :"), 0, 1);
        grid.add(cityText,1,1);
        grid.add(send,1,2);
        send.setOnAction(e ->{
            try {
                out.writeObject(new Student(nameText.getText(),cityText.getText()));
            } catch (IOException ex) {
                Logger.getLogger(StudentClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Done");
            alert.setContentText("I sent the object");
            alert.showAndWait();
            
        });
        Scene scene = new Scene(grid,600,200);
        p.setScene(scene);
        p.show();
    }
}
