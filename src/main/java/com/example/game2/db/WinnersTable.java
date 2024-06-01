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
    public void start(Stage primaryStage) {

        List<MyEntity> players = playerDataAccess.getAllPlayersFromBD();

        ObservableList<MyEntity> playerData = FXCollections.observableArrayList(players);

        TableView<MyEntity> tableView = new TableView<>(playerData);

        TableColumn<MyEntity, String> nameColumn = new TableColumn<>("Игрок");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MyEntity, Integer> winsColumn = new TableColumn<>("Число побед");
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));

        tableView.getColumns().addAll(nameColumn, winsColumn);

        VBox container = new VBox(tableView);

        Scene scene = new Scene(container, 400, 300);
        primaryStage.setTitle("Таблица лидеров");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}