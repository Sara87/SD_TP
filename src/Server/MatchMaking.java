package Server;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class MatchMaking extends Thread implements Serializable{
    private int id;
    private int rank;
    private Map<String,String> heroes;
    private List<User> team1;
    private List<User> team2;
    private ReentrantLock lock1;
    private ReentrantLock lock2;

    public MatchMaking(int id){
        this.id = id;
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

    @Override
    public void run() {

        try {
            sleep(3000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Tou aqui");
        if(heroes.size() == 3)
            winTeam();
    }

    public String winTeam() {
        Random r = new Random();
        int result = r.nextInt(1);

        if (result == 0) {
            for (User u : team1)
                u.setWins();
            for (User u : team2)
                u.setLosses();

            return "Team1";

        } else {
            for (User u : team1)
                u.setLosses();

            for (User u : team2)
                u.setWins();

            return "Team2";
        }
    }

    public String checkHeroe(String user, String heroe, List<String> h){ //todo: verificar melhor este metodo, no sei se esta bem acabado
        StringBuilder sb = new StringBuilder();

        int t1 = team(user);

        for(Map.Entry<String,String> s  : this.heroes.entrySet()){
            for(String s1 : h){
                if(s.getValue().equals(s1)) {
                    String u = s.getKey();
                    if (t1 == team(u))
                        sb.append(s).append("-").append("*").append("\n");
                    else sb.append(s).append("-").append("X").append("\n");
                }
                else sb.append(s);
                    }
                }
        if(!this.heroes.containsValue(heroe)) {
            this.heroes.put(user, heroe);
         }

    return sb.toString();
    }

    private int team(String user){

        if(this.team1.contains(user))
            return 1;

        return 2;
    }

}
