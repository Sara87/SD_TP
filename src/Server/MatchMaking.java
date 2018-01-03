package Server;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MatchMaking extends Thread implements Serializable{
    private int id;
    private int rank;
    private Map<String,String> heroes;
    private List<User> team1;
    private List<User> team2;
    private ReentrantLock heroeLock = new ReentrantLock();
    private int game;

    public MatchMaking(int rank, List<User> team1, List<User> team2) {
        this.rank = rank;
        this.team1 = team1;
        this.team2 = team2;
        this.heroes = new HashMap<>();
        this.game = 1;
    }

    public int getid() {
        return id;
    }

    public void setId(int i) {
        id = i;
    }

    public int getGame() {
        return game;
    }

    @Override
    public void run() {
        Thread w = new Thread();
        try {
            w.join(30000);
            if(heroes.size() == 3) {
                winTeam();
                this.game = 0;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String winTeam() {
        Random r = new Random();
        int result = r.nextInt(1);

        if (result == 0) {
            for (User u : team1)
                synchronized (u) {
                    u.setWins();
                }
            for (User u : team2)
                synchronized (u){
                    u.setLosses();
                }
            return "Team1";
        } else {
            for (User u : team2)
                synchronized (u) {
                    u.setWins();
                }
            for (User u : team1)
                synchronized (u){
                    u.setLosses();
                }
            return "Team2";
        }
    }

    public String checkHeroe(String user, String heroe) {
        StringBuilder sb = new StringBuilder();
        int t1 = team(user);

        heroeLock.lock();
        try {
            if (!this.heroes.containsValue(heroe)) {
                this.heroes.put(user, heroe);
                sb.append("\nHerói escolhido!");
            }
            else
                sb.append("\nHerói não está disponível. Escolha de novo!");

            for (Map.Entry<String, String> entry : this.heroes.entrySet()) {
                if (team(entry.getKey()) == t1)
                    sb.append("\n").append(entry.getKey()).append(" -> ").append(entry.getValue());
            }
        }finally {
            heroeLock.unlock();
        }
        return sb.toString();
        }


    /**
     * Retorna se o utilizador é da equipa 1 ou da equipa 2
     * @param user Username do utilizador
     * @return int Número da equipa
     */
    private int team(String user){
        if(this.team1.contains(user))
            return 1;
        return 2;
    }

}
