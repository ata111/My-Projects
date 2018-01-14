/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author ata
 */
public class StudentServer extends Application {
    ServerSocket server;
    TextArea ta = new TextArea();
    ObjectInputStream in;
    ObjectOutputStream out;
    public static void main(String []s)
    {
        launch(s);
    }
    public void start(Stage p) 
    {
        try {
            out = new ObjectOutputStream(new FileOutputStream("students.dat"));
            server = new ServerSocket(8001);
            
        } catch (IOException ex) {
            Logger.getLogger(StudentServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        BorderPane pane = new BorderPane();
        pane.setCenter(new ScrollPane(ta));
        Button show = new Button("Show Students");
        pane.setBottom(show);
        show.setOnAction(e -> {
            try {
                out.close();
               // server.close();
                Student.displayStudent(ta, new ObjectInputStream(new FileInputStream("//home//ata//NetBeansProjects//Book//students.dat")));
            } catch (IOException ex) {
                Logger.getLogger(StudentServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
         
        
        });
        Scene scene = new Scene(pane,600,200);
        p.setScene(scene);
        p.show();
        
        
        new Thread( ()->{
            try {
                Socket student = server.accept();
                Platform.runLater(()->{
                    ta.appendText("Got a connection " + new Date());
                
                });
                in = new ObjectInputStream(student.getInputStream());
                try {
                    while(true){
                    Student o = (Student)in.readObject();
                     Platform.runLater(()->{
                    ta.appendText(" \nRecieved Student Object from : " +o.getName() );
                
                });
                    out.writeObject(o);
                    }
                    
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(StudentServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(StudentServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }).start();

    }
    
}
