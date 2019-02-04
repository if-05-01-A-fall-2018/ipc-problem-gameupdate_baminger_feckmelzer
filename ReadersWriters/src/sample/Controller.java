package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller implements Initializable, Observer {

    private static final int PLAYER_AMOUNT = 10;

    @FXML
    private Label playerCountLabel, updaterLabel;

    private GameServer gameServer = new GameServer();
    private ExecutorService players = Executors.newScheduledThreadPool(PLAYER_AMOUNT);
    private Updater updater = new Updater();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameServer.addObserver(this);
        updater.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        Platform.runLater(() -> {
            if (o instanceof Updater) updaterLabel.setText("Updater: " + arg);
            else playerCountLabel.setText("Players online: " + arg);
        });
    }

    public void handleStartButton(ActionEvent event) {
        for (int i = 0; i < PLAYER_AMOUNT; i++) {
            Player newPlayer = new Player(i, gameServer);
            players.submit(newPlayer);
        }
    }

    public void handleUpdateButton(ActionEvent event) {
        updater.setUpdatee(gameServer);
        Thread updaterThread = new Thread(updater);
        updaterThread.setDaemon(true);
        updaterThread.start();
    }
}
