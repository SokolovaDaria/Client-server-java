package com.example.game2.server;

import java.io.*;
import java.net.Socket;

public class SocketMessageWrapper {
    private BufferedReader input;
    private final PrintWriter output;


    public SocketMessageWrapper(Socket socket) {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getMessage() {
        try {
            return input.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }

}
