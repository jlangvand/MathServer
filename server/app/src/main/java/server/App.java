/*
 * Copyright (C) 2021 Joakim Skogø Langvand
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * TODO: Restructure this project to follow the same conventions as the HTTP
 * server.
 */

package server;

import java.io.IOException;
import java.net.ServerSocket;


public class App {

    public void listener(int port) {

        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                MathListener listener = new MathListener(server.accept());
                Thread t = new Thread(listener);
                t.start();
            }
        }

        catch (IOException ex) {
            System.out.println("IO Exception: " + ex.getMessage());
        }

    }

    public static void main(String[] args) {

        // Default port
        int port = 1610;

        // Accept alternative port as argument
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            }

            catch (IllegalArgumentException ex) {
                System.out.println("Invalid port, using default");
            }
        }

        // Start listening for connections
        new App().listener(port);
    }
}

