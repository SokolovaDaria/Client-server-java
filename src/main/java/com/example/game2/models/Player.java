package com.example.game2.models;

public class Player {
    String name;
    int shotCounter;
    int scoreCounter;

    public void setWinsCounter(int winsCounter) {
        this.winsCounter = winsCounter;
    }
    int winsCounter;

    public String getName() {
        return name;
    }
    public int getShotCounter() {
        return shotCounter;
    }
    public int getScoreCounter() {
        return scoreCounter;
    }
    public int getWinsCounter() {
        return winsCounter;
    }
    public void incrementWins() {
         winsCounter++;
    }
    public Player(String name) {
        this.name = name;
    }
    public void addShots() {
        shotCounter++;
    }
    public void addScore(double score) {
        scoreCounter += score;
    }
    public void reset() {
        shotCounter = 0;
        scoreCounter = 0;
    }
}
