package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reader {

    private BufferedReader in;


    public Reader(Socket socket) {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String read(int comando) throws OrderFailedException {
        String cabecalho, conteudo = null;
        try {
            cabecalho = in.readLine();
            //TODO Isto tem que ser retirado
            System.out.println(">> " + cabecalho + " <<");
            if (cabecalho.equals("EXCEPTION")) {
                conteudo = in.readLine();
                throw new OrderFailedException(conteudo);
            } else {
                switch (comando - 2) {
                    case 1:
                        conteudo = in.readLine();
                        break;
                    case 2:
                        conteudo = in.readLine();
                        break;
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }
        if (conteudo == null) return " ";
        return conteudo;
    }
}
