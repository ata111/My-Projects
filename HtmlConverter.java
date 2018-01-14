/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package book;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

/**
 *
 * @author ata
 */
public class HtmlConverter {

    static String[] keywordString = {"abstract", "assert",
        "boolean", "break", "byte", "case", "catch",
        "char", "class", "const", "continue", "default",
        "do", "double", "else", "enum", "extends", "for",
        "final", "finally", "float", "goto", "if", "implements",
        "import", "instanceof", "int", "interface", "long", "native",
        "new", "package", "private", "protected", "public", "return",
        "short", "static", "strictfp", "super", "switch", "synchronized",
        "this", "throw", "throws", "transient", "try", "void", "volatile",
        "while", "true", "false", "null"};
    static HashSet<String> set = new HashSet<String>(Arrays.asList(keywordString));
    static Scanner javaFile;
    static PrintWriter htmlFile;

    public static void main(String[] s) throws FileNotFoundException {
        File java =new File("//home//ata//Downloads//ata.java");
        javaFile = new Scanner(java);
        htmlFile = new PrintWriter("//home//ata//Downloads//ata.html");
        if(java.exists())
            
        htmlFile.print("<html>" + "\n" + "<body> " + "\n");

        while (javaFile.hasNext()) {
            htmlFile.println( "<br> ");
            String bigString = javaFile.nextLine();
            String[] tokens = null;// anything except comments
            int index = bigString.indexOf("//");
            // has comments? if yes cut it and color the whole line that start with // 
            if (index != -1) {
                tokens = bigString.substring(0, index).split(" ");

                // non-comment words
                for (String token : tokens) {
                    if(token.length() != 0)
                        check(token);
                }

                String str = bigString.substring(index, bigString.length());
                check(str);

            } else {
                tokens = bigString.split(" ");
                for (String token : tokens) {
                    if(token.length() != 0)
                        check(token);
                }
            }
            
        }
        htmlFile.println();

        htmlFile.print("</body>" + "\n" + "</html>");
        javaFile.close();
        htmlFile.close();

    }

    public static void check(String token) {
        if (set.contains(token)) {
            htmlFile.print("<font color='bold navy'> " + token + "</font>");
        } else if (token.startsWith("//")) {
            htmlFile.print("<font color='green'> " + token + "</font>");
        } else if(isLiteral(token)){
            htmlFile.print("<font color='blue'> " + token + "</font>");
        }
        
        else htmlFile.print(" " +token);

    }
    public static boolean isLiteral(String token)
    {
        boolean result = true;
        if(Character.isDigit(token.charAt(0)))
            return false;
        
        
        for(int i=0;i<token.length();i++)
        {
            if(!Character.isLetter(token.charAt(i)) &&  !Character.isDigit(token.charAt(i)) )
            {
                result = false;
                break;
            }
            
        }
            
            return result;
    }

}
