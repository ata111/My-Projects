import copy

def draw():
    result = True
    for i in range (9):
        if(game_board[i] == ' '):
            result = False
            break
    return result

def print_board(board):
    for i in range(0,9,3):
        print('\n' +'\t'+ '\t'+ board[i] + ' | ' + board[i+1] + ' | ' + board[i+2] )
        print('\t'+ '\t'+ '__________')


def computer_plays(mymove):
    global game_board
    if(mymove =='one'):
        game_board[0] = 'X'
    elif(mymove =='two'):
        game_board[1] = 'X'
    elif(mymove =='three'):
        game_board[2] = 'X'
    elif(mymove =='four'):
        game_board[3] = 'X'
    elif(mymove =='five'):
        game_board[4] = 'X'
    elif(mymove =='six'):
        game_board[5] = 'X'
    elif(mymove =='seven'):
        game_board[6] = 'X'
    elif(mymove =='eight'):
        game_board[7] = 'X'
    elif(mymove =='nine'):
        game_board[8] = 'X'
        
        
        
def play():
    global game_board
    global choice
    if choice =='X':
        print('Computer Turn ')
        tot = node(game_board,None,' ')
        mymove = minmax(tot)
        computer_plays(mymove)
        print_board(game_board)
        choice = 'O'
    else:
        player_input = int(input("It's O turn choose a number to place O on it : "))
        game_board[player_input-1] = 'O'
        print_board(game_board)
        choice = 'X'
        
def is_terminal(game_board):
    if (game_board[0] =='X' and game_board[1] =='X' and  game_board[2] =='X'):
        return True

    elif (game_board[3] =='X' and game_board[4] =='X' and  game_board[5] =='X'):
        return True
    # diagonol

    elif (game_board[0] =='X' and game_board[4] =='X' and  game_board[8] =='X'):
        return True

    elif (game_board[2] =='X' and game_board[4] =='X' and  game_board[6] =='X'):
        return True

   
    elif (game_board[6] =='X' and game_board[7] =='X' and  game_board[8] =='X'):
        return True
    elif (game_board[0] =='X' and game_board[3] =='X' and  game_board[6] =='X'):
        return True
    elif (game_board[1] =='X' and game_board[4] =='X' and  game_board[7] =='X'):
        return True
    elif (game_board[2] =='X' and game_board[5] =='X' and  game_board[8] =='X'):
        return True
    # O case
    elif (game_board[0] =='O' and game_board[1] =='O' and  game_board[2] =='O'):
        return True
    
    elif (game_board[3] =='O' and game_board[4] =='O' and  game_board[5] =='O'):
        return True
    
    elif (game_board[6] =='O' and game_board[7] =='O' and  game_board[8] =='O'):
        return True
    
    elif (game_board[0] =='O' and game_board[3] =='O' and  game_board[6] =='O'):
        return True
    
    elif (game_board[1] =='O' and game_board[4] =='O' and  game_board[7] =='O'):
        return True
    
    elif (game_board[2] =='O' and game_board[5] =='O' and  game_board[8] =='O'):
        return True

    # diagonol

    elif (game_board[0] =='O' and game_board[4] =='O' and  game_board[8] =='O'):
        return True

    elif (game_board[2] =='O' and game_board[4] =='O' and  game_board[6] =='O'):
        return True
    
    elif draw():
        return True

    else:
        return False
    
    
        
def eval(game_board):
    if (game_board[0] =='X' and game_board[1] =='X' and  game_board[2] =='X'):
        return 1

    elif (game_board[3] =='X' and game_board[4] =='X' and  game_board[5] =='X'):
        return 1
    # diagonol

    elif (game_board[0] =='X' and game_board[4] =='X' and  game_board[8] =='X'):
        return 1

    elif (game_board[2] =='X' and game_board[4] =='X' and  game_board[6] =='X'):
        return 1

   
    elif (game_board[6] =='X' and game_board[7] =='X' and  game_board[8] =='X'):
        return 1
    elif (game_board[0] =='X' and game_board[3] =='X' and  game_board[6] =='X'):
        return 1
    elif (game_board[1] =='X' and game_board[4] =='X' and  game_board[7] =='X'):
        return 1
    elif (game_board[2] =='X' and game_board[5] =='X' and  game_board[8] =='X'):
        return 1
    # O case
    elif (game_board[0] =='O' and game_board[1] =='O' and  game_board[2] =='O'):
        return -1
    
    elif (game_board[3] =='O' and game_board[4] =='O' and  game_board[5] =='O'):
        return -1
    
    elif (game_board[6] =='O' and game_board[7] =='O' and  game_board[8] =='O'):
        return -1
    
    elif (game_board[0] =='O' and game_board[3] =='O' and  game_board[6] =='O'):
        return -1
    
    elif (game_board[1] =='O' and game_board[4] =='O' and  game_board[7] =='O'):
        return -1
    
    elif (game_board[2] =='O' and game_board[5] =='O' and  game_board[8] =='O'):
        return -1

    # diagonol

    elif (game_board[0] =='O' and game_board[4] =='O' and  game_board[8] =='O'):
        return -1

    elif (game_board[2] =='O' and game_board[4] =='O' and  game_board[6] =='O'):
        return -1
    
    elif draw():
        return 0


    
class node():
    def __init__(self,board,parent,move):
        self.move = move
        self.parent = parent
        self.board = copy.deepcopy(board)
        self.points = 0

 # returns the moves  check what is the move and apply it.. :D       
def minmax(state):
    #state is a node ..
    temp = max(state,-10,10)
    #temp is a node
    return temp.move
    
        
def max(state,alpha,beta):
    
    if(is_terminal(state.board)):
        state.points = eval(state.board)
        return state
    # returns a list of it's children
    
    mynode = state
    children = expand(state)
    for child in children:
        temp = min(child,alpha,beta)
        if(temp.points > alpha):
            alpha = temp.points
            mynode = temp
        if(alpha >= beta ):
            mynode.points = alpha
            return mynode   
    return mynode


def min(state,alpha,beta):
    
    if(is_terminal(state.board)):
        state.points = eval(state.board)
        return state
    # returns a list of it's children

    mynode = state
    children = expand(state)
    for child in children:
        temp = max(child,alpha,beta)
        if(temp.points < beta):
            beta = temp.points
            mynode = temp
        if(alpha >= beta ):
            mynode.points = beta
            return mynode

           
    return mynode
         
        
    
    
    
    
    
def one(state,children):
    if(state.board[0] == ' '):
        child = node(state.board,state,'one')
        child.board[0] = 'X'
        children.append(child)
    
def two(state,children):
    if(state.board[1] == ' '):
        child = node(state.board,state,'two')
        child.board[1] = 'X'
        children.append(child)       

def three(state,children):
    if(state.board[2] == ' '):
        child = node(state.board,state,'three')
        child.board[2] = 'X'
        children.append(child)         

def four(state,children):
    if(state.board[3] == ' '):
        child = node(state.board,state,'four')
        child.board[3] = 'X'
        children.append(child)

def five(state,children):
    if(state.board[4] == ' '):
        child = node(state.board,state,'five')
        child.board[4] = 'X'
        children.append(child)

def six(state,children):
    if(state.board[5] == ' '):
        child = node(state.board,state,'six')
        child.board[5] = 'X'
        children.append(child)

def seven(state,children):
    if(state.board[6] == ' '):
        child = node(state.board,state,'seven')
        child.board[6] = 'X'
        children.append(child)

def eight(state,children):
    if(state.board[7] == ' '):
        child = node(state.board,state,'eight')
        child.board[7] = 'X'
        children.append(child)
        
def nine(state,children):
    if(state.board[8] == ' '):
        child = node(state.board,state,'nine')
        child.board[8] = 'X'
        children.append(child)
        
    
def expand(state):
    children = []
    # every one of those return a node with (state ,parent ,  move  )
    one(state,children)
    two(state,children)
    three(state,children)
    four(state,children)
    five(state,children)
    six(state,children)
    seven(state,children)
    eight(state,children)
    nine(state,children)
    return children


def game_status():
    global game_board
    global game_over
    if is_terminal(game_board):
        game_over = True
    else:
        game_over = False




game_over = False;
help_board = ['1', '2' ,'3','4','5','6','7','8', '9']
game_board = [' ', ' ' ,' ',' ',' ',' ',' ',' ', ' ']
choice = 'X'
print_board(help_board)
while(not game_over):
    play()
    game_status()
    


result_hey = eval(game_board)
if( result_hey == 1):
    print('Computer won')
elif(result_hey == -1):
    print('You won')
else:
    print('Draw')
    














