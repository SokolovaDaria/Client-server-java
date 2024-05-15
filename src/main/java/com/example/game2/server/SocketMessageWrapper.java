package com.example.game2.server;

import java.io.*;
import java.net.Socket;

public class SocketMessageWrapper {
    private BufferedReader input;
    private final PrintWriter output;
    private final DataInputStream dis;
    private final DataOutputStream dos;

    public SocketMessageWrapper(Socket socket) {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            dis = new DataInputStream(socket.getInputStream());     // декораторы-обертки для передачи данных
            dos = new DataOutputStream(socket.getOutputStream());
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

    public String readData() {
        try {
            return dis.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeData(String message) {
        try {
            dos.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }

}
