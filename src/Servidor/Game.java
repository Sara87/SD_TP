package Servidor;

import java.util.List;
import java.util.Random;

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

    public void winTeam(){
        Random r = new Random();
        int result = r.nextInt(1);

        if(result == 0) this.winTeam = "team1";
        else this.winTeam = "team2";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (gameId != game.gameId) return false;
        if (!team1.equals(game.team1)) return false;
        if (!team2.equals(game.team2)) return false;
        return winTeam != null ? winTeam.equals(game.winTeam) : game.winTeam == null;
    }

    @Override
    public String toString() {
        return "Game{" + "gameId=" + gameId +
                ", team1=" + team1 +
                ", team2=" + team2 +
                ", winTeam='" + winTeam + '\'' +
                '}';
    }
}
