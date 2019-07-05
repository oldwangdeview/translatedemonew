package translatedemo.com.translatedemo.eventbus;

public class TranslateEvent {
    public String data;
    public String requestdata;
    public TranslateEvent(String data){
        this.data = data;
    }
    public TranslateEvent(String data,String request){
        this.data = data;
        this.requestdata = request;
    }
}
