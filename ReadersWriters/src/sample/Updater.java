package sample;

import java.util.Observable;

public class Updater extends Observable implements Runnable {
    private GameServer updatee;
    private int trys;

    @Override
    public void run() {
        trys = 0;
        while (!update()) {
            try {
                trys++;
                setChanged();
                notifyObservers("Update pending(" + trys + ")");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
        try {
            Thread.sleep(3000);
            setChanged();
            notifyObservers("Server updated");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updatee.restart();
    }

    public void setUpdatee(GameServer updatee) {
        this.updatee = updatee;
    }

    private boolean update() {
        boolean updating;
        if (trys == 3) {
            updating = updatee.update(true);
        } else {
            updating = updatee.update(false);
        }
        return updating;
    }
}
