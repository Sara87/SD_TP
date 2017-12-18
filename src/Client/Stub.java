package Client;


import java.io.IOException;
import java.util.NoSuchElementException;

public class Stub extends Thread {

    private boolean client;
    private Writer writer;
    private Reader reader;
    private Menu initialMenu, sessionMenu, heroes;

    public Stub(Writer w, Reader r) throws IOException {
       this.writer = w;
       this.reader = r;

       setUpMenus();
    }


    public void run() {
        int option;

        while ((option = showMenu()) != -1) {
            runCommand(option);
        }

        System.out.println("\nLigação terminada!");
        System.exit(0);
    }


    private int showMenu() {
        int option = 0;

        try {
            if (!client)
                option = initialMenu.showMenu();
            else {
                option = sessionMenu.showMenu()+2;
            }
        } catch (NoSuchElementException e) {
            return -1;
        }

        return option;
    }

    private void setUpMenus() {

        String [] initialMenu = { "Iniciar Sessão",
                          "Registar"
            };

        this.initialMenu = new Menu(initialMenu);

        String [] sessionMenu = {"Começar nova partida",
                "Consultar heróis",
                "Consultar rank"
        };

        this.sessionMenu = new Menu(sessionMenu);
    }


    private void runCommand(int op) {
        String response ;
        switch (op) {
            case 1:
                login();
                break;

            case 2:
                register();
                break;
            case 3:
                startWaiting();
                break;
                /*
            case 4:
                listHeroes();
                break;
            case 5:
                rank();
                break;*/
        }


        return;
    }



    private void login() {
        String username = initialMenu.readString("Username: ");
        String password = initialMenu.readString("Password: ");
        String query = String.join(" ", "LOGIN", username, password);

        writer.write(query);

        String response;
        try {
            response = reader.read(1);
        } catch (OrderFailedException e) {
            response = e.getMessage();
        }
        System.out.println(response);
    }


    private void register() {
        String username = initialMenu.readString("Username: ");
        String password = initialMenu.readString("Password: ");
        String query = String.join(" ", "REGISTAR", username, password);

        writer.write(query);

        String response;
        try {
            response = reader.read(2);
        } catch (OrderFailedException e) {
            response = e.getMessage();
        }

        System.out.println(response);
    }

    private void startWaiting() {
        String query = "WAITING";

        writer.write(query);

        String response;

        try {
            response = reader.read(3);
            String [] st = response.split("\n");
            heroes = new Menu(st);
            // ler a opção do heroi escolhida
            // mandar ao servidor
            // servidor reenvia lista ou devolve que heroi escolhido pode ou nao ser escolhido
            //ver a cena dos 30segundos

        } catch (OrderFailedException e) {
            response = e.getMessage();
            System.out.println(response);
        }
    }

}