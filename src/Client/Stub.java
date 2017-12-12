package Client;


import java.io.IOException;
import java.util.NoSuchElementException;

public class Stub extends Thread {

    private boolean client;
    private Writer writer;
    private Reader reader;
    private Menu initialMenu, sessionMenu;

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
                option = sessionMenu.showMenu()+22;
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


    private String runCommand(int op) {
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
        String username = initialMenu.readString("Username: ");
        String password = initialMenu.readString("Password: ");
        String query = String.join(" ", "LOGIN", username, password);

        writer.write(query);

    }


    private void register() {
        String username = initialMenu.readString("Username: ");
        String password = initialMenu.readString("Password: ");
        String query = String.join(" ", "REGISTAR", username, password);

        writer.write(query);
    }

    private void startWaiting() {
        String query = "WAITING";

        writer.write(query);
    }

}