
public class Message {
    private Student stdSender;
    private Student stdReceiver;
    private String content;

    public Message(Student stdSender, Student stdReceiver, String content) {
        this.stdSender = stdSender;
        this.stdReceiver = stdReceiver;
        this.content=this.stdSender.getLogin()+": "+content;
    }
    public String getContent() {
        return content;
    }
    public Student getStdSender() {
        return stdSender;
    }
    public Student getStdReceiver() {
        return stdReceiver;
    }
}