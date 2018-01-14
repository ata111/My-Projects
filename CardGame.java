/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author ata
 */
public class CardGame extends Application {

    Random r = new Random();
    ArrayList<Image> clubs = new ArrayList<Image>(13);
    ArrayList<Image> diamonds = new ArrayList<Image>(13);
    ArrayList<Image> hearts = new ArrayList<Image>(13);
    ArrayList<Image> spades = new ArrayList<Image>(13);
    ArrayList<Integer> cardNumbers = new ArrayList<Integer>();
    ArrayList<Integer> expNumbers = new ArrayList<Integer>();

    public static void main(String[] args) {
        launch(args);

    }

    public void fillLists() {
        String path = "file:////home//ata/Downloads/book//image/card/";
        for (int i = 0; i < 13; i++) {
            spades.add(new Image(path + (i + 1) + ".png"));
            
        }

        for (int i = 13; i < 26; i++) {
            hearts.add(new Image(path + (i + 1) + ".png"));
        }

        for (int i = 26; i < 39; i++) {
            diamonds.add(new Image(path + (i + 1) + ".png"));
        }

        for (int i = 39; i < 52; i++) {
            clubs.add(new Image(path + (i + 1) + ".png"));
        }

    }

    public void shuffle(GridPane pane) {
        expNumbers.clear();
        cardNumbers.clear();
        int value = 0;
        for (int col = 0; col < 4; col++) {
            value = r.nextInt(4);
            if (value == 0) {
                int num = r.nextInt(13);
                cardNumbers.add(num+1);
                pane.add(new ImageView(clubs.get(num)), col, 0);
            } else if (value == 1) {
                int num = r.nextInt(13);
                cardNumbers.add(num+1);

                pane.add(new ImageView(diamonds.get(num)), col, 0);

            } else if (value == 2) {
                int num = r.nextInt(13);
                cardNumbers.add(num+1);
                pane.add(new ImageView(hearts.get(num)), col, 0);
            } else {
                int num = r.nextInt(13);
                cardNumbers.add(num+1);
                pane.add(new ImageView(spades.get(num)), col, 0);
            }

        }
        printCardNumbers();
    }
    public void printExpNumbers()
    {
         System.out.println("  Exp Numbers :  ");
        for(Integer num:expNumbers)
            System.out.print(num+ "  ");
    }
    
    public void printCardNumbers()
    {
        for(Integer num:cardNumbers)
            System.out.print(num+ "  ");
    }
    public void start(Stage primaryStage) throws Exception {
        fillLists();
        BorderPane pane = new BorderPane();
        HBox top = new HBox(10);
        top.setAlignment(Pos.CENTER_RIGHT);
        Button shuffle = new Button("Shuffle");

        Label result = new Label();
        top.getChildren().add(result);
        top.getChildren().add(shuffle);

        HBox bottom = new HBox(10);
        bottom.setAlignment(Pos.TOP_CENTER);
        TextField text = new TextField();
        Button verify = new Button("Verify");
        verify.setOnAction(e->{
            int value = evaluateExpression(text.getText());
            printExpNumbers();
            if(value == 24 && cardNumbers.containsAll(expNumbers)  )
                result.setText("Correct");
            else if(cardNumbers.containsAll(expNumbers))
            {
                result.setText("The answer must be 24");
                
            }
                else if(!cardNumbers.containsAll(expNumbers))
            {
                result.setText("You must use the card numbers");
                
                
            }
                else  result.setText("Wrong");
                
            
        });
                
                
        Label expression = new Label("Enter an Expression : ");
        
        bottom.getChildren().add(expression);
        bottom.getChildren().add(text);
        bottom.getChildren().add(verify);

        GridPane cardPane = new GridPane();
        cardPane.setAlignment(Pos.CENTER);
        cardPane.setHgap(15);
        shuffle(cardPane);
        shuffle.setOnAction(e -> shuffle(cardPane));

        pane.setTop(top);
        pane.setCenter(cardPane);
        pane.setBottom(bottom);
        Scene scene = new Scene(pane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
      /** Evaluate an expression */
  public  int evaluateExpression(String expression) {
      expNumbers.clear();
    // Create operandStack to store operands
    Stack<Integer> operandStack = new Stack<>();
  
    // Create operatorStack to store operators
    Stack<Character> operatorStack = new Stack<>();
  
    // Insert blanks around (, ), +, -, /, and *
    expression = insertBlanks(expression);

    // Extract operands and operators
    String[] tokens = expression.split(" ");

    // Phase 1: Scan tokens
    for (String token: tokens) {
      if (token.length() == 0) // Blank space
        continue; // Back to the while loop to extract the next token
      else if (token.charAt(0) == '+' || token.charAt(0) == '-') {
        // Process all +, -, *, / in the top of the operator stack 
        while (!operatorStack.isEmpty() &&
          (operatorStack.peek() == '+' || 
           operatorStack.peek() == '-' ||
           operatorStack.peek() == '*' ||
           operatorStack.peek() == '/')) {
          processAnOperator(operandStack, operatorStack);
        }

        // Push the + or - operator into the operator stack
        operatorStack.push(token.charAt(0));
      }
      else if (token.charAt(0) == '*' || token.charAt(0) == '/') {
        // Process all *, / in the top of the operator stack 
        while (!operatorStack.isEmpty() &&
          (operatorStack.peek() == '*' ||
          operatorStack.peek() == '/')) {
          processAnOperator(operandStack, operatorStack);
        }

        // Push the * or / operator into the operator stack
        operatorStack.push(token.charAt(0));
      }
      else if (token.trim().charAt(0) == '(') {
        operatorStack.push('('); // Push '(' to stack
      }
      else if (token.trim().charAt(0) == ')') {
        // Process all the operators in the stack until seeing '('
        while (operatorStack.peek() != '(') {
          processAnOperator(operandStack, operatorStack);
        }
        
        operatorStack.pop(); // Pop the '(' symbol from the stack
      }
      else { // An operand scanned
        // Push an operand to the stack
        expNumbers.add(new Integer(token));
        operandStack.push(new Integer(token));
      }
    }

    // Phase 2: process all the remaining operators in the stack 
    while (!operatorStack.isEmpty()) {
      processAnOperator(operandStack, operatorStack);
    }

    // Return the result
    return operandStack.pop();
  }

  /** Process one operator: Take an operator from operatorStack and
   *  apply it on the operands in the operandStack */
  public  void processAnOperator(
      Stack<Integer> operandStack, Stack<Character> operatorStack) {
    char op = operatorStack.pop();
    int op1 = operandStack.pop();
    int op2 = operandStack.pop();
    if (op == '+') 
      operandStack.push(op2 + op1);
    else if (op == '-') 
      operandStack.push(op2 - op1);
    else if (op == '*') 
      operandStack.push(op2 * op1);
    else if (op == '/') 
      operandStack.push(op2 / op1);
  }
  
  public  String insertBlanks(String s) {
    String result = "";
    
    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == '(' || s.charAt(i) == ')' || 
          s.charAt(i) == '+' || s.charAt(i) == '-' ||
          s.charAt(i) == '*' || s.charAt(i) == '/')
        result += " " + s.charAt(i) + " ";
      else
        result += s.charAt(i);
    }
    
    return result;
  }

}
