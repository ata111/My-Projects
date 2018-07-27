/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multimedia;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
// ************************************
// Im using A* algorithm to solve this problem
// ************************************
class StateCompartor implements Comparator<State>,Serializable
{
    // i do this so later i will be able to choose the minimum state
    /*
    compare the states based on the f which is g(from the algorithm ,indicates how deep you are in the A * tree
    and i will increment it each time i create a new child state from the parent one , by the way we can create up to 3 child from the same 
    parent eg : one state going left , one right and one down , but it's not good to create a 4th one because it
    will reverse the parent state it self eg : top -> bottom )
       + h ( heuristic )
    
    */
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
//point class ..
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
            // 
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
        public String operation = "";
        public String robotNameMoved = "";
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
//        @Override
//        public int hashCode()
//        {
//            return     (int)(A.x + A.y+ B.x+  B.y +C.x+ C.y);
//        }
        
        public State()
        {
            A = new Point();
            B = new Point();
            C = new Point();
        }
	    // copy constructor
        public State (State state) {
            this.A = new Point(state.A);
	    this.B = new Point(state.B);
            this.C = new Point(state.C);
            this.parent = state.parent;
            this.g_cost = state.g_cost;
            this.h = state.h;
            this.f = state.f;
            this.operation = state.operation;
	}
        
        // a state is equal to another state if the robots (A,B,C ) have the same x and y coordinates
        @Override
        public boolean equals(Object o) {
            State s = (State)o;
           if( A.x ==s.A.x  &&  A.y ==s.A.y && B.x == s.B.x && B.y == s.B.y && C.x == s.C.x &&  C.y ==s.C.y   )
                return true;
            else return false;
        }
        
        
        }
    

     /*  this Rect class represent obstacles objects ( things that you can move on ) */              
class Rect
{
    public double x;
    public double y;
    public double width;
    public double height;
    public Rect()
    {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }
    public Rect(double xx,double yy,double ww,double hh)
    {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }
 
}
public class Algo {
    //
    // i was going to spawn multi thread , but this problem is sequential i dont think that it will work , 
    volatile boolean foundGoal = false;
	
    //  this needs to be sorted, so every time i will get the state with the minimum h(from A* algorithm we select the state with minimum h)
	// which is g+ f
    PriorityQueue<State> openList = new  PriorityQueue<State>(new StateCompartor());
    
    
    // this does not need to be sorted , no duplicate states , how ? equal method will determine which states are equal
    HashSet<State> closedList = new HashSet<State>();
    LinkedList<Rect> obstacles = new LinkedList<Rect>();
    // final stack , when i find the goal i will store it and store every expanded state in the closedList in LIFO manner
    // so i will start printing the  output  from the initial  state to the goal state
    Stack<State> finalList = new Stack<State>();
    public Stack<State> graphicList = new Stack<State>();
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
            // print them from buttom to top
            State sol = null;
            graphicList = (Stack<State>)finalList.clone();
            
            while(!finalList.isEmpty())
            {
                sol = finalList.pop();
                System.out.println(sol.robotNameMoved + " "  + sol.operation);
            }
    }
	/* The robots are just circles right? and each circle have a radius of 1 so if i want 
	two robots to touch each other well that is radius + radius , try to draw it 1 meter from the center of the first(circle)
	and another meter from the center of the second circle (robot)
	*/
    public boolean isGoal(State s1)
    {
        double distanceAB = distance(s1.A.x, s1.A.y, s1.B.x, s1.B.y);
        double distanceAC = distance(s1.A.x, s1.A.y, s1.C.x, s1.C.y);
        double distanceBC = distance(s1.B.x, s1.B.y, s1.C.x, s1.C.y);
        
        if(distanceAB <= 2  && distanceAC <= 2 ){
            foundGoal = true;
            return true;
        }
        else if(distanceAB <= 2  && distanceBC <= 2 ){
            foundGoal = true;
            return true;
        }
        else if(distanceAC <= 2  && distanceBC <= 2 ){
            foundGoal = true;
            return true;
        }
        else return false; 
    }
	// checks the x and y coordinates so that they're not in the obstacles coordinates
    public boolean checkMyStep(State s1,String robotName)
    { 
       boolean result = true;
        if(robotName.equals("A")){
            for(Rect R:obstacles){
            if ( (R.x <= s1.A.x && s1.A.x  <=  R.x+R.width  ) && (R.y <=s1.A.y && s1.A.y  <=R.y+R.height )    )
            {
                 result = false;
                 break;   
            }
                } // end of loop
            
        } // end if
        else if(robotName.equals("B"))
        {
        for(Rect R:obstacles){
            if ( (R.x <= s1.B.x && s1.B.x  <=  R.x+R.width  ) && (R.y <=s1.B.y && s1.B.y  <=R.y+R.height )    )
            {
                 result = false;
                 break;   
            }
                } // end of loop
        
        } // end if
        else if(robotName.equals("C"))
        {
            for(Rect R:obstacles){
            if ( (R.x <= s1.C.x && s1.C.x  <=  R.x+R.width  ) && (R.y <=s1.C.y && s1.C.y  <=R.y+R.height )    )
            {
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
	// a small function that will be used to calculate a heuristic value , check AI algorithm to know what a heuristic function is 
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
  
        
        return (int)(Math.sqrt(Math.pow(v1, 2)+Math.pow(v2, 2)));
    }
	/*
	
	1-first make a copy of the parent state and increase the 'g' because we're one level deeper now
	2- set it's parent  
	3- increment the x to the right 
	4- check if we found the solution 
	5- if not check if this state is not a duplicate one , because we don't want to reperform the same calculation on the newer states
	6- also check the coordinates of this state so it's not in the obstacles , if the 5 and 6 are statisfied add it to the openList
	a list that we will take the states from to make a new calculation until we  find the goal 
	*/
    void moveRight(State s1,String robotName){
        //using copy constructor to perform a deep copy , i dont want a shallow copy 
        State s2 = new State(s1);
        s2.parent = s1;
        s2.g_cost +=1;
        
        
        // herusitic , distance , add to open list 
        
        if(robotName.equals("A")){
            s2.A.x++;
            s2.operation = "moves right";
            s2.robotNameMoved = "A";
        }
        else if(robotName.equals("B")){
            s2.B.x++;
            s2.operation = "moves right";
            s2.robotNameMoved = "B";
        }
        else if(robotName.equals("C")){
            s2.C.x++;
            s2.operation = "moves right";
            s2.robotNameMoved = "C";
        }
        else
        {
            System.out.println("Error in moveRight RobotName is invalid ");
        }
        s2.h = heruistic(s2);
        s2.calculate();
        
        
        if(isGoal(s2))
        {
            System.out.println(" Found the Goal ");
            showSolution(s2);
            
        }
        else{
		// add to open list , first check if it does exists in the closed list
        // check for rects   .
        if(!closedList.contains(s2) && checkMyStep(s2,robotName)    )
        {
            
              if(!openList.contains(s2))
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
            s2.A.x--;
            if(s2.A.x < 0)
             s2.A.x = 0;
            s2.operation = "moves left";
            s2.robotNameMoved = "A";
        }
        else if(robotName.equals("B")){
            s2.B.x--;
            if(s2.B.x < 0)
             s2.B.x = 0;
            s2.operation = "moves left";
            s2.robotNameMoved = "B";
        }
        else if(robotName.equals("C")){
            s2.C.x--;
            if(s2.C.x < 0)
             s2.C.x = 0;
            s2.operation = "moves left";
            s2.robotNameMoved = "C";
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
                if(!openList.contains(s2))
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
            s2.A.y++;
            s2.operation = "moves down";
            s2.robotNameMoved = "A";
        }
        else if(robotName.equals("B")){
            s2.B.y++;
            s2.operation = "moves down";
            s2.robotNameMoved = "B";
        }
        else if(robotName.equals("C")){
            s2.C.y++;
            s2.operation = "moves down";
            s2.robotNameMoved = "C";
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
               if(!openList.contains(s2))
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
            
            s2.A.y--;
            if(s2.A.y < 0)
             s2.A.y = 0;
            s2.operation = "moves up";
            s2.robotNameMoved = "A";
        }
        else if(robotName.equals("B")){
            s2.B.y--;
            if(s2.B.y < 0)
             s2.B.y = 0;
            s2.operation = "moves up";
            s2.robotNameMoved = "B";
        }
        else if(robotName.equals("C")){
            s2.C.y--;
            if(s2.C.y < 0)
             s2.C.y = 0;
            s2.operation = "moves up";
            s2.robotNameMoved = "C";
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
          if(!openList.contains(s2))
                openList.add(s2);
            
        } // end of if
        } // end of else
        
    }
	/*
	We want to move the robot that is the closest to both of the other robots 
	this will help the heuristic function to speed up the algorithm
	*/
    public int WhichRobotToMove(State s1)
    {
        
        double AB = distance(s1.A.x, s1.A.y, s1.B.x, s1.B.y);
        double BC = distance(s1.B.x, s1.B.y, s1.C.x, s1.C.y);
        double AC = distance(s1.A.x, s1.A.y, s1.C.x, s1.C.y);
        double A = AB + AC;
        double B = AB + BC;
        double C = AC + BC;
        double max1 = Math.max(A, B);
        double max2 = Math.max(max1, C);
        if(max2 == A)
            return 0;
        else if(max2 == B)
            return 1;
        else return 2;
        
        
        
    }
	/*
	reads the initial state coordinates from a file , remember in the first we start with one state only , you can hardcode the 
	coordinates it's ok 
	*/
    public int[] readFile()
    {
        File file = new File("C:\\robots\\robots.txt");
        int []coordinates_of_three_robots = coordinates_of_three_robots = new int[6];
        try {
            Scanner scanner = new Scanner(file);
            String [] robotCoordinates = scanner.nextLine().split(" ");
            
            int j =0;
            for(String s:robotCoordinates){
                coordinates_of_three_robots[j] = Integer.parseInt(s);
                j++;
                     }
            
                
            int numberOfObstacles = Integer.parseInt(scanner.nextLine());
            for(int i=0;i<numberOfObstacles;i++)
            {
                String [] obstacleCoordinates = scanner.nextLine().split(" ");
                int x = Integer.parseInt(obstacleCoordinates[0]);
                int y = Integer.parseInt(obstacleCoordinates[1]);
                int w = Integer.parseInt(obstacleCoordinates[2]);
                int h = Integer.parseInt(obstacleCoordinates[3]);
                System.err.println(x +  " " + y );
                obstacles.add(new Rect(x,y,w,h));
            }
            
            
        } catch (Exception ex) {
            System.out.println("Error in readFile");
            //Logger.getLogger(AIReema.class.getName()).log(Level.SEVERE, null, ex);
        }
        return coordinates_of_three_robots;
    }
	// this method start it all !!
    public  void run()
    {
        //addObstacles();
        State inital = new State();
        int [] coordinates = readFile();
        inital.setCoordinates(coordinates[0], coordinates[1], coordinates[2], coordinates[3], coordinates[4], coordinates[5]);
        String [] names = {"A","B","C"};
         // can be from 0 to 2  0: A , 1:B, 2:C
        openList.add(inital);
        int i =1;
        State s = null;
        int robotNumber = 0;
        while(!foundGoal)
        {
            s = openList.peek();
            robotNumber = WhichRobotToMove(s);
           // System.out.println("Iterations " + i++  + " h : " + s.h + " x1 : " + s.A.x + " y1 :" + s.A.y  + " x2 : " + s.B.x + " y2 : " + s.B.y + " x3 : " + s.C.x + " y3 : " + s.C.y + " # closed : " +closedList.size() + " open: " + openList.size()  );
           // if we're right , don't move left because if you do that we will end up in an infinite loop
		// you can moving up and then down up and down etc... , right then left left then right
		if(s.operation.equals("moves right")){
                moveDown(s,names[robotNumber]);
                moveUp(s,names[robotNumber]);
                moveRight(s,names[robotNumber]);  
           }
		// same logic
           else if(s.operation.equals("moves left")){
                moveDown(s,names[robotNumber]);
                moveUp(s,names[robotNumber]);
                moveLeft(s,names[robotNumber]);
           }
           else if(s.operation.equals("moves up")){
                moveRight(s,names[robotNumber]);
                moveUp(s,names[robotNumber]);
                moveLeft(s,names[robotNumber]);
           }
           else{
                moveDown(s,names[robotNumber]);
                moveRight(s,names[robotNumber]);
                moveLeft(s,names[robotNumber]); 
        }
            
  
              openList.poll();
              closedList.add(s);
              
           
        }
        
    }
    
 public static void main(String []s)
 {
     Algo yes = new Algo();
     yes.run();
     
 }
 
 
 
}
