package translatedemo.com.translatedemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  "id": 4,
 *                 "content": "སྐུ་ཁམས་བཟང་",
 *                 "type": 1,
 *                 "translateContent": "你好"
 */

public class HistoryBean implements Serializable {

    public List<HistoryListBean> list = new ArrayList<>();

    public static class HistoryListBean implements  Serializable{
        public Integer id;
        public String content;
        public Integer type;
        public String translateContent;
        public String contentOne;
        public String contentTwo;
        public String dictionaryName;
        public String image;
        public Integer dictionaryId;
        public HistoryListBean(String inputdat,String outputdata){
            this.content = inputdat;
            this.translateContent = outputdata;
        }
        public HistoryListBean(){

        }

        public HistoryListBean(String inputdat,String outputdata,Integer dictionaryId,Integer type,String image){
            this.content = inputdat;
            this.translateContent = outputdata;
            this.dictionaryId = dictionaryId;
            this.type = type;
            this.image = image;
        }
    }
}
