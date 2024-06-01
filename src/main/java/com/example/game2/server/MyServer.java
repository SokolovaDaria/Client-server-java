package com.example.game2.server;

import com.example.game2.models.Model;
import com.example.game2.models.ModelBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {
    private int port = 2003;
    private InetAddress ip = null;
    ServerSocket serverSocket;
    private final ExecutorService service = Executors.newFixedThreadPool(4);
    private final Model model = ModelBuilder.build();
    private final List<MyClient> clientList = new ArrayList<>();

    public static void main(String[] args) {
        new MyServer().startServer();
    }

    public void broadcast() {
        for (MyClient myClient : clientList) {
            myClient.sendToClient();
        }
    }

    public void startServer() {
        try {
            ip = InetAddress.getLocalHost();
            serverSocket = new ServerSocket(port, 2, ip);                                          // слушает остальные сокеты

            model.initialize();

            while (true) {
                Socket clientSocket = serverSocket.accept();                                           // когда клиент подключается, accept() возвращает новый сокет для взаимодействия с клиентом
                SocketMessageWrapper socketMessageWrapper = new SocketMessageWrapper(clientSocket);
                String userName = socketMessageWrapper.getMessage(); //  имя

                if (!addClient(socketMessageWrapper, userName)) {
                    clientSocket.close();
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean addClient(SocketMessageWrapper socketMessageWrapper, String userName) {
        if (clientList.size() >= 4) {
            socketMessageWrapper.sendMessage("Игроков не может быть более четырёх!");
            return false;
        }

        if (checkClientList(userName)) {
            socketMessageWrapper.sendMessage("ACCEPT");
            MyClient client = new MyClient(socketMessageWrapper, this, userName);
            clientList.add(client);
            service.submit(client);   //            Thread thread = new Thread(client);   thread.start();
            return true;
        }
        socketMessageWrapper.sendMessage("Имя игрока должно быть уникальным!");
        return false;
    }

    private boolean checkClientList(String userName) {
        if (clientList.isEmpty()) {
            // если список клиентов пустой,  разрешаем подключение
            return true;
        } else {
            for (MyClient client : clientList) {
                if (client.getName().equals(userName)) {
                    return false; // нашли клиента с таким именем, подключение запрещено
                }
            }

            return true;
        }
    }




}
