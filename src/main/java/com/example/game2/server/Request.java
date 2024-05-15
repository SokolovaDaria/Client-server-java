package com.example.game2.server;

public class Request {
    private Action clientActions;

    public Request(Action clientActions) {
        this.clientActions = clientActions;
    }

    public Action getClientActions() {
        return clientActions;
    }

}
