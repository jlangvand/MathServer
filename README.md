# Sockets Exercise
University assignment, spring semester 2021

## Math Server
Server app written in Java. Opens a TCP socket on port 1610, on which it accepts mathematical expressions and returns the result.

To run:

    $ cd server
    $ ./gradlew run

## CLI Client
A very simple client written in Python. Connects to the Math Server and presents the user with a command line. Enter a mathematical expression in the form `1+2-3*4/5^6`.

To run: `python client/main.py`.

## HTTP Server
Java app that runs a web server on port 8080. For now it responds with a demo HTML page whatever the client requests.

To run:

    $ cd http
    $ ./gradlew run
