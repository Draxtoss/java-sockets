
import java.net.*;
import java.util.*;

public class Student {
    private String login;
    private boolean connected;
    private InetAddress address;
    private int port;
    private ArrayList<Message>messages=new ArrayList<Message>();

    public Student(String login, InetAddress address, int port) {
        this.login = login;
        this.connected = true;
        this.address = address;
        this.port = port;
    }
    public String historic(){
        String h="";
        for(Message msg:messages){
            h+=msg.getContent()+"\n";
        }
        return h;
    }
    public void addMessage(Message msg){
        this.messages.add(msg);
    }
    public String getLogin() {
        return login;
    }
    public boolean isConnected() {
        return connected;
    }
    public InetAddress getAddress() {
        return address;
    }
    public int getPort() {
        return port;
    }
    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}