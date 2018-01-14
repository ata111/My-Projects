/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
public class TicServer extends Application {
    int count = 1;
    TextArea ta = new TextArea();
    ServerSocket server;
    
      
    
    
    public static void main(String []s)
    { 
        
        launch(s);
    }

    
    public void start(Stage p) 
    {
        
        try {
            server = new ServerSocket(8000);
        } catch (IOException ex) {
            Logger.getLogger(TicServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        // creating a new thread for Listening to in-comming connections from the Players(clients)
        new Thread(() ->{
       while(true){     
           try {
               Socket player1 = server.accept();
               Socket player2 = server.accept();
               Thread session = new Thread(new Session(player1,player2,count));
               // increase the count ( sessionNumber) ...
               count++;
               session.start();
               Platform.runLater(()->{
               ta.appendText("Game Session " + (count-1) + "has been started \n");
               });
           } catch (IOException ex) {
               Logger.getLogger(TicServer.class.getName()).log(Level.SEVERE, null, ex);
           }
        
                
                }
        
        }).start();
        Scene scene = new Scene(new ScrollPane(ta),600,200);
        p.setScene(scene);
        p.show();
        
    }
    class Session implements Runnable
    {
        boolean draw = false;
        boolean player1Won = false;
        boolean player2Won = false;
        int sessionNumber = 0;
        int turn = 1;
        Socket player1;
        Socket player2;
        DataInputStream recieve1;
        DataInputStream recieve2;
        DataOutputStream send1;
        DataOutputStream send2;
        // row and col recieved from the player who has the turn to write on them
        int row = 0;
        int col = 0;
        char[][]cell = new char[3][3];
        
        public Session(Socket p1,Socket p2,int sn)
        {
            // fill the cell array with ' ' so my  isFull() can work :D
            fill();
            player1 = p1;
            player2 = p2;
            sessionNumber = sn;
            try {
                recieve1 = new DataInputStream(player1.getInputStream());
                recieve2 = new DataInputStream(player2.getInputStream());
                send1 = new DataOutputStream(player1.getOutputStream());
                send2 = new DataOutputStream(player2.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(TicServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       public boolean isFull() {
    for (int i = 0; i < 3; i++)
      for (int j = 0; j < 3; j++)
        if (cell[i][j] == ' ')
          return false;

    return true;
  }

  /** Determine if the player with the specified token wins */
  public boolean isWon(char token) {
      // check the rows
    for (int i = 0; i < 3; i++)
      if (cell[i][0] == token
          && cell[i][1] == token
          && cell[i][2]== token) {
        return true;
      }
// row columns
    for (int j = 0; j < 3; j++)
      if (cell[0][j] ==  token
          && cell[1][j] == token
          && cell[2][j] == token) {
        return true;
      }
    // diagonals
    if (cell[0][0] == token 
        && cell[1][1] == token        
        && cell[2][2] == token) {
      return true;
    }

    if (cell[0][2] == token
        && cell[1][1] == token
        && cell[2][0] == token) {
      return true;
    }

    return false;
  }
    
     public void fill()
    {
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                cell[i][j] =' ';
    }
       
        public void run() {
            try {
                // give each player a token , first one gets the X :)
                send1.writeChar('X');
                send2.writeChar('O');
                while(true)
                {
                    if(player1Won)
                    {
                        send1.writeUTF("X Won");
                        send2.writeUTF("X Won");
                        // send False for play boolean variable to stop playing for both  of them
                        send1.writeBoolean(false);
                        send2.writeBoolean(false);
                        // platform is a method that enables you to call methods on the JavaFx threads just the SwingWorker
                        Platform.runLater(()->{
                            ta.appendText("\n Sesson :" + sessionNumber + " X Won" );
                                     });
                        break;
                        
                    }
                    else if(player2Won)
                    {
                        send1.writeUTF("O Won");
                        send2.writeUTF("O Won");
                        send1.writeBoolean(false);
                        send2.writeBoolean(false);
                        Platform.runLater(()->{
                            ta.appendText("\n Sesson :" + sessionNumber + " O Won" );
                                     });
                        break;
                        
                    }
                    
                    else if(draw)
                    {
                        send1.writeUTF("Draw");
                        send2.writeUTF("Draw");
                        send1.writeBoolean(false);
                        send2.writeBoolean(false);
                        Platform.runLater(()->{
                            ta.appendText("\n Sesson :" + sessionNumber + " Draw" );
                                     });
                        break;
                        
                    }
                   
                    // display the status for the two players
                    if(turn == 1)
                    {
                        send1.writeUTF("X turn's");
                        send2.writeUTF("X turn's");
                        // sending play booleans true for player1, false for player2
                        send1.writeBoolean(true);
                        send2.writeBoolean(false);
                        // the server takes the token from player one to place it in the char array( so it can know)
                        // the result by checking them
                        row = recieve1.readInt();
                        col = recieve1.readInt();
                        // sending this info to player 2
                        send2.writeInt(row);
                        send2.writeInt(col);
                        
                        // place the token in the array and check for wins,loses,draw(Full)
                        cell[row][col] = 'X';
                        if(isFull()){
                            draw = true;
                            
                        }
                        else if(isWon('X'))
                        {
                            player1Won = true;   
                        }
                        // know it's player2 turn :D
                        turn = 2;
                    }
                    else{
                        // player 2 turn's
                        send1.writeUTF("O turn's");
                        send2.writeUTF("O turn's");
                        send1.writeBoolean(false);
                        send2.writeBoolean(true);
                        row = recieve2.readInt();
                        col = recieve2.readInt();
                        // sending this info to player 1
                        send1.writeInt(row);
                        send1.writeInt(col);
                        cell[row][col] = 'O';
                        if(isFull()){
                            draw = true;
                            
                        }
                        else if(isWon('O'))
                        {
                            player2Won = true;   
                        }
                        turn = 1;
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(TicServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
        
    }
    
}
