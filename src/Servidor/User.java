package Servidor;

import java.io.IOException;
import java.net.Socket;

public class User implements Comparable<User>{
    private String username;
    private String password;
    private int rank;
    private int wins;
    private int losses;
    private Socket socket;

    public User(){}


    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getWins() {
        return wins;
    }

    public void setWins() {
        this.wins++;
        updateRank();
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses() {
        this.losses++;
        updateRank();
    }

    public Socket getSocket() {

        return socket;
    }

    public void setSocket(Socket socket) {

        this.socket = socket;
    }

    @Override
    public int compareTo(User user) {

        return this.username.compareTo(user.getUsername());
    }

    public boolean comparePass(String pass){

        return this.password.equals(pass);
    }

    public void initCloseSocket(Socket s) throws IOException{

        if(!socket.isClosed() && socket != null)
            socket.close();

        this.socket = s;
    }


    private void updateRank(){
        int j = this.wins - this.losses;

        if(j >= 50) this.rank = 9;
        if(j >= 40) this.rank = 8;
        if(j >= 30) this.rank = 7;
        if(j >= 20) this.rank = 6;
        if(j >= 10) this.rank = 5;
        if(j >= 0) this.rank = 4;
        if(j <= -10) this.rank = 3;
        if(j <= -20) this.rank = 2;
        if(j <= -30) this.rank = 1;
        if(j <= -50) this.rank = 0;

    }


    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(username).append("\n");
        sb.append("Rank: ").append(rank);

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;

        if(obj == null || obj.getClass() != this.getClass())
            return false;

        User user = (User) obj;

        return this.username.equals(user.getUsername())
                && user.comparePass(this.password)
                && user.getRank() == this.rank;
    }

}
