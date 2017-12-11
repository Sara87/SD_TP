package Client;


import java.io.IOException;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {

        final int port = 5000;

        Socket usr = new Socket("127.0.0.1", port);
        Reader reader = new Reader(usr);
        Writer writer = new Writer(usr);
        Stub ob = new Stub(writer,reader);
        ob.run();
    }
}
