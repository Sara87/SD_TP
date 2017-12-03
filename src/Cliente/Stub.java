package Cliente;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;

public class Stub extends Thread {

    private boolean client;
    private Socket cliSocket;
    private PrintWriter out;
    private Menu menu;
    private String[] initialMenu;
    private String[] sessionMenu;

    public Stub(Socket cliSocket) throws IOException {
        this.cliSocket = cliSocket;

        out = new PrintWriter(cliSocket.getOutputStream(), true);
        menu = new Menu(initialMenu);
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
                option = menu.showMenu(initialMenu);
            else {
                option = menu.showMenu(sessionMenu);
            }
        } catch (NoSuchElementException e) {
            return -1;
        }

        return option;
    }

    private void setUpMenus() {
        initialMenu = new String[2];
        sessionMenu = new String[5];

        initialMenu[0] = "1) Sign up";
        initialMenu[1] = "2) Sign in";

        sessionMenu[0] = "1) Começar nova partida";
        sessionMenu[1] = "2) Consultar heróis";
        sessionMenu[2] = "3) Consultar rank";

    }

    private String runCommand(int option) {
        String response = "";
        switch (option) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            case 3:
                startGame();
                break;
            case 4:
                listHeroes();
                break;
            case 5:
                rank();
                break;
        }
        response = reader.ler(option);
        if (option == 1) {
            this.client = true;
        }

        return response;
    }



    private void login() {
        String username = menu.readString("Username: ");
        String password = menu.readString("Password: ");
        String query = String.join(" ", "LOGIN", username, password);

        out.println(query);
    }


    private String register() {
        String username = menu.readString("Username: ");
        String password = menu.readString("Password: ");
        String query = String.join(" ", "REGISTAR", username, password);

        out.println(query);

        return query;
    }
}