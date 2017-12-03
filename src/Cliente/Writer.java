package Cliente;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Writer {

    private PrintWriter out;

    public Writer(Socket socket) {
        try {
            out = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write (String input) {
        out.println(input);
        out.flush();
    }
}
