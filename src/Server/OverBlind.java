package Server;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class OverBlind implements Serializable {
    private Map<String,User> users;
    private List<String> heroes; // só para consulta, nunca vai ser alterada
    private Map<Integer, MatchMaking> waiting; // <rank, mm>
    private Map<Integer, MatchMaking> full;
    private ReentrantLock userLock;

    public OverBlind(){
        this.users = new HashMap<>();
        this.heroes = new ArrayList<>();
        this.userLock = new ReentrantLock();
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

}
