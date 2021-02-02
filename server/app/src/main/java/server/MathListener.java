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
 */

package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MathListener implements Runnable {
    private MathExpressionParser parser;
    private Socket socket;

    private static final String HELPTEXT_STRING = "Allowed operators: * / + - ^\nConstants: pi, e\nFunctions: sqrt() (currently broken)";

    public MathListener(Socket socket) {
        this.socket = socket;
        this.parser = new MathExpressionParser();
    }

    public void run() {
        String request = "";
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("in or out failed");
            return;
        }
        

        try {
            request = in.readLine();
            if (request.equals("CLI")) {
                System.out.println("CLI client connected");
                out.println("rdy");
            } else {
                System.out.println("Got " + request);
            }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        while (!request.equals("bye")) {
            System.out.println("In loop");
            
            try {
                request = in.readLine();
                System.out.println("Got " + request);
                out.println(parser.parse(request));
            } catch (IOException e) {
                System.out.println("Read failed");
                return;
            }
        }
    }
}
