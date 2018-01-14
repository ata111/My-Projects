package book;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author ata
 */
public class ChatServer extends Application {
   
    ArrayList<SendChat> clients = new ArrayList<SendChat>();
   
    int count = 1;
    TextArea ta = new TextArea();
    ServerSocket server;
    public static void main(String []s)
    {
        launch(s);
       
    }

    public void start(Stage p) throws Exception {
        server = new ServerSocket(8000);
        BorderPane pane = new BorderPane();
        pane.setCenter(new ScrollPane(ta));
        Scene scene = new Scene(pane,600,250);
        p.setScene(scene);
        p.show();
        // starting a new Thread for the server to listen for in-comming connections
        new Thread( ()-> {
           
                try {
                    while(true)
                    {
                    Socket client = server.accept();
                   
                    new Thread(new RecieveChat(client, count)).start();
                   
                    clients.add(new SendChat(client,count));
                     InetAddress ip =client.getInetAddress();
                     Platform.runLater(() -> {
                     ta.appendText("Got a connection from " + ip.getAddress() + "Number : " + count );
                     count++;
                    
                     }  );
                        }
                  
                } catch (IOException ex) {
                }

       
       
        }).start();
       

   
}
    public void sendChatToOtherClients(String text,String name)
    {
        for(SendChat client:clients)
        {
            client.send(name + ": "+text);
           
        }
       
    }
    // recieves the strings from the clients
    class RecieveChat implements Runnable
    {
        Socket client;
        String name = " ";
        DataInputStream in;
        int clientNumber = 0;
        String text = "";
        public RecieveChat(Socket client,int n)
        {
            this.client = client;
            clientNumber = n;
            try {
                in = new DataInputStream(client.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        public void run() {
            try{
                while(true)
                {
                    name = in.readUTF();
                    text = in.readUTF();
                    sendChatToOtherClients(text,name);
                   
                }
               
            }catch(IOException e)
            {
               
                System.out.println("Client " + clientNumber + " disconnected");
            }
        }
 
    }
    // sends the strings from the clients
    class SendChat
    {
        Socket client;
        DataOutputStream out;
        int clientNumber = 0;
        public SendChat(Socket client,int n)
        {
            this.client = client;
            clientNumber = n;
            try {
                out = new DataOutputStream(client.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        public void send(String text)
        {
            try {
                out.writeUTF(text);
            } catch (IOException ex) {
                System.out.println(" Couldn't send " + clientNumber);
            }
           
        }
       
    }
   
   
}
