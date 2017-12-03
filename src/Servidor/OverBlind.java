package Servidor;


import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class OverBlind {
    private Map<String,User> users;
    private Map<String, Heroe> heroes;
    //private Map<Integer, Game> games;
    private ReentrantLock userLock;
    private ReentrantLock gameLock;

    public OverBlind(){}

    /**
     * Login do utilizador na plataforma.
     * @param username Username do novo registo.
     * @param password Password do novo registo.
     */
    public User login (String username, String password) throws UserInvalidException {
        User u;
        try {
            u = validaUtilizador(username, password);
        } catch (Exception e) {
            throw new UserInvalidException(e.getMessage());
        }
        return u;
    }

    /**
     * Verifica se os dados do utilizador estão corretos
     * @param username Username do utilizador
     * @param password password do utilizador
     */
    public User validaUtilizador(String username, String password) throws UserInvalidException {
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
