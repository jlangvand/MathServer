import socket, select, sys

#  TODO: Structure

TCP_ADDR = '127.0.0.1'
TCP_PORT = 1610
MAX_LENGTH = 4096

BUFFER_SIZE = 1024
MESSAGE = "sum;1;-2;3;-4;5"

greeting = "RemoteCalc CLI client v1.0-ALPHA\n"

print(greeting)

print(f"Connecting to {TCP_ADDR} on port {TCP_PORT}...")

try:
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((TCP_ADDR, TCP_PORT))
    socket_list = [sys.stdin, s]
except:
    print("Connection failed, bailing out.")
    sys.exit(1)
  
def send(msg):
    s.send(msg.encode("utf-8") + b'\n')

if s.recv(4096) == b'rdy\n':
    print("Connected to server!\n")
    print("Enter an expression, 'help' for a list of available commands or 'exit' to quit.")
else:
    print("Invalid response from server")
    sys.exit(1)

msg = input("> ")

while msg != "exit":
    send(msg)
    data = s.recv(4096)
    print("ans: {}".format(data.decode("utf-8")))
    msg = input("> ")

send("bye")
data = s.recv(4096)
print("ans: " + data.decode("utf-8"))
s.close()
sys.exit(0)
