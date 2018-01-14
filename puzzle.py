import copy
from heapq import * 
from operator import attrgetter
class node:
    def __init__(self, state2 = []):
        self.parent = None
        self.state = copy.deepcopy(state2)
        # operation variable is used to store the op such as : left, right ,up , down( in other words moved the blank to the left or ...)
        self.operation = ' '
        # cost and h variable are used for A* algorithm
        self.cost = 0
        self.h = 0 
    def setParent(self, p):
        #storing the parent so i can recursivly go back and print the boards and the operations that lead me to the gial
        self.parent = p
    def setOp(self, op):
        self.operation = op
        
    def info(self):
        print_board(self.state)
        print('**Operation used : ' + self.operation)
        #print('**Parent state is : ')
        #print_board(self.parent.state)
        print ('heruistic : ' +  '\t' + str(self.h))
        print('---------------------')
    # this method is used to compare the costs for the heap sorting thing...
    def __lt__(self,other):
        return self.h > other.h

    
            
            
            
        


def show_goal(son_node):
        global found_goal
        found_goal = True
        mystack = []
        #storing the nodes in a stack , then get it's parent and store it in the stack ... again ... again ..
        # i did this to reverse the view  Last in fast out ... :D LIFO
        while(son_node.parent != None):
            mystack.append(son_node)
            son_node = son_node.parent
        print('************************************')
        #know popo them out and print the board , operation , and the total cost ( A *)
        while(mystack):
            
            newa = mystack.pop()
            print_board(newa.state)
            print('**Operation used : ' + newa.operation)
            print ('Total cost(h + g) : ' +  '\t' + str(newa.h))
        print('************************************')

def expand():
    global open_list
    global closed_list
    global bigdaddy
    global found_goal
    inital = node(board)
    
    #open_list.append(inital)
    #pushing it to the heap which is the open_list, inital.h is on what to sort it for 
    heappush(open_list ,(inital.h, inital ))

    
    if(board == goal):
        print('inital is the goal')
    else:
        while found_goal == False:
            
            #node_to_be_expanded = min(open_list,key=attrgetter('h'))
             # as you see i sorted it before in heappush method , so when i get the first element [1]  it will be the smallest one
             # the algorithm orders you to expand the node with minimal cost
            node_to_be_expanded = heappop(open_list)[1]
            
            # dict key,value are the same i need this for checking if a certial node is expaned before , i dont want to expand
            # a node that has already been expanded , because if you move it to down , and then to up you will obtain the same
            # node so no need to sort those ..
            closed_list[node_to_be_expanded] = node_to_be_expanded
            
            # open list have the nodes that have not been sorted , move up,down,right,left methods insert nodes to open_list after
            # they change the operation and the board 
            if node_to_be_expanded in open_list:
                # ok now remove the node so i will not sort it again , but becareful here i might get the node again
                #when for example the move_up method obtain a certain node and does move up operation on it and suddenly it's
                # similar to previous node or node that has been already expanded
                # in the open list so every node is expanded is added to the block_list ( Closed_list) Dict
                open_list.remove(node_to_be_expanded)
                
                
            bigdaddy = node_to_be_expanded
            if( node_to_be_expanded.operation == ' '):
                # it's passing the 2D array ( lists inside  a list)
                # state is the board(2D array)
                move_up(node_to_be_expanded.state)
                move_down(node_to_be_expanded.state)
                move_right(node_to_be_expanded.state)
                move_left(node_to_be_expanded.state)
            elif( node_to_be_expanded.operation == 'u'):
                # i added all the move methods excluding the opposite to 'u' Up which is the down
                # because if it's operation was up or 'u' and i made an 'd' operation i will get the node before doing an up operation
                # and that will slow the process since i will have to expand it again to get the up and it will go back to it's
                # previous state
                move_up(node_to_be_expanded.state)
                move_right(node_to_be_expanded.state)
                move_left(node_to_be_expanded.state)
            elif( node_to_be_expanded.operation == 'd'):
                #same applies here ..
                move_down(node_to_be_expanded.state)
                move_right(node_to_be_expanded.state)
                move_left(node_to_be_expanded.state)
            elif( node_to_be_expanded.operation == 'r'):
                move_up(node_to_be_expanded.state)
                move_down(node_to_be_expanded.state)
                move_right(node_to_be_expanded.state)
            elif( node_to_be_expanded.operation == 'l'):
                move_up(node_to_be_expanded.state)
                move_down(node_to_be_expanded.state)
                move_left(node_to_be_expanded.state)



 #this function will be used by a      heuristic function ->  h_two
def find1(num):
    for i in range(3):
        for j in range(3):
            if(num == goal[i][j]):
                return i,j
    
# heuristic function  ( 3 functions and their results will be added together to get a very high accuracy 
def h_one(state):
    result = 0
    for row in range(3):
        for column in range(3):
            if(state[row][column] != goal[row][column]):
                if(state[row][column] != ' '):
                    result += 1
    return result
    # heuristic function 
def h_two(state):
    result = 0
    if(state == goal):
        return 0
    else:
        for i in range(3):
            for j in range(3):
                x2,y2 = find1(state[i][j])
                result += abs(i-x2) + abs(j-y2)
    return result
    # heuristic function          
def h_three(state):
    result = 0
    for row in range(3):
        for column in range(3):
            if(column !=2) :
                if(state[row][column]  == goal[row][column+1]):
                    result += 1
    return result
    
                    
    
    
def print_board(board):
    for row in board:
         print(row)
# returns the row , col of the blank - > ' ' remeber this game is all about moving the blank up,down,left,right
def find_blank(state):
    for i in range(3):
        for j in range(3):
            if(state[i][j] == ' '):
                return i,j
#state here is a list of lists 2D array
def move_up(state=[]):
        son = node(state)
        # the parent is the bigdaddy = node_to_be_expanded which is the son before setting an operation on it
        son.setParent(bigdaddy)
        son.setOp('u')
        row,colum = find_blank(state)
        if(row == 0):
            #print("sorry , can't go up")
            pass
        else:
            # moving up row-1 if my row is 2 it will be 1 
            temp = son.state[row-1][colum]
            son.state[row-1][colum]= ' '
            son.state[row][colum] = temp
            son.cost = son.parent.cost + 1
            #computing the A* variable according to the algorithm , googel it it's AN AI algorithm
            # here i used 3 functions you can use one , but i want it to be very very fast :D
            son.h = son.cost +  h_one(son.state) + h_two(son.state) +  (h_three(son.state) * 20)
            
            
            if (son.state == goal):
                print(' found the goal :D ' )
                found_goal = True
                wanted_node = son
                show_goal(son)
            if son not in closed_list or son not in open_list:
                heappush(open_list , (son.h,son))
                #open_list.append(son)

            son.info()
    # no need to do a documentation here it's the same as above
def move_down(state):
    
    son = node(state)
    son.setParent(bigdaddy)
    son.setOp('d')
    row,colum = find_blank(state)
    if(row == 2):
       # print("sorry , can't go down")
       pass
    else:    
        temp = son.state[row+1][colum]
        son.state[row+1][colum]= ' '
        son.state[row][colum] = temp
        son.cost = son.parent.cost + 1
        son.h = son.cost +  h_one(son.state) + h_two(son.state) + h_three(son.state) * 20
        if (son.state == goal):
            print(' found the goal :D ' )
            found_goal = True
            wanted_node = son
            show_goal(son)
        if son not in closed_list or son not in open_list:
            heappush(open_list , (son.h,son))
            #open_list.append(son)
        son.info()
        
def move_right(state):
        son = node(state)
        son.setParent(bigdaddy)
        son.setOp('r')
        row,colum = find_blank(state)
        if(colum == 2):
            pass
            #print("sorry , can't go right")
        else:    
            temp = son.state[row][colum+1]
            son.state[row][colum+1]= ' '
            son.state[row][colum] = temp
            son.cost = son.parent.cost + 1
            son.h = son.cost +  h_one(son.state) + h_two(son.state) + h_three(son.state) * 20
            if (son.state == goal):
                print(' found the goal :D ' )
                found_goal = True
                wanted_node = son
                show_goal(son)
            if son not in closed_list or son not in open_list:
                heappush(open_list , (son.h,son))
                #open_list.append(son)
            son.info()

def move_left(state):
        son = node(state)
        son.setParent(bigdaddy)
        son.setOp('l')
        row,colum = find_blank(state)
        if(colum == 0):
           # print("sorry , can't go left")
           pass
        else:    
            temp = son.state[row][colum-1]
            son.state[row][colum-1]= ' '
            son.state[row][colum] = temp
            son.cost = son.parent.cost + 1
            son.h = son.cost +  h_one(son.state) + h_two(son.state) + h_three(son.state) * 20
            #got the goal
            if (son.state == goal):
                print(' found the goal :D ' )
                found_goal = True
                wanted_node = son
                show_goal(son)
            if son not in closed_list or son not in open_list:
                heappush(open_list , (son.h,son))
                #open_list.append(son)
            son.info()
        
# for storing nodes    
# the heap for storing the nodes that are ready for getting expanded , the move methods fill it
open_list = []
# a dict with the node being the Key and Value at the same time so i dont expand a node that similar node like it got expanded before
closed_list  = {}
# the move methods will set this boolean to true if they find it
found_goal = False
# big daddy is the node itself , before being passed to the move methods , or the node before doing a certain
#operation such as up ,down ,left ,right
bigdaddy = None
# the goal node the move methods will make it points to the goal node
wanted_node = None

#i want the A* algorithm to reach this and it will give me the steps ( by storing the string of the operations)
goal = [
[1,2,3]
,[8,4,' ']
,[7,6,5]
      ]

# the inital board that the computer will start from
board = [
[5, 6, 7]
,[4, 8,' ']
,[3, 2, 1]
      ]
print('---------------------')
expand()
    




