package Servidor;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class MatchMaking extends Thread{

    private int rank;
    private Map<Integer,String> heroes;
    private List<User> team1;
    private List<User> team2;
    private ReentrantLock lock1;
    private ReentrantLock lock2;
    private String winTeam;


    public MatchMaking() {
        this.rank = 0;
        this.heroes = new HashMap<>();
        this.team1 = new ArrayList<>();
        this.team2 = new ArrayList<>();
        this.lock1 = new ReentrantLock();
        this.lock2 = new ReentrantLock();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Map<Integer, String> getStrings() {
        return this.heroes;
    }

    public void setHeroes(Map<Integer, String> heroes) {
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

    public void winTeam(){
        Random r = new Random();
        int result = r.nextInt(1);

        if(result == 0) this.winTeam = "team1";
        else this.winTeam = "team2";
    }


}
