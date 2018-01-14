/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 *
 * @author ata
 */
public class ComputeAreaServer extends Application {
    
    
    class HandleClient implements Runnable {

        Socket client = null;

        public HandleClient (Socket client) {

            this.client = client;
        }

        public void run() {
            try{

            InetAddress ip = client.getInetAddress();
            Platform.runLater(() -> {
                ta.appendText("Got Connection from a " + ip.getHostAddress() + " in " + new Date() + "\n");
            });
            DataInputStream recieve = new DataInputStream(client.getInputStream());
            DataOutputStream send = new DataOutputStream(client.getOutputStream());
            while (true) {
                double radius = recieve.readDouble();
                double area = radius * radius * Math.PI;
                send.writeDouble(area);
                send.flush();
                Platform.runLater(() -> {
                    ta.appendText("Received  " + radius + " radius from : " +clientNo  + "\n");
                    ta.appendText("Computed Area for " +  clientNo +   "is : " + area + "\n");
                });

            }

        }
        catch (IOException ex

        
            ) {
                Platform.runLater(() -> {
                ta.appendText("Error");
            });
        }
    }

}

    private int clientNo = 0;
    TextArea ta = new TextArea();
   

    public void start(Stage primaryStage) {
        try {
            ServerSocket server = new ServerSocket(4000);
            Scene Scene = new Scene(new ScrollPane(ta), 600, 300);
            primaryStage.setScene(Scene);
            primaryStage.show();
            new Thread(
                    () -> {try {
                        

                        while(true) {
                            System.out.println("ERROR IN SERVER");
                            Socket client = server.accept();
                            System.out.println("ERROR IN SERVER");  
                            Thread thread = new Thread(new HandleClient(client));
                            thread.start();
                        }
                    } catch (IOException ex) {
                        try {
                            server.close();
                        } catch (IOException ex1) {
                            Logger.getLogger(ComputeAreaServer.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                        
                    }
                    }
            ).start();
        } catch (IOException ex) {
   
                      
            Logger.getLogger(ComputeAreaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public static void main(String []s)
    {
        launch(s);
        
    }
    
}
