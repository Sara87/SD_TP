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
        int i, k;

        for (i = 0 , k = -50; i <= 9 ; i ++) {
            if (k < 0 && j <= k ) {
                this.rank = i;
                break;
            }

            if (k >= 0 && j >= k ) {
                this.rank = i;
                break;
            }

            k += 10;
        }
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
