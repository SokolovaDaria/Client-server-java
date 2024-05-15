package com.example.game2.db;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class WinnersTable extends Application {

    private PlayerDataAccess playerDataAccess;
    public WinnersTable(PlayerDataAccess playerDataAccess) {
        this.playerDataAccess = playerDataAccess;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Получаем список всех игроков из базы данных
        List<MyEntity> players = playerDataAccess.getAllPlayersFromBD();

        // Создаем наблюдаемый список для данных таблицы
        ObservableList<MyEntity> playerData = FXCollections.observableArrayList(players);

        // Создаем таблицу и задаем колонки
        TableView<MyEntity> tableView = new TableView<>(playerData);

        TableColumn<MyEntity, String> nameColumn = new TableColumn<>("Игрок");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MyEntity, Integer> winsColumn = new TableColumn<>("Число побед");
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));

        // Добавляем колонки в таблицу
        tableView.getColumns().addAll(nameColumn, winsColumn);

        // Создаем контейнер для размещения таблицы
        VBox root = new VBox(tableView);

        // Создаем сцену и отображаем ее
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Таблица лидеров");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}