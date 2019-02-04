package sample;

import java.util.Random;

public class Player implements Runnable {
    // Maximum seconds the player plays for.
    private static final int MAX_WAIT = 10;

    private final int id;
    private GameServer gameServer;
    private boolean online = false;
    private Random random = new Random();

    public Player(int id, GameServer gameServer) {
        this.id = id;
        this.gameServer = gameServer;
    }

    @Override
    public void run() {
        while(true) {
            waitForServer();
            online = joinServer();
            if (gameServer.isUpdating()) waitForServer();
            else if (online) play();
        }
    }

    public int getId() {
        return id;
    }

    private void play() {
        try {
            Thread.sleep(getRandomMilliseconds());
            leaveServer();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void waitForServer() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean joinServer() {
        return gameServer.connect(this);
    }

    private void leaveServer() {
        gameServer.disconnect(this);
    }

    private int getRandomMilliseconds() {
        return random.nextInt(MAX_WAIT) * 1000;
    }
}
