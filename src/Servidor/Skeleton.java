package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Skeleton extends Thread{
    private Socket userSocket;
    private OverBlind overblind;
    private BufferedReader input;
    private PrintWriter output;
    private User user;

    public Skeleton(Socket s, OverBlind overblind) throws IOException{
        this.userSocket = s;
        this.overblind = overblind;
        this.input = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
        this.output = new PrintWriter(userSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        String op = null;

        while((op = readLine())!= null){
            String resp = translator(op);

            if(!resp.isEmpty())
                output.println(resp);
        }
    }

    //interpreta opção
    public String interpretation(String op){
        try{
            translator(op);
        }
        catch(OptionDeclinedException e){
            return e.getMessage();
        }
    }

    //traduz comandos do cliente
    public String translator(String op){
        return null;
    }
}
