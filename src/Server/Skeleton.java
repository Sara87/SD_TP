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
        String option;
        try {
            while ((option = input.readLine()) != null) {
                String reply = interpreter(option);
                if (!reply.isEmpty()) {
                    //TODO Tirar isto -> só para testar
                    System.out.println("Resposta servidor: " + reply);
                    output.println(reply);
                    output.flush();
                }
            }
        } catch (IOException | InterruptedException e) {
            endConnection();
        }
    }

    private String interpreter(String request) throws InterruptedException{
        try {
            return translator(request);
        } catch (OrderFailedException e) {
            return "EXCEPTION\n" + e.getMessage();
        } catch (ArrayIndexOutOfBoundsException e) {
            return "EXCEPTION\n" + "Os argumentos não foram especificados";
        }
    }

    private String translator(String request) throws ArrayIndexOutOfBoundsException, OrderFailedException, InterruptedException {
        String[] parameters = request.split(" ", 2);

        System.out.println(parameters[0].toUpperCase());

        switch(parameters[0].toUpperCase()) {
            case "REGISTAR":
                checkLogin(false);
                return registar(parameters[1]);
            case "LOGIN":
                checkLogin(false);
                return login(parameters[1]);
            case "WAITING":
                //System.out.println("VAMOS pedir um wait");
                checkLogin(true);
                return startWaiting();
            case "HEROI":
                checkLogin(true);
                return checkHeroe(parameters[1]);

            default:
                throw new OrderFailedException(parameters[0] + " não é um comando válido");
        }
    }

    private void checkLogin(boolean state) throws OrderFailedException {
        if (state == true && user == null)
            throw new OrderFailedException("É necessário iniciar sessão para poder jogar");

        if (state == false && user != null)
            throw new OrderFailedException("Já existe uma sessão iniciada");
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
            user = overblind.login(parameters[0], parameters[1]);
        } catch (ArrayIndexOutOfBoundsException | UserInvalidException e) {
            throw new OrderFailedException(e.getMessage());
        }

        return "OK\n";
    }

    private String startWaiting() throws InterruptedException{
        String h = overblind.startWaiting(user.getUsername());
        return "OK\n" + h;
    }

    private String checkHeroe(String str){
        String[] parametros = str.split(" ");
        int id = Integer.parseInt(parametros[0]);
        int heroe = Integer.parseInt(parametros[1]);
        String user = this.user.getUsername();
        String h = overblind.checkHeroe(user, id, heroe);
        return "OK\n";
    }

    private void endConnection() {
        try {
            userSocket.close();
        } catch (IOException e) {
            System.out.println("Não foi possível fechar o socket");
        }
    }


}
