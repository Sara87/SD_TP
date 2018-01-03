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
        System.out.println("Comando reader: " + comando);
        try {
            cabecalho = in.readLine();
            //TODO Isto tem que ser retirado
            if(cabecalho == null) cabecalho = in.readLine();
            System.out.println(">> " + cabecalho + " <<");
            if (cabecalho.equals("EXCEPTION")) {
                conteudo = in.readLine();
                throw new OrderFailedException(conteudo);
            } else {
                switch (comando) {
                    case 1:
                        conteudo = readLine();
                        break;
                    case 2:
                        conteudo = readLine();
                        break;
                    case 3:
                        conteudo = readList();
                        break;
                    case 4:
                        conteudo = readList();
                        break;
                    default:
                        readList();
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

        while((l = readLine()) != null || !l.equals("§")){
            if (l.isEmpty())
                break;
            sb.append(l).append("\n");
        }
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
