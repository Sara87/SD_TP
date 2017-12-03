package Servidor;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    private int gameId;
    private List<User> team1;
    private List<User> team2;
    private Map<String,Heroe> heroes; //username, heroe chosen
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

    public void winTeam(){
        Random r = new Random();
        int n1 = 0;
        int n2 = 1;
        int result = r.nextInt(n2-n1);

        if(result == 0) this.winTeam = "team1";
        else this.winTeam = "team2";
    }

    /*
    *TODO: metodo para construir a lista de herois escolhidos
     */

}
