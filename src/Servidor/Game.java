package Servidor;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    private int gameId;
    private List<User> team1;
    private List<User> team2;
    private List<Heroe> heroes;
    private ReentrantLock heroeLock;
    private String winTeam; //random entre team1 e team2

    public Game(int id, List<User> team1, List<User> team2) {
        this.gameId = id;
        this.team1 = team1;
        this.team2 = team2;
        this.winTeam = "";
    }

    public int getGameId() {
        return gameId;
    }

    public String getWinTeam() {
        return winTeam;
    }

    @Override
    public void run() {
    }
}
