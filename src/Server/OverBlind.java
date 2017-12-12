package Server;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class OverBlind implements Serializable {
    private Map<String,User> users;
    private List<String> heroes; // só para consulta, nunca vai ser alterada
    private Map<Integer, List<String>> waiting; // <rank, list<users>>
    private Map<Integer, MatchMaking> full;
    private ReentrantLock userLock;
    private AtomicInteger idMatch;

    public OverBlind(){
        this.users = new HashMap<>();
        this.heroes = new ArrayList<>();
        this.userLock = new ReentrantLock();
        this.idMatch = new AtomicInteger();
    }

    public Map<String,User> getUsers() {
        return users;
    }

    public List<String> getHeroes() {
        return heroes;
    }

    public Map<Integer, List<String>> getWaiting() {
        return waiting;
    }

    public Map<Integer, MatchMaking> getFull() {
        return full;
    }

    public ReentrantLock getUserLock() {
        return userLock;
    }

    /**
     * Registo do utilizador na plataforma.
     *
     * @param username Username do novo registo.
     * @param pass Password do novo registo.
     */
    public void register(String username, String pass) throws UserInvalidException{
        userLock.lock();
        try{
            if(this.users.containsKey(username))
                throw new UserInvalidException("Username já existente");

            User u = new User(username, pass);
            users.put(username, u);
        }
        finally{
            userLock.unlock();
        }
    }

    /**
     * Login do utilizador na plataforma.
     *
     * @param username Username do novo registo.
     * @param password Password do novo registo.
     */
    public synchronized User login (String username, String password) throws UserInvalidException {
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
        }finally{
            userLock.unlock();
            if (u == null) throw new UserInvalidException("O utilizador não existe");
        }
        return u;
    }

    public String startWaiting (String username) {
        int rank = users.get(username).getRank();
        String heroes = " ";

        if (waiting.containsKey(rank)) {
            waiting.get(rank).add(username);
            //TODO: DEPOIS DE ADICIONAR COMO FAZER PARA FICAREM A ESPERA ATE TER OS 10 ?
            //TODO : COMO NOTIFICAR OS 10 ?
            if (waiting.get(rank).size() == 10) {
                heroes = listHeroes();
                newMatchMaking(rank, waiting.get(rank));
                waiting.remove(rank,waiting.get(rank));
            }
        }

        else if (waiting.containsKey(rank + 1)) {
            waiting.get(rank + 1).add(username);
            if (waiting.get(rank + 1).size() == 10) {
                heroes = listHeroes();
                newMatchMaking(rank + 1, waiting.get(rank + 1));
                waiting.remove(rank + 1,waiting.get(rank + 1));
            }
        }

        else if (waiting.containsKey(rank - 1)) {
            waiting.get(rank - 1).add(username);
            if (waiting.get(rank - 1).size() == 10) {
                heroes = listHeroes();
                newMatchMaking(rank - 1, waiting.get(rank - 1));
                waiting.remove(rank - 1,waiting.get(rank - 1));
            }
        }

        else {
            List<String> aux = new ArrayList<>();
            aux.add(username);
            waiting.put(rank,aux);
        }

        return heroes;
    }

    private void newMatchMaking(int rank, List<String> strings) {
        List<User> team1 = new ArrayList<>();
        List<User> team2 = new ArrayList<>();

        strings.stream().limit(5).forEach(s -> { team1.add(users.get(s)) ; strings.remove(s);});
        strings.stream().forEach(s ->  team2.add(users.get(s)));

        MatchMaking m = new MatchMaking(rank,team1,team2);

        m.start();
        full.put(idMatch.getAndIncrement(), m);
        }

    private String listHeroes(){
        StringBuilder sb = new StringBuilder();
        int i = 0;

        for(String h : heroes) {
            sb.append(i + 1);
            sb.append("-");
            sb.append(h).append("\n");
        }

        return sb.toString();
    }
}
