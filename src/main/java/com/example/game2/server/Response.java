package com.example.game2.server;

import com.example.game2.db.MyEntity;
import com.example.game2.models.Player;
import com.example.game2.models.Point;

import java.util.List;

public class Response {
    List<Player> playerList;
    List<Point> targetList;
    List<Point> arrowList;
    String winner;

    public List<MyEntity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<MyEntity> entityList) {
        this.entityList = entityList;
    }

    List<MyEntity> entityList;

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public List<Point> getTargetList() {
        return targetList;
    }

    public void setTargetList(List<Point> targetList) {
        this.targetList = targetList;
    }

    public List<Point> getArrowList() {
        return arrowList;
    }

    public void setArrowList(List<Point> arrowList) {
        this.arrowList = arrowList;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

}
