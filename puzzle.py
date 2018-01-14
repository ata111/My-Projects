import copy
class node:
    def __init__(self, state2):
        self.parent = None
        self.state = copy.deepcopy(state2)
        self.operation = ' '
    def setParent(self, p):
        self.parent = p
    def setOp(self, op):
        self.operation = op
        
    def info(self):
        print_board(self.state)
        print('**Operation used : ' + self.operation)
        print('**Parent state is : ')
        print_board(self.parent)
        print('---------------------')
        




def expand():
    inital = node(board)
    move_up(inital.state)
    move_down(inital.state)
    move_right(inital.state)
    move_left(inital.state)
    
    
    
    
    
    
def print_board(board):
    for row in board:
         print(row)

def find_blank(state):
    for i in range(3):
        for j in range(3):
            if(state[i][j] == ' '):
                return i,j

def move_up(state):
        son = node(state)
        son.setParent(board)
        son.setOp('u')
        row,colum = find_blank(state)
        if(row == 0):
            print("sorry , can't go up")
        else:    
            temp = son.state[row-1][colum]
            son.state[row-1][colum]= ' '
            son.state[row][colum] = temp
            open_list.append(son)
            son.info()
    
def move_down(state):
    
    son = node(state)
    son.setParent(state)
    son.setOp('d')
    row,colum = find_blank(state)
    if(row == 2):
        print("sorry , can't go down")
    else:    
        temp = son.state[row+1][colum]
        son.state[row+1][colum]= ' '
        son.state[row][colum] = temp
        open_list.append(son)
        son.info()
        
def move_right(state):
        son = node(state)
        son.setParent(state)
        son.setOp('r')
        row,colum = find_blank(state)
        if(colum == 2):
            print("sorry , can't go right")
        else:    
            temp = son.state[row][colum+1]
            son.state[row][colum+1]= ' '
            son.state[row][colum] = temp
            open_list.append(son)
            son.info()

def move_left(state):
        son = node(state)
        son.setParent(state)
        son.setOp('l')
        row,colum = find_blank(state)
        if(colum == 2):
            print("sorry , can't go left")
        else:    
            temp = son.state[row][colum-1]
            son.state[row][colum-1]= ' '
            son.state[row][colum] = temp
            open_list.append(son)
            son.info()
        
# for storing nodes    
open_list = []
closed_list  = []

board = [ [1,2,3]
        ,[4,5,6]
        ,[7,8,' ']
          ]

print('---------------------')
expand()
    





