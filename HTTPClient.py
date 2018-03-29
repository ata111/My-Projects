from socket import  *
import pickle
import time

def send_request(client,request_line,host="localhost",connection="keep-alive",user_agent="Firefox",accept_language="en"):
    if type(client) != socket:
        raise  TypeError("You need to pass a socket object")
    try:
        message = {"Request line":request_line,
                   "Host":host,
                   "Connection":connection,
                   "User-Agent":user_agent,
                   "Accept-Language":accept_language
                   }
        # converts the dict to bytes so it can be sent over a socket
        byte_message = pickle.dumps(message)
        client.send(byte_message)
        print("Request message sent")
        response_byte_message = client.recv(1200)
        response_message = pickle.loads(response_byte_message)
        print(response_message)
    except TypeError :
        raise TypeError("Bad argument type,the headers of the message must be str")




client = socket(AF_INET,SOCK_STREAM)
client.connect(("localhost",5556))
print("Connected")
method = "GET"
requested_object_url= "/ata.png"
version = "HTTP/1.1"


#debugging 
send_request(client,method +" "+ requested_object_url+" "+version,"92.13.13.42" )
time.sleep(4)
send_request(client,method +" "+ requested_object_url+" "+version,"92.13.13.42" )

time.sleep(2)
send_request(client,method +" "+ requested_object_url+" "+version,"92.13.13.42" )



client.close()
