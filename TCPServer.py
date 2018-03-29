from socket import *
import threading
import pickle

class ServeClient(threading.Thread):

    def __init__(self,connectionSocket,clientAddress):
        threading.Thread.__init__(self)
        self.client_socket = connectionSocket
        self.address = clientAddress

    def run(self):
        while True:
            received_bytes = self.client_socket.recv(100)

            word = pickle.loads(received_bytes)
            if(word == "@"):
                quit = "quit"
                quit_bytes = pickle.dumps(quit)
                self.client_socket.send(quit_bytes)
                print(str(self.address) + " disconnected")
                self.client_socket.close()
                break
            elif(word in engDict):
                synonym = engDict[word]
                synonym_bytes = pickle.dumps(synonym)
                self.client_socket.send(synonym_bytes)
            else:
                error = "Not found in the Dictionary"
                error_bytes = pickle.dumps(error)
                self.client_socket.send(error_bytes)



engDict = {}

with open('dictionary.txt') as file:
    for line in file:
        words_list = line.split()
        key = words_list[0]
        value = words_list[1]
        engDict[key] = value

print(engDict)

serverName = gethostname()
serverPort = 5556

welcomingSocket = socket(AF_INET,SOCK_STREAM)
welcomingSocket.bind((serverName,serverPort))
welcomingSocket.listen(5)






while True:
    (connectionSocket,address) = welcomingSocket.accept()
    print( str(address) + " connected")
    client = ServeClient(connectionSocket,address)
    client.start()






