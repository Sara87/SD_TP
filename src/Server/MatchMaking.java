package Server;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class MatchMaking extends Thread implements Serializable{

    private int rank;
    private Map<String,String> heroes;
    private List<User> team1;
    private List<User> team2;
    private ReentrantLock lock1;
    private ReentrantLock lock2;

    public MatchMaking() {
        this.rank = 0;
        this.heroes = new HashMap<>();
        this.team1 = new ArrayList<>();
        this.team2 = new ArrayList<>();
        this.lock1 = new ReentrantLock();
        this.lock2 = new ReentrantLock();
    }

    public MatchMaking(int rank, List<User> team1, List<User> team2) {
        this.rank = rank;
        this.team1 = team1;
        this.team2 = team2;
        this.heroes = new HashMap<>();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Map<String, String> getStrings() {
        return this.heroes;
    }

    public void setHeroes(Map<String, String> heroes) {
        this.heroes = heroes;
    }

    public List<User> getTeam1() {
        return team1;
    }

    public void setTeam1(List<User> team1) {
        this.team1 = team1;
    }

    public List<User> getTeam2() {
        return team2;
    }

    public void setTeam2(List<User> team2) {
        this.team2 = team2;
    }

    public ReentrantLock getLock1() {
        return lock1;
    }

    public void setLock1(ReentrantLock lock1) {
        this.lock1 = lock1;
    }

    public ReentrantLock getLock2() {
        return lock2;
    }

    public void setLock2(ReentrantLock lock2) {
        this.lock2 = lock2;
    }

    public void winTeam() {
        Random r = new Random();
        int result = r.nextInt(1);

        if (result == 0) {
            for (User u : team1)
                u.setWins();
            for (User u : team2)
                u.setLosses();
        } else {
            for (User u : team1)
                u.setLosses();

            for (User u : team2)
                u.setWins();
        }
    }
}