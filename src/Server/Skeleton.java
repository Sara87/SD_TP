package Server;

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
        String request = null;

        try {
            while ((request = input.readLine()) != null) {
                String response = interpreteRequest(request);
                if (!response.isEmpty()) {
                    //TODO Tirar isto -> só para testar
                    System.out.println("Resposta servidor: " + response);
                    output.println(response);
                    output.flush();

                }
            }
        } catch (IOException e) {
            terminarConexao();
        }
    }

    private String interpreteRequest(String request){
        try {
            return translator(request);
        } catch (OrderFailedException e) {
            return "EXCEPTION\n" + e.getMessage();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "EXCEPTION\n" + "Os argumentos não foram especificados";
        }
    }

    private String translator(String request) throws ArrayIndexOutOfBoundsException, OrderFailedException {
        String[] keywords = request.split(" ", 2);

        switch(keywords[0].toUpperCase()) {
            case "REGISTAR":
                //utilizadorLogado(false);
                return registar(keywords[1]);
            case "LOGIN":
                //utilizadorLogado(false);
                return login(keywords[1]);
            default:
                throw new OrderFailedException(keywords[0] + " não é um comando válido");
        }
    }

    private String registar(String argumentos) throws OrderFailedException {
        String[] parametros = argumentos.split(" ");

        try {
             overblind.register(parametros[0], parametros[1]);

        } catch (ArrayIndexOutOfBoundsException e) {
            throw new OrderFailedException("O username/password não podem ter espaços");
        } catch (UserInvalidException e) {
            throw new OrderFailedException(e.getMessage());
        }
        return "OK";
    }

    private String login(String arguments) throws OrderFailedException {
        String[] parameters = arguments.split(" ");

        try {
            overblind.login(parameters[0], parameters[1]);
        } catch (ArrayIndexOutOfBoundsException | UserInvalidException e) {
            throw new OrderFailedException(e.getMessage());
        }
        return "OK\n";
    }

    private void terminarConexao() {
        try {
            userSocket.close();
        } catch (IOException e) {
            System.out.println("Não foi possível fechar o socket");
        }
    }
}
