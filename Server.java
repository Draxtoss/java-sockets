
import java.net.*;
import java.util.*;

public class Server {
    public static ArrayList<Student> students=new ArrayList<Student>();
    public static ArrayList<Group>groups=new ArrayList<Group>();

    public static void addStd(Student std){
        students.add(std);
    }
    public static void addGrp(Group grp){
        groups.add(grp);
    }
    public static String listStd(){
        String m="";
        for(Student std:students){
            if(std.isConnected()){
                m += std.getLogin() + ", ";
            }
        }
        return m;
    }
    public static String listGrp(){
        String m="";
        for(Group grp:groups){
            m += grp.getTitle()+", ";
        }
        return m;
    }
    public static Student checkStd(String login){
        for(Student std:students){
            if(std.getLogin().equals(login) && std.isConnected()){
                return std;
            }
        }
        return null;
    }
    public static Student checkStd(InetAddress address, int port){
        for(Student std:students){
            if(std.getAddress().getHostAddress().equals(address.getHostAddress()) && std.getPort()==port && std.isConnected()){
                return std;
            }
        }
        return null;
    }
    public static Group checkGrp(String title){
        for(Group grp:groups){
            if(grp.getTitle().equals(title)){
                return grp;
            }
        }
        return null;
    }
}