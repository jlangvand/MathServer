# Copyright (C) 2021 Joakim Skogø Langvand

# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.

# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.

# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

import socket, select, sys

#  TODO: Structure

TCP_ADDR = '127.0.0.1'
TCP_PORT = 1610 #  TODO: Override port with argument
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

send("CLI")
response = s.recv(4096)

if response == b'rdy\n':
    print("Connected to server!\n")
    print("Enter an expression, 'help' for a list of available commands or 'exit' to quit.")
else:
    print("Invalid response from server: ")
    print(response)
    sys.exit(1)

msg = input("> ")

while msg != "exit":
    send(msg)
    data = s.recv(4096)
    print("ans: {}".format(data.decode("utf-8")))
    msg = input("> ")

#  Tell the server we're done
send("bye")

#  Clean up
s.close()

sys.exit(0)
