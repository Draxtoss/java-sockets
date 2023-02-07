
import java.io.*;
import java.net.*;

public class ProcessStd {
    public static void main(String[] args) throws IOException {
        DatagramSocket sc=new DatagramSocket();

        StdSend ss=new StdSend(sc);
        ss.start();

        StdReceive sr=new StdReceive(sc);
        sr.start();
    }
}