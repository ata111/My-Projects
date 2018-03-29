from socket import *
import pickle

client = socket(AF_INET,SOCK_STREAM)

serverPort = 5556
serverName = gethostname()
print(serverName)
client.connect((serverName,serverPort))


while True:
    word = input("Input a word:")
    #convert string to bytes
    message_bytes  = pickle.dumps(word)
    client.send(message_bytes)
    received_bytes = client.recv(100)
    synonym = pickle.loads(received_bytes)
    if(synonym != "quit"):
        print( word + " ==> " + synonym)
    else:
        break

client.close()
