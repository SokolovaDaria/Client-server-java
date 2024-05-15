package com.example.game2;

import com.example.game2.models.Model;
import com.example.game2.models.ModelBuilder;
import com.example.game2.server.Response;
import com.example.game2.server.SocketMessageWrapper;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;

public class StartPageController {

    Socket socket;
    int PORT = 2003;
    InetAddress ip = null;
    SocketMessageWrapper socketMessageWrapper;
    Model model = ModelBuilder.build();;

    @FXML
    TextField nameField;
    @FXML
    void onConnect() {
        try {
            ip = InetAddress.getLocalHost();
            socket = new Socket(ip, PORT);
            socketMessageWrapper = new SocketMessageWrapper(socket);
            socketMessageWrapper.sendMessage(nameField.getText().trim());
            String response = socketMessageWrapper.getMessage();

            if (response.equals("ACCEPT")) {
                new Thread(
                        () -> {
                            while (true) {
                                String data = socketMessageWrapper.readData();

                                Gson gson = new Gson();
                                Response answer = gson.fromJson(data, Response.class);

                                model.setPlayerList(answer.getPlayerList()); // сервер посылает клиенту состояние модели
                                model.setTargetList(answer.getTargetList());
                                model.setArrowList(answer.getArrowList());
                                model.setWinner(answer.getWinner());
                                model.update();
                            }
                        }
                ).start();
                openGamePage();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка");
                alert.setContentText(response);

                alert.showAndWait();
                nameField.setText("");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void openGamePage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Parent root1 = fxmlLoader.load();
            Scene scene = new Scene(root1, 900, 600);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Shooter game");
            stage.setScene(scene);
            stage.show();

            GameController controller = fxmlLoader.getController();

            // controller.dataInit(socketMessageWrapper, nameField.getText().trim());
            controller.socketMessageWrapper = socketMessageWrapper;
            controller.playerName = nameField.getText().trim();


            model.update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
