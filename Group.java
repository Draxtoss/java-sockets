
import java.util.*;

public class Group {
    private String title;
    private ArrayList<Student> students=new ArrayList<Student>();

    public Group(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void addStd(Student std){
        this.students.add(std);
    }
    public String listStd(){
        String m="";
        for(Student std:students){
            if(std.isConnected()){
                m += std.getLogin() + "|";
            }
        }
        if(m.length()==0){
            return m;
        }
        return m.substring(0, m.length()-1);
    }
    public boolean checkStd(Student s){
        for(Student std: students){
            if(std==s){
                return true;
            }
        }
        return false;
    }
    public List<Student> getStudents() {
        return students;
    }
}