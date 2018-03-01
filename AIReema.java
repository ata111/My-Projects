/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multimedia;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import java.util.TreeSet;
class StateCompartor implements Comparator<State>,Serializable
{
    // i do this so later i will be able to choose the minimum state
    public int compare(State s1, State s2) {
        // f = g + h
        int f1 = s1.f; 
        int f2 = s2.f;
        if (f1 < f2)
            return -1;
        else if (f1 == f2)
            return 0;
        else
            return 1;
    } 
}
 class Point
    {
        public double x =0;
        public double y = 0;
        public Point(int xx,int yy){
            x = xx;
            y = yy;
        }
        // copy constructor
        public Point(Point point) {
            this.x = point.x;
		this.y = point.y;
	}  
        public Point()
        {
            this(0,0);
        }
    }
    class State
    {
        Point A,B,C;
        public State parent = null;
        public int g_cost = 0;
        public int h = 0;
        public int f = 0;
        public String operation = "nothing";
        public void calculate()
        {
            f = h +g_cost;
        }
        
        public void setCoordinates(double x1,double y1,double x2,double y2,double x3,double y3)
        {
            A.x = x1;
            A.y = y1;
            
            B.x = x2;
            B.y = y2;
            
            C.x = x3;
            C.y = y3;  
        }
        @Override
        public int hashCode()
        {
            return     (int)(A.x + A.y+ B.x+  B.y +C.x+ C.y);
        }
        // copy constructor
        public State()
        {
            A = new Point();
            B = new Point();
            C = new Point();
        }
        public State (State state) {
            this.A = state.A;
	    this.B = state.B;
            this.C = state.C;
            this.parent = state.parent;
            this.g_cost = state.g_cost;
            this.h = state.h;
            this.f = state.f;
            this.operation = state.operation;
	}
            }       
class Rect
{
    public int x;
    public int y;
    public int width;
    public int height;
    public Rect()
    {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }
 
}
public class AIReema {
    //
    volatile boolean foundGoal = false;
    //  this needs to be sorted so every time i get the  state with the minimum h
    TreeSet<State> openList = new TreeSet<State>(new StateCompartor());
    // this does not need to be sorted
    HashSet<State> closedList = new HashSet<State>();
    LinkedList<Rect> obstacles = new LinkedList<Rect>();
    // final stack , when i find the goal i will store it and store every expanded state in the closedList in LIFO manner
    // so i will start printing the  output  from the initial  state to the goal state
    Stack<State> finalList = new Stack<State>();
    public void showSolution(State s2)
    {
            finalList.add(s2);
            s2 = s2.parent;
            // add them in LIFO manner
            while(s2!=null)
            {
                finalList.add(s2);
                s2 = s2.parent;
            }
            // print them
            while(!finalList.isEmpty())
            {
                System.out.println(finalList.pop().operation);
            }
    }
    public boolean isGoal(State s1)
    {
        double distanceAB = distance(s1.A.x, s1.A.y, s1.B.x, s1.B.y);
        double distanceAC = distance(s1.A.x, s1.A.y, s1.C.x, s1.C.y);
        double distanceBC = distance(s1.B.x, s1.B.y, s1.C.x, s1.C.y);
        
        if(distanceAB <= 1  && distanceAC <= 1 ){
            foundGoal = true;
            return true;
        }
        else if(distanceAB <= 1  && distanceBC <= 1 ){
            foundGoal = true;
            return true;
        }
        else if(distanceAC <= 1  && distanceBC <= 1 ){
            foundGoal = true;
            return true;
        }
        else return false; 
    }
    public boolean checkMyStep(State s1,String robotName)
    {
       boolean result = true;
        if(robotName.equals("A")){
            for(Rect R:obstacles){
            if ( ( s1.A.x < R.x || R.x+R.width < s1.A.x) && ( s1.A.y < R.y || R.y+R.height < s1.A.x)       )
                continue;
            else {
                result = false;
                break;
            }
             
                
                } // end of loop
            
        } // end if
        
        
        else if(robotName.equals("B"))
        {
        for(Rect R:obstacles){
            if ( ( s1.B.x < R.x || R.x+R.width < s1.B.x) && ( s1.B.y < R.y || R.y+R.height < s1.B.x)       )
                continue;
            else {
                result = false;
                break;
            }
             
                
                } // end of loop
        
        } // end if
  
        else if(robotName.equals("C"))
        {
            for(Rect R:obstacles){
            if ( ( s1.C.x < R.x || R.x+R.width < s1.C.x) && ( s1.A.y < R.y || R.y+R.height < s1.C.x)       )
                continue;
            else {
                result = false;
                break;
            }
             
                
                } // end of loop
            
            
        } // end if
        else
        {
            System.out.println("Error in check step RobotName is invalid ");
        }
        
        return result;
    }
    public double distance(double x1,double y1,double x2,double y2)
    {
       double result =  Math.sqrt( Math.pow(Math.abs(x2 - x1), 2)    + Math.pow(Math.abs(y2- y1),2)  ) ;
       return result;
        
    }
    public int heruistic(State s1)
    {
        // the heruistic is all about computing the distance between a and b and between b and c
        double v1 = distance(s1.A.x, s1.A.y, s1.B.x, s1.B.y);
        double v2 = distance(s1.B.x, s1.B.y, s1.C.x, s1.C.y);
        double result = Math.sqrt(v1+v2);
        
        return (int)(result);
    }
    void moveRight(State s1,String robotName){
        //using copy constructor to perform a deep copy , i dont want a shallow copy 
        State s2 = new State(s1);
        s2.parent = s1;
        s2.g_cost +=1;
        
        
        // herusitic , distance , add to open list 
        
        if(robotName.equals("A")){
            s1.A.x++;
            s2.operation = "Robot A moves right";
        }
        else if(robotName.equals("B")){
            s1.B.x++;
            s2.operation = "Robot B moves right";
        }
        else if(robotName.equals("C")){
            s1.C.x++;
            s2.operation = "Robot C moves right";
        }
        else
        {
            System.out.println("Error in moveRight RobotName is invalid ");
        }
        s2.h = heruistic(s2);
        s2.calculate();
        
        // add to open list , first check if it does exists in the closed list
        // check for rects   .
        if(isGoal(s2))
        {
            System.out.println(" Found the Goal ");
            showSolution(s2);
            
        }
        else{
        if(!closedList.contains(s2) && checkMyStep(s2,robotName)    )
        {
            openList.add(s2);
            
        } // end of if
        } // end of else
        
    }
    void moveLeft(State s1,String robotName){
        //using copy constructor to perform a deep copy , i dont want a shallow copy 
        State s2 = new State(s1);
        s2.parent = s1;
        s2.g_cost +=1;
        
        
        // herusitic , distance , add to open list 
        
        if(robotName.equals("A")){
            s1.A.x--;
            s2.operation = "Robot A moves left";
        }
        else if(robotName.equals("B")){
            s1.B.x--;
            s2.operation = "Robot B moves left";
        }
        else if(robotName.equals("C")){
            s1.C.x--;
            s2.operation = "Robot C moves left";
        }
        else
        {
            System.out.println("Error in moveLeft RobotName is invalid ");
        }
        s2.h = heruistic(s2);
        s2.calculate();
        
        // add to open list , first check if it does exists in the closed list
        // check for rects   .
        if(isGoal(s2))
        {
            System.out.println(" Found the Goal ");
            showSolution(s2);
            
        }
        else{
        if(!closedList.contains(s2) && checkMyStep(s2,robotName)    )
        {
            openList.add(s2);
            
        } // end of if
        } // end of else
        
    }
    void moveDown(State s1,String robotName){
        //using copy constructor to perform a deep copy , i dont want a shallow copy 
        State s2 = new State(s1);
        s2.parent = s1;
        s2.g_cost +=1;
        
        
        // herusitic , distance , add to open list 
        
        if(robotName.equals("A")){
            s1.A.y++;
            s2.operation = "Robot A moves down";
        }
        else if(robotName.equals("B")){
            s1.B.y++;
            s2.operation = "Robot B moves down";
        }
        else if(robotName.equals("C")){
            s1.C.y++;
            s2.operation = "Robot C moves down";
        }
        else
        {
            System.out.println("Error in moveDown RobotName is invalid ");
        }
        s2.h = heruistic(s2);
        s2.calculate();
        
        // add to open list , first check if it does exists in the closed list
        // check for rects   .
        if(isGoal(s2))
        {
            System.out.println(" Found the Goal ");
            showSolution(s2);
            
        }
        else{
        if(!closedList.contains(s2) && checkMyStep(s2,robotName)    )
        {
            openList.add(s2);
            
        } // end of if
        } // end of else
        
    }
    void moveUp(State s1,String robotName){
        //using copy constructor to perform a deep copy , i dont want a shallow copy 
        State s2 = new State(s1);
        s2.parent = s1;
        s2.g_cost +=1;
        
        
        // herusitic , distance , add to open list 
        
        if(robotName.equals("A")){
            s1.A.y--;
            s2.operation = "Robot A moves up";
        }
        else if(robotName.equals("B")){
            s1.B.y--;
            s2.operation = "Robot B moves up";
        }
        else if(robotName.equals("C")){
            s1.C.y--;
            s2.operation = "Robot C moves up";
        }
        else
        {
            System.out.println("Error in moveUp RobotName is invalid ");
        }
        s2.h = heruistic(s2);
        s2.calculate();
        
        // add to open list , first check if it does exists in the closed list
        // check for rects   .
        if(isGoal(s2))
        {
            System.out.println(" Found the Goal ");
            showSolution(s2);
            
        }
        else{
        if(!closedList.contains(s2) && checkMyStep(s2,robotName)    )
        {
            openList.add(s2);
            
        } // end of if
        } // end of else
        
    }
    public void addObstacles()
    {
        
    }
    
    public  void run()
    {
        //addObstacles();
        State inital = new State();
        inital.setCoordinates(100, 100, 120, 100, 130, 100);
        String [] names = {"A","B","C"};
        Random r = new Random();
        openList.add(inital);
        int i =1;
        while(!foundGoal)
        {
            
           State s = openList.pollFirst();
           System.out.println("Iterations " + i++  + " h : " + s.h + " x1 : " + s.A.x + " y1 :" + s.A.y  + " x2 : " + s.B.x + " y2 : " + s.B.y + " x3 : " + s.C.x + " y3 : " + s.C.y  );
           closedList.add(s);
           moveDown(s,names[r.nextInt(names.length)]);
           moveUp(s,names[r.nextInt(names.length)]);
           moveRight(s,names[r.nextInt(names.length)]);
           moveLeft(s,names[r.nextInt(names.length)]); 
           
        }
        
        
    }
    
 public static void main(String []s)
 {
     AIReema yes = new AIReema();
     yes.run();
     
 }
 
 
 
}
