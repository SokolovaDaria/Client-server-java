package com.example.game2;

import com.example.game2.db.MyEntity;
import com.example.game2.db.PlayerDataAccessBuilder;
import com.example.game2.db.WinnersTable;
import com.example.game2.models.Model;
import com.example.game2.models.ModelBuilder;
import com.example.game2.models.Player;
import com.example.game2.models.Point;
import com.example.game2.server.IObserver;
import com.example.game2.server.SocketMessageWrapper;
import com.example.game2.server.Action;
import com.example.game2.server.Request;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameController implements IObserver {
    @FXML
    VBox informationField;
    @FXML
    Pane gamePanel;
    @FXML
    VBox playersField;
    private final Model model = ModelBuilder.build();
    String playerName;
    boolean showTableFlag = false;
    SocketMessageWrapper socketMessageWrapper;
    private final Gson gson = new Gson();
    private final List<Button> players = new ArrayList<>();
    private final List<VBox> playersInformation = new ArrayList<>();
    private final List<Circle> targets = new ArrayList<>();
    private final List<Line> arrows = new ArrayList<>();


    public void initialize() {
        model.addObserver(this);
    }                                                                              // контроллер устанавливает себя в качестве наблюдателя  для модели, чтобы получать уведомления о изменениях

    @FXML
    void ready() {                                         // формируем запрос и отправляем на сервер
        Request request = new Request(Action.isReady);
        socketMessageWrapper.writeData(gson.toJson(request));
    }

    @FXML
    void shoot() {
        Request request = new Request(Action.shoot);
        socketMessageWrapper.writeData(gson.toJson(request));
    }

    @FXML
    void pause() {
        Request request = new Request(Action.pause);
        socketMessageWrapper.writeData(gson.toJson(request));
    }

    @FXML
    void tableWinners() {
        Request request = new Request(Action.showTable);
        socketMessageWrapper.writeData(gson.toJson(request));

        showTableFlag = true;
    }

    @Override
    public void update() throws Exception {
        // проверка победителя
        String winner = model.getWinner();
        if (winner != null) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setContentText(

                        ((winner.equals(this.playerName)
                                ? "Вы победили!"
                                : "Победил " + winner))
                );
                alert.showAndWait();
            });
        }

        // обновление мишеней
        List<Point> targetList = model.getTargetList();

        if (targetList != null && targetList.size() != 0) {
            Platform.runLater(() -> {
                for (int i = 0; i < targetList.size(); i++) {
                    if (i >= targets.size()) {
                        Point point = targetList.get(i);
                        Circle target = new Circle(point.getX(), point.getY(), point.getRadius());
                        target.getStyleClass().add("targets");
                        targets.add(target);

                        gamePanel.getChildren().add(target);

                    }  else {
                        targets.get(i).setCenterY(targetList.get(i).getY()); // меняем координаты
                        targets.get(i).setCenterX(targetList.get(i).getX());
                        targets.get(i).setRadius(targetList.get(i).getRadius());
                    }
                }
            });
        }

        // обновление информации об игроках
        List<Player> playerList = model.getPlayerList();
        if (playerList != null && playerList.size() != 0) {

            Platform.runLater(() -> {
                for (int i = 0; i < playerList.size(); i++) {
                    if (i >= players.size()) {
                        VBox vBox = RightPanelController.addPlayerRightPanel(playerList.get(i));
                        playersInformation.add(vBox);

                        informationField.getChildren().add(vBox);
                    } else {
                        RightPanelController.setPlayerName(playersInformation.get(i), playerList.get(i).getName());
                        RightPanelController.setShotCounter(playersInformation.get(i), playerList.get(i).getShotCounter());
                        RightPanelController.setScoreCounter(playersInformation.get(i), playerList.get(i).getScoreCounter());
                        RightPanelController.setWinCounter(playersInformation.get(i), playerList.get(i).getWinsCounter());
                    }
                }
            });
        }

        // обновление игроков
        if (playerList != null || playerList.size() == 0 && players.size() != playerList.size()) {

            Platform.runLater(() -> {
                for (int i = 0; i < playerList.size(); i++) {
                    if (i >= players.size()) {
                        Button button = new Button();
                        button.setPrefHeight(120);
                        button.setPrefWidth(120);

                        if (playerList.get(i).getName().equals(playerName)) {
                            button.getStyleClass().add("player-client");

                        } else {
                            button.getStyleClass().add("player-connect");
                        }

                        players.add(button);
                        playersField.getChildren().add(button);
                    }
                }
            });
        }

        // обновление стрел
        List<Point> arrowList = model.getArrowList();
        if (arrowList != null && !arrowList.isEmpty()) {
            Platform.runLater(() -> {
                arrows.forEach(arrow -> gamePanel.getChildren().remove(arrow));
                for (Point point : arrowList) {
                    Line arrow = new Line(
                            point.getX(), point.getY(), point.getX() + point.getRadius(),
                            point.getY()
                    );
                    arrows.add(arrow);
                    gamePanel.getChildren().add(arrow);
                }
            });
        }


        if (showTableFlag && model.getMyEntityList() != null && model.getMyEntityList().size() != 0) {
           // showTable();
            WinnersTable winnersTable = new WinnersTable(PlayerDataAccessBuilder.build());
            winnersTable.start(new Stage());
            showTableFlag = false;
        }

    }
//    private void showTable() {
//        Platform.runLater(() -> {
//            TableView<MyEntity> tableView = new TableView<>();
//
//            TableColumn<MyEntity, String> column1 =
//                    new TableColumn<>("Игрок");
//
//            column1.setCellValueFactory(
//                    new PropertyValueFactory<>("name"));
//
//            TableColumn<MyEntity, String> column2 =
//                    new TableColumn<>("Число побед");
//
//            column2.setCellValueFactory(
//                    new PropertyValueFactory<>("wins"));
//
//            tableView.getColumns().add(column1);
//            tableView.getColumns().add(column2);
//
//            model.getMyEntityList().forEach(tableView.getItems()::add);
//
//            VBox vbox = new VBox(tableView);
//            Scene scene = new Scene(vbox);
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.setTitle("Таблица лидеров");
//            stage.show();
//        });
//    }
}
