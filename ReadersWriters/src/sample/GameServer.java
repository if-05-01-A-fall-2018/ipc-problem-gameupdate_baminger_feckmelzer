package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class GameServer extends Observable {

    private List<Player> players = new ArrayList<>();
    private boolean updating = false;

    public synchronized boolean connect(Player player) {
        boolean connected = players.add(player) && !updating;
        if (connected) {
            setChanged();
            notifyObservers(Integer.toString(players.size()));
        }
        return connected;
    }

    public synchronized void disconnect(Player player) {
        boolean disconnected = players.remove(player) && !updating;
        if (disconnected) {
            setChanged();
            notifyObservers(Integer.toString(players.size()));
        }
    }

    public synchronized boolean isUpdating() {
        return updating;
    }

    public synchronized boolean update(boolean forceUpdate) {
        if (forceUpdate) {
            players.removeAll(players);
            updating = true;
        } else if (players.size() == 0) {
            updating = true;
        }
        setChanged();
        notifyObservers(Integer.toString(players.size()));
        return updating;
    }

    public synchronized void restart() {
        updating = false;
    }
}
