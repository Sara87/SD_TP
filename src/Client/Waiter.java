package Client;

public class Waiter extends Thread{
    boolean bool = false;

    public void run() {
        try {
            sleep(30000);
            bool = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
