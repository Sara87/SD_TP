package Servidor;


import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class OverBlind {
    private Map<String,User> users;
    private Map<String, Heroe> heroes;
    private Map<Integer, Game> games;
    private ReentrantLock userLock;
    private ReentrantLock heroeLock;
    private ReentrantLock gameLock;

    public OverBlind(){}
}
