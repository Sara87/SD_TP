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
    private ReentrantLock lock1 = new ReentrantLock();
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

    public String checkHeroe(String user, String heroe, List<String> h) { //todo: verificar melhor este metodo, no sei se esta bem acabado
        StringBuilder sb = new StringBuilder();
        int t1 = team(user);

        lock1.lock();
        try {
            if (!this.heroes.containsValue(heroe)) {
                this.heroes.put(user, heroe);
                sb.append("Herói escolhido!\n");
            }
            else
                sb.append("Herói não está disponível. Escolha de novo!\n");

            for (Map.Entry<String, String> entry : this.heroes.entrySet()) {
                if (team(entry.getKey()) == t1)
                    sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
            }
        }finally {
            lock1.unlock();
        }

        /*if(!this.heroes.containsValue(heroe)) {

            this.heroes.put(user, heroe);
            for (String s1 : h) {
                for (Map.Entry<String, String> s : this.heroes.entrySet()) {
                    if (s.getValue().equals(s1)) {
                        String u = s.getKey();
                        if (t1 == team(u))
                            sb.append(s.getValue()).append("*- ").append(u).append("\n");
                    }
                    else
                        sb.append(s.getValue()).append("\n");
                }
            }
            sb.append("\n").append("Heroi escolhido").append("\n"); //todo: mudar

        }
        else {
            for (Map.Entry<String, String> s : this.heroes.entrySet()) {
                for (String s1 : h) {
                    if (s.getValue().equals(s1)) {
                        String u = s.getKey();
                        if (t1 == team(u))
                            sb.append(s).append("*- ").append(u).append("\n");
                    } else sb.append(s);
                }
            }

            sb.append("\n").append("Não pode escolher este herói. Tente outra vez.").append("\n");
        }*/
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
