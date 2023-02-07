
import java.io.*;
import java.net.*;

public class StdReceive extends Thread{
    DatagramSocket sc;

    public StdReceive(DatagramSocket sc) {
        this.sc = sc;
    }
    public void run(){
        while(true){
            DatagramPacket pk=new DatagramPacket(new byte[1024], 1024);
            try {
                sc.receive(pk);
                String msg =new String(pk.getData(), 0, pk.getLength());
                System.out.println(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}