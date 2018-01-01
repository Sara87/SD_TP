package Server;

import java.io.*;
import java.util.*;
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
        this.waiting = new HashMap<>();
        this.full = new HashMap<>();
        heroesNames();

        //TODO: tirar isto daqui, so para testar
        try {
            register("admin", "admin");
            register("a", "a");
            register("b", "b");
            register("c", "c");
        } catch (UserInvalidException e) {
            e.printStackTrace();
        }
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
        User u;
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

    //TODO: 3, mudar p 5
    public synchronized String startWaiting(String username) throws InterruptedException {
        String heroes = " ";
        int r = this.users.get(username).getRank();
        int idMM = -1;
        StringBuilder sb = new StringBuilder();

        int rank = whereToGo(username);
        System.out.println("rank: " + rank);

        if (rank != -1) {
            waiting.get(rank).add(username);

            if (waiting.get(rank).size() < 3) {//distinguir ultimo dos outros todos
                System.out.println("Esperando");
                if(waiting.get(rank).size() < 3) {
                    wait();
                }
            } else {
                idMM = newMatchMaking(rank, waiting.get(rank));
                notifyAll(); // ver isto muito melhor
                waiting.remove(rank, waiting.get(rank));
            }
        } else {
            List<String> aux = new ArrayList<>();
            aux.add(username);
            waiting.put(r, aux);
            if(waiting.get(r).size() < 3)
                wait();
        }

        heroes = listHeroes();
        sb.append(idMM).append("\n").append(heroes);

        return sb.toString();
    }

    private int newMatchMaking(int rank, List<String> strings) {
        List<User> team1 = new ArrayList<>();
        List<User> team2 = new ArrayList<>();
        int i = 1;//TODO: METER i = 5

        for(String s : strings) { //TODO: METER limit(5) e no i também, so p teste
            team1.add(users.get(s));
            strings.remove(s);
            i--;
            if(i == 0)
                break;
        }

        strings.stream().forEach(s -> team2.add(users.get(s)));

        MatchMaking m = new MatchMaking(rank, team1, team2);
        System.out.println("MatchMaking criado, rank : "+ rank);
        m.start();
        int id = idMatch.getAndIncrement();
        full.put(id, m);
        return id;
    }

    private String listHeroes() {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        for (String h : heroes)
            sb.append(h).append("\n");

        return sb.toString();
    }


    private synchronized int whereToGo(String username) { // ver se fica isto ou dá-se lock
        int rank = this.users.get(username).getRank();
        int go = -5;
        List<String> rankm = this.waiting.get(rank - 1);
        List<String> rankM = this.waiting.get(rank + 1);
        List<String> rankL = this.waiting.get(rank);
        boolean r = this.waiting.containsKey(rank);
        System.out.println("existe rank dos gajos? " + r);
        if (r) {

            int aux = existsRank(rankm, rankM, rank);
            System.out.println("Aux : " + aux);
            if (aux == -2)
                go = Math.max(rankL.size(), Math.max(rankm.size(), rankM.size()));

            if (aux == rank - 1)
                go = Math.max(rankL.size(), rankm.size());

            if (aux == rank + 1)
                go = Math.max(rankL.size(), rankM.size());

            if(aux == -3)
                go = rank;

            return go;
        } else return -1;
    }

    //método que, para além de verificar se o rank existe na lista do seu anterior e seguinte, retorna -2 se existir em ambos senão retorna em qual existe
    private int existsRank(List<String> playersm, List<String> playersM, int rank) {
        boolean b = false, b1 = false;

        if (rank != 0) {
            if (playersm != null) {
                for (String p : playersm) {
                    if (this.users.get(p).getRank() == rank) {
                        b = true;
                        break;
                    }
                }
            }

            if (playersM != null) {
                for (String p1 : playersM) {
                    if (this.users.get(p1).getRank() == rank) {
                        b1 = true;
                        break;
                    }
                }
            }
        } else {
            if(playersM != null) {
                for (String p1 : playersM) {
                    if (this.users.get(p1).getRank() == rank) {
                        b1 = true;
                        break;
                    }
                }
            }
        }

        if (b && b1) return -2;
        if (b && !b1) return rank - 1;
        if (!b && b1) return rank + 1;

        return -3;
    }

    public String checkHeroe(String user, int id, int heroe){
        MatchMaking m = this.full.get(id);
        String h = heroes.get(heroe);
        String str = m.checkHeroe(user, h, heroes);

        // esperar os 30 segundos

        return str;
    }

    private void heroesNames() {
        String[] names = {
                "Lord",
                "Ryu",
                "Squirtle",
                "Cientist",
                "Hurricane",
                "Eletro",
                "Tymoschuk",
                "ElPaquito",
                "Aurélio",
                "Broken",
                "Boogeyman",
                "Nikita",
                "Shaman-King",
                "Houdini",
                "Yoda",
                "Satan",
                "Paces",
                "X",
                "Dolly",
                "2-Pack",
                "Puyol",
                "Tchabs",
                "Candace",
                "Montain",
                "Piromaniac",
                "MiniMilk",
                "Camel",
                "Koda",
                "Marine",
                "Choosen"
        };

        this.heroes = Arrays.asList(names);
    }
}

