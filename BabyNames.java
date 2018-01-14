/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author ata
 */
public class BabyNames extends Application {
    
    
    static HashMap<String,Integer>[] boys = (HashMap<String,Integer>[])new HashMap[10];
    static HashMap<String,Integer> []girls = (HashMap<String,Integer>[])new HashMap[10];

    
    public static void storeNames()
    {
        for(int i=0;i<10;i++){
            boys[i] = new HashMap<String,Integer>();
            girls[i] = new HashMap<String,Integer>();
        }
        String path = "//home//ata//Downloads//names//";
        


            for(int year = 2001;year<2011;year++)
            {
            File file = new File(path + year);
            
                try {
                    Scanner scanner = new Scanner(file);
                    
                    while(scanner.hasNext())
                    {
                        scanner.next(); // Line number

                          boys[year-2001].put(scanner.next(), scanner.nextInt());
                          girls[year-2001].put(scanner.next(), scanner.nextInt());          
                    }
                    scanner.close();
                } catch (Exception ex) {
                        System.err.println("Can't find file " + year);               
                }
            
               
                
            
        }
          
        
        
    }
    
    public static void main(String []s)
    {
        
        launch(s);
        
        
    }

    @Override
    public void start(Stage p) {
        storeNames();
        
        BorderPane pane = new BorderPane();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        // first Row
        Label year = new Label("Select a year :");
        ComboBox years = new ComboBox();
        years.setValue(2010);
        for(int y = 2001;y<2011;y++)
            years.getItems().add(y);
        grid.add(year, 0, 0);
        grid.add(years, 1, 0);
        
        // Second Row
        Label gender = new Label("Boy or Girl?");
        ComboBox genderCombo = new ComboBox();
        genderCombo.setValue("Male");

        genderCombo.getItems().addAll("Male","Female");
        grid.add(gender, 0, 1);
        grid.add(genderCombo, 1, 1);
        
        // Third Row
        Label name = new Label("Enter a name: ");
        TextField nameText = new TextField();
        grid.add(name, 0, 2);
        grid.add(nameText, 1, 2);
         //Fourth Row
        Button findRanking = new Button("Find Ranking");
        Label result = new Label("Click on Find Ranking");
        findRanking.setOnAction(e ->
        {
            String g = (String)genderCombo.getValue();
            Integer y = (Integer)years.getValue();
            String n = nameText.getText();
            
            if(g.equals("Female"))
                result.setText("Girl name : " + n + " is ranked " + girls[y-2001].get(n) + "#" + " in year " + y);
            else
                result.setText("Boy name : " + n + " is ranked " + boys[y-2001].get(n) + "#" + " in year " + y);
            
                
            
            
            
        });

        grid.add(findRanking, 1, 3);

        
        
        
        pane.setCenter(grid);
        pane.setBottom(result);
        Scene scene = new Scene(pane,300,200);
        p.setScene(scene);
        p.show();
        
        
    }
    
}
