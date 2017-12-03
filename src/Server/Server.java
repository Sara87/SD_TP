package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int port = 5000;

    public static void main(String [] args) throws IOException{
        ServerSocket server = new ServerSocket(port);
        OverBlind overBlind =  new OverBlind();

        while(true){
            Socket c = server.accept();
            Skeleton sk = new Skeleton(c, overBlind);
            sk.start();
        }
    }

}
