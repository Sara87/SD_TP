package Server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class OverBlind implements Serializable {
    private Map<String,User> users;
    private Map<String, String> heroes;
    private ReentrantLock userLock;
    private ReentrantLock gameLock;

    public OverBlind(){
        this.users = new HashMap<>();
        this.heroes = new HashMap<>();
        this.userLock = new ReentrantLock();
        this.gameLock = new ReentrantLock();
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

    /**
     * Guardar o estado da aplicação num determinado ficheiro.
     * @param fich
     */
    /*
    public void saveObj(String fich) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fich));
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }*/


    /**
     * Iniciar a aplicação com o estado guardado num determinado ficheiro.
     * @param fich
     */
    /*
    public static OverBlind readObj(String fich) throws IOException, ClassNotFoundException {
        ObjectInputStream os = new ObjectInputStream(new FileInputStream(fich));
        OverBlind o = (OverBlind) os.readObject();
        os.close();
        return o;
    }
    */

}
