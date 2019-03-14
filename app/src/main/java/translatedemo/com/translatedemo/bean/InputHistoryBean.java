package translatedemo.com.translatedemo.bean;

import java.io.Serializable;

public class InputHistoryBean implements Serializable {

    public String inoutdata;
    public String outputdata;

    public InputHistoryBean(String inputdata,String outputdata){
        this.inoutdata = inputdata;
        this.outputdata = outputdata;
    }
    public InputHistoryBean(){};
}
