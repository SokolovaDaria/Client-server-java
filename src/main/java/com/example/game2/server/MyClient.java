package com.example.game2.server;

import com.google.gson.Gson;
import com.example.game2.models.Model;
import com.example.game2.models.ModelBuilder;
import com.example.game2.models.Player;

public class MyClient implements Runnable {
    private final Player player;
    private final Model model;
    private final MyServer server;
    private final SocketMessageWrapper socketMessageWrapper;
    private final Gson gson;

    public MyClient(SocketMessageWrapper socketMessageWrapper, MyServer server, String name) {
        this.socketMessageWrapper = socketMessageWrapper;
        this.server = server;
        player = new Player(name);
        model = ModelBuilder.build();
        gson = new Gson();
    }

    public String getName() {
        return player.getName();
    }

    public void sendToClient() { // отправляем текущие данные из модели
        Response ResponseFromServer = new Response();
        ResponseFromServer.setPlayerList(model.getPlayerList());
        ResponseFromServer.setTargetList(model.getTargetList());
        ResponseFromServer.setArrowList(model.getArrowList());
        ResponseFromServer.setWinner(model.getWinner());

        ResponseFromServer.setEntityList(model.getMyEntityList());

        socketMessageWrapper.writeData(gson.toJson(ResponseFromServer));
    }

    @Override
    public void run() {
        model.addNewPlayer(player);
        server.broadcast(); // рассылаем текущее состояние игры всем клиентам

        while (true) {
            String data = socketMessageWrapper.readData(); // принимаем входящие сообщения от клиента

            System.out.println(data);

            Request message = gson.fromJson(data, Request.class); // преобразуем из json в ClientRequest

            if (message.getClientActions() == Action.isReady) {
                model.readyToStart(server, getName());
            } else if (message.getClientActions() == Action.shoot) {
                model.requestToShooting(getName());
            } else if (message.getClientActions() == Action.pause) {
                model.requestToPause(getName());
            } else if (message.getClientActions() == Action.showTable) {
                 model.updateWinTable(server);
            }

        }
    }

}
