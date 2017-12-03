package Servidor;


import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class OverBlind {
    private Map<String,User> users;
    private Map<String, String> heroes;
    private ReentrantLock userLock;
    private ReentrantLock gameLock;

    public OverBlind(){}

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
}
