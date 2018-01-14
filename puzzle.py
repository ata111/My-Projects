import copy
from heapq import * 
from operator import attrgetter
class node:
    def __init__(self, state2 = []):
        self.parent = None
        self.state = copy.deepcopy(state2)
        self.operation = ' '
        self.cost = 0
        self.h = 0 
    def setParent(self, p):
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

    def __lt__(self,other):
        return self.h > other.h

    
            
            
            
        


def show_goal(son_node):
        global found_goal
        found_goal = True
        mystack = []
        while(son_node.parent != None):
            mystack.append(son_node)
            son_node = son_node.parent
        print('************************************')
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
    heappush(open_list ,(inital.h, inital ))

    
    if(board == goal):
        print('inital is the goal')
    else:
        while found_goal == False:
            
            #node_to_be_expanded = min(open_list,key=attrgetter('h'))
             
            node_to_be_expanded = heappop(open_list)[1]
            
            
            closed_list[node_to_be_expanded] = node_to_be_expanded
            
            if node_to_be_expanded in open_list:
                open_list.remove(node_to_be_expanded)
                
            bigdaddy = node_to_be_expanded
            if( node_to_be_expanded.operation == ' '):
                move_up(node_to_be_expanded.state)
                move_down(node_to_be_expanded.state)
                move_right(node_to_be_expanded.state)
                move_left(node_to_be_expanded.state)
            elif( node_to_be_expanded.operation == 'u'):
                move_up(node_to_be_expanded.state)
                move_right(node_to_be_expanded.state)
                move_left(node_to_be_expanded.state)
            elif( node_to_be_expanded.operation == 'd'):
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



    
def find1(num):
    for i in range(3):
        for j in range(3):
            if(num == goal[i][j]):
                return i,j
    

def h_one(state):
    result = 0
    for row in range(3):
        for column in range(3):
            if(state[row][column] != goal[row][column]):
                if(state[row][column] != ' '):
                    result += 1
    return result
    
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

def find_blank(state):
    for i in range(3):
        for j in range(3):
            if(state[i][j] == ' '):
                return i,j

def move_up(state=[]):
        son = node(state)
        son.setParent(bigdaddy)
        son.setOp('u')
        row,colum = find_blank(state)
        if(row == 0):
            #print("sorry , can't go up")
            pass
        else:
            temp = son.state[row-1][colum]
            son.state[row-1][colum]= ' '
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
open_list = []
closed_list  = {}
found_goal = False
bigdaddy = None
wanted_node = None

goal = [
[1,2,3]
,[8,4,' ']
,[7,6,5]
      ]


board = [
[5, 6, 7]
,[4, 8,' ']
,[3, 2, 1]
      ]
print('---------------------')
expand()
    




