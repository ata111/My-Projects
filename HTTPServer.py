import datetime
from socket import *
import pickle
import threading




class HandleRequest(threading.Thread):
    #static variable
    time_to_live = 7
    def __init__(self,client,name="socket_name"):
        threading.Thread.__init__(self)
        self.client_socket = client
        # this will be a dict object
        self.request_message = None
        self.name = name
    def response(self):
        version = "HTTP/1.1"
        status_code = "200"
        status_phrase = "OK"
        connection = "close"
        date = datetime.datetime.now().__str__()
        server = "Apache/2.2.3 (Ubuntu)"
        last_modified = ""
        content_length = 232
        content_type = "text/html"

        #response message
        response_message = {"Status line":version +" "+status_code + " " + status_phrase,
                            "Connection":connection,
                            "Date":date,
                            "Server":server,
                            "Last-Modified":last_modified,
                            "Content-Length":content_length,
                            "Content-Type":content_type}

        #convert from dict to bytes
        response_message_bytes = pickle.dumps(response_message)
        self.client_socket.send(response_message_bytes)

    def run(self):
        #*****************    setting the timeout **************
        clientsocket.settimeout(self.time_to_live)
        self.byte_message = self.client_socket.recv(1000)
        # converting the received bytes into a dict object
        self.request_message = pickle.loads(self.byte_message)
        print(self.request_message)
        self.response()
        if(self.request_message["Connection"] == "keep-alive"):
            print("Ok he wants a persistent connection")
            try:
                while True:
                    self.byte_message = self.client_socket.recv(1000)
                    self.request_message = pickle.loads(self.byte_message)
                    self.response()
                    print(self.request_message)
            #if .recv() method gets timed out , it will throw an exception
            except:
                print("Connection " + self.name + " timed out " )

            finally:
                clientsocket.close()

        else:
            print("Non-persistent connection")
            self.client_socket.close()



server = socket(family=AF_INET, type=SOCK_STREAM)
server.bind(("localhost",5556))
server.listen(5)
# connection number , will be used as a name for the connection
count = 1

server_files = ["ata.png","secretdoc","networksbook"]

while True:
    print("Waiting for a connection")
    (clientsocket,address) = server.accept()
    print(  str(address) + " connected")
    new_session = HandleRequest(clientsocket,  str(count)+ "")
    #start a new thread to serve the client
    new_session.start()
    count = count +1
