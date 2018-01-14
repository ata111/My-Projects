/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author ata
 */
public class RadiusClient extends Application{
    Socket socket ;
                    
    
    public void start(Stage primaryStage) throws IOException 
    {
        socket = new Socket("127.0.0.1",4000);
        DataInputStream recieve = new DataInputStream(socket.getInputStream());
        DataOutputStream send = new DataOutputStream(socket.getOutputStream());
        BorderPane pane = new BorderPane();
        TextArea ta = new TextArea();
        pane.setLeft(new Label("Enter a radius : "));
        TextField tf = new TextField();
        pane.setTop(tf);
        pane.setCenter(ta);
        tf.setOnAction(e-> { 
            double radius = Double.parseDouble(tf.getText());
            try {
                send.writeDouble(radius);
                double area = recieve.readDouble();
                ta.appendText("Area Recieved from the Server is : "+area + "\n");
                
            } catch (IOException ex) {
                Logger.getLogger(RadiusClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        });
        
        Scene scene = new Scene(pane,500,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String []s)
    {
        
        launch(s);
    }
    
}
