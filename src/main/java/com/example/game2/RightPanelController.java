package com.example.game2;

import com.example.game2.models.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class RightPanelController {
    public static VBox addPlayerRightPanel(Player player){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setPadding(new Insets(0, 0, 0, 10));
        vBox.prefWidth(250);

        Label nameLabel = new Label("Игрок:");
        nameLabel.getStyleClass().add("text-label");
        Text name = new Text(player.getName());
        name.getStyleClass().add("text");
        vBox.getChildren().add(nameLabel);
        vBox.getChildren().add(name);

        Label shotsLabel = new Label("Выстрелов:");
        shotsLabel.getStyleClass().add("text-label");
        Text shots= new Text(Integer.toString(player.getShotCounter()));
        shots.getStyleClass().add("text");
        vBox.getChildren().add(shotsLabel);
        vBox.getChildren().add(shots);

        Label scoreLabel = new Label("Счет игрока:");
        scoreLabel.getStyleClass().add("text-label");
        Text score = new Text(Integer.toString(player.getScoreCounter()));
        score.getStyleClass().add("text");
        vBox.getChildren().add(scoreLabel);
        vBox.getChildren().add(score);

        Label winsLabel = new Label("Число побед:");
        winsLabel.getStyleClass().add("text-label");
        Text winsCount = new Text(Integer.toString(player.getWinsCounter()));
        winsCount.getStyleClass().add("text");
        vBox.getChildren().add(winsLabel);
        vBox.getChildren().add(winsCount);


        return vBox;
    }

    public static void setPlayerName (VBox vBox, String name){
        Text text = (Text) vBox.getChildren().get(1);
        text.setText(name);
    }

    public static void setShotCounter (VBox vBox, int shots){
        Text text = (Text) vBox.getChildren().get(3);
        text.setText(Integer.toString(shots));
    }

    public static void setScoreCounter (VBox vBox, int score){
        Text text = (Text) vBox.getChildren().get(5);
        text.setText(Integer.toString(score));
    }

    public static void setWinCounter (VBox vBox, int wins){
        Text text = (Text) vBox.getChildren().get(7);
        text.setText(Integer.toString(wins));
    }


}
