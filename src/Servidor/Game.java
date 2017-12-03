package Servidor;

import java.util.List;

public class Game {
    private int gameId;
    private List<User> team1;
    private List<User> team2;
    private String winTeam;

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



}
