package translatedemo.com.translatedemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  "name":"happy",
 *         "sentenceDTOList":[
 *             {
 *                 "definition":"feeling or showing pleasure or contentment",
 *                 "examples":[
 *                     "they are happy to see me doing well",
 *                     "Melissa came in looking happy and excited",
 *                     "we're just happy that he's still alive"
 *                 ]
 *             },
 *             {
 *                 "definition":"fortunate and convenient",
 *                 "examples":[
 *                     "he had the happy knack of making people like him"
 *                 ]
 *             },
 *             {
 *                 "definition":"inclined to use a specified thing excessively or at random",
 *                 "examples":[
 *                     "they tended to be grenade-happy"
 *                 ]
 *             }
 *         ]
 */

public class ThreeTranslateBean implements Serializable {


    public String name;
    public String from;
    public String date;
    public String adjective;
    public List<SentenceDTOListBean> sentenceDTOList = new ArrayList<>();


}
