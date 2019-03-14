package translatedemo.com.translatedemo.eventbus;

import translatedemo.com.translatedemo.bean.LanguageBean;

public class LanguageEvent {
    public LanguageBean data;
    public LanguageEvent(LanguageBean data){
        this.data = data;
    }
}
