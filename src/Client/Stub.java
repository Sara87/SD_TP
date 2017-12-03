package Client;


import java.io.IOException;
import java.util.NoSuchElementException;

public class Stub extends Thread {

    private boolean client;
    private Menu menu;
    private Writer writer;
    private Reader reader;
    private String[] initialMenu;
    private String[] sessionMenu;

    public Stub(Writer w, Reader r) throws IOException {
       this.writer = w;
       this.reader = r;
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
        sessionMenu = new String[3];

        initialMenu[0] = "1) Iniciar Sessão";
        initialMenu[1] = "2) Registar";

        sessionMenu[0] = "1) Começar nova partida";
        sessionMenu[1] = "2) Consultar heróis";
        sessionMenu[2] = "3) Consultar rank";

    }

    private String runCommand(int op) {
        String response ;
        switch (op) {
            case 1:
                login();
                break;

            case 2:
                register();
                break;
            /*
            case 3:
                startGame();
                break;
            case 4:
                listHeroes();
                break;
            case 5:
                rank();
                break;*/
        }

        try {
            response = reader.read(op);
            if (op == 1) {
                this.client = true;
            }
        } catch (OrderFailedException e) {
            response = e.getMessage();
        }
        return response;
    }



    private void login() {
        String username = menu.readString("Username: ");
        String password = menu.readString("Password: ");
        String query = String.join(" ", "LOGIN", username, password);

        writer.write(query);

    }


    private void register() {
        String username = menu.readString("Username: ");
        String password = menu.readString("Password: ");
        String query = String.join(" ", "REGISTAR", username, password);

        writer.write(query);
    }
}