
import java.io.*;
import java.net.*;

public class ProcessSvr {
    public static void sendMsg(DatagramSocket sc, String msgSend, InetAddress address, int port) throws IOException{
        DatagramPacket pkSend = new DatagramPacket(msgSend.getBytes(), msgSend.length(), address, port);
        sc.send(pkSend);
    }
    public static void main(String[] args) throws IOException {
        DatagramSocket sc=new DatagramSocket(3000);
        while (true){
            try {
                DatagramPacket pkRcv=new DatagramPacket(new byte[1024], 1024);
                sc.receive(pkRcv);
                String msgSend, msgRcv=new String(pkRcv.getData(), 0, pkRcv.getLength());

                if(msgRcv.startsWith("##") && msgRcv.length()>2) {
                    Student std=Server.checkStd(pkRcv.getAddress(), pkRcv.getPort());
                    if(std==null){
                        Student s=Server.checkStd(msgRcv.substring(2));
                        if(s==null){
                            Server.students.add(new Student(msgRcv.substring(2), pkRcv.getAddress(), pkRcv.getPort()));
                            msgSend="Successfully signed in as "+msgRcv.substring(2);
                            sendMsg(sc, msgSend, pkRcv.getAddress(), pkRcv.getPort());
                        }else {
                            msgSend="Login "+msgRcv.substring(2)+" already in use";
                            sendMsg(sc, msgSend, pkRcv.getAddress(), pkRcv.getPort());
                        }
                    }else{
                        msgSend="Already logged in as "+std.getLogin();
                        sendMsg(sc, msgSend, std.getAddress(), std.getPort());
                    }
                }
                else {
                    Student stdSend=Server.checkStd(pkRcv.getAddress(), pkRcv.getPort());
                    if(stdSend==null){
                        msgSend="Log in first";
                        sendMsg(sc, msgSend, pkRcv.getAddress(), pkRcv.getPort());
                    }else {
                        if(msgRcv.equals("#LIST")) {
                            msgSend=Server.listStd();
                            sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                        }
                        else if(msgRcv.equals("#HISTO")) {
                            msgSend=stdSend.historic();
                            sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                        }
                        else if (msgRcv.startsWith("@#") && msgRcv.split("@#").length==3){
                            String[] t=msgRcv.split("@#");
                            Student stdRcv=Server.checkStd(t[1]);
                            if(stdRcv==null) {
                                msgSend="Student "+t[1]+" not found";
                                sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                            }else {
                                Message message=new Message(stdSend, stdRcv, t[2]);
                                msgSend=message.getContent();
                                sendMsg(sc, msgSend, stdRcv.getAddress(), stdRcv.getPort());
                                stdRcv.addMessage(message);
                                msgSend="Message sent";
                                sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                            }
                        }
                        else if(msgRcv.equals("#GRPS")){
                            msgSend=Server.listGrp();
                            sendMsg(sc, msgSend, pkRcv.getAddress(), pkRcv.getPort());
                        }
                        else if(msgRcv.startsWith("#GRP#") && msgRcv.length()>5){
                            Group grp=Server.checkGrp(msgRcv.substring(5));
                            if(grp==null){
                                grp = new Group(msgRcv.substring(5));
                                Server.addGrp(grp);
                                grp.addStd(stdSend);
                                msgSend="Group "+msgRcv.substring(5)+" successfully created";
                                sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                            }else{
                                msgSend="Title "+msgRcv.substring(5)+" already in use";
                                sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                            }
                        }
                        else if(msgRcv.startsWith("#>")){
                            Group grp=Server.checkGrp(msgRcv.substring(2));
                            if(grp==null){
                                msgSend="Group "+msgRcv.substring(2)+" not found";
                                sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                            }else{
                                if(grp.checkStd(stdSend)) {
                                    msgSend = "Already in group " + msgRcv.substring(2);
                                    sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                                }else {
                                    grp.addStd(stdSend);
                                    msgSend = "Successfully joined group " + msgRcv.substring(2);
                                    sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                                }
                            }
                        }
                        else if(msgRcv.startsWith("#ETDS#") && msgRcv.length()>6){
                            Group grp=Server.checkGrp(msgRcv.substring(6));
                            if(grp==null){
                                msgSend="Group "+msgRcv.substring(6)+" not found";
                                sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                            }else {
                                msgSend=grp.listStd();
                                sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                            }
                        }
                        else if(msgRcv.startsWith("@>") && msgRcv.split("@>").length==3){
                            String[] t=msgRcv.split("@>");
                            Group grp=Server.checkGrp(t[1]);
                            if(grp==null){
                                msgSend="Group "+t[1]+" not found";
                                sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                            }else {
                                if(grp.checkStd(stdSend)){
                                    for(Student stdRcv: grp.getStudents()){
                                        if(stdRcv.isConnected() && stdRcv!=stdSend) {
                                            Message message=new Message(stdSend, stdRcv, t[2]);
                                            msgSend=message.getContent();
                                            sendMsg(sc, msgSend, stdRcv.getAddress(), stdRcv.getPort());
                                            stdRcv.addMessage(message);
                                        }
                                    }
                                    msgSend="Messages sent";
                                    sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                                }
                                else{
                                    msgSend="Join "+grp.getTitle()+" before sending a message";
                                    sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                                }
                            }
                        }
                        else if(msgRcv.equals("#LOGOUT")) {
                            stdSend.setConnected(false);
                            msgSend = "Successfully logged out";
                            sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                        }
                        else {
                            msgSend = "Wrong syntax";
                            sendMsg(sc, msgSend, stdSend.getAddress(), stdSend.getPort());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}