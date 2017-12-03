package Client;

import java.util.Scanner;

public class Menu {
    private Scanner in;
    private String[] options;


    public Menu(String[] entries) {
        in = new Scanner(System.in);
        in.useDelimiter("[\r\n]");
    }


    public int showMenu(String[] entries) {
        int option = 0;

        String menu = String.join("\n", entries);
        System.out.println(menu + "\n");

        while(option <= 0 || option > entries.length) {
            option = readInt("Escolha uma das opções: ");
            if (option <= 0 || option > entries.length)
                System.out.println("\n> Opção inválida\n");
        }
        return option;
    }


    public String readString (String msg){
        System.out.println(msg);
        return in.next();
    }


    public int readInt(String msg) {
        int num;

        try {
            System.out.print(msg);
            num = Integer.parseInt(in.next());
        } catch (NumberFormatException e) {
            System.out.println("\n> A opção escolhida não é válido\n");
            num = readInt(msg);
        }

        return num;
    }

    public float readFloat(String msg) {
        float num;

        try {
            System.out.print(msg);
            num = Float.parseFloat(in.next());
        } catch (NumberFormatException e) {
            System.out.println("\n> A opção escolhida não é válido\n");
            num = readFloat(msg);
        }
        return num;
    }
}