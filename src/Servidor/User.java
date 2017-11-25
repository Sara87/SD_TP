package Servidor;

import java.net.Socket;

public class User implements Comparable<User>{
    private String username;
    private String password;
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

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;

        if(obj == null || obj.getClass() != this.getClass())
            return false;

        User user = (User) obj;

        return this.username.equals(user.getUsername())
                && user.comparePass(this.password);
    }


}
