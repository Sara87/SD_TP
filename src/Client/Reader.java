package Client;

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
        System.out.println("Comando: " +comando);
        try {
            cabecalho = in.readLine();
            //TODO Isto tem que ser retirado
            //System.out.println(">> " + cabecalho + " <<");
            if (cabecalho.equals("EXCEPTION")) {
                conteudo = in.readLine();
                throw new OrderFailedException(conteudo);
            } else {
                switch (comando) {
                    case 1:
                        conteudo = in.readLine();
                        break;
                    case 2:
                        conteudo = in.readLine();
                        break;
                    case 3:
                        conteudo = readList();
                        break;
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }
        if (conteudo == null) return " ";
        return conteudo;
    }

    private String readList() throws IOException{
        String l;
        StringBuilder sb = new StringBuilder();

       // System.out.println("Estou aqui!");

        while((l = in.readLine()) != null){
            if (l.isEmpty())
                break;
            sb.append(l).append("\n");
        }

      //  System.out.println("TESTE:"+ sb);

        return sb.toString();
    }

    private String readLine() {
        String line = null;

        try {
            line = in.readLine();
        } catch (IOException e) {
            System.out.println("Não foi possível ler novas mensagens");
        }

        return line;
    }
}
