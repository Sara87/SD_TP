package Client;

public class OrderFailedException extends Exception {

    public OrderFailedException(String msg) {
        super(msg);
    }
}
