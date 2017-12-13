package Server;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class OverBlind implements Serializable {
    private Map<String, User> users;
    private List<String> heroes; // só para consulta, nunca vai ser alterada
    private Map<Integer, List<String>> waiting; // <rank, list<users>>
    private Map<Integer, MatchMaking> full;
    private ReentrantLock userLock;
    private AtomicInteger idMatch;

    public OverBlind() {
        this.users = new HashMap<>();
        this.heroes = new ArrayList<>();
        this.userLock = new ReentrantLock();
        this.idMatch = new AtomicInteger();
    }

    /**
     * Registo do utilizador na plataforma.
     *
     * @param username Username do novo registo.
     * @param pass     Password do novo registo.
     */
    public void register(String username, String pass) throws UserInvalidException {
        userLock.lock();
        try {
            if (this.users.containsKey(username))
                throw new UserInvalidException("Username já existente");

            User u = new User(username, pass);
            users.put(username, u);
        } finally {
            userLock.unlock();
        }
    }

    /**
     * Login do utilizador na plataforma.
     *
     * @param username Username do novo registo.
     * @param password Password do novo registo.
     */
    public synchronized User login(String username, String password) throws UserInvalidException {
        User u = new User();
        try {
            u = checkUser(username, password);
        } catch (Exception e) {
            throw new UserInvalidException(e.getMessage());
        }
        return u;
    }

    /**
     * Verifica se os dados do utilizador estão corretos
     *
     * @param username Username do utilizador
     * @param password password do utilizador
     */
    public User checkUser(String username, String password) throws UserInvalidException {
        User u = null;
        userLock.lock();
        try {
            for (String aux : users.keySet()) {
                if ((aux.equals(username)) && users.get(aux).comparePass(password))
                    u = this.users.get(aux);
            }
        } finally {
            userLock.unlock();
            if (u == null) throw new UserInvalidException("O utilizador não existe");
        }
        return u;
    }

    public String startWaiting(String username) throws InterruptedException {
        String heroes = " ";
        int r = this.users.get(username).getRank();

        int rank = whereToGo(username);

        if (rank != -1) {
            waiting.get(rank).add(username);

            if (waiting.get(rank).size() < 10) {//distinguir ultimo dos outros todos !!

                while (waiting.get(rank).size() < 10)
                    wait();
            } else {
                heroes = listHeroes();
                newMatchMaking(rank, waiting.get(rank));
                waiting.remove(rank, waiting.get(rank));
                notifyAll(); // ver isto muito melhor
            }
        } else {
            List<String> aux = new ArrayList<>();
            aux.add(username);
            waiting.put(r, aux);
        }

        return heroes;
    }

    private void newMatchMaking(int rank, List<String> strings) {
        List<User> team1 = new ArrayList<>();
        List<User> team2 = new ArrayList<>();

        strings.stream().limit(5).forEach(s -> {
            team1.add(users.get(s));
            strings.remove(s);
        });
        strings.stream().forEach(s -> team2.add(users.get(s)));

        MatchMaking m = new MatchMaking(rank, team1, team2);

        m.start();
        full.put(idMatch.getAndIncrement(), m);
    }

    private String listHeroes() {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        for (String h : heroes) {
            sb.append(i + 1);
            sb.append("-");
            sb.append(h).append("\n");
        }

        return sb.toString();
    }


    private synchronized int whereToGo(String username) { // ver se fica isto ou dá-se lock
        int rank = this.users.get(username).getRank();
        int rm = rank - 1, rM = rank + 1, go = -5;
        List<String> rankm = this.waiting.get(rank - 1);
        List<String> rankM = this.waiting.get(rank + 1);
        List<String> rankL = this.waiting.get(rank);
        boolean r = this.waiting.containsKey(rank);

        if (r) {

            int aux = existsRank(rankm, rankM, rank);
            if (aux == -2)
                go = Math.max(rankL.size(), Math.max(rankm.size(), rankM.size()));

            if (aux == rank - 1)
                go = Math.max(rankL.size(), rankm.size());

            if (aux == rank + 1)
                go = Math.max(rankL.size(), rankM.size());

            return go;
        } else return -1;
    }

    //método que, para além de verificar se o rank existe na lista do seu anterior e seguinte, retorna 0 se existir em ambos senão retorna em qual existe
    private int existsRank(List<String> playersm, List<String> playersM, int rank) {
        boolean b = false, b1 = false;

        if (rank != 0) {
            for (String p : playersm) {
                if (this.users.get(p).getRank() == rank) {
                    b = true;
                    break;
                }
            }

            for (String p1 : playersM) {
                if (this.users.get(p1).getRank() == rank) {
                    b1 = true;
                    break;
                }
            }
        } else {
            for (String p1 : playersM) {
                if (this.users.get(p1).getRank() == rank) {
                    b1 = true;
                    break;
                }
            }
        }

        if (b && b1) return -2;
        if (b && !b1) return rank - 1;
        if (!b && b1) return rank + 1;

        return -3;
    }
}

