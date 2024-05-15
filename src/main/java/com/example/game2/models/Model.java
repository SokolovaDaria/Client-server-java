package com.example.game2.models;

import com.example.game2.db.MyEntity;
import com.example.game2.db.PlayerDataAccess;
import com.example.game2.server.IObserver;
import com.example.game2.server.MyServer;

import java.util.ArrayList;
import java.util.List;


public class Model {
    private String winner = null;
    private List<Player> playerList = new ArrayList<>();

    final PlayerDataAccess playerDataAccess;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

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

    private List<String> readyToStartList = new ArrayList<>();
    ;
    private List<String> waitList = new ArrayList<>();
    ;
    private List<String> shootList = new ArrayList<>();
    ;
    private List<Point> targetList = new ArrayList<>();
    ;
    private List<Point> arrowList = new ArrayList<>();
    ;

    public List<MyEntity> getMyEntityList() {
        return myEntityList;
    }

    private List<MyEntity> myEntityList = new ArrayList<>();;

    private List<IObserver> observerList = new ArrayList<>();

    private final int maxScore = 30;
    private int dirLeftTarget = 1;
    private int dirRightTarget = 1;



    private volatile boolean flagReset = true;
    private boolean gameRunning = true;

    private final double arrowLength = 60;
    private final double borderY = 500;
    private final double arrowXStart = 10;
    private final double leftX = 350;
    private final double targetY = 250;
    private final double rightX = 450;
    private final double leftTargetSpeed = 2.5;
    private final double rightTargetSpeed = 5;
    private final double arrowSpeed = 3.5;
    private final double arrowEnd = 450;

    public Model(PlayerDataAccess playerDataAccess) {
        this.playerDataAccess = playerDataAccess;
    }
    public void initialize() {
        targetList.add(new Point(leftX, targetY, 50));
        targetList.add(new Point(rightX, targetY, 25));

        arrowList.clear();
        // размещаем стартовые позиции
        int countOfPlayers = playerList.size();
        for (int i = 1; i <= countOfPlayers; i++) {
            double step = borderY / (countOfPlayers + 1);
            arrowList.add(new Point(arrowXStart, step * i, arrowLength));
        }
    }

    public void addNewPlayer(Player player) {
        MyEntity playerEntity = MyEntity.builder()
                .name(player.getName())
                .wins(0)
                .build();
        playerList.add(player);
        arrowList.clear();


        myEntityList.add(playerEntity);
        playerDataAccess.addPlayerToBD(playerEntity);
        MyEntity myEntity = playerDataAccess.getPlayerFromBD(playerEntity.getName());
        player.setWinsCounter(myEntity.getWins());


        // размещаем стартовые позиции стрел
        int countClients = playerList.size();
        for (int i = 1; i <= countClients; i++) {
            double step = borderY / (countClients + 1);
            arrowList.add(new Point(arrowXStart, step * i, arrowLength));
        }
    }

    public void readyToStart(MyServer server, String name) {
        if (readyToStartList.contains(name)) {
            readyToStartList.remove(name);
        } else {
            readyToStartList.add(name);
        }

        if (playerList.size() > 1 && readyToStartList.size() == playerList.size()) { // все готовы
            flagReset = false;
            START(server);
        }
    }

    private void START(MyServer server) {
        Thread thread = new Thread(
                () -> {
                    while (gameRunning) {
                        if (flagReset) {
                            winner = null;
                            break;
                        }
                        if (waitList.size() != 0) {
                            synchronized (this) {
                                try {
                                    wait();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        moveTargets();

                        if (shootList.size() != 0) {
                            shoot();
                        }

                        server.broadcast();

                        try {
                            Thread.sleep(16);
                        } catch (InterruptedException ignored) {

                        }
                    }
                }
        );
        thread.start();
    }

    private void moveTargets() {
        Point first = targetList.get(0);
        Point second = targetList.get(1);


        if ((first.getY() <= first.getRadius())                       // цель дошла до границы
                || (borderY - first.getY() <= first.getRadius())
        ) {
            dirLeftTarget *= -1;
        }
        targetList.get(1).setY(targetList.get(1).getY() - rightTargetSpeed * dirRightTarget);




        if ((second.getY() <= second.getRadius())                     // цель дошла до границы
                || (borderY - second.getY() <= second.getRadius())
        ) {
            dirRightTarget *= -1;
        }
        targetList.get(0).setY(targetList.get(0).getY() - leftTargetSpeed * dirLeftTarget);

    }

    private void shoot() {
        for (int i = 0; i < shootList.size(); i++) {
            if (shootList.get(i) == null) {
                break;
            }
            int j = i;
            Player player = null;
            for (Player data : playerList) {
                if (data.getName().equals(shootList.get(j))) {
                    player = data;
                    break;
                }
            }
            int index = playerList.indexOf(player);
            Point arrow = arrowList.get(index);
            arrow.setX(arrow.getX() + arrowSpeed);



            if (isHit(targetList.get(1), arrow.getX() + arrow.getRadius(), arrow.getY())) {
                player.addScore(20);
            } else if (isHit(targetList.get(0), arrow.getX() + arrow.getRadius(), arrow.getY())) {
                player.addScore(10);
            } else if (arrow.getX() > arrowEnd) {
            } else {
                return;
            }


            arrow.setX(arrowXStart);  // сбрасываем положение стрелы

            if (shootList.size() == 1) {
                shootList.clear();
            } else {
                shootList.remove(player.getName());
            }

            //проверка на победу
            for (Player dataManager : playerList) {
                if (dataManager.getScoreCounter() >= maxScore) {
                    this.winner = dataManager.getName();
                    RESET();
                  //  break;


                    MyEntity playerWinner = null;

                    for (MyEntity entity : myEntityList) {
                        if (entity.getName().equals(winner)) {
                            playerWinner = entity;
                            break;
                        }
                    }

                    if (playerWinner != null) {
                        playerWinner.incrementWins();
                    } else {
                        throw new RuntimeException("Winner not found in myEntityList");
                    }



                    Player p = null;

                    for (Player player1 : playerList) {
                        if (player1.getName().equals(winner)) {
                            p = player1;
                            break;
                        }
                    }
                    if (p != null) {
                        p.incrementWins();
                    } else {
                        throw new RuntimeException("Winner not found in playerList");
                    }


                    PlayerDataAccess.updatePlayerInBD(playerWinner);
                }
            }

        }
    }

    public void requestToShooting(String playerName) {
        if (flagReset) {
            return;
        }
        Player player = null;
        for (Player clientData : playerList) {
            if (clientData.getName().equals(playerName)) {
                player = clientData;
                break;
            }
        }
        if (player == null) {
            throw new IllegalStateException("Player not found");
        }
        if (!shootList.contains(player.getName())) {
            shootList.add(player.getName());
            player.addShots();
        }
    }

    public void requestToPause(String playerName) {
        if (flagReset) {
            return;
        }

        if (waitList.contains(playerName)) {
            waitList.remove(playerName);
            if (waitList.size() == 0) {
                synchronized (this) {
                    notifyAll();
                }
            }
        } else {
            waitList.add(playerName);
        }
    }

    private boolean isHit(Point target, double x, double y) {

        return (Math.sqrt(Math.pow((x - target.getX()), 2) + Math.pow((y - target.getY()), 2))
                < target.getRadius()
        );
    }

    private void RESET() {
        flagReset = true;
        readyToStartList.clear();
        targetList.clear();
        arrowList.clear();
        waitList.clear();
        shootList.clear();
        playerList.forEach(Player::reset);
        this.initialize();
    }


    public void update() {
        for (IObserver observer : observerList) {
            observer.update();
        }
    }

    public void addObserver(IObserver gbc) {
        observerList.add(gbc);
    }

    public void updateWinTable(MyServer server) {
        myEntityList = PlayerDataAccess.getAllPlayersFromBD();
        server.broadcast();
    }

}
