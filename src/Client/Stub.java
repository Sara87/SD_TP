package Client;


import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

public class Stub extends Waiter {
    private int mm;
    private boolean client;
    private String str = "";
    private Writer writer;
    private Reader reader;
    private Menu initialMenu, sessionMenu, heroes;

    TimerTask ttask = new TimerTask() {
        public void run() {
            if(str.equals(""))
                System.out.println( "\nyou input nothing. exit..." );
            verify(mm);
        }
    };

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
            else
                option = sessionMenu.showMenu() + 2;
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
            switch (op) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    mm = startWaiting();
                    Timer timer = new Timer();
                    timer.schedule(ttask, 30*1000 );
                    choosingHeroe(mm);
                    break;
            }
    }


    private void login() {
        String username = initialMenu.readString("Username: ");
        String password = initialMenu.readString("Password: ");
        String query = String.join(" ", "LOGIN", username, password);

        writer.write(query);

        String response;
        try {
            response = reader.read(1);
            client = true;
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

    private int startWaiting() {
        String query = "WAITING";
        String [] st = null , st2;
        String response;
        writer.write(query);
        try {
            response = reader.read(3);
            st = response.split("\n", 2);
            st2 = st[1].split("\n");
            this.heroes = new Menu(st2);
        } catch (OrderFailedException e) {
            response = e.getMessage();
            System.out.println(response);
        }
        return Integer.parseInt(st[0]);
    }

    private void choosingHeroe(int id) {
        String query;
        int op = heroes.showMenu();
        try {
            query = String.join(" ", "HEROI", Integer.toString(id), Integer.toString(op));
            writer.write(query);
            str = reader.read(op + 4);
            System.out.println(str);
        } catch (OrderFailedException e) {
            e.printStackTrace();
        }
    }

    public void verify (int id) {
        String query;
        query = String.join(" ","VERIFY", Integer.toString(id));
        writer.write(query);
        //TODO Ver como fazer aqui
        try {
            mm = -1;
            String res = reader.read(4);
            System.out.println(res);
        } catch (OrderFailedException e) {
            e.printStackTrace();
        }

    }

}